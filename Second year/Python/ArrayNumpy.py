import numpy as np
righe = 3
colonne = 2
a = np.array([[int(input()) for i in range (righe)] for j in range (colonne)])
for riga in a:
    print(*riga)
