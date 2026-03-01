# HH E.1-E.4 - Criptoanalise Avancada e DL-SCA

O que e a Correlacao de Pearson no contexto SCA?
?
Métrica usada em CPA (Correlation Power Analysis) para comparar um modelo hipotetico de consumo de energia (Hamming Weight da suposta sub-key) com o sinal eletrico capturado. Maior correlacao revela a chave.

O que e Masking (contramedida SCA)?
?
Randomizar as variaveis intermediarias em "shares" (ex: $X = X_1 \oplus X_2$), onde uma share e um numero aleatorio. Desfaz a correlacao de consumo de primeira ordem.

Como mascaramento e atacado?
?
Attacks de alta ordem (Higher-order SCA): combinando amostras vazadas em multiplos pontos temporais, ou usando Machine Learning / Deep Learning que descobre padroes complexos magicamente.

Vantagem do Deep Learning SCA sobre Template Attacks?
?
Deep Learning nao requer que o pesquisador crie um "modelo de vazamento" preciso, lida melhor com ruidos/desalinhamentos nas ondas, e bypassa algoritmos de mascaramento mais facilmente se treinado da forma correta.

Por que algoritmos criptograficos proprietarios sao um risco?
?
Frequentemente desenhados por amadores sem provisao contra criptoanalise moderna diferencial/linear. Normalmente identificaveis ao reverter no Ghidra pela falta das misteriosas constantes S-Box padroes.
