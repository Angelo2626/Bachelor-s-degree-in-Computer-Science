class Veicolo:
    def __init__(self, marca):
        self.marca = marca

    def descrivi(self):
        print("Questo è un veicolo.")

class Auto(Veicolo):
    def __init__(self, marca, tipo):
        super().__init__(marca)
        self.tipo = tipo

    def descrivi(self):
        super().descrivi()  # chiama il metodo della classe genitore
        print("Questo è un'auto.")

# Creiamo un’istanza di Auto
mezzo = Auto("Opel", "Crossover")
mezzo.descrivi()
