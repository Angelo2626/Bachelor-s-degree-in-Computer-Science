package com.musicmanager.model.users;

import java.util.Date;

/**
 * Classe che modella un utente autorizzato all'interno del sistema.
 * Questa classe estende la superclasse astratta {@link Utente}, definendo il profilo
 * di quegli utenti che hanno completato la registrazione e possiedono i permessi standard
 * per interagire con il catalogo multimediale (es. inserimento di brani e pubblicazione di feedback).
 * Include un flag di stato per gestire l'abilitazione del profilo a seguito della moderazione,
 * garantendo il controllo granulare degli accessi.
 */
public class UtenteAutorizzato extends Utente {

    private Date dataRegistrazione;

    // In linea con i principi della programmazione difensiva, ogni nuovo utente
    // inserito nel sistema è disattivato per impostazione predefinita, in attesa
    // della convalida da parte di un amministratore.
    private boolean attivo = false;

    /**
     * Costruttore completo della classe UtenteAutorizzato.
     * Inizializza l'istanza trasferendo i dati anagrafici essenziali al costruttore
     * della superclasse e mappa il riferimento temporale dell'avvenuta iscrizione.
     *
     * @param id L'identificativo univoco assegnato all'utente.
     * @param email L'indirizzo email utilizzato come credenziale di login primaria.
     * @param password La chiave di sicurezza (hash) dell'account.
     * @param nome Il nome proprio dell'utente.
     * @param cognome Il cognome dell'utente.
     * @param dataRegistrazione Il marcatore temporale che attesta il momento dell'iscrizione.
     */
    public UtenteAutorizzato(String id, String email, String password, String nome, String cognome, Date dataRegistrazione) {
        super(id, email, password, nome, cognome);
        this.dataRegistrazione = dataRegistrazione;
    }

    // --- METODI DI ACCESSO (GETTER E SETTER) ---

    /**
     * Interroga lo stato dell'account per verificare se l'utente è abilitato
     * a effettuare operazioni di scrittura all'interno del sistema.
     *
     * @return true se l'account è attivo, false se è ancora sospeso o in attesa di moderazione.
     */
    public boolean isAttivo() {
        return attivo;
    }

    /**
     * Aggiorna lo stato di attivazione del profilo utente.
     * Questo metodo permette ai controller di abilitare o revocare l'accesso alle funzionalità protette.
     *
     * @param attivo Il valore booleano da assegnare allo stato dell'account.
     */
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    /**
     * Restituisce la data in cui è stata sottomessa la richiesta di iscrizione iniziale.
     *
     * @return L'oggetto Date corrispondente al momento della registrazione.
     */
    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * Configura o aggiorna il marcatore temporale di registrazione dell'utente.
     *
     * @param dataRegistrazione La nuova data da associare al profilo.
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    // --- METODI DI BUSINESS (PLACEHOLDER PER LOGICA FUTURA) ---

    /**
     * Permette all'utente di proporre l'allocazione di una nuova opera musicale nel catalogo.
     * Attualmente implementato come metodo stub (placeholder) per consentire i test di integrazione
     * in attesa dello sviluppo dei moduli di persistenza dedicati.
     *
     * @param dati Stringa contenente i metadati descrittivi del brano da inserire.
     */
    public void inserisciBrano(String dati) {
        System.out.println("Inserimento brano...");
    }

    /**
     * Consente all'utente di inserire una recensione o una nota testuale a corredo di un brano.
     * Funge da punto di aggancio architetturale per lo sviluppo delle future logiche di interazione.
     *
     * @param c Il testo o il corpo del commento da pubblicare nel catalogo.
     */
    public void aggiungiCommento(String c) {
        System.out.println("Aggiunta commento...");
    }
}