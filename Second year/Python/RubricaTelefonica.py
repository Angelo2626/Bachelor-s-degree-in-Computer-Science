def aggiornamento(dizionario, numero, nome):
    if nome in dizionario:
        print("Il contatto è già presente in rubrica.")
    else:
        dizionario[nome] = numero
    return dizionario

def ricercacontatto(dizionario):
    nome = input("Che contatto stai cercando? ").capitalize()
    if nome in dizionario:
        print("Il numero di", nome, "è ", dizionario[nome])
    else:
        print("Il contatto non esiste.")

def aggiornacontatto(dizionario):
    nome = input("Che contatto vuoi aggiornare? ")
    if nome in dizionario:
        numero = int(input("Inserisci il nuovo numero di telefono: "))
        dizionario[nome] = numero
        print(f"Il nuovo contatto di", nome, "è ", dizionario[nome])
    else:
        print("Il contatto non esiste.")

    return dizionario

dizionario = {}
while True:
    valid = int(input("Premere 0 per aggiornare la rubrica, 1 per ricercare un contatto, 2 per aggiornare un contatto. Se non si vuole svolgere nessuna operazione premere 3: "))
    if valid == 0:
        numero = input("Inserisci un numero di telefono (10 cifre): ")
        if len(numero) != 10:
            numero = input("Il numero deve contenere esattamente 10 cifre, reinseriscilo: ")
        numero = int(numero)
        nome = input("Inserisci il nome del contatto: ").capitalize()
        dizionario = aggiornamento(dizionario, numero, nome)
        print("La rubrica aggiornata è: ", dizionario)
    elif valid == 1:
        ricercacontatto(dizionario)
    elif valid == 2:
        dizionario = aggiornacontatto(dizionario)
        print(dizionario)
    elif valid == 3:
        break

