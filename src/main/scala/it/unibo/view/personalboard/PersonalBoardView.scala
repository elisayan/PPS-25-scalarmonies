package it.unibo.view.personalboard
import it.unibo.model.Player
import it.unibo.model.cell.Cell
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.BoardSide.SideB
import it.unibo.model.personalboard.Coordinate
import it.unibo.view.cell.CellView
import scalafx.scene.layout.Pane
import scalafx.scene.layout.StackPane

case class PersonalBoardView(
    player: Player,
    onCellClicked: Coordinate => Unit,
    highlightedCells: List[Coordinate]
) extends Pane:

  private val modelCells: List[(Coordinate, Cell)] = player.board.cells.toList

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

  private val deltaX = 40.0
  private val deltaY = 45.0
  private val shiftY = 22.0
  private val side = player.board.getSide match
    case SideA => 5
    case SideB => 7

  private val offsetX = 10.0
  private val offsetY = 10.0

  private val pixelPositions: List[(Double, Double)] =
    val colHead = LazyList
      .iterate((offsetX, offsetY, 0)) { case (startX, startY, colIdx) =>
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
        val view = CellView(
          coord,
          cell,
          (pixelX, pixelY),
          onCellClicked,
          highlightedCells.contains(coord)
        )
        view
    }

  cells.foreach(c => children.add(c))

  private val maxPixelX =
    pixelPositions.map(_._1).maxOption.getOrElse(200.0) + 40.0
  private val maxPixelY =
    pixelPositions.map(_._2).maxOption.getOrElse(200.0) + 45.0
  this.prefWidth = maxPixelX
  this.minWidth = maxPixelX
  this.prefHeight = maxPixelY
  this.minHeight = maxPixelY
