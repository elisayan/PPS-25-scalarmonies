package it.unibo.model.centralboard

import it.unibo.model.card.{AnimalCard, Habitat}
import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.pouch.Pouches.Pouch
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CentralBoardTest extends AnyFunSuite with Matchers:
  private val DefaultSeed = 42L
  private val TokensPerSlot = 3
  private val SlotIds = List(1, 2, 3, 4, 5)
  private val TargetSlot = 1
  private val testHabitat = Habitat(List.empty)
  private val testCard = AnimalCard("TestAnimal", testHabitat, List(5, 9, 12))
  private val initialDeck = List.fill(10)(testCard)
  private val initialPouch = Pouch.initialPouch(DefaultSeed)

  test(
    "Una plancia centrale appena creata deve essere vuota in tutti i suoi slot"
  ):
    val emptyBoard = CentralBoard.empty
    SlotIds.foreach: slotId =>
      emptyBoard.isTokenSlotEmpty(slotId) shouldBe true
    emptyBoard.availableCards shouldBe empty

  test("Il riempimento deve popolare simmetricamente gli slot 1-5"):
    val (filledBoard, _, remainingDeck) =
      CentralBoard.empty.fill(initialPouch, initialDeck)
    SlotIds.foreach: slotId =>
      filledBoard.isTokenSlotEmpty(slotId) shouldBe false
      filledBoard.isCardSlotEmpty(slotId) shouldBe false
    filledBoard.availableCards should have size 5
    remainingDeck should have size (initialDeck.size - 5)

  test(
    "Prelevare da uno slot valido deve restituire i token, svuotare lo slot e lasciare intatti gli altri"
  ):
    val (filledBoard, _, _) =
      CentralBoard.empty.fill(initialPouch, initialDeck)
    val result = filledBoard.takeTokens(TargetSlot)
    result match
      case Some((drawnTokens, updatedBoard)) =>
        drawnTokens.size shouldBe TokensPerSlot
        updatedBoard.isTokenSlotEmpty(TargetSlot) shouldBe true
        updatedBoard.isTokenSlotEmpty(2) shouldBe false
      case None =>
        fail("Il prelievo doveva avere successo, ma ha restituito None")

  test(
    "Prelevare da uno slot vuoto o inesistente deve fallire sia per i token che per le carte (restituendo None)"
  ):
    val emptyBoard = CentralBoard.empty
    emptyBoard.takeTokens(99) shouldBe None
    emptyBoard.takeCard(99) shouldBe None
    emptyBoard.takeTokens(TargetSlot) shouldBe None
    emptyBoard.takeCard(TargetSlot) shouldBe None

  test("Prelevare una carta da uno slot valido deve svuotare solo quello slot"):
    val (filledBoard, pouch, deck) =
      CentralBoard.empty.fill(initialPouch, initialDeck)
    val result = filledBoard.takeCard(TargetSlot)
    result match
      case Some((card, updatedBoard)) =>
        card.name shouldBe "TestAnimal"
        updatedBoard.isCardSlotEmpty(TargetSlot) shouldBe true
        updatedBoard.isCardSlotEmpty(2) shouldBe false
        updatedBoard.availableCards should have size 4
      case None =>
        fail("Il prelievo della carta doveva funzionare")

  test(
    "Il fine turno deve ripristinare sia lo slot della carta sia quello dei token precedentemente svuotati"
  ):
    val (filledBoard, pouchAfterFill, deckAfterFill) =
      CentralBoard.empty.fill(initialPouch, initialDeck)
    val pouchSizeBeforeTurn = pouchAfterFill.size
    val (_, boardMinusCard) = filledBoard.takeCard(TargetSlot).get
    val (_, boardMinusBoth) = boardMinusCard.takeTokens(TargetSlot).get
    val (refilledBoard, finalPouch, finalDeck) =
      boardMinusBoth.fill(pouchAfterFill, deckAfterFill)
    refilledBoard.isCardSlotEmpty(TargetSlot) shouldBe false
    refilledBoard.isTokenSlotEmpty(TargetSlot) shouldBe false
    refilledBoard.availableCards should have size 5
    finalDeck should have size (deckAfterFill.size - 1)
    finalPouch.size shouldBe (pouchSizeBeforeTurn - TokensPerSlot)
