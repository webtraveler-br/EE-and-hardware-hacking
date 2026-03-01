# HH E.3 - Template Attacks e Deep Learning SCA

Workflow Profiling vs Matching em Template Attacks?
?
1 - Caracterização em Dispositivo Clone (Cria um "Perfil de Ruído/Curva 0..255"). 2 - Avaliação Atacante cruza as curvas pre-calculadas comparando o alvo restrito para matches de vazamento.

Vantagem nativa essencial de DL-SCA sobre CPA estatistico?
?
Extrai a chave alvo usando Redes Neurais que contornam desalinhamentos absurdos na captura osciloscopica e mitigam mascaramentos que destruíriam as hipoteses lineares velhas.

Vantagem CNNs sobre MLPs no Deep Learning Hardware Hacking?
?
Possuem filtro nativo anti-jitter de sinal: nao requerem que o atacante defina modelos de vazamentos ou alinhem triggers de captura microsegundo a microsegundo rigorosamente.

Framework ASCAD em padroes de pesquisa?
?
Base de dados pública universal com amostras padronizadas de captures eletromagneticas e simuladores baseados em python (keras) validantes para DL-SCA e testes anti-mascaramento teorico acadêmicos.

Visao computacional Pooling Layers reduzindo e mitgando Desalinhamentos de Traces SCA?
?
O uso de Average/Max Pooling nas camadas da CNN comprime a resolução espacial horizontal do sinal de power captado, fazendo a I.A tolerar pequenos jitter de ns (atrasos cronologicos no osciloscopio).

Dispensabilidade de Leakage Models severos no Deep Learning SCA (Redes Neurais ativas)?
?
Ao inverso do CPA, o atacante nao precisa tentar adivinhar se o chip vaza Hamming Weight ou Hamming Distance. A camada convolutiva profunda aprende sozinha as features nao lineares escondidas do chip.
