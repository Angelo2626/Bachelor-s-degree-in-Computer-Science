# type restituisce il tipo di dato
x = 10
print(type(x))

# operatori particolari
a = 5
b = 3
divisione_intera = a // b
potenza = a ** b # a^b
print(f"{divisione_intera}")
print(f"{potenza}")

# operatori AND e OR
print(a > 2 and b < 10)
print(a > 2 or b < 10)

# conversione tra tipi di dati
x = "42" # stringa
y = int(x)
z = 25 # int
testo = str(z)

# input e output
nome = input("Qual è il tuo nome?")
print(f"Ciao {nome}!")
# input restituisce la stringa, poi bisogna convertirlo
anni = input("Quanti anni hai?")
print(f"Hai {str(anni)} anni")