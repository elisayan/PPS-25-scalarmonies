package it.unibo.view

import it.unibo.controller.GameController
import it.unibo.model.GameModel
import it.unibo.model.Player
import it.unibo.model.personalboard.Coordinate
import it.unibo.view.card.AnimalCardView
import it.unibo.view.centralboard.CentralBoardView
import it.unibo.view.infopanel.InfoPanelView
import it.unibo.view.personalboard.PersonalBoardView
import it.unibo.view.playerarea.PlayerAreaView
import it.unibo.view.token.TokenView
import it.unibo.view.utils.ImageCache
import scalafx.animation.PauseTransition
import scalafx.geometry.GeometryIncludes.jfxBounds2sfx
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Cursor
import scalafx.scene.control.Button
import scalafx.scene.control.ScrollPane
import scalafx.scene.control.Tooltip
import scalafx.scene.effect.DropShadow
import scalafx.scene.image.ImageView
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
    alignment = Pos.CenterLeft
    padding = Insets(5)
    background = new Background(
      Array(new BackgroundFill(Color.Beige, new CornerRadii(5), Insets.Empty))
    )

  private val cancelTurnButton: Button = new Button("Cancella Turno"):
    font = Font.font("Palatino", FontWeight.Bold, 14.0)
    padding = Insets(3, 8, 3, 8)
    minWidth = 80
    cursor = Cursor.Hand
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
    font = Font.font("Palatino", FontWeight.Bold, 14.0)
    padding = Insets(3, 8, 3, 8)
    minWidth = 80
    cursor = Cursor.Hand
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

  private val rulesButton: Button = new Button("?"):
    font = Font.font("Palatino", FontWeight.Bold, 14.0)
    padding = Insets(3, 8, 3, 8)
    minWidth = 32
    textFill = Color.White
    focusTraversable = false
    cursor = scalafx.scene.Cursor.Hand
    background = new Background(
      Array(
        new BackgroundFill(
          Color.web("#2980b9"),
          new CornerRadii(16),
          Insets.Empty
        )
      )
    )

    onAction = _ =>
      val tip = this.tooltip.value
      if tip != null then
        val bounds = this.localToScreen(this.boundsInLocal.value)
        tip.show(this, bounds.minX, bounds.maxY + 4)

    onMouseExited = _ =>
      val tip = this.tooltip.value
      if tip != null then tip.hide()

  private def createRulesTooltip(model: GameModel): Tooltip =
    val currentSide = model.startingPlayer.board.side

    val imagePath = currentSide match
      case it.unibo.model.personalboard.BoardSide.SideA =>
        "/rules/promemoria_latoA.png"
      case it.unibo.model.personalboard.BoardSide.SideB =>
        "/rules/promemoria_latoB.png"

    val imgView = new ImageView(ImageCache.getImage(imagePath)):
      fitWidth = 230.0
      preserveRatio = true
      smooth = true

    new Tooltip():
      graphic = imgView
      showDelay = Duration(100.0)
      hideDelay = Duration(100.0)
      showDuration = Duration.Indefinite
      style = "-fx-background-color: transparent; -fx-padding: 0; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.35), 8, 0, 0, 3);"

  private val topBarContainer: HBox = new HBox(10):
    alignment = Pos.CenterLeft
    minHeight = 45.0
    prefHeight = 45.0
    maxHeight = 45.0
    padding = Insets(5, 15, 5, 15)
    HBox.setHgrow(systemMessageBar, Priority.Always)
    children =
      Seq(systemMessageBar, cancelTurnButton, endTurnButton, rulesButton)

  private val commonMarketBar: HBox = new HBox():
    alignment = Pos.Center
    minHeight = 180.0
    prefHeight = 180.0
    maxHeight = 180.0
    padding = Insets(10)
    pickOnBounds = false
    background = new Background(
      Array(new BackgroundFill(Color.Wheat, new CornerRadii(5), Insets.Empty))
    )

  private val playersContainer: FlowPane = new FlowPane():
    hgap = 30.0
    vgap = 30.0
    padding = Insets(15, 15, 5, 15)

  private val playersScrollPane: ScrollPane = new ScrollPane():
    fitToWidth = true
    fitToHeight = true
    minHeight = 0.0
    hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
    content = playersContainer

  private val personalTokenSidebar: VBox = new VBox():
    spacing = 20.0
    padding = Insets(1)
    alignment = Pos.CenterLeft
    background = new Background(
      Array(
        new BackgroundFill(
          Color.web("#e8e4d8"),
          new CornerRadii(8),
          Insets.Empty
        )
      )
    )

  private val gameplayColumn: VBox = new VBox(12):
    children = Seq(topBarContainer, commonMarketBar, playersScrollPane)
    VBox.setVgrow(playersScrollPane, Priority.Always)

  private val infoPanelView = new InfoPanelView()

  private def updateSystemMessageBar(
      message: String,
      color: Color = Color.Beige
  ): Unit =
    systemMessageBar.background = new Background(
      Array(new BackgroundFill(color, new CornerRadii(5), Insets.Empty))
    )
    systemMessageBar.children.clear()
    val msgTxt = new Text(message)
    msgTxt.font = Font.font("Palatino", FontWeight.Bold, 16.0)
    msgTxt.fill = Color.Black
    systemMessageBar.children.add(msgTxt)

  private def updatePersonalTokenSidebar(tokens: List[TokenView]): Unit =
    personalTokenSidebar.children.clear()
    tokens.foreach(token =>
      token.setScaleX(0.8)
      token.setScaleY(0.8)
      personalTokenSidebar.children.add(token)
    )

  private def updateInfoPanel(message: String): Unit =
    if message.startsWith("HEADER:") then
      infoPanelView.addTurnHeader(
        message.stripPrefix("HEADER:")
      )
    else
      val player =
        controller.currentModel.allPlayers.find(p => message.startsWith(p.name))
      player.foreach { p =>
        val text = message.stripPrefix(p.name).trim
        infoPanelView.addEntry(
          p.name,
          text,
          p.id
        )
      }

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

  def refresh(model: GameModel, logMessage: String): Unit =
    updateState(model)
    updateInfoPanel(logMessage)

  private def createPlayerArea(
      player: Player,
      highlightedCells: List[Coordinate],
      isStartingPlayer: Boolean
  ): PlayerAreaView =
    val boardView =
      PersonalBoardView(player, controller.onCellClicked, highlightedCells)
    val cards = player.activeCards.map: c =>
      val cardView = AnimalCardView(c)
      if player == controller.currentModel.currentPlayer then
        cardView.onMouseClicked = _ => controller.onSelectActiveCard(c)
        val isSelected = controller.currentModel.selectedAnimalCard.contains(c)
        val isPlayable =
          controller.currentModel.highlightedAnimalCells(c).nonEmpty
        val hasSelection = controller.currentModel.selectedAnimalCard.isDefined
        if isSelected then
          cardView.effect = new DropShadow(20.0, Color.LimeGreen)
        else if !hasSelection && isPlayable then
          cardView.effect = new DropShadow(15.0, Color.Gold)
        else cardView.effect = null
      cardView
    val completedCards = player.completedCards.map(c => AnimalCardView(c))
    PlayerAreaView(
      boardView,
      cards,
      completedCards,
      player.name,
      isStartingPlayer
    )

  private def initLayout(): Unit =
    padding = Insets(5, 10, 10, 10)
    hgap = 5.0
    vgap = 5.0

    val colGameplay = new ColumnConstraints():
      percentWidth = 84.0
    val colTokenHand = new ColumnConstraints():
      percentWidth = 3.0
    val colInfoPanel = new ColumnConstraints():
      percentWidth = 13.0
    columnConstraints.addAll(colGameplay, colTokenHand, colInfoPanel)

    val mainRow = new RowConstraints():
      vgrow = Priority.Always
    rowConstraints.add(mainRow)
    add(gameplayColumn, 0, 0)
    add(personalTokenSidebar, 1, 0)
    add(infoPanelView.root, 2, 0)

  def updateState(model: GameModel): Unit =
    val playableCards = model.currentPlayer.activeCards.filter(c =>
      model.highlightedAnimalCells(c).nonEmpty
    )
    val highlighted: List[Coordinate] = (model.selectedToken, model.selectedAnimalCard, playableCards) match
      case (Some(token), _, _)          => model.highlightedCells(token)
      case (_, Some(card), _)           => model.highlightedAnimalCells(card)
      case (None, None, singleCard :: Nil) => model.highlightedAnimalCells(singleCard)
      case _                            => List.empty
    val areas = model.allPlayers.map(p =>
      val playerHighlightedCells =
        if p == model.currentPlayer then highlighted
        else List()
      createPlayerArea(p, playerHighlightedCells, p == model.startingPlayer)
    )
    updatePlayerAreas(areas, model.currentPlayer.name)
    updatePersonalTokenSidebar(
      model.tokensInHand.map(t => TokenView(t, controller.onSelectToken))
    )
    updateCentralBoard(
      CentralBoardView(
        model.centralBoard,
        model.pouchSize,
        controller.onTakeAnimalCard,
        controller.onTakeTokens
      )
    )
    updateSystemMessageBar(model.availableActionsMessage)
    rulesButton.tooltip = createRulesTooltip(model)
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
