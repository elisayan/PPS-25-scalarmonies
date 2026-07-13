package it.unibo.model.card

import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard

object HabitatMatcher:
  def isMatch(
      board: PersonalBoard,
      origin: Coordinate,
      habitat: Habitat
  ): Boolean =
    val isTargetFree = board.cells.get(origin).exists(cell => !cell.hasAnimal)
    isTargetFree && habitat.requirements.forall { req =>
      val targetCoord = origin + req.offset
      board.cells.get(targetCoord).exists { cell =>
        cell.topToken.contains(req.terrain) && cell.height == req.height
      }
    }

  def findMatches(board: PersonalBoard, habitat: Habitat): Set[HabitatMatch] =
    for
      rotation <- habitat.allRotations
      origin <- board.cells.keys
      if isMatch(board, origin, rotation)
      involvedCells = rotation.requirements
        .map(req => origin + req.offset)
        .toSet
    yield HabitatMatch(origin, involvedCells)
