# HH 1.1 - Eletronica Digital para HH

Tensoes padrao em embarcados?
?
1.8V, 3.3V, 5V. Conectar 5V em pino de 3.3V pode queimar o chip permanentemente.

Niveis logicos TTL?
?
LOW: 0 a 0.8V. HIGH: 2 a 5V. Zona indefinida entre 0.8V e 2V.

Niveis logicos CMOS?
?
LOW: 0 a 1/3 Vcc. HIGH: 2/3 Vcc a Vcc. Mais limpo que TTL.

Pull-up resistor: para que serve?
?
Mantem pino em HIGH quando nao ha sinal ativo. Sem pull-up, pino flutuante le valores aleatorios. Valor tipico: 4.7k a 10k$\Omega$.

Por que todo CI tem capacitor de 100nF perto dos pinos VCC?
?
Capacitor de desacoplamento — filtra ruido de alta frequencia na alimentacao. Sem ele, o CI pode ter mau funcionamento por ruido.

O que e level shifting?
?
Converter niveis logicos entre tensoes diferentes. Ex: 5V ↔ 3.3V. Metodos: divisor resistivo (unidirecional), MOSFET (bidirecional), CI dedicado (TXS0108E).

Por que conectar GND entre dispositivos e obrigatorio?
?
GND e a referencia comum de tensao. Sem GND compartilhado, os sinais nao tem referencia e a comunicacao nao funciona (ou danifica componentes).
