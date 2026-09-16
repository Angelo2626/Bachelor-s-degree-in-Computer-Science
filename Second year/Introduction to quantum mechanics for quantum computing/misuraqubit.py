import cirq

# Creiamo un qubit
qubit = cirq.GridQubit(0, 0)

# Creiamo un circuito quantistico
circuit = cirq.Circuit(
    cirq.H(qubit),   # Porta Hadamard (sovrapposizione)
    cirq.measure(qubit, key='m')  # Misura il qubit con nome "m"
)

print("Circuito quantistico con Cirq:")
print(circuit)

# Simuliamo il circuito
simulator = cirq.Simulator()
result = simulator.run(circuit, repetitions=1000)

# Stampiamo i risultati della misura
print("\nRisultati della misura:")
print(result.data)  # Mostra i dati grezzi delle misure
