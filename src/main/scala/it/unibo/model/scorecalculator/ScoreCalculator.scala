package it.unibo.model.scorecalculator

import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}
import it.unibo.model.scorecalculator.Score.Score
import it.unibo.model.token.TerrainToken

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

    private def scoreFromFields(board: PersonalBoard): Score = ???

    private def scoreFromMountains(board: PersonalBoard): Score = ???

    private def scoreFromBuildings(board: PersonalBoard): Score = ???

    private def scoreFromTrees(board: PersonalBoard): Score = ???

    private def scoreFromWater(board: PersonalBoard): Score = ???

    private def scoreFromAnimalCards(board: PersonalBoard): Score =
      ???

    private def scoreFromSpirits(board: PersonalBoard): Score = ???

    override def calculateDetailedScore(board: PersonalBoard): (Score, Map[String, Score]) = ???


    