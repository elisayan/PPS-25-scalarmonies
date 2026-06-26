package it.unibo.model.personalBoard

trait Coordinate:
  def x: Int
  def y: Int

  def northNeighbour: Coordinate
  def southNeighbour: Coordinate
  def northEastNeighbour: Coordinate
  def northWestNeighbour: Coordinate
  def southEastNeighbour: Coordinate
  def southWestNeighbour: Coordinate
  def +(other: Coordinate): Coordinate
  def -(other: Coordinate): Coordinate
  def *(other: Coordinate): Coordinate
  


object Coordinate:

  def apply(x: Int, y: Int): Coordinate = CoordinateImpl(x, y)
  def unapply(c: Coordinate): Option[(Int,Int)] = Some(c.x,c.y)

  private case class CoordinateImpl(override val x: Int, override val y: Int) extends Coordinate:

    override def +(other: Coordinate): Coordinate = Coordinate(x + other.x, y + other.y)

    override def -(other: Coordinate): Coordinate = Coordinate(x - other.x, y - other.y)

    override def *(other: Coordinate): Coordinate = Coordinate(x * other.x, y * other.y)

    override def northNeighbour: Coordinate = Coordinate(x, y + 2)

    override def southNeighbour: Coordinate = Coordinate(x, y - 2)

    override def northEastNeighbour: Coordinate = Coordinate(x + 2, y + 1)

    override def northWestNeighbour: Coordinate = Coordinate(x - 2, y + 1)

    override def southEastNeighbour: Coordinate = Coordinate(x + 2, y - 1)

    override def southWestNeighbour: Coordinate = Coordinate(x - 2, y - 1)






