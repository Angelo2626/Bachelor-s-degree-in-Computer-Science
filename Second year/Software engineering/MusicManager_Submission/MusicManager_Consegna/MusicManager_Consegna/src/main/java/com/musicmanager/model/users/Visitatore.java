package com.musicmanager.model.users;

/**
 * Classe che modella l'attore "Guest" o Visitatore non ancora autenticato all'interno del sistema.
 * Questa entità estende la superclasse polimorfica {@link Utente}, ereditandone la struttura
 * anagrafica di base. In linea con i vincoli di sicurezza dell'applicazione, il raggio
 * d'azione di questa classe è volutamente ristretto e limitato in modo esclusivo alla sottomissione
 * della richiesta formale di iscrizione alla piattaforma, impedendo qualsiasi interazione diretta
 * o non tracciata con il catalogo musicale protetto.
 */
public class Visitatore extends Utente {

    /**
     * Costruttore completo della classe Visitatore.
     * Inizializza l'istanza trasferendo i parametri anagrafici e di contatto al costruttore
     * della superclasse tramite la keyword 'super', allocando correttamente questa entità
     * transitoria all'interno della memoria Heap.
     *
     * @param id L'identificativo univoco provvisorio generato per la sessione del visitatore.
     * @param email L'indirizzo email che l'utente intende associare al futuro account.
     * @param password La chiave di sicurezza protetta scelta per l'autenticazione.
     * @param nome Il nome proprio del richiedente.
     * @param cognome Il cognome del richiedente.
     */
    public Visitatore(String id, String email, String password, String nome, String cognome) {
        // Trasferimento dei dati al costruttore della classe madre per l'inizializzazione atomica
        super(id, email, password, nome, cognome);
    }

    /**
     * Implementazione della logica di business associata allo Use Case UC-03 (Richiesta Registrazione).
     * Consente al visitatore anonimo di inoltrare i propri dati di profilazione al sistema,
     * formalizzando un pacchetto informativo che verrà successivamente preso in carico ed esaminato
     * dal modulo di gestione delle utenze e dagli amministratori.
     *
     * @param datiForm La stringa serializzata contenente i dati compilati nel modulo di iscrizione.
     */
    public void richiediRegistrazione(String datiForm) {
        System.out.println("Token Visitatore [" + getEmail() + "]: Richiesta sottomessa con successo. Dati: " + datiForm);
    }
}