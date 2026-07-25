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
      println(s"slot $slot" + card.toString)

  private val tokensContainer = new StackPane:
    prefWidth = 200
    prefHeight = 200

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
      1 -> (80.0, 45.0),
      2 -> (155.0, 45.0),
      3 -> (53.0, 115.0),
      4 -> (178.0, 115.0),
      5 -> (115.0, 155.0)
    )

  board.availableTokens.foreach:
    case (slot, tokens) =>
      val tokenStack = new StackPane:
        prefWidth = 60
        prefHeight = 60

      val trianglePositions =
        List(
          (20.0, 15.0), // alto sinistra
          (42.0, 15.0), // alto destra
          (31.0, 35.0)  // basso centro
        )

      tokens.zipWithIndex.foreach:
        case (token, index) =>
          val tokenView = TokenView(token, _=>())
          tokenView.setScaleX(0.65)
          tokenView.setScaleY(0.65)

          val (x, y) = trianglePositions(index)
          tokenView.translateX = x
          tokenView.translateY = y
          tokenView.onMouseClicked = _ => onTokenClicked(slot)
          tokenStack.children.add(tokenView)

      tokenPositions
        .get(slot)
        .foreach:
          case (x, y) =>
            tokenStack.translateX = x - 145
            tokenStack.translateY = y - 120

      tokensContainer.children.add(tokenStack)

  children.addAll(
    cardsContainer,
    tokensContainer
  )
