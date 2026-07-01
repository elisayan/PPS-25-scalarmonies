package it.unibo.model.centralboard

import it.unibo.model.centralboard.CentralBoards.CentralBoard
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CentralBoardTest extends AnyFunSuite with Matchers:
  private val DefaultSeed = 42L
  private val ExpectedSlots = 5
  private val TokensPerSlot = 3

  test("Una plancia centrale appena creata deve essere vuota in tutti i suoi slot"):
    val emptyBoard = CentralBoard.empty
    (1 to ExpectedSlots).foreach: slotId =>
      emptyBoard.isSlotEmpty(slotId) shouldBe true