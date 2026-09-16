class Animale:
    def __init__(self, specie):
        self.specie = specie

    def verso(self):
        if self.specie == "cane":
            self.cane()
        else:
            self.gatto()

    def cane(self):
        print(f"Il verso di un {self.specie} è BAU!")

    def gatto(self):
        print(f"Il verso di un {self.specie} è MIAO!")

animale1 = Animale(input("Inserisci la specie: "))
animale2 = Animale(input("Inserisci la specie: "))

animale1.verso()
animale2.verso()
