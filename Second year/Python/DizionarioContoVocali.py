def contavocali(stringa):
    dizionario = {}
    vocali = "AEIOUaeiou"
    for char in stringa:
        if char in vocali and char in dizionario:
            dizionario[char] += 1
        elif char in vocali and char not in dizionario:
            dizionario[char] = 1
    return dizionario

stringa = input()
print(contavocali(stringa))
