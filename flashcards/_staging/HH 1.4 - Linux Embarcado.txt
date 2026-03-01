# HH 1.4 - Linux Embarcado

Filesystem Hierarchy Standard: diretorios essenciais?
?
/bin (binarios), /etc (configuracao), /dev (dispositivos), /proc (info do kernel), /sys (hardware), /tmp (temporarios), /var (logs).

O que e BusyBox?
?
Um unico binario que implementa centenas de comandos Unix (ls, cat, sh, wget, etc.). Padrao em Linux embarcado — economiza espaco.

Comandos de recon apos obter shell?
?
`cat /etc/passwd` (usuarios), `cat /etc/shadow` (hashes), `uname -a` (kernel), `ps aux` (processos), `netstat -tlnp` (portas), `cat /proc/mtd` (particoes flash).

Como encontrar binarios SUID?
?
`find / -perm -4000 -type f 2>/dev/null`. Binarios SUID rodam com permissoes do dono (geralmente root) — potencial escalacao de privilegio.

Como procurar senhas hardcoded em firmware?
?
`grep -r "password\|passw\|secret\|key\|admin" /etc/` ou `strings firmware.bin | grep -i pass`.

O que e /proc/cmdline?
?
Argumentos passados ao kernel no boot. Pode revelar console serial (console=ttyS0,115200), particoes, e opcoes de debug.

Init systems em embarcados?
?
SysV init (/etc/init.d/ — scripts de inicializacao), systemd (moderno), OpenRC. Verificar quais servicos iniciam automaticamente.
