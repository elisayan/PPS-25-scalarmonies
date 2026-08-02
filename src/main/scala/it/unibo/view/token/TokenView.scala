package it.unibo.view.token

import it.unibo.model.token.TerrainToken
import it.unibo.view.utils.ImageCache
import scalafx.scene.Cursor
import scalafx.scene.effect.DropShadow
import scalafx.scene.image.ImageView
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color

case class TokenView(token: TerrainToken, onSelect: TerrainToken => Unit)
    extends StackPane:

  private val tokenImage = new ImageView:
    val fileName = token.toString.toLowerCase
    image = ImageCache.getImage(s"/tokens/$fileName.png")
    fitWidth = 40
    fitHeight = 40
    preserveRatio = true
    smooth = true

  tokenImage.effect = new DropShadow(3.0, 1.0, 1.0, Color.color(0, 0, 0, 0.4))
  children.add(tokenImage)

  this.cursor = Cursor.Hand
  onMouseClicked = _ => onSelect(token)
