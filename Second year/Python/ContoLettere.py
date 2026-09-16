def parole_lunghe(lista, n):
    l = []
    for parola in lista:
        if len(parola) > n:
            l.append(parola)
    print(l)

lista = []
while True:
    parola = input("Inserisci una parola o premi 0 per smettere: ")
    if parola == "0":
        break
    else:
        lista.append(parola)
n = int(input("Inserisci un numero di lettere da valutare: "))
parole_lunghe(lista, n)
