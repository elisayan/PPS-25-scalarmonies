package it.unibo.view.card

import it.unibo.model.card.AnimalCard
import it.unibo.model.card.Habitat
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.shape.Rectangle

private object CardTheme:
  val PadRatio = 0.055
  val RightW = 0.25
  val SpacingW = 0.05
  val ImgH = 0.64
  val HabH = 0.32
  val CubeSize = 0.10
  val DeltaX = 0.23
  val ConScale = 0.22
  val HabOffsetX = 0.05
  val HabOffsetY = -0.03

  def tokenColor(t: TerrainToken): String = t match
    case TerrainToken.Water    => "#aaddff"
    case TerrainToken.Field    => "#ffeb99"
    case TerrainToken.Mountain => "#cccccc"
    case TerrainToken.Forest   => "#aaffaa"
    case TerrainToken.Building => "#b22222"
    case _                     => "#ffffff"

  def bgColor(t: TerrainToken): String = t match
    case TerrainToken.Water    => "#e0f2ff"
    case TerrainToken.Field    => "#fff8d6"
    case TerrainToken.Mountain => "#ebebeb"
    case TerrainToken.Forest   => "#e6faea"
    case TerrainToken.Building => "#fadcdc"
    case _                     => "#ffffff"

  def extractCardBg(h: Habitat): String =
    h.requirements
      .find(_.offset == Coordinate(0, 0))
      .map(r => bgColor(r.terrain))
      .getOrElse("#ffffff")

private object HexMath:
  def x(c: Coordinate, dX: Double): Double = c.x * (dX / 2.0)
  def y(c: Coordinate, dY: Double): Double = -c.y * (dY / 2.0)
  def shape(w: Double, h: Double): String =
    s"M ${w * 0.25} 0 L ${w * 0.75} 0 L $w ${h / 2.0} L ${w * 0.75} $h L ${w * 0.25} $h L 0 ${h / 2.0} Z"

private object CardComponents:

  def imageBox(id: String, w: Double, h: Double): Node =
    val url = getClass.getResource(s"/animal/$id")
    new StackPane:
      prefWidth = w; prefHeight = h; alignment = Pos.Center
      if url != null then
        val img = new Image(url.toURI.toString)
        if !img.delegate.isError then
          children = new ImageView(img):
            fitWidth = w
            fitHeight = h
            preserveRatio = false
            clip = new Rectangle:
              width = w
              height = h
              arcWidth = 20
              arcHeight = 20
        else
          children = new Label("Img Err"):
            style = "-fx-text-fill: orange;"
      else
        children = new Label("Path Err"):
          style = "-fx-text-fill: red;"

  def scoringTrack(
      card: AnimalCard,
      w: Double,
      h: Double,
      baseCubeS: Double
  ): Node =
    val count = card.points.length
    val (cubeS, vSpacing, inSpacing) =
      if count > 4 then (baseCubeS * 0.85, 4.0, 4.0)
      else (baseCubeS, 8.0, 10.0)
    new VBox:
      prefWidth = w
      prefHeight = h
      alignment = Pos.TopCenter
      spacing = vSpacing
      padding = Insets(10, 0, 0, 0)
      children = card.points.zipWithIndex.reverse.map: (pts, idx) =>
        val isPlaced = idx < card.placedCubes
        val bg = if isPlaced then "rgba(0,0,0,0.12)" else "#a52a2a"
        val bStyle = if isPlaced then "dashed" else "solid"
        new VBox:
          alignment = Pos.Center
          spacing = inSpacing
          children = Seq(
            new StackPane:
              prefWidth = cubeS; minWidth = cubeS; maxWidth = cubeS
              prefHeight = cubeS; minHeight = cubeS; maxHeight = cubeS
              style =
                s"-fx-background-color: $bg; -fx-border-color: #2c3e50; -fx-border-style: $bStyle;"
            ,
            new Label(pts.toString):
              style = "-fx-font-weight: bold; -fx-text-fill: #333333;"
          )

  def habitatBox(
      hab: Habitat,
      w: Double,
      h: Double,
      dX: Double,
      dY: Double,
      hexW: Double,
      hexH: Double,
      svg: String
  ): Node =
    new Pane:
      prefWidth = w; prefHeight = h;
      style = "-fx-background-color: transparent;"
      children = hab.requirements
        .sortBy(r => HexMath.y(r.offset, dY))
        .map: req =>
          new StackPane:
            layoutX = (w / 2.0) + (w * CardTheme.HabOffsetX) + HexMath.x(
              req.offset,
              dX
            ) - (hexW / 2.0)
            layoutY = (h / 2.0) + (h * CardTheme.HabOffsetY) + HexMath.y(
              req.offset,
              dY
            ) - (hexH / 2.0)
            val tokens = (0 until req.height).map: i =>
              val scale = 1.0 - (i * CardTheme.ConScale)
              val col =
                if req.terrain == TerrainToken.Forest && i < req.height - 1 then
                  "#8b5a2b"
                else CardTheme.tokenColor(req.terrain)
              new Region:
                prefWidth = hexW * scale; prefHeight = hexH * scale;
                maxWidth = hexW * scale; maxHeight = hexH * scale;
                style =
                  s"-fx-background-color: $col; -fx-shape: \"$svg\"; -fx-border-color: #2c3e50;"
            val animal = if req.offset == Coordinate(0, 0) then
              val cS = hexW * 0.35
              Seq(new Region:
                prefWidth = cS; prefHeight = cS; maxWidth = cS; maxHeight = cS;
                style =
                  "-fx-background-color: #e6b981; -fx-border-color: #5e3a18; -fx-border-width: 2px;"
              )
            else Seq.empty
            children = tokens ++ animal

object AnimalCardView:
  def apply(
      card: AnimalCard,
      cardWidth: Double = 120.0,
      cardHeight: Double = 160.0
  ): Node =
    val pad = cardWidth * CardTheme.PadRatio
    val usableW = cardWidth - (pad * 2)
    val usableH = cardHeight - (pad * 2)
    val rightW = usableW * CardTheme.RightW
    val spaceW = usableW * CardTheme.SpacingW
    val leftW = usableW - rightW - spaceW
    val imgH = usableH * CardTheme.ImgH
    val habH = usableH * CardTheme.HabH
    val dX = leftW * CardTheme.DeltaX
    val dY = dX * (36.0 / 32.0)
    val hexW = dX / 0.75
    val hexH = dY
    val hexSvg = HexMath.shape(hexW, hexH)

    new HBox:
      padding = Insets(pad)
      spacing = spaceW
      style =
        s"-fx-background-color: ${CardTheme.extractCardBg(card.habitat)}; -fx-border-color: #2c3e50; -fx-border-width: 2; -fx-border-radius: 8;"
      prefWidth = cardWidth; prefHeight = cardHeight
      children = Seq(
        new VBox:
          prefWidth = leftW
          alignment = Pos.TopCenter
          children = Seq(
            CardComponents.imageBox(card.imageId, leftW, imgH),
            new Region:
              vgrow = Priority.Always
            ,
            CardComponents
              .habitatBox(card.habitat, leftW, habH, dX, dY, hexW, hexH, hexSvg)
          )
        ,
        CardComponents.scoringTrack(
          card,
          rightW,
          usableH,
          cardWidth * CardTheme.CubeSize
        )
      )
