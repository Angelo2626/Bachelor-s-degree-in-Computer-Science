import numpy as np
matrice = np.random.randint(0, 256, size=(50, 50))
nuova_matrice = np.where(matrice >= 128, 255, 0)

np.savetxt("immagine.txt", nuova_matrice, fmt="%d")
