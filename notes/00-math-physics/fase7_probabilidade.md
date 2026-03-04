# Módulo 0 — Matemática/Física: Fase 7 — Probabilidade e Estatística
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 0.24: Probabilidade e Distribuições

### Conceitos

- Probabilidade >> $P(A) =$ {{casos favoráveis / total}}. $0 \leq P \leq 1$
- Probabilidade condicional >> $P(A|B) =$ {{$P(A \cap B) / P(B)$}}

### Distribuições

- Distribuição uniforme >> Todos os valores {{igualmente prováveis}} (ex: ruído de quantização do ADC)
- Gaussiana (Normal) >> $N(\mu, \sigma^2)$. $\mu$ = {{média}}, $\sigma$ = {{desvio padrão}}
- Regra 68-95-99.7 >> ±1σ = {{68%}}, ±2σ = {{95%}}, ±3σ = {{99.7%}}
- Ruído elétrico >> Modelado como {{Gaussiano}} ($V_{ruído} \sim N(0, \sigma^2)$)

### SNR

- SNR <> {{Signal-to-Noise Ratio}} ($P_{sinal} / P_{ruído}$)
- SNR em dB >> {{$10 \log_{10}(P_s / P_n)$}}
- Ruído térmico (Johnson-Nyquist) >> $V_{rms} =$ {{$\sqrt{4kTR\Delta f}$}}

O ruído é INEVITÁVEL. Todo resistor gera ruído térmico. SNR determina a qualidade de qualquer sinal — áudio hi-fi precisa de SNR > 90dB.

### Drill — Probabilidade #[[Drill]]

- $V_{sinal}$=1V_rms, $V_{ruído}$=1mV_rms → SNR = >> {{60 dB}} ($20\log_{10}(1000)$)
- ADC 10-bit → SNR máx = >> {{~62 dB}} ($6.02×10+1.76$)
- ±2σ cobre quantos % da distribuição? >> {{95%}}
- ±3σ cobre quantos %? >> {{99.7%}}
- R=10kΩ, T=300K, Δf=10kHz → $V_{rms}$ = >> {{~1.3 μV}} ($\sqrt{4×1.38×10^{-23}×300×10^4×10^4}$)

---

##  Módulo 0.25: Estatística Aplicada e Tolerâncias

### Estatística descritiva

- Média >> $\mu = (1/N) \sum x_i$
- Desvio padrão >> $\sigma = \sqrt{(1/N) \sum (x_i - \mu)^2}$
- Tolerância de resistor >> 1%, 5%, {{10%}} são os padrões comuns
- Tolerância de capacitor >> Tipicamente {{10-20%}} (muito pior que resistor!)

### Propagação de erros

- Propagação de incerteza >> $\sigma_f^2 = (\partial f/\partial x)^2 \sigma_x^2 +$ {{$(\partial f/\partial y)^2 \sigma_y^2$}} (soma em quadratura)
- Pior caso >> Usar {{extremos}} de tolerância (R=1kΩ±5% → 950Ω a 1050Ω)
- Monte Carlo >> Simular {{milhares}} de circuitos com componentes aleatórios → histograma do resultado

### Qualidade

- 6 Sigma >> {{3.4}} defeitos por milhão de oportunidades
- Monte Carlo vs pior caso >> Monte Carlo é mais {{realista}} (pior caso é muito conservador)

### Drill — Tolerâncias #[[Drill]]

- R=10kΩ ±5% → faixa = >> {{9.5kΩ a 10.5kΩ}}
- Divisor de tensão R1=R2=10kΩ → V_out ideal = >> {{V_in/2}}
- R1=9.5k, R2=10.5k (pior caso) → V_out/V_in = >> {{0.525}} (10.5/(9.5+10.5) — desvia 5%)
- R1=10.5k, R2=9.5k → V_out/V_in = >> {{0.475}} (pior caso oposto)
- Por que Monte Carlo é melhor que pior caso? >> Componentes raramente estão {{todos}} no extremo simultaneamente
