package it.unibo.model.token

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TerrainTokenTest extends AnyFlatSpec with Matchers:

  "TerrainToken.colorOf" should "map each terrain to its fixed color" in {
    TerrainToken.colorOf(TerrainToken.Water) shouldBe TokenColor.Blue
    TerrainToken.colorOf(TerrainToken.Mountain) shouldBe TokenColor.Grey
    TerrainToken.colorOf(TerrainToken.Forest) shouldBe TokenColor.Green
    TerrainToken.colorOf(TerrainToken.Field) shouldBe TokenColor.Yellow
    TerrainToken.colorOf(TerrainToken.Building) shouldBe TokenColor.Red
    TerrainToken.colorOf(TerrainToken.Ground) shouldBe TokenColor.Brown
  }
