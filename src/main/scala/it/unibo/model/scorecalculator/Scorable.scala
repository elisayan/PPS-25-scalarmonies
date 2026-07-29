package it.unibo.model.scorecalculator

import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Score.Score

trait Scorable:
  def computeScore(board: PersonalBoard): Score
