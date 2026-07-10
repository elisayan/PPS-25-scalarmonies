package it.unibo.view.token

import it.unibo.model.token.TerrainToken
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

case class TokenView(token: TerrainToken) extends StackPane:

  private val tokenShape = new Circle:
    radius = 30
    fill = tokenColor(token)
    stroke = Color.rgb(200, 180, 160)
    strokeWidth = 3
    effect = new DropShadow:
      radius = 2
      offsetX = 2
      offsetY = 2
      color = Color.rgb(0, 0, 0, 0.3)

  children.add(tokenShape)

  private def tokenColor(token: TerrainToken): Color =
    token match
      case TerrainToken.Water    => Color.rgb(0, 153, 153)
      case TerrainToken.Field    => Color.rgb(247, 240, 26)
      case TerrainToken.Mountain => Color.rgb(82, 88, 88)
      case TerrainToken.Ground   => Color.rgb(143, 91, 43)
      case TerrainToken.Forest   => Color.rgb(142, 165, 32)
      case TerrainToken.Building => Color.rgb(204, 38, 31)
