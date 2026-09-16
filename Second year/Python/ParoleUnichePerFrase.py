lista = []
while True:
    stringa = input("Inserisci una frase per continuare, 0 per uscire: ")
    if stringa == "0":
        break
    else:
        lista.append(stringa)
print(lista)

dizionario = {}

for i in range(len(lista)):
    SF = lista[i].split()
    dizionario.update({lista[i]: SF})

print(dizionario)


