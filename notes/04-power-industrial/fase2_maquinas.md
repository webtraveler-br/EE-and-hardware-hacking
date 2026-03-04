# Módulo 4 — Eletrotécnica: Fase 2 — Máquinas Elétricas
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 4.4: Transformadores

### Princípio e fórmulas

- Transformador transfere potência entre enrolamentos via >> {{acoplamento magnético}} (indução)
- Funciona com DC? >> {{Não}} — exige corrente alternada/variável (dΦ/dt ≠ 0)
- Relação de espiras (trafo ideal): $V_1/V_2$ = >> {{$N_1/N_2$}}
- Relação de correntes (trafo ideal): $I_1/I_2$ = >> {{$N_2/N_1$}} (inverso da tensão, pois $P_{in} = P_{out}$)

Elevar V 10× → I cai 10× (P constante). Fontes chaveadas e soldadores: rebaixar V para obter I altíssima.

### Transformador real e perdas

- Perdas no cobre ($W_{Cu}$) >> Causadas pela {{resistência dos enrolamentos}} ($I^2R$)
- Perdas por histerese >> Aquecimento do {{núcleo de ferro}} pela inversão do campo magnético
- Correntes parasitas (Foucault) >> Mini-correntes no ferro sólido; solução: {{laminar o núcleo}} (chapas isoladas)
- Ensaio de curto-circuito >> Mede perdas de {{cobre}} ($I_{nom}$ com V reduzida)
- Ensaio a vazio >> Mede perdas de {{ferro}} (V nominal sem carga no secundário)

### Drill — Transformadores #[[Drill]]

- Trafo 10:1 (rebaixador), primário 380V → V₂ = >> {{38V}}
- Trafo 5:1, V₁=1000V, V₂=200V, I₂=50A → I₁ = >> {{10A}} ($50×200/1000$)
- Trafo 1:20 (elevador), V₁=12V → V₂ = >> {{240V}}
- Trafo ideal, P₁=1kW, V₂=100V → I₂ = >> {{10A}}

---

## Módulo 4.5: Motor de Indução Trifásico (MIT)

### Características

- MIT (gaiola de esquilo) domina >90% da indústria porque >> Não tem {{escovas}} (robusto, baixa manutenção)
- Velocidade síncrona: $n_s$ = >> {{$120f/p$}} (f = frequência, p = nº de polos)
- Escorregamento (slip) >> $n_r$ deve ser {{menor}} que $n_s$ para gerar torque ($n_s > n_r$)
- Fórmula do slip: $s$ = >> {{$(n_s - n_r) / n_s$}}
- Slip nominal típico >> {{2% a 5%}} (ex: 1800 rpm → 1740 rpm)

### Perigo: corrente de partida

- Corrente de inrush (partida direta) >> {{5 a 8×}} a corrente nominal
- Efeito na rede >> Causa {{queda de tensão generalizada}} (voltage drop → PC da fábrica reseta)

### Drill — MIT #[[Drill]]

- f=60Hz, p=2 → $n_s$ = >> {{3600 RPM}}
- f=60Hz, p=4 → $n_s$ = >> {{1800 RPM}}
- f=60Hz, p=6 → $n_s$ = >> {{1200 RPM}}
- $n_s$=1800, $n_r$=1710 → slip = >> {{5%}} ($90/1800$)
- Motor I_nom=20A, partida direta → I_inrush ≈ >> {{100-160A}} (5-8×)

---

##  Módulo 4.6: Métodos de Partida

### Partida direta vs Y-Δ

- Partida direta: acima de ~5-7.5 CV a concessionária >> {{Proíbe}} (inrush alto demais)
- Partida Y-Δ >> Inicia em {{estrela}} (V reduzida), chaveia para {{triângulo}} (V plena)
- Redução de I na partida Y-Δ >> Corrente cai {{3×}} ($33\%$ do inrush direto)
- Reverter sentido de rotação >> Trocar {{2 das 3 fases}}
- Intertravamento >> Proteção que impede K_frente e K_ré {{acionarem juntos}} (NF cruzado + trava mecânica)

### Soft-Starter e VFD

- Soft-Starter >> Controla partida via {{tiristores (SCR)}} — rampa suave de tensão
- Soft-Starter vs VFD >> Soft-Starter só controla a {{partida}}; VFD controla a {{velocidade}} contínua

### Drill — Partida #[[Drill]]

- Motor 50A nominal, partida direta → I_pico ≈ >> {{250-400A}}
- Mesmo motor, partida Y-Δ → I_pico ≈ >> {{83-133A}} (÷3)

---

## Módulo 4.7: Inversores de Frequência (VFD)

### Princípio V/f

- Princípio de controle do VFD >> Manter {{$V/f$ = constante}}
- Controle V/f constante garante >> {{Torque nominal}} em qualquer faixa de rotação
- Acima da velocidade nominal ($f > f_{nom}$) → V trava no máximo → motor entra em >> {{enfraquecimento de campo}} (torque cai)

### Problemas do VFD

- VFDs geram >> {{Harmônicos}} na rede (comutação PWM dos IGBTs)
- Solução para harmônicos de VFD >> {{Filtros indutivos}} (reatores de linha) na entrada

Harmônicos do VFD são ondas de 3ª, 5ª, 7ª ordem. Aquela tela cheia de listras quando ligam o inversor no lab = harmônico rádio-conduzido.

### Drill — VFD #[[Drill]]

- Motor 4 polos, VFD a 30Hz → $n_s$ = >> {{900 RPM}} ($120×30/4$)
- Mesmo motor, VFD a 60Hz → $n_s$ = >> {{1800 RPM}}
- VFD a 90Hz (acima do nominal) → torque >> {{cai}} (enfraquecimento de campo)

---

## Módulo 4.15: Máquina Síncrona

### Gerador síncrono

- Escorregamento do gerador síncrono >> {{Zero}} (rotor gira exatamente na velocidade do campo)
- Tipo de máquina em usinas (Itaipu, termelétricas) >> {{Máquina síncrona}}
- Controle de tensão na rede >> Variar a {{corrente DC de excitação}} no rotor
- Aumentar excitação DC → injeta mais >> {{potência reativa Q⁺}} na rede (controla tensão)
- Aumentar potência ativa P → precisa aumentar >> {{potência mecânica na turbina}} (água/vapor)

---

##  Módulo 4.16: Sistemas de Potência (PU)

### Transmissão e por unidade

- Elevar V para transmitir longe → I cai → perdas caem >> Pois $P_{perda}$ = {{$I^2 × R_{cabo}$}}
- Sistema Por Unidade (PU) >> Normaliza grandezas para fazer cálculos {{independentes do nível de tensão}}
- Corrente de curto-circuito simétrica em PU >> $I_{cc}$ = {{$V_{pu} / Z_{pu}$}}

---

## Módulo 4.17: Qualidade de Energia e Harmônicos

### O que são harmônicos

- Harmônico de ordem $n$ >> Frequência {{$n × f_{fundamental}$}} (ex: 3º de 60Hz = 180Hz)
- Fontes típicas de harmônicos >> Cargas {{não-lineares}} (retificadores, VFDs, fontes chaveadas)
- THD (Total Harmonic Distortion) <> {{$\sqrt{\sum V_n^2} / V_1 × 100\%$}}
- Limite IEEE 519 para THD de tensão >> {{< 5%}}

### Efeitos e perigos

- Harmônicos aquecem transformadores porque >> Perdas no ferro crescem com {{$f^2$}}
- 3º harmônico no neutro trifásico >> {{Se soma}} (triplen harmonics = sequência zero)
- Consequência >> $I_{neutro}$ pode ser {{maior}} que $I_{fase}$ — neutro queima!
- Harmônicos + banco de capacitores → risco de >> {{ressonância}} ($X_C = X_L$ na harmônica)

### Soluções

- Filtro passivo sintonizado (LC) >> Curto-circuita a frequência harmônica (ex: {{5º = 300Hz}})
- Filtro ativo >> Injeta {{corrente de compensação}} em tempo real
- Transformador K >> Suporta {{aquecimento extra}} por harmônicos

### Drill — Harmônicos #[[Drill]]

- 5º harmônico de 60Hz = >> {{300 Hz}}
- 7º harmônico de 60Hz = >> {{420 Hz}}
- $V_1$=100V, $V_3$=30V, $V_5$=20V → THD = >> {{~36%}} ($\sqrt{900+400}/100$)
- 3 fases, cada uma 10A de 3º harmônico → $I_{neutro}$ = >> {{30A}} (somam-se)
