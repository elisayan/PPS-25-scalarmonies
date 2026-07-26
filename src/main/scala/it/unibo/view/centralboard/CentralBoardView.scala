package it.unibo.view.centralboard

import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.view.card.AnimalCardView
import it.unibo.view.token.TokenView
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout.HBox
import scalafx.scene.layout.StackPane

case class CentralBoardView(
    board: CentralBoard,
    onCardClicked: Int => Unit,
    onTokenClicked: Int => Unit
) extends HBox:

  spacing = 30
  padding = Insets(20)
  alignment = Pos.Center

  private val cardsContainer = new HBox:
    spacing = 10
    alignment = Pos.Center

  board.availableCards.foreach:
    case (slot, card) =>
      val cardView =
        AnimalCardView(
          card,
          cardWidth = 90,
          cardHeight = 140
        )
      cardView.onMouseClicked = _ => onCardClicked(slot)
      cardsContainer.children.add(cardView)

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
      2 -> (140.0, 45.0),
      3 -> (38.0, 117.0),
      4 -> (164.0, 117.0),
      5 -> (105.0, 160.0)
    )

  private val triangleOffsets = List(
    (0.0, -10.0),
    (-12.0, 8.0),
    (12.0, 8.0)
  )

  board.availableTokens.foreach:
    case (slot, tokens) =>
      val tokenStack = new StackPane:
        prefWidth = 40
        prefHeight = 40
        maxWidth = 40
        maxHeight = 40
        pickOnBounds = true
        onMouseClicked = _ => onTokenClicked(slot)

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
            // Posiziona il centro dello tokenStack sulle coordinate dello slot
            tokenStack.translateX = x - 100
            tokenStack.translateY = y - 100

      tokensContainer.children.add(tokenStack)

  children.addAll(
    cardsContainer,
    tokensContainer
  )
