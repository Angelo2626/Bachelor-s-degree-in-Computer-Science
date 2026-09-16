def stampa(matrice):
    for riga in matrice:
        print(*riga)

matrice = []
for i in range(5):
    a = []
    for j in range(5):
        a.append(int(input()))
    matrice.append(a)
stampa(matrice)
