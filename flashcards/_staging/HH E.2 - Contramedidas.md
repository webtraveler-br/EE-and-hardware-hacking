# HH E.2 - Contramedidas e Bypass

Constant-Time evaluation de chaves?
?
Algoritmo de codificação onde os IFs e matrizes computam sem distinção do valor do operando: tempo de execução identico cega completamente um Timing-Attack (SPA).

Masking implementacao preventiva de chaves (AES)?
?
Desmembrar a chave/dados localmente em duas acões operadas individualmente com lixo (shares em bitwise XOR) impossibilitando predição unitária sem "observar" tudo ao extremo.

Shuffling operacional defletor?
?
Embaralha de forma pseudo-aleatoria a ordem que os bytes sofrem permutacao (S-box AES por exemplo) arruinando totalmente a previsao matematica da Corrrelation Analysis padrao.

Ataque Higher-Order em Masking (AES)?
?
Exploracao sistematica combinando o somatorio das falhas logicas em "Multiplos" pontos cronologicos simultaneamente na onda comutadora pra cancelar as shares matematicamente.

Dual-rail logic implementada diretamente em Hardware?
?
Uso intensivo de "Portas Complementares Físicas" operando em par invertido que estabilizam passivamente os decibeis em constante neutra contra Power Analysis independente da flag do dado.
