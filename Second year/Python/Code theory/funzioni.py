# definizione funzione
def saluta(nome):
    print(f"Ciao {nome}")

# chiamata funzione
saluta("Alessio")

# funzione con valore di ritorno
def moltiplica(a, b):
    return a * b

risultato = moltiplica(5, 7)
print(risultato)

# scope variabili
# locali: all'interno della funzione
# globali: usabili ovunque, definite fuori da una funzione (global)
x = 10
def modifica_x():
    global x
    x = 20

print(f"Valore prima della modifica: {x}")
modifica_x()
print(f"Valore dopo la modifica: {x}")

# funzioni lambda
quadrato = lambda x: x ** 2 # funzioni in una singola riga
print(quadrato(3))