package com.musicmanager.model.users;

/**
 * Entità del modello del dominio che rappresenta l'Amministratore di Sistema.
 * Questa classe eredita dalla superclasse polimorfica {@link Utente} e ne estende
 * le prerogative operative introducendo funzionalità a privilegi elevati, in linea
 * con i requisiti di Role-Based Access Control (RBAC) definiti nelle specifiche di progetto.
 * Include le logiche di business per la moderazione dei contenuti, la gestione degli account
 * e la manutenzione dei dizionari controllati.
 */
public class Amministratore extends Utente {

    /**
     * Costruttore completo della classe Amministratore.
     * Inizializza l'istanza richiamando esplicitamente il costruttore della superclasse
     * per mappare i dati anagrafici e le credenziali di autenticazione dell'utente con privilegi.
     *
     * @param id L'identificativo univoco dell'amministratore.
     * @param email L'indirizzo email utilizzato come login primario.
     * @param password La chiave di sicurezza (hash) associata all'account.
     * @param nome Il nome proprio dell'utente.
     * @param cognome Il cognome dell'utente.
     */
    public Amministratore(String id, String email, String password, String nome, String cognome) {
        super(id, email, password, nome, cognome);
    }

    /**
     * Implementazione della logica di business associata allo Use Case UC-04 (Valutare Richieste).
     * Consente all'amministratore di esaminare una richiesta di registrazione pendente in RAM
     * ed esprimere un giudizio di approvazione o di rifiuto.
     *
     * @param idRichiesta L'identificativo univoco della richiesta di iscrizione sotto esame.
     * @param esito Valore booleano che esprime la decisione: true per approvare l'account, false per respingerlo.
     */
    public void valutaRegistrazione(String idRichiesta, boolean esito) {
        // Determiniamo lo stato testuale in forma di stringa partendo dall'esito booleano
        String stato = esito ? "APPROVATA" : "RIFIUTATA";
        System.out.println("Admin [" + getEmail() + "]: Valutazione richiesta " + idRichiesta + " -> STATO: " + stato);
    }

    /**
     * Implementazione della logica di business associata allo Use Case UC-02 (Gestione Dizionari).
     * Permette la manutenzione ordinaria delle tassonomie e dei vocabolari controllati del sistema
     * (es. aggiunta o rimozione di generi musicali o strumenti catalogati).
     *
     * @param azione La tipologia di operazione da eseguire sul dizionario (es. inserimento, cancellazione).
     * @param voce Il termine specifico oggetto della modifica.
     */
    public void gestisciDizionario(String azione, String voce) {
        System.out.println("Admin [" + getEmail() + "]: Modifica dizionario (" + azione + ") sulla voce: " + voce);
    }

    /**
     * Consente la rimozione forzata di un account utente dal sistema in caso di violazione
     * dei termini o per esigenze di manutenzione del database.
     *
     * @param idUtente L'identificativo univoco dell'utente da estromettere.
     */
    public void rimuoviUtente(String idUtente) {
        System.out.println("Admin [" + getEmail() + "]: Rimosso utente dal sistema con ID: " + idUtente);
    }

    /**
     * Esegue un'azione di moderazione restrittiva sui feedback del catalogo, permettendo
     * di oscurare o eliminare commenti o note temporali ritenuti non pertinenti o inappropriati.
     *
     * @param idCommento L'identificativo dell'istanza di commento da rimuovere.
     */
    public void moderaCommento(String idCommento) {
        System.out.println("Admin [" + getEmail() + "]: Rimosso commento non pertinente con ID: " + idCommento);
    }
}