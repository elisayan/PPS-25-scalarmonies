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

    val players =
      List(
        Player(1, "Player1", PersonalBoard(SideA)),
        Player(2, "Player2", PersonalBoard(SideA))
      )
    
    val controller = GameController(
      GameModel(players),
      (newModel, logMessage) =>
        println(s"${newModel.currentPlayer.name} sta facendo $logMessage")
    )
    var view: GameView = GameView(controller)

    controller.startGame()
