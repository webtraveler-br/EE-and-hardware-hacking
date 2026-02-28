# Pilar 2: Eletrônica Analógica e de Potência

> **Sobre este pilar**: Semicondutores são os "músculos" da eletrônica — amplificam, retificam, chaveiam e convertem energia. Aqui você vai de componentes passivos a circuitos que criam, não apenas consomem.
>
> **Ferramentas**: [LTspice](https://www.analog.com/ltspice) (análise precisa com modelos SPICE reais) + [Falstad](https://www.falstad.com/circuit/) (intuição visual)
>
> **Pré-requisitos**: [Pilar 1](ee_circuitos_roadmap.md) completo (KVL/KCL, Thévenin, fasores, filtros). [Pilar 0](ee_matematica_fisica_roadmap.md) Fases 9-10 (EM, semicondutores/junção PN) para entender a física dos diodos e transistores.
>
> **Conexões com outros pilares**:
> - **Base de**: [Pilar 0](ee_matematica_fisica_roadmap.md) — junção PN (Mód 0.36), bandas de energia (Mód 0.35), ondas EM (Mód 0.34 → Módulos 2.17-2.19 RF), probabilidade (Mód 0.24-0.25 → tolerâncias)
> - **Base de**: [Pilar 1](ee_circuitos_roadmap.md) — Thévenin (→ polarização BJT), divisor de tensão (→ Zener), filtros RC (→ filtros ativos), potência (→ conversores)
> - **Alimenta**: [Pilar 3](ee_digital_embarcados_roadmap.md) (MOSFET como chave digital), [Pilar 4](ee_eletrotecnica_automacao_roadmap.md) (retificadores, inversores, VFDs), [Lab L.6](ee_laboratorio_real_roadmap.md) (fonte real 5V)
> - **Segurança**: [HH 1.1](hardware_hacking_roadmap.md) (níveis lógicos, pull-ups), [HH 6.1](hardware_hacking_roadmap.md) (RF/SDR), [HH Avançado B.1](hardware_hacking_roadmap_avancado.md) (integridade de sinal)
>
> [Voltar ao Índice](ee_roadmap_index.md)

---

## Visão Geral

| Fase | Módulos | Foco | Horas |
|------|---------|------|-------|
| **Semicondutores Básicos** | 2.1–2.4 | Diodos, retificadores, Zener, LEDs | ~9h |
| **Transistores** | 2.5–2.8 | BJT, MOSFET, amplificação, chaveamento | ~10h |
| **Amplificadores Operacionais** | 2.9–2.12 | Amp-ops, filtros ativos, osciladores | ~9h |
| **Eletrônica de Potência** | 2.13–2.16 | Reguladores, Buck/Boost, inversores, fontes | ~10h |

---

## Fase 1 — Semicondutores Básicos

### Módulo 2.1: O Diodo — A Válvula Eletrônica
**Tempo: 2h**

#### O que memorizar
- **Junção PN**: semicondutor tipo P (excesso de lacunas) + tipo N (excesso de elétrons) = junção com barreira de potencial
- **Polarização direta**: ânodo (+) mais positivo que cátodo (-) → corrente flui. Tensão de limiar: ~0.7V (silício), ~0.3V (germânio), ~2V (LED)
- **Polarização reversa**: corrente ≈ 0 (apenas corrente de fuga, nA). Até atingir V_breakdown → conduza muito forte (destrutivo, exceto Zener)
- **Modelo ideal**: chave (ON = curto-circuito, OFF = circuito aberto)
- **Modelo real**: V_d ≈ 0.7V quando conduzindo + resistência dinâmica r_d = V_T/I_D (V_T ≈ 26mV a 25°C)
- **Curva I-V**: exponencial na direta (`I = I_s × (e^(V/nV_T) - 1)`), praticamente zero na reversa

#### Intuição
Um diodo é uma **válvula de água unidirecional** (check valve). A água só flui numa direção — se tentar inverter, bloqueia. A "mola" da válvula precisa de uma pressão mínima para abrir (0.7V no silício). Uma vez aberta, a agua flui quase livremente.

Isso é **revolucionário**: pela primeira vez, temos um componente que se comporta DIFERENTE dependendo da polaridade. Resistores, capacitores e indutores não ligam para a direção da corrente. Diodos SIM. Essa assimetria é a base de toda a eletrônica.

**A física por trás**: silício puro não conduz bem. Mas se você "dopar" com átomos de fósforo (5 elétrons de valência → 1 elétron livre = tipo N) ou boro (3 elétrons → 1 lacuna = tipo P), ele se torna semicondutor. Na junção PN, elétrons e lacunas se recombinam criando uma "zona de depleção" — uma barreira que só deixa corrente passar em um sentido quando você aplica tensão suficiente (0.7V).

#### Projeto: "O Diodo em Ação"
1. **No Falstad**: monte fonte DC variável → diodo (1N4148) → R(1kΩ) → GND
   - Varie a tensão de -5V a +5V em passos de 0.5V
   - Abaixo de 0.6V: I ≈ 0. Acima: corrente cresce exponencialmente!
   - Inverta a fonte: I = 0 (bloqueado!)
2. **No LTspice**: trace a curva I-V completa:
   ```spice
   D1 Vin 0 1N4148
   V1 Vin 0 DC 0
   .dc V1 -5 5 0.01
   ```
   - Plote I(D1) vs V(Vin) → curva exponencial clássica
3. **LED**: R = (V_fonte - V_LED) / I_LED. Para LED vermelho (V_LED=2V, I=20mA) com 5V: R = (5-2)/0.02 = 150Ω
4. **Prompt IA**: *"Explique a física da junção PN: o que são dopantes, zona de depleção, e por que a corrente reversa é praticamente zero. Use diagramas de bandas de energia se possível."*
5. **Entregável**: Curva I-V + cálculo de resistor para LED + simulações

#### Erros Comuns
- Ligar LED sem resistor limitador → I dispara (curva exponencial!) e o LED queima
- Confundir ânodo (A, perna longa/triângulo) com cátodo (K, perna curta/barra) — se inverter, não conduz
- Usar 0.7V para LEDs (está errado! LEDs têm V_f de 1.8 a 3.5V dependendo da cor)

---

### Módulo 2.2: Retificadores — De AC para DC
**Tempo: 2h**

#### O que memorizar
- **Retificador de meia-onda**: 1 diodo → só passa a semionda positiva. V_dc_média = V_pico/π. Simples mas ineficiente
- **Retificador de onda completa (ponte)**: 4 diodos (ponte de Graetz) → ambas semiondas viram positivas. V_dc = 2×V_pico/π. Padrão da indústria
- **Filtro capacitivo**: capacitor em paralelo com carga → "preenche" os vales entre picos. Ripple: `V_ripple ≈ I_carga / (f × C)`
- **Para onda completa**: f_ripple = 2×f_rede (120Hz para rede de 60Hz)
- **Tensão no capacitor**: V_dc ≈ V_pico - V_diodo (~0.7V × 2 para ponte)
- **Regra prática para C**: para ripple < 10%: `C > I / (f_ripple × 0.1 × V_dc)`

#### Intuição
Todo carregador de celular, fonte de notebook, e TV começa com um **retificador + filtro**. A tomada fornece AC senoidal, mas seus dispositivos precisam de DC estável. O retificador "desentorta" a senóide (transforma em pulsante), e o capacitor "alisa" os pulsos (como um balde que mantém o nível enquanto a torneira abre e fecha ciclicamente).

#### Projeto: "Fonte de Alimentação Básica (5V)"
1. **No LTspice**, monte a fonte completa:
   - Transformador: 220V → 9V (use fonte AC de 12.7V pico)
   - Ponte retificadora: 4 diodos 1N4007
   - Filtro: C = 1000μF
   - Carga: R = 100Ω
2. **Meça**: V_dc, V_ripple, I_carga
3. **Varie C**: 100μF, 470μF, 1000μF, 2200μF → plote ripple vs C
4. **Prompt IA**: *"Na prática, por que a tensão no capacitor não chega exatamente a V_pico? Explique o efeito da queda nos diodos (V_f), da ESR do capacitor, e do inrush current no momento da ligação."*
5. **Entregável**: Fonte completa simulada + gráfico ripple vs C + cálculos

---

### Módulo 2.3: Diodo Zener — O Regulador Natural
**Tempo: 1.5h**

#### O que memorizar
- **Zener**: diodo projetado para operar em breakdown reverso — mantém tensão **constante** (V_Z) na reversa
- **Regulador Zener**: R_série + Zener em paralelo com carga. V_carga ≈ V_Z (constante, mesmo com variação de V_input ou I_carga)
- **Dimensionamento de R_série**: `R = (V_in - V_Z) / (I_Z + I_carga)`. I_Z_mín = 5mA (manter regulação)
- **Potência**: `P_Z = V_Z × I_Z_máx`. Nunca exceder P_Z_máx (dissipação em calor)
- **Aplicações**: referência de tensão, proteção contra overvoltage (clamp), regulação simples

#### Intuição
O Zener é como uma **válvula de pressão reguladora** — não importa quanta pressão entra, a saída é sempre a mesma. É ineficiente (dissipa o excesso em calor), mas extremamente simples. Para regulação séria, usamos reguladores lineares (7805) ou chaveados (Buck) — mas o conceito começa aqui.

#### Projeto: "Regulador Zener 5.1V"
1. **Projete** um regulador Zener para: V_in = 9-15V (variável), V_out = 5.1V, I_carga = 0-50mA
2. **Monte no LTspice** com diodo Zener BZX84C5V1
3. **Teste**: varie V_in de 7V a 18V → V_out deve ficar constante em ~5.1V
4. **Teste**: varie I_carga de 0 a 80mA → encontre o ponto onde perde regulação
5. **Entregável**: Circuito + gráficos V_out vs V_in e V_out vs I_carga

---

### Módulo 2.4: Aplicações de Diodos — Clamping, Clipping, Proteção
**Tempo: 1.5h**

#### O que memorizar
- **Clipper (ceifador)**: limita tensão a um valor máximo/mínimo. Diodo + resistor
- **Clamper (grampeador)**: adiciona nível DC a um sinal AC. Capacitor + diodo
- **Proteção ESD**: diodos nos pinos de I/O para proteger contra descargas eletrostáticas
- **Flyback (proteção de indutor)**: diodo em antiparalelo com bobinas/relés (já visto no Pilar 1!)
- **Multiplicador de tensão**: cascata de diodos + capacitores → dobrador, triplicador de tensão

#### Projeto: "Circuitos de Proteção"
1. **Monte no LTspice**: clipper que limita sinal AC a ±5V (2 Zeners antiparalelo)
2. **Monte**: clamper que desloca uma senóide de ±5V para 0-10V (cap + diodo)
3. **Monte**: dobrador de tensão Cockcroft-Walton (2 diodos + 2 caps) → 12V AC → ~24V DC
4. **Entregável**: 3 circuitos simulados + explicação de cada aplicação

#### Checkpoint — Fim da Fase Semicondutores
- [ ] Entende a curva I-V do diodo e sabe usar os 3 modelos (ideal, 0.7V, exponencial)
- [ ] Projeta retificador com filtro capacitivo e calcula ripple
- [ ] Dimensiona regulador Zener corretamente
- [ ] Sabe quando usar clipper, clamper, e proteção com diodos

---

## Fase 2 — Transistores: O Coração da Eletrônica

### Módulo 2.5: BJT — O Transistor Bipolar como Chave
**Tempo: 2.5h**

#### O que memorizar
- **BJT (Bipolar Junction Transistor)**: 3 terminais — Base (B), Coletor (C), Emissor (E)
- **NPN** (mais comum): corrente entra pela Base, sai pelo Emissor. C é o "output de potência"
- **PNP**: corrente entra pelo Emissor, sai pela Base (tudo invertido)
- **Modo chave (saturação)**: `I_B > I_C / β` → transistor "liga" (V_CE ≈ 0.2V). Funciona como chave fechada
- **Modo corte**: V_BE < 0.7V → transistor "desliga" (I_C = 0). Funciona como chave aberta
- **β (hFE)**: ganho de corrente. Típico: 100-300. `I_C = β × I_B`
- **V_BE ≈ 0.7V** quando conduzindo (como diodo B-E)
- **Cálculo de R_base**: `R_B = (V_input - 0.7V) / I_B`; para garantir saturação: `I_B = I_C / β × fator_segurança` (fator ≈ 2-5)

#### Intuição
O BJT é como um **registro de água controlado por um cano fino**. Um fluxo PEQUENO pela base (cano fino) controla um fluxo GRANDE no coletor (cano grosso). É a essência da amplificação: sinal fraco → sinal forte. No modo chave, é como um relé eletrônico — uma corrente de 1mA na base pode controlar 200mA no coletor (controlando um motor, LED de potência, relé).

#### Projeto: "Chave BJT para Motor DC"
1. **No Falstad**: monte um NPN controlando um LED:
   - 5V → R_C(220Ω) → LED → Coletor | Base ← R_B(10kΩ) ← Botão → 5V | Emissor → GND
   - Pressione botão → LED liga. Solte → LED desliga
2. **No LTspice**, escale para motor:
   - V_CC = 12V, Motor modelado como R = 60Ω (I = 200mA)
   - 2N2222A (β ≈ 100), I_B necessária ≈ 200/100 × 3 = 6mA (margem)
   - R_B = (5 - 0.7) / 6m = 716Ω → use 680Ω
   - NÃO esqueça o diodo flyback no motor!
3. **Teste**: meça V_CE em saturação (deve ser < 0.3V), P_transistor, P_carga
4. **Prompt IA**: *"Um BJT 2N2222A tem β_mín = 75 e β_máx = 300 segundo o datasheet. Por que usamos o β_mín para calcular R_B e não o β típico? O que aconteceria se usássemos β = 300 e o transistor particular tivesse β = 80?"*
5. **Entregável**: Circuito chave com cálculos de R_B + simulação com motor

#### Erros Comuns
- Esquecer que β varia MUITO entre transistores do mesmo modelo (75 a 300!). NUNCA projete contando com um β específico — use o mínimo do datasheet com margem
- Confundir saturação no BJT (é quando está TOTALMENTE LIGADO, V_CE≈0.2V) com saturação no MOSFET (não tem esse conceito da mesma forma)
- Esquecer o diodo flyback quando chaveando cargas indutivas (motor, relé)

---

### Módulo 2.6: BJT — O Transistor como Amplificador
**Tempo: 2.5h**

#### O que memorizar
- **Região ativa**: V_BE ≈ 0.7V e V_CE > V_CE_sat. `I_C = β × I_B` (linear, amplificação!)
- **Ponto de operação (Q-point)**: polarização DC que coloca o transistor no "meio" da região ativa
- **Polarização por divisor de tensão**: método mais estável para definir Q-point
- **Ganho de tensão**: `A_V = -g_m × R_C = -(I_C/V_T) × R_C` (V_T ≈ 26mV). Sinal negativo = inversão de fase
- **Capacitores de acoplamento**: bloqueiam DC, passam AC — separam o ponto de operação do sinal
- **Modelo de pequenos sinais**: substituir BJT por fontes dependentes (g_m × v_be) para análise AC
- **Emissor Comum**: configuração principal de amplificador. Ganho alto, inversão de fase

#### Intuição
Polarizar um BJT é como posicionar uma bola no MEIO de uma ladeira. Se colocar no topo (saturação) ou no fundo (corte), ela não se move com empurrões pequenos. No meio (região ativa), um empurrão pequeno produz movimento grande. O "empurrão" é o sinal AC de entrada, o "movimento" é o sinal AC amplificado na saída.

#### Projeto: "Amplificador Emissor Comum"
1. **Projete** um amplificador com:
   - V_CC = 12V, I_C = 1mA (Q-point), V_CE = 6V (meio da excursão)
   - R_C = (V_CC - V_CE) / I_C = 6kΩ. R_E = 1kΩ (estabilização)
   - Divisor de tensão: V_B = V_BE + I_E×R_E ≈ 0.7 + 1m×1k = 1.7V
2. **Monte no LTspice** com 2N2222:
   - Adicione caps de acoplamento (10μF) na entrada e saída
   - Aplique sinal AC senoidal (10mV, 1kHz) na entrada
   - Plote V_in e V_out → meça ganho e verifique inversão de fase!
3. **Meça**: ganho A_V, limites de excursão (clipping), impedância de entrada/saída
4. **Prompt IA**: *"O que significa 'modelo de pequenos sinais'? Por que separamos análise DC (polarização, Q-point) da análise AC (ganho, sinal)? Mostre como o BJT vira duas fontes dependentes no modelo π-híbrido."*
5. **Entregável**: Amplificador com cálculos + formas de onda + medição de ganho

#### Erros Comuns
- Não separar análise DC de AC — primeiro polarize (DC), depois analise sinal (AC com modelo de pequenos sinais)
- Esquecer os capacitores de acoplamento — sem eles, a polarização DC de um estágio interfere no outro
- Clipping: se o sinal de entrada é grande demais, a saída "corta" (saturação ou corte). Sempre verifique a excursão máxima

---

### Módulo 2.7: MOSFET — O Transistor Moderno
**Tempo: 2.5h**

#### O que memorizar
- **MOSFET**: Gate (G), Drain (D), Source (S). Gate isolado = corrente de gate ≈ 0! (impedância de entrada ≈ infinita)
- **N-channel enhancement**: V_GS > V_th → liga. V_th típico: 1.5-4V
- **R_DS(on)**: resistência drain-source quando ligado. Valores: 1mΩ a 1Ω (MUITO menor que BJT!)
- **Vantagens sobre BJT**: controlado por TENSÃO (não corrente), R_DS(on) muito baixo, chaveamento mais rápido
- **Gate charge**: para ligar/desligar rapidamente, precisa carregar/descarregar capacitância do gate
- **Aplicações**: chaveamento de potência (H-bridge para motores), fontes chaveadas, inversores
- **Driver de gate**: circuito que fornece corrente para carregar o gate rapidamente

#### Intuição
Se o BJT é um registro controlado por fluxo de água na base (corrente), o MOSFET é um registro controlado por **pressão** na base (tensão). Como a "pressão" não requer fluxo, o MOSFET não gasta energia para ficar ligado (I_gate ≈ 0). Isso o torna MUITO mais eficiente que o BJT para chaveamento de potência. A maioria dos circuitos de potência modernos usa MOSFETs.

#### Projeto: "MOSFET H-Bridge para Motor DC"
1. **No LTspice**: monte um MOSFET N-channel (IRF540N) controlando um motor (12V, 2A)
   - Compare R_DS(on) × I² (perda no MOSFET) vs V_CE_sat × I (perda no BJT equivalente)
2. **Monte uma H-Bridge** com 4 MOSFETs (2 N-channel + 2 P-channel):
   - Controle de direção do motor: "frente" e "ré"
   - Adicione dead-time entre trocas (evita shoot-through!)
3. **PWM**: aplique PWM no gate → controle de velocidade do motor
4. **Entregável**: H-bridge simulada + comparação MOSFET vs BJT + PWM

---

### Módulo 2.8: Projeto Integrado — Chaveamento e Amplificação
**Tempo: 2h**

#### O que memorizar
- **Darlington**: 2 BJTs em cascata → β_total = β1 × β2. Ganho enorme, mas V_CE_sat ≈ 1.4V
- **Push-pull (classe B/AB)**: NPN + PNP → amplificador de potência. Saída pode source e sink corrente
- **Classe A/B/C/D**: eficiência crescente (25% → 78.5% → >90%), mas distorção aumenta

#### Projeto: "Amplificador de Áudio Classe AB"
1. **Monte** um push-pull com 2 BJTs complementares (NPN TIP41 + PNP TIP42)
2. **Adicione polarização** diodo para eliminar crossover distortion
3. **Alimente com sinal de áudio** (senóide 1kHz) → observe a saída limpa
4. **Conecte a um "speaker"** (resistor de 8Ω) → calcule potência
5. **Entregável**: Amplificador classe AB completo + análise de distorção

#### Checkpoint — Fim da Fase Transistores
- [ ] Sabe usar BJT como chave e calcular R_B para saturação
- [ ] Projeta amplificador emissor comum com polarização por divisor
- [ ] Entende MOSFET e por que é superior ao BJT para potência
- [ ] Consegue montar H-bridge e controlar motor com PWM

---

## Fase 3 — Amplificadores Operacionais

### Módulo 2.9: O Amp-Op Ideal — Regras de Ouro
**Tempo: 2h**

#### O que memorizar
- **Amp-Op**: amplificador diferencial com ganho de malha aberta ≈ infinito (real: 10⁵ a 10⁶)
- **2 entradas**: V+ (não-inversora), V- (inversora). `V_out = A × (V+ - V-)` (A ≈ ∞)
- **Regras de ouro** (com realimentação negativa):
  1. **V+ = V-** (tensão nas entradas é igual)
  2. **I+ = I- = 0** (corrente nas entradas é zero)
- **Alimentação**: V_CC+ e V_CC- (tipicamente ±12V ou ±15V). V_out limitado a ±V_CC (rail-to-rail se especificado)
- **Amp-ops clássicos**: LM741 (vintage), LM358 (dual, rail-to-rail input), TL072 (audio), LM324 (quad)

#### Intuição
O amp-op é o **canivete suíço** da eletrônica. Com apenas resistores e capacitores externos, você transforma ele em: amplificador de tensão, somador, subtrator, integrador, diferenciador, comparador, filtro ativo, oscilador, regulador de tensão, fonte de corrente... As "regras de ouro" parecem mágicas, mas são consequência do ganho infinito com realimentação negativa:

Se A = ∞ e V_out é finito, então (V+ - V-) = V_out / A = finito / ∞ = 0. Logo V+ = V-. QED!

#### Projeto: "Amp-Op Playground"
1. **Comparador** (sem realimentação):
   - V+ = sinal, V- = referência (2.5V). Saída satura: HIGH ou LOW
   - Monte no Falstad e observe
2. **Buffer (seguidor de tensão)**: V_out conectado a V-. Ganho = 1, impedância de saída ≈ 0
3. **Amplificador inversor**: `A_V = -R_F / R_1`. Com R1=1k, RF=10k → ganho = -10
4. **Amplificador não-inversor**: `A_V = 1 + R_F / R_1`. Com R1=1k, RF=9k → ganho = +10
5. **Somador inversor**: `V_out = -(R_F/R1 × V1 + R_F/R2 × V2)` — útil para mixar sinais de áudio
6. **Monte todos no LTspice** com LM741 e verifique as fórmulas
7. **Prompt IA**: *"Derive a fórmula do amplificador inversor A_V = -R_F/R_1 usando as regras de ouro. Depois, explique o que acontece quando o sinal de entrada é grande demais e a saída encosta nos rails (±V_CC)."*
8. **Entregável**: 5 circuitos com amp-op + cálculos vs simulação

#### Erros Comuns
- Esquecer a alimentação (±V_CC) do amp-op — no esquemático às vezes não aparece, mas sem ela não funciona!
- Usar amp-op como comparador (sem realimentação) e esperar precisão — amp-ops não são projetados para operar sem feedback (oscilam, são lentos). Use comparadores dedicados (LM339)
- Achar que as regras de ouro funcionam SEMPRE — só funcionam com realimentação NEGATIVA!

---

### Módulo 2.10: Filtros Ativos e Instrumentação
**Tempo: 2.5h**

#### O que memorizar
- **Filtro ativo**: amp-op + R + C. Vantagens sobre passivo: ganho ≥ 1, impedância de saída baixa, pode cascatear
- **Sallen-Key**: topologia popular para filtros de 2ª ordem (Butterworth, Chebyshev, Bessel)
- **Butterworth**: resposta plana na passband (sem ripple). Mais usado
- **Amp-op de instrumentação** (INA): 3 amp-ops que amplificam diferença entre 2 sinais, rejeitando ruído comum (CMRR alto)
- **Integrador**: `V_out = -(1/RC) ∫ V_in dt` (acumula sinal no tempo)
- **Diferenciador**: `V_out = -RC × dV_in/dt` (detecta mudanças)

#### Projeto: "Filtro Butterworth + Amp de Instrumentação"
1. **Projete filtro passa-baixas Butterworth de 2ª ordem** (Sallen-Key):
   - f_c = 1kHz, ganho = 1. Calcule R e C
   - Monte no LTspice → plote Bode (`.ac dec 1000 1 100k`)
   - Compare com filtro passivo RC de 1ª ordem → observe roll-off mais acentuado (-40dB/dec vs -20dB/dec)
2. **Monte um amp de instrumentação** com 3 amp-ops (INA128 ou discret o com 3× LM741):
   - Ganho = 100. Aplique sinal diferencial de 10mV → saída = 1V
   - Aplique ruído de modo comum (120Hz, simulando hum da rede) → observe rejeição (CMRR)
3. **Entregável**: Filtro Butterworth + INA com medição de CMRR

---

### Módulo 2.11: Osciladores e Geradores de Sinal
**Tempo: 2h**

#### O que memorizar
- **Condição de oscilação (Barkhausen)**: ganho de malha = 1, fase de malha = 0° (ou 360°)
- **Oscilador Wien Bridge**: gera senóide pura. `f = 1/(2πRC)`. Usa amp-op com realimentação positiva + controle de ganho
- **Oscilador Colpitts/Hartley**: usa LC, frequências mais altas (RF)
- **Astável com amp-op**: gera onda quadrada (comparador + RC + realimentação positiva)
- **Gerador de função**: senóide → quadrada (comparador) → triangular (integrador)

#### Projeto: "Gerador de 3 Formas de Onda"
1. **Oscilador Wien Bridge** (1kHz): Monte no LTspice, observe senóide estável
2. **Conversor senóide→quadrada**: alimente senóide num comparador (amp-op sem realimentação)
3. **Conversor quadrada→triangular**: alimente quadrada num integrador (amp-op + cap)
4. **Entregável**: Gerador de 3 formas de onda + controle de frequência

---

### Módulo 2.12: Projeto Integrado — Fonte de Bancada Regulada
**Tempo: 2.5h**

#### O que memorizar
- **Regulador linear (LM317)**: V_out = 1.25 × (1 + R2/R1). Ajustável de 1.25V a 37V
- **Limitação de corrente**: R_sense no coletor de BJT pass-transistor, feedback para amp-op
- **Display de tensão**: divisor resistivo → ADC (para projeto digital futuro)
- **Heat sinking**: P_dissipada = (V_in - V_out) × I_out → calcular heatsink necessário

#### Projeto: "Fonte de Bancada 0-30V / 0-3A"
1. **Monte no LTspice** uma fonte completa:
   - Transformador + ponte retificadora + filtro (do módulo 2.2)
   - LM317 com ajuste por potenciômetro
   - Proteção contra curto-circuito (limitação de corrente com BJT)
   - LED indicador de "ligado"
2. **Teste**: varie V_out de 1.25V a 30V → V_out deve acompanhar
3. **Teste curto-circuito**: limite de corrente a 3A → verificar proteção
4. **Entregável**: Fonte completa + esquemático profissional

#### Checkpoint — Fim da Fase Amp-Ops
- [ ] Aplica regras de ouro sem hesitação
- [ ] Projeta amplificadores inversores e não-inversores
- [ ] Sabe projetar filtros ativos de 2ª ordem
- [ ] Entende condição de Barkhausen para osciladores

---

## Fase 4 — Eletrônica de Potência

### Módulo 2.13: Reguladores Lineares vs Chaveados
**Tempo: 2h**

#### O que memorizar
- **Regulador linear** (7805, LM317): V_in DC → V_out DC menor. P_perda = (V_in - V_out) × I. Eficiência ≈ V_out/V_in
- **Exemplo**: 12V → 5V a 1A: P_out=5W, P_perda=7W, η=42%. Mais calor que potência útil!
- **Regulador chaveado**: liga/desliga muito rápido (100kHz-2MHz) e filtra → eficiência **85-95%**
- **Vantagem linear**: sem ruído de chaveamento, circuito simples. Bom para áudio e instrumentação
- **Vantagem chaveado**: eficiência, pode SUBIR tensão (boost), pode inverter polaridade. Padrão para potência

#### Intuição
Regulador linear é como controlar a vazão de água com um **registro parcialmente aberto** — a pressão excedente é desperdiçada em calor no registro. Regulador chaveado é como controlar com um registro que **abre e fecha rapidamente** — o fluxo médio é controlado pelo duty cycle, e quase nenhuma energia é desperdiçada. É a mesma ideia do PWM que você aprendeu!

#### Projeto: "Linear vs Chaveado — Comparação"
1. **Monte no LTspice**: regulador 7805 (12V→5V, 1A)
   - Meça: P_in, P_out, P_dissipada, η, V_ripple de saída
2. **Monte**: conversor buck com LM2596 (12V→5V, 1A)
   - Meça os mesmos parâmetros
3. **Compare em tabela**: η, ripple, componentes necessários, custo, ruído
4. **Entregável**: Ambos os circuitos + tabela comparativa

---

### Módulo 2.14: Conversor Buck (Step-Down)
**Tempo: 2.5h**

#### O que memorizar
- **Princípio**: chave (MOSFET) liga/desliga → indutor + capacitor filtram → V_out média = D × V_in (D = duty cycle)
- **Componentes**: MOSFET (chave), diodo (freewheeling/catch), indutor (armazenamento), capacitor (filtro)
- **Duty cycle**: `D = V_out / V_in`. Para 12V→5V → D = 5/12 = 41.7%
- **Modo contínuo (CCM)**: corrente no indutor nunca chega a zero. Preferível. Requer L mínimo
- **Ripple de corrente no indutor**: `ΔI = (V_in - V_out) × D / (f × L)`. Menor com f e L maiores
- **Frequência de chaveamento**: 100kHz-2MHz. Maior f → componentes menores, mas mais perdas de gate

#### Intuição
O Buck é como uma **caixa d'água com boia**. O MOSFET (torneira) abre → água (corrente) enche o indutor (caixa). O MOSFET fecha → o indutor continua fornecendo corrente (inércia). O capacitor (boia) mantém o nível constante. O duty cycle controla o nível médio. É genial: você transforma 12V em 5V com 90%+ de eficiência, desperdiçando apenas 0.5W em vez dos 7W do regulador linear!

#### Projeto: "Buck Converter DIY"
1. **Monte no LTspice** um Buck converter com MOSFET + diodo:
   ```spice
   * Fonte PWM como teste
   V_PWM gate 0 PULSE(0 12 0 10n 10n {D/f} {1/f})
   .param f=100k D=0.417
   M1 Vin sw gate 0 IRF540N
   D1 0 sw MBR340
   L1 sw Vout 100u
   C1 Vout 0 100u
   R_load Vout 0 5
   V1 Vin 0 12
   .tran 5m
   ```
2. **Observe**: corrente no indutor (onda triangular), tensão de saída (ripple)
3. **Varie D**: 0.2, 0.4, 0.6, 0.8 → observe V_out = D × V_in
4. **Varie L**: 10μH, 47μH, 100μH, 470μH → observe ripple diminuir
5. **Entregável**: Buck funcional + gráficos + análise de eficiência

---

### Módulo 2.15: Conversor Boost (Step-Up) e Buck-Boost
**Tempo: 2.5h**

#### O que memorizar
- **Boost**: V_out > V_in. `V_out = V_in / (1 - D)`. D=0.5 → V_out = 2×V_in
- **Princípio**: MOSFET liga → indutor carrega. MOSFET desliga → indutor "empurra" corrente para saída em tensão mais alta
- **Buck-Boost**: pode subir OU descer. `V_out = -V_in × D / (1 - D)`. Saída invertida!
- **Aplicações do Boost**: 3.7V (bateria Li-Ion) → 5V (USB), LED drivers, power banks
- **Aplicações do Buck-Boost**: quando V_in pode ser maior ou menor que V_out (ex: bateria descarregando)

#### Projeto: "Boost: Bateria 3.7V → USB 5V"
1. **Monte Boost no LTspice**: 3.7V → 5V a 500mA. D = 1 - 3.7/5 ≈ 0.26
2. **Monte Buck-Boost**: 3-6V input → 5V output (cobre toda a curva de descarga de Li-Ion)
3. **Entregável**: Ambos os conversores + análise + comparação

---

### Módulo 2.16: Inversores e Fontes Chaveadas Completas
**Tempo: 2.5h**

#### O que memorizar
- **Inversor**: converte DC → AC. H-bridge com PWM sinusoidal (SPWM) → filtro → senóide limpa
- **Full-bridge inversor**: 4 MOSFETs controlados por PWM → gera AC a partir de DC
- **Fonte chaveada flyback**: isolada (transformador de alta frequência). Usada em carregadores de celular
- **Topologias**: flyback (baixa potência), forward (média), half-bridge e full-bridge (alta potência)

#### Projeto: "Mini Inversor 12V DC → 120V AC"
1. **Monte no LTspice** um inversor full-bridge simplificado:
   - 4 MOSFETs com gate drivers
   - PWM sinusoidal (SPWM) controlando os gates
   - Filtro LC na saída → senóide limpa
   - Carga: resistor de 100Ω (simula lâmpada)
2. **Observe**: a saída deve ser uma senóide com amplitude proporcional ao V_DC
3. **Meça**: THD (Total Harmonic Distortion) — quanto mais pura a senóide, melhor
4. **Entregável**: Inversor funcional + análise de THD + esquemático

#### Checkpoint Final — Pilar 2 Completo!
- [ ] Projeta retificadores com filtro e calcula ripple
- [ ] Usa BJT como chave e amplificador, MOSFET para potência
- [ ] Projeta circuitos com amp-op (amplificadores, filtros, osciladores)
- [ ] Entende conversores DC-DC (Buck, Boost) e calcula duty cycle, indutor, capacitor
- [ ] Sabe a diferença prática entre regulador linear e chaveado
- [ ] Monta fontes de alimentação completas do transformador à saída regulada

> **Próximo passo**: [Pilar 3 — Sistemas Digitais e Microcontrolados](ee_digital_embarcados_roadmap.md)

---

## Fase 6 — Introdução a RF e Linhas de Transmissão

### Módulo 2.17: Linhas de Transmissão e Impedância Característica
**Tempo: 3h**

#### O que memorizar
- **Quando fio ≠ curto-circuito**: quando o comprimento do fio ≈ λ/10 ou mais, o fio se comporta como linha de transmissão (onda se propaga!)
- **Impedância característica**: Z₀ = √(L/C) por unidade de comprimento. Cabo coaxial RG-58: Z₀=50Ω. TV: 75Ω. Par trançado Ethernet: 100Ω
- **Reflexão**: se Z_carga ≠ Z₀, parte da energia é REFLETIDA! Γ = (Z_L-Z₀)/(Z_L+Z₀)
- **Casamento de impedância**: Z_L = Z₀ → Γ=0 → máxima transferência de potência, sem reflexão
- **Onda estacionária**: reflexões criam padrão estacionário. SWR = (1+|Γ|)/(1-|Γ|). SWR=1 = perfeito
- **λ/4 transformer**: trecho de linha de λ/4 com Z₀ = √(Z₁×Z₂) casa impedâncias diferentes
- **Velocidade de propagação**: v = c/√ε_r. Em coaxial com polietileno (ε_r=2.3): v ≈ 0.66c

#### Intuição
Em DC e baixas frequências, um fio é só um fio. Mas em MHz/GHz, o fio se torna um sistema distribuído — a onda "viaja" pelo fio e pode ser REFLETIDA no final se a carga não tem a impedância certa. É como uma onda numa corda: se a ponta está solta (aberta), reflete sem inverter. Se está presa (curto), reflete invertida. Casamento = a onda é 100% absorvida, nada volta.

#### Projeto
1. **Calcule** λ para 100MHz, 1GHz, 10GHz → a partir de qual frequência um fio de 10cm é "linha de transmissão"?
2. **Calcule Γ** para Z₀=50Ω com cargas de: 50Ω, 0Ω (curto), ∞ (aberto), 100Ω, 25Ω
3. **Prompt IA**: *"Por que cabos de antena de TV são 75Ω e cabos de laboratório/rádio são 50Ω? O que acontece se conectar uma antena de 300Ω num cabo de 75Ω sem balun?"*

---

### Módulo 2.18: Carta de Smith e Parâmetros S
**Tempo: 2.5h**

#### O que memorizar
- **Carta de Smith**: gráfico circular que mapeia impedância complexa Z em coeficiente de reflexão Γ
  - Centro = Z₀ (casado, Γ=0). Borda = |Γ|=1 (total reflexão)
  - Eixo horizontal = resistência pura. Círculos = reatância constante
- **Parâmetros S** (S-parameters): descrevem comportamento de rede de RF como reflexão/transmissão
  - S11 = reflexão na entrada (quanto volta). S21 = transmissão (quanto passa)
  - S22 = reflexão na saída. S12 = transmissão reversa (isolação)
  - |S11| < -10dB = bom casamento (< 10% refletido). |S21| > 0dB = ganho
- **VNA (Vector Network Analyzer)**: mede parâmetros S de componentes reais
- **Casamento com componentes**: L e C em série/paralelo para mover impedância na carta de Smith até o centro

#### Projeto
1. **Use simulador de Carta de Smith online**: plote impedância de antena e adicione L/C para casar
2. **Leia datasheet de filtro SAW**: interprete S21 (transmissão) e S11 (casamento)
3. **Prompt IA**: *"Explique a carta de Smith como se fosse um GPS de impedância: onde estou (Z_carga), onde quero chegar (Z₀=50Ω), e quais componentes L/C uso para 'andar' na carta."*

---

### Módulo 2.19: EMC — Compatibilidade Eletromagnética
**Tempo: 2h**

#### O que memorizar
- **EMI (Interferência)**: emissão indesejada que afeta outros dispositivos. Conduzida (pelo fio) ou irradiada (pelo ar)
- **EMC**: capacidade de funcionar sem causar nem sofrer interferência. Normativa: CISPR, FCC, CE
- **Fontes de EMI**: fontes chaveadas, motores, arcos, transmissores
- **Blindagem**: gabinete metálico atenua campos. Eficácia depende de freq, material, aberturas
- **Filtro de linha**: indutores de modo comum + capacitores X/Y na entrada de alimentação
- **Plano de terra contínuo**: minimiza área de loops → minimiza irradiação
- **Desacoplamento**: 100nF cerâmico em CADA VCC de CI, o mais perto possível

#### Projeto
1. **Identifique** 5 fontes de EMI em um esquemático de placa com microcontrolador + fonte chaveada
2. **Projete** filtro de linha básico (CMC + caps X2/Y1) para fonte de alimentação
3. **Prompt IA**: *"Explique por que placas com plano de terra contínuo emitem menos EMI que placas com trilhas de terra. Use o conceito de área de loop."*

#### Checkpoint — RF e EMC
- [ ] Sabe quando um fio se comporta como linha de transmissão (λ/10)
- [ ] Calcula reflexão e SWR
- [ ] Lê parâmetros S de datasheet
- [ ] Conhece boas práticas de EMC (desacoplamento, plano de terra, blindagem)

---

## Se Você Travar

| Problema | Solução |
|----------|--------|
| Diodo não conduz na simulação | Verifique polaridade (ânodo deve ser mais positivo que cátodo). Verifique se a tensão aplicada > 0.7V |
| BJT não satura | Verifique I_B — precisa ser suficiente (I_C/β_mín × margem). Verifique se V_CE < 0.3V |
| Amp-op saída presa em ±V_CC | Provavelmente sem realimentação negativa (modo comparador) ou entradas invertidas (realimentação positiva = oscilador, não amplificador) |
| MOSFET não liga | V_GS deve ser > V_th (leia o datasheet!). Para IRF540N, V_th ≈ 2-4V. Se controlando com 3.3V, pode não ser suficiente — use logic-level MOSFET (IRLZ44N) |
| Buck converter oscila | Verifique indutor (L muito pequeno = DCM indesejado) e capacitor de saída (C muito pequeno = ripple excessivo). Verifique frequência de chaveamento |
| Fonte de bancada não regula | Verifique se V_in > V_out + dropout do regulador (~2V para LM317). Se V_in ≈ V_out, não regula |
| Ganho do amplificador errado | Verifique se os caps de acoplamento são grandes o suficiente para a frequência do sinal (X_C deve ser << R_in na freq de interesse) |
