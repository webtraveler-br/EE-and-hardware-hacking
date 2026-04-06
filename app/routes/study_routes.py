from __future__ import annotations

from datetime import date, datetime, timedelta, timezone

from fastapi import APIRouter, Depends, HTTPException, Request, status
from pydantic import BaseModel, Field
from sqlalchemy import and_, case, func, select
from sqlalchemy.orm import Session

from ..anti_bot import SimpleRateLimiter
from ..auth import get_db, require_user
from ..models import Card, Deck, ReviewLog, SyncEvent, User, UserCardState, utcnow
from ..scheduler import FSRSService

router = APIRouter(prefix="/api", tags=["study"])


class NextCardPayload(BaseModel):
    deck_uid: str = "all"


class ReviewPayload(BaseModel):
    card_uid: str = Field(min_length=3, max_length=140)
    rating: int = Field(ge=1, le=4)
    duration_ms: int | None = Field(default=None, ge=0, le=600000)


class SyncEventPayload(BaseModel):
    event_id: str = Field(min_length=8, max_length=120)
    card_uid: str = Field(min_length=3, max_length=140)
    rating: int = Field(ge=1, le=4)
    duration_ms: int | None = Field(default=None, ge=0, le=600000)
    reviewed_at: str = Field(min_length=10, max_length=64)


class SyncImportPayload(BaseModel):
    events: list[SyncEventPayload] = Field(default_factory=list)


def _as_aware(dt: datetime | None, fallback: datetime) -> datetime:
    if dt is None:
        return fallback
    if dt.tzinfo is None:
        return dt.replace(tzinfo=timezone.utc)
    return dt


def _parse_iso_datetime(raw: str, *, fallback: datetime) -> datetime:
    value = raw.strip()
    if value.endswith("Z"):
        value = f"{value[:-1]}+00:00"

    try:
        parsed = datetime.fromisoformat(value)
    except ValueError as exc:
        raise HTTPException(status_code=400, detail=f"reviewed_at_invalido:{raw}") from exc

    return _as_aware(parsed, fallback)


def _client_ip(request: Request) -> str:
    forwarded = request.headers.get("x-forwarded-for")
    if forwarded:
        return forwarded.split(",", 1)[0].strip()
    if request.client:
        return request.client.host
    return "0.0.0.0"


def _serialize_card(card: Card, deck: Deck, state_row: UserCardState | None) -> dict:
    return {
        "card_uid": card.card_uid,
        "deck_uid": deck.deck_uid,
        "deck_title": deck.title,
        "question_md": card.question_md,
        "answer_md": card.answer_md,
        "is_new": state_row is None,
        "due_at": state_row.due_at.isoformat() if state_row else None,
    }


def _select_next_card(
    *, db: Session, user_id: int, deck_uid: str, now: datetime
) -> tuple[Card, Deck, UserCardState | None] | None:
    due_query = (
        select(Card, Deck, UserCardState)
        .join(Deck, Deck.id == Card.deck_id)
        .join(
            UserCardState,
            and_(UserCardState.card_id == Card.id, UserCardState.user_id == user_id),
        )
        .where(Card.is_active.is_(True), UserCardState.due_at <= now)
    )
    if deck_uid != "all":
        due_query = due_query.where(Deck.deck_uid == deck_uid)

    due_query = due_query.order_by(func.random()).limit(1)
    due_hit = db.execute(due_query).first()
    if due_hit is not None:
        card, deck, state_row = due_hit
        return card, deck, state_row

    seen_subq = select(UserCardState.card_id).where(UserCardState.user_id == user_id)
    new_query = (
        select(Card, Deck)
        .join(Deck, Deck.id == Card.deck_id)
        .where(Card.is_active.is_(True), Card.id.not_in(seen_subq))
    )
    if deck_uid != "all":
        new_query = new_query.where(Deck.deck_uid == deck_uid)

    new_query = new_query.order_by(Deck.sort_order.asc(), Card.position.asc()).limit(1)
    new_hit = db.execute(new_query).first()
    if new_hit is not None:
        card, deck = new_hit
        return card, deck, None

    return None


def _compute_streak(db: Session, user_id: int) -> int:
    """Compute streak efficiently by only fetching recent distinct dates."""
    today = date.today()
    # Only look back up to 366 days max
    cutoff = datetime.combine(today - timedelta(days=366), datetime.min.time()).replace(
        tzinfo=timezone.utc
    )
    raw_days = db.execute(
        select(func.date(ReviewLog.reviewed_at))
        .where(ReviewLog.user_id == user_id, ReviewLog.reviewed_at >= cutoff)
        .group_by(func.date(ReviewLog.reviewed_at))
        .order_by(func.date(ReviewLog.reviewed_at).desc())
    ).all()

    review_set = {date.fromisoformat(row[0]) for row in raw_days if row[0]}
    streak = 0
    day = today
    while day in review_set:
        streak += 1
        day -= timedelta(days=1)
    return streak


def _get_deck_stats(db: Session, user_id: int, now: datetime) -> list[dict]:
    deck_rows = db.execute(
        select(
            Deck.deck_uid,
            Deck.title,
            Deck.card_count,
            func.count(UserCardState.id).label("seen_count"),
            func.coalesce(
                func.sum(case((UserCardState.due_at <= now, 1), else_=0)), 0
            ).label("due_count"),
        )
        .outerjoin(Card, and_(Card.deck_id == Deck.id, Card.is_active.is_(True)))
        .outerjoin(
            UserCardState,
            and_(UserCardState.card_id == Card.id, UserCardState.user_id == user_id),
        )
        .group_by(Deck.id)
        .order_by(Deck.sort_order.asc(), Deck.title.asc())
    ).all()

    return [
        {
            "deck_uid": row.deck_uid,
            "title": row.title,
            "card_count": int(row.card_count or 0),
            "seen_count": int(row.seen_count or 0),
            "new_count": max(0, int(row.card_count or 0) - int(row.seen_count or 0)),
            "due_count": int(row.due_count or 0),
        }
        for row in deck_rows
    ]


def _get_quick_stats(db: Session, user_id: int, now: datetime) -> dict:
    today_start = datetime.combine(date.today(), datetime.min.time()).replace(
        tzinfo=timezone.utc
    )

    total_cards = db.execute(
        select(func.count(Card.id)).where(Card.is_active.is_(True))
    ).scalar_one()
    reviewed_cards = db.execute(
        select(func.count(UserCardState.id)).where(UserCardState.user_id == user_id)
    ).scalar_one()
    due_now = db.execute(
        select(func.count(UserCardState.id)).where(
            UserCardState.user_id == user_id, UserCardState.due_at <= now
        )
    ).scalar_one()
    today_reviews = db.execute(
        select(func.count(ReviewLog.id)).where(
            ReviewLog.user_id == user_id, ReviewLog.reviewed_at >= today_start
        )
    ).scalar_one()

    return {
        "total_cards": int(total_cards),
        "reviewed_cards": int(reviewed_cards),
        "new_cards": max(0, int(total_cards) - int(reviewed_cards)),
        "due_now": int(due_now),
        "today_reviews": int(today_reviews),
        "streak_days": _compute_streak(db, user_id),
    }


@router.get("/decks")
def list_decks(user: User = Depends(require_user), db: Session = Depends(get_db)):
    now = utcnow()
    return {"decks": _get_deck_stats(db, user.id, now)}


@router.post("/study/next")
def study_next(
    payload: NextCardPayload,
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    now = utcnow()
    deck_uid = payload.deck_uid.strip() if payload.deck_uid else "all"
    hit = _select_next_card(db=db, user_id=user.id, deck_uid=deck_uid or "all", now=now)
    if hit is None:
        return {"empty": True}

    card, deck, state_row = hit
    return {"empty": False, "card": _serialize_card(card, deck, state_row)}


@router.post("/study/review")
def study_review(
    payload: ReviewPayload,
    request: Request,
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    settings = request.app.state.settings
    ip = _client_ip(request)
    limiter: SimpleRateLimiter = request.app.state.rate_limiter
    allowed, retry_after = limiter.allow(
        key=f"review:{user.id}:{ip}",
        limit=settings.review_rate_limit_per_minute,
        window_seconds=60,
    )
    if not allowed:
        raise HTTPException(
            status_code=status.HTTP_429_TOO_MANY_REQUESTS,
            detail=f"limite_excedido_tente_em_{retry_after}s",
        )

    card = db.execute(
        select(Card).where(Card.card_uid == payload.card_uid, Card.is_active.is_(True))
    ).scalar_one_or_none()
    if card is None:
        raise HTTPException(status_code=404, detail="card_nao_encontrado")

    state_row = db.execute(
        select(UserCardState).where(
            UserCardState.user_id == user.id, UserCardState.card_id == card.id
        )
    ).scalar_one_or_none()

    old_due = state_row.due_at if state_row else None
    old_state = state_row.fsrs_state if state_row else None

    now = utcnow()
    fsrs_service: FSRSService = request.app.state.fsrs
    previous_card, new_card, review_log = fsrs_service.review(
        card_id=card.id,
        state_row=state_row,
        rating_value=payload.rating,
        review_duration_ms=payload.duration_ms,
        now=now,
    )

    if state_row is None:
        state_row = UserCardState(user_id=user.id, card_id=card.id, reps=0, lapses=0)
        db.add(state_row)

    state_row.fsrs_state = int(new_card.state.value)
    state_row.fsrs_step = new_card.step
    state_row.fsrs_stability = (
        float(new_card.stability) if new_card.stability is not None else None
    )
    state_row.fsrs_difficulty = (
        float(new_card.difficulty) if new_card.difficulty is not None else None
    )
    state_row.due_at = _as_aware(new_card.due, now)
    state_row.last_review_at = _as_aware(new_card.last_review, now)
    state_row.reps = (state_row.reps or 0) + 1
    if int(previous_card.state.value) == 2 and payload.rating == 1:
        state_row.lapses = (state_row.lapses or 0) + 1

    db.add(
        ReviewLog(
            user_id=user.id,
            card_id=card.id,
            rating=payload.rating,
            review_duration_ms=payload.duration_ms,
            reviewed_at=_as_aware(review_log.review_datetime, now),
            old_state=old_state,
            new_state=int(new_card.state.value),
            due_before=_as_aware(old_due, now) if old_due else None,
            due_after=_as_aware(new_card.due, now),
        )
    )

    db.commit()

    # Return combined response: review result + next card + updated stats
    # This eliminates 3 sequential requests from the frontend
    deck_uid = card.deck.deck_uid if card.deck else "all"
    next_hit = _select_next_card(db=db, user_id=user.id, deck_uid="all", now=now)
    next_card_data = None
    if next_hit is not None:
        nc, nd, ns = next_hit
        next_card_data = _serialize_card(nc, nd, ns)

    return {
        "ok": True,
        "card_uid": card.card_uid,
        "new_state": state_row.fsrs_state,
        "due_at": state_row.due_at.isoformat(),
        "reps": state_row.reps,
        "lapses": state_row.lapses,
        "next": {"empty": next_card_data is None, "card": next_card_data},
        "decks": _get_deck_stats(db, user.id, now),
        "quick_stats": _get_quick_stats(db, user.id, now),
    }


@router.get("/sync/export")
def sync_export(
    deck_uid: str = "all",
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    now = utcnow()
    selected_deck_uid = deck_uid.strip() if deck_uid else "all"

    rows_query = (
        select(Card, Deck, UserCardState)
        .join(Deck, Deck.id == Card.deck_id)
        .outerjoin(
            UserCardState,
            and_(UserCardState.card_id == Card.id, UserCardState.user_id == user.id),
        )
        .where(Card.is_active.is_(True))
        .order_by(Deck.sort_order.asc(), Card.position.asc())
    )
    if selected_deck_uid != "all":
        rows_query = rows_query.where(Deck.deck_uid == selected_deck_uid)

    rows = db.execute(rows_query).all()
    cards: list[dict] = []

    for card, deck, state_row in rows:
        cards.append(
            {
                "card_uid": card.card_uid,
                "deck_uid": deck.deck_uid,
                "deck_title": deck.title,
                "question_md": card.question_md,
                "answer_md": card.answer_md,
                "content_hash": card.content_hash,
                "position": int(card.position or 0),
                "state": {
                    "is_new": state_row is None,
                    "due_at": state_row.due_at.isoformat() if state_row else None,
                    "fsrs_state": int(state_row.fsrs_state) if state_row else None,
                    "fsrs_step": (
                        int(state_row.fsrs_step)
                        if state_row and state_row.fsrs_step is not None
                        else None
                    ),
                    "reps": int(state_row.reps or 0) if state_row else 0,
                    "lapses": int(state_row.lapses or 0) if state_row else 0,
                },
            }
        )

    return {
        "ok": True,
        "deck_uid": selected_deck_uid,
        "generated_at": now.isoformat(),
        "cards": cards,
        "decks": _get_deck_stats(db, user.id, now),
        "quick_stats": _get_quick_stats(db, user.id, now),
    }


@router.post("/sync/import")
def sync_import(
    payload: SyncImportPayload,
    request: Request,
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    if len(payload.events) > 1000:
        raise HTTPException(status_code=400, detail="limite_maximo_1000_eventos")

    now = utcnow()
    prepared_events: list[tuple[SyncEventPayload, str, datetime]] = []
    duplicate_in_payload = 0
    seen_payload_ids: set[str] = set()

    for event in payload.events:
        event_id = event.event_id.strip()
        if event_id in seen_payload_ids:
            duplicate_in_payload += 1
            continue
        seen_payload_ids.add(event_id)
        reviewed_at = _parse_iso_datetime(event.reviewed_at, fallback=now)
        prepared_events.append((event, event_id, reviewed_at))

    if not prepared_events:
        return {
            "ok": True,
            "accepted": 0,
            "duplicates": duplicate_in_payload,
            "errors": [],
            "decks": _get_deck_stats(db, user.id, now),
            "quick_stats": _get_quick_stats(db, user.id, now),
        }

    existing_ids = set(
        db.execute(
            select(SyncEvent.event_id).where(
                SyncEvent.user_id == user.id,
                SyncEvent.event_id.in_([event_id for _, event_id, _ in prepared_events]),
            )
        )
        .scalars()
        .all()
    )

    card_uids = sorted({event.card_uid for event, _, _ in prepared_events})
    cards_by_uid = {
        card.card_uid: card
        for card in db.execute(
            select(Card).where(Card.card_uid.in_(card_uids), Card.is_active.is_(True))
        )
        .scalars()
        .all()
    }

    fsrs_service: FSRSService = request.app.state.fsrs
    accepted = 0
    duplicates = duplicate_in_payload
    errors: list[dict[str, str]] = []

    ordered_events = sorted(prepared_events, key=lambda item: item[2])
    for event, event_id, reviewed_at in ordered_events:
        if event_id in existing_ids:
            duplicates += 1
            continue

        card = cards_by_uid.get(event.card_uid)
        if card is None:
            errors.append({"event_id": event_id, "detail": "card_nao_encontrado"})
            continue

        state_row = db.execute(
            select(UserCardState).where(
                UserCardState.user_id == user.id,
                UserCardState.card_id == card.id,
            )
        ).scalar_one_or_none()

        old_due = state_row.due_at if state_row else None
        old_state = state_row.fsrs_state if state_row else None

        previous_card, new_card, review_log = fsrs_service.review(
            card_id=card.id,
            state_row=state_row,
            rating_value=event.rating,
            review_duration_ms=event.duration_ms,
            now=reviewed_at,
        )

        if state_row is None:
            state_row = UserCardState(user_id=user.id, card_id=card.id, reps=0, lapses=0)
            db.add(state_row)

        state_row.fsrs_state = int(new_card.state.value)
        state_row.fsrs_step = new_card.step
        state_row.fsrs_stability = (
            float(new_card.stability) if new_card.stability is not None else None
        )
        state_row.fsrs_difficulty = (
            float(new_card.difficulty) if new_card.difficulty is not None else None
        )
        state_row.due_at = _as_aware(new_card.due, reviewed_at)
        state_row.last_review_at = _as_aware(new_card.last_review, reviewed_at)
        state_row.reps = (state_row.reps or 0) + 1
        if int(previous_card.state.value) == 2 and event.rating == 1:
            state_row.lapses = (state_row.lapses or 0) + 1

        db.add(
            ReviewLog(
                user_id=user.id,
                card_id=card.id,
                rating=event.rating,
                review_duration_ms=event.duration_ms,
                reviewed_at=_as_aware(review_log.review_datetime, reviewed_at),
                old_state=old_state,
                new_state=int(new_card.state.value),
                due_before=_as_aware(old_due, reviewed_at) if old_due else None,
                due_after=_as_aware(new_card.due, reviewed_at),
            )
        )

        db.add(SyncEvent(user_id=user.id, event_id=event_id))
        existing_ids.add(event_id)
        accepted += 1

    db.commit()

    fresh_now = utcnow()
    return {
        "ok": True,
        "accepted": accepted,
        "duplicates": duplicates,
        "errors": errors,
        "decks": _get_deck_stats(db, user.id, fresh_now),
        "quick_stats": _get_quick_stats(db, user.id, fresh_now),
    }


@router.get("/stats/summary")
def stats_summary(user: User = Depends(require_user), db: Session = Depends(get_db)):
    now = utcnow()

    overview = _get_quick_stats(db, user.id, now)

    per_deck_rows = db.execute(
        select(
            Deck.deck_uid,
            Deck.title,
            func.count(Card.id),
            func.count(UserCardState.id),
        )
        .join(Card, Card.deck_id == Deck.id)
        .outerjoin(
            UserCardState,
            and_(UserCardState.card_id == Card.id, UserCardState.user_id == user.id),
        )
        .where(Card.is_active.is_(True))
        .group_by(Deck.deck_uid, Deck.title, Deck.sort_order)
        .order_by(Deck.sort_order.asc())
    ).all()

    return {
        "overview": overview,
        "decks": [
            {
                "deck_uid": row[0],
                "title": row[1],
                "total_cards": int(row[2]),
                "reviewed_cards": int(row[3]),
            }
            for row in per_deck_rows
        ],
    }
