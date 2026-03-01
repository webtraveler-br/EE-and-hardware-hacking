# HH 7.2-7.3 - Fault Injection e Pratica SCA

O que e fault injection (glitching)?
?
Perturbacao intencional para causar erro na CPU. Tipos: voltage glitch (abaixar VCC momentaneamente), clock glitch (pulso de clock extra), EM fault (pulso eletromagnetico local).

Objetivo do glitching?
?
Pular instrucoes criticas: verificacao de senha, check de secure boot, validacao de assinatura. Um glitch no momento certo faz a CPU "pular" o branch de verificacao.

Parametros criticos do glitch?
?
Offset (quando apos o trigger), width (duracao do pulso), amplitude. Descobertos por varredura sistematica (sweep).

Diferenca entre voltage glitch e clock glitch?
?
Voltage: baixar VCC por nanosegundos usando MOSFET (crowbar). Clock: injetar pulso extra no CLK para a CPU executar instrucao incorreta.

Correlacao em DPA: o que se calcula?
?
Correlacao de Pearson entre modelo de consumo (Hamming weight da saida da S-box com hipotese de chave) e traces reais. Chave correta tem correlacao mais alta.
