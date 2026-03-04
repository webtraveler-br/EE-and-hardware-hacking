# Módulo 0 — Matemática/Física: Fase 5 — Equações Diferenciais
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 0.18 — EDOs de 1ª Ordem: Circuitos RC e RL

### Forma e Solução

- EDO de 1ª ordem (forma canônica) >> dy/dt + (1/τ)y = {{f(t)}}
- Solução geral da EDO de 1ª ordem >> y(t) = {{y_∞ + (y₀ - y_∞)·e^(-t/τ)}}
- O que é y_∞? >> O valor {{final}} (regime permanente, quando t → ∞)
- O que é y₀? >> O valor {{inicial}} (condição em t = 0)
- Constante de tempo τ do RC <> {{R·C}} [s]
- Constante de tempo τ do RL <> {{L/R}} [s]

### Significado de τ

- Em 1τ, sinal atinge >> {{63%}} do valor final (ou cai para 37%)
- Em 3τ, sinal atinge >> {{95%}} do valor final
- Em 5τ, sinal atinge >> {{99%}} do valor final — considerado "completo"
- τ grande = >> Sistema {{lento}}
- τ pequeno = >> Sistema {{rápido}}

### Valores de e^(-n) — Ter de Cabeça

- e⁻¹ ≈ >> {{0.368}} (37% — o que resta após 1τ)
- e⁻² ≈ >> {{0.135}} (13.5%)
- e⁻³ ≈ >> {{0.050}} (5%)
- e⁻⁵ ≈ >> {{0.0067}} (<1% — por isso 5τ ≈ "completo")

### Ponte Temporal ↔ Frequencial

- Frequência de corte de um RC >> f_c = {{1/(2πτ)}} = 1/(2πRC)
- τ ↔ f_c >> São {{inversos}} (a menos de 2π): τ grande → f_c baixa, τ pequeno → f_c alta

### Respostas Natural e Forçada

- Resposta natural (homogênea) >> Fonte = 0, sistema evolui {{livre}} — decai exponencialmente
- Resposta forçada (particular) >> Resposta à {{excitação}} externa — determina o valor final
- Resposta total >> {{Natural + Forçada}} — transitório + regime permanente
- Condição inicial determina >> A constante {{C}} na solução — ex: V_C(0) = 0 → capacitor descarregado

### Montagem da EDO a partir do Circuito

- EDO do RC (KVL) >> V = RI + V_C, com I = CdV_C/dt → {{RC·dV_C/dt + V_C = V}}
- Solução para carga RC >> V_C(t) = {{V·(1 - e^(-t/RC))}}
- Solução para descarga RC >> V_C(t) = {{V₀·e^(-t/RC)}}
- EDO do RL (KVL) >> V = RI + LdI/dt → {{(L/R)·dI/dt + I = V/R}}

### Método de Resolução

- Separação de variáveis >> dy/y = -dt/τ → ln(y) = -t/τ + C → y = {{A·e^(-t/τ)}}
- Fator integrante: quando usar? >> Quando a EDO tem forma dy/dt + P(t)y = Q(t) e P(t) {{não é constante}}

TODOS os sistemas de 1ª ordem se comportam IGUAL: exponencial que estabiliza. Carga RC, descarga RL, café esfriando, tanque esvaziando — mesma equação, mesma forma, diferente τ.

### Drill — RC e RL #[[Drill]]

- R=1kΩ, C=100μF → τ = >> {{0.1 s}}
- R=10kΩ, C=10nF → τ = >> {{100 μs}}
- L=10mH, R=100Ω → τ = >> {{0.1 ms}}
- R=1kΩ, C=100μF, V=12V → V_C(τ) ≈ >> {{7.6 V}} (63% de 12)
- R=1kΩ, C=100μF, V=12V → V_C(5τ) ≈ >> {{11.9 V}} (99% de 12)

---

##  Módulo 0.19 — EDOs de 2ª Ordem: RLC e Oscilações

### Forma Canônica

- EDO de 2ª ordem >> d²y/dt² + 2α·dy/dt + ω₀²·y = {{f(t)}}
- Equação característica >> s² + 2αs + ω₀² = 0 → s = {{-α ± √(α²-ω₀²)}}

### Parâmetros do RLC Série

- α (coeficiente de amortecimento) do RLC série <> {{R/(2L)}}
- ω₀ (frequência natural) do RLC série <> {{1/√(LC)}}

Não confunda: α = R/(2L) é amortecimento, ω₀ = 1/√(LC) é frequência natural. São grandezas DIFERENTES.

### Os 3 Casos — Classificação

- α > ω₀ → >> {{Superamortecido}} — 2 raízes reais negativas, decai sem oscilar
- α = ω₀ → >> {{Criticamente amortecido}} — raiz dupla, decai o mais rápido possível sem oscilar
- α < ω₀ → >> {{Subamortecido}} — raízes complexas, oscila e decai

### Subamortecido — O Mais Comum

- Frequência de oscilação amortecida >> ω_d = {{√(ω₀² - α²)}}
- Forma da resposta subamortecida >> y(t) = {{A·e^(-αt)·sin(ω_d·t + φ)}}
- R pequeno no RLC → >> {{Oscila muito}} (pouco amortecimento, α pequeno)
- R grande no RLC → >> Volta devagar, {{sem oscilar}} (muito amortecimento)

### Ressonância e Fator de Qualidade Q

- Ressonância ocorre quando >> ω_excitação = {{ω₀}} — amplitude máxima!
- Fator de qualidade Q = >> {{ω₀/(2α)}} = (1/R)·√(L/C)
- Q alto (>10) >> Ressonância {{estreita}} e afiada — ótimo para filtros seletivos
- Q baixo (<1) >> Ressonância {{larga}} — sistema muito amortecido
- Largura de banda BW = >> {{ω₀/Q}} = 2α — quanto menor Q, mais larga

Analogia mecânica EXATA: L = massa (inércia), C = mola (armazena), R = amortecedor (dissipa). Ressonância = empurrar o balanço na frequência certa.

Criticamente amortecido NÃO é necessariamente "melhor" — depende da aplicação. Para um voltímetro, queremos Q baixo (amortecer rápido). Para um filtro de rádio, queremos Q alto (seleção precisa).

### Drill — Classificação RLC #[[Drill]]

- R=100Ω, L=10mH, C=1μF → α=?, ω₀=? >> α={{5000}}, ω₀={{10000 rad/s}} → α<ω₀ → subamortecido
- R=200Ω, L=10mH, C=1μF → α=?, tipo? >> α={{10000}} = ω₀ → criticamente amortecido
- R=500Ω, L=10mH, C=1μF → tipo? >> α=25000 > ω₀=10000 → {{superamortecido}}

---

## Módulo 0.20 — Transformada de Laplace

### Conceito

- O que Laplace faz? >> Transforma {{cálculo}} (derivadas, EDOs) em {{álgebra}} (equações)
- Definição de Laplace >> F(s) = {{∫₀^∞ f(t)·e^(-st) dt}}

Laplace é como traduzir um problema de chinês para português — o problema é o MESMO, mas num "idioma" que você domina. Todo o Módulo 5 (Controle e Sinais) é baseado em Laplace.

### Tabela de Transformadas — MEMORIZAR

- L{1} <> {{1/s}}
- L{t} <> {{1/s²}}
- L{e^(-at)} <> {{1/(s+a)}}
- L{sin(ωt)} <> {{ω/(s²+ω²)}}
- L{cos(ωt)} <> {{s/(s²+ω²)}}
- L{δ(t)} (impulso) <> {{1}}
- L{u(t)} (degrau) <> {{1/s}}

### Propriedades Fundamentais

- L{df/dt} = >> {{s·F(s) - f(0⁻)}} — derivada vira multiplicação por s!
- L{∫f dt} = >> {{F(s)/s}} — integral vira divisão por s
- Por que L{df/dt} = sF(s)-f(0⁻) é revolucionário? >> Porque EDO vira equação {{algébrica}} — sem derivadas!

### Função de Transferência e Polos

- Função de transferência >> H(s) = {{V_out(s) / V_in(s)}}
- Polos de H(s) são >> {{Raízes do denominador}}
- Polos com parte real negativa → >> Sistema {{estável}} (semiplano esquerdo)
- Polos com parte real positiva → >> Sistema {{instável}}! (semiplano direito)
- Polos puramente imaginários → >> Oscilação {{permanente}} (marginalmente estável)

### Resposta ao Degrau vs Impulso

- Resposta ao impulso h(t) >> Saída quando entrada = {{δ(t)}} (impulso unitário)
- Resposta ao degrau s(t) >> Saída quando entrada = {{u(t)}} (degrau unitário)
- Relação entre elas >> h(t) = {{ds(t)/dt}} — impulso = derivada do degrau
- H(s) e resposta ao impulso >> H(s) = L{h(t)} — H(s) É a transformada da resposta ao {{impulso}}

### Método de Resolução por Laplace

- Passo 1 >> {{Transformar}} a EDO para domínio s (aplicar L{} em cada termo)
- Passo 2 >> {{Resolver}} a equação algébrica para Y(s)
- Passo 3 >> {{Frações parciais}} para decompor Y(s) em termos simples
- Passo 4 >> {{Inversa}} de Laplace usando a tabela → y(t)

### Impedância no Domínio s

- Z_R(s) <> {{R}}
- Z_L(s) <> {{sL}}
- Z_C(s) <> {{1/(sC)}}

Observe: no domínio s, jω é substituído por s. Quando s = jω, as impedâncias voltam às formas fasoriais.

### Drill — Polos e Estabilidade #[[Drill]]

- H(s) = 1/(s²+3s+2): polos? >> s={{-1}} e s={{-2}} → estável (ambos negativos)
- H(s) = 1/(s²-s+2): onde estão os polos? >> Parte real {{positiva}} → instável!
- H(s) = 10/(s+5): polo? >> s={{-5}} → estável, τ = {{0.2 s}}

### Drill — Transformadas #[[Drill]]

- L{3·e^(-2t)} = >> {{3/(s+2)}}
- L{5·sin(10t)} = >> {{50/(s²+100)}}
- L⁻¹{1/(s+3)} = >> {{e^(-3t)}}
- L⁻¹{4/s} = >> {{4}} (degrau × 4)
