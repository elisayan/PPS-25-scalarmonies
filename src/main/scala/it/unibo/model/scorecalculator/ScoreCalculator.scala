package it.unibo.model.scorecalculator

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Score.Score

object ScoreCalculator:

  def calculateScore(
      personalBoard: PersonalBoard,
      cards: List[AnimalCard]
  ): Score =
    calculateDetailedScore(personalBoard, cards)._1

  def calculateDetailedScore(
      board: PersonalBoard,
      cards: List[AnimalCard]
  ): (Score, Map[String, Score]) =
    val map: Map[String, Score] = Map(
      "Field" -> FieldsScoring.compute(board),
      "Water" -> WaterScoring.compute(board),
      "Building" -> BuildingsScoring.compute(board),
      "Forest" -> ForestsScoring.compute(board),
      "Mountain" -> MountainsScoring.compute(board),
      "Animal Cards" -> scoreFromAnimalCards(cards)
    )

    val totalScore =
      map.values.foldLeft(Score.zero)(_ + _) + scoreFromSpirits(board)

    (totalScore, map)

  private def scoreFromAnimalCards(cards: List[AnimalCard]): Score =
    Score(cards.foldLeft(0)((current, card) => current + card.currentPoints))

  private def scoreFromSpirits(board: PersonalBoard): Score = Score.zero
