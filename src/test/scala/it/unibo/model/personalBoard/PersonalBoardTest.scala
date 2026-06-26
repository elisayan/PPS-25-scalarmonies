package it.unibo.model.personalBoard

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PersonalBoardTest extends AnyFlatSpec with Matchers:

  val board = PersonalBoard(4, 4, 23)
  val westBorder = Coordinate(-4, 0)
  val eastBorder = Coordinate(4, 0)
  val southBorder = Coordinate(0, -4)
  val northBorder = Coordinate(0, 4)

  "A Personal Board" should "have a fixed size and fixed bounds" in:
    board should matchPattern { case PersonalBoard(4, 4, 23) => }

  it should "return None when requesting a neighbor beyond the western boundary" in:
    board.getNorthWestNeighbour(westBorder) shouldBe None
    board.getSouthWestNeighbour(westBorder) shouldBe None

    board.getNorthEastNeighbour(westBorder) should matchPattern { case Some(Cell(-2, 1)) => }
    board.getSouthEastNeighbour(westBorder) should matchPattern { case Some(Cell(-2, -1)) => }

  it should "return None when requesting a neighbor beyond the eastern boundary" in:
    board.getNorthEastNeighbour(eastBorder) shouldBe None
    board.getSouthEastNeighbour(eastBorder) shouldBe None
    board.getNorthWestNeighbour(eastBorder) should matchPattern { case Some(Cell(2, 1)) => }
    board.getSouthWestNeighbour(eastBorder) should matchPattern { case Some(Cell(2, -1)) => }

  it should "return None when requesting a neighbor beyond the southern boundary" in:
    board.getSouthNeighbour(southBorder) shouldBe None
    board.getSouthWestNeighbour(southBorder) shouldBe None
    board.getSouthEastNeighbour(southBorder) shouldBe None
    board.getNorthNeighbour(southBorder) should matchPattern { case Some(Cell(0, -2)) => }

  it should "return None when requesting a neighbor beyond the northern boundary" in:
    board.getNorthNeighbour(northBorder) shouldBe None
    board.getNorthWestNeighbour(northBorder) shouldBe None
    board.getNorthEastNeighbour(northBorder) shouldBe None
    board.getSouthNeighbour() should matchPattern { case Some(Cell(0, 2)) => }