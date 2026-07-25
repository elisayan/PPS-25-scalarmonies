package it.unibo.view.scorecalculator

import it.unibo.controller.GameController
import it.unibo.model.Player
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.ScoreCalculator
import it.unibo.model.token.TerrainToken._
import it.unibo.view.token.TokenView
import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.Background
import scalafx.scene.layout.BackgroundFill
import scalafx.scene.layout.Border
import scalafx.scene.layout.BorderStroke
import scalafx.scene.layout.BorderStrokeStyle
import scalafx.scene.layout.BorderWidths
import scalafx.scene.layout.CornerRadii
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight
import scalafx.scene.text.Text

case class ScoreCalculatorView(
    playerBoards: List[Player],
    pos: (Double, Double) = (0.0, 0.0),
    calculator: ScoreCalculator,
    controller: GameController
) extends HBox:

  private val MaxPlayers = 4

  private def setContainer(): Unit =
    spacing = 25.0
    padding = Insets(30.0)
    alignment = Pos.Center
    relocate(pos._1, pos._2)

    background = new Background(
      Array(
        new BackgroundFill(Color.Beige, new CornerRadii(15), Insets.Empty)
      )
    )

    val activePlayers = playerBoards

    for i <- 0 until MaxPlayers do
      if i < activePlayers.size then
        val player = activePlayers(i)
        children.add(createActiveLane(player, player.board))
      else children.add(createEmptyLane())

  private def createActiveLane(player: Player, board: PersonalBoard): VBox =
    val (totalScore, details) = calculator.calculateDetailedScore(
      board,
      player.activeCards ::: player.completedCards
    )

    new VBox():
      spacing = 20.0
      padding = Insets(20.0)
      alignment = Pos.TopCenter
      minWidth = 180.0

      background = new Background(
        Array(
          new BackgroundFill(Color.White, new CornerRadii(12), Insets.Empty)
        )
      )

      border = new Border(
        new BorderStroke(
          Color.LightGrey,
          BorderStrokeStyle.Solid,
          new CornerRadii(12),
          new BorderWidths(1)
        )
      )

      effect = new DropShadow(8.0, 0.0, 4.0, Color.gray(0.0, 0.15))

      val nameText = new Text("pippo".toUpperCase)
      nameText.font = Font.font("Arial", FontWeight.Bold, 16)
      nameText.fill = Color.Black

      val scoresGrid: GridPane = new GridPane():
        hgap = 15.0
        vgap = 12.0
        alignment = Pos.Center

        add(
          createTerrainBox(
            TokenView(Forest, _ => ()),
            details.getOrElse("Forest", 0).toString
          ),
          0,
          0
        )
        add(
          createTerrainBox(
            TokenView(Mountain, _ => ()),
            details.getOrElse("Mountain", 0).toString
          ),
          0,
          1
        )
        add(
          createTerrainBox(
            TokenView(Field, _ => ()),
            details.getOrElse("Field", 0).toString
          ),
          0,
          2
        )
        add(
          createTerrainBox(
            TokenView(Water, _ => ()),
            details.getOrElse("Water", 0).toString
          ),
          0,
          3
        )
        add(
          createTerrainBox(
            TokenView(Building, _ => ()),
            details.getOrElse("Building", 0).toString
          ),
          0,
          4
        )

        val animalBox: HBox = new HBox():
          spacing = 8.0
          alignment = Pos.CenterLeft
          val animalText = new Text("Animali:")
          animalText.font = Font.font("Arial", FontWeight.Normal, 13)
          val animalScore = new Text(details.getOrElse("Animal", 0).toString)
          animalScore.font = Font.font("Arial", FontWeight.Bold, 14)
          children.addAll(animalText, animalScore)

        GridPane.setColumnSpan(animalBox, 2)
        add(animalBox, 0, 5)

      val totalBox: VBox = new VBox():
        alignment = Pos.Center
        padding = Insets(15, 0, 0, 0)

        border = new Border(
          new BorderStroke(
            Color.LightGrey,
            BorderStrokeStyle.Solid,
            CornerRadii.Empty,
            new BorderWidths(1, 0, 0, 0)
          )
        )

        val totalLabel = new Text("TOTALE")
        totalLabel.font = Font.font("Arial", FontWeight.Normal, 11)
        totalLabel.fill = Color.Gray

        val totalScoreText = new Text(totalScore.toString)
        totalScoreText.font = Font.font("Arial", FontWeight.Bold, 26)
        totalScoreText.fill = Color.DarkGreen

        children.addAll(totalLabel, totalScoreText)

      children.addAll(nameText, scoresGrid, totalBox)

  private def createTerrainBox(tokenView: TokenView, score: String): HBox =
    new HBox():
      spacing = 8.0
      alignment = Pos.CenterLeft

      tokenView.setScaleX(0.65)
      tokenView.setScaleY(0.65)

      val scoreText = new Text(score)
      scoreText.font = Font.font("Arial", FontWeight.Bold, 14)
      scoreText.fill = Color.DarkSlateGray

      children.addAll(tokenView, scoreText)

  private def createEmptyLane(): VBox =
    new VBox():
      spacing = 15.0
      padding = Insets(20.0)
      alignment = Pos.Center
      minWidth = 180.0
      opacity = 0.5

      background = new Background(
        Array(
          new BackgroundFill(Color.Silver, new CornerRadii(12), Insets.Empty)
        )
      )

      border = new Border(
        new BorderStroke(
          Color.Gray,
          BorderStrokeStyle.Dashed,
          new CornerRadii(12),
          new BorderWidths(2)
        )
      )

      val nameText = new Text("---")
      nameText.font = Font.font("Arial", FontWeight.Bold, 16)
      nameText.fill = Color.Gray

      val emptyText = new Text("Slot Libero")
      emptyText.font = Font.font("Arial", 12)
      emptyText.fill = Color.Gray

      children.addAll(nameText, emptyText)

  setContainer()
