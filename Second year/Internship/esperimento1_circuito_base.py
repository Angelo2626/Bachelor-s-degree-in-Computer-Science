import cirq
import sympy    #libreria per la matematica simbolica. Verrà usata per creare dei "segnaposto (simboli)" per gli input ed i pesi, rendendo il circuito generale e riutilizzabile.
import numpy as np
from collections import Counter     #modo per contare i risultati delle misturazioni.


q0,q1 = cirq.LineQubit.range(2)  #crea due qubit in una linea, chiamati q0 e q1.

#creazione dei parametri simbolici, li utilizzo come segnaposto.

x0 = sympy.Symbol('x0')
x1 = sympy.Symbol('x1')

#pesi (parametri addestrabili)
w0 = sympy.Symbol('w0')
w1 = sympy.Symbol('w1')
w2 = sympy.Symbol('w2')
w3 = sympy.Symbol('w3')

#costruisco il circuito: creo un oggetto circuito vuoto a cui aggiungere le porte.
circuit = cirq.Circuit()

#codifica dell'input: applico le porte Rx con i parametri di input.
circuit.append(cirq.rx(x0).on(q0))
circuit.append(cirq.rx(x1).on(q1))

#Layer parametrico (Ansatz): applico la sequenza di porte con i pesi.
circuit.append(cirq.ry(w0).on(q0))
circuit.append(cirq.ry(w1).on(q1))
circuit.append(cirq.CNOT(q0, q1)) # Porta CNOT con q0 come controllo e q1 come target
circuit.append(cirq.ry(w2).on(q0))
circuit.append(cirq.ry(w3).on(q1))

#layer di misurazione: misuro alla fine entrambi i qubit. La 'key' mi serve per recuperare i risultati dopo la misurazione.

circuit.append(cirq.measure(q0, q1, key='result'))

#stampo il circuito per visualizzarlo.
print("Struttura del circuito quantistico:")
print(circuit)

#definisco i valori numerici dell'esempio del paper.
valori_esempio = {
    x0: 6.0,
    x1: 2.7,
    w0: 0.99,
    w1: -0.50,
    w2: 3.27,
    w3: -0.69
}

#creo un "resolver" per sostituire i simboli con i valori. Utilizzo il ParamResolver che è l'oggetto di cirq che si occupa di questa sostituzione.
resolver = cirq.ParamResolver(valori_esempio)

#€seguo la simulazione: creo un'istanza del simulatore di cirq.
simulator = cirq.Simulator()

shots= 10000
results = simulator.run(circuit, param_resolver=resolver, repetitions=shots)

#analizzo i risultati: dovrei ottenere un conteggio di quante volte ogni risultato è apparso. (il risultato è un numero intero: 0(00), 1(01), 2(10), 3(11))
counts =results.histogram(key='result')
print(f"\nConteggio dei risultati su {shots} shots:")
print(counts)

#calcoloe stampo le probabilità, converto i conteggi in probabilità.
probabilita = {
    '00': counts.get(0, 0) / shots,
    '01': counts.get(1, 0) / shots,
    '10': counts.get(2, 0) / shots,
    '11': counts.get(3, 0) / shots
}

print("\nProbabilità calcolate:")
print(f"P(00): {probabilita['00']:.4f}")
print(f"P(01): {probabilita['01']:.4f}")
print(f"P(10): {probabilita['10']:.4f}")
print(f"P(11): {probabilita['11']:.4f}")

print("\nConfronto con i risultati del paper (0.26, 0.21, 0.01, 0.52):")
print("Se i risultati sono simili il circuito funziona correttamente.")

#come nel paper verifico le calssificazione (la classe è determinata da q0)
prob_q0_is_0 = probabilita['00'] + probabilita['10']
prob_q0_is_1 = probabilita['01'] + probabilita['11']

print("\nVerifica della classificazione (basata su q0):")
print(f"Probabilità che q0 sia 0: {prob_q0_is_0:.4f} (Paper: 0.27)")
print(f"Probabilità che q0 sia 1: {prob_q0_is_1:.4f} (Paper: 0.73)")

if prob_q0_is_1 > prob_q0_is_0:
    print("--> Classificato come Classe 1, come nel paper.")
else:
    print("--> Classificato come Classe 0, c'è un problema.")
