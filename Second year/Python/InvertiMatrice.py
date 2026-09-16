def inverti(matrice):
    inverso = []
    for i in range(len(matrice)):
       riga = []
       for j in range(len(matrice[i])-1, -1, -1):
          riga.append(matrice[j][i])
       inverso.append(riga)
    stampa(inverso)

def stampa(matrice):
    for riga in matrice:
        print(*riga)
    print("\n")

matrice = []
for i in range(3):
    a = []
    for j in range(3):
        a.append(int(input()))
    matrice.append(a)

stampa(matrice)
inverti(matrice)
