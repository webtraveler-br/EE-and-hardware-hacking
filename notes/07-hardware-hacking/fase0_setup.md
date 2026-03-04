# Módulo 7 — Hardware Hacking: Fase 0 — Setup e Fundações
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

> Overlap com EE: Conversões bin/hex/dec já estão em Digital F1. Aqui foca-se no que é ESPECÍFICO de HH.

---

##  Módulo HH.0.1: Lab Virtual e Ferramentas

### Ferramentas essenciais

- Binwalk >> Extrai e analisa {{partições e filesystems}} de firmwares (.bin)
- Ghidra >> Disassembler/decompiler {{gratuito}} (NSA). Suporta ARM, MIPS, x86
- QEMU >> {{Emulador}} de CPUs (ARM, MIPS) — roda firmware sem hardware real
- flashrom >> Lê/escreve chips de flash {{SPI}} via CH341A ou Bus Pirate
- radare2 / r2 >> Framework de {{engenharia reversa}} por linha de comando
- PulseView (Sigrok) >> Analisador lógico — decodifica {{protocolos}} (UART/SPI/I2C)
- minicom / picocom >> {{Terminal serial}} para comunicação UART com dispositivos

### Diretórios Linux relevantes

- `/dev` >> Arquivos de {{dispositivos}} (ttyUSB0, spidev, i2c-*)
- `/proc` >> Filesystem virtual do {{kernel}} (processos, memória, hardware info)
- `/sys` >> Interface para {{drivers}} e hardware (GPIO, SPI, I2C)
- `/tmp` >> Arquivos {{temporários}} (gravável, útil para payloads)

### Drill — Ferramentas #[[Drill]]

- Extrair filesystem de firmware.bin → comando = >> {{`binwalk -e firmware.bin`}}
- Emular binário ARM no x86 → usar >> {{`qemu-arm-static`}}
- Ler chip flash SPI com CH341A → ferramenta = >> {{flashrom}}
- Disassemblar binário MIPS → ferramenta gratuita = >> {{Ghidra}}

---

## Módulo HH.0.2: Mindset e Superfícies de Ataque

### Modelo de ameaça

- STRIDE <> {{Spoofing, Tampering, Repudiation, Information Disclosure, Denial of Service, Elevation of Privilege}}

### 5 superfícies de ataque em hardware

- Superfície 1 >> Acesso {{físico}} (abrir, soldar, modificar PCB)
- Superfície 2 >> Interfaces de {{debug}} (UART, JTAG, SWD)
- Superfície 3 >> {{Firmware}} (extrair, analisar, modificar)
- Superfície 4 >> Comunicação {{wireless}} (Wi-Fi, BLE, Zigbee, RF)
- Superfície 5 >> {{Supply chain}} (componentes falsificados, backdoors de fábrica)

### Princípios

- Segurança por obscuridade >> {{Falha}} como única defesa ("ninguém vai abrir")
- Segurança por design >> Assume que atacante tem acesso {{total}} — defende em camadas
- 90% dos dispositivos IoT >> Interfaces de debug {{habilitadas}}, firmware sem criptografia, senhas hardcoded

### Drill — Mindset #[[Drill]]

- Roteador com UART habilitado + shell root → superfície = >> {{Debug}} (interface serial)
- Firmware .bin baixado do site → superfície = >> {{Firmware}} (análise offline)
- BLE sniffing de smart lock → superfície = >> {{Wireless}}
- Chip flash soldado na PCB → leitura com clip SOP8 → superfície = >> {{Física}}

---

## Módulo HH.0.3: Python para Hardware Hacking

### Manipulação de bytes

- `struct.pack('>I', val)` >> Empacota inteiro em {{big-endian}} (4 bytes)
- `struct.pack('<I', val)` >> Empacota inteiro em {{little-endian}} (4 bytes)
- `struct.unpack('<I', data)[0]` >> Desempacota {{4 bytes LE}} em inteiro
- `int.from_bytes(b, 'little')` >> Converte bytes em inteiro {{little-endian}}
- `bytes.fromhex('DEADBEEF')` >> Converte string hex em {{objeto bytes}}

### Operações bitwise

- `&` (AND) >> Extrair/{{mascarar}} bits específicos (ex: `val & 0xFF` = byte inferior)
- `|` (OR) >> {{Setar}} bits específicos (ex: `val | 0x80` = setar bit 7)
- `^` (XOR) >> {{Inverter/criptografar}} bits (ex: XOR encrypt)
- `<<` (shift left) >> Multiplicar por {{$2^n$}} (deslocar bits para esquerda)
- `>>` (shift right) >> Dividir por {{$2^n$}} (deslocar bits para direita)

### Comunicação serial

- pyserial >> `serial.Serial('/dev/ttyUSB0', {{115200}})` abre conexão UART
- Ler dados >> `ser.read(N)` lê {{N bytes}} (bloqueia até receber ou timeout)
- Abrir binário >> `open('fw.bin', '{{rb}}')` abre em modo leitura binária

### Drill — Python HH #[[Drill]]

- `struct.pack('<I', 0xDEADBEEF)` → bytes = >> {{`\xef\xbe\xad\xde`}} (LE inverte)
- `0xAB & 0x0F` = >> {{0x0B}} (mascara nibble inferior)
- `1 << 3` = >> {{8}} ($2^3$)
- `0xAA ^ 0xFF` = >> {{0x55}} (inverte todos os bits)
- XOR encrypt: `b'A' ^ 0x42` → `chr(result)` = >> {{`\x03`}} (0x41 ⊕ 0x42)

---

## Módulo HH.0.4: Endianness e Representação de Dados

> Conversões bin/hex/dec → ver **Digital F1** (Módulo 3.1). Aqui: só o que é NOVO para HH.

### Endianness

- Big-endian (BE) >> Byte mais significativo {{primeiro}} na memória
- Little-endian (LE) >> Byte menos significativo {{primeiro}} na memória
- `0x12345678` em BE >> armazenado {{`12 34 56 78`}}
- `0x12345678` em LE >> armazenado {{`78 56 34 12`}}
- x86 usa >> {{Little-endian}}
- ARM (maioria) usa >> {{Little-endian}} (configurável)
- Protocolos de rede (TCP/IP) usam >> {{Big-endian}} (network byte order)
- MIPS >> {{Configurável}} (LE ou BE dependendo do SoC)

### Tamanhos de dados

- Byte >> {{8}} bits
- Word >> {{16}} bits (em muitas archs, mas varia!)
- DWORD >> {{32}} bits (Double Word)
- QWORD >> {{64}} bits (Quad Word)

### Strings e magic bytes

- ASCII 'A' >> {{0x41}}
- Null-terminated string >> String C terminada em {{`\x00`}}
- Magic bytes >> Primeiros bytes que identificam {{formato}} do arquivo
- `\x7fELF` >> Magic de executável {{ELF}} (Linux/ARM)
- `27 05 19 56` >> Magic de imagem {{U-Boot}} (uImage)
- `68 73 71 73` >> Magic de filesystem {{SquashFS}}

### Drill — Endianness #[[Drill]]

- Dump mostra `EF BE AD DE` → valor LE = >> {{0xDEADBEEF}}
- Dump mostra `EF BE AD DE` → valor BE = >> {{0xEFBEADDE}}
- IP 192.168.1.1 (0xC0A80101) em memória LE = >> {{`01 01 A8 C0`}}
- Firmware começa com `7f 45 4c 46` → formato = >> {{ELF}}
- Firmware começa com `27 05 19 56` → formato = >> {{U-Boot (uImage)}}
- DWORD ocupa quantos bytes? >> {{4}}
