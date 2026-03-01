# HH E.4 - Criptoanalise Proprietaria

Risco de Criptografia Obscura e Algoritmos Proprietarios (IoT)?
?
Fabricantes tentam ignorar criptografia pesada padrão AES optando por XORs de chaves fixas, substitution simples ou keys estáticas em ROM pra economizar RAM e CPU.

Identificacao visual base em firmwares invertidos no Ghidra?
?
Plugins (CryptoIdentifier/findcrypt) buscam por Strings ou arrays fixos que indicam Criptografia solida padronizada em Binários anonimizados/despojados.

Vulnerabilidade estrutural XOR simples com chave estatica?
?
Known-Plaintext Attack extrai inteiramente as seeds por que $C = M \oplus K \implies K = C \oplus M$ se apenas um byte for adivinhado pela natureza do pacote em clear.

Busca da S-Box AES tradicional no Dump Binário?
?
Ghidra pesquisa as contantes hexa globais conhecidas como (0x63, 0x7c, 0x77, 0x7b...): se nao localizar, o binario utiliza variacao proprietaria insegura.

Falha estrita em RC4 e Stream Ciphers Proprietarios Customizados na Inversao?
?
Se a derivacao de chaves for falha ou a Key estatica for puramente reutilizada, o Keystream criptografico do XOR irá inevitavelmente se repetir (Key Reuse), vazando o plaintext da sessao lida cruamente para atacantes passivos.

Identificacao as cegas de Hashes Obscuros (exotic digest algorithms) no Binário invertido de Hardware IoT?
?
Atacantes medem categoricamente a exata saida cravada do buffer em hex (Tamanho do Hash cego purista de checksum limitativo gerado na interrupcao). Array de 16 bytes puros indica parentela morta de falho MD5 primitivo lido.
