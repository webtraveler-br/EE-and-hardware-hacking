# Módulo 5 — Controle e Sinais: Fase 5 — DSP (Processamento Digital de Sinais)
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 5.13: Amostragem, Nyquist e Aliasing

### Amostragem

- Amostragem >> Converter sinal contínuo x(t) em discreto x[n] = x(nT). $f_s = 1/T$
- Teorema de Nyquist-Shannon >> Para reconstruir perfeitamente: $f_s \geq$ {{$2 × f_{max}$}}
- Frequência de Nyquist >> {{$f_s / 2$}} (frequência máxima representável sem aliasing)
- Aliasing >> Frequência acima de Nyquist "dobra" → aparece como frequência {{falsa}} mais baixa. Irreversível!
- Filtro anti-aliasing >> Passa-baixa {{analógico}} ANTES do ADC (corta f > $f_s/2$)

### Quantização

- Quantização >> Converter amplitude contínua em $2^N$ níveis discretos
- SNR máx de ADC >> {{$6.02N + 1.76$}} dB ($N$ = bits de resolução)
- Oversampling >> Amostrar muito acima de Nyquist → {{relaxa}} exigência do filtro anti-aliasing

Aliasing = efeito "roda girando ao contrário" em filme. Câmera (amostrador) + roda rápida (sinal) → movimento aparente errado (freq falsa). Impossível corrigir depois!

### Drill — Amostragem #[[Drill]]

- Áudio 20kHz → $f_s$ mínima = >> {{40 kHz}}
- CD 44.1kHz → $f_{max}$ reproduzível = >> {{22.05 kHz}}
- ADC 10-bit → SNR máx = >> {{~62 dB}} (6.02×10+1.76)
- ADC 16-bit → SNR máx = >> {{~98 dB}}
- ADC 24-bit → SNR máx = >> {{~146 dB}} (áudio profissional)
- Sinal de 90kHz amostrado a 100kHz → alias em >> {{10 kHz}} ($f_s - f_{sinal}$)

---

## Módulo 5.14: Transformada Z

### Definição

- Transformada Z >> $X(z) = \sum x[n] × z^{-n}$. Equivalente discreta de {{Laplace}}
- Relação s↔z >> $z =$ {{$e^{sT}$}}
- Semiplano esquerdo (s) → >> Interior do {{círculo unitário}} (z)

### Transformadas essenciais

- $\delta[n]$ ↔ >> {{1}}
- $u[n]$ ↔ >> {{$z/(z-1)$}}
- $a^n u[n]$ ↔ >> {{$z/(z-a)$}}
- Atraso: $Z\{x[n-1]\}$ = >> {{$z^{-1} X(z)$}} ($z^{-1}$ = 1 amostra de atraso)

### Estabilidade e H(z)

- $H(z) = Y(z)/X(z)$ >> Função de transferência {{discreta}}
- Estabilidade >> Todos os polos com {{|z| < 1}} (dentro do círculo unitário)
- $z^{-1}$ em hardware = >> {{Registrador}} (flip-flop D, 1 clock de atraso)
- Bilinear (Tustin): $s =$ >> {{$(2/T)(z-1)/(z+1)$}} (converte filtro contínuo → discreto)

### Drill — Z #[[Drill]]

- Polo contínuo $s=-10$, $T=0.01$s → polo $z =$ >> {{$e^{-0.1} ≈ 0.905$}} (estável, |z|<1)
- Polo contínuo $s=+5$, $T=0.01$s → polo $z =$ >> {{$e^{0.05} ≈ 1.05$}} (instável, |z|>1)
- Filtro com polo $z=0.5$ → estável? >> {{Sim}} (|0.5|<1)

---

## Módulo 5.15: Filtros Digitais FIR e IIR

### FIR (Finite Impulse Response)

- FIR >> $y[n] = \sum b_k × x[n-k]$. Só usa {{entradas passadas}} (sem feedback)
- FIR é SEMPRE >> {{Estável}} (sem polos, sem feedback)
- Fase linear >> Possível com FIR ({{atraso constante}} para todas as frequências)
- Desvantagem >> Precisa de {{mais coeficientes}} para mesma atenuação que IIR

### IIR (Infinite Impulse Response)

- IIR >> $y[n] = \sum b_k x[n-k] - \sum a_k y[n-k]$. Usa {{saídas passadas}} (feedback)
- Vantagem >> Mais {{eficiente}} (menos coeficientes para mesma performance)
- Risco >> Pode ser {{instável}} (polos fora do círculo unitário)
- Derivado de >> Filtros analógicos clássicos ({{Butterworth, Chebyshev, Elliptic}})

### Tipos de resposta

- Butterworth >> Maximamente {{plano}} na banda passante (sem ripple)
- Chebyshev >> {{Ripple}} na passband, mas transição mais abrupta
- Design FIR >> Janelamento: truncar h[n] ideal com janela ({{Hamming, Hanning, Blackman}})

### Drill — FIR/IIR #[[Drill]]

- Filtro sem feedback → tipo = >> {{FIR}}
- Filtro com $y[n-1]$ na equação → tipo = >> {{IIR}}
- Fase linear obrigatória → usar >> {{FIR}}
- Mínima ordem (poucos coefs) → usar >> {{IIR}}
- Butterworth vs Chebyshev: qual tem ripple? >> {{Chebyshev}}

---

## Módulo 5.16: FFT Prática e Análise Espectral

### FFT prática

- FFT de N pontos → resolução $\Delta f$ = >> {{$f_s / N$}}
- N=1024, $f_s$=44100 → $\Delta f$ = >> {{~43 Hz}}
- Zero-padding >> Adicionar zeros → NÃO melhora resolução real, apenas {{interpola}} o espectro

### Janelamento

- Spectral leakage >> Sinal não-periódico na janela FFT → energia "vaza" para {{bins adjacentes}}
- Sem janela (retangular) >> Melhor resolução, {{pior}} vazamento
- Hanning/Hamming >> {{Bom compromisso}} resolução/vazamento
- Blackman >> Mínimo vazamento, {{pior resolução}}

### Ferramentas avançadas

- Espectrograma (STFT) >> FFT em janelas deslizantes → gráfico {{tempo × frequência × amplitude}}
- PSD (Power Spectral Density) >> Potência por banda de frequência. Unidade: {{V²/Hz}}
- Welch >> Método para estimar PSD com média de segmentos → {{reduz variância}}

Espectrograma = raio-X de como as frequências mudam no tempo. Usado em áudio, vibração de máquinas, EEG, side-channel attacks.

### Drill — FFT Prática #[[Drill]]

- Precisa resolver frequências separadas por 10Hz, $f_s$=10kHz → N mínimo = >> {{1000}} ($f_s/\Delta f$)
- Ruído branco no espectro → forma = >> {{Plano}} (todas freq iguais)
- Aplicar janela Hanning antes da FFT → reduz >> {{Spectral leakage}}
