# Módulo 0 — Matemática/Física: Fase 4 — Cálculo III: Multivariável
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

##  Módulo 0.15 — Derivadas Parciais e Gradiente

### Derivada Parcial

- ∂f/∂x significa >> Derivar em x tratando demais variáveis como {{constantes}}

### Gradiente

- ∇f = >> {{(∂f/∂x, ∂f/∂y, ∂f/∂z)}} — aponta para MÁXIMO crescimento
- E = -∇V >> Campo elétrico aponta na direção de {{maior queda}} de potencial

Analogia: E = -∇V como bola descendo morro no mapa topográfico do potencial. Onde linhas equipotenciais estão próximas = campo forte (gradiente grande).

### Propagação de Erros

- Fórmula >> σ_f² = {{(∂f/∂x)²σ_x² + (∂f/∂y)²σ_y²}}
- Por que soma em quadratura? >> Erros são {{independentes}} — improvável que todos sejam máximos juntos

### Cadeia Multivariável

- f(x,y), x=x(t), y=y(t): df/dt = >> {{(∂f/∂x)(dx/dt) + (∂f/∂y)(dy/dt)}}

### Drill — Parciais e Erros #[[Drill]]

- ∂P/∂I para P=I²R = >> {{2IR}}
- ∂P/∂R para P=I²R = >> {{I²}}
- R=V/I, V=12±0.1V, I=0.1±0.005A → R, σR = >> R={{120Ω}}, σR≈{{6.1Ω}}

---

## ∬ Módulo 0.16 — Integrais Múltiplas

### Conceitos

- ∬f(x,y) dA >> Soma sobre a {{área}} — "volume sob superfície"
- ∭f(x,y,z) dV >> Soma sobre o {{volume}}

### Coordenadas Especiais

- Cilíndricas (r,θ,z): dA = >> {{r dr dθ}} — simetria axial (fios, bobinas)
- Esféricas (r,θ,φ): dV = >> {{r²sinθ dr dθ dφ}} — simetria esférica (cargas pontuais)

### Aplicações EE

- Carga total numa superfície >> Q = {{∬σ dA}}
- Capacitor placas paralelas >> C = {{εA/d}}
- Capacitor cilíndrico >> C = {{2πεL/ln(b/a)}}

Quando usar cilíndricas: simetria em torno de um eixo (fio reto, solenoide). Quando usar esféricas: simetria esférica (carga pontual, dipolo, antena).

---

## ∇× Módulo 0.17 — Cálculo Vetorial: A Linguagem de Maxwell

### Operadores

- Campo vetorial >> Cada ponto do espaço associado a um {{vetor}} (E, B)
- ∇·F (divergência) = >> {{∂Fx/∂x + ∂Fy/∂y + ∂Fz/∂z}} — mede emanação
- ∇×F (rotacional) >> Mede {{circulação}} do campo

### Significado Físico

- ∇·F > 0 → >> {{fonte}} (campo sai do ponto)
- ∇·F < 0 → >> {{sumidouro}} (campo entra)
- ∇·F = 0 → >> Campo {{solenoidal}} (ex: B sempre!)

### Integrais Vetoriais

- ∮E·dl = >> {{Trabalho}} ao longo do caminho. V = -∫E·dl
- ∬B·dA = >> {{Fluxo magnético Φ}} [Wb]

### Teoremas

- Gauss (Divergência) >> ∯F·dA = {{∭(∇·F) dV}}
- Stokes >> ∮F·dl = {{∬(∇×F)·dA}}

### 4 Equações de Maxwell

- ∇·E = ρ/ε₀ >> {{Gauss Elétrica}}: cargas criam campo E
- ∇·B = 0 >> {{Gauss Magnética}}: linhas de B sempre fechadas, sem monopolos
- ∇×E = -∂B/∂t >> {{Faraday}}: B variável cria E circulante → indução
- ∇×B = μ₀J + μ₀ε₀∂E/∂t >> {{Ampère-Maxwell}}: corrente (e ∂E/∂t) cria B circulante

### Maxwell em Uma Frase

- ∇·E = ρ/ε₀ >> "Cargas são {{fontes}} de campo E"
- ∇·B = 0 >> "Campo B {{nunca}} tem início nem fim"
- ∇×E = -∂B/∂t >> "B que {{varia}} cria E circulante" → transformador
- ∇×B = μ₀J+... >> "Corrente cria B {{circulante}}" → motor

### Equação ↔ Dispositivo

- Capacitor ↔ >> {{1ª: ∇·E = ρ/ε₀}}
- Indutor ↔ >> {{4ª: ∇×B = μ₀J}}
- Transformador ↔ >> {{3ª: ∇×E = -∂B/∂t}}
- Antena ↔ >> {{3ª e 4ª juntas}} — E↔B se geram → onda EM

### Constantes

- ε₀ (permissividade do vácuo) ≈ >> {{8.854 × 10⁻¹² F/m}}
- μ₀ (permeabilidade do vácuo) = >> {{4π × 10⁻⁷ H/m}}
- c = 1/√(μ₀ε₀) = >> {{3 × 10⁸ m/s}}

As 4 equações de Maxwell descrevem TODO o eletromagnetismo — luz, rádio, motores, geradores, transformadores, antenas. Você não precisa resolvê-las diretamente na maioria da EE, mas precisa ENTENDER o que cada uma diz.
