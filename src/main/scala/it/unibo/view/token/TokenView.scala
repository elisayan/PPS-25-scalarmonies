package it.unibo.view.token

import it.unibo.model.token.TerrainToken
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout.StackPane

case class TokenView(token: TerrainToken, onSelect: TerrainToken => Unit) extends StackPane:

  private val tokenImage = new ImageView:
    image = loadTokenImage(token)
    fitWidth = 40
    fitHeight = 40
    preserveRatio = true
    smooth = true

  children.add(tokenImage)

  onMouseClicked = _ =>
    onSelect(token)

  private def loadTokenImage(token: TerrainToken): Image =
    val fileName = token.toString.toLowerCase
    val path = s"/tokens/$fileName.png"
    val resource = Option(getClass.getResource(path))
      .getOrElse(
        throw new IllegalArgumentException(
          s"Token image not found: $path"
        )
      )
    new Image(resource.toExternalForm)
