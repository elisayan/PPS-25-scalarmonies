package it.unibo.model

import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.personalBoard.Coordinate
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken

trait GameModel:
  def currentPlayer: Player

  def turnState: TurnState

  def tokensInHand: List[TerrainToken]

  def isGameOver: Boolean

  def takeTokens(slot: Int): GameModel

  def placeToken(coordinate: Coordinate): GameModel

  def endTurn(): GameModel

object GameModel:
  def apply(players: List[Player]): GameModel =
    val pouch = Pouch.initialPouch()
    // TODO Modified central board implementation by adding animal cards
    val (board, updatePouch, deck) = CentralBoard.empty.fill(pouch, List.empty)

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
    val (board, updatedPouch, deck) = CentralBoard.empty.fill(pouch, List.empty)
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

    override def currentPlayer: Player = players(currentPlayerIndex)

    override def isGameOver: Boolean = pouch.isEmpty || hasPlayerAlmostFullBoard

    override def takeTokens(slot: Int): GameModel =
      if turnState != TurnState.WaitingForObligatoryAction then
        throw IllegalStateException("Cannot takeTokens tokens in current state")
      centralBoard.takeTokens(slot) match
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
      val (refilledBoard, updatedPouch, deck) =
        centralBoard.fill(pouch, List.empty)
      this.copy(
        currentPlayerIndex = nextIndex,
        centralBoard = refilledBoard,
        pouch = updatedPouch,
        tokensInHand = List(),
        turnState = TurnState.WaitingForObligatoryAction
      )

    private def hasPlayerAlmostFullBoard: Boolean =
      players.exists { player =>
        val emptyCells = player.board.cells.values.count(!_.hasTokens)
        emptyCells <= 2
      }
