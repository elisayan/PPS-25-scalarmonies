package it.unibo.view.scorecalculator

import it.unibo.model.Player
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.token.TerrainToken.{Field, Forest, Mountain}
import scalafx.application.JFXApp3
import scalafx.scene.Scene as scene
import scalafx.scene.layout.Pane

object ScoreCalculatorViewDemo extends JFXApp3:

  override def start(): Unit =
    var b1 = PersonalBoard(SideA)
    b1 = b1.placeToken(Field, Coordinate(0, 0)).get
    b1 = b1.placeToken(Forest, Coordinate(0, 2)).get
    var b2 = PersonalBoard(SideA)
    b2 = b2.placeToken(Mountain, Coordinate(0, 0)).get
    b2 = b2.placeToken(Mountain, Coordinate(0, -2)).get

    val p1 = Player(1, "Player1", b1)
    val p2 = Player(2, "Player2", b2)
    val list = List(p1, p2)

    val root: Pane =
      ScoreCalculatorView(list, (0.0, 0.0))
    stage = new JFXApp3.PrimaryStage:
      title = "Simple Hexagon"
      scene = new scene(root, 800, 600)
