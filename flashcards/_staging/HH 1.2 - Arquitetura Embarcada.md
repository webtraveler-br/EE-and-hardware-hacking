# HH 1.2 - Arquitetura de Sistemas Embarcados

Componentes de um SoC?
?
CPU (ARM Cortex-A/M/R), ROM de boot, SRAM, controladores de perifericos (UART, SPI, I2C, GPIO), tudo num unico chip.

Boot sequence tipica?
?
ROM → Bootloader (U-Boot) → Kernel Linux → Userspace. Cada etapa pode ser atacada.

Tipos de memoria flash: NOR vs NAND?
?
NOR: XIP (execute-in-place), leitura aleatoria rapida, usada para bootloader. NAND: leitura sequencial, mais capacidade, mais barata, usada para filesystem.

O que e eMMC?
?
NAND flash + controlador integrado num unico pacote. Interface MMC. Mais comum em smartphones e dispositivos recentes.

Arquiteturas comuns em IoT?
?
ARM Cortex-M (MCU, bare-metal), ARM Cortex-A (Linux), MIPS (roteadores antigos), RISC-V (emergente, open-source).

O que e memory-mapped I/O?
?
Perifericos acessados como enderecos de memoria. Escrever num endereco especifico configura hardware. Ex: GPIO_BASE + 0x04 = registrador de direcao.

Mapa de memoria tipico de MCU?
?
Flash (codigo) em 0x08000000, SRAM em 0x20000000, perifericos em 0x40000000 (STM32). Cada arquitetura tem layout diferente.
