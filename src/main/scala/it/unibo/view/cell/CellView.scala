package it.unibo.view.cell

import it.unibo.model.cell.Cell
import it.unibo.model.personalBoard.Coordinate
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Polygon

case class CellView(coordinate: Coordinate, cell: Cell) extends StackPane:

  private val hexagon: Polygon = Polygon()

  hexagon.getPoints.addAll(
    17.0, 0.0,
    35.0, 10.0,
    35.0, 30.0,
    17.0, 40.0,
    0.0, 30.0,
    0.0, 10.0
  )
  hexagon.rotate = 90.0
  hexagon.fill = Color.LightGrey
  hexagon.stroke = Color.Black
  hexagon.strokeWidth = 2.0

  children.add(hexagon)
