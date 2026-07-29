package it.unibo.view.infopanel

import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.Priority
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight
import scalafx.scene.text.Text
import scalafx.scene.text.TextFlow

class InfoPanelView:

  private val logBox = new VBox(4):
    padding = Insets(8)
    style = """
      -fx-background-color: transparent;
      """

  private val colors = List(
    "#2d7a2d",
    "#c0392b",
    "#2471a3",
    "#d4ac0d"
  )

  private val scrollPane: ScrollPane = new ScrollPane:
    content = logBox
    fitToWidth = true
    hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
    style = "-fx-background-color: transparent; -fx-background-insets: 0;"
    VBox.setVgrow(this, Priority.Always)

  private val headerLabel = new Label("Game Log:"):
    font = Font.font("Palatino", FontWeight.Bold, 12)
    maxWidth = Double.MaxValue
    alignment = Pos.Center
    style = """
      -fx-background-color: #dcd3c1;
      -fx-text-fill: #2c3e50;
      -fx-padding: 8 10 8 10;
      -fx-border-color: #c8bca6;
      -fx-border-width: 0 0 1 0;
      """

  val root: VBox = new VBox(0):
    prefWidth = 300
    prefHeight = 500
    style =
      "-fx-background-color: #fcfbf7; -fx-border-color: #e2d8c5; -fx-border-width: 0 0 0 1;"
    children = Seq(headerLabel, scrollPane)

  def addEntry(playerName: String, message: String, playerId: Int): Unit =
    val color = colors(playerId % colors.size)
    val nameText = new Text(s"$playerName "):
      font = Font.font(
        "Palatino",
        FontWeight.Bold,
        13
      )
      style = s"-fx-fill:$color;"
    val messageText = new Text(message):
      font = Font.font(
        "Palatino",
        13
      )
      style = "-fx-fill:#222222;"

    val bgColor = if logBox.children.size % 2 == 0 then "#ffffff" else "#f9f9f9"
    val eventBox = new TextFlow:
      padding = Insets(6, 10, 6, 10)
      children.addAll(
        nameText,
        messageText
      )

      style = s"""
        -fx-background-color: $bgColor;
        -fx-background-radius: 5;
        -fx-border-color: #e2d8c5;
        -fx-border-width: 0 0 1 0;
        """

      maxWidth = 280

    logBox.children.add(
      0,
      eventBox
    )

  def addTurnHeader(playerName: String): Unit =
    val header = new Label(s"▶ Inizio del turno di $playerName"):
      font = Font.font(
        "Palatino",
        FontWeight.Bold,
        13
      )
      style = """
        -fx-background-color:#eadfce;
        -fx-text-fill:#333333;
        -fx-padding:6 10 6 10;
        -fx-background-radius:2;
        """
      maxWidth = 280
      wrapText = true

    logBox.children.add(0, header)
