package it.unibo.view.homepage

import scalafx.application.JFXApp3
import scalafx.scene.Scene

object HomeViewDemo extends JFXApp3:

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title = "Scalarmonies - Setup"
      width = 500
      height = 600
      scene = new Scene:
        root = HomeView: (playerNames, selectedSide) =>
          println(s"Game Started!")
          println(
            s"Players (${playerNames.size}): ${playerNames.mkString(", ")}"
          )
          println(s"Chosen Side: $selectedSide")
