package it.unibo.view.personalBoard

import it.unibo.model.cell.Cell
import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}
import it.unibo.view.cell.CellView
import scalafx.scene.layout.{Pane, StackPane}

case class PersonalBoardView(board: PersonalBoard) extends Pane:

  private val pixelPositions: List[(Double, Double)] =
    LazyList
      .iterate((0.0, 0.0))((px, py) => (px, py + 25.0))
      .take(23)
      .toList

  private val modelCells: List[(Coordinate, Cell)] = board.cells.toList
  println(s"lenght: ${modelCells.length}")

  private val cells: List[CellView] =
    modelCells.zip(pixelPositions).map {
      case ((coord, cell), (pixelX, pixelY)) =>
        val view = CellView(coord, cell, (pixelX, pixelY))
        view
    }

  cells.foreach(c => children.add(c))
  cells.foreach(c => println(s"pos:${c.pos}"))
