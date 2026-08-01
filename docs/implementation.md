# Implementation

## Oluwatobi Daniel Ariyo
### Cell
La cella rappresenta l'elemento base e indivisibile che compone la PersonalBoard. 
Il suo scopo principale è quello di fungere da contenitore fisico per la costruzione del paesaggio, ospitando al suo interno sia la pila di TerrainToken sia l'eventuale cubo animale piazzato dal giocatore.
Per garantire la massima integrità del modello di dominio ed evitare mutazioni indesiderate, la classe Cell è stata progettata come una case class immutabile. 
Ogni operazione di modifica della cella restituisce una nuova cella aggiornata, favorendo un approccio funzionale.
```scala
case class Cell(
                 private val tokens: List[TerrainToken] = List(),
                 hasAnimal: Boolean = false
               )
```

### Coordinate

La modellazione della plancia di gioco (PersonalBoard) richiede la gestione di una griglia a tassellatura esagonale.
Il trait Coordinate stabilisce il contratto per la rappresentazione di posizioni bidimensionali $(x, y)$, mettendo a disposizione le operazioni algebriche e le primitive spaziali per la navigazione sulla griglia.
```scala
trait Coordinate:
  def x: Int
  def y: Int
  def +(other: Coordinate): Coordinate
  def -(other: Coordinate): Coordinate
  def *(other: Coordinate): Coordinate
  def rotate60: Coordinate
```
Per nascondere i dettagli di basso livello e separare l'interfaccia dall'implementazione, l'interfaccia pubblica è definita dal trait Coordinate, mentre la struttura concreta è racchiusa all'interno della case class privata CoordinateImpl.
La creazione delle istanze è centralizzata nell'oggetto companion Coordinate tramite il factory method apply:
```scala
object Coordinate:
  private case class CoordinateImpl(override val x: Int, override val y: Int)
    extends Coordinate
```
L'orientamento degli esagoni prevede sei direzioni di adiacenza (Nord, Sud, Nord-Est, Nord-Ovest, Sud-Est, Sud-Ovest). 
Il trait fornisce direttamente i metodi con implementazione di default per calcolare i vicini tramite offset definiti:
```scala
def northNeighbour: Coordinate = Coordinate(x, y + 2)
def southNeighbour: Coordinate = Coordinate(x, y - 2)
def northEasternNeighbour: Coordinate = Coordinate(x + 2, y + 1)
def northWesternNeighbour: Coordinate = Coordinate(x - 2, y + 1)
def southEasternNeighbour: Coordinate = Coordinate(x + 2, y - 1)
def southWesternNeighbour: Coordinate = Coordinate(x - 2, y - 1)
```
Il metodo allNeighbours aggrega le sei direzioni in un Set[Coordinate], consentendo di implementare il controllo di adiacenza isNeighbour in modo snello e dichiarativo:
```scala
override def allNeighbours: Set[Coordinate] =
  Set(
    northNeighbour,
    southNeighbour,
    northEasternNeighbour,
    northWesternNeighbour,
    southEasternNeighbour,
    southWesternNeighbour
  )

override def isNeighbour(other: Coordinate): Boolean =
  allNeighbours.contains(other)
```
### Personal Board
La PersonalBoard rappresenta la plancia di gioco individuale di ciascun giocatore.
Essa è formata da una serie di celle esagonali, `Cell` con ognuna una propria coordinata, `Coordinate`.
Il regolamento di gioco prevede due differenti configurazioni di plancia (SideA e SideB), caratterizzate da dimensioni e numero di celle differenti. 
Questa variabilità è stata modellata tramite l'enumerazione BoardSide
```scala
enum BoardSide:
  case SideA
  case SideB
```
La creazione della plancia è incapsulata nell'oggetto companion PersonalBoard, che agisce da Factory. La funzione privata generateHexGrid calcola dinamicamente le coordinate valide per la tassellatura esagonale:
```scala
private def generateHexGrid(
                             widthBound: Int,
                             heightBound: Int
                           ): Map[Coordinate, Cell] =
val validCoordinates = for
  x <- -widthBound to widthBound if x % 2 == 0
  y <- -heightBound to heightBound
  if (x / 2).abs % 2 == y.abs % 2
yield Coordinate(x, y)
validCoordinates.map(c => c -> Cell(List())).toMap
```
Attraverso una for-comprehension, vengono filtrate unicamente le coppie cartesiane (x, y) che soddisfano i vincoli di parità del sistema esagonale.
La struttura concreta della plancia è definita dalla case class privata PersonalBoardImpl, non accessibile dall'esterno del modulo.
Tutte le operazioni di interrogazione e modifica dello stato applicano la gestione difensiva tramite il tipo Option

I metodi placeToken e placeAnimalOnCell effettuano le mutazioni senza alterare la plancia corrente,
ma restituendo un Option[PersonalBoard] contenente la copia aggiornata
```scala
override def placeToken(
                         token: TerrainToken,
                         c: Coordinate
                       ): Option[PersonalBoard] =
  if isValid(c) then
    cells.get(c) match
      case Some(currentCell) =>
        val updatedCell = currentCell.placeToken(token)
        val updatedCells = cells + (c -> updatedCell)
        Some(copy(cells = updatedCells))
      case None => None
  else None
```
I metodi come getNorthernNeighbour, getSouthEasternNeighbour, ecc., verificano preventivamente la validità della coordinata adiacente tramite il predicato isValid(c), restituendo None in caso di fuori bordo:
```scala
override def getNorthernNeighbour(c: Coordinate): Option[Cell] =
  if isValid(c.northNeighbour) then cells.get(c.northNeighbour) else None
```




## Filippo Ferretti

## Elisa Yan
### Terrain Token
I `TerrainToken` costituiscono gli elementi principali utilizzati dai giocatori per costruire il proprio paesaggio.
La loro modellazione è stata realizzata tramite due `enum`:
- `TokenColor`, che rappresenta i colori disponibili;
- `TerrainToken`, che definisce le differenti tipologie di terreno
```scala
enum TokenColor:
  case Grey, Brown, Green, Yellow, Blue, Red

enum TerrainToken:
  case Water, Field, Mountain, Ground, Forest, Building
```
L’utilizzo di un’enumerazione permette di rappresentare un insieme chiuso di valori, evitando la creazione di tipologie di terreno non previste dal dominio.

L’oggetto `TerrainToken` contiene inoltre il metodo `colorOf`, che associa ogni terreno al colore utilizzato per la sua rappresentazione grafica:
```scala
def colorOf(token: TerrainToken): TokenColor = token match
  case TerrainToken.Water    => TokenColor.Blue
  case TerrainToken.Mountain => TokenColor.Grey
  case TerrainToken.Forest   => TokenColor.Green
  case TerrainToken.Field    => TokenColor.Yellow
  case TerrainToken.Building => TokenColor.Red
  case TerrainToken.Ground   => TokenColor.Brown
```
La corrispondenza viene definita tramite pattern matching, rendendo esplicita e type-safe l’associazione tra ogni token e il relativo colore.

### Terrain Token Placement
L’implementazione delle regole di piazzamento dei `TerrainToken` è stata sviluppata separando la validazione dalla logica di gioco. 
La classe `TokenValidator` contiene esclusivamente le regole di piazzamento, mentre `GameModel` coordina il flusso del turno.

La validazione sfrutta il _pattern matching_ sugli `enum`, in cui ogni tipo di token definisce in maniera dichiarativa le proprie regole di sovrapposizione,
rendendo l'implementazione estendibile.

#### Higher-Order Function (HOF)
Per individuare le celle valide è stata inoltre introdotta una _Higher-Order Function_ (HOF) che astrae il meccanismo di ricerca delle coordinate, 
ricevendo come parametro una funzione predicato (`Cell => Boolean`) che descrive il criterio di selezione.
```scala
private val findCoordinates = (board: PersonalBoard, predicate: Cell => Boolean) => 
  board.cells.collect{
    case (coordinate, cell) if predicate(cell) => coordinate
  }.toList
```
La funzione `validPositions` delega la ricerca delle coordinate alla HOF, limitandosi a fornire il predicato di validazione. La `PersonalBoard` necessaria alla ricerca viene invece ottenuta come parametro contestuale tramite `using`, evitando di doverla passare esplicitamente a ogni invocazione.
```scala
def validPositions(token: TerrainToken)(using board: PersonalBoard): List[Coordinate] =
    findCoordinates(board, canPlace(token, _))
```

#### Contextual Abstractions: `using` e `given`
Per evitare di propagare esplicitamente la plancia del giocatore lungo tutta la catena di chiamate, l’implementazione sfrutta le _Contextual Abstractions_ tramite using e given.
Il metodo `validPositions` dichiara infatti la dipendenza da una `PersonalBoard` come parametro contestuale:
```scala
def validPositions(token: TerrainToken)(using
    board: PersonalBoard
): List[Coordinate] =
  findCoordinates(board, canPlace(token, _))
```
Nel `GameModel` la plancia del giocatore corrente viene resa disponibile come valore contestuale:
```scala
private given currentBoard: PersonalBoard = currentPlayer.board
```
Di conseguenza, l’invocazione del metodo non richiede più il passaggio esplicito della plancia, scala risolve automaticamente il parametro contestuale utilizzando il `given` disponibile nello scope.
```scala
TokenValidator.validPositions(token)
```

### Turn Management
La gestione del turno è modellata attraverso l'enumerazione `TurnState`, che sfrutta gli _Algebraic Data Types_ per rappresentare l'insieme finito degli stati che un turno può assumere.
In particolare, il turno può trovarsi in uno dei tre stati `WaitingForAction`, `ActionDone` oppure `TurnComplete`. 
Questa rappresentazione rende esplicite le possibili fasi del turno e impedisce la presenza di stati non validi.
```scala
enum TurnState:
  case WaitingForAction
  case ActionDone
  case TurnComplete
```
Le transizioni tra gli stati sono gestite direttamente dal `GameModel`, che rappresenta lo stato complessivo della partita.
Prima di eseguire un'operazione, il model verifica che essa sia consentita nello stato corrente; in caso contrario viene lanciata un'eccezione.
Ogni operazione valida restituisce una nuova istanza aggiornata del model attraverso il metodo `copy`, preservando l'immutabilità dello stato di gioco.

Lo stato `WaitingForAction` rappresenta l’inizio del turno. In questa fase il giocatore può prendere una carta animale, 
purché non ne abbia già presa una durante lo stesso turno e non abbia raggiunto il numero massimo di carte attive. 
Può inoltre scegliere uno degli slot di `TerrainToken` disponibili sulla plancia centrale.
Il prelievo dei token viene eseguito tramite il metodo `takeTokens` ed è consentito esclusivamente nello stato `WaitingForAction`:

Nello stato `ActionDone` il giocatore può selezionare e posizionare i token appena ottenuti sulla propria `PersonalBoard`. 
Se non ha ancora preso una carta animale durante il turno corrente, può ancora effettuare tale operazione. 
Dopo ogni piazzamento il model aggiorna il numero di token rimanenti nella mano del giocatore; quando tutti i token sono stati collocati, il turno passa automaticamente allo stato `TurnComplete`.

Infine, nello stato `TurnComplete`, il giocatore non può più eseguire ulteriori azioni e può solamente terminare il turno. 
L’operazione di fine turno aggiorna la plancia centrale, passa il controllo al giocatore successivo e riporta il `TurnState` a `WaitingForAction`, avviando un nuovo ciclo.

### Player
Il giocatore è rappresentato dalla `case class Player`, che raccoglie tutte le informazioni necessarie per descrivere lo stato di un partecipante durante la partita.
```scala
case class Player(
    id: Int,
    name: String,
    board: PersonalBoard,
    activeCards: List[AnimalCard] = List.empty,
    completedCards: List[AnimalCard] = List.empty
)
```
Ogni giocatore mantiene:
- la propria `PersonalBoard`;
- l'insieme delle carte animale ancora in gioco (`activeCards`);
- le carte completate (`completedCards`).
La struttura risulta una rappresentazione compatta dello stato del giocatore, lasciando la logica di gioco al `GameModel`.

#### Extension Methods
Per evitare di inserire nella `case class` metodi legati a specifiche regole del gioco, è stato utilizzato un _extension method_.
```scala
object Player:

  extension (player: Player)

    def hasReachedAnimalCardLimit(limit: Int): Boolean =
      player.activeCards.size >= limit
```
L’_extension method_ aggiunge un nuovo comportamento al tipo `Player` senza modificarne la definizione originale, mantenendo separata la rappresentazione dei dati dalla logica applicativa.
Nel `GameModel` il controllo diventa quindi:
```scala
if currentPlayer.hasReachedAnimalCardLimit(MaxAnimalCards) then
  throw IllegalStateException(
    "Player ha già il numero massimo di carte animale"
  )
```
Questa soluzione rende il codice riutilizzabile in altre parti dell'applicazione.

### Game Controller

