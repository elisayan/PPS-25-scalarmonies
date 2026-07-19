package it.unibo.view.card

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.{StackPane, HBox}
import scalafx.scene.paint.Color
import scalafx.geometry.{Insets, Pos}

import it.unibo.model.card.{AnimalCard, Habitat, CellRequirement}
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken

object AnimalCardViewDemo extends JFXApp3:

  override def start(): Unit =
    val mockHabitat = Habitat(List(
      CellRequirement(Coordinate(0, 0), TerrainToken.Field, 1),
      CellRequirement(Coordinate(0, -2), TerrainToken.Forest, 2),
      CellRequirement(Coordinate(2, 1), TerrainToken.Water, 1)
    ))
    val emptyCard = AnimalCard(
      name = "Orso Bruno",
      habitat = mockHabitat,
      points = List(4, 7, 12),
      imageId = "orso.png"
    )
    val cardWithOneCube = emptyCard.placeCube.getOrElse(emptyCard)
    val emptyCardNode = AnimalCardView(emptyCard)
    val updatedCardNode = AnimalCardView(cardWithOneCube)
    stage = new JFXApp3.PrimaryStage:
      title = "Test Visivo: Animal Card"
      width = 450
      height = 400
      scene = new Scene:
        fill = Color.rgb(240, 240, 240)
        root = new HBox:
          alignment = Pos.Center
          spacing = 20
          padding = Insets(20)
          children = Seq(emptyCardNode, updatedCardNode)