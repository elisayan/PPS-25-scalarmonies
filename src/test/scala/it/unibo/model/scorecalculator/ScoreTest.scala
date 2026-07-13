package it.unibo.model.scorecalculator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.scorecalculator.Score._

class ScoreTest extends AnyFlatSpec with Matchers:

  "A Score" should "be correctly created from a positive integer" in {
    val score = Score(10)
    score.toInt shouldBe 10
  }

  it should "allow creation with a value of zero" in {
    val score = Score(0)
    score.toInt shouldBe 0
    Score.zero.toInt shouldBe 0
  }

  it should "throw an IllegalArgumentException if created with a negative value" in {

    an [IllegalArgumentException] should be thrownBy {
      Score(-5)
    }
  }

  it should "correctly sum two scores using the + operator" in {
    val score1 = Score(15)
    val score2 = Score(20)

    val result = score1 + score2

    result.toInt shouldBe 35
  }

  it should "correctly subtract two scores using the - operator" in {
    val score1 = Score(50)
    val score2 = Score(20)

    val result = score1 - score2

    result.toInt shouldBe 30
  }

  it should "never drop below zero when subtracting a larger score" in {
    val score1 = Score(10)
    val score2 = Score(25)

    val result = score1 - score2

    result.toInt shouldBe 0
  }

  it should "preserve immutability during operations" in {
    val originalScore = Score(10)
    val modifier = Score(5)

    val newScore = originalScore + modifier

    originalScore.toInt shouldBe 10
    newScore.toInt shouldBe 15
  }
