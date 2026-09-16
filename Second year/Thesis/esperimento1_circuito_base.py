"""
Esperimento 1 - Validazione del circuito VQC di riferimento.

Obiettivo: verificare che l'implementazione in Cirq del circuito di Assolini et
al. riproduca la distribuzione di probabilita' pubblicata nell'articolo per
l'input [x0, x1] = [6.0, 2.7] e i pesi [0.99, -0.50, 3.27, -0.69].

NOTA SULLA CONVENZIONE DEI QUBIT (punto critico dell'intero lavoro).
  - Cirq: 'cirq.measure(q0, q1)' produce un intero costruito in big-endian
    sull'ordine di misura, cioe' n = 2*q0 + q1. Quindi q0 e' il bit PIU'
    significativo e q0 = 1 corrisponde agli interi 2 e 3.
    Lo stesso vale per l'indice del vettore di stato restituito da simulate().
  - Articolo di riferimento: la notazione usata e' |q1 q0>, cioe' q0 e' il bit
    MENO significativo (lo affermano esplicitamente nella Sezione 3).
  Le due convenzioni differiscono solo per lo scambio delle etichette 01 e 10.
"""

import cirq
import sympy
import numpy as np

SEED = 42
SHOTS = 10000

# Parametri dell'esempio dell'articolo di riferimento
INPUT_VALUES = [6.0, 2.7]
FIXED_WEIGHTS = [0.99, -0.50, 3.27, -0.69]

# Distribuzione pubblicata nell'articolo, in notazione |q1 q0>
PAPER_DIST = {"00": 0.26, "01": 0.21, "10": 0.01, "11": 0.52}
PAPER_P_Q0_IS_1 = 0.73


def create_vqc(with_measurement=True):
    """Crea e restituisce la struttura del circuito VQC parametrico.

    Se with_measurement e' False il circuito viene restituito senza la porta di
    misura, in modo da poter essere usato con simulate() per ottenere le
    ampiezze esatte dello stato finale.
    """
    q0, q1 = cirq.LineQubit.range(2)
    x0, x1 = sympy.symbols("x0 x1")
    w0, w1, w2, w3 = sympy.symbols("w0 w1 w2 w3")

    circuit = cirq.Circuit()
    # Stadio di encoding
    circuit.append(cirq.rx(x0).on(q0))
    circuit.append(cirq.rx(x1).on(q1))
    # Ansatz variazionale
    circuit.append(cirq.ry(w0).on(q0))
    circuit.append(cirq.ry(w1).on(q1))
    circuit.append(cirq.CNOT(q0, q1))
    circuit.append(cirq.ry(w2).on(q0))
    circuit.append(cirq.ry(w3).on(q1))
    # Stadio di misurazione
    if with_measurement:
        circuit.append(cirq.measure(q0, q1, key="result"))

    return circuit


def build_resolver(input_values, weights_values):
    """Associa ai simboli del circuito i valori numerici dell'esperimento."""
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


def exact_distribution(circuit_no_measure, simulator, weights_values, input_values):
    """Distribuzione di probabilita' esatta, senza rumore di campionamento.

    Restituisce un array di 4 elementi indicizzato secondo la convenzione Cirq
    (indice = 2*q0 + q1).
    """
    resolver = build_resolver(input_values, weights_values)
    result = simulator.simulate(circuit_no_measure, param_resolver=resolver)
    return np.abs(result.final_state_vector) ** 2


def sampled_distribution(circuit, simulator, weights_values, input_values, shots=SHOTS):
    """Distribuzione di probabilita' stimata da 'shots' misurazioni ripetute."""
    resolver = build_resolver(input_values, weights_values)
    results = simulator.run(circuit, param_resolver=resolver, repetitions=shots)
    counts = results.histogram(key="result")
    return np.array([counts.get(i, 0) / shots for i in range(4)])


def main():
    vqc = create_vqc(with_measurement=True)
    vqc_no_measure = create_vqc(with_measurement=False)
    simulator = cirq.Simulator(seed=SEED)

    print("Struttura del circuito quantistico:")
    print(vqc)

    exact = exact_distribution(vqc_no_measure, simulator, FIXED_WEIGHTS, INPUT_VALUES)
    sampled = sampled_distribution(vqc, simulator, FIXED_WEIGHTS, INPUT_VALUES)

    # Corrispondenza fra le due notazioni: indice Cirq -> etichetta |q1 q0>
    labels_paper = {0: "00", 1: "10", 2: "01", 3: "11"}

    print(f"\nDistribuzione di probabilita' ({SHOTS} shots)\n")
    header = f"{'Cirq (q0q1)':>12} {'Paper (q1q0)':>13} {'Esatta':>9} {'Misurata':>10} {'Paper':>7}"
    print(header)
    print("-" * len(header))
    for i in range(4):
        cirq_label = f"{i // 2}{i % 2}"
        paper_label = labels_paper[i]
        print(
            f"{cirq_label:>12} {paper_label:>13} {exact[i]:>9.4f} "
            f"{sampled[i]:>10.4f} {PAPER_DIST[paper_label]:>7.2f}"
        )

    # Classificazione: si osserva il solo qubit q0.
    # In Cirq q0 = 1 corrisponde agli interi 2 e 3 (n = 2*q0 + q1).
    p_q0_1_exact = exact[2] + exact[3]
    p_q0_1_sampled = sampled[2] + sampled[3]

    print("\nClassificazione basata su q0:")
    print(f"  P(q0 = 1) esatta   : {p_q0_1_exact:.4f}")
    print(f"  P(q0 = 1) misurata : {p_q0_1_sampled:.4f}")
    print(f"  P(q0 = 1) articolo : {PAPER_P_Q0_IS_1:.2f}")
    predicted = 1 if p_q0_1_sampled > 0.5 else 0
    print(f"  --> Classe predetta: {predicted} (articolo: 1)")

    max_dev = np.max(np.abs(sampled - exact))
    print(f"\nScarto massimo fra distribuzione misurata ed esatta: {max_dev:.4f}")
    print(f"Deviazione standard attesa dello stimatore: {np.sqrt(0.25 / SHOTS):.4f}")


if __name__ == "__main__":
    main()
