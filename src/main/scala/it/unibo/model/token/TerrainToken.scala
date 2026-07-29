package it.unibo.model.token

/** Defines the available colors for terrain tokens. */
enum TokenColor:
  case Grey, Brown, Green, Yellow, Blue, Red

/** Defines the terrain token types used in the game. */
enum TerrainToken:
  case Water, Field, Mountain, Ground, Forest, Building

object TerrainToken:
  /** Returns the color associated with a terrain token.
   * @param token
   *  the terrain token.
   * @return
   *  the corresponding [[TokenColor]].
   */
  def colorOf(token: TerrainToken): TokenColor = token match
    case TerrainToken.Water    => TokenColor.Blue
    case TerrainToken.Mountain => TokenColor.Grey
    case TerrainToken.Forest   => TokenColor.Green
    case TerrainToken.Field    => TokenColor.Yellow
    case TerrainToken.Building => TokenColor.Red
    case TerrainToken.Ground   => TokenColor.Brown
