package it.unibo.view.cell

import it.unibo.model.cell.Cell
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken.*
import it.unibo.view.token.TokenView
import scalafx.scene.Scene
import scalafx.application.JFXApp3
import scalafx.scene.layout.{Pane, StackPane}

object CellViewDemo extends JFXApp3:

  override def start(): Unit =
    val f = (a: String) => println(a)
    val c = Cell()
    val c1 = c.placeToken(Ground)
    val c2 = c1.placeToken(Ground)
    val c3 = c2.placeToken(Forest)
    val cv = CellView(Coordinate(0, 0), c3, (20.0, 50.0), f)
    val root: StackPane = StackPane()
    root.children.add(cv)
    stage = new JFXApp3.PrimaryStage:
      title = "Simple Hexagon"
      scene = new Scene(root, 400, 400)
