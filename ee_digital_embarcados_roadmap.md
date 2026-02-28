# Pilar 3: Sistemas Digitais e Embarcados

> **Sobre este pilar**: O mundo digital é binário — 0 ou 1, ligado ou desligado. Mas dessa simplicidade emerge toda a complexidade dos computadores, smartphones e IoT. Aqui você vai de portas lógicas básicas a projetos completos com Arduino.
>
> **Ferramentas**: [Falstad](https://www.falstad.com/circuit/) (portas lógicas visuais) + [Wokwi](https://wokwi.com/) (Arduino online, gratuito) + [Proteus](https://www.labcenter.com/) (simulação mista)
>
> **Pré-requisitos**: [Pilar 1](ee_circuitos_roadmap.md) (tensão, corrente, conceitos básicos). [Pilar 2](ee_eletronica_roadmap.md) Módulos 2.5-2.7 (BJT/MOSFET como chave).
>
> **Conexões com outros pilares**:
> - **Base de**: [Pilar 0](ee_matematica_fisica_roadmap.md) — sistemas numéricos (Mód 0.1), álgebra booleana (digital é álgebra pura!)
> - **Base de**: [Pilar 2](ee_eletronica_roadmap.md) — MOSFET como chave digital (Mód 2.7), níveis lógicos (3.3V vs 5V)
> - **Alimenta**: [Pilar 5](ee_controle_sinais_roadmap.md) Módulo 5.12 (controle digital), [Lab L.13-L.15](ee_laboratorio_real_roadmap.md) (Arduino/ESP32 físico)
> - **Segurança**: [HH 0.4](hardware_hacking_roadmap.md) (sistemas numéricos), [HH 1.1-1.3](hardware_hacking_roadmap.md) (eletrônica digital, arquitetura embarcada, protocolos serial), [HH 3.1-3.3](hardware_hacking_roadmap.md) (exploração UART/SPI/I2C), [HH Avançado A.1](hardware_hacking_roadmap_avancado.md) (FPGA/Verilog)
>
> [Voltar ao Índice](ee_roadmap_index.md)

---

## Visão Geral

| Fase | Módulos | Foco | Horas |
|------|---------|------|-------|
| **Lógica Combinacional** | 3.1–3.4 | Portas, álgebra booleana, Karnaugh, circuitos | ~9h |
| **Lógica Sequencial** | 3.5–3.8 | Flip-flops, contadores, registradores, FSM | ~9h |
| **Microcontroladores** | 3.9–3.13 | Arduino: GPIO, ADC, PWM, timers, comunicação | ~12h |
| **Projeto Integrado** | 3.14–3.15 | Sistemas completos, integração analógico-digital | ~5h |

---

## Fase 1 — Lógica Combinacional

### Módulo 3.1: Sistemas Numéricos e Portas Lógicas
**Tempo: 2h**

#### O que memorizar
- **Binário**: base 2. Cada dígito = 1 bit. 8 bits = 1 byte (0-255). 4 bits = 1 nibble (0-15)
- **Hexadecimal**: base 16 (0-9, A-F). Cada hex digit = 4 bits. 0xFF = 1111 1111 = 255
- **Conversões essenciais**: `1010` = 0xA = 10, `1111` = 0xF = 15, `1111 1111` = 0xFF = 255
- **Portas lógicas fundamentais**:
  - **AND** (E): saída 1 só se TODAS entradas = 1. Símbolo: `·` ou `∧`
  - **OR** (OU): saída 1 se QUALQUER entrada = 1. Símbolo: `+` ou `∨`
  - **NOT** (NÃO): inverte. Símbolo: `¬` ou barra sobre a variável
  - **NAND**: AND invertido. **Porta universal** (qualquer circuito pode ser feito só com NANDs!)
  - **NOR**: OR invertido. Também universal
  - **XOR**: "ou exclusivo" — saída 1 se entradas são DIFERENTES
- **Tabela-verdade**: tabela com todas as combinações de entrada e suas saídas
- **Conversões rápidas**:

| Binário | Hex | Decimal | | Binário | Hex | Decimal |
|---------|-----|---------|---|---------|-----|---------|
| 0000 | 0 | 0 | | 1000 | 8 | 8 |
| 0001 | 1 | 1 | | 1001 | 9 | 9 |
| 0010 | 2 | 2 | | 1010 | A | 10 |
| 0011 | 3 | 3 | | 1011 | B | 11 |
| 0100 | 4 | 4 | | 1100 | C | 12 |
| 0101 | 5 | 5 | | 1101 | D | 13 |
| 0110 | 6 | 6 | | 1110 | E | 14 |
| 0111 | 7 | 7 | | 1111 | F | 15 |

#### Intuição
Portas lógicas são os "átomos" dos computadores. Todo processador, memória e circuito digital é construído com bilhões de portas lógicas. Um AND = "preciso que TODAS as condições sejam verdadeiras" (ex: alarme soa SE porta aberta E sistema armado E não é horário comercial). Um OR = "basta UMA condição" (ex: alarme soa SE porta aberta OU janela aberta OU sensor de movimento).

**O transistor como chave (do Pilar 2) É uma porta lógica!** Um MOSFET N-channel com pull-up é um NOT gate. Dois MOSFETs em série = AND (ambos precisam estar ON). Dois em paralelo = OR (qualquer um ON liga a saída).

#### Projeto: "Logic Gate Explorer"
1. **No Falstad**: monte cada porta — AND, OR, NOT, NAND, NOR, XOR
   - Varie as entradas → preencha a tabela-verdade de cada uma
2. **Comprove**: monte um AND usando 2 MOSFETs em série. Monte um OR com 2 MOSFETs em paralelo
3. **Conversões**: pratique bin↔hex↔dec com 20 exemplos diferentes
4. **Prompt IA**: *"Explique por que portas NAND são consideradas 'universais' — como construir AND, OR e NOT usando APENAS portas NAND? Desenhe os circuitos."*
5. **Entregável**: Tabelas-verdade de todas as portas + circuitos com MOSFETs

#### Erros Comuns
- Confundir AND com OR (AND = todas verdadeiras, OR = pelo menos uma)
- Esquecer que em eletrônica digital, 0V = LOW = 0 lógico, e 5V (ou 3.3V) = HIGH = 1 lógico. A tensão exata depende da família lógica
- Não entender que binário é posicional: `1010` não é "mil e dez", é 1×8 + 0×4 + 1×2 + 0×1 = 10

---

### Módulo 3.2: Álgebra Booleana e Simplificação
**Tempo: 2h**

#### O que memorizar
- **Identidades essenciais**: A·1=A, A+0=A, A·0=0, A+1=1, A·A'=0, A+A'=1
- **Comutativa**: A·B=B·A, A+B=B+A
- **Distributiva**: A·(B+C)=A·B+A·C, A+(B·C)=(A+B)·(A+C) ← esta segunda é não-intuitiva!
- **De Morgan**: `(A·B)' = A'+B'` e `(A+B)' = A'·B'` — essencial para converter entre AND/OR/NOT
- **Absorção**: A+A·B=A, A·(A+B)=A
- **Soma de Produtos (SOP)**: forma padrão AND-OR. Cada linha da tabela-verdade com saída=1 → um minterm
- **Produto de Somas (POS)**: forma padrão OR-AND. Cada linha com saída=0 → um maxterm

#### Intuição
Álgebra booleana é a "matemática" dos circuitos digitais. Simplificar uma expressão booleana = usar menos portas lógicas = circuito menor, mais rápido e mais barato. De Morgan é a ferramenta mais poderosa: transforma AND em OR (e vice-versa) com inversões. É como ter um "tradutor" entre dois idiomas lógicos.

#### Projeto: "Simplificação de Expressões"
1. **Simplifique** usando álgebra booleana:
   - F = A·B + A·B'·C + A'·B·C → ?
   - G = (A+B)·(A+C) → ?
2. **Aplique De Morgan**: converta (A·B·C)' para forma OR
3. **Monte no Falstad**: circuito original e simplificado → verifique que produzem a mesma tabela-verdade
4. **Entregável**: Simplificações passo a passo + verificação por simulação

---

### Módulo 3.3: Mapa de Karnaugh — Simplificação Visual
**Tempo: 2.5h**

#### O que memorizar
- **Mapa de Karnaugh (K-map)**: método visual para simplificar expressões booleanas de 2, 3 ou 4 variáveis
- **Regras de agrupamento**: agrupar 1s adjacentes em retângulos de 2ⁿ (1, 2, 4, 8). Quanto maior o grupo, mais simples a expressão
- **Adjacência**: inclui wrap-around (bordas se conectam)
- **Don't-cares (X)**: valores que podem ser 0 ou 1 — use-os para fazer grupos maiores
- **3 variáveis**: mapa 2×4. **4 variáveis**: mapa 4×4
- **Limitação**: impraticável para 5+ variáveis → use software (Quine-McCluskey)

#### Projeto: "K-Map Mastery"
1. **Resolva 5 K-maps** de nível crescente:
   - 2 variáveis (trivial)
   - 3 variáveis sem don't-cares
   - 3 variáveis com don't-cares
   - 4 variáveis sem don't-cares
   - 4 variáveis com don't-cares
2. **Para cada**: desenhe o K-map, identifique agrupamentos, escreva expressão simplificada
3. **Verifique**: monte o circuito no Falstad e compare com a expressão original
4. **Entregável**: 5 K-maps resolvidos + circuitos + verificação

---

### Módulo 3.4: Circuitos Combinacionais Práticos
**Tempo: 2h**

#### O que memorizar
- **Multiplexador (MUX)**: seleciona 1 de N entradas. MUX 4:1 = 4 entradas, 2 selectores, 1 saída
- **Demultiplexador (DEMUX)**: 1 entrada → N saídas (oposto do MUX)
- **Decoder**: converte código binário em linha ativa. Decoder 3:8 ativa 1 de 8 linhas
- **Encoder**: oposto do decoder
- **Somador completo (Full Adder)**: soma 2 bits + carry_in → sum + carry_out
- **Display 7 segmentos**: decoder BCD→7 segmentos converte 4 bits em display numérico

#### Projeto: "Calculadora de 4 Bits"
1. **Monte um Full Adder** no Falstad com portas lógicas básicas
2. **Cascateie 4 Full Adders** → somador de 4 bits (Ripple Carry Adder)
3. **Adicione display 7 segmentos** na saída (decoder BCD→7seg)
4. **Monte um MUX 4:1** e demonstre como selecionar entre 4 sinais
5. **Entregável**: Somador de 4 bits com display + MUX funcional

#### Checkpoint — Fim da Fase Combinacional
- [ ] Converte entre binário, hexadecimal e decimal fluente
- [ ] Simplifica expressões booleanas com álgebra e K-maps
- [ ] Projeta circuitos combinacionais (MUX, decoder, somador)
- [ ] Entende que portas lógicas vêm de transistores

---

## Fase 2 — Lógica Sequencial

### Módulo 3.5: Flip-Flops — A Memória Digital
**Tempo: 2h**

#### O que memorizar
- **Flip-flop D**: armazena 1 bit. Na borda de subida do clock, captura D e mantém na saída Q
- **Flip-flop JK**: versátil — J=K=1 → toggle (inverte). J=1,K=0 → set. J=0,K=1 → reset
- **Flip-flop SR**: set-reset mais simples. S=R=1 é **proibido** (estado indefinido!)
- **Latch vs Flip-flop**: latch é sensível a NÍVEL (enquanto enable=1, saída acompanha input). Flip-flop é sensível a BORDA (captura apenas no instante da borda do clock)
- **Timing**: setup time (dados estáveis ANTES da borda), hold time (dados estáveis DEPOIS da borda)
- **Registrador**: array de flip-flops D → armazena uma palavra (4, 8, 16 bits)

#### Intuição
Circuitos combinacionais são "sem memória" — a saída depende apenas das entradas ATUAIS. Flip-flops adicionam **MEMÓRIA** — a saída depende do que aconteceu ANTES (estado anterior). Essa distinção é fundamental: sem memória, não haveria computadores (precisam lembrar do estado anterior), contadores (precisam saber em que número estão), ou máquinas de estado (precisam saber em que "passo" estão).

#### Projeto: "Memória de 4 Bits"
1. **No Falstad**: monte um flip-flop D com portas NAND (entenda a construção interna!)
2. **Monte 4 flip-flops D em paralelo** → registrador de 4 bits
3. **Teste**: aplique diferentes dados na entrada, pulse o clock → observe armazenamento
4. **Entregável**: FF D com portas + registrador de 4 bits funcional

---

### Módulo 3.6: Contadores e Divisores de Frequência
**Tempo: 2h**

#### O que memorizar
- **Contador assíncrono (ripple)**: FFs em cascata, cada saída Q é clock do próximo. Simples mas tem propagation delay
- **Contador síncrono**: todos os FFs recebem o MESMO clock. Mais rápido, preferível
- **Contador binário de N bits**: conta de 0 a 2ᴺ-1. 4 bits → 0 a 15
- **Contador módulo-N**: conta de 0 a N-1 e volta a 0 (ex: módulo-10 para BCD)
- **Divisor de frequência**: cada FF divide a frequência por 2. 3 FFs → ÷8. N FFs → ÷2ᴺ
- **Contador up/down**: conta para cima ou para baixo conforme sinal de controle

#### Projeto: "Relógio Digital (Base)"
1. **Monte contador binário de 4 bits** (síncrono) no Falstad
2. **Converta para módulo-10** (BCD) — reset quando chega a 1010 (10)
3. **Cascateie 2 contadores BCD** → conta de 00 a 59 (segundos de um relógio!)
4. **Adicione display 7-segmentos** em cada dígito
5. **Divisor de clock**: se o clock é 1MHz, divida por 1.000.000 para obter 1Hz → pulso por segundo
6. **Entregável**: Contador 0-59 com displays + divisor de clock

---

### Módulo 3.7: Registradores de Deslocamento
**Tempo: 1.5h**

#### O que memorizar
- **Shift register**: FFs em cadeia — dados se deslocam 1 posição por clock
- **SISO** (Serial In, Serial Out): dados entram e saem em série, 1 bit por clock
- **SIPO** (Serial In, Parallel Out): entrada serial → saída paralela (expande portas do microcontrolador!)
- **PISO** (Parallel In, Serial Out): entrada paralela → saída serial (lê múltiplos botões com poucos pinos)
- **74HC595**: shift register popular para expandir outputs do Arduino (3 pinos → 8+ outputs)
- **Aplicações**: comunicação serial, conversão paralelo↔serial, delay lines, pseudo-random generators (LFSR)

#### Projeto: "Expandindo o Arduino (Prévia)"
1. **Monte um SIPO de 8 bits** no Falstad com flip-flops D
2. **Alimente dados serialmente**: 10110010 → observe saída paralela após 8 clocks
3. **Dica**: é assim que o 74HC595 funciona — e é assim que o Arduino controla 16+ LEDs com apenas 3 pinos!
4. **Entregável**: Shift register funcional + demonstração de conversão serial→paralelo

---

### Módulo 3.8: Máquinas de Estado Finito (FSM)
**Tempo: 2.5h**

#### O que memorizar
- **FSM**: sistema com número finito de estados, transições entre eles baseadas em entradas, e saídas definidas
- **Mealy**: saída depende do estado E da entrada atual. Mais responsivo
- **Moore**: saída depende APENAS do estado. Mais previsível
- **Diagrama de estados**: representação gráfica com círculos (estados) e setas (transições)
- **Implementação**: lógica combinacional (transições) + flip-flops (armazenamento de estado)
- **Processo de design**: especificação → diagrama de estados → tabela de transições → K-map → circuito

#### Intuição
FSMs estão em TODO lugar: semáforo (verde→amarelo→vermelho), máquina de venda (idle→moeda→seleção→dispensa), protocolo UART (idle→start→data→stop). Todo comportamento sequencial pode ser modelado como FSM. Microcontroladores executam programas que são FSMs implícitas (o "estado" é o program counter + variáveis).

#### Projeto: "Semáforo de 2 Fases"
1. **Especifique**: 2 semáforos (rua A e rua B), 4 estados:
   - S0: A=Verde, B=Vermelho (30s)
   - S1: A=Amarelo, B=Vermelho (5s)
   - S2: A=Vermelho, B=Verde (30s)
   - S3: A=Vermelho, B=Amarelo (5s)
2. **Desenhe o diagrama de estados** (Moore: saída = cor dos LEDs)
3. **Monte a tabela de transições** e simplifique com K-maps
4. **Implemente no Falstad** com FFs e lógica combinacional
5. **(Bônus)** Adicione botão de pedestre que encurta o verde
6. **Entregável**: Diagrama + tabela + circuito implementado + teste

#### Checkpoint — Fim da Fase Sequencial
- [ ] Entende a diferença entre latch e flip-flop
- [ ] Projeta contadores binários e BCD
- [ ] Sabe usar shift registers para expansão de I/O
- [ ] Projeta FSMs simples do diagrama ao circuito

---

## Fase 3 — Microcontroladores (Arduino)

### Módulo 3.9: Arduino — Primeiro Contato com GPIO
**Tempo: 2h**

#### O que memorizar
- **Arduino Uno**: ATmega328P, 14 I/O digitais (6 PWM), 6 ADC, clock 16MHz, 32KB flash, 2KB SRAM
- **GPIO (General Purpose Input/Output)**: pinos que você configura como entrada ou saída
- **`pinMode(pin, MODE)`**: configura pino como INPUT, OUTPUT ou INPUT_PULLUP
- **`digitalWrite(pin, value)`**: escreve HIGH (5V) ou LOW (0V) no pino
- **`digitalRead(pin)`**: lê o estado do pino (HIGH ou LOW)
- **Pull-up interno**: resistor de ~20-50kΩ entre pino e VCC. Evita "flutuação" quando botão não está pressionado
- **Debouncing**: botão mecânico "bounça" — produz múltiplos pulsos. Solução: delay, RC filter, ou FSM em software

#### Intuição
O Arduino é onde TUDO que você aprendeu se encontra: a eletrônica analógica (Pilar 2) alimenta os sensores, a lógica digital (Pilar 3 fases anteriores) está dentro do microcontrolador, e os circuitos (Pilar 1) conectam tudo. Programar o Arduino é "desenhar máquinas de estado em C++". Cada `if/else` é uma porta lógica. Cada `while` é um contador. Cada variável é um registrador.

#### Projeto: "LED com Botão (O Hello World do Embarcado)"
1. **No Wokwi** (wokwi.com — simulador Arduino online e gratuito!):
   - Monte: Arduino Uno + LED + resistor 220Ω + botão
   ```cpp
   const int LED = 13;
   const int BTN = 2;
   
   void setup() {
       pinMode(LED, OUTPUT);
       pinMode(BTN, INPUT_PULLUP); // pull-up interno
   }
   
   void loop() {
       if (digitalRead(BTN) == LOW) {  // botão pressionado
           digitalWrite(LED, HIGH);
       } else {
           digitalWrite(LED, LOW);
       }
   }
   ```
2. **Evolua**: LED toggle (aperta → liga, aperta de novo → desliga) — requer debounce!
3. **Semáforo com Arduino**: 3 LEDs (verde, amarelo, vermelho) + delays
4. **Prompt IA**: *"Explique a diferença entre INPUT e INPUT_PULLUP. Desenhe o circuito equivalente de cada um. O que acontece quando um pino está configurado como INPUT e não está conectado a nada? Por que isso é perigoso?"*
5. **Entregável**: 3 programas rodando no Wokwi + código comentado

#### Erros Comuns
- **Pino flutuante**: um INPUT sem pull-up/down lê valores aleatórios (ruído). SEMPRE use INPUT_PULLUP ou resistor externo
- **Corrente excessiva**: cada pino do Arduino fornece MAX 40mA (20mA recomendado). Para cargas maiores, use transistor/MOSFET!
- **Bounce de botão**: um pressionamento gera 10-50 pulsos em ~5ms. Sem debounce, o código "vê" múltiplos pressionamentos

---

### Módulo 3.10: ADC e Sensores Analógicos
**Tempo: 2h**

#### O que memorizar
- **ADC (Analog-to-Digital Converter)**: converte tensão analógica (0-5V) em número digital (0-1023). Resolução: 10 bits
- **`analogRead(pin)`**: retorna valor 0-1023. Tensão = `valor × 5.0 / 1023`
- **Sensores comuns**: LDR (luz), NTC/PTC (temperatura), potenciômetro (posição), sensor de gás (MQ-x)
- **LM35**: sensor de temperatura calibrado. Saída: 10mV/°C. 250mV = 25°C
- **Divisor resistivo com sensor**: quando sensor é resistivo (LDR, NTC), monte divider com resistor fixo

#### Projeto: "Estação Meteorológica Básica"
1. **No Wokwi**: Arduino + LM35 (temperatura) + LDR (luminosidade) + potenciômetro
2. **Leia os 3 sensores** e exiba no Serial Monitor:
   ```cpp
   void loop() {
       int tempRaw = analogRead(A0);
       float tempC = tempRaw * 5.0 / 1023.0 * 100.0; // LM35: 10mV/°C
       
       int luzRaw = analogRead(A1);
       float luzPercent = luzRaw / 1023.0 * 100.0;
       
       Serial.print("Temp: "); Serial.print(tempC); Serial.println("°C");
       Serial.print("Luz: "); Serial.print(luzPercent); Serial.println("%");
       delay(1000);
   }
   ```
3. **Adicione LCD 16×2** (I2C) para exibir os valores sem depender do PC
4. **Entregável**: Estação meteorológica com 3 sensores + display LCD

---

### Módulo 3.11: PWM e Controle de Atuadores
**Tempo: 2h**

#### O que memorizar
- **`analogWrite(pin, value)`**: gera PWM (0-255 = 0-100% duty) nos pinos PWM (~)
- **Frequência PWM do Arduino**: ~490Hz (pinos 3,9,10,11) ou ~980Hz (pinos 5,6)
- **Controle de brilho**: LED com PWM aparenta brilho variável (olho integra)
- **Controle de motor DC**: PWM + MOSFET (NÃO conecte motor direto ao Arduino — I máx = 40mA por pino!)
- **Servo motor**: pulso de 1-2ms a cada 20ms. 1ms = 0°, 1.5ms = 90°, 2ms = 180°. Biblioteca `Servo.h`
- **H-bridge (L298N/L293D)**: controla direção + velocidade de motor DC. Enable = PWM

#### Projeto: "Controle de Luminosidade e Motor"
1. **LED dimmer**: potenciômetro → `analogRead` → mapeado → `analogWrite` → LED brilho variável
2. **Servo controlado por pot**: `map(analogRead(A0), 0, 1023, 0, 180)` → `servo.write(angle)`
3. **Motor DC com L298N**: 2 botões (frente/ré) + pot para velocidade (PWM)
4. **Entregável**: 3 projetos de controle de atuadores com PWM

---

### Módulo 3.12: Timers, Interrupções e Comunicação Serial
**Tempo: 3h**

#### O que memorizar
- **Timer/Counter**: hardware que conta pulsos de clock automaticamente. ATmega328 tem Timer0 (8-bit, usado por `millis()`), Timer1 (16-bit), Timer2 (8-bit)
- **`millis()`**: retorna ms desde o boot. Evita `delay()` (que bloqueia tudo)
- **Interrupções**: evento externo "pausa" o programa principal → executa ISR → retorna. Muito mais rápido que polling
- **`attachInterrupt(pin, ISR, TRIGGER)`**: RISING, FALLING, CHANGE
- **UART**: comunicação serial assíncrona. `Serial.begin(9600)`. TX/RX
- **I2C**: barramento com 2 fios (SDA, SCL). `Wire.h`. Endereçamento: até 127 dispositivos
- **SPI**: 4 fios (MOSI, MISO, SCK, SS). Mais rápido que I2C. `SPI.h`

#### Intuição
`delay()` é o "pecado capital" do embarcado — enquanto o delay roda, o Arduino não faz NADA mais. É como ficar olhando o relógio parado. `millis()` é como pôr um alarme — você faz outras coisas e verifica periodicamente se chegou a hora.

Interrupções são como um **toque de telefone** — não importa o que você esteja fazendo, quando o telefone toca, você atende imediatamente, resolve rápido, e volta ao que estava fazendo. É a forma mais eficiente de reagir a eventos externos.

#### Projeto: "Multitarefa no Arduino"
1. **Sem delay**: LED pisca a 1Hz + lê botão + lê sensor — tudo "ao mesmo tempo" usando `millis()`
2. **Interrupção**: botão gera interrupção → conta pressionamentos → exibe no Serial
3. **I2C**: conecte LCD 16×2 I2C + sensor BMP280 (temperatura+pressão) no Wokwi
4. **SPI**: conecte módulo SD card (simulado) → grave dados do sensor num "arquivo"
5. **Prompt IA**: *"Explique as diferenças entre UART, I2C e SPI: velocidade, número de fios, topologia (ponto-a-ponto vs barramento), e quando usar cada um. Monte uma tabela comparativa."*
6. **Entregável**: Código multitarefa + demonstração I2C + SPI

#### Erros Comuns
- **ISR muito longa**: interrupções devem ser RÁPIDAS (setar flag, não processar). `Serial.print()` dentro de ISR = BUG!
- **Usar `delay()` com interrupções**: `delay()` depende de interrupção do timer. Dentro de uma ISR, `delay()` não funciona
- **Variáveis compartilhadas**: variáveis usadas tanto no `loop()` quanto na ISR devem ser `volatile`

---

### Módulo 3.13: Comunicação Wireless e IoT
**Tempo: 2.5h**

#### O que memorizar
- **ESP32**: microcontrolador com WiFi + Bluetooth integrado. Muito mais capaz que Arduino Uno
- **Web server embarcado**: ESP32 serve página HTML para controlar dispositivos via browser
- **MQTT**: protocolo leve para IoT (publish/subscribe). Broker: Mosquitto, HiveMQ
- **Bluetooth Low Energy (BLE)**: consumo ultra-baixo. Usado em sensores, wearables
- **SSID, IP, porta, HTTP**: conceitos de rede essenciais para IoT

#### Projeto: "IoT Dashboard"
1. **No Wokwi** (suporta ESP32): monte ESP32 + sensor de temperatura + LED
2. **Web server**: ESP32 serve página com temperatura atual + botão para controlar LED
3. **Controle via browser**: acesse o IP do ESP32 → veja temperatura e controle o LED
4. **Entregável**: ESP32 IoT com web interface funcional

---

## Fase 4 — Projetos Integrados

### Módulo 3.14: Do Analógico ao Digital — ADC, DAC e Sampling
**Tempo: 2.5h**

#### O que memorizar
- **Teorema de Nyquist**: para capturar um sinal de frequência f_max, a taxa de amostragem deve ser ≥ 2 × f_max
- **Aliasing**: se amostrar abaixo de Nyquist, frequências "fantasma" aparecem
- **DAC (Digital-to-Analog)**: converte número digital → tensão analógica. R-2R ladder é a implementação mais simples
- **Quantização**: erro introduzido pela resolução finita. ADC 10-bit: erro máximo = 5V/1024 ≈ 4.9mV
- **Filtro anti-aliasing**: filtro passa-baixas ANTES do ADC para eliminar frequências acima de Nyquist

#### Projeto: "Osciloscópio DIY"
1. **Arduino como osciloscópio**: leia `analogRead()` em loop rápido → envie dados pelo Serial
2. **Python**: receba dados pela serial → plote em tempo real com matplotlib
3. **Meça sinais reais**: aplique senóide de um 555 timer → capture no "osciloscópio"
4. **Demonstre aliasing**: gere sinal de 5kHz, amostre a 8kHz → observe o artefato
5. **Entregável**: Osciloscópio funcionando + demonstração de Nyquist/aliasing

---

### Módulo 3.15: Projeto Final — Sistema Embarcado Completo
**Tempo: 3h**

#### 📚 O que aplicar
Tudo dos 3 pilares! Este projeto integra circuitos analógicos, eletrônica e sistemas digitais.

#### Projeto: "Sistema de Controle de Temperatura"
1. **Sensor**: LM35 (analógico) → divisor resistivo → ADC do Arduino
2. **Atuador**: ventilador 12V controlado por MOSFET + PWM
3. **Display**: LCD I2C mostra temperatura atual e setpoint
4. **Controle**: PID simplificado — se T > setpoint, aumenta PWM do ventilador
5. **Interface**: 2 botões para ajustar setpoint (up/down) com debounce por interrupção
6. **Proteção**: diodo flyback no motor do ventilador, fusível virtual (desliga se T > 80°C)
7. **Log**: salva dados em "EEPROM" (simulated storage)
8. **Entregável**: Sistema completo no Wokwi com código, esquemático e documentação

#### Checkpoint Final — Pilar 3 Completo!
- [ ] Projeta circuitos combinacionais e sequenciais
- [ ] Programa Arduino para ler sensores, controlar atuadores e comunicar
- [ ] Usa interrupções e millis() para multitarefa
- [ ] Entende I2C, SPI e UART na prática
- [ ] Integra analógico + digital num sistema funcional

> **Próximo passo**: [Pilar 4 — Eletrotécnica e Automação Industrial](ee_eletrotecnica_automacao_roadmap.md)

---

## Se Você Travar

| Problema | Solução |
|----------|--------|
| K-map dá expressão diferente | Pode haver múltiplas simplificações válidas! Verifique montando a tabela-verdade de ambas — se são iguais, ambas estão certas |
| Contador não conta certo | Verifique se está usando borda de subida ou descida. No assíncrono, cada FF é clock do próximo; no síncrono, todos recebem o MESMO clock |
| Arduino não compila | 99% das vezes é ponto-e-vírgula faltando, chave `}` não fechada, ou função chamada antes de declarada |
| Sensor lê lixo | Pino flutuante! Use pull-up/pull-down. Verifique fiação e GND compartilhado |
| LCD não mostra nada | Verifique endereço I2C (geralmente 0x27 ou 0x3F). Ajuste contraste com potenciômetro no módulo I2C |
| Motor não gira com Arduino | NUNCA conecte motor direto ao pino! Use transistor/MOSFET/L298N. O pino só fornece 20mA |
| Interrupção não funciona | No Uno, só pinos 2 e 3 aceitam `attachInterrupt()`. Verifique trigger (RISING/FALLING/CHANGE) |
| Valor ADC oscila muito | Adicione capacitor de 100nF entre pino ADC e GND. Ou faça média de 10 leituras em software |
