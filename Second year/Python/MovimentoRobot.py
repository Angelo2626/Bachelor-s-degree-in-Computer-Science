dati = {}

with open("esempio1.txt", "r") as f:
    for riga in f:
        riga = riga.strip()
        chiave, valore = riga.split()
        valore = int(valore)
        # sommiamo direttamente
        if chiave in dati:
            dati[chiave] += valore
        else:
            dati[chiave] = valore

print(dati)

mx = {}
my = {}

if dati["N"] > dati["S"]:
    my.update({"N": dati["N"] - dati["S"]})
elif dati["N"] < dati["S"]:
    my.update({"S": dati["S"] - dati["N"]})
else:
    my.update({"Fermo": 0})

if dati["E"] > dati["O"]:
    mx.update({"E": dati["E"] - dati["O"]})
elif dati["E"] < dati["O"]:
    mx.update({"O": dati["O"] - dati["E"]})
else:
    mx.update({"Fermo": 0})

for direzione, valore in my.items():
    if valore == 0:
        print("Il robot non si è mosso sull'asse verticale")
    else:
        print(f"{valore} metri in direzione {direzione}")

for direzione, valore in mx.items():
    if valore == 0:
        print("Il robot non si è mosso sull'asse orizzontale")
    else:
        print(f"{valore} metri in direzione {direzione}")
