# HH 6.1 - RF e SDR

Bandas ISM comuns para IoT?
?
433 MHz (controles remotos, sensores), 868/915 MHz (LoRa, Zigbee sub-GHz), 2.4 GHz (WiFi, BLE, Zigbee).

O que e SDR?
?
Software Defined Radio — radio onde modulacao/demodulacao e feita em software. Permite capturar e analisar qualquer sinal RF.

Hardware SDR acessivel?
?
RTL-SDR (~R$60, so recebe), HackRF One (~R$250, TX+RX), YARD Stick One (sub-GHz, otimo para 433/915 MHz).

Modulacoes comuns em IoT?
?
OOK (On-Off Keying — o mais simples, controles remotos), ASK, FSK, LoRa (chirp spread spectrum).

O que e um replay attack RF?
?
Capturar transmissao RF legítima e retransmitir. Funciona se o protocolo nao usa rolling code ou challenge-response. Comum em controles de garagem antigos.

Software para analise RF?
?
GNURadio (flow graphs), Universal Radio Hacker/URH (decodificacao), GQRX (espectro em tempo real), SDR# (Windows).
