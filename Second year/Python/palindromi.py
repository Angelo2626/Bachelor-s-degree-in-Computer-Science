def palindromi(stringa):
    p = ""
    for parola in stringa.split():
        if parola.lower() == parola.lower()[::-1]:
            p += parola
            p += " "
    with open("palindromi.txt", "w") as file:
        file.write(p)

stringa = input("Inserisci una frase: ")
palindromi(stringa)
