package it.unibo.model.token

import it.unibo.model.cell.Cell
import it.unibo.model.personalBoard.Coordinate
import it.unibo.model.personalBoard.PersonalBoard
import it.unibo.model.token.TerrainToken._

object TokenValidator:

  private val MaxStackHeight = 3

  def canPlace(token: TerrainToken, cell: Cell): Boolean =
    val height = cell.getTokens.length
    if height >= MaxStackHeight then false
    else
      token match
        case Water | Field => !cell.hasTokens
        case Mountain => !cell.hasTokens || cell.topToken.contains(Mountain)
        case Forest   => !cell.hasTokens || cell.topToken.contains(Ground)
        case Ground =>
          !cell.hasTokens || (height == 1 && cell.topToken.contains(Ground))
        case Building =>
          !cell.hasTokens || cell.topToken.exists {
            case Mountain | Ground | Building => true
            case _                            => false
          }

  def validPositions(
      token: TerrainToken,
      board: PersonalBoard
  ): List[Coordinate] =
    board.cells.collect {
      case (coordinate, cell) if canPlace(token, cell) => coordinate
    }.toList
