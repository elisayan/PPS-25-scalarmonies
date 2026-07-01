package it.unibo.model.centralboard

import it.unibo.model.token.TerrainToken

object CentralBoards:
  private val SlotsCount = 5
  private val TokensPerSlot = 3

  opaque type CentralBoard = List[List[TerrainToken]]

  object CentralBoard:
    def empty: CentralBoard =
      (List.fill(SlotsCount)(List.empty[TerrainToken]))

    extension (b: CentralBoard)
      def isSlotEmpty(slotIndex: Int): Boolean =
        b.lift(slotIndex) match
          case Some(tokens) => tokens.isEmpty
          case None => true