n = 5

def sommarighe(matrice):
    somma = 0
    flag = 0
    for i in range(n):
        sr = 0
        for j in range(n):
            sr = sr + matrice[i][j]
        if i == 0:
            somma = sr
        if somma != sr:
            flag += 1
    return flag, sr

def sommacolonne(matrice):
    somma = 0
    flag = 0
    for i in range(n):
        sc = 0
        for j in range(n):
            sc = sc + matrice[j][i]
        if i == 0:
            somma = sc
        if somma != sc:
            flag += 1
    return flag, sc

def sommadiagonali(matrice):
    sd2 = 0
    sd1 = 0
    flag = 0
    for i in range(n):
        for j in range(n):
            if i == j:
                sd1 = sd1 + matrice[i][j]
            if i + j == n - 1:
                sd2 = sd2 + matrice[i][j]
    if sd1 != sd2:
        flag += 1
    return flag, sd1

def magica(matrice):
    fc, sc = sommacolonne(matrice)
    fr, sr = sommarighe(matrice)
    fd, sd = sommadiagonali(matrice)

    if fc == fr == fd == 0 and sc == sr == sd:
        print("Matrice magica.")
    else:
        print("Matrice non magica.")

import random
matrice = []
for i in range(n):
    a = []
    for j in range(n):
        a.append(random.randint(1,9))
    matrice.append(a)

for riga in matrice:
    print(*riga)

magica(matrice)
