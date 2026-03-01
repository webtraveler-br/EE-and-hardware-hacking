# HH 1.5 - Datasheets

Estrutura tipica de um datasheet?
?
Features → Pinout → Block Diagram → Electrical Characteristics → Registers → Timing Diagrams → Package.

Secoes criticas para HH num datasheet?
?
Pinout (identificar UART/JTAG), Memory Map, Boot Configuration, Debug Interface.

Parametros eletricos essenciais a verificar?
?
VCC (alimentacao), VIH/VIL (limiares logicos), IOH/IOL (corrente de saida), velocidade maxima de clock.

Como ler timing diagrams?
?
Eixo X = tempo, eixo Y = nivel logico. Setas indicam relacoes causais (ex: CS cai → clock comeca → dados validos).

Flash W25Q128: comando de leitura?
?
Opcode 0x03 + 3 bytes de endereco → dados fluem pelo MISO. CS deve estar LOW durante toda a operacao.
