import numpy as np

array = np.random.randint(1, 101, size=(50))

na = np.where((array % 2 == 0) & (array < 80) & (array > 30), array, -1)

media = np.mean(na)
mediana = np.median(na)
deviazione_standard = np.std(na)

print(array)
print(na)
print(f"La media del nuovo array è {media}, la mediana è {mediana} e la deviazione standard è {deviazione_standard}")
