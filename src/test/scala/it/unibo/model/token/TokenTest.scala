package it.unibo.model.token

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TokenTest extends AnyFlatSpec with Matchers:

  "Mountain" should "have grey color" in {
    TerrainToken.Mountain.color shouldBe TokenColor.Grey
  }

  "TreeTrunk" should "have brown color" in {
    TerrainToken.TreeTrunk.color shouldBe TokenColor.Brown
  }

  "TreeFoliage" should "have green color" in {
    TerrainToken.TreeFoliage.color shouldBe TokenColor.Green
  }

  "Field" should "have yellow color" in {
    TerrainToken.Field.color shouldBe TokenColor.Yellow
  }

  "Water" should "have blue color" in {
    TerrainToken.Water.color shouldBe TokenColor.Blue
  }

  "Building" should "have red color" in {
    TerrainToken.Building.color shouldBe TokenColor.Red
  }
