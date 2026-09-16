class Rettangolo:
    def __init__(self, base, altezza):
        self.base = base
        self.altezza = altezza

    def calcolo_area(self):
        area = (self.base * self.altezza)
        print(f"L' area del rettangolo è {area}.")

figura = Rettangolo(int(input("Inserisci la base del triangolo: ")), int(input("Inserisci l'altezza del triangolo: ")))
figura.calcolo_area()

