from __future__ import annotations

import logging
from pathlib import Path

from fastapi import FastAPI, Request

logger = logging.getLogger(__name__)
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates

from .anti_bot import ProofOfWorkManager, SimpleRateLimiter
from .config import Settings
from .content_service import RoadmapContentService
from .database import Base, build_engine, build_session_factory
from .flashcards import sync_flashcards
from .routes.auth_routes import me_router, router as auth_router
from .routes.page_routes import router as page_router
from .routes.study_routes import router as study_router
from .scheduler import FSRSService


def create_app() -> FastAPI:
    settings = Settings.load()

    app = FastAPI(title=settings.app_name)
    app.state.settings = settings

    engine = build_engine(settings.database_url)
    session_factory = build_session_factory(engine)
    app.state.engine = engine
    app.state.session_factory = session_factory

    app.state.pow_manager = ProofOfWorkManager(
        difficulty=settings.pow_difficulty,
        ttl_seconds=settings.pow_ttl_seconds,
    )
    app.state.rate_limiter = SimpleRateLimiter()
    app.state.fsrs = FSRSService(desired_retention=settings.fsrs_desired_retention)
    app.state.content_service = RoadmapContentService(
        workspace_root=settings.workspace_root,
    )

    app.state.templates = Jinja2Templates(
        directory=str(Path(__file__).parent / "templates")
    )
    app.mount(
        "/static",
        StaticFiles(directory=str(Path(__file__).parent / "static")),
        name="static",
    )

    app.include_router(auth_router)
    app.include_router(me_router)
    app.include_router(study_router)
    app.include_router(page_router)

    @app.on_event("startup")
    def startup() -> None:
        try:
            settings.flashcard_root.mkdir(parents=True, exist_ok=True)
        except OSError:
            logger.warning("Falha ao criar diretorio de flashcards: %s", settings.flashcard_root)
        Base.metadata.create_all(bind=engine)
        app.state.content_service.refresh_index()

        db = session_factory()
        try:
            sync_flashcards(db, settings.flashcard_root)
            db.commit()
        finally:
            db.close()

    @app.middleware("http")
    async def harden_headers(request: Request, call_next):
        response = await call_next(request)
        response.headers["X-Frame-Options"] = "DENY"
        response.headers["X-Content-Type-Options"] = "nosniff"
        response.headers["Referrer-Policy"] = "strict-origin-when-cross-origin"
        response.headers["Content-Security-Policy"] = (
            "default-src 'self'; "
            "script-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net; "
            "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://fonts.googleapis.com; "
            "font-src 'self' https://fonts.gstatic.com https://cdn.jsdelivr.net; "
            "img-src 'self' data:; "
            "connect-src 'self'; "
            "frame-ancestors 'none'"
        )
        return response

    return app


app = create_app()
