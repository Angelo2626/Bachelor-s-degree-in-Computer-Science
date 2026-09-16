package com.musicmanager.view;

import com.musicmanager.controller.SessionManager;
import com.musicmanager.model.users.*;
import com.musicmanager.persistence.GestoreJSON;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;

/**
 * Controller di presentazione grafico associato alla vista fxml di autenticazione (Login).
 * Questa classe gestisce e valida le credenziali immesse dall'utente all'interno dell'interfaccia,
 * interfacciandosi con il modulo di persistenza per verificare l'identità del soggetto.
 * Coordina l'allocazione e il tracciamento delle sessioni attive mediante l'interazione con il gestore
 * globale dello stato (SessionManager) e governa i cambi di scena (Scene Switching) dello Stage primario
 * dell'applicazione JavaFX.
 */
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    /**
     * Gestisce la procedura di validazione e autenticazione scatenata dal pulsante di login.
     * Il metodo applica una strategia di verifica strutturata su due livelli distinti:
     * 1. Verifica preliminare ad accesso immediato per l'account amministratore di sistema predefinito
     * (bootstrap), provvedendo ad allocare un'istanza polimorfica di tipo {@link Amministratore} in sessione.
     * 2. Scansione sequenziale della collezione degli utenti letti dal file di persistenza JSON.
     * Se viene individuata una corrispondenza logica di credenziali, applica un vincolo difensivo
     * interrogando lo stato di abilitazione dell'account (isAttivo) prima di concedere il token
     * di sessione e disporre il caricamento della Dashboard.
     * In caso di riscontro negativo o credenziali errate, aggiorna la UI valorizzando la Label di errore.
     *
     * @param event L'evento ActionEvent generato dal click sul pulsante di Login.
     */
    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // 1. Controllo ad accesso prioritario per l'Amministratore predefinito di sistema
        if (email.equals("admin@musicmanager.it") && password.equals("admin123")) {
            SessionManager.getInstance().login(new Amministratore("ADM-01", email, password, "Super", "Admin"));
            caricaDashboard(event);
            return;
        }

        // 2. Controllo degli Utenti Autorizzati estratti dal file di persistenza JSON
        List<UtenteAutorizzato> utenti = new GestoreJSON().caricaUtenti("utenti.json");
        for (UtenteAutorizzato u : utenti) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                // Controllo di sicurezza difensivo sullo stato dell'account condizionato alla moderazione
                if (u.isAttivo()) {
                    SessionManager.getInstance().login(u);
                    caricaDashboard(event);
                } else {
                    errorLabel.setText("Account in attesa di approvazione.");
                }
                return;
            }
        }

        // Fallback logico nel caso in cui non sia stata riscontrata alcuna corrispondenza valida
        errorLabel.setText("Credenziali errate.");
    }

    /**
     * Esegue la transizione visiva dello Stage principale caricando lo Scene Graph della Dashboard.
     * Estrae in modo dinamico il riferimento alla finestra (Stage) attiva partendo dal nodo grafico
     * che ha intercettato l'evento utente, associa la nuova Scena specificandone i vincoli dimensionali
     * e provvede al suo riposizionamento centrato sul display dello schermo.
     *
     * @param event L'evento grafico ActionEvent da cui ricavare il frame visivo ospitante.
     */
    private void caricaDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 850, 600));
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reindirizza il flusso dell'interfaccia utente verso la schermata di iscrizione del sistema.
     * Innesca il caricamento dell'apposito layout FXML sostituendo lo Scene Graph corrente
     * all'interno dello Stage esistente, per consentire ai visitatori l'immissione dei propri dati.
     *
     * @param event L'evento grafico ActionEvent associato alla richiesta di registrazione.
     */
    @FXML
    public void handleRegistrazione(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/registrazione.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 500, 550));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}