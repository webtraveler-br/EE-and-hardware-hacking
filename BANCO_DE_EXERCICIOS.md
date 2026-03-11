# Banco de Exercícios — Prática Externa ao Anki

> Criado em: março 2026
> Complemento de: ANALISE_DRILLS.md
> Propósito: exercícios para a parcela de competência que o Anki **não cobre** (o "residual" de 45%)

---

## 0. Repetir ou Alternar? O Que a Pesquisa Diz

### Resposta curta

**Alternar é muito superior a repetir o mesmo exercício em bloco.** Mas revisitar o mesmo exercício depois de semanas (quando você já esqueceu a solução) também funciona.

### O que a pesquisa mostra

| Estudo | Descoberta |
|--------|-----------|
| **Rohrer & Taylor, 2007** | Alunos que alternaram tipos de problema em matemática tiveram **43% mais acerto** num teste uma semana depois, comparados com quem praticou em bloco |
| **Carpenter et al., 2012** | Interleaving (alternar tipos) supera blocked practice (um tipo por vez) para retenção a longo prazo |
| **Bjork & Bjork, 1992** | "Desirable difficulties" — praticar de forma mais difícil (alternando, espaçando) gera memória mais durável |
| **Kornell & Bjork, 2008** | Mesmo para tarefas perceptuais (classificação de arte), interleaving superou blocked practice |
| **Rohrer, Dedrick & Stershic, 2015** | Em sala de aula real (7ª série, matemática), interleaving melhorou desempenho em 25% vs. prática em bloco |

### Por que repetir o mesmo exercício falha

Quando você repete o mesmo exercício logo depois:
1. Você **reconhece** a solução em vez de **gerar** — o que ativa memória de curto prazo, não de longo prazo
2. Você não pratica a **decisão de método** — não precisa decidir "que ferramenta usar?" porque já sabe qual é
3. A **fluência ilusória** (illusion of learning) engana: parece fácil → você acha que aprendeu → mas no teste, com problemas misturados, trava

### O modelo ideal

```
Semana 1: Problema A1(circuitos) → B1(probabilidade) → C1(controle)
Semana 2: Problema A2(circuitos) → B2(probabilidade) → C2(controle) → revisitar A1 se errou
Semana 3: A3 → B3 → D1(cálculo) → revisitar B1 e C1
...
```

**Regras:**
1. **Nunca mais de 3 exercícios do mesmo tipo seguidos** — alterne módulos
2. **Revisitar exercício antigo é permitido** se: (a) passou ≥2 semanas, (b) você tenta re-resolver do zero (não olha a solução), (c) anota onde travou
3. **Novos exercícios sempre preferíveis** — use este banco de forma sequencial, não repetitiva
4. **Quando acabar o banco:** resolva dos livros indicados na seção de Recursos, ou gere variações (trocar valores, trocar método pedido)

### Quanto tempo por sessão?

| Objetivo | Duração | Frequência |
|----------|:-------:|:----------:|
| Manutenção (durante Anki) | 30–45 min | 3×/semana |
| Preparação para prova | 60–90 min | 5×/semana (2 semanas antes) |
| Estudo intensivo de módulo novo | 45–60 min | Diário (1 semana) |

---

## 1. Como Usar Este Banco

### Formato de cada exercício

```
### Ex [módulo].[número] — [Tópico] ★☆☆

**Problema:** enunciado completo, auto-contido.

**Resposta:** solução + passos-chave.

**Fonte:** referência (se baseado em livro/recurso externo).
```

### Dificuldade

- ★☆☆ = Aplicação direta de fórmula (1–3 min)
- ★★☆ = Requer decisão de método ou 2–3 passos (5–10 min)
- ★★★ = Problema integrativo, múltiplos conceitos (15–30 min)

### Quando um exercício precisa de diagrama

Circuitos são descritos textualmente com a notação:
- `—` = série
- `∥` = paralelo
- `[ ]` = agrupamento

Quando a complexidade visual exige um diagrama real, indico:
- 📐 **Falstad** (https://falstad.com/circuit/) — simulador gratuito online, montar e simular
- 📘 **Livro + página** — referência específica para quem tem o livro
- 🔗 **Link externo** — recurso online gratuito com o diagrama

---

## 2. Exercícios por Módulo

---

### 🔴 PRIORIDADE #1 — Circuitos (1.01)

> **Meta:** 20 exercícios. Residual = 40%. Foco: análise completa com múltiplos métodos.
> **Recurso com diagramas:** Sadiku — *Fundamentals of Electric Circuits* (6ª ed., Cap. 2–10) ou Falstad.

#### Nível ★☆☆ — Aplicação Direta

**Ex 1.01** — Resistores mistos ★☆☆

**Problema:** Circuito: $V_s = 30$ V em série com $R_1 = 10\,\Omega$, conectado a ($R_2 = 20\,\Omega \parallel R_3 = 20\,\Omega$). Ache a corrente total e a potência dissipada em $R_1$.

**Resposta:** $R_{23} = 10\,\Omega$. $R_{total} = 20\,\Omega$. $I = 30/20 = 1.5$ A. $P_{R1} = 1.5^2 \times 10 = 22.5$ W.

---

**Ex 1.02** — Divisor de tensão com 3 resistores ★☆☆

**Problema:** $V_s = 12$ V em série com $R_1 = 1\,\text{k}\Omega$, $R_2 = 2\,\text{k}\Omega$, $R_3 = 3\,\text{k}\Omega$. Ache $V_{R_2}$.

**Resposta:** $V_{R_2} = 12 \times \frac{2}{1+2+3} = 12 \times \frac{2}{6} = 4$ V.

---

**Ex 1.03** — Capacitor em DC ★☆☆

**Problema:** Circuito DC estacionário: $V_s = 10$ V em série com $R_1 = 2\,\text{k}\Omega$, em série com ($C = 100\,\mu$F $\parallel$ $R_2 = 3\,\text{k}\Omega$). Ache $V_C$ em regime permanente.

**Resposta:** Em DC, $C$ = aberto. Toda corrente flui por $R_1 — R_2$. $I = 10/(2k+3k) = 2$ mA. $V_C = V_{R_2} = 2 \times 10^{-3} \times 3 \times 10^3 = 6$ V.

---

**Ex 1.04** — Impedância AC ★☆☆

**Problema:** $R = 100\,\Omega$ em série com $L = 50$ mH. Frequência $f = 1$ kHz. Ache $|\mathbf{Z}|$ e o ângulo da impedância.

**Resposta:** $X_L = 2\pi \times 1000 \times 0.05 = 314.2\,\Omega$. $|\mathbf{Z}| = \sqrt{100^2 + 314.2^2} = 329.7\,\Omega$. $\theta = \arctan(314.2/100) = 72.3°$.

---

#### Nível ★★☆ — Decisão de Método + Passos

**Ex 1.05** — Thévenin completo ★★☆

**Problema:** Circuito: $V_s = 24$ V em série com $R_1 = 6\,\Omega$. Do nó entre $V_s/R_1$, saem dois ramos em paralelo: $R_2 = 12\,\Omega$ e $R_3 = 4\,\Omega$. Terminais de saída: nos extremos de $R_3$. Ache o equivalente de Thévenin e a corrente numa carga $R_L = 6\,\Omega$.

**Resposta:**
1. $V_{oc}$: Sem $R_L$, $R_3$ fica em paralelo com circuito aberto → só $R_2$ conduz. $I = 24/(6+12) = 4/3$ A. $V_{R_2} = 12 \times 4/3 = 16$ V. Mas $V_{oc}$ nos terminais de $R_3$ = $V_{R_2} = 16$ V.
2. $R_{th}$: Desligar $V_s$ (curto). $R_{th} = R_3 \parallel \text{nada}$... Espera — $R_3$ está entre os terminais, logo removemos $R_3$ para Thévenin. Vista dos terminais: $R_1 \parallel R_2 = 6 \times 12/18 = 4\,\Omega$. $R_{th} = 4\,\Omega$.
3. $V_{oc}$ (recalculando sem $R_3$): divisor de tensão — $V_{oc} = 24 \times 12/(6+12) = 16$ V. ✓
4. $I_L = V_{th}/(R_{th}+R_L) = 16/(4+6) = 1.6$ A.

---

**Ex 1.06** — Superposição ★★☆

**Problema:** Circuito com duas fontes. $V_1 = 10$ V em série com $R_1 = 5\,\Omega$, conectado ao nó A. Do nó A, sai $R_2 = 10\,\Omega$ para o nó B. Do nó B, $V_2 = 20$ V (positivo em B) em série com $R_3 = 10\,\Omega$ de volta ao terra. Ache $V_A$ usando superposição.

**Resposta:**
- Fonte $V_1$ sozinha ($V_2 = 0$, curto): $R_2$ e $R_3$ em série = $20\,\Omega$. $V_A' = 10 \times 20/(5+20) = 8$ V.
- Fonte $V_2$ sozinha ($V_1 = 0$, curto): $R_1$ e $R_2$ em série = $15\,\Omega$. Do nó B: $V_B = 20 \times 15/(10+15) = 12$ V. $V_A'' = V_B \times R_1/(R_1+R_2)$... Não, pensemos: $V_A'' = V_B \times R_1/(R_1+R_2)$? Não — $V_A$ é nó central. Divisor: $V_A'' = 20 \times R_1/(R_1+R_2+R_3)$... O circuito: com $V_1$ curtado, de A para terra vai $R_1$. De A vai $R_2$ para B, de B vai $R_3$ em série com $V_2$ para terra. Fonte $V_2$ em série com $R_3$ e depois $R_2 + R_1$ em série para terra pelo outro lado. $I = 20/(R_3+R_2+R_1) = 20/25 = 0.8$ A. $V_A'' = I \times R_1 = 0.8 \times 5 = 4$ V.
- $V_A = V_A' + V_A'' = 8 + 4 = 12$ V.

---

**Ex 1.07** — Análise Nodal com 2 nós ★★☆

**Problema:** Nó A e nó B, terra como referência. Do terra para A: fonte $I_s = 3$ A (sobe). De A para terra: $R_1 = 6\,\Omega$. De A para B: $R_2 = 3\,\Omega$. De B para terra: $R_3 = 6\,\Omega$ e fonte $V_s = 12$ V (positivo em B). Ache $V_A$.

**Resposta:**
- Nó B tem fonte de tensão para terra: $V_B = 12$ V (diretamente).
- LKC no nó A: $\frac{V_A}{6} + \frac{V_A - 12}{3} = 3$
- $\frac{V_A}{6} + \frac{V_A - 12}{3} = 3 \implies \frac{V_A + 2V_A - 24}{6} = 3 \implies 3V_A - 24 = 18 \implies V_A = 14$ V.

---

**Ex 1.08** — Transitório RC ★★☆

**Problema:** Circuito RC: $V_s = 20$ V, $R = 5\,\text{k}\Omega$, $C = 2\,\mu$F. Capacitor inicialmente descarregado. Em $t = 0$, chave fecha. Ache $V_C(t)$ e calcule $V_C$ em $t = 10$ ms.

**Resposta:** $\tau = RC = 5 \times 10^3 \times 2 \times 10^{-6} = 10$ ms. $V_C(t) = 20(1 - e^{-t/10\text{ms}})$. Em $t = 10$ ms: $V_C = 20(1 - e^{-1}) = 20 \times 0.632 = 12.64$ V.

---

**Ex 1.09** — Potência AC complexa ★★☆

**Problema:** Carga com $\mathbf{Z} = 3 + j4\,\Omega$ alimentada por $V_{rms} = 100$ V. Ache $P$, $Q$, $S$, e o fator de potência.

**Resposta:** $|\mathbf{Z}| = 5\,\Omega$. $I_{rms} = 100/5 = 20$ A. $\theta = \arctan(4/3) = 53.13°$. $S = V_{rms} \times I_{rms} = 2000$ VA. $P = S\cos\theta = 2000 \times 0.6 = 1200$ W. $Q = S\sin\theta = 2000 \times 0.8 = 1600$ VAR (indutivo). $fp = 0.6$ em atraso.

---

**Ex 1.10** — Correção de fator de potência ★★☆

**Problema:** Carga industrial: $P = 10$ kW, fp = 0.7 (indutivo), alimentada em 220 V rms, 60 Hz. Ache o capacitor necessário para corrigir para fp = 0.95.

**Resposta:** $\theta_{old} = \arccos(0.7) = 45.57°$. $Q_{old} = P\tan\theta_{old} = 10000 \times 1.02 = 10200$ VAR. $\theta_{new} = \arccos(0.95) = 18.19°$. $Q_{new} = 10000 \times \tan 18.19° = 3287$ VAR. $Q_C = 10200 - 3287 = 6913$ VAR. $C = Q_C / (\omega V^2) = 6913 / (2\pi \times 60 \times 220^2) = 6913 / 18241088 = 379\,\mu$F.

---

#### Nível ★★★ — Integrativos

**Ex 1.11** — Thévenin com fonte dependente ★★★

**Problema:** Circuito: fonte $V_s = 10$ V em série com $R_1 = 2\,\Omega$, nó A. De A para terra: $R_2 = 4\,\Omega$. De A para terminais de saída (nó B): fonte dependente VCCS controlada por $V_{R_2}$, com $g_m = 0.5$ S. Em paralelo com essa fonte dependente: $R_3 = 4\,\Omega$. Terminais: B e terra. Ache $V_{th}$ e $R_{th}$.

**Resposta:**
1. $V_{oc}$: Com terminais abertos, a corrente da VCCS flui por $R_3$. Primeiro achar $V_A$: nó A → LKC: $(V_A - 10)/2 + V_A/4 + g_m V_A = 0$ (se $V_{R_2} = V_A$). $V_A/2 - 5 + V_A/4 + 0.5V_A = 0 \implies V_A(0.5 + 0.25 + 0.5) = 5 \implies 1.25 V_A = 5 \implies V_A = 4$ V. $V_{oc} = V_B = g_m V_A \times R_3 = 0.5 \times 4 \times 4 = 8$ V.
2. $I_{sc}$: Curto nos terminais. Agora $R_3$ está em curto. $V_A$: mesma equação mas VCCS vai para curto → $V_A/2 - 5 + V_A/4 = 0$ (sem $g_m$ no nó A — a corrente da VCCS vai para o curto). $0.75V_A = 5 \implies V_A = 6.67$ V. $I_{sc} = g_m V_A = 0.5 \times 6.67 = 3.33$ A.
3. $R_{th} = V_{oc}/I_{sc} = 8/3.33 = 2.4\,\Omega$.

---

**Ex 1.12** — Análise de malhas com 3 malhas ★★★

**Problema:** Malha 1: $V_s = 36$ V, $R_1 = 6\,\Omega$, $R_2 = 3\,\Omega$ (compartilhado com malha 2). Malha 2: $R_2 = 3\,\Omega$ (compart. M1), $R_3 = 6\,\Omega$, $R_4 = 3\,\Omega$ (compart. M3). Malha 3: $R_4 = 3\,\Omega$ (compart. M2), $R_5 = 6\,\Omega$. Correntes de malha $I_1, I_2, I_3$ no sentido horário. Ache as três correntes.

**Resposta:**
- M1: $36 = 6I_1 + 3(I_1 - I_2) = 9I_1 - 3I_2$
- M2: $0 = 3(I_2 - I_1) + 6I_2 + 3(I_2 - I_3) = -3I_1 + 12I_2 - 3I_3$
- M3: $0 = 3(I_3 - I_2) + 6I_3 = -3I_2 + 9I_3$

De M3: $I_2 = 3I_3$. Substituindo em M2: $-3I_1 + 36I_3 - 3I_3 = 0 \implies I_1 = 11I_3$. Em M1: $9(11I_3) - 3(3I_3) = 36 \implies 99I_3 - 9I_3 = 36 \implies I_3 = 0.4$ A. $I_2 = 1.2$ A. $I_1 = 4.4$ A.

---

**Ex 1.13** — RLC série transitório completo ★★★

**Problema:** RLC série: $R = 4\,\Omega$, $L = 1$ H, $C = 1/4$ F. Capacitor com $V_C(0) = 10$ V, $I_L(0) = 0$. Sem fonte externa. Determine o tipo de resposta e escreva $V_C(t)$.

**Resposta:** $\alpha = R/(2L) = 2$ rad/s. $\omega_0 = 1/\sqrt{LC} = 1/\sqrt{1 \times 0.25} = 2$ rad/s. $\alpha = \omega_0$ → **criticamente amortecido**. $V_C(t) = (A_1 + A_2 t)e^{-2t}$. Condições: $V_C(0) = 10 \implies A_1 = 10$. $dV_C/dt|_0 = I_C(0)/C = I_L(0)/C = 0/0.25 = 0$. $dV_C/dt = (A_2 - 2A_1 - 2A_2 t)e^{-2t}$. Em $t=0$: $A_2 - 20 = 0 \implies A_2 = 20$. $V_C(t) = (10 + 20t)e^{-2t}$ V.

---

**Ex 1.14** — Circuito AC com duas fontes (fasorial) ★★★

**Problema:** Fonte $\tilde{V}_1 = 120\angle 0°$ V em série com $R_1 = 10\,\Omega$, nó A. De A para terra: $\mathbf{Z}_C = -j20\,\Omega$. De A: $R_2 = 10\,\Omega$ em série com fonte $\tilde{V}_2 = 120\angle 90°$ V para terra. Ache a corrente por $\mathbf{Z}_C$ usando análise nodal fasorial.

**Resposta:**
LKC em A: $\frac{\tilde{V}_A - 120\angle 0°}{10} + \frac{\tilde{V}_A}{-j20} + \frac{\tilde{V}_A - 120\angle 90°}{10} = 0$

$\tilde{V}_A\left(\frac{1}{10} + \frac{1}{-j20} + \frac{1}{10}\right) = \frac{120}{10} + \frac{120\angle 90°}{10}$

$\tilde{V}_A(0.2 + j0.05) = 12 + 12j$

$\tilde{V}_A = \frac{12 + 12j}{0.2 + j0.05} = \frac{(12+12j)(0.2-j0.05)}{0.04+0.0025} = \frac{2.4 - j0.6 + j2.4 + 0.6}{0.0425} = \frac{3 + j1.8}{0.0425}$

$\tilde{V}_A = 70.59 + j42.35 = 82.33\angle 30.96°$ V.

$\tilde{I}_C = \tilde{V}_A / (-j20) = 82.33\angle 30.96° / 20\angle -90° = 4.12\angle 120.96°$ A.

---

> 📐 **Mais exercícios de circuitos:** Montar no Falstad os circuitos acima e verificar. Depois, resolver os exercícios de fim de capítulo de **Sadiku Cap. 4 (Thévenin)**, **Cap. 3 (Nodal/Malhas)**, **Cap. 7 (1ª ordem)**, **Cap. 8 (2ª ordem)**, **Cap. 10 (AC)**. Meta: **3 exercícios por capítulo** → 15 adicionais.

---

### 🔴 PRIORIDADE #2 — Probabilidade e Estatística (0.17)

> **Meta:** 15 exercícios. Residual = 40%. Foco: modelagem + cálculo + armadilhas.
> **Sem imagens necessárias** — tudo textual.

#### Nível ★☆☆

**Ex 0.17.01** — Binomial direto ★☆☆

**Problema:** $X \sim \text{Bin}(8, 0.25)$. Calcule $P(X = 2)$.

**Resposta:** $P = \binom{8}{2}(0.25)^2(0.75)^6 = 28 \times 0.0625 \times 0.1780 = 0.3115$.

---

**Ex 0.17.02** — Poisson direto ★☆☆

**Problema:** Uma central recebe $\lambda = 4$ chamadas/hora. $P$(exatamente 2 chamadas numa hora)?

**Resposta:** $P(X=2) = e^{-4} \times 4^2 / 2! = 0.0183 \times 16 / 2 = 0.1465$.

---

**Ex 0.17.03** — Normal padrão ★☆☆

**Problema:** $X \sim N(100, 15^2)$. Ache $P(X > 130)$.

**Resposta:** $Z = (130-100)/15 = 2.0$. $P(Z > 2) = 1 - 0.9772 = 0.0228 \approx 2.3\%$.

---

#### Nível ★★☆

**Ex 0.17.04** — Qual distribuição usar? ★★☆

**Problema:** Um lote tem 5% de defeituosos. Inspeção de 20 peças. Qual distribuição modela o nº de defeituosos? Se peças forem retiradas sem reposição de um lote de 50, e qual distribuição?

**Resposta:** Com reposição (ou lote infinito): **Binomial** $\text{Bin}(20, 0.05)$. Sem reposição de lote pequeno: **Hipergeométrica** (depende do tamanho do lote e nº de defeituosos).

---

**Ex 0.17.05** — Bayes clássico ★★☆

**Problema:** Teste médico: sensibilidade = 99% (P(+|doente) = 0.99), especificidade = 95% (P(−|saudável) = 0.95). Prevalência da doença = 1%. Qual $P$(doente | +)?

**Resposta:** $P(+) = P(+|D)P(D) + P(+|\bar{D})P(\bar{D}) = 0.99 \times 0.01 + 0.05 \times 0.99 = 0.0099 + 0.0495 = 0.0594$. $P(D|+) = 0.0099/0.0594 = 16.7\%$. (Resultado surpreendente — mesmo com teste bom, prevalência baixa → maioria dos positivos é falso positivo.)

---

**Ex 0.17.06** — Variância de combinação linear ★★☆

**Problema:** $X$ e $Y$ independentes, $E(X) = 5$, $\text{Var}(X) = 4$, $E(Y) = 3$, $\text{Var}(Y) = 9$. Ache $E(2X - 3Y + 7)$ e $\text{Var}(2X - 3Y + 7)$.

**Resposta:** $E = 2(5) - 3(3) + 7 = 10 - 9 + 7 = 8$. $\text{Var} = 2^2(4) + (-3)^2(9) + 0 = 16 + 81 = 97$. (Constante não afeta variância; coeficientes saem ao quadrado.)

---

**Ex 0.17.07** — CLT aplicado ★★☆

**Problema:** Tempo de atendimento: $\mu = 10$ min, $\sigma = 3$ min. Amostra de 36 clientes. $P(\bar{X} > 11)$?

**Resposta:** Pelo CLT: $\bar{X} \sim N(10, 3^2/36) = N(10, 0.25)$. $\sigma_{\bar{X}} = 0.5$. $Z = (11-10)/0.5 = 2$. $P(Z > 2) = 0.0228$.

---

**Ex 0.17.08** — Intervalo de confiança ★★☆

**Problema:** Amostra de $n = 25$: $\bar{x} = 50$, $s = 10$. Construa IC de 95% para $\mu$ (use $t_{24, 0.025} = 2.064$).

**Resposta:** $IC = \bar{x} \pm t \times s/\sqrt{n} = 50 \pm 2.064 \times 10/5 = 50 \pm 4.128$. IC = $[45.87, 54.13]$.

---

#### Nível ★★★

**Ex 0.17.09** — Teste de hipótese completo ★★★

**Problema:** Fabricante afirma que resistores têm $\mu = 100\,\Omega$. Amostra de 16: $\bar{x} = 103$, $s = 6$. Teste $H_0: \mu = 100$ vs $H_1: \mu \neq 100$ com $\alpha = 0.05$.

**Resposta:**
1. $t = (\bar{x} - \mu_0)/(s/\sqrt{n}) = (103-100)/(6/4) = 3/1.5 = 2.0$.
2. $t_{crit} = t_{15, 0.025} = 2.131$ (bilateral).
3. $|t_{obs}| = 2.0 < 2.131 \implies$ **não rejeita** $H_0$ a 5%.
4. Conclusão: não há evidência suficiente de que $\mu \neq 100\,\Omega$ (por pouco!).

---

**Ex 0.17.10** — Problema-texto de modelagem ★★★

**Problema:** Um roteador falha em média 1 vez a cada 500 horas. (a) Qual distribuição modela o tempo até a próxima falha? (b) $P$(sobreviver 1000 h sem falha)? (c) Dado que já sobreviveu 200 h, qual $P$(sobreviver mais 500 h)?

**Resposta:**
(a) **Exponencial** com $\lambda = 1/500$ falhas/h.
(b) $P(T > 1000) = e^{-1000/500} = e^{-2} = 0.1353$.
(c) Propriedade de **falta de memória**: $P(T > 700 | T > 200) = P(T > 500) = e^{-1} = 0.3679$. O sistema não "lembra" que já sobreviveu 200 h.

---

**Ex 0.17.11** — Combinatória + probabilidade condicional ★★★

**Problema:** Uma urna tem 5 bolas vermelhas e 3 azuis. Retiram-se 3 sem reposição. (a) $P$(todas vermelhas)? (b) $P$(exatamente 2 vermelhas)? (c) Dado que a 1ª foi vermelha, $P$(as 3 são vermelhas)?

**Resposta:**
(a) $\frac{\binom{5}{3}}{\binom{8}{3}} = \frac{10}{56} = 0.1786$.
(b) $\frac{\binom{5}{2}\binom{3}{1}}{\binom{8}{3}} = \frac{10 \times 3}{56} = \frac{30}{56} = 0.5357$.
(c) $P(\text{3V}|\text{1ª V}) = \frac{P(\text{3V})}{P(\text{1ª V})} = \frac{10/56}{5/8} = \frac{10/56}{35/56} = \frac{10}{35} = 0.2857$.

---

> 📘 **Mais exercícios:** Walpole — *Probabilidade e Estatística para Engenharia e Ciências* (Cap. 3–10). Devens — *Probabilidade e Estatística*. Meta: **5 problemas de modelagem** (Qual distribuição?) + **3 testes de hipótese completos**.

---

### 🔴 PRIORIDADE #3 — Controle Clássico (5.02)

> **Meta:** 15 exercícios. Residual = 40%. Foco: decisão de método, Root Locus, Bode, estabilidade.
> **Recursos com diagramas:** Para Root Locus e Bode, praticar **à mão** com papel milimetrado. Verificar com MATLAB (`rlocus`, `bode`, `margin`).

#### Nível ★☆☆

**Ex 5.02.01** — Classificação de sistema ★☆☆

**Problema:** $G(s) = \frac{10}{s(s+2)(s+5)}$. Qual o tipo do sistema? Qual erro em regime permanente para entrada degrau unitário?

**Resposta:** Tipo 1 (um integrador → um polo em $s = 0$). Erro para degrau: $e_{ss} = 0$ (sistema Tipo 1+ acompanha degrau perfeitamente). Erro para rampa: $e_{ss} = 1/K_v = (s \times G(s))|_{s \to 0}$... $K_v = \lim_{s \to 0} sG(s) = 10/(2 \times 5) = 1$. $e_{ss, rampa} = 1$.

---

**Ex 5.02.02** — Routh-Hurwitz ★☆☆

**Problema:** Equação característica: $s^3 + 4s^2 + 5s + 2 = 0$. O sistema é estável?

**Resposta:** Tabela de Routh:
| $s^3$ | 1 | 5 |
| $s^2$ | 4 | 2 |
| $s^1$ | $(4 \times 5 - 1 \times 2)/4 = 18/4 = 4.5$ | 0 |
| $s^0$ | 2 | |

Todos positivos na 1ª coluna → **estável** (0 trocas de sinal = 0 polos no SPD).

---

**Ex 5.02.03** — Margem de ganho/fase (conceitual) ★☆☆

**Problema:** No diagrama de Bode, $|G(j\omega)|$ cruza 0 dB em $\omega_{gc} = 10$ rad/s com fase de $-150°$. $\angle G(j\omega)$ cruza $-180°$ em $\omega_{pc} = 50$ rad/s com ganho de $-20$ dB. Margens?

**Resposta:** Margem de fase: $PM = 180° + (-150°) = 30°$ (em $\omega_{gc}$). Margem de ganho: $GM = 0 - (-20) = 20$ dB (em $\omega_{pc}$). Sistema estável (ambas positivas), mas PM baixa (desempenho oscilatório provável).

---

#### Nível ★★☆

**Ex 5.02.04** — Root Locus: regras aplicadas ★★☆

**Problema:** $G(s)H(s) = \frac{K}{s(s+1)(s+3)}$. (a) Quantos ramos? (b) No eixo real, onde fica o locus? (c) Assíntotas? (d) Ponto de saída do eixo real?

**Resposta:**
(a) 3 ramos (3 polos).
(b) Eixo real: à esquerda de número ímpar de polos+zeros reais → $(-\infty, -3]$ e $[-1, 0]$.
(c) $n - m = 3 - 0 = 3$ assíntotas. Ângulos: $(2k+1) \times 180°/3 = 60°, 180°, 300°$. Centroide: $\sigma_a = (0 + (-1) + (-3))/3 = -4/3$.
(d) $\frac{d}{ds}[s(s+1)(s+3)] = 0 \implies 3s^2 + 8s + 3 = 0 \implies s = \frac{-8 \pm \sqrt{64-36}}{6} = \frac{-8 \pm 5.29}{6}$. $s_1 = -0.45$ (entre 0 e -1, no locus ✓), $s_2 = -2.22$ (entre -1 e -3, não no locus).

---

**Ex 5.02.05** — Sketch de Bode ★★☆

**Problema:** $G(s) = \frac{100(s+10)}{s(s+100)}$. Esboce o diagrama de Bode (magnitude) assintótico. Indique ganho DC e inclinações.

**Resposta:**
- Reescrever: $G(s) = \frac{100 \times 10}{100} \times \frac{(s/10+1)}{s(s/100+1)} = \frac{10}{s} \times \frac{(s/10+1)}{(s/100+1)}$.
- $\omega \ll 10$: $|G| \approx 10/\omega$ → inclinação $-20$ dB/déc, passa por $20\log(10) = 20$ dB em $\omega = 1$.
- $\omega = 10$: zero adiciona $+20$ dB/déc → total: 0 dB/déc (plano).
- $\omega = 100$: polo adiciona $-20$ dB/déc → total: $-20$ dB/déc.
- Em $\omega = 10$: $|G| \approx 10/10 = 1 = 0$ dB. Platô entre $\omega = 10$ e $\omega = 100$ a 0 dB.

📐 **Verificar:** MATLAB `bode(tf([100 1000],[1 100 0]))`.

---

**Ex 5.02.06** — Routh com parâmetro K ★★☆

**Problema:** $1 + K \times \frac{1}{s(s+2)(s+4)} = 0 \implies s^3 + 6s^2 + 8s + K = 0$. Para que faixa de $K$ o sistema em malha fechada é estável?

**Resposta:** Routh:
| $s^3$ | 1 | 8 |
| $s^2$ | 6 | K |
| $s^1$ | $(48-K)/6$ | 0 |
| $s^0$ | K | |

Estabilidade: $K > 0$ (da linha $s^0$) e $(48-K)/6 > 0 \implies K < 48$. **$0 < K < 48$**.

---

**Ex 5.02.07** — Projeto de compensador (Lead) ★★☆

**Problema:** Sistema com $G(s) = \frac{4}{s(s+2)}$. Sem compensador, PM = ? Projetar lead compensator para PM ≥ 45°.

**Resposta:**
- $G(j\omega) = \frac{4}{j\omega(j\omega+2)}$. $|G| = \frac{4}{\omega\sqrt{\omega^2+4}}$. Achar $\omega_{gc}$: $|G| = 1 \implies \omega^2(\omega^2+4) = 16$. $\omega^4 + 4\omega^2 - 16 = 0$. $\omega^2 = (-4 + \sqrt{16+64})/2 = (-4+8.94)/2 = 2.47$. $\omega_{gc} = 1.57$ rad/s.
- Fase em $\omega_{gc}$: $-90° - \arctan(1.57/2) = -90° - 38.1° = -128.1°$. $PM = 180 - 128.1 = 51.9°$.
- PM atual já > 45°! Compensador não necessário neste caso. (Exercício testa se o aluno verifica antes de projetar.)

---

#### Nível ★★★

**Ex 5.02.08** — Root Locus completo + estabilidade ★★★

**Problema:** $G(s) = \frac{K(s+2)}{s^2(s+4)}$. Trace o Root Locus. Para que valor de $K$ o sistema se torna instável (polos no eixo $j\omega$)?

**Resposta:**
- Polos: $0, 0, -4$. Zero: $-2$. $n = 3$, $m = 1$.
- Eixo real: $[-4, -2]$ (ímpar à direita) e de $0$ (polo duplo) para $-2$ — duplo polo se bifurca.
- Assíntotas: $n-m = 2$. Ângulos: $90°, 270°$. Centroide: $(0+0-4-(-2))/2 = -1$.
- A bifurcação dos polos duplos em $s=0$: saem para cima e para baixo (em direção às assíntotas verticais em $\sigma = -1$).
- Para cruzamento em $j\omega$: $s = j\omega$ na eq. caract.: $s^3 + 4s^2 + Ks + 2K = 0$. $(j\omega)^3 + 4(j\omega)^2 + Kj\omega + 2K = 0$. $-j\omega^3 - 4\omega^2 + jK\omega + 2K = 0$. Parte real: $-4\omega^2 + 2K = 0 \implies K = 2\omega^2$. Parte imaginária: $-\omega^3 + K\omega = 0 \implies \omega(-\omega^2 + K) = 0$. Como $\omega \neq 0$: $K = \omega^2$. Das duas: $2\omega^2 = \omega^2$... impossível ($\omega = 0$). Logo os ramos **nunca cruzam** o eixo $j\omega$ → sistema estável para todo $K > 0$.

---

**Ex 5.02.09** — Análise completa: Routh + Bode + resposta ★★★

**Problema:** $G(s) = \frac{20}{(s+1)(s+5)}$ em malha fechada unitária. (a) Polos de MF. (b) Sistema é estável? (c) Tipo do sistema e erro para degrau. (d) Margem de fase.

**Resposta:**
(a) Eq. caract.: $1 + G(s) = 0 \implies s^2 + 6s + 5 + 20 = 0 \implies s^2 + 6s + 25 = 0$. $s = \frac{-6 \pm \sqrt{36-100}}{2} = -3 \pm j4$. Polos complexos conjugados.
(b) Ambos com parte real $-3 < 0$ → **estável**.
(c) Tipo 0 ($G(0) = 20/5 = 4$). $K_p = 4$. $e_{ss} = 1/(1+K_p) = 1/5 = 0.2$ (20% de erro para degrau).
(d) $\omega_n = 5$, $\zeta = 3/5 = 0.6$. $PM \approx 100\zeta = 60°$ (estimativa). Exato: $\omega_{gc}$ de $|G(j\omega)| = 1$: $400/((1+\omega^2)(25+\omega^2)) = 1$. Resolver numericamente: $\omega_{gc} \approx 3.6$ rad/s. $PM = 180° + \angle G(j3.6) = 180° - \arctan(3.6) - \arctan(3.6/5) = 180° - 74.4° - 35.7° = 69.9°$.

---

**Ex 5.02.10** — Nyquist simplificado ★★★

**Problema:** $G(s) = \frac{K}{(s+1)^3}$. Para que $K$ o sistema em MF será instável?

**Resposta:** Fase de $G(j\omega)$: $\angle G = -3\arctan(\omega)$. Cruza $-180°$ quando $3\arctan(\omega) = 180° \implies \omega_{pc} = \tan(60°) = \sqrt{3}$. $|G(j\sqrt{3})| = K/(\sqrt{1+3})^3 = K/8$. Instável quando $K/8 > 1 \implies K > 8$. Marginalmente estável em $K = 8$ (oscila em $\omega = \sqrt{3}$ rad/s).

---

> 📐 **Prática obrigatória:** Traçar Root Locus à mão (10×). Traçar Bode assintótico à mão (10×). Verificar cada um no MATLAB. **Ogata** — *Modern Control Engineering* Cap. 6–7, **Dorf & Bishop** Cap. 7–8: resolver 3 exercícios de Root Locus + 3 de Bode + 2 de projeto.

---

### 🟡 PRIORIDADE #4 — Potência e Sistemas Elétricos (4.04)

> **Meta:** 10 exercícios. Residual = 40%. Foco: trifásico, PU, fator de potência.

#### Nível ★☆☆

**Ex 4.04.01** — Relações trifásicas Y ★☆☆

**Problema:** Sistema Y equilibrado: $V_{fase} = 127$ V. Ache $V_{linha}$ e, para carga $Z = 10\angle 30°\,\Omega$/fase, ache $I_{fase}$, $I_{linha}$, $P_{total}$.

**Resposta:** $V_L = \sqrt{3} \times 127 = 220$ V. Em Y: $I_F = I_L = V_F/|Z| = 127/10 = 12.7$ A. $P_{total} = 3 V_F I_F \cos\theta = 3 \times 127 \times 12.7 \times \cos 30° = 4188$ W.

---

**Ex 4.04.02** — Transformador ideal ★☆☆

**Problema:** Trafo ideal $a = N_1/N_2 = 10$. Primário: 2200 V. Carga no secundário: $Z_L = 5\,\Omega$. Ache $V_2$, $I_2$, $I_1$, e impedância refletida ao primário.

**Resposta:** $V_2 = 2200/10 = 220$ V. $I_2 = 220/5 = 44$ A. $I_1 = 44/10 = 4.4$ A. $Z_{ref} = a^2 Z_L = 100 \times 5 = 500\,\Omega$.

---

#### Nível ★★☆

**Ex 4.04.03** — Sistema por unidade (PU) ★★☆

**Problema:** Bases: $S_{base} = 100$ MVA, $V_{base} = 13.8$ kV. Gerador: $S = 50$ MVA, $X_d = 0.2$ PU (na base própria: 50 MVA, 13.8 kV). Converta $X_d$ para a base do sistema.

**Resposta:** $X_{novo} = X_{antigo} \times \frac{S_{base,novo}}{S_{base,antigo}} \times \left(\frac{V_{base,antigo}}{V_{base,novo}}\right)^2 = 0.2 \times \frac{100}{50} \times \left(\frac{13.8}{13.8}\right)^2 = 0.4$ PU.

---

**Ex 4.04.04** — Correção de FP trifásica ★★☆

**Problema:** Motor trifásico: $P = 50$ kW, fp = 0.75 (ind.), alimentado em 380 V (linha), 60 Hz. Corrigir para fp = 0.92. Ache $Q_C$ total e $C$ por fase (banco Y).

**Resposta:** $Q_{old} = 50k \times \tan(\arccos 0.75) = 50k \times 0.8819 = 44.1$ kVAR. $Q_{new} = 50k \times \tan(\arccos 0.92) = 50k \times 0.4266 = 21.3$ kVAR. $Q_C = 44.1 - 21.3 = 22.8$ kVAR. $V_F = 380/\sqrt{3} = 219.4$ V. $C/\text{fase} = Q_C / (3 \times \omega V_F^2) = 22800 / (3 \times 377 \times 219.4^2) = 22800 / 54.41 \times 10^6 = 419\,\mu$F.

---

**Ex 4.04.05** — Curto-circuito trifásico ★★☆

**Problema:** Sistema com $V_{base} = 13.8$ kV, $S_{base} = 100$ MVA. Impedância equivalente do sistema vista do barramento: $Z_{pu} = j0.25$. Ache a corrente de curto-circuito trifásica no barramento.

**Resposta:** $I_{cc,pu} = V_{pu}/Z_{pu} = 1.0/0.25 = 4.0$ PU. $I_{base} = S_{base}/(\sqrt{3} V_{base}) = 100 \times 10^6 / (\sqrt{3} \times 13800) = 4184$ A. $I_{cc} = 4.0 \times 4184 = 16736$ A $\approx 16.7$ kA.

---

#### Nível ★★★

**Ex 4.04.06** — Trifásico desequilibrado (Y sem neutro) ★★★

**Problema:** Fonte Y equilibrada, $V_{AN} = 120\angle 0°$ V. Carga Y sem neutro: $Z_A = 10\,\Omega$, $Z_B = j10\,\Omega$, $Z_C = -j10\,\Omega$. Ache a tensão no neutro flutuante ($V_{N'n}$) e as correntes de fase.

**Resposta:** Tensão do neutro da carga: $V_{N'n} = \frac{V_{AN}/Z_A + V_{BN}/Z_B + V_{CN}/Z_C}{1/Z_A + 1/Z_B + 1/Z_C}$.

$1/Z_A + 1/Z_B + 1/Z_C = 0.1 + (-j0.1) + (j0.1) = 0.1$ S.

$V_{BN} = 120\angle{-120°}$, $V_{CN} = 120\angle{120°}$.

Numerador: $120\angle 0°/10 + 120\angle{-120°}/(j10) + 120\angle{120°}/(-j10)$
$= 12\angle 0° + 12\angle{-120°-90°} + 12\angle{120°+90°}$
$= 12\angle 0° + 12\angle{-210°} + 12\angle{210°}$
$= 12 + 12\angle{150°} + 12\angle{210°}$
$= 12 + 12(-0.866 + j0.5) + 12(-0.866 - j0.5)$
$= 12 - 10.39 + j6 - 10.39 - j6 = -8.78$.

$V_{N'n} = -8.78/0.1 = -87.8$ V. (Deslocamento grande do neutro!)

$I_A = (V_{AN} - V_{N'n})/Z_A = (120 + 87.8)/10 = 20.78$ A. (Desequilíbrio severo.)

---

> 📘 **Mais exercícios:** Stevenson/Glover — *Power Systems Analysis*, Cap. 2 (PU) + Cap. 9 (curto). Chapman — *Electric Machinery*, Cap. 2 (trafo). Meta: **3 problemas PU + 2 trifásicos + 2 curto-circuito**.

---

### 🟡 PRIORIDADE #5 — Cálculo 1 (0.05)

> **Meta:** 12 exercícios. Residual = 50%. Foco: integrais, séries, otimização.
> **Sem imagens necessárias.**

**Ex 0.05.01** — Integral por substituição ★☆☆

**Problema:** $\int x \cos(x^2)\,dx$

**Resposta:** $u = x^2$, $du = 2x\,dx$. $\frac{1}{2}\int \cos u\,du = \frac{1}{2}\sin(x^2) + C$.

---

**Ex 0.05.02** — Integral por partes ★★☆

**Problema:** $\int x^2 e^x\,dx$

**Resposta:** Partes duas vezes. $u = x^2, dv = e^x dx \implies x^2 e^x - 2\int xe^x dx$. Novamente: $= x^2 e^x - 2(xe^x - e^x) + C = e^x(x^2 - 2x + 2) + C$.

---

**Ex 0.05.03** — Frações parciais ★★☆

**Problema:** $\int \frac{3x+5}{(x+1)(x+2)}\,dx$

**Resposta:** $\frac{3x+5}{(x+1)(x+2)} = \frac{A}{x+1} + \frac{B}{x+2}$. $A = (3(-1)+5)/(-1+2) = 2$. $B = (3(-2)+5)/(-2+1) = 1$. $\int = 2\ln|x+1| + \ln|x+2| + C$.

---

**Ex 0.05.04** — Convergência de série ★★☆

**Problema:** $\sum_{n=1}^{\infty} \frac{n}{2^n}$ converge? Se sim, qual o valor?

**Resposta:** Teste da razão: $\lim \frac{(n+1)/2^{n+1}}{n/2^n} = \lim \frac{n+1}{2n} = 1/2 < 1$ → converge. Valor: derivar $\sum x^n = \frac{1}{1-x}$: $\sum nx^{n-1} = \frac{1}{(1-x)^2}$. $\sum nx^n = \frac{x}{(1-x)^2}$. Em $x = 1/2$: $\frac{1/2}{(1/2)^2} = 2$.

---

**Ex 0.05.05** — Série de Taylor ★★☆

**Problema:** Expanda $f(x) = \ln(1+x)$ em série de Taylor até $x^4$. Estime $\ln(1.1)$.

**Resposta:** $\ln(1+x) = x - x^2/2 + x^3/3 - x^4/4 + \cdots$. $\ln(1.1) \approx 0.1 - 0.005 + 0.000333 - 0.000025 = 0.09531$ (exato: $0.09531$).

---

**Ex 0.05.06** — Otimização com restrição ★★★

**Problema:** Lata cilíndrica sem tampa com volume $V = 500$ cm³. Minimizar a área de material $A = \pi r^2 + 2\pi r h$. Ache $r$ e $h$ ótimos.

**Resposta:** $V = \pi r^2 h = 500 \implies h = 500/(\pi r^2)$. $A(r) = \pi r^2 + 2\pi r \times 500/(\pi r^2) = \pi r^2 + 1000/r$. $A'(r) = 2\pi r - 1000/r^2 = 0 \implies r^3 = 500/\pi \implies r = (500/\pi)^{1/3} \approx 5.42$ cm. $h = 500/(\pi \times 5.42^2) = 5.42$ cm. (Curiosidade: $h = r$ para lata sem tampa!)

---

**Ex 0.05.07** — Integral imprópria ★★☆

**Problema:** $\int_1^{\infty} \frac{1}{x^3}\,dx$ converge?

**Resposta:** $\int_1^{b} x^{-3}\,dx = \left[-\frac{1}{2x^2}\right]_1^{b} = -\frac{1}{2b^2} + \frac{1}{2}$. Com $b \to \infty$: $= 1/2$. **Converge** para $1/2$.

---

> 📘 **Mais exercícios:** Stewart — *Calculus* (Cap. 5–8: integrais, Cap. 11: séries). Resolver **5 integrais mistas** (sem indicação da técnica) + **3 séries de convergência** + **2 otimizações**.

---

### 🟡 PRIORIDADE #6 — Cálculo Multivariável (0.07)

> **Meta:** 8 exercícios. Residual = 55%. Foco: integrais múltiplas, coordenadas, teoremas vetoriais.

**Ex 0.07.01** — Integral dupla (retangular) ★☆☆

**Problema:** $\int_0^2 \int_0^3 (x^2 + y)\,dy\,dx$

**Resposta:** Interna: $\int_0^3 (x^2 + y)\,dy = [x^2 y + y^2/2]_0^3 = 3x^2 + 9/2$. Externa: $\int_0^2 (3x^2 + 4.5)\,dx = [x^3 + 4.5x]_0^2 = 8 + 9 = 17$.

---

**Ex 0.07.02** — Integral dupla (polar) ★★☆

**Problema:** Calcule $\iint_D (x^2+y^2)\,dA$ onde $D$ é o disco $x^2+y^2 \leq 4$.

**Resposta:** Polares: $\int_0^{2\pi}\int_0^2 r^2 \cdot r\,dr\,d\theta = \int_0^{2\pi}\int_0^2 r^3\,dr\,d\theta = 2\pi \times [r^4/4]_0^2 = 2\pi \times 4 = 8\pi$.

---

**Ex 0.07.03** — Integral tripla (cilíndricas) ★★☆

**Problema:** Volume do sólido entre $z = 0$ e $z = 4 - r^2$ (paraboloide), $r \leq 2$.

**Resposta:** $V = \int_0^{2\pi}\int_0^{2}\int_0^{4-r^2} r\,dz\,dr\,d\theta = 2\pi\int_0^2 r(4-r^2)\,dr = 2\pi\int_0^2(4r - r^3)\,dr = 2\pi[2r^2 - r^4/4]_0^2 = 2\pi(8-4) = 8\pi$.

---

**Ex 0.07.04** — Divergente e Rotacional ★★☆

**Problema:** $\vec{F} = (x^2 y, \, xyz, \, z^2)$. Calcule $\nabla \cdot \vec{F}$ e $\nabla \times \vec{F}$ no ponto $(1, 2, 3)$.

**Resposta:** $\nabla \cdot \vec{F} = 2xy + xz + 2z = 2(1)(2) + (1)(3) + 2(3) = 4 + 3 + 6 = 13$. $\nabla \times \vec{F} = (0 - xy, \, 0 - 0, \, yz - x^2) = (-2, 0, -1)$ em $(1,2,3)$: $(-2, 0, 5)$. Verificar: $(\partial_y z^2 - \partial_z xyz, \, \partial_z x^2y - \partial_x z^2, \, \partial_x xyz - \partial_y x^2y) = (0 - xy, 0 - 0, yz - x^2)$. Em $(1,2,3)$: $(-2, 0, 6-1) = (-2, 0, 5)$. ✓

---

**Ex 0.07.05** — Teorema de Green ★★★

**Problema:** Calcule $\oint_C (y^2\,dx + 3xy\,dy)$ onde $C$ é o triângulo com vértices $(0,0)$, $(1,0)$, $(1,2)$ (sentido anti-horário).

**Resposta:** Green: $\oint = \iint_D \left(\frac{\partial(3xy)}{\partial x} - \frac{\partial(y^2)}{\partial y}\right)\,dA = \iint_D (3y - 2y)\,dA = \iint_D y\,dA$. Região: $0 \leq x \leq 1$, $0 \leq y \leq 2x$. $\int_0^1\int_0^{2x} y\,dy\,dx = \int_0^1 [y^2/2]_0^{2x}\,dx = \int_0^1 2x^2\,dx = 2/3$.

---

> 📘 **Mais exercícios:** Stewart Cap. 15 (integrais múltiplas), Cap. 16 (cálculo vetorial). **3 integrais mudando coordenadas + 2 problemas de Stokes/Divergência** com verificação direta.

---

### 🟡 PRIORIDADE #7 — EDOs (0.09)

> **Meta:** 8 exercícios. Residual = 50%. Foco: classificar + resolver completo.

**Ex 0.09.01** — EDO separável ★☆☆

**Problema:** $y' = xy$, $y(0) = 1$.

**Resposta:** $dy/y = x\,dx \implies \ln|y| = x^2/2 + C \implies y = Ae^{x^2/2}$. $y(0)=1 \implies A=1$. $y = e^{x^2/2}$.

---

**Ex 0.09.02** — EDO linear 1ª ordem ★★☆

**Problema:** $y' + 2y = 6e^{-t}$, $y(0) = 3$.

**Resposta:** Fator integrante: $\mu = e^{2t}$. $(e^{2t}y)' = 6e^{t}$. $e^{2t}y = 6e^{t} + C$. $y = 6e^{-t} + Ce^{-2t}$. $y(0) = 3$: $6 + C = 3 \implies C = -3$. $y(t) = 6e^{-t} - 3e^{-2t}$.

---

**Ex 0.09.03** — EDO 2ª ordem homogênea ★★☆

**Problema:** $y'' + 5y' + 6y = 0$, $y(0) = 2$, $y'(0) = -5$.

**Resposta:** Eq. caract.: $r^2 + 5r + 6 = 0 \implies r = -2, -3$. $y = C_1 e^{-2t} + C_2 e^{-3t}$. $y(0) = C_1 + C_2 = 2$. $y' = -2C_1 e^{-2t} - 3C_2 e^{-3t}$. $y'(0) = -2C_1 - 3C_2 = -5$. Sistema: $C_1 = 1, C_2 = 1$. $y(t) = e^{-2t} + e^{-3t}$.

---

**Ex 0.09.04** — EDO 2ª ordem não-homogênea ★★★

**Problema:** $y'' + 4y = 8\sin(2t)$.

**Resposta:** (Caso de ressonância! $\omega_{forçada} = \omega_{natural} = 2$.) Homogênea: $y_h = C_1\cos 2t + C_2\sin 2t$. Particular: como $\sin 2t$ já é solução da homogênea, tentar $y_p = t(A\cos 2t + B\sin 2t)$. Substituir: $y_p'' + 4y_p = -4A\sin 2t + 4B\cos 2t = 8\sin 2t$. $\implies B = 0, A = -2$. $y_p = -2t\cos 2t$. Geral: $y = C_1\cos 2t + C_2\sin 2t - 2t\cos 2t$.

---

**Ex 0.09.05** — Circuito RC como EDO ★★★

**Problema:** Circuito RC: $R = 1\,\text{k}\Omega$, $C = 10\,\mu$F. Em $t=0$, aplica-se $V_s = 5\sin(1000t)$ V com $V_C(0) = 0$. Ache $V_C(t)$.

**Resposta:** $RC\,dV_C/dt + V_C = V_s$. $0.01\,V_C' + V_C = 5\sin(1000t)$. $V_C' + 100V_C = 500\sin(1000t)$. Homogênea: $V_{C,h} = Ae^{-100t}$ (transitório). Particular (coeficientes indeterminados): $V_{C,p} = a\sin(1000t) + b\cos(1000t)$. Substituir: $1000a\cos(1000t) - 1000b\sin(1000t) + 100a\sin(1000t) + 100b\cos(1000t) = 500\sin(1000t)$. $\sin$: $-1000b + 100a = 500$. $\cos$: $1000a + 100b = 0 \implies b = -10a$. $-1000(-10a) + 100a = 500 \implies 10100a = 500 \implies a = 0.0495$. $b = -0.495$. $V_C = Ae^{-100t} + 0.0495\sin(1000t) - 0.495\cos(1000t)$. $V_C(0) = 0$: $A - 0.495 = 0 \implies A = 0.495$. Transitório decai com $\tau = 10$ ms.

---

> 📘 **Mais exercícios:** Boyce & DiPrima — *Elementary Differential Equations*, Cap. 2–4. Zill — *A First Course in Differential Equations*. Meta: **3 EDOs de 1ª ordem (classificar tipo primeiro) + 3 EDOs de 2ª ordem + 2 aplicações (circuitos, mecânica)**.

---

### 🟡 PRIORIDADE #8 — Álgebra Linear (0.11)

> **Meta:** 8 exercícios. Residual = 55%.

**Ex 0.11.01** — Escalonamento ★★☆

**Problema:** Resolver $\begin{cases} x + 2y + z = 4 \\ 2x + y - z = 3 \\ x - y + 2z = 1 \end{cases}$

**Resposta:** Matriz aumentada → escalonar:
$\begin{pmatrix} 1 & 2 & 1 & | & 4 \\ 2 & 1 & -1 & | & 3 \\ 1 & -1 & 2 & | & 1 \end{pmatrix} \xrightarrow{L2-2L1, L3-L1} \begin{pmatrix} 1 & 2 & 1 & | & 4 \\ 0 & -3 & -3 & | & -5 \\ 0 & -3 & 1 & | & -3 \end{pmatrix} \xrightarrow{L3-L2} \begin{pmatrix} 1 & 2 & 1 & | & 4 \\ 0 & -3 & -3 & | & -5 \\ 0 & 0 & 4 & | & 2 \end{pmatrix}$

$z = 1/2$. $-3y - 3/2 = -5 \implies y = 7/6$. $x + 7/3 + 1/2 = 4 \implies x = 4 - 17/6 = 7/6$.

---

**Ex 0.11.02** — Autovalores e autovetores ★★☆

**Problema:** $A = \begin{pmatrix} 4 & 1 \\ 2 & 3 \end{pmatrix}$. Ache autovalores e autovetores.

**Resposta:** $\det(A - \lambda I) = (4-\lambda)(3-\lambda) - 2 = \lambda^2 - 7\lambda + 10 = 0$. $\lambda_1 = 5, \lambda_2 = 2$.
- $\lambda = 5$: $(A-5I)\vec{v} = 0 \implies \begin{pmatrix} -1 & 1 \\ 2 & -2 \end{pmatrix}\vec{v} = 0 \implies \vec{v}_1 = (1, 1)^T$.
- $\lambda = 2$: $(A-2I)\vec{v} = 0 \implies \begin{pmatrix} 2 & 1 \\ 2 & 1 \end{pmatrix}\vec{v} = 0 \implies \vec{v}_2 = (1, -2)^T$.

---

**Ex 0.11.03** — Diagonalização ★★★

**Problema:** Com $A$ do exercício anterior, calcule $A^{10}$ usando diagonalização.

**Resposta:** $A = PDP^{-1}$ onde $P = \begin{pmatrix} 1 & 1 \\ 1 & -2 \end{pmatrix}$, $D = \begin{pmatrix} 5 & 0 \\ 0 & 2 \end{pmatrix}$. $P^{-1} = \frac{1}{-3}\begin{pmatrix} -2 & -1 \\ -1 & 1 \end{pmatrix} = \begin{pmatrix} 2/3 & 1/3 \\ 1/3 & -1/3 \end{pmatrix}$. $A^{10} = PD^{10}P^{-1} = P\begin{pmatrix} 5^{10} & 0 \\ 0 & 2^{10} \end{pmatrix}P^{-1}$.

$5^{10} = 9765625$, $2^{10} = 1024$.

$A^{10} = \begin{pmatrix} 1 & 1 \\ 1 & -2 \end{pmatrix}\begin{pmatrix} 9765625 & 0 \\ 0 & 1024 \end{pmatrix}\begin{pmatrix} 2/3 & 1/3 \\ 1/3 & -1/3 \end{pmatrix}$

$= \begin{pmatrix} 6510758 & 3254883 \\ 6509710 & 3253935 \end{pmatrix}$.

(Na prática, o importante é saber o *método*, não a aritmética.)

---

**Ex 0.11.04** — Determinante e inversibilidade ★☆☆

**Problema:** $A = \begin{pmatrix} 1 & 2 & 3 \\ 0 & 4 & 5 \\ 0 & 0 & 6 \end{pmatrix}$. $\det(A)$? $A$ é inversível?

**Resposta:** Triangular → $\det = 1 \times 4 \times 6 = 24 \neq 0$ → **inversível**.

---

> 📘 **Mais exercícios:** Strang — *Introduction to Linear Algebra*, Cap. 1–6. Lay — *Linear Algebra*. Meta: **3 sistemas (incluindo 1 com infinitas soluções) + 2 autovalores 3×3 + 1 SVD aplicada**.

---

### 🟡 PRIORIDADE #9 — Eletrônica Analógica (2.01–2.03)

> **Meta:** 10 exercícios. Residual ~40%. Foco: ponto de operação, análise de sinal, amplificadores.
> **Simulação recomendada:** LTspice (gratuito). Montar cada circuito e comparar.

**Ex 2.01.01** — Diodo: ponto de operação ★☆☆

**Problema:** $V_s = 5$ V, $R = 1\,\text{k}\Omega$, diodo de silício ($V_D = 0.7$ V) em série. Ache $I_D$ e $V_R$.

**Resposta:** $I_D = (5 - 0.7)/1000 = 4.3$ mA. $V_R = 4.3$ V.

---

**Ex 2.01.02** — Retificador de meia onda: Vripple ★★☆

**Problema:** Retificador de meia onda com $V_p = 12$ V, $f = 60$ Hz, $C = 470\,\mu$F, $R_L = 1\,\text{k}\Omega$. Estime $V_{ripple}$.

**Resposta:** $V_{ripple} \approx \frac{V_p}{fRC} = \frac{12}{60 \times 1000 \times 470 \times 10^{-6}} = \frac{12}{28.2} = 0.43$ V (pp). $V_{DC} \approx V_p - V_D - V_{ripple}/2 \approx 12 - 0.7 - 0.21 = 11.09$ V.

---

**Ex 2.02.01** — Polarização BJT por divisor de tensão ★★☆

**Problema:** BJT NPN, $\beta = 100$. $V_{CC} = 12$ V, $R_C = 2\,\text{k}\Omega$, $R_E = 500\,\Omega$, $R_1 = 40\,\text{k}\Omega$, $R_2 = 10\,\text{k}\Omega$. Ache $I_C$ e $V_{CE}$.

**Resposta:** $V_{BB} = V_{CC} \times R_2/(R_1+R_2) = 12 \times 10/50 = 2.4$ V. $R_{BB} = R_1 \parallel R_2 = 8\,\text{k}\Omega$. Malha base: $V_{BB} = I_B R_{BB} + V_{BE} + I_E R_E \approx V_{BE} + I_C R_E$ (desprezando $I_B R_{BB}$ se $\beta$ grande). $I_C \approx (2.4 - 0.7)/0.5k = 3.4$ mA. $V_{CE} = 12 - 3.4(2 + 0.5) = 12 - 8.5 = 3.5$ V. (Região ativa: $V_{CE} > V_{CE,sat}$ ✓.)

---

**Ex 2.03.01** — Op-Amp: somador inversor ★☆☆

**Problema:** Somador inversor: $R_f = 10\,\text{k}\Omega$, $R_1 = 2\,\text{k}\Omega$ ($V_1 = 1$ V), $R_2 = 5\,\text{k}\Omega$ ($V_2 = 2$ V). Ache $V_o$.

**Resposta:** $V_o = -\left(\frac{R_f}{R_1}V_1 + \frac{R_f}{R_2}V_2\right) = -(5 \times 1 + 2 \times 2) = -(5 + 4) = -9$ V.

---

**Ex 2.03.02** — Filtro ativo passa-baixas ★★☆

**Problema:** Filtro passa-baixas Sallen-Key 2ª ordem: $R_1 = R_2 = 10\,\text{k}\Omega$, $C_1 = C_2 = 10$ nF. Ache $f_c$ e a atenuação em $f = 10 f_c$.

**Resposta:** $f_c = \frac{1}{2\pi RC} = \frac{1}{2\pi \times 10^4 \times 10^{-8}} = 1592$ Hz. Atenuação 2ª ordem: $-40$ dB/déc em $10 f_c$: $-40$ dB.

---

> 📐 **Simulação obrigatória:** Montar no LTspice: (1) retificador com filtro, (2) amplificador emissor-comum com análise AC, (3) filtro ativo de 2ª ordem. **Comparar cálculos com simulação.** Meta: **3 circuitos simulados + 5 exercícios de polarização BJT/MOSFET do Sedra/Smith Cap. 5–7.**

---

### 🟡 PRIORIDADE #10 — Telecomunicações (6.01)

> **Meta:** 8 exercícios. Residual = 40%.

**Ex 6.01.01** — Shannon ★☆☆

**Problema:** Canal com $B = 200$ kHz, SNR = 20 dB. Capacidade máxima?

**Resposta:** SNR linear = $10^{20/10} = 100$. $C = B\log_2(1 + \text{SNR}) = 200000 \times \log_2(101) = 200000 \times 6.66 = 1.33$ Mbit/s.

---

**Ex 6.01.02** — Link budget ★★☆

**Problema:** TX: $P_{TX} = 20$ dBm, ganho antena TX = 10 dBi. Perda espaço livre (FSPL) = 100 dB. Ganho antena RX = 5 dBi. Sensibilidade RX = $-90$ dBm. Margem?

**Resposta:** $P_{RX} = 20 + 10 - 100 + 5 = -65$ dBm. Margem $= -65 - (-90) = 25$ dB. ✓ (Link fecha com folga.)

---

**Ex 6.01.03** — Modulação: bits por símbolo ★☆☆

**Problema:** Sistema usa 64-QAM com symbol rate de 1 Msymbol/s. Bit rate?

**Resposta:** $b = \log_2 64 = 6$ bits/símbolo. $R_b = 6 \times 1 = 6$ Mbit/s.

---

**Ex 6.01.04** — Nyquist e amostragem ★☆☆

**Problema:** Sinal de voz: banda $300$ Hz – $3400$ Hz. Qual taxa de amostragem mínima?

**Resposta:** $f_s \geq 2 f_{max} = 2 \times 3400 = 6800$ Hz. (Na prática, telefonia usa $8000$ Hz.)

---

**Ex 6.01.05** — OFDM: subportadoras ★★☆

**Problema:** Sistema OFDM com $B = 20$ MHz, espaçamento de subportadoras $\Delta f = 15$ kHz. Quantas subportadoras? Se cada usa 16-QAM, bit rate bruta?

**Resposta:** $N = B/\Delta f = 20 \times 10^6 / 15000 = 1333$ subportadoras. Cada uma: $\log_2 16 = 4$ bits/símbolo. $R_b = 1333 \times 4 \times 15000 = 80$ Mbit/s (bruto, sem overhead).

---

> 📘 **Mais exercícios:** Haykin — *Communication Systems* (Cap. 3: AM/FM, Cap. 7: digital). Proakis — *Digital Communications*. Meta: **2 link budgets completos + 3 comparações de modulação + 2 problemas de Shannon**.

---

### 🟡 PRIORIDADE #11 — Métodos Numéricos (0.18)

> **Meta:** 6 exercícios. Residual = 45%. Foco: implementar + calcular à mão.

**Ex 0.18.01** — Bisseção ★☆☆

**Problema:** $f(x) = x^3 - x - 2$. Raiz em $[1, 2]$? Execute 3 iterações.

**Resposta:** $f(1) = -2 < 0$, $f(2) = 4 > 0$. Iter 1: $c = 1.5$, $f(1.5) = 3.375 - 1.5 - 2 = -0.125 < 0$. $[1.5, 2]$. Iter 2: $c = 1.75$, $f(1.75) = 5.359 - 1.75 - 2 = 1.609 > 0$. $[1.5, 1.75]$. Iter 3: $c = 1.625$, $f(1.625) = 4.291 - 1.625 - 2 = 0.666 > 0$. $[1.5, 1.625]$. Raiz $\approx 1.52$.

---

**Ex 0.18.02** — Newton-Raphson ★★☆

**Problema:** Mesma $f(x) = x^3 - x - 2$, $x_0 = 1.5$. Execute 2 iterações.

**Resposta:** $f'(x) = 3x^2 - 1$. $x_1 = 1.5 - f(1.5)/f'(1.5) = 1.5 - (-0.125)/5.75 = 1.5 + 0.0217 = 1.5217$. $x_2 = 1.5217 - f(1.5217)/f'(1.5217) = 1.5217 - 0.0004/5.947 = 1.5214$. (Convergência muito mais rápida que bisseção!)

---

**Ex 0.18.03** — Euler para EDO ★★☆

**Problema:** $y' = -2y$, $y(0) = 1$. Use Euler com $h = 0.1$ para 5 passos. Compare com solução exata.

**Resposta:**
| $t$ | Euler | Exato ($e^{-2t}$) | Erro |
|-----|-------|-----|------|
| 0.0 | 1.000 | 1.000 | 0 |
| 0.1 | 0.800 | 0.819 | 2.3% |
| 0.2 | 0.640 | 0.670 | 4.5% |
| 0.3 | 0.512 | 0.549 | 6.7% |
| 0.4 | 0.410 | 0.449 | 8.7% |
| 0.5 | 0.328 | 0.368 | 10.9% |

Euler subestima (1ª ordem). Erro acumula ~2%/passo.

---

**Ex 0.18.04** — Interpolação de Lagrange ★★☆

**Problema:** Pontos: $(1, 1)$, $(2, 4)$, $(3, 9)$. Estime $f(2.5)$.

**Resposta:** $L_0 = \frac{(2.5-2)(2.5-3)}{(1-2)(1-3)} = \frac{0.5 \times (-0.5)}{(-1)(-2)} = \frac{-0.25}{2} = -0.125$. $L_1 = \frac{(2.5-1)(2.5-3)}{(2-1)(2-3)} = \frac{1.5 \times (-0.5)}{1 \times (-1)} = 0.75$. $L_2 = \frac{(2.5-1)(2.5-2)}{(3-1)(3-2)} = \frac{1.5 \times 0.5}{2} = 0.375$. $f(2.5) = -0.125(1) + 0.75(4) + 0.375(9) = -0.125 + 3 + 3.375 = 6.25$. (Exato! Porque os pontos são de $x^2$, e Lagrange grau 2 interpola polinômio grau 2 perfeitamente.)

---

> 📘 **Prática obrigatória:** Implementar cada método em **Python** (10–20 linhas cada). Comparar convergência. Burden & Faires — *Numerical Analysis*. Meta: **implementar 5 métodos + resolver 2 problemas de convergência**.

---

### 🟢 PRIORIDADE #12 — Física Mecânica e Eletromagnetismo (0.13, 0.14)

> **Meta:** 10 exercícios combinados. Residual ~50%.

**Ex 0.13.01** — 2ª Lei de Newton (plano inclinado) ★★☆

**Problema:** Bloco de 5 kg num plano inclinado a 30°, $\mu_k = 0.2$. Ache a aceleração.

**Resposta:** $a = g(\sin\theta - \mu_k\cos\theta) = 9.8(0.5 - 0.2 \times 0.866) = 9.8(0.5 - 0.173) = 9.8 \times 0.327 = 3.2$ m/s².

---

**Ex 0.13.02** — Conservação de energia ★★☆

**Problema:** Pêndulo de 2 kg, comprimento 1 m, solto de $\theta = 60°$. Velocidade no ponto mais baixo?

**Resposta:** $h = L(1 - \cos 60°) = 1 \times 0.5 = 0.5$ m. $v = \sqrt{2gh} = \sqrt{2 \times 9.8 \times 0.5} = \sqrt{9.8} = 3.13$ m/s.

---

**Ex 0.13.03** — Termodinâmica: ciclo de Carnot ★★☆

**Problema:** Motor de Carnot: $T_H = 600$ K, $T_C = 300$ K. Eficiência? Se $Q_H = 1000$ J, quanto trabalho produz?

**Resposta:** $\eta = 1 - T_C/T_H = 1 - 300/600 = 50\%$. $W = \eta Q_H = 0.5 \times 1000 = 500$ J.

---

**Ex 0.14.01** — Lei de Gauss ★★☆

**Problema:** Esfera condutora de raio 10 cm com carga $Q = 5\,\mu$C. Campo elétrico a 20 cm do centro?

**Resposta:** Fora da esfera: $E = \frac{Q}{4\pi\varepsilon_0 r^2} = \frac{5 \times 10^{-6}}{4\pi \times 8.854 \times 10^{-12} \times 0.04} = \frac{5 \times 10^{-6}}{4.45 \times 10^{-12}} = 1.124 \times 10^6$ V/m $\approx 1.12$ MV/m.

---

**Ex 0.14.02** — Lei de Ampère (solenoide) ★★☆

**Problema:** Solenoide: 500 espiras, comprimento 25 cm, corrente 2 A. Campo magnético interno?

**Resposta:** $n = 500/0.25 = 2000$ espiras/m. $B = \mu_0 n I = 4\pi \times 10^{-7} \times 2000 \times 2 = 5.03 \times 10^{-3}$ T $\approx 5$ mT.

---

**Ex 0.14.03** — Lei de Faraday ★★★

**Problema:** Espira quadrada de lado 10 cm numa região onde $B$ varia: $B(t) = 0.5\sin(120\pi t)$ T. Ache a fem induzida máxima.

**Resposta:** $\Phi = B \times A = 0.5\sin(120\pi t) \times 0.01$ Wb. $\varepsilon = -d\Phi/dt = -0.5 \times 0.01 \times 120\pi\cos(120\pi t) = -1.885\cos(120\pi t)$ V. $|\varepsilon_{max}| = 1.885$ V $\approx 1.9$ V.

---

> 📘 **Mais exercícios:** Halliday/Resnick — *Fundamentals of Physics* (Cap. 4–8 mecânica, Cap. 21–32 EM). Griffiths — *Introduction to Electrodynamics* para EM avançado. Meta: **5 de mecânica + 5 de EM, incluindo pelo menos 1 problema de Gauss escolhendo superfície e 1 de Faraday**.

---

## 3. Recursos com Imagens e Diagramas

Alguns exercícios exigem diagramas visuais (circuitos complexos, Bode plots, diagramas de corpo livre, Root Locus). Aqui estão os recursos gratuitos:

### Circuitos
| Recurso | Tipo | Exercícios | Link |
|---------|------|:----------:|------|
| **Falstad Circuit Simulator** | Simulador online | ∞ (montar próprios) | falstad.com/circuit/ |
| **MIT OCW 6.002** | Curso + p-sets | ~80 problemas com diagramas | ocw.mit.edu |
| **All About Circuits** | Textbook online | ~200 problemas resolvidos | allaboutcircuits.com/textbook/ |
| **Khan Academy: Electrical Engineering** | Vídeos + exercícios | ~50 | khanacademy.org |

**Quantidade recomendada:** 15–20 circuitos completos com diagramas (dos p-sets do MIT ou All About Circuits).

### Controle
| Recurso | Tipo | Exercícios | Link |
|---------|------|:----------:|------|
| **MATLAB/Simulink** | Software | ∞ (verificação) | mathworks.com (licença estudante) |
| **Brian Douglas (YouTube)** | Vídeos | ~20 exemplos resolvidos | youtube.com/@BrianDouglas |
| **MIT OCW 6.003 / 6.302** | P-sets | ~40 problemas | ocw.mit.edu |
| **Control Tutorials (Univ. Michigan)** | Tutoriais MATLAB | ~15 | ctms.engin.umich.edu |

**Quantidade recomendada:** Traçar **10 Root Locus à mão** + **10 Bode à mão** + verificar cada um no MATLAB.

### Eletrônica
| Recurso | Tipo | Exercícios | Link |
|---------|------|:----------:|------|
| **LTspice** | Simulador gratuito | ∞ | analog.com/ltspice |
| **MIT OCW 6.012** | P-sets | ~30 problemas com esquemáticos | ocw.mit.edu |
| **Electronics Tutorials** | Textbook online | ~100 | electronics-tutorials.ws |

**Quantidade recomendada:** **5 circuitos simulados** no LTspice (retificador, amplificador CE, amp-op, filtro ativo, fonte regulada).

### Física
| Recurso | Tipo | Exercícios | Link |
|---------|------|:----------:|------|
| **Physics Classroom** | Exercícios interativos | ~200 | physicsclassroom.com |
| **MIT OCW 8.01/8.02** | P-sets clássicos | ~60 | ocw.mit.edu |
| **HyperPhysics** | Referência + calculadoras | Conceitual | hyperphysics.phy-astr.gsu.edu |

**Quantidade recomendada:** **5 de mecânica + 5 de EM**, priorizando diagramas de corpo livre e superfícies gaussianas.

---

## 4. Esquema de Interleaving Semanal

### Fase de Estudo Ativo (módulo novo)

```
Dia 1: 3 exercícios do módulo em estudo (★☆☆ e ★★☆)
Dia 2: 2 exercícios do módulo + 1 de revisão (módulo anterior)
Dia 3: 2 de revisão (módulos anteriores) + 1 novo (★★★)
Dia 4: Descanso de exercícios (só Anki)
Dia 5: 1 exercício integrativo (★★★) + 2 revisões aleatórias
```

### Fase de Manutenção (após terminar módulo)

```
3×/semana, 30-45 min:
  - Sorteie 3 exercícios de módulos DIFERENTES deste banco
  - Se acertou rápido: marque ✓ e prossiga
  - Se travou: refaça + revise os cards Anki relacionados
  - Se errou: refaça + adicione à lista de "refazer em 2 semanas"
```

### Tabela de Rotação (Sugestão Semanal)

| Semana | Sessão 1 | Sessão 2 | Sessão 3 |
|:------:|----------|----------|----------|
| 1 | 1.01 (★★☆) + 0.17 (★☆☆) | 5.02 (★☆☆) + 0.05 (★★☆) | 1.01 (★★★) + 0.17 (★★☆) |
| 2 | 4.04 (★☆☆) + 2.01 (★★☆) | 0.09 (★★☆) + 6.01 (★☆☆) | 5.02 (★★☆) + 0.11 (★★☆) |
| 3 | 1.01 (revisão) + 0.17 (★★★) | 0.07 (★★☆) + 0.14 (★★☆) | 4.04 (★★☆) + 0.18 (★★☆) |
| 4 | 5.02 (★★★) + 2.03 (★★☆) | 6.01 (★★☆) + 0.13 (★★☆) | Revisão: 3 exercícios errados anteriores |
| **Repetir ciclo** com exercícios novos do banco ou dos livros indicados.

---

## 5. Tracking de Progresso

Copie a tabela abaixo e use-a para rastrear quais exercícios fez:

```markdown
| Exercício | Data | Acertou? | Tempo | Refazer? | Notas |
|-----------|------|:--------:|:-----:|:--------:|-------|
| Ex 1.01   |      |          |       |          |       |
| Ex 1.02   |      |          |       |          |       |
| ...       |      |          |       |          |       |
```

### Critérios de "Acertou"
- ✅ **Acertou limpo**: resultado correto, método correto, sem consultar cards
- ⚠️ **Acertou com hesitação**: resultado correto, mas consultou card ou hesitou no método
- ❌ **Errou**: resultado errado ou método errado

**Meta:** Quando ≥80% dos exercícios forem ✅ (limpo) num módulo, aquele módulo está dominado — passe para manutenção.

---

## 6. Resumo de Volumes

| Módulo | Exercícios neste banco | Exercícios extras recomendados (livros/online) | Total sugerido |
|--------|:----------------------:|:-----------------------------------------------:|:--------------:|
| 1.01 Circuitos | 14 | 15 (MIT OCW + Sadiku) | ~30 |
| 0.17 Probabilidade | 11 | 8 (Walpole) | ~20 |
| 5.02 Controle | 10 | 8 (Ogata + MATLAB) | ~18 |
| 4.04 Potência | 6 | 7 (Stevenson) | ~13 |
| 0.05 Cálculo 1 | 7 | 10 (Stewart) | ~17 |
| 0.07 Cálculo 2/3 | 5 | 5 (Stewart) | ~10 |
| 0.09 EDOs | 5 | 5 (Boyce & DiPrima) | ~10 |
| 0.11 Álg. Linear | 4 | 4 (Strang) | ~8 |
| 2.01–2.03 Eletrônica | 5 | 5 (Sedra/Smith + LTspice) | ~10 |
| 6.01 Telecom | 5 | 5 (Haykin) | ~10 |
| 0.18 Mét. Numéricos | 4 | 5 (Burden + Python) | ~9 |
| 0.13+0.14 Física | 6 | 10 (Halliday) | ~16 |
| **TOTAL** | **82** | **87** | **~171** |

> Este banco + referências externas cobrem integralmente o "residual" apontado na ANALISE_DRILLS.md.
> Estimativa de tempo para completar tudo: **8–12 semanas** em ritmo de 3 sessões/semana.
