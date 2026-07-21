package it.unibo.view.centralboard

import it.unibo.controller.GameController
import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.view.animalcard.AnimalCardView
import it.unibo.view.token.TokenView
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{HBox, VBox}

case class CentralBoardView(
                             board: CentralBoard,
                             controller: GameController
                           ) extends HBox:

  spacing = 20.0
  padding = Insets(10)
  alignment = Pos.Center

  private val slotIds: List[Int] =
    List(1, 2, 3, 4, 5)

  private val slotViews: List[VBox] =
    slotIds.map { slot =>

      val slotContainer = new VBox:
        spacing = 10.0
        alignment = Pos.Center

      board.availableCards.get(slot).foreach { card =>
        val cardView = AnimalCardView(card)

        cardView.onMouseClicked = _ =>
          controller.onTakeAnimalCard(slot)

        slotContainer.children.add(cardView)
      }

      board.availableTokens
        .getOrElse(slot, List.empty)
        .foreach { token =>

          val tokenView = TokenView(token)

          tokenView.onMouseClicked = _ =>
            controller.onTakeTokens(slot)

          slotContainer.children.add(tokenView)
        }

      slotContainer
    }

  slotViews.foreach(slotView =>
    children.add(slotView)
  )