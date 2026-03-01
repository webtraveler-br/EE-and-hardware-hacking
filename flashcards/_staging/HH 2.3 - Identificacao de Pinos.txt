# HH 2.3 - Identificacao de Pinos com Multimetro

Procedimento para identificar UART com multimetro?
?
1) Continuidade: encontre GND (conecta ao plano de terra). 2) Tensao DC: VCC = 3.3V constante. 3) TX = oscila durante boot. 4) RX = estavel em HIGH (pull-up).

Modo continuidade do multimetro: para que serve em HH?
?
Verificar quais pinos estao conectados entre si. Bipe = conexao. Usado para mapear GND, VCC, e trilhas entre test points e chips.

UART TX idle: qual nivel logico?
?
HIGH (3.3V ou 5V). TX fica em HIGH quando nao ha dados. Durante boot, oscila (dados transmitidos).

Baud rates mais comuns para testar?
?
115200 (mais comum), 9600, 57600, 38400, 19200, 230400, 460800. Sempre comecar por 115200.

Como saber se acertou o baud rate?
?
Se o texto na tela e legivel (ASCII). Se aparece "lixo" (caracteres estranhos), o baud rate esta errado.
