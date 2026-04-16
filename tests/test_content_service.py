from pathlib import Path

from app.content_service import RoadmapContentService


def test_refresh_index_only_reads_canonical_roadmap_tree(tmp_path: Path) -> None:
    group_dir = tmp_path / "content" / "t1-circuitos"
    group_dir.mkdir(parents=True)
    roadmap_file = group_dir / "1.01-circuitos.roadmap.md"
    roadmap_file.write_text("# Circuitos\n\nConteudo", encoding="utf-8")

    # Flashcard file should NOT be indexed (not *.roadmap.md)
    fc_file = group_dir / "1.01-circuitos.md"
    fc_file.write_text("# Circuitos FC\n\n@card c1\nQ\n@@\nA\n", encoding="utf-8")

    (tmp_path / "README.md").write_text("# Fora do catalogo", encoding="utf-8")

    service = RoadmapContentService(workspace_root=tmp_path)
    service.refresh_index()

    slug = service.slug_for_relpath("content/t1-circuitos/1.01-circuitos.roadmap.md")
    assert slug is not None
    assert service.slug_for_relpath("README.md") is None
    assert service.slug_for_relpath("content/t1-circuitos/1.01-circuitos.md") is None

    assert service.slug_for_deck_uid("1.01-circuitos") == slug

    doc, html = service.get_doc(slug)
    assert doc.title == "Circuitos"
    assert "Conteudo" in html