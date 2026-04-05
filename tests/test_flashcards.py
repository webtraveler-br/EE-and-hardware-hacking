import pytest

from app.flashcards import content_hash, parse_deck_markdown


def test_parse_deck_markdown_compact_cards() -> None:
    raw = """# Deck Teste Compacto

@card card_10
Qual e a lei de Ohm?
@@
\\(V = iR\\)

@card card_11
Unidade SI de corrente?
@@
A
"""
    cards = parse_deck_markdown(raw)
    assert len(cards) == 2
    assert cards[0].card_uid == "card_10"
    assert cards[0].position == 1
    assert cards[1].card_uid == "card_11"
    assert cards[1].position == 2
    assert "lei de Ohm" in cards[0].question_md
    assert "V = iR" in cards[0].answer_md


def test_parse_deck_markdown_rejects_legacy_blocks() -> None:
    raw = """# Deck Legado

<!-- CARD
id: card_1
position: 1
-->
<!-- QUESTION -->
Qual e a lei de Ohm?
<!-- ANSWER -->
\\(V = iR\\)
<!-- /CARD -->
"""
    with pytest.raises(ValueError, match="@card"):
        parse_deck_markdown(raw)


def test_content_hash_changes_with_body() -> None:
    a = content_hash("q1", "a1")
    b = content_hash("q1", "a2")
    assert a != b
