# Roadmap EE&HH

Sistema web para estudo continuo de Engenharia Eletrica + Hardware Hacking com foco em operacao deck-first:

Nota: sim, isso tudo foi vibe codado.

1. selecionar trilha/deck em markdown
2. revisar cards com FSRS
3. acompanhar progresso por deck

Total atual: 31 decks, 2049 cards.

## Visao Geral

- Execucao: app web (FastAPI) para roadmap, estudo e stats.
- Conteudo: markdown versionado no repositorio.
- Revisao: scheduling FSRS por usuario/card.

## Estrutura do Projeto

- [app](app): backend, templates e JS da aplicacao
- [content/flashcards/decks](content/flashcards/decks): fonte canonica dos decks markdown
- [content/roadmaps/decks](content/roadmaps/decks): roadmaps orientados a deck (`*.md`)
- [scripts](scripts): utilitarios de normalizacao/reindex/deploy

## Subir Localmente (venv)

1. Criar ambiente e instalar dependencias:

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

2. (Opcional) Criar arquivo de ambiente:

```bash
cp .env.example .env
```

3. Subir a aplicacao:

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8088
```

4. Acessar:

- Home: http://localhost:8088/
- Roadmap: http://localhost:8088/roadmap
- Estudo: http://localhost:8088/study
- Health: http://localhost:8088/healthz

## Subir com Docker

```bash
docker compose up --build -d
```

## Fluxo de Conteudo (Deck-First)

1. Editar decks em `content/flashcards/decks/*.md`.
2. Normalizar formato markdown:

```bash
python3 scripts/normalize_flashcards_markdown.py
```

3. Reindexar no banco:

```bash
python3 scripts/reindex_flashcards.py
```

4. (Recomendado) Rodar testes:

```bash
python -m pytest
```

## Documentacao

- [docs/APLICACAO_E_PROPOSITO.md](docs/APLICACAO_E_PROPOSITO.md): documentacao funcional e tecnica completa.
- [docs/REGRAS_FLASHCARDS.md](docs/REGRAS_FLASHCARDS.md): padrao de criacao/manutencao dos flashcards.
