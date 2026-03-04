# Módulo 7 — Hardware Hacking: Fase 5 — Firmware: Extração, Análise e Exploração
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

> Overlap: Binwalk básico já em HH F3 (dump de flash). Aqui: análise profunda, Ghidra, emulação, modificação.

---

## Módulo HH.5.1: Extração e Descompressão de Firmware

### Binwalk avançado

- `binwalk firmware.bin` >> Lista {{magic bytes}} e partições encontradas
- `binwalk -e firmware.bin` >> {{Extrai}} automaticamente tudo que reconhecer
- `binwalk -E firmware.bin` >> Gráfico de {{entropia}} (identifica criptografia vs compressão)
- `binwalk -A firmware.bin` >> Identifica {{opcodes}} (detecta arquitetura: ARM, MIPS, x86)
- `binwalk -W fw_v1.bin fw_v2.bin` >> {{Diff}} entre duas versões de firmware

### Filesystems embarcados

- SquashFS >> Filesystem {{read-only}} comprimido. O mais comum em roteadores
- JFFS2 >> Filesystem {{read-write}} para NOR flash (journaling)
- UBIFS >> Filesystem para {{NAND}} flash (substituto moderno do JFFS2)
- CramFS >> FS comprimido {{legado}} (mais antigo que SquashFS)
- YAFFS2 >> FS para NAND flash, comum em {{Android}} antigo

### Estrutura típica de firmware

- Ordem no blob >> Header → {{bootloader}} → kernel → rootfs → config → art
- Header >> Contém {{magic bytes}}, tamanho, CRC, versão
- rootfs >> Filesystem com {{/bin, /etc, /usr}} — onde estão os binários e configs
- art partition >> {{Calibração}} wireless (dados de fábrica, específicos do hardware)

### Entropia como ferramenta

- Entropia ~1.0 (máxima, uniforme) >> Provavelmente {{criptografado}}
- Entropia ~0.9 (alta, mas com padrões) >> Provavelmente {{comprimido}} (gzip, LZMA)
- Entropia baixa (~0.3-0.5) >> Dados {{raw}}, strings, código não comprimido
- Queda abrupta de entropia >> {{Fronteira}} entre seções (fim de bloco comprimido)

### Drill — Firmware Extraction #[[Drill]]

- Binwalk mostra "Squashfs filesystem" em offset 0x180000 → próximo = >> {{`binwalk -e`}} para extrair
- Entropia uniforme 0.99 sem magic bytes → conteúdo = >> {{Criptografado}}
- Entropia 0.95 com header LZMA → conteúdo = >> {{Comprimido}} (LZMA)
- Quer comparar firmware v1.3 vs v1.4 → flag = >> {{`binwalk -W`}} (diff)
- Quer saber a arquitetura do firmware → flag = >> {{`binwalk -A`}} (opcode scan)

---

## Módulo HH.5.2: Análise Estática com Ghidra

### Workflow Ghidra

- Passo 1 >> Criar projeto → Import File → selecionar {{arquitetura}} (ARM LE 32, MIPS LE 32)
- Passo 2 >> Rodar {{auto-analysis}} (detecta funções, strings, cross-references)
- Passo 3 >> Buscar strings: Window → {{Defined Strings}} → filtrar "password", "admin", "key"
- Passo 4 >> Cross-references ({{XREFs}}) → seguir de string até a função que a usa
- Passo 5 >> Analisar no {{Decompiler}} (pseudocódigo C)

### Janelas essenciais

- Decompiler >> Mostra {{pseudocódigo C}} da função selecionada
- Listing >> Mostra {{assembly}} do binário (instrução por instrução)
- Symbol Tree >> Árvore de {{funções}}, variáveis globais, imports
- Cross References >> Mostra {{quem chama}} e {{quem é chamado}} por uma função

### Funções perigosas (buscar no Ghidra)

- `system()` / `popen()` >> Executa {{comando do OS}} — command injection se input do usuário
- `strcpy()` / `sprintf()` >> Copia sem verificar {{tamanho}} — buffer overflow
- `gets()` >> Lê input sem {{limite}} — SEMPRE vulnerável a overflow
- `strcmp()` com senha >> Comparação em {{tempo constante}}? Se não, timing attack

### Drill — Ghidra #[[Drill]]

- Firmware MIPS little-endian → configurar Ghidra como >> {{MIPS:LE:32:default}}
- Buscar credenciais hardcoded → janela = >> {{Defined Strings}} (filtrar "password")
- String "admin123" tem XREF em função `check_login` → como analisar? >> Abrir no {{Decompiler}} e ler pseudocódigo C
- Decompiler mostra `system(user_input)` → vuln = >> {{Command injection}}
- Decompiler mostra `strcpy(buf, input)` sem bounds check → vuln = >> {{Buffer overflow}}

---

##  Módulo HH.5.3: Emulação de Firmware com QEMU

### Modos de emulação

- QEMU system mode >> Emula CPU + {{memória + periféricos}} → firmware completo
- QEMU user mode >> `qemu-{arch}-static` → roda {{um binário}} individual de outra arch
- chroot trick >> Copiar `qemu-mipsel-static` para rootfs extraído → {{`chroot . /bin/sh`}}

### Ferramentas de automação

- Firmadyne >> Framework para emulação + análise {{automatizada}} de firmware Linux
- FAT (Firmware Analysis Toolkit) >> Wrapper que simplifica {{Firmadyne}} (um comando)
- `./fat.py firmware.bin` >> Emula e expõe {{interface web}} + serviços do firmware

### chroot manual

- Comando >> `sudo chroot squashfs-root /usr/bin/qemu-mipsel-static {{/bin/sh}}`
- Requisito >> Copiar `qemu-{arch}-static` para {{dentro}} do rootfs extraído
- Vantagem >> Roda binários do firmware como se estivesse {{dentro}} do dispositivo

### Drill — Emulação #[[Drill]]

- Quer rodar um binário ARM sem hardware ARM → comando = >> {{`qemu-arm-static ./binário`}}
- Quer shell completo dentro do rootfs MIPS → técnica = >> {{chroot}} com qemu-mipsel-static
- Emulação falha com "Kernel panic" → causa comum = >> Drivers de {{hardware específico}} não presentes no QEMU
- Quer acessar interface web do firmware emulado → ferramenta = >> {{FAT/Firmadyne}}

---

## Módulo HH.5.4: Modificação e Reempacotamento

### Fluxo de modificação

- Cadeia >> Extrair → {{modificar}} → reempacotar → flash (ou emular para validar)
- SquashFS repack >> {{`mksquashfs squashfs-root/ new.sqsh -comp xz`}}
- JFFS2 repack >> {{`mkfs.jffs2 -d rootfs/ -o new.jffs2`}}
- firmware-mod-kit >> Automatiza {{extração e reempacotamento}} (wrapper sobre Binwalk + mkfs)

### Modificações comuns

- Habilitar telnet/SSH >> Adicionar entry em {{`/etc/init.d/`}} ou desbloquear em config
- Trocar senha root >> Editar {{`/etc/passwd`}} ou `/etc/shadow`
- Inserir backdoor >> Script em init.d que abre {{reverse shell}} no boot
- Alterar configuração >> Mudar DNS, rota, credenciais Wi-Fi no {{`/etc/config/`}}

### Obstáculos

- CRC/checksum >> Firmware tem verificação de integridade → precisa {{recalcular}} após edição
- Tamanho >> Filesystem modificado {{maior}} que o original → não cabe na flash
- Secure boot >> Firmware assinado → modificação detectada → {{recusa boot}} (bypass necessário)

### Drill — Modificação #[[Drill]]

- Quer reempacotar SquashFS com compressão XZ → comando = >> {{`mksquashfs dir/ out.sqsh -comp xz`}}
- Firmware modificado não inicializa, "Bad CRC" → problema = >> {{Checksum}} do header não foi recalculado
- Firmware modificado é maior que a partição → solução = >> {{Remover}} arquivos desnecessários ou usar compressão mais agressiva

---

##  Módulo HH.5.5: Firmware Criptografado

### Identificação

- Criptografado vs comprimido >> Criptografia: entropia {{uniforme}}, sem patterns. Compressão: tem {{headers}} (gzip, LZMA magic)
- `file firmware.bin` retorna "data" >> Provavelmente {{criptografado}} (nenhum formato reconhecido)
- `strings firmware.bin` vazio >> Confirma {{criptografia}} (comprimido ainda teria headers)

### Estratégias de ataque

- Chave no bootloader >> Bootloader precisa decriptar → chave está na {{ROM ou flash}} (extrair via JTAG)
- Firmware downgrade >> Versão antiga sem criptografia pode conter a {{chave}} ou o algoritmo
- RAM dump durante boot >> Firmware é decriptado na {{RAM}} antes de executar → dump via JTAG nesse momento
- Chave hardcoded >> `strings bootloader.bin | grep -i key` → {{chave em plaintext}} (surpreendentemente comum)
- Side-channel >> Medir {{consumo de energia}} durante decriptação → derivar a chave (avançado)

### Drill — FW Criptografado #[[Drill]]

- Binwalk não encontra nada, entropia 0.99 uniforme → firmware está >> {{Criptografado}}
- Fabricante tem firmware v1.0 sem crypto e v2.0 com crypto → estratégia = >> {{Downgrade}} para v1.0, extrair chave/algoritmo
- JTAG disponível + firmware é decriptado durante boot → estratégia = >> {{Dump de RAM}} durante boot (capturar firmware decriptado)

---

##  Módulo HH.5.6: Hacking de Interfaces Web IoT

### Servidores web em IoT

- Web servers comuns >> {{lighttpd}}, uhttpd, GoAhead, mini_httpd, Boa
- Rodam tipicamente como >> {{root}} (command injection = root shell!)
- HTTPS >> Maioria dos dispositivos IoT {{não usa}} (tráfego em plaintext)

### Vulnerabilidades web clássicas em IoT

- Command injection >> Campos de ping/traceroute/DNS aceitam {{; cat /etc/passwd}}
- Directory traversal >> Parâmetro `?file={{../../../etc/shadow}}`
- Credenciais hardcoded >> admin/admin, admin/{{password}}, root/root
- Buffer overflow via HTTP >> Headers ou parâmetros {{muito longos}} crasham o webserver
- CSRF sem token >> Mudar senha/config via request {{forjada}}
- API sem autenticação >> Endpoints REST retornam dados sem checar {{sessão}}

### Metodologia de ataque web IoT

- Passo 1 >> Testar credenciais {{padrão}} (admin/admin, admin/1234)
- Passo 2 >> Enumerar {{endpoints}} (`curl`, buscar em links/forms/scripts)
- Passo 3 >> Testar command injection em campos de {{diagnóstico}} (ping, traceroute)
- Passo 4 >> Testar directory traversal >> `?file={{../../etc/passwd}}`
- Passo 5 >> Confirmar no Ghidra >> Buscar {{`system()`}} e {{`popen()`}} no binário do webserver

### CGI em IoT

- Scripts CGI (`.cgi`) >> {{Ponto mais vulnerável}} — frequentemente escritos em C sem validação
- Chamadas perigosas >> `sprintf(cmd, "ping %s", user_ip); system(cmd);` → {{command injection}}

### Drill — Web IoT #[[Drill]]

- Campo de ping aceita `; id` e retorna `uid=0(root)` → vuln = >> {{Command injection}} com privilégio root
- Interface web sem HTTPS → risco = >> {{Sniffing}} de credenciais em plaintext
- API endpoint `/api/config` responde sem cookie → vuln = >> {{API sem autenticação}}
- Ghidra mostra `sprintf(buf, "ping %s", input); system(buf)` → explorar com >> Input: {{`; cat /etc/shadow`}}
- Webserver é GoAhead → pesquisar >> {{CVEs conhecidos}} do GoAhead (histórico de vulns)
