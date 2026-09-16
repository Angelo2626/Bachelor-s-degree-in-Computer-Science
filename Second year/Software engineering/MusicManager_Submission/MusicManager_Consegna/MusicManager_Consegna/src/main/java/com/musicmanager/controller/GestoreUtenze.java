package com.musicmanager.controller;

import com.musicmanager.model.users.Amministratore;
import com.musicmanager.model.users.Visitatore;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller dedicato alla gestione del ciclo di vita degli utenti.
 * Questa classe agisce da orchestratore: intercetta le richieste provenienti dall'interfaccia
 * grafica (UI) e gestisce l'intera logica di approvazione o rifiuto delle nuove iscrizioni.
 */
public class GestoreUtenze {

    // Simulazione di una coda temporanea allocata in RAM per le richieste in attesa.
    // Questa lista mantiene lo stato delle registrazioni prima che vengano persistite o scartate.
    private List<String> richiestePendenti;

    /**
     * Costruttore della classe.
     * Inizializza la lista delle richieste pendenti per prevenire eventuali NullPointerException
     * al momento dell'inserimento della prima richiesta nel sistema.
     */
    public GestoreUtenze() {
        this.richiestePendenti = new ArrayList<>();
    }

    /**
     * Permette a un Visitatore (un utente non ancora autenticato) di sottomettere
     * una formale richiesta di registrazione al sistema.
     *
     * @param visitatore L'entità visitatore che effettua la richiesta.
     * @param datiForm La stringa contenente i dati compilati nel modulo di iscrizione.
     */
    public void inoltraRichiestaRegistrazione(Visitatore visitatore, String datiForm) {
        // Deleghiamo l'azione materiale all'entità Visitatore nel modello
        visitatore.richiediRegistrazione(datiForm);

        // Accodiamo la richiesta nella lista volatile in attesa del controllo dell'Amministratore
        richiestePendenti.add(datiForm);
        System.out.println("GestoreUtenze: Richiesta accodata e in attesa di approvazione della moderazione.");
    }

    /**
     * Metodo che implementa e traduce in codice il Sequence Diagram SD-04 (Valutazione Richieste).
     * Il flusso di esecuzione si biforca rigorosamente in due rami a seconda della decisione presa
     * dall'amministratore (il parametro booleano 'approvata').
     *
     * @param admin L'amministratore che ha preso in carico la valutazione.
     * @param idRichiesta L'identificativo univoco della richiesta sotto esame.
     * @param approvata L'esito della valutazione: true per approvare, false per respingere.
     */
    public void valutaRichiesta(Amministratore admin, String idRichiesta, boolean approvata) {
        // Il controller autorizza l'azione invocando il metodo specifico sull'oggetto admin
        admin.valutaRegistrazione(idRichiesta, approvata);

        if (approvata) {
            // Ramo [decisione == APPROVATA] del diagramma di sequenza
            System.out.println("GestoreUtenze: Creazione nuovo UtenteAutorizzato in corso...");
            // (Nel blocco dedicato alla persistenza, qui salveremo fisicamente il nuovo utente nel JSON)
        } else {
            // Ramo [decisione == RIFIUTATA] del diagramma di sequenza
            System.out.println("GestoreUtenze: Richiesta scartata. Nessuna entità creata in memoria.");
        }

        // Pulizia dello stato: indipendentemente dall'esito (approvazione o rifiuto),
        // l'elemento elaborato deve essere rimosso dalla coda delle pendenze per evitare duplicazioni.
        richiestePendenti.remove(idRichiesta);
    }

    /**
     * Restituisce la lista delle richieste attualmente in attesa di valutazione.
     *
     * @return La collezione di stringhe che rappresenta le richieste pendenti.
     */
    public List<String> getRichiestePendenti() {
        return richiestePendenti;
    }
}