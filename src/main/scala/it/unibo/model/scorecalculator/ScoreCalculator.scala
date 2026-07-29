package it.unibo.model.scorecalculator

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Score.Score

object ScoreCalculator:

  private val terrainScorers: Map[String, TerrainScoring] = Map(
    "Field" -> FieldsScoring,
    "Water" -> WaterScoring,
    "Building" -> BuildingsScoring,
    "Forest" -> ForestsScoring,
    "Mountain" -> MountainsScoring
  )

  def calculateScore(
      personalBoard: PersonalBoard,
      cards: List[AnimalCard]
  ): Score =
    calculateDetailedScore(personalBoard, cards)._1

  def calculateDetailedScore(
      board: PersonalBoard,
      cards: List[AnimalCard]
  ): (Score, Map[String, Score]) =
    val terrainMap: Map[String, Score] = terrainScorers.map {
      case (label, scorer) => label -> scorer.computeScore(board)
    }

    val animalScore =
      cards.foldLeft(Score.zero)((acc, card) => acc + card.computeScore(board))

    val detailedMap = terrainMap + ("Animal Cards" -> animalScore)
    val totalScore = detailedMap.values.reduce(_ + _)

    (totalScore, detailedMap)
