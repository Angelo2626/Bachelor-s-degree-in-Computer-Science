package com.musicmanager.view;

import com.musicmanager.controller.SessionManager;
import com.musicmanager.model.catalog.Brano;
import com.musicmanager.model.users.*;
import com.musicmanager.persistence.GestoreJSON;
import com.musicmanager.exceptions.BranoNonTrovatoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller di presentazione grafico principale associato alla vista della Dashboard dell'applicazione.
 * Questa classe funge da hub centrale per l'interazione con l'utente autenticato: gestisce la visualizzazione
 * del catalogo musicale all'interno di una TableView, implementa i filtri di ricerca in tempo reale tramite
 * la Stream API (incluso il deep scanning degli esecutori) e applica dinamicamente le restrizioni di sicurezza
 * basate sui ruoli (Role-Based Access Control) abilitando o nascondendo le funzionalità amministrative.
 */
public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label errorSearchLabel;
    @FXML private TableView<Brano> tabellaBrani;
    @FXML private TableColumn<Brano, String> colonnaTitolo;
    @FXML private TableColumn<Brano, String> colonnaAutore;
    @FXML private TableColumn<Brano, String> colonnaProprietario;
    @FXML private TableColumn<Brano, Integer> colonnaAnno;
    @FXML private Button btnAggiungiBrano;
    @FXML private Button btnGestioneUtenti;
    @FXML private Button btnGestioneDizionari;

    // Nuovi campi di testo strutturati per la ricerca multi-criterio assistita
    @FXML private TextField filterTitolo;
    @FXML private TextField filterAutore;
    @FXML private TextField filterGenere;
    @FXML private TextField filterEsecutore;

    // Lista osservabile master che mantiene in RAM il set completo dei dati del catalogo
    private ObservableList<Brano> tuttiIBraniMaster;

    /**
     * Metodo di inizializzazione richiamato automaticamente dal framework JavaFX dopo il caricamento del file FXML.
     * Configura le factory delle celle per mappare le proprietà dell'entità Brano sulle rispettive colonne della tabella,
     * interroga il SessionManager globale per personalizzare l'interfaccia con i dati anagrafici dell'utente loggato,
     * predispone i menu di controllo contestuali e aggancia un listener per intercettare il doppio click sulle righe,
     * indirizzando l'utente alla schermata di dettaglio del brano selezionato.
     */
    @FXML
    public void initialize() {
        // Vincolo delle colonne della TableView ai campi interni del modello mediante Reflection
        colonnaTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colonnaAutore.setCellValueFactory(new PropertyValueFactory<>("autorePrincipale"));
        colonnaAnno.setCellValueFactory(new PropertyValueFactory<>("annoComposizione"));
        colonnaProprietario.setCellValueFactory(new PropertyValueFactory<>("proprietarioEmail"));

        // Controllo di sicurezza e profilazione dell'interfaccia in base al ruolo dell'utente
        Utente loggato = SessionManager.getInstance().getUtenteLoggato();
        if (loggato != null) {
            welcomeLabel.setText("Benvenuto, " + loggato.getNome() + " " + loggato.getCognome());
            btnAggiungiBrano.setVisible(true);
            btnAggiungiBrano.setManaged(true);

            // Ramo condizionale per l'esposizione esclusiva delle opzioni di amministrazione di sistema
            if (loggato instanceof Amministratore) {
                btnGestioneUtenti.setVisible(true);
                btnGestioneUtenti.setManaged(true);
                btnGestioneDizionari.setVisible(true);
                btnGestioneDizionari.setManaged(true);
            }
        }

        creaMenuContestuale();
        caricaDatiDalDisco();

        // Assegnazione del comportamento reattivo per l'intercettazione del doppio click sulla griglia dei dati
        tabellaBrani.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tabellaBrani.getSelectionModel().getSelectedItem() != null) {
                apriFinestraDettaglio(tabellaBrani.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Gestisce la richiesta di riproduzione immediata del brano multimediale selezionato.
     * Applica controlli difensivi per verificare la presenza di una selezione valida e l'effettiva
     * associazione di risorse digitali (file o collegamenti ipertestuali) all'entità, delegando
     * poi l'apertura polimorfica al sottosistema multimediale pertinente o informando l'utente tramite alert.
     *
     * @param event L'evento grafico ActionEvent associato alla pressione del pulsante.
     */
    @FXML
    public void handlePlayDiretto(ActionEvent event) {
        Brano b = tabellaBrani.getSelectionModel().getSelectedItem();
        if (b == null) {
            new Alert(Alert.AlertType.WARNING, "Seleziona un brano dalla tabella per riprodurlo.").show();
            return;
        }

        // Verifica di consistenza difensiva sulla presenza di allegati multimediali digitali
        if (b.getDocumentiAssociati() == null || b.getDocumentiAssociati().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Errore Riproduzione");
            a.setHeaderText("Nessun contenuto multimediale");
            a.setContentText("Il brano selezionato non ha file o link associati.\nVai nel Dettaglio per aggiurnerne uno.");
            a.show();
        } else {
            // Invocazione polimorfica sul primo documento disponibile per l'attivazione del player nativo
            b.getDocumentiAssociati().get(0).apriConPlayerEsterno();
        }
    }

    /**
     * Configura e associa un menu a comparsa (ContextMenu) alla TableView.
     * Definisce le voci operative per l'esplorazione, la modifica e la rimozione rapida dei record,
     * mappando i rispettivi gestori di eventi sul thread grafico di JavaFX.
     */
    private void creaMenuContestuale() {
        ContextMenu menu = new ContextMenu();

        MenuItem itemApri = new MenuItem("Apri Dettaglio");
        itemApri.setOnAction(e -> apriFinestraDettaglio(tabellaBrani.getSelectionModel().getSelectedItem()));

        MenuItem itemModifica = new MenuItem("Modifica Brano");
        itemModifica.setOnAction(e -> handleModificaBrano());

        MenuItem itemElimina = new MenuItem("Elimina Brano");
        itemElimina.setOnAction(e -> handleEliminaBrano());

        menu.getItems().addAll(itemApri, itemModifica, itemElimina);
        tabellaBrani.setContextMenu(menu);
    }

    /**
     * Gestisce la logica di business preliminare per la modifica del brano selezionato.
     * Implementa i controlli di sicurezza RBAC verificando che l'utente loggato coincida
     * con il proprietario originario della scheda o detegna lo status di Amministratore primario.
     * In caso positivo, istanzia il loader FXML e trasmette l'entità al modulo di editing.
     */
    private void handleModificaBrano() {
        Brano b = tabellaBrani.getSelectionModel().getSelectedItem();
        if (b == null) return;

        Utente loggato = SessionManager.getInstance().getUtenteLoggato();
        if (!(loggato instanceof Amministratore) && !loggato.getEmail().equals(b.getProprietarioEmail())) {
            new Alert(Alert.AlertType.ERROR, "Non sei il proprietario.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/aggiungi_brano.fxml"));
            Parent root = loader.load();
            ((AggiungiBranoController) loader.getController()).setBranoDaModificare(b);
            new Stage() {{
                setTitle("Modifica");
                setScene(new Scene(root));
                show();
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce la rimozione logica e fisica di una traccia musicale dal catalogo.
     * Sottopone l'operazione a controlli restrittivi sui permessi dell'utente corrente e, previo
     * riscontro positivo da una finestra di dialogo di conferma, esegue l'epurazione selettiva della
     * risorsa mediante predicati lambda, aggiornando successivamente sia il database JSON che la TableView.
     */
    private void handleEliminaBrano() {
        Brano b = tabellaBrani.getSelectionModel().getSelectedItem();
        if (b == null) return;

        Utente loggato = SessionManager.getInstance().getUtenteLoggato();
        if (!(loggato instanceof Amministratore) && !loggato.getEmail().equals(b.getProprietarioEmail())) {
            new Alert(Alert.AlertType.ERROR, "Permesso negato.").show();
            return;
        }

        if (new Alert(Alert.AlertType.CONFIRMATION, "Eliminare " + b.getTitolo() + "?").showAndWait().get() == ButtonType.OK) {
            GestoreJSON g = new GestoreJSON();
            List<Brano> cat = g.caricaCatalogo("catalogo.json");

            // Rimozione mirata dell'entità basata sulla corrispondenza esatta della chiave logica
            cat.removeIf(br -> br.getTitolo().equals(b.getTitolo()));
            g.salvaCatalogo(cat, "catalogo.json");
            caricaDatiDalDisco();
        }
    }

    /**
     * Interfaccia con il sottosistema di persistenza per sincronizzare lo stato visivo della TableView.
     * Recupera l'elenco aggiornato dei brani memorizzati su file e ripopola la collezione osservabile master.
     */
    private void caricaDatiDalDisco() {
        tuttiIBraniMaster = FXCollections.observableArrayList(new GestoreJSON().caricaCatalogo("catalogo.json"));
        tabellaBrani.setItems(tuttiIBraniMaster);
        tabellaBrani.refresh();
    }

    /**
     * Ripristina la visualizzazione integrale della tabella azzerando ogni filtro di ricerca applicato.
     *
     * @param event L'evento ActionEvent scatenato dal click sul pulsante di Reset.
     */
    @FXML
    public void handleReset(ActionEvent event) {
        // Pulisce tutti i nuovi campi filtro
        filterTitolo.clear();
        filterAutore.clear();
        filterGenere.clear();
        filterEsecutore.clear();

        // Pulisce anche l'eventuale errore residuo
        errorSearchLabel.setText("");

        // Ricarica i dati
        caricaDatiDalDisco();
    }

    /**
     * Implementa la pipeline di ricerca testuale e filtraggio selettivo all'interno del catalogo.
     * Sfrutta la Stream API per valutare in modo reattivo e case-insensitive i criteri di ricerca multipli:
     * 1. Corrispondenza sul titolo del brano.
     * 2. Corrispondenza sull'autore principale.
     * 3. Corrispondenza sul genere musicale.
     * 4. Scansione profonda (flatMap) degli esecutori (String) all'interno dei segmenti (Requisito 23).
     * Qualora l'operazione terminale produca un set vuoto, viene sollevata BranoNonTrovatoException.
     *
     * @param event L'evento ActionEvent generato dall'invocazione del comando di ricerca.
     */
    @FXML
    public void handleRicerca(ActionEvent event) {
        String t = filterTitolo.getText().trim().toLowerCase();
        String a = filterAutore.getText().trim().toLowerCase();
        String g = filterGenere.getText().trim().toLowerCase();
        String es = filterEsecutore.getText().trim().toLowerCase();

        errorSearchLabel.setText("");

        try {
            List<Brano> filtrati = tuttiIBraniMaster.stream()
                    .filter(b -> t.isEmpty() || (b.getTitolo() != null && b.getTitolo().toLowerCase().contains(t)))
                    .filter(b -> a.isEmpty() || (b.getAutorePrincipale() != null && b.getAutorePrincipale().toLowerCase().contains(a)))
                    .filter(b -> g.isEmpty() || (b.getGenere() != null && b.getGenere().toLowerCase().contains(g)))
                    .filter(b -> es.isEmpty() || (b.getDocumentiAssociati() != null && b.getDocumentiAssociati().stream()
                            .filter(doc -> doc.getSegmenti() != null)
                            .flatMap(doc -> doc.getSegmenti().stream())
                            .anyMatch(seg -> seg.getEsecutori() != null && seg.getEsecutori().toLowerCase().contains(es))))
                    .collect(Collectors.toList());

            if (filtrati.isEmpty()) {
                throw new BranoNonTrovatoException("Nessun brano corrisponde ai filtri impostati.", "multi-criterio");
            }

            tabellaBrani.setItems(FXCollections.observableArrayList(filtrati));
        } catch (BranoNonTrovatoException e) {
            errorSearchLabel.setText(e.getMessage());
            tabellaBrani.setItems(FXCollections.observableArrayList());
        }
    }

    /**
     * Invalida la sessione utente attiva e reindirizza l'applicazione verso la schermata di autenticazione.
     * Sfrutta il SessionManager (Singleton) per la distruzione sicura del token e sostituisce la Scena corrente.
     *
     * @param event L'evento ActionEvent legato al click sul comando di Logout.
     */
    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(root, 600, 400));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inizializza e mostra a schermo una nuova finestra per l'inserimento guidato di un'opera musicale.
     *
     * @param event L'evento ActionEvent generato dal click sul pulsante di aggiunta brano.
     */
    @FXML
    public void handleAggiungiBrano(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/aggiungi_brano.fxml"));
            new Stage() {{
                setTitle("Nuovo Brano");
                setScene(new Scene(root, 400, 450));
                show();
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inizializza e mostra la finestra dedicata alla gestione amministrativa delle utenze di sistema.
     *
     * @param event L'evento ActionEvent associato.
     */
    @FXML
    public void handleGestioneUtenti(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/gestione_utenti.fxml"));
            new Stage() {{
                setTitle("Utenti");
                setScene(new Scene(root, 650, 450));
                show();
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inizializza e mostra lo Stage dedicato alla manutenzione e configurazione dei dizionari tassonomici.
     *
     * @param event L'evento ActionEvent associato.
     */
    @FXML
    public void handleGestioneDizionari(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/gestione_dizionari.fxml"));
            new Stage() {{
                setTitle("Dizionari");
                setScene(new Scene(root, 500, 450));
                show();
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Isola e visualizza all'interno della griglia unicamente i brani del catalogo che hanno
     * ricevuto almeno un commento o un feedback da parte dell'utente correntemente autenticato in sessione.
     *
     * @param event L'evento ActionEvent scatenato dal pulsante dei contributi personali.
     */
    @FXML
    public void handleMieiContributi(ActionEvent event) {
        String email = SessionManager.getInstance().getUtenteLoggato().getEmail();
        List<Brano> filtrati = tuttiIBraniMaster.stream()
                .filter(b -> b.getCommenti().stream().anyMatch(c -> email.equals(c.getAutoreEmail())))
                .collect(Collectors.toList());

        tabellaBrani.setItems(FXCollections.observableArrayList(filtrati));
    }

    /**
     * Gestisce l'apertura in modalità massimizzata della finestra di ispezione analitica di un brano.
     * Provvede a istanziare il loader FXML e a invocare il metodo di iniezione dei dati sul controller
     * di destinazione prima di cedere il controllo allo Stage grafico di JavaFX.
     *
     * @param brano L'istanza dell'oggetto Brano da analizzare.
     */
    private void apriFinestraDettaglio(Brano brano) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dettaglio.fxml"));
            Parent root = loader.load();

            // Passaggio dei dati al controller subordinato (Iniezione di dipendenza manuale)
            ((DettaglioController) loader.getController()).inizializzaDati(brano);

            Stage stage = new Stage();
            stage.setTitle("Dettaglio - " + brano.getTitolo());
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}