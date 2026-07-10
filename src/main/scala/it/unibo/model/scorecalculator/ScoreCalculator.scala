package it.unibo.model.scorecalculator

import it.unibo.model.personalBoard.{BoardSide, Coordinate, PersonalBoard}
import it.unibo.model.scorecalculator.Score.Score
import it.unibo.model.token.TerrainToken

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
      if connected.isEmpty then
        (group, farAway)
      else
        buildGroup(group ++ connected, farAway)

    override def calculateScore(board: PersonalBoard): Score =
      scoreFromFields(board) +
        scoreFromMountains(board) +
        scoreFromBuildings(board) +
        scoreFromForests(board) +
        scoreFromWater(board) +
        scoreFromAnimalCards(board) +
        scoreFromSpirits(board)

    private def scoreFromFields(board: PersonalBoard): Score =
      val allFields = board.cells.collect {
        case (coord, cell) if cell.topToken == TerrainToken.Field => coord
      }.toSet

      @scala.annotation.tailrec
      def calculateTotal(unprocessed: Set[Coordinate], currentScore: Score): Score =
        if unprocessed.isEmpty then currentScore
        else
          val (completedGroup, leftOver) = buildGroup(Set(unprocessed.head), unprocessed.tail)
          val points = if completedGroup.size >= 3 then Score(5) else Score.zero
          calculateTotal(leftOver, currentScore + points)

      calculateTotal(allFields, Score.zero)

    private def scoreFromMountains(board: PersonalBoard): Score =
      val mountainHeights = board.cells.collect {
        case (coord, cell) if cell.topToken == TerrainToken.Mountain => coord -> cell.height
      }
      val allMountains = mountainHeights.keySet

      def pointsForHeight(height: Int): Score = height match
        case 1 => Score(1)
        case 2 => Score(3)
        case 3 => Score(7)
        case _ => Score.zero

      @scala.annotation.tailrec
      def calculateTotal(unprocessed: Set[Coordinate], currentScore: Score): Score =
        if unprocessed.isEmpty then currentScore
        else
          val (completedGroup, leftOver) = buildGroup(Set(unprocessed.head), unprocessed.tail)

          if completedGroup.size < 2 then
            calculateTotal(leftOver, currentScore)
          else
            val groupPoints = completedGroup.foldLeft(Score.zero) { (acc, coord) =>
              acc + pointsForHeight(mountainHeights.getOrElse(coord, 0))
            }
            calculateTotal(leftOver, currentScore + groupPoints)

      calculateTotal(allMountains, Score.zero)

    private def scoreFromBuildings(board: PersonalBoard): Score =
      val allBuildings: Set[Coordinate] = board.cells.collect {
        case (coord, cell) if cell.topToken == TerrainToken.Building => coord
      }.toSet

      def isValidBuilding(coord: Coordinate): Boolean =
        val neighbourTerrains: Set[TerrainToken] = coord.allNeighbours
          .flatMap(board.cells.get)
          .map(_.topToken.get)

        neighbourTerrains.size >= 3

      val validBuildingsCount = allBuildings.count(isValidBuilding)

      Score(validBuildingsCount * 5)

    private def scoreFromForests(board: PersonalBoard): Score =
      val treeHeights: Map[Coordinate, Int] = board.cells.collect {
        case (coord, cell) if cell.topToken == TerrainToken.Forest => coord -> cell.height
      }

      def pointsForHeight(height: Int): Score = height match
        case 1 => Score(1)
        case 2 => Score(3)
        case 4 => Score(7)
        case _ => Score.zero

      treeHeights.values.foldLeft(Score.zero) { (acc, height) =>
        acc + pointsForHeight(height)
      }

    private def scoreFromWater(board: PersonalBoard): Score =

      val allWater: Set[Coordinate] = board.cells.collect {
        case (coord, cell) if cell.topToken == TerrainToken.Water => coord
      }.toSet

      def longestPathInGroup(current: Coordinate, visited: Set[Coordinate], group: Set[Coordinate]): Int =
        val validNeighbours = current.allNeighbours.intersect(group).diff(visited)
        if validNeighbours.isEmpty then
          visited.size
        else
          validNeighbours.map(next => longestPathInGroup(next, visited + next, group)).max
      @scala.annotation.tailrec
      def findRiverLengths(unprocessed: Set[Coordinate], lengths: List[Int]): List[Int] =
        if unprocessed.isEmpty then lengths
        else
          val (completedGroup, leftOver) = buildGroup(Set(unprocessed.head), unprocessed.tail)

          val maxLengthForThisGroup = completedGroup.map(start =>
            longestPathInGroup(start, Set(start), completedGroup)
          ).maxOption.getOrElse(0)

          findRiverLengths(leftOver, maxLengthForThisGroup :: lengths)

      def pointsForSideA(length: Int): Score = length match
        case l if l <= 1 => Score.zero
        case 2 => Score(2)
        case 3 => Score(5)
        case 4 => Score(8)
        case 5 => Score(11)
        case 6 => Score(15)
        case l => Score(15 + (l - 6) * 4)

      board.side match
        case BoardSide.SideA =>
          val allLengths = findRiverLengths(allWater, Nil)
          val longestRiver = allLengths.maxOption.getOrElse(0)
          pointsForSideA(longestRiver)

        case BoardSide.SideB =>
          Score(10)

    private def scoreFromAnimalCards(board: PersonalBoard): Score =
      ???

    private def scoreFromSpirits(board: PersonalBoard): Score = ???

    override def calculateDetailedScore(board: PersonalBoard): (Score, Map[String, Score]) = ???


