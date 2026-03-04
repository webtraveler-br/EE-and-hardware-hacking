# Regras de Criação de Flashcards RemNote para EE

Guia para manter as notas sustentáveis e eficientes ao criar novos módulos.

---

## 1. Estrutura de Arquivo

- **Um arquivo por fase** do roadmap (ex: `fase5_equacoes_diferenciais.md`)
- Cabeçalho padrão no início de cada arquivo explicando as camadas:

```
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).
```

---

## 2. As 3 Camadas

### Core (permanente) — ~60% dos cards

Cards que você precisa lembrar PARA SEMPRE. Critérios:

- **Fórmulas fundamentais** usadas em múltiplos módulos (V=IR, I=CdV/dt, etc.)
- **Definições** de conceitos (o que é derivada, integral, etc.)
- **Unidades e conversões** (prefixos SI, unidades derivadas)
- **Identidades** (trig, Euler, propriedades de log)
- **Classificações** (tipos de amortecimento, estabilidade, tipos de função)
- **Conexões conceituais** ("capacitor ↔ 1ª eq. Maxwell")

### Drill (temporário) — ~25% dos cards

Cards de **prática repetitiva** que devem ser suspensos após domínio. Critérios:

- **Exercícios numéricos** (calcule esta derivada, converta esta unidade)
- **Verificações dimensionais extras** (além das fundamentais)
- **Problemas de dB**, conversões grau/radiano
- **Integrais definidas** com valores específicos

**Regra**: se um drill contém um **conceito ou insight** importante, promova para Core.

### Texto sem marcação (~15% do conteúdo)

Notas que ficam organizadas no RemNote mas **NÃO geram review**:

- **Intuições e analogias** (RLC = massa-mola, E=-∇V = bola no morro)
- **Explicações do "por quê"**
- **Mnemônicos** (ELI the ICE man, LIATE, √n/2)
- **Contexto histórico ou curiosidades**
- **Avisos de erros comuns**

---

## 3. Formato dos Cards

### Card Forward (`>>`)
```
Pergunta clara e curta >> {{Resposta curta}}
```
- Pergunta deve ser **específica** — evite "O que é X?" genéricos
- Resposta em **uma linha**, máximo ~15 palavras no cloze
- Use `{{}}` (cloze) dentro do `>>` para a parte essencial

### Card Two-Way (`<>`)
```
Conceito A <> {{Conceito B}}
```
- Usar para **equivalências bidirecionais**: unidades (Ω <> V/A), valores (sin 30° <> 1/2), tabela dB
- NÃO usar para perguntas conceituais (essas são forward `>>`)

### Evitar
-  Múltiplos `{{}}` na mesma linha gera múltiplos cloze cards independentes — só faça isso intencionalmente
-  Respostas com mais de 2 linhas — quebre em cards separados
-  Cards do tipo "liste 5 propriedades de X" — cada propriedade vira um card separado

---

## 4. Meta de Cards por Módulo

| Camada | Cards/módulo | Revisões/dia geradas |
|--------|-------------|---------------------|
| Core | 10-15 | ~0.5/card em regime |
| Drill | 5-10 | 0 após suspender |
| Texto | Livre | 0 |
| **Total** | **15-25** | **~8-10/módulo** |

**Meta global**: ~500 cards core para o roadmap inteiro (~36 módulos) → ~15-20 reviews/dia

---

## 5. Critérios de Promoção e Rebaixamento

### Drill → Core (promover)
- O drill revela um **conceito importante** (ex: ∫₀^∞ e^(-t/τ) dt = τ tem significado de "constante de tempo total")
- O drill é uma **fórmula que aparece em outros módulos**
- Você **erra o drill repetidamente** — sinal de que é mais importante do que parecia

### Core → Texto (rebaixar)
- Card core que você **acerta sem pensar por 3+ meses** — o conhecimento já está internalizado
- Card que se tornou **redundante** porque outro card cobre o mesmo conceito melhor

---

## 6. Checklist Antes de Finalizar um Arquivo

- [ ] Cada seção tem pelo menos 1 linha de texto sem card (contexto/intuição)
- [ ] Core não tem exercícios numéricos puros (esses são Drill)
- [ ] Drills não contêm conceitos essenciais (se contêm, promova para )
- [ ] Nenhum card tem resposta maior que ~15 palavras no cloze
- [ ] Cards two-way `<>` são usados apenas para equivalências bidirecionais
- [ ] Total de cards core ≤ 15 por módulo
- [ ] Mnemônicos e analogias estão como texto livre, não como flashcard

---

## 7. Rotina de Manutenção

- **Ao estudar uma fase nova**: importe o arquivo no RemNote
- **Após 1 semana**: suspenda os Drills que você acerta consistentemente
- **Após 1 mês**: revise se algum Core deve ser rebaixado a texto
- **Ao criar módulos novos**: releia este documento antes de começar
