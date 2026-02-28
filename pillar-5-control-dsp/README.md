# Pilar 5: Controle, Sinais e DSP

> **Sobre este pilar**: Este é o pilar mais abstrato e mais poderoso. Aqui você aprende a fazer **qualquer sistema** se comportar exatamente como deseja — temperatura, velocidade, posição, pressão. A ferramenta é a matemática (Laplace, Fourier, Bode), mas o resultado é profundamente prático.
>
> **Ferramentas**: [GNU Octave](https://octave.org/) (gratuito, compatível com MATLAB) + Python (`scipy`, `control`, `matplotlib`) + [Simulink](https://www.mathworks.com/products/simulink.html) (ou Xcos do Scilab, gratuito)
>
> **Pré-requisitos**: [Pilar 0](ee_matematica_fisica_roadmap.md) Fases 2-3 (Cálculo I-II para Fourier), Fase 5 (EDOs e Laplace para modelagem), Fase 6 (álgebra linear para estabilidade). [Pilar 1](ee_circuitos_roadmap.md) completo (circuitos RC/RL/RLC como plantas). [Pilar 2](ee_eletronica_roadmap.md) e [Pilar 4](ee_eletrotecnica_automacao_roadmap.md) recomendados (os sistemas a controlar vêm de lá).
>
> **Conexões com outros pilares**:
> - **Base de**: [Pilar 0](ee_matematica_fisica_roadmap.md) — Laplace (Mód 0.20), EDOs (Mód 0.18-0.19 → transitórios), integrais (Mód 0.11-0.14 → Fourier), autovalores (Mód 0.23 → estabilidade), probabilidade (Mód 0.24-0.25 → ruído/DSP)
> - **Plantas de**: [Pilar 1](ee_circuitos_roadmap.md) — RC/RL/RLC como sistemas a modelar. [Pilar 4](ee_eletrotecnica_automacao_roadmap.md) — motor de indução como planta de controle de velocidade
> - **Implementação**: [Pilar 3](ee_digital_embarcados_roadmap.md) Módulos 3.10-3.11 (ADC/PWM para controle digital), [Lab L.19](ee_laboratorio_real_roadmap.md) Opção C (PID real com ESP32)
> - **Segurança**: [HH 7.1-7.3](hardware_hacking_roadmap.md) (side-channel = análise de sinais!), [HH Avançado E.1](hardware_hacking_roadmap_avancado.md) (matemática para SCA usa mesma base de correlação/estatística)
>
> [Voltar ao Índice](ee_roadmap_index.md)

---

## Visão Geral

| Fase | Módulos | Foco | Horas |
|------|---------|------|-------|
| **Sinais e Sistemas** | 5.1–5.3 | Sinais, convolução, Fourier, frequência | ~8h |
| **Modelagem e Laplace** | 5.4–5.6 | Transformada de Laplace, funções de transferência, blocos | ~7h |
| **Análise e Projeto** | 5.7–5.10 | Estabilidade, Bode, Nyquist, PID | ~10h |
| **Avançado** | 5.11–5.12 | Lugar das raízes, introdução a controle digital | ~5h |

---

## Fase 1 — Sinais e Sistemas

### Módulo 5.1: Sinais — Classificação e Operações
**Tempo: 2.5h**

#### O que memorizar
- **Sinal contínuo**: definido para todo instante de tempo. Ex: tensão analógica, v(t)
- **Sinal discreto**: definido apenas em instantes espaçados. Ex: amostras digitais, x[n]
- **Sinais fundamentais**:
  - **Degrau unitário** u(t): 0 para t<0, 1 para t≥0. Representa "ligar" algo
  - **Impulso** δ(t): infinito em t=0, zero em todo resto, integral = 1. "Pancada instantânea"
  - **Rampa** r(t) = t × u(t): cresce linearmente. Representa velocidade constante
  - **Exponencial** e^(at): crescente (a>0) ou decrescente (a<0). Circuitos RC!
  - **Senoidal**: A × sen(ωt + φ). Sinal mais importante de todos (Fourier!)
- **Operações**: deslocamento temporal x(t-t₀), escala x(at), inversão x(-t), soma, multiplicação
- **Periodicidade**: x(t) = x(t+T) para todo t. T = período, f = 1/T, ω = 2πf
- **Energia e Potência**: sinal de energia (∫|x(t)|² dt = finito) vs potência (`lim 1/T ∫|x(t)|² dt = finito`)

#### Intuição
Sinais são simplesmente **funções que representam informação variando no tempo**. A tensão na tomada é um sinal (senoide). A temperatura do forno é um sinal (exponencial decrescente quando desliga). O áudio do microfone é um sinal (combinação de senóides). A engenharia de controle e sinais trata de **manipular esses sinais**: filtrá-los (remover ruído), amplificá-los, transformá-los, e usá-los para controlar sistemas.

O degrau unitário é o sinal mais importante para controle — quando você "liga" algo (muda o setpoint), está aplicando um degrau. A resposta ao degrau revela TUDO sobre como o sistema se comporta.

#### Projeto: "Sinais no Python"
1. **Instale ferramentas**:
   ```bash
   pip3 install numpy scipy matplotlib control
   ```
2. **Plote sinais fundamentais** com matplotlib:
   ```python
   import numpy as np
   import matplotlib.pyplot as plt
   
   t = np.linspace(-2, 5, 1000)
   
   # Degrau
   u = np.heaviside(t, 1)
   # Rampa
   r = t * u
   # Exponencial decrescente
   exp_dec = np.exp(-t) * u
   # Senóide
   sen = np.sin(2*np.pi*1*t)  # 1 Hz
   
   fig, axes = plt.subplots(2, 2, figsize=(10, 8))
   axes[0,0].plot(t, u); axes[0,0].set_title('Degrau u(t)')
   axes[0,1].plot(t, r); axes[0,1].set_title('Rampa r(t)')
   axes[1,0].plot(t, exp_dec); axes[1,0].set_title('Exponencial e^{-t}')
   axes[1,1].plot(t, sen); axes[1,1].set_title('Senóide')
   plt.tight_layout(); plt.show()
   ```
3. **Operações**: desloque, escale e inverta each sinal — plote original e modificado
4. **Calcule energia** de e^(-t)×u(t): ∫₀^∞ e^(-2t) dt = 1/2 (sinal de energia)
5. **Entregável**: Script Python com 4 sinais + operações + cálculos de energia

---

### Módulo 5.2: Convolução — A Operação Fundamental
**Tempo: 2.5h**

#### O que memorizar
- **Convolução**: `y(t) = ∫ x(τ) × h(t-τ) dτ` = x(t) * h(t). Operação que define a saída de um sistema linear
- **Resposta ao impulso h(t)**: saída quando a entrada é δ(t). DEFINE completamente o sistema!
- **Propriedades**: comutativa (x*h = h*x), associativa, distributiva
- **LTI (Linear Time-Invariant)**: sistemas onde convolução funciona. Linearidade + invariância no tempo
- **Convolução com impulso**: x(t) * δ(t) = x(t) (identidade)
- **Convolução com degrau**: x(t) * u(t) = ∫₋∞^t x(τ) dτ (integral acumulada = "running average")
- **Convolução discreta**: y[n] = Σ x[k] × h[n-k]

#### Intuição
Convolução é como uma **impressão digital do sistema**. Se você der uma "pancada" (impulso) num sistema e gravar a resposta (h(t)), essa resposta contém TODA a informação sobre como o sistema transforma sinais. Para qualquer outra entrada x(t), a saída é a convolução de x com h.

**Analogia acústica**: bata palmas numa sala vazia → o eco é a h(t) da sala. Agora, se você tocar música (x(t)) nessa sala, o som que ouve é a convolução da música com o eco da sala. Sistemas de reverb digital fazem EXATAMENTE isso: convolvem o áudio com a resposta ao impulso de uma catedral/estúdio.

#### Projeto: "Convolução Visual"
1. **No Python**, implemente convolução "na mão":
   ```python
   import numpy as np
   
   def convolve_manual(x, h, dt):
       N = len(x) + len(h) - 1
       y = np.zeros(N)
       for n in range(N):
           for k in range(len(x)):
               if 0 <= n-k < len(h):
                   y[n] += x[k] * h[n-k] * dt
       return y
   ```
2. **Convolva** degrau com exponencial decrescente → observe a saída (carga de capacitor!)
3. **Compare** com `np.convolve()` — mesmo resultado, mas 1000× mais rápido
4. **Circuito RC**: h(t) = (1/RC)×e^(-t/RC). Convolva com degrau → V_capacitor(t). Compare com fórmula analítica
5. **Entregável**: Implementação de convolução + aplicação em circuito RC + visualizações

---

### Módulo 5.3: Transformada de Fourier — O Mundo das Frequências
**Tempo: 3h**

#### O que memorizar
- **Série de Fourier**: qualquer sinal periódico = soma infinita de senóides. `x(t) = a₀ + Σ(aₙcos(nωt) + bₙsen(nωt))`
- **Transformada de Fourier**: versão contínua. `X(f) = ∫ x(t) × e^(-j2πft) dt`. Decompõe sinal em frequências
- **Espectro**: gráfico |X(f)| vs f. Mostra "quanta energia" em cada frequência
- **Transformadas fundamentais**: δ(t) ↔ 1, u(t) ↔ 1/(j2πf) + ½δ(f), e^(-at)u(t) ↔ 1/(a+j2πf)
- **Propriedades**: linearidade, deslocamento no tempo = fase, convolução no tempo = multiplicação na freq!
- **DFT/FFT**: versão computacional. FFT = algoritmo rápido (O(N log N)). Usado no mundo real
- **Frequência de Nyquist**: f_s/2. Acima disso → aliasing

#### Intuição
Fourier é a **ideia mais poderosa de toda a engenharia**. Diz que QUALQUER sinal, por mais complicado que seja, é apenas uma soma de senóides de diferentes frequências. Um acorde de piano? É a soma da fundamental e dos harmônicos de cada nota. O sinal da rede elétrica poluído? É 60Hz puro + harmônicos de 180Hz, 300Hz, etc.

**Convolução no tempo = multiplicação na frequência!** Isso é revolucionário: em vez de calcular convolução (difícil), você transforma para frequência (Fourier), multiplica (fácil), e transforma de volta. É assim que filtros digitais funcionam.

#### Projeto: "Análise Espectral"
1. **Gere sinais** em Python: senóide pura, onda quadrada, sinal de áudio
2. **Calcule FFT** e plote espectro:
   ```python
   from scipy.fft import fft, fftfreq
   
   # Onda quadrada = fundamental + harmônicos ímpares
   t = np.linspace(0, 1, 44100)
   square = np.sign(np.sin(2*np.pi*440*t))  # 440Hz (Lá musical)
   
   X = fft(square)
   freqs = fftfreq(len(t), 1/44100)
   
   plt.plot(freqs[:len(freqs)//2], np.abs(X[:len(X)//2]))
   plt.xlabel('Frequência (Hz)'); plt.ylabel('|X(f)|')
   plt.title('Espectro da Onda Quadrada 440Hz')
   plt.xlim(0, 5000)
   plt.show()
   # Observe: picos em 440, 1320, 2200, 3080 Hz (harmônicos ímpares!)
   ```
3. **Filtragem**: remova harmônicos acima de 2kHz → ouça a diferença (mais "suave")
4. **Análise de sinal da rede**: gere 60Hz + harmônicos 3º(180Hz) e 5º(300Hz) → identifique no espectro
5. **Prompt IA**: *"Explique intuitivamente o que a Transformada de Fourier FAZ: por que decompor um sinal em senóides é útil? Dê 3 aplicações práticas reais (compressão de áudio, diagnóstico de máquinas, telecomunicações)."*
6. **Entregável**: FFT de 3 sinais + filtragem + análise

#### Erros Comuns
- Confundir FFT com DFT — FFT é apenas um ALGORITMO rápido para calcular a DFT (O(N log N) vs O(N²))
- Esquecer que |X(f)| é simétrico para sinais reais — só metade do espectro (0 a f_s/2) contém informação útil
- Não entender aliasing: se f_sinal > f_s/2, a frequência "dobra" e aparece errada no espectro

#### Checkpoint — Fim da Fase Sinais
- [ ] Classifica sinais e calcula energia/potência
- [ ] Entende convolução e calcula saída de sistema LTI
- [ ] Calcula FFT e interpreta espectro de frequências
- [ ] Sabe que convolução no tempo = multiplicação na frequência

---

## Fase 2 — Modelagem e Transformada de Laplace

### Módulo 5.4: Transformada de Laplace — A Ferramenta Universal
**Tempo: 2.5h**

#### O que memorizar
- **Laplace**: `F(s) = ∫₀^∞ f(t) × e^(-st) dt`, onde s = σ + jω (variável complexa)
- **Transformadas essenciais**: 1 ↔ 1/s, t ↔ 1/s², e^(-at) ↔ 1/(s+a), sen(ωt) ↔ ω/(s²+ω²), cos(ωt) ↔ s/(s²+ω²)
- **Propriedades**: linearidade, L{f'(t)} = sF(s) - f(0⁻) (derivada → multiplicação por s!), L{∫f(t)} = F(s)/s
- **Transformada inversa**: frações parciais → tabela de transformadas → f(t)
- **s = jω**: se substituir s = jω, Laplace vira Fourier! São a mesma ferramenta

#### Intuição
Laplace faz para equações diferenciais o que logaritmos fazem para multiplicação: transforma algo difícil em algo fácil. Equação diferencial (cálculo, difícil) → Transformada → Equação algébrica (álgebra, fácil) → Resolve → Transformada inversa → Solução.

A "mágica": derivação no tempo vira multiplicação por s. Integração vira divisão por s. O circuito RC no tempo precisa de equação diferencial. Em Laplace: V_C(s) = V_in(s) × 1/(1+sRC). Muito mais fácil!

#### Projeto: "Laplace para Circuitos"
1. **Resolva o circuito RC** por Laplace (em vez de equação diferencial):
   - V_in = degrau de 12V → V_in(s) = 12/s
   - V_C(s) = V_in(s) × 1/(1+sRC) = 12/(s(1+sRC))
   - Frações parciais → tabela → v_c(t) = 12(1-e^(-t/RC)). Mesma resposta!
2. **Resolva um circuito RLC** por Laplace → identifique se é sub/super/criticamente amortecido
3. **Use Python/Octave** para inversa de Laplace:
   ```python
   from sympy import *
   s, t = symbols('s t')
   F = 12 / (s * (1 + s*0.001))  # RC = 1ms
   f = inverse_laplace_transform(F, s, t)
   print(f)  # 12 - 12*exp(-1000*t)
   ```
4. **Prompt IA**: *"Resolva o circuito RLC série por Laplace com V1=degrau de 10V, R=100Ω, L=10mH, C=1μF. Identifique os polos, determine se é sub/super/criticamente amortecido, e plote a resposta v_C(t)."*
5. **Entregável**: 2 circuitos resolvidos por Laplace + verificação com simulação

#### Erros Comuns
- Esquecer as condições iniciais ao transformar derivadas: L{f'(t)} = sF(s) - f(0⁻). Se f(0) ≠ 0, o resultado muda!
- Confundir s = jω com s = σ + jω — Laplace usa s completo, Fourier usa apenas o eixo imaginário (s = jω)
- Errar sinais nas frações parciais — SEMPRE verifique substituindo valores de s de volta

---

### Módulo 5.5: Função de Transferência e Diagramas de Blocos
**Tempo: 2.5h**

#### O que memorizar
- **Função de Transferência**: `G(s) = Y(s) / X(s)` (saída/entrada em Laplace, com condições iniciais zero)
- **Polos**: raízes do denominador. Determinam estabilidade e forma da resposta
- **Zeros**: raízes do numerador. Influenciam amplitude e fase
- **Sistemas de 1ª ordem**: G(s) = K/(τs+1). τ = constante de tempo, K = ganho estático
- **Sistemas de 2ª ordem**: G(s) = Kωₙ²/(s²+2ζωₙs+ωₙ²). ζ = fator de amortecimento, ωₙ = frequência natural
- **Diagramas de blocos**: representação gráfica de sistema com blocos → simplificação por regras de álgebra
- **Álgebra de blocos**: série (multiplicação), paralelo (soma), realimentação (G/(1+GH))

#### Intuição
A função de transferência é o "DNA" do sistema. Olhando para G(s), você sabe tudo: quão rápido ele responde (τ ou ωₙ), se oscila (ζ < 1), se é estável (polos no semiplano esquerdo), e qual o ganho em regime permanente (K).

Os polos são como **pesos cósmicos** — puxam a resposta. Polo real negativo = exponencial decrescente (estável). Polo real positivo = exponencial crescente (INSTÁVEL!). Polos complexos conjugados = oscilação (amortecida se parte real < 0).

#### Projeto: "Explorando G(s) no Python"
1. **Use a biblioteca `control`**:
   ```python
   import control as ct
   import matplotlib.pyplot as plt
   
   # Sistema de 2ª ordem
   wn = 10  # frequência natural
   for zeta in [0.1, 0.5, 0.707, 1.0, 2.0]:
       G = ct.tf([wn**2], [1, 2*zeta*wn, wn**2])
       t, y = ct.step_response(G)
       plt.plot(t, y, label=f'ζ={zeta}')
   
   plt.legend(); plt.grid(); plt.xlabel('Tempo (s)')
   plt.title('Resposta ao Degrau: Variando ζ')
   plt.show()
   ```
2. **Identifique**: para cada ζ → subamortecido, criticamente amortecido, ou superamortecido
3. **Diagrama de blocos**: simplifique G1 em série com G2, feedback com H
4. **Entregável**: Gráficos de resposta ao degrau + simplificação de blocos

---

### Módulo 5.6: Modelagem de Sistemas Físicos
**Tempo: 2h**

#### O que memorizar
- **Sistema térmico**: `MCp × dT/dt = P_entrada - (T-T_amb)/R_th` → G(s) = K/(τs+1). τ = MCp × R_th
- **Motor DC**: relação tensão-velocidade via constante de torque e back-EMF
- **Tanque de nível**: Q_entrada - Q_saída = A × dh/dt. G(s) = 1/(As)
- **Analogias**: elétrico ↔ mecânico ↔ térmico ↔ hidráulico (mesmas equações, variáveis diferentes!)

| Elétrico | Mecânico | Térmico | Hidráulico |
|----------|----------|---------|------------|
| Tensão (V) | Força (F) | Temperatura (T) | Pressão (P) |
| Corrente (I) | Velocidade (v) | Fluxo de calor (Q) | Vazão (Q) |
| Resistência (R) | Atrito (b) | R_térmica (R_th) | R_hidráulica |
| Capacitância (C) | Massa (m) | Capacidade térmica (MCp) | Área (A) |
| Indutância (L) | Mola (k) | — | Inércia |

#### Projeto: "Modelando um Forno Elétrico"
1. **Modelo**: forno com resistência de aquecimento (P=1000W), massa térmica, perda para ambiente
2. **Equação diferencial** → Transformada de Laplace → G(s) = K/(τs+1)
3. **Simule** resposta ao degrau (liga o forno) → observe curva exponencial de aquecimento
4. **Entregável**: Modelo matemático + simulação + validação

---

## Fase 3 — Análise e Projeto de Controladores

### Módulo 5.7: Estabilidade — Quando o Sistema Explode
**Tempo: 2.5h**

#### O que memorizar
- **Estabilidade BIBO**: saída limitada para entrada limitada. Condição: TODOS os polos de malha fechada no semiplano ESQUERDO (parte real < 0)
- **Critério de Routh-Hurwitz**: método algébrico para verificar estabilidade sem calcular polos
- **Tabela de Routh**: organizar coeficientes do polinômio → primeira coluna deve ter todos positivos → sistema estável
- **Marginalmente estável**: polos no eixo imaginário → oscilação constante (nem cresce nem decresce)
- **Ganho crítico**: ganho de malha aberta que torna o sistema marginalmente estável

#### Intuição
Estabilidade é literalmente **vida ou morte** em controle. Um sistema de controle instável: o avião cai, o reator nuclear derrete, o carro autônomo bate. Matematicamente, instabilidade = polo com parte real positiva = exponencial crescente = saída que cresce infinitamente. Na prática, algo quebra, queima, ou explode antes de chegar ao infinito.

#### Projeto: "Estável ou Não?"
1. **Analise 5 funções de transferência** com Routh-Hurwitz:
   - G1(s) = 1/(s²+3s+2) → polos: -1, -2 → ESTÁVEL
   - G2(s) = 1/(s²-s+2) → coeficiente negativo → INSTÁVEL (sem precisar de Routh!)
   - G3(s) = 1/(s³+2s²+3s+4) → aplique Routh → estável?
2. **Simule no Python** com `control` → confirme estabilidade pela resposta ao impulso
3. **Encontre ganho crítico**: G(s) = K/(s(s+1)(s+2)). Para qual K o sistema em feedback fica instável?
4. **Entregável**: Tabelas de Routh + simulações + ganho crítico

---

### Módulo 5.8: Diagrama de Bode — Frequência como Linguagem
**Tempo: 2.5h**

#### O que memorizar
- **Bode**: 2 gráficos: |G(jω)| em dB vs log(ω) + ∠G(jω) em ° vs log(ω)
- **Traçado assintótico**: aproximação por retas. Polo em s = -a → queda de -20dB/dec a partir de ω = a
- **Margem de Ganho (GM)**: quanto ganho você pode adicionar antes de instabilidade. Medido na freq onde fase = -180°
- **Margem de Fase (PM)**: quantos graus de fase restam antes de -180°. Medido na freq onde |G| = 0dB
- **Critério de estabilidade**: GM > 0 dB E PM > 0° → sistema estável em malha fechada
- **Regras práticas**: PM > 45° (bom), PM > 60° (ótimo), PM < 30° (perigoso, muito oscilatório)

#### Intuição
O diagrama de Bode é o **raio-X** do sistema em frequência. Mostra como o sistema responde a cada frequência: quais amplifica, quais atenua, e quanto atrasa cada uma. As margens de ganho e fase são como "margem de segurança" — quanto maiores, mais robusto o sistema é contra variações e incertezas. O projeto de controladores é essencialmente ajustar o Bode para ter margens adequadas.

#### Projeto: "Lendo Bode"
1. **Plote Bode** para 4 sistemas usando Python:
   ```python
   import control as ct
   G = ct.tf([10], [1, 3, 2])  # exemplo
   ct.bode_plot(G, dB=True, Hz=False)
   plt.show()
   ```
2. **Identifique**: frequência de corte (-3dB), margem de ganho, margem de fase
3. **Trace Bode assintótico** no papel para G(s) = 100/((s+1)(s+10))
4. **Compare** assintótico com exato → onde estão as diferenças?
5. **Entregável**: 4 diagramas de Bode + análise de margens + Bode assintótico

---

### Módulo 5.9: Controlador PID — O Rei da Indústria
**Tempo: 3h**

#### O que memorizar
- **PID**: `u(t) = Kp × e(t) + Ki × ∫e(t)dt + Kd × de(t)/dt`
- **P (Proporcional)**: reação proporcional ao erro atual. Mais Kp = mais rápido, mas mais overshoot
- **I (Integral)**: acumula erro passado → elimina erro em regime permanente (offset). Mais Ki = elimina erro, mas pode causar oscilação
- **D (Derivativo)**: reage à taxa de mudança do erro → "antecipa". Mais Kd = menos overshoot, mais suave, mas sensível a ruído
- **Em Laplace**: G_PID(s) = Kp + Ki/s + Kd×s = (Kd×s² + Kp×s + Ki) / s
- **Sintonia (Ziegler-Nichols)**: método prático — encontre Ku (ganho crítico) e Tu (período de oscilação). Kp= 0.6Ku, Ki=2Kp/Tu, Kd=Kp×Tu/8

#### Intuição
O PID é como um **motorista habilidoso**:
- **P**: olha a posição atual — "estou longe do objetivo, vou virar MAIS o volante" (proporcional ao erro)
- **I**: lembra o passado — "tenho ficado consistentemente à esquerda, preciso corrigir" (acumula erros)
- **D**: antecipa o futuro — "estou me aproximando rápido demais, preciso desacelerar" (taxa de mudança)

Um PID bem sintonizado faz o motor chegar à velocidade desejada rápido, sem oscilar, e sem erro permanente. 95% dos controladores industriais são PID — é simples, robusto, e funciona surpreendentemente bem na maioria dos casos.

#### Projeto: "Controle PID de Temperatura"
1. **Planta**: forno com G(s) = 1/(10s+1) (constante de tempo = 10s, ganho = 1)
2. **Monte feedback no Python**:
   ```python
   import control as ct
   
   # Planta
   G = ct.tf([1], [10, 1])
   
   # Controlador PID
   Kp, Ki, Kd = 5, 0.5, 2
   C = ct.tf([Kd, Kp, Ki], [1, 0])  # PID
   
   # Malha fechada
   T = ct.feedback(C * G, 1)
   
   t, y = ct.step_response(T, T=np.linspace(0, 30, 1000))
   plt.plot(t, y); plt.axhline(y=1, color='r', linestyle='--')
   plt.xlabel('Tempo (s)'); plt.ylabel('Temperatura (normalizada)')
   plt.title(f'PID: Kp={Kp}, Ki={Ki}, Kd={Kd}')
   plt.grid(); plt.show()
   ```
3. **Sintonize por tentativa**: varie Kp, Ki, Kd → minimize overshoot e tempo de acomodação
4. **Sintonize por Ziegler-Nichols**: aumente Kp até oscilar → anote Ku e Tu → calcule Kp, Ki, Kd
5. **Compare**: tentativa vs Ziegler-Nichols vs apenas P vs apenas PI
6. **Métricas**: tempo de subida, overshoot (%), tempo de acomodação, erro em regime permanente
7. **Prompt IA**: *"Explique qualitativamente o que acontece quando: (a) aumento Kp demais, (b) aumento Ki demais, (c) aumento Kd demais. Para cada caso, descreva o efeito na resposta ao degrau e dê analogia com o motorista."*
8. **Entregável**: PID sintonizado + gráficos comparativos + métricas

#### Erros Comuns
- Achar que SEMPRE precisa de PID — muitas vezes PI basta (D é sensível a ruído e pode piorar o sistema)
- Sintonizar por Ziegler-Nichols e não ajustar depois — ZN dá um ponto de partida AGRESSIVO (overshoot ~25%). Quase sempre precisa reduzir Kp e Ki depois
- Esquecer saturação do atuador — na prática, o atuador tem limites (PWM 0-255, válvula 0-100%). O integrador acumula erro mesmo quando saturado → wind-up! Solução: anti-windup

---

### Módulo 5.10: Projeto de Controlador por Bode
**Tempo: 2.5h**

#### O que memorizar
- **Especificações de desempenho**: margem de fase → overshoot, bandwidth → velocidade de resposta
- **Compensador em avanço (lead)**: adiciona fase positiva → melhora margem de fase e velocidade
- **Compensador em atraso (lag)**: adiciona ganho em baixa frequência → reduz erro em regime permanente
- **Lead-lag**: combina ambos. É essencialmente um PID no domínio da frequência
- **Projeto**: definir specs → plotar Bode de G → projetar C para atingir PM e GM desejados

#### Projeto: "Compensador Lead para Sistema de Posição"
1. **Planta**: G(s) = 1/(s(s+2)) (motor DC posição)
2. **Spec**: PM ≥ 45°, erro de velocidade ≤ 5%
3. **Projeto**: plote Bode de G → identifique PM atual → projete compensador lead para atingir spec
4. **Entregável**: Compensador projetado + Bode antes/depois + resposta ao degrau

#### Checkpoint — Fim da Fase Análise e Projeto
- [ ] Verifica estabilidade com Routh-Hurwitz
- [ ] Lê e interpreta diagramas de Bode (margens de ganho e fase)
- [ ] Sintoniza PID (por tentativa e por Ziegler-Nichols)
- [ ] Entende a relação PM↔overshoot, BW↔velocidade

---

## Fase 4 — Tópicos Avançados

### Módulo 5.11: Lugar das Raízes
**Tempo: 2.5h**

#### O que memorizar
- **Lugar das raízes**: gráfico dos polos de malha fechada variando o ganho K de 0 a ∞
- **Regras de traçado**: parte de polos de malha aberta (K=0), termina nos zeros ou no infinito (K=∞)
- **Aplicação**: encontrar graficamente qual K leva polos a ζ e ωₙ desejados → design intuitivo
- **Ferramenta**: `control.root_locus(G)` no Python

#### Projeto: "Root Locus Design"
1. **Plote root locus** de G(s) = 1/(s(s+1)(s+2)) com Python
2. **Identifique**: para qual K o sistema fica instável (polos cruzam eixo imaginário)
3. **Projete K** para ζ = 0.5 → trace reta ζ = cos(θ) no root locus → encontre K no cruzamento
4. **Entregável**: Root locus + K projetado + resposta ao degrau

---

### Módulo 5.12: Introdução a Controle Digital
**Tempo: 2.5h**

#### O que memorizar
- **Amostragem**: sinal contínuo → discreto (ADC) → processamento digital → contínuo (DAC)
- **Transformada Z**: equivalente discreta da Laplace. Z = e^(sT) (T = período de amostragem)
- **PID digital**: `u[n] = u[n-1] + Kp(e[n]-e[n-1]) + Ki×T×e[n] + Kd/T×(e[n]-2e[n-1]+e[n-2])`
- **Escolha de T**: Nyquist! T < 1/(2×f_max_sinal). Regra prática: T ≤ τ/10 (10× mais rápido que a constante de tempo)
- **Implementação em microcontrolador**: timer → ISR → lê sensor (ADC) → calcula PID → atualiza atuador (PWM)

#### Intuição
Na indústria, quase todos os controladores PID são DIGITAIS — rodam num CLP, Arduino, ou DSP. O controlador contínuo que projetamos nos módulos anteriores é a "teoria", e o digital é a "prática". A conversão é surpreendentemente simples: substituir derivada por diferença (forward/backward), integral por soma acumulada, e rodar num loop com período T fixo.

#### Projeto: "PID no Arduino"
1. **Implemente PID digital em C++** (Arduino):
   ```cpp
   float Kp = 2.0, Ki = 0.5, Kd = 0.1;
   float error, prevError = 0, integral = 0;
   float dt = 0.01; // 10ms (100Hz)
   
   float pidCompute(float setpoint, float measured) {
       error = setpoint - measured;
       integral += error * dt;
       float derivative = (error - prevError) / dt;
       prevError = error;
       return Kp * error + Ki * integral + Kd * derivative;
   }
   ```
2. **Simule no Wokwi**: Arduino + sensor de temperatura + ventilador PWM → PID controla temperatura
3. **Compare** com PID contínuo (Python) → observe efeito de T diferente (1ms, 10ms, 100ms, 1s)
4. **Entregável**: PID digital no Arduino + comparação contínuo vs discreto

#### Checkpoint Final — Pilar 5 Completo!
- [ ] Calcula FFT e interpreta espectro de frequências
- [ ] Aplica Laplace para resolver circuitos e modelar sistemas
- [ ] Analisa estabilidade (Routh, Bode, root locus)
- [ ] Sintoniza PID (tentativa, Ziegler-Nichols, Bode)
- [ ] Implementa PID digital em microcontrolador
- [ ] Entende a conexão entre todos os domínios: tempo ↔ Laplace ↔ frequência

---

> Parabéns! Ao completar todos os 5 pilares, você terá o equivalente a um **currículo completo de Engenharia Elétrica**, com a vantagem de ter praticado cada conceito em simulação.
>
> [Voltar ao Índice](ee_roadmap_index.md)

---

## Fase 5 — Processamento Digital de Sinais (DSP)

### Módulo 5.13: Amostragem, Nyquist e Aliasing
**Tempo: 2.5h**

#### O que memorizar
- **Amostragem**: converter sinal contínuo x(t) em discreto x[n] = x(nT). T = período de amostragem, f_s = 1/T
- **Teorema de Nyquist-Shannon**: para reconstruir o sinal perfeitamente, f_s ≥ 2×f_max. A frequência f_s/2 = frequência de Nyquist
- **Aliasing**: se f_sinal > f_s/2, a frequência "dobra" e aparece como uma frequência FALSA mais baixa. Irreversível!
- **Filtro anti-aliasing**: filtro passa-baixa ANALÓGICO ANTES do ADC. Corta tudo acima de f_s/2
- **Quantização**: converter amplitude contínua em valores discretos (N bits → 2^N níveis). SNR_max = 6.02N + 1.76 dB
- **Oversampling**: amostrar MUITO mais rápido que Nyquist → relaxa exigência do filtro anti-aliasing. Usado em ΔΣ ADCs

#### Intuição
Aliasing é como o efeito "rodas girando ao contrário" em filmes: a câmera (amostrador) captura quadros discretos, e se a roda gira rápido demais (f_sinal > f_s/2), PARECE que gira no sentido oposto (frequência falsa). Na prática, sem filtro anti-aliasing, um sinal de 90kHz amostrado a 100kHz aparece como 10kHz — completamente errado e impossível de corrigir depois!

#### Projeto
1. **Demonstre aliasing** no Python: gere senóide de 90Hz, amostra a 100Hz → observe "fantasma" em 10Hz
2. **Calcule** SNR de ADC de 10 bits (Arduino) e 24 bits (áudio profissional)
3. **Prompt IA**: *"Explique por que CDs usam 44.1kHz de taxa de amostragem. Qual é a frequência máxima que pode reproduzir? Por que não 40kHz?"*

---

### Módulo 5.14: Transformada Z — Laplace do Mundo Discreto
**Tempo: 2.5h**

#### O que memorizar
- **Transformada Z**: X(z) = Σ x[n] × z⁻ⁿ. Análoga a Laplace para sinais discretos
- **Relação**: z = e^(sT). Semiplano esquerdo de s → interior do círculo unitário em z
- **Transformadas essenciais**: δ[n]↔1, u[n]↔z/(z-1), aⁿu[n]↔z/(z-a)
- **Propriedade do atraso**: Z{x[n-1]} = z⁻¹X(z). Atraso de 1 amostra = multiplicar por z⁻¹
- **Função de transferência discreta**: H(z) = Y(z)/X(z). Polos DENTRO do círculo unitário = estável
- **Equação de diferenças**: y[n] = b₀x[n] + b₁x[n-1] - a₁y[n-1] → H(z) = (b₀ + b₁z⁻¹)/(1 + a₁z⁻¹)
- **Resposta em frequência**: H(e^(jω)) = H(z)|_{z=e^(jω)} para ω de 0 a π (= 0 a f_s/2)

#### Intuição
Z é para sistemas discretos o que Laplace é para contínuos. A grande diferença: estabilidade em Laplace = polos no semiplano esquerdo. Em Z = polos DENTRO do círculo unitário (|z| < 1). O z⁻¹ significa "o valor de um clock atrás" — é literalmente um registrador em hardware digital!

#### Projeto
1. **Converta** filtro RC analógico H(s)=1/(RCs+1) para discreto usando bilinear (Tustin): s = (2/T)(z-1)/(z+1)
2. **Plote** resposta em frequência do filtro discreto → compare com o analógico
3. **Encontre polos** de H(z) → estão dentro do círculo unitário?

---

### Módulo 5.15: Filtros Digitais FIR e IIR
**Tempo: 3h**

#### O que memorizar
- **FIR (Finite Impulse Response)**: y[n] = Σ bₖ × x[n-k]. Só usa entradas passadas (sem feedback). SEMPRE estável!
  - Fase linear possível (atraso constante para todas as frequências)
  - Precisa de mais coeficientes (mais lento) para mesma atenuação
- **IIR (Infinite Impulse Response)**: y[n] = Σ bₖx[n-k] - Σ aₖy[n-k]. Usa saídas passadas (feedback). Pode ser instável!
  - Mais eficiente (menos coeficientes), mas pode ter problemas de estabilidade e fase não-linear
  - Derivado de filtros analógicos clássicos (Butterworth, Chebyshev, Elliptic)
- **Butterworth**: maximamente plano na banda passante. Mais suave, mais lento
- **Chebyshev**: ripple na banda passante, mas transição mais abrupta
- **Janelamento (FIR design)**: truncar resposta ao impulso ideal com janela (Hamming, Hanning, Blackman)
- **scipy.signal**: `butter()`, `cheby1()`, `firwin()`, `lfilter()`, `freqz()`

#### Projeto
1. **Projete filtro passa-baixa FIR** de 51 coeficientes, f_corte=1kHz, f_s=10kHz usando `firwin()`
2. **Projete filtro IIR Butterworth** 4ª ordem mesma especificação usando `butter()`
3. **Compare**: resposta em frequência, atraso de grupo, número de operações por amostra
4. **Aplique**: filtre sinal ruidoso (senóide + ruído branco) com ambos → ouça/plote a diferença
5. **Prompt IA**: *"Quando devo usar FIR vs IIR? Dê 3 cenários reais onde cada um é melhor."*

---

### Módulo 5.16: DSP Prático — FFT, Janelamento e Análise Espectral
**Tempo: 2.5h**

#### O que memorizar
- **FFT prática**: N pontos → N/2 bins de frequência. Resolução: Δf = f_s/N
- **Janelamento**: aplicar janela ANTES da FFT para reduzir "vazamento espectral" (spectral leakage)
  - Retangular (sem janela): melhor resolução, pior vazamento
  - Hanning/Hamming: bom compromisso
  - Blackman: mínimo vazamento, pior resolução
- **Zero-padding**: adicionar zeros ao sinal → NÃO melhora resolução real, mas interpola o espectro (visual mais suave)
- **Espectrograma (STFT)**: FFT em janelas deslizantes → gráfico tempo × frequência × amplitude. Usado em áudio, vibração, EEG
- **PSD (Power Spectral Density)**: potência por banda de frequência. Unidade: V²/Hz. Essencial para caracterizar ruído
- **Decimação/Interpolação**: reduzir/aumentar taxa de amostragem digitalmente. Anti-aliasing digital!

#### Projeto
1. **Efeito do janelamento**: aplique FFT a senóide não-inteira (e.g., 440.5Hz amostrada a 44100Hz com N=1024) sem janela e com Hanning → observe a diferença
2. **Espectrograma**: gere chirp (frequência crescente) e plote espectrogram com `scipy.signal.spectrogram()`
3. **Ruído**: gere ruído branco, calcule PSD com `scipy.signal.welch()` → deve ser plano!
4. **Aplicação real**: analise arquivo de áudio (.wav) → identifique frequências dominantes

#### Checkpoint — DSP
- [ ] Entende Nyquist, aliasing, e por que filtro anti-aliasing é obrigatório
- [ ] Converte entre domínios s e z (bilinear)
- [ ] Projeta filtros FIR e IIR e sabe quando usar cada um
- [ ] Aplica FFT com janelamento adequado e interpreta espectrograma

---

## Se Você Travar

| Problema | Solução |
|----------|--------|
| FFT mostra picos estranhos | Verifique: janelamento (aplique Hanning/Hamming), número de amostras (use potência de 2: 256, 512, 1024), e normalize pela frequência de amostragem |
| Laplace dá resultado errado | Verifique tabela de transformadas. Erro mais comum: esquecer condições iniciais ou errar sinais em frações parciais |
| Sistema instável em simulação | Verifique polos — algum no semiplano direito? Reduza o ganho K. Se usando PID, reduza Kp primeiro |
| PID oscila sem parar | Ki muito alto → reduza. Ou o ganho total está acima do crítico → reduza Kp. Verifique sinais (realimentação deve ser NEGATIVA!) |
| Bode não parece no formato esperado | Verifique se o eixo X está em escala logarítmica e o eixo Y em dB. Use `ct.bode_plot(G, dB=True)` |
| Resposta ao degrau não converge | Sistema instável ou marginalmente estável. Verifique Routh-Hurwitz. Adicione mais amortecimento (aumente R no circuito, ou aumente Kd no PID) |
| PID digital se comporta diferente do contínuo | Período de amostragem T muito grande! Regra: T ≤ τ/10. Se τ = 1s, T ≤ 0.1s. Se T > τ, o controlador digital é instável |
