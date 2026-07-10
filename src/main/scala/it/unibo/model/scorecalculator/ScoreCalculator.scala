package it.unibo.model.scorecalculator

import it.unibo.model.personalBoard.PersonalBoard
import it.unibo.model.scorecalculator.Score.Score

trait ScoreCalculator:

  def calculateScore(personalBoard: PersonalBoard): Score

  def calculateDetailedScore(board: PersonalBoard): (Score, Map[String, Score])

  object ScoreCalculator:

    def apply(): ScoreCalculator = ScoreCalculatorImpl()

    private case class ScoreCalculatorImpl() extends ScoreCalculator:

      override def calculateScore(board: PersonalBoard): Score =
        scoreFromFields(board) +
          scoreFromMountains(board) +
          scoreFromBuildings(board) +
          scoreFromTrees(board) +
          scoreFromWater(board) +
          scoreFromAnimalCards(board) +
          scoreFromSpirits(board)

      private def scoreFromFields(personalBoard: PersonalBoard): Score = ???

      private def scoreFromMountains(personalBoard: PersonalBoard): Score = ???

      private def scoreFromBuildings(personalBoard: PersonalBoard): Score = ???

      private def scoreFromTrees(personalBoard: PersonalBoard): Score = ???

      private def scoreFromWater(personalBoard: PersonalBoard): Score = ???

      private def scoreFromAnimalCards(personalBoard: PersonalBoard): Score =
        ???

      private def scoreFromSpirits(personalBoard: PersonalBoard): Score = ???

      override def calculateDetailedScore(board: PersonalBoard): (Score, Map[String, Score]) = ???
