package com.musicmanager.resources.view;

import com.musicmanager.model.catalog.Brano;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

public class RicercaAlgoritmoTest {

    private List<Brano> catalogoMock;

    @BeforeEach
    public void setUp() {
        catalogoMock = new ArrayList<>();
        catalogoMock.add(new Brano("Sinfonia n.5", 1808, "Beethoven", "admin", "Classica"));
        catalogoMock.add(new Brano("Back in Black", 1980, "AC/DC", "admin", "Rock"));
        catalogoMock.add(new Brano("Highway to Hell", 1979, "AC/DC", "admin", "Rock"));
    }

    @Test
    public void testRicercaPerAutore() {
        String queryAutore = "ac/dc"; // Simuliamo l'input dell'utente in minuscolo

        // Stessa logica della Dashboard
        List<Brano> filtrati = catalogoMock.stream()
                .filter(b -> b.getAutorePrincipale().toLowerCase().contains(queryAutore))
                .collect(Collectors.toList());

        assertEquals(2, filtrati.size(), "Dovrebbe trovare 2 brani degli AC/DC");
    }

    @Test
    public void testRicercaMultiCriterio_AND_Logico() {
        String queryGenere = "rock";
        String queryTitolo = "black";

        List<Brano> filtrati = catalogoMock.stream()
                .filter(b -> b.getGenere().toLowerCase().contains(queryGenere))
                .filter(b -> b.getTitolo().toLowerCase().contains(queryTitolo))
                .collect(Collectors.toList());

        assertEquals(1, filtrati.size(), "Dovrebbe trovare solo Back in Black");
        assertEquals("Back in Black", filtrati.get(0).getTitolo());
    }
}