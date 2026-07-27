package it.unibo.view

import it.unibo.controller.GameController
import it.unibo.model.{GameModel, Player}
import it.unibo.model.card.{AnimalCard, CellRequirement, Habitat}
import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken
import it.unibo.model.token.TerrainToken.{Building, Field, Water}
import scalafx.application.JFXApp3
import scalafx.scene.Scene

object GameViewDemo extends JFXApp3:

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title = "Harmonies Game"
      fullScreen = true

      val forestHabitat = Habitat(
        List(
          CellRequirement(Coordinate(0, 0), TerrainToken.Forest, 1),
          CellRequirement(Coordinate(1, 1), TerrainToken.Field, 2)
        )
      )
      val waterHabitat = Habitat(
        List(
          CellRequirement(Coordinate(0, 0), TerrainToken.Water, 1),
          CellRequirement(Coordinate(-1, 1), TerrainToken.Mountain, 2)
        )
      )

      val animalCards: List[AnimalCard] = List(
        AnimalCard(
          "Orso",
          forestHabitat,
          List(4, 7, 12, 16),
          imageId = "default.png"
        ),
        AnimalCard(
          "Lontra",
          waterHabitat,
          List(3, 6, 10, 15),
          imageId = "default.png"
        ),
        AnimalCard(
          "Cervo",
          forestHabitat,
          List(3, 7, 11, 16),
          imageId = "default.png"
        ),
        AnimalCard(
          "Volpe",
          forestHabitat,
          List(5, 8, 12, 18),
          imageId = "default.png"
        ),
        AnimalCard(
          "Gufo",
          waterHabitat,
          List(4, 9, 14, 20),
          imageId = "default.png"
        )
      )

      // 2. GIOCATORI
      val player1 = Player(
        id = 1,
        name = "Player 1",
        board = PersonalBoard(SideA),
        activeCards = animalCards.take(2),
        completedCards = animalCards.drop(2)
      )
      val player2 = Player(
        id = 2,
        name = "Player 2",
        board = PersonalBoard(SideA),
        activeCards = animalCards.take(1),
        completedCards = List.empty
      )

      val pouch: Pouch = Pouch.initialPouch()
      val (centralBoard, _, _) =
        CentralBoard.empty.fill(pouch = pouch, deck = animalCards)

      val model: GameModel = GameModel(
        players = List(player1, player2),
        true
      )

      val stage: JFXApp3.PrimaryStage = new JFXApp3.PrimaryStage:
        title = "ScalHarmonies"
      val controller: GameController = GameController(model, stage)

      val root: GameView = GameView(controller)

      root.updateState(model)

      scene = new Scene(root)
