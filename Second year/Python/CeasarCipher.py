import string

def CeasarCipher(stringa, shift):
    new_stringa = ""
    indice = 0
    for char in stringa:
        if char.islower():
            indice = (string.ascii_lowercase.index(char) + shift) % 26
            new_stringa = new_stringa + string.ascii_lowercase[indice]
        else:
            indice = (string.ascii_uppercase.index(char) + shift) % 26
            new_stringa = new_stringa + string.ascii_uppercase[indice]
    return new_stringa

stringa = input().strip()
shift = int(input())
print(CeasarCipher(stringa, shift))
