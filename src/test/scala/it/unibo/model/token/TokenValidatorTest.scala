package it.unibo.model.token

import it.unibo.model.cell.Cell
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TokenValidatorTest extends AnyFlatSpec with Matchers:

  // Water
  "TokenValidator" should "allow placing Water on an empty cell" in {
    TokenValidator.canPlace(TerrainToken.Water, Cell()) shouldBe true
  }
  it should "reject placing Water on a non-empty cell" in {
    val cell = Cell(List(TerrainToken.Ground))
    TokenValidator.canPlace(TerrainToken.Water, cell) shouldBe false
  }

  // Field
  it should "allow placing Field on an empty cell" in {
    TokenValidator.canPlace(TerrainToken.Field, Cell()) shouldBe true
  }
  it should "reject placing Field on a non-empty cell" in {
    val cell = Cell(List(TerrainToken.Mountain))
    TokenValidator.canPlace(TerrainToken.Field, cell) shouldBe false
  }
