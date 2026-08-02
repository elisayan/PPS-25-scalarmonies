# Design Architetturale
L'architettura dell'applicazione segue il pattern _Model-View-Controller_ (MVC), scelto per separare la logica di gioco, la gestione delle interazioni dell'utente e la rappresentazione grafica.

![Diagramma architetturale MVC](resources/architecture_diagram.png)

Il sistema è organizzato in tre componenti principali:
* il Model, che rappresenta il dominio del gioco e ne gestisce lo stato;
* la View, che fornisce l’interfaccia grafica e raccoglie le azioni dell’utente;
* il Controller, che coordina la comunicazione tra Model e View.

Le interazioni dell’utente, come la selezione di un token, il clic su una cella della plancia o la scelta di una carta animale, vengono raccolte dalla `GameView` e inoltrate al `GameController`. 
Quest’ultimo interpreta le richieste, invoca le operazioni del `GameModel` e aggiorna successivamente l’interfaccia in base al nuovo stato della partita.

## Model
Il Model costituisce il nucleo dell’applicazione e comprende le principali entità del dominio, tra cui:
* `Player`: rappresenta un giocatore della partita. Ogni giocatore possiede una plancia personale, un insieme di carte animale attive e completate e, durante il proprio turno, può eseguire le azioni previste dalle regole del gioco.
* `PersonalBoard`: rappresenta la plancia individuale del giocatore, sulla quale vengono posizionati i token terreno e, successivamente, i cubi animale per completare gli habitat.
* `CentralBoard`: rappresenta l’area condivisa da cui i giocatori prelevano i gruppi di token terreno e le carte animale durante la partita.
* `Pouch`: rappresenta il sacchetto contenente tutti i token terreno ancora disponibili, utilizzato per rifornire la plancia centrale al termine dei turni.

Il `GameModel` coordina tali componenti e mantiene lo stato complessivo della partita.

## View
La View, realizzata tramite ScalaFX, è responsabile esclusivamente della rappresentazione grafica dello stato della partita e della raccolta delle interazioni dell’utente, tra cui:
* la plancia personale del giocatore corrente;
* la plancia centrale con i token e le carte disponibili;
* le carte animale possedute dai giocatori;
* lo stato della partita e i messaggi relativi alle azioni eseguite

## Controller
Il Controller costituisce il punto di collegamento tra model e view. 
Riceve gli eventi generati dall’interfaccia grafica, invoca le corrispondenti operazioni sul model e, una volta ottenuto il nuovo stato della partita, provvede ad aggiornare la view.

L’intero flusso di gioco è quindi orchestrato dal controller, mentre model e view rimangono indipendenti tra loro.
