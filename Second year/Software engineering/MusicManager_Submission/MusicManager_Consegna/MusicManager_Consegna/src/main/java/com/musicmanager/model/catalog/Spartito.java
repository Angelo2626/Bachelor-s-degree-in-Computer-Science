package com.musicmanager.model.catalog;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Rappresenta uno spartito musicale digitalizzato, tipicamente memorizzato in formato PDF o testuale.
 * Questa classe estende la superclasse astratta {@link DocumentoDigitale}, specializzando la logica
 * di interazione con l'ambiente host per consentire la visualizzazione nativa di documenti e spartiti
 * mediante i programmi di lettura predefiniti del Sistema Operativo.
 */
public class Spartito extends DocumentoDigitale {

    /**
     * Costruttore della classe Spartito.
     * Configura l'entità associando il nome logico e il cammino fisico del file sul disco,
     * categorizzando esplicitamente la risorsa sotto la tipologia fissa di documento PDF.
     * * @param nomeFile Il nome descrittivo o l'etichetta mnemonica associata allo spartito.
     * @param percorsoLocale Il percorso di memorizzazione fisica del file all'interno del file system.
     */
    public Spartito(String nomeFile, String percorsoLocale) {
        super(nomeFile, percorsoLocale, "Documento/PDF");
    }

    /**
     * Implementazione concreta del metodo polimorfico per l'apertura e la visualizzazione della risorsa.
     * Sfrutta le API grafiche nativizzate di Java AWT per effettuare una System Call rivolta al
     * Desktop Manager dell'ambiente host, richiedendo il lancio dell'applicazione predefinita
     * per la gestione e la lettura dei file PDF.
     * Applica i principi della programmazione difensiva verificando sia la disponibilità del
     * sottosistema grafico sia l'effettiva esistenza fisica del file sul disco rigido prima di
     * invocare l'istanza di sistema, prevenendo crash nel thread principale.
     */
    @Override
    public void apriConPlayerEsterno() {
        System.out.println("System Call: Richiesta apertura visualizzatore PDF per -> " + getNomeFile());
        try {
            File filePdf = new File(getPercorsoLocale());

            // Controllo incrociato: verifica che l'ambiente supporti l'interazione Desktop
            // e che la risorsa esista materialmente sul file system.
            if (filePdf.exists() && Desktop.isDesktopSupported()) {
                // Delega l'azione di apertura al visualizzatore predefinito del sistema operativo
                Desktop.getDesktop().open(filePdf);
                System.out.println("Visualizzazione delegata al Sistema Operativo.");
            } else {
                // Ramo di gestione difensiva nel caso in cui il file sia stato rimosso o spostato
                System.out.println("Errore I/O: Spartito non trovato sul disco rigido.");
            }
        } catch (IOException e) {
            // Intercettazione di anomalie di basso livello o permessi di lettura insufficienti
            System.out.println("Eccezione durante l'apertura del documento: " + e.getMessage());
        }
    }
}