# Módulo 1 — Circuitos: Fase 4 — Corrente Alternada (AC)
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 1.14 — Senóides e Valores RMS

### Parâmetros da onda AC

- Diferença fundamental DC vs AC >> Em AC o fluxo {{inverte de sentido periodicamente}}
- Forma da onda da rede elétrica >> {{Senoidal}} pura
- Valor pico-a-pico: $V_{pp}$ = >> {{2 × V_pico}}
- 127V ou 220V da tomada é valor de >> {{RMS}} (não é o pico)

### RMS (Root Mean Square)

- Significado físico de V_RMS >> Valor AC que dissipa a {{mesma potência}} que um DC equivalente
- $V_{RMS}$ para senoide >> {{$V_{pico} / \sqrt{2}$}}
- $1/\sqrt{2}$ ≈ >> {{0.707}}
- $V_{pico}$ a partir de $V_{RMS}$ >> {{$V_{RMS} × \sqrt{2}$}} (≈ V_RMS × 1.414)

### Período e frequência

- Frequência [Hz] <> {{ciclos/segundo}}
- Período: T = >> {{1/f}} [segundos]
- Frequência angular: ω <> {{2πf}} [rad/s]

### Drill — AC/RMS #[[Drill]]

- Rede BR 60Hz → T = >> {{16.67ms}}
- Tomada 220V_RMS → V_pico = >> {{311V}}
- Tomada 127V_RMS → V_pico = >> {{180V}}
- V_pico=10V, R=10Ω → P = >> {{5W}} (V_RMS=7.07V, P=V²/R=50/10)
- 380V_RMS trifásico → V_pico = >> {{537V}}
- Rede 50Hz (Europa) → T = >> {{20ms}}

---

##  Módulo 1.15 — Impedância

### Conceito

- Impedância (Z) >> Oposição total ao fluxo AC, grandeza {{complexa}} [Ω]
- Z = >> {{R + jX}} (parte real = resistência, parte imaginária = reatância)

### Reatâncias

- Reatância indutiva: $X_L$ <> {{$\omega L$}} = $2\pi fL$
- Reatância capacitiva: $X_C$ <> {{$1/(\omega C)$}} = $1/(2\pi fC)$
- DC (f → 0): capacitor → >> {{aberto}} ($X_C$ → ∞), indutor → >> {{curto}} ($X_L$ → 0)
- Alta frequência (f → ∞): capacitor → >> {{curto}} ($X_C$ → 0), indutor → >> {{aberto}} ($X_L$ → ∞)

### Defasagem: ELI the ICE man

- ELI >> No indutor (L), tensão (E) está {{adiantada}} 90° em relação à corrente (I)
- ICE >> No capacitor (C), corrente (I) está {{adiantada}} 90° em relação à tensão (E)

Mnemônico ELI the ICE man: E-L-I = tensão lidera no indutor; I-C-E = corrente lidera no capacitor.

### Drill — Impedância #[[Drill]]

- ω=2 rad/s, L=5H → $X_L$ = >> {{10Ω}}
- f=1kHz, C=1μF → $X_C$ = >> {{~159Ω}} ($1/(2\pi×1000×10^{-6})$)
- R=100Ω, $X_L$=100Ω → |Z| = >> {{~141Ω}} ($\sqrt{100^2+100^2}$)
- Indutor puro, I = 10A∠0° → fase de V = >> {{+90°}} (ELI)

---

##  Módulo 1.16 — Fasores

### Conceito e notação

- Fasor >> Transforma equações diferenciais senoidais em {{álgebra de números complexos}}
- Fasor do indutor: $Z_L$ = >> {{$j\omega L$}}
- Fasor do capacitor: $Z_C$ = >> {{$1/(j\omega C)$}} = $-jX_C$
- Lei de Ohm fasorial <> {{$\vec{V} = \vec{Z} × \vec{I}$}}

Todos os teoremas de DC (Thévenin, Nodal, Superposição) funcionam em AC — basta trocar R por Z(complexo) e usar álgebra complexa.

### Operações com fasores

- Soma/subtração de Z → usar forma >> {{retangular}} (A ± jB)
- Multiplicação/divisão → usar forma >> {{polar}} (|Z|∠θ)
- Retangular → Polar: módulo = >> {{$\sqrt{A^2 + B^2}$}}
- Retangular → Polar: ângulo = >> {{$\arctan(B/A)$}}

### Drill — Fasores #[[Drill]]

- Z₁ = 3+j4 → |Z₁| = >> {{5Ω}} ($\sqrt{9+16}$)
- Z₁ = 3+j4 → ∠Z₁ = >> {{53.1°}} ($\arctan(4/3)$)
- Z = 10∠30° × 2∠15° → >> {{20∠45°}} (multiplica módulos, soma ângulos)
- Z = 10∠60° / 2∠20° → >> {{5∠40°}} (divide módulos, subtrai ângulos)

---

## Módulo 1.17 — Potência em AC

### Os 3 tipos de potência

- Potência Aparente (S) >> V_RMS × I_RMS, unidade: {{VA (Volt-Ampère)}}
- Potência Ativa (P) >> Fração útil que vira trabalho, unidade: {{W (Watts)}}
- Potência Reativa (Q) >> Fração que oscila sem gerar trabalho, unidade: {{VAr}}

O triângulo de potência: S é a hipotenusa, P é o cateto adjacente, Q é o cateto oposto.

### Fator de potência

- P = >> {{S × cos(φ)}}
- Q = >> {{S × sin(φ)}}
- Fator de Potência (FP) <> {{cos(φ) = P/S}}
- FP = 1 → carga puramente >> {{resistiva}} (V e I em fase)
- FP baixo em fábricas → causado por cargas >> {{indutivas}} (motores)
- Correção de FP >> Instalar {{banco de capacitores}} em paralelo (injeta Q reativa oposta)

Concessionária multa FP < 0.92. Capacitores anulam Q indutivo, elevando FP sem aumentar P real.

### Drill — Potência AC #[[Drill]]

- S=10kVA, FP=0.8 → P = >> {{8kW}}
- S=10kVA, FP=0.8 → Q = >> {{6kVAr}} ($S × \sin(\arccos(0.8))$)
- V=220V_RMS, I=10A, FP=1 → P = >> {{2200W}}
- V=220V_RMS, I=10A, FP=0.5 → P = >> {{1100W}} (metade vira Q)

---

##  Módulo 1.18 — Filtros e Diagrama de Bode

### Filtros RC de 1ª ordem

- R série + C para GND → >> {{Filtro Passa-Baixas (LPF)}} (C curta altas frequências)
- C série + R para GND → >> {{Filtro Passa-Altas (HPF)}} (C bloqueia DC)
- Frequência de corte (-3dB): $f_c$ <> {{$1/(2\pi RC)$}}

### Filtros RLC de 2ª ordem

- Tanque LC paralelo (L e C para GND) + R série → >> {{Filtro Passa-Banda}} (ressonância em $f_0$ deixa passar)
- Na ressonância, L rouba graves e C rouba agudos → sobra a frequência {{central}} ($X_L = X_C$)

Filtro passa-banda = sintonizador de rádio. Variando C ou L, muda a estação.

### Decibéis e Bode

- dB (potência): $G_{dB}$ = >> {{$10 \log_{10}(P_{out}/P_{in})$}}
- dB (tensão): $G_{dB}$ = >> {{$20 \log_{10}(V_{out}/V_{in})$}}
- −3dB em tensão corresponde a >> {{$0.707 × V_{in}$}} (metade da potência)
- Diagrama de Bode >> Dois gráficos: {{Magnitude (dB) × log(f)}} + Fase (°) × log(f)

Por que log? Porque frequências vão de Hz a GHz (9 ordens de magnitude) e audição/sensores humanos são logarítmicos.

### Drill — Filtros e dB #[[Drill]]

- R=10kΩ, C=10nF → $f_c$ = >> {{~1.59kHz}}
- Amplificador: V_in=1mV, V_out=1V → ganho em dB = >> {{60dB}} ($20\log(1000)$)
- Filtro atenua para −20dB → V_out/V_in = >> {{0.1}} ($10^{-20/20}$)
- Sinal a −3dB do pico → potência relativa = >> {{50%}} (0.707² ≈ 0.5)
- Dois estágios de −20dB em cascata → ganho total = >> {{−40dB}}
