package it.unibo.model.cell

case class Cell(private val tokens: List[Any] = List()):

  def hasTokens: Boolean = tokens.nonEmpty

  def getTokens: List[Any] = tokens

  def placeToken(token: Any): Cell =
    Cell(tokens :+ token)

  def topToken: Option[Any] = tokens.lastOption
