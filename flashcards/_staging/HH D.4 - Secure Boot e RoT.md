# HH D.4 - Secure Boot Avancado e RoT

Hardware Root of Trust (RoT): localidade?
?
Implementado em areas em que ate a CPU base nao pode reescrever: ROMs (Read-only Mask Memory), Fuses de Polisilicio Fisico e CIs especializados TPM imutaveis.

Chain of Trust flow: Bootloader Bootrom ROM verificator?
?
Boot ROM (gravação de Fabrica inviolaável e nao atualizavel) analisa a assinatura do First/Secondary Stage Loader → Verifica Kernel → Verifica Drivers (Trusted App Space).

Bypass de Secure Boot no Nintendo Switch (Fusee Gelee)?
?
Falha de buffer overflow acionavel exatamente no Boot ROM via DMA mal formado de USB stack Recovery do Tegra X1. Incorrigivel para o fabricante ate troca fisica de chips nas lojas.

Downgrade attack no Secure Boot?
?
Sempre tenta retornar o firmware ativo a uma versao anterior antiga que tinha uma assinatura valida mas foi depreciada por conter Zero-Days pubicamente discorridos ou falhas conhecidas de jailbreak.

Anti-rollback Monotonic Counter approach?
?
Defesa estrita contra downgrades: queima-se fuses irreversiveis (+1) no die para cada major release. Se a ROM detectar contador = 4 e boot = 3, trava irremediavelmente (bricked state) independentemente da assinatura ser RSA valido de fabrica.
