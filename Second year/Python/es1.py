def RimuoviZero(matrice):
    NuovaMatrice = []
    for i in matrice:
        if i[0] != 0:
            NuovaMatrice.append(i)
    return NuovaMatrice

matrice = []
righe = int(input())
colonne = int(input())

for i in range(righe):
    riga = []
    for j in range(colonne):
        riga.append(int(input()))
    matrice.append(riga)

matrice = RimuoviZero(matrice)
righe = len(matrice)
colonne = len(matrice[0])
print(righe,"X",colonne)
for riga in matrice:
    print(*riga)
