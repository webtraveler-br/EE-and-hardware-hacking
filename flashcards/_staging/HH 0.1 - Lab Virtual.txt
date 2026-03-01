# HH 0.1 - Lab Virtual

Hypervisor tipo 1 vs tipo 2?
?
Tipo 1 (bare-metal): roda direto no hardware (VMware ESXi, Hyper-V). Tipo 2 (hosted): roda sobre um OS (VirtualBox, VMware Workstation).

Para que serve /dev no Linux?
?
Contem arquivos de dispositivos — cada hardware (disco, serial, USB) e representado como um arquivo em /dev.

Para que serve /proc no Linux?
?
Filesystem virtual com informacoes do kernel e processos em tempo real. Ex: /proc/cpuinfo, /proc/mtd (particoes flash).

Para que serve /sys no Linux?
?
Interface para configurar e ler informacoes de hardware e drivers do kernel.

Como montar uma imagem de disco?
?
`mount -o loop imagem.bin /mnt/ponto_de_montagem`. Para NAND/MTD: extrair com binwalk primeiro.

Ferramentas essenciais do toolkit HH?
?
binwalk (firmware), flashrom (flash SPI), ghidra (reversing), qemu (emulacao), minicom/picocom (serial), sigrok/pulseview (analisador logico).
