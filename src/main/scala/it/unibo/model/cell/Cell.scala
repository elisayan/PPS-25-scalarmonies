package it.unibo.model.cell

import it.unibo.model.token.TerrainToken

case class Cell(private val tokens: List[TerrainToken] = List()):

  def hasTokens: Boolean = tokens.nonEmpty

  def getTokens: List[Any] = tokens

  def placeToken(token: TerrainToken): Cell =
    Cell(tokens :+ token)

  def topToken: Option[Any] = tokens.lastOption
