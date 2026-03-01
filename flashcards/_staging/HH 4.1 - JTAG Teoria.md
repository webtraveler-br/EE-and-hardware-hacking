# HH 4.1 - JTAG Teoria

Sinais JTAG (IEEE 1149.1)?
?
TDI (data in), TDO (data out), TCK (clock), TMS (mode select), TRST (reset, opcional) + VCC + GND.

O que e a TAP State Machine?
?
Maquina de estados que controla JTAG. Estados: Test-Logic-Reset → Run-Test-Idle → Shift-DR/IR → Update-DR/IR. TMS controla as transicoes.

O que e Boundary Scan?
?
Funcionalidade JTAG que permite "ver" e controlar cada pino do chip individualmente. Criado para teste de manufatura, util para HH.

O que e o IDCODE Register?
?
Registrador que identifica o chip via JTAG: fabricante, part number, versao. Usado pelo JTAGulator para confirmar que encontrou JTAG.

Ferramentas para JTAG?
?
OpenOCD (software, universal), JTAGulator (identifica pinos automaticamente), Bus Pirate, J-Link (Segger), ST-Link (STM32).

SWD vs JTAG?
?
SWD e a versao ARM simplificada: apenas 2 fios (SWDIO + SWCLK) vs 4-5 do JTAG. Mesma funcionalidade, menos pinos, exclusivo ARM.

Como encontrar pinos JTAG num PCB?
?
Procurar headers nao populados (10/20 pinos), test points. Usar JTAGulator para bruteforce de combinacoes ate obter IDCODE valido.
