package it.unibo.controller

import it.unibo.model.GameModel
import it.unibo.model.TurnState
import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.Coordinate

trait GameController:
  def startGame(): Unit
  def currentModel: GameModel
  def currentPlayerId: Int
  def currentTurnState: TurnState
  def onTakeTokens(slot: Int): Unit
  def onPlaceToken(coordinate: Coordinate): Unit
  def onEndTurn(): Unit
  def onTakeAnimalCard(slot: Int): Unit
  def onPlaceAnimalCube(card: AnimalCard): Unit
  def onCancelTurn(): Unit

object GameController:

  def apply(
      model: GameModel,
      refresh: (GameModel, String) => Unit
  ): GameController =
    GameControllerImpl(model, refresh)

  private class GameControllerImpl(
      private var model: GameModel,
      refresh: (GameModel, String) => Unit
  ) extends GameController:

    override def startGame(): Unit =
      refresh(model, s"HEADER:${model.currentPlayer.name}")

    override def currentModel: GameModel = model

    override def currentPlayerId: Int = model.currentPlayer.id

    override def currentTurnState: TurnState = model.turnState

    override def onTakeTokens(slot: Int): Unit =
      try
        val playerName = model.currentPlayer.name
        model = model.takeTokens(slot)
        val tokenNames = model.tokensInHand.map(_.toString).mkString(", ")
        refresh(model, s"$playerName prende $tokenNames")
      catch
        case e: IllegalStateException =>
          refresh(model, s"Errore: ${e.getMessage}")

    override def onPlaceToken(coordinate: Coordinate): Unit =
      try
        val playerName = model.currentPlayer.name
        val name = model.tokensInHand.headOption.map(_.toString).getOrElse("?")
        model = model.placeToken(coordinate)
        val level = model.currentPlayer.board.cells(coordinate).getTokens.size
        refresh(model, s"$playerName posiziona $name al livello $level")
      catch
        case e: IllegalStateException =>
          refresh(model, s"Errore: ${e.getMessage}")

    override def onEndTurn(): Unit =
      try
        model = model.endTurn()
        refresh(model, s"HEADER:${model.currentPlayer.name}")
      catch
        case e: IllegalStateException =>
          refresh(model, s"Errore: ${e.getMessage}")

    override def onTakeAnimalCard(slot: Int): Unit =
      try
        val playerName = model.currentPlayer.name
        val cardPosition = model.currentPlayer.activeCards.size + 1
        model = model.takeAnimalCard(slot)
        val cardName = model.currentPlayer.activeCards.last.name
        refresh(
          model,
          s"$playerName prende la carta $cardName e la porta nel suo posto $cardPosition"
        )
      catch
        case e: IllegalStateException =>
          refresh(model, s"Errore: ${e.getMessage}")

    override def onPlaceAnimalCube(card: AnimalCard): Unit =
      try
        val playerName = model.currentPlayer.name
        model = model.placeAnimalCube(card)
        val cardCompleted = !model.currentPlayer.activeCards.contains(card) &&
          !model.currentPlayer.activeCards.exists(_.name == card.name)
        if cardCompleted then
          refresh(model, s"$playerName finisce una carta animale")
          refresh(model, s"$playerName posiziona un cubo")
        else refresh(model, s"$playerName posiziona un cubo")
      catch
        case e: IllegalStateException =>
          refresh(model, s"Errore: ${e.getMessage}")

    override def onCancelTurn(): Unit =
      try
        val playerName = model.currentPlayer.name
        model = model.cancelTurn()
        refresh(model, s"$playerName annulla il turno")
      catch
        case e: IllegalStateException =>
          refresh(model, s"Errore: ${e.getMessage}")
