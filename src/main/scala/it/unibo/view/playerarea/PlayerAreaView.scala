package it.unibo.view.playerarea

import it.unibo.view.personalboard.PersonalBoardView
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.*
import scalafx.scene.paint.Color

class PlayerAreaView(
                      boardView: PersonalBoardView,
                      cards: List[Node],
                      completedCards: List[Node],
                      playerName: String,
                      maxCardSlots: Int = 4
                    ) extends VBox:

  private val cardSlots: List[Node] = List.tabulate(maxCardSlots) { i =>
    if i < cards.size then cards(i)
    else
      new StackPane:
        prefWidth = 90
        prefHeight = 140
        style = "-fx-background-color: rgba(180,180,180,0.35); " +
          "-fx-border-color: rgba(130,130,130,0.4); " +
          "-fx-border-width: 1.5; " +
          "-fx-border-radius: 8; " +
          "-fx-background-radius: 8;"
  }

  private val cardsContainer = new HBox(5):
    alignment = Pos.Center
    cardSlots.foreach(slot => children.add(slot))

  private val boardContainer = new HBox:
    alignment = Pos.Center
    children.add(boardView)

  private val nameLabel = new Label(playerName):
    style = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #c0392b;"
    alignment = Pos.Center

  private val completedCardsContainer = new FlowPane(5, 5):
    alignment = Pos.Center
    completedCards.foreach(card => children.add(card))

  prefWidth = 500
  padding = Insets(12)
  spacing = 8
  alignment = Pos.TopCenter
  background = new Background(Array(
    new BackgroundFill(Color.web("#eee8d0"), CornerRadii(12), Insets.Empty)
  ))

  children.addAll(
    cardsContainer,
    boardContainer,
    nameLabel,
    completedCardsContainer
  )