package it.unibo.view.personalboard

import it.unibo.controller.GameController
import it.unibo.model.cell.Cell
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.BoardSide.SideB
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.view.cell.CellView
import scalafx.scene.layout.Pane
import scalafx.scene.layout.StackPane

case class PersonalBoardView(board: PersonalBoard, controller: GameController)
    extends Pane:

  private val modelCells: List[(Coordinate, Cell)] = board.cells.toList

  private val orderedModelGroups: List[List[(Coordinate, Cell)]] =
    modelCells
      .groupBy { case (coord, _) => coord.x }
      .toList
      .sortBy { case (xKey, _) => xKey }
      .map { case (_, group) =>
        group.sortBy { case (coord, _) => -coord.y }
      }

  private val flattenedOrderedCells: List[(Coordinate, Cell)] =
    orderedModelGroups.flatten

  private val deltaX = 32.0
  private val deltaY = 36.0
  private val shiftY = 18.0
  private val side = board.getSide match
    case SideA => 5
    case SideB => 7

  private val pixelPositions: List[(Double, Double)] =
    val colHead = LazyList
      .iterate((100.0, 200.0, 0)) { case (startX, startY, colIdx) =>
        val nextCol = colIdx + 1
        if nextCol == 1 || nextCol == 3 || nextCol == 5 then
          (startX + deltaX, startY + shiftY, nextCol)
        else (startX + deltaX, startY - shiftY, nextCol)
      }
      .take(side)
      .toList
    orderedModelGroups.zip(colHead).flatMap {
      case (listColumns, (startX, startY, _)) =>
        listColumns.indices.map { rowIndex =>
          (startX, startY + (rowIndex * deltaY))
        }
    }

  private val cells: List[CellView] =
    flattenedOrderedCells.zip(pixelPositions).map {
      case ((coord, cell), (pixelX, pixelY)) =>
        val view = CellView(coord, cell, (pixelX, pixelY), controller)
        view
    }

  cells.foreach(c => children.add(c))
  cells.foreach(c => println(s"pos:${c.pos}"))
