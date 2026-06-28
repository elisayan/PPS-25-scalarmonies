package it.unibo.model.cell

import it.unibo.model.token.TerrainToken
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class CellTest extends AnyFlatSpec with Matchers:

  "A Cell" should "be empty when initialized" in {
    val cell = Cell(List())
    cell.getTokens should have size 0
    cell.hasTokens should be(false)
    cell.topToken shouldBe None
  }

  it should "return a new Cell when a Token is placed" in {
    val cell = Cell(List())
    val result: Cell = cell.placeToken(TerrainToken.Building)

    result shouldBe a[Cell]
    result.hasTokens should be(true)
    result.getTokens shouldBe a[List[_]]

    cell.hasTokens should be(false)
  }

  it should "return the placed token(s)" in {
    val cell = Cell(List())
    val result: Cell = cell.placeToken(TerrainToken.Building)

    result.getTokens should not be empty
    result.getTokens.head shouldBe TerrainToken.Building
  }

  it should "preserve the right order of the tokens placed inside of it" in {
    val cell2 = Cell(List(TerrainToken.Building))

    val res1: Cell = cell2.placeToken(TerrainToken.Mountain)
    val res2: Cell = res1.placeToken(TerrainToken.Forest)

    res2.getTokens should have size 3

    res2.getTokens should contain theSameElementsInOrderAs List(
      TerrainToken.Building,
      TerrainToken.Mountain,
      TerrainToken.Forest
    )
    res2.topToken shouldBe Some(TerrainToken.Forest)
  }
