# Módulo 7 — Hardware Hacking: Fase 6 — Wireless Hacking para IoT
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

> Overlap: RF básico (λ, impedância) → ver Eletrônica F5. Aqui: hacking wireless específico.

---

## Módulo HH.6.1: Fundamentos de RF e SDR

### Modulações para IoT

- OOK (On-Off Keying) >> Tipo mais {{simples}} de modulação: carrier ON = 1, OFF = 0
- ASK (Amplitude Shift Keying) >> Varia {{amplitude}} para representar bits (OOK é um caso especial)
- FSK (Frequency Shift Keying) >> Varia {{frequência}}: f₁ = 0, f₂ = 1
- PSK (Phase Shift Keying) >> Varia {{fase}} do carrier (mais robusto, Wi-Fi usa variantes)

### Bandas ISM para IoT

- 433 MHz >> Controles remotos de {{garagem}}, sensores baratos, alarmes
- 868 MHz (EU) / 915 MHz (US) >> {{LoRa}}, Zigbee sub-GHz, medidores smart
- 2.4 GHz >> {{Wi-Fi}}, Bluetooth/BLE, Zigbee, drones

### Hardware SDR

- SDR <> {{Software Defined Radio}} — demodulação em software, não hardware
- RTL-SDR >> Dongle TV digital (~R$60). Só {{recebe}} (RX). 25-1750 MHz
- HackRF One >> TX + RX, 1 MHz – {{6 GHz}} (~R$250). Pode transmitir!
- YARD Stick One >> Especializado em {{sub-GHz}} (controles, alarmes, keyfobs)
- Flipper Zero >> Dispositivo portátil multi-protocolo (sub-GHz, NFC, RFID, {{IR}})

### Software SDR

- URH (Universal Radio Hacker) >> Captura → {{demodula}} → decodifica → analisa protocolo
- GNURadio >> Framework de {{processamento de sinais}} RF (flow graphs visuais)
- GQRX >> Receptador SDR simples com {{spectrum analyzer}} (visualização rápida)

### Ataques RF comuns

- Replay attack >> {{Gravar}} transmissão legítima e retransmitir (garagem, alarme)
- Jamming >> Transmitir {{ruído}} na frequência alvo → bloqueia comunicação
- Bruteforce >> Tentar todas as combinações de {{código}} (controles com código fixo)
- Por que replay funciona >> Muitos dispositivos usam {{código fixo}} (não rolling code)

### Drill — RF/SDR #[[Drill]]

- Controle de garagem 433MHz com código fixo → ataque = >> {{Replay}} (gravar e retransmitir)
- Precisa só ouvir sinais RF do alarme → SDR barato = >> {{RTL-SDR}} (~R$60, RX only)
- Precisa transmitir sinal fake 433MHz → SDR = >> {{HackRF One}} ou YARD Stick One
- Sinal capturado mostra carrier pulsado ON/OFF → modulação = >> {{OOK}}
- Alarme usa rolling code → replay funciona? >> {{Não}} — código muda a cada transmissão

---

##  Módulo HH.6.2: Bluetooth Low Energy (BLE) Hacking

### Stack BLE

- Camada física >> 2.4 GHz, 40 canais (3 de {{advertising}}, 37 de dados)
- Link Layer >> Gerencia {{conexões}} e advertising
- GATT <> {{Generic Attribute Profile}} — organiza dados em Services → Characteristics

### GATT (modelo de dados)

- Service >> Grupo de {{characteristics}} relacionadas (ex: Battery Service 0x180F)
- Characteristic >> Um {{valor}} específico (ex: Battery Level, Heart Rate)
- UUID >> Identificador {{único}} de cada service/characteristic (16-bit padrão ou 128-bit custom)

### Advertising

- BLE advertising >> Broadcast {{sem conexão}} — qualquer scanner vê (nome, services, flags)
- Informações expostas >> Nome do dispositivo, UUIDs de services, TX power, {{manufacturer data}}
- Risco >> Dados sensíveis no advertising = {{vazamento}} para qualquer um próximo

### Ferramentas BLE

- `hcitool lescan` >> Escaneia dispositivos BLE {{próximos}}
- `gatttool -b XX:XX:XX -I` >> Conecta e interage com {{GATT}} (ler/escrever characteristics)
- `bettercap` >> Scan avançado + {{MITM}} BLE
- Wireshark >> Captura com filtro `{{btatt}}` para ver leituras/escritas GATT

### Ataques BLE

- Sniffing >> Capturar tráfego BLE {{não criptografado}} (dados de saúde, senhas)
- MITM >> Interceptar e modificar comunicação entre {{app}} e dispositivo
- Replay >> Capturar comando BLE (ex: "abrir fechadura") e {{retransmitir}}
- Pairing PIN bruteforce >> PIN de 6 dígitos = apenas {{1 milhão}} de combinações
- Just Works pairing >> {{Sem autenticação}} — MITM trivial

### UUIDs padrão conhecidos

- 0x180A <> {{Device Information}} Service
- 0x180F <> {{Battery}} Service
- 0x1812 <> {{HID (Human Interface Device)}} Service
- 0x2A19 <> Battery {{Level}} Characteristic (% restante)

### Drill — BLE #[[Drill]]

- Smart lock BLE usa "Just Works" pairing → vuln = >> {{MITM}} (sem autenticação no pairing)
- BLE scan mostra dispositivo com service UUID 0x180F → o que é? >> {{Battery Service}}
- `gatttool` lê characteristic 0x2A19 → valor 0x4B (75) → significa >> {{75% de bateria}}
- Fitness tracker BLE transmite dados de saúde sem criptografia → risco = >> {{Sniffing}} por qualquer scanner próximo
- PIN de pareamento BLE tem 6 dígitos → tentativas para bruteforce = >> {{10⁶ (1 milhão)}}

---

## Módulo HH.6.3: Wi-Fi e Zigbee para IoT

### Ataques Wi-Fi específicos para IoT

- Deauth + Evil Twin >> Derrubar dispositivo IoT do AP legítimo → criar {{AP falso}} → capturar credenciais
- OTA firmware interception >> Muitos updates via {{HTTP}} plaintext → MITM para injetar firmware malicioso
- WPS bruteforce >> PIN de 8 dígitos (efetivos {{7+1}}) → ~11.000 tentativas (Reaver/Bully)
- Credenciais Wi-Fi no firmware >> `grep -r "password\|psk" /etc/config/` → {{plaintext}} na flash

### Ferramentas Wi-Fi

- aircrack-ng suite >> airmon-ng (monitor mode), airodump-ng ({{captura}}), aireplay-ng (deauth), aircrack-ng (crack)
- bettercap >> MITM, ARP spoofing, {{packet injection}}
- wifite >> Automatiza ataque Wi-Fi (scan → {{capture → crack}} em um comando)
- hostapd >> Cria {{AP falso}} (Evil Twin)

### Zigbee

- IEEE 802.15.4 >> Protocolo base do Zigbee. 2.4 GHz, {{mesh}} networking
- Zigbee vs BLE >> Zigbee = {{mesh}} (dispositivos roteiam entre si). BLE = ponto-a-ponto
- Network key >> Chave {{AES-128}} que protege o tráfego na rede Zigbee
- Vulnerabilidade >> Network key frequentemente transmitida em {{plaintext}} durante device join!
- KillerBee >> Framework para {{sniffing e injeção}} de pacotes Zigbee (com hardware especial)

### Ataques Zigbee

- Key sniffing >> Capturar network key durante {{pairing}} de novo dispositivo
- Replay >> Retransmitir comando capturado (ex: "ligar/desligar {{lâmpada}}")
- Injection >> Enviar comandos falsos para dispositivos na {{rede}} (com a key obtida)

### Drill — Wi-Fi/Zigbee IoT #[[Drill]]

- Câmera IoT faz update via HTTP → ataque = >> {{MITM}} — interceptar e injetar firmware malicioso
- Dispositivo Zigbee sendo pareado → oportunidade = >> {{Sniffar network key}} (transmitida em plaintext)
- `grep -r "psk" /etc/config/` no firmware extraído → resultado = >> {{Senha Wi-Fi}} em plaintext
- WPS PIN tem 8 dígitos, mas efetivos = >> {{7+1}} (checksum + split em 4+3 = ~11.000 combos)
- Philips Hue usa Zigbee → topologia de rede = >> {{Mesh}} (lâmpadas roteiam entre si)
- Quer capturar handshake Wi-Fi do IoT → ferramenta = >> {{airodump-ng}} (modo monitor)
