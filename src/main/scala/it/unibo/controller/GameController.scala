package it.unibo.controller

import it.unibo.model.{GameModel, Player, TurnState}
import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.{BoardSide, Coordinate, PersonalBoard}
import it.unibo.model.scorecalculator.ScoreCalculator
import it.unibo.model.token.TerrainToken
import it.unibo.view.GameView
import it.unibo.view.homepage.HomeView
import it.unibo.view.scorecalculator.ScoreCalculatorView
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene

trait GameController:
  def startGame(): Unit
  def currentModel: GameModel
  def currentPlayerId: Int
  def currentTurnState: TurnState
  def onTakeTokens(slot: Int): Unit
  def onSelectToken(token: TerrainToken): Unit
  def onPlaceToken(coordinate: Coordinate): Unit
  def onEndTurn(): Unit
  def onTakeAnimalCard(slot: Int): Unit
  def onPlaceAnimalCube(card: AnimalCard): Unit
  def onCancelTurn(): Unit
  def start(): Unit
  def onStartGame(names: List[String], side: BoardSide): Unit
  def onEndGame(players: List[Player]): Unit

object GameController:

  def apply(model: GameModel, refresh: (GameModel, String) => Unit, stage: JFXApp3.PrimaryStage): GameController =
    GameControllerImpl(model, refresh, stage)

  private class GameControllerImpl(
      private var model: GameModel,
      refresh: (GameModel, String) => Unit,
      stage: JFXApp3.PrimaryStage
  ) extends GameController:

    private val view = GameView(this)

    private def handleError(e: IllegalStateException): Unit =
      refresh(model, s"Errore: ${e.getMessage}")
      view.showTemporaryError(s"Mossa illegale: ${e.getMessage}")

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
        view.updateState(model)
      catch
        case e: IllegalStateException => handleError(e)

    override def onSelectToken(token: TerrainToken): Unit =
      try
        model = model.selectToken(token)
        refresh(
          model,
          s"${model.currentPlayer.name} seleziona $token"
        )
        view.updateState(model)

      catch
        case e: IllegalStateException => handleError(e)

    override def onPlaceToken(coordinate: Coordinate): Unit =
      try
        val playerName = model.currentPlayer.name
        val name = model.tokensInHand.headOption.map(_.toString).getOrElse("?")
        model = model.placeToken(coordinate)
        val level = model.currentPlayer.board.cells(coordinate).getTokens.size
        refresh(model, s"$playerName posiziona $name al livello $level")
        view.updateState(model)
      catch
        case e: IllegalStateException => handleError(e)

    override def onEndTurn(): Unit =
      try
        model = model.endTurn()
        if model.isGameOver then
          onEndGame(model.getPlayers)
        else
          refresh(model, s"HEADER:${model.currentPlayer.name}")
          view.updateState(model)
      catch
        case e: IllegalStateException => handleError(e)

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
        view.updateState(model)
      catch
        case e: IllegalStateException => handleError(e)

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
        view.updateState(model)
      catch
        case e: IllegalStateException => handleError(e)

    override def onCancelTurn(): Unit =
      try
        val playerName = model.currentPlayer.name
        model = model.cancelTurn()
        refresh(model, s"$playerName annulla il turno")
        view.updateState(model)
      catch
        case e: IllegalStateException => handleError(e)

    override def start(): Unit =
      val homeView = HomeView(onStartGame)
      stage.scene = new Scene(homeView)
      stage.fullScreen = true

    override def onStartGame(names: List[String], side: BoardSide): Unit =
      val players = names.zipWithIndex.map((name, index) => Player(index, name, PersonalBoard(side)))
      model = GameModel(players)
      view.updateState(model)
      stage.scene.value.setRoot(view)
      stage.fullScreen = true

    override def onEndGame(players: List[Player]): Unit =
      val calculator = ScoreCalculator()
      val endGameView = ScoreCalculatorView(players, calculator)
      stage.scene.value.setRoot(endGameView)
      stage.fullScreen = true





