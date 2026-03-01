# HH 1.3 - Protocolos Seriais Teoria

UART: parametros padrao?
?
Assincrono, 2 fios (TX/RX), 115200 baud, 8N1 (8 data bits, no parity, 1 stop bit). Sem clock — dispositivos devem concordar no baud rate.

Frame UART?
?
Idle HIGH → start bit (LOW) → 8 data bits (LSB first) → stop bit (HIGH). Cada byte precisa de 10 bits no total.

SPI: fios e funcao de cada um?
?
MOSI (Master Out Slave In), MISO (Master In Slave Out), SCLK (clock do master), CS/SS (chip select, ativo LOW). Full-duplex, sincrono.

SPI: modos 0-3?
?
Definidos por CPOL (polaridade do clock) e CPHA (fase). Modo 0 (CPOL=0, CPHA=0) e o mais comum. O modo deve coincidir entre master e slave.

I2C: como funciona o enderecamento?
?
START condition → 7 bits de endereco + 1 bit R/W → ACK do slave → dados → STOP. Ate 127 dispositivos no barramento.

I2C: START e STOP conditions?
?
START: SDA cai enquanto SCL esta HIGH. STOP: SDA sobe enquanto SCL esta HIGH. Fora dessas condicoes, SDA so muda quando SCL esta LOW.

Quando usar UART vs SPI vs I2C no contexto de HH?
?
UART: console de debug, shell root. SPI: ler/escrever flash. I2C: ler EEPROMs com configuracoes, chaves, credenciais.

Como calcular baud rate a partir do bit duration?
?
$baud = 1 / T_{bit}$. Ex: $T_{bit} = 8{,}68\,\mu$s → $1/8{,}68 \times 10^{-6} \approx 115200$ baud.
