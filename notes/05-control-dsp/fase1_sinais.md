# Módulo 5 — Controle e Sinais: Fase 1 — Sinais e Sistemas
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 5.1: Sinais — Classificação e Operações

### Tipos de sinais

- Sinal contínuo >> Definido para {{todo}} instante de tempo. Ex: v(t)
- Sinal discreto >> Definido em instantes {{espaçados}}. Ex: x[n]
- Sinal periódico >> $x(t) = x(t+T)$ para todo $t$. T = {{período}}
- Relação f, T, ω >> $f = 1/T$, $\omega =$ {{$2\pi f$}}

### Sinais fundamentais

- Degrau unitário u(t) >> 0 para t<0, {{1}} para t≥0. Representa "ligar" algo
- Impulso δ(t) >> Infinito em t=0, zero no resto, {{integral = 1}}. "Pancada instantânea"
- Rampa r(t) >> $r(t) =$ {{$t × u(t)$}} (cresce linearmente)
- Exponencial $e^{at}$ >> Crescente se a>0, {{decrescente}} se a<0 (circuitos RC!)
- Senoidal >> $A × \sin(\omega t + \phi)$. O sinal mais importante — {{base de Fourier}}

### Energia vs potência

- Sinal de energia >> $\int |x(t)|^2 dt$ = {{finito}} (ex: pulso, exponencial decrescente)
- Sinal de potência >> Média temporal $|x(t)|^2$ = {{finito}} (ex: senoide, sinal periódico)

O degrau é o sinal mais importante para controle — ligar algo = aplicar degrau. A resposta ao degrau revela TUDO sobre o sistema.

### Drill — Sinais #[[Drill]]

- u(t) em t=−1 = >> {{0}}
- u(t) em t=2 = >> {{1}}
- r(t) em t=3 = >> {{3}} (t × u(t) = 3×1)
- $e^{-2t}$ é sinal de energia ou potência? >> {{Energia}} (integral converge)
- Senoide pura é sinal de energia ou potência? >> {{Potência}} (energia infinita, potência finita)

---

## Módulo 5.2: Convolução

### Definição e propriedades

- Convolução >> $y(t) = \int x(\tau) × h(t-\tau) \, d\tau$ = {{$x(t) * h(t)$}}
- Resposta ao impulso h(t) >> Saída quando a entrada é {{δ(t)}}. Define completamente o sistema LTI
- LTI <> {{Linear Time-Invariant}} (sistema onde convolução funciona)
- $x(t) * \delta(t)$ = >> {{$x(t)$}} (identidade da convolução)
- Convolução é >> Comutativa, associativa e {{distributiva}}

### Dualidade tempo-frequência

- Convolução no tempo = >> {{Multiplicação}} na frequência
- Multiplicação no tempo = >> {{Convolução}} na frequência

Convolução = impressão digital do sistema. Bata palmas numa sala (δ) → o eco é h(t). Toque música (x) → o que você ouve é x * h. Reverb digital faz exatamente isso.

### Drill — Convolução #[[Drill]]

- Degrau u(t) convolvido com $e^{-t}u(t)$ para circuito RC → resultado = >> {{$1 - e^{-t/RC}$}} (carga do capacitor!)
- $\delta(t) * x(t)$ = >> {{$x(t)$}}
- Filtrar sinal é mais fácil no domínio da >> {{Frequência}} (multiplicação em vez de convolução)

---

##  Módulo 5.3: Transformada de Fourier

### Série e transformada

- Série de Fourier >> Qualquer sinal {{periódico}} = soma infinita de senóides
- Transformada de Fourier >> $X(f) = \int x(t) × e^{-j2\pi ft} \, dt$. Decompõe sinal em {{frequências}}
- Espectro |X(f)| >> Mostra quanta {{energia/amplitude}} em cada frequência

### Transformadas importantes

- $\delta(t)$ ↔ >> {{1}} (impulso contém todas as frequências igualmente)
- $e^{-at}u(t)$ ↔ >> {{$1/(a + j2\pi f)$}}

### FFT

- DFT <> {{Discrete Fourier Transform}} (versão computacional de Fourier)
- FFT <> {{Fast Fourier Transform}} (algoritmo rápido, $O(N \log N)$ vs $O(N^2)$)
- FFT vs DFT >> FFT é um {{algoritmo}} mais rápido para calcular a mesma DFT, não uma transformada diferente
- Frequência de Nyquist >> {{$f_s / 2$}} (acima disso → aliasing)

Fourier = ideia mais poderosa da engenharia. QUALQUER sinal = soma de senóides. Onda quadrada = fundamental + harmônicos ímpares. Rede poluída = 60Hz + 180Hz + 300Hz...

### Drill — Fourier #[[Drill]]

- Onda quadrada 440Hz → harmônicos em >> {{440, 1320, 2200, 3080 Hz}} (ímpares: 1×, 3×, 5×, 7×)
- FFT de N=1024 pontos, f_s=44100Hz → resolução = >> {{~43 Hz}} (f_s/N)
- Espectro de ruído branco → forma = >> {{Plano}} (todas as frequências iguais)
- f_s=100Hz, sinal de 90Hz → aliasing aparece como >> {{10 Hz}} (f_s − f_sinal)
