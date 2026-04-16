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
    deck_uid_from_rel_path,
    group_folder_from_source_path,
    parse_group_folder,
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


def _build_deck_catalog_payload(content_service, decks: list[Deck]) -> list[dict]:
    catalog: list[dict] = []
    ordered = sorted(decks, key=lambda item: (item.sort_order, item.deck_uid))
    for deck in ordered:
        roadmap_slug = content_service.slug_for_deck_uid(deck.deck_uid)

        # Derive group from folder structure (primary), prefix fallback
        folder = group_folder_from_source_path(deck.source_path)
        group = parse_group_folder(folder) if folder else None

        if group:
            group_key = group.folder
            track_label = group.label
            track_title = group.title
            group_order = group.order
            group_summary = group.summary
        else:
            track_meta = track_meta_for_deck_uid(deck.deck_uid)
            group_key = f"_prefix_{track_meta.id}" if track_meta else None
            track_label = track_meta.label if track_meta else DEFAULT_TRACK_LABEL
            track_title = track_meta.title if track_meta else DEFAULT_TRACK_TITLE
            group_order = track_meta.id if track_meta else 99
            group_summary = track_meta.summary if track_meta else ""

        catalog.append(
            {
                "deck_uid": deck.deck_uid,
                "title": deck.title,
                "card_count": int(deck.card_count or 0),
                "group_key": group_key,
                "track_label": track_label,
                "track_title": track_title,
                "group_order": group_order,
                "group_summary": group_summary,
                "roadmap_slug": roadmap_slug,
            }
        )
    return catalog


def _build_deck_groups(deck_catalog: list[dict]) -> list[dict]:
    """Group the flat deck catalog by folder (or prefix fallback)."""
    groups: dict[str | None, dict] = {}
    for deck in deck_catalog:
        key = deck.get("group_key")
        if key not in groups:
            groups[key] = {
                "group_key": key,
                "track_label": deck.get("track_label", "Extra"),
                "track_title": deck.get("track_title", "Colecoes extras"),
                "summary": deck.get("group_summary", ""),
                "section_order": deck.get("group_order", 99),
                "decks": [],
                "deck_count": 0,
                "card_count": 0,
            }
        groups[key]["decks"].append(deck)
        groups[key]["deck_count"] += 1
        groups[key]["card_count"] += deck.get("card_count", 0)

    return sorted(groups.values(), key=lambda g: g["section_order"])


def _active_decks(db: Session) -> list[Deck]:
    return list(
        db.execute(
            select(Deck)
            .where(Deck.card_count > 0)
            .order_by(Deck.sort_order.asc(), Deck.deck_uid.asc())
        ).scalars().all()
    )


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
    decks = _active_decks(db)
    deck_catalog = _build_deck_catalog_payload(content_service, decks)
    deck_groups = _build_deck_groups(deck_catalog)

    total_cards = db.execute(
        select(func.count(Card.id)).where(Card.is_active.is_(True))
    ).scalar_one()

    context = {
        "project_stats": {
            "decks": len(decks),
            "cards": int(total_cards),
            "tracks": len(deck_groups),
        },
        "deck_catalog": deck_catalog,
        "deck_groups": deck_groups,
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
    decks = _active_decks(db)
    deck_catalog = _build_deck_catalog_payload(content_service, decks)
    context = {
        "deck_catalog": deck_catalog,
        "deck_groups": _build_deck_groups(deck_catalog),
    }
    return _render(request, "roadmap_index.html", context, user)


@router.get("/roadmap/{slug}", response_class=HTMLResponse)
def roadmap_doc(
    slug: str,
    request: Request,
    user: User | None = Depends(get_optional_user),
    db: Session = Depends(get_db),
):
    content_service = request.app.state.content_service
    payload = content_service.get_doc(slug)
    if payload is None:
        raise HTTPException(status_code=404, detail="documento_nao_encontrado")

    doc, html = payload
    focus_deck_uid = deck_uid_from_rel_path(doc.rel_path)

    # Determine the group for related decks: folder-based, then prefix fallback
    focus_folder: str | None = None
    track_meta = None
    if focus_deck_uid:
        focus_deck_obj = db.execute(
            select(Deck).where(Deck.deck_uid == focus_deck_uid)
        ).scalar_one_or_none()
        if focus_deck_obj:
            focus_folder = group_folder_from_source_path(focus_deck_obj.source_path)
        if not focus_folder:
            track_meta = track_meta_for_deck_uid(focus_deck_uid)

    related_decks: list[dict[str, str | int | None]] = []
    focus_deck: dict[str, str | int | None] | None = None
    if focus_folder or track_meta:
        decks = _active_decks(db)
        for deck in decks:
            match = False
            if focus_folder:
                match = group_folder_from_source_path(deck.source_path) == focus_folder
            elif track_meta:
                match = track_meta_for_deck_uid(deck.deck_uid) == track_meta
            if match:
                serialized = _serialize_deck(
                    deck,
                    roadmap_slug=content_service.slug_for_deck_uid(deck.deck_uid),
                )
                related_decks.append(serialized)
                if focus_deck_uid and deck.deck_uid == focus_deck_uid:
                    focus_deck = serialized

    # Build display info for the sidebar header
    group = parse_group_folder(focus_folder) if focus_folder else None

    context = {
        "doc": doc,
        "doc_html": html,
        "track": group or track_meta,
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
