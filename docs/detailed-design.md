# Model
L'architettura del dominio è stata progettata valorizzando la modularità e la netta separazione delle responsabilità tra i componenti. 
Il diagramma delle classi sottostante sintetizza la struttura complessiva, illustrando le relazioni e le composizioni fondamentali tra le entità di gioco.
```mermaid
classDiagram
    direction TB

%% Interfaces and Main Model
    class GameModel {
        <<trait>>
       
    }

    class GameModelImpl {
        
    }

    class TurnState {
        <<enum>>
        WaitingForAction
        ActionDone
        TurnComplete
    }

    class CentralBoard {
    }

    class Pouch {
    }

    class Player {
    }

    class Card {
    }

    class PersonalBoard {
        <<trait>>
    }

    class PersonalBoardImpl {
    }

    class BoardSide {
        <<enum>>
        SideA
        SideB
    }

    class Coordinate {
    }

    class Cell {
    }

    class TerrainToken {
    }

%% GameModel Relationships
    GameModel <|.. GameModelImpl : implements
    GameModelImpl "1" *-- "1" TurnState : tracks state
    GameModelImpl "1" *-- "2..4" Player : manages
    GameModelImpl "1" *-- "1..*" Card : contains deck
    GameModelImpl "1" *-- "1" CentralBoard : owns
    GameModelImpl "1" *-- "1" Pouch : owns

%% Player & PersonalBoard Relationships
    Player "1" *-- "1" PersonalBoard : owns
    PersonalBoard <|.. PersonalBoardImpl : implements
    PersonalBoardImpl "1" *-- "1" BoardSide : configured by
    PersonalBoardImpl "1" *-- "*" Cell : composed of
    PersonalBoardImpl "1" *-- "*" Coordinate : indexed by

%% TerrainToken Lifecycle / Containers
    Pouch "1" --o "*" TerrainToken : initially contains
    CentralBoard "1" --o "*" TerrainToken : exposes in market
    Cell "1" --o "*" TerrainToken : stores in stack

%% Card Relationships
    Card "1" *-- "*" CentralBoard: stores
    Card "*" *-- "*" Player: has
```


## Player e Card
`Player` è la `case class` che rappresenta il partecipante alla partita e ne raccoglie lo stato complessivo: l'identificativo, il nome, la plancia personale (`PersonalBoard`), l'insieme delle carte animale in corso di completamento (`activeCards`) e quelle già completate (`completedCards`).


## PersonalBoard e BoardSide
`PersonalBoard` rappresenta la plancia individuale su cui ciascun giocatore costruisce il proprio paesaggio esagonale. Il regolamento prevede due differenti layout di gioco (`SideA` e `SideB`), modellati tramite l'enumerazione `BoardSide`, che definisce in modo chiuso le configurazioni disponibili in termini di dimensioni e numero di celle valide.

Sul piano progettuale, `PersonalBoard` è definita come un `trait` pubblico, mentre la sua implementazione concreta `PersonalBoardImpl` è mantenuta `private` all'interno del relativo companion object. In questo modo il resto del sistema dipende esclusivamente dall'astrazione e mai dai dettagli realizzativi. 

Le operazioni di piazzamento dei tasselli (`placeToken`) e dei cubi animale (`placeAnimalOnCell`) seguono una logica rigorosamente immutabile, restituendo un `Option[PersonalBoard]` aggiornato: l'uso del tipo `Option` garantisce la gestione sicura dei casi limite e delle coordinate invalide senza ricorrere ad eccezioni. Inoltre, gli algoritmi di analisi del paesaggio (come la ricerca delle coordinate per tipo di terreno `coordsWithTerrain` e l'individuazione dei gruppi connessi adiacenti `findConnectedGroups` per il calcolo dei punteggi) sono modellati come *Extension Methods*.

## Coordinate e Cell
La gestione dello spazio e dei contenitori fisici della plancia è affidata alla coppia `Coordinate` e `Cell`.

`Coordinate` costituisce l'astrazione per la rappresentazione delle posizioni bidimensionali $(x, y)$ su griglia esagonale. È modellata come un `trait` astratto con implementazione privata `CoordinateImpl`. Il trait incapsula l'aritmetica vettoriale (`+`, `-`, `*`), la rotazione a $60^\circ$ (`rotate60`) e la navigazione verso i sei vicini adiacenti (`northNeighbour`, `southEasternNeighbour`, ecc.), isolando la plancia da qualsiasi calcolo geometrico di basso livello.

`Cell` rappresenta la singola posizione esagonale sulla plancia e funge da contenitore sia per la pila di tasselli terreno (`TerrainToken`), sia per l'eventuale cubo animale. La classe è modellata come una `case class` immutabile in cui i terreni sovrapposti sono gestiti come una lista LIFO (*Last-In, First-Out*). L'accesso al terreno affiorante (`topToken`) e il posizionamento degli animali (`occupyWithAnimal`) sfruttano il tipo `Option` per validare lo stato ed evitare mosse non consentite (es. piazzare animali su celle vuote o già occupate).

## GameModel e TurnState
`GameModel` rappresenta la facciata e il punto di coordinamento centrale dell'intero modello di dominio. Espone le operazioni pubbliche per guidare l'evoluzione della partita: il prelievo dei tasselli, la scelta e il posizionamento delle carte animale, il piazzamento dei token terreno e dei cubi animale, fino alla conclusione del turno o dell'intero gioco.

Sul piano architetturale, `GameModel` è definito come un `trait` pubblico, mentre la sua implementazione concreta `GameModelImpl` è mantenuta `private` all'interno del companion object. Esso funge da *Factory* mettendo a disposizione metodi `apply` sia per la creazione del gioco standard sia per scenari di test (es. forzando il sacchetto vuoto). Per garantire la flessibilità dell'esperienza utente, `GameModelImpl` implementa un meccanismo di ripristino del turno ispirato al pattern *Memento/Snapshot*: salvando una copia immutabile dello stato all'inizio del turno (`turnSnapshot`), la funzione `cancelTurn` consente al giocatore di annullare le azioni correnti e ripristinare lo stato iniziale.

La gestione delle fasi del turno è formalizzata tramite l'enumerazione `TurnState`, che sfrutta la modellazione ad *Algebraic Data Types* (ADT) per rappresentare l'insieme finito degli stati che un turno può assumere (`WaitingForAction`, `ActionDone`, `TurnComplete`). Il ciclo di vita del turno è gestito come una macchina a stati finiti: prima di eseguire un'operazione, il model verifica che essa sia consentita nello stato corrente lsciando il compito di rifiutare mosse illegali a eccezioni di stato (`IllegalStateException`), garantendo che il sistema non transiti mai verso configurazioni non valide.


## CentralBoard e Pouch
La plancia centrale (`CentralBoard`) e il sacchetto (`Pouch`) modellano il mercato comune da cui i giocatori attingono le risorse durante il proprio turno.

`CentralBoard` offre gli slot pubblici per la selezione dei tasselli terreno e delle carte animale. Le sue operazioni (`takeTokens`, `takeCard`) restituiscono una tupla contenente l'elemento prelevato e una nuova istanza aggiornata della plancia centrale, preservando la rigorosa immutabilità dello stato. Il riempimento degli slot scoperti è delegato al metodo `fill`, che coordina l'estrazione sincrona dei token dal sacchetto e delle carte dal mazzo.

`Pouch` rappresenta il contenitore fisico dei `TerrainToken` non ancora estratti. Viene inizializzato tramite il factory method `initialPouch()` e gestisce la popolazione dei tasselli in modo totalmente isolato. La sua dimensione (`pouchSize`) è una delle condizioni primarie monitorate da `GameModel` per determinare l'innesco dell'ultimo round di gioco (`isLastRound`).


## AnimalCard e AnimalDeckFactory
Le carte animale (`AnimalCard`) rappresentano gli obiettivi di punteggio che i giocatori possono acquisire dalla plancia centrale per poi completare sulla propria `PersonalBoard`.

Ogni carta definisce il numero massimo di cubi animale che può ospitare (`maxCubes`), i cubi attualmente piazzati (`placedCubes`) e la configurazione di habitat richiesta (`habitat`). Il posizionamento di un cubo viene validato mediante l'ausilio del modulo `HabitatMatcher`, che analizza la topologia della plancia personale per identificare le corrispondenze valide. Quando una carta raggiunge il limite massimo di cubi, la transizione dal gruppo delle carte attive (`activeCards`) a quelle completate (`completedCards`) avviene in modo automatico all'interno del `GameModel`.

La creazione e il mescolamento iniziale del mazzo sono isolati all'interno di `AnimalDeckFactory`, un modulo *Factory* dedicato che garantisce la casualità della disposizione delle carte all'avvio della partita.

# Controller

Il **Controller** funge da mediatore tra l'interfaccia grafica e il modello di dominio immutabile, disaccoppiando completamente la logica di presentazione dalle regole di gioco.
La sua responsabilità principale è tradurre gli input dell'utente in transizioni di stato del modello (`GameModel`), garantendo la coerenza del flusso del turno e gestendo il ciclo di vita dell'applicazione.

#### 1. Contract-First Design e Information Hiding
Per mantenere un basso accoppiamento, il controller è esposto all'esterno esclusivamente tramite il trait `GameController`, che ne definisce il contratto pubblico:

```scala
trait GameController:
  def currentModel: GameModel
  def currentPlayerId: Int
  def currentTurnState: TurnState
  def onTakeTokens(slot: Int): Unit
  def onSelectToken(token: TerrainToken): Unit
  def onPlaceToken(coordinate: Coordinate): Unit
  def onTakeAnimalCard(slot: Int): Unit
  def onSelectActiveCard(card: AnimalCard): Unit
  def onCellClicked(coordinate: Coordinate): Unit
  def onCancelTurn(): Unit
  def onEndTurn(): Unit
```

L'implementazione concreta (GameControllerImpl) è incapsulata all'interno del companion object tramite un metodo factory (apply).
In questo modo, la View interagisce solo con l'interfaccia astratta, ignorando i dettagli dello stato mutabile interno.

#### 2. Esecuzione Funzionale delle Azioni e Gestione degli Errori
Poiché GameModel è immutabile e lancia eccezioni (IllegalStateException) nel caso in cui una mossa violi le regole del turno o di impilamento, il controller centralizza l'esecuzione delle mutazioni di stato tramite l'esecuzione della higher-order function `executeAction`:
```scala
private def executeAction(action: GameModel => GameModel)(onSuccess: GameModel => Unit): Unit =
  try
    model = action(model)
    onSuccess(model)
  catch case e: IllegalStateException => handleError(e)
```

Questo approccio offre tre vantaggi progettuali:
* **Isolamento delle mutazioni:** La funzione di transizione di stato (action: GameModel => GameModel) viene applicata in un unico punto controllato.
* **Boundary di gestione errori:** Eventuali mosse illegali vengono catturate uniformemente senza far crashare l'applicazione o lasciare la GUI in uno stato inconsistente. Il fallimento viene intercettato da handleError, che notifica la vista per mostrare un feedback temporaneo all'utente (`showTemporaryError`).  
* **Aggiornamento reattivo mirato:** La callback `onSuccess` viene invocata solo a transizione avvenuta con successo, sincronizzando il rendering del nuovo stato nella vista (`view.updateState`) e aggiornando il log di gioco (`refreshView`). 

Il seguente diagramma di sequenza mostra come il controller gestisce l'esecuzione di un'azione (es. piazzamento token) ed eventuali errori.

```mermaid
sequenceDiagram
autonumber
actor User as Giocatore
participant View as GameView
participant Ctrl as GameController
participant Model as GameModel

    User->>View: Click su Cella per posizionare un Token
    View->>Ctrl: onPlaceToken(coordinate)
    
    rect rgb(240, 248, 255)
        Note over Ctrl,Model: executeAction(action)(onSuccess)
        Ctrl->>Model: action(currentModel)
        
        alt Transizione Valida
            Model-->>Ctrl: newModel (nuova istanza)
            Ctrl->>Ctrl: model = newModel
            Ctrl->>View: updateState(newModel)
            Ctrl->>View: refreshView(logMessage)
        else Mossa Illegale (IllegalStateException)
            Model-->>Ctrl: throw IllegalStateException
            Ctrl->>View: showTemporaryError(message)
            Note over Ctrl,View: Il modello corrente resta invariato
        end
    end
```

#### 3. Orchestrazione delle Scene e Flusso di Partita
Il controller amministra la grafica dell'intera applicazione, regolando la transizione tra le tre viste fondamentali senza reciproche dipendenze dirette tra di esse:
* **Bootstrap (start):** Inizializza l'applicazione mostrando la schermata di configurazione (HomeView) e passando la callback onStartGame.
* **Gameplay (onStartGame):** Crea la lista dei giocatori e l'istanza iniziale di GameModel, impostando GameView come radice della scena. 
* **Terminazione (onEndTurn -> onEndGame):** A ogni fine turno, verifica la condizione d'arresto (`model.isGameOver`); se soddisfatta, sostituisce la vista di gioco con ScoreCalculatorView per il calcolo e la visualizzazione del punteggio finale

```mermaid
stateDiagram-v2
    [*] --> Configuration : start() / Init Stage

    Configuration --> Gameplay : onStartGame(names, side)

    state Gameplay {
        [*] --> WaitingForAction
        WaitingForAction --> ActionDone : takeTokens()/ placeToken()/ takeCard()/ placeAnimalCube()
        ActionDone --> TurnComplete : [nessuna azione rimasta]
        TurnComplete --> WaitingForAction : onEndTurn() [model.isGameOver == false]
    }

    Gameplay --> GameOver : onEndTurn() [model.isGameOver == true]

    state GameOver {
        [*] --> ComputingScore : Scorable.computeScore()
        ComputingScore --> ShowingResults : Visualizzazione per categoria
    }

    GameOver --> [*]
```