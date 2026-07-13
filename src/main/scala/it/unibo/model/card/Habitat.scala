package it.unibo.model.card

import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken

/** Defines a single spatial and terrain constraint required to compose a
  * habitat.
  * @param offset
  *   The relative coordinate (distance and direction) from the point of origin.
  * @param terrain
  *   The type of terrain token required at this specific coordinate.
  * @param height
  *   The exact height (number of tokens) required for the terrain column.
  */
case class CellRequirement(
    offset: Coordinate,
    terrain: TerrainToken,
    height: Int
)

case class HabitatMatch(origin: Coordinate, involvedCells: Set[Coordinate])

/** Models the topological conformation required to place an animal's cubes.
  * @param requirements
  *   The list of spatial requirements that make up the habitat.
  */
case class Habitat(requirements: List[CellRequirement]):
  def rotate60: Habitat =
    Habitat(requirements.map(req => req.copy(offset = req.offset.rotate60)))

  def allRotations: Set[Habitat] =
    List.iterate(this, 6)(_.rotate60).toSet
