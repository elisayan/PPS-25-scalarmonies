package it.unibo.model

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.PersonalBoard

/** Represents a player participating in the game.
 * @param id
 *  the player's unique identifier.
 * @param name
 *  the player's name.
 * @param board
 *  the player's personal board.
 * @param activeCards
 *  the animal cards currently owned by the player.
 * @param completedCards
 *  the completed animal cards.
 */
case class Player(
    id: Int,
    name: String,
    board: PersonalBoard,
    activeCards: List[AnimalCard] = List(),
    completedCards: List[AnimalCard] = List()
)
