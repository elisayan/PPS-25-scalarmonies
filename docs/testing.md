# Testing
Per la verifica del corretto funzionamento dell’applicazione è stato utilizzato `ScalaTest`, sfruttando lo stile `FlatSpec`, che permette di scrivere test descrittivi e facilmente leggibili.

Durante lo sviluppo è stato adottato un approccio _Test Driven Development_ (TDD) per le componenti principali del modello. Le regole di gioco sono state implementate a partire dalla definizione dei relativi casi di test, favorendo la realizzazione di componenti indipendenti e facilmente verificabili.

La maggior parte dei test è concentrata sul `GameModel`, responsabile della logica principale dell’applicazione. 
La suite verifica sia il comportamento corretto delle operazioni sia i principali casi di errore, simulando l’intero ciclo di gioco.

In particolare, sono stati verificati:
* inizializzazione della partita e stato iniziale del modello;
* gestione del ciclo del turno (`WaitingForAction`, `ActionDone`, `TurnComplete`);
* prelievo e posizionamento dei `TerrainToken`;
* passaggio del turno tra i giocatori;
* condizioni di terminazione della partita;
* calcolo delle celle evidenziate per il piazzamento;
* acquisizione, selezione e completamento delle carte animale;
* spostamento automatico delle carte completate da `activeCards` a `completedCards`;
* ripristino dello stato tramite l’operazione `cancelTurn`;
* gestione delle eccezioni in presenza di operazioni non consentite.

Oltre ai test del `GameModel`, sono stati sviluppati test unitari dedicati agli altri componenti del modello, tra cui `TokenValidator`, `PersonalBoard`, `CentralBoard`, `AnimalCard`, `HabitatMatcher`, `ScoreCalculator` e le relative classi di supporto, verificando le regole di dominio in modo indipendente.

Per valutare il grado di copertura del codice è stato utilizzato _Scoverage_. La copertura complessiva raggiunge il 66.60% delle istruzioni e il 64.14% dei branch. 
Il package contenente la logica applicativa (`it.unibo.model`) raggiunge invece una copertura dell’81.74%, con numerose classi fondamentali che superano il 95% di copertura, tra cui `TokenValidator`, `CentralBoard`, `AnimalDeckFactory`, `HabitatMatcher` e `Player`.

La copertura complessiva risulta inferiore rispetto a quella del modello poiché comprende anche i componenti grafici sviluppati con `ScalaFX`, per i quali non sono stati realizzati test automatici. 
Le classi della `View` e parte del `Controller` dipendono infatti dall’interazione con l’interfaccia grafica e dall’ambiente `JavaFX`, risultando poco adatte ai test unitari. 
Tali componenti sono stati verificati mediante test manuali eseguendo l’applicazione e simulando partite complete.

Infine, è stata configurata una pipeline di _Continuous Integration_ tramite GitHub Actions, che esegue automaticamente la compilazione e l’intera suite di test a ogni push e pull request, permettendo di individuare tempestivamente eventuali regressioni introdotte durante lo sviluppo.
