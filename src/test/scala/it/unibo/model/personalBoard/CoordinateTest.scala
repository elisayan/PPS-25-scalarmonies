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

    c1.northEastNeighbour match
      case Coordinate(x, _) => x should equal(2)

    c1.northEastNeighbour match
      case Coordinate(_, y) => y should equal(1)

    c1.northWestNeighbour match
      case Coordinate(x, _) => x should equal(-2)

    c1.northWestNeighbour match
      case Coordinate(_, y) => y should equal(1)

    c1.southEastNeighbour match
      case Coordinate(x, _) => x should equal(2)

    c1.southEastNeighbour match
      case Coordinate(_, y) => y should equal(-1)

    c1.southWestNeighbour match
      case Coordinate(x, _) => x should equal(-2)

    c1.southWestNeighbour match
      case Coordinate(_, y) => y should equal(-1)
