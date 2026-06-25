package it.unibo.model.cell

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers

class CellTest extends AnyFlatSpec with Matchers:

  "A Cell" should "be empty when initialized"
  val cell = new Cell(List(10))
  cell.getTokens should have size 0
  cell.hasTokens should be (false)

  it should "return a new Cell when a Token is placed"
  val token = new Token("Red")
  val result: Cell = cell.placeToken(token)
  result shouldBe a [Cell]
  result.hasToken() should be (false)
  result.getTokens() should have size 0

  it should "return the placed token(s)"
  result.getTokens() should not be empty
  result.getTokens() should contain Token "Red"
  result.getTokens() shouldBe a [List[_]]
  
  it should "preserve the right order of the tokens placed inside of it"
  val cell2 = new Cell()
  val token2 = new Token()
  val token3 = new Token()
  val res1: Cell = cell.placeToken(token2)
  val res2: cell = res1.placeToken(token3)
  res2.getTokens should have size 2
  res2.getTokens should startWith Token "Red"
  res2.getTokens should endWith Token "Green"

