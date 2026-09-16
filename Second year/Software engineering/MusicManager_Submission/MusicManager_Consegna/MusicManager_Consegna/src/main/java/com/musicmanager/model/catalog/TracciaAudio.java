package com.musicmanager.model.catalog;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Rappresenta una risorsa multimediale di tipo audio (come file in formato MP3 o WAV).
 * Questa classe eredita dalla superclasse astratta {@link DocumentoDigitale} e si specializza
 * nella gestione e riproduzione di tracce sonore, effettuando chiamate a basso livello
 * per delegare l'esecuzione multimediale al riproduttore nativo del sistema operativo.
 */
public class TracciaAudio extends DocumentoDigitale {

    /**
     * Costruttore della classe TracciaAudio.
     * Inizializza i metadati descrittivi del file sonoro e ne fissa la tipologia
     * all'interno del catalogo sotto la categoria nativa dei file audio MP3.
     * * @param nomeFile Il nome logico o l'etichetta mnemonica associata alla traccia.
     * @param percorsoLocale Il cammino di directory o percorso fisico del file sul disco rigido.
     */
    public TracciaAudio(String nomeFile, String percorsoLocale) {
        super(nomeFile, percorsoLocale, "Audio/MP3");
    }

    /**
     * Implementazione concreta del metodo polimorfico destinato alla riproduzione del file.
     * Il metodo esegue una System Call interfacciandosi con le API native Java AWT Desktop
     * per richiedere al Desktop Manager del sistema operativo ospitante il lancio
     * del software di riproduzione audio predefinito (es. Windows Media Player, VLC, QuickTime).
     * * Applica le regole della programmazione difensiva eseguendo un controllo preliminare
     * incrociato: verifica sia la compatibilità dell'ambiente grafico corrente, sia
     * l'effettiva presenza fisica del file multimediale sul file system prima di inizializzare
     * il player, evitando il sollevamento di eccezioni non gestite.
     */
    @Override
    public void apriConPlayerEsterno() {
        System.out.println("System Call: Richiesta riproduzione audio per -> " + getNomeFile());
        try {
            File fileAudio = new File(getPercorsoLocale());

            // Verifica difensiva: controlla se il file esiste materialmente sul disco
            // e se il sistema operativo espone il supporto alle API di Desktop native.
            if (fileAudio.exists() && Desktop.isDesktopSupported()) {
                // Invocazione non bloccante che delega l'apertura del media al player predefinito
                Desktop.getDesktop().open(fileAudio);
                System.out.println("Esecuzione delegata al Sistema Operativo con successo.");
            } else {
                // Fallback difensivo in caso di risorsa mancante o permessi insufficienti sul file system
                System.out.println("Errore I/O: File non trovato sul disco rigido o Desktop API non supportate.");
            }
        } catch (IOException e) {
            // Intercettazione e isolamento di errori di input/output durante la transizione del thread grafico
            System.out.println("Eccezione durante l'apertura del media: " + e.getMessage());
        }
    }
}