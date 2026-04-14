from __future__ import annotations

from datetime import datetime, timedelta, timezone
from fastapi import HTTPException
from types import SimpleNamespace

from sqlalchemy.orm import Session

from app.anti_bot import SimpleRateLimiter
from app.database import Base, build_engine, build_session_factory
from app.routes.study_routes import NextCardPayload, ReviewPayload, _select_next_card, study_next, study_review
from app.models import Card, Deck, User, UserCardState
from app.scheduler import FSRSService


def _new_session() -> Session:
    engine = build_engine("sqlite+pysqlite:///:memory:")
    Base.metadata.create_all(bind=engine)
    session_factory = build_session_factory(engine)
    return session_factory()


def _mk_card(deck_id: int, uid: str, position: int) -> Card:
    return Card(
        card_uid=uid,
        deck_id=deck_id,
        source_file="tests.md",
        question_md=f"Q {uid}",
        answer_md=f"A {uid}",
        content_hash=f"hash-{uid}",
        position=position,
        is_active=True,
    )


def _mk_review_request() -> SimpleNamespace:
    fsrs = FSRSService(desired_retention=0.9)
    state = SimpleNamespace(
        fsrs=fsrs,
        rate_limiter=SimpleRateLimiter(),
        settings=SimpleNamespace(review_rate_limit_per_minute=1000),
    )
    return SimpleNamespace(
        headers={},
        client=SimpleNamespace(host="127.0.0.1"),
        app=SimpleNamespace(state=state),
    )


def test_new_cards_follow_deck_then_position_order() -> None:
    db = _new_session()
    now = datetime.now(timezone.utc)

    user = User(username="learner", password_hash="x", is_active=True)
    db.add(user)
    db.flush()

    deck1 = Deck(deck_uid="d1", title="Deck 1", sort_order=1, card_count=2)
    deck2 = Deck(deck_uid="d2", title="Deck 2", sort_order=2, card_count=2)
    db.add_all([deck1, deck2])
    db.flush()

    c11 = _mk_card(deck1.id, "d1-c1", 1)
    c12 = _mk_card(deck1.id, "d1-c2", 2)
    c21 = _mk_card(deck2.id, "d2-c1", 1)
    c22 = _mk_card(deck2.id, "d2-c2", 2)
    db.add_all([c11, c12, c21, c22])
    db.flush()

    # First deck already seen by this user, so next new card should be first card of deck2.
    db.add_all(
        [
            UserCardState(user_id=user.id, card_id=c11.id, due_at=now + timedelta(days=1)),
            UserCardState(user_id=user.id, card_id=c12.id, due_at=now + timedelta(days=1)),
        ]
    )
    db.commit()

    hit = _select_next_card(db=db, user_id=user.id, deck_uid="all", now=now)
    assert hit is not None
    card, deck, state = hit
    assert state is None
    assert deck.deck_uid == "d2"
    assert card.card_uid == "d2-c1"


def test_due_cards_are_shuffled_for_review() -> None:
    db = _new_session()
    now = datetime.now(timezone.utc)

    user = User(username="reviewer", password_hash="x", is_active=True)
    db.add(user)
    db.flush()

    deck = Deck(deck_uid="d", title="Deck", sort_order=1, card_count=3)
    db.add(deck)
    db.flush()

    cards = [_mk_card(deck.id, f"d-c{i}", i) for i in [1, 2, 3]]
    db.add_all(cards)
    db.flush()

    for card in cards:
        db.add(UserCardState(user_id=user.id, card_id=card.id, due_at=now - timedelta(minutes=1)))
    db.commit()

    seen = set()
    for _ in range(20):
        hit = _select_next_card(db=db, user_id=user.id, deck_uid="all", now=now)
        assert hit is not None
        card, _, state = hit
        assert state is not None
        seen.add(card.card_uid)
        if len(seen) > 1:
            break

    assert len(seen) > 1


def test_due_cards_take_priority_over_new_cards() -> None:
    db = _new_session()
    now = datetime.now(timezone.utc)

    user = User(username="priority-user", password_hash="x", is_active=True)
    db.add(user)
    db.flush()

    deck = Deck(deck_uid="d", title="Deck", sort_order=1, card_count=2)
    db.add(deck)
    db.flush()

    due_card = _mk_card(deck.id, "d-due", 1)
    new_card = _mk_card(deck.id, "d-new", 2)
    db.add_all([due_card, new_card])
    db.flush()

    db.add(UserCardState(user_id=user.id, card_id=due_card.id, due_at=now - timedelta(minutes=1)))
    db.commit()

    hit = _select_next_card(db=db, user_id=user.id, deck_uid="all", now=now)
    assert hit is not None
    card, _, state = hit
    assert state is not None
    assert card.card_uid == "d-due"


def test_review_next_card_respects_selected_deck_scope() -> None:
    db = _new_session()

    user = User(username="scoped-reviewer", password_hash="x", is_active=True)
    db.add(user)
    db.flush()

    deck1 = Deck(deck_uid="d1", title="Deck 1", sort_order=1, card_count=1)
    deck2 = Deck(deck_uid="d2", title="Deck 2", sort_order=2, card_count=1)
    db.add_all([deck1, deck2])
    db.flush()

    card1 = _mk_card(deck1.id, "d1-c1", 1)
    card2 = _mk_card(deck2.id, "d2-c1", 1)
    db.add_all([card1, card2])
    db.commit()

    request = _mk_review_request()

    result = study_review(
        payload=ReviewPayload(card_uid=card1.card_uid, rating=4, deck_uid="d1"),
        request=request,
        user=user,
        db=db,
    )

    assert result["ok"] is True
    assert result["next"]["empty"] is True
    assert result["next"]["card"] is None


def test_study_next_rejects_blank_deck_uid() -> None:
    db = _new_session()
    user = User(username="blank-deck-user", password_hash="x", is_active=True)
    db.add(user)
    db.commit()

    try:
        study_next(payload=NextCardPayload(deck_uid="   "), user=user, db=db)
    except HTTPException as exc:
        assert exc.status_code == 400
        assert exc.detail == "deck_uid_invalido"
    else:
        raise AssertionError("study_next deveria rejeitar deck_uid em branco")


def test_review_rejects_card_outside_selected_deck() -> None:
    db = _new_session()

    user = User(username="wrong-scope-user", password_hash="x", is_active=True)
    db.add(user)
    db.flush()

    deck1 = Deck(deck_uid="d1", title="Deck 1", sort_order=1, card_count=1)
    deck2 = Deck(deck_uid="d2", title="Deck 2", sort_order=2, card_count=1)
    db.add_all([deck1, deck2])
    db.flush()

    card1 = _mk_card(deck1.id, "d1-c1", 1)
    db.add(card1)
    db.commit()

    request = _mk_review_request()

    try:
        study_review(
            payload=ReviewPayload(card_uid=card1.card_uid, rating=4, deck_uid="d2"),
            request=request,
            user=user,
            db=db,
        )
    except HTTPException as exc:
        assert exc.status_code == 400
        assert exc.detail == "card_fora_do_deck"
    else:
        raise AssertionError("study_review deveria rejeitar card fora do deck selecionado")
