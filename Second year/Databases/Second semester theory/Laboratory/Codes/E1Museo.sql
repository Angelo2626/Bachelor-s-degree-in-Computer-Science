--Esercizio 1
--Scrivere il codice PostgreSQL che generi tutte le tabelle. 
--Per gli attributi di cui non è stato specificato il tipo, scegliere quello opportuno.
--Specificare tutti i vincoli possibili, sia intra- sia inter-relazionali.

CREATE TABLE Museo(
	nomeMuseo VARCHAR(30) DEFAULT 'Museo Veronese',
	cittaMuseo VARCHAR(20) DEFAULT 'Verona',
	indirizzoMuseo VARCHAR(30) NOT NULL,
	numeroTelefonoMuseo VARCHAR(10),
	giornoChiusuraMuseo VARCHAR(9) NOT NULL CHECK (giornoChiusuraMuseo IN ('Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato', 'Domenica')), 
	prezzoMuseo NUMERIC(5,2) DEFAUlT 10,

	PRIMARY KEY (nomeMuseo, cittaMuseo)
);

CREATE TABLE Opera(
	nomeOpera VARCHAR(30),
	cognomeAutore VARCHAR(20),
	nomeAutore VARCHAR(20) NOT NULL,
	nome_museo VARCHAR(30) DEFAULT 'Museo di Verona',
	citta_museo VARCHAR(20) DEFAULT 'Verona',
	epoca VARCHAR(20) NOT NULL,
	anno INTEGER NOT NULL,

	PRIMARY KEY (nomeOpera, cognomeAutore, nomeAutore),
	FOREIGN KEY (nome_museo, citta_museo) REFERENCES Museo(nomeMuseo, cittaMuseo)
);

CREATE TABLE Mostra(
	titolo VARCHAR(30),
	inizio DATE,
	fine DATE,
	nome_museo VARCHAR(30) DEFAULT 'Museo di Verona',
	citta_museo VARCHAR(20) DEFAULT 'Verona',
	prezzo NUMERIC(5, 2) NOT NULL,

	PRIMARY KEY (titolo, inizio),
	FOREIGN KEY (nome_museo, citta_museo) REFERENCES Museo(nomeMuseo, cittaMuseo)
);

CREATE TABLE Orario(
	progressivo INTEGER PRIMARY KEY,
	nome_museo VARCHAR(30) DEFAULT 'Museo di Verona',
	citta_museo VARCHAR(20) DEFAULT 'Verona',
	giorno VARCHAR(9) NOT NULL CHECK (giorno IN ('Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato', 'Domenica')), 
	orarioApertura TIME WITH TIME ZONE DEFAULT '09:00 CET',
	orarioChiusura TIME WITH TIME ZONE DEFAULT '19:00 CET',

	FOREIGN KEY (nome_museo, citta_museo) REFERENCES Museo(nomeMuseo, cittaMuseo)
);

--Esercizio 2
--Inserire nell'entità Museo le sguenti tuple:
--(Arena, Verona, piazza Bra, 045 8003204, martedì, 20),
--(CastelVecchio, Verona, Corso Castelvecchio, 045 594734, lunedì, 15);

INSERT INTO Museo(nomeMuseo, cittaMuseo, indirizzoMuseo, numeroTelefonoMuseo, giornoChiusuraMuseo, prezzoMuseo)
VALUES
	('Arena', 'Verona', 'piazza Bra', '0458003204', 'Martedì', 20),
	('CastelVecchio', 'Verona', 'Corso Castelvecchio', '045594734', 'Lunedì', 15);

--Esercizio 3
--Popolare le tabelle Opera e Mostra con almeno altre tre tuple ciascuna.

INSERT INTO Opera(nomeOpera, cognomeAutore, nomeAutore, nome_museo, citta_museo, epoca, anno)
VALUES
	('Sacra Famimglia', 'Mantegna', 'Andrea', 'CastelVecchio', 'Verona', '01-01-1495', 1505),
	('Ritratto di fanciullo', 'Caroto', 'Giovan Francesco', 'CastelVecchio', 'Verona', '01-01-1515', 1520),
	('Madonna della Quaglia', 'Pisanello', 'Antonio', 'CastelVecchio', 'Verona', '01-01-1420', 1420);

INSERT INTO Museo(nomeMuseo, cittaMuseo, indirizzoMuseo, numeroTelefonoMuseo, giornoChiusuraMuseo, prezzoMuseo)
VALUES
	('Museo Archelogico', 'Verona', 'Rigaste Redentore', '0458000360', 'Lunedì', 4.50),
	('Casa di Giulietta', 'Verona', 'Via Cappello', '0458034303', 'Lunedì', 6.00),
	('Galleria d Arte Moderna', 'Verona', 'Cortile Mercato Vecchio', '0458001903', 'Lunedì', 4.00)

SELECT * FROM Opera;
SELECT * FROM Museo;

--Esercizio 5
--Nell'entita museo, aggiungere l'attributo sitoInternet e inserire gli opportuni valori.

ALTER TABLE Museo
ADD COLUMN sitoInternet VARCHAR(100);

UPDATE Museo
SET sitoInternet = 'https://www.arena.it/'
WHERE nomeMuseo = 'Arena';

UPDATE Museo
SET sitoInternet = 'https://museodicastelvecchio.comune.verona.it/nqcontent.cfm?a_id=42545'
WHERE nomeMuseo = 'CastelVecchio';

UPDATE Museo
SET sitoInternet = 'https://casadigiulietta.comune.verona.it/nqcontent.cfm?a_id=42703'
WHERE nomeMuseo = 'Casa di Giulietta';

UPDATE Museo
SET sitoInternet = 'https://gam.comune.verona.it/nqcontent.cfm?a_id=42701'
WHERE nomeMuseo = 'Galleria d Arte Moderna';

SELECT * FROM Museo;

UPDATE Museo
SET nomeMuseo = 'Museo Archeologico'
WHERE nomeMuseo = 'Museo Archelogico';

SELECT * FROM Museo;

UPDATE Museo
SET sitoInternet = 'https://manverona.cultura.gov.it/'
WHERE nomeMuseo = 'Museo Archeologico';

SELECT * FROM Museo;

--Esercizio 6
--Nell'entità Mostra modificare l'attributo prezzo in prezzoIntero ed aggiungere l'attributo prezzoRidotto con valore di default 5. 
--Aggiungere il vincolo (di tabella o di attributo?) che garantisca che Mostra.prezzoRidotto sia minore di Mostra.prezzoIntero.

ALTER TABLE Mostra
RENAME COLUMN prezzo TO prezzoIntero;

ALTER TABLE Mostra
ADD COLUMN prezzoRidotto NUMERIC(5, 2) DEFAULT 5,
ADD CONSTRAINT chk_prezzi_mostra CHECK (prezzoRidotto < prezzoIntero); --Questo fa in modo che si crei un vincolo in modo che se dobbiamo eliminare eusto vincolo basta eliminare chk_prezzi_mostra senza eliminare tutta la colonna

--Esercizio 7
--Nell'entità Museo aggiornare il prezzo aggiungendo 1 Euro alle tuple esistenti.

UPDATE Museo
SET prezzoMuseo = prezzoMuseo + 1;

--Esercizio 8
--Nell'entità Mostra aggiornare il prezzoRidotto aumentandolo di 1 Euro per quelle mostre che hanno prezzoIntero inferiore a 15 Euro

UPDATE Mostra
SET prezzoRidotto = prezzoRidotto + 1
WHERE prezzoIntero = 15;

SELECT * FROM Mostra;

INSERT INTO Mostra (titolo, inizio, fine, nome_museo, citta_museo, prezzoIntero)
VALUES 
    ('Rinascimento Veronese', '2026-05-01', '2026-08-31', 'CastelVecchio', 'Verona', 15.00),
    ('I Gladiatori', '2026-09-10', '2026-11-20', 'Arena', 'Verona', 10.00),
    ('Pittura del Trecento', '2026-12-01', '2027-02-28', 'CastelVecchio', 'Verona', 12.50);

INSERT INTO Orario (progressivo, nome_museo, citta_museo, giorno, orarioApertura, orarioChiusura)
VALUES 
    (1, 'CastelVecchio', 'Verona', 'Martedì', DEFAULT, DEFAULT),
    (2, 'Arena', 'Verona', 'Sabato', '08:30 CET', '20:00 CET'),
    (3, 'Museo Archeologico', 'Verona', 'Domenica', DEFAULT, DEFAULT);

--Esercizio 9
--Si assume che in ciascuna tabella della base di dati ci siano almeno 3 righe inserite.
--Implementare le chiavi esportate per ciascuna delle 4 politiche di reazione presentate
--nella pagina precedente (usare il comando DROP CONTRAINTS e ADD CONSTRAINTS
--per effettuare il cambio di politica). Provare ad eseguire una cancellazione ed un
--aggiornamento dei valori riferiti (e dei valori non riferiti) per verificare il diverso
--comportamento del DBMS.

