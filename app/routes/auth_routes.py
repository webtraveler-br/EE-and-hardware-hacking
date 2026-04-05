from __future__ import annotations

import re

from fastapi import APIRouter, Depends, HTTPException, Request, status
from fastapi.responses import JSONResponse
from pydantic import BaseModel, Field
from sqlalchemy import select
from sqlalchemy.orm import Session

from ..anti_bot import ProofOfWorkManager, SimpleRateLimiter
from ..auth import get_db, get_optional_user
from ..config import Settings
from ..models import User, utcnow
from ..security import create_access_token, hash_password, verify_password

USERNAME_RE = re.compile(r"^[a-zA-Z0-9_.-]{3,32}$")

router = APIRouter(prefix="/api/auth", tags=["auth"])


class ChallengeVerifyPayload(BaseModel):
    challenge_id: str
    nonce: str


class RegisterPayload(ChallengeVerifyPayload):
    username: str = Field(min_length=3, max_length=32)
    password: str = Field(min_length=10, max_length=160)
    email: str | None = Field(default=None, max_length=120)
    honeypot: str | None = Field(default="", max_length=120)


class LoginPayload(ChallengeVerifyPayload):
    login: str = Field(min_length=3, max_length=120)
    password: str = Field(min_length=1, max_length=160)
    honeypot: str | None = Field(default="", max_length=120)


def _client_ip(request: Request) -> str:
    forwarded = request.headers.get("x-forwarded-for")
    if forwarded:
        return forwarded.split(",", 1)[0].strip()
    if request.client:
        return request.client.host
    return "0.0.0.0"


def _enforce_rate_limit(
    *, limiter: SimpleRateLimiter, key: str, limit: int, window_seconds: int
) -> None:
    allowed, retry_after = limiter.allow(key=key, limit=limit, window_seconds=window_seconds)
    if not allowed:
        raise HTTPException(
            status_code=status.HTTP_429_TOO_MANY_REQUESTS,
            detail=f"limite_excedido_tente_em_{retry_after}s",
        )


def _set_session_cookie(response: JSONResponse, settings: Settings, user: User) -> None:
    token = create_access_token(
        user_id=user.id,
        username=user.username,
        secret_key=settings.secret_key,
        expires_hours=settings.session_hours,
    )
    response.set_cookie(
        settings.session_cookie_name,
        token,
        max_age=settings.session_hours * 3600,
        httponly=True,
        secure=settings.cookie_secure,
        samesite="lax",
        path="/",
    )


@router.post("/challenge")
def auth_challenge(request: Request):
    settings: Settings = request.app.state.settings
    ip = _client_ip(request)
    limiter: SimpleRateLimiter = request.app.state.rate_limiter
    _enforce_rate_limit(
        limiter=limiter,
        key=f"challenge:{ip}",
        limit=settings.challenge_rate_limit_per_minute,
        window_seconds=60,
    )
    manager: ProofOfWorkManager = request.app.state.pow_manager
    return manager.create(ip)


@router.post("/register")
def auth_register(payload: RegisterPayload, request: Request, db: Session = Depends(get_db)):
    settings: Settings = request.app.state.settings
    ip = _client_ip(request)
    limiter: SimpleRateLimiter = request.app.state.rate_limiter

    _enforce_rate_limit(
        limiter=limiter,
        key=f"register:{ip}",
        limit=settings.auth_rate_limit_per_minute,
        window_seconds=60,
    )

    if payload.honeypot:
        raise HTTPException(status_code=400, detail="bot_detectado")

    if not USERNAME_RE.match(payload.username):
        raise HTTPException(status_code=400, detail="username_invalido")

    manager: ProofOfWorkManager = request.app.state.pow_manager
    valid, reason = manager.verify(
        challenge_id=payload.challenge_id, nonce=payload.nonce, ip=ip
    )
    if not valid:
        raise HTTPException(status_code=400, detail=reason)

    username_match = db.execute(
        select(User).where(User.username == payload.username)
    ).scalar_one_or_none()
    email_match = None
    if payload.email:
        email_match = db.execute(
            select(User).where(User.email == payload.email)
        ).scalar_one_or_none()

    if username_match or email_match:
        raise HTTPException(status_code=409, detail="usuario_ou_email_ja_existe")

    user = User(
        username=payload.username,
        email=payload.email.strip() if payload.email else None,
        password_hash=hash_password(payload.password),
    )
    db.add(user)
    db.commit()
    db.refresh(user)

    response = JSONResponse({"ok": True, "username": user.username})
    _set_session_cookie(response, settings, user)
    return response


@router.post("/login")
def auth_login(payload: LoginPayload, request: Request, db: Session = Depends(get_db)):
    settings: Settings = request.app.state.settings
    ip = _client_ip(request)
    limiter: SimpleRateLimiter = request.app.state.rate_limiter

    _enforce_rate_limit(
        limiter=limiter,
        key=f"login:{ip}",
        limit=settings.auth_rate_limit_per_minute,
        window_seconds=60,
    )

    if payload.honeypot:
        raise HTTPException(status_code=400, detail="bot_detectado")

    manager: ProofOfWorkManager = request.app.state.pow_manager
    valid, reason = manager.verify(
        challenge_id=payload.challenge_id, nonce=payload.nonce, ip=ip
    )
    if not valid:
        raise HTTPException(status_code=400, detail=reason)

    user = db.execute(
        select(User).where((User.username == payload.login) | (User.email == payload.login))
    ).scalar_one_or_none()
    if user is None or not verify_password(payload.password, user.password_hash):
        raise HTTPException(status_code=401, detail="credenciais_invalidas")

    user.last_login_at = utcnow()
    db.commit()

    response = JSONResponse({"ok": True, "username": user.username})
    _set_session_cookie(response, settings, user)
    return response


@router.post("/logout")
def auth_logout(request: Request):
    settings: Settings = request.app.state.settings
    response = JSONResponse({"ok": True})
    response.delete_cookie(settings.session_cookie_name, path="/")
    return response


me_router = APIRouter(tags=["auth"])


@me_router.get("/api/me")
def me(user: User | None = Depends(get_optional_user)):
    if user is None:
        return {"authenticated": False}
    return {"authenticated": True, "username": user.username}
