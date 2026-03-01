# HH B.1-B.4 - Barramentos de Alta Velocidade

Acima de ~50 MHz, fios se comportam como que?
?
Linhas de transmissao. Descasamento de impedancia causa reflexoes que corrompem sinais. Precisa de terminacao.

Tempo de propagacao em PCB?
?
~1 ns por 15 cm de trilha.

O que e DMA (Direct Memory Access)?
?
Dispositivo PCIe le/escreve RAM do host SEM envolver a CPU. Projetado para performance, explorado para ler toda a memoria.

DMA attack: como funciona?
?
Inserir placa PCIe/Thunderbolt maliciosa (FPGA) → ler TODA a RAM → extrair chaves BitLocker, senhas, tokens. Ferramenta: PCILeech.

O que e IOMMU (VT-d)?
?
Unidade que restringe quais enderecos de memoria cada dispositivo PCIe pode acessar. Defesa contra DMA attacks.

O que e BadUSB?
?
Reprogramar firmware do controlador USB para mudar identidade. Ex: pen drive vira teclado e digita comandos maliciosos.

Cold Boot Attack?
?
RAM mantem dados por segundos apos perder energia. Resfriar RAM (spray/nitrogenio) → dados sobrevivem minutos → mover para outro PC → dump → extrair chaves.

USB HID injection: por que funciona em qualquer OS?
?
Dispositivo se apresenta como teclado. Todo OS aceita teclados automaticamente sem driver. Pode digitar comandos em velocidade sobre-humana.
