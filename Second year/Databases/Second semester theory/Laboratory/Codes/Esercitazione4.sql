--Trovare identificatore, cognome e nome dei docenti che, nell’anno accademico 2010/2011, hanno tenuto
--un insegnamento (l’attributo da confrontare è nomeins) che non hanno tenuto nell’anno accademico
--precedente. Ordinare la soluzione per identificatore.
SELECT DISTINCT p.id, p.cognome, p.nome
FROM persona p
JOIN (
    -- ========================================================
    -- INIZIO SOTTOQUERY: Eseguita in un'area isolata della RAM
    -- ========================================================
    
    -- Insieme A: Cosa ha insegnato nel 2010/2011
    SELECT d.id_persona, i.nomeins
    FROM docenza d
    JOIN inserogato ie ON d.id_inserogato = ie.id
    JOIN insegn i ON ie.id_insegn = i.id
    WHERE ie.annoaccademico = '2010/2011'
    
    EXCEPT
    
    -- Insieme B: Cosa ha insegnato nel 2009/2010
    SELECT d.id_persona, i.nomeins
    FROM docenza d
    JOIN inserogato ie ON d.id_inserogato = ie.id
    JOIN insegn i ON ie.id_insegn = i.id
    WHERE ie.annoaccademico = '2009/2010'
    
    -- ========================================================
    -- FINE SOTTOQUERY
    -- ========================================================
) AS novita ON p.id = novita.id_persona
ORDER BY p.id;

--Trovare i corsi di studio che non sono gestiti dalla facoltà di “Medicina e Chirurgia” e che hanno insegnamenti
--erogati con moduli nel 2010/2011. Si visualizzi il nome del corso e il numero di insegnamenti erogati con
--moduli nel 2010/2011.
SELECT cs.nome, COUNT(ie.id) AS numero_insegnamenti
FROM inserogato ie
JOIN corsostudi cs ON ie.id_corsostudi = cs.id
WHERE ie.annoaccademico = '2010/2011'
  AND ie.modulo > 0
  AND cs.id NOT IN (
      -- ==========================================
      -- FUNZIONE BLACKLIST: Isola i corsi vietati
      -- ==========================================
      SELECT cif.id_corsostudi 
      FROM corsoinfacolta cif
      JOIN facolta f ON cif.id_facolta = f.id
      WHERE f.nome = 'Medicina e Chirurgia'
  )
GROUP BY cs.nome;

--Trovare gli insegnamenti del corso di studi con id=4 che non sono mai stati offerti al secondo quadrimestre.
--Per selezionare il secondo quadrimestre usare la condizione "abbreviazione LIKE '2%'".
SELECT I.nomeins
FROM Insegn I
JOIN InsErogato IE ON I.id = IE.id_insegn
WHERE IE.id_corsostudi = 4

EXCEPT

SELECT I.nomeins
FROM Insegn I
JOIN InsErogato IE ON I.id = IE.id_insegn
JOIN InsInPeriodo IIP ON IE.id = IIP.id_inserogato
JOIN PeriodoLez PL ON IIP.id_periodolez = PL.id
WHERE PL.abbreviazione LIKE '2%'
	AND IE.id_corsostudi = 4

--Trovare il nome dei corsi di studio che non hanno mai erogato insegnamenti che contengono nel nome
--la stringa ’matematica’ (usare ILIKE invece di LIKE per rendere il test non sensibile alle
--maiuscole/minuscole (case-insensitive)).
SELECT CS.nome
FROM CorsoStudi CS
JOIN InsErogato IE ON CS.id = IE.id_corsostudi
JOIN Insegn I ON IE.id_insegn = I.id

EXCEPT

SELECT CS.nome
FROM CorsoStudi CS
JOIN InsErogato IE ON CS.id = IE.id_corsostudi
JOIN Insegn I ON IE.id_insegn = I.id
WHERE I.nomeins ILIKE '%matematica%'

--Trovare nome, cognome e telefono dei docenti che hanno tenuto nel 2009/2010 un’occorrenza di
--insegnamento che non sia un’unità logistica del corso di studi con id=4 ma che non hanno mai tenuto un
--modulo dell’insegnamento di ’Programmazione’ del medesimo corso di studi.
SELECT p.nome, p.cognome, p.telefono
FROM persona p
JOIN (
    -- ==========================================================
    -- BLOCCO A: Docenti del 2009/2010, corso 4, NON unità logistica
    -- ==========================================================
    SELECT d.id_persona
    FROM docenza d
    JOIN inserogato ie ON d.id_inserogato = ie.id
    WHERE ie.annoaccademico = '2009/2010'
      AND ie.id_corsostudi = 4
      AND ie.modulo >= 0 -- Supponendo che le unità logistiche siano < 0
      
    EXCEPT
    
    -- ==========================================================
    -- BLOCCO B: La Blacklist (Chi ha insegnato moduli di Programmazione al corso 4)
    -- ==========================================================
    SELECT d.id_persona
    FROM docenza d
    JOIN inserogato ie ON d.id_inserogato = ie.id
    JOIN insegn i ON ie.id_insegn = i.id
    WHERE ie.id_corsostudi = 4
      AND i.nomeins ILIKE '%Programmazione%'
      AND ie.modulo > 0 -- La traccia chiede specificamente un "modulo"
      
) AS prof_validi ON p.id = prof_validi.id_persona;