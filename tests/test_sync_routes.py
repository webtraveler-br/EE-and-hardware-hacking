from __future__ import annotations

from datetime import datetime, timezone
from types import SimpleNamespace

from sqlalchemy import func, select
from sqlalchemy.orm import Session

from app.database import Base, build_engine, build_session_factory
from app.models import Card, Deck, ReviewLog, SyncEvent, User
from app.routes.study_routes import SyncEventPayload, SyncImportPayload, sync_export, sync_import
from app.scheduler import FSRSService


def _new_session() -> Session:
    engine = build_engine("sqlite+pysqlite:///:memory:")
    Base.metadata.create_all(bind=engine)
    session_factory = build_session_factory(engine)
    return session_factory()


def _mk_request() -> SimpleNamespace:
    fsrs = FSRSService(desired_retention=0.9)
    return SimpleNamespace(app=SimpleNamespace(state=SimpleNamespace(fsrs=fsrs)))


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

    exported = sync_export(deck_uid="all", user=user, db=db)
    cards = exported["cards"]
    assert len(cards) == 1

    state = cards[0]["state"]
    assert state["is_new"] is False
    assert state["reps"] == 1
    assert state["due_at"] is not None

