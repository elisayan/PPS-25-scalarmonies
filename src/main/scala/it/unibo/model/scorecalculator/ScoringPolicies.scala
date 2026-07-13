package it.unibo.model.scorecalculator

import it.unibo.model.personalboard.{Coordinate, PersonalBoard, BoardSide}
import it.unibo.model.scorecalculator.Score.Score
import it.unibo.model.token.TerrainToken

def pointsForHeight(height: Int): Score = height match
  case 1 => Score(1)
  case 2 => Score(3)
  case 3 => Score(7)
  case _ => Score.zero

object FieldsScoring:

  def compute(
      board: PersonalBoard,
      buildGroup: (
          Set[Coordinate],
          Set[Coordinate]
      ) => (Set[Coordinate], Set[Coordinate])
  ): Score =
    val allFields = board.cells.collect {
      case (coord, cell) if cell.topToken.contains(TerrainToken.Field) => coord
    }.toSet

    @scala.annotation.tailrec
    def calculateTotal(
        unprocessed: Set[Coordinate],
        currentScore: Score
    ): Score =
      if unprocessed.isEmpty then currentScore
      else
        val (completedGroup, leftOver) =
          buildGroup(Set(unprocessed.head), unprocessed.tail)
        val points = if completedGroup.size >= 2 then Score(5) else Score.zero
        calculateTotal(leftOver, currentScore + points)

    calculateTotal(allFields, Score.zero)

object MountainsScoring:

  def compute(
      board: PersonalBoard,
      buildGroup: (
          Set[Coordinate],
          Set[Coordinate]
      ) => (Set[Coordinate], Set[Coordinate])
  ): Score =
    val mountainHeights = board.cells.collect {
      case (coord, cell) if cell.topToken.contains(TerrainToken.Mountain) =>
        coord -> cell.height
    }
    val allMountains = mountainHeights.keySet

    @scala.annotation.tailrec
    def calculateTotal(
        unprocessed: Set[Coordinate],
        currentScore: Score
    ): Score =
      if unprocessed.isEmpty then currentScore
      else
        val (completedGroup, leftOver) =
          buildGroup(Set(unprocessed.head), unprocessed.tail)

        if completedGroup.size < 2 then calculateTotal(leftOver, currentScore)
        else
          val groupPoints = completedGroup.foldLeft(Score.zero) {
            (acc, coord) =>
              acc + pointsForHeight(mountainHeights.getOrElse(coord, 0))
          }
          calculateTotal(leftOver, currentScore + groupPoints)

    calculateTotal(allMountains, Score.zero)

object BuildingsScoring:

  def compute(board: PersonalBoard): Score =
    val allBuildings: Set[Coordinate] = board.cells.collect {
      case (coord, cell) if cell.topToken.contains(TerrainToken.Building) =>
        coord
    }.toSet

    def isValidBuilding(coord: Coordinate): Boolean =
      val neighbourTerrains: Set[TerrainToken] = coord.allNeighbours
        .flatMap(board.cells.get)
        .flatMap(_.topToken)
      neighbourTerrains.size >= 3

    val validBuildingsCount = allBuildings.count(isValidBuilding)
    Score(validBuildingsCount * 5)

object ForestsScoring:

  def compute(board: PersonalBoard): Score =
    val treeHeights: Map[Coordinate, Int] = board.cells.collect {
      case (coord, cell) if cell.topToken.contains(TerrainToken.Forest) =>
        coord -> cell.height
    }

    treeHeights.values.foldLeft(Score.zero) { (acc, height) =>
      acc + pointsForHeight(height)
    }

object WaterScoring:

  def compute(
      board: PersonalBoard,
      buildGroup: (
          Set[Coordinate],
          Set[Coordinate]
      ) => (Set[Coordinate], Set[Coordinate])
  ): Score =
    val allWater: Set[Coordinate] = board.cells.collect {
      case (coord, cell) if cell.topToken.contains(TerrainToken.Water) => coord
    }.toSet

    def maxPathInGroup(group: Set[Coordinate]): Int =
      def dfs(current: Coordinate, visited: Set[Coordinate]): Int =
        val validNeighbours =
          current.allNeighbours.intersect(group).diff(visited)
        if validNeighbours.isEmpty then visited.size
        else validNeighbours.map(next => dfs(next, visited + next)).max

      if group.isEmpty then 0
      else group.map(start => dfs(start, Set(start))).max

    @scala.annotation.tailrec
    def findRiverLengths(
        unprocessed: Set[Coordinate],
        lengths: List[Int]
    ): List[Int] =
      if unprocessed.isEmpty then lengths
      else
        val (completedGroup, leftOver) =
          buildGroup(Set(unprocessed.head), unprocessed.tail)
        val maxLengthForThisGroup = maxPathInGroup(completedGroup)
        findRiverLengths(leftOver, maxLengthForThisGroup :: lengths)

    def pointsForSideA(length: Int): Score = length match
      case l if l <= 1 => Score.zero
      case 2           => Score(2)
      case 3           => Score(5)
      case 4           => Score(8)
      case 5           => Score(11)
      case 6           => Score(15)
      case l           => Score(15 + (l - 6) * 4)

    @scala.annotation.tailrec
    def countIslands(unprocessedLand: Set[Coordinate], islandCount: Int): Int =
      if unprocessedLand.isEmpty then islandCount
      else
        val (_, leftOverLand) =
          buildGroup(Set(unprocessedLand.head), unprocessedLand.tail)
        countIslands(leftOverLand, islandCount + 1)

    board.side match
      case BoardSide.SideA =>
        val allLengths = findRiverLengths(allWater, Nil)
        val longestRiver = allLengths.maxOption.getOrElse(0)
        pointsForSideA(longestRiver)

      case BoardSide.SideB =>
        val allLand = board.cells.keySet.diff(allWater)
        val totalIslands = countIslands(allLand, 0)
        Score(totalIslands * 5)
