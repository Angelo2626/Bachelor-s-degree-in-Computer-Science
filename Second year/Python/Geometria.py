import math
class FiguraGeometrica:

    def area(self):

        return('metodo area non implementato')

    def perimetro(self):

        return('metodo perimetro non implementato')

    def __str__(self):
        return "Figura geometrica generica"

class Cerchio(FiguraGeometrica):
    def __init__(self, raggio):
        self.raggio = raggio
    def area(self):
        print(f"L'area del cerchio è {math.pi * self.raggio ** 2}")
    def perimetro(self):
        print(f"Il perimetro del cerchio è {2 * math.pi * self.raggio}")

class Rettangolo(FiguraGeometrica):
    def __init__(self, base, altezza):
        self.base = base
        self.altezza = altezza
    def area(self):
        print(f"L' area del rettangolo è {self.base * self.altezza}")
    def perimetro(self):
        print(f"Il perimetro del rettangolo è {(self.base + self.altezza) * 2}")

class Quadrato(FiguraGeometrica):
    def __init__(self, lato):
        self.lato = lato
    def area(self):
        print(f"L'area del quadrato è {self.lato * self.lato}")
    def perimetro(self):
        print(f"Il perimetro del quadrato è {self.lato * 4}")

cerchio = Cerchio(int(input("Inserisci il raggio del cerchio: ")))
cerchio.area()
cerchio.perimetro()
quadrato = Quadrato(int(input("Inserisci il lato del quadrato: ")))
quadrato.area()
quadrato.perimetro()
rettangolo = Rettangolo(int(input("Inserisci la base del rettangolo: ")), int(input("Inserisci l'altezza del rettangolo: ")))
rettangolo.area()
rettangolo.perimetro()
