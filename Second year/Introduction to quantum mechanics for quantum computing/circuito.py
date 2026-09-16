import cirq 
circuit= cirq.Circuit()

(q0,q1)=cirq.LineQubit.range(2)
circuit.append([cirq.H(q0),cirq.CNOT(q0,q1)])   

circuit.append([cirq.measure(q0), cirq.measure(q1)])    
print(circuit)

sim= cirq.Simulator()

results= sim.run(circuit, repetitions=10)

print(results)