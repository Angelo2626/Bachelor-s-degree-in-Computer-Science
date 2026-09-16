def stampadizionario(stringa):
    lista = stringa.split()
    dizionario = {}
    for parola in lista:
        if parola in dizionario:
            dizionario[parola] += 1
        else:
            dizionario[parola] = 1
    return dizionario

def frequenza(dizionario):
    max_val = max(dizionario.values()) #valore massimo
    #trova la parola corrispondente al valore massimo
    for parola, count in dizionario.items():
        if count == max_val:
            return parola

stringa = input()
dizionario = stampadizionario(stringa)
print(dizionario)
print("Parola più frequente:", frequenza(dizionario))
