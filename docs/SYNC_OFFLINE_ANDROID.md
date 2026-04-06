# Sync Offline Android - Arquitetura e Operacao

## Objetivo

Permitir revisao de flashcards sem internet no Android, com sincronizacao posterior segura para o backend sem duplicar eventos.

## Componentes

### Android

- MainActivity (online + sessao WebView)
- OfflineStudyActivity (download snapshot, revisao offline, upload da fila)
- OfflineStore (persistencia local em JSON)
- OfflineApiClient (chamadas HTTP para export/import)

### Backend

- GET /api/sync/export
- POST /api/sync/import
- tabela sync_events para idempotencia por usuario/evento

## Estrategia de consistencia

O desenho usa duas etapas:

1. Export de snapshot (estado autoritativo atual do servidor)
2. Import de eventos offline (delta de revisoes feitas no aparelho)

Depois do import, o cliente faz novo export. Isso garante convergencia do estado local para o estado autoritativo do servidor.

## Fluxo detalhado

### 1) Download para offline

Cliente chama:

- GET /api/sync/export?deck_uid=all

Servidor retorna para cada card:

- conteudo: question_md, answer_md, content_hash
- metadados: card_uid, deck_uid, deck_title, position
- estado do usuario: is_new, due_at, fsrs_state, fsrs_step, reps, lapses

Cliente salva em:

- offline_cards_v1.json

### 2) Revisao offline

Para cada nota (1..4), o app cria evento com:

- event_id: UUID
- card_uid
- rating
- duration_ms
- reviewed_at (ISO UTC)

Evento entra na fila local:

- offline_pending_reviews_v1.json

### 3) Upload/sync

Cliente envia lote:

- POST /api/sync/import

Payload:

- events: lista de eventos offline

Servidor executa:

1. rejeita lotes acima de 1000 eventos
2. remove duplicados dentro do proprio payload
3. parseia reviewed_at
4. ordena eventos por reviewed_at
5. ignora eventos ja vistos em sync_events (idempotencia)
6. para cada evento aceito:
   - aplica FSRS
   - atualiza/cria UserCardState
   - grava ReviewLog
   - grava SyncEvent(user_id, event_id)

Resposta:

- accepted
- duplicates
- errors (eventos invalidos, ex: card removido)
- decks e quick_stats atualizados

### 4) Pos-sync

Cliente remove da fila apenas os eventos sincronizados e faz novo:

- GET /api/sync/export?deck_uid=all

Com isso, o app atualiza o snapshot local para o estado atual do servidor.

## Garantias

- Retry-safe: reenvio do mesmo evento nao duplica review.
- Idempotencia forte: unique user_id + event_id no banco.
- Processamento ordenado por reviewed_at para manter causalidade offline.
- Convergencia: import + export finaliza com estado local alinhado ao servidor.

## Persistencia de sessao no Android

A sessao do WebView e persistida por cookie:

1. app le header de cookie via CookieManager
2. salva em SharedPreferences
3. restaura cookies no startup antes do primeiro loadUrl

Isso reduz perda de sessao entre relancamentos do app.

## Testes implementados

Arquivo:

- tests/test_sync_routes.py

Cobre:

- idempotencia de import por event_id
- export refletindo estado atualizado apos import

## Limites atuais

- Fila/snapshot em JSON local sem criptografia dedicada.
- Scheduler local do Android e simplificado; scheduler autoritativo e o do backend.
- UI offline atual sincroniza com deck_uid=all.

## Evolucoes sugeridas

1. Criptografar arquivos offline no dispositivo.
2. Permitir sync por deck especifico no app.
3. Adicionar retry exponencial com backoff e telemetria de falha.
4. Incluir testes E2E mobile-api para sincronizacao em lote.
