def pariodispari(numero):
    if numero % 2 == 0:
        print("Pari")
    elif numero % 2 != 0:
        print("Dispari")
    elif numero == 0:
        print("Zero")

numero = int(input("Inserisci un numero: "))
pariodispari(numero)
