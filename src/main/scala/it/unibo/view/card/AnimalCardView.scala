package it.unibo.view.card

import it.unibo.model.card.AnimalCard
import it.unibo.model.card.Habitat
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken
import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout._

object AnimalCardView:

  private object Layout:
    val PaddingRatio = 0.055
    val ImgSizeRatio = 0.40
    val HabWidthRatio = 0.66
    val HabHeightRatio = 0.40
    val CubeSizeRatio = 0.12
    val SpacingRatio = 0.035
    val TrackTopPadRatio = 0.09
    val MainFontRatio = 0.06
    val SmallFontRatio = 0.05
    val DeltaXRatio = 0.22
    val HexAspectRatio = 36.0 / 32.0
    val HexPoint1 = 0.25
    val HexPoint2 = 0.75
    val ConcentricScale = 0.22
    val AnimalCubeRatio = 0.35

  def apply(
      card: AnimalCard,
      cardWidth: Double = 180.0,
      cardHeight: Double = 280.0
  ): Node =
    val paddingSize = cardWidth * Layout.PaddingRatio
    val imgSize = cardWidth * Layout.ImgSizeRatio
    val habW = cardWidth * Layout.HabWidthRatio
    val habH = cardHeight * Layout.HabHeightRatio
    val cubeSize = cardWidth * Layout.CubeSizeRatio
    val spacingVBox = cardHeight * Layout.SpacingRatio
    val trackPadTop = cardHeight * Layout.TrackTopPadRatio
    val mainFont = cardWidth * Layout.MainFontRatio
    val deltaX = habW * Layout.DeltaXRatio
    val deltaY = deltaX * Layout.HexAspectRatio
    val hexWidth = deltaX / Layout.HexPoint2
    val hexHeight = deltaY
    val hexShape =
      s"M ${hexWidth * Layout.HexPoint1} 0 L ${hexWidth * Layout.HexPoint2} 0 L $hexWidth ${hexHeight / 2.0} L ${hexWidth * Layout.HexPoint2} $hexHeight L ${hexWidth * Layout.HexPoint1} $hexHeight L 0 ${hexHeight / 2.0} Z"

    new BorderPane:
      padding = Insets(paddingSize)
      val bgColor: String = extractBgColor(card.habitat)
      style =
        s"-fx-background-color: $bgColor; -fx-border-color: #2c3e50; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;"
      prefWidth = cardWidth
      prefHeight = cardHeight
      center = new VBox:
        alignment = Pos.TopCenter
        spacing = spacingVBox
        children = Seq(
          renderImage(card.imageId, imgSize, mainFont),
          new Region:
            vgrow = Priority.Always
          ,
          renderHabitat(
            card.habitat,
            habW,
            habH,
            hexWidth,
            hexHeight,
            hexShape,
            deltaX,
            deltaY
          )
        )
      right =
        renderScoringTrack(card, cubeSize, trackPadTop, paddingSize, mainFont)

  private def renderImage(
      imageId: String,
      imgSize: Double,
      fontSize: Double
  ): Node =
    val imageUrl = getClass.getResource(s"/animal/$imageId")
    if imageUrl != null then
      val fxImage = new Image(imageUrl.toURI.toString)
      if fxImage.delegate.isError then
        new Label("Img Err"):
          style = s"-fx-text-fill: orange; -fx-font-size: ${fontSize}px;"
      else
        new ImageView(fxImage):
          fitWidth = imgSize
          fitHeight = imgSize
          preserveRatio = true
    else
      new Label(s"Path Err"):
        style = s"-fx-text-fill: red; -fx-font-size: ${fontSize}px;"

  private def renderHabitat(
      habitat: Habitat,
      habW: Double,
      habH: Double,
      hexW: Double,
      hexH: Double,
      hexShape: String,
      dX: Double,
      dY: Double
  ): Node =
    new Pane:
      prefWidth = habW
      prefHeight = habH
      style =
        "-fx-background-color: rgba(255, 255, 255, 0.5); -fx-border-color: #555; -fx-border-style: dashed; -fx-background-radius: 5;"
      val sortedReqs =
        habitat.requirements.sortBy(req => toCartesianY(req.offset, dY))
      children = sortedReqs.map: req =>
        new StackPane:
          val pixelX = toCartesianX(req.offset, dX)
          val pixelY = toCartesianY(req.offset, dY)
          layoutX = (habW / 2.0) + pixelX - (hexW / 2.0)
          layoutY = (habH / 2.0) + pixelY - (hexH / 2.0)
          prefWidth = hexW
          prefHeight = hexH
          val tokens = (0 until req.height).map: i =>
            val scale = 1.0 - (i * Layout.ConcentricScale)
            val hexColor =
              if req.terrain == TerrainToken.Forest && i < req.height - 1 then
                "#8b5a2b"
              else tokenToCssHex(req.terrain)
            new Region:
              prefWidth = hexW * scale
              prefHeight = hexH * scale
              maxWidth = hexW * scale
              maxHeight = hexH * scale
              style = s"-fx-background-color: $hexColor; " +
                s"-fx-shape: \"$hexShape\"; " +
                s"-fx-border-color: #2c3e50; -fx-border-width: 1px;"
          val animalCube = if req.offset == Coordinate(0, 0) then
            val cSize = hexW * Layout.AnimalCubeRatio
            Seq(new Region:
              prefWidth = cSize
              prefHeight = cSize
              maxWidth = cSize
              maxHeight = cSize
              style =
                "-fx-background-color: #e6b981; -fx-border-color: #5e3a18; -fx-border-width: 2px; -fx-background-radius: 3;"
            )
          else Seq.empty
          children = tokens ++ animalCube

  private def renderScoringTrack(
      card: AnimalCard,
      cubeSize: Double,
      padTop: Double,
      padLeft: Double,
      fontSize: Double
  ): Node =
    new VBox:
      alignment = Pos.TopRight
      spacing = 8
      padding = Insets(padTop, 0, 0, padLeft)
      children = card.points.zipWithIndex.reverse.map:
        (pointValue, originalIndex) =>
          val isPlaced = originalIndex < card.placedCubes
          new VBox:
            alignment = Pos.Center
            spacing = 10
            children = Seq(
              new StackPane:
                prefWidth = cubeSize
                prefHeight = cubeSize
                style =
                  if isPlaced then
                    "-fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 4; -fx-border-color: #2c3e50; -fx-border-radius: 4; -fx-border-style: dashed;"
                  else
                    "-fx-background-color: #a52a2a; -fx-background-radius: 4; -fx-border-color: #2c3e50; -fx-border-radius: 4;"
              ,
              new Label(pointValue.toString):
                style =
                  s"-fx-font-weight: bold; -fx-text-fill: #333333; -fx-font-size: ${fontSize}px;"
            )

  private def extractBgColor(habitat: Habitat): String =
    habitat.requirements
      .find(_.offset == Coordinate(0, 0))
      .map(req => tokenToCssHex(req.terrain))
      .getOrElse("#ffffff")

  private def tokenToCssHex(terrain: TerrainToken): String = terrain match
    case TerrainToken.Water    => "#aaddff"
    case TerrainToken.Field    => "#ffeb99"
    case TerrainToken.Mountain => "#cccccc"
    case TerrainToken.Forest   => "#aaffaa"
    case TerrainToken.Building => "#b222222"
    case _                     => "#ffffff"

  private def toCartesianX(coord: Coordinate, dX: Double): Double =
    coord.x * (dX / 2.0)
  private def toCartesianY(coord: Coordinate, dY: Double): Double =
    -coord.y * (dY / 2.0)
