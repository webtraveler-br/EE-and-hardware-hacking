# HH D.1 - ARM TrustZone Arquitetura

ARM TrustZone: isolamento hardware?
?
Divide a CPU e memoria em "Normal World" (rodando Linux/Android) e "Secure World" (rodando o TEE OS).

Niveis de excecao (Exception Levels) no ARMv8?
?
EL0 (Userspace), EL1 (Kernel), EL2 (Hypervisor), EL3 (Secure Monitor). O Secure World tem seus proprios EL0-S e EL1-S paralelos ao Normal World.

SMC (Secure Monitor Call): funcao?
?
Instrucao assembly que transfere o controle do Normal World para o Monitor (EL3). O EL3 gerencia as concessoes de entrada no Secure World de forma arbitrada.

TEE OS (Trusted Execution Environment): funcao?
?
Sistema operacional hyper-seguro (ex: OP-TEE, QSEE, Kinibi) rodando no Secure World. Focado quase inteiramente em seguranca de transacoes, biometria e armazenamento criptografico.

Trusted Applications (TAs) na arquitetura TrustZone?
?
Pequenos aplicativos em C/C++ rodando no nivel de usuario do TEE (EL0-S). Realizam funcoes isoladas (ex: validacao de impressao digital, DRM).

SMC handler no EL3: risco de seguranca central?
?
Recebe argumentos diretamente do SO vulneravel (Normal World). Falhas na validacao desses "inputs crús" por parte do SMC handler comprometem a CPU inteira.
