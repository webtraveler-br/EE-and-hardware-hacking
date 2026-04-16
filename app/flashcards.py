from __future__ import annotations

from dataclasses import dataclass
import hashlib
from pathlib import Path
import re

from sqlalchemy import select
from sqlalchemy.orm import Session

from .models import Card, Deck, utcnow

COMPACT_CARD_RE = re.compile(
    r"^@card\s+(?P<card_uid>\S+)\s*\n(?P<body>.*?)(?=^@card\s+|\Z)",
    re.MULTILINE | re.DOTALL,
)

COMPACT_SEPARATOR_RE = re.compile(r"^@@\s*$", re.MULTILINE)


@dataclass(frozen=True)
class ParsedDeckCard:
    card_uid: str
    position: int
    question_md: str
    answer_md: str


def parse_deck_markdown(text: str) -> list[ParsedDeckCard]:
    normalized = text.replace("\r\n", "\n")
    parsed_cards: list[ParsedDeckCard] = []

    for index, match in enumerate(COMPACT_CARD_RE.finditer(normalized), start=1):
        card_uid = match.group("card_uid").strip()
        body = match.group("body").strip()

        if not card_uid or not body:
            raise ValueError("Card invalido: @card exige id e corpo com pergunta/resposta.")

        sep_hit = COMPACT_SEPARATOR_RE.search(body)
        if sep_hit is None:
            raise ValueError(f"Card '{card_uid}' sem separador '@@'.")

        question_md = body[: sep_hit.start()].strip()
        answer_md = body[sep_hit.end() :].strip()
        if not question_md or not answer_md:
            raise ValueError(f"Card '{card_uid}' precisa de pergunta e resposta nao vazias.")

        parsed_cards.append(
            ParsedDeckCard(
                card_uid=card_uid,
                position=index,
                question_md=question_md,
                answer_md=answer_md,
            )
        )

    if not parsed_cards:
        raise ValueError("Deck invalido: esperado ao menos um bloco '@card' com separador '@@'.")

    return parsed_cards


def _extract_deck_title(text: str) -> str:
    for line in text.splitlines():
        stripped = line.strip()
        if stripped.startswith("#"):
            title = stripped.lstrip("#").strip()
            if title:
                return title
    raise ValueError("Deck invalido: esperado titulo markdown com heading '#'.")


def content_hash(question_md: str, answer_md: str) -> str:
    raw = f"{question_md}\n\n---\n\n{answer_md}".encode("utf-8")
    return hashlib.sha256(raw).hexdigest()


def _upsert_card(
    *,
    db: Session,
    card_by_uid: dict[str, Card],
    card_uid: str,
    deck_id: int,
    source_file: str,
    question_md: str,
    answer_md: str,
    position: int,
) -> bool:
    digest = content_hash(question_md, answer_md)
    card = card_by_uid.get(card_uid)
    if card is None:
        card = Card(
            card_uid=card_uid,
            deck_id=deck_id,
            source_file=source_file,
            question_md=question_md,
            answer_md=answer_md,
            content_hash=digest,
            position=position,
            is_active=True,
        )
        db.add(card)
        card_by_uid[card_uid] = card
        return True

    changed = False
    if card.deck_id != deck_id:
        card.deck_id = deck_id
        changed = True
    if card.source_file != source_file:
        card.source_file = source_file
        changed = True
    if card.question_md != question_md:
        card.question_md = question_md
        changed = True
    if card.answer_md != answer_md:
        card.answer_md = answer_md
        changed = True
    if card.content_hash != digest:
        card.content_hash = digest
        changed = True
    if card.position != position:
        card.position = position
        changed = True
    if not card.is_active:
        card.is_active = True
        changed = True

    if changed:
        card.updated_at = utcnow()
    return changed


def _sync_from_deck_files(
    *,
    db: Session,
    cards_root: Path,
    deck_files: list[Path],
) -> dict[str, int]:
    deck_by_uid = {deck.deck_uid: deck for deck in db.execute(select(Deck)).scalars().all()}
    card_by_uid = {card.card_uid: card for card in db.execute(select(Card)).scalars().all()}

    active_decks: set[str] = set()
    active_card_uids: set[str] = set()
    updated = 0
    total_cards = 0

    for deck_order, deck_file in enumerate(deck_files, start=1):
        deck_uid = deck_file.stem

        raw_text = deck_file.read_text(encoding="utf-8", errors="replace")
        inferred_title = _extract_deck_title(raw_text)

        title = inferred_title
        source_path = deck_file.relative_to(cards_root).as_posix()
        order = deck_order

        deck = deck_by_uid.get(deck_uid)
        if deck is None:
            deck = Deck(
                deck_uid=deck_uid,
                title=title,
                source_path=source_path,
                sort_order=order,
                card_count=0,
            )
            db.add(deck)
            db.flush()
            deck_by_uid[deck_uid] = deck
            updated += 1
        else:
            if deck.title != title:
                deck.title = title
            if deck.source_path != source_path:
                deck.source_path = source_path
            if deck.sort_order != order:
                deck.sort_order = order
            deck.updated_at = utcnow()

        parsed_cards = parse_deck_markdown(raw_text)
        deck.card_count = len(parsed_cards)
        rel_file = deck_file.relative_to(cards_root).as_posix()

        for default_position, parsed in enumerate(parsed_cards, start=1):
            position = parsed.position or default_position
            changed = _upsert_card(
                db=db,
                card_by_uid=card_by_uid,
                card_uid=parsed.card_uid,
                deck_id=deck.id,
                source_file=rel_file,
                question_md=parsed.question_md,
                answer_md=parsed.answer_md,
                position=position,
            )
            if changed:
                updated += 1

            active_card_uids.add(parsed.card_uid)
            total_cards += 1

        active_decks.add(deck_uid)

    inactive = 0
    for card in card_by_uid.values():
        if card.card_uid not in active_card_uids and card.is_active:
            card.is_active = False
            card.updated_at = utcnow()
            inactive += 1

    for deck in deck_by_uid.values():
        if deck.deck_uid not in active_decks:
            deck.card_count = 0
            deck.updated_at = utcnow()

    return {
        "decks": len(active_decks),
        "cards": total_cards,
        "updated": updated,
        "inactive": inactive,
    }


def sync_flashcards(db: Session, content_root: Path) -> dict[str, int]:
    if not content_root.exists():
        return {"decks": 0, "cards": 0, "updated": 0, "inactive": 0}

    deck_files = sorted(
        f for f in content_root.rglob("*.md")
        if not f.name.endswith(".roadmap.md")
    )
    if not deck_files:
        return {"decks": 0, "cards": 0, "updated": 0, "inactive": 0}

    return _sync_from_deck_files(
        db=db,
        cards_root=content_root,
        deck_files=deck_files,
    )

