# Módulo 9: HTB CPTS — Certified Penetration Testing Specialist

> **Sobre este módulo**: Path completo de preparação para a certificação HTB CPTS. Cobre o ciclo
> completo de pentest: reconhecimento, enumeração, exploração web e de rede, pivoting,
> Active Directory, escalação de privilégios e relatórios profissionais. Baseado nos 28 módulos
> do Penetration Tester Job-Role Path do HTB Academy.
>
> **Ferramentas**: Kali Linux, Nmap, Burp Suite, Metasploit, ffuf, SQLMap, Bloodhound, Chisel, Ligolo
>
> **Pré-requisitos**: [Módulo 3 — Digital + Embarcados](../03-digital-embedded/README.md), [Módulo 7 — HH Básico](../07-hardware-hacking/README.md)
>
> **Conexões com outros módulos**:
> - **Base de**: Redes (Módulo 3), Protocolos (Módulo 7)
> - **Alimenta**: Pentest de hardware com contexto de rede
> - **Prática real**: HTB Academy labs, Hack The Box machines
> - **Segurança**: Complementa HH com pentest de software/rede
>
> [Voltar ao Índice](../README.md)

---

## Visão Geral

| Fase | Módulos | Foco | Horas |
|------|---------|------|-------|
| **Fundamentos** | 9.01–9.02 | Processo e setup | ~8h |
| **Reconhecimento** | 9.03–9.06 | Enumeração e footprinting | ~16h |
| **Exploração Inicial** | 9.07–9.12 | Shells, senhas, pivoting | ~28h |
| **Active Directory** | 9.13 | AD enumeration & attacks | ~10h |
| **Web Attacks** | 9.14–9.24 | Proxies, injection, fuzzing | ~42h |
| **Privilege Escalation** | 9.25–9.26 | Linux e Windows privesc | ~10h |
| **Finalização** | 9.27–9.28 | Relatórios e lab final | ~6h |

### Prontidão por Módulo

> ⚠️ **Módulo em planejamento** — absorção será definida quando o conteúdo for desenvolvido.

| Módulo | Tema | Absorção |
|--------|------|----------|
| 9.01 | Processo de Pentest | 📚 Estudo Prévio |
| 9.02 | Fundamentos e Setup | 🔶 Projeto Ponte — configurar lab |
| 9.03 | Enumeração de Rede com Nmap | 🔶 Projeto Ponte — scan de lab |
| 9.04 | Footprinting | 🔶 Projeto Ponte |
| 9.05 | Coleta de Informação Web | 🔶 Projeto Ponte |
| 9.06 | Avaliação de Vulnerabilidades | 📚 Estudo Prévio |
| 9.07 | Transferência de Arquivos | ❄️ Cold Start OK |
| 9.08 | Shells e Payloads | 🔶 Projeto Ponte |
| 9.09 | Metasploit Framework | 🔶 Projeto Ponte |
| 9.10 | Ataques a Senhas | 🔶 Projeto Ponte |
| 9.11 | Ataque a Serviços Comuns | 🔶 Projeto Ponte |
| 9.12 | Pivoting e Tunneling | 📚 Estudo Prévio |
| 9.13 | Active Directory | 📚 Estudo Prévio |
| 9.14 | Web Proxies | 🔶 Projeto Ponte — Burp Suite |
| 9.15 | Fuzzing Web com Ffuf | 🔶 Projeto Ponte |
| 9.16 | Brute Force de Login | ❄️ Cold Start OK |
| 9.17 | SQL Injection | 🔶 Projeto Ponte |
| 9.18 | SQLMap | ❄️ Cold Start OK |
| 9.19 | Cross-Site Scripting (XSS) | 🔶 Projeto Ponte |
| 9.20 | File Inclusion (LFI/RFI) | 🔶 Projeto Ponte |
| 9.21 | Ataques via Upload | 🔶 Projeto Ponte |
| 9.22 | Command Injection | 🔶 Projeto Ponte |
| 9.23 | Ataques Web Avançados | 📚 Estudo Prévio |
| 9.24 | Ataque a Aplicações Comuns | 📚 Estudo Prévio |
| 9.25 | Escalação de Privilégios Linux | 🔶 Projeto Ponte |
| 9.26 | Escalação de Privilégios Windows | 🔶 Projeto Ponte |
| 9.27 | Documentação e Relatórios | ❄️ Cold Start OK |
| 9.28 | Ataque a Redes Corporativas | 🔴 Material Externo — lab HTB |

> **Legenda**: ❄️ Cards funcionam sozinhos · 🔶 Fazer projeto ANTES dos cards · 📚 Assistir vídeo/ler antes · 🔴 Material externo obrigatório

---

## Fase 1 — Fundamentos

### Módulo 9.01: Processo de Pentest
**Tempo: 3h**

> 🚧 Conteúdo será desenvolvido — baseado no módulo "Penetration Testing Process" do HTB Academy.
> Cobre: metodologia PTES, fases do pentest, escopo, regras de engajamento, aspectos legais.

### Módulo 9.02: Fundamentos e Setup
**Tempo: 5h**

> 🚧 Conteúdo será desenvolvido — baseado no módulo "Getting Started" do HTB Academy.
> Cobre: setup do ambiente (VM, VPN), ferramentas essenciais, primeiro target.

---

## Fase 2 — Reconhecimento

### Módulo 9.03: Enumeração de Rede com Nmap
**Tempo: 4h**

> 🚧 Baseado em "Network Enumeration with Nmap". Scans, scripts NSE, evasão de firewall.

### Módulo 9.04: Footprinting
**Tempo: 5h**

> 🚧 Baseado em "Footprinting". Enumeração de serviços: FTP, SMB, NFS, DNS, SMTP, IMAP, SNMP, MySQL, MSSQL, Oracle, IPMI.

### Módulo 9.05: Coleta de Informação Web
**Tempo: 4h**

> 🚧 Baseado em "Information Gathering - Web Edition". WHOIS, DNS, subdomínios, web crawling, tecnologias.

### Módulo 9.06: Avaliação de Vulnerabilidades
**Tempo: 3h**

> 🚧 Baseado em "Vulnerability Assessment". Scanners, Nessus, classificação de vulnerabilidades.

---

## Fase 3 — Exploração Inicial

### Módulo 9.07: Transferência de Arquivos
**Tempo: 3h**

> 🚧 Baseado em "File Transfers". Upload/download em Linux e Windows, métodos criativos.

### Módulo 9.08: Shells e Payloads
**Tempo: 5h**

> 🚧 Baseado em "Shells & Payloads". Reverse shells, bind shells, web shells, payloads customizados.

### Módulo 9.09: Metasploit Framework
**Tempo: 4h**

> 🚧 Baseado em "Using the Metasploit Framework". Módulos, exploits, post-exploitation, meterpreter.

### Módulo 9.10: Ataques a Senhas
**Tempo: 6h**

> 🚧 Baseado em "Password Attacks". Wordlists, hashcat, john, credential stuffing, pass-the-hash.

### Módulo 9.11: Ataque a Serviços Comuns
**Tempo: 5h**

> 🚧 Baseado em "Attacking Common Services". FTP, SMB, SQL, RDP, WinRM, etc.

### Módulo 9.12: Pivoting e Tunneling
**Tempo: 5h**

> 🚧 Baseado em "Pivoting, Tunneling, and Port Forwarding". SSH tunneling, chisel, ligolo, proxychains, double pivot.

---

## Fase 4 — Active Directory

### Módulo 9.13: Active Directory
**Tempo: 10h**

> 🚧 Baseado em "Active Directory Enumeration & Attacks". Kerberoasting, AS-REP Roasting, BloodHound, DCSync, Pass-the-Hash, Golden/Silver Ticket.

---

## Fase 5 — Web Attacks

### Módulo 9.14: Web Proxies
**Tempo: 3h**

> 🚧 Baseado em "Using Web Proxies". Burp Suite, ZAP, interceptação, repeater, intruder.

### Módulo 9.15: Fuzzing Web com Ffuf
**Tempo: 3h**

> 🚧 Baseado em "Attacking Web Applications with Ffuf". Directory/vhost/parameter fuzzing.

### Módulo 9.16: Brute Force de Login
**Tempo: 3h**

> 🚧 Baseado em "Login Brute Forcing". Hydra, formulários web, protocolos de rede.

### Módulo 9.17: SQL Injection
**Tempo: 4h**

> 🚧 Baseado em "SQL Injection Fundamentals". Union, blind, error-based, stacked queries.

### Módulo 9.18: SQLMap
**Tempo: 3h**

> 🚧 Baseado em "SQLMap Essentials". Automação de SQLi, tamper scripts, dump.

### Módulo 9.19: Cross-Site Scripting (XSS)
**Tempo: 3h**

> 🚧 Baseado em "Cross-Site Scripting (XSS)". Reflected, stored, DOM-based, filtro bypass.

### Módulo 9.20: File Inclusion (LFI/RFI)
**Tempo: 3h**

> 🚧 Baseado em "File Inclusion". LFI, RFI, log poisoning, wrappers PHP.

### Módulo 9.21: Ataques via Upload
**Tempo: 3h**

> 🚧 Baseado em "File Upload Attacks". Bypass de validação, web shells via upload.

### Módulo 9.22: Command Injection
**Tempo: 3h**

> 🚧 Baseado em "Command Injections". OS injection, filtro bypass, blind injection.

### Módulo 9.23: Ataques Web Avançados
**Tempo: 5h**

> 🚧 Baseado em "Web Attacks". IDOR, XXE, SSRF, HTTP verb tampering.

### Módulo 9.24: Ataque a Aplicações Comuns
**Tempo: 8h**

> 🚧 Baseado em "Attacking Common Applications". WordPress, Drupal, Tomcat, Jenkins, osTicket, GitLab, etc.

---

## Fase 6 — Privilege Escalation

### Módulo 9.25: Escalação de Privilégios Linux
**Tempo: 5h**

> 🚧 Baseado em "Linux Privilege Escalation". SUID, capabilities, cron, kernel exploits, sudo abuse.

### Módulo 9.26: Escalação de Privilégios Windows
**Tempo: 5h**

> 🚧 Baseado em "Windows Privilege Escalation". SeImpersonate, services, DLL hijacking, UAC bypass.

---

## Fase 7 — Finalização

### Módulo 9.27: Documentação e Relatórios
**Tempo: 2h**

> 🚧 Baseado em "Documentation & Reporting". Notetaking, screenshots, relatório executivo e técnico.

### Módulo 9.28: Ataque a Redes Corporativas
**Tempo: 4h**

> 🚧 Baseado em "Attacking Enterprise Networks". Lab final que simula o exame CPTS — pentest completo end-to-end.

---

## Se Você Travar

| Problema | Solução |
|----------|---------|
| Não consigo explorar o serviço | Volte ao footprinting — enumere melhor |
| Shell morre imediatamente | Tente shell diferente (bash, sh, python, powershell) |
| Não acho vetor de privesc | Use LinPEAS/WinPEAS, verifique SUID, cron, services |
| AD é confuso | Comece pelo BloodHound — visualize o path |
| Relatório ficou fraco | Siga o template do módulo 9.27, foque em impacto de negócio |

---

[Voltar ao Índice](../README.md)
