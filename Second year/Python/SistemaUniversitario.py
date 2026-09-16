import numpy as np
class Studente:
    def __init__(self, nome, cognome, matricola):
        self.nome = nome
        self.cognome = cognome
        self.matricola = matricola

studenti = {}
while True:
    v = int(input("Premere 1 per aggiungere uno studente, premere 2 per aggiungere un voto, premere 3 per calcolare la media dello studente, premere 4 per vedere lo studente con la media più alta. Premere 5 per vedere la lista degli studenti" \
    "Premere 0 per terminare il programma --> "))
    if v == 0:
        break
    elif v != 1 and v != 0 and v != 2 and v != 3 and v != 4 and v != 5:
        print("ERRORE! Devi: ")
    elif v == 1:
        nome = input("Inserisci il nome dello studente: ").capitalize()
        cognome = input("Inserisci il cognome dello studente: ").capitalize()
        while True:
            matricola = input("Inserisci il numero di matricola (5 cifre): ")
            if len(matricola) == 5 and matricola.isdigit():
                break
            else:
                print("Devi inserire il numero di matricola corretto.")
        studente = Studente(nome, cognome, matricola)
        if studente.matricola not in studenti:
            studenti[matricola] = {"studente": studente, "voti": [], "cfu": 0}
        else:
            print("Lo studente è già registrato!")
            break
    elif v == 2:
        while True:
            matricola = input("Inserisci il numero di matricola per inserire il voto (5 cifre): ")
            if len(matricola) == 5 and matricola.isdigit():
                break
            else:
                print("Devi inserire il numero di matricola corretto.")
        if studente.matricola in studenti:
            while True:
                voto = int(input("Inserisci un voto da 18 a 30: "))
                cfu = int(input("Inserisci il numero di cfu (gli esami vanno da 2 a 12 cfu): "))
                if (voto < 18 or voto > 30) or (cfu < 2 or cfu > 12):
                    print("Inserisci devi inserire valori corretti. ")
                else:
                    studenti[matricola]["voti"].append(voto)
                    studenti[matricola]["cfu"] += cfu
                    break
    elif v == 3:
        while True:
            matricola = input("Inserisci il numero di matricola per calcolare la media (5 cifre): ")
            if len(matricola) == 5 and matricola.isdigit():
                break
            else:
                print("Devi inserire il numero di matricola corretto.")

        voti = studenti[matricola]["voti"]
        if voti:
            media = np.mean(voti)
            stud = studenti[matricola]["studente"]
            print(f"La media di {stud.nome} {stud.cognome} è {media:.2f}")
        else:
            print("Lo studente non ha ancora voti registrati.")

    elif v == 4:
        massimo = -1
        studente_top = None
        matricola_top = None

        for matricola, info in studenti.items():
            if info["voti"]:  # controllo per evitare media su lista vuota
                media = np.mean(info["voti"])
                if media > massimo:
                    massimo = media
                    studente_top = info["studente"]
                    matricola_top = matricola

        if studente_top:
            print(f"Lo studente con la media più alta è {studente_top.nome} {studente_top.cognome} (matricola {matricola_top}) con la media di {massimo:.2f}")
        else:
            print("Nessuno studente ha voti registrati.")

    elif v == 5:
        print("\n")
        for matricola, info in studenti.items():
            studente = info["studente"]
            print(f"Matricola: {matricola}")
            print(f"Nome: {studente.nome} {studente.cognome}")
            print(f"Voti: {info['voti']}")
            print(f"CFU: {info['cfu']}")
            print("-" * 30)


