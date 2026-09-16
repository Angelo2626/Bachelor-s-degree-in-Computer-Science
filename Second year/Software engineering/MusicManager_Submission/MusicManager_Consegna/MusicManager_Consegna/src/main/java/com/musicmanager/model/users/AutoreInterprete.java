package com.musicmanager.model.users;

import java.util.Date;
import java.util.List;

/**
 * Specializzazione polimorfica della classe {@link UtenteAutorizzato}.
 * Questa entità modella gli utenti del sistema che possiedono diritti d'autore,
 * di esecuzione o di interpretazione sui brani presenti nel catalogo.
 * Introduce attributi e comportamenti specifici richiesti dai requisiti di business,
 * come l'estensione dei privilegi di inserimento per i feedback (commenti ufficiali)
 * e la profilazione artistica tramite biografia, ruoli e strumentazione associata.
 */
public class AutoreInterprete extends UtenteAutorizzato {

    private String biografia;

    /**
     * Costruttore completo della classe AutoreInterprete.
     * Inizializza l'istanza trasferendo i dati anagrafici e la traccia temporale di registrazione
     * al costruttore della superclasse, provvedendo poi ad allocare la biografia specifica dell'artista.
     *
     * @param id L'identificativo univoco dell'autore/interprete.
     * @param email L'indirizzo email utilizzato per l'autenticazione.
     * @param password La chiave d'accesso protetta (stringa hash) dell'account.
     * @param nome Il nome proprio dell'utente.
     * @param cognome Il cognome dell'utente.
     * @param dataRegistrazione Il marcatore temporale che attesta l'iscrizione al sistema.
     * @param biografia Il testo descrittivo del percorso artistico e professionale dell'utente.
     */
    public AutoreInterprete(String id, String email, String password, String nome, String cognome, Date dataRegistrazione, String biografia) {
        // Invocazione esplicita del costruttore di UtenteAutorizzato per preservare la catena di ereditarietà
        super(id, email, password, nome, cognome, dataRegistrazione);
        this.biografia = biografia;
    }

    /**
     * Consente all'utente di specificare o aggiornare il proprio ruolo artistico principale
     * (es. Compositore, Arrangiatore, Esecutore) all'interno della piattaforma.
     *
     * @param ruolo La stringa che descrive la qualifica o il ruolo dell'artista.
     */
    public void specificaRuolo(String ruolo) {
        System.out.println("Autore/Interprete [" + getEmail() + "]: Impostato ruolo specifico -> " + ruolo);
    }

    /**
     * Permette di associare al profilo dell'artista l'elenco delle competenze strumentali,
     * mappando una lista di stringhe preventivamente validate o censite.
     *
     * @param strumenti La lista di stringhe che rappresenta gli strumenti musicali suonati.
     */
    public void specificaStrumenti(List<String> strumenti) {
        System.out.println("Autore/Interprete [" + getEmail() + "]: Strumenti suonati associati -> " + strumenti);
    }

    /**
     * Logica di business avanzata che permette a questa specifica categoria di utenti di inserire
     * un feedback autorevole nel catalogo. Il commento generato sarà contrassegnato dal flag
     * di ufficialità, distinguendosi dalle recensioni dei normali utenti autorizzati.
     *
     * @param testo Il contenuto testuale del commento ufficiale da pubblicare.
     */
    public void inserisciCommentoUfficiale(String testo) {
        System.out.println("Autore/Interprete [" + getEmail() + "]: Inserito commento UFFICIALE -> " + testo);
    }

    // --- METODI DI ACCESSO (GETTER E SETTER DI INTERFACCIA) ---

    /**
     * Restituisce la biografia dell'autore.
     * Il metodo adotta la nomenclatura contratta in lingua inglese per garantire
     * la perfetta e automatica specularità con i campi del file di persistenza
     * gestiti dal modulo GestoreJSON, evitando disallineamenti in fase di riflessione.
     *
     * @return La stringa contenente il testo biografico.
     */
    public String getBiography() {
        return biografia;
    }

    /**
     * Aggiorna il testo della biografia dell'autore.
     * Mantiene la firma uniforme richiesta dalle librerie di deserializzazione dell'architettura.
     *
     * @param biografia Il nuovo testo biografico da associare al profilo.
     */
    public void setBiography(String biografia) {
        this.biografia = biografia;
    }
}