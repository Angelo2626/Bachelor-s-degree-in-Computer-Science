import cirq
import numpy as np
import matplotlib.pyplot as plt
from collections import defaultdict

# 1) Definisci i qubit
q0, q1 = cirq.LineQubit.range(2)
sim = cirq.Simulator()

# 2) Circuit "which-way" senza eraser
circuit_now = cirq.Circuit(
    cirq.H(q0),              # Sovrapposizione iniziale
    cirq.CNOT(q0, q1),       # Entanglement ("which-way")
    cirq.H(q0),              # Ricombinazione
    cirq.measure(q0, key='m0'),
    cirq.measure(q1, key='m1'),
)

# 3) Circuit con eraser
circuit_erase = cirq.Circuit(
    cirq.H(q0),
    cirq.CNOT(q0, q1),
    cirq.H(q1),              # ERASER: cancella l'informazione di cammino
    cirq.H(q0),
    cirq.measure(q0, key='m0'),
    cirq.measure(q1, key='m1'),
)

# 4) Funzione di simulazione e plot
def run_and_plot(circuit, title):
    result = sim.run(circuit, repetitions=2000)
    m0 = result.measurements['m0'].flatten()
    counts = np.bincount(m0, minlength=2)
    
    plt.figure(figsize=(8, 5))
    plt.bar([0,1], counts/len(m0))
    plt.xticks([0,1])
    plt.ylim(0,1)
    plt.title(title)
    plt.xlabel('Risultato m0')
    plt.ylabel('Probabilità')
    plt.show()
    
    return result

# 5) Funzione per analizzare risultati condizionati del quantum eraser
def analyze_conditional_results(result):
    """Analizza i risultati condizionandoli sul valore di q1"""
    # Estrai misurazioni
    m0 = result.measurements['m0'].flatten()
    m1 = result.measurements['m1'].flatten()
    
    # Raggruppa risultati per valore di m1
    conditional_results = defaultdict(list)
    for i in range(len(m0)):
        conditional_results[m1[i]].append(m0[i])
    
    # Plot per ogni valore condizionato
    plt.figure(figsize=(12, 5))
    
    for m1_val in sorted(conditional_results.keys()):
        counts = np.bincount(conditional_results[m1_val], minlength=2)
        total = len(conditional_results[m1_val])
        
        plt.subplot(1, 2, m1_val + 1)
        plt.bar([0, 1], counts/total)
        plt.ylim(0, 1)
        plt.title(f'Condizionato su m1={m1_val}')
        plt.xlabel('Risultato m0')
        plt.ylabel('Probabilità')
    
    plt.tight_layout()
    plt.show()

# 6) Funzione per variare l'angolo di fase e osservare lo spostamento delle frange
def phase_shift_experiment(angles):
    """Sperimenta con diverse fasi tra i due rami del percorso"""
    probabilities = []
    
    for angle in angles:
        # Circuito con phase shift
        circuit_phase = cirq.Circuit(
            cirq.H(q0),                   # Sovrapposizione
            cirq.CNOT(q0, q1),            # Entanglement
            cirq.Z(q0)**angle,            # Phase shift (angolo variabile)
            cirq.H(q1),                   # Eraser
            cirq.H(q0),                   # Ricombinazione
            cirq.measure(q0, key='m0'),
            cirq.measure(q1, key='m1'),
        )
        
        # Esegui simulazione
        result = sim.run(circuit_phase, repetitions=1000)
        m0 = result.measurements['m0'].flatten()
        m1 = result.measurements['m1'].flatten()
        
        # Calcola probabilità condizionate
        cond_prob = {}
        for m1_val in [0, 1]:
            indices = np.where(m1 == m1_val)[0]
            if len(indices) > 0:
                m0_cond = m0[indices]
                counts = np.bincount(m0_cond, minlength=2)
                cond_prob[m1_val] = counts / len(m0_cond)
            else:
                cond_prob[m1_val] = [0, 0]
        
        probabilities.append((angle, cond_prob))
    
    # Plot dei risultati
    plt.figure(figsize=(10, 6))
    angles_rad = [a * np.pi for a in angles]  # Converti in radianti per il plot
    
    for m1_val in [0, 1]:
        prob_0 = [p[1][m1_val][0] for p in probabilities]
        plt.plot(angles_rad, prob_0, 'o-', label=f'P(m0=0|m1={m1_val})')
    
    plt.xlabel('Phase shift (π radianti)')
    plt.ylabel('Probabilità condizionata P(m0=0|m1)')
    plt.title('Effetto della fase sulle frange di interferenza')
    plt.xticks([0, np.pi/4, np.pi/2, 3*np.pi/4, np.pi, 5*np.pi/4, 3*np.pi/2, 7*np.pi/4, 2*np.pi],
               ['0', 'π/4', 'π/2', '3π/4', 'π', '5π/4', '3π/2', '7π/4', '2π'])
    plt.grid(True)
    plt.legend()
    plt.show()

# 7) Implementazione di un semplice protocollo di teletrasporto quantistico
def quantum_teleportation():
    """Implementa un semplice protocollo di teletrasporto quantistico"""
    # Definisci 3 qubit: source, channel, target
    source, channel, target = cirq.LineQubit.range(3)
    
    # Prepara lo stato iniziale da teletrasportare (|+⟩)
    # Crea la coppia entangled (stato di Bell)
    # Esegui le operazioni di teletrasporto
    teleport_circuit = cirq.Circuit(
        # Prepara stato |+⟩ nel qubit source
        cirq.H(source),
        
        # Crea coppia entangled tra channel e target
        cirq.H(channel),
        cirq.CNOT(channel, target),
        
        # Operazioni di teletrasporto
        cirq.CNOT(source, channel),
        cirq.H(source),
        
        # Misurazioni
        cirq.measure(source, key='m_source'),
        cirq.measure(channel, key='m_channel'),
    )
    
    # Esegui il circuito molte volte
    result = sim.run(teleport_circuit, repetitions=100)
    m_source = result.measurements['m_source'].flatten()
    m_channel = result.measurements['m_channel'].flatten()
    
    # Per ogni combinazione di misurazioni, crea e simula il circuito di correzione
    teleport_results = []
    
    for i in range(len(m_source)):
        # Crea un circuito di verifica per questo risultato
        verify_circuit = cirq.Circuit()
        
        # Inizializza il target allo stato |0⟩
        # In un teletrasporto reale, il target avrebbe già lo stato trasferito
        # ma dobbiamo simularlo qui
        
        # Prepara lo stato |+⟩ come riferimento (lo stesso che abbiamo teletrasportato)
        verify_circuit.append(cirq.H(target))
        
        # Applica correzioni in base alle misurazioni
        if m_channel[i] == 1:
            verify_circuit.append(cirq.X(target))
        if m_source[i] == 1:
            verify_circuit.append(cirq.Z(target))
            
        # Aggiungi misurazione finale
        verify_circuit.append(cirq.measure(target, key='final'))
        
        # Simula questo circuito
        final_result = sim.run(verify_circuit, repetitions=1)
        final = final_result.measurements['final'][0, 0]
        
        teleport_results.append((m_source[i], m_channel[i], final))
    
    # Analizza i risultati
    print("\nRisultati del teletrasporto quantistico:")
    print("----------------------------------------")
    print("   m_source | m_channel | Stato finale ")
    print("----------------------------------------")
    
    counts = defaultdict(int)
    for m_s, m_c, final in teleport_results:
        counts[(m_s, m_c, final)] += 1
        
    for (m_s, m_c, final), count in sorted(counts.items()):
        print(f"      {m_s}     |     {m_c}    |     {final}      : {count} volte")
    
    # Graficamente, mostriamo la distribuzione dei risultati finali
    final_states = [result[2] for result in teleport_results]
    final_counts = np.bincount(final_states, minlength=2)
    
    plt.figure(figsize=(8, 5))
    plt.bar([0, 1], final_counts/len(final_states))
    plt.ylim(0, 1)
    plt.title('Stato finale dopo il teletrasporto')
    plt.xlabel('Stato del qubit target')
    plt.ylabel('Probabilità')
    plt.xticks([0, 1], ['|0⟩', '|1⟩'])
    plt.show()
    
    # Bonus: mostra anche correlazione tra misurazioni e risultato finale
    plt.figure(figsize=(10, 5))
    
    # Risultati condizionati sulle misure dei qubit di controllo
    for m_s in [0, 1]:
        for m_c in [0, 1]:
            indices = [(i, s, c, f) for i, (s, c, f) in enumerate(teleport_results) 
                      if s == m_s and c == m_c]
            if indices:
                finals = [f for _, _, _, f in indices]
                counts = np.bincount(finals, minlength=2)
                
                plt.subplot(2, 2, m_s*2 + m_c + 1)
                plt.bar([0, 1], counts/len(finals))
                plt.ylim(0, 1)
                plt.title(f'Risultato con m_source={m_s}, m_channel={m_c}')
                plt.xlabel('Stato finale')
                plt.ylabel('Probabilità')
    
    plt.tight_layout()
    plt.show()

# 8) Esegui e osserva l'esperimento base
print("Esecuzione esperimento base dell'eraser quantistico")
result_now = run_and_plot(circuit_now, 'Senza eraser → nessuna interferenza')
result_erase = run_and_plot(circuit_erase, 'Con eraser → interferenza')

# 9) Analisi condizionale dell'eraser quantistico
print("\nAnalisi condizionale dei risultati dell'eraser quantistico")
analyze_conditional_results(result_erase)

# 10) Sperimenta con diverse fasi
print("\nSperimento con diverse fasi tra i cammini quantistici")
angles = np.linspace(0, 2, 21)  # Da 0 a 2π in unità di π
phase_shift_experiment(angles)

# 11) Esegui l'esperimento di teletrasporto
print("\nEsecuzione esperimento di teletrasporto quantistico")
quantum_teleportation()