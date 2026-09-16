sentence = input().split()
dizionario = {}

for parola in sentence:
    if parola in dizionario:
        dizionario[parola] += 1
    else:
        dizionario[parola] = 1

print(dizionario)
