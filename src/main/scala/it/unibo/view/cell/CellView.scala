package it.unibo.view.cell

import it.unibo.model.cell.Cell
import it.unibo.model.personalBoard.Coordinate
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Polygon
import scalafx.scene.text.Font
import scalafx.scene.text.Text

case class CellView(
    coordinate: Coordinate,
    cell: Cell,
    pos: (Double, Double) = (0.0, 0.0)
) extends StackPane:

  private val hexagon: Polygon = Polygon()

  hexagon.getPoints.addAll(
    17.0, 0.0, 35.0, 10.0, 35.0, 30.0, 17.0, 40.0, 0.0, 30.0, 0.0, 10.0
  )
  hexagon.rotate = 90.0
  hexagon.fill = Color.LightGrey
  hexagon.stroke = Color.Black
  hexagon.strokeWidth = 1.0
  this.relocate(pos._1, pos._2)
  private val label = new Text;
  label.text = s"${coordinate.x}, ${coordinate.y}"
  label.fill = Color.Black
  label.font = Font.font("Arial", 12)

  children.addAll(hexagon, label)
