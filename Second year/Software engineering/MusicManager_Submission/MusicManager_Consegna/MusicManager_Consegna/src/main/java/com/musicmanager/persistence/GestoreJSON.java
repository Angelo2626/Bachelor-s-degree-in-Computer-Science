package com.musicmanager.persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.musicmanager.model.catalog.*;
import com.musicmanager.model.users.*;
import com.musicmanager.model.feedback.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente centralizzato dedicato alla persistenza e al recupero dei dati in formato JSON.
 * Questa classe orchestra la serializzazione e deserializzazione dell'intero stato dell'applicazione,
 * inclusi il catalogo dei brani, i profili utente e i dizionari di sistema, interfacciandosi con la libreria Google Gson.
 * * Per superare i limiti strutturali del formato JSON nella rappresentazione dell'ereditarietà e del polimorfismo,
 * il costruttore registra adattatori gerarchici di tipo (Type Hierarchy Adapters) personalizzati.
 * Questo approccio architetturale assicura che, in fase di scrittura, venga iniettato un metadato discriminante
 * e che, in fase di lettura, il ClassLoader ripristini l'esatto tipo concreto degli oggetti (es. TracciaAudio,
 * NotaTemporale, AutoreInterprete) all'interno dei riferimenti polimorfici della superclasse.
 */
public class GestoreJSON {

    private Gson gson;

    /**
     * Costruttore della classe GestoreJSON.
     * Configura l'ambiente operativo di Gson definendo e registrando gli adattatori polimorfici custom
     * per la gestione delle gerarchie di DocumentoDigitale, UtenteAutorizzato e Commento.
     * Attiva inoltre la formattazione avanzata (Pretty Printing) per garantire la leggibilità dei file su disco.
     */
    public GestoreJSON() {

        // --- ADATTATORI POLIMORFICI PER LA GERARCHIA DEI DOCUMENTI DIGITALI ---

        JsonSerializer<DocumentoDigitale> docSerializer = (src, t, ctx) -> {
            JsonObject obj = new JsonObject();
            // Inseriamo il nome semplice della classe concreta come discriminante per la deserializzazione
            obj.addProperty("typeClass", src.getClass().getSimpleName());
            obj.addProperty("nomeFile", src.getNomeFile());
            obj.addProperty("percorsoLocale", src.getPercorsoLocale());
            obj.add("segmenti", ctx.serialize(src.getSegmenti()));
            return obj;
        };

        JsonDeserializer<DocumentoDigitale> docDeserializer = (json, t, ctx) -> {
            JsonObject obj = json.getAsJsonObject();
            String type = obj.get("typeClass").getAsString();

            // Pattern Matching basato sull'arrow switch per riallocare la sottoclasse esatta
            DocumentoDigitale d = switch (type) {
                case "TracciaAudio" -> new TracciaAudio(obj.get("nomeFile").getAsString(), obj.get("percorsoLocale").getAsString());
                case "TracciaVideo" -> new TracciaVideo(obj.get("nomeFile").getAsString(), obj.get("percorsoLocale").getAsString());
                case "Spartito" -> new Spartito(obj.get("nomeFile").getAsString(), obj.get("percorsoLocale").getAsString());
                default -> new CollegamentoWeb(obj.get("nomeFile").getAsString(), obj.get("percorsoLocale").getAsString());
            };

            // Recupero ricorsivo della collezione dei segmenti temporali associati al documento
            if (obj.has("segmenti")) {
                List<SegmentoEsecuzione> segs = ctx.deserialize(obj.get("segmenti"), new TypeToken<ArrayList<SegmentoEsecuzione>>(){}.getType());
                if (segs != null) {
                    for (SegmentoEsecuzione s : segs) {
                        d.aggiungiSegmento(s);
                    }
                }
            }
            return d;
        };

        // --- ADATTATORI POLIMORFICI PER LA GERARCHIA DEGLI UTENTI AUTORIZZATI ---

        JsonSerializer<UtenteAutorizzato> userSerializer = (src, t, ctx) -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", (src instanceof AutoreInterprete) ? "Autore" : "Standard");
            obj.addProperty("id", src.getId());
            obj.addProperty("email", src.getEmail());
            obj.addProperty("password", src.getPassword());
            obj.addProperty("nome", src.getNome());
            obj.addProperty("cognome", src.getCognome());
            obj.addProperty("attivo", src.isAttivo());
            obj.addProperty("dataRegistrazione", src.getDataRegistrazione() != null ? src.getDataRegistrazione().getTime() : new java.util.Date().getTime());

            // Serializzazione condizionale del campo biografia se l'istanza è un interprete
            if (src instanceof AutoreInterprete) {
                obj.addProperty("biografia", ((AutoreInterprete) src).getBiography());
            }
            return obj;
        };

        JsonDeserializer<UtenteAutorizzato> userDeserializer = (json, t, ctx) -> {
            JsonObject obj = json.getAsJsonObject();
            String type = obj.get("type").getAsString();

            // Recupero della data di registrazione persistita (epoch millis); fallback all'istante corrente per i record legacy privi del campo
            java.util.Date dataRegistrazione = obj.has("dataRegistrazione") ? new java.util.Date(obj.get("dataRegistrazione").getAsLong()) : new java.util.Date();

            UtenteAutorizzato u = "Autore".equals(type) ?
                    new AutoreInterprete(obj.get("id").getAsString(), obj.get("email").getAsString(), obj.get("password").getAsString(), obj.get("nome").getAsString(), obj.get("cognome").getAsString(), dataRegistrazione, obj.has("biografia") ? obj.get("biografia").getAsString() : "") :
                    new UtenteAutorizzato(obj.get("id").getAsString(), obj.get("email").getAsString(), obj.get("password").getAsString(), obj.get("nome").getAsString(), obj.get("cognome").getAsString(), dataRegistrazione);

            u.setAttivo(obj.get("attivo").getAsBoolean());
            return u;
        };

        // --- ADATTATORI POLIMORFICI PER LA GERARCHIA DEI COMMENTI E FEEDBACK ---

        JsonSerializer<Commento> commSerializer = (src, t, ctx) -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", (src instanceof NotaTemporale) ? "Nota" : "Standard");
            obj.addProperty("testo", src.getTesto());
            obj.addProperty("autoreEmail", src.getAutoreEmail());
            obj.addProperty("isUfficiale", src.isUfficiale());
            obj.addProperty("dataInserimento", src.getDataInserimento() != null ? src.getDataInserimento().getTime() : new java.util.Date().getTime());
            obj.addProperty("strumentoUsato", src.getStrumentoUsato());
            obj.add("risposte", ctx.serialize(src.getGerarchiaRisposte()));

            // Iniezione dei metadati spaziotemporali in caso di nota temporale legata al media
            if (src instanceof NotaTemporale nt) {
                obj.addProperty("inizio", nt.getIstanteInizio());
                obj.addProperty("fine", nt.getIstanteFine());
            }
            return obj;
        };

        JsonDeserializer<Commento> commDeserializer = (json, t, ctx) -> {
            JsonObject obj = json.getAsJsonObject();
            Commento c;

            // Recupero della data di inserimento persistita (epoch millis); fallback all'istante corrente per i record legacy privi del campo
            java.util.Date dataInserimento = obj.has("dataInserimento") ? new java.util.Date(obj.get("dataInserimento").getAsLong()) : new java.util.Date();

            if ("Nota".equals(obj.get("type").getAsString())) {
                c = new NotaTemporale(obj.get("testo").getAsString(), dataInserimento, obj.get("isUfficiale").getAsBoolean(), obj.get("autoreEmail").getAsString(), obj.get("inizio").getAsFloat(), obj.get("fine").getAsFloat());
            } else {
                c = new Commento(obj.get("testo").getAsString(), dataInserimento, obj.get("isUfficiale").getAsBoolean(), obj.get("autoreEmail").getAsString());
            }

            if (obj.has("strumentoUsato") && !obj.get("strumentoUsato").isJsonNull()) {
                c.setStrumentoUsato(obj.get("strumentoUsato").getAsString());
            }

            // Ricostruzione ricorsiva della struttura ad albero delle risposte (Pattern Composite)
            if (obj.has("risposte")) {
                List<Commento> risp = ctx.deserialize(obj.get("risposte"), new TypeToken<ArrayList<Commento>>(){}.getType());
                if (risp != null) {
                    for (Commento r : risp) {
                        c.aggiungiRisposta(r);
                    }
                }
            }
            return c;
        };

        // Consolidamento delle configurazioni nel motore Gson e abilitazione dell'incolonnamento
        this.gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(DocumentoDigitale.class, docSerializer)
                .registerTypeHierarchyAdapter(DocumentoDigitale.class, docDeserializer)
                .registerTypeHierarchyAdapter(UtenteAutorizzato.class, userSerializer)
                .registerTypeHierarchyAdapter(UtenteAutorizzato.class, userDeserializer)
                .registerTypeHierarchyAdapter(Commento.class, commSerializer)
                .registerTypeHierarchyAdapter(Commento.class, commDeserializer)
                .setPrettyPrinting()
                .create();
    }

    // --- METODI DI PERSISTENZA DEL CATALOGO BRANI ---

    /**
     * Serializza l'intera collezione dei brani musicali e provvede al salvataggio su disco.
     * * @param cat La lista di oggetti Brano che costituiscono il catalogo in RAM.
     * @param p Il percorso di destinazione del file JSON all'interno del file system.
     */
    public void salvaCatalogo(List<Brano> cat, String p) {
        try (FileWriter w = new FileWriter(p)) {
            gson.toJson(cat, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica il catalogo musicale leggendo il file JSON dal percorso specificato.
     * Sfrutta la riflessione tramite TypeToken per mappare correttamente la lista generica.
     * * @param p Il percorso del file JSON da leggere.
     * @return La lista dei brani ricostruiti in memoria, o una collezione vuota in caso di errore.
     */
    public List<Brano> caricaCatalogo(String p) {
        try (FileReader r = new FileReader(p)) {
            Type t = new TypeToken<ArrayList<Brano>>(){}.getType();
            return gson.fromJson(r, t);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --- METODI DI PERSISTENZA DELLE UTENZE ---

    /**
     * Serializza la lista degli utenti autorizzati memorizzandola nel file specificato.
     * * @param u La lista contenente i profili utente gestiti in RAM.
     * @param p Il percorso di salvataggio del file JSON.
     */
    public void salvaUtenti(List<UtenteAutorizzato> u, String p) {
        try (FileWriter w = new FileWriter(p)) {
            gson.toJson(u, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica e ricostruisce l'elenco degli utenti autorizzati registrati nel file JSON.
     * * @param p Il percorso del file JSON contenente i dati di profilazione.
     * @return La lista degli utenti recuperati, oppure una lista vuota in caso di anomalia di I/O.
     */
    public List<UtenteAutorizzato> caricaUtenti(String p) {
        try (FileReader r = new FileReader(p)) {
            Type t = new TypeToken<ArrayList<UtenteAutorizzato>>(){}.getType();
            return gson.fromJson(r, t);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --- METODI DI PERSISTENZA DEI DIZIONARI CONTROLLATI ---

    /**
     * Salva lo stato corrente dei dizionari e dei vocabolari controllati di sistema.
     * * @param d La lista delle istanze di Dizionario attive nell'applicazione.
     * @param p Il percorso fisico in cui archiviare i dati.
     */
    public void salvaDizionari(List<Dizionario> d, String p) {
        try (FileWriter w = new FileWriter(p)) {
            gson.toJson(d, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica i dizionari tassonomici dal disco.
     * Questo metodo adotta una strategia di programmazione difensiva a tolleranza d'errore (fault-tolerance):
     * qualora il file fosse inesistente, vuoto o corrotto, il blocco catch intercetta l'anomalia
     * e provvede a istanziare e restituire immediatamente un set di dizionari di base obbligatori
     * ("Generi" e "Strumenti"). Questo garantisce che l'applicazione rimanga pienamente operativa
     * e coerente fin dal suo primo avvio assoluto.
     * * @param p Il percorso del file JSON da cui attingere i dizionari.
     * @return La lista dei dizionari validati, oppure la struttura predefinita di bootstrap in caso di errore.
     */
    public List<Dizionario> caricaDizionari(String p) {
        try (FileReader r = new FileReader(p)) {
            Type t = new TypeToken<ArrayList<Dizionario>>(){}.getType();
            List<Dizionario> lista = gson.fromJson(r, t);
            if (lista == null || lista.isEmpty()) {
                throw new Exception("File vuoto o non inizializzato");
            }
            return lista;
        } catch (Exception e) {
            // Ramo difensivo di fallback: inizializzazione a caldo dei dizionari minimi strutturali
            List<Dizionario> defaultDiz = new ArrayList<>();
            defaultDiz.add(new Dizionario("Generi"));
            defaultDiz.add(new Dizionario("Strumenti"));
            return defaultDiz;
        }
    }
}