# Módulo 0 — Matemática/Física: Fase 8 — Mecânica e Termodinâmica
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 0.26: Leis de Newton, Energia e Potência

### Leis e analogias

- 1ª Lei (Inércia) >> Corpo em repouso permanece em repouso. Análogo: {{indutor}} resiste a mudança de corrente
- 2ª Lei >> $F = ma$. Análogo elétrico: $V =$ {{$L × dI/dt$}}
- 3ª Lei >> Ação e reação. Em circuitos: {{Lei de Lenz}} (reação se opõe à ação)

### Energia

- Energia cinética >> $E_c = \frac{1}{2}mv^2$. Análogo: $E_L =$ {{$\frac{1}{2}LI^2$}}
- Energia potencial >> $E_p = mgh$. Análogo: $E_C =$ {{$\frac{1}{2}CV^2$}}
- Potência >> $P = dW/dt = F × v$. Em EE: $P =$ {{$V × I$}}
- Eficiência >> $\eta =$ {{$P_{saída} / P_{entrada}$}} × 100%

### Analogias (tabela)

- Força (F) <> {{Tensão (V)}}
- Velocidade (v) <> {{Corrente (I)}}
- Massa (m) <> {{Indutância (L)}}
- Mola (1/k) <> {{Capacitância (C)}}
- Atrito (b) <> {{Resistência (R)}}

### Drill — Newton #[[Drill]]

- Motor 250W por 2h → energia = >> {{1.8 MJ}} (ou 0.5 kWh)
- Motor P_in=500W, P_mec=400W → η = >> {{80%}}, P_calor = {{100W}}
- $E_L$ com L=10mH, I=5A = >> {{0.125 J}} (½×0.01×25)
- $E_C$ com C=1mF, V=10V = >> {{0.05 J}} (½×0.001×100)

---

## Módulo 0.27: Rotação e Torque

### Rotacional

- Torque >> $\tau = r × F$. Unidade: {{N·m}}. "Força que faz girar"
- 2ª Lei rotacional >> $\tau =$ {{$J × \alpha$}} ($J$ = momento de inércia, $\alpha$ = aceleração angular)
- Potência rotacional >> $P =$ {{$\tau × \omega$}} ($\omega$ em rad/s)
- RPM → rad/s >> $\omega =$ {{$n × 2\pi / 60$}}
- 1800 RPM = >> {{188.5 rad/s}}
- Energia cinética rotacional >> $E =$ {{$\frac{1}{2}J\omega^2$}}

### Motor

- Motor >> Converte $P_{elétrica} = V × I$ em $P_{mecânica} =$ {{$\tau × \omega$}}
- Mais torque com mesma potência → motor gira >> {{Mais devagar}} (redutor/engrenagem)

### Drill — Rotação #[[Drill]]

- Motor 3CV (2237W), 1740RPM → τ = >> {{~12.3 N·m}} (P/ω = 2237/182.2)
- Motor 1CV (746W), 3600RPM → τ = >> {{~1.98 N·m}}
- Dobrar torque com mesma potência → velocidade = >> {{Metade}}

---

##  Módulo 0.28: Oscilações e Ondas

### MHS e ressonância

- MHS >> $x(t) = A \sin(\omega t + \phi)$. Mola: $\omega = \sqrt{k/m}$. RLC: $\omega_0 =$ {{$1/\sqrt{LC}$}}
- Amortecimento >> Exponencial × senóide = oscilação que {{decai}}. Exatamente como RLC subamortecido
- Ressonância >> Excitar na frequência {{natural}} → amplitude máxima (filtros, antenas, rádio)

### Ondas

- Velocidade da onda >> $v =$ {{$f × \lambda$}}
- Superposição >> Ondas se somam {{linearmente}} (interferência construtiva/destrutiva)
- Ondas EM >> $c =$ {{$3 × 10^8$}} m/s

### Drill — Ondas #[[Drill]]

- FM 100MHz → λ = >> {{3m}} (c/f)
- WiFi 2.4GHz → λ = >> {{12.5 cm}}
- 5G 28GHz → λ = >> {{~1.07 cm}}
- Luz visível 600THz → λ = >> {{500 nm}}

---

## Módulo 0.29: Termodinâmica e Circuitos Térmicos

### Conceitos térmicos

- Calor >> $Q = mc\Delta T$. Energia transferida por diferença de temperatura
- Condução >> $\dot{Q} = kA(\Delta T)/L$. Análogo: I = ΔV/R → $R_{th} =$ {{$L/(kA)$}}
- Circuito térmico >> $T_{junção} = T_{amb} +$ {{$P × R_{th}$}}

### Dissipadores (essencial para potência!)

- $R_{th}$ total >> $R_{th,jc} + R_{th,cs} +$ {{$R_{th,sa}$}} (junção→case + case→sink + sink→ar)
- Potência máxima >> $P_{max} =$ {{$(T_{j,max} - T_{amb}) / R_{th,total}$}}
- $T_{j,max}$ típica de MOSFET/BJT >> {{150°C}} (acima = destruição)

Dissipação térmica é o LIMITADOR prático de todo circuito de potência. Se não dissipar $I^2 R_{DS(on)}$, o MOSFET morre. Circuito térmico = circuito elétrico com temperatura=tensão, calor=corrente.

### Drill — Térmica #[[Drill]]

- MOSFET: $R_{DS(on)}$=50mΩ, I=10A → P_dissipada = >> {{5W}} ($I^2 R$)
- $R_{th,jc}$=1.5, $R_{th,cs}$=0.5, $R_{th,sa}$=5 → $R_{th,total}$ = >> {{7 °C/W}}
- P=5W, $R_{th}$=7°C/W, $T_{amb}$=40°C → $T_j$ = >> {{75°C}} (40+5×7, OK!)
- P=20W, $R_{th}$=7, $T_{amb}$=40 → $T_j$ = >> {{180°C}} (40+140 → ACIMA de 150 → queima!)
- $T_{j,max}$=150°C, $T_{amb}$=40°C, P=20W → $R_{th,sa}$ máx = >> {{3 °C/W}} ((150−40)/20 − 2 = 3.5, arredondar)
