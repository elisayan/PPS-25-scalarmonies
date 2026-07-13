package it.unibo.model.personalboard

trait Coordinate:
  def x: Int
  def y: Int

  def +(other: Coordinate): Coordinate
  def -(other: Coordinate): Coordinate
  def *(other: Coordinate): Coordinate

  def northNeighbour: Coordinate = Coordinate(x, y + 2)
  def southNeighbour: Coordinate = Coordinate(x, y - 2)
  def northEasternNeighbour: Coordinate = Coordinate(x + 2, y + 1)
  def northWesternNeighbour: Coordinate = Coordinate(x - 2, y + 1)
  def southEasternNeighbour: Coordinate = Coordinate(x + 2, y - 1)
  def southWesternNeighbour: Coordinate = Coordinate(x - 2, y - 1)
  def allNeighbours: Set[Coordinate]
  def isNeighbour(other: Coordinate): Boolean

  def rotate60: Coordinate

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
    override def rotate60: Coordinate =
      Coordinate((x + 2 * y) / 2, (-3 * x + 2 * y) / 4)

    override def allNeighbours: Set[Coordinate] =
      Set(
        northNeighbour,
        southNeighbour,
        northEasternNeighbour,
        northWesternNeighbour,
        southEasternNeighbour,
        southWesternNeighbour
      )

    override def isNeighbour(other: Coordinate): Boolean = allNeighbours.contains(other)
