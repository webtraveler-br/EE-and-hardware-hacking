# HH 3.5 - U-Boot e Bootloaders

Comandos U-Boot essenciais?
?
`printenv` (variaveis), `setenv bootargs "..."` (alterar args do kernel), `md 0x80000000 100` (memory dump), `tftpboot` (boot via rede), `saveenv` (salvar mudancas).

Variaveis U-Boot criticas para seguranca?
?
`bootcmd` (comando executado no autoboot), `bootargs` (parametros do kernel, pode conter console=), `bootdelay` (tempo para interromper o boot).

Como obter shell root via U-Boot?
?
Alterar `bootargs` para incluir `init=/bin/sh` — o kernel inicia diretamente num shell root, bypassando o init normal e autenticacao.

Como interromper o autoboot?
?
Pressionar tecla (Enter, Esc, ou qualquer) durante o countdown. `bootdelay` define o tempo. Se 0 ou negativo, pode ser impossivel interromper.

Bypass de senha U-Boot?
?
Alterar `bootdelay` via EEPROM, encontrar senha no dump da flash, ou usar glitching para pular a verificacao.
