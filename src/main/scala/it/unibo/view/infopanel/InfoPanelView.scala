package it.unibo.view.infopanel

import scalafx.geometry.Insets
import scalafx.scene.control.{Label, ScrollPane}
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Font, FontWeight, Text, TextFlow}

class InfoPanelView:

  private val logBox = new VBox(4):
    padding = Insets(8)
    style =
      """
      -fx-background-color: transparent;
      """

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
    vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded

    style =
      """
      -fx-background-color: transparent;
      -fx-background-insets: 0;
      """

  def addEntry(playerName:String, message:String):Unit =
    val color =
      playerColors.getOrElse(playerName,"#333333")
    val nameText = new Text(s"$playerName "):
      font = Font.font(
          "System",
          FontWeight.Bold,
          13
        )
      style = s"-fx-fill:$color;"
    val messageText = new Text(message):
      font =
        Font.font(
          "System",
          13
        )
      style = "-fx-fill:#222222;"

    val eventBox = new TextFlow:
      padding = Insets(6,10,6,10)
      children.addAll(
        nameText,
        messageText
      )

      style =
        """
        -fx-background-color:white;
        -fx-background-radius:2;
        -fx-border-radius:2;
        -fx-border-color:#e2d8c5;
        -fx-border-width:1;
        """

      maxWidth = 280

    logBox.children.add(
      0,
      eventBox
    )

  def addTurnHeader(playerName:String):Unit =
    val header = new Label(s"▶ Inizio del turno di $playerName"):
      font =
        Font.font(
          "System",
          FontWeight.Bold,
          13
        )
      style =
        """
        -fx-background-color:#eadfce;
        -fx-text-fill:#333333;
        -fx-padding:6 10 6 10;
        -fx-background-radius:2;
        """
      maxWidth = 280
      wrapText = true

    logBox.children.add(0, header)
