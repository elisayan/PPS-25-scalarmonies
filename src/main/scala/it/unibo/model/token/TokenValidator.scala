package it.unibo.model.token

import it.unibo.model.cell.Cell
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.token.TerrainToken._

/** Validates terrain token placement according to the game rules. */
object TokenValidator:

  private val MaxStackHeight = 3
  private val findCoordinates =
    (board: PersonalBoard, predicate: Cell => Boolean) =>
      board.cells.collect {
        case (coordinate, cell) if predicate(cell) => coordinate
      }.toList

  /** Checks whether a terrain token can be placed on a cell.
    * @param token
    *   the terrain token to place.
    * @param cell
    *   the target cell.
    * @return
    *   true if the placement is valid, false otherwise.
    */
  def canPlace(token: TerrainToken, cell: Cell): Boolean =
    val height = cell.getTokens.length
    if height >= MaxStackHeight then false
    else if cell.hasAnimal then false
    else
      token match
        case Water | Field => !cell.hasTokens
        case Mountain => !cell.hasTokens || cell.topToken.contains(Mountain)
        case Forest   => !cell.hasTokens || cell.topToken.contains(Ground)
        case Ground =>
          !cell.hasTokens || (height == 1 && cell.topToken.contains(Ground))
        case Building =>
          !cell.hasTokens || (height == 1 && cell.topToken.exists {
            case Mountain | Ground | Building => true
            case _                            => false
          })

  /** Returns all valid placement coordinates for a terrain token.
    * @param token
    *   the terrain token to place.
    * @param board
    *   the player's board.
    * @return
    *   the list of valid coordinates.
    */
  def validPositions(
      token: TerrainToken,
      board: PersonalBoard
  ): List[Coordinate] =
    findCoordinates(board, canPlace(token, _))
