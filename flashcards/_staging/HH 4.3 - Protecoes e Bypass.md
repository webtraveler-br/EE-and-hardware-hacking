# HH 4.3 - Protecoes e Bypass

STM32 RDP (Readout Protection)?
?
Level 0: sem protecao. Level 1: JTAG desabilitado mas reversivel (pode ser bypassado com glitching). Level 2: permanente (irreversivel).

NXP LPC CRP (Code Read Protection)?
?
CRP1: parcial. CRP2: mais restritivo. CRP3: protecao total. Versoes antigas tem bypasses conhecidos.

O que e Secure Boot?
?
Verifica assinatura criptografica do firmware antes de executar. Se assinatura invalida, recusa boot. Protege contra firmware modificado.

O que e JTAG Lock/Fuse?
?
Bit OTP (One-Time Programmable) que desabilita JTAG permanentemente. Uma vez queimado, nao pode ser revertido — mas pode ser bypassado com fault injection.

Tecnicas de bypass de protecao?
?
Voltage glitching no momento do check, exploits no UART bootloader, cold boot attacks (congelar RAM para preservar dados), laser fault injection.
