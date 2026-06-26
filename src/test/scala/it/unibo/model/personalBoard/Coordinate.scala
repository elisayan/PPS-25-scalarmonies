package it.unibo.model.personalBoard

trait Coordinate:
  def x: Int
  def y: Int

  def +(other: Coordinate): Coordinate
  def -(other: Coordinate): Coordinate
  def *(other: Coordinate): Coordinate

  def northNeighbour: Coordinate = Coordinate(x, y + 2)
  def southNeighbour: Coordinate = Coordinate(x, y - 2)
  def northEastNeighbour: Coordinate = Coordinate(x + 2, y + 1)
  def northWestNeighbour: Coordinate = Coordinate(x - 2, y + 1)
  def southEastNeighbour: Coordinate = Coordinate(x + 2, y - 1)
  def southWestNeighbour: Coordinate = Coordinate(x - 2, y - 1)

object Coordinate:

  def apply(x: Int, y: Int): Coordinate = CoordinateImpl(x, y)

  def unapply(c: Coordinate): Option[(Int, Int)] =
    if c == null then None else Some((c.x, c.y))

  private case class CoordinateImpl(override val x: Int, override val y: Int)
      extends Coordinate:
    override def +(other: Coordinate): Coordinate =
      Coordinate(x + other.x, y + other.y)
    override def -(other: Coordinate): Coordinate =
      Coordinate(x - other.x, y - other.y)
    override def *(other: Coordinate): Coordinate =
      Coordinate(x * other.x, y * other.y)
