from pathlib import Path

from app.content_service import RoadmapContentService


def test_refresh_index_only_reads_canonical_roadmap_tree(tmp_path: Path) -> None:
    roadmap_dir = tmp_path / "content" / "roadmaps" / "decks"
    roadmap_dir.mkdir(parents=True)
    roadmap_file = roadmap_dir / "1.01-circuitos.md"
    roadmap_file.write_text("# Circuitos\n\nConteudo", encoding="utf-8")

    (tmp_path / "README.md").write_text("# Fora do catalogo", encoding="utf-8")

    service = RoadmapContentService(workspace_root=tmp_path)
    service.refresh_index()

    slug = service.slug_for_relpath("content/roadmaps/decks/1.01-circuitos.md")
    assert slug is not None
    assert service.slug_for_relpath("README.md") is None

    doc, html = service.get_doc(slug)
    assert doc.title == "Circuitos"
    assert doc.section == "Trilha 1 · Circuitos"
    assert "Conteudo" in html