# HH C.3 - Engenharia Reversa de Gate-Level

Standard cells em projeto de CIs?
?
Portas logicas pre-definidas (NAND, NOR, Flip-Flops) com layout visual padronizado na biblioteca do fabricante.

Utilidade das standard cells na RE visual?
?
Permite reconhecer visualmente funcoes logicas num die shot repetitivo, mapendo-os diretamente em simbolos de portas logicas.

Gate-level netlist na RE de silicio?
?
Arquivo texto (como um source code) resultante do mapeamento, descrevendo como cada standard cell esta conectada as outras portas.

Processo completo de Gate-Level RE?
?
Die shot → identificar standard cells → traçar todas as interconexões (routing) → gerar netlist final → analisar em simulador logico (Verilog).

Identificacao fisica de uma NAND gate em CMOS antigo?
?
Costuma ser composta pelo agrupamento de 2 transistores PMOS em paralelo superior e 2 NMOS em serie inferior.

Camouflaged gates na obfuscação de hardware?
?
Tecnica anti-RE onde duas portas logicas de funcões diferentes (ex: NAND e NOR) sao fabricadas para parecerem visualmente identicas sob microscopio optico/SEM.

Camouflaged gates vs Logic Obfuscation?
?
Camuflagem foca no visual fisico da porta. Obfuscação logica embaraalha a funcionalidade inserindo "chaves" (bits falsos) que devem ser alimentadas para o circuito funcionar.

Desafio da RE em chips da atualidade (ex: ARM de smartphone)?
?
Bilhoes de transistores em ate 10+ camadas de metal. RE manual e humana e impossivel, exigindo automacao pesada via machine learning e scripts customizados.
