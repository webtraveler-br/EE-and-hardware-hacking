# Módulo 1 — Circuitos: Fase 1 — DC Básico
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 1.1 — Tensão, Corrente e Resistência

### Definições e Unidades

- Tensão (V) >> Diferença de {{potencial elétrico}} entre dois pontos
- Corrente (I) >> Taxa de fluxo de {{cargas elétricas}} (elétrons)
- Resistência (R) >> {{Oposição}} ao fluxo de corrente
- Volt [V] <> {{J/C}} (energia por carga)
- Ampère [A] <> {{C/s}} (carga por segundo)
- Ohm [Ω] <> {{V/A}}
- Watt [W] <> {{V·A}} = J/s
- Joule [J] <> {{W·s}} (energia no SI)
- kWh <> {{3.600.000 J}} (energia prática, relógio de luz)

### Lei de Ohm

- V = >> {{I × R}}
- I = >> {{V / R}}
- R = >> {{V / I}}

### Potência Elétrica

- P (base) = >> {{V × I}}
- P (usando R e I) = >> {{I² × R}}
- P (usando V e R) = >> {{V² / R}}
- Energia = >> {{P × t}}

### Conceitos Práticos

- Tensão é medida em >> {{paralelo}} com o componente (entre dois pontos)
- Corrente é medida em >> {{série}} com o componente (através dele)
- A corrente é "consumida" por um resistor? >> {{Não}} — a corrente que entra é igual à que sai
- O que o resistor realmente dissipa? >> {{Energia}} (como calor)
- Curto-circuito ideal >> Caminho com R = {{0}} → I tende a infinito
- Regra de dimensionamento térmico >> $P_{dissipada}$ deve ser {{menor}} que $P_{máxima}$ do componente

Analogia hidráulica: Tensão = pressão, Corrente = fluxo, Resistência = estreitamento do cano.

### Drill — Lei de Ohm #[[Drill]]

- V=12V, R=1kΩ → I = >> {{12 mA}}
- V=5V, R=330Ω → I ≈ >> {{15 mA}}
- I=20mA, R=220Ω → V = >> {{4.4 V}}
- V=3.3V, I=10mA → R = >> {{330 Ω}}
- V=5V, I=2A → R = >> {{2.5 Ω}}
- I=10mA, R=10kΩ → V = >> {{100 V}}

### Drill — Potência #[[Drill]]

- V=12V, I=100mA → P = >> {{1.2 W}}
- V=5V, R=100Ω → P = >> {{0.25 W}} (V²/R)
- I=2A, R=10Ω → P = >> {{40 W}} (I²R)
- R=10kΩ, I=10mA → P = >> {{1 W}}
- LED: 2V, 20mA → P = >> {{40 mW}}
- Resistor ¼W dissipando 1W → >> {{Queima}} (P > P_máx)

---

##  Módulo 1.1B — Código de Cores e Datasheets

### Tabela de Cores (dígitos 0-9)

- Preto <> {{0}}
- Marrom <> {{1}}
- Vermelho <> {{2}}
- Laranja <> {{3}}
- Amarelo <> {{4}}
- Verde <> {{5}}
- Azul <> {{6}}
- Violeta <> {{7}}
- Cinza <> {{8}}
- Branco <> {{9}}

Mnemônico: "Pretinha Maria Viajou Longe Através Verde Azul Violeta Céu Branco"

### Multiplicador e tolerância

- 3ª faixa do resistor de 4 faixas >> {{Multiplicador}} (nº de zeros)
- Ouro na 4ª faixa (tolerância) <> {{±5%}}
- Prata na 4ª faixa (tolerância) <> {{±10%}}

### Componentes reais

- Série E12 >> {{12 valores}} padronizados por década
- SMD <> {{Surface-Mount Device}} (montagem em superfície)
- THT <> {{Through-Hole Technology}} (montagem por furos)
- Absolute Maximum Rating no datasheet >> Condições que {{destroem}} o componente

Datasheets essenciais: 1N4148 (diodo de sinal), LM7805 (regulador 5V), 2N2222 (BJT NPN).

### Drill — Código de Cores #[[Drill]]

- Marrom, Preto, Vermelho → >> {{1kΩ}} (10 × 10²)
- Marrom, Preto, Laranja → >> {{10kΩ}} (10 × 10³)
- Vermelho, Vermelho, Vermelho → >> {{2.2kΩ}} (22 × 10²)
- Amarelo, Violeta, Marrom → >> {{470Ω}} (47 × 10¹)
- Amarelo, Violeta, Vermelho → >> {{4.7kΩ}} (47 × 10²)
- Marrom, Preto, Amarelo → >> {{100kΩ}} (10 × 10⁴)

---

##  Módulo 1.2 — Resistores em Série e Paralelo

### Regras fundamentais

- Componentes em série compartilham a mesma >> {{corrente}}
- Componentes em série dividem a >> {{tensão}} (proporcional a R)
- Componentes em paralelo compartilham a mesma >> {{tensão}}
- Componentes em paralelo dividem a >> {{corrente}} (inversamente proporcional a R)

### Fórmulas de associação

- R série = >> {{R₁ + R₂ + R₃ + ...}}
- R paralelo (geral) >> 1/R_eq = {{1/R₁ + 1/R₂ + ...}}
- R paralelo (2 resistores) >> R_eq = {{(R₁ × R₂) / (R₁ + R₂)}}
- N resistores iguais (R) em paralelo >> R_eq = {{R / N}}

### Divisor de tensão

- Fórmula do divisor de tensão (V sobre R₂) >> V_out = {{V_in × R₂ / (R₁ + R₂)}}
- R₁ = R₂ no divisor → V_out = >> {{V_in / 2}}
- Por que NÃO usar divisor resistivo como fonte de alimentação? >> V_out depende da {{carga}} (varia com I)

### Divisor de corrente

- Fórmula do divisor (I no ramo 1, 2 ramos) >> I₁ = I_total × {{R₂ / (R₁ + R₂)}}
- A corrente num ramo é proporcional ao resistor >> {{oposto}} (busca o caminho mais fácil)

### Drill — Resistores #[[Drill]]

- 1kΩ + 2kΩ série = >> {{3kΩ}}
- 10kΩ ∥ 10kΩ = >> {{5kΩ}}
- 300Ω ∥ 300Ω ∥ 300Ω = >> {{100Ω}}
- 3kΩ ∥ 6kΩ = >> {{2kΩ}}
- 1MΩ ∥ 1MΩ = >> {{500kΩ}}

### Drill — Divisores #[[Drill]]

- V_in=12V, R₁=R₂ → V_out = >> {{6V}}
- V_in=10V, R₁=1k, R₂=9k → V_out = >> {{9V}}
- V_in=5V, R₁=2k, R₂=3k → V_out = >> {{3V}}
- I_total=10mA, R₁=1k, R₂=9k → qual ramo leva mais corrente? >> {{R₁}} (menor R)
- I_total=100mA, 2 ramos iguais → cada um = >> {{50mA}}

---

## Módulo 1.3 — Leis de Kirchhoff (KVL e KCL)

### Leis

- KCL >> Σ correntes entrando = {{Σ correntes saindo}} (conservação da carga)
- KVL >> Soma das tensões em malha fechada = {{zero}} (conservação da energia)

### Topologia

- Nó >> Ponto de união de {{≥ 3}} terminais
- Malha >> Caminho {{fechado}} sem cruzar um nó mais de uma vez
- Ramo >> Caminho entre {{dois nós adjacentes}} contendo ≥ 1 elemento

### Convenções KVL

- Sentido da corrente escolhido errado → valor calculado sai >> {{negativo}} (mas o módulo está correto)
- Malha entra pelo terminal (−) de fonte de 12V → parcela KVL = >> {{+12V}}
- Malha acompanha I passando por R → queda KVL = >> {{−IR}}

KCL: cruzamento de canos — água não some nem brota. KVL: subir e descer montanha voltando ao mesmo ponto — ganhos = perdas.

### Drill — KCL #[[Drill]]

- Nó: entram 10mA e 5mA, sai 8mA por X. Corrente em Y = >> {{7mA saindo}}
- Nó: entram 15mA, 2 saídas iguais → cada uma = >> {{7.5mA}}

### Drill — KVL #[[Drill]]

- 12V em série com R₁ (queda 5V) e R₂ (queda V_x). V_x = >> {{7V}} (12 − 5 = 7)
- Malha: +9V − 3V − V_R = 0 → V_R = >> {{6V}}

---

## Módulo 1.4 — Fontes Reais e Potência

### Fontes ideais vs reais

- Fonte de tensão ideal >> V constante, R_interna = {{0}}
- Fonte de corrente ideal >> I constante, R_interna = {{∞}}
- Fonte real de tensão carregada >> V_carga = {{V_fonte − I × R_interna}}
- Se I é muito alta numa bateria → V nos terminais >> {{cai}} (queda I×R_interna)

### Eficiência e máxima transferência

- Eficiência (η) <> {{P_carga / P_total}}
- Máxima transferência de potência ocorre quando R_carga = >> {{R_interna}}
- Eficiência na máxima transferência = >> {{50%}} (metade vira calor na R_interna)
- Fontes de alimentação (PC, celular): buscamos >> {{eficiência}} (η > 85%)
- Antenas e adaptadores de áudio: buscamos >> {{máxima transferência}} (casamento de impedância)

Motor WiFi faz placa resetar = surto de I alto × R_interna da bateria → queda de tensão → brown-out no microcontrolador.

### Drill — Fontes Reais #[[Drill]]

- Li-Ion 4.2V, R_int=0.1Ω, I=2A → V_real = >> {{4.0V}} (4.2 − 0.2)
- Mesma bateria, I=0A (circuito aberto) → V = >> {{4.2V}}
- Bateria 12V, R_int=1Ω, R_carga=1Ω → P_carga = >> {{36W}} (I=6A, V_carga=6V)

---

## Módulo 1.5 — Superposição

### Princípio

- Superposição vale para circuitos >> {{lineares}} (R, L, C — não diodos/transistores)
- Resposta total = >> {{soma algébrica}} das respostas individuais de cada fonte
- Desligar fonte de tensão → substituir por >> {{curto-circuito}} (fio)
- Desligar fonte de corrente → substituir por >> {{circuito aberto}}

### Armadilhas

- Diodos e transistores: superposição funciona? >> {{Não}} — componentes não-lineares
- Somar potências P₁ + P₂? >> {{Não}} — P = I²R não é linear; some I ou V primeiro, depois calcule P

Superposição responde "quem manda mais?" num circuito com múltiplas fontes. Ruído como fonte superposta = princípio de blindagem em HW.

### Drill — Superposição #[[Drill]]

- Circuito: V₁=10V e V₂=5V, ambas com R=1kΩ em série, carga R_L=1kΩ. I por V₁ sozinha (V₂ curto)? >> Calcule com {{divisor/Ohm no circuito simplificado}}
