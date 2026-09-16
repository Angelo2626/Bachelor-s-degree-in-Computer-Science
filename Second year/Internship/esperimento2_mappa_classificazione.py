import cirq
import sympy
import numpy as np
import matplotlib.pyplot as plt     #libreria per creare grafici
from tqdm import tqdm       #libreria per mostrare una barra di avanzamento nei cicli for

#ora riprendiamo il circuito di prima in una funzione.

def create_vqc():
    """Crea e restituisce la struttura del circuito VQC parametrico."""
    q0, q1 = cirq.LineQubit.range(2)
    x0, x1 = sympy.Symbol('x0'), sympy.Symbol('x1')
    w0, w1, w2, w3 = sympy.symbols('w0 w1 w2 w3')

    circuit = cirq.Circuit()
    # Encoding
    circuit.append(cirq.rx(x0).on(q0))
    circuit.append(cirq.rx(x1).on(q1))
    # Ansatz
    circuit.append(cirq.ry(w0).on(q0))
    circuit.append(cirq.ry(w1).on(q1))
    circuit.append(cirq.CNOT(q0, q1))
    circuit.append(cirq.ry(w2).on(q0))
    circuit.append(cirq.ry(w3).on(q1))
    # Misura
    circuit.append(cirq.measure(q0, q1, key='result'))

    # restituisce il circuito ed i simboli per un facile accesso
    return circuit, [x0, x1], [w0, w1, w2, w3]

def classify_point(circuit, simulator, weights_values, input_values, shots=1000):
    """
    Esegue il circuito per un dato input e restituisce la classe predetta (0 o 1).
    La classificazione si basa sullo stato del qubit q0.
    """
    #definisco i simboli che mi aspetto di trovare nel circuito.
    x0, x1 = sympy.Symbol('x0'), sympy.Symbol('x1')
    w0, w1, w2, w3 = sympy.symbols('w0 w1 w2 w3')

    #costruisco il dizionario che mappa i simboli ai loro valori numerici.
    valori = {
        x0: input_values[0], x1: input_values[1],
        w0: weights_values[0], w1: weights_values[1],
        w2: weights_values[2], w3: weights_values[3]
    }

    resolver = cirq.ParamResolver(valori)
    results = simulator.run(circuit, param_resolver=resolver, repetitions=shots)
    counts = results.histogram(key='result')

    #calcolo la probabilità che q0 sia 1 (risultati 01 e 11, cioè 1 e 3 )
    #in cirq il bit meno significativo è il primo qubit (q0)
    #risultato 1 (binario 01) e 3 (binario 11) hanno q0=1
    prob_q0_is_1 = (counts.get(1, 0) + counts.get(3, 0)) / shots

    # Se la probabilità è > 0.5, classifico come 1, altrimenti 0
    return 1 if prob_q0_is_1 > 0.5 else 0

def main():
    #creo il circuito ed il simulatore
    vqc, symbols_in, symbols_w = create_vqc()
    simulator = cirq.Simulator()

    #uso gli stesi pesi fissi dell'articolo
    fixed_weights = [0.99, -0.50, 3.27, -0.69]

    #definisco la griglia di input che voglio testare
    # #le porte rotazionali sono periodiche con 2*pi, quindi questo è un buon range
    resolution = 100
    x0_vals = np.linspace(0, 2 * np.pi, resolution)
    x1_vals = np.linspace(0, 2 * np.pi, resolution)

    #preparo una matrice per salvare i risultati della classificazione
    results_grid = np.zeros((resolution, resolution))

    print("Inizio calcolo della mappa di classificazione...")
    #itero su ogni punto della griglia.
    for i, x0_val in enumerate(tqdm(x0_vals)):
        for j, x1_val in enumerate(x1_vals):
            input_point = [x0_val, x1_val]
            #classifico il punto e slavo il risultato
            results_grid[j, i] = classify_point(vqc, simulator, fixed_weights, input_point)

    print("Calcolo completato. Visualizzazione del grafico...")

    #visualizzo la mappa con Matplotlib
    plt.figure(figsize=(8, 8))

    #uso imshow per visulazizare la griglia di risultati come un'immagine
    plt.imshow(results_grid, origin='lower', extent=[0, 2 * np.pi, 0, 2 * np.pi], cmap='viridis')

    plt.title("Mappa di Classificazione del VQC")
    plt.xlabel("Input x0 (angolo di rotazione)")
    plt.ylabel("Input x1 (angolo di rotazione)")

    cbar = plt.colorbar(ticks=[0, 1])
    cbar.set_label("Classe Predetta")

    #aggiungo il punto di test dell'articolo per vedere dov si trova
    plt.scatter([6.0], [2.7], c='red', marker='*', s=200, label='Punto Test Paper (6.0, 2.7)')
    plt.legend()

    plt.show()

#eseguo la funzione principale
if __name__ == "__main__":
    main()
