#!/usr/bin/env python3
import re
import os
import subprocess

def get_progress():
    try:
        with open("PROGRESS.md", "r") as f:
            lines = f.readlines()
    except Exception as e:
        print("Erro ao ler PROGRESS.md:", e)
        return

    current_module = None
    modules = {}

    for line in lines:
        if line.startswith("## "):
            current_module = line.strip().replace("## ", "").split('(~')[0].strip()
            modules[current_module] = {"total": 0, "done": 0}
        
        if current_module and line.strip().startswith('|') and ('[ ]' in line or '[x]' in line.lower()):
            if '| Status |' in line or '|:-' in line:
                continue
            
            modules[current_module]["total"] += 1
            if '[x]' in line.lower():
                modules[current_module]["done"] += 1

    print("\n" + "="*50)
    print(" ⚡ HARDWARE HACKING & EE - PROGRESSO ⚡")
    print("="*50 + "\n")
    
    total_all = 0
    done_all = 0

    for mod, data in modules.items():
        if data["total"] == 0:
            continue
        
        t = data["total"]
        d = data["done"]
        total_all += t
        done_all += d
        
        pct = (d / t) * 100
        bar_len = 20
        filled = int(bar_len * d / t)
        bar = "█" * filled + "░" * (bar_len - filled)
        
        print(f"{mod[:35]:<35} | {d:02d}/{t:02d} | {bar} {pct:.0f}%")

    if total_all > 0:
        pct_all = (done_all / total_all) * 100
        filled_all = int(40 * done_all / total_all)
        bar_all = "█" * filled_all + "░" * (40 - filled_all)
        print("\n" + "-"*50)
        print(f" TOTAL: {done_all:03d}/{total_all:03d} | {bar_all} {pct_all:.1f}%")
        print("-"*50 + "\n")
    else:
        print("Nenhum módulo encontrado.")
        return

    # Update the READMEs silently
    update_roadmaps_readme(modules)
    update_github_profile_readme(modules)

def update_roadmaps_readme(m):
    path = "README.md"
    if not os.path.exists(path): return
    
    with open(path, "r") as f:
        c = f.read()

    def replacer(node_id, title_part, done, total):
        nonlocal c
        pattern = rf'({node_id}\["{title_part}<br/>).*?(módulos.*?)(?="\])'
        new_val = rf'\g<1>{done}/{total} \g<2>'
        c = re.sub(pattern, new_val, c)

    m0 = m.get('Módulo 0 — Matemática e Física', {'done':0,'total':36})
    m1 = m.get('Módulo 1 — Circuitos DC e AC', {'done':0,'total':19})
    m2 = m.get('Módulo 2 — Eletrônica Analógica + RF', {'done':0,'total':19})
    m3 = m.get('Módulo 3 — Digital e Embarcados', {'done':0,'total':15})
    m4 = m.get('Módulo 4 — Eletrotécnica e Automação', {'done':0,'total':17})
    m5 = m.get('Módulo 5 — Controle e Sinais + DSP', {'done':0,'total':16})
    mL = m.get('Módulo 6 — Laboratório Real', {'done':0,'total':19})
    mHHB = m.get('Hardware Hacking — Básico', {'done':0,'total':38})
    mHHA = m.get('Hardware Hacking — Avançado', {'done':0,'total':25})

    replacer('P0', 'Módulo 0<br/>Matemática e Física', m0['done'], m0['total'])
    replacer('P1', 'Módulo 1<br/>Circuitos DC/AC', m1['done'], m1['total'])
    replacer('P2', r'Módulo 2<br/>Eletrônica \+ RF', m2['done'], m2['total'])
    replacer('P3', r'Módulo 3<br/>Digital \+ Embarcados', m3['done'], m3['total'])
    replacer('P4', r'Módulo 4<br/>Eletrotécnica \+ Potência', m4['done'], m4['total'])
    replacer('P5', r'Módulo 5<br/>Controle \+ DSP', m5['done'], m5['total'])
    replacer('LAB', 'Módulo 6<br/>Laboratório Real', mL['done'], mL['total'])
    replacer('HHB', 'HH Básico', mHHB['done'], mHHB['total'])
    replacer('HHA', 'HH Avançado', mHHA['done'], mHHA['total'])

    t_mod = m0['total']+m1['total']+m2['total']+m3['total']+m4['total']+m5['total']+mL['total']+mHHB['total']+mHHA['total']
    d_mod = m0['done']+m1['done']+m2['done']+m3['done']+m4['done']+m5['done']+mL['done']+mHHB['done']+mHHA['done']

    # Substitui badge Módulos
    c = re.sub(r'(badge/Módulos-).+?(-blueviolet)', rf'\g<1>{d_mod}%2F{t_mod}\g<2>', c)

    with open(path, "w") as f:
        f.write(c)

def update_github_profile_readme(m):
    cwd = "../webtraveler-br" if os.environ.get("GITHUB_ACTIONS") else "../github-profile"
    path = os.path.join(cwd, "README.md")
    if not os.path.exists(path):
        print(f"\n  >> (Aviso: {path} não encontrado, não foi possível atualizar o profile)")
        return
    
    with open(path, "r") as f:
        c = f.read()

    def replacer(node_id, title_part, done, total):
        nonlocal c
        pattern = rf'({node_id}\["{title_part}<br/>).*?(?="\])'
        new_val = rf'\g<1>{done}/{total}'
        c = re.sub(pattern, new_val, c)

    m0 = m.get('Módulo 0 — Matemática e Física', {'done':0,'total':36})
    m1 = m.get('Módulo 1 — Circuitos DC e AC', {'done':0,'total':19})
    m2 = m.get('Módulo 2 — Eletrônica Analógica + RF', {'done':0,'total':19})
    m3 = m.get('Módulo 3 — Digital e Embarcados', {'done':0,'total':15})
    m4 = m.get('Módulo 4 — Eletrotécnica e Automação', {'done':0,'total':17})
    m5 = m.get('Módulo 5 — Controle e Sinais + DSP', {'done':0,'total':16})
    mL = m.get('Módulo 6 — Laboratório Real', {'done':0,'total':19})
    mHHB = m.get('Hardware Hacking — Básico', {'done':0,'total':38})
    mHHA = m.get('Hardware Hacking — Avançado', {'done':0,'total':25})

    replacer('P0', 'Pilar 0<br/>Matemática e Física', m0['done'], m0['total'])
    replacer('P1', 'Pilar 1<br/>Circuitos', m1['done'], m1['total'])
    replacer('P2', r'Pilar 2<br/>Eletrônica \+ RF', m2['done'], m2['total'])
    replacer('P3', 'Pilar 3<br/>Digital', m3['done'], m3['total'])
    replacer('P4', 'Pilar 4<br/>Eletrotécnica', m4['done'], m4['total'])
    replacer('P5', r'Pilar 5<br/>Controle \+ DSP', m5['done'], m5['total'])
    replacer('LAB', 'Lab Real', mL['done'], mL['total'])
    replacer('HHB', 'HH Básico', mHHB['done'], mHHB['total'])
    replacer('HHA', 'HH Avançado', mHHA['done'], mHHA['total'])

    with open(path, "w") as f:
        f.write(c)
        
if __name__ == "__main__":
    get_progress()
