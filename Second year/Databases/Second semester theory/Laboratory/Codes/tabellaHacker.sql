CREATE TABLE Hacker ( -
    Matricola VARCHAR(5) PRIMARY KEY, --VARCHAR(5) vuol dire che è al massimo 5 caratteri, PRIMARY KEY vuol dire che quel valore è una chiave, non può essere lasciata vuota e non ce ne possono essere 2 uguali
    Nome VARCHAR(30) NOT NULL, --NOT NULL vuol dire che quel valore non può essere lasciato vuoto
    Livello INTEGER DEFAULT 1 --DEFAULT 1 vuol dire che se non inseriamo nulla, il sistema inserira 1 di default
);

INSERT INTO Hacker (Matricola, Nome, Livello)
VALUES ('H0001', 'Angelo', 10);

INSERT INTO Hacker (Matricola, Nome)
VALUES ('H0002', 'Cypher');

SELECT * FROM Hacker;

UPDATE Hacker
SET Livello = 0
WHERE Nome = 'Cypher';

SELECT * FROM Hacker;

DELETE FROM Hacker
WHERE Nome = 'Cypher';

SELECT * FROM Hacker;

INSERT INTO Hacker (Matricola, Nome, Livello) VALUES --Cosi posso creare più righe con un solo comando, separando correttamente con le virgole
('H0003', 'Trinity', 9),
('H0004', 'Morpheus', 10),
('H0005', 'Mouse', 3),
('H0006', 'Switch', 6);

SELECT * FROM Hacker

INSERT INTO Hacker (Matricola, Nome, Livello) VALUES --Cosi posso creare più righe con un solo comando, separando correttamente con le virgole
('H0003', 'Trinity', 9),
('H0004', 'Morpheus', 10),
('H0005', 'Mouse', 3),
('H0006', 'Switch', 6);

SELECT * FROM Hacker

SELECT Nome, Livello --Colonne che vogliamo
FROM Hacker			 --Tabella da cui prendiamo i dati
WHERE Livello > 5;	 --Condizione

SELECT Nome, Livello
FROM Hacker
WHERE Livello > 5
ORDER BY Livello DESC; --Ordine decrescente


