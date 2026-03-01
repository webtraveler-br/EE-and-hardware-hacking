# HH 7.1 - Side-Channel Attacks Teoria

Tipos de side-channel attack?
?
Timing (tempo de execucao varia com dados), power analysis (SPA/DPA), eletromagnetica (EM emanations), acustica, cache.

O que e SPA (Simple Power Analysis)?
?
Analise visual de UM trace de consumo de energia. Revela operacoes: multiplicacao consome mais que adicao → revela bits da chave RSA diretamente.

O que e DPA (Differential Power Analysis)?
?
Correlacao estatistica de MILHARES de traces de consumo para extrair chaves criptograficas. Mais poderoso que SPA, funciona mesmo com ruido.

Timing attack: como funciona?
?
Tempo de execucao varia dependendo dos dados. Ex: comparacao de senha byte-a-byte — cada byte correto adiciona tempo. Atacante mede tempo para descobrir cada byte.

Contramedidas contra side-channel?
?
Algoritmos constant-time, injecao de ruido no consumo, masking (randomizar valores intermediarios), blindagem EM.

O que e o ChipWhisperer?
?
Plataforma open-source para side-channel attacks e fault injection. FPGA Artix-7 + ADC + software Python. Padrao academico e profissional.
