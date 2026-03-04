# Módulo 2 — Eletrônica: Fase 3 — Amplificadores Operacionais
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 2.9: Amp-Op — Regras de Ouro

### O amplificador operacional

- Amp-Op >> Amplificador diferencial com ganho de malha aberta ≈ {{infinito}} (real: 10⁵-10⁶)
- 2 entradas >> V⁺ (não-inversora) e V⁻ (inversora). $V_{out} = A × (V⁺ - V⁻)$
- Alimentação >> ±V_CC (tipicamente ±12V ou ±15V). Saída limitada a {{±V_CC}}

### Regras de ouro (com realimentação negativa)

- Regra 1 >> $V⁺ =$ {{$V⁻$}} (tensão nas entradas é igual)
- Regra 2 >> $I⁺ = I⁻ =$ {{0}} (corrente nas entradas é zero)
- As regras de ouro só valem com >> Realimentação {{negativa}} (saída → V⁻)
- Com realimentação positiva → amp-op vira >> {{Oscilador}} ou comparador (satura em ±V_CC)

### Configurações clássicas

- Buffer (seguidor de tensão) >> Saída conectada a V⁻. Ganho = {{1}}, Z_out ≈ 0
- Amplificador inversor >> $A_V =$ {{$-R_F / R_1$}}
- Amplificador não-inversor >> $A_V =$ {{$1 + R_F / R_1$}}
- Somador inversor >> $V_{out} =$ {{$-(R_F/R_1 × V_1 + R_F/R_2 × V_2)$}}
- Diferencial (subtrator) >> $V_{out} = (V_2 - V_1) ×$ {{$R_F / R_1$}} (quando R_F/R_1 = R_2'/R_2)

### Amp-ops clássicos

- LM741 <> {{Vintage}} (lento, baixo CMRR — para aprender)
- LM358 <> {{Dual, rail-to-rail input}} (barato, genérico)
- TL072 <> {{Áudio}} (baixo ruído, JFET input)
- LM324 <> {{Quad}} (4 amp-ops num chip)

Amp-op = canivete suíço. Com R e C externos vira: amplificador, somador, filtro, oscilador, regulador, fonte de corrente...

### Drill — Amp-Op Básico #[[Drill]]

- Inversor: R_1=1kΩ, R_F=10kΩ → A_V = >> {{−10}}
- Não-inversor: R_1=1kΩ, R_F=9kΩ → A_V = >> {{+10}}
- Inversor: R_1=10kΩ, R_F=100kΩ, V_in=0.1V → V_out = >> {{−1V}}
- Não-inversor: R_1=1kΩ, R_F=4kΩ, V_in=1V → V_out = >> {{5V}} (1+4/1=5)
- Somador: R_F=10k, R_1=R_2=10k, V_1=1V, V_2=2V → V_out = >> {{−3V}}
- Buffer: V_in=3.3V → V_out = >> {{3.3V}} (ganho 1)

---

## Módulo 2.10: Filtros Ativos e Instrumentação

### Filtros ativos

- Vantagem do filtro ativo sobre passivo >> Ganho ≥ 1, Z_out baixa, {{cascateável}} sem perda
- Sallen-Key <> {{Topologia}} popular para filtros de 2ª ordem
- Butterworth >> Resposta {{plana}} na passband (sem ripple) — mais usado
- Roll-off 2ª ordem >> {{−40 dB/dec}} (vs −20 dB/dec do passivo 1ª ordem)

### Integrador e diferenciador

- Integrador >> $V_{out} =$ {{$-(1/RC) \int V_{in} \, dt$}} (acumula sinal no tempo)
- Diferenciador >> $V_{out} =$ {{$-RC × dV_{in}/dt$}} (detecta mudanças rápidas)

### Amplificador de instrumentação (INA)

- INA >> {{3 amp-ops}} que amplificam diferença entre 2 sinais, rejeitando ruído comum
- CMRR <> {{Common Mode Rejection Ratio}} (quanto ruído comum é rejeitado)
- INA clássico <> {{INA128}} (ganho ajustável por 1 resistor)

### Drill — Filtros #[[Drill]]

- Filtro passivo RC 1ª ordem: roll-off = >> {{−20 dB/dec}}
- Filtro Sallen-Key 2ª ordem: roll-off = >> {{−40 dB/dec}}
- Integrador com onda quadrada na entrada → saída = >> {{Triangular}} (integral de constante = rampa)
- Diferenciador com onda triangular na entrada → saída = >> {{Quadrada}} (derivada de rampa = constante)

---

##  Módulo 2.11: Osciladores e Geradores de Sinal

### Condição de oscilação

- Critério de Barkhausen >> Ganho de malha = {{1}} e fase de malha = {{0°}} (ou 360°)
- Oscilador Wien Bridge >> Gera {{senóide}} pura. $f = 1/(2\pi RC)$
- Oscilador Colpitts/Hartley >> Usa {{LC}}, para frequências mais altas (RF)
- Astável com amp-op >> Gera {{onda quadrada}} (comparador + RC + realimentação positiva)

### Gerador de funções (cadeia de conversão)

- Senóide → quadrada >> Passar por {{comparador}} (amp-op sem realimentação)
- Quadrada → triangular >> Passar por {{integrador}} (amp-op + cap)

### Drill — Osciladores #[[Drill]]

- Wien Bridge: R=10kΩ, C=10nF → f = >> {{~1.59 kHz}} ($1/(2\pi×10^4×10^{-8})$)
- Wien Bridge: R=1kΩ, C=100nF → f = >> {{~1.59 kHz}} (mesma! diferentes R/C, mesmo RC)
- Barkhausen: ganho de malha = 0.9 → oscila? >> {{Não}} (precisa ser ≥ 1)

---

## Módulo 2.12: Fonte de Bancada Regulada (Projeto Integrado)

### Regulador LM317

- LM317 >> Regulador linear {{ajustável}} (1.25V a 37V)
- Fórmula: $V_{out}$ = >> {{$1.25 × (1 + R_2/R_1)$}}
- Dropout voltage >> $V_{in}$ deve ser pelo menos {{~2V}} acima de $V_{out}$

### Proteções

- Limitação de corrente >> R_sense + BJT pass-transistor → limita {{$I_{max}$}} da saída
- Heat sink >> $P_{dissipada}$ = {{$(V_{in} - V_{out}) × I_{out}$}} → calcular dissipador térmico

### Drill — LM317 #[[Drill]]

- R_1=240Ω, R_2=720Ω → V_out = >> {{5V}} (1.25×(1+720/240) = 1.25×4)
- R_1=240Ω, R_2=1.5kΩ → V_out = >> {{9.06V}} (1.25×(1+1500/240))
- V_in=12V, V_out=5V, I=1A → P_dissipada = >> {{7W}} ((12−5)×1)
- P=7W com θ_JA=50°C/W → ΔT = >> {{350°C}} — precisa de heatsink!
