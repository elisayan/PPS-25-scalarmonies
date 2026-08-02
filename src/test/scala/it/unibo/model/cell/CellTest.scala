package it.unibo.model.cell

import it.unibo.model.token.TerrainToken
import it.unibo.model.token.TerrainToken.{Building, Forest, Mountain}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class CellTest extends AnyFlatSpec with Matchers:

  "A Cell" should "be empty when initialized" in {
    val cell = Cell()
    cell.getTokens should have size 0
    cell.hasTokens should be(false)
    cell.topToken shouldBe None
  }

  it should "return a new Cell when a Token is placed" in {
    val cell = Cell()
    val result: Cell = cell.placeToken(Building)

    result shouldBe a[Cell]
    result.hasTokens should be(true)
    result.getTokens shouldBe a[Seq[_]]

    cell.hasTokens should be(false)
  }

  it should "return the placed token(s)" in {
    val cell = Cell()
    val result: Cell = cell.placeToken(Building)

    result.getTokens should not be empty
    result.getTokens.head shouldBe Building
  }

  it should "preserve the right order of the tokens placed inside of it" in {
    val cell = Cell()
    val res: Cell = cell.placeToken(Building)
    val res1: Cell = res.placeToken(Mountain)
    val res2: Cell = res1.placeToken(Forest)

    res2.getTokens should have size 3

    res2.getTokens should contain theSameElementsInOrderAs Seq(
      Building,
      Mountain,
      Forest
    )
    res2.topToken shouldBe Some(Forest)
  }
