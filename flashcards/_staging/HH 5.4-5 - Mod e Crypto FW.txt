# HH 5.4-5.5 - Modificacao e Firmware Criptografado

Processo de modificacao de firmware?
?
Extrair (binwalk) → modificar (editar arquivos no filesystem) → reempacotar (mksquashfs) → flash ou emular.

Modificacoes comuns em firmware?
?
Habilitar telnet/SSH, trocar senha root, inserir reverse shell no boot (/etc/init.d/), adicionar backdoor persistente.

O que verificar apos reempacotar?
?
Recalcular checksums/CRC (muitos firmwares verificam integridade). Se firmware e assinado, precisa bypass do secure boot.

Indicadores de firmware criptografado?
?
Entropia proxima a 1.0 uniforme, sem strings legiveis, sem magic bytes reconheciveis, `file` retorna "data".

Onde encontrar a chave de decriptacao?
?
Bootloader (precisa decriptar para executar), OTP fuses, TEE. Alternativas: versao antiga sem criptografia, dump de RAM via JTAG durante boot.
