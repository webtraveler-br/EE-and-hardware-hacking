# Módulo 5 — Controle e Sinais: Fase 3 — Análise e Projeto de Controladores
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 5.7: Estabilidade

### Condições

- Estabilidade BIBO >> {{Todos}} os polos de malha fechada no semiplano esquerdo (Re < 0)
- Polo com Re > 0 → >> {{Instável}} (exponencial crescente → algo explode)
- Polo com Re = 0 → >> {{Marginalmente estável}} (oscilação constante, nem cresce nem decresce)

### Critério de Routh-Hurwitz

- Routh-Hurwitz >> Método {{algébrico}} para verificar estabilidade sem calcular polos
- Condição necessária >> Todos os coeficientes do polinômio devem ser {{positivos}} (e presentes)
- Tabela de Routh >> Construir tabela → primeira coluna toda {{positiva}} → estável
- Número de trocas de sinal na 1ª coluna = >> Número de polos no {{semiplano direito}}
- Ganho crítico ($K_u$) >> Ganho que torna o sistema {{marginalmente estável}}

Na prática: instabilidade = avião cai, reator derrete, motor dispara. Matematicamente = polo com Re > 0 = exponencial crescente.

### Drill — Estabilidade #[[Drill]]

- $s^2 + 3s + 2$ → polos −1, −2 → >> {{Estável}} (ambos Re < 0)
- $s^2 - s + 2$ → coeficiente negativo → >> {{Instável}} (nem precisa de Routh!)
- $s^2 + 4$ → polos ±2j → >> {{Marginalmente estável}} (eixo imaginário)
- $G(s) = K/(s(s+1)(s+2))$ com feedback → K_u = >> {{6}} (Routh: s² row = 0)

---

## Módulo 5.8: Diagrama de Bode

### Conceito

- Bode >> 2 gráficos: {{|G(jω)| em dB}} vs log(ω) + {{∠G(jω) em °}} vs log(ω)
- Traçado assintótico >> Polo em $s = -a$ → queda de {{−20 dB/dec}} a partir de $\omega = a$
- Zero em $s = -a$ → subida de >> {{+20 dB/dec}} a partir de $\omega = a$
- Polo na origem $(1/s)$ → >> {{−20 dB/dec}} em toda a faixa + fase fixa de −90°

### Margens de estabilidade

- Margem de Ganho (GM) >> Quanto ganho pode adicionar antes de instabilidade. Medida em $\omega$ onde {{fase = −180°}}
- Margem de Fase (PM) >> Quantos graus restam antes de −180°. Medida em $\omega$ onde {{|G| = 0 dB}}
- Critério >> GM > 0 dB E PM > 0° → {{estável}} em malha fechada
- PM > 60° → >> {{Ótimo}} (pouco overshoot)
- PM > 45° → >> {{Bom}}
- PM < 30° → >> {{Perigoso}} (muito oscilatório)

Bode = raio-X do sistema em frequência. Margens = margem de segurança contra variações e incertezas.

### Drill — Bode #[[Drill]]

- $G(s) = 10/(s+1)$ → ganho DC (ω=0) em dB = >> {{20 dB}} (20log10(10))
- Frequência de corte (−3dB) do mesmo = >> {{1 rad/s}} (polo em s=−1)
- Integrador puro $1/s$ → magnitude a ω=10 = >> {{−20 dB}} (−20log10(10))
- Sistema com PM=20° → overshoot será >> {{Alto}} (~50%+, perigoso)

---

## Módulo 5.9: Controlador PID

### Ação PID

- PID >> $u(t) = K_p e(t) + K_i \int e(t) dt + K_d \frac{de(t)}{dt}$
- P (Proporcional) >> Reação proporcional ao erro {{atual}}. Mais $K_p$ = mais rápido, mais overshoot
- I (Integral) >> Acumula erro {{passado}} → elimina erro em regime permanente. Mais $K_i$ = pode oscilar
- D (Derivativo) >> Reage à {{taxa de mudança}} do erro ("antecipa"). Mais $K_d$ = mais suave, sensível a ruído
- PID em Laplace >> $G_{PID}(s) =$ {{$K_p + K_i/s + K_d s$}}

### Sintonia

- Ziegler-Nichols >> Encontrar $K_u$ (ganho crítico) e $T_u$ (período de oscilação)
- Z-N: $K_p$ = >> {{$0.6 K_u$}}
- Z-N: $K_i$ = >> {{$2K_p / T_u$}}
- Z-N: $K_d$ = >> {{$K_p T_u / 8$}}
- Anti-windup >> Previne acumulação do integrador quando atuador {{satura}} (PWM em 255, válvula em 100%)
- Na prática >> PI basta na maioria dos casos. D é {{sensível a ruído}} e pode piorar

PID = motorista: P olha a posição (erro atual), I lembra o passado (erro acumulado), D antecipa o futuro (taxa de mudança). 95% dos controladores industriais são PID.

### Drill — PID #[[Drill]]

- Só P: erro em regime permanente >> {{≠ 0}} (offset permanente)
- P + I: erro em regime permanente >> {{= 0}} (integrador elimina)
- $K_u$=10, $T_u$=2s → Z-N: $K_p$ = >> {{6}} (0.6×10)
- Mesmo: $K_i$ = >> {{6}} (2×6/2)
- Mesmo: $K_d$ = >> {{1.5}} (6×2/8)
- Atuador saturado + integrador acumulando → problema = >> {{Wind-up}}

---

## Módulo 5.10: Projeto por Bode (Lead/Lag)

### Compensadores

- Compensador Lead (avanço) >> Adiciona {{fase positiva}} → melhora PM e velocidade
- Compensador Lag (atraso) >> Adiciona {{ganho em baixa freq}} → reduz erro em regime permanente
- Lead-Lag >> Combina ambos. Equivale a {{PID}} no domínio da frequência

### Especificações → Bode

- PM → >> {{Overshoot}} (PM↑ = overshoot↓)
- Bandwidth → >> {{Velocidade}} de resposta (BW↑ = mais rápido)
- Ganho DC → >> Erro em regime {{permanente}} (ganho↑ = erro↓)

### Drill — Lead/Lag #[[Drill]]

- PM atual = 20°, spec PM ≥ 45° → precisa de >> {{Lead}} (adicionar fase)
- Erro de posição alto mas PM ok → precisa de >> {{Lag}} (ganho em baixa freq)
- Ambos os problemas → usar >> {{Lead-Lag}}
