package it.unibo.model.token

import org.scalatest.flatspec.AnyFlatSpec

class TokenTest extends AnyFlatSpec:

  "A Terrain Token" should "have a specific type and color" in {
    val mountainToken = TerrainToken.Mountain
    assert(mountainToken.color == TokenColor.Grey)

    val treeTrunkToken = TerrainToken.TreeTrunk
    assert(treeTrunkToken.color == TokenColor.Brown)

    val treeFoliageToken = TerrainToken.TreeFoliage
    assert(treeFoliageToken.color == TokenColor.Green)

    val fieldToken = TerrainToken.Field
    assert(fieldToken.color == TokenColor.Yellow)

    val waterToken = TerrainToken.Water
    assert(waterToken.color == TokenColor.Blue)

    val buildingToken = TerrainToken.Building
    assert(buildingToken.color == TokenColor.Red)
  }
