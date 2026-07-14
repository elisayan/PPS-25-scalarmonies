package it.unibo.view.infopanel

import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.Priority
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight

class InfoPanelView():

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
    val row = new HBox(6):
      padding = Insets(3, 8, 3, 8)
      style = "-fx-background-color: transparent;"
    val nameLabel = new Label(playerName):
      font = Font.font("System", FontWeight.Bold, 12)
      style = s"-fx-text-fill: $color;"
      minWidth = 80
    val msgLabel = new Label(message):
      font = Font.font("System", 12)
      style = "-fx-text-fill: #333333;"
      wrapText = true
      HBox.setHgrow(this, Priority.Always)
    row.children.addAll(nameLabel, msgLabel)
    logBox.children.add(0, row)
    logBox.children.add(1, divider())

  def addTurnHeader(playerName: String): Unit =
    val headerLabel = new Label(s"► Inizio del turno di $playerName"):
      font = Font.font("System", FontWeight.Bold, 12)
      style = "-fx-text-fill: #222222; " +
        "-fx-background-color: #e8e0d0; " +
        "-fx-padding: 4 8 4 8;"
      maxWidth = Double.MaxValue
    logBox.children.add(0, headerLabel)

  private def divider() =
    new Label(""):
      prefHeight = 1
      maxWidth = Double.MaxValue
      style = "-fx-background-color: #ddd; -fx-padding: 0;"
