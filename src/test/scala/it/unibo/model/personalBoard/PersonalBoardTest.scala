package it.unibo.model.personalBoard

import it.unibo.model.cell.Cell
import it.unibo.model.personalBoard.BoardSide.SideA
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PersonalBoardTest extends AnyFlatSpec with Matchers:

  val validCoordinates: Seq[Coordinate] = for
    x <- -4 to 4 if x % 2 == 0
    y <- -4 to 4
    if (x / 2).abs % 2 == y.abs % 2
  yield Coordinate(x, y)
  val map: Map[Coordinate, Cell] =
    validCoordinates.map(c => c -> Cell()).toMap
  val board = PersonalBoard(SideA)
  val westBorder = Coordinate(-4, 0)
  val eastBorder = Coordinate(4, 0)
  val southBorder = Coordinate(0, -4)
  val northBorder = Coordinate(0, 4)

  "A Personal Board" should "have fixed parameters" in:
    board should matchPattern { case PersonalBoard(4, 4, 23, map) => }

  it should "return None when requesting a neighbor beyond the western boundary" in:
    board.getNorthWesternNeighbour(westBorder) shouldBe None
    board.getSouthWesternNeighbour(westBorder) shouldBe None

  it should "return None when requesting a neighbor beyond the eastern boundary" in:
    board.getNorthEasternNeighbour(eastBorder) shouldBe None
    board.getSouthEasternNeighbour(eastBorder) shouldBe None

  it should "return None when requesting a neighbor beyond the southern boundary" in:
    board.getSouthernNeighbour(southBorder) shouldBe None
    board.getSouthWesternNeighbour(southBorder) shouldBe None
    board.getSouthEasternNeighbour(southBorder) shouldBe None

  it should "return None when requesting a neighbor beyond the northern boundary" in:
    board.getNorthernNeighbour(northBorder) shouldBe None
    board.getNorthWesternNeighbour(northBorder) shouldBe None
    board.getNorthEasternNeighbour(northBorder) shouldBe None
