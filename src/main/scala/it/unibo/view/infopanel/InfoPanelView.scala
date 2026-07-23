package it.unibo.view.infopanel

import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.Priority
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Font, FontWeight, Text, TextFlow}

class InfoPanelView:

  private val logBox = new VBox(2):
    padding = Insets(10)
    style = "-fx-background-color: #f5f0e8;"

  private val playerColors = Map(
    "Player1" -> "#2d7a2d",
    "Player2" -> "#c0392b",
    "Player3" -> "#2471a3",
    "Player4" -> "#d4ac0d"
  )

  val root: ScrollPane = new ScrollPane:
    content = logBox
    fitToWidth = true
    prefWidth = 300
    prefHeight = 500
    hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    style = "-fx-background-color: #f5f0e8; " +
      "-fx-border-color: #c8b89a; " +
      "-fx-border-width: 1;"

  def addEntry(playerName: String, message: String): Unit =
    val color = playerColors.getOrElse(playerName, "#333333")
  
    val nameText = new Text(s"$playerName "):
      font = Font.font("System", FontWeight.Bold, 12)
      style = s"-fx-fill: $color;"
  
    val messageText = new Text(message):
      font = Font.font("System", 12)
      style = "-fx-fill: #333333;"
  
    val textFlow = new TextFlow:
      padding = Insets(3, 8, 3, 8)
      prefWidth = 280
      children.addAll(
        nameText,
        messageText
      )
  
    logBox.children.add(0, textFlow)
    logBox.children.add(1, divider())

  def addTurnHeader(playerName: String): Unit =
    val headerLabel = new Label(s"► Inizio del turno di $playerName"):
      font = Font.font("System", FontWeight.Bold, 12)
      style = "-fx-text-fill: #222222; " +
        "-fx-background-color: #e8e0d0; " +
        "-fx-padding: 4 8 4 8;"
      wrapText = true
      prefWidth = 280
      maxWidth = Double.MaxValue
    logBox.children.add(0, headerLabel)

  private def divider() =
    new Label(""):
      prefHeight = 1
      maxWidth = Double.MaxValue
      style = "-fx-background-color: #ddd; -fx-padding: 0;"
