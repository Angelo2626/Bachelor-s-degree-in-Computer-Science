def count_word(stringa):
    lista = stringa.split()
    return len(lista)

def upper(stringa):
    lista = stringa.split()
    lista_maiuscole = []
    for parola in lista:
        if parola[0].isupper() == True:
            lista_maiuscole.append(parola)
    return lista_maiuscole

def digit(stringa):
    lista = stringa.split()
    flag = 0
    lista_numeri = []
    for parola in lista:
        for c in parola:
            if c.isdigit() == True:
                flag = 1
        if flag == 1:
            lista_numeri.append(parola)
            flag = 0
        else:
            flag = 0
    return lista_numeri

def count_letters(stringa):
    lista = stringa.split()
    min = lista[0]
    for parola in lista:
        if len(parola) < len(min):
            min = parola
    return len(min)

stringa = input()
print("Numero di parole:", count_word(stringa))
print("Parole maiuscole:", upper(stringa))
print("Parole con numeri:", digit(stringa))
print("Parola più corta:", count_letters(stringa))
