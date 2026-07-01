package it.unibo.model.centralboard

import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.pouch.Pouches.Pouch
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CentralBoardTest extends AnyFunSuite with Matchers:
  private val DefaultSeed = 42L
  private val ExpectedSlots = 5
  private val TokensPerSlot = 3
  private val SlotIds = List(1, 2, 3, 4, 5)

  test("Una plancia centrale appena creata deve essere vuota in tutti i suoi slot"):
    val emptyBoard = CentralBoard.empty
    SlotIds.foreach: slotId =>
      emptyBoard.isSlotEmpty(slotId) shouldBe true

  test("Il riempimento della plancia deve popolare gli slot vuoti e consumare il sacchetto"):
    val initialPouch = Pouch.initialPouch(DefaultSeed)
    val pouchInitialSize = initialPouch.size
    val emptyBoard = CentralBoard.empty
    val (filledBoard, newPouch) = emptyBoard.fill(initialPouch)
    SlotIds.foreach: slotId =>
      filledBoard.isSlotEmpty(slotId) shouldBe false
    val expectedTokensDrawn = ExpectedSlots * TokensPerSlot
    newPouch.size shouldBe (pouchInitialSize - expectedTokensDrawn)