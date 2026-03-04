# Módulo 3 — Digital: Fase 2 — Lógica Sequencial
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 3.5: Flip-Flops — A Memória Digital

### Tipos de flip-flop

- Flip-flop D >> Captura D na {{borda de subida}} do clock → mantém em Q
- Flip-flop JK >> J=K=1 → {{toggle}}. J=1,K=0 → set. J=0,K=1 → reset
- Flip-flop SR >> S=R=1 é {{proibido}} (estado indefinido!)
- Latch vs Flip-flop >> Latch: sensível a {{nível}}. FF: sensível a {{borda}}

### Timing e registradores

- Setup time >> Dados devem estar estáveis {{antes}} da borda do clock
- Hold time >> Dados devem permanecer estáveis {{depois}} da borda do clock
- Registrador >> Array de {{flip-flops D}} → armazena uma palavra (4, 8, 16 bits)

Circuitos combinacionais = sem memória (saída depende só de entradas atuais). Flip-flops adicionam MEMÓRIA (saída depende do estado anterior). Sem isso, não existiriam computadores.

### Drill — Flip-Flops #[[Drill]]

- FF D: D=1, borda ↑ → Q = >> {{1}}
- FF D: D=0, borda ↑ → Q = >> {{0}}
- FF JK: J=1, K=1, borda ↑ → Q = >> {{Q̄}} (toggle, inverte)
- FF SR: S=1, R=0 → Q = >> {{1}} (set)
- FF SR: S=0, R=1 → Q = >> {{0}} (reset)
- FF SR: S=1, R=1 → >> {{Proibido}} (indeterminado)
- Registrador de 8 bits armazena quantos bits? >> {{8}} (1 FF por bit)

---

## Módulo 3.6: Contadores e Divisores de Frequência

### Tipos de contadores

- Contador assíncrono (ripple) >> Cada Q é clock do próximo FF. Simples mas tem {{propagation delay}}
- Contador síncrono >> Todos os FFs recebem o {{mesmo clock}}. Mais rápido, preferível
- Contador binário N bits >> Conta de 0 a {{$2^N - 1$}}
- Contador módulo-N >> Conta de 0 a {{N−1}} e reseta (ex: mod-10 = BCD)

### Divisor de frequência

- Cada flip-flop divide a frequência por >> {{2}}
- N flip-flops → divisor por >> {{$2^N$}}
- Clock 1MHz → 3 FFs → saída = >> {{125 kHz}} ($10^6 / 2^3$)

### Drill — Contadores #[[Drill]]

- Contador 4 bits: conta de 0 a >> {{15}} ($2^4 - 1$)
- Contador 8 bits: conta de 0 a >> {{255}}
- Módulo-10 (BCD): reseta quando atinge >> {{1010}} (10 em binário)
- Clock 16MHz, precisa de 1Hz → dividir por >> {{$16 × 10^6$}} (24 FFs ≈ 16.7M, ou use prescaler)
- 4 FFs em cascata → divisor de >> {{16}} ($2^4$)

---

##  Módulo 3.7: Registradores de Deslocamento

### Tipos

- SISO <> {{Serial In, Serial Out}} (dados entram e saem 1 bit por clock)
- SIPO <> {{Serial In, Parallel Out}} (entrada serial → saída paralela)
- PISO <> {{Parallel In, Serial Out}} (entrada paralela → saída serial)
- PIPO <> {{Parallel In, Parallel Out}} (registrador comum)

### Aplicações HH

- 74HC595 >> Shift register SIPO → expande {{outputs}} do Arduino (3 pinos → 8+ saídas)
- PISO >> Lê múltiplos {{botões/switches}} com poucos pinos
- LFSR (Linear Feedback Shift Register) >> Gera sequências {{pseudo-aleatórias}}

SIPO é exatamente como SPI funciona: dados seriais pelo MOSI, clock pelo SCK, e a saída aparece em paralelo no chip.

### Drill — Shift Registers #[[Drill]]

- 74HC595: quantos pinos do Arduino usa? >> {{3}} (data, clock, latch)
- 74HC595: quantas saídas tem? >> {{8}}
- 2× 74HC595 cascateados → total de saídas = >> {{16}}
- Shift register 8 bits: quantos clocks para carregar 1 byte? >> {{8}}

---

##  Módulo 3.8: Máquinas de Estado Finito (FSM)

### Conceitos

- FSM <> {{Finite State Machine}} (estados finitos + transições + saídas)
- Moore >> Saída depende apenas do {{estado}} (mais previsível)
- Mealy >> Saída depende do estado E da {{entrada atual}} (mais responsivo)
- Diagrama de estados >> Círculos ({{estados}}) + setas ({{transições}})

### Implementação

- FSM em hardware >> Lógica combinacional (transições) + {{flip-flops}} (estado)
- Processo de design >> Especificação → diagrama → tabela de transições → {{K-map}} → circuito

FSMs estão em TUDO: semáforo, vending machine, protocolo UART (idle→start→data→stop), todo firmware é uma FSM implícita.

### Drill — FSM #[[Drill]]

- Semáforo com 4 estados (VdVm, AmVm, VmVd, VmAm) → quantos FFs? >> {{2}} ($2^2 = 4$ estados)
- 8 estados possíveis → quantos FFs? >> {{3}} ($2^3 = 8$)
- Moore: saída muda quando? >> Na {{transição de estado}} (próxima borda de clock)
- Mealy: saída muda quando? >> {{Imediatamente}} com a mudança de entrada (assíncrono com clock)
- UART idle→start bit → 8 data bits → stop bit → idle. Quantos estados? >> {{11}} (idle + start + 8 data + stop)
