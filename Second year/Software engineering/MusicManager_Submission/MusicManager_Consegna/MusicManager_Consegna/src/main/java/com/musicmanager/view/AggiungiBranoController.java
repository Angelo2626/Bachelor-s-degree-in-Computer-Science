package com.musicmanager.view;

import com.musicmanager.model.catalog.*;
import com.musicmanager.persistence.GestoreJSON;
import com.musicmanager.controller.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;

/**
 * Controller di presentazione grafico associato alla vista fxml di inserimento e modifica dei brani.
 * Questa classe orchestra il ciclo di vita della finestra di dialogo, implementando un pattern
 * a doppio stato: funge da modulo di inserimento per una nuova entità se il riferimento interno
 * sentinella è nullo, oppure muta dinamicamente in modulo di modifica se viene iniettata
 * un'istanza preesistente di un brano.
 */
public class AggiungiBranoController {

    @FXML private TextField titoloField;
    @FXML private TextField autoreField;
    @FXML private TextField annoField;
    @FXML private ChoiceBox<String> genereChoice;
    @FXML private Label statusLabel;
    @FXML private Label titoloFinestra;

    // Riferimento sentinella utilizzato per discriminare lo stato operativo della vista:
    // se impostato a null indica un'operazione di Inserimento, altrimenti di Modifica.
    private Brano branoInModifica = null;

    /**
     * Metodo di inizializzazione del framework JavaFX, invocato automaticamente dopo
     * il caricamento del file FXML dello Scene Graph.
     * Provvede al caricamento a caldo dei dizionari tassonomici tramite il modulo di persistenza
     * e ne estrae la categoria dedicata ai generi musicali per popolare dinamicamente
     * il componente grafico di selezione ChoiceBox.
     */
    @FXML
    public void initialize() {
        GestoreJSON g = new GestoreJSON();
        List<Dizionario> dizs = g.caricaDizionari("dizionari.json");

        // Scansione dei dizionari di sistema per estrarre il dominio dei generi musicali ammessi
        for (Dizionario d : dizs) {
            if ("Generi".equals(d.getTipoDizionario())) {
                genereChoice.setItems(FXCollections.observableArrayList(d.getVoci()));
            }
        }
    }

    /**
     * Consente l'iniezione di dipendenza di un'istanza di tipo Brano da parte di un controller esterno,
     * forzando il passaggio della vista allo stato operativo di "Modifica".
     * Aggiorna i componenti testuali e grafici della UI con i metadati correnti dell'oggetto e,
     * per garantire l'integrità del database ipertestuale, inibisce la modifica del titolo
     * del brano, che funge da chiave logica di allineamento nel file JSON.
     *
     * @param b L'istanza dell'oggetto Brano che si intende sottoporre a modifica.
     */
    public void setBranoDaModificare(Brano b) {
        this.branoInModifica = b;
        titoloFinestra.setText("Modifica Brano");
        titoloField.setText(b.getTitolo());
        autoreField.setText(b.getAutorePrincipale());
        annoField.setText(String.valueOf(b.getAnnoComposizione()));
        genereChoice.setValue(b.getGenere());

        // Applichiamo una restrizione difensiva per impedire la variazione della chiave logica
        titoloField.setEditable(false);
    }

    /**
     * Gestisce l'evento di salvataggio scatenato dalla pressione del pulsante di conferma nella UI.
     * Il metodo esegue preliminarmente la pulizia e la validazione sintattica dei campi di input;
     * dopodiché, applicando i principi della programmazione difensiva in un blocco try-catch,
     * effettua il parsing numerico dell'anno e biforca l'esecuzione:
     * - In caso di Inserimento, verifica tramite la Stream API l'assenza di duplicati logici (case-insensitive)
     * e associa la traccia all'email dell'utente attualmente in sessione.
     * - In caso di Modifica, scansiona il catalogo per aggiornare l'entità corrispondente.
     * Al termine, consolida i cambiamenti serializzando lo stato sul database JSON.
     *
     * @param event L'evento di tipo ActionEvent generato dal click sul pulsante.
     */
    @FXML
    public void handleSalva(ActionEvent event) {
        String t = titoloField.getText().trim();
        String a = autoreField.getText().trim();
        String y = annoField.getText().trim();
        String g = genereChoice.getValue();

        // Controllo di validità difensivo: impedisce la sottomissione di campi vuoti o incompleti
        if (t.isEmpty() || a.isEmpty() || y.isEmpty() || g == null) {
            statusLabel.setText("Compila tutti i campi.");
            return;
        }

        try {
            // Conversione controllata della stringa dell'anno in un valore intero primitivo
            int anno = Integer.parseInt(y);
            GestoreJSON gestore = new GestoreJSON();

            // Logica di validazione strutturata dei campi in base ai dizionari tassonomici
            List<Dizionario> dizionari = gestore.caricaDizionari("dizionari.json");

            boolean genereValido = dizionari.stream()
                    .filter(d -> "Generi".equals(d.getTipoDizionario()))
                    .anyMatch(d -> d.verificaEsistenza(g));

            boolean autoreValido = dizionari.stream()
                    .filter(d -> "Autori".equals(d.getTipoDizionario()))
                    .anyMatch(d -> d.verificaEsistenza(a));

            if (!genereValido || !autoreValido) {
                statusLabel.setText("Errore: Genere o Autore non validi nei dizionari.");
                return;
            }

            List<Brano> catalogo = gestore.caricaCatalogo("catalogo.json");

            if (branoInModifica == null) {
                // --- RAMO LOGICO: INSERIMENTO NUOVO BRANO ---
                if (catalogo.stream().anyMatch(b -> b.getTitolo().equalsIgnoreCase(t))) {
                    statusLabel.setText("Errore: Titolo già esistente.");
                    return;
                }
                // Recupero accoppiato del proprietario tramite il gestore globale della sessione (Singleton)
                String email = SessionManager.getInstance().getUtenteLoggato().getEmail();
                catalogo.add(new Brano(t, anno, a, email, g));
            } else {
                // --- RAMO LOGICO: MODIFICA ELEMENTO ESISTENTE ---
                for (Brano b : catalogo) {
                    if (b.getTitolo().equals(branoInModifica.getTitolo())) {
                        b.setAutorePrincipale(a);
                        b.setAnnoComposizione(anno);
                        b.setGenere(g);
                        break;
                    }
                }
            }

            // Consolidamento persistente delle modifiche su disco
            gestore.salvaCatalogo(catalogo, "catalogo.json");
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Salvato! Chiudi e premi Reset.");

        } catch (NumberFormatException nfe) {
            statusLabel.setText("Errore: L'anno deve essere un numero intero.");
        } catch (Exception e) {
            // Intercettazione robusta di eccezioni di I/O di basso livello
            statusLabel.setText("Errore nei dati.");
        }
    }

    /**
     * Gestisce l'interruzione volontaria dell'operazione corrente da parte dell'utente.
     * Estrae dinamicamente il riferimento al nodo grafico che ha scatenato l'evento,
     * risale la gerarchia dello Scene Graph per individuare la Scena e lo Stage (finestra)
     * ospitante, e provvede alla chiusura formale del relativo frame visivo nativo.
     *
     * @param event L'evento ActionEvent generato dal click sul pulsante Annulla.
     */
    @FXML
    public void handleAnnulla(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}