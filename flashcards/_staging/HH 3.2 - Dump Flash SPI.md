# HH 3.2 - Dump de Flash SPI

Pinout do SOIC-8 flash SPI?
?
Pin 1 = CS, 2 = MISO (DO), 3 = WP, 4 = GND, 5 = MOSI (DI), 6 = CLK, 7 = HOLD, 8 = VCC.

Comando flash SPI para leitura?
?
Opcode 0x03 + 3 bytes de endereco. Dados fluem pelo MISO enquanto CS esta LOW.

Como fazer dump com flashrom e CH341A?
?
`flashrom -p ch341a_spi -r dump.bin`. Fazer 2+ dumps e comparar hash para verificar integridade.

Como verificar integridade do dump?
?
Fazer multiplos dumps e comparar: `md5sum dump1.bin dump2.bin`. Se hash diferente, ha erro de leitura — verificar conexoes.

O que e SOP8 clip?
?
Garra que abraca o chip SOIC-8 sem dessoldar. Permite leitura in-circuit. Alternativa: dessoldar o chip com estacao de ar quente.

Apos obter o dump, qual o proximo passo?
?
`binwalk -e dump.bin` para extrair componentes (bootloader, kernel, filesystem). `strings dump.bin | grep -i pass` para credenciais rapidas.
