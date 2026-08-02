package it.unibo.view.playerarea

import it.unibo.model.Player
import it.unibo.model.card.{AnimalCard, CellRequirement, Habitat}
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.token.TerrainToken
import it.unibo.view.card.AnimalCardView
import it.unibo.view.personalboard.PersonalBoardView
import scalafx.application.JFXApp3
import scalafx.scene.Scene

object PlayerAreaViewDemo extends JFXApp3:

  override def start(): Unit =
    val board = PersonalBoard(SideA)

    val player = Player(
      id = 1,
      name = "Player 1",
      board = board,
      activeCards = List(),
      completedCards = List()
    )

    val boardView = PersonalBoardView(
      player,
      coordinate => println(s"Clicked: $coordinate"),
      highlightedCells = List()
    )

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

    val animalCards = List(
      AnimalCard(
        "Orso",
        forestHabitat,
        List(4, 7, 12, 16)
      ),
      AnimalCard(
        "Lontra",
        waterHabitat,
        List(3, 6, 10, 15)
      ),
      AnimalCard(
        "Volpe",
        forestHabitat,
        List(5, 8, 12, 18)
      ),
      AnimalCard(
        "Gufo",
        waterHabitat,
        List(4, 9, 14, 20)
      ),
      AnimalCard(
        "Cervo",
        forestHabitat,
        List(3, 7, 11, 16)
      )
    )

    val availableCards = animalCards
      .take(2)
      .map(card => AnimalCardView(card, cardWidth = 90, cardHeight = 140))

    val completedCards = animalCards
      .drop(1)
      .map(card => AnimalCardView(card, cardWidth = 55, cardHeight = 85))

    val playerArea = PlayerAreaView(
      boardView,
      availableCards,
      completedCards,
      "Player 1",
      maxCardSlots = 4
    )

    stage = new JFXApp3.PrimaryStage:
      title = "Player Area Demo"
      scene = new Scene(playerArea)
      sizeToScene()
