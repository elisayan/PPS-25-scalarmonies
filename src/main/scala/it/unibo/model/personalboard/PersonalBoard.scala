package it.unibo.model.personalboard

import it.unibo.model.cell.Cell
import it.unibo.model.token.TerrainToken

/** Represents the layout side of the personal board being played.
  */
enum BoardSide:
  /** Side A configuration. */
  case SideA

  /** Side B configuration. */
  case SideB

/** Represents a player's personal board on a hexagonal grid.
  */
trait PersonalBoard:

  /** The height bound of the board grid. */
  def heightBound: Int

  /** The width bound of the board grid. */
  def widthBound: Int

  /** The total number of valid cells on the board. */
  def totalCells: Int

  /** The map associating each Coordinate to its corresponding Cell. */
  def cells: Map[Coordinate, Cell]

  /** The BoardSide layout used by this board. */
  def side: BoardSide

  /** Gets the northern neighboring Cell of the given coordinate, if present.
    *
    * @param c
    *   the target Coordinate
    * @return
    *   an Option containing the northern Cell, or None if out of bounds
    */
  def getNorthernNeighbour(c: Coordinate): Option[Cell]

  /** Gets the southern neighboring Cell of the given coordinate, if present.
    *
    * @param c
    *   the target Coordinate
    * @return
    *   an Option containing the southern Cell, or None if out of bounds
    */
  def getSouthernNeighbour(c: Coordinate): Option[Cell]

  /** Gets the south-eastern neighboring Cell of the given coordinate, if
    * present.
    *
    * @param c
    *   the target Coordinate
    * @return
    *   an Option containing the south-eastern Cell, or None if out of bounds
    */
  def getSouthEasternNeighbour(c: Coordinate): Option[Cell]

  /** Gets the south-western neighboring Cell of the given coordinate, if
    * present.
    *
    * @param c
    *   the target Coordinate
    * @return
    *   an Option containing the south-western Cell, or None if out of bounds
    */
  def getSouthWesternNeighbour(c: Coordinate): Option[Cell]

  /** Gets the north-eastern neighboring Cell of the given coordinate, if
    * present.
    *
    * @param c
    *   the target Coordinate
    * @return
    *   an Option containing the north-eastern Cell, or None if out of bounds
    */
  def getNorthEasternNeighbour(c: Coordinate): Option[Cell]

  /** Gets the north-western neighboring Cell of the given coordinate, if
    * present.
    *
    * @param c
    *   the target Coordinate
    * @return
    *   an Option containing the north-western Cell, or None if out of bounds
    */
  def getNorthWesternNeighbour(c: Coordinate): Option[Cell]

  /** Places a terrain token at the specified coordinate.
    *
    * @param token
    *   the TerrainToken to place
    * @param c
    *   the target Coordinate
    * @return
    *   an Option containing the updated PersonalBoard, or None if the
    *   coordinate is invalid
    */
  def placeToken(token: TerrainToken, c: Coordinate): Option[PersonalBoard]

  /** Returns the BoardSide layout of this board.
    *
    * @return
    *   the BoardSide layout
    */
  def getSide: BoardSide = side

  /** Places an animal cube on the cell at the specified coordinate.
    *
    * @param c
    *   the target Coordinate
    * @return
    *   an Option containing the updated PersonalBoard, or None if placement
    *   fails or cell is invalid
    */
  def placeAnimalOnCell(c: Coordinate): Option[PersonalBoard]

/** Companion object and factory for creating PersonalBoard instances.
  */
object PersonalBoard:

  private def generateHexGrid(
      widthBound: Int,
      heightBound: Int
  ): Map[Coordinate, Cell] =
    val validCoordinates = for
      x <- -widthBound to widthBound if x % 2 == 0
      y <- -heightBound to heightBound
      if (x / 2).abs % 2 == y.abs % 2
    yield Coordinate(x, y)
    validCoordinates.map(c => c -> Cell(List())).toMap

  /** Creates a new PersonalBoard initialized for the specified board side.
    *
    * @param side
    *   the BoardSide layout to build
    * @return
    *   a new PersonalBoard instance
    */
  def apply(side: BoardSide): PersonalBoard = side match
    case BoardSide.SideA =>
      PersonalBoardImpl(4, 4, 23, generateHexGrid(4, 4), side)
    case BoardSide.SideB =>
      PersonalBoardImpl(6, 3, 25, generateHexGrid(6, 3), side)

  /** Extractor method for pattern matching on a PersonalBoard instance.
    *
    * @param board
    *   the PersonalBoard to extract data from
    * @return
    *   an Option containing a tuple with heightBound, widthBound, totalCells,
    *   and cells, or None if board is null
    */
  def unapply(
      board: PersonalBoard
  ): Option[(Int, Int, Int, Map[Coordinate, Cell])] =
    if board == null then None
    else
      Some((board.heightBound, board.widthBound, board.totalCells, board.cells))

  private case class PersonalBoardImpl(
      override val heightBound: Int,
      override val widthBound: Int,
      override val totalCells: Int,
      override val cells: Map[Coordinate, Cell],
      override val side: BoardSide
  ) extends PersonalBoard:

    private def isValid(c: Coordinate): Boolean = cells.contains(c)

    override def getNorthernNeighbour(c: Coordinate): Option[Cell] =
      if isValid(c.northNeighbour) then cells.get(c.northNeighbour) else None

    override def getSouthernNeighbour(c: Coordinate): Option[Cell] =
      if isValid(c.southNeighbour) then cells.get(c.southNeighbour) else None

    override def getSouthEasternNeighbour(c: Coordinate): Option[Cell] =
      if isValid(c.southEasternNeighbour) then
        cells.get(c.southEasternNeighbour)
      else None

    override def getSouthWesternNeighbour(c: Coordinate): Option[Cell] =
      if isValid(c.southWesternNeighbour) then
        cells.get(c.southWesternNeighbour)
      else None

    override def getNorthEasternNeighbour(c: Coordinate): Option[Cell] =
      if isValid(c.northEasternNeighbour) then
        cells.get(c.northEasternNeighbour)
      else None

    override def getNorthWesternNeighbour(c: Coordinate): Option[Cell] =
      if isValid(c.northWesternNeighbour) then
        cells.get(c.northWesternNeighbour)
      else None

    override def placeToken(
        token: TerrainToken,
        c: Coordinate
    ): Option[PersonalBoard] =
      if isValid(c) then
        cells.get(c) match
          case Some(currentCell) =>
            val updatedCell = currentCell.placeToken(token)
            val updatedCells = cells + (c -> updatedCell)
            Some(copy(cells = updatedCells))
          case None => None
      else None

    override def placeAnimalOnCell(c: Coordinate): Option[PersonalBoard] =
      if isValid(c) then
        cells.get(c) match
          case Some(currentCell) =>
            currentCell.occupyWithAnimal.map(updatedCell =>
              this.copy(cells = cells + (c -> updatedCell))
            )
          case None => None
      else None
