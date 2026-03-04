# Módulo 3 — Digital: Fase 4 — Projeto Integrado
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 3.14: ADC, DAC e Amostragem

### Teorema de Nyquist

- Teorema de Nyquist >> Taxa de amostragem deve ser ≥ {{$2 × f_{max}$}} do sinal
- Aliasing >> Amostragem abaixo de Nyquist → frequências {{fantasma}} aparecem
- Filtro anti-aliasing >> Passa-baixas {{antes}} do ADC (elimina f > Nyquist)

### DAC e quantização

- DAC <> {{Digital-to-Analog Converter}} (número digital → tensão analógica)
- R-2R ladder >> Implementação mais simples de {{DAC}} (só resistores!)
- Erro de quantização (ADC 10-bit, Vref=5V) >> {{~4.9 mV}} (5V/1024)

### Drill — Amostragem #[[Drill]]

- Sinal de áudio 20kHz → taxa mínima de amostragem = >> {{40 kHz}} (Nyquist)
- CD usa 44.1kHz → frequência máxima reproduzível = >> {{22.05 kHz}} (44.1/2)
- ADC 12-bit, Vref=3.3V → resolução = >> {{~0.8 mV}} (3.3/4096)
- ADC 8-bit → quantos níveis? >> {{256}} ($2^8$)
- Amostrar 1kHz a 1.5kHz → resultado = >> {{Aliasing}} (< 2×1kHz)

---

## Módulo 3.15: Sistema Embarcado Completo

### Integração dos 3 pilares

- Sensor analógico → ADC → MCU: Pilar >> {{1}} (circuitos) + {{2}} (eletrônica)
- Lógica de controle → firmware: Pilar >> {{3}} (digital)
- MCU → MOSFET → atuador: Pilar >> {{2}} (eletrônica de potência)
- Proteções (flyback, fusível, ESD) → Pilar >> {{2}} (diodos, Zener)

### Boas práticas de projeto

- Debounce >> Hardware (RC) ou software ({{millis()}} com delay mínimo)
- EEPROM >> Memória {{não-volátil}} do ATmega (1KB) — salva configurações que sobrevivem ao reset
- Watchdog timer >> Reseta o MCU se o firmware {{travar}} (loop infinito não-intencional)
- Brownout detector >> Reseta o MCU se a tensão de alimentação cair abaixo de {{limite seguro}}

### Drill — Projeto #[[Drill]]

- Ventilador 12V controlado por Arduino → interface = >> {{MOSFET}} (N-channel + flyback diode)
- Salvar setpoint que sobrevive ao reboot → usar >> {{EEPROM}}
- Firmware travou → quem reseta automaticamente? >> {{Watchdog timer}}
- Sensor LM35 → Arduino → LCD I2C. Protocolos usados = >> {{Analógico (ADC)}} + {{I2C}}
