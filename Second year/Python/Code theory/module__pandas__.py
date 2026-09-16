# INTRODUZIONE
import pandas as pd

ds = { # creazione di un dataset
    'Mesi': ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio'],
    'Giorni': [31, 28, 31, 30, 31]
}

df = pd.DataFrame(ds) # creazione di un dataframe

print(df)
print(df.info()) # restituisce più informazioni sul dataframe

######################################################
######################################################
######################################################

# SERIE DI DATI
import pandas as pd

""" Ogni colonna del dataframe è una serie di dati (una lista) """

lista = [10, 15, 20]
serie_pandas = pd.Series(lista) # creazione di una serie proveniente da una lista
print(serie_pandas) # la lista diventa una colonna per un dataframe

serie_pandas = pd.Series(lista, index = ["5x2", "5x3", "5x4"]) # modifica delle label
""" Una label è un indice """
print(serie_pandas)
print(serie_pandas["5x3"]) # ricerca di un elemento dalla label

lista = {"a": 1, "b": 2, "c": 3} # creazione di una serie con un dict
serie_pandas = pd.Series(lista)
print(serie_pandas)

######################################################
######################################################
######################################################

# APRIRE FILE CSV E JSON
import pandas as pd

df_csv = pd.read_csv('Quotazioni_Fantacalcio.csv') # apertura file csv
print(df_csv)

df_json = pd.read_json('Quotazioni.json') # apertura file json
print(df_json)

df.to_csv("Quotazioni_Fantacalcio.csv", # Sovrascrive il file originale
          index=False) # Non mette l'indice di riga

######################################################
######################################################
######################################################

# SELEZIONARE DATI DA DATAFRAME
import pandas as pd

df = pd.read_csv('Quotazioni_Fantacalcio.csv')

print(df[0:3]) # con uno slicing si possono prendere dei dati

print(df.head()) # prende le prime 5 righe
print(df.tail()) # prende le ultime 5 righe

print(df["Squadra"]) # prende la colonna con il nome "squadra"
print(df[["Squadra", "Nome"]]) # prende più colonne

print(df["Squadra"][0:5]) # si possono concatenare le richieste

######################################################
######################################################
######################################################

# LOC VS ILOC --> Localization vs Index Localization
import pandas as pd

df = pd.read_csv('Quotazioni_Fantacalcio.csv', index_col = "Nome")
""" 'Nome' diventa la colonna principale """

print(df.loc["Maignan"]) # prende tutta la riga "Maignan"

print(df.loc["Maignan", "Squadra"]) # prende la cella

print(df.iloc[0])
""" Con iloc invece si prendono le righe con gli indici """

######################################################
######################################################
######################################################

# ITERARE DATAFRAME
import pandas as pd

df = pd.read_csv('Quotazioni_Fantacalcio.csv').head()

for key, value in df.items(): # iterazione chiave - valore
    print(key, value)

for index, row in df.iterrows(): # iterazione indice - riga
    print(index, row)

for row in df.itertuples(): # iterazione sotto forma di tupla
    print(row)

""" L'iterazione crea una copia del df, non serve modificare i dati """

######################################################
######################################################
######################################################

# ORDINARE DATAFRAME
import pandas as pd

df = pd.read_csv('Quotazioni_Fantacalcio.csv')

df_ordinato = df.sort_index() # ordina per indice (di default è dall'indice più piccolo)
df_ordinato = df.sort_index(ascending = False) # ordina dall'indice più grande
print(df_ordinato.head(3))

df_ordinato = df.sort_values(by = "Nome") # ordina per colonna
print(df_ordinato[["Nome", "Squadra"]].head())

df_portieri = df[df["R"] == "P"].sort_values(by=["Qt.A", "Nome"], ascending = [False, True])
print(df_portieri[["Nome", "Squadra", "Qt.A"]])
""" Prende solo la lista dei portieri e li ordina dalla quotazione più alta in giù,
    Quelli con la stessa quotazione vengono ordinati in base al nome """

######################################################
######################################################
######################################################

# AGGIUNGERE E RIMUOVERE RIGHE E COLONNE
import pandas as pd

df = pd.read_csv('Quotazioni_Fantacalcio.csv')

df["Nuova"] = "Valore" # Creazione di una nuova colonna
""" Si può fare anche con una lista di nuove colonne """
print(df)

df.insert(1, # indice della nuova colonna
          "New", # nome della nuova colonna
          "Dato") # dato
print(df)

df = df.assign(Altra = "qwerty") # altro metodo (si possono anche modificare i nomi)
print(df)

df.drop("New", axis = 1) # elimina una colonna (axis = 0 per la riga)

df.drop(["New", "Nuova", "Altra"], # nome della colonna (o della riga)
        inplace = True, # True --> modifica il dataframe
        axis = 1) # 0: elimina la riga <--> 1: elimina la colonna
print(df)

del df["Qt.I M"] # altro metodo

# df.pop("Qt.I M") --> altro metodo

df.to_csv("Quotazioni_Fantacalcio.csv", index=False)