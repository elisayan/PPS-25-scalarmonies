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
