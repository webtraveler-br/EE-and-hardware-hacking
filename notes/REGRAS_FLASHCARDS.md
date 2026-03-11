# Regras de Criação de Flashcards — CSV (Anki/Obsidian)

Regras abstraídas de 5 sessões de revisão e otimização de decks.

---

## 1. Formato de Arquivo

- **CSV**, separador `;` (ponto-e-vírgula)
- Cada linha: `pergunta;resposta` — sem cabeçalho, sem aspas
- Notação matemática: LaTeX envolto em `\(\)` — ex: `\(V = IR\)`
- Texto descritivo: sem LaTeX, em português
- Sem linhas em branco, sem comentários, sem metadados

---

## 2. Direção do Card (Anti-Padrão #2)

A pergunta deve refletir o **fluxo natural de reconhecimento em prova/trabalho**.

### Fórmulas e Equações
- **Correto**: `Nome/Símbolo (Contexto) → Fórmula`
  - Ex: `\(V_{th}\) (Tensão de Thévenin) → \(V_{oc}\)`
- **Errado**: `Fórmula → Nome` (recognition passivo, não testa recall)
  - Exceção: equações de superfícies quádricas (`equação → nome`) — a prova pede reconhecimento visual

### Unidades e Equivalências
- **Correto**: `Unidade [símbolo] → Decomposição em unidades derivadas úteis`
  - Ex: `Farad [F] → C/V = A·s/V`
- **Errado**: decomposição puramente em unidades base SI (`kg·m²·s⁻⁴·A²`) — inútil para recall prático (Anti-Padrão #1: "sopa de unidades base")

### Identidades Matemáticas
- Direção deve ser `compacto → expandido` para identidades de expansão
- Direção deve ser `expandido → compacto` quando o padrão útil é **reconhecer** a forma simplificada
  - Ex: `\(a^n b^n → (ab)^n\)` — o útil é reconhecer que o produto de potências colapsa

### Regra de ouro
> Se na prova você vê X e precisa lembrar Y → o card é `X → Y`

---

## 3. Anti-Padrão #1: Sopa de Unidades Base

- **Nunca** decompor unidades até `kg·m·s·A` exceto quando a relação é diretamente F=ma.
- Sempre usar **unidades derivadas** que tenham significado prático:
  - `Ω = V/A` (vem de V = IR)
  - `F = C/V` (vem de Q = CV)
  - `H = V·s/A` (vem de V = L·dI/dt)
  - `W = V·A = J/s`
- Provas dimensionais devem verificar via **cadeia de unidades derivadas**, não base.

---

## 4. Anti-Padrão #3: Rote sem Ganho de Velocidade

- Não memorizar resultados que são trivialmente deriváveis.
- **Exceção**: manter se a memorização direta **economiza tempo em prova**:
  - Valores do círculo unitário (0.03) → manter
  - Integrais tabeladas básicas (0.05) → manter
  - Tabela de Laplace direta (0.09) → manter
  - Carga/descarga RC (0.14) → manter
- **Remover/reformular** se o resultado é facilmente derivável E raramente necessário sob pressão de tempo.

---

## 5. Concisão

### Pergunta
- Máximo ~20 palavras de texto visível (tokens LaTeX como `\frac{}{}` não contam)
- Usar símbolo + contexto mínimo entre parênteses: `\(X_C\) (Reatância Capacitiva)`
- Sem artigos ou verbos desnecessários: ~~"Qual é o valor de"~~ → direto ao símbolo
- Para Tipo B, preferir `Nome (contexto)` sobre `O que é Nome?`

### Resposta
- Fórmula pura, sem prosa explicativa
- Sem adjetivos decorativos: ~~"dramaticamente"~~, ~~"fundamentalíssimo"~~
- Se a resposta precisa de contexto, ele vai **na pergunta**
- Máximo 1 linha (≤150 caracteres). Se precisa de mais, quebrar em cards separados
- **Tipo D (Procedimento):** até 200 caracteres. Se o procedimento tem >3 passos, quebrar em 2 cards

---

## 6. Linguagem e Tom

- Português técnico formal (PT-BR)
- Sem linguagem informal, metáforas coloquiais ou humor: ~~"Água de Salsicha"~~
- Sem redundância entre pergunta e resposta
- Unidades e grandezas em notação padrão SI
- Termos em inglês entre parênteses quando são jargão universal: `(Skin Effect)`, `(Duty Cycle)`

---

## 7. Estrutura por Tipo de Card

### Tipo A: Fórmula/Definição (maioria)
```
\(Símbolo \ (\text{Nome/Contexto})\);\(Fórmula\)
```

### Tipo B: Conceito/Definição textual
```
Pergunta direta sobre o conceito;Resposta em 1 frase, sem \(\)
```

### Tipo C: Intuição/Ponte (bridge files)
```
Pergunta "por quê" ou "qual conexão";Resposta em 1 frase com insight prático
```

### Tipo D: Procedimento
```
\(\text{Procedimento: Nome do Método}\);Passos numerados concisos (≤200 chars)
```
> Se o procedimento tem >3 passos, quebrar em 2 cards: passos-chave + detalhes

### Tipo E: Cálculo Rápido (*Quick Calc*)
```
Dados numéricos concretos → Valor computado
```
- Valores "redondos" que exercitem o padrão, não arbitrários
- Fórmula subjacente já existe como Tipo A — Tipo E testa fluência de aplicação
- Resposta: valor + unidade (≤50 chars)

### Tipo F: Diagnóstico / Seleção de Método
```
Situação ou sintoma → Método/classificação correta
```
- Sem cálculo — testa decisão
- Resposta ≤100 chars

### Tipo G: Próximo Passo (*What comes next?*)
```
Contexto + estado atual → Próxima ação do procedimento
```
- Encadear 2–4 cards G cobre um procedimento sem Tipo D monolítico
- Redundância intencional com Tipo D (Wozniak R17: derivation steps)
- Resposta ≤100 chars

### Tipo H: Armadilha / Erro Comum (*Trap Card*)
```
Armadilha: situação confusa → Resposta correta + motivo do erro
```
- Prefixo "Armadilha:" obrigatório na pergunta
- Resposta ≤150 chars

---

## 8. Cobertura e Sobreposição

- **Sem duplicação** entre arquivos. Se um conceito aparece em 0.14 (física) e 1.01 (circuitos), deve estar em **apenas um** e com o nível de detalhe adequado ao contexto.
  - Kirchhoff em 0.14: versão básica (nome → equação simples)
  - Kirchhoff em 1.01: versão completa com procedimentos de análise nodal/malhas
- **Bridge files** (`*_na_engenharia.csv`) conectam teoria pura ao uso em EE — não repetem fórmulas, explicam *por que* e *onde* se usam.
- Cada deck deve ser **autocontido** para seu nível — não depender de outro deck para fazer sentido.
- **Exceção cross-perspectiva:** mesmo protocolo (ex: UART, SPI, I2C) pode ter cards em 2 módulos se a perspectiva é distinta (engenharia vs segurança/hacking).

---

## 9. Conversão de RemNote → CSV

Ao converter cards do formato RemNote (`>>`, `<>`, `{{}}`) para CSV:

| RemNote | CSV |
|---------|-----|
| `Pergunta >> {{Resposta}}` | `Pergunta;Resposta` |
| `A <> {{B}}` | Escolher a direção mais útil (ver Regra 2) |
| `#[[Drill]]` seções | **Avaliar** — drills de lista (específicos) descartados. Drills de padrão (Tipos E–H) podem ser incluídos |
| Texto sem marcação | **Descartar** — narrativas/mnemônicos não geram cards |
| `{{múltiplos clozes}}` numa linha | **Quebrar** em cards separados |
| Markdown (`*`, `#`, etc.) | **Remover** — CSV é puro |

### Matemática
- `$...$` RemNote → `\(...\)` CSV (inline LaTeX)
- Cifrão fora de LaTeX: usar `\$` (ex: `R\$60`, `US\$50`)
- Subscritos/sobrescritos: manter notação LaTeX
- Símbolos Unicode (→, ≈, ≥) são aceitos no CSV quando não há equivalente LaTeX simples

---

## 10. Critérios de Inclusão

Um card entra no deck permanente se:

1. **É uma fórmula fundamental** usada em múltiplos contextos
2. **É uma definição** que precisa de recall preciso
3. **É uma conexão** entre domínios que seria esquecida
4. **É um procedimento** com passos que erros de sequência causam falha
5. **Economiza tempo em prova** por evitar derivação
6. **É um drill de padrão** (Tipos E/F/G/H) que exercita fluência com valores genéricos

Um card **NÃO** entra se:

1. É um exercício numérico de lista de livro (enunciado longo, específico de uma figura)
2. É derivável trivialmente de outro card
3. É uma narrativa ou explicação longa (>1 frase)
4. É um mnemônico (fica como texto de referência, não card)
5. É contexto histórico ou curiosidade

---

## 11. Validação Final (Checklist)

- [ ] Cada linha tem exatamente 1 `;`
- [ ] LaTeX abre `\(` e fecha `\)` em pares
- [ ] Sem linhas em branco
- [ ] Direção do card está correta (Regra 2)
- [ ] Sem sopa de unidades base (Regra 3)
- [ ] Respostas têm ≤1 frase
- [ ] Sem duplicação com outros CSVs
- [ ] Sem linguagem informal
- [ ] Sem adjetivos decorativos nas respostas
