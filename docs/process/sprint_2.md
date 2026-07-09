# Sprint 2

**Start Date:** 01/07/2026 \
**End Date:** 08/07/2026

## Sprint Goal
Implementare le principali strutture del dominio di gioco (PersonalBoard, CentralBoard, AnimalCard e AnimalCubes) con approccio funzionale e immutabile in Scala 3, garantendo l’integrazione tra le componenti core, la gestione del turno e la validazione delle regole di piazzamento, supportata da test unitari e dalla creazione di una view minimale per il testing del core game loop.

## Sprint Backlog

| Priorità | Titolo                                                           | Assegnazione | Stima Iniziale | Note                                                                                                                                    | Giorno 1 | Giorno 2 | Giorno 3 | Giorno 4 | Giorno 5 | Giorno 6 | Giorno 7 |
|:---------|:-----------------------------------------------------------------|:-------------|:---------------|:----------------------------------------------------------------------------------------------------------------------------------------|:---------|:---------|:---------|:---------|:---------|:---------|:---------|
| **1**    | Implementazione `PersonalBoard` e sistema di coordinate          | Daniel       | 3              | Utilizzo rigoroso dell'immutabilità per la griglia spaziale                                                                             | 3        | 3        | 5        | 2        | 0        | 0        | 0        |
| **2**    | Setup ScalaTest e test unitari per istanziazione `PersonalBoard` | Daniel       | 3              | Setup del framework e verifica creazione corretta celle                                                                                 | 3        | 3        | 3        | 3        | 3        | 1        | 0        |
| **3**    | Implementazione `AnimalCard`                                     | Filippo      | 5              | Implementazione della logica di creazione e gestione delle AnimalCard                                                                   | 3        | 3        | 1        | 0        | 0        | 0        | 0        |
| **4**    | Integrazione `CentralBoard` con `AnimalCard`                     | Filippo      | 3              | Gestione della board centrale e popolamento degli slot dell’offerta tramite integrazione con AnimalCard                                 | 3        | 3        | 3        | 3        | 3        | 3        | 3        |
| **5**    | Sviluppo validazione piazzamento                                 | Elisa        | 3              | Implementazione per il piazzamento dei dischi sulla PersonalBoard  con integrazione delle regole di validazione legate agli AnimalCubes | 5        | 5        | 5        | 5        | 5        | 5        | 5        |
| **6**    | Gestione turni e loop                                            | Elisa        | 8              | Implementazione del flusso di gioco: gestione dei turni, ordine dei giocatori e ciclo principale della partita                          | 5        | 5        | 5        | 5        | 5        | 3        | 0        |
| **7**    | Pattern matching per `AnimalCubes`                               | Filippo      | 8              | Implementazione delle regole di matching tra pattern richiesti e configurazioni di AnimalCubes                                          | 8        | 8        | 8        | 5        | 5        | 3        | 0        |
| **8**    | Implementazione motore di punteggio                              | Daniel       | 5              | Sviluppo del sistema di scoring basato sulle condizioni di gioco e verifica delle condizioni di punteggio                               | 5        | 5        | 5        | 5        | 5        | 5        | 5        |
| **10**   | Creazione View Minimale                                          | Team         | 8              | Creazione di una view minimale per testare il corretto funzionamento                                                                    | 8        | 8        | 6        | 6        | 3        | 0        | 0        |

---

## Sprint Review
Durante questo sprint sono state implementate con successo le fondamenta della `PersonalBoard` insieme al relativo sistema di coordinate immutabili, supportate da una suite di test unitari tramite `ScalaTest`. Sono state inoltre completate le strutture logiche per le `AnimalCard`, l'algoritmo di pattern matching per gli `AnimalCubes`, la gestione del loop dei turni e una prima View Minimale per verificare il flusso generale.

Per l'integrazione completa della `CentralBoard`, lo sviluppo avanzato della validazione del piazzamento e l'implementazione del motore di punteggio si è resa necessaria una forte interdipendenza con le logiche dei token e la struttura ad alveare della plancia visiva, emersa in fase di sviluppo. Di conseguenza, questi item sono stati spostati alla fase successiva, dove verranno rifiniti e integrati stabilmente con l'interfaccia grafica.
