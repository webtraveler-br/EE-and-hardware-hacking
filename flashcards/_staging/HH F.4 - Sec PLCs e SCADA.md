# HH F.4 - Seguranca em PLCs e SCADA

Risco central no Hardware de Controladores CLP (PLC security)?
?
A vasta maioria dos CLPs industriais legados foram construidos sem nenhuma preocupação com autenticacao ou criptografia nas instrucoes recebidas pelas redes RS485. Confiam cegamente em qualquer pacote.

Ataque na Interface Humano-Maquina (HMI-Ataque)?
?
Ao comprometer a IHM (tela touch que o operador usa), o invasor engana o operador mostrando dados falsos de pressao normal, enquanto envia comandos destrutivos silenciosos de abertura de valvulas para a rede do CLP.

Engenharia Reversa no Projeto de Software Ladder?
?
Extrair o programa compilado da memoria do CLP. Apos identificar a logica (ex: descobrir que o contato Q0.4 liga a esteira transportadora), o invasor cria scripts exatos para ativar ou desativar portas especificas da fabrica.

Ataque Replay e Injeçao de rede (Modbus)?
?
Durante a intercepcao da rede com Wireshark, o invasor captura um pacote legitimo do engenheiro. Depois, ele reenvia infinitamente esse mesmo pacote capturado (ex: comando de abrir portao) e a rede legitima o disparo.

Ausencia de Assinatura de Firmware nos CLPs legados?
?
Atacantes sofisticados usam vulnerabilidades para injetar um firmware maligno personalizado direto na eeprom do CLP. A placa aceita o sistema operacional zumbi sem checar assinaturas criptograficas nativas de hardware.
