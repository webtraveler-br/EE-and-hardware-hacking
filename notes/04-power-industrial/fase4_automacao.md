# Módulo 4 — Eletrotécnica: Fase 4 — Automação Industrial
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 4.11: Comandos Elétricos e Contatores

### Circuitos de comando vs força

- Circuito de comando >> Fios {{finos}} (24V/110V) — lógica, botões, bobinas
- Circuito de força >> Fios {{grossos}} (380V) — potência, motor, contatores
- Contator <> {{Relé de potência}} (botão de 24V aciona lâminas de 380V)
- NA (Normalmente Aberto) <> {{NO}} — fecham quando a bobina liga
- NF (Normalmente Fechado) <> {{NC}} — abrem quando a bobina liga
- Selo (auto-retenção) >> Contato NA em {{paralelo}} com a botoeira — mantém K ligado após soltar o dedo

### Segurança

- Intertravamento >> NF de K_ré no circuito de K_frente (e vice-versa) → impede {{acionamento simultâneo}}
- Botão de emergência (S0) deve ser >> {{NF}} (rompe o circuito se apertado OU se quebrar)

### Drill — Lógica de contatos #[[Drill]]

- Contatos em série na trilha → lógica >> {{AND}} (ambos precisam conduzir)
- Contatos em paralelo → lógica >> {{OR}} (basta um conduzir)
- Motor K ligou e selo está ativo. Apertar S0 (NF) → >> {{Motor desliga}} (rompe o selo)
- K1 tem contato NA auxiliar e NF auxiliar. Bobina K1 energizada → NA fica >> {{fechado}}, NF fica >> {{aberto}}

---

## Módulo 4.12: CLP e Ladder

### Conceitos

- CLP <> {{Controlador Lógico Programável}} (PLC)
- Vantagem do CLP sobre painel de contatores >> Alterar lógica por {{software}} (sem reconectar fios)
- Ladder <> {{Diagrama de escada}} (linguagem gráfica que imita contatores)
- Scan cycle do CLP >> {{1) Ler inputs → 2) Executar programa → 3) Atualizar outputs}}

### Blocos Ladder

- TON <> {{Timer On-Delay}} (liga saída após X segundos de entrada contínua)
- TOFF <> {{Timer Off-Delay}} (mantém saída por X segundos após entrada desligar)
- TP <> {{Timer Pulse}} (pulso fixo a partir de um trigger)
- CTU <> {{Counter Up}} (conta eventos, aciona ao atingir setpoint)
- Comparadores (EQ, GT, LT) >> Permitem leituras {{analógicas}} no CLP (nível, temperatura, pressão)

### Drill — Ladder #[[Drill]]

- Tampa do forno aberta → ventilador roda 20s após fechar → bloco = >> {{TOFF}} (delay após desligar)
- Botão pulsado 0.5s → sirene toca exatos 5s → bloco = >> {{TP}} (pulse timer)
- Esteira conta 10 peças → aciona empacotador → bloco = >> {{CTU}} (counter up, setpoint=10)
- Sensor de temperatura > 80°C → aciona ventilador → comparador = >> {{GT}} (greater than)

---

##  Módulo 4.14: Sensores Industriais

### Tipos de sensores

- Sensor indutivo >> Detecta exclusivamente {{metais}} (1-30mm, por relutância magnética)
- Sensor capacitivo >> Detecta {{qualquer material}} (água em plástico, pó, papelão — pelo dielétrico)
- Sensor óptico >> Usa {{luz}} (barreira, reflexivo, difuso)
- Encoder >> Mede {{posição/velocidade}} do eixo (disco com furos ou magnético)

### NPN vs PNP (HH essencial)

- NPN (Sink) >> Sensor ativado puxa saída para {{GND}} (precisa de pull-up externo)
- PNP (Source) >> Sensor ativado empurra saída para {{+V}} (ex: +24V)
- Padrão asiático (placas/microPLC) <> {{NPN}} (sink)
- Padrão europeu industrial <> {{PNP}} (source)

### Loop 4-20mA

- Por que 4-20mA e não 0-20mA? >> Se o sinal cair a {{0mA}}, o engenheiro sabe que o cabo {{rompeu}} (wire-break detection)
- 4mA = >> {{zero de escala}} (ex: 0°C ou 0 bar)
- 20mA = >> {{fundo de escala}} (ex: 100°C ou 10 bar)

4-20mA é o padrão failsafe das refinarias. 0mA nunca é valor válido → se leu 0 = cabo cortado.

### Drill — Sensores #[[Drill]]

- Detectar lata de alumínio na esteira → sensor = >> {{Indutivo}} (metal)
- Detectar caixa de papelão → sensor = >> {{Capacitivo}} (não-metal)
- Sensor NPN ativado → nível na saída = >> {{GND (0V)}}
- Loop 4-20mA, leitura 12mA, escala 0-100°C → temperatura = >> {{50°C}} ((12−4)/(20−4) × 100)
- Loop 4-20mA, leitura 0mA → significa >> {{Cabo rompido}} (não é 0°C!)

---

##  Módulo 4.13: GRAFCET / SFC

### Automação sequencial

- Ladder é lógica >> {{combinacional}} (tudo roda junto a cada scan)
- Processos "enche → aquece → drena" pedem lógica >> {{sequencial}} (máquina de estados)
- GRAFCET <> {{Gráfico Funcional de Comando de Etapas}} (norma europeia para sequências)
- SFC <> {{Sequential Function Chart}} (implementação do GRAFCET na IEC 61131-3)
- Etapa (step) >> Quadrado que executa uma {{ação}} (ex: ligar bomba)
- Transição >> Condição que autoriza passar para a {{próxima etapa}} (ex: sensor de nível atingido)
- Pode avançar sem transição verdadeira? >> {{Nunca}} — somente a transição autoriza o avanço

### Drill — GRAFCET #[[Drill]]

- Processo: encher tanque → aquecer → drenar. Quantas etapas no GRAFCET? >> {{3}} (+ etapa inicial "espera")
- Etapa "Aquecendo" → transição "T > 80°C" → próxima etapa >> {{Drenar}}
