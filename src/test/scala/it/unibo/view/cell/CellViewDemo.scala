package it.unibo.view.cell

import it.unibo.model.cell.Cell
import it.unibo.model.personalBoard.Coordinate
import scalafx.scene.Scene
import scalafx.application.JFXApp3
import scalafx.scene.layout.StackPane

object CellViewDemo extends JFXApp3:

  override def start(): Unit =
    val root: StackPane = CellView(Coordinate(0, 0), Cell())
    stage = new JFXApp3.PrimaryStage:
      title = "Simple Hexagon"
      scene = new Scene(root,400,400)

