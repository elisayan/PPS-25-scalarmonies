package it.unibo.model

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.PersonalBoard

/** Represents a player participating in the game.
  * @param id
  *   the player's unique identifier.
  * @param name
  *   the player's name.
  * @param board
  *   the player's personal board.
  * @param activeCards
  *   the animal cards currently owned by the player.
  * @param completedCards
  *   the completed animal cards.
  */
case class Player(
    id: Int,
    name: String,
    board: PersonalBoard,
    activeCards: List[AnimalCard] = List(),
    completedCards: List[AnimalCard] = List()
)

object Player:

  /** Adds domain-specific operations to [[Player]]. */
  extension (player: Player)
    /** Checks whether the player has reached the maximum number of active
      * animal cards.
      * @param limit
      *   the maximum number of active cards.
      * @return
      *   true if the player cannot take another animal card.
      */
    def hasReachedAnimalCardLimit(limit: Int): Boolean =
      player.activeCards.size >= limit
