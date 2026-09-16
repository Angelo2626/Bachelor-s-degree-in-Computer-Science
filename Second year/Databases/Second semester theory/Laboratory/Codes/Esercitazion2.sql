--Eseguire i seguenti esercizi usando la propria base di dati dove sono state inserite le tabelle relative ai
--musei, opere e mostre.

--Visualizzare tutti i musei della città di Verona con il loro giorno di chiusura.
SELECT giornoChiusura
FROM Museo;

SELECT * FROM Mostra
--Visualizzare per ogni mostra che inizia con la lettera ’R’, una stringa composta dal titolo e dalla città in cui
--si svolge.
SELECT titolo || '-' || cittaM AS MostraDettaglio
FROM Mostra
WHERE titolo LIKE 'R%';

-- 1. Aggiungiamo la colonna mancante alla tabella esistente
ALTER TABLE Mostra ADD COLUMN fine DATE;

-- 2. Inseriamo le date di fine per le mostre che avevi già (impostandole nel futuro rispetto al 2025/2026)
UPDATE Mostra SET fine = '2026-08-30' WHERE titolo = 'Impressionisti a Verona';
UPDATE Mostra SET fine = '2026-09-15' WHERE titolo = 'Arte Moderna';
UPDATE Mostra SET fine = '2026-12-30' WHERE titolo = 'Scultura Italiana';
-- Se hai inserito anche le mostre con la 'R' dell'ultimo messaggio:
UPDATE Mostra SET fine = '2027-01-10' WHERE titolo = 'Rinascimento Veneto';
UPDATE Mostra SET fine = '2027-02-15' WHERE titolo = 'Roma e l''Arena';
UPDATE Mostra SET fine = '2027-03-01' WHERE titolo = 'Realismo e Avanguardia';

--Visualizzare il titolo di ogni mostra ancora in corso e quanti giorni rimane ancora aperta a partire dalla data
--corrente. Usare la costante CURRENT_DATE per avere la data corrente.
SELECT titolo, fine - CURRENT_DATE AS giorni_rimanenti
FROM Mostra
WHERE inizio <= CURRENT_DATE AND fine >= CURRENT_DATE;

SELECT * FROM Orario
ALTER TABLE Orario 
    ALTER COLUMN orarioApertura TYPE TIME USING orarioApertura::time,
    ALTER COLUMN orarioChiusura TYPE TIME USING orarioChiusura::time;

ALTER TABLE Orario ADD COLUMN giornoSettimana VARCHAR(10) CHECK (giornoSettimana IN('Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato', 'Domenica'));
ALTER TABLE Orario ALTER COLUMN giornoSettimana SET NOT NULL;

--Visualizzare per ogni museo l’orario di apertura e chiusura il martedì. Se per un museo il martedì è giorno di
--chiusura, non mostrare nulla.
SELECT Museo.nome, Orario.orarioApertura, Orario.orarioChiusura
FROM Orario
INNER JOIN Museo ON Orario.omuseo = Museo.nome AND Orario.ocitta = Museo.citta
WHERE Orario.giornoSettimana = 'Martedì' AND Museo.giornoChiusura != 'Martedì';

--Assicurarsi che almeno una mostra abbia il prezzo ridotto non valorizzato (NULL) usando eventualmente il
--comando UPDATE per modificare almeno una riga.
--Visualizzare tutte le mostre che hanno prezzo ridotto non valorizzato usando prima l’espressione ERRATA
--’prezzoRidotto = NULL’ e poi l’espressione corretta prezzoRidotto IS NULL.
SELECT * 
FROM Mostra
WHERE prezzoRidotto = NULL;

SELECT * 
FROM Mostra
WHERE prezzoRidotto IS NULL;

SELECT * FROM Mostra;
ALTER TABLE Mostra ALTER COLUMN prezzoRidotto DROP NOT NULL;
UPDATE Mostra
SET prezzoRidotto = NULL
WHERE titolo = 'Rinascimento Veneto';

--Visualizzare tutte le mostre non terminate in ordine di data inizio e, in caso di pari data inizio, data fine.
SELECT * FROM Mostra

SELECT titolo
FROM Mostra
WHERE fine > CURRENT_DATE OR fine IS NULL
ORDER BY inizio ASC, fine ASC;

--Visualizzare il numero totale di giorni di apertura del museo ’Arena’ di ’Verona’
SELECT * FROM Orario;

SELECT COUNT(giornoChiusura)
FROM Museo
WHERE giornoChiusura = 'Lunedì' AND nome = 'Arena';

--Visualizzare le ore medie di apertura del museo ’Arena’ di ’Verona’.
--Suggerimento: convertire orarioapertura e orariochiusura usando ’::time’.
SELECT AVG(orarioApertura) AS media_ore_apertura
FROM Orario
WHERE omuseo = 'Arena' AND ocitta = 'Verona';

--Indicare il numero di autori distinti presenti in tutti i musei
SELECT * FROM Museo;
SELECT * FROM Opera;

SELECT DISTINCT ON (nomeautore) nomeautore, museoo
FROM Opera;