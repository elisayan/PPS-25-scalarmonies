package it.unibo.model.card

import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Scorable
import it.unibo.model.scorecalculator.Score
import it.unibo.model.scorecalculator.Score.Score

/** Represents a generic playing card in the game
  */
sealed trait Card

/** Represents an immutable Animal Card managing animal cube progression and
  * score calculation
  */
trait AnimalCard extends Card with Scorable:
  /** @return the animal's name */
  def name: String

  /** @return The habitat pattern required by the card */
  def habitat: Habitat

  /** @return
    *   The progression of victory points obtainable, indexed by placed cubes
    */
  def points: List[Int]

  /** @return
    *   the image resource identifier
    */
  def imageId: String

  /** @return The maximum number of animal cubes this card can hold */
  def maxCubes: Int

  /** @return The number of animal cubes currently placed */
  def placedCubes: Int

  /** @return
    *   The victory points currently guaranteed based on the placed cubes
    */
  def currentPoints: Int

  /** Attempts to place an animal cube from the card
    * @return
    *   `Some` updated card if cubes remain, `None` otherwise
    */
  def placeCube: Option[AnimalCard]

object AnimalCard:
  /** Creates a new [[AnimalCard]] ready for play with zero cubes placed.
    * @param name
    *   The animal's name
    * @param habitat
    *   The required habitat structure.
    * @param points
    *   The list of victory points (the length of the list defines the maximum
    *   number of cubes).
    * @param imageId
    *   resource name for rendering
    */
  def apply(
      name: String,
      habitat: Habitat,
      points: List[Int],
      imageId: String = "default.png"
  ): AnimalCard =
    AnimalCardImpl(name, habitat, points, points.length, imageId)

  private case class AnimalCardImpl(
      override val name: String,
      override val habitat: Habitat,
      override val points: List[Int],
      cubesRemaining: Int,
      override val imageId: String
  ) extends AnimalCard:

    override val maxCubes: Int = points.length

    override def placedCubes: Int = maxCubes - cubesRemaining

    override def currentPoints: Int = placedCubes match
      case p if p <= 0            => 0
      case p if p > points.length => points.last
      case p                      => points(p - 1)

    override def placeCube: Option[AnimalCard] =
      if cubesRemaining > 0 then Some(copy(cubesRemaining = cubesRemaining - 1))
      else None

    override def computeScore(board: Option[PersonalBoard]): Score =
      Score(currentPoints)
