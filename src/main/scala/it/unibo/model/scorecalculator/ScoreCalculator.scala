package it.unibo.model.scorecalculator

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}
import it.unibo.model.scorecalculator.Score.Score

trait ScoreCalculator:

  def calculateScore(personalBoard: PersonalBoard, cards: List[AnimalCard]): Score

  def calculateDetailedScore(board: PersonalBoard, cards: List[AnimalCard]): (Score, Map[String, Score])

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

    override def calculateScore(board: PersonalBoard, cards: List[AnimalCard]): Score =
      scoreFromFields(board) +
        scoreFromMountains(board) +
        scoreFromBuildings(board) +
        scoreFromForests(board) +
        scoreFromWater(board) +
        scoreFromAnimalCards(cards) +
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

    private def scoreFromAnimalCards(cards: List[AnimalCard]): Score =
      Score(cards.foldLeft(0)((current, card) => current + card.currentPoints))

    private def scoreFromSpirits(board: PersonalBoard): Score = Score.zero

    override def calculateDetailedScore(board: PersonalBoard, cards: List[AnimalCard]): (Score, Map[String, Score]) = ???


