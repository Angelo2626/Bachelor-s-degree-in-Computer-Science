package com.musicmanager.model.users;

/**
 * Classe astratta che definisce l'architettura di base e lo stato comune di un utente all'interno del sistema.
 * Questa classe centralizza la memorizzazione dei dati anagrafici fondamentali e le funzioni standard
 * di autenticazione, fungendo da superclasse per tutte le categorie di utenti (es. Amministratore, UtenteAutorizzato).
 * L'utilizzo dell'astrazione consente una gestione polimorfica delle sessioni all'interno dell'applicazione,
 * mentre l'adozione di modificatori di accesso privati garantisce il rispetto del principio di
 * incapsulamento e occultamento dell'informazione (Information Hiding).
 */
public abstract class Utente {

    // Attributi privati protetti per impedire l'accesso diretto e incontrollato dall'esterno
    private String id;
    private String email;
    private String password;
    private String nome;
    private String cognome;

    /**
     * Costruttore completo della classe astratta Utente.
     * Viene invocato in modo esplicito dalle classi derivate tramite la keyword 'super'
     * per inizializzare in modo coerente lo stato dell'oggetto nella memoria Heap.
     *
     * @param id L'identificativo univoco generato dal sistema per l'utente.
     * @param email L'indirizzo di posta elettronica, utilizzato come identificativo di login primario.
     * @param password La credenziale di sicurezza associata all'account.
     * @param nome Il nome proprio dell'utente.
     * @param cognome Il cognome dell'utente.
     */
    public Utente(String id, String email, String password, String nome, String cognome) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Metodo di business deputato alla validazione preliminare delle credenziali di accesso.
     * Verifica la corrispondenza esatta delle informazioni immesse rispetto ai dati registrati;
     * questa logica funge da aggancio logico iniziale per i successivi controlli di sessione
     * orchestrati dai controller dell'applicazione.
     *
     * @param email L'indirizzo email immesso nel modulo di autenticazione.
     * @param password La password immessa nel modulo di autenticazione.
     * @return true se le credenziali coincidono perfettamente con lo stato interno, false altrimenti.
     */
    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            System.out.println("Login effettuato con successo per l'utente: " + this.email);
            return true;
        }
        return false;
    }

    /**
     * Metodo di business deputato alla terminazione formale della sessione dell'utente corrente.
     * Notifica al sistema l'avvenuta disconnessione, predisponendo la distruzione sicura
     * dei riferimenti volatili allocati in memoria.
     */
    public void logout() {
        System.out.println("Logout effettuato per l'utente: " + this.email);
    }

    // --- METODI DI ACCESSO (GETTER E SETTER - INTERFACCIA PUBBLICA CONTROLLATA) ---

    /**
     * Restituisce l'identificativo univoco dell'utente.
     * @return La stringa contenente l'ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Configura o aggiorna l'identificativo univoco dell'utente.
     * @param id Il nuovo ID da assegnare.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Restituisce l'indirizzo email dell'utente.
     * @return La stringa dell'email di login.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Aggiorna l'indirizzo email registrato per l'account.
     * @param email La nuova email da impostare.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce la credenziale di sicurezza (password) dell'account.
     * @return La stringa della password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta o modifica la password dell'account.
     * @param password La nuova password da memorizzare.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce il nome proprio dell'utente.
     * @return La stringa del nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Aggiorna il nome dell'utente.
     * @param nome Il nome da inserire.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     * @return La stringa del cognome.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Aggiorna il cognome dell'utente.
     * @param cognome Il cognome da inserire.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
}