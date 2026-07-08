package it.unibo.model.card

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}
import it.unibo.model.cell.Cell
import it.unibo.model.token.TerrainToken

class HabitatMatcherTest extends AnyFunSuite:
  private val testHabitat = Habitat(List(
    CellRequirement(Coordinate(0, 0), TerrainToken.Forest, 1),
    CellRequirement(Coordinate(0, 2), TerrainToken.Water, 1),
  ))
  private val treeCellFree = Cell(List(TerrainToken.Forest))
  private val treeCellOccupied = Cell(List(TerrainToken.Forest), hasAnimal = true)
  private val waterCell = Cell(List(TerrainToken.Water))
  private val boardWithFreeTarget = PersonalBoard(
    heightBound = 3,
    widthBound = 3,
    cells = Map(
      Coordinate(0, 0) -> treeCellFree,
      Coordinate(0, 2) -> waterCell
    )
  )
  private val boardWithOccupiedTarget = PersonalBoard(
    heightBound = 3,
    widthBound = 3,
    cells = Map(
      Coordinate(0, 0) -> treeCellOccupied,
      Coordinate(0, 2) -> waterCell
    )
  )

  test("isMatch deve ritornare true se la plancia soddisfa l'habitat a partire dall'origine indicata altrimenti falso"):
    HabitatMatcher.isMatch(boardWithFreeTarget, Coordinate(0, 0), testHabitat) shouldBe true
    HabitatMatcher.isMatch(boardWithFreeTarget, Coordinate(2, 1), testHabitat) shouldBe false
    HabitatMatcher.isMatch(boardWithFreeTarget, Coordinate(5, 5), testHabitat) shouldBe false

  test("findMatches deve ritornare l'insieme esatto di HabitatMatch validi sulla plancia"):
    val matches = HabitatMatcher.findMatches(boardWithFreeTarget, testHabitat)
    matches should have size 1
    val foundMatch = matches.head
    foundMatch.origin shouldBe Coordinate(0, 0)
    foundMatch.involvedCells should have size 2
    foundMatch.involvedCells should contain allOf (Coordinate(0, 0), Coordinate(0, 2))

  test("findMatches deve ritornare un Set vuoto se l'habitat non è presente da nessuna parte"):
    val impossibleHabitat = Habitat(List(
      CellRequirement(Coordinate(0, 0), TerrainToken.Field, 1),
      CellRequirement(Coordinate(0, 2), TerrainToken.Field, 1)
    ))
    val matches = HabitatMatcher.findMatches(boardWithFreeTarget, impossibleHabitat)
    matches shouldBe empty

  test("findMatches deve ritornare un Set vuoto se la cella bersaglio è già occupata da un animale"):
    val matches = HabitatMatcher.findMatches(boardWithOccupiedTarget, testHabitat)
    matches shouldBe empty