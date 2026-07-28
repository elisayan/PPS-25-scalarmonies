package it.unibo.view.utils

import scalafx.scene.image.Image
import scala.collection.mutable

object ImageCache:
  private val cache: mutable.Map[String, Image] = mutable.Map()

  def getImage(path: String): Image =
    cache.getOrElseUpdate(path, {
      val resource = Option(getClass.getResource(path)).getOrElse(
        throw new IllegalArgumentException(s"Immagine non trovata nel classpath: $path")
      )
      new Image(resource.toExternalForm)
    })