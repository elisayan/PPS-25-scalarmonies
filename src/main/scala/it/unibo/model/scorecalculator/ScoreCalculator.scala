package it.unibo.model.scorecalculator

import it.unibo.model.card.AnimalCard
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Score.Score

object ScoreCalculator:

  def calculateScore(
      personalBoard: PersonalBoard,
      cards: List[AnimalCard]
  ): Score =
    scoreFromFields(personalBoard) +
      scoreFromMountains(personalBoard) +
      scoreFromBuildings(personalBoard) +
      scoreFromForests(personalBoard) +
      scoreFromWater(personalBoard) +
      scoreFromAnimalCards(cards) +
      scoreFromSpirits(personalBoard)

  def calculateDetailedScore(
      board: PersonalBoard,
      cards: List[AnimalCard]
  ): (Score, Map[String, Score]) =
    val map: Map[String, Score] = Map(
      "Field" -> scoreFromFields(board),
      "Water" -> scoreFromWater(board),
      "Building" -> scoreFromBuildings(board),
      "Forest" -> scoreFromForests(board),
      "Mountain" -> scoreFromMountains(board),
      "Animal Cards" -> scoreFromAnimalCards(cards)
    )

    (calculateScore(board, cards), map)

  @scala.annotation.tailrec
  private def buildGroup(
      group: Set[Coordinate],
      remaining: Set[Coordinate]
  ): (Set[Coordinate], Set[Coordinate]) =
    val (connected, farAway) =
      remaining.partition(coord => coord.allNeighbours.exists(group.contains))
    if connected.isEmpty then (group, farAway)
    else buildGroup(group ++ connected, farAway)

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
