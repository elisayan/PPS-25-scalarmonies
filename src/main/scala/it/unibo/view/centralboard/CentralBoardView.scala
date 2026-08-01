package it.unibo.view.centralboard

import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.view.card.AnimalCardView
import it.unibo.view.token.TokenView
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Cursor
import scalafx.scene.control.Label
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout.HBox
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.VBox

case class CentralBoardView(
    board: CentralBoard,
    pouchSize: Int,
    onCardClicked: Int => Unit,
    onTokenClicked: Int => Unit
) extends HBox:

  spacing = 20
  padding = Insets(5, 15, 5, 15)
  alignment = Pos.Center

  private val cardsContainer = new HBox:
    spacing = 10
    alignment = Pos.Center
    children = board.availableCards.map:
      case (slot, card) =>
        val cardView = AnimalCardView(card)
        cardView.onMouseClicked = _ => onCardClicked(slot)
        cardView
    .toSeq

  private val tokensContainer = new StackPane:
    prefWidth = 200
    prefHeight = 200
    pickOnBounds = false

  private val boardImage =
    new ImageView(
      new Image(
        getClass.getResource("/centralboard/central_board.png").toString
      )
    ):
      fitWidth = 200
      fitHeight = 200
      preserveRatio = true
  tokensContainer.children.add(boardImage)

  private val tokenPositions =
    Map(
      1 -> (62.0, 45.0),
      2 -> (137.0, 45.0),
      3 -> (38.0, 117.0),
      4 -> (161.0, 117.0),
      5 -> (100.0, 155.0)
    )

  private val triangleOffsets = List(
    (0.0, -10.0),
    (-12.0, 8.0),
    (12.0, 8.0)
  )

  private val pouchLabel = new Label:
    text = s"x$pouchSize"
    style = """
      -fx-font-family: 'Palatino';
      -fx-font-size: 18px;
      -fx-font-weight: bold;
      """

  board.availableTokens.foreach:
    case (slot, tokens) =>
      val tokenStack = new StackPane:
        prefWidth = 40
        prefHeight = 40
        maxWidth = 40
        maxHeight = 40
        pickOnBounds = true
        onMouseClicked = _ => onTokenClicked(slot)
        cursor = Cursor.Hand

      tokens.zipWithIndex.foreach:
        case (token, index) =>
          val tokenView = TokenView(token, _ => ())
          tokenView.setScaleX(0.55)
          tokenView.setScaleY(0.55)

          val (offsetX, offsetY) =
            triangleOffsets.lift(index).getOrElse((0.0, 0.0))
          tokenView.translateX = offsetX
          tokenView.translateY = offsetY

          tokenView.mouseTransparent = true

          tokenStack.children.add(tokenView)

      tokenPositions
        .get(slot)
        .foreach:
          case (x, y) =>
            tokenStack.translateX = x - 100
            tokenStack.translateY = y - 100

      tokensContainer.children.add(tokenStack)

  pouchLabel.translateX = 75
  pouchLabel.translateY = -35

  private val boardWithPouch = new VBox(0):
    alignment = Pos.Center
    scaleX = 0.8
    scaleY = 0.8
    maxHeight = 180.0
    pickOnBounds = false
    children.addAll(tokensContainer, pouchLabel)

  this.pickOnBounds = false

  children.addAll(
    cardsContainer,
    boardWithPouch
  )
