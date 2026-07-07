package it.unibo.controller

import it.unibo.model.GameModel
import it.unibo.model.personalBoard.Coordinate
import it.unibo.view.GameView

trait GameController:
  def currentModel: GameModel
  def onTakeTokens(slot: Int): Unit
  def onPlaceToken(coordinate: Coordinate): Unit
  def onEndTurn(): Unit

object GameController:

  def apply(model: GameModel): GameController =
    GameControllerImpl(model)

  private class GameControllerImpl(private var model: GameModel) extends GameController:

    override def currentModel: GameModel = model

    override def onTakeTokens(slot: Int): Unit =
      //try
        model = model.takeTokens(slot)
      //  view.render(model)
      //catch
        //case e: IllegalStateException => view.showError(e.getMessage)

    override def onPlaceToken(coordinate: Coordinate): Unit =
      model = model.placeToken(coordinate)

    override def onEndTurn(): Unit =
      model = model.endTurn()
