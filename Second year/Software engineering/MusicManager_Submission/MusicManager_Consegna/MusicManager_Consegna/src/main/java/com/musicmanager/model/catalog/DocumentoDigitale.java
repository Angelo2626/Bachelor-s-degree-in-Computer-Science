package com.musicmanager.model.catalog;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che definisce il concetto generico di risorsa o file multimediale
 * associato a un'opera musicale.
 * Funge da superclasse all'interno della gerarchia dei documenti digitali, esponendo
 * proprietà comuni come il nome del file, il percorso di archiviazione e la tipologia di risorsa.
 * Include inoltre il supporto per la suddivisione in segmenti temporali (timestamp), utile per
 * la scomposizione e l'indicizzazione interna di tracce lunghe o registrazioni di concerti.
 */
public abstract class DocumentoDigitale {

    private String nomeFile;
    private String percorsoLocale;
    private String tipo; // Identifica il formato della risorsa, ad esempio "PDF", "MP3", "MP4"

    // Collezione di segmenti temporali introdotta per la gestione avanzata dei timestamp
    // all'interno dello stesso file multimediale.
    private List<SegmentoEsecuzione> segmenti;

    /**
     * Costruttore della classe astratta DocumentoDigitale.
     * Inizializza i metadati descrittivi fondamentali del file e provvede a istanziare
     * la lista dei segmenti temporali per prevenire eccezioni di tipo NullPointerException
     * nelle fasi successive di utilizzo.
     * * @param nomeFile Il nome mnemonico o l'etichetta associata al file.
     * @param percorsoLocale Il percorso di archiviazione sul file system o la stringa di puntamento.
     * @param tipo Il formato specifico del documento.
     */
    public DocumentoDigitale(String nomeFile, String percorsoLocale, String tipo) {
        this.nomeFile = nomeFile;
        this.percorsoLocale = percorsoLocale;
        this.tipo = tipo;
        this.segmenti = new ArrayList<>();
    }

    /**
     * Metodo astratto privo di corpo implementativo.
     * Sfrutta il meccanismo del polimorfismo per obbligare tutte le classi derivate
     * a definire la logica esatta di apertura e riproduzione della risorsa,
     * interfacciandosi in modo specifico con le API nativizzate del Sistema Operativo ospitante.
     */
    public abstract void apriConPlayerEsterno();

    /**
     * Simula il trasferimento o il download del file digitale verso una destinazione locale.
     * * @param percorsoDestinazione Il cammino di directory in cui salvare la risorsa.
     * @return true per indicare il corretto completamento dell'operazione simulata.
     */
    public boolean scaricaFile(String percorsoDestinazione) {
        System.out.println("Simulazione download del file " + nomeFile + " verso: " + percorsoDestinazione);
        return true;
    }

    /**
     * Restituisce una stringa descrittiva contenente i dettagli salienti della risorsa digitale.
     * * @return Una stringa formattata con il nome e la tipologia di documento.
     */
    public String getDettagliEsecuzione() {
        return "Documento: " + nomeFile + " | Tipo: " + tipo;
    }

    // --- METODI DI ACCESSO (GETTER E SETTER) ---

    /**
     * Restituisce il nome logico del file multimediale.
     * @return La stringa del nome del file.
     */
    public String getNomeFile() {
        return nomeFile;
    }

    /**
     * Aggiorna il nome associato alla risorsa.
     * @param nomeFile Il nuovo nome da assegnare.
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    /**
     * Restituisce il percorso fisico o l'indirizzo logico memorizzato.
     * @return La stringa contenente il percorso.
     */
    public String getPercorsoLocale() {
        return percorsoLocale;
    }

    /**
     * Modifica il percorso locale del documento digitale.
     * @param percorsoLocale Il nuovo percorso da impostare.
     */
    public void setPercorsoLocale(String percorsoLocale) {
        this.percorsoLocale = percorsoLocale;
    }

    /**
     * Restituisce l'estensione o la tipologia di formato del file.
     * @return La stringa del tipo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Imposta il formato identificativo del documento.
     * @param tipo La stringa che rappresenta il tipo.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Fornisce l'elenco completo dei segmenti temporali mappati sulla risorsa.
     * @return La lista contenente le istanze di SegmentoEsecuzione.
     */
    public List<SegmentoEsecuzione> getSegmenti() {
        return segmenti;
    }

    /**
     * Consente di associare un'intera collezione preesistente di segmenti al documento.
     * @param segmenti La nuova lista di segmenti da agganciare.
     */
    public void setSegmenti(List<SegmentoEsecuzione> segmenti) {
        this.segmenti = segmenti;
    }

    /**
     * Aggiunge un singolo segmento o marcatore temporale alla risorsa corrente.
     * Il metodo integra una verifica difensiva per assicurarsi che l'oggetto passato
     * sia valido e non nullo prima di inserirlo nella collezione.
     * * @param s L'istanza del segmento di esecuzione da registrare.
     */
    public void aggiungiSegmento(SegmentoEsecuzione s) {
        if (s != null) {
            this.segmenti.add(s);
        }
    }
}