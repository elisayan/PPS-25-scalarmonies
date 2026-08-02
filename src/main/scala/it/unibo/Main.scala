package it.unibo

import it.unibo.controller.GameController
import it.unibo.model._
import scalafx.application.JFXApp3

object Main extends JFXApp3:

  override def start(): Unit =

    stage = new JFXApp3.PrimaryStage:
      title = "ScalHarmonies"

    stage.maximized = true

    val controller = GameController(GameModel(List()), stage)

    controller.start()
