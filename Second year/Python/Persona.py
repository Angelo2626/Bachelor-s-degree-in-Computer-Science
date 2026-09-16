class Persona:
    def __init__(self, nome, cognome):
        self.nome = nome
        self.cognome = cognome

    def presentati(self):
        print(f"Ciao! Sono {self.nome} {self.cognome}")

class Lavoratore(Persona):
    def __init__(self, nome, cognome, azienda):
        super().__init__(nome, cognome)
        self.azienda = azienda
    def presentati(self):
        super().presentati()
        print(f"Ciao! Sono {self.nome} {self.cognome} e lavoro nell'azienda {self.azienda}")

class StudenteLavoratore(Lavoratore):
    def __init__(self, nome, cognome, azienda, Università):
        super().__init__(nome, cognome, azienda)
        self.Università = Università
    def presentati(self):
        super().presentati()
        print(f"Ciao! Sono {self.nome} {self.cognome}, lavoro nell'azienda {self.azienda} e studio nell'{self.Università}")

while True:
    nome = input("Inserisci il nome: ").capitalize()
    cognome = input("Inserisci il cognome: ").capitalize()
    persona = Persona(nome, cognome)
    while True:
        controllo = int(input("Inserisci 1 se la persona è un lavoratore, 2 se è uno studente-lavoratore o 0 se non studia e non lavora: "))
        if controllo == 1 or controllo == 2 or controllo == 0:
            break
    if controllo == 1:
        persona = Lavoratore(nome, cognome, input("Inserisci il nome dell'azienda: "))
    elif controllo == 0:
        persona.presentati()
    elif controllo == 2:
        persona = StudenteLavoratore(nome, cognome, input("Inserisci il nome dell'azienda: "), input("Inserisci il nome dell'università: "))
        persona.presentati()
    break
