# Módulo 2 — Eletrônica: Fase 2 — Transistores
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 2.5: BJT como Chave

### Terminais e modos

- BJT (Bipolar Junction Transistor) — 3 terminais <> {{Base (B), Coletor (C), Emissor (E)}}
- NPN >> Corrente entra pela {{Base}}, sai pelo Emissor
- PNP >> Tudo {{invertido}} (corrente entra pelo Emissor)
- $V_{BE}$ quando conduzindo ≈ >> {{0.7V}} (junção B-E = diodo)

### Modo chave

- Saturação (chave LIGADA) >> $I_B >$ {{$I_C / \beta$}} → $V_{CE}$ ≈ 0.2V
- Corte (chave DESLIGADA) >> $V_{BE} <$ {{0.7V}} → $I_C$ = 0
- β (hFE) <> {{Ganho de corrente}} ($I_C = \beta × I_B$). Típico: 100-300
- Cálculo de R_base >> $R_B =$ {{$(V_{in} - 0.7) / I_B$}}
- Fator de segurança para saturação >> $I_B = I_C / \beta_{min} ×$ {{2 a 5}} (nunca usar β típico!)

### Regras práticas HH

- Por que usar $\beta_{min}$ e não $\beta_{típico}$? >> β varia {{muito}} entre transistores do mesmo lote (75-300)
- Carga indutiva (motor, relé) no coletor → obrigatório >> {{Diodo flyback}}
- BJT chave clássico <> {{2N2222}} (NPN, até 800mA)

BJT como chave: 1mA na base → 200mA no coletor. Relé eletrônico que um GPIO de 3.3V pode controlar.

### Drill — BJT Chave #[[Drill]]

- Motor 200mA, β_min=100, fator 3× → I_B = >> {{6mA}} (200/100×3)
- R_B para I_B=6mA com V_in=5V → >> {{716Ω}} ((5−0.7)/0.006) → usar 680Ω
- GPIO 3.3V, R_B=1kΩ → I_B = >> {{2.6mA}} ((3.3−0.7)/1000)
- I_B=2.6mA, β_min=100 → I_C máx garantida = >> {{260mA}}
- V_CC=5V, LED(V_f=2V), R_C=150Ω, V_CE_sat=0.2V → I_LED = >> {{~18.7mA}} ((5−2−0.2)/150)

---

## Módulo 2.6: BJT como Amplificador

### Região ativa

- Região ativa >> $V_{BE}$ ≈ 0.7V e $V_{CE} >$ {{$V_{CE,sat}$}} → $I_C = \beta × I_B$ (linear)
- Q-point (ponto de operação) >> Polarização DC que coloca o transistor no {{meio}} da região ativa

### Polarização e ganho

- Polarização mais estável >> {{Divisor de tensão}} na base
- Capacitores de acoplamento >> Bloqueiam {{DC}}, passam AC (isolam estágios)
- Ganho de tensão (emissor comum): $A_V$ = >> {{$-g_m × R_C$}} = $-(I_C/V_T) × R_C$
- Sinal negativo no ganho significa >> {{Inversão de fase}} (180°)
- $g_m$ (transcondutância do BJT) = >> {{$I_C / V_T$}} (V_T ≈ 26mV)

### Modelo de pequenos sinais

- Análise DC vs AC >> Primeiro {{polarize}} (DC, Q-point), depois analise sinal (AC, modelo π)
- Clipping >> Sinal de entrada grande demais → saída {{corta}} (atinge saturação ou corte)

Polarizar = posicionar bola no meio da ladeira. Empurrão (AC) gera movimento (amplificação). No topo (saturação) ou fundo (corte), nada se move.

### Drill — Amplificador #[[Drill]]

- V_CC=12V, I_C=1mA, V_CE=6V → R_C = >> {{6kΩ}} ((12−6)/0.001)
- I_C=1mA → g_m = >> {{38.5 mA/V}} (0.001/0.026)
- g_m=38.5mA/V, R_C=6kΩ → |A_V| = >> {{231}} (g_m × R_C)
- V_in=10mV_pp → V_out ≈ >> {{2.31V_pp}} (231 × 10mV)
- Saída máxima antes de clipping ≈ >> {{V_CE(Q)}} ≈ 6V_pp (excursão simétrica)

---

## Módulo 2.7: MOSFET

### Características

- MOSFET — 3 terminais <> {{Gate (G), Drain (D), Source (S)}}
- Gate é >> {{Isolado}} (corrente de gate ≈ 0, impedância infinita)
- N-channel enhancement: liga quando $V_{GS} >$ >> {{$V_{th}$}} (threshold, típico 1.5-4V)
- $R_{DS(on)}$ <> {{Resistência drain-source quando ligado}} (1mΩ a 1Ω)

### MOSFET vs BJT

- BJT é controlado por >> {{corrente}} (I_B) | MOSFET por >> {{tensão}} (V_GS)
- $R_{DS(on)}$ do MOSFET vs $V_{CE,sat}$ do BJT >> MOSFET tem {{muito menos}} perda em potência
- MOSFET não consome corrente no gate em DC >> Eficiência {{superior}} para chaveamento
- Gate charge >> Para chavear rápido, precisa {{carregar/descarregar}} a capacitância do gate
- Driver de gate >> Circuito que fornece {{corrente de pico}} para carregar o gate rapidamente

### Aplicações

- H-Bridge >> 4 MOSFETs para controlar motor DC em {{2 direções}} (frente/ré)
- Dead-time na H-Bridge >> Tempo morto entre chaves para evitar {{shoot-through}} (curto V_CC → GND)
- PWM no gate >> Controla {{velocidade}} do motor (duty cycle → V_média)
- MOSFET logic-level <> {{IRLZ44N}} (V_th baixo, liga com 3.3V/5V direto do GPIO)
- MOSFET potência clássico <> {{IRF540N}} (precisa de driver, V_th ≈ 2-4V)

Quase todo circuito de potência moderno usa MOSFET: fontes chaveadas, inversores, ESCs de drone, VFDs.

### Drill — MOSFET #[[Drill]]

- IRF540N: $R_{DS(on)}$=44mΩ, I=10A → P_perda = >> {{4.4W}} ($I^2 × R_{DS(on)}$)
- BJT equivalente: $V_{CE,sat}$=0.3V, I=10A → P_perda = >> {{3W}} ($V_{CE} × I$)
- Quem perde mais em 10A? >> {{MOSFET}} (4.4W vs 3W) — mas em correntes menores MOSFET ganha
- $R_{DS(on)}$=10mΩ, I=5A → P_perda = >> {{0.25W}} (MOSFET ganha fácil)
- Motor 12V/5A, PWM duty=50% → V_média no motor = >> {{6V}}

---

## Módulo 2.8: Projeto Integrado — Chaveamento e Amplificação

### Topologias avançadas

- Darlington >> 2 BJTs em cascata → $\beta_{total}$ = {{$\beta_1 × \beta_2$}} (ganho enorme)
- $V_{CE,sat}$ do Darlington ≈ >> {{1.4V}} (2 × 0.7V — desvantagem)
- Push-Pull (Classe B/AB) >> NPN + PNP → saída pode {{source e sink}} corrente
- Crossover distortion >> Distorção na passagem por {{0V}} (zona morta dos 0.7V de cada transistor)
- Correção de crossover >> Polarização com {{diodos}} (classe AB)

### Classes de amplificação

- Classe A <> Eficiência {{~25%}} (sempre conduzindo, sem distorção)
- Classe B <> Eficiência {{~78.5%}} (cada transistor conduz metade do ciclo)
- Classe AB <> Eficiência {{~50-70%}} (compromisso: pouca distorção, boa eficiência)
- Classe D <> Eficiência {{>90%}} (chaveamento PWM, filtro na saída)

### Drill — Topologias #[[Drill]]

- Dois BJTs com β=100 em Darlington → β_total = >> {{10.000}}
- Amplificador classe A, P_CC=10W → P_saída máx ≈ >> {{2.5W}} (η≈25%)
- Amplificador classe D, P_CC=10W → P_saída máx ≈ >> {{9W}} (η>90%)
- Push-Pull sem polarização, sinal 1V_pp → zona morta de >> {{±0.7V}} (crossover)
