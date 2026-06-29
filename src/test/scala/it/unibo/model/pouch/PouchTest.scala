package it.unibo.model.pouch

import it.unibo.model.Token
import it.unibo.model.pouch.Pouches.Pouch
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PouchTest extends AnyFunSuite with Matchers{
  test("Un sacchetto deve restituire i token estratti e un nuovo stato"):
    val tokens: List[Token] = List(Token.Red, Token.Green, Token.Yellow, Token.Blue, Token.Gray)
    val initialPouch: Pouch = Pouch(tokens, seed = 42L)
    val initialSize: Int = initialPouch.size
    val (drawn, newPouch) = initialPouch.draw(2)
    drawn.size shouldBe 2
    newPouch.size shouldBe (initialSize - 2)
    initialPouch.size shouldBe initialSize

  test("Se si estraggono più token di quanti presenti, restituisce quelli rimasti e un sacchetto vuoto"):
    val startPouch = Pouch(List(Token.Green), seed = 1L)
    val (drawn, newPouch) = startPouch.draw(3)
    drawn.size shouldBe 1
    newPouch.isEmpty shouldBe true
    val emptyPouch = Pouch(List.empty, seed = 123L)
    val (emptyDrawn, newEmptyPouch) = emptyPouch.draw(3)
    emptyDrawn.isEmpty shouldBe true
    newEmptyPouch.isEmpty shouldBe true

  test("initialPouch deve istanziare un sacchetto completo e con il numero corretto di token totali"):
      val startingPouch = Pouch.initialPouch(seed = 99L)
      startingPouch.size shouldBe 60
      startingPouch.isEmpty shouldBe false

  test("initialPouch deve garantire il determinismo dell'estrazione a parità di seed"):
      val pouch1 = Pouch.initialPouch(seed = 777L)
      val pouch2 = Pouch.initialPouch(seed = 777L)
      val (drawn1, _) = pouch1.draw(3)
      val (drawn2, _) = pouch2.draw(3)
      drawn1 shouldBe drawn2
}
