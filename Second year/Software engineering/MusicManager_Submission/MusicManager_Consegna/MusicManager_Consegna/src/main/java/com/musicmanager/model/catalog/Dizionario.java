package com.musicmanager.model.catalog;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe deputata alla gestione dei vocabolari controllati e delle tassonomie ammesse nel sistema.
 * Viene utilizzata principalmente per convalidare metadati critici, come i generi musicali,
 * impedendo l'inserimento di stringhe arbitrarie nel catalogo.
 * La classe include una logica difensiva per prevenire mancate inizializzazioni delle collezioni
 * causate dai meccanismi di riflessione dei framework di serializzazione (come Google Gson).
 */
public class Dizionario {

    private String tipoDizionario;
    private List<String> voci;

    /**
     * Costruttore della classe Dizionario.
     * Alloca la struttura dati in memoria e ne definisce l'ambito di applicazione.
     * * @param tipoDizionario Stringa identificativa che definisce la categoria del dizionario (es. "Generi").
     */
    public Dizionario(String tipoDizionario) {
        this.tipoDizionario = tipoDizionario;
        this.voci = new ArrayList<>();
    }

    /**
     * Verifica se un determinato termine è presente all'interno del dizionario.
     * Il metodo interroga la collezione passando dal getter sicuro, garantendo così
     * che la lista sia stata correttamente istanziata prima del controllo.
     * * @param termine La stringa da cercare nel vocabolario.
     * @return true se il termine esiste ed è valido, false in caso contrario.
     */
    public boolean verificaEsistenza(String termine) {
        return getVoci().contains(termine);
    }

    /**
     * Inserisce un nuovo termine normalizzato all'interno del dizionario.
     * Il metodo implementa una verifica preliminare per escludere l'inserimento
     * di duplicati e per ripristinare la lista qualora fosse stata dereferenziata.
     * * @param nuovaVoce Il nuovo termine o genere musicale da accreditare nel sistema.
     */
    public void aggiornaDizionario(String nuovaVoce) {
        // Controllo difensivo di sicurezza per intercettare collezioni nulle a runtime
        if (voci == null) {
            voci = new ArrayList<>();
        }

        // Impedisce la ridondanza dei dati inserendo la voce solo se non è già censita
        if (!voci.contains(nuovaVoce)) {
            voci.add(nuovaVoce);
            System.out.println("Dizionario [" + tipoDizionario + "]: Aggiunta voce -> " + nuovaVoce);
        }
    }

    // --- METODI DI ACCESSO (GETTER E SETTER SICURI) ---

    /**
     * Restituisce la categoria o tipologia del dizionario corrente.
     * @return La stringa identificativa del tipo di dizionario.
     */
    public String getTipoDizionario() {
        return tipoDizionario;
    }

    /**
     * Configura la stringa identificativa della tipologia del dizionario.
     * @param tipoDizionario Il nome della categoria da associare.
     */
    public void setTipoDizionario(String tipoDizionario) {
        this.tipoDizionario = tipoDizionario;
    }

    /**
     * Fornisce l'elenco dei termini censiti nel dizionario.
     * Questo getter adotta un approccio lazy e difensivo: se il motore di deserializzazione
     * Gson ha ripristinato l'oggetto bypassando il costruttore standard, il metodo rileva
     * l'eventuale valore nullo della lista e provvede a istanziarla immediatamente in memoria Heap,
     * scongiurando il rischio di eccezioni di tipo NullPointerException nei moduli chiamanti.
     * * @return La lista di stringhe che compongono il dizionario.
     */
    public List<String> getVoci() {
        if (voci == null) {
            voci = new ArrayList<>();
        }
        return voci;
    }

    /**
     * Assegna in blocco una nuova collezione di voci al dizionario corrente.
     * @param voci La lista di stringhe da impostare.
     */
    public void setVoci(List<String> voci) {
        this.voci = voci;
    }
}