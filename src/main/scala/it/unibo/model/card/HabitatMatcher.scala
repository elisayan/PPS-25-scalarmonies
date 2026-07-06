package it.unibo.model.card

import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}

object HabitatMatcher {
  def isMatch(board: PersonalBoard, origin: Coordinate, habitat: Habitat): Boolean =
    habitat.requirements.forall { req =>
      val targetCord = origin + req.offset
      board.cells.get(targetCord).exists { cell =>
        cell.topToken.contains(req.terrain) && cell.height == req.height
      }
    }
}