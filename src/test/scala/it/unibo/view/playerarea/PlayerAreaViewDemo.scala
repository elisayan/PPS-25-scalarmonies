package it.unibo.view.playerarea

import it.unibo.controller.GameController
import it.unibo.model.GameModel
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

    val model =
      GameModel(List())

    val controller =
      GameController(
        model,
        (_, message) => println(message)
      )


    // -----------------------------
    // Personal board finta
    // -----------------------------

    val board =
      PersonalBoard(SideA)

    val boardView =
      PersonalBoardView(
        board,
        controller
      )


    // -----------------------------
    // Habitat per le carte
    // -----------------------------

    val habitatForest =
      Habitat(
        List(
          CellRequirement(
            Coordinate(0,0),
            TerrainToken.Forest,
            1
          ),
          CellRequirement(
            Coordinate(1,1),
            TerrainToken.Field,
            2
          )
        )
      )


    val habitatWater =
      Habitat(
        List(
          CellRequirement(
            Coordinate(0,0),
            TerrainToken.Water,
            1
          ),
          CellRequirement(
            Coordinate(-1,1),
            TerrainToken.Mountain,
            2
          )
        )
      )


    // -----------------------------
    // Animal cards
    // -----------------------------

    val animalCards =
      List(
        AnimalCard(
          name = "Orso",
          habitat = habitatForest,
          points = List(4,7,12,16),
          imageId = "default.png"
        ),

        AnimalCard(
          name = "Lontra",
          habitat = habitatWater,
          points = List(3,6,10,15),
          imageId = "default.png"
        ),

        AnimalCard(
          name = "Volpe",
          habitat = habitatForest,
          points = List(5,8,12,18),
          imageId = "default.png"
        )
      )


    // trasformazione Model -> View

    val cardViews =
      animalCards.map(card =>
        AnimalCardView(card)
      )


    // -----------------------------
    // Player Area
    // -----------------------------

    val playerArea =
      PlayerAreaView(
        boardView,
        cardViews,
        "Player 1"
      )


    stage =
      new JFXApp3.PrimaryStage:

        title = "Player Area Demo"

        scene =
          new Scene(
            playerArea,
            1200,
            800
          )