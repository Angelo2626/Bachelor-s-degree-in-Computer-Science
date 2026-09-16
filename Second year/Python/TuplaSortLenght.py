stringa = input()
lista = set(stringa.split())
print(lista)

lista_completa = []
vocali = "aeiouAEIOU"
count = 0

for parola in lista:
    for char in parola:
        if char in vocali:
            count += 1
    tupla = (parola, len(parola), count)
    count = 0
    lista_completa.append(tupla)

lista_completa.sort(key=lambda x: x[1])
print(lista_completa)
