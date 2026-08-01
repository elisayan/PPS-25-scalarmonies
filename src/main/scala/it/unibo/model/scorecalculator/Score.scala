package it.unibo.model.scorecalculator

import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Score.Score

/** Represents a contract for any entity or rule capable of computing a score
  */
trait Scorable:

  /** Computes the score evaluated on the given personal board.
    *
    * @param board
    *   the PersonalBoard to evaluate
    * @return
    *   the calculated Score
    */
  def computeScore(board: PersonalBoard): Score

/** Represents the game scoring domain, modeling non-negative victory points
  * accumulated by a player.
  */
object Score:

  /** Represents a non-negative numerical score value. */
  opaque type Score = Int

  /** Creates a new Score instance from a non-negative integer.
    *
    * @param value
    *   the integer value of the score, must be non-negative
    * @return
    *   the created Score
    * @throws IllegalArgumentException
    *   if value is negative
    */
  def apply(value: Int): Score =
    require(value >= 0)
    value

  /** Constant representing a Score of value zero. */
  val zero: Score = 0

  extension (s: Score)

    /** Adds another Score to this score.
      *
      * @param other
      *   the Score to add
      * @return
      *   a new Score representing the sum
      */
    def +(other: Score): Score = other + s

    /** Subtracts another Score from this score, guaranteeing a minimum result
      * of zero.
      *
      * @param other
      *   the Score to subtract
      * @return
      *   a new Score representing the non-negative difference
      */
    def -(other: Score): Score =
      val res = s - other
      if res < 0 then 0 else res

    /** Returns the underlying primitive Int representation of this Score.
      *
      * @return
      *   the integer value
      */
    def toInt: Int = s
