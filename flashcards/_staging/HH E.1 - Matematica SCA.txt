# HH E.1 - Matematica para SCA Avancado

Correlacao de Pearson no contexto SCA?
?
Mede a relacao linear entre variaveis: compara o consumo hipotetico do modelo de ataque e os traços reais SNR no hardware analisado.

Hamming Weight no vazamento digital?
?
Modelo basico de consumo que assume que a energia gasta no barramento reflete literalmente o "numero de bits '1'" sendo processados num instante ($X_{0} = \text{0Watts}, X_{255} = MaxWatts$).

Hamming Distance em analises dinamicas?
?
Modelo focado nas MUDANÇAS de bits registrador a registrador (Flip-flops). Ex: A mudanca de 0xFF (tudo 1) para 0x00 gasta mais energia que 0x01 para 0x02.

Mutual Information Analysis (MIA)?
?
Generalizacao estatistica mais sofisticada pra correlacao que permite capturar relacoes nao-lineares de consumo fisico entre o firmware e o osciloscopio real.

Template Attacks teorico vs pratico?
?
Método estatisticamente "ótimo". Monta um modelo probabilistico de leakage usando um clone exato da placa antes de tentar vazar chaves reais. E o padrao ouro pra SCA ruidoso.

Calculo essencial de SNR (Signal-To-Noise Ratio)?
?
Quanto ruido a PCB/fonte tem. $Var(\text{Sinal}) / Var(\text{Ruido})$. Dita imediatamente quantas milhoes ou poucas dezenas de traces serao exigidas para o Correlation Attack AES quebrar.
