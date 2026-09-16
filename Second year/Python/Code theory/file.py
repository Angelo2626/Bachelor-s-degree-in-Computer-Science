# apertura
file = open(r"C:\Users\Holegard_XXXVI\Desktop\Python\Teoria\numeri.txt", 'r') # apre il file in modalità lettura
contenuto = file.read() # legge tutto il contenuto del file
print(contenuto)
file.close() # chiude il file

print("\nLETTURA RIGA PER RIGA")
file = open(r"C:\Users\Holegard_XXXVI\Desktop\Python\Teoria\numeri.txt", 'r')
for riga in file:
    print(f"Nuova riga: {riga.strip()}")
file.close()

# blocco with per chiudere automaticamente il file
print("\nBLOCCO WITH")
with open(r"C:\Users\Holegard_XXXVI\Desktop\Python\Teoria\numeri.txt", 'r') as file:
    print(file.read())

# scrittura su file
with open('output.txt', 'w') as file: # con 'w' si crea o si sovrascrive un file
    file.write("Questa e' la prima riga\n")
    file.write("Questa e' la seconda riga\n")

with open('output.txt', 'a') as file: # con 'a' si aggiunge una nuova riga
    file.write("Questa riga e' stata aggiunta\n")

# scrivere una lista di righe
righe = ["Prima riga\n", "Seconda riga\n", "Terza riga\n", "Quarta riga\n"]
with open('output.txt', 'w') as file:
    file.writelines(righe)

##########################################################################
##########################################################################

# FILE CSV
import csv

# scrittura di un file csv
dati = [
    ["nome", "anni", "comune"],
    ["Alessio", 23, "Cerignola"],
    ["Gaia", 22, "Verona"]
]

with open('nuovi_studenti.csv', 'w', newline="") as file:
    scrittore_csv = csv.writer(file)
    scrittore_csv.writerows(dati)

# lettura di un file csv
with open('nuovi_studenti.csv', 'r') as file:
    lettore_csv = csv.reader(file)
    for riga in lettore_csv:
        print(riga)


#######################################################
#######################################################

# JSON
import json

# scrittura di dati
dati = {
    "nome": "Fabrizio",
    "anni": 18,
    "comune": "Civitavecchia"
}

with open('dati.json', 'w') as file:
    json.dump(dati, file) # scrive il dizionario come file JSON

# lettura di dati
with open('dati.json', 'r') as file:
    print(json.load(file))

###############################################################
###############################################################

# PICKLE
import pickle

# scrittura di dati
dati = {
    "nome": "Filippo",
    "anni": 43,
    "comune": "San Giovanni Rotondo"
}

with open('dati.pkl', 'wb') as file:
    pickle.dump(dati, file)

# lettura di dati

with open('dati.pkl', 'rb') as file:
    print(pickle.load(file))