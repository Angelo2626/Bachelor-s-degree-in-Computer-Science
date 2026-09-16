package com.musicmanager.model.catalog;

import java.awt.Desktop;
import java.net.URI;

/**
 * Rappresenta una risorsa multimediale ospitata esternamente alla macchina locale,
 * come ad esempio un collegamento ipertestuale a piattaforme di streaming (YouTube, SoundCloud, Spotify).
 * Estende la classe astratta DocumentoDigitale, riutilizzando i meccanismi di tracciamento
 * ma specializzando la logica di riproduzione attraverso l'interazione con il browser web
 * predefinito del sistema operativo host.
 */
public class CollegamentoWeb extends DocumentoDigitale {

    /**
     * Costruttore della classe CollegamentoWeb.
     * Inizializza i metadati descrittivi della risorsa esterna.
     * * @param nomeFile Il nome mnemonico o l'etichetta associata al collegamento.
     * @param urlWeb L'indirizzo URL assoluto della risorsa web remota.
     */
    public CollegamentoWeb(String nomeFile, String urlWeb) {
        // In questo scenario specifico, l'attributo ereditato "percorsoLocale"
        // viene riutilizzato semanticamente per ospitare la stringa dell'URL esterno.
        super(nomeFile, urlWeb, "Link/Web");
    }

    /**
     * Implementazione concreta del metodo di riproduzione polimorfica.
     * Il metodo effettua una System Call sfruttando le API nativizzate di Java AWT
     * per richiedere al Window Manager dell'ambiente host l'apertura del browser
     * predefinito di sistema, instradando l'utente verso la risorsa remota.
     */
    @Override
    public void apriConPlayerEsterno() {
        System.out.println("System Call: Richiesta apertura Browser Web per -> " + getPercorsoLocale());
        try {
            // Controllo difensivo per verificare che l'ambiente grafico supporti le funzionalità
            // della classe Desktop e che l'azione di navigazione ipertestuale sia consentita dal kernel host.
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

                // Allocazione dell'oggetto Uniform Resource Identifier (URI) a partire dalla stringa ereditata
                // ed esecuzione della chiamata non bloccante per l'attivazione del browser.
                Desktop.getDesktop().browse(new URI(getPercorsoLocale()));
                System.out.println("Navigazione delegata al Browser predefinito.");
            } else {
                // Ramo di ripiego (fallback) nel caso in cui il sottosistema operativo non esponga l'azione richiesta
                System.out.println("Errore: Azione BROWSE non supportata dal Sistema Operativo corrente.");
            }
        } catch (Exception e) {
            // Intercettazione di anomalie sintattiche nella costruzione dell'URI o problemi di I/O di basso livello
            System.out.println("Eccezione di rete o sintassi URL errata: " + e.getMessage());
        }
    }
}