CREATE TABLE Missione(
	IDMissione INTEGER PRIMARY KEY,
	NomeMissione VARCHAR(50),
	HackerAssegnato VARCHAR(5) REFERENCES Hacker(Matricola) --Stiamo dicendo al sistema che HackerAssegnato è una chiave esterna, con riferimento a matricola
);

INSERT INTO Missione (IDMissione, NomeMissione, HackerAssegnato) VALUES
(1, 'Infiltrazione Server Centrale', 'H0004'), 
(2, 'Estrazione Codici', 'H0003'),           
(3, 'Sabotaggio Rete', 'H0006'),             
(4, 'Ricerca nella Matrice', NULL);

SELECT * FROM Mission

SELECT Hacker.Nome, Missione.NomeMissione --Visto che peschiamo da 2 tabelle diverse è buona norma usare NomeTabella.NomeColonna
FROM Hacker --Tabella di partenza (Quella di sinistra per intenderci)
INNER JOIN Missione ON Hacker.Matricola = Missione.HackerAssegnato; --Indica la condizione (in questo caso l'uguaglianza) e indichiamo tra quali colonne dobbiamo applicare la condizione

SELECT Hacker.Nome, Missione.NomeMissione
FROM Hacker
RIGHT JOIN Missione ON Hacker.Matricola = Missione.HackerAssegnato;

--1. La famiglia degli OUTER JOIN (Left, Right, Full)Sono gli unici JOIN "inclusivi". 
--Servono appositamente per non perdere dati. Se c'è una corrispondenza, uniscono le righe. 
--Se non c'è, tengono comunque in vita la riga della tabella principale (quella a sinistra o quella a destra) e riempiono gli spazi vuoti con NULL.
--2. La famiglia degli INNER JOIN (Esclusivi)Qui dentro ricadono la maggior parte degli altri concetti che hai nominato. 
--L'INNER JOIN è spietato: se la chiave non trova un gemello esatto nell'altra tabella, la riga viene scartata dal risultato finale.
--Theta Join ($\theta$-join): È un concetto teorico. In SQL si traduce semplicemente in un normale INNER JOIN in cui la condizione 
--dopo ON non è un'uguaglianza, ma un operatore diverso (es. >, <, <>). Ad esempio: potresti unire due tabelle solo se il livello 
--dell'hacker è maggiore della difficoltà della missione.Natural Join: È un comando SQL vero e proprio (NATURAL JOIN). 
--È un JOIN pigro. Non usi la parola ON, perché il database cerca automaticamente le colonne che hanno lo stesso identico nome in entrambe 
--le tabelle e le unisce usando l'uguaglianza. Nel mondo reale è pericoloso e si evita, perché se in futuro qualcuno aggiunge una 
--colonna con un nome ambiguo, l'incastro si rompe.
--Self Join: Non è un comando speciale, ma un trucco logico. È un normalissimo INNER (o LEFT) JOIN dove la tabella di sinistra e la tabella di destra sono la stessa tabella.

-- Aggiungiamo la colonna che conterrà la matricola del mentore
ALTER TABLE Hacker ADD COLUMN MatricolaMentore VARCHAR(5);

-- Morpheus fa da mentore a Neo (Neo aveva la matricola H0001, Morpheus H0004)
UPDATE Hacker SET MatricolaMentore = 'H0004' WHERE Nome = 'Neo';

-- Trinity fa da mentore a Mouse (Mouse H0005, Trinity H0003)
UPDATE Hacker SET MatricolaMentore = 'H0003' WHERE Nome = 'Mouse';

SELECT Allievo.Nome AS NomeAllievo, Mentore.Nome AS NomeMentore --AS serve da alias per cambiare nome
FROM Hacker AS Allievo --qui facciamo lo stesso con la tabella hacker che diventa allievo
INNER JOIN Hacker AS Mentore ON Allievo.MatricolaMentore = Mentore.Matricola;