title: Requirement specification
nav_order: 2
parent: Report

# Requirement specification

## 1. Business Requirements

Il progetto "Scalarmonies" mira a realizzare una simulazione digitale e interattiva del gioco da tavolo *Harmonies*, supportando da 1 a 4 giocatori con un'interfaccia grafica minimale. L'obiettivo principale è fornire un sistema stabile che automatizzi il flusso dei turni, il rispetto dei vincoli spaziali e il calcolo dei punteggi.

In ambito accademico, il successo di questo progetto sarà determinato dal soddisfacimento dei seguenti obiettivi:
* Consolidare la padronanza di concetti avanzati in Scala 3 (es. monadi, Algebraic Data Types, higher-order functions).
* Sfruttare rigorosamente il paradigma di programmazione funzionale, prediligendo l'immutabilità degli stati.
* Adottare tecniche di Test Driven Development (TDD) per validare le meccaniche di base.
* Applicare metodologie agili e pratiche DevOps per la gestione del processo, includendo GitFlow, Continuous Integration (CI) tramite GitHub Actions e automazione della build (SBT).

## 2. Modello di Dominio

## 3. Requisiti Funzionali

### 3.1 Requisiti Utente (FR-U)
L'utente deve poter:
* Selezionare il numero di giocatori (da 1 a 4) per avviare la partita.
* Selezionare, durante il proprio turno, un set composto da tre dischi terreno dall'offerta comune.
* Cliccare sulla propria plancia individuale per posizionare i dischi, rispettando i vincoli di impilamento.
* Acquisire carte animale dall'offerta comune.
* Posizionare un segnalino animale su una cella valida della plancia per completare il pattern di una carta.
* Visualizzare la classifica finale e i punteggi al termine della partita.

**Funzionalità Opzionali:**
* Giocare in modalità singleplayer contro avversari virtuali gestiti tramite euristiche.
* Abilitare le carte "Spiriti della Natura".

### 3.2 Requisiti di Sistema (FR-S)
Il sistema deve:
* Gestire l'inizio della partita, popolare il tabellone centrale dei dischi ed estrarre le carte animale iniziali.
* Estrarre casualmente i dischi dal sacchetto per ripristinare il tabellone centrale a ogni round.
* Validare il posizionamento dei dischi, impedendo mosse geometricamente illegali o fuori dai bordi della plancia.
* Scansionare la plancia per eseguire il pattern matching delle conformazioni richieste dalle carte animale.
* Calcolare ricorsivamente il punteggio finale sommando i punti derivanti dagli habitat e dagli animali ospitati.
* Rilevare le condizioni di fine partita, ovvero l'esaurimento dei dischi o il popolamento delle celle necessarie da parte di uno dei due giocatori.

## 4. Requisiti Non Funzionali (NFR)

* **Robustezza (Gestione Errori):** Il motore logico non deve lanciare eccezioni a runtime per stati invalidi, ma gestirli puramente a livello funzionale tramite `Either`, `Option` o `Try`.
* **Testabilità:** La logica del dominio deve essere isolata dalla UI e coperta da test unitari automatizzati.
* **Performance** L'interfaccia utente deve fornire un feedback reattivo. Nella modalità opzionale singleplayer, il calcolo delle mosse dell'avversario virtuale non deve bloccare il thread della UI.

## 5. Requisiti Di Implementazione (IR)

* Il sistema deve essere sviluppato in Scala 3.
* Il design deve aderire all'architettura a livelli (MVC).
* Deve essere utilizzato un framework grafico per la View.
* Il codice sorgente deve essere tracciato tramite Git e supportato da una pipeline di Continuous Integration.