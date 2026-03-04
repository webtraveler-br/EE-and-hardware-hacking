# Módulo 5 — Controle e Sinais: Fase 2 — Modelagem e Laplace
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 5.4: Transformada de Laplace

### Definição

- Laplace >> $F(s) = \int_0^\infty f(t) × e^{-st} \, dt$, onde $s =$ {{$\sigma + j\omega$}}
- $s = j\omega$ → Laplace vira >> {{Fourier}} (são a mesma ferramenta!)

### Transformadas essenciais

- $1$ ↔ >> {{$1/s$}}
- $t$ ↔ >> {{$1/s^2$}}
- $e^{-at}$ ↔ >> {{$1/(s+a)$}}
- $\sin(\omega t)$ ↔ >> {{$\omega/(s^2+\omega^2)$}}
- $\cos(\omega t)$ ↔ >> {{$s/(s^2+\omega^2)$}}

### Propriedades

- Derivada no tempo: $\mathcal{L}\{f'(t)\}$ = >> {{$sF(s) - f(0^-)$}} (derivada → multiplicação por s!)
- Integral no tempo: $\mathcal{L}\{\int f(t)\}$ = >> {{$F(s)/s$}}
- Inversa >> Frações parciais → {{tabela de transformadas}} → f(t)

Laplace faz para EDOs o que log faz para multiplicação: transforma algo difícil (cálculo) em algo fácil (álgebra). Circuito RC no tempo = EDO. Em Laplace: $V_C(s) = V_{in}(s) × 1/(1+sRC)$. Muito mais fácil.

### Drill — Laplace #[[Drill]]

- $\mathcal{L}\{u(t)\}$ = >> {{$1/s$}}
- $\mathcal{L}\{e^{-3t}\}$ = >> {{$1/(s+3)$}}
- $\mathcal{L}^{-1}\{1/(s+5)\}$ = >> {{$e^{-5t}$}}
- $\mathcal{L}^{-1}\{1/s^2\}$ = >> {{$t$}} (rampa)
- Derivada de $f(t)$ em Laplace com $f(0)=0$ → >> {{$sF(s)$}}

---

## Módulo 5.5: Função de Transferência e Blocos

### Função de transferência

- G(s) = >> {{$Y(s) / X(s)$}} (saída/entrada em Laplace, condições iniciais zero)
- Polos >> Raízes do {{denominador}}. Determinam estabilidade e forma da resposta
- Zeros >> Raízes do {{numerador}}. Influenciam amplitude e fase

### Sistemas de 1ª e 2ª ordem

- 1ª ordem: $G(s)$ = >> {{$K / (\tau s + 1)$}}. τ = constante de tempo, K = ganho estático
- 2ª ordem: $G(s)$ = >> {{$K\omega_n^2 / (s^2 + 2\zeta\omega_n s + \omega_n^2)$}}
- ζ (zeta) <> {{Fator de amortecimento}}
- ω_n <> {{Frequência natural}}
- $\zeta < 1$ → >> {{Subamortecido}} (oscila)
- $\zeta = 1$ → >> {{Criticamente amortecido}} (mais rápido sem oscilar)
- $\zeta > 1$ → >> {{Superamortecido}} (lento, sem oscilação)

### Álgebra de blocos

- Blocos em série >> {{Multiplicação}} ($G_1 × G_2$)
- Blocos em paralelo >> {{Soma}} ($G_1 + G_2$)
- Realimentação negativa >> {{$G / (1 + GH)$}}

Polos = pesos cósmicos. Real negativo = estável (exp decrescente). Real positivo = INSTÁVEL (exp crescente). Complexos = oscilação (amortecida se Re < 0).

### Drill — G(s) #[[Drill]]

- $G(s) = 10/(s+2)$ → polo em >> {{s = −2}} (estável)
- $G(s) = 5/(s−3)$ → polo em >> {{s = +3}} (instável!)
- Sistema com ζ=0.3 → comportamento = >> {{Subamortecido}} (oscila bastante)
- Sistema com ζ=0.707 → >> {{Subamortecido ótimo}} (pouco overshoot, bom compromisso)
- Série: $G_1=2/(s+1)$, $G_2=3/(s+2)$ → $G_{total}$ = >> {{$6/((s+1)(s+2))$}}
- Feedback: $G=10/s$, $H=1$ → $T(s)$ = >> {{$10/(s+10)$}} (G/(1+GH))

---

## Módulo 5.6: Modelagem de Sistemas Físicos

### Analogias entre domínios

- Tensão (V) <> Força (F) <> Temperatura (T) <> {{Pressão (P)}}
- Corrente (I) <> Velocidade (v) <> Fluxo de calor <> {{Vazão (Q)}}
- R elétrica <> Atrito (b) <> $R_{th}$ <> {{$R_{hidráulica}$}}
- C elétrica <> Massa (m) <> $MC_p$ <> {{Área (A)}}
- L elétrica <> Mola (1/k) <> — <> {{Inércia}}

### Modelos clássicos

- Sistema térmico (forno) >> $G(s) =$ {{$K / (\tau s + 1)$}} com $\tau = MC_p × R_{th}$
- Motor DC (velocidade) >> Relação tensão-velocidade via {{constante de torque}} e back-EMF
- Tanque de nível >> $G(s) =$ {{$1/(As)$}} (integrador puro)

Mesmas equações, variáveis diferentes! Se sabe resolver circuito RC, sabe resolver forno, tanque, mola-amortecedor.

### Drill — Modelagem #[[Drill]]

- Forno com τ=100s: após 1τ, temperatura atinge >> {{63%}} do valor final ($1 - e^{-1}$)
- Após 3τ >> {{95%}} do valor final
- Após 5τ >> {{~99%}} (considera-se regime permanente)
- Circuito RC com R=1kΩ, C=1μF → τ = >> {{1ms}} (RC)
- Mesmo circuit → $G(s) = 1/(1+$ >> {{$0.001s$}}$)$
