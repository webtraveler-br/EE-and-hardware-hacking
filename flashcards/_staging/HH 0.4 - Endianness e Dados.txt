# HH 0.4 - Endianness e Representacao de Dados

Big-endian vs Little-endian?
?
Big-endian: MSB primeiro. 0x12345678 → 12 34 56 78. Usado em protocolos de rede (TCP/IP). Little-endian: LSB primeiro → 78 56 34 12. Usado em x86, ARM.

Se eu leio 'EF BE AD DE' num dump, qual o valor?
?
Em little-endian: 0xDEADBEEF. Em big-endian: 0xEFBEADDE. Verificar a arquitetura do target.

Potencias de 2 essenciais?
?
$2^{10} = 1024$ (1K), $2^{16} = 65536$ (64K), $2^{20} \approx 1M$, $2^{32} \approx 4G$.

Tamanhos de dados?
?
byte = 8 bits, word = 16 ou 32 bits (depende da arch!), dword = 32 bits, qword = 64 bits.

0x10 em decimal?
?
16 (nao 10!). Cada digito hex vale 16x o anterior. 0x100 = 256, 0x1000 = 4096.

Null-terminated string?
?
String terminada por byte 0x00. Padrao em C. Ao analisar hex dump, strings terminam quando aparece \x00.

Como identificar strings em hex dump?
?
Sequencias de bytes entre 0x20-0x7E sao ASCII imprimivel. `strings firmware.bin` extrai automaticamente.

Magic bytes comuns em firmware?
?
27 05 19 56 = U-Boot image header. 68 73 71 73 = squashfs. 1F 8B = gzip. 89 50 4E 47 = PNG.
