from __future__ import annotations

from dataclasses import dataclass
import base64
from pathlib import Path

from markdown_it import MarkdownIt
from mdit_py_plugins.footnote import footnote_plugin
from mdit_py_plugins.tasklists import tasklists_plugin

from .roadmap_catalog import (
    ROADMAP_DECKS_DIR,
    section_key_for_rel_path,
    section_label_for_key,
)


@dataclass(frozen=True)
class RoadmapDoc:
    slug: str
    rel_path: str
    title: str
    section: str
    section_key: str


class RoadmapContentService:
    def __init__(self, *, workspace_root: Path) -> None:
        self.workspace_root = workspace_root
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
        deck_root = self.workspace_root / ROADMAP_DECKS_DIR
        if not deck_root.exists():
            self._docs = docs
            self._slug_by_relpath = slug_by_relpath
            return

        for md_path in sorted(deck_root.glob("*.md")):
            rel_path = md_path.relative_to(self.workspace_root).as_posix()
            section_key = section_key_for_rel_path(rel_path)
            text = md_path.read_text(encoding="utf-8", errors="replace")
            title = self._extract_title(md_path, text)
            section = section_label_for_key(section_key)
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
