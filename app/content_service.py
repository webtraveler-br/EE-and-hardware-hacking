from __future__ import annotations

from dataclasses import dataclass
import base64
from pathlib import Path

from markdown_it import MarkdownIt
from mdit_py_plugins.footnote import footnote_plugin
from mdit_py_plugins.tasklists import tasklists_plugin

from .roadmap_catalog import CONTENT_DIR, deck_uid_from_rel_path


@dataclass(frozen=True)
class RoadmapDoc:
    slug: str
    rel_path: str
    title: str


class RoadmapContentService:
    def __init__(self, *, workspace_root: Path) -> None:
        self.workspace_root = workspace_root
        self._docs: dict[str, RoadmapDoc] = {}
        self._slug_by_relpath: dict[str, str] = {}
        self._slug_by_deck_uid: dict[str, str] = {}
        self._renderer = (
            MarkdownIt("commonmark", {"html": False, "linkify": True, "typographer": True})
            .enable("table")
            .use(tasklists_plugin)
            .use(footnote_plugin)
        )

    def refresh_index(self) -> None:
        docs: dict[str, RoadmapDoc] = {}
        slug_by_relpath: dict[str, str] = {}
        slug_by_deck_uid: dict[str, str] = {}
        content_root = self.workspace_root / CONTENT_DIR
        if not content_root.exists():
            self._docs = docs
            self._slug_by_relpath = slug_by_relpath
            self._slug_by_deck_uid = slug_by_deck_uid
            return

        for md_path in sorted(content_root.rglob("*.roadmap.md")):
            rel_path = md_path.relative_to(self.workspace_root).as_posix()
            text = md_path.read_text(encoding="utf-8", errors="replace")
            title = self._extract_title(md_path, text)
            slug = relpath_to_slug(rel_path)
            docs[slug] = RoadmapDoc(
                slug=slug,
                rel_path=rel_path,
                title=title,
            )
            slug_by_relpath[rel_path] = slug
            uid = deck_uid_from_rel_path(rel_path)
            if uid:
                slug_by_deck_uid[uid] = slug

        self._docs = docs
        self._slug_by_relpath = slug_by_relpath
        self._slug_by_deck_uid = slug_by_deck_uid

    def slug_for_relpath(self, rel_path: str) -> str | None:
        return self._slug_by_relpath.get(rel_path)

    def slug_for_deck_uid(self, deck_uid: str) -> str | None:
        return self._slug_by_deck_uid.get(deck_uid)

    def get_doc(self, slug: str) -> tuple[RoadmapDoc, str] | None:
        doc = self._docs.get(slug)
        if doc is None:
            return None

        abs_path = (self.workspace_root / doc.rel_path).resolve()
        ws = self.workspace_root.resolve()
        if ws not in abs_path.parents and abs_path != ws:
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
