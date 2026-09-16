class ContoBancario:
    def __init__(self, titolare, saldo, scoperto_massimo):
        self.titolare = titolare
        self.saldo = saldo
        self.scoperto_massimo = scoperto_massimo

    def info(self):
        return f"Il conto di {self.titolare} ha un saldo disponibile di {self.saldo} e ha uno scoperto di {self.scoperto_massimo}\n"

    def prelievo(self, importo):
        self.saldo = self.saldo - importo - self.scoperto_massimo
        if self.saldo > 0:
            print("Il saldo disponibile è: ", self.saldo, "\n")
        elif self.saldo < 0 and self.saldo >= -100:
            self.scoperto_massimo = self.saldo
            self.saldo = 0
            print("Il saldo disponibile è sceso a $",self.saldo, "e lo scoperto è di $",self.scoperto_massimo,". Si ricorda che lo scoperto massimo è di $100.00")
        elif self.saldo < -100:
            self.saldo = self.saldo + importo + self.scoperto_massimo
            print("Il saldo non è disponibile.")

    def deposito(self, importo):
        if self.saldo >= 0 and self.scoperto_massimo == 0:
            self.saldo += importo
            print("Il nuovo saldo disponibile è: $", self.saldo)
        elif self.saldo == 0 and self.scoperto_massimo < 0:
            self.scoperto_massimo += importo
            if self.scoperto_massimo > 0:
                self.saldo += self.scoperto_massimo
                self.scoperto_massimo = 0
            print("Il nuovo saldo disponibile è: $", self.saldo)

    def bonifico(self, c2, importo):
        self.prelievo(importo)
        c2.deposito(importo)


c1 = ContoBancario("Angelo", 12000.00, 0.00)
importo = float(input("Inserisci l'importo da prelevare: "))
c1.prelievo(importo)
importo = float(input("Quanto si vuole depositare?"))
c1.deposito(importo)
print(c1.info())

c2 = ContoBancario("Gaia", 40000.00, 0.00)
importo = float(input("Inserisci l'importo da prelevare: "))
c2.prelievo(importo)
importo = float(input("Quanto si vuole depositare?"))
c2.deposito(importo)
print(c2.info())

importo = float(input(f"Quanto si vuole trasferire al conto di {c2.titolare}: "))
c1.bonifico(c2,importo)
