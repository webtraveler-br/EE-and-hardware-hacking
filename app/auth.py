from __future__ import annotations

from typing import Generator

from fastapi import Depends, HTTPException, Request, status
from sqlalchemy import select
from sqlalchemy.orm import Session

from .models import User
from .security import decode_access_token


def get_db(request: Request) -> Generator[Session, None, None]:
    session_factory = request.app.state.session_factory
    db = session_factory()
    try:
        yield db
    finally:
        db.close()


def get_optional_user(request: Request, db: Session = Depends(get_db)) -> User | None:
    cookie_name = request.app.state.settings.session_cookie_name
    secret_key = request.app.state.settings.secret_key

    token = request.cookies.get(cookie_name)
    if not token:
        return None

    payload = decode_access_token(token, secret_key)
    if payload is None:
        return None

    sub = payload.get("sub")
    if sub is None:
        return None

    try:
        user_id = int(sub)
    except (TypeError, ValueError):
        return None

    return db.execute(select(User).where(User.id == user_id, User.is_active.is_(True))).scalar_one_or_none()


def require_user(user: User | None = Depends(get_optional_user)) -> User:
    if user is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="nao_autenticado")
    return user
