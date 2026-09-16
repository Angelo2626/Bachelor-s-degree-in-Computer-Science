package com.musicmanager.view;

import com.musicmanager.model.users.UtenteAutorizzato;
import com.musicmanager.persistence.GestoreJSON;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.Optional;

/**
 * Controller di presentazione grafico associato alla vista fxml per la gestione e la moderazione delle utenze.
 * Questa classe definisce il pannello di controllo riservato agli amministratori di sistema, implementando
 * i flussi operativi per il monitoraggio degli account registrati, l'approvazione delle richieste di iscrizione
 * pendenti (Use Case UC-04) e la rimozione forzata o revoca dei profili utente. La sincronizzazione tra lo strato
 * grafico e il database ipertestuale JSON avviene mediante l'uso integrato di collezioni osservabili e factory di cella.
 */
public class GestioneUtentiController {

    @FXML private TableView<UtenteAutorizzato> tabellaUtenti;
    @FXML private TableColumn<UtenteAutorizzato, String> colEmail, colNome, colCognome;
    @FXML private TableColumn<UtenteAutorizzato, Boolean> colAttivo;

    private List<UtenteAutorizzato> listaUtenti;
    private GestoreJSON gestore = new GestoreJSON();

    /**
     * Metodo di inizializzazione automatico previsto dal ciclo di vita del framework JavaFX.
     * Configura programmaticamente le colonne della TableView associandole ai campi interni dell'entità
     * {@link UtenteAutorizzato} tramite il meccanismo di Reflection esposto dalle istanze di {@link PropertyValueFactory}.
     * Al termine della mappatura visiva, innesca il caricamento a caldo dei record dal file di persistenza.
     */
    @FXML
    public void initialize() {
        // Vincolo accoppiato tra le colonne della griglia grafica e i metodi getter del modello del dominio
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colAttivo.setCellValueFactory(new PropertyValueFactory<>("attivo"));

        caricaDati();
    }

    /**
     * Sincronizza lo stato interno della TableView attingendo i dati dal disco rigido.
     * Interfaccia l'applicazione con il modulo GestoreJSON per leggere l'elenco aggiornato degli utenti,
     * effettuando successivamente il wrapping della collezione all'interno di una lista osservabile
     * per forzarne la corretta renderizzazione nel viewport grafico.
     */
    private void caricaDati() {
        listaUtenti = gestore.caricaUtenti("utenti.json");
        tabellaUtenti.setItems(FXCollections.observableArrayList(listaUtenti));
    }

    /**
     * Gestisce il processo di approvazione formale dell'account utente selezionato nella tabella.
     * Se è presente una selezione valida, il metodo muta lo stato interno dell'entità impostando il flag
     * di attivazione a true. Successivamente, consolida l'intera struttura aggiornata su file JSON e
     * invoca una chiamata di refresh non bloccante sulla TableView per comandare il ridisegno atomico
     * delle righe grafiche interessate dalla modifica.
     */
    @FXML
    public void handleApprova() {
        UtenteAutorizzato selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            selezionato.setAttivo(true);

            // Consolidamento persistente della mutazione dello stato sul database JSON
            gestore.salvaUtenti(listaUtenti, "utenti.json");
            tabellaUtenti.refresh();

            System.out.println("Admin: Utente " + selezionato.getEmail() + " approvato.");
        }
    }

    /**
     * Gestisce la rimozione definitiva di un profilo utente dall'applicazione.
     * Applica le regole della programmazione difensiva per intercettare lo stato della selezione;
     * qualora venga individuato un record valido, istanzia e visualizza un modulo di dialogo
     * modale di conferma (Alert di tipo CONFIRMATION) al fine di prevenire la perdita accidentale dei dati.
     * Se l'operatore conferma esplicitamente l'intento d'epurazione, l'entità viene rimossa dalla lista
     * in memoria, serializzata su disco e l'interfaccia utente viene aggiornata di conseguenza.
     */
    @FXML
    public void handleElimina() {
        UtenteAutorizzato u = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (u != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Elimina Account");
            alert.setHeaderText("Eliminare " + u.getEmail() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            // Ramo condizionale difensivo basato sulla valutazione del token opzionale di risposta
            if (result.isPresent() && result.get() == ButtonType.OK) {
                listaUtenti.remove(u);
                gestore.salvaUtenti(listaUtenti, "utenti.json");
                caricaDati();
            }
        }
    }
}