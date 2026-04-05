from __future__ import annotations

from dataclasses import dataclass
import logging
from pathlib import Path
import os
import secrets

logger = logging.getLogger(__name__)


def _env_bool(name: str, default: bool) -> bool:
    raw = os.getenv(name)
    if raw is None:
        return default
    return raw.strip().lower() in {"1", "true", "yes", "on"}


@dataclass(frozen=True)
class Settings:
    app_name: str
    secret_key: str
    session_cookie_name: str
    session_hours: int
    cookie_secure: bool
    database_url: str
    workspace_root: Path
    flashcard_root: Path
    markdown_excluded_dirs: tuple[str, ...]
    pow_difficulty: int
    pow_ttl_seconds: int
    auth_rate_limit_per_minute: int
    challenge_rate_limit_per_minute: int
    review_rate_limit_per_minute: int
    fsrs_desired_retention: float

    @classmethod
    def load(cls) -> "Settings":
        app_root = Path(__file__).resolve().parents[1]
        workspace_default = Path(__file__).resolve().parents[1]

        workspace_root = Path(
            os.getenv("ROADMAP_WORKSPACE_ROOT", str(workspace_default))
        ).resolve()

        flashcard_root = Path(
            os.getenv(
                "FLASHCARD_ROOT",
                str(workspace_root / "content" / "flashcards"),
            )
        ).resolve()

        default_db_path = app_root / "data" / "study_os.db"
        database_url = os.getenv("DATABASE_URL", f"sqlite:///{default_db_path}")

        excluded_raw = os.getenv(
            "MARKDOWN_EXCLUDED_DIRS",
            ".git,.venv,__pycache__,data",
        )
        excluded_dirs = tuple(
            part.strip() for part in excluded_raw.split(",") if part.strip()
        )

        secret_key = os.getenv("APP_SECRET_KEY")
        if not secret_key:
            secret_key = secrets.token_urlsafe(32)
            logger.warning(
                "APP_SECRET_KEY nao definida — usando chave efemera. "
                "Sessoes serao invalidadas ao reiniciar."
            )

        return cls(
            app_name=os.getenv("APP_NAME", "Roadmap EE&HH"),
            secret_key=secret_key,
            session_cookie_name=os.getenv("SESSION_COOKIE_NAME", "roadmap_session"),
            session_hours=int(os.getenv("SESSION_HOURS", "336")),
            cookie_secure=_env_bool("COOKIE_SECURE", False),
            database_url=database_url,
            workspace_root=workspace_root,
            flashcard_root=flashcard_root,
            markdown_excluded_dirs=excluded_dirs,
            pow_difficulty=max(1, int(os.getenv("POW_DIFFICULTY", "3"))),
            pow_ttl_seconds=max(30, int(os.getenv("POW_TTL_SECONDS", "180"))),
            auth_rate_limit_per_minute=max(
                3, int(os.getenv("AUTH_RATE_LIMIT_PER_MINUTE", "20"))
            ),
            challenge_rate_limit_per_minute=max(
                10, int(os.getenv("CHALLENGE_RATE_LIMIT_PER_MINUTE", "80"))
            ),
            review_rate_limit_per_minute=max(
                30, int(os.getenv("REVIEW_RATE_LIMIT_PER_MINUTE", "240"))
            ),
            fsrs_desired_retention=float(os.getenv("FSRS_DESIRED_RETENTION", "0.9")),
        )
