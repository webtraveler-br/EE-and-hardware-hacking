# Módulo 4 — Eletrotécnica: Fase 1 — Sistemas Trifásicos
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 4.1: Geração e Sistema Trifásico

### Por que trifásico?

- Monofásico em motores → potência instantânea >> {{Pulsa e passa por zero}} (vibração, torque inconstante)
- Vantagem do trifásico no estator >> Cria {{campo magnético girante}} uniforme e constante
- 3 circuitos monofásicos separados precisariam de >> {{6 fios}} (3 fases + 3 neutros)
- Trifásico equilibrado precisa de apenas >> {{3 fios}} (economia de cobre)
- Frequência da rede brasileira <> {{60 Hz}}

### Estrela (Y)

- Defasagem entre fases Va, Vb, Vc >> {{120°}} ($2\pi/3$ rad)
- Ponto central da estrela <> {{Neutro}}
- Tensão de fase ($V_F$) >> Medida entre uma fase e o {{neutro}}
- Tensão de linha ($V_L$) >> Medida entre {{duas fases}}
- Relação Y — tensão: $V_L$ = >> {{$\sqrt{3} × V_F$}}
- Relação Y — corrente: $I_L$ = >> {{$I_F$}} (mesma corrente, série)

### Triângulo (Δ)

- Conexão Δ possui neutro? >> {{Não}} (não há nó central)
- Relação Δ — tensão: $V_L$ = >> {{$V_F$}} (mesma tensão, paralelo)
- Relação Δ — corrente: $I_L$ = >> {{$\sqrt{3} × I_F$}}

### Prática de campo (Brasil)

- $V_F$ = 127V → $V_L$ = >> {{220V}} ($127 × \sqrt{3}$)
- $V_L$ = 380V → $V_F$ = >> {{220V}} ($380 / \sqrt{3}$)
- Inverter 2 das 3 fases no motor → >> Motor gira ao {{contrário}} (sequência de fases invertida)

### Drill — Y/Δ #[[Drill]]

- Rede Δ, $V_L$ = 380V → $V_F$ na bobina = >> {{380V}} ($V_L = V_F$ em Δ)
- Rede Δ, $I_L$ = 30A → $I_F$ na bobina = >> {{17.3A}} ($30/\sqrt{3}$)
- Rede Y, $I_L$ = 10A → $I_F$ = >> {{10A}} ($I_L = I_F$ em Y)
- Rede Y, $V_F$ = 220V → $V_L$ = >> {{381V}} ($220 × \sqrt{3}$)
- Rede Y, $V_L$ = 440V → $V_F$ = >> {{254V}} ($440/\sqrt{3}$)

---

## Módulo 4.2: Potência Trifásica e Fator de Potência

### Fórmulas de potência trifásica (valores de linha)

- Corrente no neutro de sistema equilibrado = >> {{0}} (cargas idênticas se cancelam)
- $S_{3\phi}$ (aparente) = >> {{$\sqrt{3} × V_L × I_L$}} [VA]
- $P_{3\phi}$ (ativa) = >> {{$\sqrt{3} × V_L × I_L × \cos\phi$}} [W]
- $Q_{3\phi}$ (reativa) = >> {{$\sqrt{3} × V_L × I_L × \sin\phi$}} [VAr]

O $\sqrt{3}$ aparece porque usamos $V_L$ e $I_L$ (valores de linha). Se usar $V_F$ e $I_F$ diretamente: $S = 3 × V_F × I_F$ (3 monofásicos).

### Fator de potência industrial

- Limite mínimo de FP exigido pela ANEEL >> {{0.92}} (abaixo disso, multa)
- Causa principal de FP baixo na indústria >> {{Motores de indução}} (carga indutiva consome Q)
- Correção de FP >> Instalar {{banco de capacitores}} em paralelo (injeta Q⁻ compensando Q⁺)
- Corrigir FP reduz a potência ativa P? >> {{Não}} — reduz S e I na linha, desafogando a rede

### Drill — Potência trifásica #[[Drill]]

- $V_L$=380V, $I_L$=100A, FP=0.85 → P = >> {{~56kW}} ($\sqrt{3}×380×100×0.85$)
- S=100kVA, P=80kW → FP = >> {{0.80}} (P/S)
- FP=0.80 → toma multa ANEEL? >> {{Sim}} (< 0.92)
- $V_L$=220V, $I_L$=50A, FP=1.0 → P = >> {{~19kW}} ($\sqrt{3}×220×50$)

---

## Módulo 4.3: Medição e Instrumentação Trifásica

### Transformadores de instrumentos

- TC <> {{Transformador de Corrente}} (reduz I para o medidor, ex: 200/5A)
- TP <> {{Transformador de Potencial}} (reduz V para o medidor, ex: 13.8kV/115V)
- TC 200/5A com 200A no primário → secundário = >> {{5A}}

### Medição de potência

- Wattímetro >> Instrumento que mede {{potência ativa}} (cruza V e I internamente)
- Teorema de Blondel: sistema 3 fios (sem neutro) → quantos wattímetros? >> {{2}}
- Potência total (MDW) = >> {{$W_1 + W_2$}}
- $W_2$ lê negativo quando FP < >> {{0.5}} (fenômeno fasorial normal, não defeito)

### Drill — TC #[[Drill]]

- TC 500/5A, medidor lê 2A → corrente real no cabo = >> {{200A}} (regra de três: 2×500/5)
- TC 100/5A, corrente real 80A → medidor lê = >> {{4A}} (80×5/100)
- TC 1000/5A, medidor lê 3.5A → corrente real = >> {{700A}}
