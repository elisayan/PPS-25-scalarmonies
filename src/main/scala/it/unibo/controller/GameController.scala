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

    private def executeAction(action: GameModel => GameModel)(
        onSuccess: GameModel => Unit
    ): Unit =
      try
        model = action(model)
        onSuccess(model)
      catch case e: IllegalStateException => handleError(e)

    private def handleAutomaticAnimalPlacement(
        coordinate: Coordinate,
        playerName: String
    ): Unit =
      val playableCards = model.currentPlayer.activeCards.filter(card =>
        model.highlightedAnimalCells(card).contains(coordinate)
      )
      playableCards match
        case card :: Nil =>
          executeAction(
            _.selectAnimalCard(card)
              .placeAnimalCube(coordinate)
          ) { updatedModel =>
            refreshView(s"$playerName posiziona un cubo animale")
            view.updateState(updatedModel)
          }
        case _ => ()

    override def currentModel: GameModel = model

    override def currentPlayerId: Int = model.currentPlayer.id

    override def currentTurnState: TurnState = model.turnState

    override def onTakeTokens(slot: Int): Unit =
      val playerName = model.currentPlayer.name

      executeAction(_.takeTokens(slot)) { updatedModel =>
        val tokenNames =
          updatedModel.tokensInHand
            .map(_.toString)
            .mkString(", ")
        refreshView(s"$playerName prende $tokenNames")
        view.updateState(updatedModel)
      }

    override def onSelectToken(token: TerrainToken): Unit =
      executeAction(_.selectToken(token)) { updatedModel =>
        refreshView(s"${updatedModel.currentPlayer.name} seleziona $token")
        view.updateState(updatedModel)
      }

    override def onPlaceToken(coordinate: Coordinate): Unit =
      val playerName = model.currentPlayer.name
      val tokenName = model.selectedToken.map(_.toString).getOrElse("?")

      executeAction(_.placeToken(coordinate)) { updatedModel =>
        val level =
          updatedModel.currentPlayer.board.cells(coordinate).getTokens.size
        refreshView(s"$playerName posiziona $tokenName al livello $level")
        view.updateState(updatedModel)
      }

    override def onEndTurn(): Unit =
      executeAction(_.endTurn()): updatedModel =>
        if updatedModel.isGameOver then
          onEndGame(updatedModel.allPlayers)
        else
          refreshView(s"HEADER:${updatedModel.currentPlayer.name}")
          view.updateState(updatedModel)

    override def onTakeAnimalCard(slot: Int): Unit =
      val playerName = model.currentPlayer.name
      val cardPosition = model.currentPlayer.activeCards.size + 1

      executeAction(_.takeAnimalCard(slot)) { updatedModel =>
        val cardName = updatedModel.currentPlayer.activeCards.last.name
        refreshView(
          s"$playerName prende la carta $cardName " + s"e la porta nel suo posto $cardPosition"
        )
        view.updateState(updatedModel)
      }

    override def onSelectActiveCard(card: AnimalCard): Unit =
      if !model.selectedAnimalCard.contains(card) then
        executeAction(_.selectAnimalCard(card)) { updatedModel =>
          refreshView(
            s"${updatedModel.currentPlayer.name} " + s"sceglie la carta ${card.name}"
          )
          view.updateState(updatedModel)
        }

    override def onCellClicked(coordinate: Coordinate): Unit =
      val playerName = model.currentPlayer.name

      (model.selectedToken, model.selectedAnimalCard) match
        case (Some(_), _) =>
          executeAction(_.placeToken(coordinate)) { updatedModel =>
            refreshView(s"$playerName posiziona un token")
            view.updateState(updatedModel)
          }
        case (_, Some(_)) =>
          executeAction(_.placeAnimalCube(coordinate)) { updatedModel =>
            refreshView(s"$playerName posiziona un cubo animale")
            view.updateState(updatedModel)
          }
        case _ =>
          handleAutomaticAnimalPlacement(coordinate, playerName)

    override def onCancelTurn(): Unit =
      val playerName = model.currentPlayer.name

      executeAction(_.cancelTurn()) { updatedModel =>
        refreshView(s"$playerName annulla il turno")
        view.updateState(updatedModel)
      }

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
