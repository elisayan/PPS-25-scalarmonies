package it.unibo.model.scorecalculator

import it.unibo.model.personalboard.BoardSide
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.scorecalculator.Score.Score
import it.unibo.model.token.TerrainToken

def pointsForHeight(height: Int): Score = height match
  case 1 => Score(1)
  case 2 => Score(3)
  case 3 => Score(7)
  case _ => Score.zero

private def scoreForCoords(
    board: PersonalBoard,
    coords: Iterable[Coordinate]
): Score =
  coords.foldLeft(Score.zero) { (acc, coord) =>
    val height = board.cells.get(coord).map(_.height).getOrElse(0)
    acc + pointsForHeight(height)
  }

trait TerrainScoring extends Scorable:
  override def computeScore(board: PersonalBoard): Score = compute(board)
  def compute(board: PersonalBoard): Score

object FieldsScoring extends TerrainScoring:

  override def compute(board: PersonalBoard): Score =
    val allFields = board.coordsWithTerrain(TerrainToken.Field)
    val groups = board.findConnectedGroups(allFields)
    val validGroupsCount = groups.count(_.size >= 2)
    Score(validGroupsCount * 5)

object MountainsScoring extends TerrainScoring:

  override def compute(board: PersonalBoard): Score =
    val allMountains = board.coordsWithTerrain(TerrainToken.Mountain)
    val groups = board.findConnectedGroups(allMountains)

    groups.filter(_.size >= 2).foldLeft(Score.zero) { (acc, group) =>
      acc + scoreForCoords(board, group)
    }

object BuildingsScoring extends TerrainScoring:

  override def compute(board: PersonalBoard): Score =
    val allBuildings = board.coordsWithTerrain(TerrainToken.Building)

    def isValidBuilding(coord: Coordinate): Boolean =
      val neighbourTerrains: Set[TerrainToken] = coord.allNeighbours
        .flatMap(board.cells.get)
        .flatMap(_.topToken)
      neighbourTerrains.size >= 3

    val validBuildingsCount = allBuildings.count(isValidBuilding)
    Score(validBuildingsCount * 5)

object ForestsScoring extends TerrainScoring:

  override def compute(board: PersonalBoard): Score =
    val forestCoords = board.coordsWithTerrain(TerrainToken.Forest)
    scoreForCoords(board, forestCoords)

object WaterScoring extends TerrainScoring:

  override def compute(board: PersonalBoard): Score =
    val allWater = board.coordsWithTerrain(TerrainToken.Water)

    def maxPathInGroup(group: Set[Coordinate]): Int =
      def dfs(current: Coordinate, visited: Set[Coordinate]): Int =
        val validNeighbours =
          current.allNeighbours.intersect(group).diff(visited)
        if validNeighbours.isEmpty then visited.size
        else validNeighbours.map(next => dfs(next, visited + next)).max

      if group.isEmpty then 0
      else group.map(start => dfs(start, Set(start))).max

    def pointsForSideA(length: Int): Score = length match
      case l if l <= 1 => Score.zero
      case 2           => Score(2)
      case 3           => Score(5)
      case 4           => Score(8)
      case 5           => Score(11)
      case 6           => Score(15)
      case l           => Score(15 + (l - 6) * 4)

    board.side match
      case BoardSide.SideA =>
        val groups = board.findConnectedGroups(allWater)
        val longestRiver = groups.map(maxPathInGroup).maxOption.getOrElse(0)
        pointsForSideA(longestRiver)

      case BoardSide.SideB =>
        val allLand = board.cells.keySet.diff(allWater)
        val islands = board.findConnectedGroups(allLand)
        Score(islands.size * 5)
