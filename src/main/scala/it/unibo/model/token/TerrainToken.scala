package it.unibo.model.token

enum TokenColor:
  case Grey, Brown, Green, Yellow, Blue, Red

enum TerrainToken:
  case Water, Field, Mountain, Ground, Forest, Building

object TerrainToken:
  def colorOf(token: TerrainToken): TokenColor = token match
    case TerrainToken.Water    => TokenColor.Blue
    case TerrainToken.Mountain => TokenColor.Grey
    case TerrainToken.Forest   => TokenColor.Green
    case TerrainToken.Field    => TokenColor.Yellow
    case TerrainToken.Building => TokenColor.Red
    case TerrainToken.Ground   => TokenColor.Brown
