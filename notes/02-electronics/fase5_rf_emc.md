# Módulo 2 — Eletrônica: Fase 5 — RF, Linhas de Transmissão e EMC
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 2.17: Linhas de Transmissão

### Quando fio ≠ curto-circuito

- Fio se comporta como linha de transmissão quando comprimento ≥ >> {{λ/10}} (onda se propaga!)
- Impedância característica: $Z_0$ = >> {{$\sqrt{L/C}$}} (por unidade de comprimento)
- Cabo coaxial RG-58 <> {{50Ω}}
- Cabo coaxial TV <> {{75Ω}}
- Par trançado Ethernet <> {{100Ω}}

### Reflexão e casamento

- Coeficiente de reflexão: Γ = >> {{$(Z_L - Z_0) / (Z_L + Z_0)$}}
- Casamento perfeito ($Z_L = Z_0$) → Γ = >> {{0}} (zero reflexão, máxima transferência)
- Carga aberta ($Z_L = ∞$) → Γ = >> {{+1}} (reflexão total, sem inversão)
- Carga em curto ($Z_L = 0$) → Γ = >> {{−1}} (reflexão total, com inversão)
- SWR (Standing Wave Ratio) = >> {{$(1 + |Γ|) / (1 - |Γ|)$}}. SWR = 1 = perfeito

### Velocidade e λ/4

- Velocidade de propagação: $v$ = >> {{$c / \sqrt{\varepsilon_r}$}}
- No coaxial com polietileno ($\varepsilon_r$=2.3): v ≈ >> {{0.66c}}
- Transformador λ/4 >> Trecho de linha com $Z_0 =$ {{$\sqrt{Z_1 × Z_2}$}} casa impedâncias diferentes

Em DC, fio é fio. Em GHz, fio é antena. Se $Z_L ≠ Z_0$, energia volta e interfere — como eco numa sala.

### Drill — Linhas de Transmissão #[[Drill]]

- f=100MHz, v=c → λ = >> {{3m}} (c/f). Fio de 30cm = λ/10 → {{já é linha de transmissão}}
- f=1GHz → λ = >> {{30cm}}. Fio de 3cm já importa!
- f=10GHz → λ = >> {{3cm}}. Trilha de PCB de 3mm importa!
- $Z_0$=50Ω, $Z_L$=100Ω → Γ = >> {{0.33}} ((100−50)/(100+50))
- $Z_0$=50Ω, $Z_L$=50Ω → Γ = >> {{0}} (casado)
- SWR com Γ=0.33 → >> {{2.0}} ((1+0.33)/(1−0.33)) — aceitável mas não ideal

---

##  Módulo 2.18: Carta de Smith e Parâmetros S

### Carta de Smith

- Carta de Smith >> Mapa gráfico que converte {{impedância complexa Z}} em coeficiente de reflexão Γ
- Centro da carta = >> {{$Z_0$ (casado, Γ=0)}}
- Borda da carta = >> {{|Γ|=1 (reflexão total)}}
- Eixo horizontal >> {{Resistência pura}} (sem parte imaginária)
- Mover para cima >> Adiciona reatância {{indutiva}} (+jX)
- Mover para baixo >> Adiciona reatância {{capacitiva}} (−jX)

### Parâmetros S

- S-parameters >> Descrevem {{reflexão e transmissão}} de uma rede RF
- S11 >> {{Reflexão na entrada}} (quanto volta). |S11| < −10dB = bom casamento
- S21 >> {{Transmissão}} (quanto passa). |S21| > 0dB = ganho
- S22 >> {{Reflexão na saída}}
- S12 >> {{Transmissão reversa}} (isolação)
- VNA <> {{Vector Network Analyzer}} (mede parâmetros S)

### Casamento prático

- |S11| < −10dB significa >> Menos de {{10%}} da potência é refletida
- |S11| < −20dB significa >> Menos de {{1%}} refletida (excelente)
- Casar impedância com componentes >> Adicionar L e C em série/paralelo para {{mover na carta de Smith até o centro}}

Carta de Smith = GPS de impedância. Você está em Z_L, quer chegar em Z_0=50Ω. L e C são os "passos" no mapa.

### Drill — Parâmetros S #[[Drill]]

- |S11| = −6dB → potência refletida ≈ >> {{25%}} (10^(−6/10) ≈ 0.25)
- |S11| = −10dB → potência refletida = >> {{10%}}
- |S11| = −20dB → potência refletida = >> {{1%}}
- Amplificador com S21 = 20dB → ganho de tensão = >> {{10×}} (10^(20/20))

---

## Módulo 2.19: EMC — Compatibilidade Eletromagnética

### EMI e normativas

- EMI <> {{Interferência Eletromagnética}} (emissão indesejada)
- EMI conduzida >> Interferência que viaja pelos {{fios}} (alimentação, sinais)
- EMI irradiada >> Interferência que viaja pelo {{ar}} (como antena)
- EMC <> {{Compatibilidade Eletromagnética}} (funcionar sem causar nem sofrer interferência)
- Normativas >> {{CISPR, FCC}} (EUA), {{CE}} (Europa)

### Fontes e soluções

- Fontes de EMI >> Fontes chaveadas, {{motores}}, arcos elétricos, transmissores
- Blindagem >> Gabinete {{metálico}} atenua campos (eficácia depende de freq e aberturas)
- Filtro de linha >> Indutores de modo comum + {{capacitores X/Y}} na entrada
- Capacitor X >> Entre {{fase e fase}} (ou fase-neutro). Falha = curto → fusível protege
- Capacitor Y >> Entre {{fase e terra}}. Falha = fuga → valor pequeno (nF) por segurança

### Boas práticas de PCB

- Plano de terra contínuo >> Minimiza {{área de loops}} → minimiza irradiação
- Desacoplamento >> {{100nF cerâmico}} em CADA V_CC de CI, o mais perto possível
- Regra do loop >> Quanto menor a área do loop de corrente, {{menor}} a emissão EMI

Placa sem plano de terra contínuo = antena. Cada trilha de retorno cria um loop que irradia. Plano de terra = caminho de retorno mínimo = loop mínimo = EMI mínima.

### Drill — EMC #[[Drill]]

- Fonte chaveada 100kHz → harmônicos em >> {{200, 300, 400... kHz}} (múltiplos)
- Cap de desacoplamento padrão digital >> {{100nF}} cerâmico
- Loop de 1cm² vs loop de 10cm² → qual irradia mais? >> {{10cm²}} (proporcional à área)
- Cap entre fase e terra = tipo >> {{Y}}
- Cap entre fase e neutro = tipo >> {{X}}
