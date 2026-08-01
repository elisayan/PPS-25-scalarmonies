package it.unibo.model.card

import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken

/** A single spatial and terrain requirement within a habitat pattern.
  * @param offset
  *   relative coordinates from the pattern's origin
  * @param terrain
  *   required token type
  * @param height
  *   exact stack height required
  */
case class CellRequirement(
    offset: Coordinate,
    terrain: TerrainToken,
    height: Int
)

/** Represents a valid habitat match found on the board.
  * @param origin
  *   coordinate matching the requirement offset (0, 0)
  * @param involvedCells
  *   all board coordinates forming the habitat
  */
case class HabitatMatch(origin: Coordinate, involvedCells: Set[Coordinate])

/** Models the topological conformation required to place an animal's cubes.
  * @param requirements
  *   The list of spatial requirements forming the habitat.
  */
case class Habitat(requirements: List[CellRequirement]):
  /** @return a new [[Habitat]] rotated by 60 degrees clockwise */
  def rotate60: Habitat =
    Habitat(requirements.map(req => req.copy(offset = req.offset.rotate60)))

  /** @return the set of all 6 possible hexagonal rotations of this habitat */
  def allRotations: Set[Habitat] =
    List.iterate(this, 6)(_.rotate60).toSet

/** DSL utilities for declarative habitat definitions. */
object HabitatDSL:
  extension (coord: Coordinate)
    /** Creates a [[CellRequirement]] at this coordinate. */
    def req(terrain: TerrainToken, height: Int): CellRequirement =
      CellRequirement(coord, terrain, height)
