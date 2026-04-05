from __future__ import annotations

from dataclasses import dataclass
import base64
from pathlib import Path
import re

from markdown_it import MarkdownIt
from mdit_py_plugins.footnote import footnote_plugin
from mdit_py_plugins.tasklists import tasklists_plugin

ROADMAP_DECKS_DIR = "content/roadmaps/decks"
DECK_UID_RE = re.compile(r"^(?P<track>\d+)\..+")

SECTION_LABELS = {
    "0": "Trilha 0 · Matematica e Fisica",
    "1": "Trilha 1 · Circuitos",
    "2": "Trilha 2 · Eletronica e RF",
    "3": "Trilha 3 · Digital e Embarcados",
    "4": "Trilha 4 · Potencia e Industrial",
    "5": "Trilha 5 · Controle e DSP",
    "6": "Trilha 6 · Laboratorio",
    "7": "Trilha 7 · Hardware Hacking",
    "8": "Trilha 8 · Hardware Hacking Avancado",
    "9": "Trilha 9 · CPTS",
    "decks": "Outros Decks",
}

SECTION_ORDER = {
    "0": 0,
    "1": 1,
    "2": 2,
    "3": 3,
    "4": 4,
    "5": 5,
    "6": 6,
    "7": 7,
    "8": 8,
    "9": 9,
    "decks": 99,
}


@dataclass(frozen=True)
class RoadmapDoc:
    slug: str
    rel_path: str
    title: str
    section: str
    section_key: str


class RoadmapContentService:
    def __init__(self, *, workspace_root: Path, excluded_dirs: tuple[str, ...]) -> None:
        self.workspace_root = workspace_root
        self.excluded_dirs = set(excluded_dirs)
        self._docs: dict[str, RoadmapDoc] = {}
        self._slug_by_relpath: dict[str, str] = {}
        self._renderer = (
            MarkdownIt("commonmark", {"html": False, "linkify": True, "typographer": True})
            .enable("table")
            .use(tasklists_plugin)
            .use(footnote_plugin)
        )

    def refresh_index(self) -> None:
        docs: dict[str, RoadmapDoc] = {}
        slug_by_relpath: dict[str, str] = {}
        for md_path in sorted(self.workspace_root.rglob("*.md")):
            if self._should_skip(md_path):
                continue

            rel_path = md_path.relative_to(self.workspace_root).as_posix()
            if not self._is_project_markdown(rel_path):
                continue

            section_key = self._section_key(rel_path)
            text = md_path.read_text(encoding="utf-8", errors="replace")
            title = self._extract_title(md_path, text)
            section = SECTION_LABELS.get(section_key, "Outros")
            slug = relpath_to_slug(rel_path)
            docs[slug] = RoadmapDoc(
                slug=slug,
                rel_path=rel_path,
                title=title,
                section=section,
                section_key=section_key,
            )
            slug_by_relpath[rel_path] = slug

        self._docs = docs
        self._slug_by_relpath = slug_by_relpath

    def list_docs(self) -> list[RoadmapDoc]:
        return sorted(
            self._docs.values(),
            key=lambda item: (SECTION_ORDER.get(item.section_key, 99), item.rel_path),
        )

    def slug_for_relpath(self, rel_path: str) -> str | None:
        return self._slug_by_relpath.get(rel_path)

    def get_doc(self, slug: str) -> tuple[RoadmapDoc, str] | None:
        doc = self._docs.get(slug)
        if doc is None:
            return None

        abs_path = (self.workspace_root / doc.rel_path).resolve()
        if self.workspace_root not in abs_path.parents and abs_path != self.workspace_root:
            return None

        if not abs_path.exists():
            return None

        text = abs_path.read_text(encoding="utf-8", errors="replace")
        html = self._renderer.render(text)
        return doc, html

    def _should_skip(self, path: Path) -> bool:
        rel_parts = path.relative_to(self.workspace_root).parts
        return any(part in self.excluded_dirs for part in rel_parts)

    @staticmethod
    def _is_project_markdown(rel_path: str) -> bool:
        parts = rel_path.split("/")
        return (
            len(parts) == 4
            and parts[0] == "content"
            and parts[1] == "roadmaps"
            and parts[2] == "decks"
            and parts[3].endswith(".md")
        )

    @staticmethod
    def _section_key(rel_path: str) -> str:
        parts = rel_path.split("/")
        if (
            len(parts) == 4
            and parts[0] == "content"
            and parts[1] == "roadmaps"
            and parts[2] == "decks"
            and parts[3].endswith(".md")
        ):
            deck_uid = parts[3][:-3]
            hit = DECK_UID_RE.match(deck_uid)
            if hit:
                track = hit.group("track")
                if track in SECTION_ORDER:
                    return track
            return "decks"
        return "decks"

    @staticmethod
    def _extract_title(path: Path, text: str) -> str:
        for line in text.splitlines():
            line = line.strip()
            if line.startswith("#"):
                return line.lstrip("#").strip()
        return path.stem.replace("-", " ").replace("_", " ").strip().title()


def relpath_to_slug(rel_path: str) -> str:
    token = base64.urlsafe_b64encode(rel_path.encode("utf-8")).decode("ascii")
    return token.rstrip("=")


def slug_to_relpath(slug: str) -> str:
    padded = slug + "=" * ((4 - len(slug) % 4) % 4)
    return base64.urlsafe_b64decode(padded.encode("ascii")).decode("utf-8")
