import cirq
import sympy
import numpy as np
import matplotlib.pyplot as plt
from tqdm import tqdm

#riutilizzo le funzioni collaudate nel secondo esperimento

def create_vqc():
    """Crea e restituisce la struttura del circuito VQC parametrico."""
    q0, q1 = cirq.LineQubit.range(2)
    x0, x1 = sympy.Symbol('x0'), sympy.Symbol('x1')
    w0, w1, w2, w3 = sympy.symbols('w0 w1 w2 w3')

    circuit = cirq.Circuit()
    circuit.append(cirq.rx(x0).on(q0))
    circuit.append(cirq.rx(x1).on(q1))
    circuit.append(cirq.ry(w0).on(q0))
    circuit.append(cirq.ry(w1).on(q1))
    circuit.append(cirq.CNOT(q0, q1))
    circuit.append(cirq.ry(w2).on(q0))
    circuit.append(cirq.ry(w3).on(q1))
    circuit.append(cirq.measure(q0, q1, key='result'))

    return circuit

def classify_point(circuit, simulator, weights_values, input_values, shots=2000):
    """Esegue il circuito per un dato input e restituisce la classe predetta (0 o 1)."""
    x0, x1 = sympy.Symbol('x0'), sympy.Symbol('x1')
    w0, w1, w2, w3 = sympy.symbols('w0 w1 w2 w3')

    valori = {
        x0: input_values[0], x1: input_values[1],
        w0: weights_values[0], w1: weights_values[1],
        w2: weights_values[2], w3: weights_values[3]
    }

    resolver = cirq.ParamResolver(valori)
    results = simulator.run(circuit, param_resolver=resolver, repetitions=shots)
    counts = results.histogram(key='result')

    prob_q0_is_1 = (counts.get(1, 0) + counts.get(3, 0)) / shots
    return 1 if prob_q0_is_1 > 0.5 else 0

#la nuova funzione per il test di robustezza

def test_robustness(circuit, simulator, weights, center_point, epsilon, num_samples=100):
    """
    Testa la robustezza del VQC in un'area quadrata attorno a un punto centrale.

    Args:
        center_point (list): Le coordinate [x0, x1] del punto da testare.
        epsilon (float): Il "raggio" della perturbazione. L'area testata sarà
                         [x0-eps, x0+eps] e [x1-eps, x1+eps].
        num_samples (int): Il numero di punti casuali da testare all'interno dell'area.

    Returns:
        bool: True se tutti i campioni nell'area hanno la stessa classificazione
              del punto centrale, False altrimenti.
    """

    #classifico il punto centrale per avere un riferimento
    original_class = classify_point(circuit, simulator, weights, center_point)

    #genero 'num_samples' punti casuali all'intereno del quadrato di perturabzione
    #np.random.uniform genera numeri casuali in un intervallo
    # il primo argomento è il limite inferiore, il secondo il superiore, il terzo la dimensione.

    random_x0s = np.random.uniform(center_point[0] - epsilon, center_point[0] + epsilon, num_samples)
    random_x1s = np.random.uniform(center_point[1] - epsilon, center_point[1] + epsilon, num_samples)

    #verifico ogni punto casuale
    for i in range(num_samples):
        sample_point = [random_x0s[i], random_x1s[i]]
        sample_class = classify_point(circuit, simulator, weights, sample_point)

        #se anche solo un punto ha una classificazione diversa, il test fallisce
        if sample_class != original_class:
            return False # Il circuito non è robusto per questo epsilon

    # Se il ciclo finisce senza trovare controesempi, il test ha successo
    return True # Il circuito è robusto per questo epsilon

#uso la nuova funzione per trovare l'epsilon massimo

def main():
    vqc = create_vqc()
    simulator = cirq.Simulator()
    fixed_weights = [0.99, -0.50, 3.27, -0.69]

    # MODIFICA: scelgo un punto più stabile
    point_to_test = [3.14, 3.14]

    print(f"Inizio analisi di robustezza per il punto {point_to_test}...")

    epsilon = 0.0
    epsilon_step = 0.05
    max_robust_epsilon = 0.0

    # Uso tqdm per la barra di progresso
    from tqdm import trange
    # Testo fino a un epsilon massimo ragionevole
    for _ in trange(40, desc="Testing Epsilon"): # Testa fino a epsilon = 2.0
        if test_robustness(vqc, simulator, fixed_weights, point_to_test, epsilon + epsilon_step):
            epsilon += epsilon_step
            max_robust_epsilon = epsilon
        else:
            # Se il test fallisce, quello è il limite
            print(f"\nTest fallito per epsilon > {max_robust_epsilon:.2f}")
            break

    print("\n--- Risultato dell'Analisi ---")
    print(f"Il massimo epsilon di robustezza trovato è: {max_robust_epsilon:.2f}")

    # --- Visualizzazione ---
    view_range = max(max_robust_epsilon * 2.5, 0.5) # Assicura una vista minima
    resolution = 50
    x0_vals = np.linspace(point_to_test[0] - view_range, point_to_test[0] + view_range, resolution)
    x1_vals = np.linspace(point_to_test[1] - view_range, point_to_test[1] + view_range, resolution)

    results_grid = np.zeros((resolution, resolution))
    # Uso tqdm anche qui
    from tqdm import tqdm
    print("Creazione mappa di visualizzazione...")
    for i, x0_val in enumerate(tqdm(x0_vals)):
        for j, x1_val in enumerate(x1_vals):
            results_grid[j, i] = classify_point(vqc, simulator, fixed_weights, [x0_val, x1_val], shots=200)

    plt.figure(figsize=(10, 8))
    plt.imshow(results_grid, origin='lower',
               extent=[x0_vals[0], x0_vals[-1], x1_vals[0], x1_vals[-1]],
               cmap='viridis', aspect='equal')
    plt.scatter([point_to_test[0]], [point_to_test[1]], c='white', marker='*', s=200, label='Punto Centrale')
    rect = plt.Rectangle(
        (point_to_test[0] - max_robust_epsilon, point_to_test[1] - max_robust_epsilon),
        2 * max_robust_epsilon, 2 * max_robust_epsilon,
        linewidth=2, edgecolor='red', facecolor='none', label=f'Area Robusta (eps={max_robust_epsilon:.2f})'
    )
    plt.gca().add_patch(rect)
    plt.title("Visualizzazione della Robustezza")
    plt.xlabel("Input x0")
    plt.ylabel("Input x1")
    plt.legend()
    plt.colorbar(label="Classe Predetta")
    plt.show()

if __name__ == "__main__":
    main()
