package it.unibo.model.token

import org.scalatest.flatspec.AnyFlatSpec

class TokenTest extends AnyFlatSpec{
  
  "A Terrain Token" should "have a specific type and color" in {
    val mountainToken = TerrainToken.Mountain
    assert(mountainToken.color == TokenColor.Grey)
  }
}
