import numpy as np

def prodotto_scalare(m1, m2):
    ps = np.sum(m1 * m2)
    print(f"Il prodotto scalare tra le due matrici è {ps}")

def autovalori(m1):
    aut = np.linalg.eigvals(m1)
    somma = aut.sum()
    print(f"La somma degli autovaori della matrice {m1} è {somma}")

def maxval(m2):
    massimo = m2.argmax()
    print(f"Il valore massimo si trova in posizione {massimo}")

m1 = np.random.uniform(0, 2, size=(100, 100))
m2 = np.random.uniform(0, 2, size=(100, 100))

prodotto_scalare(m1,m2)
autovalori(m1)
maxval(m2)
