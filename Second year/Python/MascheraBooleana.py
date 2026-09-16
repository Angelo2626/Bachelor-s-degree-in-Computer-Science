import numpy as np
def stampa(matrice):
    for riga in matrice:
        print(*riga)
    print("\n")

def sostituzione(matrice):
    nuovo_array = np.where((3 % matrice == 0) | (5 % matrice == 0), -1, matrice)
    stampa(nuovo_array)
    media(nuovo_array)

def media(matrice):
    print(f"La media dei valori diversi da -1 della matrice è {np.mean(matrice[matrice != -1])}")

matrice = np.random.randint(1, 101, (10, 10))
stampa(matrice)
sostituzione(matrice)

