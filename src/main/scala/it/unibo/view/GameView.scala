package it.unibo.view

import it.unibo.controller.GameController
import it.unibo.model.token.TerrainToken
import it.unibo.view.centralboard.CentralBoardView
import it.unibo.view.infopanel.InfoPanelView
import it.unibo.view.personalboard.PersonalBoardView
import it.unibo.view.playerarea.PlayerAreaView
import it.unibo.view.token.TokenView
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.{Background, BackgroundFill, Border, BorderPane, BorderStroke, ColumnConstraints, CornerRadii, FlowPane, GridPane, HBox, RowConstraints, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.layout.LayoutIncludes.jfxBorderStrokeStyle2sfx
import scalafx.scene.text.{Font, FontWeight, Text}

class GameView(controller: GameController) extends GridPane:

  private val systemMessageBar: HBox = new HBox():
    alignment = Pos.Center
    padding = Insets(10)
    background = new Background(Array(new BackgroundFill(Color.Beige, new CornerRadii(5), Insets.Empty)))

  private val commonMarketBar: HBox = new HBox():
    alignment = Pos.Center
    padding = Insets(15)
    background = new Background(Array(new BackgroundFill(Color.Wheat, new CornerRadii(5), Insets.Empty)))

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
    background = new Background(Array(new BackgroundFill(Color.LightGrey, new CornerRadii(5), Insets.Empty)))

  private val infoPanelLog: VBox = new VBox():
    spacing = 10.0
    padding = Insets(15)
    alignment = Pos.TopLeft
    background = new Background(Array(new BackgroundFill(Color.Lavender, new CornerRadii(5), Insets.Empty)))

  private def initLayout(): Unit =
    padding = Insets(10)
    hgap = 5.0
    vgap = 5.0

    val colGameplay = new ColumnConstraints() {
      percentWidth = 84.0
    }
    val colTokenHand = new ColumnConstraints() {
      percentWidth = 3.0
    }
    val colInfoPanel = new ColumnConstraints() {
      percentWidth = 13.0
    }
    columnConstraints.addAll(colGameplay, colTokenHand, colInfoPanel)

    val rowSystemMessage = new RowConstraints() {
      percentHeight = 3.0
    }
    val rowCommonMarket = new RowConstraints() {
      percentHeight = 32.0
    }
    val rowPlayersZone = new RowConstraints() {
      percentHeight = 65.0
    }
    rowConstraints.addAll(rowSystemMessage, rowCommonMarket, rowPlayersZone)


    add(systemMessageBar, 0, 0)
    add(commonMarketBar, 0, 1)
    add(playersScrollPane, 0, 2)

    GridPane.setRowSpan(personalTokenSidebar, 3)
    add(personalTokenSidebar, 1, 0)

    GridPane.setRowSpan(infoPanelLog, 3)
    add(infoPanelLog, 2, 0)

  def updateSystemMessageBar(message: String): Unit =
    systemMessageBar.children.clear()
    val msgTxt = new Text(message)
    msgTxt.font = Font.font("Arial", FontWeight.Bold, 18.0)
    systemMessageBar.children.add(msgTxt)

  def updatePersonalTokenSidebar(tokens: List[TokenView]): Unit =
    personalTokenSidebar.children.clear()
    tokens.foreach(token =>
      token.setScaleX(0.8)
      token.setScaleY(0.8)
      personalTokenSidebar.children.add(token))

  def updateInfoPanelLog(panel: InfoPanelView): Unit =
    infoPanelLog.children.clear()
    infoPanelLog.children.add(panel.root)

  def updatePlayersBoards(boards: List[PersonalBoardView]): Unit =
    playersContainer.children.clear()
    boards.foreach(board => playersContainer.children.add(board))

  def updatePlayerAreas(areas: List[PlayerAreaView]): Unit =
    playersContainer.children.clear()
    areas.foreach(area => playersContainer.children.add(area))

  def updateCentralBoard(board: CentralBoardView): Unit = {
    commonMarketBar.children.clear()
    commonMarketBar.children.add(board)
  }

  initLayout()