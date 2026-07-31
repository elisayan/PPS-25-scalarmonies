package it.unibo.model.card

import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard

object HabitatMatcher:
  extension(board: PersonalBoard)
    def isMatch(origin: Coordinate, habitat: Habitat): Boolean =
      val isTargetFree = board.cells.get(origin).exists(!_.hasAnimal)
      isTargetFree && habitat.requirements.forall : req =>
        val targetCoord = origin + req.offset
        board.cells.get(targetCoord).exists: cell =>
          cell.topToken.contains(req.terrain) && cell.height == req.height

    def findMatches(habitat: Habitat): Set[HabitatMatch] =
      for
        rotation <- habitat.allRotations
        origin <- board.cells.keys
        if board.isMatch(origin, rotation)
        involvedCells = rotation.requirements.map(req => origin + req.offset).toSet
      yield HabitatMatch(origin, involvedCells)
