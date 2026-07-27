package it.unibo

import it.unibo.controller.GameController
import it.unibo.model._
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.view.GameView
import scalafx.application.JFXApp3
import scalafx.scene.Scene

object Main extends JFXApp3:

  override def start(): Unit =

    stage = new JFXApp3.PrimaryStage:
      title = "ScalHarmonies"

    stage.maximized = true

    val controller = GameController(GameModel(List()),
      (newModel, logMessage) =>
        println(s"${newModel.currentPlayer.name} sta facendo $logMessage"), stage)

    controller.start()
