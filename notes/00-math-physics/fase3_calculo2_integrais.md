# Módulo 0 — Matemática/Física: Fase 3 — Cálculo II: Integrais
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 0.11 — O Conceito de Integral

### Conceito

- Integral = >> "{{Quanto no total}}" — área sob a curva = acumulação
- Derivada vs Integral >> Derivada = "quão rápido?", Integral = "{{quanto acumulou?}}"
- ∫ₐᵇ f(x)dx = >> {{F(b) - F(a)}}, onde F'(x) = f(x) (Teorema Fundamental)
- Derivada e integral são >> Operações {{inversas}}: ∫f'(x)dx = f(x) + C

### Integrais Básicas

- ∫k dt = >> {{kt + C}}
- ∫tⁿ dt = >> {{tⁿ⁺¹/(n+1) + C}} (n ≠ -1)
- ∫e^(at) dt = >> {{e^(at)/a + C}}
- ∫sin(ωt) dt = >> {{-cos(ωt)/ω + C}}
- ∫cos(ωt) dt = >> {{sin(ωt)/ω + C}}
- ∫1/t dt = >> {{ln|t| + C}}
- ∫1/(1+t²) dt = >> {{arctan(t) + C}}

### Definida vs Indefinida

- Integral definida >> Resultado é um {{número}}
- Integral indefinida >> Resultado é uma {{função + C}}
- Constante C representa >> A {{condição inicial}} (ex: V_C(0) = 0)

### Integrais na EE

- Tensão no capacitor >> V_C = {{(1/C) ∫I dt}}
- Energia consumida >> E = {{∫P(t) dt}}
- Carga elétrica >> Q = {{∫I(t) dt}}
- Energia no capacitor >> E = {{½CV²}} (derivada por ∫₀^V CV dV)
- Energia no indutor >> E = {{½LI²}} (derivada por ∫₀^I LI dI)

Intuição: potência P(t) é taxa de consumo de energia. ∫P(t)dt é a energia total — é literalmente o que o medidor da sua casa calcula para a conta de luz.

### Drill — Integrais Definidas #[[Drill]]

- ∫₀¹ 3t² dt = >> {{1}}
- ∫₀^(π/ω) sin(ωt) dt = >> {{2/ω}}
- ∫₀^∞ e^(-t/τ) dt = >> {{τ}}

---

## Módulo 0.12 — Técnicas de Integração

### Substituição (u-sub)

- Quando usar? >> Quando integral tem forma {{∫f(g(x))·g'(x) dx}} — reversa da cadeia

### Por Partes

- Fórmula >> ∫u dv = {{uv - ∫v du}}
- Mnemônico LIATE para escolher u >> {{Log, Inversa trig, Algébrica, Trig, Exponencial}} — u é o mais à esquerda

Integração por partes aparece em demonstrações de energia e processamento de sinais. Na prática diária, use SymPy — o importante é RECONHECER quando aplicar.

### Frações Parciais

- Objetivo >> Decompor fração complexa em frações {{simples}} da tabela de Laplace
- 1/((s+a)(s+b)) = >> {{A/(s+a) + B/(s+b)}}
- Cover-up: para achar A >> Cubra (s+a), substitua s={{-a}} no resto
- Por que importa? >> TODA {{inversa de Laplace}} precisa dessa decomposição!

### Drill — Técnicas #[[Drill]]

- ∫sin(3t)·3 dt = >> {{-cos(3t) + C}}
- ∫2t·e^(t²) dt = >> {{e^(t²) + C}}
- ∫t·e^(-t) dt = >> {{-(t+1)e^(-t) + C}}
- 5/((s+1)(s+2)): A, B = >> A={{5}}, B={{-5}}

---

## Módulo 0.13 — Aplicações de Integrais

### Valor Médio e RMS

- V_avg num período T >> {{(1/T) ∫₀ᵀ v(t) dt}}
- V_avg de senóide pura >> {{0}} (positivo cancela negativo)
- V_rms (fórmula) >> {{√((1/T) ∫₀ᵀ v²(t) dt)}}
- V_rms de senóide <> {{V_p/√2}} ≈ 0.707·V_p
- V_rms de onda quadrada <> {{V_p}} (100% do pico)
- V_rms de onda triangular <> {{V_p/√3}} ≈ 0.577·V_p
- Significado de V_rms >> Valor DC que produz a {{mesma potência média}} no mesmo R

### Tensões Brasileiras

- 127V da tomada é valor >> {{RMS}}. Pico = {{179.6V}}
- 220V da tomada é valor >> {{RMS}}. Pico = {{311V}}
- Fator de crista de senóide (V_p/V_rms) <> {{√2}} ≈ 1.414

### Energia

- Fórmula geral de energia >> E = {{∫₀ᵗ P(τ) dτ}}

### Drill — RMS e Energia #[[Drill]]

- V_p = 340V. V_rms = >> {{240V}}
- V_rms = 127V. V_pp = >> {{359V}}
- Chuveiro 5500W × 15min = >> {{1.375 kWh}}
- Fator de crista de onda quadrada = >> {{1}}

---

## Módulo 0.14 — Integrais Impróprias e Métodos Numéricos

### Integrais Impróprias

- ∫₀^∞ e^(-at) dt = >> {{1/a}} (a > 0)
- ∫₀^∞ 1/t dt = >> {{∞}} — DIVERGE!
- Convergência: ∫₁^∞ 1/tⁿ converge se >> n > {{1}}
- Se integral diverge na EE >> Sistema {{instável}}!

### Conexão com Laplace

- Transformada de Laplace >> F(s) = {{∫₀^∞ f(t)·e^(-st) dt}} — é uma integral imprópria!

Energia total dissipada na descarga RC: ∫₀^∞ I²R dt = ½CV₀² — TODA a energia do capacitor vira calor no resistor. A integral converge porque e^(-t/τ) decai → sistema estável.

### Métodos Numéricos

- Quando usar integração numérica? >> Quando a integral {{não tem forma fechada}} ou é muito complexa
- Ferramenta Python >> {{scipy.integrate.quad()}}

### Drill — Impróprias #[[Drill]]

- ∫₀^∞ e^(-t/τ) dt = >> {{τ}}
- ∫₀^∞ 1/t² dt = >> {{1}} (converge)
