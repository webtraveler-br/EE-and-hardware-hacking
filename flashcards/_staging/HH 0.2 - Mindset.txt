# HH 0.2 - Mindset de Hardware Hacking

5 superficies de ataque de hardware?
?
Fisica (abrir, conectar), interfaces de debug (UART/JTAG), firmware (extrair e analisar), comunicacao wireless (RF/WiFi/BLE), supply chain (comprometimento no fabrico).

O que e STRIDE?
?
Modelo de ameacas: Spoofing, Tampering, Repudiation, Information Disclosure, Denial of Service, Elevation of Privilege. Aplicado a cada superficie do dispositivo.

Seguranca por obscuridade vs seguranca por design?
?
Obscuridade: esconder detalhes (firmware criptografado, pads escondidos) — quebra quando alguem descobre. Design: protecao resistente mesmo com detalhes publicos (secure boot, crypto forte).

Por que fabricantes deixam interfaces de debug habilitadas?
?
Usam durante desenvolvimento/producao para teste e debug. Frequentemente esquecem de desabilitar na versao final — "ninguem vai abrir o dispositivo."

O que e um threat model?
?
Documento que mapeia: superficie de ataque, vetor, probabilidade e impacto. Tabela com UART, firmware, WiFi, etc. para priorizar ataques.
