# Módulo 7 — Hardware Hacking: Fase 8 — Integração, Projetos e Portfólio
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo HH.8.1: CTF de Hardware

### Plataformas de CTF

- Microcorruption >> CTF de RE com {{MSP430}} (assembly embarcado, bypass de autenticação)
- exploit.education >> {{Phoenix}}: exploitation ARM (stack overflow, format string)
- RHme challenges >> Challenges sobre {{hardware real}} (side-channel, fault injection, RE)

### Categorias de CTF hardware

- Firmware RE >> Analisar binário ARM/MIPS → encontrar {{flag}} escondida (Ghidra, strings, XREFs)
- Crypto >> Side-channel, timing attack, {{weak algorithms}} (ECB mode, XOR, ROT13)
- Binary exploitation >> Buffer overflow em ARM/MIPS → {{controlar PC}} → shellcode/ROP
- Protocol analysis >> Decodificar tráfego {{serial/I2C/SPI}} capturado → extrair dados

### Ferramentas rápidas para CTF

- CyberChef >> Conversões e decodings {{online}} (Base64, XOR, hex, rotação)
- pwntools >> Framework Python para {{exploit development}} (send/recv, packing, ROP)
- ROPgadget >> Encontra {{gadgets ROP}} em binários (para exploitation ARM/MIPS)

### Drill — CTF #[[Drill]]

- Challenge mostra assembly MSP430 com `cmp` antes de `jz` → ataque = >> {{Patch do jump}} ou encontrar valor correto
- Binário ARM com `strcpy(buf, argv[1])` e buf de 64 bytes → vuln = >> {{Buffer overflow}}
- Dados hexadecimais parecem Base64 → ferramenta rápida = >> {{CyberChef}} (tenta múltiplos decodings)
- Precisa de exploit framework em Python → usar = >> {{pwntools}} (send, recv, p32, p64)

---

## Módulo HH.8.2: Metodologia de Pentest IoT

### OWASP IoT Top 10

- I1 >> {{Weak, Guessable, or Hardcoded Passwords}}
- I2 >> {{Insecure Network Services}}
- I3 >> {{Insecure Ecosystem Interfaces}} (APIs, web, mobile)
- I4 >> {{Lack of Secure Update Mechanism}}
- I5 >> {{Use of Insecure or Outdated Components}}

### Fases do pentest IoT

- Fase 1 >> {{Reconhecimento}} (OSINT, FCC ID, datasheets, CVE research)
- Fase 2 >> {{Análise de PCB}} (componentes, interfaces, test points)
- Fase 3 >> {{Firmware}} (extração, Binwalk, análise de filesystem)
- Fase 4 >> {{Análise estática}} (Ghidra, busca de vulns em binários)
- Fase 5 >> {{Emulação}} (QEMU/FAT, teste dinâmico)
- Fase 6 >> {{Exploração}} (ativar vulns encontradas, PoC)
- Fase 7 >> {{Pós-exploração}} (impacto, persistência, exfiltração)

### Relatório profissional

- Executive Summary >> 1 página, {{não-técnico}}, para gestores/diretoria
- Technical Findings >> Detalhes com {{screenshots, PoC code, reprodução}}
- CVSS score >> Classificação padronizada de {{severidade}} (0.0 a 10.0)
- Remediation >> Recomendações de {{mitigação}} para cada achado

### Responsible Disclosure

- Responsible disclosure >> Reportar vuln ao fabricante com {{prazo}} para correção (tipicamente 90 dias)
- Full disclosure >> Publicar vuln {{sem avisar}} o fabricante (controverso, mas legal no Brasil)
- CVE >> Identificador {{público}} único para cada vulnerabilidade (ex: CVE-2024-12345)

### Drill — Pentest IoT #[[Drill]]

- Roteador com admin/admin → OWASP IoT = >> {{I1}} (Weak/Hardcoded Passwords)
- Firmware update via HTTP sem assinatura → OWASP IoT = >> {{I4}} (Lack of Secure Update)
- Encontrou vuln crítica → quer CVE → processo = >> {{Responsible disclosure}} ao fabricante + solicitar CVE (MITRE/NVD)
- Relatório para diretoria → seção = >> {{Executive Summary}} (1 página, não-técnico, impacto de negócio)
- Achado com PoC → CVSS 9.8 → classificação = >> {{Crítico}}

---

## Módulo HH.8.3: Portfólio e Próximos Passos

### Construção de portfólio

- GitHub >> Repositório com `/writeups`, `/tools`, `/reports`, {{`README.md`}}
- Blog >> Writeups detalhados (Hugo/Jekyll/Medium) — demonstra {{comunicação técnica}}
- YouTube >> Demonstrações visuais de {{ataques}} e análises (diferencial forte)

### Certificações relevantes

- TCM PNPT >> Pentest prático (inclui {{IoT}} e hardware)
- OSCP >> Offensive Security — foco em {{exploitation}} e pentest
- Hardware Hacking Expert (TrainSec) >> Específica para {{hardware}} security

### Comunidades e conferências

- DEF CON >> {{Hardware Hacking Village}} (Las Vegas, maior conferência de hacking)
- H2HC >> Hackers to Hackers Conference ({{São Paulo}}, maior do Brasil)
- Roadsec >> Conferência itinerante de segurança ({{Brasil}})
- Bug bounty >> HackerOne e Bugcrowd têm programas de {{IoT}}

### Drill — Portfólio #[[Drill]]

- Diferencial forte no portfólio = >> {{Writeups detalhados}} + ferramentas open-source + relatórios de pentest
- Maior conferência de hacking do Brasil = >> {{H2HC}} (Hackers to Hackers Conference)
- Quer certificação prática de pentest = >> {{PNPT}} (TCM) ou {{OSCP}} (OffSec)
- Quer programas de bug bounty IoT = >> {{HackerOne}} e Bugcrowd
