righe = int(input())
colonne = int(input())
matrice = []

for i in range(righe):
    riga = []
    for j in range(colonne):
        riga.append(int(input()))
    matrice.append(riga)

for riga in matrice:
    print(*riga)
