class ContoBancario:
    def __init__(self, saldo_iniziale):
        self.__saldo = saldo_iniziale

    def mostrasaldo(self):
        print(f"Il saldo è ${self.__saldo} ")

    def deposito(self, rilascio):
        self.__saldo += rilascio

    def preliev(self, prelievo):
        if conto.__saldo - prelievo < 0:
            print("L'importo non è disponibile.")
        else:
            self.__saldo -= prelievo


conto = ContoBancario(int(input("Inserisci il saldo iniziale: ")))
conto.mostrasaldo()

rilascio = int(input("Inserisci il saldo da depositare: "))

prelievo = int(input("Inserisci il saldo da prelevare: "))

conto.deposito(rilascio)
conto.mostrasaldo()

conto.preliev(prelievo)
conto.mostrasaldo()
