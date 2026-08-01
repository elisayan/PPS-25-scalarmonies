package it.unibo.model.scorecalculator

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Score.Score

/** Main calculator object providing functions to compute total and detailed
  * scores for a player's personal board and animal cards.
  */
object ScoreCalculator:

  private val terrainScorers: Map[String, TerrainScoring] = Map(
    "Field" -> FieldsScoring,
    "Water" -> WaterScoring,
    "Building" -> BuildingsScoring,
    "Forest" -> ForestsScoring,
    "Mountain" -> MountainsScoring
  )

  /** Calculates the overall total score obtained from a personal board and a
    * list of animal cards.
    *
    * @param personalBoard
    *   the PersonalBoard to evaluate
    * @param cards
    *   the list of AnimalCard instances to evaluate
    * @return
    *   the total calculated Score
    */
  def calculateScore(
      personalBoard: PersonalBoard,
      cards: List[AnimalCard]
  ): Score =
    calculateDetailedScore(personalBoard, cards)._1

  /** Calculates the total score along with a detailed breakdown of scores for
    * each category.
    *
    * @param board
    *   the PersonalBoard to evaluate
    * @param cards
    *   the list of AnimalCard instances to evaluate
    * @return
    *   a tuple containing the total Score and a Map associating each category
    *   label to its Score
    */
  def calculateDetailedScore(
      board: PersonalBoard,
      cards: List[AnimalCard]
  ): (Score, Map[String, Score]) =
    val terrainMap: Map[String, Score] = terrainScorers.map {
      case (label, scorer) => label -> scorer.computeScore(board)
    }

    val animalScore =
      cards.foldLeft(Score.zero)((acc, card) =>
        acc + card.computeScore(board)
      )

    val detailedMap = terrainMap + ("Animal Cards" -> animalScore)
    val totalScore = detailedMap.values.reduce(_ + _)

    (totalScore, detailedMap)
