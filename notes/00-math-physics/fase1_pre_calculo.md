# Módulo 0 — Matemática/Física: Fase 1 — Pré-Cálculo
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 0.1 — Unidades SI, Prefixos e Análise Dimensional

### Prefixos SI

- pico (p) <> {{10⁻¹²}}
- nano (n) <> {{10⁻⁹}}
- micro (μ) <> {{10⁻⁶}}
- mili (m) <> {{10⁻³}}
- kilo (k) <> {{10³}}
- mega (M) <> {{10⁶}}
- giga (G) <> {{10⁹}}
- Confundir m (mili) com M (mega) >> Erro de {{10⁹}} vezes!

### Unidades Derivadas SI

- Volt [V] <> {{W/A}} = J/C
- Ohm [Ω] <> {{V/A}}
- Farad [F] <> {{A·s/V}}
- Henry [H] <> {{V·s/A}}
- Watt [W] <> {{V·A}} = J/s
- Coulomb [C] <> {{A·s}}
- Hertz [Hz] <> {{1/s}}
- Siemens [S] <> {{A/V}} = 1/Ω

### Análise Dimensional — Conceito

- O que é análise dimensional? >> Verificar se as {{unidades}} dos dois lados da equação são {{iguais}}. Se não batem, há erro.
- Regra do engenheiro >> SEMPRE verifique {{unidades}} do resultado final

### Provas Dimensionais Fundamentais

- τ = RC → [Ω]·[F] = >> {{[V/A]·[A·s/V] = [s]}} OK
- P = V²/R → [V²]/[Ω] = >> {{[V·A] = [W]}} OK
- I = C·dV/dt → [F·V/s] = >> {{[A]}} OK
- V = L·dI/dt → [H·A/s] = >> {{[V]}} OK
- E = ½CV² → [F·V²] = >> {{[W·s] = [J]}} OK
- E = ½LI² → [H·A²] = >> {{[W·s] = [J]}} OK
- f = 1/(2π√LC) → 1/√[H·F] = >> {{[1/s] = [Hz]}} OK

Mnemônicos para lembrar unidades derivadas:
- [Ω] = [V/A] ← vem direto de V = IR → R = V/I
- [F] = [A·s/V] ← vem de Q = CV → C = Q/V = A·s/V
- [H] = [V·s/A] ← vem de V = L·dI/dt → L = V·dt/dI

### Notação

- Notação de engenharia vs científica >> Expoente sempre {{múltiplo de 3}} (alinha com prefixos SI)

### Drill — Conversões Rápidas #[[Drill]]

- 2200 μF = ? mF >> {{2.2}} mF
- 3.3 kΩ = ? Ω >> {{3300}} Ω
- 16 MHz = ? Hz >> {{16 × 10⁶}} Hz
- 47 nF = ? μF >> {{0.047}} μF
- 680 pF = ? nF >> {{0.68}} nF
- 100 mA = ? A >> {{0.1}} A
- 2.2 μH = ? H >> {{2.2 × 10⁻⁶}} H
- 10 GHz = ? MHz >> {{10000}} MHz
- 330 kΩ = ? MΩ >> {{0.33}} MΩ
- 0.01 μF = ? nF >> {{10}} nF
- 5600 pF = ? nF >> {{5.6}} nF
- 750 mW = ? W >> {{0.75}} W

### Drill — Provas Dimensionais Extras #[[Drill]]

- P = I²R → [A²·Ω] = >> {{[A·V] = [W]}} OK
- Q = CV → [F·V] = >> {{[A·s] = [C]}} OK
- ω₀ = 1/√(LC) → 1/√[H·F] = >> {{[1/s] = [rad/s]}} OK

---

## Módulo 0.2 — Álgebra: Equações e Isolamento

### Lei de Ohm e Potência — Isolamento

- V = IR → I = >> {{V/R}}
- V = IR → R = >> {{V/I}}
- P = VI → I = >> {{P/V}}
- P = VI → V = >> {{P/I}}
- P = V²/R → R = >> {{V²/P}}
- P = I²R → I = >> {{√(P/R)}}

### Equação de 2º Grau

- Fórmula de Bhaskara >> x = {{(-b ± √(b²-4ac)) / 2a}}
- Δ > 0 → >> {{2 raízes reais}} → superamortecido
- Δ = 0 → >> {{Raiz dupla}} → criticamente amortecido
- Δ < 0 → >> {{Raízes complexas}} → oscilação!

Δ < 0 NÃO significa "sem solução" — significa raízes complexas = circuito oscila. É um dos erros mais comuns de quem vem do ensino médio.

### Isolamento de Fórmulas EE

- f = 1/(2π√LC) → C = >> {{1/(4π²f²L)}}
- f = 1/(2π√LC) → L = >> {{1/(4π²f²C)}}
- V_out = V_in·R2/(R1+R2) → R2 = >> {{V_out·R1 / (V_in - V_out)}}
- V_rms = V_p/√2 → V_p = >> {{V_rms·√2}}
- ω = 2πf → f = >> {{ω/(2π)}}
- τ = RC → R = >> {{τ/C}}
- τ = L/R → L = >> {{τ·R}}

### Sistemas Lineares

- Regra de Cramer 2×2: x = >> {{det(Aₓ)/det(A)}}
- det(A) = 0 significa >> Sistema {{impossível ou indeterminado}}

### Drill — Isolamento Rápido #[[Drill]]

- Q = CV → C = >> {{Q/V}}
- Z = V/I → I = >> {{V/Z}}
- τ = RC → C = >> {{τ/R}}

---

## Módulo 0.3 — Funções: Reconhecimento Rápido

### Tipos de Função em EE

- V = IR (R fixo). Tipo? >> {{Linear}}
- P = I²R. Tipo? >> {{Quadrática}} em I
- I = V/R (V fixo). Tipo? >> {{Inversa}} (hipérbole)
- V_C = V₀·e^(-t/τ). Tipo? >> {{Exponencial decrescente}}
- V_C = V_f(1-e^(-t/τ)). Tipo? >> {{Exponencial que satura}}
- v(t) = V_p·sin(ωt). Tipo? >> {{Senoidal}}

### Análise Proporcional

- P = I²R. I dobra → P >> {{quadruplica}} (4×)
- P = V²/R. V dobra → P >> {{quadruplica}} (4×)
- P = V²/R. R dobra → P >> {{cai pela metade}}
- f = 1/(2π√LC). C quadruplica → f >> {{cai pela metade}} (√4=2)

Reconhecer o tipo de função olhando a fórmula é "superpoder": P = I²R → "quadrática em I", V_C = V_f(1-e^(-t/τ)) → "exponencial que estabiliza". Saber isso ANTES de calcular é o que separa engenheiro de calculadora.

### Paridade

- cos(-x) = cos(x) → cosseno é >> {{par}}
- sin(-x) = -sin(x) → seno é >> {{ímpar}}

---

## Módulo 0.4 — Trigonometria: A Linguagem AC

### Definições

- sin(θ) <> {{oposto / hipotenusa}}
- cos(θ) <> {{adjacente / hipotenusa}}
- tan(θ) <> {{sin(θ) / cos(θ)}}

### Valores Notáveis

- sin(30°) = sin(π/6) <> {{1/2}}
- sin(45°) = sin(π/4) <> {{√2/2}}
- sin(60°) = sin(π/3) <> {{√3/2}}
- sin(90°) = sin(π/2) <> {{1}}
- cos(0°) <> {{1}}
- cos(90°) = cos(π/2) <> {{0}}

Valores triviais: sin(0°) = 0, cos(180°) = -1, cos(60°) = sin(30°) = 1/2 — inferíveis sem card.

Mnemônico: sin para 0°, 30°, 45°, 60°, 90° = √n/2 com n de 0 a 4 → √0/2=0, √1/2=0.5, √2/2≈0.707, √3/2≈0.866, √4/2=1

### Radianos

- 2π rad <> {{360°}}
- π rad <> {{180°}}
- π/2 rad <> {{90°}}
- π/3 rad <> {{60°}}
- π/4 rad <> {{45°}}
- π/6 rad <> {{30°}}
- Por que engenharia usa radianos? >> Porque ω = 2πf fica em {{rad/s}} e derivadas de sin/cos só funcionam em radianos

Armadilha: sin(90) com calculadora em RAD ≈ 0.894, não 1! Sempre cheque o modo.

### Identidades Essenciais

- sin²θ + cos²θ = >> {{1}}
- sin(A+B) = >> {{sinA·cosB + cosA·sinB}}
- cos(A+B) = >> {{cosA·cosB - sinA·sinB}}
- sin(2A) = >> {{2sinA·cosA}}
- cos(2A) = >> {{cos²A - sin²A}}

### Senóide AC

- v(t) = V_p·sin(ωt + φ): V_p = >> {{Amplitude}} (pico)
- v(t) = V_p·sin(ωt + φ): ω = >> {{Frequência angular}} [rad/s]
- v(t) = V_p·sin(ωt + φ): φ = >> {{Fase inicial}} [rad]
- Período T = >> {{1/f}} = 2π/ω

### Defasagem V/I — ELI the ICE man

- ELI: no indutor (L), E(tensão) >> {{adianta}} I em 90°
- ICE: no capacitor (C), I >> {{adianta}} E(tensão) em 90°

Intuição: cada sinal AC = ponto girando num círculo. Frequência = velocidade de rotação. Amplitude = raio. Fase = ponto de partida.

### Drill — Conversão Graus/Radianos #[[Drill]]

- 1 rad ≈ ? graus >> {{57.3°}}
- 120° = ? rad >> {{2π/3}}
- 270° = ? rad >> {{3π/2}}
- 5π/6 rad = ? graus >> {{150°}}

---

##  Módulo 0.5 — Números Complexos: O Motor da Análise AC

### Fundamentos

- Em EE, j = √(-1) porque >> {{i}} já é {{corrente}}
- j² = >> {{-1}}
- j⁴ = >> {{1}}
- 1/j = >> {{-j}}

### Formas de Representação

- Retangular: z = >> {{a + jb}}
- Polar: z = >> {{r∠θ}} = r·e^(jθ)
- Módulo |z| = >> {{√(a² + b²)}}
- Ângulo θ = >> {{arctan(b/a)}} — verifique o quadrante!
- Polar→Retangular: a = {{r·cosθ}}, b = {{r·sinθ}}

### Operações

- Soma de complexos: forma >> {{retangular}}
- Multiplicação de complexos: forma >> {{polar}}
- r₁∠θ₁ × r₂∠θ₂ = >> {{r₁r₂ ∠(θ₁+θ₂)}}
- r₁∠θ₁ / r₂∠θ₂ = >> {{(r₁/r₂) ∠(θ₁-θ₂)}}

Somar em polar = ERRADO! Sempre converta para retangular antes de somar. arctan retorna o mesmo valor para quadrantes opostos — sempre confira o sinal de a e b.

### Euler e Conjugado

- e^(jθ) = >> {{cosθ + j·sinθ}}
- Conjugado z* de (a+jb) = >> {{a - jb}}
- z·z* = >> {{|z|²}}

### Impedâncias Complexas

- Z do resistor <> {{R}}
- Z do indutor <> {{jωL}}
- Z do capacitor <> {{1/(jωC)}} = -j/(ωC)
- Z_RLC série <> {{R + j(ωL - 1/ωC)}}
- Ressonância série: ωL = 1/(ωC) → ω₀ = >> {{1/√(LC)}}

Sem complexos, análise AC requer equações diferenciais. Com complexos (fasores), AC vira álgebra: V = Z·I. Essa é a razão #1 para aprender complexos na EE.

### Drill — Potências de j #[[Drill]]

- j¹ = >> {{j}}
- j³ = >> {{-j}}
- j⁵ = >> {{j}} (ciclo de 4)
- j⁻¹ = >> {{-j}}

---

## Módulo 0.6 — Logaritmos, Exponenciais e Decibéis

### Constante e

- e ≈ >> {{2.71828}}
- O que torna e especial? >> d/dx[eˣ] = {{eˣ}} — própria derivada!

Onde e aparece na EE: carga/descarga RC/RL, Laplace, Fourier, resposta transitória — em praticamente toda análise dinâmica.

### Propriedades de Log

- log(A·B) = >> {{logA + logB}}
- log(A/B) = >> {{logA - logB}}
- log(Aⁿ) = >> {{n·logA}}
- ln(eˣ) <> {{x}}
- e^(lnx) <> {{x}}
- ln(1) <> {{0}}
- ln(e) <> {{1}}

### Decibéis

- dB de potência <> {{10·log₁₀(P₂/P₁)}}
- dB de tensão <> {{20·log₁₀(V₂/V₁)}}
- Por que 20 para tensão? >> P ∝ V², logo 10·log(V²) = {{20·logV}}

### Tabela dB — DECORAR

- +3 dB potência <> {{2×}} potência
- -3 dB potência <> {{½}} potência (freq. de corte!)
- +6 dB tensão <> {{2×}} tensão
- -6 dB tensão <> {{½}} tensão
- +10 dB potência <> {{10×}} potência
- +20 dB tensão <> {{10×}} tensão
- -20 dB tensão <> {{0.1×}} tensão
- -40 dB tensão <> {{0.01×}} tensão
- 0 dB <> ganho = {{1×}}

Escala log comprime 1 Hz a 1 GHz (9 décadas) num gráfico legível — essencial para Bode.

### Drill — Problemas de dB #[[Drill]]

- Amp +26dB → Filtro -12dB → Amp +10dB = ? dB >> {{+24 dB}}
- +24 dB tensão ≈ ? vezes >> {{15.85×}}
- 1mV → amp +60dB → saída? >> {{1 V}}
- 5V → 0.5V: atenuação em dB? >> {{-20 dB}}
- +3dB +3dB +3dB potência = ? >> {{+9 dB}} = 8× potência

### Drill — Log Básico #[[Drill]]

- log₁₀(100) = >> {{2}}
- log₁₀(1000) = >> {{3}}
- log₁₀(0.1) = >> {{-1}}
- log₁₀(0.01) = >> {{-2}}
