# Módulo 1 — Circuitos: Fase 2 — DC Avançado
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 1.6 — Thévenin e Norton

### Teoremas de simplificação

- Thévenin simplifica rede linear em >> {{1 fonte de tensão + 1 resistor em série}}
- Norton simplifica rede linear em >> {{1 fonte de corrente + 1 resistor em paralelo}}
- Objetivo prático de Thévenin >> Ver o que a {{carga}} enxerga do resto do circuito
- Vantagem >> Varia a carga {{sem recalcular}} o circuito inteiro

### Cálculo

- V_th >> Tensão medida em circuito {{aberto}} (carga removida)
- I_n >> Corrente medida em {{curto-circuito}} (carga em curto)
- R_th (só fontes independentes) >> Desligue fontes → calcule {{R_equivalente}} vista dos terminais
- Desligar fonte de tensão para achar R_th >> Substituir por {{curto-circuito}}
- Desligar fonte de corrente para achar R_th >> Substituir por {{circuito aberto}}

### Conversão Thévenin ↔ Norton

- V_th = >> {{I_n × R_n}}
- I_n = >> {{V_th / R_th}}
- R_th = >> {{R_n}} (sempre iguais)

### Conexão HH: GPIO e Pull-ups

- Pino GPIO em Output HIGH → modelo Thévenin >> {{V_cc com R_th baixa (~50Ω)}}
- Pino GPIO em Output LOW → modelo Thévenin >> {{GND com R_th baixa}}
- Estado Hi-Z (alta impedância) >> R_th → {{∞}} (pino desconectado)
- Função do Pull-up >> Ancorar tensão em {{V_cc}} quando pino está em Hi-Z
- Função do Pull-down >> Ancorar tensão em {{GND}} quando pino está em Hi-Z
- Open-Drain: nível ALTO depende de >> {{Pull-up externo}} (só puxa para GND nativamente)
- Open-Drain com Pull-up alto (R grande) + capacitância parasita >> Borda de subida {{lenta}} (τ = RC)

Entender Thévenin = entender GPIOs. Cada pino é um V_th + R_th que muda conforme o modo.

### Drill — Thévenin #[[Drill]]

- V=10V, R₁=1k, R₂=1k (divisor). V_th sobre R₂ = >> {{5V}}
- Mesmo circuito: R_th (bateria curto, R₁ ∥ R₂) = >> {{500Ω}}
- V_th=5V, R_th=500Ω, carga curta → I = >> {{10mA}}
- Norton: I_n=10mA, R_n=500Ω → V_th = >> {{5V}}
- V_th=3.3V, R_th=100Ω, carga 1kΩ → V_carga = >> {{3.0V}} (divisor: 1k/(1k+0.1k)×3.3)
- GPIO Hi-Z sem pull-up → nível lógico = >> {{flutuante/indeterminado}}

---

##  Módulo 1.7 — Análise Nodal e de Malhas

### Quando usar métodos sistemáticos

- Limite da análise manual (série/paralelo) >> Múltiplas malhas com fontes que {{não se reduzem}}
- Nodal e Malhas geram >> Sistemas de equações lineares {{Ax = b}}

### Análise Nodal

- Nodal aplica sistematicamente >> {{KCL}} em cada nó + Lei de Ohm
- Primeiro passo >> Escolher nó de {{referência (GND = 0V)}}
- Diagonal da matriz G >> Soma das {{condutâncias}} conectadas ao nó
- Fora da diagonal (G_ij) >> {{−(condutância entre nó i e j)}}
- Supernó >> Fonte de {{tensão}} entre dois nós não-GND (sem R entre eles)
- Como resolver supernó >> Englobar os 2 nós, fazer KCL na fronteira + equação {{V₁ − V₂ = V_fonte}}

### Análise de Malhas

- Malhas aplica sistematicamente >> {{KVL}} em cada malha + Lei de Ohm
- Supermalha >> Fonte de {{corrente}} compartilhada entre duas malhas
- Como resolver supermalha >> KVL na malha combinada + equação {{I₁ − I₂ = I_fonte}}

Para HH/embarcados, Nodal é preferível: quase todo circuito real tem um GND comum, e ADCs medem tensão absoluta vs GND.

### Drill — Nodal/Malhas #[[Drill]]

- Circuito com 4 nós (incluindo GND): quantas equações nodais? >> {{3}} (n − 1)
- Circuito com 3 malhas independentes: quantas equações de malha? >> {{3}}
- Fonte de 5V entre nó A e nó B (ambos não-GND): tipo de anomalia? >> {{Supernó}}

---

##  Módulo 1.8 — Fontes Dependentes

### Conceito

- Fonte dependente >> Controlada por tensão ou corrente em {{outro ponto}} do circuito
- Modelam componentes ativos como >> {{Transistores e Op-Amps}}

### Os 4 tipos e unidades

- VCVS (tensão → tensão) >> Ganho $A_v$ ou $\mu$, unidade: {{adimensional (V/V)}}
- CCCS (corrente → corrente) >> Ganho $\beta$ ou $h_{FE}$, unidade: {{adimensional (A/A)}}
- VCCS (tensão → corrente) >> Transcondutância $g_m$, unidade: {{Siemens (A/V)}}
- CCVS (corrente → tensão) >> Transresistência $r$, unidade: {{Ohms (V/A)}}

### Regra crítica

- Superposição: posso desligar fonte dependente? >> {{NUNCA}} — fontes dependentes ficam intocadas

VCCS = MOSFET ($V_{GS}$ → $I_D$). CCCS = BJT ($I_B$ → $I_C$). CCVS = fotodetector com transimpedância.

### Drill — Fontes Dependentes #[[Drill]]

- MOSFET: $g_m$ = 1mA/V, $V_{GS}$ = 2V → $I_D$ = >> {{2mA}}
- Op-amp ideal: ganho $A_v$ = 10⁴, $V_{in}$ = 1μV → $V_{out}$ = >> {{10mV}}
- BJT: $\beta$ = 100, $I_B$ = 50μA → $I_C$ = >> {{5mA}}
- Transresistência r = 10kΩ, I_in = 100μA → V_out = >> {{1V}}

---

## Módulo 1.9 — Fluxo de Decisão DC

### Quando usar cada ferramenta

- Múltiplas malhas complexas (software/Numpy) >> {{Nodal}} (monta matriz Ax=b)
- Isolar contribuição de cada fonte (ruído, sensibilidade) >> {{Superposição}}
- Projetar para carga variável (trocar componente na ponta) >> {{Thévenin}} (reduz tudo a V_th + R_th)

A escolha da ferramenta é tão importante quanto saber usá-la. Thévenin para design, Nodal para análise, Superposição para debug.
