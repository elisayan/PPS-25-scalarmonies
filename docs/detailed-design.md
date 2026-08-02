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

Il **Controller** funge da mediatore tra l'interfaccia grafica e il model di dominio immutabile, disaccoppiando completamente la logica di presentazione dalle regole di gioco.
La sua responsabilità principale è tradurre gli input dell'utente in transizioni di stato del model (`GameModel`), garantendo la coerenza del flusso del turno e gestendo il ciclo di vita dell'applicazione.

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

La comunicazione tra logica di controllo e presentazione si basa su una netta separazione delle responsabilità e sull'iniezione delle dipendenze:
* **Input (View $\to$ Controller):** La componente di presentazione (GameView) riceve l'interfaccia GameController nel proprio costruttore per inoltrare reattivamente gli eventi dell'utente (es. click su una cella tramite onCellClicked). La View non possiede alcuna logica decisionale né conosce le regole del gioco.
* **Output (Controller $\to$ View):** L'implementazione interna del controller mantiene un riferimento alla View attiva, pilotandone il rendering deterministico e ordinando l'aggiornamento grafico (`view.updateState(newModel)`) solo a seguito di una transizione di stato avvenuta con successo.

#### 2. Esecuzione Funzionale delle Azioni e Gestione degli Errori
Poiché GameModel è immutabile e lancia eccezioni (IllegalStateException) nel caso in cui una mossa violi le regole del turno o di impilamento, il controller centralizza l'esecuzione delle mutazioni di stato tramite l'esecuzione della higher-order function `executeAction`:
```scala
private def executeAction(action: GameModel => GameModel)(onSuccess: GameModel => Unit): Unit =
  try
    model = action(model)
    onSuccess(model)
  catch case e: IllegalStateException => handleError(e)
```

Mentre il dominio del gioco è un puro sistema di funzioni senza effetti collaterali, l'istanza privata private var model: GameModel del controller rappresenta l'unico punto di mutabilità controllata dell'intera applicazione.
L'assegnamento model = newModel avviene solo all'interno di executeAction, garantendo che lo stato dell'applicazione non possa mai disallinearsi o subire modifiche concorrenti non tracciate.

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
            Note over Ctrl,View: Il model corrente resta invariato
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

# View

La **View** costituisce il livello di presentazione del sistema, realizzata avvalendosi della libreria grafica **ScalaFX**.
I componenti visivi sono completamente privi di logica decisionale o di regole di dominio, hanno il solo compito di proiettare visivamente lo stato immutabile del gioco (`GameModel`) e di intercettare le interazioni dell'utente, inoltrandole al `GameController`.

#### 1. Architettura Composizionale e Gerarchia dei Componenti
Per evitare strutture monolitiche e facilitare la manutenzione, l'interfaccia di gioco (`GameView`) è organizzata secondo una struttura ad albero fortemente coesa e gerarchica. La vista principale agisce da orchestratore visivo, aggregando macro-aree funzionali indipendenti che, a loro volta, compongono elementi atomici riutilizzabili:

* **`PlayerAreaView`:** Aggrega e organizza l'area individuale di ciascun giocatore, contenendo la plancia personale (`PersonalBoardView`), la mano di carte animale attive e lo storico delle carte completate.
* **`PersonalBoardView` e `CellView`:** Gestiscono il rendering esagonale della griglia di gioco. `PersonalBoardView` mappa le coordinate del model sul piano cartesiano, istanziando le singole `CellView` che disegnano i poligoni esagonali e impilano i segnalini (`TokenView`) e i cubi animale.
* **`CentralBoardView`:** Rappresenta l'offerta pubblica comune, organizzando gli slot dei dischi terreno prelevabili, le carte animale del mercato e il contatore del sacchetto residuo (`Pouch`).
* **`InfoPanelView`:** Fornisce un log di gioco reattivo che traccia cronologicamente lo storico dei turni e le azioni intraprese dai giocatori.

Il seguente diagramma delle classi illustra la gerarchia composizionale della vista di gioco e le relazioni tra i componenti:

```mermaid
classDiagram
    direction TB
    class GameView {
        +updateState(model: GameModel)
        +refresh(model: GameModel, logMessage: String)
    }
    class PlayerAreaView {
        -playerName: String
        +setDisabledArea(disabled: Boolean)
    }
    class PersonalBoardView {
        -cells: List[CellView]
    }
    class CellView {
        -coordinate: Coordinate
        -onCellClicked: Coordinate => Unit
    }
    class CentralBoardView {
        -onCardClicked: Int => Unit
        -onTokenClicked: Int => Unit
    }
    class InfoPanelView {
        +addEntry(playerName, message, playerId)
    }
    class AnimalCardView
    class TokenView

    GameView *-- "1..4" PlayerAreaView : aggrega
    GameView *-- "1" CentralBoardView : aggrega
    GameView *-- "1" InfoPanelView : aggrega
    PlayerAreaView *-- "1" PersonalBoardView : contiene
    PlayerAreaView *-- "*" AnimalCardView : mostra
    PersonalBoardView *-- "*" CellView : compone griglia
    CellView *-- "*" TokenView : impila
    CentralBoardView *-- "*" AnimalCardView : offre
    CentralBoardView *-- "*" TokenView : offre
```

#### 2. Disaccoppiamento Funzionale tramite Callbacks

Per garantire che i singoli componenti grafici della gerarchia (come `CellView`, `TokenView` o `AnimalCardView`) siano riutilizzabili e testabili in isolamento, nessun componente grafico di dettaglio possiede un riferimento diretto al `GameController` o al `GameModel`.
La gestione degli eventi di input (click su un token, selezione di una cella, scelta di una carta) è interamente disaccoppiata tramite il passaggio di funzioni di callback (Higher-Order Functions) iniettate nel costruttore dei componenti:

```scala
case class CellView(
    coordinate: Coordinate,
    var cell: Cell,
    pos: (Double, Double) = (0.0, 0.0),
    onCellClicked: Coordinate => Unit,
    highlighted: Boolean
) extends StackPane:
  onMouseClicked = _ => onCellClicked(coordinate)
```

#### 3. Rendering Deterministico e Proiezione dello Stato

In accordo con l'architettura funzionale del model, la View non conserva uno stato locale mutabile relativo alle regole o all'avanzamento della partita.
L'aggiornamento dell'interfaccia si basa sul principio di proiezione deterministica dello stato:
Quando il controller completa una transizione di turno valida, invoca il metodo `view.updateState(newModel)`, passando la nuova istanza immutabile di GameModel.
La GameView estrae dallo snapshot le informazioni rilevanti e propaga in modo discendente l'aggiornamento a tutte le sotto-viste.
Questo design garantisce che l'interfaccia grafica sia sempre una rappresentazione fedele e coerente dello stato corrente del model.

```mermaid
flowchart LR
subgraph Input [1. Evento Utente / Callback]
direction TB
CV[CellView / TokenView] -->|onCellClicked| GV[GameView]
GV -->|controller.onCellClicked| CTRL[GameController]
end

    subgraph Mutation [2. Transizione di Stato]
        CTRL -->|executeAction| GM[GameModel Immutabile]
        GM -->|newModel| CTRL
    end

    subgraph Output [3. Proiezione]
        direction TB
        CTRL -->|updateState newModel| GV_OUT[GameView]
        GV_OUT -->|propaga stato| PAV[PlayerAreaView]
        GV_OUT -->|propaga stato| CBV[CentralBoardView]
        GV_OUT -->|propaga log| IPV[InfoPanelView]
    end

    Input --> Mutation --> Output
```

#### 4. Isolamento delle Viste nel Ciclo di Vita
L'interfaccia dell'applicazione è disaccoppiata in tre macro-schermate principali, ciascuna responsabile di una specifica fase del ciclo di vita del gioco:
* **HomeView:** Gestisce il bootstrap, la configurazione dei giocatori (nomi, numero di partecipanti) e la scelta del lato della plancia (SideA o SideB), integrando un tutorial interattivo sulle regole.
* **GameView:** Rappresenta l'ambiente del gameplay attivo.
* **ScoreCalculatorView:** Costituisce la schermata di terminazione della partita, calcolando e mostrando la ripartizione del punteggio finale per ogni singola categoria di terreno e per le carte animale in uno stile di riepilogo tabellare.

Le tre schermate sono classi del tutto indipendenti e non comunicano tra loro: la transizione da un contesto all'altro è governata unicamente dallo stage manager del GameController.  