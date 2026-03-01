# HH 2.2 - Analise Visual de PCB

Como identificar o SoC numa PCB?
?
Maior chip, geralmente BGA (sem pernas visiveis), posicionado no centro da placa. Part number no topo.

Como identificar flash SPI?
?
Chip SOIC-8 (8 pinos), geralmente perto do SoC. Part numbers comuns: W25Q (Winbond), MX25L (Macronix), GD25Q (GigaDevice).

Padrao de 4 pinos alinhados numa PCB?
?
Provavelmente UART: GND, VCC, TX, RX. Procurar headers nao populados ou test points.

Padrao de 10 ou 20 pinos numa PCB?
?
Provavelmente JTAG (ARM standard 10-pin ou 20-pin). Verificar se conecta ao SoC.

Encapsulamentos comuns: QFP, BGA, SOIC?
?
QFP: pinos nos 4 lados (MCUs). BGA: pinos embaixo (SoCs). SOIC: pinos em 2 lados (flash, EEPROMs, reguladores). SOT-23: 3 pinos (transistores, reguladores).

Como identificar um part number desconhecido?
?
Buscar o numero no topo do CI no Google ou Octopart. Alguns fabricantes usam markings proprietarios — procurar em forums.
