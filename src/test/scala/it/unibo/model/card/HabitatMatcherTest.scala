package it.unibo.model.card

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.token.TerrainToken.{Forest, Water, Mountain, Field}

class HabitatMatcherTest extends AnyFunSuite:
  private val testHabitat = Habitat(
    List(
      CellRequirement(Coordinate(0, 0), Forest, 1),
      CellRequirement(Coordinate(0, 2), Water, 1)
    )
  )
  private val boardWithFreeTarget: PersonalBoard = PersonalBoard(SideA)
    .placeToken(Forest, Coordinate(0, 0)).get
    .placeToken(Water, Coordinate(0, 2)).get
  private val boardWithOccupiedTarget: PersonalBoard =
    boardWithFreeTarget.placeAnimalOnCell(Coordinate(0, 0)).get

  test(
    "isMatch deve ritornare true se la plancia soddisfa l'habitat altrimenti falso"
  ):
    HabitatMatcher.isMatch(
      boardWithFreeTarget,
      Coordinate(0, 0),
      testHabitat
    ) shouldBe true
    HabitatMatcher.isMatch(
      boardWithFreeTarget,
      Coordinate(2, 1),
      testHabitat
    ) shouldBe false
    HabitatMatcher.isMatch(
      boardWithFreeTarget,
      Coordinate(10, 10),
      testHabitat
    ) shouldBe false

  test(
    "findMatches deve ritornare l'insieme esatto di HabitatMatch validi sulla plancia"
  ):
    val matches = HabitatMatcher.findMatches(boardWithFreeTarget, testHabitat)
    matches should have size 1
    val foundMatch = matches.head
    foundMatch.origin shouldBe Coordinate(0, 0)
    foundMatch.involvedCells should contain allOf (Coordinate(0, 0), Coordinate(
      0,
      2
    ))

  test("findMatches deve ritornare Set vuoto se l'habitat è impossibile"):
    val impossibleHabitat = Habitat(
      List(
        CellRequirement(Coordinate(0, 0), Field, 1),
        CellRequirement(Coordinate(0, 2), Field, 1)
      )
    )
    HabitatMatcher.findMatches(
      boardWithFreeTarget,
      impossibleHabitat
    ) shouldBe empty

  test("findMatches deve ritornare Set vuoto se la cella bersaglio è occupata"):
    HabitatMatcher.findMatches(
      boardWithOccupiedTarget,
      testHabitat
    ) shouldBe empty

  test("findMatches test su SideA (rotazioni, traslazioni e altezze errate)"):
    val complexHabitat = Habitat(
      List(
        CellRequirement(Coordinate(0, 0), Forest, 1),
        CellRequirement(Coordinate(2, 1), Water, 1),
        CellRequirement(Coordinate(2, -1), Mountain, 2)
      )
    )
    val largeBoard = PersonalBoard(SideA)
      .placeToken(Forest, Coordinate(0, 0)).get
      .placeToken(Water, Coordinate(2, 1)).get
      .placeToken(Mountain, Coordinate(2, -1)).get
      .placeToken(Mountain, Coordinate(2, -1)).get
      .placeToken(Forest, Coordinate(-2, -1)).get
      .placeToken(Water, Coordinate(-4, 0)).get
      .placeToken(Mountain, Coordinate(-2, 1)).get
      .placeToken(Mountain, Coordinate(-2, 1)).get
      .placeToken(Forest, Coordinate(0, -4)).get
      .placeToken(Water, Coordinate(2, -3)).get
      .placeToken(Mountain, Coordinate(0, -2))
    val matches = HabitatMatcher.findMatches(largeBoard.get, complexHabitat)
    matches should have size 2
    matches
      .map(_.origin) should contain allOf (Coordinate(0, 0), Coordinate(-2, -1))
