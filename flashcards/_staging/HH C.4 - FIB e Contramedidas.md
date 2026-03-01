# HH C.4 - FIB Circuit Editing e Contramedidas

Milling operacao em FIB: aplicacao em seguranca?
?
Uso do feixe de ions para cortar fisicamente trilhas micrometricas. Exemplo pratico: desabilitar um fuse de protecao (RDP/CRP).

Deposition operacao em FIB: aplicacao em seguranca?
?
Uso do feixe para injetar/depositar metal ou material isolante. Util para criar novas conecções expondo os barramentos internos protegidos aos pads publicos.

Active mesh contramedida em smartcards?
?
Rede de fios extremamente finos dispostos sobre a logica ativa inteira do die. Se o atacante tentar cortar uma trilha com FIB, inevitavelmente quebra o mesh, ativando autodestruicao do chip.

Sensores ambientais contra decapping/FIB?
?
Chips criticos contem fotosensores (detectam a luz ao decapar), microlotermometros e detectores de picos de voltagem anomala (fault injection/glitch trigger).

Por que Circuit Editing e restrito a alvos de muito alto valor?
?
Custo destrutivo extremamente alto ($10K-$50K por sessa/chip). Atacantes costumam perder varios chips operaveis por tentativa erro na sintonizacao do FIB.
