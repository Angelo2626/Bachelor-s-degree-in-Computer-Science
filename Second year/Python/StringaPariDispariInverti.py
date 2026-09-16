def lista_pari(stringa):
    lista = stringa.split()
    lista_pari = []
    for parola in lista:
        if len(parola) % 2 == 0:
            lista_pari.append(parola)
    return lista_pari

def dispari_invertita(stringa):
    lista = stringa.split()
    lista_dispari = []
    for parola in lista:
        if len(parola) % 2 != 0:
            lista_dispari.append(parola[::-1])
    return lista_dispari

def combinazione(stringa):
    lista = stringa.split()
    lista_combinata = []
    for parola in lista:
        if len(parola) % 2 == 0:
            lista_combinata.append(parola)
        else:
            lista_combinata.append(parola[::-1])
    return lista_combinata

stringa = input()
print("Parole lunghezza pari:", lista_pari(stringa))
print("Parole lunghezza dispari invertite:", dispari_invertita(stringa))
print("Frase combinata:", combinazione(stringa))
