# HH 5.3 - Emulacao de Firmware e QEMU

O que é o QEMU (Quick Emulator)?
?
Emulador e virtualizador open-source que traduz instrucoes de um CPU alvo (ex: ARM, MIPS) para as instrucoes nativas da maquina host (ex: x86). Essencial para rodar firmwares extraídos sem ter o hardware fisico MIPS na bancada.

Diferença entre User-Mode e System-Mode Emulation?
?
User-Mode executa apenas um unico binário isolado (ex: rodar `/bin/busybox` compilado pra ARM no seu Linux x86). System-Mode emula a maquina inteira (CPU, RAM, Discos, perifericos I/O), bootando um kernel completo como se fosse o roteador fisico real.

Dificuldades do QEMU system-mode para IoT?
?
Para funcionar, não basta apenas o binário; é preciso o kernel compativel, a arvore de dispositivos (Device Tree), e mapear milimetricamente onde a memoria RAM e Flash ficam naquela placa especifica.

Emulacao de NVRAM (Non-Volatile RAM)?
?
Muitos firmwares de roteadores travam no boot pois tentam ler configuracoes numa memoria NVRAM fisica que o QEMU nao possui nativo. Soluciona-se injetando bibliotecas falsas como a `libnvram` via `LD_PRELOAD` para simular as respostas ao firmware.

Framework FIRMADYNE?
?
Orquestrador automatizado focado em emular roteadores e cameras IoT. Ele extrai o rootfs do firmware, altera as particoes, configura o kernel MIPS emulado do QEMU e tenta levantar a interface Web nativa do sistema pra voce atacar na sua rede local.

Como emular binarios ARM isolados via Chroot User-Mode?
?
Apos extrair o root directory do firmware (`_extracted`), voce copia o binario estatico `qemu-arm-static` para dentro da pasta raiz dele e usa o comando `chroot` p/ trancar o shell e fingir que seu bash agora é aquele roteador ARM restrito executandos binários inativos.
