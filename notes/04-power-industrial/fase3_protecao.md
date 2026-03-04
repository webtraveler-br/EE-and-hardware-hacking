# Módulo 4 — Eletrotécnica: Fase 3 — Proteção e Instalações
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 4.8: Proteção Elétrica

### Fusíveis e disjuntores

- Função do fusível >> Proteger contra {{curto-circuito}} (sacrifica o elo por aquecimento)
- Disjuntor termomagnético >> Componente {{rearmável}} que substituiu fusíveis
- Atuação térmica do disjuntor (lâmina bimetálica) >> Protege contra {{sobrecarga}} (lenta, atraso)
- Atuação magnética do disjuntor (bobina) >> Protege contra {{curto-circuito}} (instantânea)
- Curva B (3-5× I_nom) >> Para cargas {{resistivas}} (iluminação, chuveiro)
- Curva C (5-10× I_nom) >> Para cargas com {{inrush}} (motores, transformadores)

O disjuntor protege o FIO DA PAREDE contra incêndio, não o equipamento.

### DR (Diferencial Residual)

- DR <> {{Dispositivo Diferencial Residual}} (proteção contra choque)
- Sensibilidade do DR para proteção humana >> {{30mA}}
- Lógica de disparo do DR >> Dispara se $I_{fase} \neq$ {{$I_{neutro}$}} (fuga para terra = choque)
- Relé térmico de sobrecarga >> Protege {{motor travado}} contra aquecimento prolongado (bimetálico ajustável)

30mA no coração = fibrilação. O DR salva vidas; o disjuntor salva fios.

### Drill — Proteção #[[Drill]]

- Fio suporta 25A, chuveiro puxa 21A → disjuntor correto (16A, 25A, 32A)? >> {{25A}} (protege o fio, nunca acima da capacidade)
- Máquina derreteu mas disjuntor de 50A não caiu → por quê? >> Corrente nunca passou de {{50A}} (falta de DR para detectar fuga)
- Motor de 10A travou por 30min → quem protege? >> {{Relé térmico}}
- Chuveiro + PC no mesmo circuito → DR dispara com fuga no PC. O chuveiro cai? >> {{Sim}} (mesma linha)

---

##  Módulo 4.9: Instalações Elétricas (NBR 5410)

### Dimensionamento de fios

- Seção mínima para iluminação (NBR 5410) >> {{1.5 mm²}}
- Seção mínima para TUG (tomada de uso geral) >> {{2.5 mm²}}
- TUE (circuito exclusivo) obrigatório acima de >> {{1200 VA}} (chuveiro, ar, microondas)
- Agrupamento de cabos no eletroduto → exige >> {{fator de correção}} (reduz capacidade de corrente)

### Queda de tensão

- Limite total de queda (medidor → última tomada, NBR 5410) >> {{7%}}
- Fórmula da queda de tensão >> $V_{queda}$ = {{$I_{carga} × R_{cabo}$}}

### Drill — Dimensionamento #[[Drill]]

- Ar-condicionado 2200W, 220V, FP=1 → I = >> {{10A}} (P/V)
- Fio 1.5mm² com R=1Ω, I=10A → queda = >> {{10V}} (V=IR)
- Fonte 220V, queda 10V → V na carga = >> {{210V}} (220−10)
- Queda de 10V em 220V → queda % = >> {{4.5%}} (10/220×100) — dentro do limite

---

## Módulo 4.10: Aterramento (TN, TT, IT)

### Esquemas de aterramento

- PE <> {{Protective Earth}} (fio verde/amarelo de proteção)
- TN-C >> PE e Neutro {{combinados}} no mesmo cabo (PEN) — barato mas perigoso
- TN-S >> PE e Neutro {{separados}} após o quadro — padrão brasileiro seguro
- TT >> Aterramento {{independente}} na carga (própria haste) — comum em zonas rurais
- IT >> Alimentação {{isolada}} da terra (sem contato fase-terra) — centros cirúrgicos

O TN-S é o padrão NBR. O IT evita faísca no primeiro defeito — vital em salas de cirurgia com gases inflamáveis.

### Drill — Aterramento #[[Drill]]

- Predição comercial novo → esquema recomendado? >> {{TN-S}}
- Centro cirúrgico → esquema obrigatório? >> {{IT}}
- Fio PEN combina PE+N no mesmo cabo → esquema? >> {{TN-C}}
