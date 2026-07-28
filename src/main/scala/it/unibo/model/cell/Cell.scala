package it.unibo.model.cell

import it.unibo.model.token.TerrainToken

/** Represents a cell on the personal board.
  *
  * @param tokens
  *   the list of tokens placed in this cell
  * @param hasAnimal
  *   whether the cell contains an animal cube or not
  */
case class Cell(
    private val tokens: List[TerrainToken] = List(),
    hasAnimal: Boolean = false
):

  /** Checks whether this cell contains any terrain tokens.
    *
    * @return
    *   `true` if there is at least one token, `false` otherwise
    */
  def hasTokens: Boolean = tokens.nonEmpty

  /** Returns the list of tokens currently placed in this cell.
    *
    * @return
    *   the list of TerrainToken instances
    */
  def getTokens: List[TerrainToken] = tokens

  /** Places a new terrain token on top of the existing stack in this cell.
    *
    * @param token
    *   the TerrainToken to place
    * @return
    *   a new Cell instance containing the added token
    */
  def placeToken(token: TerrainToken): Cell =
    Cell(tokens :+ token)

  /** Returns the topmost token placed in this cell, if any.
    *
    * @return
    *   an Option containing the top terrainToken, or None if the
    *   cell is empty
    */
  def topToken: Option[TerrainToken] = tokens.lastOption

  /** Returns the height of this cell, corresponding to the total number of
    * placed tokens.
    *
    * @return
    *   the number of tokens stacked in this cell
    */
  def height: Int = tokens.size

  /** Occupies this cell with an animal cube if it is valid to do so.
    *
    * @return
    *   an Option containing the updated Cell with the animal cube, or
    *   None if the cell has no tokens or is already occupied by an animal
    */
  def occupyWithAnimal: Option[Cell] =
    if tokens.isEmpty || hasAnimal then None
    else Some(this.copy(hasAnimal = true))

/** Factory object for creating Cell instances. */
object Cell:
  /** Creates a default empty Cell with no tokens and no animal cube.
    *
    * @return
    *   a new empty Cell
    */
  def apply(): Cell = Cell(List.empty)
