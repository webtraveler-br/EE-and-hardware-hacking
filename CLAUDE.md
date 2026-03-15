# Roadmaps - Currículo de EE & Hardware Hacking

Currículo autodidata de Engenharia Elétrica (UTFPR) + Hardware Hacking + CPTS.
10 módulos (0-9), ~620h, ~232 submódulos. Progresso rastreado em PROGRESS.md.

## Estrutura

- `notes/*.csv` — flashcards `pergunta;resposta` (separador `;`, sem header)
- `notes/REGRAS_FLASHCARDS.md` — **fonte canônica** de todas as regras de criação de cards (LER ANTES de criar/editar CSVs)
- `00-09/` — diretórios de módulos, cada um com `README.md` e `projects/`
- `PROGRESS.md` — checklist mestre com tabelas `| # | Módulo | Tempo | Status |`
- `GUIA_DE_ESTUDO.md` — metodologia com modos de absorção
- `progress.py` — atualiza badges no README

## Numeração

- Formato: `pilar.submódulo` — ex: `0.05` = Módulo 0, submódulo 5
- Pilares: 0=Mat/Fís, 1=Circuitos, 2=Eletrônica, 3=Digital, 4=Potência, 5=Controle/DSP, 6=Lab, 7=HH Básico, 8=HH Avançado, 9=CPTS

## Convenções dos CSVs

- Formato: `pergunta;resposta` (ponto-e-vírgula como separador)
- Sem header, sem aspas desnecessárias
- Fórmulas em mathjax para anki, ex: \(\cdot\)
- Nome do arquivo: `módulo.submódulo-tema_snake_case.csv`
- **Regras completas**: ver `notes/REGRAS_FLASHCARDS.md` (não duplicar aqui)

## Modos de absorção (usados em GUIA_DE_ESTUDO.md e READMEs)

- ❄️ Cold Start OK — cards primeiro
- 🔶 Projeto Ponte — projeto ANTES dos cards
- 📚 Estudo Prévio — vídeo/livro antes
- 🔴 Material Externo Obrigatório

## Template de README de módulo

Cada `XX-nome/README.md` segue: título → blockquote (Sobre, Ferramentas, Pré-requisitos, Conexões) → Visão Geral (tabela de fases) → Prontidão por Módulo → Fases com submódulos (O que memorizar, Intuição, Projeto, Erros Comuns) → Se Você Travar.

## Comandos úteis

- `python3 progress.py` — atualiza badges de progresso no README

## Instruções para o Claude

- **Eficiência de tokens é prioridade.** Plano Pro com cota limitada.
- Use subagentes `haiku` para leitura/exploração de código.
- Use subagentes `sonnet` para review e planejamento leve.
- Reserve Opus para planejamento pesado e decisões arquiteturais.
- Não releia arquivos já lidos na conversa.
- Respostas curtas e diretas. Sem resumos desnecessários.
- Ao criar/editar CSVs: **ler `notes/REGRAS_FLASHCARDS.md` primeiro**.
- Conteúdo técnico — usar notação padrão de engenharia (PT-BR).
