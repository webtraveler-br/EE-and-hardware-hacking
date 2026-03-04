# Módulo 7 — Hardware Hacking: Fase 2 — Reconhecimento e Análise de PCB
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo HH.2.1: OSINT para Dispositivos IoT

### Fontes de informação

- FCC ID >> Fotos {{internas}} da PCB do dispositivo (fcc.gov/oet/ea/fccid)
- WikiDevi >> Database com {{chipsets}} e especificações de roteadores/APs
- OpenWrt Table of Hardware >> Lista dispositivos com {{suporte}} a firmware custom
- Shodan / Censys >> Buscadores de dispositivos expostos na {{internet}}
- Exploit-DB / NVD >> Bases de {{CVEs}} e exploits conhecidos por modelo/chipset

### Firmware público

- Firmware do fabricante >> Quase sempre disponível no site de {{suporte}} (download direto)
- Google dorking >> `inurl:firmware filetype:bin site:{{domínio do fabricante}}`
- archive.org >> Versões {{antigas}} de firmware (podem ter menos proteções)

### Shodan dorks úteis

- `port:23 "login"` >> Telnets {{abertos}} com login prompt
- `http.title:"router"` >> Painéis web de {{roteadores}} expostos
- `port:80 "GoAhead"` >> Webcam/câmeras com GoAhead {{webserver}} (frequentemente vulneráveis)

### Drill — OSINT #[[Drill]]

- Precisa ver PCB sem abrir o dispositivo → fonte = >> {{FCC ID}} (fotos internas obrigatórias para certificação)
- Buscar CVEs de um chipset específico → base = >> {{NVD / Exploit-DB}}
- Firmware antigo removido do site → tentar em >> {{archive.org (Wayback Machine)}}
- Verificar se roteador suporta OpenWrt → consultar >> {{OpenWrt Table of Hardware}}

---

## Módulo HH.2.2: Análise Visual de PCB

### Encapsulamentos de circuitos integrados

- QFP (Quad Flat Package) >> Pinos nos {{4 lados}}. SoCs menores, FPGAs
- BGA (Ball Grid Array) >> Esferas de solda {{embaixo}} do chip. SoCs principais, difícil acesso
- SOIC-8 >> 8 pinos, retangular. Típico de flash {{SPI}} e EEPROMs
- SOT-23 >> 3-6 pinos, minúsculo. Transistores e reguladores {{pequenos}}
- 0805 / 0603 >> Código de tamanho (imperial). Resistores/capacitores {{SMD}} passivos

### Identificação de componentes na PCB

- SoC >> Maior chip (geralmente {{BGA}}), centro da placa, muitas trilhas irradiando
- Flash SPI >> Chip {{SOIC-8}} perto do SoC (8 pinos, retangular)
- RAM >> Chips retangulares {{simétricos}}, muito próximos ao SoC (barramento de dados largo)
- Cristal oscilador >> Componente {{metálico}} retangular/cilíndrico, 2 ou 4 pinos. Frequência gravada
- Regulador de tensão >> Chip com {{heatsink}} pad ou tab grande

### Part numbers

- Número no topo do CI >> Identifica o chip {{exato}} → buscar no Google/Octopart
- Winbond W25Q128 >> Flash SPI {{16MB}} (128 Mbit)
- Macronix MX25L >> Flash SPI (família similar ao {{W25Q}})
- MediaTek MT76xx >> SoC de {{roteador}} (MIPS ou ARM)
- Realtek RTL8196 >> SoC de roteador {{MIPS}} (muito comum no Brasil)

### Test points e headers

- 4 pinos em linha >> Provavelmente {{UART}} (GND, RX, TX, VCC)
- 10 ou 20 pinos em grade >> Provavelmente {{JTAG}} (ARM standard)
- Pads rotulados TP1, TP2... >> {{Test points}} de engenharia (podem ser UART/JTAG)
- Pads rotulados J1, J2... >> {{Headers}} (conectores) de debug
- Seguir trilhas de test point até CI >> Revela a {{função}} do pad

### Drill — PCB #[[Drill]]

- Chip 8 pinos, retangular, perto do SoC → provável = >> {{Flash SPI (SOIC-8)}}
- Chip enorme com esferas embaixo → encapsulamento = >> {{BGA}}
- 4 pads em linha na borda da PCB → provavelmente = >> {{UART header}}
- "W25Q64" gravado no chip → é = >> {{Flash SPI 64 Mbit (8MB) Winbond}}
- "RT5350" no SoC → arquitetura = >> {{MIPS}} (Ralink/MediaTek)

---

## Módulo HH.2.3: Identificação de Pinos UART com Multímetro

### Procedimento de 4 passos

- Passo 1 (GND) >> Modo {{continuidade}}: testar cada pad contra blindagem/terra do conector de força. Bipa = GND
- Passo 2 (VCC) >> Modo {{tensão DC}}: medir cada pad restante. Leitura constante {{3.3V ou 5V}} = VCC
- Passo 3 (TX) >> Tensão que {{varia}} durante o boot (dados saindo do dispositivo)
- Passo 4 (RX) >> Pino restante. Geralmente {{flutuante}} ou em HIGH quando idle

### Modo continuidade do multímetro

- Continuidade >> Verifica se há {{conexão elétrica}} entre dois pontos (R ≈ 0)
- Uso >> Mapear {{GND}}, rastrear {{trilhas}}, verificar curtos

### Conexão UART

- Após identificar pinos >> Conectar GND→GND, TX(alvo)→{{RX(adapter)}}, RX(alvo)→{{TX(adapter)}}
- VCC do alvo → {{NÃO conectar}} ao adapter (alimentar o device pela fonte dele)
- Software >> `minicom -D /dev/ttyUSB0 -b {{115200}}`
- Se lixo na tela >> Trocar {{baud rate}}
- Se nada na tela >> Inverter {{TX/RX}}

### Drill — Identificação #[[Drill]]

- Pad bipa em continuidade com terra do jack DC → é = >> {{GND}}
- Pad lê 3.3V constante → é = >> {{VCC}}
- Pad com tensão que pisca durante boot → é = >> {{TX}} (dados saindo)
- Conectou tudo certo mas tela mostra caracteres aleatórios → problema = >> {{Baud rate errado}}
- Conectou mas nada aparece (tela vazia) → duas causas prováveis = >> {{TX/RX invertidos}} ou baud rate errado

---

## Módulo HH.2.4: Mapeamento Completo do Alvo

### Checklist de reconhecimento

- Hardware >> SoC ({{arquitetura}}), Flash ({{tipo + tamanho}}), RAM, interfaces expostas
- Software >> {{OS}}, versão do kernel, bootloader, serviços rodando
- Rede >> Portas {{abertas}}, protocolos, APIs expostas
- Físico >> Test points, headers, componentes {{removíveis}}
- Supply chain >> Fabricante, {{ODM}} (mesmo firmware em modelos diferentes?)

### Priorização de ataque

- Vetor mais fácil geralmente >> {{UART}} (shell direto se console de debug ativo)
- Vetor mais informação >> {{Dump de flash SPI}} (firmware completo offline)
- Vetor mais stealth >> {{Firmware do site}} (sem tocar no hardware)

### Drill — Mapeamento #[[Drill]]

- Alvo tem UART com root shell + flash SPI + firmware público → por onde começar? >> {{Firmware público}} (sem tocar no hardware, zero risco)
- UART shell mostra "login:" sem dar root → próximo passo = >> {{Dump de flash}} e extrair `/etc/shadow` para crack offline
- Dois modelos de roteador diferentes, mesmo ODM → firmware provavelmente = >> {{Compartilhado}} (ou muito similar)
