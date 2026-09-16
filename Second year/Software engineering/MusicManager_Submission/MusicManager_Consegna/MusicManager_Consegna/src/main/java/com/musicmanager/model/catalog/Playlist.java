package com.musicmanager.model.catalog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Rappresenta una raccolta personalizzata di brani musicali organizzata dall'utente.
 * Questa classe implementa l'interfaccia nativa {@link Iterable}, consentendo
 * l'attraversamento sequenziale dei propri elementi tramite il costrutto standard for-each.
 * L'architettura si appoggia al Design Pattern Iterator per disaccoppiare la struttura
 * interna di memorizzazione dei dati dalle logiche di navigazione e scorrimento dei record.
 */
public class Playlist implements Iterable<Brano> {

    private String nomePlaylist;
    private List<Brano> collezioneBrani;

    /**
     * Costruttore della classe Playlist.
     * Alloca la struttura dati flessibile nella memoria Heap per ospitare i brani
     * che verranno successivamente aggregati all'interno della raccolta.
     * * @param nomePlaylist Il nome identificativo o titolo assegnato alla playlist.
     */
    public Playlist(String nomePlaylist) {
        this.nomePlaylist = nomePlaylist;
        this.collezioneBrani = new ArrayList<>();
    }

    /**
     * Inserisce un riferimento ad un oggetto Brano all'interno della collezione corrente.
     * * @param brano L'istanza della traccia musicale da aggiungere alla raccolta.
     */
    public void aggiungiBrano(Brano brano) {
        this.collezioneBrani.add(brano);
        System.out.println("Playlist [" + nomePlaylist + "]: Aggiunto il brano '" + brano.getTitolo() + "'");
    }

    /**
     * Restituisce la cardinalità attuale della playlist, ovvero il numero di elementi
     * correntemente registrati in memoria.
     * * @return Il valore intero corrispondente al numero di brani presenti.
     */
    public int getNumeroBrani() {
        return collezioneBrani.size();
    }

    /**
     * Metodo strutturale previsto dal contratto d'interfaccia di {@link Iterable}.
     * Alloca e restituisce un'istanza del cursore personalizzato, configurata e pronta
     * a scorrere in modo sequenziale i brani di questa specifica playlist.
     * * @return Un'istanza di Iterator pronta per l'attraversamento dello stato interno.
     */
    @Override
    public Iterator<Brano> iterator() {
        return new PlaylistIterator();
    }

    /**
     * Inner Class (Classe Annidata) privata che implementa l'interfaccia {@link Iterator}.
     * Questa scelta progettuale garantisce il massimo livello di incapsulamento: la classe interna
     * ha accesso diretto e privilegiato ai membri privati della classe contenitore (inclusa la lista
     * 'collezioneBrani'), permettendo di scorrere i dati senza dover esporre pubblicamente
     * la struttura di memorizzazione sottostante.
     */
    private class PlaylistIterator implements Iterator<Brano> {

        // Cursore numerico che mantiene traccia dell'indice corrente durante il ciclo di attraversamento
        private int cursor = 0;

        /**
         * Verifica la presenza di ulteriori elementi da scorrere all'interno della collezione.
         * Il controllo confronta la posizione attuale dell'indice con la dimensione reale della lista in RAM.
         * * @return true se il cursore non ha ancora raggiunto la fine della lista, false in caso contrario.
         */
        @Override
        public boolean hasNext() {
            return cursor < collezioneBrani.size();
        }

        /**
         * Restituisce l'elemento successivo nella sequenza di iterazione e avanza
         * contestualmente la posizione del cursore interno.
         * Applica le regole della programmazione difensiva intercettando preventivamente
         * le condizioni di fine corsa per evitare letture inconsistenti.
         * * @return L'istanza del Brano individuata alla posizione corrente.
         * @throws NoSuchElementException Se l'iterazione viene invocata quando non vi sono più elementi disponibili.
         */
        @Override
        public Brano next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterazione terminata: nessun altro brano in memoria.");
            }
            // Recupera il brano all'indice attuale e incrementa il puntatore in modo atomico tramite post-incremento
            return collezioneBrani.get(cursor++);
        }
    }
}