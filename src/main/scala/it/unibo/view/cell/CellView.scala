package it.unibo.view.cell

import it.unibo.model.cell.Cell
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken
import it.unibo.view.token.TokenView
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Polygon
import scalafx.scene.text.Font
import scalafx.scene.text.Text
import scalafx.Includes._

case class CellView(
    coordinate: Coordinate,
    var cell: Cell,
    pos: (Double, Double) = (0.0, 0.0),
    onCellClicked: String => Unit // Aggiornato a Coordinate => Unit per essere type-safe
) extends StackPane:

  private val hexagon: Polygon = Polygon()
  private val label = new Text()
  children.addAll(hexagon, label)

  private def setHexagon(): Unit =
    hexagon.getPoints.addAll(
      0.0, -20.2, 17.5, -10.1, 17.5, 10.1, 0.0, 20.2, -17.5, 10.1, -17.5, -10.1
    )
    hexagon.fill = Color.LightGrey
    hexagon.stroke = Color.Black
    hexagon.strokeWidth = 1.0
    hexagon.rotate = 90.0
    hexagon.setScaleX(1.2)
    hexagon.setScaleY(1.2)
    relocate(pos._1, pos._2)
    label.text = s""
    label.fill = Color.Black
    label.font = Font.font("Arial", 12)
    label.mouseTransparent = true
    placeTokenView()

  hexagon.onMouseClicked = (event: MouseEvent) =>
    onCellClicked(
      "ciao" // Passa direttamente l'oggetto Coordinate tipato
      // triggera la chiamata a Controller che avvisa Model che deve distruggere e ricostruire nuovo mondo
    )

  def updateState(newCell: Cell): Unit =
    println(
      s"Aggiornamento cella $coordinate nel view con i nuovi dati del modello"
      // dopo aver costruito nuovo mondo faccio update linkando questa CellView a nuova Cell
      // con stesse Coordinate ma List[Token] AGGIORNATA
    )
    this.cell = newCell
    placeTokenView()

  def highlight(enabled: Boolean): Unit =
    if enabled then hexagon.fill = Color.LightGreen
    else hexagon.fill = Color.LightGrey

  private def placeTokenView(): Unit =
    val tokens = cell.getTokens
    val offsets = cell.height match
      case 1 => List(0.0)
      case 2 => List(3.0, -4.0)
      case 3 => List(7.0, 0.0, -7.0)
      case _ => Nil
    tokens.zip(offsets).foreach { (token, offsetY) =>
      val tv = createConfiguredTokenView(token, offsetY)
      children.add(tv)
    }

  private def createConfiguredTokenView(
      token: TerrainToken,
      offsetY: Double
  ): TokenView =
    val tv = TokenView(token)
    tv.setScaleX(0.8)
    tv.setScaleY(0.8)
    tv.setTranslateY(offsetY)
    tv

  setHexagon()
