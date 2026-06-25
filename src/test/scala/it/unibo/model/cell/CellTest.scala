package it.unibo.model.cell

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class CellTest extends AnyFlatSpec with Matchers:

  case class Token(color: String)

  "A Cell" should "be empty when initialized"
  val cell = new Cell(List())
  cell.getTokens should have size 0
  cell.hasTokens should be (false)

  it should "return a new Cell when a Token is placed"
  val token = Token("Red")
  val result: Cell = cell.placeToken(token)
  result shouldBe a [Cell]
  result.hasTokens should be (true)
  result.getTokens shouldBe a [List[_]]

  it should "return the placed token(s)"
  result.getTokens should not be empty
  result.getTokens shouldBe a [List[_]]
  result.getTokens.head should be (Token("Red"))

  it should "preserve the right order of the tokens placed inside of it"
  val cell2 = new Cell(List(Token("Red")))
  val token2 = Token("Grey")
  val token3 = Token("Green")
  val res1: Cell = cell2.placeToken(token2)
  val res2: Cell = res1.placeToken(token3)
  res2.getTokens should have size 3
  res2.getTokens should contain theSameElementsInOrderAs List(Token("Red"), Token("Grey"), Token("Green"))

