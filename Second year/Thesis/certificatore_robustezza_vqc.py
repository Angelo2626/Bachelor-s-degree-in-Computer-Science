"""
Esperimento 4 - Verifica sistematica a griglia e confronto con il valore esatto.

Lo script riunisce in un unico strumento le tre stime del raggio di robustezza:

  1. EPSILON ESATTO, calcolato in forma chiusa. Per questo circuito il confine
     decisionale coincide con cos(x0) = 0, quindi il raggio di robustezza in
     norma infinito e' la distanza di x0 dal piu' vicino multiplo dispari di
     pi/2. E' il riferimento rispetto al quale misurare l'errore dei metodi
     empirici.
  2. TESTING STOCASTICO, che campiona punti casuali nell'area di perturbazione.
  3. VERIFICA A GRIGLIA, che discretizza sistematicamente l'area di
     perturbazione e controlla ogni nodo della griglia.

Lo script e' organizzato in stadi indipendenti, selezionabili da riga di
comando, i cui risultati vengono accumulati in un file JSON:

    python certificatore_robustezza_vqc.py --stage confronto
    python certificatore_robustezza_vqc.py --stage shots --shots 500
    python certificatore_robustezza_vqc.py --stage certificato
    python certificatore_robustezza_vqc.py --stage figura-confronto
"""

import argparse
import json
import os
import time

import cirq
import numpy as np
import sympy
import matplotlib.pyplot as plt

SEED = 42
FIXED_WEIGHTS = [0.99, -0.50, 3.27, -0.69]

SHOTS = 2000
GRID_RESOLUTION = 20
NUM_SAMPLES = 100
EPSILON_STEP = 0.05
EPSILON_MAX = 2.5
N_REPEAT = 3

POINT_A = [6.0, 2.7]
POINT_B = [float(np.pi), float(np.pi)]

RESULTS_FILE = "../risultati.json"


# ==========================================================================
# SEZIONE 1: il "motore" del VQC
# ==========================================================================

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


def prob_q0_is_1_exact(circuit_nm, simulator, weights, input_values):
    """P(q0 = 1) esatta: nel vettore di stato l'indice vale 2*q0 + q1."""
    resolver = build_resolver(input_values, weights)
    state = simulator.simulate(circuit_nm, param_resolver=resolver)
    probs = np.abs(state.final_state_vector) ** 2
    return float(probs[2] + probs[3])


def classify_point(circuit, simulator, weights, input_values, shots=SHOTS):
    """Classe predetta (0 o 1). Con measure(q0, q1) l'istogramma restituisce
    2*q0 + q1, quindi gli esiti con q0 = 1 sono gli interi 2 e 3."""
    resolver = build_resolver(input_values, weights)
    results = simulator.run(circuit, param_resolver=resolver, repetitions=shots)
    counts = results.histogram(key="result")
    return 1 if (counts.get(2, 0) + counts.get(3, 0)) / shots > 0.5 else 0


# ==========================================================================
# SEZIONE 2: le tre strategie di stima del raggio di robustezza
# ==========================================================================

def exact_max_epsilon(point):
    """Raggio di robustezza esatto in norma infinito, in forma chiusa.

    Il confine decisionale del circuito e' l'insieme cos(x0) = 0, cioe' le rette
    verticali x0 = pi/2 + k*pi. La classificazione e' indipendente da x1, quindi
    il raggio e' la distanza di x0 dalla retta piu' vicina.
    """
    x0 = point[0]
    k = np.round((x0 - np.pi / 2) / np.pi)
    return float(abs(x0 - (np.pi / 2 + k * np.pi)))


def test_robustness_sampling(circuit, simulator, weights, center, epsilon, rng,
                             num_samples=NUM_SAMPLES, shots=SHOTS):
    """Testing stocastico: campiona punti casuali nella palla L-infinito."""
    original_class = classify_point(circuit, simulator, weights, center, shots)
    xs0 = rng.uniform(center[0] - epsilon, center[0] + epsilon, num_samples)
    xs1 = rng.uniform(center[1] - epsilon, center[1] + epsilon, num_samples)
    for k in range(num_samples):
        if classify_point(circuit, simulator, weights, [xs0[k], xs1[k]], shots) != original_class:
            return False
    return True


def verify_robustness_grid_scan(circuit, simulator, weights, center, epsilon,
                                grid_resolution=GRID_RESOLUTION, shots=SHOTS):
    """Verifica sistematica: controlla ogni nodo di una griglia regolare che
    ricopre l'area di perturbazione, estremi (e quindi vertici) inclusi.

    Restituisce True se tutti i nodi ricevono la classe del centro.
    """
    original_class = classify_point(circuit, simulator, weights, center, shots)

    x0_grid = np.linspace(center[0] - epsilon, center[0] + epsilon, grid_resolution)
    x1_grid = np.linspace(center[1] - epsilon, center[1] + epsilon, grid_resolution)

    for x0_val in x0_grid:
        for x1_val in x1_grid:
            sample_class = classify_point(circuit, simulator, weights, [x0_val, x1_val], shots)
            if sample_class != original_class:
                return False
    return True


def find_max_epsilon(check_fn, step=EPSILON_STEP, eps_max=EPSILON_MAX):
    """Ricerca incrementale del massimo epsilon che supera 'check_fn'.

    Il primo valore testato e' epsilon = 0, che verifica la sola stabilita'
    statistica del punto centrale. La ricerca si arresta al primo fallimento:
    la proprieta' di robustezza e' monotona decrescente in epsilon, quindi un
    fallimento a epsilon implica il fallimento per ogni valore maggiore.
    """
    max_eps = 0.0
    n_steps = int(round(eps_max / step)) + 1
    for k in range(n_steps):
        eps = k * step
        if check_fn(eps):
            max_eps = eps
        else:
            if k == 0:
                return None
            break
    return max_eps


# ==========================================================================
# SEZIONE 3: gestione dei risultati
# ==========================================================================

def load_results():
    if os.path.exists(RESULTS_FILE):
        with open(RESULTS_FILE) as f:
            return json.load(f)
    return {}


def save_results(data):
    with open(RESULTS_FILE, "w") as f:
        json.dump(data, f, indent=2)


# ==========================================================================
# SEZIONE 4: stadi sperimentali
# ==========================================================================

def stage_confronto(vqc, vqc_nm, simulator, rng, only_point=None):
    """Confronta testing stocastico e verifica a griglia con il valore esatto."""
    data = load_results()
    data.setdefault("confronto", {})

    print(f"Confronto dei metodi ({N_REPEAT} ripetizioni, {SHOTS} shots, "
          f"griglia {GRID_RESOLUTION}x{GRID_RESOLUTION}, {NUM_SAMPLES} campioni casuali)\n")

    todo = [("A", POINT_A), ("B", POINT_B)]
    if only_point:
        todo = [t for t in todo if t[0] == only_point]

    for name, point in todo:
        exact = exact_max_epsilon(point)
        p_exact = prob_q0_is_1_exact(vqc_nm, simulator, FIXED_WEIGHTS, point)
        print(f"Punto {name} = {np.round(point, 3).tolist()}   "
              f"P(q0=1) = {p_exact:.4f}   epsilon esatto = {exact:.4f}")

        sampling_vals, grid_vals = [], []
        t0 = time.time()
        for _ in range(N_REPEAT):
            eps = find_max_epsilon(
                lambda e: test_robustness_sampling(vqc, simulator, FIXED_WEIGHTS, point, e, rng)
            )
            sampling_vals.append(0.0 if eps is None else eps)
        t_sampling = time.time() - t0

        t0 = time.time()
        for _ in range(N_REPEAT):
            eps = find_max_epsilon(
                lambda e: verify_robustness_grid_scan(vqc, simulator, FIXED_WEIGHTS, point, e)
            )
            grid_vals.append(0.0 if eps is None else eps)
        t_grid = time.time() - t0

        print(f"  testing stocastico : {np.mean(sampling_vals):.2f} +/- {np.std(sampling_vals):.2f}"
              f"   errore {np.mean(sampling_vals) - exact:+.3f}   ({t_sampling / N_REPEAT:.1f} s/ripetizione)")
        print(f"  verifica a griglia : {np.mean(grid_vals):.2f} +/- {np.std(grid_vals):.2f}"
              f"   errore {np.mean(grid_vals) - exact:+.3f}   ({t_grid / N_REPEAT:.1f} s/ripetizione)\n")

        data["confronto"][name] = {
            "point": list(point),
            "p_exact": p_exact,
            "epsilon_exact": exact,
            "sampling": sampling_vals,
            "grid": grid_vals,
            "time_sampling": t_sampling / N_REPEAT,
            "time_grid": t_grid / N_REPEAT,
        }
    save_results(data)


def stage_shots(vqc, vqc_nm, simulator, shots, n_repeat=N_REPEAT):
    """Studia come il numero di shots influenza la verifica a griglia."""
    data = load_results()
    data.setdefault("shots", {})

    exact = exact_max_epsilon(POINT_A)
    vals = []
    t0 = time.time()
    for _ in range(n_repeat):
        eps = find_max_epsilon(
            lambda e: verify_robustness_grid_scan(vqc, simulator, FIXED_WEIGHTS, POINT_A, e, shots=shots)
        )
        vals.append(0.0 if eps is None else eps)
    elapsed = (time.time() - t0) / n_repeat

    print(f"shots = {shots:>5}   epsilon = {np.mean(vals):.2f} +/- {np.std(vals):.2f}"
          f"   errore {np.mean(vals) - exact:+.3f}   ({elapsed:.1f} s/ripetizione)   valori {vals}")

    data["shots"][str(shots)] = {"values": vals, "time": elapsed, "epsilon_exact": exact}
    save_results(data)


def stage_certificato(vqc, vqc_nm, simulator):
    """Genera il certificato visivo per il punto A."""
    data = load_results()
    grid_vals = data.get("confronto", {}).get("A", {}).get("grid")
    if not grid_vals:
        raise SystemExit("Eseguire prima lo stadio 'confronto'.")
    max_eps = float(np.mean(grid_vals))
    exact = exact_max_epsilon(POINT_A)

    view = 2.2
    resolution = 70
    x0_vals = np.linspace(POINT_A[0] - view, POINT_A[0] + view, resolution)
    x1_vals = np.linspace(POINT_A[1] - view, POINT_A[1] + view, resolution)

    class_grid = np.zeros((resolution, resolution))
    prob_grid = np.zeros((resolution, resolution))
    for i, a in enumerate(x0_vals):
        for j, b in enumerate(x1_vals):
            class_grid[j, i] = classify_point(vqc, simulator, FIXED_WEIGHTS, [a, b], shots=200)
            prob_grid[j, i] = prob_q0_is_1_exact(vqc_nm, simulator, FIXED_WEIGHTS, [a, b])

    plt.figure(figsize=(8, 6.5))
    plt.imshow(class_grid, origin="lower",
               extent=[x0_vals[0], x0_vals[-1], x1_vals[0], x1_vals[-1]],
               cmap="viridis", aspect="equal")
    plt.contour(x0_vals, x1_vals, prob_grid, levels=[0.5], colors="red", linewidths=1.5)
    plt.scatter([POINT_A[0]], [POINT_A[1]], c="white", marker="*", s=240,
                edgecolors="black", linewidths=0.8, label="Punto A = (6.0, 2.7)")

    plt.gca().add_patch(plt.Rectangle(
        (POINT_A[0] - max_eps, POINT_A[1] - max_eps), 2 * max_eps, 2 * max_eps,
        linewidth=2.2, edgecolor="white", facecolor="none",
        label=f"Verifica a griglia ($\\varepsilon$={max_eps:.2f})"))
    plt.gca().add_patch(plt.Rectangle(
        (POINT_A[0] - exact, POINT_A[1] - exact), 2 * exact, 2 * exact,
        linewidth=2.2, edgecolor="red", linestyle="--", facecolor="none",
        label=f"Raggio esatto ($\\varepsilon$={exact:.2f})"))

    plt.title("Certificato di robustezza empirico per il punto A")
    plt.xlabel("Input $x_0$")
    plt.ylabel("Input $x_1$")
    plt.legend(loc="upper right", fontsize=8, framealpha=0.9)
    cbar = plt.colorbar(ticks=[0, 1])
    cbar.set_label("Classe predetta")
    plt.tight_layout()
    plt.savefig("../figure/certificato_robustezza.png", dpi=200)
    plt.close()
    print("Figura salvata in ../figure/certificato_robustezza.png")


def stage_figura_confronto():
    """Grafico riassuntivo dell'effetto del numero di shots."""
    data = load_results()
    if "shots" not in data:
        raise SystemExit("Eseguire prima lo stadio 'shots'.")
    keys = sorted(data["shots"], key=int)
    xs = [int(k) for k in keys]
    means = [float(np.mean(data["shots"][k]["values"])) for k in keys]
    stds = [float(np.std(data["shots"][k]["values"])) for k in keys]
    exact = data["shots"][keys[0]]["epsilon_exact"]

    sampling = data.get("confronto", {}).get("A", {}).get("sampling")

    plt.figure(figsize=(7.5, 4.8))
    plt.errorbar(xs, means, yerr=stds, marker="o", capsize=4, color="#2166ac",
                 label="Verifica a griglia $20\\times20$")
    plt.axhline(exact, color="red", linestyle="--", linewidth=1.6,
                label=f"Raggio esatto = {exact:.3f}")
    if sampling:
        plt.errorbar([SHOTS], [np.mean(sampling)], yerr=[np.std(sampling)], marker="s",
                     capsize=4, color="#b2182b", linestyle="none",
                     label="Testing stocastico (2000 shots)")
    plt.xscale("log")
    plt.xlabel("Numero di shots per punto")
    plt.ylabel("Raggio di robustezza stimato $\\varepsilon$")
    plt.title("Convergenza della stima al crescere degli shots (punto A)")
    plt.legend(fontsize=9)
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.savefig("../figure/effetto_shots.png", dpi=200)
    plt.close()
    print("Figura salvata in ../figure/effetto_shots.png")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--stage", default="confronto",
                        choices=["confronto", "shots", "certificato", "figura-confronto"])
    parser.add_argument("--shots", type=int, default=SHOTS)
    parser.add_argument("--repeat", type=int, default=N_REPEAT)
    parser.add_argument("--point", default=None, choices=["A", "B"])
    args = parser.parse_args()

    if args.stage == "figura-confronto":
        stage_figura_confronto()
        return

    vqc = create_vqc(with_measurement=True)
    vqc_nm = create_vqc(with_measurement=False)
    simulator = cirq.Simulator(seed=SEED)
    rng = np.random.default_rng(SEED)

    if args.stage == "confronto":
        stage_confronto(vqc, vqc_nm, simulator, rng, args.point)
    elif args.stage == "shots":
        stage_shots(vqc, vqc_nm, simulator, args.shots, args.repeat)
    elif args.stage == "certificato":
        stage_certificato(vqc, vqc_nm, simulator)


if __name__ == "__main__":
    main()
