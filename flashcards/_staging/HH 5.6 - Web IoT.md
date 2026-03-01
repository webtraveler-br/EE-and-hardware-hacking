# HH 5.6 - Hacking Web IoT

Web servers comuns em IoT?
?
lighttpd, uhttpd (OpenWrt), GoAhead, mini_httpd. Muitos rodam como root — command injection = root shell.

Vulnerabilidades web classicas em IoT?
?
Command injection em campos de diagnostico (ping, traceroute), credenciais hardcoded, directory traversal, buffer overflow via HTTP, CSRF, APIs sem autenticacao.

Como testar command injection?
?
Em campos de input (ping, DNS): tentar `; cat /etc/passwd` ou `| id` ou `$(whoami)`. Verificar se a resposta contem output do comando.

Por que CGI e especialmente vulneravel em IoT?
?
Scripts CGI em `/cgi-bin/` frequentemente passam parametros HTTP diretamente para `system()` ou `popen()` sem sanitizacao.

Muitos endpoints IoT "protegidos" nao sao: por que?
?
"Protecao" muitas vezes e apenas redirect JavaScript no frontend. Testar endpoints diretos com curl sem cookie de autenticacao.
