package it.unibo.view.infopanel

import it.unibo.controller.GameController
import it.unibo.model.TurnState
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class InfoPanelView(controller: GameController):

  val root = new VBox()
  private val currentPlayerLabel = new Label()

  root.children.add(currentPlayerLabel)

  def refresh(id: Int, state: TurnState): Unit =
    currentPlayerLabel.text = s"Player: $id\nTurn: $state"
