righe = int(input())
colonne = int(input())
matrice = []

for i in range(righe):
    riga = []
    for j in range(colonne):
        riga.append(int(input()))
    matrice.append(riga)

for i in range(righe):
    somma = 0
    for j in range(colonne):
        somma += matrice[i][j]
    print("la somma della riga", i, "è", somma)

pos = (0,0)
max_val = 0

for i in range(righe):
    for j in range(colonne):
        if matrice[i][j] > max_val:
            max_val = matrice[i][j]
            pos = (i,j)
