# Pilar 4: Eletrotécnica e Automação Industrial

> **Sobre este pilar**: Aqui a engenharia sai do laboratório e vai para a **fábrica e a subestação**. Motores de centenas de HP, sistemas trifásicos, proteção que salva vidas, e CLPs que controlam linhas de produção inteiras.
>
> **Ferramentas**: [CADe SIMU](https://www.cadesimu.net/) (comandos elétricos e automação) + [LTspice](https://www.analog.com/ltspice) (transformadores e motores) + PC_SIMU (CLP em Ladder)
>
> **Pré-requisitos**: [Pilar 1](ee_circuitos_roadmap.md) completo (AC essencial!). [Pilar 0](ee_matematica_fisica_roadmap.md) Fase 8 (mecânica/torque para motores) e Fases 9-10 (EM para transformadores). [Pilar 2](ee_eletronica_roadmap.md) recomendado (retificadores, inversores).
>
> **Conexões com outros pilares**:
> - **Base de**: [Pilar 0](ee_matematica_fisica_roadmap.md) — trig/fasores (Mód 0.4-0.5 → trifásico), torque/rotação (Mód 0.27 → motores), indução EM (Mód 0.33 → transformadores), termodinâmica (Mód 0.29 → dissipação)
> - **Base de**: [Pilar 1](ee_circuitos_roadmap.md) — AC/fasores (→ trifásico), potência (→ FP industrial), transformadores (→ Mód 4.4)
> - **Base de**: [Pilar 2](ee_eletronica_roadmap.md) — retificadores (→ VFD), MOSFET de potência (→ inversores), reguladores (→ fontes industriais)
> - **Alimenta**: [Pilar 5](ee_controle_sinais_roadmap.md) Módulo 5.6 (modelagem de motor como planta), [Pilar 5](ee_controle_sinais_roadmap.md) Módulo 5.9 (PID de velocidade)
> - **Segurança**: [HH Avançado F.1-F.4](hardware_hacking_roadmap_avancado.md) (CAN bus, Modbus/PROFINET, segurança de PLCs/SCADA)
>
> [Voltar ao Índice](ee_roadmap_index.md)

---

## Visão Geral

| Fase | Módulos | Foco | Horas |
|------|---------|------|-------|
| **Sistemas Trifásicos** | 4.1–4.3 | Geração, transmissão, cargas 3Φ | ~7h |
| **Máquinas Elétricas** | 4.4–4.7 | Transformadores, motores, partida, drives | ~10h |
| **Proteção e Instalações** | 4.8–4.10 | Fusíveis, disjuntores, dimensionamento | ~7h |
| **Automação Industrial** | 4.11–4.14 | Contatores, CLPs, Ladder, sensores | ~8h |

---

## Fase 1 — Sistemas Trifásicos

### Módulo 4.1: Geração e Sistema Trifásico
**Tempo: 2.5h**

#### O que memorizar
- **Por que trifásico?**: campo magnético girante constante (motor não precisa de "empurrão" para partir), potência instantânea constante (sem pulsação), usa menos cobre que 3 circuitos monofásicos separados
- **3 fases**: V_A, V_B, V_C defasadas de 120° entre si. Frequência: 60Hz (Brasil)
- **Estrela (Y)**: neutro disponível. V_linha = √3 × V_fase. Ex: V_fase = 127V → V_linha = 220V
- **Triângulo (Δ)**: sem neutro. V_linha = V_fase. I_linha = √3 × I_fase
- **Tensões típicas Brasil**: 127/220V (residencial), 380V (industrial), 13.8kV (distribuição), 138-765kV (transmissão)
- **Sequência de fases**: ABC (positiva) ou ACB (negativa) — IMPORTA para motores (inverte sentido de rotação!)

#### Intuição
Imagine 3 pessoas empurrando um carrossel simultaneamente, espaçadas igualmente a 120°. A cada instante, pelo menos uma está empurrando com força grande — o carrossel gira de forma suave e constante. Com 1 pessoa (monofásico), há momentos sem empurrão. Com 3, o torque é constante. É por isso que motores trifásicos são dominantes na indústria: potência constante, sem vibração, mais eficientes.

#### Projeto: "Visualizando Trifásico"
1. **No Falstad**: monte 3 fontes AC defasadas de 120° + 3 cargas iguais
   - V_A = 127∠0°, V_B = 127∠-120°, V_C = 127∠-240° (V RMS)
   - Observe: potência total CONSTANTE (soma das 3 potências instantâneas = constante!)
2. **Estrela vs Triângulo**: conecte 3 resistores de 100Ω em estrela, depois em triângulo
   - Meça V_fase, V_linha, I_fase, I_linha, P_total em cada configuração
   - Confirme: V_linha_Y = √3 × V_fase_Y e I_linha_Δ = √3 × I_fase_Δ
3. **No LTspice**: simule com `.tran 50m` — plote as 3 tensões e potência instantânea total
4. **Prompt IA**: *"Por que o sistema trifásico venceu o monofásico na guerra das correntes? Explique as 4 vantagens reais: campo girante, potência constante, economia de cobre, e possibilidade de 2 níveis de tensão (fase-neutro e fase-fase)."*
5. **Entregável**: Simulações + tabela comparativa Y vs Δ + gráficos

#### Erros Comuns
- Confundir tensão de fase com tensão de linha — em estrela: V_L = √3 × V_F. Se usar V_F onde deveria V_L, erro de 73%!
- Esquecer o fator √3 nos cálculos — é o erro mais frequente em trifásico
- Inverter sequência de fases ao conectar motor → motor gira ao contrário! (perigoso em esteiras/guindastes)

---

### Módulo 4.2: Potência Trifásica e Fator de Potência Industrial
**Tempo: 2h**

#### O que memorizar
- **Potência trifásica (carga equilibrada)**:
  - `P = √3 × V_L × I_L × cos(φ)` (ativa, Watts)
  - `Q = √3 × V_L × I_L × sen(φ)` (reativa, VAr)
  - `S = √3 × V_L × I_L` (aparente, VA)
- **Carga equilibrada**: as 3 fases têm mesma impedância → corrente no neutro = 0
- **Carga desequilibrada**: impedâncias diferentes → corrente no neutro ≠ 0 (problema!)
- **Correção de FP trifásico**: banco de capacitores (Y ou Δ) em paralelo com carga
- **Multa**: ANEEL/distribuidora aplica multa se FP < 0.92 (excedente de reativo)

#### Projeto: "Cálculo de Banco de Capacitores"
1. **Cenário**: fábrica consome 200kW a FP=0.78, alimentação 380V trifásica
   - Calcule S, Q, I_L
   - FP desejado: 0.95 → Calcule Q_capacitor necessário
   - Dimensione o banco de capacitores (C por fase, em Y)
2. **Monte no LTspice**: carga RL trifásica + banco de capacitores
3. **Compare**: I_linha antes e depois da correção → diminui!
4. **Entregável**: Cálculo completo + simulação + estimativa de economia

---

### Módulo 4.3: Medição e Instrumentação Trifásica
**Tempo: 1.5h**

#### O que memorizar
- **Wattímetro**: mede potência ativa. Método dos 2 wattímetros (Blondel)
- **Medição dos 2 wattímetros**: P_total = W1 + W2. Se W2 < 0, FP < 0.5 (cuidado!)
- **TC (Transformador de Corrente)**: reduz correntes altas (ex: 200A) para medição segura (ex: 5A)
- **TP (Transformador de Potencial)**: reduz tensões altas para medição
- **Analisador de energia**: mede V, I, P, Q, S, FP, THD, harmônicos em tempo real

#### Projeto: "Simulando Medição de Potência"
1. **Monte método dos 2 wattímetros** no LTspice para carga equilibrada e desequilibrada
2. **Calcule FP a partir das leituras**: `cos(φ) = cos(arctan(√3 × (W1-W2)/(W1+W2)))`
3. **Entregável**: Simulação + cálculos + comparação com valores teóricos

---

## Fase 2 — Máquinas Elétricas

### Módulo 4.4: Transformadores — O Coração da Rede Elétrica
**Tempo: 2.5h**

#### O que memorizar
- **Princípio**: duas bobinas acopladas magneticamente. V1/V2 = N1/N2 (relação de espiras)
- **Transformador ideal**: P_entrada = P_saída. V1×I1 = V2×I2. Sem perdas
- **Transformador real**: perdas no cobre (resistência dos fios, I²R), perdas no ferro (histerese + correntes parasitas)
- **Circuito equivalente**: impedância referida ao primário ou secundário
- **Ensaio a vazio**: mede perdas no ferro (P_fe) e corrente de magnetização
- **Ensaio em curto-circuito**: mede perdas no cobre (P_cu) e impedância de curto (Z_cc)
- **Regulação de tensão**: `%reg = (V_vazio - V_carga) / V_carga × 100`

#### Intuição
O transformador é a razão pela qual usamos AC e não DC para transmitir energia. Tesla venceu Edison porque: gerar em 13.8kV → transformar para 500kV (I baixa, perdas I²R baixas) → transmitir centenas de km → transformar para 220V → usar. Com DC, não dá para transformar (precisa de conversores eletrônicos complexos). Com AC, basta 2 bobinas e um núcleo de ferro.

#### Projeto: "Transformador Virtual"
1. **No LTspice**: monte transformador ideal (K=1) e real (K=0.98, R_prim, R_sec, L_mag)
   - Relação 10:1 (2200V → 220V)
   - Compare V_saída com e sem carga
2. **Simule os ensaios**: a vazio (sem carga, meça P_fe) e curto-circuito (V baixa, meça P_cu)
3. **Calcule regulação** para cargas de FP=1, 0.8ind, 0.8cap
4. **Entregável**: Circuito equivalente + ensaios simulados + regulação

---

### Módulo 4.5: Motor de Indução Trifásico — O Burro de Carga
**Tempo: 2.5h**

#### O que memorizar
- **Motor de indução**: mais usado na indústria (>90% dos motores). Robusto, barato, pouca manutenção
- **Campo girante**: 3 bobinas defasadas de 120° no estator → campo magnético girante
- **Velocidade síncrona**: `n_s = 120 × f / p` (f = frequência, p = nº de polos). 4 polos, 60Hz → n_s = 1800 RPM
- **Escorregamento**: `s = (n_s - n_r) / n_s`. Rotor sempre mais lento que o campo (senão, não haveria indução!)
- **Escorregamento típico**: 2-5% em carga nominal. n_r ≈ 1750 RPM para motor 4 polos
- **Corrente de partida**: 5 a 8× a corrente nominal! Pode causar queda de tensão na rede
- **Placa do motor**: potência (CV ou kW), tensão (220/380V), corrente, RPM, FP, rendimento, classe de isolamento

#### Intuição
Imagine um campo magnético girando ao redor de um cilindro de alumínio (rotor). O campo induz correntes no alumínio (Lei de Lenz), que criam seu próprio campo magnético, que interage com o campo girante → torque → rotação. O rotor tenta "alcançar" o campo, mas nunca consegue (sempre um pouco mais lento — o escorregamento). Se alcançasse, não haveria mais indução, não haveria mais corrente, não haveria mais torque.

#### Projeto: "Análise de Motor pela Placa"
1. **Selecione um motor real** (dados de catálogo WEG/ABB):
   - Ex: WEG W22 5CV, 4 polos, 220/380V, 60Hz
2. **Calcule**: n_s, s (com RPM nominal), I_partida (5×I_nom), P_elétrica = P_mec / η
3. **Monte circuito equivalente** no LTspice (R1, X1, R2'/s, X2', Xm)
4. **Simule**: varie s de 0 a 1 → plote curva torque vs velocidade (curva característica!)
5. **Prompt IA**: *"Um motor de indução 4 polos, 60Hz, tem RPM nominal de 1740. Calcule: velocidade síncrona, escorregamento, frequência do rotor. Explique por que o rotor NUNCA pode girar na velocidade síncrona."*
6. **Entregável**: Análise completa de motor + curva T×n

#### Erros Comuns
- Confundir número de polos com número de bobinas — 4 polos = 4 polos magnéticos, não 4 bobinas
- Ligar motor 220V em rede 380V (ou vice-versa) → ligação errada (Y quando deveria ser Δ) = motor queima
- Ignorar corrente de partida (5-8× I_nom) → se não dimensionar proteção e fiação adequadamente, provoca queda de tensão na rede inteira

---

### Módulo 4.6: Métodos de Partida de Motores
**Tempo: 2.5h**

#### O que memorizar
- **Partida direta**: contator liga motor diretamente na rede. I_partida = 5-8× I_nom. Só para motores pequenos (< 5-7.5CV)
- **Estrela-Triângulo (Y-Δ)**: parte em estrela (V/√3, I/3) → após aceleração, comuta para triângulo. I_partida = 33% da direta
- **Soft-Starter**: eletrônica de potência (SCRs) reduz tensão gradualmente. Controle suave, sem transiente de comutação
- **Inversor de frequência (VFD)**: controla V E f → controle total de velocidade e torque. Padrão moderno
- **Regra ABNT/concessionária**: partida direta limitada a motores até certa potência (varia por concessionária, geralmente 5-7.5CV)

#### Intuição
Dar partida direta num motor de 100CV é como ligar um chuveiro de 50kW — a rede inteira sofre (queda de tensão). Estrela-Triângulo é como começar em 1ª marcha e depois trocar — reduz o "solavanco" inicial. Soft-Starter é como acelerador progressivo. E o inversor de frequência é como ter um câmbio CVT — controle total em qualquer velocidade.

#### Projeto: "Simulando 3 Métodos de Partida"
1. **No CADe SIMU**: desenhe o diagrama de força e comando para:
   - Partida direta com reversão (3 contatores)
   - Estrela-Triângulo (3 contatores + temporizador)
   - Inclua proteção térmica e fusíveis
2. **Para cada**: desenhe o diagrama de tempo (sequência de acionamento)
3. **Compare**: corrente de partida, complexidade, custo
4. **Prompt IA**: *"Desenhe o diagrama de tempo da partida estrela-triângulo: quando K1, K2 e K3 ligam/desligam? O que acontece com a corrente em cada transição? O que é o 'pico de comutação' Y→Δ e por que ele existe?"*
5. **Entregável**: 2 diagramas de força + comando + análise comparativa

#### Erros Comuns
- Esquecer intertravamento elétrico e mecânico na reversão → dois contatores ligados = CURTO-CIRCUITO entre fases!
- Temporizar mal a transição Y→Δ (cedo demais = motor não acelerou, pico de corrente. Tarde demais = motor sobreaquece em Y)
- Não usar contato NF de proteção térmica no circuito de comando

---

### Módulo 4.7: Inversores de Frequência (VFD)
**Tempo: 2h**

#### O que memorizar
- **VFD (Variable Frequency Drive)**: retifica AC→DC → inverte DC→AC com f e V variáveis (V/f constante)
- **Curva V/f**: para manter o fluxo magnético constante, V/f deve ser constante. Reduzir f sem reduzir V → satura o motor
- **Frequência acima da nominal**: V não pode aumentar além da nominal → motor perde torque (região de enfraquecimento de campo)
- **Parâmetros de ajuste**: rampa de aceleração/desaceleração, I_máx, f_mín/máx, tipo de carga
- **Harmônicos**: VFDs geram harmônicos na rede (3º, 5º, 7º) → podem causar problemas. Filtros são necessários

#### Projeto: "Parametrizando um Inversor"
1. **Pesquise um VFD real** (WEG CFW500, ABB ACS580) — leia o manual
2. **Liste os 10 parâmetros principais** e explique cada um
3. **Cenário**: motor 10CV controlando uma esteira → defina rampa de 10s, I_máx = 150% I_nom, proteção por subtensão
4. **Entregável**: Tabela de parametrização + justificativa para cada parâmetro

---

## Fase 3 — Proteção e Instalações

### Módulo 4.8: Proteção Elétrica — Salvando Vidas e Equipamentos
**Tempo: 2.5h**

#### O que memorizar
- **Fusível**: protege contra curto-circuito. Fio fino que derrete com corrente excessiva. Descartável
- **Disjuntor termomagnético**: protege contra sobrecarga (bimetálico, lento) E curto-circuito (bobina eletromagnética, rápido). Rearmável
- **Disjuntor DR (Diferencial Residual)**: detecta fuga de corrente para terra (diferença entre fase e neutro). Protege PESSOAS contra choque elétrico. Sensibilidade: 30mA (pessoas), 300mA (equipamentos)
- **Relé térmico**: protege motor contra sobrecarga prolongada. Bimetálico → desliga contator
- **Aterramento**: conexão ao solo que cria caminho de baixa impedância para correntes de falta
- **Coordenação**: proteções devem atuar em cascata (mais próxima do defeito atua primeiro)
- **Curva de disparo**: tempo vs corrente do disjuntor (curvas B, C, D)

#### Intuição
Sem proteção, um curto-circuito derrete fios, causa incêndio e mata. O fusível/disjuntor age como "elo fraco intencional" — se sacrifica antes que o circuito principal sofra. O DR é como um "fiscal de cobrança" que verifica: a corrente que saiu pela fase voltou pelo neutro? Se não voltou toda (parte foi pelo corpo de alguém para a terra), desliga em milissegundos. É a proteção mais importante para humanos.

#### Projeto: "Dimensionando Proteção"
1. **Cenário**: circuito residencial com:
   - Tomadas: 10 tomadas de 100W, fio 2.5mm², disjuntor ?A
   - Chuveiro: 5500W/220V, fio ?mm², disjuntor ?A
   - Ar-condicionado: 2200W/220V, fio ?mm², disjuntor ?A
2. **Dimensione** fios e disjuntores seguindo NBR 5410:
   - I_projeto = P / V
   - Fio: tabela de capacidade de condução (considere agrupamento e temperatura)
   - Disjuntor: I_disj ≥ I_projeto e I_disj ≤ I_fio
3. **Monte diagrama unifilar** no CADe SIMU
4. **Entregável**: Memória de cálculo + diagrama unifilar

---

### Módulo 4.9: Instalações Elétricas — NBR 5410
**Tempo: 2.5h**

#### O que memorizar
- **Divisão de circuitos**: iluminação e tomadas em circuitos separados. Circuitos dedicados para cargas > 1200VA (chuveiro, ar-condicionado)
- **Quantidade mínima de circuitos**: NBR 5410 define regras de quantidade de tomadas e pontos de iluminação por cômodo
- **Seção de condutores**: depende de I_projeto, método de instalação, agrupamento, temperatura, queda de tensão
- **Queda de tensão**: máximo 7% do ponto de entrega ao ponto mais distante (4% internos é boa prática)
- **Quadro de distribuição**: disjuntor geral + DR + disjuntores por circuito

#### Projeto: "Projeto Elétrico Residencial Simplificado"
1. **Planta**: apartamento de 2 quartos (sala, cozinha, 2 quartos, banheiro, lavanderia)
2. **Levantamento de carga**: pontos de iluminação + tomadas (NBR 5410)
3. **Divisão de circuitos**: mínimo NBR + circuitos dedicados
4. **Dimensionamento**: fios, disjuntores, DR, quadro geral
5. **Diagrama unifilar** do quadro
6. **Entregável**: Projeto completo + memória de cálculo

---

### Módulo 4.10: Aterramento e Sistemas TN/TT/IT
**Tempo: 2h**

#### O que memorizar
- **TN-S**: neutro e terra separados o tempo todo. Mais seguro
- **TN-C**: neutro e terra combinados (PEN). Mais barato, menos seguro
- **TN-C-S**: PEN no alimentador, separa em PE e N no quadro. Mais comum no Brasil
- **TT**: terra da instalação independente do neutro do transformador. Obrigatório DR
- **IT**: sem aterramento direto. Usado em hospitais (continuidade em caso de primeira falta)
- **Resistência de aterramento**: < 10Ω (NBR 5410, regra geral). Hastes copperweld, malha de aterramento

#### Projeto: "Análise de Aterramento"
1. **Desenhe** diagrama dos 3 sistemas (TN-S, TT, IT) mostrando caminho da corrente de falta
2. **Calcule**: corrente de falta para terra em sistema TT (I_falta = V / (R_fonte + R_terra))
3. **Verifique**: DR de 30mA desarmaria com essa corrente? Em quanto tempo?
4. **Entregável**: Diagramas + cálculos + análise de segurança

---

## Fase 4 — Automação Industrial

### Módulo 4.11: Lógica de Contatores e Intertravamento
**Tempo: 2h**

#### O que memorizar
- **Contator**: relé de potência. Bobina energizada → contatos de força fecham (liga motor)
- **Contatos auxiliares**: NA (Normalmente Aberto) e NF (Normalmente Fechado)
- **Selo (auto-retenção)**: contato NA em paralelo com botão de liga → mantém contator ligado após soltar o botão
- **Intertravamento**: contato NF de um contator no circuito do outro → impede ambos ligarem ao mesmo tempo (segurança para reversão de motor!)
- **Simbologia**: S0 (desliga), S1/S2 (liga), K1/K2 (contatores), KT (temporizador), F1-F3 (fusíveis)

#### Intuição
A lógica de contatores é a **programação antes dos computadores**. Cada fio é uma "linha de código", cada contato NA é um `AND`, cada contato NF é um `NOT`, e a ligação em paralelo é um `OR`. O selo (auto-retenção) é uma variável booleana que se mantém TRUE depois de setada. E o intertravamento é uma condição `if (K1) then NOT K2`.

#### Projeto: "Comandos Clássicos no CADe SIMU"
1. **Partida direta com selo**: S0 (desliga) + S1 (liga) + K1 (contator) + selo
2. **Partida direta com reversão e intertravamento**: S0 + S1 (frente) + S2 (ré) + K1 + K2 + intertravamento mecânico e elétrico
3. **Estrela-Triângulo**: K1 (linha) + K2 (estrela) + K3 (triângulo) + temporizador
4. **Entregável**: 3 diagramas de comando + força no CADe SIMU

---

### Módulo 4.12: CLP — Controlador Lógico Programável (Ladder)
**Tempo: 2.5h**

#### O que memorizar
- **CLP/PLC**: substitui lógica de contatores por programa digital. Entradas (sensores) → Processamento → Saídas (atuadores)
- **Scan cycle**: 1) Lê entradas → 2) Executa programa → 3) Atualiza saídas → repete (~1-20ms)
- **Linguagem Ladder**: visual, baseada em relés. Contato NA, NF, bobina, temporizador, contador
- **Temporizadores**: TON (delay on), TOFF (delay off), TP (pulse)
- **Contadores**: CTU (up), CTD (down). Contam eventos (peças, ciclos)
- **Comparadores**: EQ, GT, LT para dados numéricos (analógicos)
- **Marcas/flags**: bits internos de memória — "variáveis booleanas" do CLP

#### Intuição
O CLP é a evolução natural dos contatores: em vez de refazer toda a fiação para mudar a lógica, basta reprogramar. Uma fábrica inteira pode ser controlada por CLPs: esteiras, robôs, prensas, fornos — tudo coordenado por programas Ladder que qualquer técnico eletricista consegue ler (parece um diagrama elétrico!).

#### Projeto: "Convertendo Contatores para CLP"
1. **Converta cada diagrama do módulo 4.11** para programa Ladder:
   - Partida direta → I0.0 (S0), I0.1 (S1) → Q0.0 (K1) + selo em Ladder
   - Reversão com intertravamento → compare com a solução em contatores
   - Estrela-Triângulo com temporizador TON
2. **Use um simulador de CLP** (OpenPLC, PC_SIMU, ou Codesys learning edition)
3. **Teste**: simule as entradas → observe saídas ativando corretamente
4. **Entregável**: 3 programas Ladder funcionais + comparação com contatores

---

### Módulo 4.13: CLP Avançado — Semáforo e Processos Sequenciais
**Tempo: 2h**

#### O que memorizar
- **GRAFCET**: linguagem gráfica para processos sequenciais. Etapas + Transições + Ações
- **Sequência**: cada etapa tem ações associadas e condições de transição para a próxima
- **Divergência**: bifurcações (alternativa: OU, simultânea: E)
- **Modos de operação**: automático, manual, emergência
- **SFC (Sequential Function Chart)**: versão IEC 61131-3 do GRAFCET, implementável em CLPs

#### Projeto: "Semáforo Inteligente em CLP"
1. **Semáforo de 2 vias** com GRAFCET:
   - 4 etapas: Via A verde + Via B vermelho → Via A amarelo → Via B verde + Via A vermelho → Via B amarelo
   - Temporizadores em cada etapa
2. **Adicione botão de pedestre**: interrompe a sequência, vai para Via B verde com pedestrian walk
3. **Modo noturno**: após 22h, apenas amarelo piscando em ambas vias
4. **Implemente em Ladder** a partir do GRAFCET
5. **Entregável**: GRAFCET desenhado + programa Ladder + testes de cada modo

---

### Módulo 4.14: Sensores Industriais e Integração
**Tempo: 2h**

#### O que memorizar
- **Sensor indutivo**: detecta metais a curta distância (~2-30mm). Sem contato físico
- **Sensor capacitivo**: detecta qualquer material (líquidos, plásticos, metais). Distância ~2-20mm
- **Sensor óptico**: emissão/recepção de luz. Tipos: barreira, retro-reflexivo, difuso
- **Encoder**: rotativo, conta pulsos → mede posição/velocidade. Incremental (pulsos) e absoluto (posição exata)
- **Sinais**: NPN (sink) vs PNP (source). Brasil usa mais PNP. Saída: 24V DC ou 4-20mA (analógico)
- **4-20mA**: padrão industrial para sinais analógicos. 4mA = 0%, 20mA = 100%. Por que não 0-20? Porque 0mA é indistinguível de fio partido!

#### Projeto: "Linha de Produção Simulada"
1. **Cenário**: esteira transportadora com:
   - Sensor indutivo: detecta presença de peça metálica
   - Sensor óptico: contagem de peças
   - Motor da esteira controlado por CLP
2. **Lógica CLP**: esteira liga quando botão pressionado, para quando sensor detecta peça na posição, conta peças, para após 10 peças (lote completo)
3. **Implemente em Ladder** com temporizadores e contadores
4. **Prompt IA**: *"No CLP, qual a diferença entre um temporizador TON (delay on) e um TP (pulse)? Desenhe o diagrama de tempo de cada um. Quando usar cada tipo?"*
5. **Entregável**: Programa Ladder completo + lógica documentada

#### Checkpoint Final — Pilar 4 Completo!
- [ ] Calcula potência, corrente e FP em sistemas trifásicos
- [ ] Entende o motor de indução e seus métodos de partida
- [ ] Dimensiona proteção e instalações elétricas (NBR 5410 básica)
- [ ] Programa CLPs em Ladder para processos industriais
- [ ] Sabe converter diagram a de contatores para programa Ladder

> **Próximo passo**: [Pilar 5 — Controle e Sinais](ee_controle_sinais_roadmap.md)

---

## Fase 5 — Máquinas Avançadas e Sistemas de Potência

### Módulo 4.15: Motor/Gerador Síncrono
**Tempo: 2.5h**

#### O que memorizar
- **Máquina síncrona**: rotor gira EXATAMENTE na velocidade síncrona (n_s = 120f/p). Diferente do motor de indução!
- **Como gerador**: toda usina (hidro, térmica, nuclear, eólica) usa gerador síncrono. Controle de V e f
- **Como motor**: usado quando se precisa de velocidade CONSTANTE ou controle preciso de fator de potência
- **Excitação**: corrente DC no rotor cria campo magnético. Mais excitação → mais potência reativa (Q) gerada
- **Curva V**: I_armadura vs I_excitação. Mínimo = FP=1. À esquerda: sub-excitado (absorve Q). À direita: sobre-excitado (gera Q)
- **Compensador síncrono**: motor síncrono sem carga mecânica, usado APENAS para corrigir fator de potência
- **Diagrama fasorial**: V=E+jXsI. Permite calcular E, δ (ângulo de carga), e estabilidade

#### Intuição
O gerador síncrono é o "coração" do sistema elétrico. Controlar sua excitação controla a tensão da rede. Controlar o torque mecânico (turbina) controla a frequência. Se a demanda aumenta, os geradores desaceleram momentaneamente (frequência cai) até os reguladores aumentarem a potência das turbinas. É por isso que a frequência da rede é um indicador de equilíbrio geração-carga!

#### Projeto
1. **Diagrama fasorial**: desenhe para FP=1, 0.8 ind, 0.8 cap → observe como δ e E mudam
2. **Curva V**: plote I_armadura vs I_excitação para carga constante → identifique ponto de FP=1
3. **Prompt IA**: *"Por que todas as usinas usam geradores síncronos e não assíncronos? Explique o controle de tensão via excitação e o controle de frequência via potência mecânica."*

---

### Módulo 4.16: Introdução a Sistemas de Potência
**Tempo: 3h**

#### O que memorizar
- **Sistema de potência**: geração → transmissão (HV) → distribuição (MV/LV) → consumo
- **Por que alta tensão**: P=VI, mesma P com V alto → I baixo → perdas I²R muito menores → fios mais finos
- **Sistema por unidade (pu)**: normaliza tensões, correntes e impedâncias pela base. Simplifica cálculos em sistemas com múltiplas tensões (transformadores)
- **Curto-circuito**: corrente que flui quando duas fases (ou fase-terra) se conectam acidentalmente
  - Icc = V/Z_total. Z_total inclui impedância de TUDO: gerador, transformadores, linhas, cabos
  - 3Φ simétrico: Icc3 = V_pu / Z_pu (mais simples). Grau: ~10-50kA em sistemas industriais!
  - Capacidade de interrupção do disjuntor DEVE ser ≥ Icc no ponto de instalação
- **Fluxo de potência (Load Flow)**: calcula V, I, P, Q em cada barra do sistema para uma dada carga. Base do planejamento
- **Tipos de barra**: Slack (V, θ fixos — referência), PV (geração — P, V fixos), PQ (carga — P, Q fixos)

#### Projeto
1. **Calcule Icc** num sistema simples: gerador → transformador → barra de 380V. Monte diagrama de impedâncias em pu
2. **Verifique**: disjuntor de 25kA é suficiente para este ponto?
3. **Load flow**: monte sistema de 3 barras → resolva com Python (método de Newton-Raphson simplificado)
4. **Prompt IA**: *"Explique o sistema por unidade (pu) com um exemplo: gerador de 10MW, 13.8kV alimentando transformador 13.8/138kV, linha de 100km, transformador 138/13.8kV. Por que pu facilita o cálculo?"*

---

### Módulo 4.17: Qualidade de Energia e Harmônicos
**Tempo: 2h**

#### O que memorizar
- **Harmônicos**: frequências múltiplas da fundamental (60Hz). 3º=180Hz, 5º=300Hz, 7º=420Hz
- **Fontes**: retificadores, inversores, fontes chaveadas, lâmpadas fluorescentes. Cargas não-lineares!
- **THD (Total Harmonic Distortion)**: THD = √(Σ Vn²)/V₁ × 100%. Limite IEEE 519: THD < 5%
- **Efeitos**: aquecimento de transformadores e cabos, ressonância com banco de capacitores, disparo falso de equipamentos
- **3º harmônico no neutro**: em sistema trifásico, harmônicos de 3ª ordem se SOMAM no neutro! I_neutro pode ser > I_fase!
- **Soluções**: filtros passivos (LC sintonizado), filtros ativos (eletrônica de potência), transformadores K
- **Afundamento de tensão (sag)**: queda momentânea de tensão (partida de motor, falta remota). Duração: 0.5 ciclos a 1min

#### Projeto
1. **Gere** sinal com harmônicos em Python: v(t) = sin(ωt) + 0.3sin(3ωt) + 0.2sin(5ωt) → calcule THD
2. **Filtre**: projete filtro LC sintonizado no 5º harmônico (300Hz) → verifique redução de THD
3. **Simule** no LTspice: retificador trifásico → observe corrente rica em harmônicos na rede

#### Checkpoint — Máquinas e Potência
- [ ] Entende gerador síncrono e controle de V/f
- [ ] Calcula corrente de curto-circuito em pu
- [ ] Sabe o que são harmônicos e como afetam a rede

---

## Se Você Travar

| Problema | Solução |
|----------|--------|
| Confusão V_fase vs V_linha | Em estrela: V_L = √3 × V_F. Em triângulo: V_L = V_F. SEMPRE identifique se a carga está em Y ou Δ antes de calcular |
| Motor não parte (simulação) | Verifique sequência de fases. Verifique ligação Y/Δ vs tensão de rede |
| Diagrama de força vs comando | Força = potência (fios grossos, fusíveis, contatores). Comando = lógica (fios finos, botões, bobinas). SÃO SEPARADOS! |
| Ladder não funciona | Verifique: contatos NA/NF corretos? Bobina energiza? Selo (auto-retenção) está lá? Botão de emergência é NF? |
| Dimensão de fio errada | Sempre parta da corrente de projeto (I = P / (V × FP × η)). Depois aplique fatores de correção (agrupamento, temperatura) |
| Fator de potência não melhora | Verifique se o banco de capacitores está em paralelo com a carga (não em série!). Calcule Q_cap = Q_antes - Q_depois |

> **⚠️ SEGURANÇA**: Nunca trabalhe em circuitos trifásicos energizados. Tensões de 380V+ são LETAIS. Sempre desligue, teste ausência de tensão, aterre, e sinalize antes de qualquer intervenção.
