package it.unibo.view.cell

import it.unibo.model.cell.Cell
import it.unibo.model.personalBoard.Coordinate
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Polygon
import scalafx.scene.text.Font
import scalafx.scene.text.Text
import scalafx.Includes._

case class CellView(
    coordinate: Coordinate,
    cell: Cell,
    pos: (Double, Double) = (0.0, 0.0),
    onCellClicked: String => Unit
) extends StackPane:

  private val hexagon: Polygon = Polygon()

  hexagon.getPoints.addAll(
    0.0, -20.2, 17.5, -10.1, 17.5, 10.1, 0.0, 20.2, -17.5, 10.1, -17.5, -10.1
  )

  hexagon.fill = Color.LightGrey
  hexagon.stroke = Color.Black
  hexagon.strokeWidth = 3.0
  hexagon.rotate = 90.0
  relocate(pos._1, pos._2)

  private val label = new Text()
  label.text = s"${coordinate.x}, ${coordinate.y}"
  label.fill = Color.Black
  label.font = Font.font("Arial", 12)
  label.mouseTransparent = true

  hexagon.onMouseClicked = (event: MouseEvent) =>
    onCellClicked(
      s"cliccato esagono con coordinata ${coordinate.x}, ${coordinate.y}"
    )

  def updateState(newCell: Cell): Unit =
    println(
      s"Aggiornamento cella $coordinate nel view con i nuovi dati del modello"
    )

  def cellIsEligible(): Unit =
    hexagon.stroke = Color.Green

  def revertBack(): Unit =
    hexagon.stroke = Color.Black

  children.addAll(hexagon, label)
