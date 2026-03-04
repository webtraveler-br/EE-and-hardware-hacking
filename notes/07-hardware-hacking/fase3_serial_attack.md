# Módulo 7 — Hardware Hacking: Fase 3 — Interfaces Seriais: Ataque e Exploração
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

> Overlap: UART/SPI/I2C básicos → ver HH F1 (Mód HH.1.3). Aqui: técnicas de ATAQUE.

---

##  Módulo HH.3.1: Exploração UART na Prática

### Exploração

- Console UART de debug >> Frequentemente dá shell {{root}} sem autenticação (esquecido em produção)
- Ferramentas de terminal >> {{minicom}}, screen, picocom, PuTTY
- `minicom -D /dev/ttyUSB0 -b 115200` >> Abre terminal serial no {{adapter USB-UART}}
- Após shell: primeiro comando >> {{`id`}} ou `whoami` (confirmar UID=0 root)

### Troubleshooting serial

- Lixo na tela >> {{Baud rate}} errado (tentar 115200, 9600, etc.)
- Tela em branco >> TX/RX {{invertidos}} ou dispositivo não transmitindo
- Caracteres repetidos/faltando >> Level {{mismatch}} (5V adapter em chip 3.3V)
- Sem resposta a comandos >> Boot já passou do prompt; tentar enviar Enter durante o {{boot}}

### Baud rate auto-detect

- Baud rates mais comuns >> {{115200}} (90% dos casos), 9600, 57600
- Cálculo >> baud = $1 / T_{bit}$
- Analisador lógico >> Medir duração do menor {{pulso}} → calcular baud rate

### Drill — UART Exploit #[[Drill]]

- Conectou UART, shell diz `uid=0(root)` → nível de acesso = >> {{Root total}} (máximo)
- Tela mostra `¥ü³Ñ` → ação = >> {{Mudar baud rate}} (tentar 9600 ou 57600)
- UART dá `login:` mas não sabe a senha → próximo passo = >> {{Dump de flash}} para extrair `/etc/shadow`
- `cat /etc/shadow` mostra `root:$1$abc:...` → formato hash = >> {{MD5 ($1$)}}, crackável com hashcat

---

## Módulo HH.3.2: Dump de Flash SPI — Prática de Ataque

### Procedimento de dump

- Ferramenta padrão >> {{flashrom}} (linha de comando, suporta centenas de chips)
- Comando de leitura >> `flashrom -p ch341a_spi -r {{dump.bin}}`
- Verificação >> Fazer {{2-3 dumps}} e comparar MD5 (devem ser idênticos)
- Se MD5 difere >> Problema de {{conexão}} elétrica (clip mal encaixado, sinal fraco)

### In-circuit vs chip-off

- In-circuit (SOP8 clip) >> Ler chip {{sem dessoldar}} — mais fácil, mas pode ter conflito com SoC
- Chip-off >> {{Dessoldar}} o chip, ler isolado — mais confiável, mas destrutivo
- Conflito in-circuit >> SoC tenta falar com flash {{ao mesmo tempo}} → dados corrompidos. Solução: manter SoC em reset

### Pós-dump: análise com Binwalk

- `binwalk dump.bin` >> Lista {{partições}} e magic bytes encontrados
- `binwalk -e dump.bin` >> Extrai {{filesystem}} automaticamente
- `binwalk -E dump.bin` >> Gráfico de {{entropia}} (alta = comprimido/criptografado)
- `strings dump.bin | grep -i "password"` >> Busca {{credenciais}} hardcoded

### Drill — Flash Dump #[[Drill]]

- Dump 1: MD5=abc123, Dump 2: MD5=abc123, Dump 3: MD5=def456 → ação = >> {{Refazer dump 3}} (clip mal encaixado)
- Entropia do bloco = 0.99 → conteúdo provavelmente = >> {{Criptografado ou comprimido}}
- Entropia do bloco = 0.3 → conteúdo provavelmente = >> {{Dados raw/strings}} (legível)
- Binwalk mostra "SquashFS" em offset 0x180000 → próximo = >> {{`binwalk -e`}} para extrair filesystem

---

## Módulo HH.3.3: Hacking de EEPROM I2C

### O que EEPROMs guardam

- Dados típicos >> Número de série, chaves de {{licença}}, credenciais Wi-Fi, calibração
- AT24C02 >> {{256 bytes}} (2 Kbit). Pequena, só configs
- AT24C256 >> {{32 KB}} (256 Kbit). Pode conter certificados, chaves
- Sem proteção >> Maioria das EEPROMs permite leitura {{sem autenticação}}

### Leitura e escrita

- Via Linux >> `i2cget -y 1 0x50 0x00` lê {{1 byte}} do endereço 0x00
- Via Bus Pirate >> Modo I2C → enviar START + {{endereço}} + R → ler dados
- Via Python >> `smbus2.SMBus(1).read_byte_data({{0x50}}, 0x00)`
- Dump completo >> Loop lendo {{byte a byte}} do endereço 0x00 até o último

### Ataques práticos

- Modificar serial number >> Bypass de {{licenciamento}} ou warranty
- Alterar config Wi-Fi >> Redirecionar tráfego para {{AP controlado}}
- Substituir chave de criptografia >> Descriptografar comunicações {{futuras}}

### Drill — EEPROM #[[Drill]]

- EEPROM no endereço 0x52 → pinos A0-A2 = >> {{A1=1}} (0x50 + 2 = 0x52)
- `i2cdetect` mostra 0x50, 0x68 → 0x50 = >> {{EEPROM}}, 0x68 = provavelmente {{RTC (relógio)}}
- Dump da EEPROM contém `admin:root123\x00` → achado = >> {{Credenciais hardcoded}}

---

## Módulo HH.3.4: Attack Chain Completa

### Cadeia de ataque serial

- Fase 1 >> {{OSINT}} — identificar alvo, baixar firmware público, FCC ID
- Fase 2 >> {{PCB}} — abrir, identificar chips, encontrar test points
- Fase 3 >> {{UART}} — conectar, obter shell (ou pelo menos boot log)
- Fase 4 >> {{Flash dump}} — extrair firmware completo para análise offline
- Fase 5 >> {{Análise}} — Binwalk, strings, Ghidra → encontrar vulns

### Pós-exploração

- Pivoting >> Do shell UART, usar {{rede}} para atacar outros dispositivos na LAN
- Persistência >> Modificar firmware na flash, inserir {{backdoor}} (sobrevive reboot)
- Exfiltração >> Extrair chaves, certificados, {{credenciais}} armazenadas

### Ferramentas de prática

- DVRF >> {{Damn Vulnerable Router Firmware}} — firmware MIPS com vulns intencionais
- EmbedOS >> Ambiente de prática para {{firmware analysis}}
- firmware-analysis-toolkit >> Emulação automatizada com {{QEMU}}

### Drill — Attack Chain #[[Drill]]

- Tem shell root mas quer persistência → ação = >> {{Modificar firmware na flash}} (backdoor)
- Shell root + `netstat` mostra porta 80 aberta → próximo = >> {{Investigar webserver}} (command injection, default creds)
- Firmware análise encontrou `system(user_input)` → vuln = >> {{Command injection}} (OS command execution)

---

## Módulo HH.3.5: U-Boot e Bootloaders

### U-Boot essencial

- U-Boot >> Bootloader {{mais comum}} em embarcados Linux (roteadores, câmeras, NAS)
- Acesso >> Via {{UART}} durante o boot — pressionar tecla para interromper autoboot
- `bootdelay` >> Variável que define {{tempo}} (em segundos) para interromper o boot
- Se bootdelay = 0 → Sem janela para interromper → buscar {{bypass}} (EEPROM, glitch)

### Comandos U-Boot

- `printenv` >> Mostra todas as {{variáveis de ambiente}}
- `setenv bootargs "... init=/bin/sh"` >> Boot direto para {{shell root}} (bypass de init/login)
- `md 0x80000000 100` >> Memory display — lê {{memória}} a partir do endereço
- `sf probe; sf read 0x80000000 0x0 0x1000` >> Lê {{flash SPI}} pelo U-Boot
- `tftpboot 0x80000000 firmware.bin` >> Carrega firmware via {{rede}} (TFTP)
- `saveenv` >> Salva variáveis alteradas na {{flash}} (persiste após reboot)

### Variáveis críticas

- `bootcmd` >> Comando executado no {{autoboot}} (define o que roda)
- `bootargs` >> Parâmetros passados ao {{kernel}} (console, root, init)
- `serverip` / `ipaddr` >> IPs para boot via {{TFTP}} (carregar firmware custom)

### Ataques via U-Boot

- `init=/bin/sh` no bootargs >> Kernel inicia {{shell}} em vez do sistema normal (root direto)
- `single` no bootargs >> Boot em modo {{single-user}} (sem rede, sem login, root direto)
- TFTP boot >> Carregar firmware {{modificado}} pela rede (bypass total)
- `md` + dump manual >> Ler {{toda a flash}} pelo terminal se outras ferramentas falham

### Drill — U-Boot #[[Drill]]

- Device liga, vê "Hit any key to stop autoboot: 3" → ação = >> {{Pressionar tecla}} imediatamente
- `printenv` mostra `bootdelay=0` → sem janela → alternativa = >> {{Alterar via EEPROM}} ou {{voltage glitch}}
- Quer shell root sem saber a senha → variável a mudar = >> {{`bootargs`}} (adicionar `init=/bin/sh`)
- Alterou bootargs mas rebootou e voltou ao normal → esqueceu de >> {{`saveenv`}}
- Quer carregar firmware custom pela rede → comando = >> {{`tftpboot`}} + `bootm`
- `bootcmd=bootm 0x9F020000` → firmware começa no offset = >> {{0x9F020000}} da flash
