# HH 4.2 - Exploracao JTAG e SWD

Memory dump via OpenOCD: comandos?
?
`mdw 0x08000000 100` (display word), `mdb 0x20000000 256` (display byte). Permite ler toda a memoria incluindo RAM com dados em runtime.

Flash read/write via OpenOCD?
?
`flash read_image dump.bin 0x08000000 0x10000` (ler), `flash write_image firmware.bin 0x08000000` (escrever).

CPU control via JTAG?
?
`halt` (pausar CPU), `resume` (continuar), `step` (uma instrucao), `reg` (ler registradores), `bp 0x08001234 4 hw` (breakpoint).

GDB via OpenOCD?
?
OpenOCD expoe GDB server na porta 3333. Conectar: `gdb-multiarch firmware.elf` → `target remote :1234`. Permite debug completo do firmware.

O que se pode fazer com JTAG conectado?
?
Ler/escrever TODA a memoria (flash + RAM), pausar CPU em qualquer instrucao, inserir breakpoints, modificar registradores, extrair firmware, inserir backdoors.
