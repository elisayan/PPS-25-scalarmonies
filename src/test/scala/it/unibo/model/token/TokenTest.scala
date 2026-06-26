package it.unibo.model.token

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TokenTest extends AnyFlatSpec with Matchers:

  val colors = Seq(
    TokenColor.Grey,
    TokenColor.Brown,
    TokenColor.Green,
    TokenColor.Yellow,
    TokenColor.Blue,
    TokenColor.Red
  )

  "A Token" should "store the provided color" in {
    colors.foreach { color =>
      Token(color).color shouldBe color
    }
  }
