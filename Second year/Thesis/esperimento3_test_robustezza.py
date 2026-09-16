"""
Esperimento 3 - Instabilita' statistica e testing stocastico della robustezza.

Il modulo e' diviso in due parti.

  Parte A: studio della stabilita' della classificazione a input FISSO. Lo
  stesso punto viene classificato molte volte; se la probabilita' esatta e'
  lontana da 0.5 la classe non cambia mai, se e' vicina a 0.5 la classe oscilla
  fra esecuzioni successive. Questo isola l'effetto del campionamento finito
  dalla geometria del confine decisionale.

  Parte B: stima del raggio di robustezza epsilon tramite testing stocastico,
  cioe' campionando punti casuali nella palla in norma infinito centrata sul
  punto di test. La stima viene ripetuta piu' volte per quantificarne la
  variabilita'.
"""

import time

import cirq
import numpy as np
import sympy
import matplotlib.pyplot as plt
from tqdm import trange

SEED = 42
FIXED_WEIGHTS = [0.99, -0.50, 3.27, -0.69]

SHOTS = 2000
NUM_SAMPLES = 100
EPSILON_STEP = 0.05
EPSILON_MAX = 2.5
N_REPEAT = 5
N_STABILITY = 200

POINT_A = [6.0, 2.7]        # punto di test dell'articolo di riferimento
POINT_B = [np.pi, np.pi]    # punto interno alla principale regione di Classe 0
POINT_C = [4.7, 2.7]        # punto praticamente sul confine decisionale


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
    """Probabilita' esatta che q0 valga 1 (indici 2 e 3 del vettore di stato)."""
    resolver = build_resolver(input_values, weights_values)
    state = simulator.simulate(circuit_no_measure, param_resolver=resolver)
    probs = np.abs(state.final_state_vector) ** 2
    return float(probs[2] + probs[3])


def prob_q0_is_1_sampled(circuit, simulator, weights_values, input_values, shots=SHOTS):
    """Stima campionaria di P(q0 = 1): con measure(q0, q1) gli esiti con
    q0 = 1 sono gli interi 2 e 3, poiche' l'istogramma vale 2*q0 + q1."""
    resolver = build_resolver(input_values, weights_values)
    results = simulator.run(circuit, param_resolver=resolver, repetitions=shots)
    counts = results.histogram(key="result")
    return (counts.get(2, 0) + counts.get(3, 0)) / shots


def classify_point(circuit, simulator, weights_values, input_values, shots=SHOTS):
    """Classe predetta (0 o 1) per un singolo input."""
    return 1 if prob_q0_is_1_sampled(circuit, simulator, weights_values, input_values, shots) > 0.5 else 0


# ---------------------------------------------------------------------------
# Parte A: stabilita' della classificazione a input fisso
# ---------------------------------------------------------------------------

def stability_study(circuit, circuit_nm, simulator, weights, point, n_runs=N_STABILITY, shots=SHOTS):
    """Classifica ripetutamente lo stesso punto e restituisce le stime di
    P(q0 = 1) insieme alla frazione di esecuzioni che assegnano la Classe 1."""
    exact = prob_q0_is_1_exact(circuit_nm, simulator, weights, point)
    estimates = np.array(
        [prob_q0_is_1_sampled(circuit, simulator, weights, point, shots) for _ in range(n_runs)]
    )
    fraction_class_1 = float(np.mean(estimates > 0.5))
    return exact, estimates, fraction_class_1


# ---------------------------------------------------------------------------
# Parte B: testing stocastico del raggio di robustezza
# ---------------------------------------------------------------------------

def test_robustness_sampling(circuit, simulator, weights, center_point, epsilon,
                             rng, num_samples=NUM_SAMPLES, shots=SHOTS):
    """Testa la robustezza campionando punti casuali nella palla L-infinito.

    Restituisce True se tutti i campioni ricevono la stessa classe del centro.
    Con epsilon = 0 il test si riduce a classificare ripetutamente il centro,
    e quindi misura la sola stabilita' statistica del punto.
    """
    original_class = classify_point(circuit, simulator, weights, center_point, shots)

    xs0 = rng.uniform(center_point[0] - epsilon, center_point[0] + epsilon, num_samples)
    xs1 = rng.uniform(center_point[1] - epsilon, center_point[1] + epsilon, num_samples)

    for k in range(num_samples):
        sample_class = classify_point(circuit, simulator, weights, [xs0[k], xs1[k]], shots)
        if sample_class != original_class:
            return False
    return True


def find_max_epsilon_sampling(circuit, simulator, weights, point, rng,
                              step=EPSILON_STEP, eps_max=EPSILON_MAX, verbose=True):
    """Ricerca incrementale del massimo epsilon che supera il testing stocastico.

    Il primo valore testato e' epsilon = 0: se il test fallisce gia' li', il
    punto non e' statisticamente stabile e il raggio di robustezza e' nullo.
    """
    n_steps = int(round(eps_max / step)) + 1
    max_eps = 0.0
    for k in trange(n_steps, desc="Testing epsilon", disable=not verbose):
        eps = k * step
        if test_robustness_sampling(circuit, simulator, weights, point, eps, rng):
            max_eps = eps
        else:
            if k == 0:
                return None  # instabile gia' a perturbazione nulla
            break
    return max_eps


def plot_local_map(circuit, circuit_nm, simulator, weights, center_point, max_eps,
                   filename, title, shots=200, resolution=60):
    """Mappa locale attorno al punto di test con il quadrato robusto trovato."""
    view = max(max_eps * 1.6, 0.5)
    x0_vals = np.linspace(center_point[0] - view, center_point[0] + view, resolution)
    x1_vals = np.linspace(center_point[1] - view, center_point[1] + view, resolution)

    class_grid = np.zeros((resolution, resolution))
    prob_grid = np.zeros((resolution, resolution))
    for i, a in enumerate(x0_vals):
        for j, b in enumerate(x1_vals):
            class_grid[j, i] = classify_point(circuit, simulator, weights, [a, b], shots)
            prob_grid[j, i] = prob_q0_is_1_exact(circuit_nm, simulator, weights, [a, b])

    plt.figure(figsize=(7.5, 6))
    plt.imshow(class_grid, origin="lower",
               extent=[x0_vals[0], x0_vals[-1], x1_vals[0], x1_vals[-1]],
               cmap="viridis", aspect="equal")
    plt.contour(x0_vals, x1_vals, prob_grid, levels=[0.5], colors="red", linewidths=1.5)
    plt.scatter([center_point[0]], [center_point[1]], c="white", marker="*", s=230,
                edgecolors="black", linewidths=0.8, label="Punto di test")
    rect = plt.Rectangle(
        (center_point[0] - max_eps, center_point[1] - max_eps),
        2 * max_eps, 2 * max_eps,
        linewidth=2, edgecolor="white", facecolor="none",
        label=f"Area robusta stimata ($\\varepsilon$={max_eps:.2f})",
    )
    plt.gca().add_patch(rect)
    plt.title(title)
    plt.xlabel("Input $x_0$")
    plt.ylabel("Input $x_1$")
    plt.legend(loc="upper right", fontsize=8, framealpha=0.9)
    cbar = plt.colorbar(ticks=[0, 1])
    cbar.set_label("Classe predetta")
    plt.tight_layout()
    plt.savefig(filename, dpi=200)
    plt.close()


def main():
    vqc = create_vqc(with_measurement=True)
    vqc_nm = create_vqc(with_measurement=False)
    simulator = cirq.Simulator(seed=SEED)
    rng = np.random.default_rng(SEED)

    # ---------------- Parte A ----------------
    print("=" * 70)
    print("PARTE A - Stabilita' della classificazione a input fisso")
    print("=" * 70)
    print(f"{N_STABILITY} classificazioni ripetute dello stesso punto, {SHOTS} shots ciascuna.\n")

    labels = ["A = (6.0, 2.7)", "B = (pi, pi)", "C = (4.7, 2.7)"]
    points = [POINT_A, POINT_B, POINT_C]
    all_estimates = []

    header = f"{'Punto':<16}{'P(q0=1) esatta':>16}{'scarto da 0.5':>15}{'% Classe 1':>13}"
    print(header)
    print("-" * len(header))
    for label, pt in zip(labels, points):
        exact, estimates, frac = stability_study(vqc, vqc_nm, simulator, FIXED_WEIGHTS, pt)
        all_estimates.append(estimates)
        print(f"{label:<16}{exact:>16.4f}{abs(exact - 0.5):>15.4f}{frac * 100:>12.1f}%")

    print(f"\nDeviazione standard teorica dello stimatore con {SHOTS} shots: "
          f"{np.sqrt(0.25 / SHOTS):.4f}")

    plt.figure(figsize=(8, 4.5))
    colors = ["#2166ac", "#4d9221", "#b2182b"]
    for estimates, label, color in zip(all_estimates, labels, colors):
        plt.hist(estimates, bins=25, alpha=0.65, label=label, color=color)
    plt.axvline(0.5, color="black", linestyle="--", linewidth=1.5, label="Soglia di decisione")
    plt.xlabel("$\\hat{P}(q_0=1)$ stimata")
    plt.ylabel("Frequenza")
    plt.title(f"Distribuzione della stima su {N_STABILITY} esecuzioni ripetute")
    plt.legend(fontsize=9)
    plt.tight_layout()
    plt.savefig("../figure/instabilita_statistica.png", dpi=200)
    plt.close()
    print("Figura salvata in ../figure/instabilita_statistica.png")

    # ---------------- Parte B ----------------
    print("\n" + "=" * 70)
    print("PARTE B - Testing stocastico del raggio di robustezza")
    print("=" * 70)
    print(f"{NUM_SAMPLES} campioni casuali per valore di epsilon, {SHOTS} shots, "
          f"{N_REPEAT} ripetizioni indipendenti.\n")

    results = {}
    for label, pt in zip(labels, points):
        values = []
        start = time.time()
        for r in range(N_REPEAT):
            eps = find_max_epsilon_sampling(vqc, simulator, FIXED_WEIGHTS, pt, rng, verbose=False)
            values.append(0.0 if eps is None else eps)
        elapsed = time.time() - start
        results[label] = (np.mean(values), np.std(values), values, elapsed)
        print(f"{label:<16} epsilon = {np.mean(values):.2f} +/- {np.std(values):.2f}   "
              f"valori: {[round(v, 2) for v in values]}   ({elapsed:.1f} s)")

    eps_a = results[labels[0]][0]
    plot_local_map(vqc, vqc_nm, simulator, FIXED_WEIGHTS, POINT_A, eps_a,
                   "../figure/robustezza_testing_A.png",
                   "Testing stocastico nel punto A = (6.0, 2.7)")
    eps_b = results[labels[1]][0]
    plot_local_map(vqc, vqc_nm, simulator, FIXED_WEIGHTS, POINT_B, eps_b,
                   "../figure/robustezza_testing_B.png",
                   "Testing stocastico nel punto B = $(\\pi,\\pi)$")
    print("\nFigure salvate in ../figure/robustezza_testing_A.png e ../figure/robustezza_testing_B.png")


if __name__ == "__main__":
    main()
