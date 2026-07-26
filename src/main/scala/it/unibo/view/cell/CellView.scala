package it.unibo.view.cell

import it.unibo.controller.GameController
import it.unibo.model.cell.Cell
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken
import it.unibo.view.token.TokenView
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Polygon
import scalafx.scene.text.Font
import scalafx.scene.text.Text

case class CellView(
    coordinate: Coordinate,
    var cell: Cell,
    pos: (Double, Double) = (0.0, 0.0),
    onCellClicked: Coordinate => Unit,
    highlighted: Boolean
) extends StackPane:

  private val hexagon: Polygon = Polygon()
  private val label = new Text()
  children.addAll(hexagon, label)
  pickOnBounds = true
  onMouseClicked = (_: MouseEvent) => onCellClicked(coordinate)

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

    hexagon.mouseTransparent = true

    relocate(pos._1, pos._2)
    label.text = s""
    label.fill = Color.Black
    label.font = Font.font("Arial", 12)
    label.mouseTransparent = true
    placeTokenView()

  private def highlight(enabled: Boolean): Unit =
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
    val tv = TokenView(token, _ => ())
    tv.setScaleX(0.8)
    tv.setScaleY(0.8)
    tv.setTranslateY(offsetY)

    tv.mouseTransparent = true
    tv

  setHexagon()
  highlight(highlighted)
