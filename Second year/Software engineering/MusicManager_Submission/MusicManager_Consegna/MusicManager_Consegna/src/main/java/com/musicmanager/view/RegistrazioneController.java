package com.musicmanager.view;

import com.musicmanager.model.users.UtenteAutorizzato;
import com.musicmanager.model.users.AutoreInterprete;
import com.musicmanager.persistence.GestoreJSON;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.List;
import java.util.UUID;

/**
 * Controller di presentazione grafico associato alla vista fxml per la registrazione dei nuovi utenti.
 * Questa classe governa il ciclo di vita del modulo di iscrizione alla piattaforma (Use Case UC-03).
 * Sfrutta i paradigmi della programmazione reattiva per modificare dinamicamente lo Scene Graph visivo
 * a runtime in base alle scelte dell'utente e applica il principio del polimorfismo per instanziare
 * l'esatta tipologia di entità del dominio (UtenteAutorizzato o AutoreInterprete) richiesta,
 * implementando infine verifiche difensive anti-duplicazione sul database JSON.
 */
public class RegistrazioneController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private ChoiceBox<String> ruoloChoice;
    @FXML private TextArea bioArea;

    /**
     * Metodo di inizializzazione previsto dal ciclo di vita del framework JavaFX, eseguito
     * automaticamente a seguito del caricamento della vista e dell'iniezione dei componenti FXML.
     * Configura il ChoiceBox con le tassonomie dei ruoli e implementa un approccio reattivo
     * agganciando un ChangeListener sulla proprietà di selezione: questa scelta ingegneristica
     * consente di rimodulare dinamicamente il layout visivo e logico (setVisible e setManaged),
     * mostrando il campo biografia esclusivamente qualora l'utente selezioni il profilo artistico.
     */
    @FXML
    public void initialize() {
        ruoloChoice.setItems(FXCollections.observableArrayList("Utente Standard", "Autore/Interprete"));
        ruoloChoice.getSelectionModel().selectFirst();

        // Implementazione del comportamento reattivo guidato dai mutamenti di stato del componente grafico
        ruoloChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newVal) -> {
            boolean isAutore = "Autore/Interprete".equals(newVal);
            bioArea.setVisible(isAutore);
            bioArea.setManaged(isAutore);
        });
    }

    /**
     * Elabora e convalida la sottomissione formale dei dati inseriti nel modulo di registrazione.
     * Il metodo applica le regole della programmazione difensiva eseguendo controlli di validità sintattica
     * per escludere l'omissione di campi obbligatori. Successivamente, si interfaccia con il modulo
     * GestoreJSON e sfrutta la Stream API per verificare l'assenza di collisioni logiche sull'indirizzo email.
     * Qualora i controlli abbiano esito positivo, genera un identificativo pseudo-casuale alfanumerico
     * tramite algoritmo UUID e mappa polimorficamente l'istanza corretta.
     * In conformità con i requisiti di sicurezza del sistema, l'account viene impostato come non attivo
     * (setAttivo(false)) in attesa della moderazione degli amministratori.
     *
     * @param event L'evento ActionEvent generato dal click sul pulsante di conferma.
     */
    @FXML
    public void handleRegistra(ActionEvent event) {
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String email = emailField.getText().trim().toLowerCase();
        String password = passwordField.getText();

        // Controllo di integrità difensivo sui requisiti minimi obbligatori di input
        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Errore: Campi obbligatori mancanti.");
            return;
        }

        GestoreJSON gestore = new GestoreJSON();
        List<UtenteAutorizzato> utenti = gestore.caricaUtenti("utenti.json");

        // Verifica funzionale ad alta efficienza per impedire la duplicazione delle identità nel sistema
        if (utenti.stream().anyMatch(u -> u.getEmail().equals(email))) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Errore: Email già registrata.");
            return;
        }

        // Generazione atomica di un token univoco abbreviato per l'identificazione della risorsa
        String id = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        UtenteAutorizzato nuovo;

        // Biforcazione polimorfica basata sulla valutazione del ruolo selezionato nella UI
        if ("Autore/Interprete".equals(ruoloChoice.getValue())) {
            nuovo = new AutoreInterprete(id, email, password, nome, cognome, new java.util.Date(), bioArea.getText());
        } else {
            nuovo = new UtenteAutorizzato(id, email, password, nome, cognome, new java.util.Date());
        }

        // Stato iniziale di sospensione cautelativa a tutela dell'integrità del sistema
        nuovo.setAttivo(false);
        utenti.add(nuovo);

        // Sincronizzazione persistente sul database ipertestuale JSON
        gestore.salvaUtenti(utenti, "utenti.json");

        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setText("Richiesta inviata! Attendi approvazione Admin.");
    }

    /**
     * Intercetta la richiesta volontaria di interruzione della registrazione e ripristina il flusso.
     *
     * @param event L'evento ActionEvent associato al pulsante Annulla.
     */
    @FXML
    public void handleAnnulla(ActionEvent event) {
        tornaAlLogin(event);
    }

    /**
     * Esegue l'inversione del flusso d'interfaccia utente operando uno Scene Switching verso la vista di Login.
     * Estrae in modo non bloccante lo Stage attivo risalendo lo Scene Graph a partire dal widget che ha
     * scatenato l'evento, caricando il rispettivo layout FXML predefinito.
     *
     * @param event L'evento grafico ActionEvent da cui estrarre i riferimenti del frame ospitante.
     */
    private void tornaAlLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}