package it.unibo.model.pouch

import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** Test suite for the [[Pouches]] module. Verifies pure functional principles:
  * strict state immutability, deterministic draws via seeds, correct initial
  * token distribution, and safe edge-case handling.
  */
class PouchTest extends AnyFunSuite with Matchers:
  private val DrawnAmount = 3
  private val TestSeed = 42L
  test("Un sacchetto deve restituire i token estratti e un nuovo stato"):
    val tokens: List[TerrainToken] =
      List(
        TerrainToken.Building,
        TerrainToken.Water,
        TerrainToken.Field,
        TerrainToken.Mountain,
        TerrainToken.Forest
      )
    val initialPouch: Pouch = Pouch(tokens, TestSeed)
    val initialSize: Int = initialPouch.size
    val (drawn, newPouch) = initialPouch.draw(DrawnAmount)
    drawn.size shouldBe DrawnAmount
    newPouch.size shouldBe (initialSize - DrawnAmount)
    initialPouch.size shouldBe initialSize

  test(
    "Se si estraggono più token di quanti presenti, restituisce quelli rimasti e un sacchetto vuoto"
  ):
    val startPouch = Pouch(List(TerrainToken.Forest), TestSeed)
    val (drawn, newPouch) = startPouch.draw(DrawnAmount)
    drawn.size shouldBe 1
    newPouch.isEmpty shouldBe true
    val emptyPouch = Pouch(List.empty, TestSeed)
    val (emptyDrawn, newEmptyPouch) = emptyPouch.draw(DrawnAmount)
    emptyDrawn.isEmpty shouldBe true
    newEmptyPouch.isEmpty shouldBe true

  test(
    "initialPouch deve istanziare un sacchetto completo e con il numero corretto di token totali"
  ):
    val startingPouch = Pouch.initialPouch(TestSeed)
    val pouchSize = 120
    startingPouch.size shouldBe pouchSize
    startingPouch.isEmpty shouldBe false

  test(
    "initialPouch deve garantire il determinismo dell'estrazione a parità di seed"
  ):
    val pouch1 = Pouch.initialPouch(TestSeed)
    val pouch2 = Pouch.initialPouch(TestSeed)
    val (drawn1, _) = pouch1.draw(DrawnAmount)
    val (drawn2, _) = pouch2.draw(DrawnAmount)
    drawn1 shouldBe drawn2
