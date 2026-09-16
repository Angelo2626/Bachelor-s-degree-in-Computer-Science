def MatriceTrasposta(matrice):
    NuovaMatrice = []
    for i in range(len(matrice)):
        NuovaRiga = []
        for j in range(len(matrice[0])):
            NuovaRiga.append(matrice[j][i])
        NuovaMatrice.append(NuovaRiga)
    return NuovaMatrice


righe = int(input())
colonne = int(input())
matrice = []

for i in range(righe):
    riga = []
    for j in range(colonne):
        riga.append(int(input()))
    matrice.append(riga)

matrice = MatriceTrasposta(matrice)

print("Matrice trasposta:\n")
for riga in matrice:
    print(*riga)
