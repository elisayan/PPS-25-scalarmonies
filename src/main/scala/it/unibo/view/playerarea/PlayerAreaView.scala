package it.unibo.view.playerarea

import it.unibo.view.personalboard.PersonalBoardView
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.effect.{ColorAdjust, DropShadow}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

class PlayerAreaView(
    boardView: PersonalBoardView,
    cards: List[Node],
    completedCards: List[Node],
    playerName: String,
    isStartingPlayer: Boolean = false,
    maxCardSlots: Int = 4
) extends VBox:

  private val cardSlots: List[Node] = List.tabulate(maxCardSlots) { i =>
    if i < cards.size then cards(i)
    else
      new StackPane:
        prefWidth = 110
        prefHeight = 150
        minWidth = 110
        minHeight = 150
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
    VBox.setVgrow(this, Priority.Always)
    children.add(boardView)

  private val nameLabel = new Label(
    if isStartingPlayer then s"$playerName (1°)"
    else playerName
  ):
    font = Font.font("Palatino", FontWeight.Bold, 15.0)
    style =
      "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #c0392b;"
    alignment = Pos.Center

  private val completedCardsContainer = new FlowPane(5, 5):
    alignment = Pos.Center
    completedCards.foreach(card => children.add(card))

  private val disabledEffect = new ColorAdjust:
    brightness = -0.1
    saturation = -0.2

  private val activeGlow = new DropShadow(20.0, Color.LightGreen)

  prefWidth = 500
  padding = Insets(12)
  spacing = 8
  alignment = Pos.TopCenter
  background = new Background(
    Array(
      new BackgroundFill(Color.web("#eee8d0"), CornerRadii(12), Insets.Empty)
    )
  )

  children.addAll(
    nameLabel,
    cardsContainer,
    boardContainer,
    completedCardsContainer
  )

  def name: String = playerName

  def setDisabledArea(disabled: Boolean): Unit =
    this.disable = disabled
    if disabled then
      this.effect = disabledEffect
      this.opacity = 0.7
    else
        this.effect = activeGlow
        this.opacity = 1.0
