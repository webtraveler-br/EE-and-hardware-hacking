# Roadmap Android App

Aplicativo Android nativo para o sistema Roadmap EEHH.

## O que o app faz hoje

- Acesso online ao sistema via WebView.
- Persistencia de sessao (cookie) entre aberturas do app.
- Modo offline para estudar cards sem internet.
- Sincronizacao de revisoes offline quando a conexao volta.

## URL padrão

O app abre por padrão:

https://example.com/

Para build de producao/publico, configure via variavel de ambiente ou propriedade Gradle.

## Fluxo de uso (online + offline)

1. Abra o app e faca login online.
2. Toque no botao `Offline`.
3. Toque em `Baixar` para trazer snapshot dos cards.
4. Estude offline e avalie com notas 1..4.
5. Quando estiver online, toque em `Sincronizar`.

Ao sincronizar, o app:

- envia fila local de eventos para `/api/sync/import`
- remove eventos aceitos/sincronizados
- baixa novo snapshot de `/api/sync/export`

## Gerar APK debug

1. Entre na pasta do app Android:

   ./android-app

2. Rode o build:

   ./gradlew assembleDebug

3. APK gerado em:

   app/build/outputs/apk/debug/app-debug.apk

Build validado localmente com sucesso em 2026-04-06.

## Trocar URL base no build

Opcao 1 (recomendada): variavel de ambiente

export ROADMAP_BASE_URL=https://seu-endereco/
./gradlew assembleDebug

Opcao 2: propriedade Gradle

Você pode sobrescrever a URL no build com a propriedade ROADMAP_BASE_URL:

./gradlew assembleDebug -PROADMAP_BASE_URL=https://seu-endereco/

Exemplo para dev local em emulador Android:

./gradlew assembleDebug -PROADMAP_BASE_URL=http://10.0.2.2:8088/

## Endpoints usados pelo modo offline

- GET /api/sync/export?deck_uid=all
- POST /api/sync/import

O endpoint de import e idempotente por `event_id`: reenvio do mesmo evento nao duplica review.

## Arquivos locais do modo offline

Salvos em `filesDir` da app:

- `offline_cards_v1.json`
- `offline_pending_reviews_v1.json`

## Limitacoes atuais

- agendamento offline local e simplificado; estado final vem do servidor apos sync
- sem criptografia dedicada para os JSON offline
- sync hoje opera com snapshot `deck_uid=all`
