```markdown
# MusicManager — Note di Esecuzione Rapida

Questo archivio contiene il codice sorgente e gli artefatti di configurazione di **MusicManager**, un sistema software orientato agli oggetti sviluppato in Java per l'esame di Ingegneria del Software.

## Struttura della Root
* `src/`: Codice sorgente dell'applicazione (Main e Test unitari).
* `src/javadoc/`: Manuale ipertestuale delle classi (aprire `index.html` per navigare).
* `*.json`: Database flat-file già popolati con un catalogo dati di prova.
* `Documentazione.pdf`: Relazione tecnica completa di specifiche UML, diagrammi e matrici di test.
* `pom.xml`: Descrittore delle dipendenze di progetto.
* `media/`: File multimediali di test (mp3, mp4, pdf)

## Prerequisiti
* Java JDK 17 o superiore.
* Un IDE moderno con supporto Maven integrato (es. IntelliJ IDEA, Eclipse).

## Istruzioni per l'Avvio tramite IDE
Per garantire che l'applicazione mantenga la corretta Working Directory per la lettura e scrittura dei file JSON, seguire questi passaggi:

1. **Importazione:** Aprire l'IDE (es. IntelliJ IDEA) e fare clic su **Open** (o *Import*). Selezionare la cartella radice `MusicManager_Consegna/`. L'IDE configurerà autonomamente l'ambiente a partire dal file `pom.xml`.
2. **Download Dipendenze:** Attendere che l'IDE completi l'indicizzazione e il download automatico delle librerie (Google Gson, JavaFX Modules, JUnit 5).
3. **Esecuzione Test:** Per verificare la stabilità, fare clic con il tasto destro sulla cartella `src/test/java` e selezionare **Run 'All Tests'** (Suite JUnit 5).
4. **Bootstrap Applicazione:** Navigare nel package `com.musicmanager.main`, aprire la classe **`Main.java`** e avviarla facendo clic sulla freccia verde di **Run** accanto al metodo `main(String[] args)`.

*Nota: La classe Main funge da Launcher Wrapper statico per consentire l'avvio corretto dei moduli grafici senza conflitti legati a Project Jigsaw.*

## Avvio Rapido da Riga di Comando (CLI)
Se preferisci non utilizzare un IDE, puoi compilare ed eseguire l'applicazione direttamente dal terminale utilizzando Maven:

1. **Compilazione:**
   ```bash
   mvn clean compile

```

2. **Esecuzione dell'applicazione:**
```bash
mvn javafx:run

```


3. **Esecuzione dei test:**
```bash
mvn test

```



*Nota: Assicurati di essere nella cartella radice del progetto (`MusicManager_Consegna/`) prima di eseguire i comandi, in modo che l'applicazione possa individuare correttamente i file JSON di configurazione.*

## Credenziali di Test per la Valutazione

I database JSON inclusi contengono già profili di prova abilitati per testare i diversi livelli di privilegio (RBAC):

1. **Account Amministratore (Governance globale):**
* **Email:** `admin@musicmanager.it`
* **Password:** `admin123`


2. **Account Utente Autore/Interprete (Fruizione, Social e Contenuti Ufficiali):**
* **Email:** `angelo@musica.it`
* **Password:** `angelo`



```

```