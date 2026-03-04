# Módulo 7 — Hardware Hacking: Fase 7 — Ataques Avançados de Hardware
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo HH.7.1: Side-Channel Attacks — Teoria

### Tipos de side-channel

- Timing attack >> Tempo de execução {{varia}} dependendo dos dados processados
- SPA (Simple Power Analysis) >> {{Observação direta}} de um trace de consumo (identifica operações)
- DPA (Differential Power Analysis) >> Análise {{estatística}} de milhares de traces (extrai chaves)
- Electromagnetic emanation >> Captura {{radiação EM}} do chip processando dados
- Cache attack >> Explorar diferença de tempo entre cache {{hit}} e miss

### Timing attack

- Vulnerabilidade >> Comparação de senha {{byte a byte}} — retorna mais rápido se 1º byte errado
- Exploração >> Medir {{tempo}} para cada tentativa → o caractere correto demora mais (passa para o próximo)
- Contramedida >> Comparação em {{tempo constante}} (ex: `hmac.compare_digest` em Python)

### Power Analysis

- SPA >> Com {{1 trace}} de consumo, identificar operações: multiplicação gasta mais que adição (ex: RSA)
- DPA/CPA >> Com {{milhares}} de traces, correlação estatística revela a chave bit a bit
- Hamming Weight model >> Consumo proporcional ao número de bits "{{1}}" sendo processados
- Alvo ideal >> {{S-Box}} do AES (saída depende de chave + plaintext → correlação possível)

### Contramedidas

- Constant-time code >> Mesmo {{tempo}} de execução independente dos dados
- Power noise injection >> Adicionar {{ruído aleatório}} no consumo para dificultar análise
- Masking >> Misturar dados com {{valores aleatórios}} durante processamento (desfazer no final)
- Jitter >> Adicionar {{atrasos aleatórios}} no clock para desalinhar traces

### Drill — Side-Channel #[[Drill]]

- Senha comparada byte a byte → ataque = >> {{Timing attack}}
- RSA: trace mostra pico grande seguido de pico pequeno → operações = >> {{Multiplicação}} (pico grande) e quadrado (pico pequeno)
- Precisa de 10.000 traces para extrair chave AES → tipo = >> {{DPA/CPA}} (estatístico)
- `if (a == b)` vs `hmac.compare_digest(a, b)` → qual é seguro? >> {{`hmac.compare_digest`}} (tempo constante)
- Contramedida: adicionar ruído aleatório no VCC → protege contra >> {{Power analysis}} (dificulta correlação)

---

## Módulo HH.7.2: Fault Injection

### Técnicas de glitching

- Voltage glitching >> Abaixar {{VCC}} por nanossegundos → instrução corrompida
- Clock glitching >> Encurtar um ciclo de {{clock}} → CPU não termina a instrução
- EMFI >> Pulso {{eletromagnético}} direcionado ao chip (PicoEMP, ChipShouter)
- Laser fault injection >> {{Laser}} focado em ponto específico do die → inverte bits na SRAM
- Temperature >> Chip fora da faixa de temperatura → comportamento {{imprevisível}}

### Alvos de glitching

- Secure boot check >> Glitch no `if (signature_valid)` → CPU {{pula}} a verificação
- RDP/CRP check >> Glitch no momento da leitura do fuse → chip acha que proteção está {{desabilitada}}
- Password check >> Glitch no `strcmp` → retorna {{igual}} mesmo sendo diferente
- Loop counter >> Corromper contador → loop de criptografia executa {{menos rounds}} (chave mais fraca)

### Parâmetros de glitch

- Offset >> {{Momento}} exato do glitch em relação a um trigger (ex: edge no pino de clock)
- Width >> {{Duração}} do glitch em nanossegundos (muito curto = sem efeito, muito longo = crash)
- Repetição >> Milhares de tentativas variando offset e width → {{bruteforce}} do parâmetro correto

### Ferramentas

- ChipWhisperer >> Kit {{open-source}} completo (capture + glitch + target boards + Jupyter notebooks)
- PicoEMP >> EMFI (electromagnetic fault injection) {{barato}} (~$50)
- Glitcher DIY >> MOSFET + FPGA para gerar pulsos com precisão de {{nanossegundos}}

### Drill — Fault Injection #[[Drill]]

- Quer pular `if (valid == false)` no secure boot → técnica = >> {{Voltage glitch}} no momento do branch
- Glitch muito longo → resultado = >> {{Crash/reset}} do chip (não útil)
- Glitch muito curto → resultado = >> {{Nenhum efeito}} (não corrompeu a instrução)
- Precisa testar 50.000 combinações de offset/width → processo = >> {{Automatizado}} (ChipWhisperer + script)
- EMFI vs voltage glitch: EMFI é mais >> {{Localizado}} (afeta área específica do chip)
- ChipWhisperer Lite custa ~R$1500 → alternativa educacional = >> {{Software-only}} (notebooks Jupyter com traces pré-capturados)

---

## Módulo HH.7.3: Power Analysis com ChipWhisperer

### CPA (Correlation Power Analysis)

- Power trace >> Gráfico de {{consumo de energia}} ao longo do tempo durante operação criptográfica
- CPA ataca >> {{SubBytes}} do AES (saída da S-Box depende de chave ⊕ plaintext)
- Hipótese >> Para cada byte da chave, testar {{256}} valores possíveis (0x00 a 0xFF)
- Modelo >> Calcular {{Hamming Weight}} da saída da S-Box para cada hipótese
- Correlação >> Hipótese com maior {{correlação}} entre modelo e traces reais = byte correto da chave
- Traces necessários >> Tipicamente {{500-5000}} para AES-128 (depende do SNR)

### Workflow CPA

- Passo 1 >> Capturar traces: enviar {{plaintexts}} conhecidos, gravar consumo durante encriptação
- Passo 2 >> Para cada byte da chave, calcular S-Box output para todas {{256 hipóteses}}
- Passo 3 >> Calcular {{Hamming Weight}} de cada hipótese
- Passo 4 >> Correlacionar com traces reais → hipótese com maior {{r (Pearson)}} = chave correta
- Passo 5 >> Repetir para todos os {{16}} bytes da chave AES-128

### Drill — CPA #[[Drill]]

- AES-128 tem chave de 16 bytes → hipóteses por byte = >> {{256}} (0x00-0xFF)
- Total de hipóteses para chave completa = >> {{256 × 16 = 4096}} (não 256¹⁶!)
- SNR baixo → precisa de mais >> {{Traces}} (mais dados estatísticos)
- Correlação maior para hipótese 0x4F no byte 3 → provável chave byte 3 = >> {{0x4F}}
- Hamming Weight de 0xFF = >> {{8}} (todos 8 bits são 1)
- Hamming Weight de 0x00 = >> {{0}} (todos bits são 0)

---

##  Módulo HH.7.4: Ataques Físicos e Chip-Off

### Técnicas

- Chip-off >> {{Dessoldar}} chip de flash e ler com programador externo
- Hot air rework >> Estação com {{ar quente}} para desoldar componentes SMD/BGA
- Cold boot >> Congelar (spray cooling) para reter dados na {{SRAM}} sem alimentação
- Decapping >> Remoção do encapsulamento com {{ácido nítrico}} → expõe o die de silício
- Microprobing >> Usar probes de {{precisão}} para tocar trilhas internas do die

### Casos célebres

- Xbox 360 Reset Glitch Hack >> {{Glitch}} no reset line durante boot → bypass de fuses
- Nintendo Switch Fusée Gelée >> Bug no {{bootloader ROM}} (Tegra X1) → code execution via USB
- Ledger wallet attack >> {{Voltage glitch}} para extrair seed phrases da secure element

### Custo e acessibilidade

- Chip-off (hot air station) >> ~R${{500-1000}} (acessível para pesquisadores)
- Decapping (ácido + microscópio) >> ~R${{2000-5000}} (requer equipamento de laboratório)
- Laser FI >> R${{50.000+}} (acadêmico/governamental)

### Drill — Ataques Físicos #[[Drill]]

- JTAG desabilitado, fuses queimados, firmware criptografado → último recurso = >> {{Chip-off}} (dessoldar flash, ler externamente)
- Chave AES está na SRAM em runtime → como preservar após desligar? >> {{Cold boot}} (congelar chip, reter dados voláteis)
- Xbox 360 hack usou → >> {{Reset glitch}} (pulso no pino de reset durante boot check)
- Switch hack explorou bug no → >> {{Bootloader ROM}} do Tegra X1 (não atualizável!)
- Decapping expõe o → >> {{Die de silício}} para inspeção com microscópio
