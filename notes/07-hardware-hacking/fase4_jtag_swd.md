# Módulo 7 — Hardware Hacking: Fase 4 — Debug Interfaces: JTAG e SWD
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo HH.4.1: JTAG — Teoria e Identificação

### Sinais JTAG (IEEE 1149.1)

- TCK <> {{Test Clock}} (clock do JTAG)
- TMS <> {{Test Mode Select}} (controla TAP state machine)
- TDI <> {{Test Data In}} (dados entrando no chip)
- TDO <> {{Test Data Out}} (dados saindo do chip)
- TRST <> {{Test Reset}} (opcional — reseta TAP state machine)
- Mínimo de sinais >> {{4}} (TCK, TMS, TDI, TDO) + GND + VCC

### TAP State Machine

- TAP <> {{Test Access Port}} (controlador JTAG dentro do chip)
- Estado inicial >> {{Test-Logic-Reset}} (TMS=1 por 5 clocks → sempre reseta)
- Shift-DR >> Envia/recebe dados do {{Data Register}} (lê IDCODE, boundary scan)
- Shift-IR >> Seleciona {{instrução}} a executar (BYPASS, IDCODE, EXTEST)

### Registradores JTAG

- IDCODE >> Identificação do chip: {{fabricante}}, part number, versão
- BYPASS >> Registrador de {{1 bit}} — encurta cadeia em daisy-chain (pula chip)
- Boundary Scan >> Permite ler/controlar {{pinos individuais}} do chip (teste de manufatura)

### SWD (Serial Wire Debug)

- SWD >> Versão {{ARM}} simplificada do JTAG — apenas {{2 fios}}
- SWDIO <> {{Serial Wire Data I/O}} (bidirecional)
- SWCLK <> {{Serial Wire Clock}}
- Vantagem >> Menos pinos (2 vs 4-5), mesma {{funcionalidade}} de debug
- Usado em >> {{STM32}}, nRF52, ESP32-S3, todos ARM Cortex-M

### Identificação de pinos JTAG

- JTAGulator >> Dispositivo que testa todas {{combinações}} de pinos automaticamente
- Header 10/20 pinos >> Provavelmente {{JTAG}} (padrão ARM)
- Header 2×5 (10-pin) ARM >> Pinout padrão: {{TCK, TMS, TDI, TDO, TRST, GND, VCC}}
- OpenOCD >> Software que fala {{JTAG/SWD}} via adaptadores (J-Link, ST-Link, FTDI)

### Drill — JTAG Teoria #[[Drill]]

- Quantos fios mínimos para JTAG? >> {{4}} sinais + GND (TCK, TMS, TDI, TDO)
- Quantos fios para SWD? >> {{2}} sinais + GND (SWDIO, SWCLK)
- TMS em HIGH por 5 clocks → estado = >> {{Test-Logic-Reset}} (reset garantido)
- IDCODE do chip serve para >> {{Identificar}} fabricante e modelo via JTAG
- 6 test points, precisa achar JTAG → ferramenta = >> {{JTAGulator}} (bruteforce de pinos)
- STM32 Cortex-M → interface de debug preferida = >> {{SWD}} (2 fios, mais simples)

---

## Módulo HH.4.2: Exploração via JTAG/SWD

### OpenOCD — Comandos essenciais

- Iniciar sessão >> `openocd -f interface/{{adaptador}}.cfg -f target/{{chip}}.cfg`
- `halt` >> {{Para}} a CPU (pausa execução)
- `resume` >> {{Retoma}} execução da CPU
- `step` >> Executa {{1 instrução}} e para
- `reg` >> Lista todos os {{registradores}} do processador
- `mdw 0x08000000 256` >> Memory display: lê {{256 words}} a partir do endereço
- `mdb 0x20000000 1024` >> Memory display: lê {{1024 bytes}} (RAM)
- Porta GDB >> OpenOCD expõe GDB server na porta {{3333}}

### Dump de memória via JTAG

- Flash read >> `flash read_image {{dump.bin}} 0x08000000 0x10000` (lê 64KB da flash)
- Flash write >> `flash write_image erase {{modified.bin}} 0x08000000` (escreve firmware modificado)
- RAM dump >> `dump_image {{ram.bin}} 0x20000000 0x5000` (lê SRAM — pode conter chaves em runtime!)

### Debug com GDB remoto

- Conectar >> `gdb-multiarch` → `target remote :{{3333}}`
- `break *0x08001234` >> Breakpoint no endereço {{0x08001234}}
- `x/s &password` >> Lê {{string}} na variável "password" direto da memória
- `x/20xw $sp` >> Lê {{20 words}} a partir do stack pointer (examinar stack)
- `info registers` >> Mostra {{todos os registradores}} (PC, SP, LR, R0-R12)
- `set {int}0x20000100 = 1` >> {{Escreve}} valor 1 no endereço de memória (modificar em runtime!)

### O que extrair via JTAG

- Flash completa >> {{Firmware}} inteiro para análise offline
- RAM em runtime >> Chaves de {{criptografia}} carregadas na memória
- Registradores >> Estado da CPU, {{program counter}} (onde está executando)
- Stack >> Variáveis locais, endereços de {{retorno}} (útil para exploits)

JTAG = debugger GDB conectado direto no hardware. Pode pausar, ler, escrever qualquer coisa. É o nível mais alto de acesso possível.

### Drill — Exploração JTAG #[[Drill]]

- Quer ler firmware do STM32 via SWD → software = >> {{OpenOCD}} + GDB
- OpenOCD rodando, quer pausar a CPU → comando = >> {{`halt`}}
- Quer ler senha que está na RAM em runtime → comando GDB = >> {{`x/s &password`}}
- Quer fazer dump de 64KB de flash a partir de 0x08000000 → >> {{`flash read_image dump.bin 0x08000000 0x10000`}}
- GDB conectado, `info registers` mostra `pc = 0x08001234` → CPU está executando = >> Instrução no endereço {{0x08001234}}
- Quer modificar variável em runtime sem recompilar → GDB >> {{`set {int}0x20000100 = valor`}}

---

## Módulo HH.4.3: Proteções de Debug e Bypass

### Proteções por fabricante

- STM32 RDP Level 0 >> {{Sem proteção}} — JTAG/SWD totalmente acessível
- STM32 RDP Level 1 >> JTAG/SWD {{desabilitado}}. Flash protegida. Reversível (apaga flash)
- STM32 RDP Level 2 >> Proteção {{permanente}} (irreversível, fuses queimados)
- NXP LPC CRP1 >> ISP parcialmente {{restrito}} (leitura bloqueada, mas erase permitido)
- NXP LPC CRP2 >> ISP mais restrito, sem {{leitura}} de flash
- NXP LPC CRP3 >> ISP {{completamente}} desabilitado (mais restritivo, mas bypass conhecido!)
- ESP32 Secure Boot >> Verifica {{assinatura}} do firmware antes de executar
- ESP32 Flash Encryption >> Firmware na flash é {{criptografado}} (AES-256)

### Técnicas de bypass

- Voltage glitching >> Pulso de tensão no momento do {{check}} de proteção → bit lido errado → bypass
- RDP Level 1 bypass >> Glitch durante boot pode fazer chip {{ignorar}} RDP flag
- Cold boot >> Resfriar chip para reter dados na {{SRAM}} mesmo sem alimentação
- UART bootloader exploit >> Alguns chips têm vulns no bootloader {{ROM}} (não atualizável)
- Dessoldar e regravar >> Ler flash {{externamente}}, modificar, regravar (se não criptografado)
- CRP3 bypass (NXP) >> Falha permite escrever código que lê a flash {{de dentro}} do chip

### OTP Fuses

- OTP <> {{One-Time Programmable}} — bits que só podem ser escritos {{uma vez}}
- JTAG disable fuse >> Queima permanente que desabilita {{debug}} (irreversível)
- Secure boot fuse >> Grava chave pública, firmware não assinado {{não executa}}

### Drill — Proteções #[[Drill]]

- STM32 com RDP=1 → JTAG funciona? >> {{Não}} — desabilitado (mas reversível apagando flash)
- STM32 com RDP=2 → reversível? >> {{Não}} — permanente (fuses queimados)
- Quer bypassar RDP1 sem apagar flash → técnica = >> {{Voltage glitching}} durante boot
- ESP32 com Secure Boot + Flash Encryption → dump SPI útil? >> {{Não}} — dados criptografados
- ESP32 sem Secure Boot, sem Flash Encryption → dump SPI útil? >> {{Sim}} — firmware legível
- NXP LPC com CRP3 → esperado ser seguro, mas → >> Bypass {{conhecido}} permite extração de código
- Fuse de JTAG queimado = proteção >> {{Permanente}} (OTP, não reversível)
