package it.unibo.model.token

import it.unibo.model.cell.Cell
import it.unibo.model.token.TerrainToken.*

object TokenValidator:

  private val MaxStackHeight = 3

  private def topToken(cell: Cell): Option[TerrainToken] =
    cell.topToken.collect { case t: TerrainToken => t }

  def canPlace(token: TerrainToken, cell: Cell): Boolean =
    val tokens = cell.getTokens.length
    if tokens >= MaxStackHeight then false
    else
      token match
        case TerrainToken.Water | TerrainToken.Field => !cell.hasTokens
        case Mountain                                => ???
        case Forest                                  => ???
        case Building                                => ???
        case Ground                                  => ???
