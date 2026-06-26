package it.unibo.model.personalBoard

import it.unibo.model.cell.Cell
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PersonalBoardTest extends AnyFlatSpec with Matchers:

  val map: Map[Coordinate, Cell] = Map()
  val board = PersonalBoard(4, 4, map)
  val westBorder = Coordinate(-4, 0)
  val eastBorder = Coordinate(4, 0)
  val southBorder = Coordinate(0, -4)
  val northBorder = Coordinate(0, 4)

  "A Personal Board" should "have a fixed size and fixed bounds" in:
    board should matchPattern { case PersonalBoard(4, 4, map) => }

  it should "return None when requesting a neighbor beyond the western boundary" in:
    board.getNortWesternNeighbour(westBorder) shouldBe None
    board.getSouthWesternNeighbour(westBorder) shouldBe None

  it should "return None when requesting a neighbor beyond the eastern boundary" in:
    board.getNortEasternNeighbour(eastBorder) shouldBe None
    board.getSouthEasternNeighbour(eastBorder) shouldBe None

  it should "return None when requesting a neighbor beyond the southern boundary" in:
    board.getSouthernNeighbour(southBorder) shouldBe None
    board.getSouthWesternNeighbour(southBorder) shouldBe None
    board.getSouthEasternNeighbour(southBorder) shouldBe None

  it should "return None when requesting a neighbor beyond the northern boundary" in:
    board.getNorthernNeighbour(northBorder) shouldBe None
    board.getNortWesternNeighbour(northBorder) shouldBe None
    board.getNortEasternNeighbour(northBorder) shouldBe None
