# Flashcards - EE & Hardware Hacking

Cofre Obsidian com o plugin Sprout (FSRS) para manutencao de conhecimento.

## Como funciona o _staging

O Sprout le todos os arquivos `.md` do cofre e detecta cards automaticamente (qualquer bloco com `?` como separador). Isso significa que **cards em `_staging/` ja aparecem no Sprout**.

O fluxo e o seguinte:

1. Termino um modulo
2. Abro o arquivo correspondente em `_staging/` (ex: `0.1 - Unidades SI.md`)
3. Leio os cards, deleto os que nao fazem sentido, edito os que quiser
4. Movo o arquivo para a pasta do pilar (ex: `Pilar 0 - Matematica/`)
5. Cards que ja revisei no Sprout mantem o historico de revisao mesmo depois de mover

Se quiser impedir o Sprout de ler um arquivo de staging antes de revisar, renomeie a extensao para `.txt` temporariamente. Quando estiver pronto, renomeie de volta para `.md`.

Na pratica, nao precisa se preocupar com isso — se um card de staging aparecer numa sessao de revisao, e so mais uma chance de revisar antes de mover.

## Estrutura

```
_staging/            ← cards novos, gerados por modulo
Pilar 0 - Matematica/← cards aprovados e em uso
Pilar 1 - Circuitos/
...
```

## Formato dos cards

Frente e verso separados por `?` numa linha sozinha:

```
Pergunta aqui
?
Resposta aqui
```

Math com LaTeX: `$V = IR$` inline ou `$$\int f(x)\,dx$$` em bloco.
