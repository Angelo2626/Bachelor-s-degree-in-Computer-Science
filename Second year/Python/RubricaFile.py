while True:
    valid = int(input("Se si vuole aggiungere un contatto in rubrica premere 1, altrimenti 0.\n"))
    if valid == 1:
        nome = input("Inserisci il nome: ").capitalize()
        numero = input("Inserisci il numero: ")
        with open("rubrica.txt", "a") as file:
            file.write(nome + ":" + numero + "\n")
    else:
        break

with open ("rubrica.txt", "r") as file:
    rubrica = file.read()
print(rubrica)
