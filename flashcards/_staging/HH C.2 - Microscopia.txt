# HH C.2 - Microscopia e Imageamento

Microscopio optico em RE de silicio: limitacao central?
?
Resolucao utilitaria se limita a ~200nm. Funciona apenas para processos de fabricacao maiores que 0.5µm (chips antigos).

SEM (Scanning Electron Microscope): resolucao e uso?
?
Resolucao de ~1nm usando feixe de eletron varrendo a superficie. Essencial para chips modernos (7-14nm) limitados pelotica.

FIB (Focused Ion Beam) vs SEM?
?
SEM apenas "enxerga" (imageia) o chip. FIB usa feixe de ions para "operar": corta trilhas e deposita material no die.

Identificacao visual de block RAM / Flash no die?
?
Apresentam padrao repetitivo, matricial e altamente denso comparado ao resto do chip.

Identificacao visual de nucleo da CPU (logica) no die?
?
Apresenta padroes de tracos e transistores irregulares, confusos e esparsos, sem a organizacao matricial de uma memoria.

Identificacao visual de pads de I/O no die?
?
Ficam geralmente posicionados no perimetro do chip, em blocos grandes isolados conectados aos pinos fisicos externos.

Ferramenta software para analise assistida de die shots?
?
Degate (open-source). Permite analisar imagens de alta resolucao, marcar estruturas e rastrear trilhas em multiplas camadas.

Custo comparativo entre ferramentas de microscopia?
?
Microscopio metalurgico: ~$500-2000 (acessivel). SEM: ~$50K. FIB: ~$200K+. RE moderna depende de labs externos comerciais ou universidades.
