package it.unibo.controller

import it.unibo.model.GameModel
import it.unibo.model.TurnState
import it.unibo.model.personalboard.Coordinate

trait GameController:
  def currentModel: GameModel
  def currentPlayerId: Int
  def currentTurnState: TurnState
  def onTakeTokens(slot: Int): Unit
  def onPlaceToken(coordinate: Coordinate): Unit
  def onEndTurn(): Unit

object GameController:

  def apply(model: GameModel, refresh: () => Unit): GameController =
    GameControllerImpl(model, refresh)

  private class GameControllerImpl(
      private var model: GameModel,
      refresh: () => Unit
  ) extends GameController:

    override def currentModel: GameModel = model

    override def currentPlayerId: Int = model.currentPlayer.id

    override def currentTurnState: TurnState = model.turnState

    override def onTakeTokens(slot: Int): Unit =
      model = model.takeTokens(slot)
      refresh()

    override def onPlaceToken(coordinate: Coordinate): Unit =
      model = model.placeToken(coordinate)
      refresh()

    override def onEndTurn(): Unit =
      model = model.endTurn()
      refresh()
