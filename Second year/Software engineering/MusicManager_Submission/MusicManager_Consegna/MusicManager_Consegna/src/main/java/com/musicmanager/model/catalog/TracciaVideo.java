package com.musicmanager.model.catalog;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Rappresenta una risorsa multimediale di tipo video (come file in formato MP4 o MKV)
 * memorizzata localmente nel file system.
 * Questa classe estende la superclasse astratta {@link DocumentoDigitale}, specializzando
 * la logica di riproduzione multimediale. Attraverso l'uso del polimorfismo, delega
 * l'apertura e il rendering visivo dei flussi video alle applicazioni native predefinite
 * nell'ambiente del Sistema Operativo host.
 */
public class TracciaVideo extends DocumentoDigitale {

    /**
     * Costruttore della classe TracciaVideo.
     * Mappa i metadati descrittivi della risorsa visiva e ne fissa la tipologia
     * all'interno del catalogo sotto la categoria specifica dei file video MP4.
     * * @param nomeFile Il nome logico o l'etichetta mnemonica associata al filmato.
     * @param percorsoLocale Il cammino di directory o percorso fisico del file sul disco rigido.
     */
    public TracciaVideo(String nomeFile, String percorsoLocale) {
        super(nomeFile, percorsoLocale, "Video/MP4");
    }

    /**
     * Implementazione concreta del metodo polimorfico destinato alla riproduzione del file.
     * Il metodo effettua una System Call interfacciandosi con le API grafiche nativizzate
     * di Java AWT per richiedere al Desktop Manager del sistema operativo il lancio
     * del riproduttore video predefinito dell'utente (es. VLC, Windows Media Player, QuickTime).
     * * Applica i principi della programmazione difensiva eseguendo una verifica preliminare
     * incrociata: accerta sia l'effettiva presenza fisica del file sul disco, sia la
     * compatibilità dell'ambiente grafico corrente prima di cedere il controllo del flusso,
     * minimizzando il rischio di crash imprevisti nel thread principale.
     */
    @Override
    public void apriConPlayerEsterno() {
        System.out.println("System Call: Richiesta avvio riproduttore video per -> " + getNomeFile());
        try {
            File fileVideo = new File(getPercorsoLocale());

            // Verifica di integrità: controlla che la risorsa esista sul file system
            // e che il sistema operativo esponga il supporto per le API Desktop nativizzate
            if (fileVideo.exists() && Desktop.isDesktopSupported()) {
                // Invocazione non bloccante per delegare l'esecuzione visiva al player di sistema
                Desktop.getDesktop().open(fileVideo);
                System.out.println("Riproduzione video delegata al Sistema Operativo.");
            } else {
                // Fallback difensivo in caso di risorsa rimossa o di ambiente headless privo di supporto grafico
                System.out.println("Errore I/O: File video non trovato.");
            }
        } catch (IOException e) {
            // Intercettazione e isolamento robusto di anomalie di basso livello durante il passaggio di consegne all'OS
            System.out.println("Eccezione durante l'apertura del media: " + e.getMessage());
        }
    }
}