package it.unibo.model.cell

class Cell(private val tokens: List[Any]):

  def hasTokens: Boolean = tokens.nonEmpty

  def getTokens: List[Any] = tokens

  def placeToken(token: Any): Cell =
    val list = List(token)
    val cell = new Cell(tokens ::: list)
    cell