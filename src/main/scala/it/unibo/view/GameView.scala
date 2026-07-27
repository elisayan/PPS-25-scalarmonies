package it.unibo.view

import it.unibo.controller.GameController
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.{GameModel, Player}
import it.unibo.view.card.AnimalCardView
import it.unibo.view.centralboard.CentralBoardView
import it.unibo.view.infopanel.InfoPanelView
import it.unibo.view.personalboard.PersonalBoardView
import it.unibo.view.playerarea.PlayerAreaView
import it.unibo.view.token.TokenView
import scalafx.animation.PauseTransition
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.layout.Background
import scalafx.scene.layout.BackgroundFill
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.CornerRadii
import scalafx.scene.layout.FlowPane
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.Priority
import scalafx.scene.layout.RowConstraints
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight
import scalafx.scene.text.Text
import scalafx.util.Duration

class GameView(controller: GameController) extends GridPane:

  private var errorTimer: Option[PauseTransition] = None

  private val systemMessageBar: HBox = new HBox():
    alignment = Pos.Center
    padding = Insets(5)
    background = new Background(
      Array(new BackgroundFill(Color.Beige, new CornerRadii(5), Insets.Empty))
    )

  private val cancelTurnButton: Button = new Button("Cancella Turno"):
    font = Font.font("Arial", FontWeight.Bold, 14.0)
    padding = Insets(3, 8, 3, 8)
    minWidth = 80
    textFill = Color.White
    background = new Background(
      Array(new BackgroundFill(Color.Red, new CornerRadii(5), Insets.Empty))
    )
    onAction = _ => controller.onCancelTurn()
    hover.onChange((_, _, isHovered) =>
      background = new Background(
        Array(
          new BackgroundFill(
            if isHovered then Color.DarkRed else Color.Red,
            new CornerRadii(5),
            Insets.Empty
          )
        )
      )
    )

  private val endTurnButton: Button = new Button("Fine Turno"):
    font = Font.font("Arial", FontWeight.Bold, 14.0)
    padding = Insets(3, 8, 3, 8)
    minWidth = 80
    textFill = Color.White
    background = new Background(
      Array(new BackgroundFill(Color.Green, new CornerRadii(5), Insets.Empty))
    )
    onAction = _ => controller.onEndTurn()
    hover.onChange((_, _, isHovered) =>
      background = new Background(
        Array(
          new BackgroundFill(
            if isHovered then Color.DarkGreen else Color.Green,
            new CornerRadii(5),
            Insets.Empty
          )
        )
      )
    )

  private val topBarContainer: HBox = new HBox(10):
    alignment = Pos.Center
    HBox.setHgrow(systemMessageBar, Priority.Always)
    children = Seq(systemMessageBar, cancelTurnButton, endTurnButton)

  private val commonMarketBar: HBox = new HBox():
    alignment = Pos.Center
    padding = Insets(10)
    background = new Background(
      Array(new BackgroundFill(Color.Wheat, new CornerRadii(5), Insets.Empty))
    )

  private val playersContainer: FlowPane = new FlowPane():
    hgap = 30.0
    vgap = 30.0
    padding = Insets(1)

  private val playersScrollPane: ScrollPane = new ScrollPane():
    fitToWidth = true
    fitToHeight = true
    hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
    content = playersContainer

  private val personalTokenSidebar: VBox = new VBox():
    spacing = 20.0
    padding = Insets(1)
    alignment = Pos.CenterLeft
    background = new Background(
      Array(
        new BackgroundFill(Color.LightGrey, new CornerRadii(5), Insets.Empty)
      )
    )

  private val infoPanelLog: VBox = new VBox():
    spacing = 10.0
    padding = Insets(15)
    alignment = Pos.TopLeft
    background = new Background(
      Array(
        new BackgroundFill(Color.Lavender, new CornerRadii(5), Insets.Empty)
      )
    )

  private def updateSystemMessageBar(
      message: String,
      color: Color = Color.Beige
  ): Unit =
    systemMessageBar.background = new Background(
      Array(new BackgroundFill(color, new CornerRadii(5), Insets.Empty))
    )
    systemMessageBar.children.clear()
    val msgTxt = new Text(message)
    msgTxt.font = Font.font("Arial", FontWeight.Bold, 16.0)
    msgTxt.fill = Color.Black
    systemMessageBar.children.add(msgTxt)

  private def updatePersonalTokenSidebar(tokens: List[TokenView]): Unit =
    personalTokenSidebar.children.clear()
    tokens.foreach(token =>
      token.setScaleX(0.8)
      token.setScaleY(0.8)
      personalTokenSidebar.children.add(token)
    )

  private def updateInfoPanelLog(panel: InfoPanelView): Unit =
    infoPanelLog.children.clear()
    infoPanelLog.children.add(panel.root)

  private def updatePlayerAreas(
      areas: List[PlayerAreaView],
      currentPlayerName: String
  ): Unit =
    playersContainer.children.clear()
    areas.foreach { area =>
      area.setDisabledArea(area.name != currentPlayerName)
      playersContainer.children.add(area)
    }

  private def updateCentralBoard(board: CentralBoardView): Unit =
    commonMarketBar.children.clear()
    commonMarketBar.children.add(board)

  def refresh(playerName: String, logMessage: String): Unit = ???

  private def createPlayerArea(player: Player, highlightedCells: List[Coordinate]): PlayerAreaView =
    val boardView = PersonalBoardView(player, controller.onPlaceToken, highlightedCells)
    val cards = player.activeCards.map(c => AnimalCardView(c))
    val completedCards = player.completedCards.map(c => AnimalCardView(c))
    val area = PlayerAreaView(boardView, cards, completedCards, player.name)
    area

  private def initLayout(): Unit =
    padding = Insets(10)
    hgap = 5.0
    vgap = 5.0

    val colGameplay = new ColumnConstraints():
      percentWidth = 84.0
    val colTokenHand = new ColumnConstraints():
      percentWidth = 3.0
    val colInfoPanel = new ColumnConstraints():
      percentWidth = 13.0
    columnConstraints.addAll(colGameplay, colTokenHand, colInfoPanel)

    val rowSystemMessage = new RowConstraints():
      percentHeight = 5.0
    val rowCommonMarket = new RowConstraints():
      percentHeight = 31.0
    val rowPlayersZone = new RowConstraints():
      percentHeight = 64.0
    rowConstraints.addAll(rowSystemMessage, rowCommonMarket, rowPlayersZone)

    add(topBarContainer, 0, 0)
    add(commonMarketBar, 0, 1)
    add(playersScrollPane, 0, 2)

    GridPane.setRowSpan(personalTokenSidebar, 3)
    add(personalTokenSidebar, 1, 0)

    GridPane.setRowSpan(infoPanelLog, 3)
    add(infoPanelLog, 2, 0)

  def updateState(model: GameModel): Unit =
    val highlighted = model.selectedToken match {
      case Some(token) => model.highlightedCells(token)
      case None => List()
    }
    val areas = model.getPlayers.map(p =>
      val playerHighlightedCells =
        if p == model.currentPlayer then
          highlighted
        else
          List()
      createPlayerArea(p, playerHighlightedCells))
    updatePlayerAreas(areas, model.currentPlayer.name)
    updatePersonalTokenSidebar(
      model.tokensInHand.map(t => TokenView(t, controller.onSelectToken))
    )
    updateCentralBoard(
      CentralBoardView(
        model.centralBoard,
        controller.onTakeAnimalCard,
        controller.onTakeTokens
      )
    )
    updateSystemMessageBar(model.availableActionsMessage)

  def showTemporaryError(
      errorMessage: String = "Mossa illegale! Controllare le regole",
      durationSeconds: Int = 3
  ): Unit =
    errorTimer.foreach(_.stop())
    updateSystemMessageBar(errorMessage, Color.Red)
    val pause = new PauseTransition(Duration(durationSeconds * 1000.0))
    pause.onFinished = _ =>
      updateSystemMessageBar(controller.currentModel.availableActionsMessage)
      errorTimer = None

    errorTimer = Some(pause)
    pause.play()

  initLayout()
