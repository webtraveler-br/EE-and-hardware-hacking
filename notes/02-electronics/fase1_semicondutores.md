# Módulo 2 — Eletrônica: Fase 1 — Semicondutores Básicos
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 2.1: O Diodo — A Válvula Eletrônica

### Junção PN e polarização

- Semicondutor tipo N >> Dopado com átomos de 5 e⁻ de valência (fósforo) → excesso de {{elétrons livres}}
- Semicondutor tipo P >> Dopado com átomos de 3 e⁻ de valência (boro) → excesso de {{lacunas}}
- Junção PN >> Cria {{zona de depleção}} (barreira de potencial na interface)
- Polarização direta >> Ânodo (+) mais positivo que cátodo (−) → {{corrente flui}}
- Polarização reversa >> Corrente ≈ {{0}} (apenas fuga em nA)
- Tensão de limiar — Silício <> {{~0.7V}}
- Tensão de limiar — Germânio <> {{~0.3V}}
- Tensão de limiar — LED <> {{~1.8-3.5V}} (depende da cor)

### Modelos do diodo

- Modelo ideal >> Chave: ON = {{curto}}, OFF = {{aberto}}
- Modelo prático >> Queda fixa de {{0.7V}} quando conduzindo
- Modelo exponencial >> $I = I_s × (e^{V/nV_T} - 1)$, onde $V_T$ ≈ {{26mV}} a 25°C
- Resistência dinâmica: $r_d$ = >> {{$V_T / I_D$}}

### Prática HH

- Ânodo do diodo (perna longa / triângulo) <> {{A}}
- Cátodo do diodo (perna curta / barra) <> {{K}}
- Resistor limitador de LED: R = >> {{$(V_{fonte} - V_{LED}) / I_{LED}$}}
- LED sem resistor → >> {{Queima}} (curva exponencial, I dispara)
- Diodo de sinal clássico <> {{1N4148}}
- Diodo de potência clássico <> {{1N4007}}

### Drill — Diodos #[[Drill]]

- LED vermelho (V_f=2V, I=20mA) com 5V → R = >> {{150Ω}} ((5−2)/0.02)
- LED azul (V_f=3.2V, I=20mA) com 5V → R = >> {{90Ω}} ((5−3.2)/0.02)
- Diodo Si em direta, I=1mA → r_d = >> {{26Ω}} (26mV/1mA)
- Diodo Si em direta, I=10mA → r_d = >> {{2.6Ω}}
- Tensão no diodo Si conduzindo ≈ >> {{0.7V}} (modelo prático)

---

## Módulo 2.2: Retificadores — De AC para DC

### Topologias

- Retificador meia-onda >> {{1 diodo}} → só passa semionda positiva
- $V_{DC}$ média (meia-onda) = >> {{$V_{pico} / \pi$}}
- Retificador onda completa (ponte de Graetz) >> {{4 diodos}} → ambas semiondas viram positivas
- $V_{DC}$ média (onda completa) = >> {{$2 × V_{pico} / \pi$}}
- Frequência do ripple (meia-onda, rede 60Hz) = >> {{60 Hz}}
- Frequência do ripple (onda completa, rede 60Hz) = >> {{120 Hz}}

### Filtro capacitivo

- Filtro capacitivo >> Capacitor em {{paralelo}} com a carga ("alisa" os pulsos)
- Ripple: $V_{ripple}$ ≈ >> {{$I_{carga} / (f × C)$}}
- Tensão DC com filtro (ponte) ≈ >> {{$V_{pico} - 2V_d$}} (~1.4V de queda nos 2 diodos da ponte)
- Regra prática: C para ripple < 10% >> $C >$ {{$I / (f_{ripple} × 0.1 × V_{DC})$}}

Toda fonte (carregador, TV, notebook) começa com retificador + filtro. AC senoidal → DC pulsante → DC "liso".

### Drill — Retificadores #[[Drill]]

- Trafo 9V_RMS → V_pico = >> {{12.7V}} (9×√2)
- Ponte retificadora com V_pico=12.7V → V_DC ≈ >> {{11.3V}} (12.7 − 2×0.7)
- Carga 100mA, C=1000μF, f=120Hz → V_ripple = >> {{0.83V}} (0.1/(120×0.001))
- Mesma carga com C=2200μF → V_ripple = >> {{0.38V}} (quase metade)
- Retificador meia-onda: ripple é pior porque f = >> {{60Hz}} (metade da ponte)

---

## Módulo 2.3: Diodo Zener — O Regulador Natural

### Princípio

- Zener opera em >> {{breakdown reverso}} (mantém V_Z constante)
- Regulador Zener >> R_série + Zener em paralelo com carga → $V_{carga}$ ≈ {{$V_Z$}}
- Dimensionamento de R_série >> $R = (V_{in} - V_Z) /$ {{$(I_Z + I_{carga})$}}
- $I_Z$ mínima para regulação >> ≈ {{5mA}}
- Potência no Zener: $P_Z$ = >> {{$V_Z × I_Z$}} (não exceder $P_{Z,max}$!)

Zener = válvula de pressão — entrada variável, saída constante. Simples mas ineficiente (excesso vira calor). Para regulação séria → LM7805 ou Buck.

### Drill — Zener #[[Drill]]

- $V_{in}$=12V, $V_Z$=5.1V, $I_{carga}$=20mA, $I_Z$=5mA → R = >> {{276Ω}} ((12−5.1)/0.025)
- Mesmo circuito: $P_Z$ = >> {{25.5mW}} (5.1×5mA)
- $V_{in}$ cai pra 7V, R=276Ω → I_total = >> {{6.9mA}} ((7−5.1)/276) — regulação marginal
- Zener 5.1V, $P_{max}$=500mW → $I_{Z,max}$ = >> {{98mA}} (500/5.1)

---

## Módulo 2.4: Aplicações de Diodos

### Circuitos com diodos

- Clipper (ceifador) >> {{Limita}} tensão a um valor máx/mín (diodo + R)
- Clamper (grampeador) >> {{Adiciona}} nível DC a sinal AC (cap + diodo)
- Proteção ESD >> Diodos nos pinos de {{I/O}} para absorver descargas
- Flyback (já visto em Pilar 1!) >> Diodo em antiparalelo com carga {{indutiva}}
- Dobrador de tensão >> Cascata de diodos + caps: $V_{out}$ ≈ {{$2 × V_{pico}$}}

### Drill — Aplicações #[[Drill]]

- Limitar sinal AC a ±5V → usar >> {{2 Zeners de 5.1V}} antiparalelo
- Senóide ±5V + clamper → saída = >> {{0 a 10V}} (desloca o nível DC)
- Dobrador: entrada 12V_pico AC → saída DC ≈ >> {{24V}}
