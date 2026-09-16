ContoCaratteri = 0
ContoParole = 0
ContoRighe = 0
ContoVocali = 0
vocali = "aeiouAEIOU"

with open("testo.txt", "r") as file:
    Contenuto = file.read()
    for char in Contenuto:
        if char.isalpha():
            ContoCaratteri += 1
    for char in Contenuto:
        if char.isalpha() and char in vocali:
            ContoVocali += 1

with open("testo.txt", "r") as file:
    ListaRighe = file.readlines()
    ContoRighe = len(ListaRighe)
    for i in range(len(ListaRighe)):
        ListaParole = ListaRighe[i].split()
        ContoParole += len(ListaParole)

with open("statistiche.txt", "a") as file:
    file.write("Numero di caratteri: " + str(ContoCaratteri) + "\nNumero di parole: " + str(ContoParole) + "\nNumero di righe: " + str(ContoRighe) + "\nContoVocali: " + str(ContoVocali))
