package it.unibo.view.scorecalculator

import it.unibo.model.Player
import it.unibo.model.personalBoard.PersonalBoard
import it.unibo.model.scorecalculator.ScoreCalculator
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.text.{Font, FontWeight, Text}
import scalafx.scene.paint.Color
import scalafx.geometry.{Insets, Pos}
import scalafx.Includes.*

case class ScoreCalculatorView(
                                playerBoards: List[Player],
                                pos: (Double, Double) = (0.0, 0.0),
                                calculator: ScoreCalculator
                              ) extends HBox:

  private val MaxPlayers = 4

  private def setContainer(): Unit =
    spacing = 20.0
    padding = Insets(25.0)
    alignment = Pos.Center
    relocate(pos._1, pos._2)
    
    background = new scalafx.scene.layout.Background(Array(
      new scalafx.scene.layout.BackgroundFill(Color.Beige, new scalafx.scene.layout.CornerRadii(0), Insets.Empty)
    ))

    val activePlayers = playerBoards

    for i <- 0 until MaxPlayers do
      if i < activePlayers.size then
        val player = activePlayers(i)
        children.add(createActiveLane(player, player.board))
      else
        children.add(createEmptyLane())

  private def createActiveLane(player: Player, board: PersonalBoard): VBox =
    val (totalScore, details) = calculator.calculateDetailedScore(board, player.activeCards ::: player.completedCards)

    new VBox():
      spacing = 15.0
      padding = Insets(15.0)
      alignment = Pos.TopCenter
      minWidth = 160.0
      
      val nameText = new Text("pippo")
      nameText.font = Font.font("Arial", FontWeight.Bold, 16)
      
      val scoresGrid: GridPane = new GridPane():
        hgap = 10.0
        vgap = 12.0
        alignment = Pos.Center
        
        add(new Text("🌲 Foreste:"), 0, 0)
        add(new Text(details.getOrElse("Forest", 0).toString), 1, 0)

        add(new Text("⛰️ Montagne:"), 0, 1)
        add(new Text(details.getOrElse("Mountain", 0).toString), 1, 1)

        add(new Text("🌾 Campi:"), 0, 2)
        add(new Text(details.getOrElse("Field", 0).toString), 1, 2)

        add(new Text("🦎 Animali:"), 0, 3)
        add(new Text(details.getOrElse("Animal", 0).toString), 1, 3)

        add(new Text("💧 Acqua:"), 0, 4)
        add(new Text(details.getOrElse("Water", 0).toString), 1, 4)

      
      val totalBox: VBox = new VBox():
        alignment = Pos.Center
        padding = Insets(10, 0, 0, 0)

        val totalLabel = new Text("TOTALE")
        totalLabel.font = Font.font("Arial", FontWeight.Normal, 12)
        
        val totalScoreText = new Text(totalScore.toString)
        totalScoreText.font = Font.font("Arial", FontWeight.Bold, 22)

        children.addAll(totalLabel, totalScoreText)

      children.addAll(nameText, scoresGrid, totalBox)

  private def createEmptyLane(): VBox =
    new VBox():
      spacing = 15.0
      padding = Insets(15.0)
      alignment = Pos.TopCenter
      minWidth = 160.0

      val nameText = new Text("---")
      nameText.font = Font.font("Arial", FontWeight.Bold, 16)

      val emptyText = new Text("Slot Libero")
      emptyText.font = Font.font("Arial", 12)
      emptyText.fill = Color.Gray

      children.addAll(nameText, emptyText)
  
  setContainer()