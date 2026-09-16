import random

def creamatrice(matrice):
    r = int(input("Inserisci la dimensione delle righe: "))
    c = int(input("Inserisci la dimensione delle colonne: "))
    for i in range(r):
        a = []
        for j in range(c):
            a.append(random.randint(1,9))
        matrice.append(a)
    return matrice, r, c

def stampa(matrice):
    for riga in matrice:
        print(*riga)

def prodottomatriciale(m1, r1, c1, m2, r2, c2):
    if(c1 != r2):
        print("Le matrici non sono compatibili per svolgere un prodotto matriciale.")
    else:
        matrice = []
        for i in range(r1):
            a = []
            for j in range(c2):
                a.append(0)
            matrice.append(a)
        for i in range(r1):
            for j in range(c2):
                somma = 0
                for z in range(c1):
                    somma =+ (m1[i][z] * m2[z][j])
                matrice[i][j] = somma
        stampa(matrice)

m1 = []
m2 = []
m1, r1, c1 = creamatrice(m1)
m2, r2, c2 = creamatrice(m2)
stampa(m1)
print("\n")
stampa(m2)
print("\n")
prodottomatriciale(m1, r1, c1, m2, r2, c2)
