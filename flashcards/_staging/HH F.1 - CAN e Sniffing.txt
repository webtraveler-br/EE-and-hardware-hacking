# HH F.1 - CAN Bus e Sniffing

CAN (Controller Area Network) em engenharia automotiva?
?
Barramento difuso e Broadcast padronizado multi-nós em topologia diferencial utilizado na integraçao das unidades UCEs/ECUs desde anos 1980 sem distinçao logica inata de "origem" ou credenciamento local.

Atribuicao Hierarquica de acesso e Colisão CAN?
?
Arquitetado via Arbitragem por prioridade Dominante e Recessiva. ID inferior numérico = Prioridade Massiva real ($0x000$ trava todos os outros transmissores na contencao do pacote imediato).

Seguranca Inata Zero CAN bus padrao (Veiculos Atuais)?
?
Não há sequer campo de Identificação, MAC address nativo na rede, criptografia ou assinatura no trafego veicular nativo ($CAN-A$ e $CAN-B$) admitindo total anonimato dos emissores injetados no port OBD-II.

Ferramentas mandatorias essenciais em GNU/Linux (SocketCAN)?
?
Modulo Kernel/`can-utils`: suite com $candump$, $cansniffer$, $cansend$ responsaveis pelo monitoramento passivo do protocolo base e posterior dump log de controle e injecão manual.

CAN FD (Flexible Data-rate) mudanca estrutural atrelada focativa base natural?
?
Aumenta a velocidade bit rate limitante amarrativa purista nativa (ate 8mbps logicos bruscos lidos) e o Payload Frame (saltando dos restrives 8 Bytes de praxe para massivos 64 bytes crus inatos focais de envios pesados lidos nativamente).

Replay Attack classico inclemente (Ataque de Repeticao Cega) massivo sobre o Barramento automotivo CAN padronizado base nua limita?
?
Grava-se via candump todas as msgs can do carro na acao cega amarrativa (Ex: trancar porta via chave wireless). O hacker apos a gravaco roda um canplayer re-atirando o trafego estático purista gravado natio limite logico no bus OBD, destrancando a porta passivamente puriste.
