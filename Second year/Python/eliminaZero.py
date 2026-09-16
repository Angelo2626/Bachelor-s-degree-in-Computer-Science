matrice = []
with open("in.txt", "r") as file:
    for riga in file:
        numeri = list(map(int, riga.split()))
        matrice.append(numeri)

for riga in matrice:
    print(*riga)
print("\n")

matrice = [riga[::-1] for riga in matrice if riga[0] != 0]

with open("out.txt", "w") as file:
    for riga in matrice:
        file.write(f"{riga}\n")
