# liste
numeri = [1, 2, 3]
parole = ["albero", "fagiolo", "cedrata"]
misti = [1, "albero", 3.14, True]
print(misti[2]) # parte da 0
print(numeri[-1]) # i numeri negativi accedono all'ultimo elemento
numeri.append(4) # inserisce un elemento alla fine della lista
numeri.insert(4, 0) # inserisce un elemento in una posizione specifica
print(numeri)
numeri.sort() # ordina gli elementi della lista
print(numeri)
numeri.reverse() # inverte i valori della lista
print(numeri)
numeri.pop() # rimuove l'ultimo elemento (va bene anche remove())
print(numeri)
print(numeri[0:2]) # slicing: prende i numeri dall'indice 0 a 1

# tuple
animali = ("cane", "gatto", "cavallo") # non possono essere modificate

# dizionari
studente = {
    "nome": "Alessio",
    "età": 22,
    "città": "Cerignola"
}
print(studente["nome"])
studente["corso"] = "Informatica" # aggiungere un nuovo elemento
print(studente)
studente.pop("città") # rimuove un elemento

for chiave in studente: # iterare sulle chiavi
    print(chiave)

for chiave, valore in studente.items(): # iterare su chiavi e valori
    print(f"{chiave}: {valore}")

print(studente.keys()) # restituisce le chiavi del dizionario
print(studente.values()) # restituisce i valori del dizionario
print(studente.items()) # restituisce tutte le coppie chiave - valore del dizionario
print(studente.get("nome")) # restituisce il valore di una chiave
print(studente.pop("età")) # elimina il valore associato a una chiave

# iterare su strutture dati complesse
studenti = [
    {"nome": "Gianluca", "anni": 21},
    {"nome": "Filippo", "anni": 18},
    {"nome": "Angelo", "anni": 34}
]

for studente in studenti:
    print(studente.items())

corsi = {
    "Matematica": ["Mario", "Luisa"],
    "Informatica": ["Alessio", "Francesco"]
}

for corso, studenti in corsi.items():
    print(f"Corso: {corso}")
    for studente in studenti:
        print(f" - {studente}")