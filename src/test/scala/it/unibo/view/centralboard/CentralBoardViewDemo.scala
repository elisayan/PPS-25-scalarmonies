package it.unibo.view.centralboard

import it.unibo.controller.GameController
import it.unibo.model.GameModel
import it.unibo.model.card.{AnimalCard, CellRequirement, Habitat}
import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken

import scalafx.application.JFXApp3
import scalafx.scene.Scene

object CentralBoardViewDemo extends JFXApp3:

  override def start(): Unit =

    val model = GameModel(List())
    val stage2: JFXApp3.PrimaryStage = new JFXApp3.PrimaryStage:
      title = "ScalHarmonies"
    val controller = GameController(model, stage2)

    val pouch = Pouch.initialPouch()

    val habitat1 =
      Habitat(
        List(
          CellRequirement(
            Coordinate(0, 0),
            TerrainToken.Field,
            1
          ),
          CellRequirement(
            Coordinate(1, 1),
            TerrainToken.Forest,
            2
          )
        )
      )

    val habitat2 =
      Habitat(
        List(
          CellRequirement(
            Coordinate(0, 0),
            TerrainToken.Water,
            1
          ),
          CellRequirement(
            Coordinate(-1, 1),
            TerrainToken.Mountain,
            2
          )
        )
      )

    val deck: List[AnimalCard] =
      List(
        AnimalCard(
          name = "Orso",
          habitat = habitat1,
          points = List(4, 7, 12, 16),
          imageId = "default.png"
        ),
        AnimalCard(
          name = "Lontra",
          habitat = habitat2,
          points = List(3, 6, 10, 15),
          imageId = "default.png"
        ),
        AnimalCard(
          name = "Volpe",
          habitat = habitat1,
          points = List(5, 8, 12, 18),
          imageId = "default.png"
        ),
        AnimalCard(
          name = "Gufo",
          habitat = habitat2,
          points = List(4, 9, 14, 20),
          imageId = "default.png"
        ),
        AnimalCard(
          name = "Cervo",
          habitat = habitat1,
          points = List(3, 7, 11, 16),
          imageId = "default.png"
        )
      )

    val (centralBoard, updatedPouch, _) =
      CentralBoard.empty.fill(
        pouch = pouch,
        deck = deck
      )

    val centralBoardView =
      CentralBoardView(
        board = centralBoard,
        pouchSize = updatedPouch.size,
        onCardClicked = controller.onTakeAnimalCard,
        onTokenClicked = controller.onTakeTokens
      )

    stage = new JFXApp3.PrimaryStage:
      title = "Central Board Demo"
      scene = new Scene(centralBoardView)
      sizeToScene()
