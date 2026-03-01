# HH 6.3 - WiFi e Zigbee IoT

Ataques WiFi especificos para IoT?
?
WPS bruteforce, deauth → Evil Twin → captura de credenciais, interceptacao de firmware OTA (update sem TLS), credenciais WiFi em plaintext na flash.

O que e Zigbee?
?
IEEE 802.15.4, rede mesh, 2.4 GHz. Usado em automacao residencial (Philips Hue, SmartThings). Baixo consumo, baixa velocidade.

Vulnerabilidade classica do Zigbee?
?
Network key frequentemente transmitida em plaintext durante o join de novos dispositivos. Quem sniffa nesse momento obtem a chave de toda a rede.

Ferramentas Zigbee?
?
KillerBee (framework Python), ApiMote/RZUSBstick (hardware sniffer), TI SmartRF Packet Sniffer.

Ferramentas WiFi?
?
aircrack-ng suite (monitor mode, deauth, crack), bettercap (MITM), wifite (automacao), hostapd (Evil Twin).
