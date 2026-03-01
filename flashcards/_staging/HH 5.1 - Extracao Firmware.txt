# HH 5.1 - Extracao de Firmware

Binwalk flags essenciais?
?
`-e` (extrair), `-E` (entropia — gera grafico), `-A` (buscar opcodes), `-W` (diff entre firmwares).

Filesystems embarcados comuns?
?
SquashFS (read-only, comprimido, mais comum), JFFS2 (read-write, flash), CramFS (read-only), UBIFS (NAND moderno), YAFFS2 (NAND antigo).

Estrutura tipica de firmware?
?
Header → bootloader → kernel → rootfs (filesystem) → config → art (calibracao wireless). binwalk revela cada camada.

O que a analise de entropia revela?
?
Alta entropia (~1.0) = comprimido ou criptografado. Baixa = dados raw/strings. Queda abrupta = fim de secao comprimida. Entropia maxima uniforme = provavelmente criptografado (nao apenas comprimido).

Como distinguir compressao de criptografia?
?
Compressao: binwalk detecta headers (gzip, LZMA). Criptografia: nenhum header reconhecido, entropia maxima uniforme. Verificar se fabricante menciona "encrypted firmware".
