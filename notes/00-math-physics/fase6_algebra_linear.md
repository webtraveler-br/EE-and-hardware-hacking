# Módulo 0 — Matemática/Física: Fase 6 — Álgebra Linear
Cole este arquivo diretamente no RemNote.
- = Card permanente (core). Mantenha ativo para sempre.
- #[[Drill]] = Card temporário. Suspenda após acertar 3-5x seguidas.
- Texto sem marcação = Nota de referência (não gera review).

---

## Módulo 0.21 — Vetores e Operações

### Fundamentos

- Vetor >> Grandeza com {{magnitude}} e {{direção}}. v = (vx, vy, vz)
- Módulo |v| = >> {{√(vx² + vy² + vz²)}}
- Vetores unitários >> î, ĵ, k̂ — qualquer vetor = {{combinação linear}} deles

### Produto Escalar (dot product)

- a·b = >> {{|a|·|b|·cos(θ)}} = ax·bx + ay·by + az·bz
- Resultado do produto escalar >> Um {{escalar}} (número)
- Significado >> Mede "quanto {{a}} está na direção de {{b}}"
- a·b = 0 → >> Vetores {{perpendiculares}} (ortogonais)
- Aplicação EE >> P = F·v — potência é produto {{escalar}}

### Produto Vetorial (cross product)

- a×b = >> Vetor com módulo {{|a|·|b|·sin(θ)}} e direção perpendicular a ambos
- Resultado do produto vetorial >> Um {{vetor}} perpendicular ao plano de a e b
- Significado >> Mede "área do {{paralelogramo}}" formado por a e b
- Aplicação EE >> F = qv×B — força magnética é produto {{vetorial}}
- a×b vs b×a >> {{a×b = -(b×a)}} — ordem importa! (anticomutativo)

F = qv×B usar produto vetorial garante que a força é SEMPRE perpendicular à velocidade e ao campo — por isso a força magnética não faz trabalho, só desvia trajetória.

### Drill — Vetores #[[Drill]]

- a=(3,4,0), b=(1,2,5): a·b = >> {{3+8+0}} = {{11}}
- a=(3,4,0), |a| = >> {{5}}
- F=qv×B, q=1.6e-19C, v=(10⁶,0,0), B=(0,0,0.5): F = >> {{8×10⁻¹⁴ N}} na direção ĵ

---

## Módulo 0.22 — Matrizes e Sistemas Lineares

### Forma Matricial

- Sistema linear em forma matricial >> {{Ax = b}} — A=coeficientes, x=incógnitas, b=constantes
- Quando o sistema tem solução única? >> Quando det(A) {{≠ 0}}
- det(A) = 0 → >> Sistema {{impossível ou indeterminado}} — circuito mal definido

### Determinante e Inversa

- det 2×2: det[a b; c d] = >> {{ad - bc}}
- Inversa: A⁻¹ existe se >> det(A) {{≠ 0}}
- Solução: x = >> {{A⁻¹·b}}

### Métodos de Resolução

- Cramer (prático para 2×2) >> xᵢ = {{det(Aᵢ)/det(A)}}
- Eliminação Gaussiana >> {{Escalonamento}} → resolução por substituição. Funciona para qualquer tamanho
- Python >> {{np.linalg.solve(A, b)}} — resolve em milissegundos

### Conexão com Circuitos

- Análise nodal/malhas SEMPRE produz >> Sistema linear {{Ax = b}}
- Diagonal da matriz nodal >> Soma das {{condutâncias}} conectadas ao nó
- Fora da diagonal >> {{Negativo}} da condutância entre os nós

### Princípio da Superposição

- Superposição: quando usar? >> Em sistemas {{lineares}} — a resposta total = soma das respostas a cada fonte {{individualmente}}
- Como aplicar superposição >> Desligue todas as fontes menos uma, calcule, repita, e {{some}} os resultados
- "Desligar" fonte de tensão >> Substitua por {{curto-circuito}}
- "Desligar" fonte de corrente >> Substitua por {{circuito aberto}}

Superposição funciona SOMENTE em circuitos lineares (R, L, C com fontes independentes). NÃO funciona para potência (P = I²R não é linear).

A matriz é a "foto numérica" do circuito. Na análise nodal: cada nó = uma linha, cada conexão = um coeficiente. np.linalg.solve() resolve circuitos de qualquer complexidade.

### Drill — Determinantes #[[Drill]]

- det[2 3; 1 4] = >> {{8-3}} = {{5}}
- det[1 0; 0 1] = >> {{1}} (identidade, sempre 1)
- det[2 4; 1 2] = >> {{0}} → sistema indeterminado

---

## Módulo 0.23 — Autovalores, Autovetores e Estabilidade

### Definições

- Autovalor λ >> Solução de {{det(A - λI) = 0}}
- Autovetor v >> Vetor tal que {{Av = λv}} — direção que não muda, só escala
- Significado de λ >> "Quanto A {{amplifica}} (ou atenua) naquela direção"

### Estabilidade — A Pergunta Fundamental

- Autovalor real negativo → >> {{Decaimento}} exponencial — estável
- Autovalor real positivo → >> {{Crescimento}} exponencial — INSTÁVEL!
- Autovalor complexo, parte real < 0 → >> {{Oscila e decai}} — estável
- Autovalor complexo, parte real > 0 → >> {{Oscila e cresce}} — instável!
- Autovalor puramente imaginário → >> {{Oscilação permanente}} — marginalmente estável

### Conexão com Polos

- Polos de G(s) = >> {{Autovalores}} da matriz de estado
- Semiplano esquerdo (Re < 0) = >> {{Estável}}
- Semiplano direito (Re > 0) = >> {{Instável}}

### Rank e Espaço Nulo

- Rank de A >> Número de linhas/colunas {{linearmente independentes}}
- Rank completo (rank = n) >> Sistema tem solução {{única}} — circuito bem definido
- Rank incompleto (rank < n) >> Sistema {{indeterminado}} — circuito tem graus de liberdade

Rank responde "quantas equações são realmente independentes?" sem precisar resolver. Se rank < n, alguma equação é redundante ou contraditória.

### Diagonalização

- A = PDP⁻¹ >> Desacopla o sistema em modos {{independentes}}
- D contém >> Os {{autovalores}} na diagonal
- P contém >> Os {{autovetores}} como colunas

Autovalores respondem A pergunta fundamental de controle: "o sistema é estável?". Todos negativos → perturbações decaem → estável. Algum positivo → perturbações crescem → explode. O Módulo 5 (Controle) é inteiramente sobre MOVER autovalores usando feedback.

### Drill — Autovalores e Polos #[[Drill]]

- G(s)=1/(s²+3s+2): polos? Estável? >> s={{-1}}, s={{-2}} → ambos negativos → {{estável}}
- G(s)=1/(s²-s+2): estável? >> Polos com parte real {{positiva}} → {{instável}}
- Python para autovalores >> {{np.linalg.eig(A)}}
