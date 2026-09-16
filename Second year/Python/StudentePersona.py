import numpy as np

class Persona:
    def __init__(self, nome, cognome, età):
        self.nome = nome
        self.cognome = cognome
        self.età = età

class Studente(Persona):
    def __init__(self, nome, cognome, età, voti, matricola):
        super().__init__(nome, cognome, età)
        self.voti = voti
        self.matricola = matricola

    def media(self):
        return np.mean(self.voti)

media_corrente = 0
media_max = float('-inf')

for i in range(5):
    nome = input("Inserisci il nome dello studente: ")
    cognome = input("Inserisci il cognome dello studente: ")
    while True:
        età = int(input("Inserisci l'età dello studente: "))
        if 18 <= età <= 90:
            break
    voti = np.random.randint(18, 31, (10))
    while True:
        matricola = input("Inserisci il numero di matricola (5 cifre): ")
        if len(matricola) == 5 and matricola.isdigit():
            break

    studente = Studente(nome, cognome, età, voti, matricola)
    media_corrente = studente.media()

    if media_corrente > media_max:
        media_max = media_corrente
        studente_top = studente

print(f"Lo studente con la media più alta è {studente_top.nome} {studente_top.cognome} "
      f"(matricola {studente_top.matricola}) con la media del {studente_top.media():.2f}")
