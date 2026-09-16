package com.musicmanager.model.feedback;

import java.util.Date;

/**
 * Specializzazione della classe {@link Commento} orientata al tracciamento spaziotemporale.
 * Questa classe permette di associare un feedback testuale a un preciso marcatore temporale
 * (un istante singolo o un intervallo espresso in secondi) all'interno di una risorsa multimediale.
 * Estendendo la classe madre, eredita l'intera logica di tracciamento dell'autore e dell'ufficialità,
 * aggiungendo controlli di integrità semantica legati alla riproduzione temporale del media.
 */
public class NotaTemporale extends Commento {

    // Variabili in virgola mobile per mappare con precisione millesimale i secondi esatti
    private float istanteInizio;
    private float istanteFine;

    /**
     * Costruttore completo della classe NotaTemporale.
     * Inizializza l'entità invocando il costruttore della superclasse per preservare la coerenza
     * del modello dei dati e mappa i limiti dell'intervallo temporale associato.
     *
     * @param testo Il corpo testuale del feedback o della nota.
     * @param dataInserimento Il marcatore temporale di immissione della nota nel sistema.
     * @param isUfficiale Stato di ufficialità del feedback all'interno del catalogo.
     * @param autoreEmail L'indirizzo email dell'utente che ha generato la nota.
     * @param istanteInizio Il secondo esatto che delimita l'inizio dell'intervallo di riferimento.
     * @param istanteFine Il secondo esatto che delimita la conclusione dell'intervallo di riferimento.
     */
    public NotaTemporale(String testo, Date dataInserimento, boolean isUfficiale, String autoreEmail, float istanteInizio, float istanteFine) {
        // Trasferimento dei metadati fondamentali al costruttore della classe base Commento
        super(testo, dataInserimento, isUfficiale, autoreEmail);
        this.istanteInizio = istanteInizio;
        this.istanteFine = istanteFine;
    }

    /**
     * Restituisce una rappresentazione testuale formattata dell'intervallo temporale,
     * utile per l'esposizione coerente all'interno dei componenti dell'interfaccia grafica.
     *
     * @return Una stringa racchiusa tra parentesi quadre nel formato [inizio s - fine s].
     */
    public String getIntervallo() {
        return "[" + istanteInizio + "s - " + istanteFine + "s]";
    }

    /**
     * Valuta la congruenza logica dei marcatori temporali rispetto alla durata complessiva del file.
     * Questo metodo incarna i principi della programmazione difensiva, assicurandosi che i limiti
     * inseriti non violino le barriere fisiche della traccia multimediale o la coerenza cronologica.
     *
     * @param durataTotaleMedia La lunghezza complessiva in secondi del file multimediale di riferimento.
     * @return true se l'intervallo è semanticamente valido e compatibile, false in caso contrario.
     */
    public boolean validaTimestamp(float durataTotaleMedia) {
        if (istanteInizio >= 0 && istanteFine <= durataTotaleMedia && istanteInizio <= istanteFine) {
            return true;
        }
        System.out.println("Errore: Timestamp non valido rispetto alla durata del media.");
        return false;
    }

    // --- METODI DI ACCESSO (GETTER E SETTER) ---

    /**
     * Restituisce l'istante temporale di inizio dell'intervallo.
     * @return Il valore float corrispondente al secondo di avvio.
     */
    public float getIstanteInizio() {
        return istanteInizio;
    }

    /**
     * Imposta o aggiorna l'istante temporale di inizio.
     * @param istanteInizio Il nuovo valore in secondi da assegnare.
     */
    public void setIstanteInizio(float istanteInizio) {
        this.istanteInizio = istanteInizio;
    }

    /**
     * Restituisce l'istante temporale di conclusione dell'intervallo.
     * @return Il valore float corrispondente al secondo di fine.
     */
    public float getIstanteFine() {
        return istanteFine;
    }

    /**
     * Imposta o aggiorna l'istante temporale di conclusione.
     * @param istanteFine Il nuovo valore in secondi da assegnare.
     */
    public void setIstanteFine(float istanteFine) {
        this.istanteFine = istanteFine;
    }
}