package it.unibo.model.personalBoard

import it.unibo.model.cell.Cell

case class Token(color: String)

trait PersonalBoard:
  def heightBound: Int
  def widthBound: Int
  def totalCells: Int
  def cells: Map[Coordinate, Cell]

  def getNorthernNeighbour(c: Coordinate): Option[Cell]
  def getSouthernNeighbour(c: Coordinate): Option[Cell]
  def getSouthEasternNeighbour(c: Coordinate): Option[Cell]
  def getSouthWesternNeighbour(c: Coordinate): Option[Cell]
  def getNorthEasternNeighbour(c: Coordinate): Option[Cell]
  def getNorthWesternNeighbour(c: Coordinate): Option[Cell]
  def placeToken(token: Token, c: Coordinate): PersonalBoard

object PersonalBoard:

  enum BoardSide:
    case SideA, SideB

  private def generateHexGrid(widthBound: Int, heightBound: Int): Map[Coordinate, Cell] =
    val validCoordinates = for
      x <- -widthBound to widthBound if x % 2 == 0
      y <- -heightBound to heightBound
      if (x / 2).abs % 2 == y.abs % 2
    yield Coordinate(x, y)
    validCoordinates.map(c => c -> Cell(List())).toMap

  def apply(side: BoardSide): PersonalBoard = side match
    case BoardSide.SideA => PersonalBoardImpl(4,4,23,generateHexGrid(4,4))

    case BoardSide.SideB => PersonalBoardImpl(6,3,25, generateHexGrid(6,3))

  def unapply(board: PersonalBoard): Option[(Int, Int, Int, Map[Coordinate, Cell])] =
    if board == null then None else Some((board.heightBound, board.widthBound, board.totalCells, board.cells))

  private case class PersonalBoardImpl(
      override val heightBound: Int,
      override val widthBound: Int,
      override val totalCells: Int,
      override val cells: Map[Coordinate, Cell]
  ) extends PersonalBoard:

    private def isValid(c: Coordinate): Boolean = c.x.abs <= widthBound && c.y.abs <= heightBound

    private def checkBounds(c: Coordinate): Option[Cell] = if isValid(c) then cells.get(c) else None

    override def getNorthernNeighbour(c: Coordinate): Option[Cell] = checkBounds(c.northNeighbour)

    override def getSouthernNeighbour(c: Coordinate): Option[Cell] = checkBounds(c.southNeighbour)

    override def getSouthEasternNeighbour(c: Coordinate): Option[Cell] = checkBounds(c.southEastNeighbour)

    override def getSouthWesternNeighbour(c: Coordinate): Option[Cell] = checkBounds(c.southWestNeighbour)

    override def getNorthEasternNeighbour(c: Coordinate): Option[Cell] = checkBounds(c.northEastNeighbour)

    override def getNorthWesternNeighbour(c: Coordinate): Option[Cell] = checkBounds(c.northWestNeighbour)

    override def placeToken(token: Token, c: Coordinate): PersonalBoard =
      cells.get(c) match
        case Some(currentCell) =>
          val updatedCell = currentCell.placeToken(token)
          val updatedCells = cells + (c -> updatedCell)
          this.copy(cells = updatedCells)

        case None => this
