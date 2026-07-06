package it.unibo.model.cell

import it.unibo.model.token.TerrainToken

case class Cell(private val tokens: List[TerrainToken] = List()):

  def hasTokens: Boolean = tokens.nonEmpty

  def getTokens: Seq[TerrainToken] = tokens

  def placeToken(token: TerrainToken): Cell =
    Cell(tokens :+ token)

  def topToken: Option[TerrainToken] = tokens.lastOption

object Token:
  def apply(): Cell = Cell(List.empty)
