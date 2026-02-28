# Pilar Lab: Laboratório Real

> **Sobre este pilar**: Este pilar transforma o conhecimento teórico e simulado dos Pilares 0-5 em **habilidade prática real**. Cada módulo corresponde a projetos dos outros pilares, agora executados com componentes físicos, instrumentos reais e as imperfeições do mundo real.
>
> **Quando estudar**: EM PARALELO com os pilares teóricos, a partir do Pilar 1. Não espere terminar tudo para começar a praticar — cada módulo aqui indica de qual pilar é pré-requisito.
>
> **Mapa de pré-requisitos por módulo**:
> - **L.2-L.3**: [Pilar 1](ee_circuitos_roadmap.md) Mód 1.1-1.3 (Ohm, KVL/KCL, divisor de tensão)
> - **L.4**: [Pilar 1](ee_circuitos_roadmap.md) Mód 1.10-1.11 (transitórios RC/RL) + [Pilar 0](ee_matematica_fisica_roadmap.md) Mód 0.18 (EDO 1ª ordem)
> - **L.5**: [Pilar 1](ee_circuitos_roadmap.md) Mód 1.13-1.14 (AC, senóides)
> - **L.6**: [Pilar 2](ee_eletronica_roadmap.md) Mód 2.1-2.4 (diodos, retificação, Zener)
> - **L.10-L.12**: [Pilar 2](ee_eletronica_roadmap.md) (esquemáticos como modelo) + [HH 2.2](hardware_hacking_roadmap.md) (análise visual de PCB — perspectiva complementar!)
> - **L.13-L.14**: [Pilar 3](ee_digital_embarcados_roadmap.md) Mód 3.9-3.12 (Arduino, ADC, PWM, serial)
> - **L.15**: [Pilar 3](ee_digital_embarcados_roadmap.md) Mód 3.13 (WiFi/IoT) + [HH 5.6](hardware_hacking_roadmap.md) (segurança de interfaces web IoT!)
> - **L.17**: [Pilar 2](ee_eletronica_roadmap.md) Mód 2.19 (EMC teórica → agora na prática)
> - **L.19 Opção C**: [Pilar 5](ee_controle_sinais_roadmap.md) Mód 5.9 (PID teórico → implementação real!)
>
> [Voltar ao Índice](ee_roadmap_index.md)

---

## Visão Geral

| Fase | Módulos | Foco | Horas |
|------|---------|------|-------|
| **Montagem da Bancada** | L.1–L.2 | Equipamentos, segurança, primeiros contatos | ~4h |
| **Protoboard e Medição** | L.3–L.6 | Circuitos reais, osciloscópio, multímetro | ~10h |
| **Solda e Construção** | L.7–L.9 | Solda, perfboard, montagem | ~8h |
| **PCB Design** | L.10–L.12 | KiCad, layout, fabricação | ~12h |
| **Embarcados Reais** | L.13–L.15 | Arduino/ESP32 físico, sensores reais, debugging | ~10h |
| **Instrumentação Avançada** | L.16–L.17 | Análise de sinais, EMI, debugging avançado | ~6h |
| **Projeto Completo** | L.18–L.19 | Do conceito à PCB fabricada e testada | ~10h |

**Total: ~60h** de prática hands-on.

---

## Segurança

> **ATENÇÃO**
> **NUNCA trabalhe com tensão de rede (127/220V) sem supervisão de profissional qualificado.**
> Este pilar usa **exclusivamente baixa tensão (≤30V DC)** até o módulo L.17. Tensões acima de 50V DC ou 30V AC podem ser LETAIS.

**Regras de segurança:**
1. SEMPRE desligue a alimentação antes de modificar o circuito
2. NUNCA toque em dois pontos do circuito simultaneamente quando energizado
3. Use óculos de proteção ao soldar
4. Ventile o ambiente ao soldar (fumaça de fluxo é tóxica a longo prazo)
5. Capacitores podem reter carga — descarregue antes de manusear
6. Na dúvida, DESLIGUE e PERGUNTE

---

## Fase 1 — Montagem da Bancada

### Módulo L.1: Equipamentos Essenciais — O Kit Mínimo
**Tempo: 2h (pesquisa + compra)**

#### Lista de Compras (investimento mínimo ~R$250-400)

**Instrumentos (prioridade 1):**

| Item | Faixa de preço | Por que é essencial |
|------|---------------|-------------------|
| Multímetro digital (ex: UNI-T UT33C) | R$50-100 | Mede V, I, R, continuidade, diodo |
| Fonte de bancada ajustável 0-30V/0-5A | R$150-250 | Alimentação controlada com limite de corrente |
| *Alternativa à fonte*: carregador USB 5V + módulo step-down | R$30-50 | Mais barato mas limitado |

**Componentes (prioridade 1):**

| Item | Faixa | Quantidade |
|------|-------|-----------|
| Protoboard 830 pontos | R$15-25 | 1-2 |
| Kit jumpers M-M, M-F, F-F | R$15-20 | 1 kit (~120 fios) |
| Kit resistores 1/4W (1Ω a 1MΩ) | R$20-30 | 1 kit (~600 peças) |
| Kit capacitores cerâmicos + eletrolíticos | R$20-30 | 1 kit |
| Kit LEDs 5mm (vermelho, verde, amarelo, azul) | R$10-15 | 1 kit (~100) |
| Potenciômetros 10kΩ | R$5-10 | 3-5 |
| Push buttons 6×6mm | R$5-10 | 10-20 |
| Arduino Uno R3 (clone) | R$35-50 | 1 |
| Diodos 1N4007 + 1N4148 + Zener 5.1V | R$10-15 | 10+10+5 |
| Transistores BC547 (NPN) + IRF540N (MOSFET) | R$10-15 | 5+3 |

**Instrumentos (prioridade 2 — comprar depois):**

| Item | Faixa | Quando comprar |
|------|-------|---------------|
| Osciloscópio DSO (ex: Hantek DSO5102P ou DSO-138 kit) | R$100-600 | Módulo L.5 |
| Ferro de solda com controle de temperatura | R$50-100 | Módulo L.7 |
| Estanho 0.8mm 60/40 com fluxo | R$20-30 | Módulo L.7 |
| Sugador de solda + malha dessoldadora | R$15-25 | Módulo L.7 |
| Lupa/terceira mão com lupa | R$25-40 | Módulo L.7 |

#### Projeto: "Inventário e Teste"
1. **Monte a lista** personalizada baseada no seu orçamento
2. **Ao receber**: teste CADA componente com o multímetro
   - Resistores: meça R, compare com código de cores (Módulo 1.1B!)
   - LEDs: teste no modo diodo do multímetro
   - Capacitores: multímetro com modo capacímetro, ou estime pela descarga RC
3. **Organize**: caixa de componentes com divisórias, etiquetados

---

### Módulo L.2: O Multímetro — Seu Melhor Amigo
**Tempo: 2h**
**Pré-requisito**: [Pilar 1, Mód 1.1-1.2](ee_circuitos_roadmap.md) (Lei de Ohm, tensão, corrente)

#### 📚 O que dominar
- **Tensão DC/AC** (V): pontas em PARALELO com o componente. NUNCA em série!
- **Corrente** (A): pontas em SÉRIE com o circuito. Selecione escala adequada ANTES de conectar
- **Resistência** (Ω): circuito DESLIGADO! Corrente do multímetro passa pelo componente
- **Continuidade** (beep): verifica se dois pontos estão conectados. Essencial para debugging
- **Teste de diodo**: mede V_f (~0.6V para Si, ~0.3V para Ge, ~2V para LED)
- **Escala automática vs manual**: automática é mais segura para iniciantes

#### PERIGO
- Medir CORRENTE com as pontas na posição de TENSÃO → fusível queima (ou pior)
- Medir RESISTÊNCIA com circuito ligado → leitura errada e possível dano ao multímetro
- Selecionar escala de corrente muito baixa para a corrente real → fusível queima

#### Projeto: "10 Medições Fundamentais"
1. **Meça** a tensão de: pilha 1.5V, pilha 9V, USB (5V), fonte de bancada ajustada em 3.3V e 12V
2. **Monte** circuito R=1kΩ com V=5V → meça I (deve ser ~5mA). Confirme V=IR
3. **Meça** resistência de 5 resistores → compare com código de cores
4. **Teste** continuidade de jumpers e fios — encontre um fio "quebrado" propositalmente
5. **Teste** 3 LEDs no modo diodo — identifique cor pela V_f

---

## Fase 2 — Protoboard e Medição

### Módulo L.3: Circuitos na Protoboard — Seu Primeiro Circuito Real
**Tempo: 2.5h**
**Pré-requisito**: [Pilar 1, Mód 1.1-1.3](ee_circuitos_roadmap.md) (Ohm, KVL, divisor de tensão)

#### 📚 O que dominar
- **Layout da protoboard**: barras laterais (VCC/GND) conectam na horizontal, furos centrais na vertical (5 pinos por coluna)
- **Regra**: componentes ATRAVESSAM o canal central. Nunca dois pinos do mesmo componente na mesma coluna
- **Alimentação**: conecte VCC e GND das DUAS barras laterais (com jumper entre elas)

#### Projeto: "Circuitos do Pilar 1 no Mundo Real"
1. **Divisor de tensão**: R1=1kΩ, R2=2.2kΩ, V_in=5V → meça V_out. Compare com V_in × R2/(R1+R2)
2. **LED + resistor**: calcule R para LED vermelho com 5V. Monte. Meça I com multímetro em série
3. **LEDs em série**: 2 LEDs + resistor. Calcule R considerando 2×V_f
4. **LEDs em paralelo**: cada LED com seu próprio resistor (NUNCA compartilhe resistor em paralelo!)
5. **Circuito com potenciômetro**: dimmer de LED analógico

#### Erros Comuns da Protoboard
- Componente com dois pinos na MESMA coluna → curto-circuito
- Esquecer de conectar GND entre fonte e circuito → nada funciona
- Fio fazendo mau contato → comportamento intermitente (o bug mais difícil!)
- Usar protoboard para correntes >500mA → contatos aquecem, derretem

---

### Módulo L.4: Transitórios Reais — RC e RL na Bancada
**Tempo: 2.5h**
**Pré-requisito**: [Pilar 1, Mód 1.10-1.11](ee_circuitos_roadmap.md) (transitórios RC/RL). Ver também [Pilar 0, Mód 0.18](ee_matematica_fisica_roadmap.md) (EDO de 1ª ordem, τ=RC)

#### Projeto: "Vendo τ com Seus Próprios Olhos"
1. **RC lento** (visível): R=100kΩ, C=1000μF → τ=100s. Carregue com 5V, desconecte, meça V a cada 10s com multímetro. Plote V(t) → exponencial decrescente!
2. **RC rápido** (osciloscópio): R=1kΩ, C=100nF → τ=100μs. Alimente com onda quadrada 1kHz → observe carga/descarga no osciloscópio
3. **Anti-bounce**: R=10kΩ, C=100nF no botão → compare sinal com e sem filtro RC no osciloscópio
4. **Meça τ**: tempo para V cair a 37% (1/e) do valor inicial

---

### Módulo L.5: O Osciloscópio — Vendo Sinais no Tempo
**Tempo: 3h**
**Pré-requisito**: [Pilar 1, Mód 1.13-1.14](ee_circuitos_roadmap.md) (AC, senóides, frequência)

#### 📚 O que dominar
- **Canais**: CH1, CH2 (geralmente 2-4). Cada um mede um ponto do circuito
- **Timebase** (horizontal): tempo por divisão (s/div, ms/div, μs/div)
- **Volts/div** (vertical): tensão por divisão
- **Trigger**: sincroniza a captura com um evento (borda de subida/descida). SEM trigger, a tela "rola" sem parar
- **Modo AC/DC**: DC mostra tudo. AC remove o offset DC (só vê a parte variável)
- **Cursores**: medem tensão, tempo, frequência com precisão
- **FFT**: alguns osciloscópios fazem FFT — mostra espectro de frequência!

#### Projeto: "Dominando o Osciloscópio"
1. **Calibração**: conecte a sonda ao sinal de calibração do osciloscópio (onda quadrada 1kHz). Ajuste a compensação da sonda
2. **Meça** a senóide da rede (com transformador 220→12V isolador! NUNCA conecte direto à rede!): frequência, V_pico, V_rms
3. **Meça PWM do Arduino**: `analogWrite(9, 128)`. Observe duty cycle de 50%, frequência de 490Hz
4. **2 canais**: meça V_in e V_out de um filtro RC simultaneamente → observe defasagem!
5. **Trigger**: configure para capturar um evento único (botão pressionado → capture o bounce)

---

### Módulo L.6: Fontes e Reguladores na Prática
**Tempo: 2h**
**Pré-requisito**: [Pilar 2, Mód 2.1-2.4](ee_eletronica_roadmap.md) (diodos, retificação, Zener)

#### Projeto: "Fonte de Alimentação Real 5V"
1. **Retificador**: transformador 12V AC → ponte de diodos 1N4007 → observe ripple com osciloscópio (SEM capacitor: senoide retificada)
2. **Filtro**: adicione C=1000μF → observe redução do ripple. Meça V_ripple
3. **Regulador 7805**: adicione LM7805 → saída 5V estável. Meça com multímetro
4. **Teste de carga**: conecte cargas de 100mA a 1A. Meça queda de tensão, toque no 7805 → ESQUENTA! Calcule P_dissipada = (V_in-5)×I
5. **Dissipador**: adicione dissipador ao 7805 → meça temperatura com termômetro antes e depois

---

## Fase 3 — Solda e Construção

### Módulo L.7: Soldagem — A Habilidade Manual Fundamental
**Tempo: 3h**

#### 📚 O que dominar
- **Temperatura**: 350-380°C para solda 60/40 (estanho/chumbo). Lead-free: 380-420°C
- **Técnica**: aqueça a JUNTA (pad + terminal), aplique estanho NA JUNTA (não na ponta do ferro)
- **Tempo**: 2-3 segundos por junta. Mais = dano ao componente. Menos = solda fria
- **Solda boa**: cônica, brilhante, côncava. **Solda ruim**: globular, opaca, convexa (solda fria!)
- **Fluxo**: limpa superfícies, melhora aderência. Contido no estanho com alma de resina
- **Dessoldagem**: sugador (mais fácil) ou malha dessoldadora (mais limpa)

#### Projeto: "Progressão de Solda"
1. **Pratique** em placa universal: solde 20 resistores em linha. Inspecione cada junta
2. **Solde** 10 jumpers (fios) — prática de descascar e estanhar
3. **Solde** conectores header (mais difícil — precisa manter alinhamento)
4. **Monte circuito funcional**: LED + resistor em placa universal, soldado permanentemente
5. **Dessoldagem**: remova 5 componentes sem danificar a placa

#### Segurança
- NUNCA toque na ponta do ferro (350°C!)
- Use suporte para o ferro quando não estiver usando
- Ventile — fumaça do fluxo irrita olhos e pulmões
- Lave as mãos após soldar (estanho com chumbo é tóxico)

---

### Módulo L.8: Perfboard e Montagem Permanente
**Tempo: 2.5h**
**Pré-requisito**: [Lab L.7](#módulo-l7-soldagem--a-habilidade-manual-fundamental) (solda)

#### Projeto: "Fonte 5V Permanente"
1. **Planeje o layout** no papel antes de soldar (componentes de um lado, solda do outro)
2. **Monte a fonte do L.6** em perfboard: ponte de diodos + capacitor + 7805 + LEDs indicadores
3. **Adicione**: conector de entrada (P4), conector de saída (bornes), fusível
4. **Teste**: meça V_out com carga. Funciona igual ao protoboard? (deveria funcionar MELHOR — conexões mais sólidas)

---

### Módulo L.9: Cabos, Conectores e Mecânica
**Tempo: 2.5h**

#### 📚 O que dominar
- **Conectores comuns**: P4 (alimentação), bornes parafuso, headers 2.54mm, JST, KF2510
- **Crimpagem**: para conectores tipo Dupont/JST. Requer alicate de crimpar
- **Organização de cabos**: abraçadeiras, espiral, identificação
- **Gabinete**: caixas plásticas (patola, hammond). Furação para conectores e display
- **Fixação de PCB**: espaçadores M3, parafusos, encaixe snap-fit

#### Projeto
1. **Crimpe** 10 conectores Dupont (macho e fêmea)
2. **Monte** a fonte L.8 em gabinete plástico com conectores externos
3. **Etiquete** todos os conectores e fios

---

## Fase 4 — Design de PCB

### Módulo L.10: KiCad — Esquemático
**Tempo: 3h**

#### 📚 O que dominar
- **KiCad** (gratuito, open-source, profissional): esquemático → layout → Gerber → fabricação
- **Esquemático**: arraste componentes da biblioteca, conecte com fios, adicione net labels, alimentação (VCC/GND)
- **Símbolos**: resistor=R, capacitor=C, LED=D, CI=U. Cada símbolo tem pinos com nomes
- **Anotação**: numere componentes automaticamente (R1, R2, C1, C2...)
- **ERC (Electrical Rules Check)**: verifica erros de conexão (pinos desconectados, conflitos)

#### Projeto: "Esquemático da Fonte 5V"
1. **Instale KiCad 8** (gratuito)
2. **Desenhe** o esquemático da fonte 5V que você já montou em perfboard
3. **Adicione**: conector P4, ponte de diodos, capacitores, LM7805, LEDs, conectores de saída
4. **Rode ERC** e corrija todos os erros
5. **Prompt IA**: *"Revise meu esquemático de fonte 5V com LM7805. Estou esquecendo algum capacitor de desacoplamento, proteção, ou boa prática?"*

---

### Módulo L.11: KiCad — Layout de PCB
**Tempo: 5h**

#### 📚 O que dominar
- **Footprints**: representação física do componente (dimensões, furos, pads)
- **Associação**: cada símbolo do esquemático → um footprint (THT ou SMD)
- **Roteamento**: conecte os pads conforme o esquemático. Trilhas = fios de cobre
- **Largura de trilha**: para corrente. Regra rápida: 1A ≈ 0.3mm (camada interna), 0.5mm (camada externa)
- **Plano de terra**: preencha toda área não usada com GND — reduz ruído, melhora térmica
- **Regras de design (DRC)**: clearance mínimo (0.2mm típico), largura mínima, furos mínimos
- **Silkscreen**: texto e contornos dos componentes vistos na placa. Identifique conectores, polaridade, versão

#### Intuição
Layout de PCB é como urbanismo: as trilhas são ruas, os componentes são prédios, o plano de terra é o terreno. Ruas largas para tráfego pesado (corrente alta), ruas estreitas para sinais. Mantenha "vias rápidas" (trilhas de sinal) longe de "fábricas barulhentas" (fontes chaveadas, cristais) para evitar interferência.

#### Projeto: "Layout da Fonte 5V"
1. **Importe** o esquemático para o layout
2. **Posicione** componentes logicamente (entrada à esquerda, saída à direita)
3. **Roteie** todas as trilhas. Use largura adequada para corrente
4. **Adicione** plano de terra, furos de fixação, silkscreen informativo
5. **Rode DRC** e corrija todos os erros
6. **Visualize em 3D** (KiCad tem visualizador 3D integrado)

---

### Módulo L.12: Fabricação e Montagem de PCB
**Tempo: 4h (+ tempo de espera da fabricação)**

#### 📚 O que dominar
- **Gerber files**: formato padrão para fabricação. KiCad exporta automaticamente
- **Fabricantes**: JLCPCB (~$2 para 5 placas, frete ~$5-15, 7-15 dias), PCBWay, OSHPARK
- **BOM (Bill of Materials)**: lista de todos os componentes com valores, footprints, quantidades
- **Montagem**: soldar componentes na placa fabricada. Comece pelos mais baixos (resistores) → mais altos (capacitores, CIs, conectores)

#### Projeto: "Sua Primeira PCB Fabricada!"
1. **Exporte Gerbers** da fonte 5V
2. **Envie** para JLCPCB (ou outro). Escolha: FR4, 1.6mm, HASL, cor verde (mais barato)
3. **Enquanto espera**: prepare BOM, compre componentes faltantes
4. **Ao receber**: inspecione a placa. Teste continuidade com multímetro ANTES de soldar
5. **Solde** todos os componentes, começando pelos menores
6. **Teste**: meça V_out, ripple, regulação com carga. FUNCIONA? 🎉

---

## Fase 5 — Embarcados no Mundo Real

### Módulo L.13: Arduino Físico — Do Wokwi à Bancada
**Tempo: 3h**
**Pré-requisito**: [Pilar 3, Mód 3.9-3.11](ee_digital_embarcados_roadmap.md) (Arduino, ADC, PWM)

#### Projeto: "Portando Projetos do Wokwi"
1. **Blink**: LED externo + resistor. Mesmo código do simulador, mas no hardware real
2. **Botão com debounce**: observe o bounce REAL no osciloscópio!
3. **ADC + sensor**: LM35 ou NTC real. Compare leitura com termômetro
4. **PWM + LED**: dimmer com potenciômetro. Compare suavidade com simulação
5. **Diferenças do real**: tolerância de componentes, ruído no ADC, contato do sensor

#### O que muda do simulador para o real
- ADC oscila (ruído) → precisa de filtro (capacitor + média em software)
- Botões bounçam de verdade → debounce é OBRIGATÓRIO
- Fios longos captam ruído → mantenha fios de sinal curtos
- Corrente real é limitada → LED sem resistor QUEIMA em 0.5s
- Componentes têm tolerância → R=1kΩ±5% pode ser 950Ω a 1050Ω

---

### Módulo L.14: Sensores e Atuadores Reais
**Tempo: 3.5h**
**Pré-requisito**: [Pilar 3, Mód 3.10-3.12](ee_digital_embarcados_roadmap.md) (sensores, serial, I2C)

#### Projeto: "Estação Meteorológica Real"
1. **Sensor de temperatura** (DHT22 ou DS18B20): leia e exiba no Serial
2. **LCD I2C real**: conecte fisicamente (4 fios), resolva problemas de endereço
3. **Relé**: controle uma carga AC (lâmpada 127V) via relé. CUIDADO: isolamento!
4. **Motor DC + L298N**: controle velocidade e sentido com PWM real
5. **Servo**: controle ângulo com potenciômetro

---

### Módulo L.15: ESP32 e Rede Real
**Tempo: 3.5h**
**Pré-requisito**: [Pilar 3, Mód 3.13](ee_digital_embarcados_roadmap.md) (WiFi/IoT). Ver também [HH 5.6](hardware_hacking_roadmap.md) para entender a segurança dessas interfaces!

#### Projeto: "IoT Real com MQTT"
1. **ESP32 físico**: conecte ao WiFi de casa
2. **Web server**: sirva página HTML com dados de sensor real
3. **MQTT**: publique temperatura para broker público (HiveMQ)
4. **Dashboard**: Node-RED ou Grafana mostrando dados em tempo real
5. **Sleep mode**: meça consumo com multímetro. Deep sleep = ~10μA vs ativo = ~80mA

---

## Fase 6 — Instrumentação e Debugging Avançado

### Módulo L.16: Debugging de Hardware — A Arte de Encontrar Bugs Físicos
**Tempo: 3h**

#### 📚 Guia de Debugging

| Sintoma | Causa provável | Diagnóstico |
|---------|---------------|-------------|
| Nada funciona | Sem alimentação | Meça VCC em cada CI. Verifique GND |
| Funciona intermitente | Mau contato | Mexa nos fios. Solda fria? Protoboard velha? |
| CI esquenta | Curto-circuito ou excesso de corrente | Meça corrente total. Verifique ligação |
| Sinal distorcido | Saturação, clipping, ou ruído | Observe no osciloscópio. Verifique alimentação |
| ADC lê lixo | Pino flutuante ou ruído | Adicione pull-up/down e capacitor de filtro |
| I2C não comunica | Endereço errado ou falta pull-up | Scan I2C. Adicione 4.7kΩ pull-up em SDA e SCL |
| Motor não gira | Sem driver, corrente insuficiente | NUNCA direto no pino! Use MOSFET ou L298N |
| Circuito oscila | Feedback positivo indesejado | Adicione caps de desacoplamento (100nF em cada VCC de CI) |

#### Projeto: "O Circuito Sabotado"
1. **Peça a alguém** (ou IA) para descrever um circuito com 3 bugs escondidos
2. **Monte** o circuito bugado na protoboard
3. **Debug** sistematicamente: meça VCC, verifique continuidade, observe sinais
4. **Documente**: qual era o bug, como encontrou, como corrigiu

---

### Módulo L.17: EMI, Aterramento e Boas Práticas
**Tempo: 3h**

#### 📚 O que dominar
- **Capacitor de desacoplamento**: 100nF cerâmico o MAIS PERTO possível de cada VCC de CI
- **Plano de terra**: reduza a área dos loops de corrente (menores loops = menos irradiação)
- **Star grounding**: terra analógico e digital separados, conectados em UM ponto
- **Cabos blindados**: para sinais analógicos sensíveis
- **Snubber**: RC em paralelo com contatos de relé (absorve arco)
- **TVS/varistor**: proteção contra surtos na entrada de alimentação
- **Regra dos 3 C's**: Curto, Capacitor, Continuidade — os 3 primeiros checks de qualquer debug

#### Projeto: "Antes e Depois do Desacoplamento"
1. **Monte** circuito com Arduino + vários módulos I2C SEM caps de desacoplamento → observe erros
2. **Adicione** 100nF em cada VCC → observe a melhoria
3. **Meça ruído** na alimentação com osciloscópio (AC coupling): antes e depois

---

## Fase 7 — Projeto Completo: Do Conceito à Realidade

### Módulo L.18: O Processo de Engenharia
**Tempo: 4h**

#### 📚 O processo real
1. **Requisitos**: o que o produto deve fazer? Quais limitações?
2. **Pesquisa de componentes**: Mouser/Digikey/LCSC. Leia datasheets!
3. **Esquemático**: desenhe no KiCad. Revisão de pares
4. **Prototipagem**: protoboard primeiro! Valide a ideia antes de fabricar PCB
5. **Layout**: design da PCB. DRC + revisão
6. **Fabricação**: envie Gerbers. Enquanto espera, prepare o firmware
7. **Montagem**: solde, inspecione, teste funcional
8. **Debugging**: corrija bugs inevitáveis
9. **Documentação**: esquemático, BOM, firmware, manual de operação
10. **Iteração**: v2.0 corrigindo problemas da v1.0

#### Projeto: "Termômetro Digital com Alarme"
1. **Requisitos**: medir temperatura, exibir em display, alarme se T>30°C
2. **Componentes**: ESP32 + DS18B20 + OLED I2C + buzzer + LED
3. **Protoboard**: monte e teste o firmware completo
4. **KiCad**: esquemático + layout PCB
5. **Fabrique e monte**: PCB real
6. **Teste**: validação funcional completa

---

### Módulo L.19: Projeto Final — Sistema Completo Profissional
**Tempo: 6h**

#### 🔨 Escolha UM projeto (todos integram múltiplos pilares):

**Opção A — Carregador Li-Ion com Display:**
- Controle de carga CC/CV com LM317 ou TP4056
- LCD I2C mostrando V, I, estado de carga
- Proteção: sobrecorrente, sobretensão, reversão de polaridade
- PCB fabricada + gabinete

**Opção B — Amplificador de Áudio Classe D:**
- PWM do Arduino modulando MOSFET
- Filtro LC na saída (reconstrução do sinal)
- Entrada P2 (3.5mm) + volume (potenciômetro)
- Potência: 5-10W em 8Ω

**Opção C — Controle PID Real de Temperatura:**
- NTC + Arduino + MOSFET + resistor de aquecimento
- PID em software com auto-tune
- Interface: OLED + encoder rotativo
- Log de dados via MQTT/WiFi

#### Checkpoint Final — Laboratório Real
- [ ] Mede tensão, corrente e resistência com confiança
- [ ] Usa osciloscópio para analisar sinais no tempo
- [ ] Solda componentes THT com qualidade consistente
- [ ] Projeta PCB no KiCad: esquemático → layout → Gerber
- [ ] Fabrica e monta PCB real
- [ ] Debuga hardware sistematicamente
- [ ] Leva um projeto do conceito à realidade física

---

## Se Você Travar

| Problema | Solução |
|----------|--------|
| Circuito não funciona | Verifique ALIMENTAÇÃO primeiro (VCC em cada CI). Depois GND. Depois sinais |
| LED não acende | Invertido? Sem resistor (queimou)? Meça V_f com multímetro no modo diodo |
| Arduino não é reconhecido | Driver CH340/CP2102 instalado? Cabo USB tem dados (não só carga)? |
| Solda não adere | Ponta do ferro suja (limpe com esponja). Pad oxidado (aplique fluxo). Temperatura baixa |
| PCB tem erro | SEMPRE teste continuidade/curto ANTES de soldar. Compare com esquemático |
| Osciloscópio mostra lixo | Sonda descalibrada? Trigger configurado? GND conectado? Acoplamento AC/DC correto? |
| I2C não funciona | Pull-ups de 4.7kΩ em SDA e SCL? Endereço correto? Scan com `Wire.begin()` |
| Circuito oscila sozinho | Falta capacitor de desacoplamento (100nF) perto do VCC do CI |

> **Lembre-se**: O primeiro circuito no mundo real NUNCA funciona de primeira. Isso é normal. Debugging é uma habilidade, não um sinal de fracasso. Cada bug encontrado e resolvido te torna um engenheiro melhor.

> [Voltar ao Índice](ee_roadmap_index.md)
