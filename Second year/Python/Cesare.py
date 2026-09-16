import string

def cesare(stringa, n):
    NuovaStringa = ""
    alfabeto = string.ascii_lowercase
    ALFABETO = string.ascii_uppercase
    for char in stringa:
        if char in alfabeto:
            pos = alfabeto.find(char) + n
            NuovaStringa += alfabeto[pos]
        elif char in ALFABETO:
            pos = ALFABETO.find(char) + n
            NuovaStringa += ALFABETO[pos]
        else:
            NuovaStringa += char
    return NuovaStringa

stringa = input()
n = int(input())
print(cesare(stringa, n))
