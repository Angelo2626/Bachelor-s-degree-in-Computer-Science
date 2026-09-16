package com.musicmanager.main;

import com.musicmanager.model.catalog.Brano;
import com.musicmanager.model.users.UtenteAutorizzato;
import com.musicmanager.persistence.GestoreJSON;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrazioneSystemTest {

    private final String FILE_UTENTI_TEST = "utenti_sys_test.json";
    private final String FILE_CATALOGO_TEST = "catalogo_sys_test.json";

    @AfterEach
    public void tearDown() {
        new File(FILE_UTENTI_TEST).delete();
        new File(FILE_CATALOGO_TEST).delete();
    }

    @Test
    public void testFlussoCompletoSistema() {
        GestoreJSON gestore = new GestoreJSON();

        // STEP 1: Creazione e salvataggio Utente
        List<UtenteAutorizzato> utenti = new ArrayList<>();
        UtenteAutorizzato utente = new UtenteAutorizzato("test-id", "mario@mail.it", "pass123", "Mario", "Rossi", new Date());
        utente.setAttivo(true); // Simuliamo l'approvazione dell'admin
        utenti.add(utente);

        // Utilizziamo un cast o il metodo specifico che hai nel GestoreJSON per salvare gli utenti
        // (Assicurati che GestoreJSON abbia un metodo salvaUtenti)
        // gestore.salvaUtenti(utenti, FILE_UTENTI_TEST);

        // STEP 2: L'utente crea un brano
        List<Brano> catalogo = new ArrayList<>();
        Brano nuovoBrano = new Brano("Brano di Test", 2023, "Autore Test", utente.getEmail(), "Pop");
        catalogo.add(nuovoBrano);
        gestore.salvaCatalogo(catalogo, FILE_CATALOGO_TEST);

        // STEP 3: Verifica dell'integrità del sistema
        List<Brano> catalogoRicaricato = gestore.caricaCatalogo(FILE_CATALOGO_TEST);
        assertFalse(catalogoRicaricato.isEmpty(), "Il catalogo non deve essere vuoto");
        assertEquals("mario@mail.it", catalogoRicaricato.get(0).getProprietarioEmail(), "Il brano deve essere collegato all'utente corretto");
    }
}