package it.unibo.model.personalBoard

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CoordinateTest extends AnyFlatSpec with Matchers:

  "A Coordinate" should "return correctly its parameters" in:
    val coordinate = Coordinate(3, 7)
    coordinate match
      case Coordinate(x, _) => x should equal(3)

    coordinate match
      case Coordinate(_, y) => y should equal(7)

  it should "always return a new Coordinate object when modified" in:
    val c1 = Coordinate(2, 5)
    val c2 = Coordinate(3, 5)
    c1 + c2 shouldBe a[Coordinate]
    c1 * c2 shouldBe a[Coordinate]
    c1 - c2 shouldBe a[Coordinate]

  it should "always return a new Coordinate object with the correct applied operation and result" in:
    val c3 = Coordinate(1, 1)
    val c4 = Coordinate(2, 1)
    val c5 = c3 + c4
    val c6 = c3 - c4

    c5 match
      case Coordinate(x, _) => x should equal(3)

    c5 match
      case Coordinate(_, y) => y should equal(2)

    c6 match
      case Coordinate(x, _) => x should equal(-1)

    c6 match
      case Coordinate(_, y) => y should equal(0)

  it should "be structurally equal to another coordinate with same values" in:
    val c1 = Coordinate(2, 1)
    val c2 = Coordinate(2, 1)

    c1 should be(c2)
    c1.hashCode() should be(c2.hashCode())

  it should "correctly compute its neighbours positions" in:
    val c1 = Coordinate(0, 0)

    c1.northNeighbour match
      case Coordinate(x, _) => x should equal(0)

    c1.northNeighbour match
      case Coordinate(_, y) => y should equal(2)

    c1.southNeighbour match
      case Coordinate(x, _) => x should equal(0)

    c1.southNeighbour match
      case Coordinate(_, y) => y should equal(-2)

    c1.northEasternNeighbour match
      case Coordinate(x, _) => x should equal(2)

    c1.northEasternNeighbour match
      case Coordinate(_, y) => y should equal(1)

    c1.northWesternNeighbour match
      case Coordinate(x, _) => x should equal(-2)

    c1.northWesternNeighbour match
      case Coordinate(_, y) => y should equal(1)

    c1.southEasternNeighbour match
      case Coordinate(x, _) => x should equal(2)

    c1.southEasternNeighbour match
      case Coordinate(_, y) => y should equal(-1)

    c1.southWesternNeighbour match
      case Coordinate(x, _) => x should equal(-2)

    c1.southWesternNeighbour match
      case Coordinate(_, y) => y should equal(-1)

  it should "rotate 60 degrees clockwise correctly" in:
    val start = Coordinate(0, 2)
    val r1 = start.rotate60
    r1 match
      case Coordinate(x, y) =>
        x should equal(2)
        y should equal(1)
    val r6 = start.rotate60.rotate60.rotate60.rotate60.rotate60.rotate60
    r6 should be(start)

  it should "return all correct neighbours" in :
    val center = Coordinate(0, 0)
    val expectedNeighbours = Set(
      Coordinate(0, 2), // North: (0, 0 + 2)
      Coordinate(0, -2), // South: (0, 0 - 2)
      Coordinate(2, 1), // North-East: (0 + 2, 0 + 1)
      Coordinate(-2, 1), // North-West: (0 - 2, 0 + 1)
      Coordinate(2, -1), // South-East: (0 + 2, 0 - 1)
      Coordinate(-2, -1) // South-West: (0 - 2, 0 - 1)
    )

    val actualNeighbours = center.allNeighbours

    actualNeighbours shouldBe expectedNeighbours

    actualNeighbours.size shouldBe 6

    expectedNeighbours.foreach(neighbour =>
      center.isNeighbour(neighbour) shouldBe true
    )

    val distantCoordinate = Coordinate(4, 4)
    center.isNeighbour(distantCoordinate) shouldBe false
    actualNeighbours.contains(distantCoordinate) shouldBe false

  it should "correctly check whether another coordinate is its neighbour" in :
    val center = Coordinate(0, 0)


    val validNeighbours = Set(
      Coordinate(0, 2), // North
      Coordinate(0, -2), // South
      Coordinate(2, 1), // North-East
      Coordinate(-2, 1), // North-West
      Coordinate(2, -1), // South-East
      Coordinate(-2, -1) // South-West
    )

    validNeighbours.foreach(neighbour =>
      center.isNeighbour(neighbour) shouldBe true
    )

    val trickyNonNeighbours = Set(
      Coordinate(0, 1),
      Coordinate(1, 0),
      Coordinate(1, 1),
      Coordinate(2, 2),
      Coordinate(0, 0)
    )

    trickyNonNeighbours.foreach(nonNeighbour =>
      center.isNeighbour(nonNeighbour) shouldBe false
    )

    val farAway = Coordinate(100, -50)
    center.isNeighbour(farAway) shouldBe false



