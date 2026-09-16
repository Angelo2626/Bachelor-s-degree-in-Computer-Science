import numpy as np

def somma_righe_colonne(matrice):
    sr = matrice.sum(axis = 1)
    sc = matrice.sum(axis = 0)
    return sr, sc

def somma_diagonali(matrice):
    sd1 = np.trace(matrice)
    sd2 = np.trace(np.fliplr(matrice))
    return sd1, sd2

matrice = np.random.randint(1, 10, size=(3, 3))
for riga in matrice:
    print(np.round(riga, 2))

sr, sc = somma_righe_colonne(matrice)
sd1, sd2 = somma_diagonali(matrice)
if np.all(sr == sr[0]) and np.all(sc == sc[0]) and sr[0] == sc[0] == sd1 == sd2:
    print("E' un quadrato magico.")
else:
    print("Non è un quadrato magico")
