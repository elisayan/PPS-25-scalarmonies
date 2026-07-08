package it.unibo.model

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalBoard.PersonalBoard

case class Player(id: Int, board: PersonalBoard, activeCards: List[AnimalCard] = List())
