package it.unibo.model

import it.unibo.model.TurnState.ActionDone
import it.unibo.model.card.AnimalCard
import it.unibo.model.card.AnimalDeckFactory
import it.unibo.model.card.HabitatMatcher
import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken
import it.unibo.model.token.TokenValidator

trait GameModel:
  def startingPlayer: Player
  def currentPlayer: Player
  def turnState: TurnState
  def tokensInHand: List[TerrainToken]
  def pouchSize: Int
  def isGameOver: Boolean
  def takeTokens(slot: Int): GameModel
  def selectedToken: Option[TerrainToken]
  def selectToken(token: TerrainToken): GameModel
  def takeAnimalCard(slot: Int): GameModel
  def placeToken(coordinate: Coordinate): GameModel
  def endTurn(): GameModel
  def highlightedCells(token: TerrainToken): List[Coordinate]
  def selectedAnimalCard: Option[AnimalCard]
  def selectAnimalCard(card: AnimalCard): GameModel
  def highlightedAnimalCells(card: AnimalCard): List[Coordinate]
  def placeAnimalCube(coordinate: Coordinate): GameModel
  def cancelTurn(): GameModel
  def getPlayers: List[Player]
  def centralBoard: CentralBoard
  def availableActionsMessage: String

object GameModel:
  def apply(
      players: List[Player],
      deck: List[AnimalCard] = AnimalDeckFactory.createShuffledDeck()
  ): GameModel =
    val pouch = Pouch.initialPouch()
    val (board, updatedPouch, updatedDeck) =
      CentralBoard.empty.fill(pouch, deck)
    GameModelImpl(
      players = players,
      currentPlayerIndex = 0,
      centralBoard = board,
      pouch = updatedPouch,
      deck = updatedDeck,
      turnState = TurnState.WaitingForAction
    )

  def apply(players: List[Player], forceEmptyPouch: Boolean): GameModel =
    val pouch = if forceEmptyPouch then Pouch(List()) else Pouch.initialPouch()
    val (board, updatedPouch, updatedDeck) =
      CentralBoard.empty.fill(pouch, List())
    GameModelImpl(
      players = players,
      currentPlayerIndex = 0,
      centralBoard = board,
      pouch = updatedPouch,
      deck = updatedDeck,
      turnState = TurnState.WaitingForAction
    )

  private case class GameModelImpl(
      players: List[Player],
      currentPlayerIndex: Int,
      centralBoard: CentralBoard,
      pouch: Pouch,
      deck: List[AnimalCard],
      turnSnapshot: Option[GameModelImpl] = None,
      override val turnState: TurnState,
      override val tokensInHand: List[TerrainToken] = List(),
      override val selectedToken: Option[TerrainToken] = None,
      override val selectedAnimalCard: Option[AnimalCard] = None,
      hasTakenCardThisTurn: Boolean = false,
      isLastRound: Boolean = false
  ) extends GameModel:

    private val MaxAnimalCards = 4

    override def startingPlayer: Player = players.head

    override def currentPlayer: Player = players(currentPlayerIndex)

    override def pouchSize: Int = pouch.size

    override def isGameOver: Boolean =
      isLastRound && currentPlayerIndex == 0

    override def takeTokens(slot: Int): GameModel =
      if turnState != TurnState.WaitingForAction then
        throw IllegalStateException(
          "Non puoi scegliere i tokens nello stato corrente"
        )

      centralBoard.takeTokens(slot) match
        case None =>
          throw IllegalStateException(s"Slot $slot è vuoto o invalido")
        case Some((tokens, updatedBoard)) =>
          this.copy(
            centralBoard = updatedBoard,
            tokensInHand = tokens,
            turnState = TurnState.ActionDone,
            turnSnapshot = turnSnapshot.orElse(Some(this))
          )

    override def selectToken(token: TerrainToken): GameModel =
      if !tokensInHand.contains(token) then
        throw IllegalStateException("Token non disponibile")
      this.copy(
        selectedToken = Some(token),
        selectedAnimalCard = None
      )

    override def takeAnimalCard(slot: Int): GameModel =
      if hasTakenCardThisTurn then
        throw IllegalStateException(
          "Player ha gia scelto una carta Animale questo turno"
        )
      if currentPlayer.activeCards.size >= MaxAnimalCards then
        throw IllegalStateException(
          "Player ha già il numero di carte Animale massimo"
        )

      centralBoard.takeCard(slot) match
        case None =>
          throw IllegalStateException(
            s"Slot $slot della carta è vuoto o invalido"
          )
        case Some((card, updatedBoard)) =>
          val updatedPlayer = currentPlayer.copy(
            activeCards = currentPlayer.activeCards :+ card
          )
          this.copy(
            centralBoard = updatedBoard,
            players = players.updated(currentPlayerIndex, updatedPlayer),
            turnSnapshot = turnSnapshot.orElse(Some(this)),
            hasTakenCardThisTurn = true
          )

    override def placeToken(coordinate: Coordinate): GameModel =
      if turnState != TurnState.ActionDone then
        throw IllegalStateException(
          "Non puoi posizionare token nello stato corrente"
        )

      val token = selectedToken.getOrElse(
        throw IllegalStateException("Prima di questa azione scegliere un token")
      )

      if !highlightedCells(token).contains(coordinate) then
        throw IllegalStateException("Piazzamento token invalido")

      val updatedBoard = currentPlayer.board.placeToken(token, coordinate)
      val updatedPlayer = currentPlayer.copy(board = updatedBoard.get)
      val updatedPlayers = players.updated(currentPlayerIndex, updatedPlayer)
      val index = tokensInHand.indexOf(token)

      val remainingTokens = tokensInHand.patch(index, Nil, 1)

      val newState =
        if remainingTokens.isEmpty then TurnState.TurnComplete
        else TurnState.ActionDone

      this.copy(
        players = updatedPlayers,
        tokensInHand = remainingTokens,
        selectedToken = None,
        turnState = newState
      )

    override def endTurn(): GameModel =
      if turnState != TurnState.TurnComplete then
        throw IllegalStateException(
          "Non puoi terminare il turno prima di aver piazzato tutti i tokens"
        )

      val nextIndex = (currentPlayerIndex + 1) % players.size
      val (refilledBoard, updatedPouch, updatedDeck) =
        centralBoard.fill(pouch, deck)

      val endConditionTriggered =
        updatedPouch.isEmpty || hasPlayerAlmostFullBoard
      val nextIsLastRound = isLastRound || endConditionTriggered

      this.copy(
        currentPlayerIndex = nextIndex,
        centralBoard = refilledBoard,
        pouch = updatedPouch,
        deck = updatedDeck,
        tokensInHand = List(),
        turnState = TurnState.WaitingForAction,
        turnSnapshot = None,
        hasTakenCardThisTurn = false,
        isLastRound = nextIsLastRound
      )

    override def highlightedCells(token: TerrainToken): List[Coordinate] =
      val physicallyValid =
        TokenValidator.validPositions(token, currentPlayer.board)

      val blockedCells: Set[Coordinate] = currentPlayer.activeCards
        .filter(_.placedCubes > 0)
        .flatMap(card =>
          HabitatMatcher.findMatches(currentPlayer.board, card.habitat)
        )
        .flatMap(m => m.involvedCells + m.origin)
        .toSet

      physicallyValid.filterNot(blockedCells.contains)

    override def selectAnimalCard(card: AnimalCard): GameModel =
      if !currentPlayer.activeCards.contains(card) then
        throw IllegalStateException("Carta non posseduta")
      if highlightedAnimalCells(card).isEmpty then
        throw IllegalStateException(
          "Nessun habitat completato per questa carta"
        )
      this.copy(selectedAnimalCard = Some(card), selectedToken = None)

    override def highlightedAnimalCells(card: AnimalCard): List[Coordinate] =
      HabitatMatcher
        .findMatches(currentPlayer.board, card.habitat)
        .map(_.origin)
        .toList

    override def placeAnimalCube(coordinate: Coordinate): GameModel =
      val card = selectedAnimalCard.getOrElse(
        throw IllegalStateException("Nessuna carta selezionata")
      )

      if !highlightedAnimalCells(card).contains(coordinate) then
        throw IllegalStateException("Posizione non valida per questo habitat")

      val updatedBoard = currentPlayer.board.placeAnimalOnCell(coordinate).get
      val updatedCard = card.placeCube.get

      val (newActive, newCompleted) =
        if updatedCard.placedCubes == updatedCard.maxCubes then
          (
            currentPlayer.activeCards.filterNot(_ == card),
            currentPlayer.completedCards :+ updatedCard
          )
        else
          (
            currentPlayer.activeCards.map(c =>
              if c == card then updatedCard else c
            ),
            currentPlayer.completedCards
          )

      val updatedPlayer = currentPlayer.copy(
        board = updatedBoard,
        activeCards = newActive,
        completedCards = newCompleted
      )

      this.copy(
        players = players.updated(currentPlayerIndex, updatedPlayer),
        selectedAnimalCard = None,
        turnSnapshot = turnSnapshot.orElse(Some(this))
      )

    override def cancelTurn(): GameModel =
      turnSnapshot match
        case Some(snapshot) => snapshot.copy(turnSnapshot = None)
        case None           => this

    override def getPlayers: List[Player] =
      players

    override def availableActionsMessage: String =
      val actions = List(
        Option.when(turnState == TurnState.WaitingForAction)("scegli tokens"),
        Option.when(
          !hasTakenCardThisTurn && currentPlayer.activeCards.size < MaxAnimalCards
        )("scegli una carta animale"),
        Option.when(
          currentPlayer.activeCards
            .exists(c => c.placedCubes < c.maxCubes)
        )("posiziona cubo animale"),
        Option.when(turnState == ActionDone && tokensInHand.nonEmpty)(
          "posiziona token"
        ),
        Option.when(
          turnState == TurnState.TurnComplete && hasTakenCardThisTurn
        )("Nessuna azione possibile rimasta")
      ).flatten

      if actions.isEmpty then
        s"${currentPlayer.name} non ha azioni disponibili (termina il turno)"
      else s"${currentPlayer.name} " + actions.mkString(" oppure ")

    private def hasPlayerAlmostFullBoard: Boolean =
      players.exists { player =>
        val emptyCells = player.board.cells.values.count(!_.hasTokens)
        emptyCells <= 2
      }
