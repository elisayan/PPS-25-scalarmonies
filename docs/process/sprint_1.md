# Sprint 1

**Start Date:** 24/06/2026 \
**End Date:** 01/07/2026

## Sprint Goal
Implementare le fondamenta del dominio di gioco (Plancia, Dischi e Sacchetto) utilizzando esclusivamente costrutti funzionali e immutabili (ADT in Scala 3), garantendo la copertura del core logic tramite test unitari.

## Sprint Backlog

| Priorità | Titolo                                                           | Assegnazione | Stima Iniziale | Note                                                                           | Giorno 1 | Giorno 2 | Giorno 3 | Giorno 4 | Giorno 5 | Giorno 6 | Giorno 7 |
|:---------|:-----------------------------------------------------------------|:-------------|:---------------|:-------------------------------------------------------------------------------|:---------|:---------|:---------|:---------|:---------|:---------|:---------|
| **1**    | Implementazione `PersonalBoard` e sistema di coordinate          | Daniel       | 8              | Utilizzo rigoroso dell'immutabilità per la griglia spaziale                    | 8        | 8        | 5        | 2        | 0        | 0        | 0        |
| **2**    | Implementazione `Token`                                          | Elisa        | 5              | Creazione case class/enum per i dischi                                         | 5        | 3        | 0        | 0        | 0        | 0        | 0        |
| **3**    | Setup ScalaTest e test unitari per istanziazione `PersonalBoard` | Daniel       | 3              | Setup del framework e verifica creazione corretta celle                        | 3        | 3        | 3        | 3        | 3        | 1        | 0        |
| **4**    | Sviluppo logica di estrazione randomica (`Pouch`)                | Filippo      | 5              | Metodi funzionali per l'estrazione randomica dei dischi                        | 5        | 5        | 2        | 1        | 0        | 0        | 0        |
| **5**    | Sviluppo struttura `CentralBoard`                                | Filippo      | 8              | Logica per popolare e aggiornare gli slot dell'offerta                         | 8        | 8        | 8        | 5        | 0        | 0        | 0        |
| **6**    | Sviluppo validazione piazzamento                                 | Elisa        | 8              | Implementazione controllo validità piazzamento dischi (limiti fisici e logici) | 5        | 5        | 5        | 3        | 3        | 0        | 0        |
| **7**    | Test unitari per logiche Sacchetto                               | Filippo      | 3              | Verifica dell'estrazione dei dischi dal sacchetto nell'offerta comune          | 3        | 3        | 3        | 3        | 3        | 1        | 0        |
| **8**    | Test unitari piazzamento Dischi                                  | Elisa        | 5              | Verifica della corretta validazione nel piazzamento dei dischi                 |          |          |          |          |          |          |          |

---

## Sprint Review
Durante questo sprint sono state implementate le classi `Cell`, `Coordinate`, `TerrainToken` e `Pouch`.
Per `TokenValidator` e `CentralBoard` si è resa necessaria la presenza delle `AnimalCards`, che saranno implementate nei prossimi sprint per completare le funzionalità previste.
È stata inoltre definita la struttura della `PersonalBoard`, le cui funzionalità saranno ulteriormente rifinite e migliorate nel prossimo sprint.