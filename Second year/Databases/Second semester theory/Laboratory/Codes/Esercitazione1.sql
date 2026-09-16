CREATE TABLE museo(
	nome VARCHAR(30) NOT NULL DEFAULT 'MuseoVeronese',
	citta VARCHAR(20) NOT NULL DEFAULT 'Verona',
	indirizzo VARCHAR(30) NOT NULL,
	numeroTelefono VARCHAR(10) NOT NULL CHECK(numeroTelefono ~ '^[0-9]+$'),
	giornoChiusura VARCHAR(10) NOT NULL CHECK(giornoChiusura IN('Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato', 'Domenica')),
	prezzo DECIMAL(5,2) NOT NULL DEFAULT 10,
	PRIMARY KEY(nome, citta)
);

CREATE TABLE Opera(
	nomeO VARCHAR(30),
	cognomeAutore VARCHAR(20) NOT NULL,
	nomeAutore VARCHAR(20) NOT NULL,
	museoO VARCHAR(30) NOT NULL,
	cittaO VARCHAR(20) NOT NULL,
	epoca VARCHAR(20), 
	anno INTEGER,

	PRIMARY KEY(nomeO, cognomeAutore, nomeAutore),
	FOREIGN KEY(museoO, cittaO) REFERENCES Museo(nome, citta)
);

CREATE TABLE Mostra(
	titolo VARCHAR(30) NOT NULL,
	inizio DATE NOT NULL,
	museoM VARCHAR(30) NOT NULL,
	cittaM VARCHAR(20) NOT NULL,
	prezzo DECIMAL(5,2),

	PRIMARY KEY(titolo, inizio),
	FOREIGN KEY(museoM, cittaM) REFERENCES Museo(nome, citta)
);

CREATE TABLE Orario(
	progressivo SERIAL PRIMARY KEY,
	Omuseo VARCHAR(30) NOT NULL,
	Ocitta VARCHAR(20) NOT NULL,
	orarioApertura TIMESTAMPTZ NOT NULL,
	orarioChiusura TIMESTAMPTZ NOT NULL,
	CHECK (orarioApertura < orarioChiusura),

	FOREIGN KEY(Omuseo, Ocitta) REFERENCES Museo(nome, citta)
);

ALTER TABLE Museo ALTER COLUMN numeroTelefono TYPE VARCHAR(20);

INSERT INTO Museo(nome, citta, indirizzo, numeroTelefono, giornoChiusura, prezzo)
VALUES
	('Arena', 'Verona', 'piazza Bra','0458003204', 'Martedì', 20), 
	('CastelVecchio', 'Verona', 'Corso Castelvecchio','045594734', 'Lunedì', 15);

INSERT INTO Opera (nomeO, cognomeAutore, nomeAutore, museoO, cittaO, epoca, anno)
VALUES
    ('La Gioconda', 'da Vinci', 'Leonardo', 'Arena', 'Verona', 'Rinascimento', 1503),
    ('Notte stellata', 'van Gogh', 'Vincent', 'CastelVecchio', 'Verona', 'Post-impressionismo', 1889),
    ('Il bacio', 'Hayez', 'Francesco', 'Arena', 'Verona', 'Romanticismo', 1859);

INSERT INTO Mostra (titolo, inizio, museoM, cittaM, prezzo)
VALUES
    ('Impressionisti a Verona', '2025-06-01', 'Arena', 'Verona', 12.00),
    ('Arte Moderna', '2025-07-15', 'CastelVecchio', 'Verona', 10.00),
    ('Scultura Italiana', '2025-09-10', 'Arena', 'Verona', 8.50);	

ALTER TABLE Museo ADD COLUMN sitoInternet VARCHAR(50) NOT NULL DEFAULT 'https://www.comune.verona.it/';


UPDATE Museo
SET sitoInternet = 'https://www.arena.it/'
WHERE nome = 'Arena';


UPDATE Museo
SET sitoInternet = 'https://museodicastelvecchio.comune.verona.it/'
WHERE nome = 'CastelVecchio';

SELECT * FROM Museo;

ALTER TABLE Mostra RENAME COLUMN prezzo TO prezzoIntero;

ALTER TABLE Mostra ADD COLUMN prezzoRidotto DECIMAL(5,2) NOT NULL DEFAULT 5 CHECK (prezzoRidotto < prezzoIntero);

UPDATE Museo SET prezzo = prezzo + 1;

SELECT * FROM Mostra;

UPDATE Mostra 
SET prezzoRidotto = prezzoRidotto + 1
WHERE prezzoIntero < 15;

