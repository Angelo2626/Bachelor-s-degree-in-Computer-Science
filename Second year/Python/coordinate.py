import numpy as np
import math

coordinate = np.random.randint(0, 11, (20, 2))
n = len(coordinate)

md = [[0]*n for _ in range(n)]

massimo = float('-inf')
minimo = float('inf')
cmax = ()
cmin = ()

for i in range(n):
    for x in range(n):
        if i != x:
            numero = round(math.dist(coordinate[i], coordinate[x]), 2)
            md[i][x] = numero

            if numero > massimo:
                massimo = numero
                cmax = (coordinate[i], coordinate[x])

            if numero < minimo:
                minimo = numero
                cmin = (coordinate[i], coordinate[x])

for riga in md:
    print(*riga)

print(f"\nI punti con la distanza euclidea minore sono {cmin} con distanza {minimo}")
print(f"I punti con la distanza euclidea massima sono {cmax} con distanza {massimo}")
