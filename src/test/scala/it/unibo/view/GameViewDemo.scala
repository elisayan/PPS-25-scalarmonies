package it.unibo.view

import it.unibo.controller.GameController
import it.unibo.model.GameModel
import it.unibo.model.card.{AnimalCard, CellRequirement, Habitat}
import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken
import it.unibo.model.token.TerrainToken.{Building, Field, Water}
import it.unibo.view.card.AnimalCardView
import it.unibo.view.centralboard.CentralBoardView
import it.unibo.view.infopanel.InfoPanelView
import it.unibo.view.personalboard.PersonalBoardView
import it.unibo.view.playerarea.PlayerAreaView
import it.unibo.view.token.TokenView
import scalafx.application.JFXApp3
import scalafx.scene.{Node, Scene}

object GameViewDemo extends JFXApp3:

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title = "Harmonies Game"
      fullScreen = true

      val model: GameModel = GameModel(List())
      val controller: GameController = GameController(model, (_, _) => ())
      val root: GameView = GameView(controller)

      val tokens: List[TokenView] =
        List(TokenView(Field), TokenView(Building), TokenView(Water))

      // info panel
      val infoPanel: InfoPanelView = InfoPanelView()
      infoPanel.addTurnHeader("Player 1")
      infoPanel.addEntry("Player 1", "Player 1 fakes drawing a card")

      // cards habitat
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

      // central board
      val pouch: Pouch = Pouch.initialPouch()
      val (centralBoard, _, _) =
        CentralBoard.empty.fill(
          pouch = pouch,
          deck = animalCards
        )

      val centralBoardView =
        CentralBoardView(
          board = centralBoard,
          onCardClicked = controller.onTakeAnimalCard,
          onTokenClicked = controller.onTakeTokens
        )

      // player 1
      val board1 = PersonalBoard(SideA)
      val boardView1 = PersonalBoardView(board1, controller)
      val availableCards1: List[Node] =
        animalCards.take(2).map(c => AnimalCardView(c, 90, 140))
      val completedCards1: List[Node] =
        animalCards.drop(2).map(c => AnimalCardView(c, 55, 85))
      val playerArea1 =
        PlayerAreaView(boardView1, availableCards1, completedCards1, "Player 1")

      // player 2
      val board2 = PersonalBoard(SideA)
      val boardView2 = PersonalBoardView(board2, controller)
      val availableCards2: List[Node] =
        animalCards.take(1).map(c => AnimalCardView(c, 90, 140))
      val completedCards2: List[Nothing] = List.empty
      val playerArea2 =
        PlayerAreaView(boardView2, availableCards2, completedCards2, "Player 2")

      root.updateSystemMessageBar("Turno di: Player 1")
      root.updatePersonalTokenSidebar(tokens)
      root.updateInfoPanelLog(infoPanel)
      root.updateCentralBoard(centralBoardView)
      root.updatePlayerAreas(List(playerArea1, playerArea2))

      scene = new Scene(root)
