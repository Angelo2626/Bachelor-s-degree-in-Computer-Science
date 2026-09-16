package com.musicmanager.persistence;

import com.musicmanager.model.catalog.Brano;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GestoreJSONTest {

    private GestoreJSON gestore;
    private final String FILE_TEST = "catalogo_test.json";

    @BeforeEach
    public void setUp() {
        gestore = new GestoreJSON();
    }

    @AfterEach
    public void tearDown() {
        // Pulizia: elimina il file di test dopo ogni esecuzione
        File file = new File(FILE_TEST);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSalvaECaricaCatalogo() {
        // 1. Prepariamo i dati di test
        List<Brano> catalogoOriginale = new ArrayList<>();
        Brano b1 = new Brano("Bohemian Rhapsody", 1975, "Queen", "admin@musicmanager.it", "Rock");
        catalogoOriginale.add(b1);

        // 2. Salviamo su disco
        gestore.salvaCatalogo(catalogoOriginale, FILE_TEST);

        // 3. Ricarichiamo da disco
        List<Brano> catalogoCaricato = gestore.caricaCatalogo(FILE_TEST);

        // 4. Verifichiamo che i dati coincidano
        assertNotNull(catalogoCaricato, "Il catalogo caricato non deve essere nullo");
        assertEquals(1, catalogoCaricato.size(), "Il catalogo deve contenere esattamente un brano");
        assertEquals("Bohemian Rhapsody", catalogoCaricato.get(0).getTitolo(), "Il titolo deve coincidere");
        assertEquals("Rock", catalogoCaricato.get(0).getGenere(), "Il genere deve coincidere");
    }
}