# HH D.3 - Intel SGX e Enclaves

Intel SGX (Software Guard Extensions): isolamento?
?
Protege pedacos de memoria executavel via criptografia ("enclaves") a ponto do proprio sistema operacional hospedeiro, ring-0, Hypervisor e root nao conseguirem ler.

Limitacao fundamental contornada pelos atacantes em SGX?
?
Acesso isolado pode ser vazado lateralmente usando Cache Side-Channel na CPU, extraindo especulativamente os segredos mesmo com criptografia perfeita do hardware (Spectre-like delays).

Plundervolt: ataque contra SGX?
?
Utiliza as MSR (Model-Specific Registers) do proprio processador intel hospedeiro para temporariamente baixar a voltagem abaixo do sustentavel (Glitch dinamico via software) forcando SGX a pular branches.

TrustZone vs SGX: estrutura comparativa?
?
TrustZone corta o mundo em "2" lados por limitacao de HW e bus. Intel SGX trabalha com "containers/enclaves" baseados na propria task da RAM, virtualizando MÚLTIPLOS cofres independentes de seguranca local.

AMD SEV (Secure Encrypted Virtualization)?
?
Aborda Confidential Computing encriptando as paginas da memoria inteira da VM. Previne que o administrador do data center leia dados crus nas maquinas locadas na cloud hosteada.
