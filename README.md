# Roadmap EE&HH

Sistema web para estudo continuo de Engenharia Eletrica + Hardware Hacking com foco em operacao deck-first:

1. selecionar trilha/deck em markdown
2. revisar cards com FSRS
3. acompanhar progresso por deck

Total atual: 31 decks, 2049 cards.

## Novidades Recentes (2026-04-06)

- App Android com WebView e persistencia de sessao entre aberturas.
- Modo offline no Android para estudar sem internet.
- Sincronizacao offline->online com idempotencia por `event_id`.
- API de sync no backend:
	- `GET /api/sync/export`
	- `POST /api/sync/import`
- Deploy Debian validado com health local/publico e smoke tests.
- Script de deploy atualizado para nao sincronizar artefatos pesados do Android (`android-app/.gradle`, `android-app/build`, `android-app/app/build`).

## Visao Geral

- Execucao: app web (FastAPI) + app Android (WebView + modo offline).
- Conteudo: markdown versionado no repositorio.
- Revisao: scheduling FSRS por usuario/card.

## Estrutura do Projeto

- [app](app): backend, templates e JS da aplicacao
- [android-app](android-app): cliente Android (online + offline/sync)
- [content/flashcards/decks](content/flashcards/decks): fonte canonica dos decks markdown
- [content/roadmaps/decks](content/roadmaps/decks): roadmaps orientados a deck (`*.md`)
- [scripts](scripts): utilitarios de normalizacao/reindex/deploy
- [docs](docs): documentacao tecnica e operacional

## Subir Localmente (venv)

1. Criar ambiente e instalar dependencias:

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

2. Criar arquivo de ambiente:

```bash
cp .env.example .env
```

`APP_SECRET_KEY` agora e obrigatoria. A aplicacao carrega `.env` automaticamente no root do repositorio.

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

## Deploy Debian

Script oficial:

```bash
DEPLOY_HOST=deploy@server DEPLOY_DIR=/srv/roadmap_web scripts/deploy_debian.sh --health-public https://seu-host-publico/healthz
```

O script faz:

1. sincronizacao de codigo para o host remoto
2. validacao de `<DEPLOY_DIR>/.env` e de `APP_SECRET_KEY`
3. `docker compose up --build -d`
4. validacao de health local (`127.0.0.1:8088`)
5. validacao de health publico (quando informado)

Variaveis/flags disponiveis em [scripts/deploy_debian.sh](scripts/deploy_debian.sh).

## App Android e Sync Offline

Fluxo resumido:

1. login online no app
2. abrir modo Offline
3. baixar snapshot de cards
4. revisar offline (gera fila local de eventos)
5. sincronizar quando houver internet

Detalhes tecnicos em [docs/SYNC_OFFLINE_ANDROID.md](docs/SYNC_OFFLINE_ANDROID.md).

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
- [docs/SYNC_OFFLINE_ANDROID.md](docs/SYNC_OFFLINE_ANDROID.md): arquitetura do sync offline do Android com backend.
