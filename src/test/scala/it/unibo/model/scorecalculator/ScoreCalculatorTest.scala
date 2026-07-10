package it.unibo.model.scorecalculator

import it.unibo.model.personalBoard.BoardSide.SideA
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}
import it.unibo.model.scorecalculator.Score.toInt
import it.unibo.model.token.TerrainToken
import it.unibo.model.token.TerrainToken.{Building, Field, Forest, Ground, Water}

class ScoreCalculatorTest extends AnyFlatSpec with Matchers:

  val calculator: ScoreCalculator = ScoreCalculator()


  def createMockBoard(): PersonalBoard =
    val board = PersonalBoard(SideA)
    board

  "A ScoreCalculator" should "calculate a score of zero for an empty board" in {
    val emptyBoard: PersonalBoard = createMockBoard()
    val (total, details) = calculator.calculateDetailedScore(emptyBoard)
    total.toInt shouldBe 0
    details("Water").toInt shouldBe 0
    details("Mountains").toInt shouldBe 0
  }

  it should "correctly isolate and calculate score from fields" in:

    val fieldBoard = createMockBoard()
    val f1 = fieldBoard.placeToken(TerrainToken.Field, Coordinate(0,0))
    val f2 = f1.get.placeToken(TerrainToken.Field, Coordinate(0, 2))
    val (total, details) = calculator.calculateDetailedScore(f2.get)
    details("Fields").toInt shouldBe 5
    details("Water").toInt shouldBe 0
    total.toInt shouldBe 5


  it should "correctly calculate score from buildings" in:
    val buildBoard = createMockBoard()
    val b1 = buildBoard.placeToken(Building, Coordinate(0, 0))
    val b2 = b1.get.placeToken(Building, Coordinate(0, 0))
    val b3 = b2.get.placeToken(Forest, Coordinate(0, 2))
    val b4 = b3.get.placeToken(Water, Coordinate(0, -2))
    val b5 = b4.get.placeToken(Field, Coordinate(2, 1))
    val (total, details) = calculator.calculateDetailedScore(b5.get)
    details("Building").toInt shouldBe 5

    val buildBoard2 = createMockBoard()
    val b6 = buildBoard2.placeToken(Building, Coordinate(0, 0))
    val b7 = b6.get.placeToken(Building, Coordinate(0, 0))
    val b8 = b7.get.placeToken(Forest, Coordinate(0, 2))
    val b9 = b8.get.placeToken(Water, Coordinate(0, -2))
    val (tot, det) = calculator.calculateDetailedScore(b9.get)
    det("Building").toInt shouldBe 0


  it should "correctly calculate score from forests" in:
    val forestBoard = createMockBoard()
    val f1 = forestBoard.placeToken(Forest, Coordinate(0, 0))
    val f2 = f1.get.placeToken(Ground, Coordinate(0, 2))
    val f3 = f2.get.placeToken(Forest, Coordinate(0, 2))
    val (total, detail) = calculator.calculateDetailedScore(f3.get)
    detail("Forest").toInt shouldBe 4

  it should "correctly calculate score from water when using the default strategy" in {
    val waterBoard = createMockBoard()

    val (total, details) = calculator.calculateDetailedScore(waterBoard)

    details("Water").toInt shouldBe 10
    details("Fields").toInt shouldBe 0
    total.toInt shouldBe 10
  }

  it should "return the same total score between standard and detailed calculation" in {
    val randomBoard = createMockBoard()

    val standardTotal = calculator.calculateScore(randomBoard)
    val (detailedTotal, _) = calculator.calculateDetailedScore(randomBoard)

    standardTotal.toInt shouldBe detailedTotal.toInt
  }
