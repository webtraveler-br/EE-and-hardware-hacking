#!/usr/bin/env python3
from __future__ import annotations

import argparse
import json
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[1]
if str(ROOT) not in sys.path:
    sys.path.insert(0, str(ROOT))

from app.flashcards import parse_deck_markdown

INLINE_OPEN_RE = re.compile(r"(?<!\\)\\{1,2}\(")
INLINE_CLOSE_RE = re.compile(r"(?<!\\)\\{1,2}\)")
DISPLAY_OPEN_RE = re.compile(r"(?<!\\)\\{1,2}\[")
DISPLAY_CLOSE_RE = re.compile(r"(?<!\\)\\{1,2}\]")


def _normalize_math_delimiters(text: str) -> str:
    # Normalize legacy Anki/MathJax delimiters to markdown-friendly dollars.
    normalized = DISPLAY_OPEN_RE.sub("$$", text)
    normalized = DISPLAY_CLOSE_RE.sub("$$", normalized)
    normalized = INLINE_OPEN_RE.sub("$", normalized)
    normalized = INLINE_CLOSE_RE.sub("$", normalized)
    return normalized


def _extract_title(raw_text: str) -> str:
    for line in raw_text.splitlines():
        stripped = line.strip()
        if stripped.startswith("#"):
            title = stripped.lstrip("#").strip()
            if title:
                return title
    raise ValueError("Deck invalido: esperado titulo markdown com heading '#'.")


def _compact_deck(raw_text: str) -> tuple[str, int]:
    title = _extract_title(raw_text)
    cards = parse_deck_markdown(raw_text)

    lines: list[str] = [f"# {title}", ""]
    for card in cards:
        lines.append(f"@card {card.card_uid}")
        lines.append(_normalize_math_delimiters(card.question_md.strip()))
        lines.append("@@")
        lines.append(_normalize_math_delimiters(card.answer_md.strip()))
        lines.append("")

    compact = "\n".join(lines).rstrip() + "\n"
    return compact, len(cards)


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        description=(
            "Normalize flashcard decks into compact markdown format "
            "(@card <id> + @@ separator)."
        )
    )
    parser.add_argument(
        "--decks-dir",
        default="content",
        help="Relative path to content directory",
    )
    parser.add_argument(
        "--check",
        action="store_true",
        help="Do not write files. Exit 1 if normalization changes are needed.",
    )
    return parser


def main() -> int:
    args = build_parser().parse_args()
    decks_dir = (ROOT / args.decks_dir).resolve()
    if not decks_dir.exists():
        raise FileNotFoundError(f"Diretorio de decks nao encontrado: {decks_dir}")

    files = sorted(
        f for f in decks_dir.rglob("*.md")
        if not f.name.endswith(".roadmap.md")
    )
    rewritten = 0
    unchanged = 0
    total_cards = 0
    bytes_before = 0
    bytes_after = 0

    for path in files:
        raw = path.read_text(encoding="utf-8", errors="replace")
        compact, card_count = _compact_deck(raw)

        bytes_before += len(raw.encode("utf-8"))
        bytes_after += len(compact.encode("utf-8"))
        total_cards += card_count

        if compact == raw:
            unchanged += 1
            continue

        rewritten += 1
        if not args.check:
            path.write_text(compact, encoding="utf-8")

    summary = {
        "decks": len(files),
        "cards": total_cards,
        "rewritten": rewritten,
        "unchanged": unchanged,
        "bytes_before": bytes_before,
        "bytes_after": bytes_after,
        "bytes_saved": max(0, bytes_before - bytes_after),
    }
    print(json.dumps(summary, ensure_ascii=False, indent=2))

    if args.check and rewritten > 0:
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
