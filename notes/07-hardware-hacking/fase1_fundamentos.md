# Módulo 7 — Hardware Hacking: Fase 1 — Fundamentos de Sistemas Embarcados
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

> Overlap: Eletrônica básica (Ohm, pull-ups, divisor tensão) → ver Circuitos F1/F2.
> UART/SPI/I2C teoria → ver Digital F3 (Mód 3.12). Aqui foca nos aspectos HH-específicos.

---

## Módulo HH.1.1: Eletrônica Digital — Complementos HH

> Lei de Ohm, pull-ups, bypass caps → já em Circuitos F1/F2 e Digital F3. Aqui: só contexto HH.

### Tensões padrão e level shifting

- TTL HIGH >> {{2.0 – 5.0V}} | TTL LOW >> {{0 – 0.8V}}
- CMOS HIGH >> {{⅔ Vcc – Vcc}} | CMOS LOW >> {{0 – ⅓ Vcc}}
- 3 tensões de operação mais comuns >> {{1.8V, 3.3V, 5V}}
- Conectar 5V direto em chip 3.3V → resultado >> {{Dano permanente}} ao chip!
- Level shifter >> Converte entre níveis de tensão {{diferentes}} de forma segura
- Divisor de tensão 5V→3.3V (solução barata) >> R1={{1.8kΩ}}, R2={{3.3kΩ}} (aproximado)

### Regra de ouro HH

- Antes de conectar QUALQUER coisa >> Verificar {{tensão de operação}} do alvo (multímetro)
- GND comum >> {{Obrigatório}} entre seu adapter e o dispositivo alvo

### Drill — Levels #[[Drill]]

- ESP32 opera a >> {{3.3V}} (GPIO e UART)
- Arduino Uno opera a >> {{5V}} (mas tem pinos 3.3V)
- USB-UART CH340 existe em versões >> {{3.3V e 5V}} (verificar antes de conectar!)
- Sinal 5V no pino 3.3V → corrente excessiva → >> {{Queima}} o chip

---

## Módulo HH.1.2: Arquitetura de Sistemas Embarcados

### SoC (System on Chip)

- SoC >> Chip único com CPU + ROM + RAM + {{periféricos}} integrados
- ARM Cortex-M >> Microcontrolador ({{MCU}}) — bare metal, IoT simples, sensores
- ARM Cortex-A >> Processador de {{aplicação}} — roda Linux, Android (roteadores, câmeras)
- MIPS >> Arquitetura comum em {{roteadores}} (MediaTek, Qualcomm Atheros)
- RISC-V >> Arquitetura {{open-source}} emergente

### Tipos de memória

- NOR Flash >> Executa código {{in-place}} (XIP). Acesso aleatório rápido. Cara, pequena
- NAND Flash >> Barata, grande. Precisa de {{bootloader}} para carregar código na RAM
- eMMC >> NAND + controlador integrado. Parece um {{HD/SSD}} miniatura. Smartphones, tablets
- EEPROM >> Pequena (bytes a KB), eletricamente apagável. Guarda {{configurações}} e chaves

### Boot sequence

- 1º: ROM de boot (mask ROM) >> Código {{imutável}} no SoC — define de onde carregar o bootloader
- 2º: Bootloader (U-Boot) >> Inicializa hardware, carrega {{kernel}} da flash
- 3º: Kernel Linux >> Inicializa drivers, monta {{filesystem}}
- 4º: Userspace (init) >> Inicia {{serviços}} e aplicações

### Memory-mapped I/O

- MMIO >> Periféricos acessados como {{endereços de memória}} (ler/escrever registradores = ler/escrever RAM)
- Mapa de memória típico >> 0x0000: ROM → 0x2000: SRAM → 0x4000: {{Periféricos}} → ...

### Drill — Arquitetura #[[Drill]]

- Roteador TP-Link com MediaTek MT7621 → arquitetura = >> {{MIPS}}
- ESP32 → arquitetura = >> {{Xtensa}} (dual-core, não é ARM!)
- STM32F103 → arquitetura = >> {{ARM Cortex-M3}}
- Raspberry Pi → arquitetura = >> {{ARM Cortex-A}}
- Chip W25Q128 → tipo de memória = >> {{NOR Flash SPI}}
- Boot sequence: ROM → >> {{U-Boot}} → Kernel → Userspace

---

## Módulo HH.1.3: Protocolos Seriais — Contexto HH

> Teoria de UART/SPI/I2C → ver Digital F3 (Módulo 3.12). Aqui: detalhes práticos de ataque.

### UART para hacking

- Frame UART >> idle HIGH → start bit ({{LOW}}) → 8 data → stop bit ({{HIGH}})
- 8N1 <> {{8 data bits, No parity, 1 stop bit}} (configuração padrão)
- Baud rates comuns >> {{9600, 115200}} (tentar 115200 primeiro!)
- Baud rate = >> $1 / T_{bit}$ (ex: bit dura 8.68μs → {{115200}} baud)
- Conexão cruzada >> TX do dispositivo → {{RX}} do adapter (e vice-versa)
- UART console de debug >> Frequentemente dá {{root shell}} sem autenticação!

### SPI para dump de flash

- Pinout SPI flash SOIC-8 >> P1={{CS}}, P2={{MISO}}, P3=WP, P4={{GND}}, P5={{MOSI}}, P6={{CLK}}, P7=HOLD, P8={{VCC}}
- Opcode de leitura SPI >> {{0x03}} + 3 bytes de endereço → dados fluem por MISO
- SOP8 clip >> "Garra" que abraça chip de {{8 pinos}} sem dessoldar
- CH341A >> Programador USB barato (~R$20) para ler/escrever flash {{SPI}}

### I2C para EEPROMs

- Endereço EEPROM típico >> {{0x50}} a 0x57 (pinos A0-A2 mudam o endereço)
- `i2cdetect -y 1` >> Lista todos os {{dispositivos}} no barramento I2C
- EEPROMs guardam >> Números de série, {{chaves de licença}}, configurações, calibração

### Drill — Protocolos HH #[[Drill]]

- Bit dura 8.68μs → baud rate = >> {{115200}} (1/8.68μ)
- Lixo na tela do terminal serial → causa provável = >> {{Baud rate errado}}
- Nada aparece no UART → primeira coisa a tentar = >> {{Inverter TX/RX}}
- Flash W25Q128: ler primeiros 256B → opcode + endereço = >> {{0x03 0x00 0x00 0x00}}
- Scan I2C encontrou dispositivo em 0x50 → provável = >> {{EEPROM}}

---

##  Módulo HH.1.4: Linux Embarcado

### Filesystem

- BusyBox >> Binário {{único}} que implementa centenas de comandos Unix (ls, cat, sh, wget...)
- `/etc/passwd` >> Lista de {{usuários}} do sistema (UID, shell)
- `/etc/shadow` >> {{Hashes}} de senhas (se acessível = jackpot!)
- `/etc/init.d/` >> Scripts de {{inicialização}} de serviços
- `/proc/mtd` >> Partições {{MTD}} (flash) — mostra layout da memória
- `/proc/cmdline` >> Argumentos do {{kernel}} no boot

### Primeiros 60 segundos após shell

- `id` / `whoami` >> Verificar se é {{root}} (uid=0)
- `uname -a` >> Versão do {{kernel}} e arquitetura
- `ps aux` >> {{Processos}} rodando (serviços, daemons)
- `netstat -tlnp` >> {{Portas}} abertas e serviços escutando
- `find / -perm -4000` >> Binários {{SUID}} (escalação de privilégio)
- `cat /proc/cmdline` >> Pode revelar {{senhas}} e configs no boot

### Busca por credenciais

- `grep -r "password" /etc/` >> Busca {{senhas hardcoded}} em configs
- `find / -name "*.conf" -o -name "*.cfg"` >> Arquivos de {{configuração}}
- `cat /etc/config/wireless` >> Credenciais {{Wi-Fi}} (OpenWrt)

### Drill — Linux Embarcado #[[Drill]]

- Qual binário substitui ls/cat/sh/wget num embarcado? >> {{BusyBox}}
- Precisa saber se é root: comando = >> {{`id`}} ou `whoami`
- Procurar senhas em configs: comando = >> {{`grep -r "password" /etc/`}}
- Ver partições da flash: arquivo = >> {{`/proc/mtd`}}
- Binários que rodam como root mesmo sem ser root: flag = >> {{SUID}} (bit 4000)

---

## Módulo HH.1.5: Leitura de Datasheets (Foco HH)

### Seções-chave para hacking

- Estrutura do datasheet >> Features → Pinout → Block Diagram → Electrical → {{Registers}} → Timing
- Seção #1 para HH >> {{Pinout}} (identificar UART/JTAG/SPI)
- Seção #2 >> {{Memory Map}} (onde estão ROM, RAM, periféricos)
- Seção #3 >> {{Boot Configuration}} (boot pins, modo de recovery)
- Seção #4 >> {{Debug Interface}} (JTAG/SWD habilitado por default?)

### Parâmetros elétricos essenciais

- VCC >> Tensão de {{alimentação}} (1.8V, 3.3V, 5V)
- VIH / VIL >> Nível lógico {{HIGH / LOW}} mínimo/máximo
- IOH / IOL >> Corrente de {{saída}} que o pino fornece/drena

### Chips de flash comuns

- W25Q128 (Winbond) >> Flash SPI {{128 Mbit}} (16MB). SOIC-8. Onipresente
- MX25L12835F (Macronix) >> Flash SPI {{128 Mbit}}. Similar ao W25Q128
- Read Data (opcode) >> {{0x03}}. JEDEC ID: {{0x9F}}

### Drill — Datasheets #[[Drill]]

- W25Q128: capacidade em bytes = >> {{16 MB}} (128 Mbit / 8)
- Pino 1 do SOIC-8 SPI flash = >> {{CS (Chip Select)}}
- Pino 8 = >> {{VCC}}
- Quer saber se JTAG está habilitado → seção do datasheet = >> {{Debug Interface / Boot Configuration}}

---

## Módulo HH.1.6: Equipamento e Solda

### Kit mínimo de HH

- USB-UART adapter >> CP2102 ou {{CH340}} (~R$15) — comunicação serial
- Analisador lógico >> Clone Saleae {{8 canais}} (~R$30) — decodifica protocolos
- Bus Pirate >> Interface {{multiprotocolo}} (UART/SPI/I2C/JTAG) (~R$80)
- CH341A >> Programador de flash {{SPI}} (~R$20)
- SOP8 clip >> Conecta em chip flash sem {{dessoldar}} (~R$15)
- Ferro de solda >> TS100/Pinecil com controle de {{temperatura}} (~R$150)

### Solda básica para HH

- Temperatura típica >> {{320–370°C}} (a maioria do estanho funde a ~183°C, margem para transferência)
- Estanho 63/37 >> Liga eutética de {{estanho/chumbo}}, funde a 183°C (melhor que lead-free para hobby)
- Flux >> {{Limpa}} as superfícies e melhora a aderência da solda
- Test point (TP) >> Pad exposto na PCB para {{medição/conexão}} (solde fio fino aqui)

### Drill — Equipamento #[[Drill]]

- Precisa ler flash SPI sem dessoldar → acessório = >> {{SOP8 clip}} + CH341A
- Precisa conectar no UART do alvo → adaptador = >> {{USB-UART (CP2102/CH340)}}
- Precisa decodificar sinais SPI no osciloscópio barato → ferramenta = >> {{Analisador lógico + PulseView}}
