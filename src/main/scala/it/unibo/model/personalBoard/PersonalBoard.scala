package it.unibo.model.personalBoard

import it.unibo.model.cell.Cell

case class Token(color: String)

trait PersonalBoard:
  def heightBound: Int
  def widthBound: Int
  def cells: Map[Coordinate, Cell]

  def getNorthernNeighbour(coordinate: Coordinate): Option[Cell]
  def getSouthernNeighbour(coordinate: Coordinate): Option[Cell]
  def getSouthEasternNeighbour(coordinate: Coordinate): Option[Cell]
  def getSouthWesternNeighbour(coordinate: Coordinate): Option[Cell]
  def getNortEasternNeighbour(coordinate: Coordinate): Option[Cell]
  def getNortWesternNeighbour(coordinate: Coordinate): Option[Cell]
  def placeToken(token: Token): PersonalBoard

object PersonalBoard:

  def apply(
      heightBound: Int,
      widthBound: Int,
      cells: Map[Coordinate, Cell]
  ): PersonalBoard = PersonalBoardImpl(heightBound, widthBound, cells)

  def unapply(board: PersonalBoard): Option[(Int, Int, Map[Coordinate, Cell])] =
    if board == null then None
    else Some((board.heightBound, board.widthBound, board.cells))

  private case class PersonalBoardImpl(
      override val heightBound: Int,
      override val widthBound: Int,
      override val cells: Map[Coordinate, Cell]
  ) extends PersonalBoard:

    private def isValid(c: Coordinate): Boolean =
      c.x.abs <= widthBound && c.y.abs <= heightBound

    private def checkBounds(c: Coordinate): Option[Cell] =
      if isValid(c) then cells.get(c) else None

    override def getNorthernNeighbour(coordinate: Coordinate): Option[Cell] =
      ???

    override def getSouthernNeighbour(coordinate: Coordinate): Option[Cell] =
      ???

    override def getSouthEasternNeighbour(
        coordinate: Coordinate
    ): Option[Cell] = ???

    override def getSouthWesternNeighbour(
        coordinate: Coordinate
    ): Option[Cell] = ???

    override def getNortEasternNeighbour(coordinate: Coordinate): Option[Cell] =
      ???

    override def getNortWesternNeighbour(coordinate: Coordinate): Option[Cell] =
      ???

    override def placeToken(token: Token): PersonalBoard = ???
