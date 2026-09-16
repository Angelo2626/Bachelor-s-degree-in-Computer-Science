def aggiungi_utente(file, nome, età):
    file.write(f"{nome.capitalize()}: {età}\n")

def maggiorenne(dizionario):
    for key, value in dizionario.items():
        if value >= 18:
            print(f"{key}: {value}")

dizionario = {}
while True:
    while True:
        flag = input("Inserisci 1 per aggiungere una utenti, 2 per leggere la lista di utenti, 3 per leggere una lista con le persone maggiorenni. Premere 0 per chiudere il programma: ")
        if flag == "1" or flag == "2" or flag == "3" or flag == "0":
            break
        else:
            print("Devi inserire un valore valido: 1 (aggiungi utente), 2 (leggi lista utenti), 3 (leggi lista maggiorenni), 0 (termina il programma)")
    if flag == "0":
        break
    elif flag == "1":
        nome = input("Inserisci il nome: ")
        età = int(input("Inserisci l'età: "))
        dizionario.update({nome: età})
        with open("utenti.txt", "a") as file:
            aggiungi_utente(file, nome, età)
    elif flag == "2":
        print(dizionario)
    elif flag == "3":
        maggiorenne(dizionario)

