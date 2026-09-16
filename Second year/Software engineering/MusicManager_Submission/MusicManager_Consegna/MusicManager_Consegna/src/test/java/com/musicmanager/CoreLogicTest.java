package com.musicmanager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Date;
import com.musicmanager.model.feedback.NotaTemporale;
import com.musicmanager.model.catalog.SegmentoEsecuzione;

/**
 * Suite di test unitari per la validazione della logica di business.
 * Segue il pattern AAA (Arrange, Act, Assert).
 */
public class CoreLogicTest {

    @Test
    public void testValidazioneIntervalloTemporale() {
        // ARRANGE: Creiamo una nota con tempo fine precedente all'inizio (errore)
        NotaTemporale notaInvalida = new NotaTemporale("Test", new Date(), false, "test@user.it", 120.0f, 60.0f);

        // ACT: Verifichiamo la validità rispetto a una durata media di 300s
        boolean isValid = notaInvalida.validaTimestamp(300.0f);

        // ASSERT: Il sistema deve rilevare l'incoerenza cronologica
        assertFalse(isValid, "La nota non dovrebbe essere valida se la fine precede l'inizio.");
    }

    @Test
    public void testMappaturaSegmentoConcerto() {
        // ARRANGE
        SegmentoEsecuzione segmento = new SegmentoEsecuzione(
                "Solo Chitarra", 10.0f, 50.0f, "John Doe", "Chitarra", "20/05/2026", "Verona", "Ottima"
        );

        // ASSERT: Verifica che i metadati siano stati mappati correttamente
        assertEquals("Solo Chitarra", segmento.getTitoloBrano());
        assertEquals(40.0f, segmento.getFine() - segmento.getInizio(), "La durata del segmento deve essere di 40 secondi.");
    }

    @Test
    public void testEmailAutoreCommento() {
        // ARRANGE
        NotaTemporale nt = new NotaTemporale("Nota", new Date(), true, "autore@musica.it", 0f, 10f);

        // ASSERT
        assertEquals("autore@musica.it", nt.getAutoreEmail(), "L'email dell'autore deve essere persistita correttamente.");
        assertTrue(nt.isUfficiale(), "Il flag di ufficialità deve essere vero.");
    }
}