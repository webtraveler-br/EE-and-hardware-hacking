# Guia de Geracao de Flashcards

Referencia para gerar cards para os roadmaps de EE e Hardware Hacking.

## Principios

1. **Um fato por card.** Nunca "liste todas as regras de derivacao". Cada regra e um card separado.
2. **Pergunta especifica, resposta curta.** Se a resposta tem mais de 3 linhas, o card esta grande demais — quebre em dois.
3. **Formato pergunta → resposta.** Nao use cards so com definicao. "O que e impedancia?" e melhor que "Impedancia: oposicao ao fluxo de corrente alternada".
4. **Contexto de EE.** Sempre que possivel, conecte a formula ao uso pratico. "$\tau = RC$" sozinho e vazio. "$\tau = RC$ — em $t = \tau$ o capacitor carregou 63.2%" e util.
5. **Sem cards de consulta.** Nao gere cards para coisas que o aluno deve consultar (pinagem de chips, tabelas de constantes, sintaxe de ferramentas). So gere cards de coisas que precisam ser instantaneas.

## O que incluir

- Formulas fundamentais e suas variacoes ($V=IR$, $P=I^2R$, $P=V^2/R$)
- Conversoes e prefixos que aparecem constantemente
- Definicoes de conceitos que conectam modulos (Thevenin, impedancia, FSRS)
- Relacoes causa-efeito ("se R dobra com V fixo, o que acontece com I?")
- Erros comuns do modulo (se o roadmap lista "erros comuns", faca cards deles)
- Valores de referencia (-3dB = metade da potencia, $e^{-1} \approx 0.368$)

## O que nao incluir

- Demonstracoes ou provas matematicas
- Passos de resolucao de problemas (isso se pratica resolvendo, nao com cards)
- Detalhes de ferramentas (como usar Falstad, LTspice, KiCad)
- Conteudo que exige diagrama visual complexo (circuitos inteiros, graficos de Bode)
- Historia ou curiosidades
- Qualquer coisa que esteja na secao "Intuicao" do roadmap como texto longo — a intuicao se constroi com pratica, nao com cards

## Formato tecnico

```
Pergunta clara e direta
?
Resposta concisa, com LaTeX se houver math: $V = IR$
```

- Use `$...$` para math inline
- Headers `#` sao usados para organizar por secao dentro do arquivo, nao geram cards
- Sem emojis
- Sem formatacao excessiva — negrito so pro essencial

## Quantidade por modulo

- Modulos simples (1-1.5h): 5-10 cards
- Modulos medios (2-2.5h): 8-15 cards
- Modulos densos (3h+): 12-20 cards
- Nunca mais de 20 cards por modulo — se passar, esta incluindo coisas desnecessarias

## Nome do arquivo

`X.Y - Nome do Modulo.md` (ex: `0.1 - Unidades SI.md`, `1.6 - KVL e KCL.md`)

Sempre colocado em `_staging/` para revisao antes de mover para a pasta do pilar.
