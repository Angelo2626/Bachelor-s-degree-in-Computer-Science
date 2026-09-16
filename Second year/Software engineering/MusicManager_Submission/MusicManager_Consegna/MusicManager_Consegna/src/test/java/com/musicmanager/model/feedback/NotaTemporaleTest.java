package com.musicmanager.model.feedback; // Controlla che coincida con il tuo pacchetto reale

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class NotaTemporaleTest {

    private NotaTemporale nota;
    private final float DURATA_MEDIA_TEST = 180.0f; // Un brano di esempio di 3 minuti (180 secondi)

    @BeforeEach
    public void setUp() {
        // Passiamo i 6 argomenti direttamente nel costruttore:
        // 1. Testo, 2. Data, 3. Email, 4. IsUfficiale, 5. Inizio, 6. Fine
        nota = new NotaTemporale(
                "Assolo di chitarra eccezionale",
                new Date(),
                false,
                "studente@univr.it",
                10.0f,  // istanteInizio di partenza per i test
                60.0f   // istanteFine di partenza per i test
        );
    }
    @Test
    public void testValidaTimestamp_Successo() {
        // Scenario Happy Path: inizio=10.0f, fine=60.0f su un brano di 180s.
        // Deve ritornare true.
        assertTrue(nota.validaTimestamp(DURATA_MEDIA_TEST),
                "Il timestamp dovrebbe essere valido per intervalli coerenti.");
    }

    @Test
    public void testValidaTimestamp_ErroreInizioNegativo() {
        // Creiamo una nota con istante d'inizio errato (-5.0f)
        NotaTemporale notaInvalida = new NotaTemporale(
                "Inizio errato", new Date(), false, "studente@univr.it", -5.0f, 60.0f
        );
        // Deve ritornare false.
        assertFalse(notaInvalida.validaTimestamp(DURATA_MEDIA_TEST),
                "La validazione deve fallire se l'istante di inizio è negativo.");
    }

    @Test
    public void testValidaTimestamp_ErroreInizioDopoFine() {
        // Creiamo una nota in cui l'inizio (70.0f) viene dopo la fine (50.0f)
        NotaTemporale notaInvalida = new NotaTemporale(
                "Inversione temporale", new Date(), false, "studente@univr.it", 70.0f, 50.0f
        );
        // Deve ritornare false.
        assertFalse(notaInvalida.validaTimestamp(DURATA_MEDIA_TEST),
                "La validazione deve fallire se l'inizio supera l'istante di fine.");
    }

    @Test
    public void testValidaTimestamp_ErroreFineSuperioreDurata() {
        // Creiamo una nota in cui la fine (200.0f) supera la durata del brano (180.0f)
        NotaTemporale notaInvalida = new NotaTemporale(
                "Oltre la durata", new Date(), false, "studente@univr.it", 10.0f, 200.0f
        );
        // Deve ritornare false.
        assertFalse(notaInvalida.validaTimestamp(DURATA_MEDIA_TEST),
                "La validazione deve fallire se l'istante di fine supera la durata del brano.");
    }
}