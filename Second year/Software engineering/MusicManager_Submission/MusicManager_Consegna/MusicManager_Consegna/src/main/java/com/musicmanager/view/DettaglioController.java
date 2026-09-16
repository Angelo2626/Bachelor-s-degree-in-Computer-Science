package com.musicmanager.view;

import com.musicmanager.model.catalog.*;
import com.musicmanager.model.feedback.*;
import com.musicmanager.model.users.*;
import com.musicmanager.persistence.GestoreJSON;
import com.musicmanager.controller.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller di presentazione grafico associato alla vista fxml di ispezione analitica di un brano.
 * Questa classe gestisce la visualizzazione approfondita dei metadati di una traccia musicale,
 * l'interazione ricorsiva ad albero con la gerarchia dei feedback (Pattern Composite mappato su TreeView),
 * il caricamento difensivo di allegati multimediali locali o remoti e la strutturazione
 * delle mappature temporali per le esecuzioni dal vivo (concerti).
 */
public class DettaglioController {

    @FXML private Label titoloLabel;
    @FXML private Label autoreAnnoLabel;
    @FXML private Label proprietarioLabel;
    @FXML private Label fileStatusLabel;
    @FXML private Label labelTitoloMappatura;
    @FXML private TreeView<String> alberoCommenti;
    @FXML private TextField nuovoCommentoField;
    @FXML private TextField strumentoField;
    @FXML private CheckBox checkUfficiale;
    @FXML private TextField inizioH, inizioM, inizioS;
    @FXML private TextField fineH, fineM, fineS;

    @FXML private TableView<SegmentoEsecuzione> tabellaSegmenti;
    @FXML private TableColumn<SegmentoEsecuzione, String> colSegTitolo;
    @FXML private TableColumn<SegmentoEsecuzione, String> colSegEsecutori;
    @FXML private TableColumn<SegmentoEsecuzione, String> colSegStrumenti;
    @FXML private TableColumn<SegmentoEsecuzione, String> colSegData;
    @FXML private TableColumn<SegmentoEsecuzione, String> colSegLuogo;
    @FXML private TableColumn<SegmentoEsecuzione, Float> colSegInizio;
    @FXML private TableColumn<SegmentoEsecuzione, Float> colSegFine;
    @FXML private TextField segTitoloField;
    @FXML private TextField segEsecField;
    @FXML private TextField segStrumentiField;
    @FXML private TextField segLuogoField;
    @FXML private TextField segInH, segInM, segInS;
    @FXML private TextField segOutH, segOutM, segOutS;
    @FXML private DatePicker segDataPicker;

    private Brano branoCorrente;
    private Map<TreeItem<String>, Commento> mappaCommenti = new HashMap<>();
    private SegmentoEsecuzione segmentoInModifica = null;

    /**
     * Inizializza i componenti grafici della vista iniettando il riferimento del brano selezionato.
     * Configura le factory delle celle per la TableView dei segmenti di concerto e implementa
     * i controlli di sicurezza basati sui ruoli (RBAC): espone le opzioni di marcatura ufficiale
     * e inserimento strumenti solo se l'utente in sessione possiede le credenziali di
     * AutoreInterprete o Amministratore.
     *
     * @param brano L'istanza dell'oggetto Brano da analizzare.
     */
    public void inizializzaDati(Brano brano) {
        this.branoCorrente = brano;
        titoloLabel.setText(brano.getTitolo());
        autoreAnnoLabel.setText(brano.getAutorePrincipale() + " (" + brano.getGenere() + ") - " + brano.getAnnoComposizione());
        proprietarioLabel.setText("Caricato da: " + brano.getProprietarioEmail());

        // Configurazione delle factory per il binding dei dati della tabella segmenti tramite Reflection
        colSegTitolo.setCellValueFactory(new PropertyValueFactory<>("titoloBrano"));
        colSegInizio.setCellValueFactory(new PropertyValueFactory<>("inizio"));
        colSegFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
        colSegEsecutori.setCellValueFactory(new PropertyValueFactory<>("esecutori"));
        colSegStrumenti.setCellValueFactory(new PropertyValueFactory<>("strumenti"));
        colSegData.setCellValueFactory(new PropertyValueFactory<>("dataEsecuzione"));
        colSegLuogo.setCellValueFactory(new PropertyValueFactory<>("luogoEsecuzione"));

        creaMenuTabellaSegmenti();

        // Controllo granulare dei permessi (RBAC) per l'abilitazione di controlli critici
        Utente loggato = SessionManager.getInstance().getUtenteLoggato();
        if (loggato instanceof AutoreInterprete || loggato instanceof Amministratore) {
            checkUfficiale.setVisible(true);
            checkUfficiale.setManaged(true);
            strumentoField.setVisible(true);
            strumentoField.setManaged(true);
        }
        aggiornaUI();
    }

    // --- LOGICA DI ELIMINAZIONE DEI COMMENTI ---

    /**
     * Gestisce la rimozione forzata di un commento selezionato dall'albero grafico.
     * Il metodo applica rigorosi controlli di autorizzazione difensiva: l'operazione è
     * consentita esclusivamente se l'utente corrente ricopre il ruolo di Amministratore
     * o coincide con l'autore originario del feedback. Se autorizzata, viene invocato un
     * algoritmo di rimozione ricorsiva sulla gerarchia ad albero prima di consolidare i dati su disco.
     */
    @FXML
    public void handleEliminaCommento() {
        TreeItem<String> selectedItem = alberoCommenti.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        Commento daEliminare = mappaCommenti.get(selectedItem);
        Utente loggato = SessionManager.getInstance().getUtenteLoggato();

        boolean isAdmin = loggato instanceof Amministratore;
        boolean isAutore = loggato.getEmail().equals(daEliminare.getAutoreEmail());

        if (isAdmin || isAutore) {
            // Esecuzione dell'algoritmo di epurazione ricorsiva all'interno della struttura composita
            boolean rimosso = cercaERimuoviCommento(branoCorrente.getCommenti(), daEliminare);

            if (rimosso) {
                salvaModificheSuDisco();
                aggiornaUI();
                System.out.println("Social: Commento rimosso.");
            }
        } else {
            // Ramo difensivo: blocco dell'azione e notifica di violazione dei privilegi tramite Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di Permessi");
            alert.setHeaderText("Azione non consentita");
            alert.setContentText("Non puoi eliminare i commenti degli altri utenti.");
            alert.showAndWait();
        }
    }

    /**
     * Algoritmo di ricerca e rimozione in profondità (Depth-First Search) per l'epurazione di un commento.
     * Scansiona la lista dei nodi radice e, in caso di mancato riscontro immediato, si propaga
     * ricorsivamente all'interno delle risposte annidate di ciascun nodo della gerarchia.
     *
     * @param lista La collezione corrente di commenti in cui cercare.
     * @param target L'istanza dell'oggetto Commento da rimuovere.
     * @return true se l'elemento è stato individuato ed eliminato con successo, false altrimenti.
     */
    private boolean cercaERimuoviCommento(List<Commento> lista, Commento target) {
        if (lista.remove(target)) return true;
        for (Commento c : lista) {
            if (cercaERimuoviCommento(c.getGerarchiaRisposte(), target)) return true;
        }
        return false;
    }

    // --- LOGICA DI INVIO DEI COMMENTI ---

    /**
     * Elabora e inserisce un nuovo commento all'interno della struttura del brano.
     * Valuta l'input testuale e analizza i campi numerici dei timestamp: se viene rilevato
     * un valore temporale valido, istanzia polimorficamente un oggetto di tipo {@link NotaTemporale},
     * altrimenti genera un {@link Commento} standard. Supporta l'inserimento mirato come risposta
     * agganciando il nuovo nodo al TreeItem correntemente selezionato nella UI.
     */
    @FXML
    public void handleInviaCommento() {
        String testo = nuovoCommentoField.getText().trim();
        if (testo.isEmpty()) return;
        Utente loggato = SessionManager.getInstance().getUtenteLoggato();

        float in = calcolaSecondi(inizioH, inizioM, inizioS);
        float fn = calcolaSecondi(fineH, fineM, fineS);

        // Controllo di coerenza cronologica per la prevenzione di anomalie temporali
        if (fn > 0 && fn < in) {
            new Alert(Alert.AlertType.ERROR, "L'intervallo temporale non è valido.").showAndWait();
            return;
        }

        // Instanziazione polimorfica condizionata dalla presenza di marcatori temporali
        Commento nuovo = (in > 0 || fn > 0) ?
                new NotaTemporale(testo, new Date(), checkUfficiale.isSelected(), loggato.getEmail(), in, fn) :
                new Commento(testo, new Date(), checkUfficiale.isSelected(), loggato.getEmail());

        if (strumentoField.isVisible()) {
            nuovo.setStrumentoUsato(strumentoField.getText());
        }

        TreeItem<String> selected = alberoCommenti.getSelectionModel().getSelectedItem();
        if (selected != null && mappaCommenti.containsKey(selected)) {
            // Inserimento subordinato come risposta all'interno del thread gerarchico
            mappaCommenti.get(selected).aggiungiRisposta(nuovo);
        } else {
            // Inserimento come feedback radice di primo livello del brano
            branoCorrente.aggiungiCommento(nuovo);
        }

        salvaModificheSuDisco();
        nuovoCommentoField.clear();
        strumentoField.clear();
        inizioH.clear(); inizioM.clear(); inizioS.clear();
        fineH.clear(); fineM.clear(); fineS.clear();
        aggiornaUI();
    }

    // --- GESTIONE DEI FILE E DEI COLLEGAMENTI (PREVENZIONE PERDITA DATI) ---

    /**
     * Finestra di dialogo preventiva finalizzata alla salvaguardia dell'integrità dei dati.
     * Intercetta i tentativi di sovrascrittura di un contenuto multimediale esistente,
     * avvisando esplicitamente l'utente che la sostituzione causerà l'epurazione automatica
     * e irreversibile della precedente mappatura del concerto.
     *
     * @return true se l'utente acconsente alla sostituzione del file, false se interrompe l'azione.
     */
    private boolean confermaSovrascrittura() {
        if (!branoCorrente.getDocumentiAssociati().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "L'associazione di un nuovo contenuto eliminerà quello attuale e la relativa mappatura concerto. Continuare?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.setTitle("Conferma Sostituzione");
            return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
        }
        return true;
    }

    /**
     * Gestisce l'associazione fisica di un file locale tramite FileChooser.
     * Sottopone l'azione a verifica di sovrascrittura e analizza l'estensione del file
     * scelto per allocare l'esatta sottoclasse concreta (Spartito, TracciaAudio, TracciaVideo),
     * aggiornando le dipendenze del brano.
     *
     * @param event L'evento ActionEvent scatenato dal click sul controllo.
     */
    @FXML
    public void handleAssociaFile(ActionEvent event) {
        if (!confermaSovrascrittura()) return;
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (f != null) {
            String p = f.getAbsolutePath();
            DocumentoDigitale d = p.endsWith(".pdf") ?
                    new Spartito(f.getName(), p) :
                    (p.endsWith(".mp3") ? new TracciaAudio(f.getName(), p) : new TracciaVideo(f.getName(), p));

            branoCorrente.getDocumentiAssociati().clear();
            branoCorrente.aggiungiDocumento(d);
            salvaModificheSuDisco();
            aggiornaUI();
        }
    }

    /**
     * Gestisce l'associazione ipertestuale di una risorsa web esterna tramite TextInputDialog.
     * Ripristina lo stato pulendo le associazioni precedenti e istanzia un oggetto CollegamentoWeb.
     *
     * @param event L'evento ActionEvent associato.
     */
    @FXML
    public void handleAssociaLink(ActionEvent event) {
        if (!confermaSovrascrittura()) return;
        TextInputDialog dialog = new TextInputDialog("https://...");
        Optional<String> r = dialog.showAndWait();

        r.ifPresent(url -> {
            branoCorrente.getDocumentiAssociati().clear();
            branoCorrente.aggiungiDocumento(new CollegamentoWeb("Link", url));
            salvaModificheSuDisco();
            aggiornaUI();
        });
    }

    // --- MAPPATURA CONCERTO ---

    /**
     * Gestisce l'inserimento o il consolidamento di un segmento di esecuzione (mappatura live).
     * Applica controlli difensivi bloccanti per verificare la presenza di una risorsa multimediale
     * e l'obbligatorietà della data dell'evento. Sfrutta una variabile di stato per operare:
     * - In modalità inserimento se il riferimento è nullo, aggiungendo un nuovo record alla traccia audio/video.
     * - In modalità modifica se punta a un segmento preesistente, aggiornandone i campi interni.
     */
    @FXML
    public void handleAggiungiSegmento() {
        if (branoCorrente.getDocumentiAssociati().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Associa prima un contenuto multimediale.").showAndWait();
            return;
        }
        if (segDataPicker.getValue() == null && segmentoInModifica == null) {
            new Alert(Alert.AlertType.ERROR, "Data obbligatoria.").showAndWait();
            return;
        }
        try {
            float in = calcolaSecondi(segInH, segInM, segInS);
            float out = calcolaSecondi(segOutH, segOutM, segOutS);

            if (out < in) {
                new Alert(Alert.AlertType.ERROR, "Tempo out minore di tempo in.").showAndWait();
                return;
            }

            String data = (segDataPicker.getValue() != null) ? segDataPicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";

            if (segmentoInModifica == null) {
                // Allocazione ed inserimento ex-novo del segmento temporale nella traccia multimediale attiva
                branoCorrente.getDocumentiAssociati().get(0).aggiungiSegmento(
                        new SegmentoEsecuzione(segTitoloField.getText(), in, out, segEsecField.getText(), segStrumentiField.getText(), data, segLuogoField.getText(), "Ottima")
                );
            } else {
                // Aggiornamento dei campi dell'oggetto esistente (Modifica in-place)
                segmentoInModifica.setTitoloBrano(segTitoloField.getText());
                segmentoInModifica.setInizio(in);
                segmentoInModifica.setFine(out);
                segmentoInModifica.setEsecutori(segEsecField.getText());
                segmentoInModifica.setStrumenti(segStrumentiField.getText());
                segmentoInModifica.setLuogoEsecuzione(segLuogoField.getText());
                if (!data.isEmpty()) {
                    segmentoInModifica.setDataEsecuzione(data);
                }
                segmentoInModifica = null;
                labelTitoloMappatura.setText("Mappa un nuovo brano:");
            }

            salvaModificheSuDisco();
            tabellaSegmenti.refresh();
            aggiornaUI();

            // Svuotamento dei campi della form per predisporre la UI a nuove immissioni
            segTitoloField.clear(); segEsecField.clear(); segStrumentiField.clear(); segLuogoField.clear();
            segInH.clear(); segInM.clear(); segInS.clear(); segOutH.clear(); segOutM.clear(); segOutS.clear();
            segDataPicker.setValue(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- METODI DI CONFIGURAZIONE E SUPPORTO ---

    /**
     * Sincronizza lo stato visivo dei componenti grafici della UI con i dati del modello in RAM.
     * Pulisce i vecchi riferimenti, ricostruisce ricorsivamente la visualizzazione gerarchica dei
     * commenti sulla TreeView ed aggiorna la TableView dei segmenti di concerto.
     */
    private void aggiornaUI() {
        mappaCommenti.clear();
        TreeItem<String> root = new TreeItem<>("Root");

        if (branoCorrente.getCommenti() != null) {
            for (Commento c : branoCorrente.getCommenti()) {
                aggiungiCommentoRicorsivo(c, root);
            }
        }
        alberoCommenti.setRoot(root);
        alberoCommenti.setShowRoot(false);

        if (!branoCorrente.getDocumentiAssociati().isEmpty()) {
            DocumentoDigitale doc = branoCorrente.getDocumentiAssociati().get(0);
            tabellaSegmenti.setItems(FXCollections.observableArrayList(doc.getSegmenti()));
            fileStatusLabel.setText("Collegato: " + doc.getNomeFile());
        } else {
            tabellaSegmenti.setItems(FXCollections.observableArrayList());
            fileStatusLabel.setText("Nessun file o link associati.");
        }
    }

    /**
     * Sviluppa ricorsivamente lo Scene Graph della TreeView iniettando i nodi di commento.
     * Costruisce la stringa visiva normalizzando le informazioni dell'autore, della strumentazione,
     * dello status ufficiale (sostituendo l'emoji con la notazione formale `[UFFICIALE]`)
     * e degli intervalli di tempo, propagando l'elaborazione lungo i rami delle risposte figlie.
     *
     * @param c Il nodo di commento corrente da analizzare.
     * @param parentItem Il TreeItem grafico di destinazione a cui agganciare il nodo generato.
     */
    private void aggiungiCommentoRicorsivo(Commento c, TreeItem<String> parentItem) {
        String infoExtra = (c.getStrumentoUsato() != null && !c.getStrumentoUsato().isEmpty()) ? " [" + c.getStrumentoUsato() + "]" : "";
        String testo = (c.isUfficiale() ? "[UFFICIALE] " : "") + "(" + c.getAutoreEmail() + ")" + infoExtra + ": " + c.getTesto();

        if (c instanceof NotaTemporale nt) {
            testo += " (" + nt.getIstanteInizio() + "s - " + nt.getIstanteFine() + "s)";
        }

        TreeItem<String> newItem = new TreeItem<>(testo);
        newItem.setExpanded(true);
        parentItem.getChildren().add(newItem);
        mappaCommenti.put(newItem, c);

        for (Commento risp : c.getGerarchiaRisposte()) {
            aggiungiCommentoRicorsivo(risp, newItem);
        }
    }

    /**
     * Metodo di utilità per la conversione dei parametri scompattati di tempo nel corrispettivo in secondi primitivi.
     *
     * @param h Campo di input associato alle ore.
     * @param m Campo di input associato ai minuti.
     * @param s Campo di input associato ai secondi.
     * @return Il computo complessivo espresso in formato float.
     */
    private float calcolaSecondi(TextField h, TextField m, TextField s) {
        try {
            float ore = h.getText().isEmpty() ? 0 : Float.parseFloat(h.getText().trim());
            float min = m.getText().isEmpty() ? 0 : Float.parseFloat(m.getText().trim());
            float sec = s.getText().isEmpty() ? 0 : Float.parseFloat(s.getText().trim());
            return (ore * 3600) + (min * 60) + sec;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Configura e associa un menu a comparsa (ContextMenu) alla TableView dei segmenti.
     * Definisce le opzioni formali per avviare la modifica e l'eliminazione dei record dei concerti.
     */
    private void creaMenuTabellaSegmenti() {
        ContextMenu menu = new ContextMenu();

        MenuItem edit = new MenuItem("Modifica");
        edit.setOnAction(e -> handlePreparaModificaSegmento());

        MenuItem delete = new MenuItem("Elimina");
        delete.setOnAction(e -> handleEliminaSegmento());

        menu.getItems().addAll(edit, delete);
        tabellaSegmenti.setContextMenu(menu);
    }

    /**
     * Prepara l'interfaccia utente caricando i metadati del segmento selezionato nella form di input,
     * impostando la UI nello stato operativo di modifica per il record concertistico.
     */
    private void handlePreparaModificaSegmento() {
        SegmentoEsecuzione s = tabellaSegmenti.getSelectionModel().getSelectedItem();
        if (s == null) return;

        this.segmentoInModifica = s;
        labelTitoloMappatura.setText("MODIFICA Brano del Concerto:");
        segTitoloField.setText(s.getTitoloBrano());
        segEsecField.setText(s.getEsecutori());
        segStrumentiField.setText(s.getStrumenti());
        segLuogoField.setText(s.getLuogoEsecuzione());

        int in = (int) s.getInizio();
        segInH.setText(String.valueOf(in / 3600));
        segInM.setText(String.valueOf((in % 3600) / 60));
        segInS.setText(String.valueOf(in % 60));

        int out = (int) s.getFine();
        segOutH.setText(String.valueOf(out / 3600));
        segOutM.setText(String.valueOf((out % 3600) / 60));
        segOutS.setText(String.valueOf(out % 60));
    }

    /**
     * Rimuove il segmento selezionato dalla traccia e sincronizza le modifiche su disco.
     */
    private void handleEliminaSegmento() {
        SegmentoEsecuzione s = tabellaSegmenti.getSelectionModel().getSelectedItem();
        if (s != null && !branoCorrente.getDocumentiAssociati().isEmpty()) {
            branoCorrente.getDocumentiAssociati().get(0).getSegmenti().remove(s);
            salvaModificheSuDisco();
            aggiornaUI();
        }
    }

    /**
     * Innesca la riproduzione del media richiamando il metodo polimorfico dell'allegato digitale.
     */
    @FXML
    public void handleRiproduciMedia() {
        if (!branoCorrente.getDocumentiAssociati().isEmpty()) {
            branoCorrente.getDocumentiAssociati().get(0).apriConPlayerEsterno();
        }
    }

    /**
     * Interfaccia con il sottosistema di persistenza JSON per sovrascrivere l'istanza
     * modificata del brano all'interno del catalogo centralizzato memorizzato su disco.
     */
    private void salvaModificheSuDisco() {
        GestoreJSON g = new GestoreJSON();
        List<Brano> cat = g.caricaCatalogo("catalogo.json");
        for (int i = 0; i < cat.size(); i++) {
            if (cat.get(i).getTitolo().equals(branoCorrente.getTitolo())) {
                cat.set(i, branoCorrente);
                break;
            }
        }
        g.salvaCatalogo(cat, "catalogo.json");
    }

    /**
     * Gestisce la chiusura ordinaria della finestra estraendo lo Stage dal thread grafico dell'evento.
     *
     * @param event L'evento ActionEvent associato.
     */
    @FXML
    public void handleChiudi(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}