# HH A.3-A.5 - Protocolos em FPGA e Glitcher

UART TX em FPGA: estrutura?
?
FSM: idle → start bit (LOW) → 8 data bits (shift register, LSB first) → stop bit (HIGH). Divisor de clock para baud rate.

SPI Master em FPGA: estrutura?
?
FSM: idle → assert CS (LOW) → clock out MOSI + clock in MISO → deassert CS. Clock gerado pelo master.

Voltage glitching: parametros?
?
Offset (delay apos trigger, em ciclos de clock), width (duracao do pulso, tipicamente 1-100ns), amplitude. Descobertos por sweep sistematico.

Circuito crowbar para glitching?
?
MOSFET N-channel que curto-circuita VCC para GND brevemente. Gate controlado pela FPGA. Precisa de resistor de gate e diodo de protecao.

Por que FPGA e necessaria para glitching?
?
Precisao de nanossegundos. Arduino = ~62.5ns por instrucao, FPGA reage em 1 ciclo (10ns @100MHz). Software nao tem resolucao temporal suficiente.

Sweep de parametros de glitch?
?
Varrer offset e width automaticamente (milhares de combinacoes). Script Python controla FPGA via serial, loga cada tentativa (offset, width, resultado).
