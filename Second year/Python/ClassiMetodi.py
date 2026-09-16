class Libro:
    def __init__(self, titolo, autore, pagine, prezzo):
        self.titolo = titolo
        self.autore = autore
        self.pagine = pagine
        self.prezzo = prezzo

    def sconto(self, percentuale):
        sconto = self.prezzo * percentuale / 100
        self.prezzo -= sconto

    def info(self):
        return f"{self.titolo} di {self.autore}, {self.pagine} pagine, ${self.prezzo:.2f}"

libro = Libro("Il Piccolo Principe", "Saint-Exupery", 96, 15.0)
libro.sconto(20)
print(libro.info())
