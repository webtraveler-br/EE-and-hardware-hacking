from __future__ import annotations

import re

from fastapi import APIRouter, Depends, HTTPException, Request
from fastapi.responses import HTMLResponse, RedirectResponse
from sqlalchemy import func, select
from sqlalchemy.orm import Session

from ..auth import get_db, get_optional_user
from ..models import Card, Deck, User

router = APIRouter(tags=["pages"])

ROADMAP_DECKS_DIR = "content/roadmaps/decks"

TRACKS = [
    {
        "id": 0,
        "label": "T0",
        "title": "Matematica e Fisica",
        "hours": "~87h",
        "summary": "Base formal para modelagem em EE e hardware.",
    },
    {
        "id": 1,
        "label": "T1",
        "title": "Circuitos DC/AC",
        "hours": "~40h",
        "summary": "Analise de redes, transitorio e AC.",
    },
    {
        "id": 2,
        "label": "T2",
        "title": "Eletronica e RF",
        "hours": "~45h",
        "summary": "Semicondutores, ganho, fontes e EMC.",
    },
    {
        "id": 3,
        "label": "T3",
        "title": "Digital e Embarcados",
        "hours": "~35h",
        "summary": "Logica digital, firmware e perifericos.",
    },
    {
        "id": 4,
        "label": "T4",
        "title": "Potencia e Industrial",
        "hours": "~40h",
        "summary": "Maquinas, protecao e automacao.",
    },
    {
        "id": 5,
        "label": "T5",
        "title": "Controle e DSP",
        "hours": "~40h",
        "summary": "Modelagem, estabilidade e sinais.",
    },
    {
        "id": 6,
        "label": "T6",
        "title": "Laboratorio Real",
        "hours": "~60h",
        "summary": "Execucao fisica e validacao de bancada.",
    },
    {
        "id": 7,
        "label": "T7",
        "title": "Hardware Hacking Basico",
        "hours": "~77h",
        "summary": "Recon, interfaces e firmware.",
    },
    {
        "id": 8,
        "label": "T8",
        "title": "Hardware Hacking Avancado",
        "hours": "~86h",
        "summary": "Pesquisa aplicada e ataques avancados.",
    },
    {
        "id": 9,
        "label": "T9",
        "title": "HTB CPTS",
        "hours": "~120h",
        "summary": "Metodo de pentest end-to-end.",
    },
]

DECK_PREFIX_RE = re.compile(r"^(?P<track>\d+)\.")
TRACK_BY_ID = {item["id"]: item for item in TRACKS}


def _render(request: Request, template: str, context: dict, user: User | None):
    settings = request.app.state.settings
    payload = {"request": request, "user": user, "app_name": settings.app_name}
    payload.update(context)
    templates = request.app.state.templates
    return templates.TemplateResponse(template, payload)


def _track_id_from_deck_uid(deck_uid: str) -> int | None:
    hit = DECK_PREFIX_RE.match(deck_uid.strip())
    if not hit:
        return None
    return int(hit.group("track"))


def _deck_roadmap_rel_path(deck_uid: str) -> str:
    return f"{ROADMAP_DECKS_DIR}/{deck_uid}.md"


def _serialize_deck(deck: Deck, *, roadmap_slug: str | None = None) -> dict[str, str | int | None]:
    payload: dict[str, str | int | None] = {
        "deck_uid": deck.deck_uid,
        "title": deck.title,
        "card_count": int(deck.card_count or 0),
    }
    if roadmap_slug:
        payload["roadmap_slug"] = roadmap_slug
    return payload


def _deck_uid_from_rel_path(rel_path: str) -> str | None:
    prefix = f"{ROADMAP_DECKS_DIR}/"
    if not rel_path.startswith(prefix):
        return None

    tail = rel_path[len(prefix) :]
    if tail.endswith(".md") and "/" not in tail:
        return tail[:-3]
    return None


def _build_deck_catalog_payload(content_service, decks: list[Deck]) -> list[dict[str, str | int | None]]:
    catalog: list[dict[str, str | int | None]] = []
    ordered = sorted(decks, key=lambda item: (item.sort_order, item.deck_uid))
    for deck in ordered:
        track_id = _track_id_from_deck_uid(deck.deck_uid)
        track_meta = TRACK_BY_ID.get(track_id) if track_id is not None else None

        roadmap_slug = content_service.slug_for_relpath(_deck_roadmap_rel_path(deck.deck_uid))
        track_label = "Sem trilha"
        track_title = "Deck sem classificacao"

        if track_meta is not None:
            track_label = track_meta["label"]
            track_title = track_meta["title"]

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
    focus_deck_uid = _deck_uid_from_rel_path(doc.rel_path)
    track_id = _track_id_from_deck_uid(focus_deck_uid) if focus_deck_uid else None
    track_meta = TRACK_BY_ID.get(track_id) if track_id is not None else None

    related_decks: list[dict[str, str | int | None]] = []
    focus_deck: dict[str, str | int | None] | None = None
    if track_meta is not None and track_id is not None:
        decks = (
            db.execute(select(Deck).order_by(Deck.sort_order.asc(), Deck.deck_uid.asc()))
            .scalars()
            .all()
        )
        for deck in decks:
            if _track_id_from_deck_uid(deck.deck_uid) == track_id:
                serialized = _serialize_deck(
                    deck,
                    roadmap_slug=request.app.state.content_service.slug_for_relpath(
                        _deck_roadmap_rel_path(deck.deck_uid)
                    ),
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
