import string

def crealista(key, num):
    a = []
    for i in range(num):
        if key[i % len(key)] in string.ascii_lowercase:
            pos = string.ascii_lowercase.find(key[i % len(key)])
            a.append(pos)
        elif key[i % len(key)] in string.ascii_uppercase:
            pos = string.ascii_uppercase.find(key[i % len(key)])
            a.append(pos)
        else:
            a.append(0)
    return a

def vigenère(stringa, lista):
    ns = ""
    for i in range(len(stringa)):
        if stringa[i] in string.ascii_lowercase:
            pos = (string.ascii_lowercase.find(stringa[i]) + lista[i]) % 26
            ns += string.ascii_lowercase[pos]
        elif stringa[i] in string.ascii_uppercase:
            pos = (string.ascii_uppercase.find(stringa[i]) + lista[i]) % 26
            ns += string.ascii_uppercase[pos]
        else:
            ns += stringa[i]
    return ns

stringa = input()
key = input()
lista = crealista(key, len(stringa))
print(vigenère(stringa, lista))
