import numpy as np
import pandas as pd
from matplotlib import pyplot as plt

df = pd.read_csv("voti.csv")
print(df)

df["Media"] = df.mean(axis = 1, numeric_only = True)
print(df)


print(f"Il ragazzo con la media più alta è {df.loc[df["Media"].idxmax(), "Nome"]} con una media del {df["Media"].max()}")

colori = plt.cm.tab20(np.linspace(0, 1, len(df)))

plt.bar(df.index, df["Media"], color=colori)
plt.xticks(df.index, df["Nome"], rotation=45)  # usa i nomi come etichette
plt.ylabel("Media")
plt.title("Media dei voti per persona")
plt.show()
