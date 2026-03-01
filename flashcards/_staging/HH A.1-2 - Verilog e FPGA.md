# HH A.1-A.2 - Verilog e FPGA

O que e uma FPGA?
?
Field Programmable Gate Array — hardware reconfiguravel com milhares de blocos logicos. Tudo executa em PARALELO e em nanossegundos. Essencial para glitching e analise de protocolos.

Verilog: wire vs reg?
?
`wire` = conexao combinacional (valor determinado continuamente). `reg` = armazenamento sequencial (atualizado em eventos como borda de clock).

Verilog: = vs <=?
?
`=` (blocking) = atribuicao sequencial, usado em logica combinacional. `<=` (non-blocking) = atribuicao paralela, usado em `always @(posedge clk)`.

Componentes internos de FPGA?
?
CLBs (blocos logicos configuraveis), IOBs (I/O), Block RAM, DSP slices, PLLs/DCMs (gerenciar clock).

FPGAs para seguranca/hacking?
?
iCE40 (~$25, toolchain open-source), Artix-7 (ChipWhisperer), ECP5 (Lattice, open-source). Ferramentas: Yosys (sintese), nextpnr (place & route), Icarus Verilog (simulacao).

O que e um bitstream?
?
Arquivo que configura o FPGA — e o "firmware" do FPGA. Carregado na SRAM ao ligar (volatil) ou salvo em flash externa.

Como simular Verilog?
?
`iverilog -o sim modulo.v testbench.v && vvp sim` → gera VCD. `gtkwave sim.vcd` → visualizar formas de onda.
