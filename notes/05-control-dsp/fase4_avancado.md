# Módulo 5 — Controle e Sinais: Fase 4 — Tópicos Avançados
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 5.11: Lugar das Raízes

### Conceito

- Lugar das raízes >> Gráfico dos polos de malha fechada variando {{$K$ de 0 a ∞}}
- Início ($K$=0) >> Nos {{polos}} de malha aberta
- Fim ($K$→∞) >> Nos {{zeros}} de malha aberta (ou no infinito)
- Cruzamento do eixo imaginário >> $K$ nesse ponto = {{$K_u$}} (ganho crítico)

### Design pelo root locus

- Especificação de ζ >> Traçar reta $\theta = \cos^{-1}(\zeta)$ → projetar K no {{cruzamento}}
- ω_n desejado >> Semicírculo de raio $\omega_n$ → K no {{cruzamento}}
- Compensador >> Adicionar polo/zero para {{moldar}} o root locus e atingir specs

Root locus é o "GPS visual" de controle: você vê exatamente para onde os polos vão conforme aumenta o ganho. Cruzou o eixo imaginário = instável.

### Drill — Root Locus #[[Drill]]

- $G(s) = 1/(s(s+2))$: quantos ramos? >> {{2}} (2 polos)
- Ramos partem de >> {{s=0 e s=−2}} (polos de malha aberta)
- Quando ramos cruzam eixo imaginário → K ali = >> {{$K_u$}} (sistema fica marginalmente estável)

---

## Módulo 5.12: Controle Digital

### Amostragem e Transformada Z

- Cadeia de controle digital >> Sensor → {{ADC}} → processamento → {{DAC}} → atuador
- Transformada Z >> Equivalente discreta de Laplace. $z =$ {{$e^{sT}$}}
- Estabilidade em Z >> Polos dentro do {{círculo unitário}} (|z| < 1)
- $z^{-1}$ >> Atraso de {{1 amostra}} (= registrador em hardware)
- Semiplano esquerdo (s) → >> {{Interior}} do círculo unitário (z)

### PID digital

- PID digital >> $u[n] = u[n-1] + K_p(e[n]-e[n-1]) + K_i T e[n] + \frac{K_d}{T}(e[n]-2e[n-1]+e[n-2])$
- Escolha de T (período) >> Regra prática: $T \leq$ {{$\tau/10$}} (10× mais rápido que a constante de tempo)
- Nyquist para controle >> $T <$ {{$1/(2 f_{max})$}}

### Implementação em MCU

- Loop de controle >> Timer → ISR → lê {{ADC}} → calcula PID → atualiza {{PWM}}
- Discretização de derivada >> $(e[n] - e[n-1]) /$ {{$T$}} (diferença finita)
- Discretização de integral >> Soma acumulada: $\sum e[k] ×$ {{$T$}}
- Conversão s→z (bilinear/Tustin) >> $s =$ {{$(2/T)(z-1)/(z+1)$}}

Na indústria, quase todo PID é DIGITAL (CLP, Arduino, DSP). O contínuo é a teoria, o digital é a prática. A conversão é surpreendentemente simples.

### Drill — Controle Digital #[[Drill]]

- Planta com τ=1s → T máximo recomendado = >> {{100ms}} (τ/10)
- Planta com τ=10ms → T máximo = >> {{1ms}}
- Polo contínuo em s=−5, T=0.1s → polo em z = >> {{$e^{-0.5}$ ≈ 0.607}} (dentro do círculo → estável)
- Polo contínuo em s=+2, T=0.1s → polo em z = >> {{$e^{0.2}$ ≈ 1.22}} (fora do círculo → instável!)
- $z^{-1}$ em hardware = >> {{Registrador}} (flip-flop D, 1 clock de atraso)
