import numpy as np
import pandas as pd
import string
import random

def nome_casuale(lunghezza=5):
    return ''.join(random.choices(string.ascii_uppercase, k=lunghezza))

def stipendio_orario_medio(df):
    df.groupby("Dipartimento")
    media = df.groupby("Dipartimento").apply(lambda x: (x["Stipendio"] / x["Ore_lavorate"]).mean())
    print(media)
    varianza(media)

def varianza(media):
    v = np.var(media)
    print(f"La varianza massima è: {v.max()}")
reparti = ["IT", "HR", "Finanza", "Marketing"]
ds = {
    "Nome" : [nome_casuale() for _ in range(200)],
    "Età" : np.random.randint(20, 61, (200)),
    "Dipartimento" : [random.choice(reparti) for _ in range(200)],
    "Stipendio" : np.random.randint(1000, 5001, (200)),
    "Ore_lavorate" : np.random.randint(20, 51, (200))
}

df = pd.DataFrame(ds)

print(df)
stipendio_orario_medio(df)
