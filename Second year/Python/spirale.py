def stampa(matrice):
    for riga in matrice:
        print(*riga)

def spirale(matrice, n):
    indice = 1
    alto, basso, destra, sinistra = 0, n-1, n-1, 0
    while indice <= n*n:
        for i in range(sinistra, destra+1, +1):
            matrice[alto][i] = indice
            indice += 1
        alto += 1
        for i in range(alto, basso+1, +1):
            matrice[i][destra] = indice
            indice += 1
        destra -= 1
        for i in range(destra, sinistra-1, -1):
            matrice[basso][i] = indice
            indice += 1
        basso -= 1
        for i in range(basso, alto-1, -1):
            matrice[i][sinistra] = indice
            indice += 1
        sinistra +=1
    stampa(matrice)

matrice = []
n = int(input("Inserisci la dimensione della matrice: "))

for i in range(n):
    a = []
    for j in range(n):
        a.append(0)
    matrice.append(a)

stampa(matrice)
print("\n")
spirale(matrice, n)
