package it.unibo.model.centralboard

import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken

object CentralBoards:
  private val TokensPerSlot = 3
  private val SlotIds = List(1, 2, 3, 4, 5)

  opaque type CentralBoard = Map[Int, List[TerrainToken]]

  object CentralBoard:
    def empty: CentralBoard =
      SlotIds.map(id => id -> List.empty[TerrainToken]).toMap

    extension (b: CentralBoard)
      def isSlotEmpty(slot: Int): Boolean =
        b.get(slot) match
          case Some(tokens) => tokens.isEmpty
          case None         => true

      def fill(pouch: Pouch): (CentralBoard, Pouch) =
        SlotIds.foldLeft((b, pouch)) {
          case ((currentBoard, currentPouch), slotId) =>
            if currentBoard.isSlotEmpty(slotId) then
              val (drawnTokens, nextPouch) = currentPouch.draw(TokensPerSlot)
              val nextBoard = currentBoard.updated(slotId, drawnTokens)
              (nextBoard, nextPouch)
            else (currentBoard, currentPouch)
        }

      def take(slot: Int): Option[(List[TerrainToken], CentralBoard)] =
        if !SlotIds.contains(slot) then None
        else
          val tokens = b(slot)
          if tokens.isEmpty then None
          else Some((tokens, b.updated(slot, List.empty)))
