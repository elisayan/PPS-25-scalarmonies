package it.unibo.view

import it.unibo.controller.GameController
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class GameView(controller: GameController):

  val root = new VBox()
  private val playerLabel = new Label()
  private val stateLabel = new Label()
  private val takeButton = new Button("Take tokens")

  takeButton.onAction = _ =>
    controller.onTakeTokens(1)
    takeButton.disable = true

  root.children.addAll(playerLabel, stateLabel, takeButton)

  def refresh(): Unit =
    val model = controller.currentModel
    playerLabel.text = s"Current player: ${model.currentPlayer.id}"
    stateLabel.text = s"State: ${model.turnState}"

  refresh()
