package it.unibo.model.scorecalculator

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.BoardSide.{SideA, SideB}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.scorecalculator.Score.toInt
import it.unibo.model.token.TerrainToken
import it.unibo.model.token.TerrainToken.{Building, Field, Forest, Ground, Water}

class ScoreCalculatorTest extends AnyFlatSpec with Matchers:

  val calculator: ScoreCalculator = ScoreCalculator()
  val emptyCardList: List[AnimalCard] = List.empty


  def createMockBoardA(): PersonalBoard =
    val board = PersonalBoard(SideA)
    board

  def createMockBoardB(): PersonalBoard =
    val board = PersonalBoard(SideB)
    board


  "A ScoreCalculator" should "calculate a score of zero for an empty board" in {
    val emptyBoard: PersonalBoard = createMockBoardA()
    val (total, details) = calculator.calculateDetailedScore(emptyBoard, emptyCardList)
    total.toInt shouldBe 0
    details("Water").toInt shouldBe 0
    details("Mountain").toInt shouldBe 0
  }

  it should "correctly isolate and calculate score from fields" in:

    val fieldBoard = createMockBoardA()
    val f1 = fieldBoard.placeToken(Field, Coordinate(0,0))
    val f2 = f1.get.placeToken(Field, Coordinate(0, 2))
    val f3 = f2.get.placeToken(Field, Coordinate(4, -4))
    val f4 = f3.get.placeToken(Field, Coordinate(4, -2))
    val f5 = f4.get.placeToken(Field, Coordinate(2, -3))
    val (total, details) = calculator.calculateDetailedScore(f5.get, emptyCardList)
    details("Field").toInt shouldBe 10
    details("Water").toInt shouldBe 0
    total.toInt shouldBe 10


  it should "correctly calculate score from buildings" in:
    val buildBoard = createMockBoardA()
    val b1 = buildBoard.placeToken(Building, Coordinate(0, 0))
    val b2 = b1.get.placeToken(Building, Coordinate(0, 0))
    val b3 = b2.get.placeToken(Forest, Coordinate(0, 2))
    val b4 = b3.get.placeToken(Water, Coordinate(0, -2))
    val b5 = b4.get.placeToken(Field, Coordinate(2, 1))
    val b6 = b5.get.placeToken(Building, Coordinate(-4, -4))
    val (total, details) = calculator.calculateDetailedScore(b6.get, emptyCardList)
    details("Building").toInt shouldBe 5

    val buildBoard2 = createMockBoardA()
    val b7 = buildBoard2.placeToken(Building, Coordinate(0, 0))
    val b8 = b7.get.placeToken(Building, Coordinate(0, 0))
    val b9 = b8.get.placeToken(Forest, Coordinate(0, 2))
    val b10 = b9.get.placeToken(Water, Coordinate(0, -2))
    val (tot, det) = calculator.calculateDetailedScore(b10.get, emptyCardList)
    det("Building").toInt shouldBe 0


  it should "correctly calculate score from forests" in:
    val forestBoard = createMockBoardA()
    val f1 = forestBoard.placeToken(Forest, Coordinate(0, 0))
    val f2 = f1.get.placeToken(Ground, Coordinate(0, 2))
    val f3 = f2.get.placeToken(Forest, Coordinate(0, 2))
    val (total, detail) = calculator.calculateDetailedScore(f3.get, emptyCardList)
    detail("Forest").toInt shouldBe 4

    val forestBoard2 = createMockBoardA()
    val f4 = forestBoard2.placeToken(Ground, Coordinate(0, 0))
    val f5 = f4.get.placeToken(Ground, Coordinate(0, 0))
    val f6 = f5.get.placeToken(Forest, Coordinate(0, 0))
    val (tot, dets) = calculator.calculateDetailedScore(f6.get, emptyCardList)
    dets("Forest").toInt shouldBe 7

  it should "correctly calculate score for longest river of water tokens" in:
    val waterBoard = createMockBoardA()
    val w1 = waterBoard.placeToken(Water, Coordinate(2, -3))
    val w2 = w1.get.placeToken(Water, Coordinate(0, -2))
    val w3 = w2.get.placeToken(Water, Coordinate(4, -4))
    val w4 = w3.get.placeToken(Water, Coordinate(-2, -1))
    val (total, details) = calculator.calculateDetailedScore(w4.get, emptyCardList)
    details("Water").toInt shouldBe 8

    val waterBoard2 = createMockBoardA()
    val step1 = waterBoard2.placeToken(Water, Coordinate(4, -4))
    val step2 = step1.get.placeToken(Water, Coordinate(2, -3))
    val step3 = step2.get.placeToken(Water, Coordinate(0, -2))
    val branchLeft = step3.get.placeToken(Water, Coordinate(0, 0))
    val branchLeft2 = branchLeft.get.placeToken(Water, Coordinate(0, 2))
    val finalBoard = branchLeft2.get.placeToken(Water, Coordinate(-2, -3))
    val finalBoard2 = finalBoard.get.placeToken(Water, Coordinate(-4, -4))
    val (total2, details2) = calculator.calculateDetailedScore(finalBoard2.get, emptyCardList)
    details2("Water").toInt shouldBe 11

  it should "correctly calculate score for islands created by water on Side B" in:
    val boardB1 = createMockBoardB()

    val w1 = boardB1.placeToken(Water, Coordinate(4, -2))
    val w2 = w1.get.placeToken(Water, Coordinate(6, -1))
    val finalBoard1 = w2.get.placeToken(Water, Coordinate(2, -3))

    val (total1, details1) = calculator.calculateDetailedScore(finalBoard1.get, emptyCardList)

    details1("Water").toInt shouldBe 10

    val boardB2 = createMockBoardB()

    val step1 = boardB2.placeToken(Water, Coordinate(-6, 1))
    val step2 = step1.get.placeToken(Water, Coordinate(-4, 2))
    val step3 = step2.get.placeToken(Water, Coordinate(-2, 1))
    val step4 = step3.get.placeToken(Water, Coordinate(0, 0))
    val step5 = step4.get.placeToken(Water, Coordinate(2, 1))
    val finalBoard2 = step5.get.placeToken(Water, Coordinate(2, 3))

    val (total2, details2) = calculator.calculateDetailedScore(finalBoard2.get, emptyCardList)

    details2("Water").toInt shouldBe 15


  it should "return the same total score between standard and detailed calculation" in:
    val randomBoard = createMockBoardA()

    val standardTotal = calculator.calculateScore(randomBoard, emptyCardList)
    val (detailedTotal, _) = calculator.calculateDetailedScore(randomBoard, emptyCardList)

    standardTotal.toInt shouldBe detailedTotal.toInt

