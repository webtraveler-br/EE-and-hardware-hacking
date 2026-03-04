# Módulo 3 — Digital: Fase 1 — Lógica Combinacional
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 3.1: Sistemas Numéricos e Portas Lógicas

### Sistemas numéricos

- 1 byte <> {{8 bits}} (0-255)
- 1 nibble <> {{4 bits}} (0-15)
- Hexadecimal >> Base {{16}} (cada hex digit = 4 bits)
- 0xFF <> {{1111 1111}} <> {{255}}
- 0x0A <> {{0000 1010}} <> {{10}}
- 0x0F <> {{0000 1111}} <> {{15}}

### Portas lógicas

- AND >> Saída 1 só se {{TODAS}} as entradas = 1. Símbolo: · ou ∧
- OR >> Saída 1 se {{QUALQUER}} entrada = 1. Símbolo: + ou ∨
- NOT >> {{Inverte}}. Símbolo: ¬ ou barra
- NAND >> AND {{invertido}}. Porta {{universal}} (qualquer circuito pode ser feito só com NANDs)
- NOR >> OR invertido. Também {{universal}}
- XOR >> Saída 1 se entradas são {{diferentes}}. Símbolo: ⊕

### Portas com MOSFETs

- 2 NMOS em série = >> {{AND}} (ambos ON para conduzir)
- 2 NMOS em paralelo = >> {{OR}} (qualquer ON conduz)
- 1 NMOS com pull-up = >> {{NOT}} (inverte)

Em eletrônica digital: 0V = LOW = 0, e 5V (ou 3.3V) = HIGH = 1.

### Drill — Conversões #[[Drill]]

- `1100` em decimal = >> {{12}} (8+4)
- `1010 0101` em hex = >> {{0xA5}}
- 0x2F em binário = >> {{0010 1111}}
- 0x2F em decimal = >> {{47}} (2×16+15)
- 200 em hex = >> {{0xC8}}
- 0b1111 1111 em decimal = >> {{255}}
- AND: 1·0 = >> {{0}}
- OR: 0+1 = >> {{1}}
- XOR: 1⊕1 = >> {{0}}
- NAND: (1·1)' = >> {{0}}

---

## Módulo 3.2: Álgebra Booleana

### Identidades

- $A · 1$ = >> {{A}} | $A + 0$ = >> {{A}}
- $A · 0$ = >> {{0}} | $A + 1$ = >> {{1}}
- $A · A'$ = >> {{0}} | $A + A'$ = >> {{1}}
- Absorção: $A + A·B$ = >> {{A}}

### Teoremas fundamentais

- De Morgan (AND): $(A · B)'$ = >> {{$A' + B'$}}
- De Morgan (OR): $(A + B)'$ = >> {{$A' · B'$}}
- Distributiva (não-intuitiva!): $A + (B · C)$ = >> {{$(A+B) · (A+C)$}}
- SOP (Soma de Produtos) >> Forma {{AND-OR}}. Cada linha da tabela com saída=1 → 1 minterm
- POS (Produto de Somas) >> Forma {{OR-AND}}. Cada linha com saída=0 → 1 maxterm

De Morgan = tradutor entre AND e OR com inversões. Simplificar = menos portas = circuito mais barato.

### Drill — Booleana #[[Drill]]

- $(A · B · C)'$ por De Morgan = >> {{$A' + B' + C'$}}
- $(A + B)'$ por De Morgan = >> {{$A' · B'$}}
- $A · B + A · B'$ simplifica para >> {{A}} (fatorar A, B+B'=1)
- $A + A · B$ simplifica para >> {{A}} (absorção)

---

##  Módulo 3.3: Mapa de Karnaugh

### Método K-map

- K-map >> Método {{visual}} para simplificar expressões booleanas (2-4 variáveis)
- Regras de agrupamento >> Agrupar 1s adjacentes em retângulos de {{$2^n$}} (1, 2, 4, 8)
- Maior o grupo → >> {{Mais simples}} a expressão resultante
- Adjacência >> Inclui {{wrap-around}} (bordas se conectam)
- Don't-cares (X) >> Usar para fazer grupos {{maiores}} (podem ser 0 ou 1)
- 3 variáveis >> Mapa {{2×4}}
- 4 variáveis >> Mapa {{4×4}}
- Limitação >> Impraticável para {{5+}} variáveis (usar Quine-McCluskey)

### Drill — K-map #[[Drill]]

- Grupo de 1 célula no K-map → quantos literais? >> {{N}} (todos, sem simplificação)
- Grupo de 2 células → elimina >> {{1}} variável
- Grupo de 4 células → elimina >> {{2}} variáveis
- Grupo de 8 células (4 var) → elimina >> {{3}} variáveis

---

##  Módulo 3.4: Circuitos Combinacionais Práticos

### Blocos funcionais

- MUX (Multiplexador) >> Seleciona {{1 de N}} entradas. MUX 4:1 = 4 entradas, 2 seletores, 1 saída
- DEMUX >> {{Oposto}} do MUX: 1 entrada → N saídas
- Decoder >> Converte código binário em {{linha ativa}}. Decoder 3:8 ativa 1 de 8
- Encoder >> {{Oposto}} do decoder: linha ativa → código binário
- Full Adder >> Soma 2 bits + $C_{in}$ → {{Sum + $C_{out}$}}
- Ripple Carry Adder >> {{N Full Adders}} em cascata → somador de N bits

### Display e prática

- 7 segmentos >> Decoder {{BCD→7seg}} converte 4 bits em display numérico
- MUX 4:1 com seletores S1S0=10 → saída = >> Entrada {{$I_2$}} (seleciona pelo índice binário)

### Drill — Combinacionais #[[Drill]]

- Decoder 3:8: entrada 101 → qual linha ativa? >> {{Linha 5}}
- MUX 8:1: quantos seletores precisa? >> {{3}} ($2^3 = 8$)
- Full Adder: A=1, B=1, Cin=0 → Sum=?, Cout=? >> {{Sum=0, Cout=1}} (1+1=10)
- Full Adder: A=1, B=1, Cin=1 → Sum=?, Cout=? >> {{Sum=1, Cout=1}} (1+1+1=11)
- Somador 4-bit: 0101 + 0011 = >> {{1000}} (5+3=8)
