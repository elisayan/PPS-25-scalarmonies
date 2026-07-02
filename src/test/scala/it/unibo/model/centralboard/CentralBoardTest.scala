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
  private val TargetSlot = 1

  test(
    "Una plancia centrale appena creata deve essere vuota in tutti i suoi slot"
  ):
    val emptyBoard = CentralBoard.empty
    SlotIds.foreach: slotId =>
      emptyBoard.isSlotEmpty(slotId) shouldBe true

  test(
    "Il riempimento della plancia deve popolare gli slot vuoti e consumare il sacchetto"
  ):
    val initialPouch = Pouch.initialPouch(DefaultSeed)
    val pouchInitialSize = initialPouch.size
    val emptyBoard = CentralBoard.empty
    val (filledBoard, newPouch) = emptyBoard.fill(initialPouch)
    SlotIds.foreach: slotId =>
      filledBoard.isSlotEmpty(slotId) shouldBe false
    val expectedTokensDrawn = ExpectedSlots * TokensPerSlot
    newPouch.size shouldBe (pouchInitialSize - expectedTokensDrawn)

  test(
    "Prelevare da uno slot valido deve restituire i token, svuotare lo slot e lasciare intatti gli altri"
  ):
    val (filledBoard, _) =
      CentralBoard.empty.fill(Pouch.initialPouch(DefaultSeed))
    val result = filledBoard.take(TargetSlot)
    result match
      case Some((drawnTokens, updatedBoard)) =>
        drawnTokens.size shouldBe TokensPerSlot
        updatedBoard.isSlotEmpty(TargetSlot) shouldBe true
        updatedBoard.isSlotEmpty(2) shouldBe false
      case None =>
        fail("Il prelievo doveva avere successo, ma ha restituito None")

  test(
    "Prelevare da uno slot vuoto o inesistente deve fallire (restituendo None)"
  ):
    val emptyBoard = CentralBoard.empty
    val resultEmpty = emptyBoard.take(TargetSlot)
    resultEmpty shouldBe None
    val resultInvalid = emptyBoard.take(99)
    resultInvalid shouldBe None
