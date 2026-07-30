package it.unibo.view.scorecalculator

import it.unibo.model.Player
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Score
import it.unibo.model.scorecalculator.ScoreCalculator
import it.unibo.model.token.TerrainToken._
import it.unibo.view.token.TokenView
import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight
import scalafx.scene.text.Text

case class ScoreCalculatorView(
    playerBoards: List[Player],
    pos: (Double, Double) = (0.0, 0.0)
) extends HBox:

  private val MaxPlayers = 4
  private val MaxAnimalCards = 8

  // Colori dei banner per i giocatori
  private val PlayerBannerColors = Seq(
    Color.web("#78A153"), // Verde per Giocatore 1
    Color.web("#D26A4A"), // Terracotta per Giocatore 2
    Color.web("#4A8CB7"), // Blu per Giocatore 3
    Color.web("#C49A3C") // Ocra per Giocatore 4
  )

  private def setContainer(): Unit =
    spacing = 15.0
    padding = Insets(20.0)
    alignment = Pos.Center
    maxHeight = Double.MaxValue
    relocate(pos._1, pos._2)

    // Sfondo pergamena/sabbia BGA
    background = new Background(
      Array(
        new BackgroundFill(
          Color.web("#E8DBB8"),
          new CornerRadii(16),
          Insets.Empty
        )
      )
    )

    effect = new DropShadow(12.0, 0.0, 6.0, Color.rgb(0, 0, 0, 0.25))

    val activePlayers = playerBoards

    for i <- 0 until MaxPlayers do
      if i < activePlayers.size then
        val player = activePlayers(i)
        val bannerColor = PlayerBannerColors(i % PlayerBannerColors.size)
        children.add(createActiveLane(player, player.board, bannerColor))
      else children.add(createEmptyLane())

  private def createActiveLane(
      player: Player,
      board: PersonalBoard,
      bannerColor: Color
  ): VBox =
    val (totalScore, details) = ScoreCalculator.calculateDetailedScore(
      board,
      player.activeCards ::: player.completedCards
    )

    val forestPts = details.getOrElse("Forest", Score.zero).toInt
    val mountainPts = details.getOrElse("Mountain", Score.zero).toInt
    val fieldPts = details.getOrElse("Field", Score.zero).toInt
    val buildingPts = details.getOrElse("Building", Score.zero).toInt
    val waterPts = details.getOrElse("Water", Score.zero).toInt

    val totalTerrainsPts =
      forestPts + mountainPts + fieldPts + buildingPts + waterPts

    val completedCards = player.completedCards ::: player.activeCards
    val cardScores: List[Int] =
      completedCards.map(c => c.computeScore(Some(board)).toInt)
    val totalAnimalsPts = cardScores.sum

    new VBox():
      spacing = 12.0
      padding = Insets(12.0)
      alignment = Pos.TopCenter
      minWidth = 190.0
      maxWidth = 200.0
      maxHeight = Double.MaxValue
      vgrow = Priority.Always

      background = new Background(
        Array(
          new BackgroundFill(
            Color.web("#D1AF80"),
            new CornerRadii(14),
            Insets.Empty
          )
        )
      )

      border = new Border(
        new BorderStroke(
          Color.web("#A88354"),
          BorderStrokeStyle.Solid,
          new CornerRadii(14),
          new BorderWidths(1.5)
        )
      )

      effect = new DropShadow(6.0, 0.0, 3.0, Color.rgb(0, 0, 0, 0.18))

      val bannerBox: StackPane = new StackPane():
        padding = Insets(5, 10, 5, 10)
        background = new Background(
          Array(
            new BackgroundFill(bannerColor, new CornerRadii(8), Insets.Empty)
          )
        )
        val nameText = new Text(player.name)
        nameText.font = Font.font("Georgia", FontWeight.Bold, 15)
        nameText.fill = Color.White
        children.add(nameText)

      val leftColumn: VBox = new VBox():
        alignment = Pos.Center
        vgrow = Priority.Always
        children.addAll(
          createTerrainGroup(
            TokenView(Forest, _ => ()),
            Color.web("#8CB755"),
            forestPts
          ),
          createTerrainGroup(
            TokenView(Mountain, _ => ()),
            Color.web("#8F99A4"),
            mountainPts
          ),
          createTerrainGroup(
            TokenView(Field, _ => ()),
            Color.web("#E8C239"),
            fieldPts
          ),
          createTerrainGroup(
            TokenView(Building, _ => ()),
            Color.web("#D75C5C"),
            buildingPts
          ),
          createTerrainGroup(
            TokenView(Water, _ => ()),
            Color.web("#3FA3D2"),
            waterPts
          )
        )

      VBox.setVgrow(leftColumn, Priority.Always)

      val verticalDivider: Region = new Region():
        minWidth = 2.0
        maxWidth = 2.0
        vgrow = Priority.Always
        background = new Background(
          Array(
            new BackgroundFill(
              Color.web("#8A6B43"),
              CornerRadii.Empty,
              Insets.Empty
            )
          )
        )

      val rightColumn: VBox = createAnimalSection(cardScores)

      val columnsContainer: HBox = new HBox():
        spacing = 10.0
        alignment = Pos.Center
        vgrow = Priority.Always
        maxHeight = Double.MaxValue
        children.addAll(leftColumn, verticalDivider, rightColumn)

      VBox.setVgrow(columnsContainer, Priority.Always)

      val summaryBox: VBox = new VBox():
        spacing = 2.0
        alignment = Pos.Center
        padding = Insets(6, 0, 2, 0)

        val sumRow: HBox = new HBox():
          spacing = 6.0
          alignment = Pos.Center

          val terrainBadge: StackPane =
            createPebbleBadge(totalTerrainsPts.toString, 13)
          val plusText = new Text("+")
          plusText.font = Font.font("Georgia", FontWeight.Bold, 14)
          plusText.fill = Color.web("#4A3728")

          val animalBadge: StackPane =
            createPebbleBadge(totalAnimalsPts.toString, 13)

          children.addAll(terrainBadge, plusText, animalBadge)

        val totalRow: HBox = new HBox():
          spacing = 6.0
          alignment = Pos.Center

          val equalsText = new Text("=")
          equalsText.font = Font.font("Georgia", FontWeight.Bold, 15)
          equalsText.fill = Color.web("#4A3728")

          val grandTotalBadge: StackPane =
            createPebbleBadge(totalScore.toInt.toString, 17, isBold = true)

          children.addAll(equalsText, grandTotalBadge)

        children.addAll(sumRow, totalRow)

      children.addAll(bannerBox, columnsContainer, summaryBox)

  private def createTerrainGroup(
      tokenView: TokenView,
      badgeColor: Color,
      score: Int
  ): VBox =
    new VBox():
      spacing = 2.0
      padding = Insets(4.0, 0, 4.0, 0)
      alignment = Pos.Center

      tokenView.setScaleX(0.70)
      tokenView.setScaleY(0.70)

      val badge: StackPane = new StackPane():
        minWidth = 34.0
        minHeight = 34.0
        maxWidth = 34.0
        maxHeight = 34.0
        background = new Background(
          Array(
            new BackgroundFill(badgeColor, new CornerRadii(17), Insets.Empty)
          )
        )
        effect = new DropShadow(3.0, 0.0, 1.5, Color.rgb(0, 0, 0, 0.2))

        val scoreText = new Text(score.toString)
        scoreText.font = Font.font("Georgia", FontWeight.Bold, 14)
        scoreText.fill = Color.web("#1A2416")

        children.add(scoreText)

      children.addAll(tokenView, badge)

  private def createAnimalSection(cardScores: List[Int]): VBox =
    new VBox():
      spacing = 25.0
      alignment = Pos.Center
      vgrow = Priority.Always

      val headerIcon = new Text("🐾")
      headerIcon.font = Font.font(14)

      val slots: Seq[StackPane] = (0 until MaxAnimalCards).map { index =>
        val scoreOpt = cardScores.lift(index)
        createAnimalCardSlot(scoreOpt)
      }

      children.add(headerIcon)
      slots.foreach(s => children.add(s))

  private def createAnimalCardSlot(scoreOpt: Option[Int]): StackPane =
    new StackPane():

      minWidth = 44.0
      maxWidth = 44.0
      minHeight = 22.0
      maxHeight = 22.0

      scoreOpt match
        case Some(score) =>
          background = new Background(
            Array(
              new BackgroundFill(
                Color.web("#FFFDF9"),
                new CornerRadii(6),
                Insets.Empty
              )
            )
          )
          border = new Border(
            new BorderStroke(
              Color.web("#C2B49D"),
              BorderStrokeStyle.Solid,
              new CornerRadii(6),
              new BorderWidths(1)
            )
          )
          effect = new DropShadow(2.0, 0.0, 1.0, Color.rgb(0, 0, 0, 0.15))

          val scoreText = new Text(score.toString)
          scoreText.font = Font.font("Georgia", FontWeight.Bold, 12)
          scoreText.fill = Color.web("#2C220E")

          children.add(scoreText)

        case None =>
          background = new Background(
            Array(
              new BackgroundFill(
                Color.web("#BE9A6B"),
                new CornerRadii(6),
                Insets.Empty
              )
            )
          )
          border = new Border(
            new BorderStroke(
              Color.web("#A88354"),
              BorderStrokeStyle.Solid,
              new CornerRadii(6),
              new BorderWidths(1)
            )
          )

  private def createPebbleBadge(
      value: String,
      fontSize: Int,
      isBold: Boolean = false
  ): StackPane =
    new StackPane():
      padding = Insets(3, 10, 3, 10)
      minWidth = 40.0
      background = new Background(
        Array(
          new BackgroundFill(
            Color.web("#FFFDF9"),
            new CornerRadii(12),
            Insets.Empty
          )
        )
      )
      border = new Border(
        new BorderStroke(
          Color.web("#D1C4B0"),
          BorderStrokeStyle.Solid,
          new CornerRadii(12),
          new BorderWidths(1)
        )
      )
      effect = new DropShadow(2.0, 0.0, 1.0, Color.rgb(0, 0, 0, 0.12))

      val txt = new Text(value)
      txt.font = Font.font(
        "Georgia",
        if isBold then FontWeight.Bold else FontWeight.Normal,
        fontSize
      )
      txt.fill = Color.web("#2A2A2A")

      children.add(txt)

  private def createEmptyLane(): VBox =
    new VBox():
      spacing = 10.0
      padding = Insets(12.0)
      alignment = Pos.Center
      minWidth = 190.0
      maxWidth = 200.0
      maxHeight = Double.MaxValue
      vgrow = Priority.Always

      background = new Background(
        Array(
          new BackgroundFill(
            Color.web("#CBB08C", 0.5),
            new CornerRadii(14),
            Insets.Empty
          )
        )
      )

      border = new Border(
        new BorderStroke(
          Color.web("#B59B79"),
          BorderStrokeStyle.Dashed,
          new CornerRadii(14),
          new BorderWidths(1.5)
        )
      )

      val nameText = new Text("---")
      nameText.font = Font.font("Georgia", FontWeight.Bold, 14)
      nameText.fill = Color.web("#8C765C")

      val emptyText = new Text("Slot Libero")
      emptyText.font = Font.font("Georgia", 12)
      emptyText.fill = Color.web("#8C765C")

      children.addAll(nameText, emptyText)

  setContainer()
