def paroleuniche(stringa):
    lista = set(stringa.split())
    count = 0
    setparole = set()
    for parola in lista:
        count += 1
        setparole.add(parola)
    return count, setparole

def vocali(stringa):
    vocali = "aeiouAEIOU"
    setvocali = set()
    for char in stringa:
        if char in vocali:
            setvocali.add(char)
    return setvocali

def consonanti(stringa):
    vocali = "aeiouAEIOU"
    setconsonanti = set()
    for char in stringa:
        if char.isalpha() and char not in vocali:
            setconsonanti.add(char)
    return setconsonanti

stringa = input()
count, setparole = paroleuniche(stringa)
print("Numero di parole uniche:", count)
print("Parole uniche:", setparole)
print("Vocali presenti:", vocali(stringa))
print("Consonanti presenti:", consonanti(stringa))
