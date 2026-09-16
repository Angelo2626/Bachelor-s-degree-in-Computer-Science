import cirq
import matplotlib.pyplot as plt

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
print(result.data)

# Conta quante volte abbiamo ottenuto 0 e 1
counts = result.data['m'].value_counts()
print("\nConteggio delle misure (quanti 0 e quanti 1):")
print(counts)

# Grafico della distribuzione delle misure
counts.plot(kind='bar', color=['blue', 'orange'])
plt.xlabel("Stati misurati")
plt.ylabel("Occorrenze")
plt.title("Distribuzione delle misure del qubit")
plt.xticks(rotation=0)
plt.show()
