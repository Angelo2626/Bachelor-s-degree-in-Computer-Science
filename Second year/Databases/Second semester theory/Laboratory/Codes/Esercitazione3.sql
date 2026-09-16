--Visualizzare il numero di corso studi presenti nella base di dati.
SELECT COUNT(codice)
FROM CorsoStudi;

--Visualizzare il nome, il codice, l’indirizzo e l’identificatore del preside di tutte le facoltà
SELECT nome, codice, indirizzo, id_preside_persona
FROM Facolta;

--Trovare per ogni corso di studi che ha erogato insegnamenti nel 2010/2011 il suo nome e il nome delle
--facoltà che lo gestiscono (si noti che un corso può essere gestito da più facoltà). Non usare la relazione
--diretta tra InsErogato e Facoltà. Porre i risultati in ordine di nome corso studi.
SELECT CS.nome, F.nome
FROM InsErogato IE
JOIN CorsoStudi CS ON IE.id_corsostudi = CS.id
JOIN CorsoInFacolta CIF ON CS.id = CIF.id_corsostudi
JOIN Facolta F ON CIF.id_facolta = F.id
WHERE IE.annoaccademico = '2010/2011'
GROUP BY CS.nome, F.nome;

--Visualizzare il nome, il codice e l’abbreviazione di tutti i corsi di studio gestiti dalla facoltà di Medicina e
--Chirurgia.
SELECT CS.nome, CS.codice, CS.abbreviazione
FROM CorsoStudi CS
JOIN CorsoInFacolta CIF ON CS.id = CIF.id_corsostudi
JOIN Facolta F ON CIF.id_facolta = F.id
WHERE F.nome = 'Medicina e Chirurgia';

--Visualizzare il codice, il nome e l’abbreviazione di tutti corsi di studio che nel nome contengono la
--sottostringa ’lingue’ (eseguire il confronto usando ILIKE invece di LIKE: in questo modo i caratteri
--maiuscolo e minuscolo non sono diversi).
SELECT codice, nome, abbreviazione
FROM CorsoStudi 
WHERE nome ILIKE '%lingue%'

--Visualizzare le sedi dei corsi di studi in un elenco senza duplicati.
SELECT DISTINCT sede
FROM CorsoStudi

--Visualizzare solo i moduli degli insegnamenti erogati nel 2010/2011 nei corsi di studi della facoltà di Economia.
--Si visualizzi il nome dell’insegnamento, il discriminante (attributo descrizione della tabella Discriminante),
--il nome del modulo e l’attributo modulo
SELECT i.nomeins, d.descrizione, ie.nomemodulo, ie.modulo
FROM inserogato ie
JOIN corsostudi cs ON ie.id_corsostudi = cs.id
JOIN corsoinfacolta cif ON cs.id = cif.id_corsostudi
JOIN facolta f ON cif.id_facolta = f.id
JOIN insegn i ON ie.id_insegn = i.id
JOIN discriminante d ON ie.id_discriminante = d.id
WHERE ie.annoaccademico = '2010/2011' 
  AND f.nome = 'Economia'
  AND ie.modulo > 0;

--Visualizzare il nome e il discriminante (attributo descrizione della tabella Discriminante) degli
--insegnamenti erogati nel 2009/2010 che non sono moduli e che hanno 3, 5 o 12 crediti. Si ordini il
--risultato per discriminante.
SELECT DISTINCT I.nomeins, D.descrizione
FROM Insegn I
JOIN InsErogato IE ON I.id = IE.id_insegn
JOIN Discriminante D ON IE.id_discriminante = D.id
WHERE IE.annoaccademico = '2009/2010' AND IE.modulo = 0 AND IE.crediti IN (3, 5, 12)
ORDER BY D.descrizione;

--Visualizzare l’identificatore, il nome e il discriminante degli insegnamenti erogati nel 2008/2009 che non
--sono moduli o unità logistiche e con peso maggiore di 9 crediti. Ordinare per nome.
SELECT I.id, I.nomeins, D.nome
FROM Insegn I
JOIN InsErogato IE ON I.id = IE.id_insegn
JOIN Discriminante D ON IE.id_discriminante = D.id
WHERE IE.annoaccademico = '2008/2009'
AND IE.modulo = 0
AND IE.crediti > 9
ORDER BY I.nomeins;

--Visualizzare in ordine alfabetico di nome degli insegnamenti (esclusi i moduli e le unità logistiche) erogati
--nel 2010/2011 nel corso di studi in Informatica, riportando il nome, il discriminante, i crediti e gli anni di
--erogazione.
SELECT i.nomeins, d.descrizione, ie.crediti, ie.annierogazione
FROM corsostudi cs
JOIN inserogato ie ON cs.id = ie.id_corsostudi
JOIN insegn i ON ie.id_insegn = i.id
JOIN discriminante d ON ie.id_discriminante = d.id
WHERE cs.nome ILIKE '%Informatica%'
  AND ie.modulo = 0 
  AND ie.annoaccademico = '2010/2011'
ORDER BY i.nomeins ASC;

--Trovare il massimo numero di crediti associato a un insegnamento fra quelli erogati nel 2010/2011.
SELECT MAX(crediti)
FROM inserogato
WHERE annoaccademico = '2010/2011';

--Trovare, per ogni anno accademico, il massimo e il minimo numero di crediti erogati tra gli insegnamenti dell’anno.
SELECT I.nomeins, MAX(IE.crediti) AS MC, MIN(IE.crediti) AS MIC
FROM InsErogato IE
JOIN Insegn I ON IE.id_insegn = I.id

--Trovare nome, cognome dei docenti che nell’anno accademico 2010/2011 erano docenti in almeno due
--corsi di studio (vale a dire erano docenti in almeno due insegnamenti o moduli A e B dove A è del corso C1
--e B è del corso C2 con C1 <> C2).
SELECT P.nome, P.cognome
FROM Persona P
JOIN Docenza D ON P.id = D.id_persona
JOIN InsErogato IE ON D.id_inserogato = IE.id
JOIN CorsoStudi CS ON IE.id_corsostudi = CS.id
WHERE IE.annoaccademico = '2010/2011'
GROUP BY P.nome, P.cognome
HAVING COUNT(DISTINCT IE.id_corsostudi) >= 2;

--Trovare per ogni periodo di lezione del 2010/2011 la cui descrizione inizia con ’I semestre’ o ’Primo
--semestre’ il numero di occorrenze di insegnamento allocate in quel periodo. Si visualizzi quindi:
--l’abbreviazione, il discriminante, inizio, fine e il conteggio richiesto ordinati rispetto all’inizio e fine.
SELECT PL.abbreviazione, PD.discriminante, PD.inizio, PD.fine  
FROM 