# HH 5.2 - Analise Estatica com Ghidra

Ghidra workflow basico?
?
Criar projeto → importar binario → selecionar arquitetura (ARM LE 32-bit para maioria IoT) → auto-analysis → explorar.

Janelas essenciais do Ghidra?
?
Decompiler (pseudocodigo C), Listing (assembly), Symbol Tree (funcoes/variaveis), Cross References (quem chama/e chamado por).

Funcoes perigosas a procurar no firmware?
?
`system()` (command injection), `strcpy()` / `sprintf()` / `gets()` (buffer overflow — copiam sem verificar tamanho).

Como encontrar credenciais hardcoded no Ghidra?
?
Window → Defined Strings → filtrar por "password", "admin", "key", "secret". Clicar na string → ver cross-references para encontrar a funcao que a usa.

Como a IA ajuda na analise de firmware?
?
Colar pseudocodigo C do Ghidra e pedir analise de vulnerabilidades. Util para entender funcoes complexas. Sempre validar — IA pode errar em detalhes de arquitetura.
