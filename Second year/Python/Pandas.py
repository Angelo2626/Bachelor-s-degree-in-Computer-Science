import pandas as pd

file_path = "dataset1.csv"
colonne_richieste = ["Nome", "Età", "Salario"]

try:
    df = pd.read_csv(file_path)

    for col in colonne_richieste:
        if col not in df.columns:
            raise KeyError(f"Colonna mancante: {col}")

except FileNotFoundError:
    print(f"Errore: il file '{file_path}' non esiste.")
except KeyError as e:
    print(f"Errore: {e}")
else:
    print("File letto correttamente!")
    print(df.head())

df_filtrato = df[(df["Età"] > 30) & (df["Salario"] > 35000)]
df_filtrato.to_csv("output_filtrato.csv", index = False)
