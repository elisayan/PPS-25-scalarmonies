package it.unibo.model.centralboard

import it.unibo.model.card.AnimalCard
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken

/** Manages the central market board offering terrain tokens and animal cards.
  */
object CentralBoards:
  private val TokensPerSlot = 3
  private val SlotIds = List(1, 2, 3, 4, 5)

  private case class OfferState(
      tokenSlots: Map[Int, List[TerrainToken]],
      cardSlots: Map[Int, Option[AnimalCard]]
  )

  opaque type CentralBoard = OfferState

  object CentralBoard:
    /** @return an empty [[CentralBoard]] with no cards or tokens */
    def empty: CentralBoard =
      OfferState(
        SlotIds.map(id => id -> List.empty[TerrainToken]).toMap,
        SlotIds.map(id => id -> None).toMap
      )

    extension (b: CentralBoard)
      /** @param slot
        *   target slot index
        * @return
        *   true if the token slot is empty or invalid
        */
      def isTokenSlotEmpty(slot: Int): Boolean =
        b.tokenSlots.get(slot) match
          case Some(tokens) => tokens.isEmpty
          case None         => true

      /** @param slot
        *   target slot index
        * @return
        *   true if the card slot is empty or invalid
        */
      def isCardSlotEmpty(slot: Int): Boolean =
        b.cardSlots.get(slot) match
          case Some(card) => card.isEmpty
          case None       => true

      /** Refills all empty token and card slots from the pouch and deck.
        * @param pouch
        *   current game pouch
        * @param deck
        *   remaining animal cards deck
        * @return
        *   updated board, remaining pouch, and remaining deck
        */
      def fill(
          pouch: Pouch,
          deck: List[AnimalCard]
      ): (CentralBoard, Pouch, List[AnimalCard]) =
        val (nextTokens, nextPouch) = SlotIds.foldLeft((b.tokenSlots, pouch)):
          case ((currentSlots, currentPouch), slotId) =>
            if currentSlots.get(slotId).exists(_.isEmpty) then
              val (drawnTokens, updatedPouch) = currentPouch.draw(TokensPerSlot)
              (currentSlots.updated(slotId, drawnTokens), updatedPouch)
            else (currentSlots, currentPouch)

        val (nextCards, nextDeck) = SlotIds.foldLeft((b.cardSlots, deck)):
          case ((currentCards, currentDeck), slotId) =>
            if currentCards(slotId).isEmpty && currentDeck.nonEmpty then
              (
                currentCards.updated(slotId, Some(currentDeck.head)),
                currentDeck.tail
              )
            else (currentCards, currentDeck)
        (OfferState(nextTokens, nextCards), nextPouch, nextDeck)

      /** Extracts tokens from the selected slot.
        * @param slot
        *   target slot index
        * @return
        *   `Some` drawn tokens and updated board, or `None` if slot is
        *   empty/invalid
        */
      def takeTokens(slot: Int): Option[(List[TerrainToken], CentralBoard)] =
        for tokens <- b.tokenSlots.get(slot).filter(_.nonEmpty)
        yield
          val updatedTokens = b.tokenSlots.updated(slot, List.empty)
          (tokens, OfferState(updatedTokens, b.cardSlots))

      /** Extracts an animal card from the selected slot.
        * @param slot
        *   target slot index
        * @return
        *   `Some` drawn card and updated board, or `None` if slot is
        *   empty/invalid
        */
      def takeCard(slot: Int): Option[(AnimalCard, CentralBoard)] =
        for
          _ <- Option
            .when(SlotIds.contains(slot) && !b.isCardSlotEmpty(slot))(())
          card <- b.cardSlots(slot)
        yield
          val updatedCards = b.cardSlots.updated(slot, None)
          (card, OfferState(b.tokenSlots, updatedCards))

      /** @return map of currently available cards indexed by slot id */
      def availableCards: Map[Int, AnimalCard] =
        b.cardSlots.collect { case (id, Some(card)) => id -> card }

      /** @return map of currently available tokens indexed by slot id */
      def availableTokens: Map[Int, List[TerrainToken]] =
        b.tokenSlots.filter { case (_, tokens) => tokens.nonEmpty }
