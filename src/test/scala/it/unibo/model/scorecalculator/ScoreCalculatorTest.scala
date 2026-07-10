package it.unibo.model.scorecalculator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.personalBoard.PersonalBoard
import it.unibo.model.scorecalculator.Score
import it.unibo.model.scorecalculator.Score.toInt 

class ScoreCalculatorTest extends AnyFlatSpec with Matchers:
  
  val calculator: ScoreCalculator = ScoreCalculator()

  
  def createMockBoardWithTokens(tokens: Map[String, Int]): PersonalBoard =
    // Esempio: PersonalBoard( ... )
    ???

  "A ScoreCalculator" should "calculate a score of zero for an empty board" in {
    val emptyBoard: PersonalBoard = createMockBoardWithTokens(Map.empty)

    val (total, details) = calculator.calculateDetailedScore(emptyBoard)

    
    total.toInt shouldBe 0
    
    details("Water").toInt shouldBe 0
    details("Mountains").toInt shouldBe 0
  }

  it should "correctly isolate and calculate score from fields" in {
    
    val fieldBoard = createMockBoardWithTokens(Map("Campi" -> 5))

    val (total, details) = calculator.calculateDetailedScore(fieldBoard)

  
    details("Campi").toInt shouldBe 5
    details("Fiumi").toInt shouldBe 0
    total.toInt shouldBe 5
  }

  it should "correctly calculate score from water when using the default strategy" in {
    val waterBoard = createMockBoardWithTokens(Map("Fiumi" -> 10))

    val (total, details) = calculator.calculateDetailedScore(waterBoard)

    details("Fiumi").toInt shouldBe 10
    details("Campi").toInt shouldBe 0
    total.toInt shouldBe 10
  }

  it should "return the same total score between standard and detailed calculation" in {
    val randomBoard = createMockBoardWithTokens(Map("Campi" -> 5, "Montagne" -> 3))

    val standardTotal = calculator.calculateScore(randomBoard)
    val (detailedTotal, _) = calculator.calculateDetailedScore(randomBoard)
    
    standardTotal.toInt shouldBe detailedTotal.toInt
  }
