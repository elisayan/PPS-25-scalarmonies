package it.unibo.view

import it.unibo.controller.GameController
import scalafx.scene.layout.BorderPane


class GameView(controller: GameController):

  //private val personalBoardView = PersonalBoardView(controller)
  //private val centralBoardView = CentralBoardView(controller)
  //private val infoPanelView = InfoPanelView(controller)

  val root = new BorderPane //:
    //top = infoPanelView.root
    //center = centralBoardView.root
    //bottom = personalBoardView.root

  def refresh(): Unit =
    //val model = controller.currentModel

    //personalBoardView.refresh(model.currentPlayer.board)
    //centralBoardView.refresh(model.centralBoard)
    //infoPanelView.refresh(model)