package it.unibo.model.card

import it.unibo.model.personalBoard.Coordinate
import it.unibo.model.token.TerrainToken

sealed trait Card

case class CellRequirement(
    offset: Coordinate,
    terrain: TerrainToken,
    height: Int
)

case class Habitat(requirements: List[CellRequirement])

trait AnimalCard extends Card:
  def name: String
  def habitat: Habitat
  def points: List[Int]
  def maxCubes: Int
  def placedCubes: Int
  def currentPoints: Int
  def placeCube: Option[AnimalCard]

object AnimalCard:
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