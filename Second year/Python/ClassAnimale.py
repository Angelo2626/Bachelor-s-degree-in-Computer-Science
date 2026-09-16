class Animale:
    def __init__(self, nome, eta):
        self.nome = nome
        self.eta = eta

    def mangia(self):
        return f"{self.nome} sta mangiando 🍖"

    def info(self):
        return f"Sono {self.nome} e ho {self.eta} anni"

# PROVALO SUBITO:
gatto = Animale("Whiskers", 3)
cane = Animale("Fido", 5)
uccello = Animale("Tweet", 1)

print(gatto.info())
print(cane.mangia())
print(uccello.mangia())
