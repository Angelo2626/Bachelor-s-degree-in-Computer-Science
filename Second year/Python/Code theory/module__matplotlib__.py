# INTRODUZIONE
from matplotlib import pyplot as plt
x = [1, 2, 3, 4, 5]
y = [10, 30, 20, 50, 40]

plt.plot(x, y) # creazione del grafico

plt.title("Titolo del Grafico")
plt.xlabel("Descrizione Asse X")
plt.ylabel("Descrizione Asse Y")

plt.show() # visualizzazione del grafico

y_due = [40, 20, 50, 10, 30]

plt.plot(x, y)
plt.plot(x, y_due)
plt.legend(["Prima linea", "Seconda linea"]) # legenda
""" è meglio se si mettono già nel comando plot.
    Es: plt.plot(x, y, label = "Prima linea") """
plt.grid() # aggiunge una griglia

plt.plot(x, y,
         color = "green", # colore della linea
         linestyle = "dotted", # stile della linea
         marker = "o", # mette il punto sui vari dati
         )


plt.show() # si può creare un grafico con più linee