package it.unibo.view.personalboard

import it.unibo.controller.GameController
import it.unibo.model.{GameModel, Player}
import it.unibo.model.personalboard.PersonalBoard
import scalafx.application.JFXApp3
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.view.GameView
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

object PersonalBoardViewDemo extends JFXApp3:
  override def start(): Unit =
    val model: GameModel = GameModel(List())
    val player = Player(1, "Daniel", PersonalBoard(SideA), List(), List())
    val view = GameView()
    val controller: GameController = GameController(model, view, (_, _) => ())
    val root: Pane = PersonalBoardView(player, controller)

    stage = new JFXApp3.PrimaryStage:
      title = "Simple Personal Board"
      scene = new Scene(root, 800, 800)
