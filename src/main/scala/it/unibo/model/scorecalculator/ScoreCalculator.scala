package it.unibo.model.scorecalculator

import it.unibo.model.personalBoard.{ Coordinate, PersonalBoard}
import it.unibo.model.scorecalculator.Score.Score

trait ScoreCalculator:

  def calculateScore(personalBoard: PersonalBoard): Score

  def calculateDetailedScore(board: PersonalBoard): (Score, Map[String, Score])

object ScoreCalculator:
  def apply(): ScoreCalculator = ScoreCalculatorImpl()

  private case class ScoreCalculatorImpl() extends ScoreCalculator:

    @scala.annotation.tailrec
    private def buildGroup(group: Set[Coordinate], remaining: Set[Coordinate]): (Set[Coordinate], Set[Coordinate]) =
      val (connected, farAway) = remaining.partition(coord =>
        coord.allNeighbours.exists(group.contains)
      )
      if connected.isEmpty then (group, farAway)
      else buildGroup(group ++ connected, farAway)

    override def calculateScore(board: PersonalBoard): Score =
      scoreFromFields(board) +
        scoreFromMountains(board) +
        scoreFromBuildings(board) +
        scoreFromForests(board) +
        scoreFromWater(board) +
        scoreFromAnimalCards(board) +
        scoreFromSpirits(board)

    private def scoreFromFields(board: PersonalBoard): Score =
      FieldsScoring.compute(board, buildGroup)

    private def scoreFromMountains(board: PersonalBoard): Score =
      MountainsScoring.compute(board, buildGroup)

    private def scoreFromBuildings(board: PersonalBoard): Score =
      BuildingsScoring.compute(board)

    private def scoreFromForests(board: PersonalBoard): Score =
      ForestsScoring.compute(board)

    private def scoreFromWater(board: PersonalBoard): Score =
      WaterScoring.compute(board, buildGroup)

    private def scoreFromAnimalCards(board: PersonalBoard): Score =
      ???

    private def scoreFromSpirits(board: PersonalBoard): Score = ???

    override def calculateDetailedScore(board: PersonalBoard): (Score, Map[String, Score]) = ???


