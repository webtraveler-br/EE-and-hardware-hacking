# Guia de Absorção: Cold Start, Projetos Ponte e Warm Start

**Data:** Março 2026
**Base:** Auditoria de Suficiência (Frio/Quente por módulo) + Classificação IA de Projetos
**Total:** 128 módulos × 7 pilares | ~154 projetos

---

## Impacto Estimado — Por que seguir este guia?

### Absorção de Conhecimento

Cada módulo tem uma nota de absorção **a frio** (Frio) e **a quente** (Quente), na escala 0-10. A diferença entre elas é o ganho que o guia proporciona.

| Métrica | Sem guia (cold start) | Com guia (warm start) | Δ |
|---------|:---------------------:|:---------------------:|:---:|
| **71 módulos 🔶** (55% do roadmap) | Frio médio **5.5** / 10 | Quente médio **8.4** / 10 | **+53%** |
| **7 módulos 📚/🔴** (6% do roadmap) | Frio médio **4.5** / 10 | ~**7.5** / 10 (c/ material prévio) | **+67%** |
| **50 módulos ❄️** (39% do roadmap) | Frio médio **7.4** / 10 | 7.4 / 10 (já autocontidos) | 0% |
| **Média global ponderada** | **6.2** / 10 | **8.0** / 10 | **+29%** |

> **Tradução:** Sem o guia, o aluno absorve 62% do conteúdo na primeira passada. Com o guia, absorve 80%. Isso significa **menos frustração**, **menos re-estudos**, e **cards que grudam de primeira**.

#### Impacto por pilar (gap Frio → Quente)

| Pilar | Gap | Impacto do guia |
|-------|:---:|-----------------|
| P5 — Controle/DSP | **+2.9** | 🔥 Maior benefício — 94% dos módulos são 🔶, projeto Python é essencial |
| P0 — Matemática | +2.5 | Alto benefício — conceitos abstratos precisam de visualização |
| P2 — Eletrônica | +2.5 | Alto — LTspice "mostra" o que cards descrevem |
| P4 — Potência | +2.3 | Alto — CADe SIMU e LTspice dominam as pontes |
| P1 — Circuitos | +2.2 | Sólido — Falstad/LTspice para AC e transitórios |
| P7 — Hardware Hacking | +2.2 | Sólido — ferramentas (Ghidra, OpenOCD) para módulos avançados |
| P3 — Digital/Embarcados | +1.8 | Menor gap — já é muito hands-on, Wokwi ajuda mas cards funcionam |

### Economia de Tempo

Estimativa baseada em mecânica SRS (Spaced Repetition System) com ~2.535 cards:

| Fonte de economia | Sem guia | Com guia | Economia |
|-------------------|:--------:|:--------:|:--------:|
| **Revisões SRS extras** (cards que falham na 1ª revisão) | ~630 falhas → 1.890 re-reviews | ~210 falhas → 420 re-reviews | **~25h** |
| **Leeches** (cards que nunca grudam) | ~100 leeches × 25 revisões | ~20 leeches × 25 revisões | **~30h** |
| **Re-estudo** (fazer projeto depois e ter que voltar aos cards) | ~35 módulos × 45min re-study | ~0 (projeto já feito) | **~25h** |
| **Frustração 📚/🔴** (tentar cards cold em 7 módulos impossíveis) | 7 × 2h de tentativa fracassada | 0h (vai direto pro recurso certo) | **~14h** |
| **Total estimado** | | | **~80–100h** |

> **~500h de roadmap + ~240h de revisão SRS (20min/dia, 2 anos) = ~740h totais.**
> O guia economiza **80-100 horas**, ou **~12% do tempo total**. Mas o ganho real é qualitativo: o aluno NUNCA experimenta a espiral de frustração "cards que não fazem sentido → forçar → não gruda → projeto → 'ah, ERA isso!' → voltar e re-aprender".

---

## Como usar este guia

Cada módulo tem uma **nota de absorção a frio** (0-10) — quanto um aluno que NUNCA viu o conteúdo absorve usando SÓ os cards. Cada projeto tem uma **categoria IA** (A/B/C/D) — quão automatizável é.

Cruzando as duas informações, cada módulo recebe um **modo de estudo**:

| Modo | Quando | O que fazer |
|------|--------|-------------|
| ❄️ **Cold Start OK** | Frio ≥ 7 | Cards primeiro → projeto depois (para profundidade) |
| 🔶 **Projeto Ponte** | Frio 5-6 + projeto B/C/D | Fazer o projeto ANTES → cards viram warm start |
| 📚 **Estudo Prévio** | Frio ≤ 5 + projeto A (IA faz tudo) | Vídeo/livro antes → cards + projeto substituto |
| 🔴 **Material Externo** | Frio ≤ 4 | Aula/livro/vídeo obrigatório antes de qualquer coisa |

E cada projeto recebe uma **classificação combinada**:

| Tag Projeto | Significado |
|-------------|-------------|
| 🎯 **Ponte** | Fazer ANTES dos cards — é O caminho pro warm start |
| 🏗️ **Profundidade** | Bom complemento, mas cards já funcionam sem ele |
| ⚡ **Automatizável** | IA faz tudo — substituir pelo projeto proposto |
| 🔧 **Bancada** | Hands-on físico — insubstituível, sem atalho |

### O "caminho dourado" 🎯

Para módulos 🔶: **Projeto → Cards → Drills**. O projeto constrói a intuição visual/prática que os cards sozinhos não conseguem dar. Depois de fazer o projeto, os cards viram revisão (warm start) em vez de aprendizado do zero (cold start). **Essa é a combinação mais poderosa do roadmap.**

---

## Visão Geral — Distribuição

| Modo | Módulos | % | Insight |
|------|:-------:|:-:|---------|
| ❄️ Cold Start | 50 | 39% | Cards são autossuficientes |
| 🔶 Projeto Ponte | 71 | 55% | Projeto é O caminho pro warm start |
| 📚 Estudo Prévio | 6 | 5% | Precisa de recurso externo (projeto A não ensina) |
| 📚+⚡ (com substituto proposto) | (6→0) | — | Se substituições forem implementadas, todos viram 🔶 |
| 🔴 Material Externo | 1 | 1% | Só 2.12 (Linhas de Transmissão) |

> **Insight**: 94% dos módulos (❄️ + 🔶) têm um caminho claro para absorção eficiente. Os 6 módulos 📚 são todos de P0 (matemática abstrata com projeto A) — implementar os projetos substitutos propostos eliminaria essa categoria.

---

## P0 — Matemática e Física (28 módulos)

| # | Módulo | Frio | Modo | Proj | Tag Proj | Ação recomendada |
|---|--------|:----:|:----:|:----:|:--------:|------------------|
| 0.01 | SI e Análise Dimensional | 7 | ❄️ | A | ⚡ | Cards direto; projeto dispensável |
| 0.02 | Álgebra e Isolamento | 7 | ❄️ | A | ⚡ | Cards direto; projeto dispensável |
| 0.03 | Funções | 6 | 🔶 | B | 🎯 | **Desmos plots** → reconhecer formas visuais → cards |
| 0.04 | Trigonometria | 6 | 🔶 | B | 🎯 | **Desmos animação trig** → ver fase/amplitude → cards |
| 0.05 | Números Complexos | 5 | 🔶 | B | 🎯 | **Python Argand** → visualizar j como rotação → cards |
| 0.06 | Logaritmos e dB | 7 | ❄️ | A | ⚡ | Cards direto; muito prático |
| 0.07 | Limites | 5 | 📚 | A | ⚡ | ⚠️ Cards fracos + projeto IA → **vídeo 3B1B** antes |
| 0.08 | Derivadas | 6 | ❄️ | A | ⚡ | Cards ok (Frio 6); projeto substituível |
| 0.09 | Taylor e Otimização | 5 | 🔶 | B | 🎯 | **Otimização de potência + linearização** → cards |
| 0.10 | Conceito de Integral | 6 | ❄️ | A | ⚡ | Cards ok (Frio 6); projeto substituível |
| 0.11 | Técnicas de Integração | 5 | 📚 | A | ⚡ | ⚠️ Cards fracos + projeto IA → **praticar manual, log erros** |
| 0.12 | RMS e Métodos Numéricos | 6 | 🔶 | B | 🎯 | **Vrms 3 formas de onda** → conectar calc a medições → cards |
| 0.13 | Cálculo Multivariável | 5 | 📚 | A | ⚡ | ⚠️ Cards fracos + projeto IA → **vídeo 3B1B** + substituto proposto |
| 0.14 | Cálculo Vetorial | 5 | 🔶 | B | 🎯 | **Python quiver plots** → VER campos → cards |
| 0.15 | EDO 1ª Ordem / RC-RL | 6 | 🔶 | C | 🎯 | **Falstad RC** → ver τ ao vivo → cards |
| 0.16 | EDO 2ª Ordem / RLC | 5 | 🔶 | C | 🎯 | **LTspice RLC** → ver sub/crit/super amortecido → cards |
| 0.17 | Transformada de Laplace | 5 | 📚 | A | ⚡ | ⚠️ Cards fracos + projeto IA → **vídeo Zach Star/3B1B** |
| 0.18 | Vetores | 7 | ❄️ | A | ⚡ | Cards direto; geométrico/visual |
| 0.19 | Matrizes e Sistemas | 6 | ❄️ | A | ⚡ | Cards ok (Frio 6); adicionar interpretação de resultados |
| 0.20 | Autovalores e Estabilidade | 5 | 📚 | A | ⚡ | ⚠️ Abstrato + projeto IA → **vídeo 3B1B** + substituto proposto |
| 0.21 | Probabilidade | 6 | 🔶 | B | 🎯 | **Monte Carlo 10k** → ver CLT acontecendo → cards |
| 0.22 | Estatística e Tolerâncias | 6 | 🔶 | B | 🎯 | **Monte Carlo tolerâncias** → ver variabilidade → cards |
| 0.23 | Mecânica Clássica | 6 | ❄️ | A | ⚡ | Cards ok (Frio 6); analogias capturam bem |
| 0.24 | Oscilações e Termodinâmica | 5 | 🔶 | B | 🎯 | **Python MHS amortecido** → ver decaimento → cards |
| 0.25 | Eletrostática e Corrente | 6 | 🔶 | B | 🎯 | **Visualização de campo** → intuição EM → cards |
| 0.26 | Magnetismo e Indução | 6 | 🔶 | C | 🎯 | **LTspice transformador** → ver Faraday → cards |
| 0.27 | Maxwell e Ondas EM | 5 | 📚 | A | ⚡ | ⚠️ Maxwell é abstratíssimo + projeto é cálculo → **vídeo 3B1B** |
| 0.28 | Física Moderna | 5 | 🔶 | B | 🎯 | **Diagramas de banda** → conectar quântica a dispositivos → cards |

### Resumo P0
| Modo | Qtd | % |
|------|:---:|:-:|
| ❄️ Cold Start | 8 | 29% |
| 🔶 Projeto Ponte | 14 | 50% |
| 📚 Estudo Prévio | 6 | 21% |

> **P0 é o pilar mais vulnerável.** 6 módulos com Frio=5 têm projetos A (IA faz tudo). Implementar os projetos substitutos propostos converteria todos os 6 em 🔶 Ponte.

---

## P1 — Circuitos (15 módulos)

| # | Módulo | Frio | Modo | Proj | Tag Proj | Ação recomendada |
|---|--------|:----:|:----:|:----:|:--------:|------------------|
| 1.01 | V, I, R — Lei de Ohm | 8 | ❄️ | B | 🏗️ | Cards direto; Falstad agrega profundidade |
| 1.02 | Código de Cores | 8 | ❄️ | A | ⚡ | Cards direto; exercício de memorização |
| 1.03 | Série e Paralelo | 8 | ❄️ | B | 🏗️ | Cards direto; divisor de tensão no Falstad |
| 1.04 | Kirchhoff (KVL/KCL) | 7 | ❄️ | B | 🏗️ | Cards direto; Falstad para validação |
| 1.05 | Fontes Reais e Superposição | 6 | 🔶 | B | 🎯 | **Falstad fonte com cargas variáveis** → ver regulação → cards |
| 1.06 | Thévenin/Norton | 6 | 🔶 | C | 🎯 | **LTspice circuito complexo** → passo-a-passo Thévenin → cards |
| 1.07 | Análise Nodal/Malhas | 6 | 🔶 | B | 🎯 | **Python numpy 3 nós** → comparar métodos → cards |
| 1.08 | Fontes Dependentes | 5 | 🔶 | B | 🎯 | **4 métodos no mesmo circuito** → ver diferenças → cards |
| 1.09 | Capacitores | 7 | ❄️ | C | 🏗️ | Cards direto; Falstad/LTspice para ver τ |
| 1.10 | Indutores | 7 | ❄️ | C | 🏗️ | Cards direto; ver spike de tensão + flyback |
| 1.11 | RLC / Ressonância / 555 | 5 | 🔶 | C | 🎯 | **LTspice RLC + 555** → ver ressonância/oscilação → cards |
| 1.12 | Senóides e RMS | 6 | 🔶 | B | 🎯 | **Falstad AC** → ver defasagem pela primeira vez → cards |
| 1.13 | Impedância | 6 | 🔶 | B | 🎯 | **Falstad freq variável** → ver Z mudar → cards |
| 1.14 | Fasores | 5 | 🔶 | C | 🎯 | **LTspice AC + diagrama fasorial** → ver rotação → cards |
| 1.15 | Potência AC / Filtros / Bode | 5 | 🔶 | C | 🎯 | **LTspice filtros + Bode** → ver atenuação → cards |

### Resumo P1
| Modo | Qtd | % |
|------|:---:|:-:|
| ❄️ Cold Start | 6 | 40% |
| 🔶 Projeto Ponte | 9 | 60% |

> **P1 é bem equilibrado.** DC básico (1.01-1.04) funciona cold start. AC e transitórios precisam de simulação visual.

---

## P2 — Eletrônica (14 módulos)

| # | Módulo | Frio | Modo | Proj | Tag Proj | Ação recomendada |
|---|--------|:----:|:----:|:----:|:--------:|------------------|
| 2.01 | Diodo e Junção PN | 6 | 🔶 | C | 🎯 | **LTspice curva I-V** → ver exponencial → cards |
| 2.02 | Retificadores | 7 | ❄️ | C | 🏗️ | Cards direto; LTspice para ver ripple |
| 2.03 | Zener e Aplicações | 7 | ❄️ | C | 🏗️ | Cards direto; LTspice para 4 circuitos |
| 2.04 | BJT como Chave | 7 | ❄️ | C | 🏗️ | Cards direto; LTspice H-bridge |
| 2.05 | BJT Amplificador | 5 | 🔶 | C | 🎯 | **LTspice emissor-comum** → ver ponto Q + ganho → cards |
| 2.06 | MOSFET e Projeto | 7 | ❄️ | C | 🏗️ | Cards direto; LTspice H-bridge |
| 2.07 | Op-Amp — Regras de Ouro | 7 | ❄️ | C | 🏗️ | Cards direto; LTspice 5 configs |
| 2.08 | Filtros e Osciladores | 5 | 🔶 | C | 🎯 | **LTspice Butterworth + Wien** → ver resposta → cards |
| 2.09 | Reguladores Lin vs Sw | 7 | ❄️ | A | ⚡ | Cards direto; tabela substituível |
| 2.10 | Conversor Buck | 6 | 🔶 | C | 🎯 | **LTspice Buck variar D e L** → ver ripple → cards |
| 2.11 | Boost / Buck-Boost | 5 | 🔶 | C | 🎯 | **LTspice Boost 3.7→5V** → ver inversão → cards |
| 2.12 | Linhas de Transmissão | 4 | 🔴 | A | ⚡ | ⚠️ Único módulo Frio≤4 + projeto A → **livro/vídeo obrigatório** |
| 2.13 | Carta de Smith / Param S | 5 | 🔶 | C | 🎯 | **Simulador Smith** → manipular pontos → cards |
| 2.14 | EMC | 5 | 🔶 | B | 🎯 | **Identificar 5 fontes EMI** → design thinking → cards |

### Resumo P2
| Modo | Qtd | % |
|------|:---:|:-:|
| ❄️ Cold Start | 6 | 43% |
| 🔶 Projeto Ponte | 7 | 50% |
| 🔴 Material Externo | 1 | 7% |

> **P2 é dominado por LTspice.** A maioria dos projetos ponte usa simulação — o aluno VÊ o comportamento antes de memorizar.

---

## P3 — Digital e Embarcados (11 módulos)

| # | Módulo | Frio | Modo | Proj | Tag Proj | Ação recomendada |
|---|--------|:----:|:----:|:----:|:--------:|------------------|
| 3.01 | Sistemas Numéricos e Portas | 8 | ❄️ | B | 🏗️ | Cards direto; Falstad para ver portas |
| 3.02 | Álgebra Booleana | 7 | ❄️ | B | 🏗️ | Cards direto; simplificação como prática |
| 3.03 | Karnaugh e Combinacionais | 6 | 🔶 | B | 🎯 | **K-map + somador** → prática visual → cards |
| 3.04 | Flip-Flops | 7 | ❄️ | B | 🏗️ | Cards direto; tabelas-verdade claras |
| 3.05 | Contadores e Divisores | 7 | ❄️ | B | 🏗️ | Cards direto; fórmulas diretas |
| 3.06 | Registradores e FSM | 6 | 🔶 | B | 🎯 | **Shift register + FSM semáforo** → ver estados → cards |
| 3.07 | Arduino GPIO | 8 | ❄️ | C | 🏗️ | Cards direto; Wokwi como prática |
| 3.08 | ADC, Sensores, PWM | 7 | ❄️ | C | 🏗️ | Cards direto; Wokwi multi-sensor |
| 3.09 | Timers, Interrupções, Serial | 6 | 🔶 | C | 🎯 | **Wokwi millis + ISR + I2C** → ver interrupções → cards |
| 3.10 | Wireless e IoT (ESP32) | 6 | 🔶 | C | 🎯 | **Wokwi ESP32 web server** → ver WiFi funcionar → cards |
| 3.11 | ADC/DAC Sistema Completo | 6 | 🔶 | C | 🎯 | **Wokwi sistema PID + EEPROM** → integrar tudo → cards |

### Resumo P3
| Modo | Qtd | % |
|------|:---:|:-:|
| ❄️ Cold Start | 6 | 55% |
| 🔶 Projeto Ponte | 5 | 45% |

> **P3 é o pilar mais autocontido.** Mais da metade funciona cold start. Os projetos Wokwi são as melhores pontes.

---

## P4 — Potência e Industrial (13 módulos)

| # | Módulo | Frio | Modo | Proj | Tag Proj | Ação recomendada |
|---|--------|:----:|:----:|:----:|:--------:|------------------|
| 4.01 | Sistema Trifásico | 6 | 🔶 | C | 🎯 | **Falstad + LTspice trifásico** → ver potência constante → cards |
| 4.02 | Potência Trif. e Medição | 6 | 🔶 | C | 🎯 | **LTspice 2 wattímetros** → medir FP → cards |
| 4.03 | Transformadores | 7 | ❄️ | C | 🏗️ | Cards direto; LTspice ensaios para profundidade |
| 4.04 | Motor de Indução | 6 | 🔶 | C | 🎯 | **LTspice equiv. + curva T×n** → ver escorregamento → cards |
| 4.05 | Partida e VFD | 5 | 🔶 | C | 🎯 | **CADe SIMU 3 arranques + Python FFT** → ver V/f → cards |
| 4.06 | Proteção Elétrica | 8 | ❄️ | C | 🏗️ | Cards direto; extremamente prático |
| 4.07 | Instalações e Aterramento | 7 | ❄️ | C | 🏗️ | Cards direto; NBR 5410 é receita |
| 4.08 | Comandos e Contatores | 7 | ❄️ | C | 🏗️ | Cards direto; lógica AND/OR de contatos |
| 4.09 | CLP e Ladder/GRAFCET | 6 | 🔶 | C | 🎯 | **PLC-Sim Ladder + GRAFCET** → ver temporização → cards |
| 4.10 | Sensores Industriais | 8 | ❄️ | C | 🏗️ | Cards direto; NPN/PNP e 4-20mA standalone |
| 4.11 | Máquinas Síncronas | 6 | 🔶 | C | 🎯 | **Python fasores + LTspice** → ver curva V → cards |
| 4.12 | Transmissão e Sistema PU | 5 | 🔶 | B | 🎯 | **Cálculos PU manuais + Python** → ver base change → cards |
| 4.13 | Qualidade e Harmônicos | 5 | 🔶 | B | 🎯 | **Python FFT de rede poluída** → ver componentes → cards |

### Resumo P4
| Modo | Qtd | % |
|------|:---:|:-:|
| ❄️ Cold Start | 5 | 38% |
| 🔶 Projeto Ponte | 8 | 62% |

> **P4 é dominado por CADe SIMU e LTspice.** Conteúdo industrial é visual — diagrama de contatos, curvas de motor, fasores.

---

## P5 — Controle e DSP (16 módulos)

| # | Módulo | Frio | Modo | Proj | Tag Proj | Ação recomendada |
|---|--------|:----:|:----:|:----:|:--------:|------------------|
| 5.01 | Classificação de Sinais | 5 | 🔶 | B | 🎯 | **Python 4 sinais + operações** → vocabulário visual → cards |
| 5.02 | Convolução e Resp. Impulso | 5 | 🔶 | B | 🎯 | **Convolução manual + RC** → ver h(t) funcionando → cards |
| 5.03 | Fourier e FFT | 5 | 🔶 | B | 🎯 | **FFT de 3 sinais + filtragem** → "aha moment" espectral → cards |
| 5.04 | Transformada de Laplace | 5 | 🔶 | B | 🎯 | **Laplace RC + RLC + SymPy** → classificar amortecimento → cards |
| 5.05 | Função de Transf. e Blocos | 6 | 🔶 | B | 🎯 | **Step response variando ζ** → VER mudança → cards |
| 5.06 | Modelagem de Sistemas | 6 | 🔶 | B | 🎯 | **Modelando forno** → física→ODE→G(s) → cards |
| 5.07 | Estabilidade | 6 | 🔶 | B | 🎯 | **5 TFs Routh + K_cr** → encontrar limite → cards |
| 5.08 | Diagrama de Bode | 5 | 🔶 | B | 🎯 | **4 sistemas Bode assintótico** → aprender a "ler" → cards |
| 5.09 | Controlador PID | 7 | ❄️ | B | 🏗️ | Cards direto (intuição "motorista" excelente); ZN como prática |
| 5.10 | Compensadores Lead/Lag | 5 | 🔶 | B | 🎯 | **Lead por Bode** → projeto por especificação → cards |
| 5.11 | Lugar das Raízes | 5 | 🔶 | B | 🎯 | **Root Locus: K para ζ=0.5** → ver polos andando → cards |
| 5.12 | Controle Digital | 5 | 🔶 | C | 🎯 | **Wokwi PID Arduino** → contínuo vs discreto → cards |
| 5.13 | Amostragem e Nyquist | 6 | 🔶 | B | 🎯 | **Python aliasing** → ver "roda ao contrário" → cards |
| 5.14 | Transformada Z | 5 | 🔶 | B | 🎯 | **Bilinear: RC analógico→digital** → ponte contínuo↔discreto → cards |
| 5.15 | FIR e IIR | 6 | 🔶 | B | 🎯 | **scipy.signal FIR vs IIR** → comparar na prática → cards |
| 5.16 | FFT Prática | 5 | 🔶 | B | 🎯 | **Espectrograma + PSD + áudio** → análise de sinal real → cards |

### Resumo P5
| Modo | Qtd | % |
|------|:---:|:-:|
| ❄️ Cold Start | 1 | 6% |
| 🔶 Projeto Ponte | 15 | 94% |

> **P5 é o pilar onde projetos ponte são MAIS essenciais.** Quase todos módulos precisam de visualização Python para "clicar". A boa notícia: 93% dos projetos são B (processo ensina), exatamente o perfil de ponte ideal.

---

## P7 — Hardware Hacking (31 módulos)

| # | Módulo | Frio | Modo | Proj | Tag Proj | Ação recomendada |
|---|--------|:----:|:----:|:----:|:--------:|------------------|
| 7.01 | Lab, Mindset, Superfícies | 7 | ❄️ | C+B | 🏗️ | Cards direto; setup VM + threat model |
| 7.02 | Python para HH | 7 | ❄️ | B | 🏗️ | Cards direto; toolkit Python como prática |
| 7.03 | Endianness e Dados | 8 | ❄️ | A | ⚡ | Cards direto; exercícios substituíveis |
| 7.04 | Eletrônica, Equipamento, Solda | 7 | ❄️ | C+B | 🏗️ | Cards direto; simulador + lista de compras |
| 7.05 | Arquitetura Embarcada | 6 | 🔶 | B | 🎯 | **Datasheet + diagrama de blocos** → mapear SoC → cards |
| 7.06 | Protocolos Seriais (HH) | 7 | ❄️ | C | 🏗️ | Cards direto; PulseView como prática |
| 7.07 | Linux Embarcado | 8 | ❄️ | C | 🏗️ | Cards direto ("60s pós-shell"); Binwalk como prática |
| 7.08 | Leitura de Datasheets | 7 | ❄️ | B | 🏗️ | Cards direto; speed-run como prática |
| 7.09 | OSINT para Dispositivos | 8 | ❄️ | B | 🏗️ | Cards direto; FCC ID standalone |
| 7.10 | Análise Visual de PCB | 7 | ❄️ | B | 🏗️ | Cards direto; análise de fotos como prática |
| 7.11 | Identificação UART | 7 | ❄️ | C+B | 🏗️ | Cards direto (4 passos); Wokwi + report |
| 7.12 | Exploração UART | 7 | ❄️ | C | 🏗️ | Cards direto; QEMU shell como prática |
| 7.13 | Dump de Flash SPI | 7 | ❄️ | C | 🏗️ | Cards direto (pipeline Binwalk) |
| 7.14 | EEPROM / Attack Chain | 7 | ❄️ | C | 🏗️ | Cards direto; DVRF como prática |
| 7.15 | U-Boot e Bootloaders | 7 | ❄️ | C | 🏗️ | Cards direto; QEMU como prática |
| 7.16 | JTAG — Teoria | 5 | 🔶 | C | 🎯 | **OpenOCD + TAP FSM** → ver estados → cards |
| 7.17 | Exploração JTAG/SWD | 6 | 🔶 | C | 🎯 | **QEMU + GDB debug** → ver memória → cards |
| 7.18 | Proteções e Bypass | 5 | 🔶 | B | 🎯 | **Pesquisa RDP/CRP/ESP32** → documentar bypasses → cards |
| 7.19 | Extração de Firmware | 7 | ❄️ | C | 🏗️ | Cards direto; Binwalk avançado como prática |
| 7.20 | Análise Estática / Emulação | 5 | 🔶 | C | 🎯 | **Ghidra RE de DVRF** → ver vulnerabilidade → cards |
| 7.21 | Modificação / FW Crypto | 5 | 🔶 | C | 🎯 | **QEMU Firmadyne** → emular e modificar → cards |
| 7.22 | Hacking Web IoT | 7 | ❄️ | C | 🏗️ | Cards direto; command injection claro |
| 7.23 | RF e SDR | 6 | 🔶 | C | 🎯 | **GNURadio + URH** → capturar sinal → cards |
| 7.24 | BLE Hacking | 6 | 🔶 | C | 🎯 | **Wireshark BLE + bleak** → ver GATT → cards |
| 7.25 | Wi-Fi e Zigbee | 5 | 🔶 | C | 🎯 | **Docker IoT + nmap** → ver superfície rede → cards |
| 7.26 | Side-Channel Teoria | 5 | 🔶 | B | 🎯 | **Python timing attack** → implementar vuln + defesa → cards |
| 7.27 | Fault Injection | 5 | 🔶 | C | 🎯 | **ChipWhisperer notebooks** → ver glitch → cards |
| 7.28 | Power Analysis (CW) | 5 | 🔶 | C | 🎯 | **CW traces pré-capturados** → ver correlação → cards |
| 7.29 | Ataques Físicos / Chip-off | 6 | 🔶 | B | 🎯 | **Pesquisa 3 casos** → estudar Xbox/Switch/Ledger → cards |
| 7.30 | Metodologia Pentest IoT | 7 | ❄️ | C | 🏗️ | Cards direto (OWASP framework) |
| 7.31 | CTF e Portfólio | 7 | ❄️ | C+B | 🏗️ | Cards direto; CTFs como prática contínua |

### Resumo P7
| Modo | Qtd | % |
|------|:---:|:-:|
| ❄️ Cold Start | 18 | 58% |
| 🔶 Projeto Ponte | 13 | 42% |

> **P7 é o mais "standalone".** Módulos procedurais (fases 2-5) funcionam cold start graças ao formato receita/checklist. Os avançados (JTAG, side-channel, RE) precisam da ferramenta para clicar.

---

## 🎯 Os 20 Melhores Projetos Ponte

Projetos que são SIMULTANEAMENTE demonstrações interessantes E o melhor caminho pro warm start. Priorizados por: (1) impacto na absorção, (2) qualidade como demonstração, (3) abrangência de conceitos.

| Rank | Módulo | Projeto | Ferramenta | Por que é ponte ideal |
|:----:|--------|---------|------------|----------------------|
| 1 | 5.03 | FFT de 3 sinais + filtragem | Python | "Aha moment" fundamental — VER espectro de onda quadrada |
| 2 | 5.05 | Step response variando ζ | Python control | VER polo se mover e resposta mudar — core de controle |
| 3 | 5.09 | PID temperatura + ZN | Python | Variar Kp/Ki/Kd e VER efeito em tempo real |
| 4 | 1.11 | RLC + ressonância + 555 | LTspice | VER ressonância pela primeira vez; 555 oscilando |
| 5 | 5.02 | Convolução manual + RC | Python | Implementar "na mão" → entender h(t) profundamente |
| 6 | 0.05 | Complexos + plano Argand | Python | VER j como rotação — base para fasores |
| 7 | 2.05 | BJT emissor-comum | LTspice | VER ponto Q + ganho + saturação/corte |
| 8 | 4.01 | Trifásico Falstad + LTspice | Falstad/LT | VER potência trifásica constante |
| 9 | 7.20 | Ghidra RE (DVRF) | Ghidra | VER vulnerabilidade no disassembly |
| 10 | 3.09 | millis + ISR + I2C/SPI | Wokwi | VER interrupção disparar em tempo real |
| 11 | 0.14 | Python quiver plots | Python | VER campos vetoriais → Maxwell faz sentido |
| 12 | 5.08 | 4 sistemas Bode assintótico | Python | Construir Bode "no papel" → COMPARAR com Python |
| 13 | 2.10 | Buck variar D e L | LTspice | VER relação duty-cycle ↔ Vout ↔ ripple |
| 14 | 7.26 | Timing attack Python | Python | IMPLEMENTAR ataque + defesa → entender side-channel |
| 15 | 5.11 | Root Locus: K para ζ=0.5 | Python | VER polos andando no plano s conforme K varia |
| 16 | 4.05 | 3 partidas + VFD + FFT | CADe/Python | VER corrente de partida + harmônicos |
| 17 | 1.14 | RLC fasorial + LTspice AC | LTspice | VER diagrama fasorial girar |
| 18 | 2.13 | Smith Chart simulador | Simulador | MANIPULAR pontos na carta → GPS de impedância |
| 19 | 0.16 | RLC sub/crít/super LTspice | LTspice | VER 3 tipos de amortecimento lado a lado |
| 20 | 5.13 | Aliasing Python | Python | VER "roda girando ao contrário" — Nyquist clica |

> **Padrão:** 19/20 envolvem VISUALIZAÇÃO (VER, MANIPULAR). O formato card não consegue replicar essa experiência visual. O projeto é o complemento perfeito.

---

## 📚 Módulos que Precisam de Material Externo (6 📚 + 1 🔴)

Estes módulos têm Frio≤5 E projeto categoria A (IA faz tudo). Nem os cards nem o projeto atual são suficientes para cold start.

| # | Módulo | Recurso Recomendado | Substituto Proposto |
|---|--------|--------------------|---------------------------------|
| 0.07 | Limites | [3B1B: Essence of Calculus](https://www.youtube.com/watch?v=WUvTyaaNkzM) ep 7 | Análise transitório capacitor no Falstad |
| 0.11 | Técnicas de Integração | Praticar manual → log de erros → SymPy depois | Manter, mas entregável = log de erros |
| 0.13 | Cálculo Multivariável | [3B1B: Multivariable Calculus](https://www.khanacademy.org/math/multivariable-calculus) | Análise de sensibilidade de divisor com Monte Carlo |
| 0.17 | Transf. de Laplace | [Zach Star: Laplace](https://www.youtube.com/watch?v=n2y7n6jw5d0) | Falstad RC + comparar com solução Laplace |
| 0.20 | Autovalores | [3B1B: Eigenvectors](https://www.youtube.com/watch?v=PFDu9oVAE-g) | Prever comportamento RLC por autovalores → LTspice |
| 0.27 | Maxwell e Ondas EM | [3B1B: Maxwell's Equations](https://www.youtube.com/watch?v=NI3R5o1gwq0) | Simulação de onda EM propagando |
| 2.12 | Linhas de Transmissão | [W2AEW: Transmission Lines](https://www.youtube.com/watch?v=DovunJKhz3o) | Simulador de reflexão online → visualizar Γ e VSWR |

> **Se os projetos substitutos forem implementados**, os 6 📚 passam para 🔶 Ponte e 2.12 recebe material visual adequado. Prioridade recomendada.

---

## Estatísticas Globais

### Distribuição de Modos por Pilar

| Pilar | ❄️ | 🔶 | 📚 | 🔴 | Total | % ❄️ | % Ponte |
|-------|:--:|:--:|:--:|:--:|:-----:|:----:|:-------:|
| P0 | 8 | 14 | 6 | 0 | 28 | 29% | 50% |
| P1 | 6 | 9 | 0 | 0 | 15 | 40% | 60% |
| P2 | 6 | 7 | 0 | 1 | 14 | 43% | 50% |
| P3 | 6 | 5 | 0 | 0 | 11 | 55% | 45% |
| P4 | 5 | 8 | 0 | 0 | 13 | 38% | 62% |
| P5 | 1 | 15 | 0 | 0 | 16 | 6% | 94% |
| P7 | 18 | 13 | 0 | 0 | 31 | 58% | 42% |
| **Total** | **50** | **71** | **6** | **1** | **128** | **39%** | **55%** |

### Distribuição de Tags de Projeto

| Tag | Qtd | % | Significado |
|-----|:---:|:-:|-------------|
| 🎯 Ponte | 71 | 46% | Projetos que SÃO o caminho pro warm start |
| 🏗️ Profundidade | 45 | 29% | Bom complemento, cards já funcionam |
| ⚡ Automatizável | 18 | 12% | IA faz tudo — valor pedagógico baixo |
| 🔧 Bancada (P6) | 17 | 11% | Hands-on físico insubstituível |
| (P6 C) | 2 | 1% | KiCad — ferramenta necessária |

### Correlação Modo × Projeto

| | Proj A | Proj B | Proj C | Proj D |
|---|:---:|:---:|:---:|:---:|
| ❄️ Cold Start | 9 (⚡) | 16 (🏗️) | 25 (🏗️) | 0 |
| 🔶 Ponte | 0 | 35 (🎯) | 36 (🎯) | 0 |
| 📚 Estudo Prévio | 6 (⚡) | 0 | 0 | 0 |
| 🔴 Material Ext | 1 (⚡) | 0 | 0 | 0 |
| P6 (Lab) | 0 | 0 | 2 | 17 (🔧) |

> **Todos os 6 módulos 📚 + 1 módulo 🔴 têm projetos A.** Não é coincidência — quando o módulo é abstrato E o projeto é automatizável, ambas as ferramentas (cards + projeto) falham no cold start. A solução é converter os projetos A em B/C.

---

## Recomendação Final

### Para o aluno:
1. **Olhe o modo do módulo** (❄️/🔶/📚/🔴) antes de começar
2. **Se ❄️**: Mergulhe nos cards. Projeto depois, pra profundidade.
3. **Se 🔶**: Faça o projeto PRIMEIRO. Leia o README, monte/simule/rode, observe. DEPOIS estude os cards — eles vão "clicar" como revisão (warm start).
4. **Se 📚/🔴**: Assista ao vídeo/leia o capítulo do livro indicado. Depois projeto. Depois cards.

### Para o roadmap:
1. **Substituir os 18 projetos A** pelos B/C propostos → elimina toda a categoria 📚
2. **As tags ❄️/🔶/📚/🔴 já estão marcadas** em cada arquivo de notas e nas tabelas de prontidão dos READMEs de cada pilar
