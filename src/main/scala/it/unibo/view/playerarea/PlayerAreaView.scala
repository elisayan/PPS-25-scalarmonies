package it.unibo.view.playerarea

import it.unibo.view.personalboard.PersonalBoardView
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{Background, BackgroundFill, CornerRadii, HBox, VBox}
import scalafx.scene.paint.Color

class PlayerAreaView(
                      boardView: PersonalBoardView,
                      cards: List[Node],
                      playerName: String
                    ) extends VBox:

  private val cardsContainer = new HBox:
    spacing = 10
    alignment = Pos.Center

  private val nameLabel = new Label(playerName):
    alignment = Pos.Center

  padding = Insets(10)
  spacing = 10
  alignment = Pos.Center

  background = new Background(Array(
    new BackgroundFill(Color.Beige, CornerRadii(10), Insets.Empty)
  ))

  cards.foreach(card =>
    cardsContainer.children.add(card)
  )

  children.addAll(
    cardsContainer,
    boardView,
    nameLabel
  )