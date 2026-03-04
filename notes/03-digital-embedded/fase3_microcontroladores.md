# Módulo 3 — Digital: Fase 3 — Microcontroladores (Arduino)
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 3.9: Arduino — GPIO

### Hardware

- Arduino Uno — MCU <> {{ATmega328P}}
- Clock do Uno >> {{16 MHz}}
- I/O digitais >> {{14}} pinos (6 com PWM)
- Entradas analógicas >> {{6}} (A0-A5)
- Flash >> {{32 KB}} | SRAM >> {{2 KB}}
- Corrente máxima por pino >> {{40 mA}} (recomendável 20mA)

### Funções GPIO

- `pinMode(pin, MODE)` >> Configura pino como {{INPUT, OUTPUT ou INPUT_PULLUP}}
- `digitalWrite(pin, val)` >> Escreve {{HIGH (5V) ou LOW (0V)}}
- `digitalRead(pin)` >> Lê estado do pino (retorna {{HIGH ou LOW}})
- INPUT_PULLUP >> Ativa resistor interno de {{~20-50kΩ}} entre pino e VCC

### Problemas clássicos

- Pino flutuante >> INPUT sem pull-up/down lê {{lixo}} (ruído aleatório)
- Carga > 40mA no pino → >> Usar {{transistor/MOSFET}} externo
- Bounce de botão >> 1 pressionamento gera {{10-50 pulsos}} em ~5ms. Solução: debounce

### Drill — GPIO #[[Drill]]

- LED 20mA no pino 13 do Arduino → precisa de resistor? >> {{Sim}} (R = (5−2)/0.02 = 150Ω)
- Motor DC 500mA → conectar direto no pino? >> {{Não!}} Usar MOSFET (pino máx 40mA)
- Botão sem pull-up → digitalRead lê >> {{Valor aleatório}} (flutuante)
- INPUT_PULLUP + botão conectando pino ao GND → pressionado lê >> {{LOW}} (GND vence pull-up)

---

## Módulo 3.10: ADC e Sensores

### Conversão analógica

- ADC do Arduino >> {{10 bits}} de resolução (0-1023)
- `analogRead(pin)` >> Retorna {{0-1023}}
- Conversão para tensão >> $V = valor × $ {{$5.0 / 1023$}}
- LM35 >> Sensor de temperatura: {{10mV/°C}} (250mV = 25°C)

### Sensores comuns

- LDR <> {{Light Dependent Resistor}} (varia R com luz) → usar divisor resistivo
- NTC <> {{Negative Temperature Coefficient}} (R diminui com temperatura)
- Potenciômetro >> Divisor de tensão {{ajustável}} (posição angular → tensão)

### Drill — ADC #[[Drill]]

- analogRead = 512 → tensão = >> {{~2.5V}} (512/1023×5)
- LM35 lê 300mV → temperatura = >> {{30°C}} (300/10)
- analogRead = 1023 → tensão = >> {{5.0V}} (fundo de escala)
- ADC 10-bit, Vref=5V → resolução = >> {{~4.9mV}} (5/1023)

---

## Módulo 3.11: PWM e Atuadores

### PWM no Arduino

- `analogWrite(pin, val)` >> PWM de 0-255 ({{0-100%}} duty cycle)
- Frequência PWM (pinos 3,9,10,11) >> {{~490 Hz}}
- Frequência PWM (pinos 5,6) >> {{~980 Hz}}
- LED com PWM >> Brilho {{variável}} (olho integra os pulsos)

### Controle de motores

- Motor DC + PWM >> Usar {{MOSFET}} no gate (nunca direto no pino!)
- Servo motor >> Pulso de {{1-2ms}} a cada 20ms. 1ms=0°, 1.5ms=90°, 2ms=180°
- H-Bridge (L298N/L293D) >> Controla {{direção + velocidade}} (enable = PWM)

### Drill — PWM #[[Drill]]

- analogWrite(pin, 127) → duty cycle = >> {{~50%}} (127/255)
- analogWrite(pin, 255) → duty cycle = >> {{100%}}
- Servo: pulso 1.5ms → ângulo = >> {{90°}}
- Motor 12V, PWM duty=75% → V_média = >> {{9V}}

---

## ⏱ Módulo 3.12: Timers, Interrupções e Comunicação

### Timers

- `millis()` >> Retorna {{ms desde o boot}} — evita delay() bloqueante
- `delay()` é ruim porque >> {{Bloqueia}} toda execução (nada mais roda)
- ATmega328P tem >> Timer0 (8-bit, usado por millis), Timer1 ({{16-bit}}), Timer2 (8-bit)

### Interrupções

- Interrupção >> Evento externo {{pausa}} o programa → executa ISR → retorna
- `attachInterrupt(pin, ISR, TRIGGER)` >> Pinos {{2 e 3}} no Uno
- Triggers >> RISING, FALLING, {{CHANGE}}
- ISR deve ser >> {{Curta}} (setar flag, não processar). Sem Serial.print()!
- Variáveis compartilhadas (loop + ISR) devem ser >> {{`volatile`}}

### Protocolos de comunicação

- UART <> {{Serial assíncrona}} (TX/RX, ponto-a-ponto)
- I2C <> {{2 fios}} (SDA + SCL). Barramento, até 127 dispositivos. `Wire.h`
- SPI <> {{4 fios}} (MOSI, MISO, SCK, SS). Mais rápido que I2C. `SPI.h`

### Comparação UART / I2C / SPI

- Mais rápido >> {{SPI}} (~10 MHz+)
- Menos fios >> {{I2C}} (apenas 2)
- Mais simples >> {{UART}} (sem clock, sem endereço)
- Múltiplos dispositivos >> {{I2C}} (barramento endereçado) ou SPI (1 SS por device)

### Drill — Timers e Protocolos #[[Drill]]

- Piscar LED sem delay() → usar >> {{millis()}} com variável de tempo
- Interrupção no pino 2, borda de subida → >> `attachInterrupt(0, ISR, {{RISING}})`
- LCD I2C usa quantos fios de dados? >> {{2}} (SDA + SCL)
- Módulo SD card usa qual protocolo? >> {{SPI}}
- Sensor BMP280 típico → protocolo >> {{I2C}} (endereço 0x76/0x77)

---

##  Módulo 3.13: IoT e Comunicação Wireless

### ESP32

- ESP32 >> MCU com {{WiFi + Bluetooth}} integrado (dual-core, muito mais capaz que Uno)
- Web server embarcado >> ESP32 serve {{página HTML}} para controle via browser
- MQTT <> {{Protocolo publish/subscribe}} leve para IoT
- BLE <> {{Bluetooth Low Energy}} (ultra-baixo consumo, sensores, wearables)

### Conceitos de rede (HH essencial)

- SSID <> {{Nome da rede WiFi}}
- IP >> Endereço {{numérico}} do dispositivo na rede
- Porta >> Número que identifica o {{serviço}} (80=HTTP, 443=HTTPS)

### Drill — IoT #[[Drill]]

- ESP32 vs Arduino Uno: qual tem WiFi? >> {{ESP32}}
- MQTT: quem distribui mensagens? >> O {{broker}} (Mosquitto, HiveMQ)
- BLE vs WiFi: qual consome menos? >> {{BLE}}
