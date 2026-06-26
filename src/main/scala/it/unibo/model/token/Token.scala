package it.unibo.model.token

enum TokenColor:
  case Grey, Brown, Green, Yellow, Blue, Red

enum TerrainToken(val color: TokenColor):
  case Mountain extends TerrainToken(TokenColor.Grey)
  case TreeTrunk extends TerrainToken(TokenColor.Brown)
  case TreeFoliage extends TerrainToken(TokenColor.Green)
  case Field extends TerrainToken(TokenColor.Yellow)
  case Water extends TerrainToken(TokenColor.Blue)
  case Building extends TerrainToken(TokenColor.Red)
