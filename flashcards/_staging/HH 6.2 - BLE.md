# HH 6.2 - BLE Hacking

Stack BLE?
?
Physical → Link Layer → L2CAP → ATT/GATT → Profiles. GATT e a camada mais relevante para HH.

GATT: estrutura?
?
Services → Characteristics → Descriptors. Cada characteristic tem UUID, valor, e permissoes (read/write/notify).

O que e BLE advertising?
?
Dispositivos BLE fazem broadcast de presenca e capabilities. Visivel para qualquer scanner — revela informacoes antes mesmo de conectar.

Ferramentas BLE?
?
`bluetoothctl`, `hcitool lescan`, `gatttool`, `bettercap` (ble.recon), Wireshark com perfil BLE.

Ataques BLE comuns?
?
Sniffing (trafego sem criptografia), MITM, replay de comandos, brute-force do pairing PIN (6 digitos = 1M combinacoes).
