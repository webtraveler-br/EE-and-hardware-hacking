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
ALL_DECK_UID = "all"


class NextCardPayload(BaseModel):
    deck_uid: str = "all"


class ReviewPayload(BaseModel):
    card_uid: str = Field(min_length=3, max_length=140)
    rating: int = Field(ge=1, le=4)
    duration_ms: int | None = Field(default=None, ge=0, le=600000)
    deck_uid: str | None = Field(default=None, max_length=120)


class SyncEventPayload(BaseModel):
    event_id: str = Field(min_length=8, max_length=120)
    card_uid: str = Field(min_length=3, max_length=140)
    rating: int = Field(ge=1, le=4)
    duration_ms: int | None = Field(default=None, ge=0, le=600000)
    reviewed_at: str = Field(min_length=10, max_length=64)


class SyncImportPayload(BaseModel):
    events: list[SyncEventPayload] = Field(default_factory=list)


class SyncLocalCardPayload(BaseModel):
    card_uid: str = Field(min_length=3, max_length=140)
    content_hash: str = Field(min_length=1, max_length=64)
    deck_uid: str = Field(min_length=1, max_length=120)
    position: int = Field(ge=0)
    due_at: str | None = Field(default=None, max_length=64)
    reps: int = Field(default=0, ge=0)
    lapses: int = Field(default=0, ge=0)
    is_new: bool = True
    fsrs_state: int | None = Field(default=None)
    fsrs_step: int | None = Field(default=None)
    fsrs_stability: float | None = Field(default=None)
    fsrs_difficulty: float | None = Field(default=None)
    last_review_at: str | None = Field(default=None, max_length=64)


class SyncExportDeltaPayload(BaseModel):
    deck_uid: str = Field(default=ALL_DECK_UID, max_length=120)
    cards: list[SyncLocalCardPayload] = Field(default_factory=list)


class SyncPushPayload(BaseModel):
    """Unified sync: import pending events + fetch delta since timestamp."""

    events: list[SyncEventPayload] = Field(default_factory=list)
    deck_uid: str = Field(default=ALL_DECK_UID, max_length=120)
    last_sync_at: str | None = Field(default=None, max_length=64)
    local_card_count: int = Field(default=0, ge=0)


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


def _normalize_deck_uid(raw: str | None) -> str:
    if raw is None:
        return ALL_DECK_UID

    deck_uid = raw.strip()
    if not deck_uid:
        raise HTTPException(status_code=400, detail="deck_uid_invalido")
    return deck_uid


def _resolve_deck_scope(db: Session, raw: str | None) -> tuple[str, Deck | None]:
    deck_uid = _normalize_deck_uid(raw)
    if deck_uid == ALL_DECK_UID:
        return deck_uid, None

    deck = db.execute(select(Deck).where(Deck.deck_uid == deck_uid)).scalar_one_or_none()
    if deck is None:
        raise HTTPException(status_code=404, detail="deck_nao_encontrado")
    return deck_uid, deck


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


def _serialize_sync_card(card: Card, deck: Deck, state_row: UserCardState | None) -> dict:
    return {
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
            "fsrs_stability": (
                float(state_row.fsrs_stability)
                if state_row and state_row.fsrs_stability is not None
                else None
            ),
            "fsrs_difficulty": (
                float(state_row.fsrs_difficulty)
                if state_row and state_row.fsrs_difficulty is not None
                else None
            ),
            "last_review_at": (
                state_row.last_review_at.isoformat()
                if state_row and state_row.last_review_at
                else None
            ),
            "reps": int(state_row.reps or 0) if state_row else 0,
            "lapses": int(state_row.lapses or 0) if state_row else 0,
        },
    }


def _serialize_sync_rows(rows: list[tuple[Card, Deck, UserCardState | None]]) -> list[dict]:
    return [_serialize_sync_card(card, deck, state_row) for card, deck, state_row in rows]


def _sync_export_rows(db: Session, user_id: int, deck_uid: str):
    rows_query = (
        select(Card, Deck, UserCardState)
        .join(Deck, Deck.id == Card.deck_id)
        .outerjoin(
            UserCardState,
            and_(UserCardState.card_id == Card.id, UserCardState.user_id == user_id),
        )
        .where(Card.is_active.is_(True))
        .order_by(Deck.sort_order.asc(), Card.position.asc())
    )
    if deck_uid != ALL_DECK_UID:
        rows_query = rows_query.where(Deck.deck_uid == deck_uid)
    return db.execute(rows_query).all()


def _local_due_matches(remote_due_at: datetime | None, local_due_at_raw: str | None) -> bool:
    if remote_due_at is None and not local_due_at_raw:
        return True
    if remote_due_at is None or not local_due_at_raw:
        return False
    return _as_aware(remote_due_at, remote_due_at) == _parse_iso_datetime(
        local_due_at_raw,
        fallback=_as_aware(remote_due_at, remote_due_at),
    )


def _local_float_matches(remote_value: float | None, local_value: float | None) -> bool:
    if remote_value is None and local_value is None:
        return True
    if remote_value is None or local_value is None:
        return False
    return abs(float(remote_value) - float(local_value)) <= 1e-9


def _card_needs_upsert(
    *,
    card: Card,
    deck: Deck,
    state_row: UserCardState | None,
    local_card: SyncLocalCardPayload | None,
) -> bool:
    if local_card is None:
        return True

    remote_is_new = state_row is None
    remote_reps = int(state_row.reps or 0) if state_row else 0
    remote_lapses = int(state_row.lapses or 0) if state_row else 0
    remote_fsrs_state = int(state_row.fsrs_state) if state_row else None
    remote_fsrs_step = (
        int(state_row.fsrs_step) if state_row and state_row.fsrs_step is not None else None
    )
    remote_fsrs_stability = (
        float(state_row.fsrs_stability)
        if state_row and state_row.fsrs_stability is not None
        else None
    )
    remote_fsrs_difficulty = (
        float(state_row.fsrs_difficulty)
        if state_row and state_row.fsrs_difficulty is not None
        else None
    )

    return (
        local_card.content_hash != card.content_hash
        or local_card.deck_uid != deck.deck_uid
        or local_card.position != int(card.position or 0)
        or local_card.is_new != remote_is_new
        or local_card.reps != remote_reps
        or local_card.lapses != remote_lapses
        or (
            local_card.fsrs_state is not None and local_card.fsrs_state != remote_fsrs_state
        )
        or (
            local_card.fsrs_step is not None and local_card.fsrs_step != remote_fsrs_step
        )
        or (
            local_card.fsrs_stability is not None
            and not _local_float_matches(remote_fsrs_stability, local_card.fsrs_stability)
        )
        or (
            local_card.fsrs_difficulty is not None
            and not _local_float_matches(remote_fsrs_difficulty, local_card.fsrs_difficulty)
        )
        or (
            local_card.last_review_at is not None
            and not _local_due_matches(
                state_row.last_review_at if state_row else None,
                local_card.last_review_at,
            )
        )
        or not _local_due_matches(state_row.due_at if state_row else None, local_card.due_at)
    )


def _select_next_card(
    *, db: Session, user_id: int, deck_uid: str, now: datetime
) -> tuple[Card, Deck, UserCardState | None] | None:
    # Priority 1: random due card
    due_query = (
        select(Card.id)
        .join(Deck, Deck.id == Card.deck_id)
        .join(
            UserCardState,
            and_(UserCardState.card_id == Card.id, UserCardState.user_id == user_id),
        )
        .where(Card.is_active.is_(True), UserCardState.due_at <= now)
    )
    if deck_uid != ALL_DECK_UID:
        due_query = due_query.where(Deck.deck_uid == deck_uid)
    due_query = due_query.order_by(func.random()).limit(1)

    due_id = db.execute(due_query).scalar_one_or_none()
    if due_id is not None:
        row = db.execute(
            select(Card, Deck, UserCardState)
            .join(Deck, Deck.id == Card.deck_id)
            .join(UserCardState, and_(UserCardState.card_id == Card.id, UserCardState.user_id == user_id))
            .where(Card.id == due_id)
        ).first()
        if row is not None:
            return row[0], row[1], row[2]

    # Priority 2: first unseen card by order
    seen_subq = select(UserCardState.card_id).where(UserCardState.user_id == user_id)
    new_query = (
        select(Card, Deck)
        .join(Deck, Deck.id == Card.deck_id)
        .where(Card.is_active.is_(True), Card.id.not_in(seen_subq))
    )
    if deck_uid != ALL_DECK_UID:
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
            func.coalesce(
                func.sum(
                    case(
                        (
                            UserCardState.fsrs_state.in_((1, 3)),
                            1,
                        ),
                        else_=0,
                    )
                ),
                0,
            ).label("learning_count"),
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
            "learning_count": int(row.learning_count or 0),
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


def _enforce_sync_rate_limit(request: Request, user: User) -> None:
    settings = request.app.state.settings
    ip = _client_ip(request)
    limiter: SimpleRateLimiter = request.app.state.rate_limiter
    allowed, retry_after = limiter.allow(
        key=f"sync:{user.id}:{ip}",
        limit=settings.sync_rate_limit_per_minute,
        window_seconds=60,
    )
    if not allowed:
        raise HTTPException(
            status_code=status.HTTP_429_TOO_MANY_REQUESTS,
            detail=f"limite_excedido_tente_em_{retry_after}s",
        )


@router.post("/study/next")
def study_next(
    payload: NextCardPayload,
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    now = utcnow()
    selected_deck_uid, _ = _resolve_deck_scope(db, payload.deck_uid)
    hit = _select_next_card(db=db, user_id=user.id, deck_uid=selected_deck_uid, now=now)
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
    selected_deck_uid, selected_deck = _resolve_deck_scope(db, payload.deck_uid)
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
    if selected_deck is not None and card.deck_id != selected_deck.id:
        raise HTTPException(status_code=400, detail="card_fora_do_deck")

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
    next_hit = _select_next_card(
        db=db,
        user_id=user.id,
        deck_uid=selected_deck_uid,
        now=now,
    )
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
    request: Request,
    deck_uid: str = ALL_DECK_UID,
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    _enforce_sync_rate_limit(request, user)
    now = utcnow()
    selected_deck_uid, _ = _resolve_deck_scope(db, deck_uid)
    rows = _sync_export_rows(db, user.id, selected_deck_uid)

    return {
        "ok": True,
        "deck_uid": selected_deck_uid,
        "generated_at": now.isoformat(),
        "scheduler_profile": request.app.state.fsrs.profile_payload(),
        "cards": _serialize_sync_rows(rows),
        "decks": _get_deck_stats(db, user.id, now),
        "quick_stats": _get_quick_stats(db, user.id, now),
    }


@router.post("/sync/export-delta")
def sync_export_delta(
    payload: SyncExportDeltaPayload,
    request: Request,
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    _enforce_sync_rate_limit(request, user)
    now = utcnow()
    selected_deck_uid, _ = _resolve_deck_scope(db, payload.deck_uid)
    rows = _sync_export_rows(db, user.id, selected_deck_uid)

    local_cards = {item.card_uid: item for item in payload.cards}
    remote_card_uids = {card.card_uid for card, _, _ in rows}

    upserts = [
        _serialize_sync_card(card, deck, state_row)
        for card, deck, state_row in rows
        if _card_needs_upsert(
            card=card,
            deck=deck,
            state_row=state_row,
            local_card=local_cards.get(card.card_uid),
        )
    ]
    removed_card_uids = sorted(set(local_cards) - remote_card_uids)

    return {
        "ok": True,
        "deck_uid": selected_deck_uid,
        "generated_at": now.isoformat(),
        "scheduler_profile": request.app.state.fsrs.profile_payload(),
        "upserts": upserts,
        "removed_card_uids": removed_card_uids,
        "unchanged_count": max(0, len(rows) - len(upserts)),
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
    _enforce_sync_rate_limit(request, user)
    if len(payload.events) > 1000:
        raise HTTPException(status_code=400, detail="limite_maximo_1000_eventos")

    now = utcnow()
    fsrs_service: FSRSService = request.app.state.fsrs

    accepted, duplicates, errors = _import_events_batch(
        db=db,
        user=user,
        events=payload.events,
        fsrs_service=fsrs_service,
        now=now,
    )
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


# ---------------------------------------------------------------------------
# Lightweight sync status check
# ---------------------------------------------------------------------------

@router.get("/sync/status")
def sync_status(
    request: Request,
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    """Return a lightweight fingerprint so Android can skip sync when nothing changed.

    The response includes:
    - server_card_count: total active cards on server
    - server_state_count: number of UserCardState rows for this user
    - latest_server_modified_at: max(server_modified_at) across user states
    - latest_card_updated_at: max(updated_at) across all active cards
    - pending_due_count: cards currently due for review
    """
    now = utcnow()

    server_card_count = db.execute(
        select(func.count(Card.id)).where(Card.is_active.is_(True))
    ).scalar_one()

    state_agg = db.execute(
        select(
            func.count(UserCardState.id),
            func.max(UserCardState.server_modified_at),
        ).where(UserCardState.user_id == user.id)
    ).first()
    server_state_count = int(state_agg[0] or 0) if state_agg else 0
    latest_state_modified = state_agg[1] if state_agg else None

    latest_card_updated = db.execute(
        select(func.max(Card.updated_at)).where(Card.is_active.is_(True))
    ).scalar_one_or_none()

    due_count = db.execute(
        select(func.count(UserCardState.id)).where(
            UserCardState.user_id == user.id,
            UserCardState.due_at <= now,
        )
    ).scalar_one()

    return {
        "ok": True,
        "server_now": now.isoformat(),
        "server_card_count": int(server_card_count),
        "server_state_count": server_state_count,
        "latest_server_modified_at": (
            latest_state_modified.isoformat() if latest_state_modified else None
        ),
        "latest_card_updated_at": (
            latest_card_updated.isoformat() if latest_card_updated else None
        ),
        "pending_due_count": int(due_count),
    }


# ---------------------------------------------------------------------------
# Unified sync/push: import events + timestamp-based delta in one round trip
# ---------------------------------------------------------------------------

def _import_events_batch(
    *,
    db: Session,
    user: User,
    events: list[SyncEventPayload],
    fsrs_service: FSRSService,
    now: datetime,
) -> tuple[int, int, list[dict[str, str]]]:
    """Process import events and return (accepted, duplicates, errors)."""
    prepared_events: list[tuple[SyncEventPayload, str, datetime]] = []
    duplicate_in_payload = 0
    seen_payload_ids: set[str] = set()

    for event in events:
        event_id = event.event_id.strip()
        if event_id in seen_payload_ids:
            duplicate_in_payload += 1
            continue
        seen_payload_ids.add(event_id)
        reviewed_at = _parse_iso_datetime(event.reviewed_at, fallback=now)
        prepared_events.append((event, event_id, reviewed_at))

    if not prepared_events:
        return 0, duplicate_in_payload, []

    existing_ids = set(
        db.execute(
            select(SyncEvent.event_id).where(
                SyncEvent.user_id == user.id,
                SyncEvent.event_id.in_([eid for _, eid, _ in prepared_events]),
            )
        )
        .scalars()
        .all()
    )

    card_uids = sorted({ev.card_uid for ev, _, _ in prepared_events})
    cards_by_uid = {
        card.card_uid: card
        for card in db.execute(
            select(Card).where(Card.card_uid.in_(card_uids), Card.is_active.is_(True))
        )
        .scalars()
        .all()
    }
    state_rows_by_card_id = {}
    if cards_by_uid:
        state_rows_by_card_id = {
            sr.card_id: sr
            for sr in db.execute(
                select(UserCardState).where(
                    UserCardState.user_id == user.id,
                    UserCardState.card_id.in_([c.id for c in cards_by_uid.values()]),
                )
            )
            .scalars()
            .all()
        }

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

        state_row = state_rows_by_card_id.get(card.id)
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
            state_rows_by_card_id[card.id] = state_row

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

    return accepted, duplicates, errors


def _delta_since_timestamp(
    *,
    db: Session,
    user_id: int,
    deck_uid: str,
    since: datetime | None,
    local_card_count: int,
) -> tuple[list[dict], list[str], int]:
    """Return (upserts, removed_uids, unchanged_count) based on server timestamps.

    If `since` is None or local has 0 cards, return all cards as upserts.
    """
    rows = _sync_export_rows(db, user_id, deck_uid)

    if since is None or local_card_count == 0:
        # Full export needed
        return _serialize_sync_rows(rows), [], 0

    # Find cards with content or state modified after `since`
    upserts = []
    unchanged = 0
    for card, deck, state_row in rows:
        card_changed = card.updated_at > since
        state_changed = (
            state_row is not None and state_row.server_modified_at > since
        )
        # New card that didn't exist before — state_row might be None
        state_is_new = state_row is None and local_card_count > 0

        if card_changed or state_changed or state_is_new:
            upserts.append(_serialize_sync_card(card, deck, state_row))
        else:
            unchanged += 1

    # Detect removed cards: if local_card_count > total server rows,
    # some cards were deleted but we can't know which without a list.
    # Return all server card_uids so client can diff locally.
    server_uids = sorted(card.card_uid for card, _, _ in rows)

    return upserts, server_uids, unchanged


@router.post("/sync/push")
def sync_push(
    payload: SyncPushPayload,
    request: Request,
    user: User = Depends(require_user),
    db: Session = Depends(get_db),
):
    """Unified sync endpoint: import + delta in one round trip.

    1. Imports pending review events (if any)
    2. Returns cards modified since `last_sync_at`
    3. Returns all server card_uids so client can detect removals

    This replaces the need for separate /sync/import + /sync/export-delta calls.
    """
    _enforce_sync_rate_limit(request, user)

    if len(payload.events) > 1000:
        raise HTTPException(status_code=400, detail="limite_maximo_1000_eventos")

    now = utcnow()
    selected_deck_uid, _ = _resolve_deck_scope(db, payload.deck_uid)
    fsrs_service: FSRSService = request.app.state.fsrs

    # Phase 1: import pending events
    accepted, duplicates, errors = 0, 0, []
    if payload.events:
        accepted, duplicates, errors = _import_events_batch(
            db=db,
            user=user,
            events=payload.events,
            fsrs_service=fsrs_service,
            now=now,
        )
        db.commit()

    # Phase 2: compute delta since last sync
    since = None
    if payload.last_sync_at:
        since = _parse_iso_datetime(payload.last_sync_at, fallback=now)

    fresh_now = utcnow()
    upserts, server_card_uids, unchanged_count = _delta_since_timestamp(
        db=db,
        user_id=user.id,
        deck_uid=selected_deck_uid,
        since=since,
        local_card_count=payload.local_card_count,
    )

    return {
        "ok": True,
        "server_now": fresh_now.isoformat(),
        "import_result": {
            "accepted": accepted,
            "duplicates": duplicates,
            "errors": errors,
        },
        "deck_uid": selected_deck_uid,
        "scheduler_profile": fsrs_service.profile_payload(),
        "upserts": upserts,
        "server_card_uids": server_card_uids,
        "unchanged_count": unchanged_count,
        "decks": _get_deck_stats(db, user.id, fresh_now),
        "quick_stats": _get_quick_stats(db, user.id, fresh_now),
    }
