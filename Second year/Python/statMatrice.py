import numpy as np

def stampa(matrice):
    for riga in matrice:
        print(*riga)
    print("\n")

def valore_assoluto(matrice):
    for i in range(len(matrice)):
        for j in range(len(matrice[i])):
            if matrice[i][j] < 0:
                matrice[i][j] = -matrice[i][j]
    stampa(matrice)
    return matrice

def normalizzazione(matrice):
    matrice_normalizzata = (matrice - matrice.mean()) / matrice.std()
    stampa(matrice_normalizzata)

def correlazione(matrice):
    correlazione = np.corrcoef(matrice, rowvar=False)
    stampa(correlazione)

matrice = np.random.randint(-10, 11, (50, 50))

stampa(matrice)
matrice = valore_assoluto(matrice)
normalizzazione(matrice)
correlazione(matrice)
