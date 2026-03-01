# HH 3.3 - EEPROM I2C

EEPROMs comuns e capacidades?
?
AT24C02 (256 bytes), AT24C256 (32 KB). Armazenam configuracao, chaves, licencas, numeros de serie.

Enderecos I2C tipicos de EEPROM?
?
0x50 a 0x57 (base 0x50, variando pelos pinos A0-A2 do chip).

Como escanear barramento I2C no Linux?
?
`i2cdetect -y 1` — lista todos os enderecos com dispositivos respondendo.

Protocolo I2C resumido?
?
START → endereco 7-bit + R/W bit → ACK → dados → ACK → STOP. Tudo em 2 fios (SDA + SCL).

O que fabricantes guardam em EEPROMs?
?
Numeros de serie, chaves de licenca, configuracoes de fabrica, calibracao de sensores, credenciais WiFi, MACs. Muitas vezes sem protecao.
