package com.musicmanager.model.catalog;

/**
 * Rappresenta un segmento temporale specifico o un singolo brano tracciato all'interno
 * di una registrazione più estesa (come un concerto, un'opera o una suite musicale).
 * Questa classe incapsula i metadati spaziotemporali e i dettagli tecnici richiesti dalle specifiche,
 * consentendo di mappare con precisione i timestamp di inizio e fine, i musicisti coinvolti,
 * gli strumenti utilizzati e i dettagli contestuali obbligatori per i flussi dal vivo,
 * quali la data e il luogo dell'esecuzione.
 */
public class SegmentoEsecuzione {

    private String titoloBrano;
    private float inizio;
    private float fine;
    private String esecutori;
    private String strumenti;
    private String dataEsecuzione;
    private String luogoEsecuzione;
    private String noteQualita;

    /**
     * Costruttore completo della classe SegmentoEsecuzione.
     * Alloca in memoria Heap un nuovo marcatore temporale valorizzando tutti i dettagli tecnici,
     * descrittivi e geografici dell'istanza.
     * * @param titoloBrano Il titolo del brano specifico contenuto nel segmento.
     * @param inizio Il timestamp espresso in secondi (o minuti) che delimita l'inizio del segmento.
     * @param fine Il timestamp che delimita la conclusione del segmento all'interno del file.
     * @param esecutori I nomi dei musicisti o interpreti che partecipano all'esecuzione.
     * @param strumenti Gli strumenti musicali impiegati nel segmento.
     * @param data La data in cui ha avuto luogo l'evento dal vivo.
     * @param luogo La collocazione geografica o la sala concerti in cui è stata registrata la traccia.
     * @param noteQualita Eventuali annotazioni tecniche sulla fedeltà audio o sullo stato della traccia.
     */
    public SegmentoEsecuzione(String titoloBrano, float inizio, float fine, String esecutori, String strumenti, String data, String luogo, String noteQualita) {
        this.titoloBrano = titoloBrano;
        this.inizio = inizio;
        this.fine = fine;
        this.esecutori = esecutori;
        this.strumenti = strumenti;
        this.dataEsecuzione = data;
        this.luogoEsecuzione = luogo;
        this.noteQualita = noteQualita;
    }

    // --- METODI DI ACCESSO (GETTER E SETTER) ---

    /**
     * Restituisce il titolo del brano associato al segmento.
     * @return La stringa del titolo del brano.
     */
    public String getTitoloBrano() {
        return titoloBrano;
    }

    /**
     * Aggiorna il titolo del brano associato al segmento.
     * @param titoloBrano Il nuovo titolo da impostare.
     */
    public void setTitoloBrano(String titoloBrano) {
        this.titoloBrano = titoloBrano;
    }

    /**
     * Restituisce il marcatore temporale di inizio esecuzione.
     * @return Il valore in virgola mobile corrispondente al punto di avvio.
     */
    public float getInizio() {
        return inizio;
    }

    /**
     * Configura il punto temporale di avvio del segmento.
     * @param inizio Il valore float di inizio.
     */
    public void setInizio(float inizio) {
        this.inizio = inizio;
    }

    /**
     * Restituisce il marcatore temporale di fine esecuzione.
     * @return Il valore in virgola mobile corrispondente alla fine del blocco.
     */
    public float getFine() {
        return fine;
    }

    /**
     * Configura il punto temporale di conclusione del segmento.
     * @param fine Il valore float di fine.
     */
    public void setFine(float fine) {
        this.fine = fine;
    }

    /**
     * Restituisce l'elenco testuale degli esecutori coinvolti.
     * @return Stringa contenente i musicisti.
     */
    public String getEsecutori() {
        return esecutori;
    }

    /**
     * Aggiorna l'elenco degli esecutori del segmento.
     * @param esecutori La stringa con i nuovi interpreti.
     */
    public void setEsecutori(String esecutori) {
        this.esecutori = esecutori;
    }

    /**
     * Restituisce gli strumenti musicali catalogati per questa esecuzione.
     * @return Stringa descrittiva degli strumenti.
     */
    public String getStrumenti() {
        return strumenti;
    }

    /**
     * Imposta la lista di strumenti utilizzati nel segmento.
     * @param strumenti I dettagli sugli strumenti da associare.
     */
    public void setStrumenti(String strumenti) {
        this.strumenti = strumenti;
    }

    /**
     * Restituisce la data in cui è stata registrata l'esecuzione dal vivo.
     * @return La stringa della data di esecuzione.
     */
    public String getDataEsecuzione() {
        return dataEsecuzione;
    }

    /**
     * Imposta o modifica la data dell'evento live.
     * @param dataEsecuzione La nuova stringa della data.
     */
    public void setDataEsecuzione(String dataEsecuzione) {
        this.dataEsecuzione = dataEsecuzione;
    }

    /**
     * Restituisce il luogo fisico o la venue dell'evento dal vivo.
     * @return La stringa corrispondente al luogo di esecuzione.
     */
    public String getLuogoEsecuzione() {
        return luogoEsecuzione;
    }

    /**
     * Aggiorna le informazioni sulla geolocalizzazione o sulla struttura ospitante.
     * @param luogoEsecuzione Il nome della venue o della città da impostare.
     */
    public void setLuogoEsecuzione(String luogoEsecuzione) {
        this.luogoEsecuzione = luogoEsecuzione;
    }

    /**
     * Fornisce le note tecniche relative alla qualità della traccia o dell'esecuzione.
     * @return Stringa contenente le annotazioni qualitative.
     */
    public String getNoteQualita() {
        return noteQualita;
    }

    /**
     * Inserisce o modifica le note di qualità audio/tecnica del segmento.
     * @param noteQualita Il testo descrittivo dei dettagli qualitativi.
     */
    public void setNoteQualita(String noteQualita) {
        this.noteQualita = noteQualita;
    }
}