package it.unibo.model

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.PersonalBoard

case class Player(
    id: Int,
    name: String,
    board: PersonalBoard,
    activeCards: List[AnimalCard] = List(),
    completedCards: List[AnimalCard] = List()
)
