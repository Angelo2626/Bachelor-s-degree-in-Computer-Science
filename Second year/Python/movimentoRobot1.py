def robot(nome):
    movimento = {}
    with open(nome, "r") as file:
        for riga in file:
            riga = riga.strip()
            if not riga:
                continue
            parti = riga.split()
            direzione = parti[0]
            metri = int(parti[1])
            if direzione in movimento:
                movimento[direzione] += metri
            else:
                movimento[direzione] = metri
    return movimento

def crea_tabella(file_list):
    colonne = []
    max_lunghezza = 20

    for nome in file_list:
        mov = robot(nome)
        righe = []
        if mov.get("N",0) > mov.get("S",0):
            righe.append(f"{mov['N'] - mov.get('S',0)} METRI VERSO N |")
        elif mov.get("S",0) > mov.get("N",0):
            righe.append(f"{mov['S'] - mov.get('N',0)} METRI VERSO S |")
        if mov.get("E",0) > mov.get("O",0):
            righe.append(f"{mov['E'] - mov.get('O',0)} METRI VERSO E |")
        elif mov.get("O",0) > mov.get("E",0):
            righe.append(f"{mov['O'] - mov.get('E',0)} METRI VERSO O |")
        colonne.append(righe)

    max_righe = max(len(col) for col in colonne)

    for i in range(max_righe):
        riga = ""
        for col in colonne:
            if i < len(col):
                riga += col[i].ljust(max_lunghezza)
            else:
                riga += " ".ljust(max_lunghezza)
        print(riga)

    print("-" * max_lunghezza * len(colonne))

    riga_file = ""
    for nome in file_list:
        riga_file += nome.upper().ljust(max_lunghezza)
    print(riga_file)

l = ["esempio1.txt", "esempio2.txt", "esempio3.txt"]
crea_tabella(l)

