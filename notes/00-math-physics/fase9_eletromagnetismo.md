# Módulo 0 — Matemática/Física: Fase 9 — Eletromagnetismo Completo
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 0.30: Eletrostática

### Cargas e campos

- Carga do elétron >> $q = -1.6 × 10^{-19}$ {{C}}
- 1 Ampère = >> {{1 C/s}}
- Lei de Coulomb >> $F =$ {{$kQ_1 Q_2 / r^2$}} ($k = 9×10^9$ N·m²/C²)
- Campo elétrico >> $E = F/q =$ {{$kQ/r^2$}} (V/m). Aponta de + para −
- Lei de Gauss >> $\oint \vec{E} \cdot d\vec{A} =$ {{$Q_{enc} / \varepsilon_0$}}
- Potencial >> $V = kQ/r$. Relação: $E =$ {{$-\nabla V$}}

### Capacitância

- $C = Q/V$. Placas paralelas: $C =$ >> {{$\varepsilon A / d$}}
- Energia do capacitor >> $E =$ {{$\frac{1}{2}CV^2$}}
- Dielétrico >> Material entre placas aumenta C por fator {{$\varepsilon_r$}} (cerâmica: $\varepsilon_r ≈ 1000$!)

### Drill — Eletrostática #[[Drill]]

- Placas 1cm², separação 1mm, vácuo → C = >> {{~0.88 pF}} ($\varepsilon_0 A/d$)
- Mesmo com dielétrico $\varepsilon_r$=100 → C = >> {{~88 pF}}
- $C$=100μF, $V$=5V → energia = >> {{1.25 mJ}} (½CV²)

---

## Módulo 0.31: Corrente e Lei de Ohm Microscópica

### Corrente microscópica

- $I = nqv_d A$ >> $n$=densidade de portadores, $v_d$=velocidade de {{deriva}}
- Velocidade de deriva >> Surpreendentemente {{lenta}} (~0.1mm/s para Cu com 1A). O sinal viaja a ~c!
- Resistividade >> $R = \rho L/A$. Cobre: $\rho =$ {{$1.7×10^{-8}$}} Ω·m
- Efeito da temperatura >> Metais: $\alpha > 0$ (R {{sobe}}). Semicondutores: $\alpha < 0$ (R {{desce}})
- Lei de Ohm microscópica >> $J =$ {{$\sigma E$}} ($\sigma = 1/\rho$ = condutividade)

### Drill — Corrente #[[Drill]]

- Fio Cu 100m, seção 2.5mm² → R = >> {{0.68Ω}} ($1.7×10^{-8}×100/2.5×10^{-6}$)
- R=4Ω a 20°C, α=0.004/°C → R a 80°C = >> {{4.96Ω}} (4×(1+0.004×60))

---

## Módulo 0.32: Magnetostática

### Campos magnéticos

- Campo de fio reto >> $B =$ {{$\mu_0 I / (2\pi r)$}}. Regra da mão direita
- Lei de Ampère >> $\oint \vec{B} \cdot d\vec{l} =$ {{$\mu_0 I_{enc}$}}
- Solenoide >> $B =$ {{$\mu_0 n I$}} ($n$ = espiras/metro). Uniforme dentro, ~zero fora
- Indutância do solenoide >> $L =$ {{$\mu_0 N^2 A / l$}}
- Materiais ferromagnéticos >> $\mu_r ≈$ {{$5000$}} para ferro (concentra campo!)
- Histerese >> B não segue H linearmente → {{perdas}} por ciclo (magnéticas)

### Força magnética

- Força em condutor >> $F =$ {{$BIL$}} (princípio do motor!)
- Correntes paralelas mesmo sentido → >> {{Atraem-se}}
- Correntes paralelas sentido oposto → >> {{Repelem-se}}

### Drill — Magnetismo #[[Drill]]

- Fio com 10A, distância 5cm → B = >> {{40 μT}} ($4\pi×10^{-7}×10/(2\pi×0.05)$)
- Solenoide 500 espiras, 20cm, I=2A → B = >> {{~6.28 mT}} ($\mu_0×2500×2$)

---

##  Módulo 0.33: Indução — Faraday e Lenz

### Leis de indução

- Lei de Faraday >> $\text{fem} =$ {{$-N × d\Phi/dt$}} (variação de fluxo → tensão induzida)
- Fluxo magnético >> $\Phi = B × A × \cos\theta$. Unidade: {{Weber (Wb)}}
- Lei de Lenz >> O sinal negativo! Tensão induzida se {{opõe}} à variação (inércia magnética)
- $V = L × dI/dt$ >> Faraday aplicado a uma {{bobina}} (indutor)

### Aplicações

- Gerador >> fem$(t) = NBA\omega \sin(\omega t)$ — é uma {{senóide}}! Origem da AC!
- Transformador ideal >> $V_1/V_2 =$ {{$N_1/N_2$}}, $I_1/I_2 = N_2/N_1$, $P_1 = P_2$
- Indutância mútua >> $M = k\sqrt{L_1 L_2}$. $k$ = coeficiente de {{acoplamento}} (0 a 1)
- Correntes parasitas (Foucault) >> Correntes em massas condutoras → {{perdas}}. Solução: laminação do núcleo

Brasil: 3600RPM, 2 polos → 60Hz. A rede elétrica inteira é senóide porque geradores giram!

### Drill — Faraday #[[Drill]]

- N=100, B=0.5T, A=50cm², ω=377rad/s → fem_pico = >> {{~94V}} (100×0.5×50×10⁻⁴×377)
- Trafo N₁=2200, N₂=110, V₁=220V → V₂ = >> {{11V}} (220×110/2200)
- Trafo N₁=100, N₂=1000, V₁=12V → V₂ = >> {{120V}} (step-up 10:1)

---

##  Módulo 0.34: Equações de Maxwell e Ondas EM

### As 4 equações

- Maxwell 1 (Gauss E) >> $\nabla \cdot \vec{E} = \rho/\varepsilon_0$ — Cargas {{criam}} campo E
- Maxwell 2 (Gauss B) >> $\nabla \cdot \vec{B} = 0$ — Não existem {{monopolos}} magnéticos
- Maxwell 3 (Faraday) >> $\nabla × \vec{E} = -\partial \vec{B}/\partial t$ — B variável cria {{E rotacional}}
- Maxwell 4 (Ampère-Maxwell) >> $\nabla × \vec{B} = \mu_0 \vec{J} + \mu_0 \varepsilon_0 \partial \vec{E}/\partial t$ — Corrente e E variável criam {{B}}

### Ondas EM

- Velocidade da luz >> $c =$ {{$1/\sqrt{\mu_0 \varepsilon_0}$}} $= 3×10^8$ m/s
- Impedância do espaço livre >> $Z_0 = \sqrt{\mu_0/\varepsilon_0} ≈$ {{377Ω}}
- Antena >> Converte corrente oscilante em {{onda EM}} (e vice-versa). $\lambda = c/f$
- Vetor de Poynting >> $\vec{S} =$ {{$\vec{E} × \vec{B} / \mu_0$}} (fluxo de energia EM)

Maxwell 3 + 4 combinadas → equação de onda! Campo E variável cria B, que cria E, que cria B... → onda se autopropaga pelo espaço. Luz, WiFi, raios X = mesma coisa, freq diferente.

### Drill — Maxwell #[[Drill]]

- Qual equação garante que não existem monopolos magnéticos? >> {{Gauss B}} ($\nabla \cdot B = 0$)
- Qual equação é a base do gerador/transformador? >> {{Faraday}} ($\nabla × E = -\partial B/\partial t$)
- Qual equação é a base do motor? >> {{Ampère}} (corrente → campo B → força)
- Antena λ/4 para WiFi 2.4GHz: comprimento = >> {{~3.1 cm}} (12.5cm/4)
