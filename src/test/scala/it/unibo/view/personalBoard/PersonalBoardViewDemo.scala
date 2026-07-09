package it.unibo.view.personalBoard

import it.unibo.model.personalBoard.PersonalBoard
import scalafx.application.JFXApp3
import it.unibo.model.personalBoard.BoardSide.SideA
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

object PersonalBoardViewDemo extends JFXApp3:
  override def start(): Unit =
    val root: Pane = PersonalBoardView(PersonalBoard(SideA))

    stage = new JFXApp3.PrimaryStage:
      title = "Simple Personal Board"
      scene = new Scene(root, 800, 800)
