from __future__ import annotations

from dataclasses import dataclass
from pathlib import PurePosixPath
import re

ROADMAP_DECKS_DIR = PurePosixPath("content/roadmaps/decks")
DEFAULT_SECTION_KEY = "decks"
DEFAULT_SECTION_LABEL = "Outros Decks"
DEFAULT_SECTION_ORDER = 99
DEFAULT_TRACK_LABEL = "Sem trilha"
DEFAULT_TRACK_TITLE = "Deck sem classificacao"

DECK_PREFIX_RE = re.compile(r"^(?P<track>\d+)\.")


@dataclass(frozen=True)
class TrackMeta:
    id: int
    label: str
    title: str
    hours: str
    summary: str
    section_label: str
    section_order: int


TRACKS: tuple[TrackMeta, ...] = (
    TrackMeta(
        id=0,
        label="T0",
        title="Matematica e Fisica",
        hours="~87h",
        summary="Base formal para modelagem em EE e hardware.",
        section_label="Trilha 0 · Matematica e Fisica",
        section_order=0,
    ),
    TrackMeta(
        id=1,
        label="T1",
        title="Circuitos DC/AC",
        hours="~40h",
        summary="Analise de redes, transitorio e AC.",
        section_label="Trilha 1 · Circuitos",
        section_order=1,
    ),
    TrackMeta(
        id=2,
        label="T2",
        title="Eletronica e RF",
        hours="~45h",
        summary="Semicondutores, ganho, fontes e EMC.",
        section_label="Trilha 2 · Eletronica e RF",
        section_order=2,
    ),
    TrackMeta(
        id=3,
        label="T3",
        title="Digital e Embarcados",
        hours="~35h",
        summary="Logica digital, firmware e perifericos.",
        section_label="Trilha 3 · Digital e Embarcados",
        section_order=3,
    ),
    TrackMeta(
        id=4,
        label="T4",
        title="Potencia e Industrial",
        hours="~40h",
        summary="Maquinas, protecao e automacao.",
        section_label="Trilha 4 · Potencia e Industrial",
        section_order=4,
    ),
    TrackMeta(
        id=5,
        label="T5",
        title="Controle e DSP",
        hours="~40h",
        summary="Modelagem, estabilidade e sinais.",
        section_label="Trilha 5 · Controle e DSP",
        section_order=5,
    ),
    TrackMeta(
        id=6,
        label="T6",
        title="Laboratorio Real",
        hours="~60h",
        summary="Execucao fisica e validacao de bancada.",
        section_label="Trilha 6 · Laboratorio",
        section_order=6,
    ),
    TrackMeta(
        id=7,
        label="T7",
        title="Hardware Hacking Basico",
        hours="~77h",
        summary="Recon, interfaces e firmware.",
        section_label="Trilha 7 · Hardware Hacking",
        section_order=7,
    ),
    TrackMeta(
        id=8,
        label="T8",
        title="Hardware Hacking Avancado",
        hours="~86h",
        summary="Pesquisa aplicada e ataques avancados.",
        section_label="Trilha 8 · Hardware Hacking Avancado",
        section_order=8,
    ),
    TrackMeta(
        id=9,
        label="T9",
        title="HTB CPTS",
        hours="~120h",
        summary="Metodo de pentest end-to-end.",
        section_label="Trilha 9 · CPTS",
        section_order=9,
    ),
)

TRACK_BY_ID = {track.id: track for track in TRACKS}


def track_id_from_deck_uid(deck_uid: str) -> int | None:
    hit = DECK_PREFIX_RE.match(deck_uid.strip())
    if not hit:
        return None
    return int(hit.group("track"))


def track_meta_for_deck_uid(deck_uid: str) -> TrackMeta | None:
    track_id = track_id_from_deck_uid(deck_uid)
    if track_id is None:
        return None
    return TRACK_BY_ID.get(track_id)


def deck_roadmap_rel_path(deck_uid: str) -> str:
    return (ROADMAP_DECKS_DIR / f"{deck_uid}.md").as_posix()


def deck_uid_from_rel_path(rel_path: str) -> str | None:
    prefix = f"{ROADMAP_DECKS_DIR.as_posix()}/"
    if not rel_path.startswith(prefix):
        return None

    tail = rel_path[len(prefix) :]
    if tail.endswith(".md") and "/" not in tail:
        return tail[:-3]
    return None


def section_key_for_rel_path(rel_path: str) -> str:
    deck_uid = deck_uid_from_rel_path(rel_path)
    if deck_uid is None:
        return DEFAULT_SECTION_KEY

    track_id = track_id_from_deck_uid(deck_uid)
    if track_id is None or track_id not in TRACK_BY_ID:
        return DEFAULT_SECTION_KEY
    return str(track_id)


def section_label_for_key(section_key: str) -> str:
    if section_key == DEFAULT_SECTION_KEY:
        return DEFAULT_SECTION_LABEL

    track_id = int(section_key)
    track = TRACK_BY_ID.get(track_id)
    if track is None:
        return DEFAULT_SECTION_LABEL
    return track.section_label


def section_order_for_key(section_key: str) -> int:
    if section_key == DEFAULT_SECTION_KEY:
        return DEFAULT_SECTION_ORDER

    track_id = int(section_key)
    track = TRACK_BY_ID.get(track_id)
    if track is None:
        return DEFAULT_SECTION_ORDER
    return track.section_order