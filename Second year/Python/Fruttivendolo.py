def spesa(frutta, acquisto):
    dizionario = {}
    for i in acquisto:
        if i not in dizionario:
            dizionario.update({i: acquisto.count(i)})
    return dizionario

frutta = {"mela", "pera", "banana"}
acquisto = ["banana", "kiwi", "mela", "mela"]
print(f"La differenza tra la frutta disponibile e quella acquistata è {set(acquisto) - frutta}")
print(spesa(frutta, acquisto))
