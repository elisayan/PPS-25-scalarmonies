package it.unibo.model.card

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}
import it.unibo.model.cell.Cell
import it.unibo.model.token.TerrainToken

class HabitatMatcherTest extends AnyFunSuite:
  private val treeCell = Cell(List(TerrainToken.Forest))
  private val waterCell = Cell(List(TerrainToken.Water))
  private val testCell = Map(
    Coordinate(0, 0) -> treeCell,
    Coordinate(0, 1) -> waterCell
  )
  private val testBoard = PersonalBoard(heightBound = 3, widthBound = 3, cells = testCell)
  private val testHabitat = Habitat(List(
    CellRequirement(Coordinate(0, 0), TerrainToken.Forest, 1),
    CellRequirement(Coordinate(0, 1), TerrainToken.Water, 1)
  ))

  test("isMatch deve ritornare true se la plancia soddisfa l'habitat a partire dall'origine indicata" +
    " altrimenti deve ritornare falso"):
    HabitatMatcher.isMatch(testBoard, Coordinate(0, 0), testHabitat) shouldBe true
    HabitatMatcher.isMatch(testBoard, Coordinate(0, 1), testHabitat) shouldBe false
    HabitatMatcher.isMatch(testBoard, Coordinate(5, 5), testHabitat) shouldBe false