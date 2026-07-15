package it.unibo.view.card

import scalafx.Includes.*
import scalafx.scene.Node
import scalafx.scene.layout.*
import scalafx.scene.control.Label
import scalafx.scene.image.{Image, ImageView}
import scalafx.geometry.{Insets, Pos}
import it.unibo.model.card.{AnimalCard, Habitat}
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken

object AnimalCardView:
  def apply(card: AnimalCard): Node =
    new BorderPane:
      padding = Insets(10)
      val bgColor: String = extractBgColor(card.habitat)
      style = s"-fx-background-color: $bgColor; -fx-border-color: #2c3e50; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;"
      prefWidth = 180
      prefHeight = 280
      center = new VBox:
        alignment = Pos.TopCenter
        spacing = 10
        children = Seq(
          renderImage(card.imageId),
          new Region { vgrow = Priority.Always },
          renderHabitat(card.habitat)
        )
      right = renderScoringTrack(card)

  private def renderImage(imageId: String): Node =
    val imageStream = getClass.getResourceAsStream(s"/images/$imageId")
    if imageStream != null then
      new ImageView:
        image = new Image(imageStream)
        fitWidth = 70
        fitHeight = 70
        preserveRatio = true
    else
      new Label(s"$imageId"):
        style = "-fx-text-fill: red; -fx-font-size: 11px;"

  private def renderHabitat(habitat: Habitat): Node =
    new Pane:
      prefHeight = 80
      prefWidth = 120
      style = "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-border-color: #555; -fx-border-style: dashed; -fx-background-radius: 5;"
      children = habitat.requirements.map: req =>
        new StackPane:
          val pixelX: Double = toCartesianX(req.offset)
          val pixelY: Double = toCartesianY(req.offset)
          layoutX = (120 / 2) + pixelX - 10
          layoutY = (80 / 2) + pixelY - 10
          prefWidth = 20
          prefHeight = 20
          style = s"-fx-background-color: ${tokenToCssHex(req.terrain)}; -fx-border-color: #2c3e50; -fx-border-radius: 10; -fx-background-radius: 10;"
          children = new Label(if req.height > 1 then req.height.toString else ""):
            style = "-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #333333;"

  private def renderScoringTrack(card: AnimalCard): Node =
    new VBox:
      alignment = Pos.TopRight
      spacing = 8
      padding = Insets(25, 0, 0, 10)
      children = card.points.zipWithIndex.reverse.map: (pointValue, originalIndex) =>
        val isPlaced = originalIndex < card.placedCubes
        new VBox:
          alignment = Pos.Center
          spacing = 10
          children = Seq(
            new StackPane:
              prefWidth = 22
              prefHeight = 22
              style = if isPlaced then
                "-fx-background-color: #8e44ad; -fx-background-radius: 4; -fx-border-color: #2c3e50; -fx-border-radius: 4;"
              else
                "-fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 4; -fx-border-color: #2c3e50; -fx-border-radius: 4; -fx-border-style: dashed;"
            ,
            new Label(pointValue.toString):
              style = "-fx-font-weight: bold; -fx-text-fill: #333333; -fx-font-size: 11px;"
          )

  private def extractBgColor(habitat: Habitat): String =
    habitat.requirements.find(_.offset == Coordinate(0, 0))
      .map(req => tokenToCssHex(req.terrain))
      .getOrElse("#ffffff")

  private def tokenToCssHex(terrain: TerrainToken): String = terrain match
    case TerrainToken.Water    => "#aaddff"
    case TerrainToken.Field    => "#ffeb99"
    case TerrainToken.Mountain => "#cccccc"
    case TerrainToken.Forest   => "#aaffaa"
    case TerrainToken.Building => "#b222222"
    case _                     => "#ffffff"

  // TODO: Da implementare la logica esagonale della plancia!
  private def toCartesianX(coord: Coordinate): Double = 0.0
  private def toCartesianY(coord: Coordinate): Double = 0.0