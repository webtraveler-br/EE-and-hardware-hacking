from __future__ import annotations

from datetime import datetime, timedelta, timezone

from sqlalchemy.orm import Session

from app.database import Base, build_engine, build_session_factory
from app.routes.study_routes import _select_next_card
from app.models import Card, Deck, User, UserCardState


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
