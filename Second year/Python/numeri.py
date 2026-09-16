with open("numeri.bin","r") as file:
    content = file.read()

lp = ""
ld = ""
for numero in content.split():
    if int(numero) % 2 == 0:
        lp += numero
        lp += " "
    else:
        ld += numero
        ld += " "

with open("pari.txt", "w") as file:
    file.write(lp)
with open("dispari.txt", "w") as file:
    file.write(ld)
