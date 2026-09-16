package com.musicmanager.model.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entità del modello del dominio deputata alla gestione dei feedback, delle recensioni
 * e delle note temporali espresse dagli utenti all'interno del sistema.
 * La classe implementa una struttura ricorsiva assimilabile al paradigma del Design Pattern Composite:
 * un commento può a sua volta contenere una collezione di sottomessaggi (risposte),
 * strutturando in questo modo una gerarchia ad albero adatta a rappresentare thread di discussione complessi.
 * Include inoltre proprietà specifiche per tracciare l'ufficialità del contenuto e gli strumenti
 * musicali impiegati, soddisfacendo le specifiche funzionali dedicate agli interpreti.
 */
public class Commento {

    private String testo;
    private Date dataInserimento;
    private boolean isUfficiale;
    private String autoreEmail;
    private String strumentoUsato;
    private List<Commento> risposte;

    /**
     * Costruttore completo della classe Commento.
     * Alloca in memoria Heap un'istanza di feedback impostando i metadati iniziali
     * obbligatori e predisponendo la lista interna per le risposte annidate,
     * applicando i principi della programmazione difensiva per prevenire anomalie da puntatore nullo.
     *
     * @param testo Il contenuto testuale o corpo del commento.
     * @param dataInserimento Il marcatore temporale (timestamp) di inserimento del messaggio.
     * @param isUfficiale Valore booleano che attesta l'eventuale ufficialità o autorevolezza del feedback.
     * @param autoreEmail L'indirizzo email dell'utente autore del testo.
     */
    public Commento(String testo, Date dataInserimento, boolean isUfficiale, String autoreEmail) {
        this.testo = testo;
        this.dataInserimento = dataInserimento;
        this.isUfficiale = isUfficiale;
        this.autoreEmail = autoreEmail;
        this.risposte = new ArrayList<>();
    }

    // --- METODI DI ACCESSO (GETTER E SETTER) ---

    /**
     * Restituisce il contenuto testuale del commento.
     * @return La stringa del testo del messaggio.
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Aggiorna o modifica il testo del commento.
     * @param testo Il nuovo contenuto testuale da assegnare.
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Restituisce il marcatore temporale di inserimento del commento.
     * @return L'oggetto Date corrispondente al momento di pubblicazione del feedback.
     */
    public java.util.Date getDataInserimento() {
        return dataInserimento;
    }

    /**
     * Imposta o aggiorna il marcatore temporale di inserimento del commento.
     * @param dataInserimento La nuova data da associare al commento.
     */
    public void setDataInserimento(java.util.Date dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    /**
     * Verifica se il commento possiede lo status di ufficialità all'interno del catalogo.
     * @return true se il commento è contrassegnato come ufficiale, false in caso contrario.
     */
    public boolean isUfficiale() {
        return isUfficiale;
    }

    /**
     * Configura lo stato di ufficialità del commento corrente.
     * @param ufficiale Il valore booleano da impostare.
     */
    public void setUfficiale(boolean ufficiale) {
        isUfficiale = ufficiale;
    }

    /**
     * Restituisce l'indirizzo email dell'utente che ha redatto e pubblicato il feedback.
     * @return La stringa dell'email dell'autore.
     */
    public String getAutoreEmail() {
        return autoreEmail;
    }

    /**
     * Associa il commento a un utente specifico registrandone l'indirizzo email.
     * @param autoreEmail L'email del redattore da associare.
     */
    public void setAutoreEmail(String autoreEmail) {
        this.autoreEmail = autoreEmail;
    }

    /**
     * Restituisce la stringa descrittiva dello strumento musicale eventualmente utilizzato
     * o menzionato nel contesto dei requisiti per gli interpreti.
     * @return Lo strumento musicale memorizzato, o null se non specificato.
     */
    public String getStrumentoUsato() {
        return strumentoUsato;
    }

    /**
     * Specifica o aggiorna il riferimento allo strumento musicale associato al feedback.
     * @param strumentoUsato Il nome dello strumento da registrare.
     */
    public void setStrumentoUsato(String strumentoUsato) {
        this.strumentoUsato = strumentoUsato;
    }

    /**
     * Fornisce l'intera ramificazione o gerarchia di risposte collegate in modo ricorsivo
     * a questo specifico nodo di commento.
     * @return La lista contenente i riferimenti alle risposte figlie.
     */
    public List<Commento> getGerarchiaRisposte() {
        return risposte;
    }

    /**
     * Inserisce un commento di risposta all'interno della collezione corrente.
     * Applica una verifica di integrità preventiva per assicurarsi che il riferimento
     * passato sia valido e non nullo prima dell'inserimento fisico nella struttura in RAM.
     *
     * @param r L'istanza del commento di risposta da agganciare alla gerarchia.
     */
    public void aggiungiRisposta(Commento r) {
        if (r != null) {
            this.risposte.add(r);
        }
    }
}