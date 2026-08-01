package it.unibo.model.card

import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard

/** Provides algorithmic matching capabilities to locate habitats on a board. */
object HabitatMatcher:
  extension (board: PersonalBoard)
    /** Checks whether a specific habitat orientation matches at the given
      * origin.
      * @param origin
      *   target board coordinate
      * @param habitat
      *   habitat pattern to verify
      * @return
      *   true if target is free of animals and all requirements are met
      */
    def isMatch(origin: Coordinate, habitat: Habitat): Boolean =
      val isTargetFree = board.cells.get(origin).exists(!_.hasAnimal)
      isTargetFree && habitat.requirements.forall: req =>
        val targetCoord = origin + req.offset
        board.cells
          .get(targetCoord)
          .exists: cell =>
            cell.topToken.contains(req.terrain) && cell.height == req.height

    /** Finds all valid habitat occurrences on the board across all 6 rotations.
      * @param habitat
      *   habitat pattern to search for
      * @return
      *   a set of matching occurrences
      */
    def findMatches(habitat: Habitat): Set[HabitatMatch] =
      for
        rotation <- habitat.allRotations
        origin <- board.cells.keys
        if board.isMatch(origin, rotation)
        involvedCells = rotation.requirements
          .map(req => origin + req.offset)
          .toSet
      yield HabitatMatch(origin, involvedCells)
