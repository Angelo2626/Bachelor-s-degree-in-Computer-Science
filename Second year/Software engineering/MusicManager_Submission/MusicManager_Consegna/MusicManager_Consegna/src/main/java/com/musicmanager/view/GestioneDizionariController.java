package com.musicmanager.view;

import com.musicmanager.model.catalog.Dizionario;
import com.musicmanager.persistence.GestoreJSON;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

/**
 * Controller di presentazione grafico associato alla vista fxml per la manutenzione dei dizionari.
 * Questa classe orchestra l'interfaccia utente dedicata alla gestione delle tassonomie e dei
 * vocabolari controllati di sistema (generi musicali, strumenti, titoli e autori). Interagisce
 * direttamente con lo strato di persistenza per consentire agli amministratori l'inserimento
 * dinamico di nuove voci, garantendo la consistenza del dominio dei dati e implementando un'architettura
 * reattiva basata su listener e collezioni osservabili.
 */
public class GestioneDizionariController {

    @FXML private ChoiceBox<String> tipoDizChoice;
    @FXML private ListView<String> listaVoci;
    @FXML private TextField nuovaVoceField;

    private List<Dizionario> dizionari;
    private GestoreJSON gestore = new GestoreJSON();

    /**
     * Metodo di inizializzazione previsto dal ciclo di vita del framework JavaFX, invocato
     * automaticamente a seguito del caricamento dello Scene Graph.
     * Provvede alla configurazione programmatica del ChoiceBox per definire i domini gestibili,
     * includendo generi, strumenti, titoli e autori in conformità ai requisiti di sistema. Delega
     * al componente di persistenza il caricamento a caldo delle strutture memorizzate su disco
     * e associa un Observer (ChangeListener) al modello di selezione per aggiornare reattivamente la UI.
     */
    @FXML
    public void initialize() {
        // Configurazione estesa per coprire tutti i dizionari aggiornabili richiesti dalle specifiche
        tipoDizChoice.setItems(FXCollections.observableArrayList("Generi", "Strumenti", "Titoli", "Autori"));
        tipoDizChoice.getSelectionModel().selectFirst();

        // Recupero centralizzato dei dizionari dal sottosistema di persistenza JSON
        dizionari = gestore.caricaDizionari("dizionari.json");

        // Allineamento iniziale dello stato dei componenti grafici
        aggiornaInterfaccia();

        // Implementazione del pattern Observer tramite Reactive Listener sulla proprietà di selezione
        tipoDizChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newVal) -> {
            aggiornaInterfaccia();
        });
    }

    /**
     * Sincronizza lo stato visivo della ListView con il contenuto del dizionario attualmente selezionato.
     * Il metodo estrae il token identificativo dal ChoiceBox, scansiona la collezione delle entità
     * di tipo {@link Dizionario} in RAM e, una volta individuata la corrispondenza logica, impacchetta
     * i record all'interno di un adattatore osservabile (ObservableList) per forzare il refresh
     * atomico del viewport grafico.
     */
    private void aggiornaInterfaccia() {
        String tipoSelezionato = tipoDizChoice.getValue();
        for (Dizionario d : dizionari) {
            if (d.getTipoDizionario().equals(tipoSelezionato)) {
                // Il wrapping in una collezione osservabile notifica lo strato grafico della mutazione dello stato
                listaVoci.setItems(FXCollections.observableArrayList(d.getVoci()));
                break;
            }
        }
    }

    /**
     * Gestisce l'evento di inserimento di un nuovo termine tassonomico all'interno del dizionario corrente.
     * Applica i principi della programmazione difensiva ripulendo e validando la stringa di input
     * per intercettare ed escludere sottomissioni vuote. Successivamente scansiona la struttura in memoria:
     * - Se il dizionario corrispondente viene individuato, delega l'inserimento alla logica interna dell'entità.
     * - Ramo di fallback difensivo: qualora la categoria non risultasse censita nella collezione master,
     * provvede all'allocazione dinamica ex-novo del dominio prima di aggregarlo alla lista.
     * Al termine delle operazioni, esegue la serializzazione fisica su disco e richiede il riallineamento visivo.
     */
    @FXML
    public void handleAggiungi() {
        String voce = nuovaVoceField.getText().trim();
        if (voce.isEmpty()) return;

        String tipoSelezionato = tipoDizChoice.getValue();
        boolean trovato = false;

        for (Dizionario d : dizionari) {
            if (d.getTipoDizionario().equals(tipoSelezionato)) {
                d.aggiornaDizionario(voce);
                trovato = true;
                break;
            }
        }

        // Gestione robusta delle anomalie: allocazione a caldo se la risorsa strutturale è assente
        if (!trovato) {
            Dizionario nuovoDiz = new Dizionario(tipoSelezionato);
            nuovoDiz.aggiornaDizionario(voce);
            dizionari.add(nuovoDiz);
        }

        // Consolidamento persistente delle modifiche apportate sul database ipertestuale JSON
        gestore.salvaDizionari(dizionari, "dizionari.json");

        // Aggiornamento reattivo immediato dell'interfaccia utente e svuotamento del buffer di input
        aggiornaInterfaccia();
        nuovaVoceField.clear();
    }
}