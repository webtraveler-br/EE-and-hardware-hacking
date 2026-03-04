# Módulo 0 — Matemática/Física: Fase 10 — Física Moderna e Semicondutores
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 0.35: Mecânica Quântica para Engenheiros

### Quantização e bandas

- Energia do fóton >> $E =$ {{$hf$}} ($h = 6.63×10^{-34}$ J·s)
- Dualidade onda-partícula >> Elétrons se comportam como {{ondas}} (tunelamento, difração)
- Bandas de energia >> Níveis discretos viram {{bandas}} quando átomos se juntam no cristal

### Gap (banda proibida)

- Gap ($E_g$) >> Energia mínima para promover elétron de {{valência}} para {{condução}}
- Metal: gap = >> {{0}} (conduz sempre)
- Semicondutor: gap ≈ >> {{~1 eV}} (Si: 1.12eV, GaAs: 1.42eV) — controlável
- Isolante: gap > >> {{~3 eV}} (não conduz)
- Semicondutor: T↑ → mais elétrons saltam gap → R >> {{Diminui}} (oposto de metais!)

Si é o "Goldilocks" da eletrônica: gap pequeno o bastante para controlar com dopagem e tensão, grande o bastante para não conduzir sozinho em temperatura ambiente.

### Drill — Quântica #[[Drill]]

- Luz vermelha λ=700nm → $E$ = >> {{~1.77 eV}} ($hc/\lambda$)
- LED azul λ=470nm → $E$ = >> {{~2.64 eV}}
- Raio X λ=0.1nm → $E$ = >> {{~12.4 keV}}
- Por que LED azul tem $V_f$ maior que vermelho? >> Gap do material é {{maior}} → precisa mais energia

---

## Módulo 0.36: Semicondutores — De Átomos a Transistores

### Dopagem

- Silício intrínseco >> {{4}} elétrons de valência, cristal covalente, pouco condutivo
- Tipo N >> Dopagem com fósforo ({{5}} val.) → elétron "extra" livre. Majoritários = {{elétrons}}
- Tipo P >> Dopagem com boro ({{3}} val.) → "lacuna". Majoritários = {{lacunas}}

### Junção PN

- Zona de depleção >> Região na interface sem {{portadores livres}} (funciona como isolante)
- Barreira de potencial (Si) >> ≈ {{0.7V}}
- Polarização direta ($V > 0.7$V) → corrente >> {{Flui}} (barreira reduzida)
- Polarização reversa → corrente ≈ >> {{0}} (só fuga em nA, barreira aumenta)
- Breakdown reverso >> Tensão reversa muito alta → {{avalanche}} → corrente dispara (Zener usa isso!)

### Equações fundamentais

- $V_T$ (tensão térmica) = >> {{$kT/q ≈ 26$mV}} a 300K
- Equação do diodo >> $I = I_s × (e^{V/nV_T} -$ {{$1)$}}
- $V_T$ aparece em >> {{Toda}} equação de semicondutores ($g_m$, $r_d$, diodo, BJT)

### Drill — Semicondutores #[[Drill]]

- Si intrínseco: 4 elétrons de valência → tipo de ligação = >> {{Covalente}}
- Fósforo (5 val.) dopa Si como tipo >> {{N}} (elétron extra)
- Boro (3 val.) dopa Si como tipo >> {{P}} (lacuna)
- $V_T$ a 27°C (300K) = >> {{~26 mV}}
- Diodo Si: $V_{limiar}$ ≈ >> {{0.7V}}. Ge: ≈ {{0.3V}}
