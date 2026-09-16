package com.musicmanager.controller;

import com.musicmanager.model.users.Utente;

/**
 * Gestore globale della sessione utente.
 * Questa classe implementa rigorosamente il Design Pattern Singleton per garantire
 * un unico punto di accesso centralizzato allo stato di autenticazione in tutta l'applicazione.
 * In questo modo si evita l'allocazione accidentale di istanze multiple in memoria e si assicura
 * che i vari controller leggano e modifichino sempre lo stesso, univoco, stato di sessione.
 */
public class SessionManager {

    // Variabile statica privata che ospita l'unica istanza consentita all'interno della JVM.
    private static SessionManager instance;

    // Riferimento polimorfico all'utente attualmente connesso.
    // Può puntare a oggetti di tipo UtenteAutorizzato o Amministratore.
    private Utente utenteLoggato;

    /**
     * Costruttore privato: è il cuore del pattern Singleton.
     * Impedisce l'utilizzo della keyword 'new' dall'esterno della classe, bloccando fisicamente
     * la creazione di istanze multiple e garantendo il controllo totale sulla memoria dedicata alla sessione.
     */
    private SessionManager() {
        // Al momento della creazione, nessun utente è autenticato, quindi il puntatore viene inizializzato a null.
        this.utenteLoggato = null;
    }

    /**
     * Metodo di accesso globale (Global Point of Access) che sfrutta la logica della Lazy Initialization.
     * Se l'istanza non esiste ancora in memoria, la JVM provvede ad allocarla.
     * Altrimenti, restituisce semplicemente il puntatore all'istanza già esistente.
     * * @return L'unica istanza valida e attiva di SessionManager.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
            System.out.println("SessionManager: Inizializzata nuova istanza Singleton in memoria.");
        }
        return instance;
    }

    // --- METODI DI BUSINESS PER LA GESTIONE DELLA SESSIONE ---

    /**
     * Associa un utente autenticato all'istanza globale, avviando di fatto una nuova sessione di navigazione.
     * * @param utente L'entità utente (verificata preventivamente dal sistema di login) che ha effettuato l'accesso.
     */
    public void login(Utente utente) {
        this.utenteLoggato = utente;
        System.out.println("SessionManager: Sessione avviata per [" + utente.getEmail() + "]");
    }

    /**
     * Chiude la sessione corrente, distruggendo il riferimento in RAM all'utente connesso
     * e ripristinando il sistema allo stato di navigazione anonima.
     */
    public void logout() {
        if (utenteLoggato != null) {
            System.out.println("SessionManager: Chiusura sessione per [" + utenteLoggato.getEmail() + "]");
            this.utenteLoggato = null;
        }
    }

    /**
     * Interroga il sistema per verificare se esiste un utente attualmente registrato nel manager di sessione.
     * * @return true se c'è un utente loggato, false se l'utente corrente è un visitatore anonimo.
     */
    public boolean isUserLogged() {
        return this.utenteLoggato != null;
    }

    /**
     * Restituisce il puntatore all'oggetto utente attualmente in sessione.
     * Essendo un riferimento polimorfico, le altre classi possono interrogarlo tramite l'operatore
     * 'instanceof' per gestire in modo dinamico i privilegi di accesso (RBAC).
     * * @return L'istanza dell'utente loggato, oppure null se nessuno ha effettuato l'accesso.
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }
}