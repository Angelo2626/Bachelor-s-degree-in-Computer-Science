#BUILT-IN
valore = a.count(valore)    #conta quante volte valore appare in una lista, tupla o stringa. Non va bene per set e dizionari
round(numero, x)            #approssima numero a x cifre decimali
a.capitalize()              #mette la prima lettera maiuscola
frase.title()               #metta la prima lettera maiuscola a tutte le parole della frase

#CICLI
for i in range(1, n+1):     #se n = 5, i va da 1 a 5
#
for i in range(10, 0, -1):  #for come in c
    print(i)

#SET
a = set()                   #creazione set

#NUMPY
import numpy as np

matrice = np.random.uniform(5, 10,(3, 3))   #crea una matrice 3x3 con numeri che vanno da 5 a 10
print(np.round(matrice, 2))                 #stampa la matrice con arrotondamento a 2 cifre decimali
matrice = np.random.randint(1, 10, size=(3, 3))  #crea una matrice 3x3 con numeri INTERI casuali che vanno da 1 a 9

sr = matrice.sum(axis = 1)                  #somma delle righe
sc = matrice.sum(axis = 0)                  #somma delle colonne
sd1 = np.trace(matrice)                     #somma della diagonale principale
sd2 = np.trace(np.fliplr(matrice))          #somma della diagonale opposta

nuovo_array = array[array > 20]                 #nel nuovo array ci saranno solo i valori in cui il valore di array è maggiore di 20
nuovo_array = np.where(array > 20, array, 0)    #where funziona così np.where(condizione, valore se condizione vera, valore se la condizione non è rispettata)

np.savetxt("immagine.txt", nuova_matrice, fmt="%d")     #salva in un file di testo chiamato immagine.txt il contenuto di nuova matrice nel formato intero (%d come in c)

cumulativa = np.cumsum(arr)             #somma cumulativa

#PANDAS
import pandas as pd
df = pd.read_csv('Titanic-Original.csv')                                #salva il dataset in una variabile chiamata df
df = pd.read.csv('Titanic-Original.csv', index_col = 'PassengerId')     #mette la colonna PassengerId come colonna indice

df.index        #elenca tutti gli indici delle righe
df.columns      #elenca tutti i nomi delle colonne eccetto quella indice
df.shape        #mostra il numero totale di righe e colonne

df.head()       #restituisce le prime 5 righe del dataset (possiamo anche decidere noi l'indice)
df.tail()       #restituisce le ultime 5 righe del dataset (possiamo anche decidere noi l'indice)

df.info()       #stampa informazioni sul dataframe

df = pd.read.csv('Titanic-Original.csv', index_col = 'PassengerId', dtype = {'Name': pd.StringDtype()})     #assegno alla colonna name il tipo stringa

df.max()        #restituisce il valore massimo della colonna
df.min()        #restituisce il valore minimo della colonna

df.sum()                        #somma tutte le colonne
df.sum(numeric_only = True)     #somma solo le colonne con valori numerici
df.mean()                       #media della colonna
df.describe()                   #descrizione statistica della colonna
df.isna()                       #dove il valore è Nan

df.sort_values('Fare')                          #Ordina la colonna in modo crescente
df.sort_values('Fare', ascending = False)       #Ordine decrescente
df.dropna()                         #elimina le righe con valori nulli
df.dropna(subset = 'Ages')          #elimina solo le colonne indicate che contengono valori nulli

df.plot()           #chiama un grafico a righe
df['Age']           #restituisce i valori della colonna età (è una serie)

df.loc[2]                                       #ci restituisce la relativa riga (il 2 è casuale)
df.loc[2, 'Age']                                #ci restituisce il valore su quella colonna
df['Age'].plot                                  #fa il grafico sulla colonna Age
df['Age'].plot(kind = 'hist')                   #istogramma
df['Age'].value_counts()                        #conta quante righe possiedono un dato valore
df['Age'].value_counts().plot(kind = 'pie')
df.goupby('Pclass').get_group(1)                #crea dei gruppo di righe per ogni classe

df_filtrato = df[df['Age'] == 74]                           #condizione per filtrare il dataframe (l'operatore può cambiare)
de_filtrato.to_csv("output_filtrato.csv", index = False)    #salva in un nuovo file csv il dataframe, index = false evita di salvare l'indice del Dataframe come colonna nel csv

df["media"] = df.mean(axis = 1, numeric_only = True)        # crea una nuova colonna chiamata media, e fa la media riga per riga ignorando i valori non numerici
df.cumsum()                                     #somma cumulativa
#GESTIONE ERRORI
try:                                                #blocco di codice che voglio provare ad eseguire
    # codice che potrebbe dare errore
except TipoErrore:                                  #blocco di codice che viene eseguito se si verifica l'errore specificato
    # cosa fare se si verifica quell'errore
