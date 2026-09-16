def mean(lista):
    media = 0
    for i in range(len(lista)):
        media += lista[i]
    media = media / len(lista)
    return media

lista = []
while True:
    count = int(input("Inserisci il numero di elementi nella lista: "))
    if count <= 0 :
        break
    else:
        for i in range(count):
            n = int(input("Inserisci un numero della lista: "))
            lista.append(n)
        break
if len(lista) == 0:
    print("None")
else:
    print(f"La media aritmetica della lista {lista} è {mean(lista)}")
