package com.musicmanager.model.catalog;

import com.musicmanager.model.feedback.Commento;
import java.util.ArrayList;
import java.util.List;

/**
 * Entità fondamentale del modello del dominio che rappresenta un brano musicale.
 * La classe funge da aggregatore principale memorizzando i metadati descrittivi della traccia,
 * i riferimenti ai file multimediali digitali associati e la collezione di feedback
 * e note temporali inseriti dagli utenti autorizzati.
 */
public class Brano {

    // attributi primari per l'identificazione e la categorizzazione del brano
    private String titolo;
    private int annoComposizione;

    // Campo introdotto per soddisfare il requisito di validazione tramite dizionari di sistema
    private String autorePrincipale;
    private String proprietarioEmail;

    // Campo introdotto per soddisfare il requisito di validazione tramite dizionari di sistema
    private String genere;

    // Strutture dati per la gestione delle relazioni uno-a-molti con i documenti e i commenti
    private List<DocumentoDigitale> documentiAssociati;
    private List<Commento> commenti;

    /**
     * Costruttore completo della classe Brano.
     * Inizializza tutti i metadati obbligatori del brano e predispone le liste
     * vuote in memoria per accogliere i documenti multimediali e i commenti futuri,
     * evitando anomalie da puntatore nullo.
     * * @param titolo Il nome o titolo identificativo dell'opera musicale.
     * @param annoComposizione L'anno in cui il brano è stato composto o pubblicato.
     * @param autorePrincipale Il compositore, l'artista o la band di riferimento, preventivamente validato dal dizionario.
     * @param proprietarioEmail L'indirizzo email dell'utente o autore che possiede l'istanza.
     * @param genere Il genere musicale associato, preventivamente validato dal dizionario.
     */
    public Brano(String titolo, int annoComposizione, String autorePrincipale, String proprietarioEmail, String genere) {
        this.titolo = titolo;
        this.annoComposizione = annoComposizione;
        this.autorePrincipale = autorePrincipale;
        this.proprietarioEmail = proprietarioEmail;
        this.genere = genere;
        this.documentiAssociati = new ArrayList<>();
        this.commenti = new ArrayList<>();
    }

    // --- METODI DI ACCESSO (GETTER E SETTER) ---

    /**
     * Restituisce il titolo del brano.
     * @return La stringa del titolo.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta o aggiorna il titolo del brano.
     * @param titolo Il nuovo titolo da assegnare.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Restituisce l'anno di composizione dell'opera.
     * @return L'anno espresso come numero intero.
     */
    public int getAnnoComposizione() {
        return annoComposizione;
    }

    /**
     * Imposta o aggiorna l'anno di composizione.
     * @param annoComposizione Il valore numerico dell'anno.
     */
    public void setAnnoComposizione(int annoComposizione) {
        this.annoComposizione = annoComposizione;
    }

    /**
     * Restituisce il nome dell'autore o interprete principale.
     * @return La stringa contenente l'autore.
     */
    public String getAutorePrincipale() {
        return autorePrincipale;
    }

    /**
     * Imposta o modifica il riferimento all'autore principale.
     * @param autorePrincipale Il nome dell'autore da impostare.
     */
    public void setAutorePrincipale(String autorePrincipale) {
        this.autorePrincipale = autorePrincipale;
    }

    /**
     * Restituisce l'email dell'utente proprietario di questa scheda brano.
     * @return L'indirizzo email di riferimento.
     */
    public String getProprietarioEmail() {
        return proprietarioEmail;
    }

    /**
     * Assegna la proprietà del brano a un utente tramite il suo indirizzo email.
     * @param proprietarioEmail L'email del nuovo proprietario.
     */
    public void setProprietarioEmail(String proprietarioEmail) {
        this.proprietarioEmail = proprietarioEmail;
    }

    /**
     * Restituisce il genere musicale associato al brano.
     * @return La stringa del genere.
     */
    public String getGenere() {
        return genere;
    }

    /**
     * Aggiorna il genere musicale del brano.
     * @param genere Il nuovo genere da impostare.
     */
    public void setGenere(String genere) {
        this.genere = genere;
    }

    /**
     * Fornisce l'elenco dei documenti digitali (audio, video, spartiti) collegati al brano.
     * @return La lista dei documenti digitali associati.
     */
    public List<DocumentoDigitale> getDocumentiAssociati() {
        return documentiAssociati;
    }

    /**
     * Sostituisce in blocco la lista dei documenti associati.
     * @param documentiAssociati La nuova lista di documenti digitali.
     */
    public void setDocumentiAssociati(List<DocumentoDigitale> documentiAssociati) {
        this.documentiAssociati = documentiAssociati;
    }

    /**
     * Restituisce l'elenco completo dei commenti lasciati dagli utenti su questo brano.
     * @return La lista contenente i commenti.
     */
    public List<Commento> getCommenti() {
        return commenti;
    }

    /**
     * Consente di sovrascrivere o inizializzare l'intera collezione dei commenti.
     * @param commenti La lista di oggetti commento da associare.
     */
    public void setCommenti(List<Commento> commenti) {
        this.commenti = commenti;
    }

    // --- METODI DI BUSINESS PER LA GESTIONE DELLE RELAZIONI ---

    /**
     * Aggiunge un singolo documento digitale alla traccia corrente, previa verifica di
     * integrità per assicurarsi che il riferimento passato non sia nullo.
     * * @param doc L'istanza del documento digitale (audio, video o spartito) da agganciare.
     */
    public void aggiungiDocumento(DocumentoDigitale doc) {
        if (doc != null) {
            this.documentiAssociati.add(doc);
        }
    }

    /**
     * Inserisce un nuovo commento all'interno della lista dei feedback del brano.
     * Il controllo difensivo previene l'aggiunta accidentale di elementi non istanziati.
     * * @param c L'oggetto commento o nota temporale da registrare.
     */
    public void aggiungiCommento(Commento c) {
        if (c != null) {
            this.commenti.add(c);
        }
    }
}