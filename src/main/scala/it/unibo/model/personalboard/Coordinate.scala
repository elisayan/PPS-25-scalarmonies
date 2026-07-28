package it.unibo.model.personalboard

/** Represents a 2D coordinate on a hexagonal grid system, i.e. Personal Board
  */
trait Coordinate:

  /** The x-coordinate value. */
  def x: Int

  /** The y-coordinate value. */
  def y: Int

  /** Adds another coordinate to this one element-wise.
    *
    * @param other
    *   the coordinate to add
    * @return
    *   a new coordinate representing the sum
    */
  def +(other: Coordinate): Coordinate

  /** Subtracts another coordinate from this one element-wise.
    *
    * @param other
    *   the coordinate to subtract
    * @return
    *   a new coordinate representing the difference
    */
  def -(other: Coordinate): Coordinate

  /** Multiplies another coordinate with this one element-wise.
    *
    * @param other
    *   the coordinate to multiply by
    * @return
    *   a new coordinate representing the product
    */
  def *(other: Coordinate): Coordinate

  /** Returns the northern neighboring coordinate.
    *
    * @return
    *   the coordinate directly north
    */
  def northNeighbour: Coordinate = Coordinate(x, y + 2)

  /** Returns the southern neighboring coordinate.
    *
    * @return
    *   the coordinate directly south
    */
  def southNeighbour: Coordinate = Coordinate(x, y - 2)

  /** Returns the north-eastern neighboring coordinate.
    *
    * @return
    *   the coordinate to the north-east
    */
  def northEasternNeighbour: Coordinate = Coordinate(x + 2, y + 1)

  /** Returns the north-western neighboring coordinate.
    *
    * @return
    *   the coordinate to the north-west
    */
  def northWesternNeighbour: Coordinate = Coordinate(x - 2, y + 1)

  /** Returns the south-eastern neighboring coordinate.
    *
    * @return
    *   the coordinate to the south-east
    */
  def southEasternNeighbour: Coordinate = Coordinate(x + 2, y - 1)

  /** Returns the south-western neighboring coordinate.
    *
    * @return
    *   the coordinate to the south-west
    */
  def southWesternNeighbour: Coordinate = Coordinate(x - 2, y - 1)

  /** Returns the set of all six adjacent neighboring coordinates.
    *
    * @return
    *   a Set containing all neighboring coordinate instances
    */
  def allNeighbours: Set[Coordinate]

  /** Checks whether another coordinate is adjacent to this one.
    *
    * @param other
    *   the coordinate to check
    * @return
    *   `true` if `other` is a neighbor, `false` otherwise
    */
  def isNeighbour(other: Coordinate): Boolean

  /** Rotates this coordinate by 60 degrees.
    *
    * @return
    *   a new coordinate rotated by 60 degrees
    */
  def rotate60: Coordinate

/** Companion object and factory for creating coordinate instances. */
object Coordinate:

  /** Creates a new coordinate instance with the given coordinates.
    *
    * @param x
    *   the x-coordinate
    * @param y
    *   the y-coordinate
    * @return
    *   a new coordinate instance
    */
  def apply(x: Int, y: Int): Coordinate = CoordinateImpl(x, y)

  /** Extractor method for pattern matching on a coordinate.
    *
    * @param c
    *   the coordinate to extract values from
    * @return
    *   an Option containing a tuple with (x, y) coordinates, or None if `c` is
    *   null
    */
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

    override def isNeighbour(other: Coordinate): Boolean =
      allNeighbours.contains(other)
