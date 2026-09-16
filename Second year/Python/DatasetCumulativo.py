import numpy as np
import pandas as pd
from matplotlib import pyplot as plt

ds = {
    "Colonna_1": np.random.randint(0, 101, (200)),
    "Colonna_2": np.random.randint(0, 101, (200)),
    "Colonna_3": np.random.randint(0, 101, (200)),
    "Colonna_4": np.random.randint(0, 101, (200))
}

df = pd.DataFrame(ds)
somma1 = df["Colonna_1"].cumsum()
somma2 = df["Colonna_2"].cumsum()
somma3 = df["Colonna_3"].cumsum()
somma4 = df["Colonna_4"].cumsum()

for i in range(200):
    if somma1[i] >= 500:
        print(f"La 1° colonna è quella con la somma cumulativa che supera per la prima volta quota 500, e lo fa all'indice {i}")
        break
    elif somma2[i] >= 500:
        print(f"La 2° colonna è quella con la somma cumulativa che supera per la prima volta quota 500, e lo fa all'indice {i}")
        break
    elif somma3[i] >= 500:
        print(f"La 3° colonna è quella con la somma cumulativa che supera per la prima volta quota 500, e lo fa all'indice {i}")
        break
    elif somma4[i] >= 500:
        print(f"La 4° colonna è quella con la somma cumulativa che supera per la prima volta quota 500, e lo fa all'indice {i}")
        break


plt.plot(somma1, label = "Colonna_1")
plt.plot(somma2, label = "Colonna_2")
plt.plot(somma3, label = "Colonna_3")
plt.plot(somma4, label = "Colonna_4")
plt.title("Somme cumulative")
plt.grid(True)
plt.show()
