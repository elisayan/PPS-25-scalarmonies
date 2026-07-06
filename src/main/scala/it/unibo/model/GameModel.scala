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
    val (board, updatePouch) = CentralBoard.empty.fill(pouch)

    GameModelImpl(
      players = players,
      currentPlayerIndex = 0,
      centralBoard = board,
      pouch = updatePouch,
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

    override def isGameOver: Boolean = false

    override def takeTokens(slot: Int): GameModel =
      if turnState != TurnState.WaitingForObligatoryAction then
        throw IllegalStateException("Cannot take tokens in current state")
      centralBoard.take(slot) match
        case None => throw IllegalStateException(s"Slot $slot is empty or invalid")
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
      val newState = if remainingTokens.isEmpty then TurnState.TurnComplete
      else TurnState.ObligatoryActionDone
      this.copy(
        players = updatedPlayers,
        tokensInHand = remainingTokens,
        turnState = newState
      )

    override def endTurn(): GameModel = this