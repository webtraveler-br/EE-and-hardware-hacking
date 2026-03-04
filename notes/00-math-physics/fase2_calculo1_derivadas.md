# Módulo 0 — Matemática/Física: Fase 2 — Cálculo I: Derivadas
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 0.7 — Limites e Continuidade

### Conceito de Limite

- Limite de f(x) quando x→a >> O valor que f(x) {{se aproxima}} — não precisa ser f(a)
- Derivada como limite >> f'(x) = lim(h→0) {{[f(x+h) - f(x)] / h}}

### Limites Fundamentais

- lim(x→0) sin(x)/x = >> {{1}}
- lim(x→∞) (1+1/x)ˣ = >> {{e}}
- lim(x→∞) e^(-x) = >> {{0}}
- lim(x→0) (eˣ-1)/x = >> {{1}}

### L'Hôpital

- Quando usar L'Hôpital? >> Quando o limite dá {{0/0}} ou {{∞/∞}}
- Regra >> lim f/g = lim {{f'/g'}} — derive num e denom separadamente

Continuidade: f é contínua em a se lim(x→a) f(x) = f(a). Sem "buraco" nem "salto". Exemplo EE: conforme f → f₀ num RLC série, |Z| → mínimo (ressonância) — é um limite!

### Drill — Limites #[[Drill]]

- lim(x→∞) 1/x = >> {{0}}
- lim(x→0⁺) ln(x) = >> {{-∞}}
- lim(x→0) sin(x)/x por L'Hôpital >> = lim cos(x)/1 = {{1}} OK

---

## Módulo 0.8 — Derivadas: Taxa de Variação

### Conceito

- Derivada = >> {{Taxa de mudança instantânea}} — "quão rápido muda AGORA?"

### Regras de Derivação

- d/dt[constante] = >> {{0}}
- d/dt[tⁿ] = >> {{n·tⁿ⁻¹}}
- d/dt[eᵃᵗ] = >> {{a·eᵃᵗ}}
- d/dt[sin(ωt)] = >> {{ω·cos(ωt)}}
- d/dt[cos(ωt)] = >> {{-ω·sin(ωt)}}
- d/dt[ln(t)] = >> {{1/t}}
- d/dt[tan(t)] = >> {{sec²(t)}}
- Regra da cadeia: d/dt[f(g(t))] = >> {{f'(g(t))·g'(t)}}
- Regra do produto: d/dt[f·g] = >> {{f'g + fg'}}
- Regra do quociente: d/dt[f/g] = >> {{(f'g - fg') / g²}}

### Conexão EE — Capacitor e Indutor

- Corrente no capacitor >> I = {{C·dV/dt}}
- Tensão no indutor >> V = {{L·dI/dt}}
- Início da carga RC: I é >> {{máxima}} — dV/dt é grande
- Final da carga RC: I é >> {{zero}} — dV/dt ≈ 0

Intuição: derivada de sin é cos — quando sin está no pico (parado), cos = 0. Quando sin cruza zero (taxa máxima), cos = máximo. São defasados 90°.

### Erros Clássicos

- d/dt[e^(-t/τ)] = >> {{(-1/τ)·e^(-t/τ)}} — nunca esqueça a cadeia!
- eˣ vs xⁿ >> d/dx[eˣ] = {{eˣ}}, d/dx[xⁿ] = {{nxⁿ⁻¹}} — regras DIFERENTES

### Drill — Derivadas #[[Drill]]

- d/dt[5t³] = >> {{15t²}}
- d/dt[3sin(100t)] = >> {{300cos(100t)}}
- d/dt[12(1-e^(-t/τ))] = >> {{(12/τ)·e^(-t/τ)}}
- d/dt[t·e^(-t)] = >> {{e^(-t)·(1-t)}}
- d/dt[e^(-2t)·cos(10t)] = >> {{e^(-2t)[-2cos(10t) - 10sin(10t)]}}
- d/dt[sin²(ωt)] = >> {{ω·sin(2ωt)}}
- d/dt[1/(1+t²)] = >> {{-2t/(1+t²)²}}

---

## Módulo 0.9 — Aplicações de Derivadas

### Máximo e Mínimo

- Para encontrar extremos >> Faça f'(x) = {{0}} e analise f''(x)
- f'=0 e f''<0 → >> {{Máximo}}
- f'=0 e f''>0 → >> {{Mínimo}}
- f''=0 → >> {{Ponto de inflexão}}

### Otimização EE

- Máxima transferência de potência >> R_carga = {{R_Thévenin}}
- Linearização (Taylor 1ª ordem) >> f(x) ≈ {{f(a) + f'(a)·(x-a)}}

Modelo de pequenos sinais do transistor = linearização de Taylor da curva I-V no Q-point. O engenheiro "congela" a função no ponto de operação e usa só a reta tangente.

### Taxa Relacionada

- V = IR, I varia, R fixo: dV/dt = >> {{R·dI/dt}}
- P = V·I, ambos variam: dP/dt = >> {{(dV/dt)·I + V·(dI/dt)}}

---

## Módulo 0.10 — Séries de Taylor e Aproximações

### Maclaurin (a=0)

- eˣ = >> {{1 + x + x²/2! + x³/3! + ...}}
- sin(x) = >> {{x - x³/3! + x⁵/5! - ...}}
- cos(x) = >> {{1 - x²/2! + x⁴/4! - ...}}

### Aproximações para |x|≪1

- eˣ ≈ >> {{1 + x}}
- sin(x) ≈ >> {{x}}
- cos(x) ≈ >> {{1 - x²/2}}
- (1+x)ⁿ ≈ >> {{1 + nx}}
- ln(1+x) ≈ >> {{x}}
- 1/(1+x) ≈ >> {{1 - x}}
- tan(x) ≈ >> {{x}}

### Convergência

- Série de eˣ converge para >> {{todo x real}}
- Série de ln(1+x) converge para >> {{|x| ≤ 1}}
- Série geométrica 1/(1-x) converge para >> {{|x| < 1}}

Aplicação: sin(x) ≈ x é usado em oscilações de pequena amplitude e modelo de pequenos sinais. A condutância de pequenos sinais do diodo (gm = I_Q/(nV_T)) vem de linearizar I = I_s·e^(V/nV_T) por Taylor.
