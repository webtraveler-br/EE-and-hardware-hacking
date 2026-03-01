# HH 3.1 - Exploracao UART

Conexao UART: qual fio vai onde?
?
TX do dispositivo → RX do adapter. RX do dispositivo → TX do adapter. GND com GND. Conexao CRUZADA.

Ferramentas de terminal serial?
?
minicom, screen (`screen /dev/ttyUSB0 115200`), picocom, PuTTY. Todas fazem o mesmo — abrir comunicacao serial.

Primeiros comandos apos obter shell UART?
?
`id` (quem sou?), `whoami`, `cat /etc/shadow` (hashes), `ps aux` (processos), `netstat -tlnp` (portas), `mount` (particoes).

Erro #1 em UART?
?
TX/RX invertidos. Se nada aparece, troque os fios. E o problema mais comum.

Level mismatch em UART: o que acontece?
?
Conectar adapter de 5V em dispositivo de 3.3V pode queimar o chip do dispositivo. Sempre verificar tensao ANTES de conectar.

O que e U-Boot e quais comandos uteis?
?
Bootloader universal para Linux embarcado. `printenv` (variaveis de ambiente), `md` (memory dump), `tftpboot` (carregar imagem via rede), `setenv` (modificar variaveis).

Como interromper o boot em U-Boot?
?
Apertar tecla (geralmente Enter, Esq, ou qualquer tecla) durante o countdown no inicio do boot. Permite acesso ao prompt U-Boot.
