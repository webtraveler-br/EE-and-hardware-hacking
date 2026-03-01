# HH 0.3 - Python para Hardware Hacking

struct.pack e struct.unpack: para que servem?
?
Empacotar/desempacotar dados binarios. `struct.pack('>I', 0xDEADBEEF)` → 4 bytes big-endian. `struct.unpack('<I', data)` → inteiro little-endian.

Operacoes bitwise essenciais?
?
`&` (AND — mascarar bits), `|` (OR — setar bits), `^` (XOR — toggle/crypto), `<<` / `>>` (shift — multiplicar/dividir por 2).

Como abrir arquivo binario em Python?
?
`with open('firmware.bin', 'rb') as f: data = f.read()`. Usar 'rb' para leitura binaria, 'wb' para escrita.

int.from_bytes: como usar?
?
`int.from_bytes(b'\xDE\xAD', 'big')` → 0xDEAD. Converte bytes para inteiro, especificando endianness.

Para que serve pyserial?
?
Comunicacao serial com dispositivos. `serial.Serial('/dev/ttyUSB0', 115200)` abre porta. `.read()`, `.write()` para enviar/receber dados.

Como formatar valor em hexadecimal?
?
`hex(255)` → '0xff'. `f'{255:08x}'` → '000000ff' (8 digitos com padding). `bytes.fromhex('DEADBEEF')` → bytes.
