# HH F.3 - Redes Industriais ICS e Modbus

O que sao sistemas SCADA?
?
Supervisores centrais que coletam dados e monitoram redes de PLCs industriais a distancia.

O que eh o protocolo Modbus TCP?
?
Protocolo industrial mestre-escravo legado que usa TCP porta 502 sem criptografia ou autenticacao nativa.

Qual a falha critica das redes DCS antigas?
?
Eram isoladas da internet (Air-Gapped). Com a conexao moderna de escritorios, ficaram extremamente vulneraveis a ataques externos.

Como ocorre um ataque de injeção em Modbus?
?
O invasor reenvia um pacote Modbus gravado ordenando que uma bomba hidraulica aumente a pressao, e a maquina obedece cegamente.

O que é a segmentacao de Purdue Model?
?
Um modelo de rede para fabricas que separa a rede de escritorio da rede de chao de fabrica usando multiplos firewalls.
