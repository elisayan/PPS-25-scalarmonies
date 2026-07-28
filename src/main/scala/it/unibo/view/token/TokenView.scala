package it.unibo.view.token

import it.unibo.model.token.TerrainToken
import it.unibo.view.utils.ImageCache
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout.StackPane

case class TokenView(token: TerrainToken, onSelect: TerrainToken => Unit)
    extends StackPane:

  private val tokenImage = new ImageView:
    val fileName = token.toString.toLowerCase
    image = ImageCache.getImage(s"/tokens/$fileName.png")
    fitWidth = 40
    fitHeight = 40
    preserveRatio = true
    smooth = true

  children.add(tokenImage)

  onMouseClicked = _ => onSelect(token)