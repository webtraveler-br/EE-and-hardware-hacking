# Pilar 1: Circuitos DC e AC

> **Sobre este pilar**: Cada módulo é uma sessão de **1h–3h** onde você primeiro **simula** o circuito, **observa** o comportamento, **questiona** a IA para entender a física, e depois **modela** matematicamente. A matemática vem DEPOIS da intuição.
>
> **Ferramentas**: [Falstad](https://www.falstad.com/circuit/) (visualização intuitiva) + [LTspice](https://www.analog.com/ltspice) (análise precisa)
>
> **Pré-requisitos**: [Pilar 0, Fases 1-3](ee_matematica_fisica_roadmap.md) (álgebra, trig, complexos, derivadas, integrais). Fases 4-5 para módulos avançados (cálculo vetorial, EDOs).
>
> **Conexões com outros pilares**:
> - **Base de**: [Pilar 0](ee_matematica_fisica_roadmap.md) — álgebra (Mód 0.2), trig/senóides (Mód 0.4), complexos/fasores (Mód 0.5), dB (Mód 0.6), derivadas (Mód 0.7-0.10), integrais (Mód 0.11-0.14), EDOs (Mód 0.18-0.20), matrizes (Mód 0.21-0.22)
> - **Alimenta**: [Pilar 2](ee_eletronica_roadmap.md) (Thévenin, polarização, filtros), [Pilar 4](ee_eletrotecnica_automacao_roadmap.md) (trifásico, transformadores), [Pilar 5](ee_controle_sinais_roadmap.md) (modelagem de plantas)
> - **Prática real**: [Lab L.2-L.5](ee_laboratorio_real_roadmap.md) (multímetro, protoboard, transitórios, osciloscópio)
> - **Segurança**: [HH 1.1](hardware_hacking_roadmap.md) (eletrônica digital), [HH 2.2-2.3](hardware_hacking_roadmap.md) (análise de PCB, identificação de pinos)
>
> [Voltar ao Índice](ee_roadmap_index.md)

---

## Visão Geral

| Fase | Módulos | Foco | Horas |
|------|---------|------|-------|
| **DC Básico** | 1.1–1.5 | Tensão, corrente, resistência, leis de Kirchhoff | ~10h |
| **DC Avançado** | 1.6–1.9 | Thévenin, Norton, superposição, análise nodal/malha | ~9h |
| **Transitórios** | 1.10–1.13 | Capacitores, indutores, circuitos RC/RL/RLC | ~9h |
| **AC** | 1.14–1.18 | Corrente alternada, fasores, impedância, potência, filtros | ~12h |

---

## Fase 1 — Corrente Contínua: Fundamentos

### Módulo 1.1: Tensão, Corrente e Resistência — Os 3 Pilares
**Tempo: 2h**

#### O que memorizar
- **Tensão (V)**: diferença de potencial elétrico entre dois pontos. Unidade: Volt (V). É a "pressão" que empurra cargas
- **Corrente (I)**: fluxo de cargas elétricas. Unidade: Ampère (A). É o "fluxo de água" no circuito
- **Resistência (R)**: oposição ao fluxo de corrente. Unidade: Ohm (Ω). É o "estreitamento do cano"
- **Lei de Ohm**: `V = I × R` — a equação mais importante de toda a engenharia elétrica
- **Potência**: `P = V × I = I²R = V²/R` — unidade: Watt (W). É a taxa de consumo/dissipação de energia
- **Energia**: `E = P × t` — unidade: Joule (J) ou kWh. Um kWh = 3.600.000 J
- **Prefixos SI**: mili (m, 10⁻³), micro (μ, 10⁻⁶), kilo (k, 10³), mega (M, 10⁶)

#### Intuição
Pense num circuito como um sistema hidráulico:
- **Tensão** = pressão da água (altura do reservatório)
- **Corrente** = fluxo de água (litros/segundo passando pelo cano)
- **Resistência** = estreitamento do cano (quanto menor o cano, menos água passa)
- **Potência** = trabalho útil que a água faz (girar uma roda d'água)

A Lei de Ohm diz: com mais pressão (tensão), mais água flui (corrente). Com cano mais estreito (resistência), menos água passa. É simples, mas é a fundação de TUDO.

**Por que importa**: quando você calcula se um fio aguenta a corrente de um motor, está usando V=IR. Quando dimensiona um resistor para um LED não queimar, está usando V=IR. Quando sua conta de luz é calculada, usa P=V×I×tempo.

#### Projeto: "Primeiros Circuitos"
1. **Abra o Falstad**: [falstad.com/circuit](https://www.falstad.com/circuit/)
2. **Circuito 1 — Lei de Ohm visual**:
   - Monte: fonte de 12V + resistor de 1kΩ
   - Observe as "partículas" de corrente (bolinhas verdes) — veja a velocidade
   - Clique no resistor → verifique: V = 12V, I = 12mA, P = 144mW
   - Agora mude R para 2kΩ → I cai para 6mA. Mude para 500Ω → I sobe para 24mA
   - **Conclusão**: I é inversamente proporcional a R (com V constante)
3. **Circuito 2 — Potência e aquecimento**:
   - Monte: fonte 12V + resistor de 100Ω
   - Calcule: I = 12/100 = 120mA, P = 12 × 0.12 = 1.44W
   - Um resistor de ¼W aguentaria? **NÃO! Queimaria!** Precisa de pelo menos 2W
   - **Lição**: sempre verifique a potência dissipada antes de escolher um componente
4. **Circuito 3 — Curto-circuito**:
   - Monte: fonte 12V + fio direto (sem resistor)
   - Observe: corrente vai ao infinito → na vida real, o fio derrete ou o fusível abre
   - **Lição**: resistência zero = corrente infinita = destruição
5. **Prompt IA**: *"Explique a diferença entre tensão e corrente usando 3 analogias diferentes. Depois, explique por que um curto-circuito é perigoso em termos de P=I²R."*
6. **Entregável**: Capturas de tela dos 3 circuitos + cálculos escritos à mão

#### Erros Comuns
- Confundir tensão com corrente (tensão é diferença de potencial ENTRE dois pontos, corrente é fluxo ATRAVÉS de um componente)
- Esquecer os prefixos: 1kΩ = 1000Ω, 1mA = 0.001A. Errar por fator de 1000 é devastador
- Achar que corrente é "consumida" pelo componente — NÃO! A corrente que entra é igual à que sai. O que é "consumido" é energia (potência × tempo)

---

### Módulo 1.1B: Lendo Componentes Reais — Código de Cores e Datasheets
**Tempo: 1h**

#### O que memorizar
- **Código de cores de resistores (4 faixas)**: Preto=0, Marrom=1, Vermelho=2, Laranja=3, Amarelo=4, Verde=5, Azul=6, Violeta=7, Cinza=8, Branco=9. Multiplicador (3ª faixa): mesmas cores = 10^N. Tolerância (4ª): Ouro=5%, Prata=10%
- **Mnemônico**: "**P**retinha **M**aria **V**iajou **L**onge **A**través **V**erde **A**zul **V**ioleta **C**éu **B**ranco" (0-9)
- **E12/E24**: séries de valores padronizados. E12: 1.0, 1.2, 1.5, 1.8, 2.2, 2.7, 3.3, 3.9, 4.7, 5.6, 6.8, 8.2 × 10^N
- **Lendo datasheets**: todo componente tem um datasheet com especificações máximas (V_max, I_max, P_max, temperatura)
- **Valores comerciais de capacitores**: 10, 22, 47, 100 (pF, nF, μF)
- **SMD vs Through-Hole**: SMD é menor (montagem superficial), through-hole tem fios (mais fácil para protótipos)

#### Intuição
Na simulação, você escolhe "1kΩ" e pronto. Na vida real, precisa pegar o resistor correto da gaveta lendo as faixas coloridas. Parece bobo, mas é a habilidade mais prática que existe — errar o resistor por um fator de 10 (marrom vs vermelho) é o erro mais comum de quem começa.

#### Projeto: "Leitura Rápida"
1. **Pratique 20 resistores** com um [simulador de código de cores online](https://www.digikey.com/en/resources/conversion-calculators/conversion-calculator-resistor-color-code)
2. **Abra 3 datasheets** (1N4148, LM7805, 2N2222) e identifique: pinagem, ratings máximos, gráficos de operação
3. **Prompt IA**: *"Me dê 10 resistores com código de cores aleatório. Vou tentar ler o valor e você confirma."*
4. **Entregável**: 20 resistores lidos corretamente + resumo dos 3 datasheets

---

### Módulo 1.2: Resistores em Série e Paralelo
**Tempo: 2h**

#### O que memorizar
- **Série**: mesma corrente, tensões se dividem. `R_total = R1 + R2 + R3 + ...`
- **Paralelo**: mesma tensão, correntes se dividem. `1/R_total = 1/R1 + 1/R2 + ...` ou para dois: `R_total = (R1 × R2) / (R1 + R2)`
- **Divisor de tensão**: `V_out = V_in × R2 / (R1 + R2)` — circuito mais importante da eletrônica!
- **Divisor de corrente**: `I1 = I_total × R2 / (R1 + R2)` (corrente pelo ramo é proporcional à resistência do OUTRO ramo)
- **Resistor equivalente**: simplificar combinações série-paralelo até ter um único resistor

#### Intuição
**Série** = canos em sequência. Toda a água passa por todos os canos. Se um cano é fino (alta resistência), ele limita o fluxo total. A resistência total é a soma.

**Paralelo** = canos lado a lado. A água escolhe o caminho mais fácil (menor resistência). Mais caminhos em paralelo = menos resistência total (porque a água tem mais opções). Dois resistores de 1kΩ em paralelo = 500Ω. Sempre menor que o menor resistor individual!

**Divisor de tensão** é como dividir uma queda d'água em degraus. Se R1 = R2, cada um fica com metade da tensão. Se R2 >> R1, quase toda a tensão cai em R2.

#### Projeto: "O Divisor de Tensão Universal"
1. **No Falstad**, monte um divisor de tensão: 12V → R1(10kΩ) → R2(10kΩ) → GND
   - Meça V_out (entre R1 e R2): deve ser 6V. Confira com a fórmula
2. **Varie R2**: 1kΩ, 5kΩ, 10kΩ, 20kΩ, 100kΩ — anote V_out para cada valor
   - Plote V_out vs R2 (pode ser no papel ou em Python) → observe a curva
3. **Problema prático**: preciso de 3.3V a partir de 5V para alimentar um sensor
   - Calcule R1 e R2 para obter 3.3V (dica: R1/R2 = (5-3.3)/3.3 ≈ 0.515)
   - Monte no Falstad e verifique
4. **Circuitos em paralelo**: monte 3 resistores de 1kΩ em paralelo
   - R_total = ? Verifique no Falstad (deve ser ~333Ω)
5. **Desafio**: circuito misto — série-paralelo complexo:
   ```
   12V ─── R1(1k) ─┬─ R2(2k) ─── GND
                    │
                    └─ R3(3k) ─── GND
   ```
   - Calcule R_total, I_total, e a corrente em cada ramo
6. **Prompt IA**: *"Projete um divisor de tensão para obter 3.3V a partir de 12V. Depois, explique por que NÃO é uma boa ideia usar um divisor de tensão para alimentar um microcontrolador. O que acontece quando a carga varia?"*
7. **Entregável**: Todos os circuitos simulados + cálculos + gráfico do divisor de tensão

---

### Módulo 1.3: Leis de Kirchhoff — KVL e KCL
**Tempo: 2h**

#### O que memorizar
- **KCL (Lei das Correntes de Kirchhoff)**: a soma das correntes que entram num nó é igual à soma das que saem. `∑I_entrada = ∑I_saída`
- **KVL (Lei das Tensões de Kirchhoff)**: a soma das tensões ao longo de uma malha fechada é zero. `∑V_malha = 0`
- **Convenção de sinais**: siga o sentido da malha — subindo no elemento = +V, descendo = -V. Entrando pela porta + da fonte = +V
- **Nó**: ponto onde 3 ou mais condutores se encontram
- **Malha**: caminho fechado no circuito (volta ao ponto de partida)
- **Ramo**: caminho entre dois nós (contém um ou mais componentes em série)

#### Intuição
**KCL = conservação de carga**: elétrons não aparecem do nada nem desaparecem. Se 5A entram num nó, 5A têm que sair (por uma ou mais saídas). É como um entroncamento de trânsito — o número de carros que entram é igual ao que sai.

**KVL = conservação de energia**: se você caminhar ao longo de um circuito fechado e voltar ao ponto de partida, o ganho líquido de energia é zero. Cada fonte adiciona energia (+V), cada resistor consome energia (-V). Na volta completa: ganhos = perdas.

**Estas são as duas leis mais poderosas da análise de circuitos.** Com KVL + KCL + V=IR, você resolve QUALQUER circuito, por mais complexo que seja.

#### Projeto: "Kirchhoff Detective"
1. **Monte no Falstad** o circuito com 2 malhas:
   ```
        ┌── R1(1kΩ) ──┬── R2(2kΩ) ──┐
   V1(12V)            R3(3kΩ)       V2(6V)
        └──────────────┴─────────────┘
   ```
2. **Antes de simular**, aplique KVL e KCL no papel:
   - Malha 1 (esquerda): V1 - I1×R1 - I3×R3 = 0
   - Malha 2 (direita): -V2 + I2×R2 + I3×R3 = 0 (cuidado com sinais!)
   - Nó central: I1 = I2 + I3 (KCL)
   - Resolva o sistema de 3 equações → encontre I1, I2, I3
3. **Simule** e compare: suas respostas batem com o Falstad?
4. **Repita** com um circuito de 3 malhas (mais complexo)
5. **Prompt IA**: *"Dado este circuito com 2 fontes e 3 resistores [descreva], monte as equações de KVL e KCL. Resolva passo a passo mostrando cada substituição."*
6. **Entregável**: Sistema de equações resolvido à mão + comparação com simulação

#### Erros Comuns
- **Errar sinais na KVL**: decida um sentido para percorrer a malha e seja consistente. Se a corrente "sobe" no resistor = queda de tensão (negativo)
- **Esquecer de usar KCL**: muita gente tenta resolver só com KVL e fica com equações a mais/menos
- **Confundir nó com conexão simples**: um fio reto entre 2 componentes NÃO é um nó (tem a mesma tensão)

---

### Módulo 1.4: Fontes Reais, Potência e Eficiência
**Tempo: 1.5h**

#### O que memorizar
- **Fonte ideal de tensão**: mantém V constante independente da corrente. Resistência interna = 0. **Não existe na realidade**
- **Fonte real de tensão**: V = V_fonte - I × R_interna. Quanto mais corrente, mais a tensão cai
- **Máxima transferência de potência**: ocorre quando R_carga = R_interna (mas eficiência = 50%!)
- **Eficiência**: η = P_carga / P_total × 100%. Em fontes de potência, queremos η > 85%, não máxima transferência
- **Fonte ideal de corrente**: mantém I constante independente da tensão. Resistência interna = ∞

#### Intuição
Uma bateria de carro tem V = 12.6V quando descarregada em repouso. Mas quando você dá partida (motor de arranque puxa 200A), a tensão cai para ~10V. Para onde foram os 2.6V? Foram "perdidos" na resistência interna da bateria (~13mΩ). Quanto mais corrente, mais perda. É por isso que baterias "fraquinham" — a resistência interna aumenta com a idade.

**Máxima transferência de potência vs eficiência**: são conceitos opostos! Em áudio (amplificador → alto-falante), queremos máxima potência no speaker, mesmo que metade da energia vire calor. Em energia solar (painel → bateria), queremos eficiência máxima (perder o mínimo possível).

#### Projeto: "Bateria Real vs Ideal"
1. **No Falstad**, monte:
   - Fonte de 12V + resistência interna de 1Ω + carga variável (10Ω, 5Ω, 2Ω, 1Ω, 0.5Ω)
2. **Para cada carga**, meça e anote: V_carga, I_circuito, P_carga, P_interna, η
3. **Plote** 3 gráficos:
   - V_carga vs I (deve ser reta descendente)
   - P_carga vs R_carga (deve ter um máximo em R=1Ω = R_interna)
   - η vs R_carga (deve subir com R_carga)
4. **Conclusão**: onde é o ponto de máxima potência? E de máxima eficiência? São o mesmo?
5. **Prompt IA**: *"Compare máxima transferência de potência com máxima eficiência em 3 aplicações reais: amplificador de áudio, painel solar, e carregador de celular. Em qual caso cada conceito é mais relevante?"*
6. **Entregável**: Tabela de medições + 3 gráficos + análise escrita

#### Erros Comuns
- Confundir fonte ideal com fonte real ao analisar circuitos (pilhas velhas = resistência interna alta, baterias novas = baixa)
- Achar que "máxima potência" = melhor opção sempre — na maioria das aplicações práticas, eficiência importa MAIS

---

### Módulo 1.5: Circuitos com Múltiplas Fontes — Superposição
**Tempo: 2h**

#### O que memorizar
- **Princípio da Superposição**: em circuitos lineares com múltiplas fontes, a resposta total é a soma das respostas individuais
- **Procedimento**: desligar todas as fontes exceto uma → calcular → repetir para cada fonte → somar resultados
- **"Desligar" uma fonte**: fonte de tensão → substituir por curto-circuito (fio). Fonte de corrente → substituir por circuito aberto
- **Linearidade**: superposição SÓ funciona em circuitos lineares (resistores, capacitores, indutores). NÃO funciona com diodos, transistores, etc.
- **Quando usar**: circuitos com 2+ fontes independentes. É mais intuitivo que resolver sistemas de equações

#### Intuição
Imagine uma piscina com 2 torneiras (fontes) e 1 ralo (carga). Para calcular o nível da água, você pode: (1) abrir só a torneira A, medir o nível; (2) abrir só a torneira B, medir; (3) somar os dois níveis. É exatamente isso que a superposição faz — analisa cada fonte isoladamente e soma os efeitos.

#### Projeto: "Superposição na Prática"
1. **Monte no Falstad**:
   ```
        ┌── R1(2kΩ) ──┬── R2(4kΩ) ──┐
   V1(10V)             R3(6kΩ)     V2(5V)
        └──────────────┴─────────────┘
   ```
2. **Simule o circuito completo** — anote I em cada ramo e V em cada nó
3. **Agora aplique superposição** (no papel + simulação):
   - **Etapa 1**: V1 ativa, V2 = curto → calcule I1', I2', I3'
   - **Etapa 2**: V2 ativa, V1 = curto → calcule I1'', I2'', I3''
   - **Soma**: I1 = I1' + I1'', etc.
4. **Compare** com a simulação do circuito completo — devem bater!
5. **Entregável**: Resolução passo a passo + comparação com simulação

#### Checkpoint — Fim da Fase DC Básico
Antes de avançar, você deve conseguir:
- [ ] Aplicar V=IR em qualquer circuito simples sem hesitação
- [ ] Calcular resistência equivalente de qualquer combo série-paralelo
- [ ] Montar e resolver equações KVL/KCL para circuitos de 2-3 malhas
- [ ] Explicar a diferença entre fonte ideal e real
- [ ] Usar superposição em circuitos com 2+ fontes

---

## Fase 2 — Corrente Contínua: Análise Avançada

### Módulo 1.6: Thévenin e Norton — Simplificando o Mundo
**Tempo: 2.5h**

#### O que memorizar
- **Teorema de Thévenin**: qualquer rede linear de 2 terminais pode ser substituída por uma fonte de tensão V_th em série com uma resistência R_th
- **Como encontrar V_th**: tensão de circuito aberto (desconecte a carga, meça V entre os terminais)
- **Como encontrar R_th**: desligue todas as fontes independentes, calcule R equivalente vista dos terminais
- **Teorema de Norton**: mesma rede, mas representada como fonte de corrente I_n em paralelo com R_n
- **Conversão**: V_th = I_n × R_n, e R_th = R_n (são equivalentes!)
- **Aplicação prática**: simplifica circuitos complexos quando você só se importa com o que acontece em 2 terminais (ex: o que a carga "vê")

#### Intuição
Imagine que você tem um circuito monstro de 50 componentes alimentando um LED. Você quer saber se o LED vai ligar e com que brilho. Thévenin diz: **não importa o quão complexo é o circuito, do ponto de vista do LED, ele se comporta como uma fonte simples + um resistor.** É como se toda a complexidade fosse "resumida" em 2 números: V_th e R_th. É o MVP da simplificação de circuitos.

#### Projeto: "Thévenin de Circuitos Reais"
1. **Monte um circuito complexo no Falstad** (3+ resistores, 1-2 fontes):
   ```
   12V ── R1(1k) ──┬── R2(2k) ──┬── [CARGA]
                    │             │
                    R3(3k)      R4(4k)
                    │             │
                    └─────────────┘
                          │
                         GND
   ```
2. **Encontre o equivalente Thévenin** dos terminais [CARGA]:
   - Remova a carga → meça V entre os terminais = V_th
   - Desligue a fonte (curto) → calcule R entre os terminais = R_th
3. **Verifique**: substitua o circuito todo por V_th + R_th → conecte diferentes cargas (100Ω, 1kΩ, 10kΩ) → as correntes devem ser idênticas ao circuito original!
4. **Converta para Norton**: I_n = V_th / R_th, R_n = R_th
5. **No LTspice**: refaça o circuito com precisão — use `.op` para análise DC
6. **Entregável**: Thévenin e Norton calculados + verificação com 3 cargas + LTspice

---

### Módulo 1.7: Análise Nodal e de Malhas — Métodos Sistemáticos
**Tempo: 2.5h**

#### O que memorizar
- **Análise Nodal**: escolha um nó como referência (GND), escreva KCL para cada nó desconhecido → sistema de equações em tensões nodais
- **Análise de Malhas**: defina correntes de malha (imaginárias, girando em cada "janela" do circuito) → escreva KVL para cada malha → sistema de equações em correntes de malha
- **Quando usar qual**:
  - Nodal: melhor quando há muitas fontes de corrente ou poucos nós
  - Malhas: melhor quando há muitas fontes de tensão ou poucas malhas
- **Supermalha**: quando uma fonte de corrente fica entre 2 malhas
- **Supernó**: quando uma fonte de tensão fica entre 2 nós

#### Intuição
KVL + KCL dão equações "ad hoc" — você escolhe quais nós e malhas analisar, e pode acabar com equações redundantes ou insuficientes. **Análise nodal e de malhas são métodos SISTEMÁTICOS** — seguindo o procedimento, você SEMPRE chega na quantidade certa de equações. É como a diferença entre resolver um problema de matemática "por tentativa" vs "por fórmula". Métodos sistemáticos escalam para circuitos de qualquer tamanho.

#### Projeto: "Resolvedor Sistemático"
1. **Circuito com 3 nós** (2 desconhecidos):
   ```
        ┌── R1(1k) ──┬── R2(2k) ──┬── R3(3k) ──┐
   V1(12V)          nó A        nó B           V2(6V)
        └─────────────┴────R4(4k)─┴─────────────┘
   ```
2. **Resolva por análise nodal**:
   - Nó referência: GND (inferior)
   - KCL no nó A: (V1-Va)/R1 = (Va-Vb)/R2 + Va/R4 ... → monte as equações
   - KCL no nó B: (Va-Vb)/R2 = (Vb-V2)/R3 + Vb/R4 ...
   - Resolva o sistema 2×2
3. **Resolva o mesmo circuito por análise de malhas**:
   - Defina correntes de malha I_a (esquerda) e I_b (direita)
   - KVL malha A: V1 - I_a×R1 - (I_a-I_b)×R2 - I_a×R4 = 0
   - KVL malha B: -(I_b-I_a)×R2 - I_b×R3 + V2 - I_b×R4 = 0
4. **Compare** ambas as soluções — devem dar os mesmos valores de corrente e tensão!
5. **Simule no Falstad** e confira todas as respostas
6. **Bônus**: resolva com Python (numpy):
   ```python
   import numpy as np
   # Ax = b → x = A⁻¹b
   A = np.array([[...], [...]])
   b = np.array([...])
   x = np.linalg.solve(A, b)
   ```
7. **Entregável**: Resolução nodal + malhas + script Python + comparação com simulação

---

### Módulo 1.8: Circuitos com Fontes Dependentes
**Tempo: 2h**

#### O que memorizar
- **Fonte dependente**: tensão ou corrente controlada por OUTRA variável do circuito
- **4 tipos**: VCVS (tensão controlada por tensão), VCCS (corrente controlada por tensão), CCVS (tensão controlada por corrente), CCCS (corrente controlada por corrente)
- **Notação**: μV_x (ganho de tensão), g_m × V_x (transcondutância), r × I_x (transresistência), β × I_x (ganho de corrente)
- **Importância**: transistores, amp-ops e outros dispositivos ativos são modelados como fontes dependentes! Este é o "link" entre circuitos passivos e eletrônica
- **Análise**: mesmos métodos (KVL, KCL, nodal, malhas), mas a fonte dependente adiciona uma equação extra que relaciona a variável de controle com a fonte

#### Intuição
Um transistor BJT é essencialmente uma **fonte de corrente controlada por corrente**: a corrente do coletor (grande) é β vezes a corrente de base (pequena). Um MOSFET é uma **fonte de corrente controlada por tensão**: I_D = g_m × V_GS. Entender fontes dependentes agora vai tornar o Pilar 2 (Eletrônica) muito mais fácil.

#### Projeto: "Modelando um Amplificador Simples"
1. **Monte um circuito com VCVS** (amplificador de tensão ideal):
   - Entrada: V_in = 1V
   - VCVS: V_out = 10 × V_in (ganho = 10)
   - Carga: R_L = 1kΩ
   - Monte no LTspice usando componente "E" (voltage-controlled voltage source)
2. **Adicione resistência de saída**: coloque R_s = 100Ω em série com a VCVS
   - Observe: como R_L afeta V_out agora? (divisor de tensão R_s / R_L!)
3. **Simule um transistor simplificado**: CCCS com β=100
   - I_B = 10μA → I_C = 1mA
   - Monte no LTspice e verifique
4. **Entregável**: Circuitos com fontes dependentes + análise de como R_L afeta ganho

---

### Módulo 1.9: Projeto DC Integrado — Analisador de Circuito
**Tempo: 2h**

#### O que memorizar
- **Fluxo de análise DC completo**: identificar série/paralelo → simplificar → Thévenin/Norton → nodal/malhas → resolver
- **Escolha do método**: bom engenheiro sabe qual método é mais eficiente para cada circuito
- **Documentação**: sempre documente suas premissas, métodos e resultados

#### Projeto: "O Circuito Desafio"
1. **Resolva o circuito abaixo usando TODOS os métodos** aprendidos:
   ```
   V1(24V) ─── R1(1k) ──┬── R2(2k) ──┬── R3(3k) ── V2(12V)
                          │             │
                        R4(4k)       R5(5k)
                          │             │
                     I1(2mA)↑         GND
                          │
                         GND
   ```
2. Métodos: (a) Superposição, (b) Thévenin nos terminais de R5, (c) Análise nodal, (d) Análise de malhas
3. **Todos devem dar a MESMA resposta!**
4. **Automatize** com script Python
5. **Entregável**: 4 resoluções + script Python + comparação com LTspice

#### Checkpoint — Fim da Fase DC Avançado
- [ ] Consegue encontrar Thévenin/Norton de qualquer circuito linear
- [ ] Sabe montar equações de análise nodal e de malhas sistematicamente
- [ ] Entende fontes dependentes e sabe usar em análise
- [ ] Resolve circuitos complexos com múltiplos métodos e obtém a mesma resposta

---

## Fase 3 — Transitórios: Quando o Tempo Importa

### Módulo 1.10: Capacitores — Armazenando Carga
**Tempo: 2h**

#### O que memorizar
- **Capacitor**: armazena energia no campo elétrico entre duas placas. `Q = C × V`, `I = C × dV/dt`
- **Unidade**: Farad (F). Valores típicos: pF (pico), nF (nano), μF (micro), mF (mili)
- **Energia armazenada**: `E = ½CV²`
- **Em série**: `1/C_total = 1/C1 + 1/C2` (oposto aos resistores!)
- **Em paralelo**: `C_total = C1 + C2` (oposto aos resistores!)
- **Circuito RC**: constante de tempo τ = R × C. Carga: `V(t) = V_final × (1 - e^(-t/τ))`. Descarga: `V(t) = V_inicial × e^(-t/τ)`
- **Regra dos 5τ**: após 5 constantes de tempo, capacitor está 99.3% carregado/descarregado

#### Intuição
Capacitor = **balde de água**. A tensão é o nível da água. A corrente é o fluxo entrando/saindo. Um balde grande (C grande) precisa de mais água (carga) para atingir o mesmo nível (tensão). A "taxa de enchimento" diminui conforme o balde enche — o fluxo (corrente) é máximo no início e diminui exponencialmente. τ = R×C é o "tempo de enchimento" — define a velocidade do processo.

**`I = C × dV/dt`** é a equação mágica: corrente no capacitor é proporcional à TAXA DE VARIAÇÃO da tensão. Se a tensão é constante, I = 0 (capacitor = circuito aberto para DC!). Se a tensão muda instantaneamente, I → ∞ (por isso capacitores em fontes de alimentação "absorvem" picos).

#### Projeto: "Carga e Descarga RC"
1. **No Falstad**, monte: 12V → chave → R(1kΩ) → C(100μF) → GND
   - Calcule τ = R×C = 1k × 100μ = 0.1s = 100ms
   - Feche a chave → observe a carga (curva exponencial!)
   - Marque o tempo para chegar a ~63% de 12V (≈7.56V) → deve ser ~100ms = τ
2. **Agora descarregue**: troque a fonte por um fio (curto-circuito)
   - Observe: mesma curva exponencial, mas caindo
   - Após 5τ = 500ms, V ≈ 0V
3. **Mude os valores**: R=10kΩ, C=10μF → mesmo τ (100ms), mesma curva!
4. **No LTspice**, faça análise transiente (`.tran 1s`):
   ```spice
   V1 Vin 0 PULSE(0 12 10m 1n 1n 500m 1)
   R1 Vin Vout 1k
   C1 Vout 0 100u
   .tran 1s
   ```
5. **Aplicação real**: circuito anti-bounce para botão → R=10k, C=100nF → τ=1ms
6. **Prompt IA**: *"Explique por que a corrente de um capacitor é proporcional a dV/dt e não a V diretamente. Use uma analogia com um balde de água sendo enchido."*
7. **Entregável**: Simulações Falstad + LTspice + cálculos de τ + aplicação anti-bounce

#### Erros Comuns
- Esquecer que capacitor é circuito aberto em DC (corrente = 0 se tensão é constante)
- Não considerar o ESR (resistência série equivalente) em capacitores eletrolíticos — afeta ripple em fontes
- Confundir tensão máxima do capacitor com tensão de operação — sempre use capacitor com rating ≥ 2× V_operação

---

### Módulo 1.11: Indutores — Armazenando Campo Magnético
**Tempo: 2h**

#### O que memorizar
- **Indutor**: armazena energia no campo magnético em uma bobina. `V = L × dI/dt`
- **Unidade**: Henry (H). Valores típicos: μH (micro), mH (mili)
- **Energia armazenada**: `E = ½LI²`
- **Circuito RL**: τ = L/R. Corrente cresce: `I(t) = I_final × (1 - e^(-t/τ))`. Decresce: `I(t) = I_inicial × e^(-t/τ)`
- **Comportamento**: indutor resiste a MUDANÇAS de corrente (oposto do capacitor que resiste a mudanças de tensão)
- **Perigo**: desligar um indutor com corrente → dI/dt enorme → V = L×dI/dt → SPIKE de tensão altíssimo → pode destruir componentes!

#### Intuição
Indutor = **roda d'água pesada** (volante de inércia). Uma vez girando (corrente fluindo), resiste a parar. Para iniciar (aumentar corrente), precisa de "esforço" (tensão). Quanto maior a roda (L grande), mais esforço e tempo para mudar a velocidade.

**O spike de tensão é o conceito mais importante**: quando você desliga uma corrente num indutor, é como parar uma roda d'água bruscamente — a energia armazenada tem que ir para algum lugar. Ela se manifesta como um pico de tensão que pode ser centenas de volts! É por isso que bobinas de relés sempre têm um diodo de proteção (flyback diode) em paralelo.

#### Projeto: "O Indutor e seus Perigos"
1. **No Falstad**, monte: 12V → chave → R(100Ω) → L(10mH) → GND
   - τ = L/R = 10m/100 = 0.1ms = 100μs
   - Feche a chave → observe a corrente subir exponencialmente
2. **O spike**: agora ABRA a chave com corrente fluindo
   - Observe o pico de tensão! (pode ser centenas de volts)
3. **Proteção com diodo flyback**:
   - Adicione um diodo em antiparalelo com o indutor
   - Abra a chave novamente → o spike é absorvido pelo diodo!
4. **No LTspice**: simule o circuito RL com `.tran` — observe o transiente com precisão
5. **Aplicação real**: por que todo relé, solenóide e motor tem um diodo flyback?
6. **Prompt IA**: *"Um relé de 12V é desligado bruscamente. O indutor tem L=50mH e a corrente era I=100mA. Calcule a energia armazenada (½LI²) e explique por que essa energia pode gerar centenas de volts."*
7. **Entregável**: Simulações + medição do spike + demonstração do diodo flyback

#### Erros Comuns
- Esquecer o diodo flyback em circuitos com indutores — o spike DESTRUIRÁ o transistor que controla a bobina
- Confundir V = L×dI/dt com V = L×I (indutor NÃO é resistor!)

---

### Módulo 1.12: Circuito RLC — O Sistema Completo
**Tempo: 2.5h**

#### O que memorizar
- **RLC série**: R + L + C em série com uma fonte. Equação: `L × d²i/dt² + R × di/dt + i/C = V_fonte`
- **Frequência natural**: `ω₀ = 1/√(LC)`, frequência em Hz: `f₀ = 1/(2π√(LC))`
- **Fator de amortecimento**: `α = R/(2L)`. Define o tipo de resposta:
  - **Subamortecido** (α < ω₀): oscilação com amplitude decrescente. O mais comum
  - **Criticamente amortecido** (α = ω₀): volta ao equilíbrio mais rápido sem oscilar
  - **Superamortecido** (α > ω₀): lento, sem oscilação. Pouco usado
- **Fator de qualidade Q**: `Q = ω₀L/R = 1/(R√(C/L))`. Q alto = oscilação forte, Q baixo = amortecimento rápido
- **Ressonância**: quando frequência de excitação = f₀, impedância é mínima (série) ou máxima (paralelo)

#### Intuição
O circuito RLC é como um **balanço**:
- **L** = massa (inércia — resiste a mudanças)
- **C** = mola (restaura à posição original — armazena e devolve energia)
- **R** = atrito (dissipa energia — amortece o movimento)

Se R é pequeno (pouco atrito), o balanço oscila muitas vezes (subamortecido, Q alto). Se R é grande (muito atrito), para rápido (superamortecido). Criticamente amortecido é o ponto ótimo — volta ao repouso o mais rápido possível sem passar do ponto (usado em sistemas de suspensão de carro, por exemplo).

#### Projeto: "RLC Explorer"
1. **No Falstad**, monte RLC série: 12V(step) → R(100Ω) → L(10mH) → C(1μF) → GND
   - Calcule: f₀ = 1/(2π√(10m × 1μ)) ≈ 1592 Hz, α = 100/(2×0.01) = 5000, ω₀ = 2π×1592 ≈ 10000
   - α < ω₀? Se sim → subamortecido (vai oscilar!)
2. **Observe a resposta** quando aplica um step de tensão → oscilação amortecida
3. **Varie R**: 10Ω (alta oscilação), 200Ω (amortecido), 632Ω (R = 2√(L/C) = criticamente amortecido)
4. **No LTspice**, plote a resposta para os 3 casos — compare as curvas
5. **Visualize na frequência**: aplique uma fonte AC com frequência variável → observe a ressonância
6. **Prompt IA**: *"Explique a analogia entre um circuito RLC e um sistema massa-mola-amortecedor. Mapeie cada componente elétrico ao equivalente mecânico e explique qualitativamente o que acontece quando o amortecimento é baixo vs alto."*
7. **Entregável**: 3 curvas (sub, crítico, super) + cálculos + análise de ressonância

---

### Módulo 1.13: Aplicações de Transitórios — Timer 555 e PWM
**Tempo: 2h**

#### O que memorizar
- **Timer 555**: CI analógico clássico que usa carga/descarga de capacitor para gerar pulsos
- **Modo astável**: gera onda quadrada contínua. `f = 1.44 / ((R1 + 2×R2) × C)`, duty cycle controlado por R1 e R2
- **Modo monoestável**: gera um único pulso de duração `T = 1.1 × R × C`
- **PWM**: modulação por largura de pulso — controla potência "média" variando duty cycle
- **Aplicações**: controle de brilho de LED, velocidade de motor DC, fontes chaveadas

#### Intuição
O 555 é a **aplicação perfeita** de tudo que você aprendeu: capacitor carrega através de resistor (τ = RC), e comparadores internos "decidem" quando trocar de estado. Entender o 555 é entender transitórios RC em ação. PWM é o truque mais elegante da eletrônica: se você ligar e desligar um LED 10.000 vezes por segundo com 50% duty, ele parece estar a 50% de brilho. Não precisa desperdiçar energia num resistor!

#### Projeto: "555 Oscillator & PWM Controller"
1. **No Falstad**: monte um 555 astável:
   - R1 = 1kΩ, R2 = 10kΩ, C = 10μF
   - Calcule: f = 1.44 / ((1k + 20k) × 10μ) ≈ 6.86 Hz
   - Observe a onda quadrada! Observe a curva de carga/descarga do capacitor
2. **Varie R2 e C** para obter diferentes frequências (1Hz, 10Hz, 1kHz)
3. **No LTspice**: monte o mesmo circuito com modelo SPICE real do NE555
4. **Controle de brilho PWM**: conecte o 555 a um LED via transistor
   - Varie o duty cycle mudando R1 e R2
   - Observe: brilho muda sem desperdiçar energia!
5. **Entregável**: Circuito 555 simulado + cálculos de frequência + demonstração PWM

#### Checkpoint — Fim da Fase Transitórios
- [ ] Calcula τ de circuitos RC e RL sem hesitação
- [ ] Sabe que `I = C × dV/dt` e `V = L × dI/dt` — e o que significam fisicamente
- [ ] Entende oscilação amortecida em circuitos RLC
- [ ] Sabe dimensionar um 555 para uma frequência desejada
- [ ] Entende PWM e por que é mais eficiente que controle linear

---

## Fase 4 — Corrente Alternada: O Mundo Real

### Módulo 1.14: Introdução a AC — Senóides e Valores RMS
**Tempo: 2h**

#### O que memorizar
- **Corrente alternada**: varia senoidalmente no tempo. `v(t) = V_pico × sen(2π × f × t + φ)`
- **Parâmetros**: V_pico (amplitude máxima), f (frequência em Hz), ω = 2πf (frequência angular), T = 1/f (período), φ (fase)
- **Rede elétrica brasileira**: 127V ou 220V **RMS**, 60Hz
- **Valor RMS**: `V_rms = V_pico / √2 ≈ 0.707 × V_pico`. É o valor que produz a mesma potência que DC equivalente
- **Valor pico-a-pico**: `V_pp = 2 × V_pico`
- **Exemplo**: tomada 127V RMS → V_pico = 127 × √2 ≈ 179.6V → V_pp ≈ 359V!
- **Por que AC?**: pode ser transformada (subir/descer tensão facilmente), transmite energia a longas distâncias com menos perda

#### Intuição
A rede elétrica da sua casa NÃO é 127V constante — é uma onda senoidal que oscila entre +179.6V e -179.6V, 60 vezes por segundo. Os "127V" que aparece é o valor **RMS** — equivalente a um DC de 127V em termos de potência. Quando você mede com multímetro DC numa tomada, marca zero (porque a média é zero). Quando mede AC, marca 127V (o multímetro calcula RMS).

**Por que senoidal?** Porque geradores rotativos (turbinas) naturalmente produzem senóides (uma bobina girando num campo magnético). E matematicamente, qualquer sinal pode ser decomposto em senóides (Fourier) — a senóide é o "átomo" dos sinais AC.

#### Projeto: "Explorando AC"
1. **No Falstad**, monte: fonte AC (120V pico, 60Hz) → R(1kΩ) → scope
   - Observe a senóide. Identifique: V_pico, V_pp, T, V_rms
2. **Adicione uma segunda fonte** com mesma frequência mas fase diferente (90°)
   - Observe ambas no scope — veja a defasagem
3. **Experimento**: conecte um LED direto na AC (com resistor limitador)
   - Observe: o LED pisca! (60Hz, mas olho humano não percebe)
   - Mas só numa semionda (LED é diodo — conduz em um sentido só)
4. **No LTspice**: fonte AC → resistor → `.tran 50m` → plote V(t) e I(t)
5. **Calcule**: se V_rms = 127V, qual a potência num chuveiro de 5500W? I = P/V = 5500/127 ≈ 43.3A (é MUITA corrente!)
6. **Entregável**: Simulações + identificação de parâmetros + cálculo de potência do chuveiro

---

### Módulo 1.15: Impedância — Resistência no Mundo AC
**Tempo: 2.5h**

#### O que memorizar
- **Impedância (Z)**: generalização da resistência para AC. Unidade: Ohm (Ω). É um número **complexo**: Z = R + jX
- **Reatância capacitiva**: `X_C = 1/(ωC) = 1/(2πfC)`. Diminui com frequência (capacitor "conduz melhor" em alta frequência)
- **Reatância indutiva**: `X_L = ωL = 2πfL`. Aumenta com frequência (indutor "bloqueia" alta frequência)
- **Impedância do resistor**: Z_R = R (puramente real, sem defasagem)
- **Impedância do capacitor**: Z_C = 1/(jωC) = -jX_C (atrasa corrente em 90° relativo à tensão: "ICE" - I leads V in C)
- **Impedância do indutor**: Z_L = jωL = jX_L (adianta tensão em 90° relativo à corrente: "ELI" - V leads I in L)
- **Lei de Ohm em AC**: `V = Z × I` (com V, Z, I como números complexos)
- **Mnemônico**: **ELI the ICE man** — no indutor (L), tensão (E) lidera corrente (I). No capacitor (C), corrente (I) lidera tensão (E)

#### Intuição
Em DC, resistores são os únicos que limitam corrente. Em AC, capacitores e indutores TAMBÉM limitam corrente — mas de uma forma diferente: eles causam **defasagem** entre tensão e corrente. Um capacitor de 10μF numa frequência de 60Hz tem reatância X_C = 265Ω — se comporta como um "resistor" de 265Ω, mas que adianta a corrente em 90°.

Números complexos parecem assustadores, mas são apenas uma ferramenta para rastrear **duas coisas ao mesmo tempo**: magnitude e fase. A parte real (R) é a resistência "normal", a parte imaginária (X) é a reatância que causa defasagem.

#### Projeto: "Impedância na Prática"
1. **Calcule a reatância** de C = 10μF em:
   - f = 60Hz → X_C = 1/(2π×60×10μ) = 265.3Ω
   - f = 1kHz → X_C = 15.9Ω
   - f = 1MHz → X_C = 0.016Ω (praticamente curto!)
2. **No Falstad**, monte: AC(10V peak, variável) → C(10μF) → R(100Ω) → GND
   - Varie f: 10Hz, 100Hz, 1kHz, 10kHz
   - Observe: a corrente AUMENTA com frequência (porque X_C diminui)
   - Observe a defasagem entre V e I no scope
3. **Repita com indutor**: AC → L(10mH) → R(100Ω)
   - Varie f: comportamento oposto (corrente diminui com frequência)
4. **Circuito RLC série em AC**: calcule Z_total = R + jωL + 1/(jωC) para f = 1kHz
   - R=100Ω, L=10mH, C=1μF → Z = 100 + j62.8 - j159.2 = 100 - j96.4Ω
   - |Z| = √(100² + 96.4²) = 138.8Ω
   - I = V/|Z| → simule e compare!
5. **Entregável**: Cálculos de impedância + simulações + gráfico de |Z| vs frequência

---

### Módulo 1.16: Fasores e Análise de Circuitos AC
**Tempo: 2.5h**

#### O que memorizar
- **Fasor**: representação de uma senóide como número complexo. `v(t) = V_m × cos(ωt + φ)` → fasor `V = V_m ∠ φ`
- **Operações com fasores**: soma, subtração, multiplicação (amplificação), divisão — tudo com álgebra complexa
- **Diagrama fasorial**: representação gráfica — vetores girando. V e I aparecendo com seus ângulos relativos
- **Análise de circuitos AC**: TODOS os métodos DC (KVL, KCL, Thévenin, nodal, malhas) funcionam com fasores! Só substituir R por Z
- **Conversão retangular ↔ polar**: `a + jb = √(a²+b²) ∠ arctan(b/a)`

#### Intuição
Fasores são o truque genial que transformou AC complexo em cálculos simples. Em vez de trabalhar com senos e cossenos (equações diferenciais), você converte para fasores (números complexos / álgebra) → resolve com os MESMOS MÉTODOS que já aprendeu para DC → converte de volta. É como traduzir um problema de japonês para português, resolver, e traduzir de volta.

#### Projeto: "Circuito AC com Fasores"
1. **Resolva o circuito RLC série** usando fasores:
   - V_fonte = 120∠0° V (rms), f = 60Hz
   - R = 10Ω, L = 50mH, C = 100μF
   - Z_R = 10, Z_L = j18.85, Z_C = -j26.53
   - Z_total = 10 + j18.85 - j26.53 = 10 - j7.68Ω = 12.6∠-37.5°Ω
   - I = V/Z = 120∠0° / 12.6∠-37.5° = 9.52∠37.5° A
   - V_R = I × R, V_L = I × jωL, V_C = I × 1/(jωC) → verifique KVL: V_R + V_L + V_C = V_fonte
2. **Desenhe o diagrama fasorial** (pode ser no papel ou Python)
3. **Simule no LTspice** com análise AC (`.ac dec 100 1 100k`)
4. **Entregável**: Resolução fasorial completa + diagrama + comparação LTspice

---

### Módulo 1.17: Potência em AC — Ativa, Reativa e Aparente
**Tempo: 2h**

#### O que memorizar
- **Potência Ativa (P)**: potência real, faz trabalho útil (calor, luz, movimento). Unidade: Watt (W). `P = V×I×cos(φ)`
- **Potência Reativa (Q)**: energia vai e volta entre fonte e componente reativo (L ou C). Unidade: VAr. `Q = V×I×sen(φ)`
- **Potência Aparente (S)**: "potência total" que a rede "enxerga". Unidade: VA. `S = V×I = √(P² + Q²)`
- **Fator de Potência (FP)**: `cos(φ) = P/S`. FP=1 ideal, FP<1 desperdiça capacidade da rede
- **Triângulo de potências**: P (horizontal) + Q (vertical) = S (hipotenusa)
- **Correção de FP**: adicionar capacitor em paralelo com carga indutiva (motor) para compensar Q

#### Intuição
Imagine que você pede uma cerveja num bar. A espuma é a **potência reativa** — ocupa espaço no copo mas não mata sua sede. O líquido é a **potência ativa** — é o que você realmente quer. O copo inteiro (líquido + espuma) é a **potência aparente**. O fator de potência é a razão líquido/copo. A concessionária te cobra pelo copo inteiro (S), mas você só aproveita o líquido (P). Corrigir FP = eliminar a espuma = botar um capacitor.

**Na indústria**, FP < 0.92 resulta em MULTA na conta de luz. É por isso que fábricas com muitos motores (indutivos, FP~0.8) instalam bancos de capacitores.

#### Projeto: "Conta de Energia Industrial"
1. **Cenário**: fábrica com carga de 100kW, FP = 0.75 (indutivo), V = 220V
   - S = P/FP = 100k/0.75 = 133.3 kVA
   - Q = √(S²-P²) = √(133.3²-100²) = 88.2 kVAr (indutivo)
   - I = S/V = 133.3k/220 = 606A
2. **Corrija para FP = 0.95**:
   - Nova S = 100k/0.95 = 105.3 kVA
   - Novo Q = √(105.3²-100²) = 33 kVAr
   - Q_capacitor = 88.2 - 33 = 55.2 kVAr (capacitor necessário)
   - C = Q_c / (ω × V²) → calcule
3. **Monte no LTspice**: carga RL + capacitor de correção → meça FP antes e depois
4. **Entregável**: Cálculo completo de correção de FP + simulação + economia estimada

---

### Módulo 1.18: Filtros Passivos e Ressonância
**Tempo: 2.5h**

#### O que memorizar
- **Filtro passa-baixas (RC)**: `f_c = 1/(2πRC)`. Deixa passar frequências abaixo de f_c, atenua acima. -20dB/década
- **Filtro passa-altas (RC/CR)**: mesma f_c, comportamento oposto
- **Filtro passa-banda (RLC)**: deixa passar faixa em torno de f₀ = 1/(2π√(LC)). Largura de banda = f₀/Q
- **Ressonância série**: impedância mínima em f₀ → corrente máxima
- **Ressonância paralela**: impedância máxima em f₀ → corrente mínima
- **Decibéis**: `ganho_dB = 20 × log₁₀(V_out/V_in)`. -3dB = meia potência = 70.7% da tensão
- **Diagrama de Bode**: ganho (dB) e fase (°) vs frequência (escala log) — ESSENCIAL para entender filtros

#### Intuição
Filtros são os "peneiradores" de frequência. O rádio FM funciona com um filtro passa-banda sintonizado na estação (ex: 89.1 MHz — deixa essa frequência passar, bloqueia todas as outras). Sistemas de áudio usam filtros para separar graves (passa-baixas para subwoofer), médios (passa-banda), e agudos (passa-altas para tweeter). Na engenharia de potência, filtros eliminam harmônicos da rede.

#### Projeto: "Equalizador de 3 Bandas"
1. **Projete 3 filtros** com R e C:
   - Passa-baixas: f_c = 300Hz (graves)
   - Passa-banda: f_centro = 2kHz, Q = 5 (médios)
   - Passa-altas: f_c = 5kHz (agudos)
2. **No LTspice**, monte cada filtro e faça análise AC (`.ac dec 1000 1 100k`):
   - Plote |V_out/V_in| em dB vs frequência → diagrama de Bode!
3. **Monte o equalizador completo**: entrada → 3 filtros em paralelo
4. **Ressonância**: monte RLC série com f₀ = 1kHz, varie Q mudando R
   - Observe como Q alto = pico estreito e afiado
5. **Prompt IA**: *"Explique intuitivamente por que um capacitor 'bloqueia DC' e 'passa AC', usando a equação X_C = 1/(2πfC)."*
6. **Entregável**: 3 filtros + Bode plots + equalizador simulado

#### Checkpoint Final — Pilar 1 Completo!
Parabéns! Ao completar todos os 18 módulos, você deve conseguir:
- [ ] Analisar qualquer circuito DC com múltiplas fontes e componentes
- [ ] Calcular transitórios RC/RL/RLC e dimensionar timers
- [ ] Trabalhar com fasores e impedância em circuitos AC
- [ ] Calcular e corrigir fator de potência
- [ ] Projetar filtros passivos e entender diagramas de Bode
- [ ] Usar Falstad para intuição e LTspice para análise precisa
- [ ] Explicar cada conceito com analogias próprias

---

## Se Você Travar

| Problema | Solução |
|----------|--------|
| Kirchhoff não fecha | Verifique convenção de sinais — ESCOLHA um sentido para percorrer a malha e não mude. Se encontrar corrente negativa, está TUDO BEM (significa sentido oposto ao assumido) |
| Thévenin dá valores estranhos | Confirme que desligou TODAS as fontes independentes (mas NÃO as dependentes!) ao calcular R_th |
| Simulação diverge do cálculo | Verifique prefixos SI (1k vs 1M). Depois verifique se a simulação está em regime permanente (espere 5τ) |
| Resposta RC/RL não bate | Verifique se usou τ=RC (não τ=R/C). Para RL é τ=L/R (não τ=LR). Dica: τ tem unidade de SEGUNDOS |
| Fasores confusos | Sempre converta para polar (magnitude∠fase) antes de multiplicar/dividir. Use retangular (a+jb) para somar/subtrair |
| Bode plot incompreensível | Comece com o traçado assintótico (retas). -20dB/dec por polo, +20dB/dec por zero. O gráfico real é a versão "suavizada" |
| Confusão V_rms vs V_pico | V_rms = V_pico/√2. Na hora de calcular potência, SEMPRE use V_rms. Multímetros comuns medem RMS |
| Não sei que método usar | Poucos nós → nodal. Poucas malhas → malhas. 2 terminais de interesse → Thévenin. Múltiplas fontes → superposição. Circuito simples → série-paralelo |

> **Próximo passo**: [Pilar 2 — Eletrônica Analógica e de Potência](ee_eletronica_roadmap.md) — agora que você domina circuitos passivos, é hora de adicionar semicondutores e criar circuitos que amplificam, retificam e convertem energia!
