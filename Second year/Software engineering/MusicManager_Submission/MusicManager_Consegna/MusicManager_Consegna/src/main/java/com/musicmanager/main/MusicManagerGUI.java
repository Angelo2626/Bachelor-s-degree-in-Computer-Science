package com.musicmanager.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 * Classe principale responsabile del bootstrap e dell'inizializzazione del framework JavaFX.
 * Estendendo la classe astratta Application, si inserisce nel ciclo di vita nativo
 * del motore grafico, predisponendo la finestra di dialogo iniziale e orchestrando
 * il caricamento della scena di login primaria.
 */
public class MusicManagerGUI extends Application {

    /**
     * Punto di ingresso principale per la pipeline grafica di JavaFX.
     * Questo metodo viene invocato automaticamente dal runtime dopo l'esecuzione della fase di boot.
     * Si occupa di caricare il layout dichiarativo FXML, istanziare lo Scene Graph e
     * configurare il frame visivo nativo del sistema operativo (Stage).
     * * @param primaryStage La finestra nativa principale fornita dal window manager del sistema operativo host.
     * @throws Exception Se si verificano anomalie durante il caricamento, la lettura o il parsing del file FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Individuazione del percorso logico del file FXML all'interno dell'albero delle risorse compilate.
        // Questo approccio è essenziale per garantire la portabilità del software quando viene consolidato in un JAR.
        URL resource = getClass().getResource("/view/login.fxml");
        if (resource == null) {
            throw new IllegalArgumentException("Errore critico: File login.fxml non trovato nell'albero delle risorse.");
        }

        // Esecuzione del parsing XML e contestuale allocazione della gerarchia di oggetti visivi nella memoria Heap.
        // L'oggetto Parent risultante rappresenta il nodo radice dello Scene Graph iniziale.
        Parent root = FXMLLoader.load(resource);

        // Allocazione di una nuova Scena (il contenitore logico dei nodi), definendone le dimensioni geometriche (600x400 pixel).
        Scene scene = new Scene(root, 600, 400);

        // Configurazione dei parametri di visualizzazione dello Stage nativo.
        primaryStage.setTitle("Gestione Contenuti Musicali - Autenticazione");
        primaryStage.setScene(scene);

        // Blocco del ridimensionamento per impedire alterazioni o disallineamenti estetici degli elementi dell'interfaccia.
        primaryStage.setResizable(false);

        // Invocazione dei servizi di rendering del sistema operativo per mostrare fisicamente la finestra a schermo.
        primaryStage.show();
    }

    /**
     * Metodo di fallback statico. Permette l'avvio controllato della pipeline grafica nel caso in cui
     * il software venga eseguito direttamente, fungendo da aggancio per i sistemi di build automation.
     * * @param args Gli argomenti passati da riga di comando all'avvio dell'applicazione.
     */
    public static void main(String[] args) {
        // Trasferimento definitivo del flusso di controllo del thread al ciclo degli eventi interno di JavaFX.
        launch(args);
    }
}