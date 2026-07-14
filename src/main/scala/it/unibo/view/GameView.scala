package it.unibo.view

import it.unibo.controller.GameController
import it.unibo.view.infopanel.InfoPanelView
import scalafx.scene.layout.BorderPane

class GameView(controller: GameController):

  // private val personalBoardView = PersonalBoardView(controller)
  // private val centralBoardView = CentralBoardView(controller)
  private val infoPanelView = InfoPanelView()

  val root = new BorderPane()
  // root.setCenter(centralBoardView.root)
  root.setRight(infoPanelView.root)
  // root.setBottom(personalBoardView.root)

  def refresh(playerName: String, logMessage: String): Unit =
    // personalBoardView.refresh(model.currentPlayer.board)
    // centralBoardView.refresh(model.centralBoard)

    if logMessage.startsWith("HEADER:") then
      infoPanelView.addTurnHeader(logMessage.stripPrefix("HEADER:"))
    else infoPanelView.addEntry(playerName, logMessage)
