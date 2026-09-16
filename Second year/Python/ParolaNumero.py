parola_a_numero = {
    "zero": 0,
    "uno": 1,
    "due": 2,
    "tre": 3,
    "quattro": 4,
    "cinque": 5,
    "sei": 6,
    "sette": 7,
    "otto": 8,
    "nove": 9
}
somma = 0
with open("parolaNumero.txt", "r") as file:
    for riga in file:
        numero = 0
        for parola in riga.split():
            if parola in parola_a_numero:
                numero = (numero * 10) + parola_a_numero[parola]
        somma += numero

print(somma)

