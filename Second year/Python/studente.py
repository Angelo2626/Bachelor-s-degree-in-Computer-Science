class Studente:
    def __init__(self, nome, voto):
        self.nome = nome
        self.voto = voto

    def descrizione(self):
        print(f"Lo studente {self.nome.capitalize()} ha voto {self.voto}")

studente1 = Studente(input("Inserisci il nome dello studente: "), int(input("Inserisci il voto dello studente: ")))
studente2 = Studente(input("Inserisci il nome dello studente: "), int(input("Inserisci il voto dello studente: ")))

studente1.descrizione()
studente2.descrizione()
