package it.unibo.view.animalcube

import it.unibo.model.Player
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.token.TerrainToken
import it.unibo.view.personalboard.PersonalBoardView
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color

object BoardWithCubesDemo extends JFXApp3:

  override def start(): Unit =
    var board = PersonalBoard(SideA)
    board = board.placeToken(TerrainToken.Field, Coordinate(0, 0)).get
    board = board.placeAnimalOnCell(Coordinate(0, 0)).get
    board = board.placeToken(TerrainToken.Ground, Coordinate(-2, 1)).get
    board = board.placeToken(TerrainToken.Forest, Coordinate(-2, 1)).get
    board = board.placeAnimalOnCell(Coordinate(-2, 1)).get
    board = board.placeToken(TerrainToken.Mountain, Coordinate(2, 1)).get
    board = board.placeToken(TerrainToken.Mountain, Coordinate(2, 1)).get
    board = board.placeToken(TerrainToken.Mountain, Coordinate(2, 1)).get
    board = board.placeAnimalOnCell(Coordinate(2, 1)).get
    board = board.placeToken(TerrainToken.Water, Coordinate(0, -2)).get

    val dummyPlayer = Player(
      id = 1,
      name = "Demo Player",
      board = board,
      activeCards = List(),
      completedCards = List()
    )

    val boardView = PersonalBoardView(
      player = dummyPlayer,
      onCellClicked = coord => println(s"Hai cliccato la cella: $coord"),
      highlightedCells = List(Coordinate(0, 2), Coordinate(0, -2))
    )

    stage = new JFXApp3.PrimaryStage:
      title = "Test Visivo: Cubi Animale su Plancia"
      scene = new Scene:
        fill = Color.rgb(240, 240, 240)
        root = new StackPane:
          padding = Insets(50)
          children = boardView
