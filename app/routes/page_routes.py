from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException, Request
from fastapi.responses import HTMLResponse, RedirectResponse
from sqlalchemy import func, select
from sqlalchemy.orm import Session

from ..auth import get_db, get_optional_user
from ..models import Card, Deck, User
from ..roadmap_catalog import (
    DEFAULT_TRACK_LABEL,
    DEFAULT_TRACK_TITLE,
    deck_roadmap_rel_path,
    deck_uid_from_rel_path,
    track_meta_for_deck_uid,
)

router = APIRouter(tags=["pages"])


def _render(request: Request, template: str, context: dict, user: User | None):
    settings = request.app.state.settings
    payload = {"request": request, "user": user, "app_name": settings.app_name}
    payload.update(context)
    templates = request.app.state.templates
    return templates.TemplateResponse(template, payload)


def _serialize_deck(deck: Deck, *, roadmap_slug: str | None = None) -> dict[str, str | int | None]:
    payload: dict[str, str | int | None] = {
        "deck_uid": deck.deck_uid,
        "title": deck.title,
        "card_count": int(deck.card_count or 0),
    }
    if roadmap_slug:
        payload["roadmap_slug"] = roadmap_slug
    return payload


def _build_deck_catalog_payload(content_service, decks: list[Deck]) -> list[dict[str, str | int | None]]:
    catalog: list[dict[str, str | int | None]] = []
    ordered = sorted(decks, key=lambda item: (item.sort_order, item.deck_uid))
    for deck in ordered:
        track_meta = track_meta_for_deck_uid(deck.deck_uid)
        track_id = track_meta.id if track_meta is not None else None

        roadmap_slug = content_service.slug_for_relpath(deck_roadmap_rel_path(deck.deck_uid))
        track_label = DEFAULT_TRACK_LABEL
        track_title = DEFAULT_TRACK_TITLE

        if track_meta is not None:
            track_label = track_meta.label
            track_title = track_meta.title

        catalog.append(
            {
                "deck_uid": deck.deck_uid,
                "title": deck.title,
                "card_count": int(deck.card_count or 0),
                "track_id": track_id,
                "track_label": track_label,
                "track_title": track_title,
                "roadmap_slug": roadmap_slug,
            }
        )
    return catalog


@router.get("/healthz")
def healthz() -> dict[str, str]:
    return {"status": "ok"}


@router.get("/", response_class=HTMLResponse)
def home(
    request: Request,
    user: User | None = Depends(get_optional_user),
    db: Session = Depends(get_db),
):
    content_service = request.app.state.content_service
    decks = db.execute(select(Deck).order_by(Deck.sort_order.asc(), Deck.deck_uid.asc())).scalars().all()
    deck_catalog = _build_deck_catalog_payload(content_service, decks)

    total_decks = len(decks)
    total_cards = db.execute(
        select(func.count(Card.id)).where(Card.is_active.is_(True))
    ).scalar_one()

    context = {
        "project_stats": {
            "decks": int(total_decks),
            "cards": int(total_cards),
        },
        "deck_catalog": deck_catalog,
    }
    return _render(request, "index.html", context, user)


@router.get("/auth/login", response_class=HTMLResponse)
def login_page(request: Request):
    return _render(request, "login.html", {}, None)


@router.get("/auth/register", response_class=HTMLResponse)
def register_page(request: Request):
    return _render(request, "register.html", {}, None)


@router.get("/roadmap", response_class=HTMLResponse)
def roadmap_index(
    request: Request,
    user: User | None = Depends(get_optional_user),
    db: Session = Depends(get_db),
):
    content_service = request.app.state.content_service
    decks = db.execute(select(Deck).order_by(Deck.sort_order.asc(), Deck.deck_uid.asc())).scalars().all()
    context = {
        "deck_catalog": _build_deck_catalog_payload(content_service, decks),
    }
    return _render(request, "roadmap_index.html", context, user)


@router.get("/roadmap/{slug}", response_class=HTMLResponse)
def roadmap_doc(
    slug: str,
    request: Request,
    user: User | None = Depends(get_optional_user),
    db: Session = Depends(get_db),
):
    payload = request.app.state.content_service.get_doc(slug)
    if payload is None:
        raise HTTPException(status_code=404, detail="documento_nao_encontrado")

    doc, html = payload
    focus_deck_uid = deck_uid_from_rel_path(doc.rel_path)
    track_meta = track_meta_for_deck_uid(focus_deck_uid) if focus_deck_uid else None

    related_decks: list[dict[str, str | int | None]] = []
    focus_deck: dict[str, str | int | None] | None = None
    if track_meta is not None:
        decks = (
            db.execute(select(Deck).order_by(Deck.sort_order.asc(), Deck.deck_uid.asc()))
            .scalars()
            .all()
        )
        for deck in decks:
            if track_meta_for_deck_uid(deck.deck_uid) == track_meta:
                serialized = _serialize_deck(
                    deck,
                    roadmap_slug=request.app.state.content_service.slug_for_relpath(deck_roadmap_rel_path(deck.deck_uid)),
                )
                related_decks.append(serialized)
                if focus_deck_uid and deck.deck_uid == focus_deck_uid:
                    focus_deck = serialized

    context = {
        "doc": doc,
        "doc_html": html,
        "track": track_meta,
        "related_decks": related_decks,
        "focus_deck": focus_deck,
    }
    return _render(request, "roadmap_page.html", context, user)


@router.get("/study", response_class=HTMLResponse)
def study_page(request: Request, user: User | None = Depends(get_optional_user)):
    if user is None:
        return RedirectResponse(url="/auth/login", status_code=302)
    return _render(request, "study.html", {}, user)


@router.get("/stats", response_class=HTMLResponse)
def stats_page(request: Request, user: User | None = Depends(get_optional_user)):
    if user is None:
        return RedirectResponse(url="/auth/login", status_code=302)
    return _render(request, "stats.html", {}, user)
