class Telefono:
    def __init__(self, marca, modello, batteria, memoria):
        self.marca = marca
        self.modello = modello
        self.batteria = batteria
        self.memoria = memoria

    def usa(self, minuti):
        if self.batteria > 0:
            self.batteria -= minuti

    def carica(self):
        self.batteria = 100

    def installa_app(self, nome, dimensione):
        if self.memoria >= dimensione:
            self.memoria -= dimensione

    def info(self):
        return f"La marca del telefono é: {self.marca}, il modello è: {self.modello}. La batteria è al {self.batteria}% e ha {self.memoria} a disposizione."

mio_telefono = Telefono("Samsung", "Galaxy S23", 100, 128)

print(mio_telefono.info())

mio_telefono.usa(30)
print(mio_telefono.batteria)

mio_telefono.installa_app("whatsapp", 2)
mio_telefono.installa_app("Gaming App", 200)

mio_telefono.carica()
print(mio_telefono.batteria)


