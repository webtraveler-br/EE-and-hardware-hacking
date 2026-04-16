from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path
import os


def _env_bool(name: str, default: bool) -> bool:
    raw = os.getenv(name)
    if raw is None:
        return default
    return raw.strip().lower() in {"1", "true", "yes", "on"}


def _load_dotenv_file(env_path: Path) -> None:
    if not env_path.exists():
        return

    try:
        lines = env_path.read_text(encoding="utf-8").splitlines()
    except OSError:
        return

    for raw_line in lines:
        line = raw_line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue

        key, value = line.split("=", 1)
        key = key.strip()
        if not key or key in os.environ:
            continue

        value = value.strip()
        if len(value) >= 2 and value[0] == value[-1] and value[0] in {'"', "'"}:
            value = value[1:-1]
        os.environ[key] = value


def _required_env(name: str) -> str:
    value = os.getenv(name, "").strip()
    if not value:
        raise ValueError(f"{name} obrigatoria. Defina no ambiente ou em .env.")
    return value


@dataclass(frozen=True)
class Settings:
    app_name: str
    secret_key: str
    session_cookie_name: str
    session_hours: int
    cookie_secure: bool
    database_url: str
    workspace_root: Path
    content_root: Path
    pow_difficulty: int
    pow_ttl_seconds: int
    auth_rate_limit_per_minute: int
    challenge_rate_limit_per_minute: int
    review_rate_limit_per_minute: int
    sync_rate_limit_per_minute: int
    fsrs_desired_retention: float

    @classmethod
    def load(cls) -> "Settings":
        app_root = Path(__file__).resolve().parents[1]
        workspace_default = Path(__file__).resolve().parents[1]
        _load_dotenv_file(app_root / ".env")

        workspace_root = Path(
            os.getenv("ROADMAP_WORKSPACE_ROOT", str(workspace_default))
        ).resolve()

        content_root = Path(
            os.getenv(
                "CONTENT_ROOT",
                str(workspace_root / "content"),
            )
        ).resolve()

        default_db_path = app_root / "data" / "study_os.db"
        database_url = os.getenv("DATABASE_URL", f"sqlite:///{default_db_path}")
        secret_key = _required_env("APP_SECRET_KEY")
        if secret_key == "CHANGE_ME_GENERATE_A_LONG_RANDOM_SECRET":
            raise ValueError("APP_SECRET_KEY placeholder invalida. Troque a chave antes de subir a app.")

        return cls(
            app_name=os.getenv("APP_NAME", "Roadmap EE&HH"),
            secret_key=secret_key,
            session_cookie_name=os.getenv("SESSION_COOKIE_NAME", "roadmap_session"),
            session_hours=int(os.getenv("SESSION_HOURS", "336")),
            cookie_secure=_env_bool("COOKIE_SECURE", False),
            database_url=database_url,
            workspace_root=workspace_root,
            content_root=content_root,
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
            sync_rate_limit_per_minute=max(
                5, int(os.getenv("SYNC_RATE_LIMIT_PER_MINUTE", "30"))
            ),
            fsrs_desired_retention=float(os.getenv("FSRS_DESIRED_RETENTION", "0.9")),
        )
