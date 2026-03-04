# Módulo 2 — Eletrônica: Fase 4 — Eletrônica de Potência
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 2.13: Reguladores Lineares vs Chaveados

### Regulador linear

- Regulador linear >> Dissipa excesso de tensão como {{calor}} ($P = (V_{in} - V_{out}) × I$)
- Eficiência do linear: η = >> {{$V_{out} / V_{in}$}}
- Exemplo: 12V→5V a 1A → $P_{out}$=5W, $P_{perda}$=7W, η = >> {{42%}} (mais calor que útil!)
- Vantagem >> Sem {{ruído de chaveamento}} (bom para áudio e instrumentação)
- 7805 <> {{Regulador linear fixo 5V}} (TO-220, dropout ~2V)

### Regulador chaveado

- Regulador chaveado >> Liga/desliga em alta frequência (100kHz-2MHz) + filtro LC → η = {{85-95%}}
- Vantagens sobre linear >> Pode {{subir}} tensão (boost), {{inverter}} polaridade, eficiência muito maior
- Desvantagem >> Gera {{ruído EMI}} (harmônicos do chaveamento)

Linear = registro parcialmente aberto (desperdiça pressão em calor). Chaveado = registro que abre/fecha rápido (controla fluxo médio pelo duty cycle, quase sem perda).

### Drill — Linear vs Chaveado #[[Drill]]

- 7805: V_in=9V, I=500mA → P_perda = >> {{2W}} ((9−5)×0.5)
- 7805: V_in=24V, I=1A → P_perda = >> {{19W}} (precisa de heatsink enorme!)
- Chaveado 12V→5V, η=90%, I=1A → P_perda = >> {{0.56W}} (P_out/η − P_out = 5.56−5)
- Bateria Li-Ion 3.7V para USB 5V → precisa de >> {{Boost}} (linear não sobe tensão)

---

##  Módulo 2.14: Conversor Buck (Step-Down)

### Princípio

- Buck >> DC-DC que {{abaixa}} tensão. $V_{out} = D × V_{in}$
- D (duty cycle) = >> {{$V_{out} / V_{in}$}}
- Componentes >> MOSFET (chave) + diodo (freewheeling) + {{indutor}} (armazenamento) + {{capacitor}} (filtro)

### Funcionamento

- MOSFET ON → indutor {{carrega}} (corrente sobe, armazena energia)
- MOSFET OFF → indutor {{descarrega}} pela carga via diodo freewheeling (corrente desce)
- CCM (Continuous Conduction Mode) >> Corrente no indutor nunca chega a {{zero}} — preferível
- Ripple de corrente: $\Delta I$ = >> {{$(V_{in} - V_{out}) × D / (f × L)$}}
- Frequência de chaveamento >> 100kHz-2MHz. Maior f → componentes {{menores}} mas mais perdas

### Chips clássicos

- LM2596 <> {{Buck regulador}} clássico (3A, 150kHz, adjustable)

Buck = caixa d'água com boia. MOSFET (torneira) enche, indutor (caixa) mantém, capacitor (boia) estabiliza. Duty cycle controla o nível médio.

### Drill — Buck #[[Drill]]

- 12V→5V → D = >> {{41.7%}} (5/12)
- 24V→3.3V → D = >> {{13.8%}} (3.3/24)
- 12V→5V, f=100kHz, L=100μH → $\Delta I$ = >> {{~29mA}} ((12−5)×0.417/(10⁵×10⁻⁴))
- V_in=5V, D=60% → V_out = >> {{3V}}
- Buck com η=90%: P_out=10W → P_in = >> {{11.1W}} (10/0.9)

---

##  Módulo 2.15: Conversor Boost (Step-Up) e Buck-Boost

### Boost

- Boost >> DC-DC que {{sobe}} tensão. $V_{out} = V_{in} / (1-D)$
- D=50% → $V_{out}$ = >> {{$2 × V_{in}$}} (1/(1−0.5) = 2)
- Princípio >> MOSFET ON: indutor carrega. MOSFET OFF: indutor "empurra" corrente em tensão {{mais alta}}

### Buck-Boost

- Buck-Boost >> Pode subir OU descer. $V_{out} = -V_{in} × D / (1-D)$. Saída {{invertida}}!
- Uso típico >> Quando V_in pode ser {{maior ou menor}} que V_out (ex: bateria descarregando)

### Aplicações

- Power bank: 3.7V (Li-Ion) → 5V (USB) >> {{Boost}}
- LED driver: bateria variável → corrente constante >> {{Buck-Boost}} ou {{Boost}}
- Alimentação de sensor em campo: 12V→24V >> {{Boost}}

### Drill — Boost/Buck-Boost #[[Drill]]

- Boost: 3.7V→5V → D = >> {{26%}} (1 − 3.7/5 = 0.26)
- Boost: 5V→12V → D = >> {{58.3%}} (1 − 5/12)
- Boost: D=75% → $V_{out}/V_{in}$ = >> {{4×}} (1/(1−0.75) = 4)
- Buck-Boost: $V_{in}$=12V, D=50% → $V_{out}$ = >> {{−12V}} (−12×0.5/0.5, invertida!)
- Bateria Li-Ion 3.0-4.2V → saída fixa 3.3V → topologia = >> {{Buck-Boost}} (V_in cruza V_out)

---

## Módulo 2.16: Inversores e Fontes Chaveadas

### Inversores DC→AC

- Inversor >> Converte {{DC → AC}} usando H-bridge + PWM
- SPWM (Sinusoidal PWM) >> PWM cuja largura varia {{senoidalmente}} → filtro LC → senóide limpa
- Full-bridge inversor >> {{4 MOSFETs}} com gate drivers
- THD (Total Harmonic Distortion) >> Quanto menor, mais {{pura}} a senóide de saída

### Fontes chaveadas isoladas

- Flyback >> Usa {{transformador de alta frequência}} para isolação. Baixa potência (carregadores de celular)
- Forward >> Média potência. Transfere energia {{durante}} o pulso ON
- Half-bridge / Full-bridge >> Alta potência. Mais eficiente

### Topologias por potência

- Flyback <> {{<100W}} (carregador celular, adapter)
- Forward <> {{100-500W}} (fonte de PC)
- Full-bridge <> {{>500W}} (fontes industriais, inversores solares)

### Drill — Inversores e Fontes #[[Drill]]

- Carregador USB 5V/2A → potência = >> {{10W}} → topologia provável = >> {{Flyback}}
- Fonte de PC 500W → topologia provável = >> {{Forward ou Half-bridge}}
- Inversor solar 3kW → topologia = >> {{Full-bridge}}
- H-bridge com V_DC=48V → V_AC máxima (pico) = >> {{48V}} (amplitude máxima = V_DC)
