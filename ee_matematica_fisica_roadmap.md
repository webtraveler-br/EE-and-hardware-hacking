# Pilar 0: Matemática e Física

> **Sobre este pilar**: Este pilar cobre **toda a matemática e física** de um currículo real de Engenharia Elétrica, mas com foco em **intuição, simulação e visualização** em vez de cálculo manual exaustivo. Você vai entender POR QUE cada ferramenta existe e ONDE ela aparece nos Pilares 1-5, fazendo exercícios manuais estratégicos para cimentar o essencial.
>
> **Ferramentas**: Python (numpy, matplotlib, sympy, scipy) + [Desmos](https://www.desmos.com/calculator) + [GeoGebra](https://www.geogebra.org/) + [3Blue1Brown](https://www.3blue1brown.com/) (vídeos)
>
> **Quando estudar**: ANTES do Pilar 1, ou em paralelo. Fases 1-4 (Matemática Pura) são pré-requisito para os Pilares 1-3. Fases 5-6 são pré-requisito para Pilares 4-5. Fase 7+ (Física) pode ser estudada em paralelo com Pilar 1.
>
> **Onde cada fase é usada**:
> - **Fases 1-2** (Pré-Cálculo, Álgebra) → [Pilar 1](ee_circuitos_roadmap.md) inteiro, [HH 0.4](hardware_hacking_roadmap.md) (sistemas numéricos)
> - **Fase 1** (Complexos, Trig, dB) → [Pilar 1](ee_circuitos_roadmap.md) Módulos 1.13-1.19 (AC/fasores), [Pilar 2](ee_eletronica_roadmap.md) (filtros), [Pilar 4](ee_eletrotecnica_automacao_roadmap.md) (trifásico), [Pilar 5](ee_controle_sinais_roadmap.md) (Bode)
> - **Fases 2-3** (Cálculo I-II) → [Pilar 1](ee_circuitos_roadmap.md) Módulos 1.10-1.12 (transitórios RC/RL), [Pilar 5](ee_controle_sinais_roadmap.md) (sinais, Fourier)
> - **Fase 4** (Cálculo III) → [Pilar 4](ee_eletrotecnica_automacao_roadmap.md) Módulo 4.4 (transformadores), [HH Avançado B.1](hardware_hacking_roadmap_avancado.md) (integridade de sinal)
> - **Fase 5** (EDOs, Laplace) → [Pilar 1](ee_circuitos_roadmap.md) (RLC), [Pilar 5](ee_controle_sinais_roadmap.md) Módulos 5.4-5.6 (modelagem), [Lab L.4](ee_laboratorio_real_roadmap.md) (transitórios reais)
> - **Fase 6** (Álgebra Linear) → [Pilar 1](ee_circuitos_roadmap.md) Módulos 1.7-1.8 (análise nodal/malhas), [Pilar 5](ee_controle_sinais_roadmap.md) Módulo 5.7 (estabilidade)
> - **Fase 7** (Probabilidade) → [Pilar 2](ee_eletronica_roadmap.md) (tolerâncias), [Pilar 5](ee_controle_sinais_roadmap.md) Módulos 5.13-5.16 (DSP, ruído), [HH 7.1-7.3](hardware_hacking_roadmap.md) (side-channel/power analysis)
> - **Fase 8** (Mecânica) → [Pilar 4](ee_eletrotecnica_automacao_roadmap.md) Módulos 4.5-4.7 (motores), [Pilar 5](ee_controle_sinais_roadmap.md) Módulo 5.6 (modelagem)
> - **Fases 9-10** (EM, Semicondutores) → [Pilar 2](ee_eletronica_roadmap.md) Módulos 2.1-2.7 (diodos, BJT, MOSFET), [Pilar 4](ee_eletrotecnica_automacao_roadmap.md) Módulo 4.4 (transformadores), [HH 6.1](hardware_hacking_roadmap.md) (RF/SDR)
>
> [Voltar ao Índice](ee_roadmap_index.md)

---

## Visão Geral

| Fase | Módulos | Foco | Horas |
|------|---------|------|-------|
| **Pré-Cálculo** | 0.1–0.6 | Unidades, álgebra, funções, trigonometria, complexos, dB | ~12h |
| **Cálculo I — Derivadas** | 0.7–0.10 | Limites, derivadas, aplicações, séries de Taylor | ~10h |
| **Cálculo II — Integrais** | 0.11–0.14 | Integrais, técnicas, aplicações, métodos numéricos | ~10h |
| **Cálculo III — Multivariável** | 0.15–0.17 | Derivadas parciais, integrais múltiplas, cálculo vetorial | ~8h |
| **Equações Diferenciais** | 0.18–0.20 | EDOs 1ª/2ª ordem, Laplace | ~8h |
| **Álgebra Linear** | 0.21–0.23 | Vetores, matrizes, autovalores | ~7h |
| **Probabilidade e Estatística** | 0.24–0.25 | Distribuições, análise de erros, tolerâncias | ~5h |
| **Física: Mecânica e Térmica** | 0.26–0.29 | Newton, rotação, oscilações, termodinâmica | ~10h |
| **Física: Eletromagnetismo** | 0.30–0.34 | Eletrostática, magnetismo, Faraday, Maxwell | ~12h |
| **Física: Moderna e Semicondutores** | 0.35–0.36 | Quântica básica, bandas de energia, materiais | ~5h |

**Total: ~87h** — equivalente a ~4 disciplinas de um curso real, comprimidas com foco em aplicação.

---

## Fase 1 — Pré-Cálculo: A Linguagem da Engenharia

### Módulo 0.1: Unidades SI, Prefixos e Análise Dimensional
**Tempo: 1.5h**

#### 📚 O que memorizar

| Prefixo | Símbolo | Fator | Exemplo EE |
|---------|---------|-------|-----------|
| pico | p | 10⁻¹² | pF (caps cerâmicos) |
| nano | n | 10⁻⁹ | nF, ns (tempos digitais) |
| micro | μ | 10⁻⁶ | μF, μA, μH |
| mili | m | 10⁻³ | mA, mV, mH |
| kilo | k | 10³ | kΩ, kHz, kW |
| mega | M | 10⁶ | MΩ, MHz |
| giga | G | 10⁹ | GHz |

- **Análise dimensional**: SEMPRE verifique unidades. τ = RC → [s] = [Ω]×[F] = [V/A]×[As/V] = [s] ✓
- **Notação científica**: 4.7 × 10³ = 4700. Essencial em Python e calculadoras

#### Projeto
1. **10 conversões rápidas** cronometradas (<30s cada): 2200μF→?mF, 3.3kΩ→?Ω, 16MHz→?Hz, etc.
2. **Análise dimensional** de 3 fórmulas: τ=RC, f=1/(2π√LC), P=V²/R
3. **Prompt IA**: *"Demonstre que τ=RC tem unidade de segundos expandindo Ω e F em unidades básicas."*

#### Erros Comuns
- Confundir **m** (mili, 10⁻³) com **M** (mega, 10⁶) — diferença de 10⁹!
- Não verificar dimensões da resposta final

---

### Módulo 0.2: Álgebra — Equações, Isolamento e Sistemas
**Tempo: 2h**

#### 📚 O que memorizar
- **1º grau**: ax + b = 0 → x = -b/a. V=IR → I=V/R, R=V/I
- **2º grau**: ax² + bx + c = 0 → x = (-b ± √(b²-4ac))/2a. Aparece em RLC!
- **Discriminante**: Δ>0 = 2 raízes reais. Δ=0 = raiz dupla. Δ<0 = raízes complexas (oscilação!)
- **Sistemas lineares 2×2 e 3×3**: Cramer, substituição, ou Python
- **Isolamento em fórmulas complexas**: de f=1/(2π√LC), isolar C requer elevar ao quadrado e inverter

#### Projeto
1. **5 isolamentos** de fórmulas reais: de V=IR isole R; de P=V²/R isole R; de f=1/(2π√LC) isole C; de V_out=V_in×R2/(R1+R2) isole R2; de V_rms=V_p/√2 isole V_p
2. **3 sistemas 2×2** no papel (análise de malhas)
3. **1 sistema 3×3** com Python
4. **Equação RLC**: resolva s²+200s+10⁶=0 → identifique se oscila

#### Erros Comuns
- Δ<0 NÃO significa "sem solução" — significa raízes complexas = oscilação!
- Errar sinais ao isolar variáveis dentro de raízes ou frações

---

### Módulo 0.3: Funções — Tipos, Gráficos e Comportamento
**Tempo: 2h**

#### 📚 O que memorizar
- **Linear** y=kx: reta. V=IR com R constante
- **Quadrática** y=kx²: parábola. P=I²R — corrente dobra, potência 4×!
- **Inversa** y=k/x: hipérbole. I=V/R com V constante
- **Exponencial** y=e^(ax): crescimento (a>0) ou decaimento (a<0). Carga/descarga RC!
- **Logarítmica** y=ln(x): cresce cada vez mais devagar. Percepção de som (dB)
- **Raiz quadrada** y=√x: cresce desacelerando
- **Senoidal** y=sin(ωt): oscilação. AC!
- **Domínio e imagem**: para que valores de x a função existe? Que valores y pode assumir?
- **Composição**: f(g(x)). Ex: e^(sin(t)) — função dentro de função → regra da cadeia!
- **Função par/ímpar**: cos é par (cos(-x)=cos(x)), sin é ímpar (sin(-x)=-sin(x))

#### Intuição
Reconhecer o TIPO de função olhando uma fórmula é superpoder: P=I²R → "quadrática em I, se I dobra P quadruplica". V_C=V_f(1-e^(-t/τ)) → "exponencial que estabiliza". Saber isso antes de calcular qualquer coisa é o que separa engenheiro de calculadora.

#### Projeto
1. **No Desmos**: plote todas as 7 funções — identifique cada forma visualmente
2. **Match**: dada uma fórmula EE, identifique o tipo de função (5 exercícios)
3. **Prompt IA**: *"Explique por que P=V²/R e P=I²R não se contradizem. Se V dobra com R fixo, o que acontece com I e P?"*

---

### Módulo 0.4: Trigonometria — A Linguagem do Mundo AC
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Definições**: sin(θ)=oposto/hipotenusa, cos(θ)=adjacente/hipotenusa, tan(θ)=sin/cos
- **Círculo unitário**: senóide é projeção de um ponto girando num círculo

| Graus | Rad | sin | cos | tan |
|-------|-----|-----|-----|-----|
| 0° | 0 | 0 | 1 | 0 |
| 30° | π/6 | 0.5 | 0.866 | 0.577 |
| 45° | π/4 | 0.707 | 0.707 | 1 |
| 60° | π/3 | 0.866 | 0.5 | 1.732 |
| 90° | π/2 | 1 | 0 | ∞ |
| 180° | π | 0 | -1 | 0 |
| 270° | 3π/2 | -1 | 0 | ∞ |

- **Radianos**: 2π rad = 360°. Engenharia SEMPRE em radianos (ω = 2πf em rad/s)
- **Identidades essenciais**: sin²+cos²=1, sin(A±B)=sinAcosB±cosAsinB, cos(2A)=cos²A-sin²A
- **Equação da senóide**: v(t) = V_p × sin(ωt + φ). ω=frequência angular, φ=fase
- **Defasagem**: φ diferente → uma "adianta" ou "atrasa" a outra
- **Inversa**: arcsin, arccos, arctan — "qual ângulo dá esse valor?"

#### Intuição
Cada sinal AC é um **ponto girando num círculo**. Frequência = velocidade de giro. Amplitude = raio. Fase = onde começa. "Corrente atrasa tensão em 90°" = dois pontos giram juntos, um ¼ de volta atrás.

#### Projeto
1. **Desmos**: anime ponto girando → senóide sendo desenhada
2. **Defasagem**: plote sin(ωt) e sin(ωt+π/2) → observe 90° de atraso
3. **5 exercícios**: dados V_p, f, φ → escreva v(t) e plote
4. **Identidade na prática**: demonstre que sin²(x)+cos²(x)=1 é Pitágoras no círculo unitário

#### Erros Comuns
- Calculadora em graus quando deveria ser radianos — sin(90°)=1 mas sin(90 rad)≈0.894!
- Confundir f (Hz) com ω (rad/s): ω = 2πf

---

### Módulo 0.5: Números Complexos — Vetores 2D com Superpoderes
**Tempo: 2.5h**

#### 📚 O que memorizar
- **j = √(-1)** (i em matemática, j em engenharia porque i=corrente). j²=-1, j³=-j, j⁴=1
- **Retangular**: z = a + jb. Parte real a, parte imaginária b
- **Polar**: z = r∠θ = r×e^(jθ). r=|z|=√(a²+b²), θ=arctan(b/a)
- **Conversão**: a=r×cos(θ), b=r×sin(θ)
- **Operações**: soma→retangular. Multiplicação/divisão→polar
- **Euler**: e^(jθ) = cos(θ) + j×sin(θ) — ponte entre exponencial e trigonometria
- **Conjugado**: z*=a-jb. z×z*=|z|²
- **1/j = -j** (demonstre: multiplique num e denom por j)

#### Intuição
Complexos são **vetores que rastreiam magnitude E fase** simultaneamente. Em DC: V=12V (um número). Em AC: V=120∠30° (número complexo). Sem complexos, AC requer equações diferenciais. COM complexos, vira V=ZI (álgebra!). Fasores SÃO números complexos.

#### Projeto
1. **Python**: visualize z₁=3+4j e z₂=5∠60° no plano complexo
2. **10 exercícios**: 5 conversões ret↔polar, 3 mult/div polar, 2 somas retangular
3. **Aplicação**: calcule Z = R + jωL + 1/(jωC) para R=100Ω, L=10mH, C=1μF, f=1kHz

#### Erros Comuns
- arctan dá mesmo valor para quadrantes opostos — SEMPRE verifique o quadrante!
- Somar em polar (errado!) — soma SÓ em retangular

---

### Módulo 0.6: Logaritmos, Exponenciais e Decibéis
**Tempo: 2h**

#### 📚 O que memorizar
- **e = 2.71828...**: base do log natural. e^x é sua própria derivada! Aparece em TUDO: RC, RL, crescimento/decaimento
- **log₁₀(x)**: "10 elevado a quanto dá x?". log(100)=2, log(1000)=3
- **ln(x)**: log base e. ln(e)=1, ln(1)=0, ln(e^x)=x
- **Propriedades**: log(AB)=logA+logB, log(A/B)=logA-logB, log(Aⁿ)=n×logA
- **dB potência**: dB = 10×log₁₀(P₂/P₁)
- **dB tensão**: dB = 20×log₁₀(V₂/V₁) (20 porque P∝V²)
- **Referências**: +3dB≈2× potência, +6dB≈2× tensão, +10dB=10× potência, +20dB=10× tensão, -3dB=meia potência
- **Escala log**: comprime 1Hz a 1GHz num gráfico legível (Bode!)

#### Projeto
1. **Tabela rápida** de cabeça: +3dB=?×P, -6dB=?×V, +20dB=?×V, -40dB=?×V
2. **Cascata**: amp +26dB → filtro -12dB → amp +10dB. Ganho total em dB e em vezes
3. **Plote** resposta de filtro RC em escala linear e log — compare utilidade

#### Checkpoint — Pré-Cálculo
- [ ] Converte prefixos SI em <30s, verifica dimensões
- [ ] Isola qualquer variável de fórmulas EE
- [ ] Calcula sin/cos de ângulos notáveis, opera com radianos
- [ ] Converte complexos ret↔polar, opera em ambas formas
- [ ] Opera em dB naturalmente

---

## Fase 2 — Cálculo I: Derivadas

### Módulo 0.7: Limites e Continuidade — O Conceito
**Tempo: 2h**

#### 📚 O que memorizar
- **Limite**: valor que f(x) se APROXIMA quando x se aproxima de a. Não precisa ser f(a)!
- **Limites importantes**: lim(x→0) sin(x)/x = 1. lim(x→∞) (1+1/x)^x = e
- **Continuidade**: f é contínua se lim(x→a)f(x) = f(a). Sem "buraco" nem "salto"
- **Limites e derivadas**: derivada = lim(h→0) [f(x+h)-f(x)]/h — é a inclinação "instantânea"

#### Intuição
Você NÃO precisa calcular limites formalmente (ε-δ) na engenharia. Mas precisa entender o CONCEITO: "o que acontece quando me aproximo de um valor?". Exemplo: frequência de ressonância — conforme f→f₀, a impedância do RLC série → mínima. É um limite! Na prática, use L'Hôpital ou Python para limites difíceis.

#### Projeto
1. **Desmos**: plote f(x)=sin(x)/x → observe que f(0) não existe, mas o limite é 1
2. **Calcule 5 limites** intuitivamente (sem ε-δ): lim(x→2) x², lim(x→0) sin(x)/x, lim(x→∞) 1/x, lim(x→0) (e^x-1)/x, lim(x→∞) e^(-x)
3. **Conexão**: explique por que a corrente no capacitor num instante é o LIMITE da corrente média quando Δt→0

---

### Módulo 0.8: Derivadas — Taxa de Variação
**Tempo: 3h**

#### 📚 O que memorizar
- **Derivada = inclinação = taxa de mudança instantânea**
- **7 regras essenciais**:
  - d/dt[k] = 0 (constante)
  - d/dt[tⁿ] = n×tⁿ⁻¹ (potência)
  - d/dt[e^(at)] = a×e^(at) (exponencial — é sua própria derivada!)
  - d/dt[sin(ωt)] = ω×cos(ωt)
  - d/dt[cos(ωt)] = -ω×sin(ωt)
  - **Cadeia**: d/dt[f(g(t))] = f'(g)×g'(t). Ex: d/dt[e^(-t/τ)] = (-1/τ)×e^(-t/τ)
  - **Produto**: d/dt[f×g] = f'g + fg'. Ex: d/dt[t×e^(-t)] = e^(-t)(1-t)
- **Quociente**: d/dt[f/g] = (f'g-fg')/g²
- **Conexão EE**: I=C×dV/dt (capacitor), V=L×dI/dt (indutor)

#### Intuição
Derivada responde "quão rápido muda AGORA?". A derivada de sin é cos — quando sin está no pico (parado), cos=0; quando sin cruza zero (máxima taxa), cos=máximo. Na carga RC, I=C×dV/dt: corrente é MÁXIMA no início (V mudando rápido) e ZERO no final (V constante).

#### Projeto
1. **Desmos**: plote sin(x) e cos(x) juntos, verifique visualmente que cos=inclinação de sin
2. **6 derivadas manuais**: d/dt[5t²], d/dt[3sin(100t)], d/dt[12(1-e^(-t/τ))], d/dt[e^(-2t)cos(10t)], d/dt[ln(t)], d/dt[1/(1+t²)]
3. **Aplicação**: V_C(t)=12(1-e^(-t/τ)), calcule I(t)=C×dV/dt passo a passo com regra da cadeia
4. **Prompt IA**: *"Calcule I(t)=C×dV/dt para V(t)=5sin(2π60t) e C=100μF. O que o cosseno na resposta diz sobre a fase entre V e I no capacitor?"*

#### Erros Comuns
- Esquecer regra da cadeia em e^(-t/τ) — derivada NÃO é e^(-t/τ), é (-1/τ)e^(-t/τ)
- Confundir d/dx[e^x]=e^x com d/dx[x^n]=nx^(n-1) (exponencial ≠ potência!)

---

### Módulo 0.9: Aplicações de Derivadas
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Máximo/mínimo**: f'(x)=0 e f''(x)<0 → máximo. f''(x)>0 → mínimo
- **Ponto de inflexão**: f''(x)=0 → curvatura muda de côncava para convexa
- **Linearização (Taylor 1ª ordem)**: f(x) ≈ f(a) + f'(a)(x-a) para x próximo de a
- **Taxa relacionada**: se V=IR e I muda no tempo, dV/dt = R×dI/dt (com R constante)
- **Aplicação em otimização**: máxima transferência de potência ocorre quando dP/dR_L=0

#### Projeto
1. **Otimização**: derive P_carga = V_th²×R_L/(R_th+R_L)² em relação a R_L. Iguale a zero → encontre R_L=R_th (máxima transferência de potência!)
2. **Linearização**: linearize a curva I-V do diodo (I=I_s×e^(V/nV_T)) em torno do ponto de operação
3. **Prompt IA**: *"Encontre o ponto de máxima potência de um painel solar cuja curva P-V é P=V(5-0.1V²). Use derivada para achar V_mp."*

---

### Módulo 0.10: Séries de Taylor e Aproximações
**Tempo: 2h**

#### 📚 O que memorizar
- **Taylor**: f(x) = f(a) + f'(a)(x-a) + f''(a)(x-a)²/2! + ...
- **Maclaurin (em torno de 0)**: e^x = 1+x+x²/2!+x³/3!+..., sin(x) ≈ x-x³/6, cos(x) ≈ 1-x²/2
- **Aproximações lineares úteis**: para x<<1: e^x≈1+x, sin(x)≈x, cos(x)≈1-x²/2, (1+x)ⁿ≈1+nx, ln(1+x)≈x
- **Convergência**: a série precisa convergir! Raio de convergência importa
- **Aplicação**: linearizar circuitos não-lineares (diodo, transistor) em torno do Q-point

#### Intuição
Taylor diz: "qualquer função suave pode ser aproximada por polinômios". Para valores PEQUENOS, basta o 1º termo (linear). sin(x)≈x para x pequeno é usado CONSTANTEMENTE: oscilações pequenas de um pêndulo, pequenos sinais em amplificadores. O "modelo de pequenos sinais" do transistor É uma linearização de Taylor!

#### Projeto
1. **Desmos**: plote sin(x) e suas aproximações de Taylor (1, 3, 5, 7 termos) → observe a convergência
2. **Calcule** e^0.1 usando Taylor com 1, 2, 3 termos → compare com valor real (erro?)
3. **Aplicação**: linearize I=I_s×e^(V/V_T) em torno de V=V_Q → obtenha modelo de pequenos sinais

#### Checkpoint — Cálculo I
- [ ] Calcula derivadas com regra da cadeia e do produto
- [ ] Encontra máximos/mínimos usando f'=0
- [ ] Lineariza funções usando Taylor de 1ª ordem
- [ ] Sabe que I=C×dV/dt e V=L×dI/dt e o que significam fisicamente

---

## Fase 3 — Cálculo II: Integrais

### Módulo 0.11: O Conceito de Integral
**Tempo: 2h**

#### 📚 O que memorizar
- **Integral = área sob a curva = acumulação**. ∫P(t)dt = energia. ∫I(t)dt = carga
- **Teorema Fundamental**: ∫ₐᵇ f(x)dx = F(b)-F(a), onde F'=f
- **Integrais básicas**: ∫k dt=kt, ∫tⁿ dt=tⁿ⁺¹/(n+1), ∫e^(at) dt=e^(at)/a, ∫sin(ωt) dt=-cos(ωt)/ω, ∫cos(ωt) dt=sin(ωt)/ω, ∫1/t dt=ln|t|
- **Integral definida vs indefinida**: definida (número) vs indefinida (função + C)
- **Conexão EE**: V_C = (1/C)∫I dt, Energia = ∫P(t)dt, Carga = ∫I(t)dt

#### Intuição
Se derivada="quão rápido?", integral="quanto no total?". A potência P(t) é taxa de consumo de energia. ∫P(t)dt é a energia total — é literalmente o que o medidor de energia da sua casa calcula para a conta de luz!

#### Projeto
1. **3 integrais manuais**: ∫₀¹ 3t² dt, ∫₀^(π/ω) sin(ωt) dt, ∫₀^∞ e^(-t/τ) dt
2. **Derive** E=½CV² calculando ∫₀^V CV dV (energia no capacitor)
3. **Python**: compare `scipy.integrate.quad()` com resultado analítico

---

### Módulo 0.12: Técnicas de Integração
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Substituição (u-sub)**: ∫f(g(x))g'(x)dx = ∫f(u)du. A "reversa" da regra da cadeia
- **Integração por partes**: ∫u dv = uv - ∫v du. Para ∫t×e^(-t)dt, ∫ln(t)dt, etc.
- **Frações parciais**: decompor 1/((s+1)(s+2)) = A/(s+1) + B/(s+2). Essencial para Laplace inversa!
- **Tabela de integrais**: na prática, engenheiros usam tabelas e computadores, não memorizam 200 integrais
- **SymPy**: `from sympy import *; integrate(exp(-t)*sin(t), t)` — resolve qualquer integral

#### Intuição
Frações parciais parece técnica obscura até o Pilar 5: TODA inversa de Laplace usa frações parciais para decompor G(s) em termos simples. Integração por partes aparece em demonstrações de energia e em processamento de sinais. Mas na PRÁTICA diária, use SymPy/Wolfram Alpha — o importante é RECONHECER quando cada técnica se aplica.

#### Projeto
1. **Substituição**: ∫sin(3t)×3 dt (u=3t)
2. **Por partes**: ∫t×e^(-t) dt (tabular method)
3. **Frações parciais**: decomponha 5/((s+1)(s+2)) e integre cada termo
4. **SymPy**: resolva 3 integrais difíceis automaticamente. Compare com manual

---

### Módulo 0.13: Aplicações de Integrais
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Valor médio**: V_avg = (1/T)∫₀ᵀ v(t) dt. Para senóide pura: V_avg = 0!
- **Valor RMS**: V_rms = √((1/T)∫₀ᵀ v²(t) dt). Para senóide: V_rms = V_p/√2 ≈ 0.707×V_p
- **Energia**: E = ∫₀ᵗ P(τ)dτ = ∫₀ᵗ v(τ)×i(τ) dτ
- **Comprimento de arco, área, volume**: aparecem em cálculo de capacitância de geometrias complexas
- **Valor eficaz de formas não-senoidais**: onda quadrada → V_rms = V_p (100% do pico!)

#### Projeto
1. **Derive V_rms = V_p/√2** analiticamente para v(t) = V_p×sin(ωt)
2. **Calcule V_rms** de onda quadrada e onda triangular — compare com senóide
3. **Energia**: calcule energia consumida por um chuveiro de 5500W em 15min, em J e kWh
4. **Prompt IA**: *"Por que a concessionária usa V_rms (127V) e não V_pico (179.6V)? Qual o significado físico do valor eficaz?"*

---

### Módulo 0.14: Integrais Impróprias e Métodos Numéricos
**Tempo: 2h**

#### 📚 O que memorizar
- **Integral imprópria**: limite infinito. ∫₀^∞ e^(-t/τ)dt = τ (converge!)
- **Convergência**: ∫₀^∞ e^(-at)dt converge se a>0. ∫₀^∞ 1/t dt diverge!
- **Métodos numéricos**: trapézios, Simpson, quadratura gaussiana
- **`scipy.integrate.quad()`**: calcula qualquer integral numericamente com precisão arbitrária
- **Monte Carlo**: integração por amostragem aleatória — prático para dimensões altas

#### Intuição
Integrais impróprias aparecem CONSTANTEMENTE: a energia total dissipada por um circuito RC durante a descarga é ∫₀^∞ P(t)dt. A transformada de Laplace É uma integral imprópria: F(s)=∫₀^∞ f(t)e^(-st)dt. Se a integral diverge, o sistema é instável!

#### Projeto
1. **Calcule** ∫₀^∞ e^(-t)dt manualmente e com Python
2. **Implemente** regra dos trapézios em Python (10 linhas) → compare com `quad()`
3. **Energia total**: calcule ∫₀^∞ I²(t)×R dt para I(t)=I₀e^(-t/τ) num circuito RL

#### Checkpoint — Cálculo II
- [ ] Calcula integrais básicas e aplica técnicas (substituição, partes, frações parciais)
- [ ] Calcula V_rms e valor médio de formas de onda
- [ ] Entende integrais impróprias e quando convergem
- [ ] Usa Python para integrais numéricas

---

## Fase 4 — Cálculo III: Multivariável e Vetorial

### Módulo 0.15: Derivadas Parciais e Gradiente
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Derivada parcial**: ∂f/∂x = derivada em relação a x tratando y como constante
- **Gradiente**: ∇f = (∂f/∂x, ∂f/∂y, ∂f/∂z) — vetor que aponta na direção de MÁXIMO crescimento
- **Campo elétrico**: E = -∇V (campo é o negativo do gradiente do potencial!)
- **Diferencial total**: df = (∂f/∂x)dx + (∂f/∂y)dy. Propagação de erros!
- **Regra da cadeia multivariável**: se f(x,y) e x=x(t), y=y(t): df/dt = (∂f/∂x)(dx/dt) + (∂f/∂y)(dy/dt)

#### Intuição
Na EE de uma variável, derivada dá a inclinação. Com múltiplas variáveis, o GRADIENTE dá a direção de máxima variação. O campo elétrico E = -∇V diz: "a direção da força sobre uma carga é a descida mais íngreme do potencial" — como uma bola rolando morro abaixo no "mapa topográfico" do potencial elétrico.

#### Projeto
1. **Calcule** ∂P/∂I e ∂P/∂R para P=I²R. Interprete: "como P muda se eu aumento SÓ I? E SÓ R?"
2. **Gradiente**: dado V(x,y)=kQ/√(x²+y²), calcule ∇V e mostre que E=-∇V aponta radialmente
3. **Propagação de erros**: se R=V/I, com V=12±0.1V e I=0.1±0.005A, qual a incerteza em R?
4. **Prompt IA**: *"Explique por que E=-∇V usando a analogia do mapa topográfico. Onde o campo é mais forte: onde as linhas equipotenciais estão próximas ou afastadas?"*

---

### Módulo 0.16: Integrais Múltiplas
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Integral dupla**: ∬f(x,y) dA — "volume sob a superfície" ou "soma sobre a área"
- **Integral tripla**: ∭f(x,y,z) dV — "soma sobre o volume"
- **Coordenadas cilíndricas**: (r,θ,z). dA=r dr dθ. Para problemas com simetria axial (fios, bobinas)
- **Coordenadas esféricas**: (r,θ,φ). dV=r²sinθ dr dθ dφ. Para cargas pontuais, antenas
- **Aplicações**: carga total Q=∬σ dA (distribuição superficial), capacitância de geometrias complexas

#### Projeto
1. **Calcule** a carga total numa placa circular com densidade σ(r) = σ₀×e^(-r/a) usando ∬σ r dr dθ
2. **Capacitor cilíndrico**: derive C = 2πεL/ln(b/a) usando integral em coordenadas cilíndricas
3. **Python**: use `scipy.integrate.dblquad()` para integrais duplas

---

### Módulo 0.17: Cálculo Vetorial — A Linguagem de Maxwell
**Tempo: 3h**

#### 📚 O que memorizar
- **Campo vetorial**: cada ponto do espaço tem um vetor associado (ex: E, B)
- **Divergência**: ∇·F = ∂Fx/∂x + ∂Fy/∂y + ∂Fz/∂z. Mede se o campo "emana" de um ponto (fonte) ou "converge" (sumidouro)
- **Rotacional**: ∇×F. Mede a "rotação" ou "circulação" do campo em torno de um ponto
- **Integral de linha**: ∮E·dl = trabalho ao longo do caminho. V = -∫E·dl (definição de tensão!)
- **Integral de superfície**: ∬B·dA = fluxo magnético Φ
- **Teorema de Gauss (divergência)**: ∯F·dA = ∭∇·F dV
- **Teorema de Stokes**: ∮F·dl = ∬(∇×F)·dA
- **Equações de Maxwell** (forma diferencial):
  1. ∇·E = ρ/ε₀ (Gauss elétrico — cargas criam campo E)
  2. ∇·B = 0 (Gauss magnético — não existem monopolos magnéticos)
  3. ∇×E = -∂B/∂t (Faraday — campo B variável cria campo E rotacional)
  4. ∇×B = μ₀J + μ₀ε₀∂E/∂t (Ampère-Maxwell — corrente e campo E variável criam campo B)

#### Intuição
As 4 equações de Maxwell descrevem TODO o eletromagnetismo — luz, rádio, motores, geradores, transformadores, antenas, fibra óptica. São o "código fonte" da natureza eletromagnética. Você NÃO precisa resolver as equações de Maxwell diretamente na maioria da EE, mas precisa ENTENDER o que cada uma diz para ter intuição sobre campos e ondas.

**∇·E = ρ/ε₀**: cargas elétricas são "fontes" de campo E (linhas emanam de +, convergem em -)
**∇·B = 0**: linhas de campo B são SEMPRE fechadas (sem início nem fim)
**∇×E = -∂B/∂t**: campo B variável cria E "circulante" → Faraday → indução → transformadores!
**∇×B = μ₀J + ...**: corrente cria B "circulante" → Ampère → eletroímãs, motores!

#### Projeto
1. **Visualize** campos vetoriais 2D no Python (quiver plot): campo E de carga pontual, campo B de fio reto
2. **Calcule divergência e rotacional** de F=(x,y,0) e F=(-y,x,0) — qual tem div≠0? Qual tem rot≠0?
3. **Conecte a Maxwell**: identifique qual equação de Maxwell está "por trás" de: (a) capacitor, (b) indutor, (c) transformador, (d) antena
4. **Prompt IA**: *"Explique as 4 equações de Maxwell em português simples, uma frase por equação, e dê um dispositivo EE que depende de cada uma."*

#### Checkpoint — Cálculo III
- [ ] Calcula derivadas parciais e gradiente
- [ ] Entende divergência e rotacional conceitualmente
- [ ] Sabe que E=-∇V e o que significa
- [ ] Conhece as 4 equações de Maxwell e o que cada uma "diz"

---

## Fase 5 — Equações Diferenciais

### Módulo 0.18: EDOs de 1ª Ordem — Circuitos RC e RL
**Tempo: 3h**

#### 📚 O que memorizar
- **EDO de 1ª ordem**: dy/dt + (1/τ)y = f(t). Solução: y(t) = y_∞ + (y₀ - y_∞)×e^(-t/τ)
- **Constante de tempo τ**: tempo para atingir 63% do final (ou cair a 37%). Em 5τ → 99%
- **RC**: τ = RC. **RL**: τ = L/R
- **Separável**: dy/y = -dt/τ → ln(y) = -t/τ + C → y = Ae^(-t/τ)
- **Fator integrante**: para dy/dt + P(t)y = Q(t), multiplique por e^(∫P dt)
- **Condição inicial**: determina a constante C. V_C(0) = 0 significa capacitor descarregado
- **Resposta natural** (homogênea): fonte=0, sistema evolui livre. **Forçada** (particular): resposta à excitação

#### Intuição
TODOS os sistemas de 1ª ordem se comportam IGUAL: exponencial que estabiliza. Carga RC, descarga RL, café esfriando, tanque esvaziando — mesma equação, mesma solução, diferente τ. A constante τ responde "quão rápido?": τ grande = lento, τ pequeno = rápido.

#### Projeto
1. **Monte a EDO** do circuito RC a partir de KVL: V=RI+V_C, I=C dV_C/dt → RC(dV_C/dt)+V_C=V
2. **Resolva** no papel (separação de variáveis) → V_C(t) = V(1-e^(-t/RC))
3. **Resolva com `sympy.dsolve()`** → compare
4. **Simule no Falstad**: RC com V=12V, R=1kΩ, C=100μF → τ=0.1s → meça e confirme
5. **Prompt IA**: *"Por que TODOS os circuitos RC (independente dos valores de R e C) têm a mesma forma de resposta exponencial? O que τ=RC controla?"*

---

### Módulo 0.19: EDOs de 2ª Ordem — Circuito RLC e Oscilações
**Tempo: 3h**

#### 📚 O que memorizar
- **EDO de 2ª ordem**: d²y/dt² + 2α(dy/dt) + ω₀²y = f(t)
- **Equação característica**: s² + 2αs + ω₀² = 0 → s = -α ± √(α²-ω₀²)
- **RLC série**: α = R/(2L), ω₀ = 1/√(LC)
- **3 casos**:

| Condição | Tipo | Raízes | Comportamento |
|----------|------|--------|---------------|
| α > ω₀ | Superamortecido | 2 reais negativas | Decai lento, sem oscilar |
| α = ω₀ | Criticamente amortecido | 1 real dupla | Decai mais rápido possível sem oscilar |
| α < ω₀ | Subamortecido | Complexas conjugadas | Oscila e decai |

- **Frequência de oscilação amortecida**: ω_d = √(ω₀²-α²)
- **Ressonância**: quando ω_excitação = ω₀ → amplitude máxima!

#### Intuição
É EXATAMENTE o sistema massa-mola com amortecedor! L=massa (inércia), C=mola (armazena), R=amortecedor (dissipa). Pouco amortecimento (R pequeno) → oscila muito. Muito amortecimento (R grande) → volta devagar sem oscilar. Ressonância = empurrar o balanço na frequência certa → amplitude cresce.

#### Projeto
1. **Calcule α e ω₀** para R=100Ω, L=10mH, C=1μF → determine o tipo
2. **Plote as 3 respostas** no Python para os 3 tipos (variando R)
3. **Simule no LTspice**: RLC série com degrau → observe sub/super/criticamente amortecido
4. **Ressonância**: aplique senóide de frequência variável → encontre f₀ onde |Z| é mínimo

#### Erros Comuns
- Confundir α com ω₀: α=R/(2L) é amortecimento, ω₀=1/√(LC) é freq natural
- Achar que criticamente amortecido é "melhor" — depende da aplicação!

---

### Módulo 0.20: Transformada de Laplace — Algébra no Lugar de Cálculo
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Definição**: F(s) = ∫₀^∞ f(t)e^(-st) dt (integral imprópria!)
- **Transformadas essenciais**: L{1}=1/s, L{e^(-at)}=1/(s+a), L{sin(ωt)}=ω/(s²+ω²), L{cos(ωt)}=s/(s²+ω²), L{t}=1/s², L{δ(t)}=1
- **Propriedade da derivada**: L{df/dt} = sF(s) - f(0⁻). EDO vira equação ALGÉBRICA!
- **Propriedade da integral**: L{∫f dt} = F(s)/s
- **Inversa**: decompor em frações parciais → tabela inversa
- **Função de transferência**: H(s) = V_out(s)/V_in(s). Polos = raízes do denominador = estabilidade!

#### Intuição
Laplace transforma CÁLCULO (derivadas, integrais, EDOs) em ÁLGEBRA (multiplicação, divisão, equações). É como traduzir um problema de chinês para português — o problema é o MESMO, mas num idioma que você domina. Todo o Pilar 5 é baseado em Laplace.

#### Projeto
1. **Resolva o circuito RC por Laplace**: V_in=degrau, transforme, resolva V_C(s), inverta
2. **Compare** com solução temporal direta — mesmo resultado, método mais simples!
3. **Polos**: encontre polos de H(s)=1/(s²+3s+2). Estável? Simule a resposta ao impulso
4. **SymPy**: `inverse_laplace_transform(1/(s*(1+s*0.001)), s, t)` → verifique

#### Checkpoint — Equações Diferenciais
- [ ] Resolve EDO de 1ª ordem (RC/RL) e identifica τ
- [ ] Classifica EDO de 2ª ordem (RLC) sem resolver: sub/super/criticamente amortecido
- [ ] Usa Laplace para converter EDO em álgebra
- [ ] Identifica polos e classifica estabilidade

---

## Fase 6 — Álgebra Linear

### Módulo 0.21: Vetores e Operações
**Tempo: 2h**

#### 📚 O que memorizar
- **Vetor**: magnitude + direção. v = (vx, vy, vz)
- **Soma**: componente a componente. **Módulo**: |v| = √(vx²+vy²+vz²)
- **Produto escalar**: a·b = |a||b|cos(θ) = axbx+ayby+azbz. Resultado: escalar. Mede "quanto a está na direção de b"
- **Produto vetorial**: a×b = |a||b|sin(θ)n̂. Resultado: vetor perpendicular. Mede "área do paralelogramo"
- **Aplicações**: F=qv×B (força magnética usa produto vetorial!), P=F·v (potência = produto escalar!)
- **Vetores unitários**: î, ĵ, k̂. Qualquer vetor = combinação linear de unitários

#### Projeto
1. **Calcule** a·b e a×b para a=(3,4,0), b=(1,2,5)
2. **Aplicação**: calcule a força magnética F=qv×B para q=1.6×10⁻¹⁹C, v=(10⁶,0,0)m/s, B=(0,0,0.5)T
3. **Prompt IA**: *"Por que a força magnética F=qv×B é um produto vetorial e não escalar? O que isso diz sobre a direção da força em relação à velocidade e ao campo?"*

---

### Módulo 0.22: Matrizes e Sistemas Lineares
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Forma matricial**: Ax = b. A=matriz dos coeficientes, x=incógnitas, b=constantes
- **Determinante 2×2**: det=ad-bc. det=0 → sistema impossível/indeterminado
- **Determinante 3×3**: expansão por cofatores (ou use Python)
- **Inversa**: A⁻¹ existe se det(A)≠0. x = A⁻¹b
- **Cramer**: xᵢ = det(Aᵢ)/det(A). Prático para 2×2 no papel
- **Eliminação gaussiana**: escalonamento → resolução. Funciona para qualquer tamanho
- **Conexão**: análise nodal/malhas SEMPRE dá sistema linear Ax=b

#### Intuição
A matriz é uma "foto numérica" do circuito. Na análise nodal, cada nó vira uma linha, cada conexão vira um coeficiente. A diagonal tem a soma das condutâncias do nó. Os termos fora da diagonal são negativos (condutâncias compartilhadas). `np.linalg.solve(A,b)` resolve circuitos de qualquer complexidade em milissegundos.

#### Projeto
1. **Monte Ax=b** para circuito de 2 malhas → resolva por Cramer no papel
2. **Monte Ax=b** para circuito de 3 nós → resolva com `np.linalg.solve()`
3. **Verifique** que det(A)≠0 → solução única → circuito bem definido

---

### Módulo 0.23: Autovalores, Autovetores e Estabilidade
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Autovalor λ**: solução de det(A-λI)=0. "Quanto A amplifica/atenua naquela direção"
- **Autovetor**: vetor v tal que Av = λv. Direção que não muda, só escala
- **Autovalor real negativo**: decaimento exponencial (estável)
- **Autovalor real positivo**: crescimento exponencial (INSTÁVEL!)
- **Autovalor complexo**: oscilação. Parte real <0 → decai. Parte real >0 → cresce (instável!)
- **Polos de G(s) = autovalores da matriz de estado**. Semiplano esquerdo = estável
- **Diagonalização**: A=PDP⁻¹. Desacopla o sistema em modos independentes

#### Intuição
Autovalores respondem A pergunta fundamental: **"o sistema é estável?"**. Todos negativos → perturbações decaem → estável. Algum positivo → perturbações crescem → explode. O Pilar 5 (Controle) é inteiramente sobre mover autovalores para onde queremos usando feedback.

#### Projeto
1. **Encontre polos** de G(s) = 1/(s²+3s+2) → s=-1, s=-2 → estável
2. **Encontre polos** de G(s) = 1/(s²-s+2) → parte real positiva? → instável
3. **Python**: `np.linalg.eig(A)` para matriz de circuito → interprete autovalores

#### Checkpoint — Álgebra Linear
- [ ] Opera com vetores (escalar, vetorial, módulo)
- [ ] Monta e resolve Ax=b para circuitos
- [ ] Encontra autovalores e classifica estabilidade
- [ ] Sabe que polos de G(s) = autovalores

---

## Fase 7 — Probabilidade e Estatística para Engenharia

### Módulo 0.24: Probabilidade e Distribuições
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Probabilidade**: P(A) = casos favoráveis / total. 0 ≤ P ≤ 1
- **Distribuição uniforme**: todos os valores igualmente prováveis. Ex: ruído de quantização do ADC
- **Distribuição Gaussiana (Normal)**: μ=média, σ=desvio padrão. 68% em ±1σ, 95% em ±2σ, 99.7% em ±3σ
- **Ruído elétrico**: modelado como Gaussiano! V_ruído ~ N(0, σ²)
- **SNR (Signal-to-Noise Ratio)**: SNR = P_sinal/P_ruído (em dB: 10log₁₀(P_s/P_n))
- **Probabilidade condicional**: P(A|B) = P(A∩B)/P(B). Base do teorema de Bayes

#### Intuição
Ruído é INEVITÁVEL em qualquer sistema real. Todo circuito tem ruído térmico (Johnson-Nyquist): V_rms = √(4kTRΔf). Saber probabilidade permite QUANTIFICAR o ruído e "design around it". SNR determina a qualidade de qualquer sinal: áudio hi-fi precisa de SNR>90dB.

#### Projeto
1. **Python**: gere 10.000 amostras Gaussianas → plote histograma → verifique regra 68-95-99.7
2. **Calcule** ruído térmico de R=10kΩ a T=300K, Δf=10kHz: V_rms = √(4×1.38×10⁻²³×300×10⁴×10⁴)
3. **SNR**: sinal de 1V_rms com ruído de 1mV_rms → SNR em dB?
4. **Prompt IA**: *"Explique por que um ADC de 10 bits tem SNR máximo de ~62dB. Como isso se relaciona com quantização?"*

---

### Módulo 0.25: Estatística Aplicada e Tolerâncias
**Tempo: 2h**

#### 📚 O que memorizar
- **Média**: μ = (1/N)Σxᵢ. **Desvio padrão**: σ = √((1/N)Σ(xᵢ-μ)²)
- **Tolerâncias de componentes**: resistores 1%, 5%, 10%. Capacitores 10%, 20%
- **Propagação de erros**: se f(x,y), σ_f² = (∂f/∂x)²σ_x² + (∂f/∂y)²σ_y² (soma em quadratura!)
- **Análise de pior caso**: use extremos de tolerância. Ex: R=1kΩ±5% → 950Ω a 1050Ω
- **Monte Carlo**: simule 10.000 circuitos com componentes aleatórios dentro da tolerância → histograma do resultado
- **6 Sigma**: 3.4 defeitos por milhão. Usado em fabricação de eletrônicos

#### Projeto
1. **Propagação de erros**: divisor de tensão com R1=10kΩ±5%, R2=10kΩ±5%. Qual a faixa de V_out?
2. **Monte Carlo em Python**: simule 10.000 divisores com R1,R2 = N(10k, 500) → histograma de V_out
3. **Compare**: pior caso vs Monte Carlo — qual é mais realista?
4. **Tipo de tolerância**: monte Carlo com distribuição uniforme vs Gaussiana → qual é mais realista para resistores?

#### Checkpoint — Probabilidade e Estatística
- [ ] Entende distribuição Gaussiana e regra 68-95-99.7
- [ ] Calcula SNR em dB
- [ ] Propaga erros/tolerâncias em circuitos
- [ ] Usa Monte Carlo para análise estatística

---

## Fase 8 — Física: Mecânica e Termodinâmica

### Módulo 0.26: Leis de Newton, Energia e Potência
**Tempo: 2.5h**

#### 📚 O que memorizar
- **1ª Lei (Inércia)**: corpo em repouso permanece em repouso. Análogo: indutor resiste a mudança de corrente!
- **2ª Lei**: F = ma. Análogo elétrico: V = L×dI/dt (tensão = "força" que muda a corrente)
- **3ª Lei**: ação e reação. Em circuitos: Lei de Lenz (reação se opõe à ação)
- **Trabalho**: W = F×d×cos(θ). Unidade: Joule [J]
- **Energia cinética**: Ec = ½mv². Análogo: E_indutor = ½LI²
- **Energia potencial**: Ep = mgh. Análogo: E_capacitor = ½CV²
- **Potência**: P = dW/dt = F×v. Em EE: P = V×I
- **Conservação de energia**: ΔEc + ΔEp = W_externo. Em circuitos: KVL!
- **Eficiência**: η = P_saída/P_entrada × 100%

#### Intuição
Mecânica e circuitos são GOVERNADOS pelas mesmas leis! F=ma (mecânica) ↔ V=LdI/dt (elétrica). Massa resiste a mudança de velocidade, indutância resiste a mudança de corrente. Mola armazena energia potencial, capacitor armazena energia elétrica. Atrito dissipa em calor, resistência dissipa em calor. A tabela de analogias é EXATA, não aproximada.

#### Projeto
1. **Monte a tabela** de analogias completa: F↔V, v↔I, m↔L, 1/k↔C, b↔R, Ec↔E_L, Ep↔E_C
2. **Calcule**: energia de um motor de 250W funcionando por 2h → E = P×t (em J e kWh)
3. **Eficiência**: motor com P_in=500W e P_mec=400W → η=80%, P_calor=100W

---

### Módulo 0.27: Rotação, Torque e Momento de Inércia
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Torque**: τ = r × F = r×F×sin(θ). Unidade: N·m. "Força que faz girar"
- **Momento de inércia** (J): resistência à aceleração angular. J_cilindro = ½mr²
- **2ª Lei rotacional**: τ = J×α (α = aceleração angular). Análogo: V = L×dI/dt
- **Potência rotacional**: P = τ×ω (ω em rad/s). Conexão motor: P_mec = τ×n×2π/60
- **Velocidade angular**: ω = 2πn/60 (n em RPM, ω em rad/s)
- **RPM↔rad/s**: 1800 RPM = 1800×2π/60 = 188.5 rad/s
- **Energia cinética rotacional**: E = ½J×ω²
- **Motor**: converte P_elétrica = V×I em P_mecânica = τ×ω. Perdas = calor

#### Intuição
Motores são o tema central do Pilar 4. A equação P = τ×ω determina TUDO: se precisa de mais torque com mesma potência, o motor gira mais devagar (engrenagem/redutor). RPM alto + torque baixo = carro em 5ª marcha. RPM baixo + torque alto = carro em 1ª marcha. Mesma potência, diferente ponto de operação!

#### Projeto
1. **Converta**: motor de 3CV (2237W), 1740RPM → calcule torque τ = P/ω
2. **Momento de inércia**: calcule J e energia cinética de roda de 5kg, r=15cm a 1000RPM
3. **Prompt IA**: *"Um motor de 1CV aciona uma esteira. Se dobrar a carga (torque), o que acontece com a velocidade para manter a potência constante? E com a corrente?"*

---

### Módulo 0.28: Oscilações e Ondas
**Tempo: 2.5h**

#### 📚 O que memorizar
- **MHS (Movimento Harmônico Simples)**: x(t) = A×sin(ωt+φ). ω = √(k/m) para mola, ω₀ = 1/√(LC) para RLC!
- **Período e frequência**: T = 2π/ω = 1/f
- **Amortecimento**: exponencial × senóide = oscilação que decai. Exatamente como RLC subamortecido!
- **Ressonância**: excitar na frequência natural → amplitude máxima. Base de filtros, antenas, rádio
- **Onda**: perturbação que se propaga. v = f×λ (velocidade = frequência × comprimento de onda)
- **Superposição**: ondas se somam linearmente. Interferência construtiva e destrutiva
- **Ondas estacionárias**: superposição de ondas viajando em sentidos opostos → nós e ventres
- **Ondas eletromagnéticas**: E e B perpendiculares, propagam-se a c = 3×10⁸ m/s

#### Projeto
1. **Python**: simule MHS amortecido x(t) = Ae^(-αt)sin(ω_d×t) para 3 valores de amortecimento
2. **Ressonância**: plote amplitude vs frequência de excitação → observe o pico em ω₀
3. **Ondas EM**: calcule λ para: FM 100MHz, WiFi 2.4GHz, luz visível 500THz

---

### Módulo 0.29: Termodinâmica e Circuitos Térmicos
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Temperatura**: medida de agitação molecular. K = °C + 273.15
- **Calor**: energia transferida por diferença de temperatura. Q = mcΔT
- **Condução**: Q̇ = kA(ΔT)/L. Análogo: I = (ΔV)/R → resistência térmica R_th = L/(kA)
- **1ª Lei**: ΔU = Q - W (energia conservada). Em EE: potência entra = potência sai + perdas
- **2ª Lei**: calor flui espontaneamente de quente para frio. Eficiência < 100%
- **Circuito térmico**: T_junção = T_ambiente + P × R_th. Essencial para dimensionar dissipadores!
- **Dissipador de calor**: R_th_total = R_th_junção-case + R_th_case-sink + R_th_sink-ar
- **Potência máxima**: T_j_max = T_amb + P_max × R_th_total → P_max = (T_j_max - T_amb)/R_th_total

#### Intuição
Dissipação térmica é o LIMITADOR prático de todo circuito de potência. Um MOSFET pode conduzir 100A, mas se não dissipar o calor I²R_DS(on), a temperatura da junção passa de 150°C e ele MORRE. O circuito térmico é IGUAL ao circuito elétrico: temperatura=tensão, fluxo de calor=corrente, resistência térmica=resistência. Se você entende Ohm, já entende térmica!

#### Projeto
1. **Circuito térmico**: MOSFET com R_DS(on)=50mΩ, I=10A, R_th_jc=1.5°C/W, R_th_cs=0.5°C/W. Qual R_th_sa máximo se T_amb=40°C, T_j_max=150°C?
2. **Analogia completa**: desenhe o circuito térmico como se fosse elétrico (fonte de corrente = potência, resistores = R_th, tensão = temperatura)
3. **Prompt IA**: *"Por que dissipadores de calor têm aletas? Explique usando o conceito de resistência térmica e área de superfície."*

#### Checkpoint — Mecânica e Térmica
- [ ] Conhece as analogias mecânica↔elétrica
- [ ] Calcula potência de motor: P = τ×ω
- [ ] Entende ressonância e amortecimento (base do RLC)
- [ ] Dimensiona dissipador usando circuito térmico

---

## Fase 9 — Física: Eletromagnetismo Completo

### Módulo 0.30: Eletrostática — Cargas, Campos e Potencial
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Carga**: Q em Coulombs. Elétron: q = -1.6×10⁻¹⁹ C. 1A = 1C/s
- **Lei de Coulomb**: F = kQ₁Q₂/r² (k = 9×10⁹ N·m²/C²)
- **Campo elétrico**: E = F/q = kQ/r² (V/m). Aponta de + para -
- **Lei de Gauss**: ∮E·dA = Q_enc/ε₀. Simetria → simplifica MUITO
- **Potencial**: V = kQ/r. V = -∫E·dl. E = -∇V
- **Capacitância**: C = Q/V. Placas paralelas: C = εA/d
- **Energia**: E = ½CV² = ½QV = Q²/(2C)
- **Dielétricos**: ε = ε_r × ε₀. Materiais entre as placas aumentam C por ε_r (ex: cerâmica ε_r≈1000!)
- **Densidade de energia**: u = ½εE² (J/m³) — energia armazenada no campo

#### Projeto
1. **Visualize**: linhas de campo de carga pontual, dipolo, placas paralelas (simulador)
2. **Gauss**: derive E de esfera carregada, cilindro infinito, plano infinito usando simetria
3. **Calcule C** de capacitor de placas 1cm×1cm, separação 1mm, dielétrico ε_r=100

---

### Módulo 0.31: Corrente, Resistência e Lei de Ohm Microscópica
**Tempo: 2h**

#### 📚 O que memorizar
- **Corrente**: I = dQ/dt = nqvₐA (n=densidade de portadores, vₐ=velocidade de deriva)
- **Velocidade de deriva**: surpreendentemente LENTA (~0.1mm/s para cobre com 1A)! O sinal é que viaja a ~c
- **Resistividade**: ρ (Ω·m). R = ρL/A. Cobre: ρ=1.7×10⁻⁸. Silício: ρ~10³
- **Efeito da temperatura**: R(T) = R₀(1 + α×ΔT). Metais: α>0 (R sobe). Semicondutores: α<0 (R desce!)
- **Efeito Joule**: P = I²R. Calor dissipado pela corrente passando por resistência
- **Densidade de corrente**: J = I/A = σE (σ = 1/ρ = condutividade). Lei de Ohm microscópica!
- **Supercondutividade**: ρ→0 abaixo da temperatura crítica. Corrente sem perdas!

#### Intuição
Lei de Ohm é um RESUMO estatístico de bilhões de elétrons colidindo com átomos. Cada colisão perde energia (calor). Mais colisões = mais resistência. Temperatura sobe = átomos vibram mais = mais colisões = R aumenta (metais). Semicondutores são o oposto: temperatura sobe = mais portadores livres = R diminui!

#### Projeto
1. **Calcule** R de um fio de cobre de 100m, 2.5mm² de seção → compare com tabela NBR 5410
2. **Temperatura**: fio começa com R=4Ω a 20°C (α=0.004/°C). Qual R a 80°C?
3. **Prompt IA**: *"Se elétrons se movem a ~0.1mm/s, como a lâmpada acende instantaneamente quando aperto o interruptor?"*

---

### Módulo 0.32: Magnetostática — Campos de Correntes
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Campo de fio reto**: B = μ₀I/(2πr). Regra da mão direita
- **Lei de Ampère**: ∮B·dl = μ₀I_enc. Análogo magnético da Lei de Gauss
- **Biot-Savart**: dB = (μ₀/4π) × (Idl×r̂)/r². Para geometrias sem simetria
- **Solenoide**: B = μ₀nI (n = espiras/metro). Campo uniforme DENTRO, ~zero FORA
- **Bobina (indutor)**: L = μ₀N²A/l. N=espiras, A=área, l=comprimento
- **Força entre fios**: F/l = μ₀I₁I₂/(2πd). Correntes paralelas=atraem. Antiparalelas=repelem
- **Materiais magnéticos**: μ = μ_r × μ₀. Ferro: μ_r ≈ 5000 → concentra campo!
- **Histerese**: B não segue H linearmente em materiais ferromagnéticos → perdas por histerese
- **Força em condutor**: F = BIL (motor!). Torque = NBAI×sin(θ) (motor DC!)

#### Projeto
1. **Calcule** B a 5cm de um fio com 10A
2. **Solenoide**: 500 espiras, 20cm, I=2A → calcule B interno e L
3. **Motor**: calcule torque de bobina 100 espiras, 5cm×5cm, I=1A, B=0.3T
4. **Prompt IA**: *"Por que o núcleo de ferro aumenta a indutância de uma bobina por ~5000×? O que são domínios magnéticos?"*

---

### Módulo 0.33: Indução Eletromagnética — Faraday e Lenz
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Lei de Faraday**: fem = -N×dΦ/dt. Variação de fluxo → tensão induzida
- **Fluxo magnético**: Φ = B×A×cos(θ). Unidade: Weber (Wb)
- **Lei de Lenz**: o negativo! Tensão induzida se opõe à variação (inércia magnética)
- **Maneiras de variar Φ**: mudar B (transformador), mudar A (pistão), mudar θ (gerador rotativo!)
- **fem de gerador**: fem(t) = NBAω×sin(ωt) — é uma SENÓIDE! Origem da AC!
- **V = L×dI/dt**: Faraday aplicado a uma bobina. V é proporcional à TAXA de variação de I
- **Indutância mútua**: M = k√(L₁L₂). k=coeficiente de acoplamento (0 a 1)
- **Transformador ideal**: V₁/V₂ = N₁/N₂, I₁/I₂ = N₂/N₁, P₁ = P₂
- **Correntes parasitas (Foucault)**: correntes induzidas em massas condutoras → perdas! Solução: laminação

#### Intuição
A fem do gerador é NBAω×sin(ωt) — esta é A ORIGEM de toda AC! Uma bobina girando num campo magnético produz NATURALMENTE uma senóide. Frequência = velocidade de rotação. No Brasil: gerador a 3600RPM com 2 polos → 60Hz. A rede elétrica inteira é uma senóide porque geradores giram!

#### Projeto
1. **Gerador**: N=100, B=0.5T, A=50cm², ω=377rad/s(60Hz) → calcule fem_pico
2. **Transformador no LTspice**: N₁=2200, N₂=110 → 220V→11V. Verifique relação de espiras
3. **Prompt IA**: *"Explique correntes de Foucault: por que núcleos de transformadores são laminados e não maciços? Calcule a redução de perdas."*

---

### Módulo 0.34: Equações de Maxwell e Ondas Eletromagnéticas
**Tempo: 2.5h**

#### 📚 O que memorizar

| # | Equação | Nome | Significado |
|---|---------|------|-------------|
| 1 | ∇·E = ρ/ε₀ | Gauss E | Cargas criam campo E (linhas emanam de +, convergem em -) |
| 2 | ∇·B = 0 | Gauss B | Não existem monopolos magnéticos (linhas de B são fechadas) |
| 3 | ∇×E = -∂B/∂t | Faraday | Campo B variável cria E rotacional → indução! |
| 4 | ∇×B = μ₀J + μ₀ε₀∂E/∂t | Ampère-Maxwell | Corrente E campo E variável criam B → ondas EM! |

- **Ondas EM**: combinar 3 e 4 → equação de onda! c = 1/√(μ₀ε₀) = 3×10⁸ m/s
- **Espectro**: rádio (kHz-GHz) → microondas → infravermelho → visível → UV → raios X → gama
- **Antena**: converte corrente oscilante em onda EM (e vice-versa). λ = c/f
- **Impedância do espaço livre**: Z₀ = √(μ₀/ε₀) ≈ 377Ω
- **Vetor de Poynting**: S = E×B/μ₀. Direção e magnitude do fluxo de energia EM

#### Intuição
Maxwell mostrou que luz, rádio, WiFi, raios X — TUDO é a mesma coisa: ondas eletromagnéticas, diferindo apenas em frequência. A 4ª equação (com o termo ε₀∂E/∂t adicionado por Maxwell) foi a chave: campo E variável cria B, que cria E, que cria B... → a onda se autopropaga pelo espaço a 300.000 km/s!

#### Projeto
1. **Calcule λ** para: AM 1000kHz, FM 100MHz, WiFi 2.4GHz, 5G 28GHz, luz 600nm
2. **Antena λ/4**: para FM 100MHz, qual o comprimento? Para WiFi? Compare com antenas reais
3. **Prompt IA**: *"Explique como Maxwell 'previu' as ondas EM matematicamente, antes de Hertz demonstrá-las experimentalmente. Qual o papel do termo ε₀∂E/∂t?"*

#### Checkpoint — Eletromagnetismo
- [ ] Calcula campos E e B de distribuições simples
- [ ] Aplica Faraday para calcular fem induzida
- [ ] Entende transformador, gerador e motor como aplicações de Faraday/Ampère
- [ ] Conhece as 4 equações de Maxwell e sabe de onde vem cada dispositivo EE

---

## Fase 10 — Física Moderna e Semicondutores

### Módulo 0.35: Mecânica Quântica para Engenheiros
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Quantização**: energia vem em "pacotes" (fótons). E = hf (h = 6.63×10⁻³⁴ J·s)
- **Dualidade onda-partícula**: elétrons se comportam como ondas (tunelamento, difração)
- **Modelo atômico**: elétrons em ORBITAIS (não órbitas!), cada orbital = nível de energia
- **Bandas de energia**: quando átomos se juntam, níveis viram BANDAS (banda de valência, banda de condução)
- **Gap (banda proibida)**: Eg = energia mínima para promover elétron de valência para condução
  - Metal: gap=0 (conduz sempre)
  - Semicondutor: gap pequeno (Si: 1.12eV, GaAs: 1.42eV) — conduz com energia suficiente
  - Isolante: gap grande (>3eV) — não conduz
- **Temperatura e condução**: em semicondutores, T↑ → mais elétrons saltam o gap → σ↑ → R↓ (oposto de metais!)

#### Intuição
Toda a eletrônica existe por causa do gap de 1.12eV do silício. Pequeno o suficiente para que dopagem e voltagem controlem a condução, grande o suficiente para que não conduza sozinho à temperatura ambiente. Se o gap fosse 0 (metal), transistores não existiriam. Se fosse 5eV (isolante), nada funcionaria. O silício é o "Goldilock" da eletrônica.

#### Projeto
1. **Calcule** energia de fóton de: luz vermelha (700nm), LED azul (470nm), raio X (0.1nm)
2. **Visualize**: diagrama de bandas para metal, semicondutor e isolante (desenhe os gaps)
3. **Prompt IA**: *"Explique por que LEDs de cores diferentes têm tensões de limiar diferentes (vermelho~1.8V, azul~3.3V). Conecte isso com E=hf e o gap do material."*

---

### Módulo 0.36: Semicondutores — De Átomos a Transistores
**Tempo: 2.5h**

#### 📚 O que memorizar
- **Silício intrínseco**: 4 elétrons de valência, cristal covalente, pouco condutivo
- **Dopagem tipo N**: adicionar fósforo (5 val.) → elétron "livre" extra → portadores majoritários = elétrons
- **Dopagem tipo P**: adicionar boro (3 val.) → "lacuna" → portadores majoritários = lacunas
- **Junção PN**: zona de depleção, barreira de potencial (~0.7V para Si)
  - Polarização direta (V>0.7V): corrente flui! I = I_s × (e^(V/nV_T) - 1)
  - Polarização reversa: só corrente de fuga (nA). Barreira AUMENTA
  - Breakdown: tensão reversa muito alta → avalanche → corrente dispara (Zener usa isso!)
- **V_T = kT/q ≈ 26mV** a 300K (tensão térmica) — aparece em TODA equação de semicondutores
- **Portadores minoritários**: poucos, mas importantes (corrente de fuga, transistor bipolar)

#### Intuição
A junção PN é o "coração" de toda eletrônica. É uma válvula de mão única para corrente elétrica. A zona de depleção é uma "terra de ninguém" sem portadores livres — funciona como isolante. Polarização direta "espreme" a zona → corrente flui. Reversa "alarga" → corrente bloqueada. É como uma porta giratória que só abre num sentido.

#### Projeto
1. **Desenhe**: diagrama de bandas da junção PN em equilíbrio, direta e reversa
2. **Plote** a equação do diodo I = I_s(e^(V/V_T)-1) para I_s=10nA, T=300K → observe a exponencial
3. **Prompt IA**: *"Explique por que V_limiar do diodo de silício é ~0.7V e do germânio é ~0.3V. Relacione com o gap do material e a tensão térmica V_T."*

#### Checkpoint Final — Base Completa!
- [ ] Entende bandas de energia e por que semicondutores são especiais
- [ ] Sabe o que é dopagem N e P e como forma a junção PN
- [ ] Entende a curva I-V do diodo e a equação exponencial
- [ ] Tem a base completa para o Pilar 2 (Eletrônica)

---

## Se Você Travar

| Problema | Solução |
|----------|--------|
| Prefixos confusos | μ=10⁻⁶, m=10⁻³, k=10³, M=10⁶. Mova vírgula em grupos de 3 |
| Complexos impossíveis | Assista [3Blue1Brown — What is i?](https://youtu.be/T647CGsuOVU). Complexos = rotações 2D |
| Derivada/integral abstratas | Derivada = "quão rápido?" Integral = "quanto total?" Só isso |
| EDO assusta | 1ª ordem = exponencial. 2ª ordem = pode oscilar. sympy.dsolve() faz o resto |
| Laplace sem sentido | Laplace traduz cálculo → álgebra. d/dt → s, ∫dt → 1/s |
| Matrizes confusas | `np.linalg.solve(A,b)` — resolva primeiro, entenda depois |
| Maxwell incompreensível | 4 frases: (1) cargas criam E, (2) B não tem fonte, (3) B variável cria E, (4) corrente cria B |
| Campo E vs B | E empurra cargas na direção do campo. B desvia cargas perpendicularmente |
| Gap de energia? | Metal=0 (conduz sempre), Semicondutor~1eV (controlável), Isolante>3eV (não conduz) |
| Térmica vs elétrica | Temperatura=tensão, fluxo calor=corrente, R_th=resistência. Mesma equação! |

> **Próximo passo**: [Pilar 1 — Fundamentos de Circuitos](ee_circuitos_roadmap.md) — com toda a base matemática e física, você está pronto para construir e analisar circuitos!
