package it.unibo.view

import it.unibo.controller.GameController
import it.unibo.model.{GameModel, Player}
import it.unibo.view.card.AnimalCardView
import it.unibo.view.centralboard.CentralBoardView
import it.unibo.view.infopanel.InfoPanelView
import it.unibo.view.personalboard.PersonalBoardView
import it.unibo.view.playerarea.PlayerAreaView
import it.unibo.view.token.TokenView
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

class GameView(controller: GameController) extends GridPane:

  private val systemMessageBar: HBox = new HBox():
    alignment = Pos.Center
    padding = Insets(5)
    background = new Background(
      Array(new BackgroundFill(Color.Beige, new CornerRadii(5), Insets.Empty))
    )

  private val endTurnButton: Button = new Button("Fine Turno"):
    font = Font.font("Arial", FontWeight.Bold, 14.0)
    padding = Insets(6, 12, 6, 12)
    minWidth = 120
    onAction = _ => controller.onEndTurn()

  private val topBarContainer: HBox = new HBox(10):
    alignment = Pos.Center
    HBox.setHgrow(systemMessageBar, Priority.Always)
    children = Seq(systemMessageBar, endTurnButton)

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

  private def updateSystemMessageBar(message: String): Unit =
    systemMessageBar.children.clear()
    val msgTxt = new Text(message)
    msgTxt.font = Font.font("Arial", FontWeight.Bold, 16.0)
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

  private def updatePlayerAreas(areas: List[PlayerAreaView], currentPlayerName: String): Unit =
    playersContainer.children.clear()
    areas.foreach { area =>
      area.setDisabledArea(area.name != currentPlayerName)
      playersContainer.children.add(area)
  }

  private def updateCentralBoard(board: CentralBoardView): Unit =
    commonMarketBar.children.clear()
    commonMarketBar.children.add(board)

  def refresh(playerName: String, logMessage: String): Unit = ???

  private def createPlayerArea(player: Player): PlayerAreaView =
    val boardView = PersonalBoardView(player, controller.onPlaceToken)
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
    val areas = model.getPlayers.map(p => createPlayerArea(p))
    updatePlayerAreas(areas, model.currentPlayer.name)
    updatePersonalTokenSidebar(model.tokensInHand.map(t => TokenView(t)))
    updateCentralBoard(CentralBoardView(model.centralBoard, controller.onTakeAnimalCard, controller.onTakeTokens))
    updateSystemMessageBar(model.availableActionsMessage)

  initLayout()