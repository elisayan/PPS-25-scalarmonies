package it.unibo

import it.unibo.controller.GameController
import it.unibo.model._
import it.unibo.model.*
import it.unibo.model.personalBoard.BoardSide.SideA
import it.unibo.model.personalBoard.PersonalBoard
import it.unibo.view.GameView
import scalafx.application.JFXApp3
import scalafx.scene.Scene

object Main extends JFXApp3:

  override def start(): Unit =

    val players =
      List(Player(1, PersonalBoard(SideA)), Player(2, PersonalBoard(SideA)))
    var view: GameView = null
    val controller = GameController(GameModel(players), () => view.refresh())

    view = GameView(controller)
    stage = new JFXApp3.PrimaryStage:
      title = "Scalarmonies"
      scene = new Scene:
        root = view.root
