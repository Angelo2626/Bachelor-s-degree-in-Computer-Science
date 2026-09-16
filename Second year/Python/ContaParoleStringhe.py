def ContaParole(stringa):
    conto = stringa.split()
    return len(conto)

def ContaVocali(stringa):
    lista = stringa.split()
    vocali = "aeiouAEIOU"
    count = 0
    global_count = 0
    for parola in lista:
        for char in parola:
            if char in vocali:
                count += 1
        if count >= 3:
            global_count += 1
            count = 0
        else:
            count = 0

    return global_count

def longest_word(stringa):
    lista = stringa.split()
    max = ""
    count = 0
    for parola in lista:
        for i in range(len(parola)):
            count +=1
        if count > len(max):
            max = parola
            count = 0
        else:
            count = 0
    return max

stringa = input()
count = ContaParole(stringa)
print("Numero di parole:", count)
count_vocali = ContaVocali(stringa)
print("Parole con più di 3 vocali:", count_vocali)
print("Parola più lunga:", longest_word(stringa))
