package it.unibo.model

import it.unibo.model.card.{AnimalCard, HabitatMatcher}
import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.personalBoard.Coordinate
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.{TerrainToken, TokenValidator}

trait GameModel:
  def currentPlayer: Player

  def turnState: TurnState

  def tokensInHand: List[TerrainToken]

  def isGameOver: Boolean

  def takeTokens(slot: Int): GameModel

  def placeToken(coordinate: Coordinate): GameModel

  def endTurn(): GameModel

  def highlightedCells(token: TerrainToken): List[Coordinate]

  def takeAnimalCard(card: AnimalCard): GameModel

  def placeAnimalCube(card: AnimalCard): GameModel

object GameModel:
  def apply(players: List[Player]): GameModel =
    val pouch = Pouch.initialPouch()
    val (board, updatePouch) = CentralBoard.empty.fill(pouch)

    GameModelImpl(
      players = players,
      currentPlayerIndex = 0,
      centralBoard = board,
      pouch = updatePouch,
      turnState = TurnState.WaitingForObligatoryAction
    )

  // solo per test: permette di forzare stato specifico
  def apply(players: List[Player], forceEmptyPouch: Boolean): GameModel =
    val pouch = if forceEmptyPouch then Pouch(List()) else Pouch.initialPouch()
    val (board, updatedPouch) = CentralBoard.empty.fill(pouch)
    GameModelImpl(
      players = players,
      currentPlayerIndex = 0,
      centralBoard = board,
      pouch = updatedPouch,
      turnState = TurnState.WaitingForObligatoryAction
    )

  private case class GameModelImpl(
      players: List[Player],
      currentPlayerIndex: Int,
      centralBoard: CentralBoard,
      pouch: Pouch,
      override val turnState: TurnState,
      override val tokensInHand: List[TerrainToken] = List()
  ) extends GameModel:

    private val MaxAnimalCards = 4

    override def currentPlayer: Player = players(currentPlayerIndex)

    override def isGameOver: Boolean = pouch.isEmpty || hasPlayerAlmostFullBoard

    override def takeTokens(slot: Int): GameModel =
      if turnState != TurnState.WaitingForObligatoryAction then
        throw IllegalStateException("Cannot take tokens in current state")
      centralBoard.take(slot) match
        case None =>
          throw IllegalStateException(s"Slot $slot is empty or invalid")
        case Some((tokens, updatedBoard)) =>
          this.copy(
            centralBoard = updatedBoard,
            tokensInHand = tokens,
            turnState = TurnState.ObligatoryActionDone
          )

    override def placeToken(coordinate: Coordinate): GameModel =
      if turnState != TurnState.ObligatoryActionDone then
        throw IllegalStateException("Cannot place token in current state")
      val token = tokensInHand.head
      val updatedBoard = currentPlayer.board.placeToken(token, coordinate)
      val updatedPlayer = currentPlayer.copy(board = updatedBoard)
      val updatedPlayers = players.updated(currentPlayerIndex, updatedPlayer)
      val remainingTokens = tokensInHand.tail
      val newState =
        if remainingTokens.isEmpty then TurnState.TurnComplete
        else TurnState.ObligatoryActionDone
      this.copy(
        players = updatedPlayers,
        tokensInHand = remainingTokens,
        turnState = newState
      )

    override def endTurn(): GameModel =
      if turnState != TurnState.TurnComplete then
        throw IllegalStateException("Cannot end turn before placing all tokens")
      val nextIndex = (currentPlayerIndex + 1) % players.size
      val (refilledBoard, updatedPouch) = centralBoard.fill(pouch)
      this.copy(
        currentPlayerIndex = nextIndex,
        centralBoard = refilledBoard,
        pouch = updatedPouch,
        tokensInHand = List(),
        turnState = TurnState.WaitingForObligatoryAction
      )

    override def highlightedCells(token: TerrainToken): List[Coordinate] =
      val physicallyValid = TokenValidator.validPositions(token, currentPlayer.board)
      val blockedCells: Set[Coordinate] = currentPlayer.activeCards
        .filter(_.placedCubes > 0)
        .flatMap(card => HabitatMatcher.findMatches(currentPlayer.board, card.habitat))
        .flatMap(m => m.involvedCells + m.origin)
        .toSet
      physicallyValid.filterNot(blockedCells.contains)

    override def takeAnimalCard(card: AnimalCard): GameModel =
      if turnState == TurnState.TurnComplete then
        throw IllegalStateException("Cannot take animal card after turn is complete")
      if currentPlayer.activeCards.size >= MaxAnimalCards then
        throw IllegalStateException("Player already has maximum animal cards")

      val updatedPlayer = currentPlayer.copy(activeCards = currentPlayer.activeCards :+ card)
      this.copy(players = players.updated(currentPlayerIndex, updatedPlayer))

    override def placeAnimalCube(card: AnimalCard): GameModel =
      if turnState == TurnState.TurnComplete then
        throw IllegalStateException("Cannot place animal cube after turn is complete")
      val cardIndex = currentPlayer.activeCards.indexOf(card)
      if cardIndex == -1 then
        throw IllegalStateException("Card not found in player's active cards")
      card.placeCube match
        case None => throw IllegalStateException("No cubes remaining on this card")
        case Some(updatedCard) =>
          val updatedCards = currentPlayer.activeCards.updated(cardIndex, updatedCard)
          val updatedPlayer = currentPlayer.copy(activeCards = updatedCards)
          this.copy(players = players.updated(currentPlayerIndex, updatedPlayer))
          
    private def hasPlayerAlmostFullBoard: Boolean =
      players.exists { player =>
        val emptyCells = player.board.cells.values.count(!_.hasTokens)
        emptyCells <= 2
      }
