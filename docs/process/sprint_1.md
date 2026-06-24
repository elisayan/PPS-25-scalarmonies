# Sprint 1

**Start Date:** 24/06/2026 \
**End Date:** 01/07/2026

## Sprint Goal
Implementare le fondamenta del dominio di gioco (Plancia, Dischi e Sacchetto) utilizzando esclusivamente costrutti funzionali e immutabili (ADT in Scala 3), garantendo la copertura del core logic tramite test unitari.

## Sprint Backlog

| Priorità | Titolo                                                     | Assegnazione | Stima Iniziale | Note                                                                           | Giorno 1 | Giorno 2 | Giorno 3 | Giorno 4 | Giorno 5 | Giorno 6 | Giorno 7 |
|:---------|:-----------------------------------------------------------|:-------------|:---------------|:-------------------------------------------------------------------------------|:---------|:---------|:---------|:---------|:---------|:---------|:---------|
| **1**    | Implementazione `Plancia` e sistema di coordinate          | Daniel       | 8              | Utilizzo rigoroso dell'immutabilità per la griglia spaziale                    | 8        | 8        | 5        | 2        | 0        | 0        | 0        |
| **2**    | Implementazione `Disco`                                    | Elisa        | 8              | Creazione case class/enum per i dischi                                         | 5        | 3        | 0        | 0        | 0        | 0        | 0        |
| **3**    | Setup ScalaTest e test unitari per istanziazione `Plancia` | Daniel       | 3              | Setup del framework e verifica creazione corretta celle                        | 3        | 3        | 3        | 3        | 3        | 1        | 0        |
| **4**    | Sviluppo logica di estrazione randomica (Sacchetto)        | Filippo      | 5              | Metodi funzionali per l'estrazione randomica dei dischi                        | 5        | 5        | 2        | 0        | 0        | 0        | 0        |
| **5**    | Sviluppo struttura `OffertaComune`                         | Filippo      | 5              | Logica per popolare e aggiornare gli slot dell'offerta                         | 5        | 5        | 5        | 5        | 3        | 0        | 0        |
| **6**    | Sviluppo validazione piazzamento                           | Elisa        | 8              | Implementazione controllo validità piazzamento dischi (limiti fisici e logici) | 5        | 5        | 5        | 5        | 5        | 3        | 0        |
| **7**    | Test unitari per logiche Sacchetto                         | Filippo      | 3              | Verifica dell'estrazione dei dischi dal sacchetto nell'offerta comune          | 3        | 3        | 3        | 3        | 3        | 1        | 0        |
| **8**    | Test unitari piazzamento Dischi                            | Elisa        | 3              | Verifica della corretta validazione nel piazzamento dei dischi                 |          |          |          |          |          |          |          |
| **9**    | Creazione View Minimale                                    | Team         | 8              | Creazione di una view minimale per testare il corretto funzionamento           |          |          |          |          |          |          |          |

---

## Sprint Review
