def StampaNome(studenti):
    lista = []
    for key, value in studenti.items():
        if value >= 25:
            lista.append(value)
            print(key)
    print(lista)

studenti = {"Luca": 27, "Anna": 30, "Marco": 18, "Sara": 24}
StampaNome(studenti)
