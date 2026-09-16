package com.musicmanager.exceptions;

/**
 * Eccezione personalizzata di tipo Checked utilizzata per la gestione difensiva delle anomalie.
 * Questa classe viene sollevata per segnalare un'anomalia semantica all'interno del sistema:
 * ovvero quando una query di ricerca applicata al catalogo musicale non produce alcun risultato.
 * Estendendo la classe madre 'Exception', obbliga i controller chiamanti a gestire esplicitamente
 * il caso anomalo tramite blocchi try-catch, garantendo la stabilità dell'interfaccia grafica.
 */
public class BranoNonTrovatoException extends Exception {

    // Mantiene traccia in memoria della specifica stringa di ricerca che ha scatenato l'eccezione
    private String queryErrata;

    /**
     * Costruttore dell'eccezione personalizzata.
     *
     * @param message Il messaggio di errore descrittivo destinato al log o all'interfaccia utente.
     * @param queryErrata La chiave di ricerca originale immessa dall'utente che ha causato il fallimento.
     */
    public BranoNonTrovatoException(String message, String queryErrata) {
        // Invoca il costruttore della superclasse 'Exception' per inizializzare il messaggio
        // di errore e allocare correttamente l'intero Stack Trace nella memoria della JVM.
        super(message);
        this.queryErrata = queryErrata;
    }

    /**
     * Restituisce la query di ricerca che ha causato il sollevamento dell'eccezione.
     * Questo metodo si rivela fondamentale per l'architettura MVC, in quanto permette
     * ai controller visivi di recuperare il dato errato e fornire un feedback preciso
     * e costruttivo all'utente finale tramite l'interfaccia grafica.
     *
     * @return La stringa immessa nel campo di ricerca che non ha prodotto corrispondenze.
     */
    public String getQueryErrata() {
        return queryErrata;
    }
}