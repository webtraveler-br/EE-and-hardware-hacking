from __future__ import annotations

from dataclasses import dataclass
from pathlib import PurePosixPath
import re

CONTENT_DIR = PurePosixPath("content")

DECK_PREFIX_RE = re.compile(r"^(?P<track>\d+)\.")
FOLDER_RE = re.compile(r"^t(\d+)-(.+)$")

_TITLE_UPPERCASE = {"rf", "emc", "dsp", "cpts", "hh", "dc", "ac", "edos", "htb"}
_TITLE_LOWERCASE = {"e", "de", "em", "do", "da", "dos", "das", "o", "a", "os", "as"}


DEFAULT_TRACK_LABEL = "Extra"
DEFAULT_TRACK_TITLE = "Colecoes extras"


@dataclass(frozen=True)
class TrackMeta:
    id: int
    label: str
    title: str
    hours: str
    summary: str


@dataclass(frozen=True)
class GroupMeta:
    folder: str
    order: int
    label: str
    title: str
    summary: str
    hours: str


# Optional metadata overlay — hours/summary for known tracks.
TRACKS: tuple[TrackMeta, ...] = (
    TrackMeta(id=0, label="T0", title="Matematica e Fisica", hours="~87h",
              summary="Base formal para modelagem em EE e hardware."),
    TrackMeta(id=1, label="T1", title="Circuitos DC/AC", hours="~40h",
              summary="Analise de redes, transitorio e AC."),
    TrackMeta(id=2, label="T2", title="Eletronica e RF", hours="~45h",
              summary="Semicondutores, ganho, fontes e EMC."),
    TrackMeta(id=3, label="T3", title="Digital e Embarcados", hours="~35h",
              summary="Logica digital, firmware e perifericos."),
    TrackMeta(id=4, label="T4", title="Potencia e Industrial", hours="~40h",
              summary="Maquinas, protecao e automacao."),
    TrackMeta(id=5, label="T5", title="Controle, DSP e Telecom", hours="~50h",
              summary="Modelagem, estabilidade, sinais e telecomunicacoes."),
    TrackMeta(id=7, label="T7", title="Hardware Hacking", hours="~77h",
              summary="Recon, interfaces e firmware."),
    TrackMeta(id=9, label="T9", title="CPTS", hours="~120h",
              summary="Metodo de pentest end-to-end."),
)

TRACK_BY_ID = {track.id: track for track in TRACKS}


# ── Folder-based grouping ──────────────────────────────────


def _folder_slug_to_title(slug: str) -> str:
    words = slug.replace("-", " ").split()
    result: list[str] = []
    for i, w in enumerate(words):
        low = w.lower()
        if low in _TITLE_UPPERCASE:
            result.append(w.upper())
        elif i > 0 and low in _TITLE_LOWERCASE:
            result.append(low)
        else:
            result.append(w.capitalize())
    return " ".join(result)


def parse_group_folder(folder_name: str) -> GroupMeta | None:
    m = FOLDER_RE.match(folder_name)
    if not m:
        return None
    order = int(m.group(1))
    slug = m.group(2)
    label = f"T{order}"
    title = _folder_slug_to_title(slug)
    track = TRACK_BY_ID.get(order)
    return GroupMeta(
        folder=folder_name,
        order=order,
        label=label,
        title=title,
        summary=track.summary if track else "",
        hours=track.hours if track else "",
    )


def group_folder_from_source_path(source_path: str | None) -> str | None:
    """Extract group folder from a source_path like 'tN-xxx/uid.md'."""
    if not source_path:
        return None
    parts = PurePosixPath(source_path).parts
    if len(parts) >= 2:
        return parts[0]
    return None


# ── Deck UID extraction ────────────────────────────────────


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


def deck_uid_from_rel_path(rel_path: str) -> str | None:
    """Extract deck_uid from a roadmap rel_path like 'content/tN-xxx/X.roadmap.md'."""
    path = PurePosixPath(rel_path)
    if not path.name.endswith(".roadmap.md"):
        return None
    return path.name[: -len(".roadmap.md")]