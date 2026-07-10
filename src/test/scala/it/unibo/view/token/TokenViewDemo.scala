package it.unibo.view.token

import it.unibo.model.token.TerrainToken
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.geometry.Insets

object TokenViewDemo extends JFXApp3:

  override def start(): Unit =

    val tokens = Seq(
      TokenView(TerrainToken.Water),
      TokenView(TerrainToken.Field),
      TokenView(TerrainToken.Mountain),
      TokenView(TerrainToken.Ground),
      TokenView(TerrainToken.Forest),
      TokenView(TerrainToken.Building)
    )

    val container = new HBox(10): // spacing 10px tra i token
      children = tokens
      padding = Insets(20)
      style = "-fx-background-color: white;"

    stage = new JFXApp3.PrimaryStage:
      title = "Token View Demo"
      scene = new Scene:
        root = container
