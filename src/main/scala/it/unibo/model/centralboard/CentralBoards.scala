package it.unibo.model.centralboard

import it.unibo.model.card.AnimalCard
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken

object CentralBoards:
  private val TokensPerSlot = 3
  private val SlotIds = List(1, 2, 3, 4, 5)

  private case class OfferState(
      tokenSlots: Map[Int, List[TerrainToken]],
      cardSlots: Map[Int, Option[AnimalCard]]
  )

  opaque type CentralBoard = OfferState

  object CentralBoard:
    def empty: CentralBoard =
      OfferState(
        SlotIds.map(id => id -> List.empty[TerrainToken]).toMap,
        SlotIds.map(id => id -> None).toMap
      )

    extension (b: CentralBoard)
      def isTokenSlotEmpty(slot: Int): Boolean =
        b.tokenSlots.get(slot) match
          case Some(tokens) => tokens.isEmpty
          case None         => true

      def isCardSlotEmpty(slot: Int): Boolean =
        b.cardSlots.get(slot) match
          case Some(card) => card.isEmpty
          case None       => true

      def fill(
          pouch: Pouch,
          deck: List[AnimalCard]
      ): (CentralBoard, Pouch, List[AnimalCard]) =
        val (nextTokens, nextPouch) = SlotIds.foldLeft((b.tokenSlots, pouch)) {
          case ((currentSlots, currentPouch), slotId) =>
            if currentSlots.get(slotId).exists(_.isEmpty) then
              val (drawnTokens, updatedPouch) = currentPouch.draw(TokensPerSlot)
              (currentSlots.updated(slotId, drawnTokens), updatedPouch)
            else (currentSlots, currentPouch)
        }
        val (nextCards, nextDeck) = SlotIds.foldLeft((b.cardSlots, deck)) {
          case ((currentCards, currentDeck), slotId) =>
            if currentCards(slotId).isEmpty && currentDeck.nonEmpty then
              (
                currentCards.updated(slotId, Some(currentDeck.head)),
                currentDeck.tail
              )
            else (currentCards, currentDeck)
        }
        (OfferState(nextTokens, nextCards), nextPouch, nextDeck)

      def takeTokens(slot: Int): Option[(List[TerrainToken], CentralBoard)] =
        if !SlotIds.contains(slot) || b.isTokenSlotEmpty(slot) then None
        else
          val tokens = b.tokenSlots(slot)
          val updatedTokens = b.tokenSlots.updated(slot, List.empty)
          Some((tokens, OfferState(updatedTokens, b.cardSlots)))

      def takeCard(slot: Int): Option[(AnimalCard, CentralBoard)] =
        if !SlotIds.contains(slot) || b.isCardSlotEmpty(slot) then None
        else
          val card = b.cardSlots(slot).get
          val updatedCards = b.cardSlots.updated(slot, None)
          Some((card, OfferState(b.tokenSlots, updatedCards)))

      def availableCards: Map[Int, AnimalCard] =
        b.cardSlots.collect { case (id, Some(card)) => id -> card }

      def availableTokens: Map[Int, List[TerrainToken]] =
        b.tokenSlots.filter { case (_, tokens) => tokens.nonEmpty }
