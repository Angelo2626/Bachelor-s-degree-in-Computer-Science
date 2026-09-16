import cirq
import sympy
import numpy as np
import matplotlib.pyplot as plt
from tqdm import tqdm

# ==============================================================================
# SEZIONE 1: IL "MOTORE" DEL VQC (Funzioni Base Riutilizzabili)
# ==============================================================================

def create_vqc():
    """
    Crea e restituisce la struttura del circuito VQC parametrico.
    Questa funzione definisce l'architettura del mio modello.
    """
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

    return circuit

def classify_point(circuit, simulator, weights_values, input_values, shots=2000):
    """Esegue il circuito per un singolo input e restituisce la classe predetta (0 o 1)."""
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

# ==============================================================================
# SEZIONE 2: LA LOGICA DI VERIFICA
# ==============================================================================

def verify_robustness_grid_scan(circuit, simulator, weights, center_point, epsilon, grid_resolution=20):
    """
    Verifica la robustezza in un'area tramite una scansione a griglia sistematica.
    Questo metodo è più rigoroso del campionamento casuale.

    Returns:
        bool: True se TUTTI i punti della griglia hanno la stessa classificazione.
    """
    # 1. Determina la classificazione di riferimento al centro dell'area
    original_class = classify_point(circuit, simulator, weights, center_point)

    # 2. Crea le coordinate della griglia di verifica all'interno del quadrato di epsilon
    x0_grid = np.linspace(center_point[0] - epsilon, center_point[0] + epsilon, grid_resolution)
    x1_grid = np.linspace(center_point[1] - epsilon, center_point[1] + epsilon, grid_resolution)

    # 3. Scansiona ogni punto della griglia
    for x0_val in x0_grid:
        for x1_val in x1_grid:
            sample_point = [x0_val, x1_val]
            sample_class = classify_point(circuit, simulator, weights, sample_point, shots=500) # Meno shots per velocità

            # Se anche un solo punto è diverso, la verifica fallisce
            if sample_class != original_class:
                return False

    # Se la scansione è completa senza trovare controesempi, la verifica ha successo
    return True

def find_max_robust_epsilon(circuit, simulator, weights, point_to_test):
    """
    Uso la verifica a griglia per trovare il massimo epsilon di robustezza.
    """
    epsilon = 0.0
    epsilon_step = 0.05
    max_robust_epsilon = 0.0

    # Uso trange per una barra di progresso pulita
    from tqdm import trange
    print(f"Inizio analisi di robustezza sistematica per il punto {point_to_test}...")
    # Testo fino a un massimo di 40 passi (epsilon = 2.0)
    for _ in trange(40, desc="Verifying Epsilon"):
        next_epsilon_to_test = epsilon + epsilon_step
        if verify_robustness_grid_scan(circuit, simulator, weights, point_to_test, next_epsilon_to_test):
            # Se l'area più grande è ancora robusta, la accetto
            epsilon = next_epsilon_to_test
            max_robust_epsilon = epsilon
        else:
            # Al primo fallimento, mi fermo
            print(f"\nVerifica fallita per epsilon > {max_robust_epsilon:.2f}")
            break

    return max_robust_epsilon

# ==============================================================================
# SEZIONE 3: VISUALIZZAZIONE E ESECUZIONE (Il "Prodotto" Finale)
# ==============================================================================

def plot_robustness_certificate(circuit, simulator, weights, center_point, max_epsilon):
    """
    Crea un grafico finale che funge da "certificato di robustezza" visivo.
    """
    print("Creazione del certificato visivo...")

    view_range = max(max_epsilon * 2.5, 0.5)
    resolution = 50
    x0_vals = np.linspace(center_point[0] - view_range, center_point[0] + view_range, resolution)
    x1_vals = np.linspace(center_point[1] - view_range, center_point[1] + view_range, resolution)

    results_grid = np.zeros((resolution, resolution))
    for i, x0_val in enumerate(tqdm(x0_vals, desc="Plotting Map")):
        for j, x1_val in enumerate(x1_vals):
            results_grid[j, i] = classify_point(circuit, simulator, weights, [x0_val, x1_val], shots=200)

    plt.figure(figsize=(10, 8))
    plt.imshow(results_grid, origin='lower',
               extent=[x0_vals[0], x0_vals[-1], x1_vals[0], x1_vals[-1]],
               cmap='viridis', aspect='equal')

    plt.scatter([center_point[0]], [center_point[1]], c='white', marker='*', s=200, label='Punto Centrale')

    rect = plt.Rectangle(
        (center_point[0] - max_epsilon, center_point[1] - max_epsilon),
        2 * max_epsilon, 2 * max_epsilon,
        linewidth=2, edgecolor='red', facecolor='none', label=f'Area Robusta Verificata (eps={max_epsilon:.2f})'
    )
    plt.gca().add_patch(rect)

    plt.title("Certificato di Robustezza del VQC")
    plt.xlabel("Input x0")
    plt.ylabel("Input x1")
    plt.legend()
    plt.colorbar(label="Classe Predetta")
    plt.show()

def main():
    """Funzione principale che orchestra l'intero processo."""

    # --- Parametri dell'Esperimento ---
    FIXED_WEIGHTS = [0.99, -0.50, 3.27, -0.69]
    POINT_TO_TEST = [3.14, 3.14] # Il punto stabile che ho trovato nel 3 esperimento

    # 1. Setup del circuito e del simulatore
    vqc = create_vqc()
    simulator = cirq.Simulator()

    # 2. Eseguo l'analisi di robustezza per trovare l'epsilon massimo
    max_epsilon = find_max_robust_epsilon(vqc, simulator, FIXED_WEIGHTS, POINT_TO_TEST)

    print("\n" + "="*30)
    print("RISULTATO FINALE DELL'ANALISI")
    print(f"  Punto Analizzato: {POINT_TO_TEST}")
    print(f"  Massimo Epsilon Verificato: {max_epsilon:.2f}")
    print("="*30 + "\n")

    # 3. Genero il grafico/certificato finale
    if max_epsilon > 0:
        plot_robustness_certificate(vqc, simulator, FIXED_WEIGHTS, POINT_TO_TEST, max_epsilon)
    else:
        print("Robustezza nulla. Impossibile generare un certificato significativo.")


if __name__ == "__main__":
    main()
