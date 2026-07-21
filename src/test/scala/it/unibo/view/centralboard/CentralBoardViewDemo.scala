package it.unibo.view.centralboard

import it.unibo.controller.GameController
import it.unibo.model.GameModel
import it.unibo.model.card.AnimalCard
import it.unibo.model.token.TerrainToken
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

  object CentralBoardViewDemo extends JFXApp3:

    override def start(): Unit =
      val model: GameModel = GameModel(List())

      val controller: GameController =
        GameController(
          model,
          (_, message) => println(message)
        )

      val centralBoardView = new CentralBoardView(controller)

      val tokenSlots: Map[Int, List[TerrainToken]] = Map(
        1 -> List(
          TerrainToken.Mountain,
          TerrainToken.Forest,
          TerrainToken.Water
        ),
        2 -> List(
          TerrainToken.Field,
          TerrainToken.Ground,
          TerrainToken.Building
        )
      )

      val cardSlots: Map[Int, AnimalCard] = Map.empty

      centralBoardView.update(
        tokenSlots = tokenSlots,
        cardSlots = cardSlots
      )

      val root = new Pane:
        children = List(centralBoardView)

      stage = new JFXApp3.PrimaryStage:
        title = "Central Board Demo"
        scene = new Scene(root, 1200, 400)
