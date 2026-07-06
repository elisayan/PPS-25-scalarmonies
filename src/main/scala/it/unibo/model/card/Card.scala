package it.unibo.model.card

import it.unibo.model.personalBoard.Coordinate
import it.unibo.model.token.TerrainToken

/** Represents the general concept of a Card within the game.
  */
sealed trait Card

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

/** Models the topological conformation required to place an animal's cubes.
  * @param requirements
  *   The list of spatial requirements that make up the habitat.
  */
case class Habitat(requirements: List[CellRequirement])

/** Represents an Animal Card, responsible for managing animal cubes and
  * calculating points. The instance is purely immutable: any state modification
  * generates a new card.
  */
trait AnimalCard extends Card:
  /** @return The name of the animal. */
  def name: String

  /** @return The habitat pattern required by the card. */
  def habitat: Habitat

  /** @return
    *   The progression of victory points obtainable, indexed by placed cubes.
    */
  def points: List[Int]

  /** @return The maximum number of animal cubes this card can hold. */
  def maxCubes: Int

  /** @return The number of animal cubes currently taken/placed. */
  def placedCubes: Int

  /** @return
    *   The victory points currently guaranteed based on the placed cubes.
    */
  def currentPoints: Int

  /** Attempts to take and place an animal cube from the card.
    * @return
    *   A `Some` containing the new instance of the card with updated cubes, or
    *   `None` if the card has already exhausted its available cubes.
    */
  def placeCube: Option[AnimalCard]

object AnimalCard:
  /** Creates a new instance of an Animal Card ready for the game (with zero
    * cubes placed).
    * @param name
    *   The name of the animal.
    * @param habitat
    *   The required habitat structure.
    * @param points
    *   The list of victory points (the length of the list defines the maximum
    *   number of cubes).
    * @return
    *   A new immutable instance of [[AnimalCard]].
    */
  def apply(name: String, habitat: Habitat, points: List[Int]): AnimalCard =
    AnimalCardImpl(name, habitat, points, points.length)

  private case class AnimalCardImpl(
      override val name: String,
      override val habitat: Habitat,
      override val points: List[Int],
      cubesRemaining: Int
  ) extends AnimalCard:

    override val maxCubes: Int = points.length

    override def placedCubes: Int = maxCubes - cubesRemaining

    override def currentPoints: Int =
      val placed = placedCubes
      if placed <= 0 then 0
      else if placed > points.length then points.last
      else points(placed - 1)

    override def placeCube: Option[AnimalCard] =
      if cubesRemaining > 0 then Some(copy(cubesRemaining = cubesRemaining - 1))
      else None
