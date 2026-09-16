-- ==============================================================================
-- 1. CREAZIONE DELLE TABELLE (L'Architettura in Memoria)
-- ==============================================================================

-- T1: Anagrafica base degli studenti
CREATE TABLE Studente (
    id SERIAL PRIMARY KEY,
    matricola VARCHAR(10) UNIQUE NOT NULL,
    nome VARCHAR(50),
    cognome VARCHAR(50)
);

-- T2: Anagrafica base dei professori
CREATE TABLE Docente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50),
    cognome VARCHAR(50),
    dipartimento VARCHAR(50)
);

-- T3: Il catalogo astratto delle materie
CREATE TABLE Corso (
    id SERIAL PRIMARY KEY,
    nome_corso VARCHAR(100),
    crediti INT
);

-- T4: La Tabella Ponte Centrale (L'equivalente del tuo InsErogato)
-- Collega un corso astratto a un professore fisico in un determinato anno.
CREATE TABLE EdizioneCorso (
    id SERIAL PRIMARY KEY,
    id_corso INT REFERENCES Corso(id),
    id_docente INT REFERENCES Docente(id),
    anno_accademico VARCHAR(9)
);

-- T5: Il registro dei voti
CREATE TABLE Esame (
    id SERIAL PRIMARY KEY,
    id_studente INT REFERENCES Studente(id),
    id_edizione INT REFERENCES EdizioneCorso(id),
    voto INT CHECK (voto >= 18 AND voto <= 31), -- 31 simboleggia il 30 e lode
    data_registrazione DATE
);

-- ==============================================================================
-- 2. POPOLAMENTO DEI DATI (L'inserimento nei blocchi del disco)
-- ==============================================================================

-- Inserimento Studenti (Nota: Giulia Neri è la nostra studentessa "fantasma")
INSERT INTO Studente (matricola, nome, cognome) VALUES
('VR40001', 'Mario', 'Rossi'),
('VR40002', 'Luigi', 'Verdi'),
('VR40003', 'Giulia', 'Neri'); 

-- Inserimento Docenti
INSERT INTO Docente (nome, cognome, dipartimento) VALUES
('Alan', 'Turing', 'Informatica'),
('Ada', 'Lovelace', 'Matematica'),
('John', 'Von Neumann', 'Informatica');

-- Inserimento Corsi
INSERT INTO Corso (nome_corso, crediti) VALUES
('Basi di Dati', 12),
('Analisi 2', 9),
('Algoritmi', 12),
('Fisica', 6);S

-- Inserimento Edizioni (Chi insegna cosa e quando)
INSERT INTO EdizioneCorso (id_corso, id_docente, anno_accademico) VALUES
(1, 1, '2025/2026'), -- Turing insegna Basi di Dati quest'anno
(2, 2, '2025/2026'), -- Lovelace insegna Analisi 2 quest'anno
(3, 1, '2025/2026'), -- Turing insegna anche Algoritmi
(4, 3, '2024/2025'), -- Von Neumann ha insegnato Fisica l'anno scorso
(1, 3, '2024/2025'); -- Von Neumann ha insegnato Basi di Dati l'anno scorso

-- Inserimento Esami (Il libretto universitario)
INSERT INTO Esame (id_studente, id_edizione, voto, data_registrazione) VALUES
(1, 1, 28, '2026-06-15'), -- Mario ha preso 28 in Basi di Dati con Turing
(1, 2, 24, '2026-06-20'), -- Mario ha preso 24 in Analisi 2
(2, 1, 31, '2026-06-15'), -- Luigi ha preso 30L in Basi di Dati
(2, 4, 18, '2025-02-10'); -- Luigi ha preso 18 in Fisica l'anno scorso
-- Giulia (Studente 3) non ha nessun record qui.

--Voglio trovare i voti (stampa solo la colonna voto) 
--presenti nel registro che sono strettamente maggiori di TUTTI i voti presi dallo studente 1.
SELECT voto
FROM Esame
WHERE voto > ALL (
	SELECT voto
	FROM Esame
	WHERE id_studente = 1
);

--Se io ti chiedessi: "Trovare i voti registrati nel 
--database che sono strettamente maggiori di ALMENO UNO dei voti presi dallo studente 1".
SELECT voto
FROM Esame
WHERE voto > ANY (
	SELECT voto
	FROM Esame
	WHERE id_studente = 1
);

SELECT s.nome, s.cognome
FROM Studente s
WHERE NOT EXISTS(
	SELECT 1
	FROM Esame e
	WHERE e.id_studente = s.id
);

--Trovare nome e cognome degli studenti che hanno superato ALMENO UN esame con il professor 'Alan Turing', 
--ma che NON hanno MAI sostenuto un esame con la professoressa 'Ada Lovelace'."
SELECT s.nome, s.cognome
FROM studente s
JOIN (
    -- ==========================================================
    -- BLOCCO A: Array degli ID degli studenti di Alan Turing
    -- ==========================================================
    SELECT e.id_studente
    FROM esame e
    JOIN edizionecorso ec ON e.id_edizione = ec.id
    JOIN docente d ON ec.id_docente = d.id
    WHERE d.nome = 'Alan'
    
    EXCEPT
    
    -- ==========================================================
    -- BLOCCO B: La Blacklist (Array degli ID degli studenti di Ada)
    -- ==========================================================
    SELECT e.id_studente
    FROM esame e
    JOIN edizionecorso ec ON e.id_edizione = ec.id
    JOIN docente d ON ec.id_docente = d.id
    WHERE d.nome = 'Ada'

) AS studenti_validi ON s.id = studenti_validi.id_studente;


