package it.unibo.model.cell

import it.unibo.model.token.TerrainToken

case class Cell(
    private val tokens: List[TerrainToken] = List(),
    hasAnimal: Boolean = false
):

  def hasTokens: Boolean = tokens.nonEmpty

  def getTokens: List[TerrainToken] = tokens

  def placeToken(token: TerrainToken): Cell =
    Cell(tokens :+ token)

  def topToken: Option[TerrainToken] = tokens.lastOption

  def height: Int = tokens.size

  def placeAnimal: Option[Cell] =
    if tokens.isEmpty || hasAnimal then None
    else Some(this.copy(hasAnimal = true))
