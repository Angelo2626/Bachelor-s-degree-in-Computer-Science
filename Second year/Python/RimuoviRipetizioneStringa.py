def rimuovi_ripetizione(stringa):
    risultato = ""
    for char in stringa:
        if char not in risultato:
            risultato += char
    return risultato

stringa = input()
print(rimuovi_ripetizione(stringa))

