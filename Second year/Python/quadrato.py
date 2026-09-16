while True:
    n = int(input("Inserisci un numero: "))
    if n >= 0:
        print(n**2)
        break
    else:
        print("Hai inserito un numero negativo.")
        break
