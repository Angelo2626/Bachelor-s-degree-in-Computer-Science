package com.musicmanager.controller;

import com.musicmanager.model.catalog.Brano;
import com.musicmanager.model.catalog.Dizionario;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller centrale dedicato alla gestione del dominio musicale.
 * Questa classe funge da barriera di controllo, assicurandosi che nessun dato
 * incoerente o privo di riferimenti validi possa entrare nel catalogo principale.
 */
public class GestoreCatalogo {

    // Variabili residenti in memoria RAM che mantengono lo stato dell'applicativo
    // prima del salvataggio definitivo su file.
    private List<Brano> archivioBrani;
    private Dizionario dizionarioGeneri;

    /**
     * Costruttore della classe.
     * Alloca le strutture dati in memoria e predispone i valori di base per permettere
     * il corretto funzionamento del sistema fin dal primo avvio.
     */
    public GestoreCatalogo() {
        this.archivioBrani = new ArrayList<>();
        this.dizionarioGeneri = new Dizionario("Generi");

        // Popoliamo a caldo la memoria con alcuni valori di default per poter
        // effettuare i test iniziali senza dover caricare dizionari esterni.
        this.dizionarioGeneri.aggiornaDizionario("Rock");
        this.dizionarioGeneri.aggiornaDizionario("Classica");
        this.dizionarioGeneri.aggiornaDizionario("Jazz");
    }

    /**
     * Metodo che implementa la logica del Sequence Diagram SD-01 (Inserimento Nuovo Brano).
     * Il sistema verifica prima di tutto la coerenza del dato: un brano può essere inserito
     * solo se il genere specificato appartiene al dizionario di sistema.
     * * @param nuovoBrano L'istanza del brano che si desidera aggiungere al catalogo.
     * @param genereSelezionato La stringa che rappresenta il genere musicale associato.
     * @return true se l'inserimento va a buon fine, false nel caso in cui il genere non sia valido.
     */
    public boolean aggiungiNuovoBrano(Brano nuovoBrano, String genereSelezionato) {

        // Blocco "alt" del Sequence Diagram: effettuiamo un controllo semantico
        // per assicurarci che il genere sia riconosciuto dal dizionario.
        if (dizionarioGeneri.verificaEsistenza(genereSelezionato)) {
            archivioBrani.add(nuovoBrano);
            System.out.println("GestoreCatalogo: Inserimento consentito. Brano '" + nuovoBrano.getTitolo() + "' allocato nel catalogo.");
            return true;
        } else {
            // Ramo "altrimenti": applichiamo la programmazione difensiva e blocchiamo
            // l'operazione per evitare di sporcare il database con generi inesistenti.
            System.out.println("GestoreCatalogo: Violazione di integrità. Il genere '" + genereSelezionato + "' non esiste nel dizionario.");
            return false;
        }
    }

    /**
     * Metodo che implementa la logica del Sequence Diagram SD-03 (Ricerca e Visualizzazione).
     * Scansiona l'intero archivio per trovare i brani il cui titolo contiene la stringa cercata.
     * * @param filtroTitolo La parola chiave o la porzione di testo da cercare nei titoli.
     * @return Una lista contenente i riferimenti in memoria dei brani che soddisfano la ricerca.
     */
    public List<Brano> ricercaBrani(String filtroTitolo) {
        List<Brano> risultati = new ArrayList<>();

        // Ciclo "loop" del Sequence Diagram: scorriamo ogni elemento presente in RAM.
        for (Brano b : archivioBrani) {
            // Eseguiamo un confronto testuale normalizzando tutto in minuscolo,
            // in modo che la ricerca non sia sensibile alle differenze di maiuscole o minuscole.
            if (b.getTitolo().toLowerCase().contains(filtroTitolo.toLowerCase())) {
                risultati.add(b);
            }
        }
        System.out.println("GestoreCatalogo: Interrogazione completata. Rilevate " + risultati.size() + " corrispondenze.");

        // Ritorna l'indirizzo di memoria della sottolista filtrata
        return risultati;
    }

    /**
     * Espone l'archivio residente in RAM. Questo metodo è essenziale per permettere
     * al modulo di persistenza (es. GestoreJSON) di leggere i dati e serializzarli su disco.
     * * @return La lista completa dei brani attualmente in memoria.
     */
    public List<Brano> getArchivioBrani() {
        return archivioBrani;
    }
}