package it.unibo.view.token

import it.unibo.model.token.TerrainToken
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

case class TokenView(token: TerrainToken) extends StackPane:

  private val tokenShape = new Circle:
    radius = 30
    fill = tokenColor(token)

  children.add(tokenShape)

  private def tokenColor(token: TerrainToken): Color =
    token match
      case TerrainToken.Water    => Color.Blue
      case TerrainToken.Field    => Color.Yellow
      case TerrainToken.Mountain => Color.Gray
      case TerrainToken.Ground   => Color.Brown
      case TerrainToken.Forest   => Color.Green
      case TerrainToken.Building => Color.Red