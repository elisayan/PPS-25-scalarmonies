package it.unibo.controller

import it.unibo.model.GameModel
import it.unibo.model.personalBoard.Coordinate

trait GameController:
  def currentModel: GameModel
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

    override def onTakeTokens(slot: Int): Unit =
      model = model.takeTokens(slot)
      refresh()

    override def onPlaceToken(coordinate: Coordinate): Unit =
      model = model.placeToken(coordinate)
      refresh()

    override def onEndTurn(): Unit =
      model = model.endTurn()
      refresh()
