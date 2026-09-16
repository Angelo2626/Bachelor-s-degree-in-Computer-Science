# classe
class Cane:
    # metodo costruttore
    def __init__(self, nome, razza):
        # attributi di istanza
        self.nome = nome # attributo nome
        self.razza = razza # attributo razza

    # metodo per far abbaiare il cane
    def abbaia(self):
        print(f"{self.nome} sta abbaiando")

# oggetto = istanza di una classe
mio_cane = Cane("Fido", "Labrador") # creazione di un oggetto
mio_cane.abbaia()

# ereditarietà e poliformismo
class Animale:
    def __init__(self, nome):
        self.nome = nome

    def dormi(self):
        print(f"{self.nome} sta dormendo")

class Gatto(Animale): # classe Gatto con gli attributi e i metodi di Animale --> ereditarietà
    def miagola(self):
        print(f"{self.nome} sta miagolando")

gatto = Gatto("Jeremy")
gatto.dormi()
gatto.miagola()

# incapsulamento
class ContoBancario:
    def __init__(self, titolare, saldo):
        self.titolare = titolare
        self.__saldo = saldo # attributo privato

    def deposita(self, importo):
        self.__saldo += importo

    def mostra_saldo(self):
        print(f"Il saldo di {self.titolare} è {self.__saldo} euro")

    # getter e setter
    def get_saldo(self):
        return self.__saldo

    def set_saldo(self, nuovo_saldo):
        if nuovo_saldo >= 0:
            self.__saldo = nuovo_saldo
        else:
            print("Il saldo non può essere negativo")

conto = ContoBancario("Mario", 1000)
conto.mostra_saldo()
conto.deposita(500)
conto.mostra_saldo()

print(conto.get_saldo()) # ora si può prendere il valore dell'attributo privato
conto.set_saldo(2000)
print(conto.get_saldo())