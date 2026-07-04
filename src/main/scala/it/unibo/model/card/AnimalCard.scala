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
  def cubesRemaining: Int
  def maxCubes: Int
  def placedCubes: Int
  def currentPoints: Int

object AnimalCard:
  def apply(name: String, habitat: Habitat, points: List[Int]): AnimalCard =
    AnimalCardImpl(name, habitat, points)

  private case class AnimalCardImpl(
                                     override val name: String,
                                     override val habitat: Habitat,
                                     override val points: List[Int],
                                   ) extends AnimalCard:

    override val cubesRemaining: Int = points.size

    override val maxCubes: Int = points.length

    override def placedCubes: Int = maxCubes - cubesRemaining

    override def currentPoints: Int =
      val placed = placedCubes
      if placed <= 0 then 0
      else if placed > points.length then points.last
      else points(placed - 1)