package it.unibo.controller

import it.unibo.model.GameModel
import it.unibo.model.Player
import it.unibo.model.TurnState
import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.BoardSide
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.token.TerrainToken
import it.unibo.view.GameView
import it.unibo.view.homepage.HomeView
import it.unibo.view.scorecalculator.ScoreCalculatorView
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene

/** Defines the controller responsible for handling user interactions. */
trait GameController:

  /** @return the current game model. */
  def currentModel: GameModel

  /** @return the identifier of the current player. */
  def currentPlayerId: Int

  /** @return the current turn state. */
  def currentTurnState: TurnState

  /** Handles the selection of a terrain token slot.
    * @param slot
    *   the selected slot.
    */
  def onTakeTokens(slot: Int): Unit

  /** Handles the selection of a terrain token.
    * @param token
    *   the selected terrain token.
    */
  def onSelectToken(token: TerrainToken): Unit

  /** Handles the placement of the selected terrain token.
    * @param coordinate
    *   the target coordinate.
    */
  def onPlaceToken(coordinate: Coordinate): Unit

  /** Handles the end of the current turn. */
  def onEndTurn(): Unit

  /** Handles the selection of an animal card.
    * @param slot
    *   the selected slot.
    */
  def onTakeAnimalCard(slot: Int): Unit

  /** Handles the selection of an active animal card.
    * @param card
    *   the selected animal card.
    */
  def onSelectActiveCard(card: AnimalCard): Unit

  /** Handles a click on a board cell.
    * @param coordinate
    *   the selected coordinate.
    */
  def onCellClicked(coordinate: Coordinate): Unit

  /** Handles the cancellation of the current turn. */
  def onCancelTurn(): Unit

  /** Starts the application. */
  def start(): Unit

  /** Starts a new game.
    * @param names
    *   the player names.
    * @param side
    *   the selected board side.
    */
  def onStartGame(names: List[String], side: BoardSide): Unit

  /** Handles the end of the game.
    * @param players
    *   the players with their final game state.
    */
  def onEndGame(players: List[Player]): Unit

object GameController:

  /** Creates a new game controller.
    * @param model
    *   the initial game model.
    * @param stage
    *   the application's primary stage.
    * @return
    *   a new game controller.
    */
  def apply(model: GameModel, stage: PrimaryStage): GameController =
    GameControllerImpl(model, stage)

  private class GameControllerImpl(
      private var model: GameModel,
      stage: JFXApp3.PrimaryStage
  ) extends GameController:

    private val view = GameView(this)

    private def refreshView(message: String): Unit =
      view.refresh(model, message)

    private def handleError(e: IllegalStateException): Unit =
      refreshView(s"Errore: ${e.getMessage}")
      view.showTemporaryError(s"Mossa illegale: ${e.getMessage}")

    override def currentModel: GameModel = model

    override def currentPlayerId: Int = model.currentPlayer.id

    override def currentTurnState: TurnState = model.turnState

    override def onTakeTokens(slot: Int): Unit =
      try
        val playerName = model.currentPlayer.name
        model = model.takeTokens(slot)
        val tokenNames = model.tokensInHand.map(_.toString).mkString(", ")
        refreshView(s"$playerName prende $tokenNames")
        view.updateState(model)
      catch case e: IllegalStateException => handleError(e)

    override def onSelectToken(token: TerrainToken): Unit =
      try
        model = model.selectToken(token)
        refreshView(s"${model.currentPlayer.name} seleziona $token")
        view.updateState(model)

      catch case e: IllegalStateException => handleError(e)

    override def onPlaceToken(coordinate: Coordinate): Unit =
      try
        val playerName = model.currentPlayer.name
        val name = model.tokensInHand.headOption.map(_.toString).getOrElse("?")
        model = model.placeToken(coordinate)
        val level = model.currentPlayer.board.cells(coordinate).getTokens.size
        refreshView(s"$playerName posiziona $name al livello $level")
        view.updateState(model)
      catch case e: IllegalStateException => handleError(e)

    override def onEndTurn(): Unit =
      try
        model = model.endTurn()
        if model.isGameOver then onEndGame(model.getPlayers)
        else
          refreshView(s"HEADER:${model.currentPlayer.name}")
          view.updateState(model)
      catch case e: IllegalStateException => handleError(e)

    override def onTakeAnimalCard(slot: Int): Unit =
      try
        val playerName = model.currentPlayer.name
        val cardPosition = model.currentPlayer.activeCards.size + 1
        model = model.takeAnimalCard(slot)
        val cardName = model.currentPlayer.activeCards.last.name
        refreshView(
          s"$playerName prende la carta $cardName e la porta nel suo posto $cardPosition"
        )
        view.updateState(model)
      catch case e: IllegalStateException => handleError(e)

    override def onSelectActiveCard(card: AnimalCard): Unit =
      try
        if model.selectedAnimalCard.contains(card) then return
        model = model.selectAnimalCard(card)
        refreshView(
          s"${model.currentPlayer.name} sceglie la carta ${card.name}"
        )
        view.updateState(model)
      catch case e: IllegalStateException => handleError(e)

    override def onCellClicked(coordinate: Coordinate): Unit =
      try
        val playerName = model.currentPlayer.name
        if model.selectedToken.isDefined then
          model = model.placeToken(coordinate)
          refreshView(s"$playerName posiziona un token")
        else if model.selectedAnimalCard.isDefined then
          model = model.placeAnimalCube(coordinate)
          refreshView(s"$playerName posiziona un cubo animale")
        else
          val playableCards = model.currentPlayer.activeCards.filter(c =>
            model.highlightedAnimalCells(c).nonEmpty
          )
          if playableCards.size == 1 && model
              .highlightedAnimalCells(playableCards.head)
              .contains(coordinate)
          then
            model = model.selectAnimalCard(playableCards.head)
            model = model.placeAnimalCube(coordinate)
            refreshView(s"$playerName posiziona un cubo animale")
          else return
        view.updateState(model)
      catch case e: IllegalStateException => handleError(e)

    override def onCancelTurn(): Unit =
      try
        val playerName = model.currentPlayer.name
        model = model.cancelTurn()
        refreshView(s"$playerName annulla il turno")
        view.updateState(model)
      catch case e: IllegalStateException => handleError(e)

    override def start(): Unit =
      val homeView = HomeView(onStartGame)
      stage.scene = new Scene(homeView)
      stage.maximized = true

    override def onStartGame(names: List[String], side: BoardSide): Unit =
      val players = names.zipWithIndex.map((name, index) =>
        Player(index, name, PersonalBoard(side))
      )
      model = GameModel(players)
      view.updateState(model)
      refreshView(s"HEADER:${model.currentPlayer.name}")
      stage.scene.value.setRoot(view)
      stage.maximized = true

    override def onEndGame(players: List[Player]): Unit =
      val endGameView = ScoreCalculatorView(players)
      stage.scene.value.setRoot(endGameView)
      stage.maximized = true
