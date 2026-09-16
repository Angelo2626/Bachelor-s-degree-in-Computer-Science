package com.musicmanager.main;

/**
 * Punto di ingresso assoluto (Entry Point) dell'applicazione.
 * * Questa classe funge da "Launcher wrapper" ed è strutturata come espediente
 * tecnico essenziale per aggirare i controlli rigorosi sui moduli (Project Jigsaw)
 * introdotti nelle versioni recenti della JVM. A partire da Java 9, l'avvio diretto
 * di un'applicazione il cui entry point estende 'javafx.application.Application'
 * all'interno di un pacchetto JAR non modulare solleverebbe un errore distruttivo a runtime.
 * Separando il punto di avvio statico dall'architettura grafica vera e propria, si forza
 * il ClassLoader a inizializzare correttamente il sistema visivo e le relative dipendenze.
 */
public class Main {

    /**
     * Metodo di avvio principale invocato dal Sistema Operativo host al lancio del software.
     * Gestisce la fase embrionale di bootstrap e delega il controllo al ciclo di vita della GUI.
     * * @param args Eventuali argomenti o flag passati da riga di comando all'atto del boot.
     */
    public static void main(String[] args) {
        // Stampa di diagnostica iniziale nel flusso di output standard. È utile a livello
        // di tracciamento per confermare la corretta esecuzione della JVM prima che
        // l'applicazione occupi il thread grafico principale.
        System.out.println("--- AVVIO MOTORE GRAFICO JAVAFX ---");

        // Trasferiamo il flusso di esecuzione e i parametri originali di runtime
        // alla classe responsabile della gestione del sottosistema visivo.
        MusicManagerGUI.main(args);
    }
}