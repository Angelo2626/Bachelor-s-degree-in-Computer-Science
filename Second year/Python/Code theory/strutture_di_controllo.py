# condizioni
voto = 25
if voto >= 27:
    print("Hai avuto un voto eccellente")
elif voto >= 18:
    print("Hai avuto un buon voto")
else:
    print("Devi migliorare")

# cicli
frutti = ["mela", "pera", "banana"]
for frutto in frutti:
    print(frutto)

for i in range(5):
    if i == 3:
        break # termina il ciclo
    print(i)

for i in range(10):
    if i % 2 == 0:
        continue # passa alla prossima iterazione
    print(i)

contatore = 0
while contatore < 5:
    print(contatore)
    contatore += 1
