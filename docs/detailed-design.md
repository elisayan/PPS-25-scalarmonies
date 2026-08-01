# Model
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
    GameModelImpl "1" *-- "1" ScoreCalculator : uses
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
```

## Player
```mermaid
classDiagram
    direction TB

    class Player {
        -id: String
    }

    class PersonalBoard {
        <<trait>>
        +placeToken(token: TerrainToken, c: Coordinate) Option~PersonalBoard~
        +placeAnimalOnCell(c: Coordinate) Option~PersonalBoard~
        +getNorthernNeighbour(c: Coordinate) Option~Cell~
        +getSouthernNeighbour(c: Coordinate) Option~Cell~
        +getNorthEasternNeighbour(c: Coordinate) Option~Cell~
        +getNorthWesternNeighbour(c: Coordinate) Option~Cell~
        +getSouthWesternNeighbour(c: Coordinate) Option~Cell~
        +getSouthEasternNeighbour(c: Coordinate) Option~Cell~
    }

    class PersonalBoardImpl {
        -cells: Map~Coordinate, Cell~
        -side: BoardSide
        -height: Int
        -width: Int
    }

    class BoardSide {
        <<enumeration>>
        SideA
        SideB
    }

    class Coordinate {
        <<trait>>
        +northNeighbour() Coordinate
        +southNeighbour() Coordinate
        +northEsternNeighbour() Coordinate
        +northWesternNeighbour() Coordinate
        +southEsternNeighbour() Coordinate
        +southWesternNeighbour() Coordinate
        +allNeighbours() Set~Coordinate~
        +isNeighbour(other: Coordinate) Boolean
        +rotate60() Coordinate
    }

    class CoordinateImpl {
        -x: Int
        -y: Int
    }

    class Cell {
        -tokens: List~TerrainToken~
        +hasAnimal: Boolean
        +placeToken(token: TerrainToken) Cell
    }

    class TerrainToken {
        
    }

    %% Relationships
    Player "1" *-- "1" PersonalBoard : owns
    PersonalBoard <|.. PersonalBoardImpl : implements
    Coordinate <|.. CoordinateImpl : implements

    PersonalBoardImpl "1" *-- "1" BoardSide : configured by
    PersonalBoardImpl "1" *-- "*" Cell : composed of
    PersonalBoardImpl "1" *-- "*" Coordinate : indexed by
    Cell "1" --o "*" TerrainToken : stores in stack
```
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