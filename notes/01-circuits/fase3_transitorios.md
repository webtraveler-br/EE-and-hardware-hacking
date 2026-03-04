# Módulo 1 — Circuitos: Fase 3 — Transitórios
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 1.10 — Capacitores

### Física e fórmulas

- Capacitor armazena energia em >> {{campo elétrico}} (entre placas paralelas isoladas)
- Capacitância [F] <> {{Farad}}
- Carga armazenada: Q = >> {{C × V}}
- Energia armazenada: $E_C$ = >> {{½CV²}}
- Corrente no capacitor: I = >> {{C × dV/dt}}

I é proporcional à TAXA DE VARIAÇÃO da tensão. Sem variação (DC estável), I = 0.

### Associação

- Capacitores em série somam como resistores em >> {{paralelo}} (1/C_eq = 1/C₁ + 1/C₂)
- Capacitores em paralelo somam como resistores em >> {{série}} (C_eq = C₁ + C₂)

### Transitório RC

- Constante de tempo RC: τ = >> {{R × C}} [segundos]
- Carga/descarga completa (99.3%) ocorre após >> {{5τ}}
- Capacitor em regime permanente DC (t → ∞) → comporta-se como >> {{circuito aberto}} (dV/dt = 0)
- Capacitor em t = 0 (descarregado, fonte abrupta) → comporta-se como >> {{curto-circuito}}

### Conexão HH: Bypass e ESR

- Capacitor de bypass (desacoplamento) >> Capacitor em paralelo com V_cc do chip para fornecer {{picos de corrente rápidos}} e estabilizar tensão
- Valor típico de bypass em lógica digital >> {{100nF}} cerâmico, o mais perto possível do chip
- ESR <> {{Equivalent Series Resistance}} (resistência parasita interna do capacitor)
- Impacto da ESR >> Dissipa potência como $I_{ripple}^2 × ESR$ → {{aquecimento}} (pode explodir em fontes chaveadas)

Sem bypass, microcontrolador reseta quando relé liga. O cap cerâmico rápido absorve o surto de corrente.

### Drill — Capacitores #[[Drill]]

- 100nF ∥ 100nF = >> {{200nF}}
- 100nF série 100nF = >> {{50nF}}
- R=10kΩ, C=1μF → τ = >> {{10ms}}
- Mesmo RC, carga completa (5τ) = >> {{50ms}}
- Supercap 1F, V=5V → E = >> {{12.5J}} (½×1×25)
- C=100μF, V=3.3V → Q = >> {{330μC}} (C×V)

---

## Módulo 1.11 — Indutores

### Física e fórmulas

- Indutor armazena energia em >> {{campo magnético}} (gerado pela corrente na bobina)
- Indutância [H] <> {{Henry}}
- Energia armazenada: $E_L$ = >> {{½LI²}}
- Tensão no indutor: V = >> {{L × dI/dt}}

Indutor = roda d'água pesada. Resiste a mudanças bruscas de corrente.

### Associação e transitório RL

- Indutores em série somam como >> {{resistores em série}} (L_eq = L₁ + L₂)
- Indutores em paralelo somam como >> {{resistores em paralelo}}
- Constante de tempo RL: τ = >> {{L / R}}
- Indutor em regime permanente DC → comporta-se como >> {{curto-circuito}} (fio, V = 0)
- Indutor em t = 0⁺ (fonte abrupta) → comporta-se como >> {{circuito aberto}}

### Perigos de bancada

- Interromper corrente num indutor (relé/motor) → gera >> {{spike de tensão inversa}} (centenas de volts, V = L×dI/dt com dI/dt enorme)
- Proteção contra flyback >> {{Diodo de roda-livre}} (flyback diode, em antiparalelo com a carga indutiva)
- Corrente de saturação ($I_{sat}$) >> Se I > I_sat, a indutância {{colapsa}} (nucleo satura → vira fio → sobrecorrente)

### Drill — Indutores #[[Drill]]

- R=1kΩ, L=10μH → τ = >> {{10ns}} (L/R)
- R=100Ω, L=1mH → τ = >> {{10μs}}
- Relé 12V, L=50mH, I=100mA desligado abruptamente em 1μs → V_spike ≈ >> {{5000V}} (L×dI/dt)
- Indutor DC estável 5A → V nos terminais = >> {{0V}} (dI/dt = 0)

---

## Módulo 1.12 — Circuito RLC e Ressonância

### Equação e frequência natural

- RLC série gera equação diferencial de >> {{2ª ordem}} (segunda ordem linear)
- Frequência natural (não amortecida): $f_0$ = >> {{$1 / (2\pi\sqrt{LC})$}}
- Frequência angular natural: $\omega_0$ = >> {{$1 / \sqrt{LC}$}}

### Tipos de amortecimento

R age como atrito. L e C são forças opostas (inércia magnética vs elasticidade elétrica).

- $\alpha > \omega_0$ → >> {{Superamortecido}} (decai lento, sem oscilação)
- $\alpha < \omega_0$ → >> {{Subamortecido}} (oscila antes de estabilizar)
- $\alpha = \omega_0$ → >> {{Criticamente amortecido}} (mais rápido possível sem oscilar)

### Ressonância

- Na frequência $f_0$, reatâncias se cancelam: $X_L = X_C$ → Z = >> {{R puro}} (mínima impedância em série)
- Fator de qualidade (Q) alto significa >> Pico de ressonância {{mais estreito e seletivo}}

Ressonância: de pontes civis vibrando com vento até sintonizadores de rádio AM/FM e filtros WiFi 5GHz.

### Drill — RLC #[[Drill]]

- L=1mH, C=1μF → $f_0$ = >> {{~5033 Hz}} ($1/(2\pi\sqrt{10^{-3}×10^{-6}})$)
- L=100μH, C=100pF → $f_0$ = >> {{~1.59 MHz}} (faixa de rádio AM)
- RLC série na ressonância: R=50Ω, V=10V_rms → I = >> {{200mA}} (Z=R=50Ω)

---

## ⏱ Módulo 1.13 — Aplicações: Timer 555 e PWM

### Timer NE555

- O 555 opera usando limiares de >> {{⅓V_cc e ⅔V_cc}} para controlar carga/descarga de capacitor externo
- Modo astável >> Oscilador: gera {{onda quadrada}} contínua (clock)
- Modo monoestável >> Pulso único: trigger dispara {{um pulso de duração fixa}}

### PWM

- PWM <> {{Pulse-Width Modulation}} (modulação por largura de pulso)
- Duty cycle <> {{Fração do período em nível alto}} (0% a 100%)
- V_média de um PWM >> {{V_max × duty cycle}}
- Vantagem energética do PWM vs divisor resistivo >> Eficiência ~{{100%}} (chaveamento, sem dissipação em R)

PWM + filtro RC = DAC barato. PWM a 25% de 5V → 1.25V médio no multímetro. Controla LEDs, motores, aquecedores.

### Drill — PWM #[[Drill]]

- PWM 5V, duty 25% → V_média = >> {{1.25V}}
- PWM 3.3V, duty 50% → V_média = >> {{1.65V}}
- PWM 12V, duty 75% → V_média = >> {{9V}}
- Motor precisa de 9V a partir de 12V → duty = >> {{75%}}
