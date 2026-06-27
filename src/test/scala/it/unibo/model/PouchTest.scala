package it.unibo.model

import it.unibo.model.pouch.Pouches.Pouch
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PouchTest extends AnyFunSuite with Matchers{
  test("Un sacchetto deve restituire i token estratti e un nuovo stato")
    val tokens: List[Token] = List(Token.Red, Token.Green, Token.Yellow, Token.Blue, Token.Gray)
    val initialPouch = Pouch(tokens, seed = 42L)
    val initialSize = initialPouch.size
    val (drawn, newPouch) = initialPouch.draw(2)
    drawn.size shouldBe 2
    newPouch.size shouldBe (initialSize - 2)
    initialPouch.size shouldBe initialSize
}
