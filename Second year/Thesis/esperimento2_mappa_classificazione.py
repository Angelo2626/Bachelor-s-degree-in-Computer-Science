"""
Esperimento 2 - Caratterizzazione del confine decisionale del VQC.

Genera due mappe complementari sullo spazio di input [0, 2*pi]^2:
  (a) la mappa ESATTA, ottenuta dalle ampiezze dello stato finale, che mostra
      il confine decisionale reale del modello come curva netta;
  (b) la mappa CAMPIONATA con un numero finito di shots, che mostra la
      variabilita' introdotta dallo stimatore vicino al confine.

Il confronto fra le due chiarisce che il confine "frastagliato" non e' una
proprieta' del circuito ma un effetto del campionamento finito.
"""

import time

import cirq
import numpy as np
import sympy
import matplotlib.pyplot as plt
from tqdm import tqdm

SEED = 42
FIXED_WEIGHTS = [0.99, -0.50, 3.27, -0.69]
RESOLUTION = 100
SHOTS = 1000

PAPER_POINT = (6.0, 2.7)
STABLE_POINT = (np.pi, np.pi)
BOUNDARY_POINT = (4.7, 2.7)


def create_vqc(with_measurement=True):
    """Crea e restituisce la struttura del circuito VQC parametrico."""
    q0, q1 = cirq.LineQubit.range(2)
    x0, x1 = sympy.symbols("x0 x1")
    w0, w1, w2, w3 = sympy.symbols("w0 w1 w2 w3")

    circuit = cirq.Circuit()
    circuit.append(cirq.rx(x0).on(q0))
    circuit.append(cirq.rx(x1).on(q1))
    circuit.append(cirq.ry(w0).on(q0))
    circuit.append(cirq.ry(w1).on(q1))
    circuit.append(cirq.CNOT(q0, q1))
    circuit.append(cirq.ry(w2).on(q0))
    circuit.append(cirq.ry(w3).on(q1))
    if with_measurement:
        circuit.append(cirq.measure(q0, q1, key="result"))
    return circuit


def build_resolver(input_values, weights_values):
    x0, x1 = sympy.symbols("x0 x1")
    w0, w1, w2, w3 = sympy.symbols("w0 w1 w2 w3")
    return cirq.ParamResolver(
        {
            x0: input_values[0],
            x1: input_values[1],
            w0: weights_values[0],
            w1: weights_values[1],
            w2: weights_values[2],
            w3: weights_values[3],
        }
    )


def prob_q0_is_1_exact(circuit_no_measure, simulator, weights_values, input_values):
    """Probabilita' esatta che q0 sia misurato nello stato 1.

    Nel vettore di stato di Cirq l'indice vale 2*q0 + q1, quindi gli stati con
    q0 = 1 sono quelli di indice 2 e 3.
    """
    resolver = build_resolver(input_values, weights_values)
    state = simulator.simulate(circuit_no_measure, param_resolver=resolver)
    probs = np.abs(state.final_state_vector) ** 2
    return float(probs[2] + probs[3])


def prob_q0_is_1_sampled(circuit, simulator, weights_values, input_values, shots=SHOTS):
    """Stima campionaria di P(q0 = 1) su 'shots' misurazioni.

    Con cirq.measure(q0, q1) l'intero restituito dall'istogramma vale
    2*q0 + q1: gli esiti con q0 = 1 sono quindi 2 e 3.
    """
    resolver = build_resolver(input_values, weights_values)
    results = simulator.run(circuit, param_resolver=resolver, repetitions=shots)
    counts = results.histogram(key="result")
    return (counts.get(2, 0) + counts.get(3, 0)) / shots


def classify_point(circuit, simulator, weights_values, input_values, shots=SHOTS):
    """Classe predetta (0 o 1) stimata da 'shots' misurazioni."""
    return 1 if prob_q0_is_1_sampled(circuit, simulator, weights_values, input_values, shots) > 0.5 else 0


def prob_q0_is_1_closed_form(x0, x1, weights):
    """Forma chiusa di P(q0 = 1) per questo specifico circuito.

    Ricavata seguendo l'evoluzione del vettore di Bloch di q0:
      P(q0=1) = ( 1 - cos(x0) * [ cos(w0)cos(w2)
                                  - sin(w0)sin(w1)sin(w2)cos(x1) ] ) / 2
    Il fattore fra parentesi quadre non dipende da x0 e, per i pesi
    dell'articolo, non cambia mai segno: il confine decisionale coincide quindi
    esattamente con cos(x0) = 0.
    """
    w0, w1, w2, w3 = weights
    bracket = np.cos(w0) * np.cos(w2) - np.sin(w0) * np.sin(w1) * np.sin(w2) * np.cos(x1)
    return (1 - np.cos(x0) * bracket) / 2


def verify_closed_form(circuit_no_measure, simulator, weights, n=40):
    """Confronta la forma chiusa con il simulatore su una griglia di controllo."""
    grid = np.linspace(0, 2 * np.pi, n)
    max_err = 0.0
    for a in grid:
        for b in grid:
            p_sim = prob_q0_is_1_exact(circuit_no_measure, simulator, weights, [a, b])
            p_cf = prob_q0_is_1_closed_form(a, b, weights)
            max_err = max(max_err, abs(p_sim - p_cf))
    return max_err


def main():
    vqc = create_vqc(with_measurement=True)
    vqc_no_measure = create_vqc(with_measurement=False)
    simulator = cirq.Simulator(seed=SEED)

    x0_vals = np.linspace(0, 2 * np.pi, RESOLUTION)
    x1_vals = np.linspace(0, 2 * np.pi, RESOLUTION)

    prob_grid = np.zeros((RESOLUTION, RESOLUTION))
    class_grid = np.zeros((RESOLUTION, RESOLUTION))

    print("Calcolo della mappa esatta e della mappa campionata...")
    start = time.time()
    for i, x0_val in enumerate(tqdm(x0_vals)):
        for j, x1_val in enumerate(x1_vals):
            point = [x0_val, x1_val]
            prob_grid[j, i] = prob_q0_is_1_exact(vqc_no_measure, simulator, FIXED_WEIGHTS, point)
            class_grid[j, i] = classify_point(vqc, simulator, FIXED_WEIGHTS, point)
    elapsed = time.time() - start

    exact_class_grid = (prob_grid > 0.5).astype(float)
    disagreement = np.mean(exact_class_grid != class_grid)

    print(f"\nTempo di calcolo: {elapsed:.1f} s")
    print(f"Frazione dello spazio classificata come Classe 1 (esatta): {exact_class_grid.mean():.4f}")
    print(f"Frazione dello spazio classificata come Classe 1 (campionata): {class_grid.mean():.4f}")
    print(f"Punti in cui la mappa campionata differisce da quella esatta: {disagreement * 100:.2f}%")

    for name, pt in [("paper", PAPER_POINT), ("stabile", STABLE_POINT), ("confine", BOUNDARY_POINT)]:
        p = prob_q0_is_1_exact(vqc_no_measure, simulator, FIXED_WEIGHTS, list(pt))
        print(f"  punto {name} {np.round(pt, 3)}: P(q0=1) = {p:.4f}  ->  classe {int(p > 0.5)}")

    # --- Verifica della forma chiusa e struttura del confine ---
    err = verify_closed_form(vqc_no_measure, simulator, FIXED_WEIGHTS)
    print(f"\nErrore massimo forma chiusa vs simulatore: {err:.2e}")

    w0, w1, w2, w3 = FIXED_WEIGHTS
    bracket = np.cos(w0) * np.cos(w2) - np.sin(w0) * np.sin(w1) * np.sin(w2) * np.cos(x1_vals)
    print(f"Fattore [cos(w0)cos(w2) - sin(w0)sin(w1)sin(w2)cos(x1)] "
          f"nell'intervallo [{bracket.min():.4f}, {bracket.max():.4f}]")
    print("Il fattore non cambia segno: il confine e' esattamente cos(x0) = 0, "
          "cioe' x0 = pi/2 e x0 = 3pi/2.")
    span = prob_grid.max(axis=0) - prob_grid.min(axis=0)
    print(f"Escursione massima di P(q0=1) al variare del solo x1: {span.max():.4f}")

    # --- Visualizzazione ---
    extent = [0, 2 * np.pi, 0, 2 * np.pi]
    fig, axes = plt.subplots(1, 2, figsize=(13, 5.6))

    im0 = axes[0].imshow(prob_grid, origin="lower", extent=extent, cmap="coolwarm", vmin=0, vmax=1)
    axes[0].contour(x0_vals, x1_vals, prob_grid, levels=[0.5], colors="black", linewidths=1.5)
    axes[0].set_title("(a) $P(q_0=1)$ esatta e confine decisionale")
    fig.colorbar(im0, ax=axes[0], label="$P(q_0=1)$")

    im1 = axes[1].imshow(class_grid, origin="lower", extent=extent, cmap="viridis")
    axes[1].contour(x0_vals, x1_vals, prob_grid, levels=[0.5], colors="red", linewidths=1.2)
    axes[1].set_title(f"(b) Classe stimata con {SHOTS} shots")
    cbar = fig.colorbar(im1, ax=axes[1], ticks=[0, 1])
    cbar.set_label("Classe predetta")

    for ax in axes:
        ax.set_xlabel("Input $x_0$")
        ax.set_ylabel("Input $x_1$")
        ax.scatter(*PAPER_POINT, c="white", marker="*", s=220, edgecolors="black",
                   linewidths=0.8, label="A = (6.0, 2.7)", zorder=5)
        ax.scatter(*STABLE_POINT, c="white", marker="o", s=90, edgecolors="black",
                   linewidths=0.8, label=r"B = $(\pi,\pi)$", zorder=5)
        ax.scatter(*BOUNDARY_POINT, c="white", marker="X", s=110, edgecolors="black",
                   linewidths=0.8, label="C = (4.7, 2.7)", zorder=5)
    axes[0].legend(loc="upper left", fontsize=8, framealpha=0.9)

    plt.tight_layout()
    plt.savefig("../figure/confine_decisionale.png", dpi=200)
    print("\nFigura salvata in ../figure/confine_decisionale.png")

    np.save("../figure/prob_grid.npy", prob_grid)


if __name__ == "__main__":
    main()
