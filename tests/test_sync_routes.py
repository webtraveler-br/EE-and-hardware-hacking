from __future__ import annotations

from datetime import datetime, timedelta, timezone
from fastapi import HTTPException
from types import SimpleNamespace

from sqlalchemy import func, select
from sqlalchemy.orm import Session

from app.database import Base, build_engine, build_session_factory
from app.models import Card, Deck, ReviewLog, SyncEvent, User, UserCardState
from app.routes.study_routes import (
    SyncEventPayload,
    SyncExportDeltaPayload,
    SyncImportPayload,
    SyncLocalCardPayload,
    list_decks,
    sync_export,
    sync_export_delta,
    sync_import,
)
from app.anti_bot import SimpleRateLimiter
from app.scheduler import FSRSService


def _new_session() -> Session:
    engine = build_engine("sqlite+pysqlite:///:memory:")
    Base.metadata.create_all(bind=engine)
    session_factory = build_session_factory(engine)
    return session_factory()


def _mk_request() -> SimpleNamespace:
    fsrs = FSRSService(desired_retention=0.9)
    settings = SimpleNamespace(
        sync_rate_limit_per_minute=1000,
        review_rate_limit_per_minute=1000,
    )
    return SimpleNamespace(
        app=SimpleNamespace(
            state=SimpleNamespace(
                fsrs=fsrs,
                settings=settings,
                rate_limiter=SimpleRateLimiter(),
            )
        ),
        headers={},
        client=SimpleNamespace(host="127.0.0.1"),
    )


def _seed_user_and_card(db: Session) -> tuple[User, Card]:
    user = User(username="offline_user", password_hash="x", is_active=True)
    db.add(user)
    db.flush()

    deck = Deck(deck_uid="deck-sync", title="Deck Sync", sort_order=1, card_count=1)
    db.add(deck)
    db.flush()

    card = Card(
        card_uid="deck-sync-card-1",
        deck_id=deck.id,
        source_file="tests.md",
        question_md="Q",
        answer_md="A",
        content_hash="hash-sync",
        position=1,
        is_active=True,
    )
    db.add(card)
    db.commit()

    return user, card


def test_sync_import_is_idempotent_by_event_id() -> None:
    db = _new_session()
    user, card = _seed_user_and_card(db)
    request = _mk_request()

    reviewed_at = datetime.now(timezone.utc).isoformat()
    payload = SyncImportPayload(
        events=[
            SyncEventPayload(
                event_id="evt-sync-0001",
                card_uid=card.card_uid,
                rating=3,
                duration_ms=12000,
                reviewed_at=reviewed_at,
            )
        ]
    )

    first = sync_import(payload=payload, request=request, user=user, db=db)
    assert first["accepted"] == 1
    assert first["duplicates"] == 0
    assert first["errors"] == []

    second = sync_import(payload=payload, request=request, user=user, db=db)
    assert second["accepted"] == 0
    assert second["duplicates"] == 1
    assert second["errors"] == []

    assert db.execute(select(func.count(SyncEvent.id))).scalar_one() == 1
    assert db.execute(select(func.count(ReviewLog.id))).scalar_one() == 1


def test_sync_import_reuses_state_for_multiple_events_of_same_card() -> None:
    db = _new_session()
    user, card = _seed_user_and_card(db)
    request = _mk_request()

    reviewed_at = datetime.now(timezone.utc)
    payload = SyncImportPayload(
        events=[
            SyncEventPayload(
                event_id="evt-sync-1001",
                card_uid=card.card_uid,
                rating=3,
                duration_ms=12000,
                reviewed_at=reviewed_at.isoformat(),
            ),
            SyncEventPayload(
                event_id="evt-sync-1002",
                card_uid=card.card_uid,
                rating=4,
                duration_ms=8000,
                reviewed_at=(reviewed_at + timedelta(minutes=1)).isoformat(),
            ),
        ]
    )

    result = sync_import(payload=payload, request=request, user=user, db=db)
    assert result["accepted"] == 2
    assert result["duplicates"] == 0
    assert result["errors"] == []

    state_rows = db.execute(select(UserCardState)).scalars().all()
    assert len(state_rows) == 1
    assert state_rows[0].user_id == user.id
    assert state_rows[0].card_id == card.id
    assert state_rows[0].reps == 2
    assert db.execute(select(func.count(SyncEvent.id))).scalar_one() == 2
    assert db.execute(select(func.count(ReviewLog.id))).scalar_one() == 2


def test_sync_export_returns_state_after_import() -> None:
    db = _new_session()
    user, card = _seed_user_and_card(db)
    request = _mk_request()

    payload = SyncImportPayload(
        events=[
            SyncEventPayload(
                event_id="evt-sync-0002",
                card_uid=card.card_uid,
                rating=2,
                duration_ms=9000,
                reviewed_at=datetime.now(timezone.utc).isoformat(),
            )
        ]
    )
    result = sync_import(payload=payload, request=request, user=user, db=db)
    assert result["accepted"] == 1

    exported = sync_export(deck_uid="all", request=request, user=user, db=db)
    cards = exported["cards"]
    assert len(cards) == 1
    assert exported["scheduler_profile"]["desired_retention"] == 0.9
    assert exported["scheduler_profile"]["learning_steps_seconds"] == [60, 600]
    assert exported["scheduler_profile"]["relearning_steps_seconds"] == [600]
    assert exported["scheduler_profile"]["enable_fuzzing"] is False

    state = cards[0]["state"]
    assert state["is_new"] is False
    assert state["reps"] == 1
    assert state["due_at"] is not None
    assert state["fsrs_stability"] is not None
    assert state["fsrs_difficulty"] is not None
    assert state["last_review_at"] is not None
    assert exported["decks"][0]["learning_count"] >= 0


def test_list_decks_includes_learning_count() -> None:
    db = _new_session()
    user, card = _seed_user_and_card(db)

    state = UserCardState(
        user_id=user.id,
        card_id=card.id,
        reps=1,
        lapses=0,
        fsrs_state=1,
        due_at=datetime.now(timezone.utc),
    )
    db.add(state)
    db.commit()

    payload = list_decks(user=user, db=db)
    assert payload["decks"][0]["learning_count"] == 1


def test_sync_export_delta_returns_only_changed_cards_and_removed_ids() -> None:
    db = _new_session()
    user, card = _seed_user_and_card(db)

    state = UserCardState(
        user_id=user.id,
        card_id=card.id,
        reps=2,
        lapses=1,
        due_at=datetime(2026, 4, 12, tzinfo=timezone.utc),
    )
    db.add(state)

    removed_card = Card(
        card_uid="deck-sync-card-removed",
        deck_id=card.deck_id,
        source_file="tests.md",
        question_md="Q old",
        answer_md="A old",
        content_hash="hash-old",
        position=2,
        is_active=False,
    )
    db.add(removed_card)
    db.commit()

    payload = SyncExportDeltaPayload(
        deck_uid="all",
        cards=[
            SyncLocalCardPayload(
                card_uid=card.card_uid,
                content_hash=card.content_hash,
                deck_uid="deck-sync",
                position=1,
                due_at=state.due_at.isoformat(),
                reps=2,
                lapses=1,
                is_new=False,
            ),
            SyncLocalCardPayload(
                card_uid="deck-sync-card-removed",
                content_hash="hash-old",
                deck_uid="deck-sync",
                position=2,
                due_at=None,
                reps=0,
                lapses=0,
                is_new=True,
            ),
        ],
    )

    exported = sync_export_delta(payload=payload, request=_mk_request(), user=user, db=db)
    assert exported["upserts"] == []
    assert exported["removed_card_uids"] == ["deck-sync-card-removed"]
    assert exported["unchanged_count"] == 1


def test_sync_export_delta_detects_content_and_state_changes() -> None:
    db = _new_session()
    user, card = _seed_user_and_card(db)

    state = UserCardState(
        user_id=user.id,
        card_id=card.id,
        reps=3,
        lapses=1,
        due_at=datetime(2026, 4, 13, tzinfo=timezone.utc),
    )
    db.add(state)
    db.commit()

    payload = SyncExportDeltaPayload(
        deck_uid="all",
        cards=[
            SyncLocalCardPayload(
                card_uid=card.card_uid,
                content_hash="old-hash",
                deck_uid="deck-sync",
                position=1,
                due_at=datetime(2026, 4, 10, tzinfo=timezone.utc).isoformat(),
                reps=1,
                lapses=0,
                is_new=False,
            )
        ],
    )

    exported = sync_export_delta(payload=payload, request=_mk_request(), user=user, db=db)
    assert len(exported["upserts"]) == 1
    upsert = exported["upserts"][0]
    assert upsert["card_uid"] == card.card_uid
    assert upsert["content_hash"] == card.content_hash
    assert upsert["state"]["reps"] == 3
    assert upsert["state"]["lapses"] == 1
    assert upsert["state"]["due_at"] == state.due_at.isoformat()
    assert exported["removed_card_uids"] == []
    assert exported["unchanged_count"] == 0


def test_sync_export_rejects_unknown_deck_scope() -> None:
    db = _new_session()
    user, _ = _seed_user_and_card(db)
    request = _mk_request()

    try:
        sync_export(deck_uid="deck-inexistente", request=request, user=user, db=db)
    except HTTPException as exc:
        assert exc.status_code == 404
        assert exc.detail == "deck_nao_encontrado"
    else:
        raise AssertionError("sync_export deveria rejeitar deck inexistente")

