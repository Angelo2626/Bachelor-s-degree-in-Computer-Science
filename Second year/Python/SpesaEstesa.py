def calcolo_spesa(acquisti, prezzi):
    dizionario = {}
    for merce in acquisti:
        if merce not in dizionario:
            dizionario.update({merce: round(prezzi[merce]*acquisti.count(merce), 2)})
    print(dizionario)

def conto_merce(acquisti):
    conto = set()
    for merce in acquisti:
        if acquisti.count(merce) > 2:
            conto.add(merce)
    print(conto)

acquisti = ["mela", "banana", "pera", "banana", "kiwi", "mela", "mela"]
prezzi = {"mela": 1.2, "banana": 0.8, "pera": 1.5, "kiwi": 2.0}
calcolo_spesa(acquisti, prezzi)
conto_merce(acquisti)
