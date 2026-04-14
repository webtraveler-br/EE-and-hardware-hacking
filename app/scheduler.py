from __future__ import annotations

from datetime import datetime, timedelta, timezone

from fsrs import Card as FsrsCard
from fsrs import Rating, Scheduler, State

from .models import UserCardState


class FSRSService:
    def __init__(
        self,
        *,
        desired_retention: float,
        learning_steps: tuple[timedelta, ...] = (
            timedelta(minutes=1),
            timedelta(minutes=10),
        ),
        relearning_steps: tuple[timedelta, ...] = (timedelta(minutes=10),),
        maximum_interval: int = 36500,
        enable_fuzzing: bool = False,
    ) -> None:
        self._scheduler = Scheduler(
            desired_retention=desired_retention,
            learning_steps=learning_steps,
            relearning_steps=relearning_steps,
            maximum_interval=maximum_interval,
            enable_fuzzing=enable_fuzzing,
        )

    def profile_payload(self) -> dict[str, object]:
        return {
            "parameters": [float(value) for value in self._scheduler.parameters],
            "desired_retention": float(self._scheduler.desired_retention),
            "learning_steps_seconds": [
                int(step.total_seconds()) for step in self._scheduler.learning_steps
            ],
            "relearning_steps_seconds": [
                int(step.total_seconds()) for step in self._scheduler.relearning_steps
            ],
            "maximum_interval": int(self._scheduler.maximum_interval),
            "enable_fuzzing": bool(self._scheduler.enable_fuzzing),
        }

    def build_card(self, *, card_id: int, state_row: UserCardState | None, now: datetime) -> FsrsCard:
        if state_row is None:
            return FsrsCard(card_id=card_id, due=now)

        state = State(state_row.fsrs_state)
        due = state_row.due_at or now
        if due.tzinfo is None:
            due = due.replace(tzinfo=timezone.utc)

        last_review = state_row.last_review_at
        if last_review and last_review.tzinfo is None:
            last_review = last_review.replace(tzinfo=timezone.utc)

        return FsrsCard(
            card_id=card_id,
            state=state,
            step=state_row.fsrs_step,
            stability=state_row.fsrs_stability,
            difficulty=state_row.fsrs_difficulty,
            due=due,
            last_review=last_review,
        )

    def review(
        self,
        *,
        card_id: int,
        state_row: UserCardState | None,
        rating_value: int,
        review_duration_ms: int | None,
        now: datetime,
    ):
        fsrs_card = self.build_card(card_id=card_id, state_row=state_row, now=now)
        rating = Rating(rating_value)
        duration_seconds = None
        if review_duration_ms is not None and review_duration_ms >= 0:
            duration_seconds = review_duration_ms // 1000

        new_card, review_log = self._scheduler.review_card(
            fsrs_card,
            rating,
            review_datetime=now,
            review_duration=duration_seconds,
        )
        return fsrs_card, new_card, review_log
