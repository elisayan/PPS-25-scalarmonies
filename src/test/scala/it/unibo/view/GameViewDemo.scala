package it.unibo.view

import it.unibo.controller.GameController
import it.unibo.model.{GameModel, Player}
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.token.TerrainToken.{Building, Field, Water}
import it.unibo.view.cell.CellViewDemo.stage
import it.unibo.view.infopanel.InfoPanelView
import it.unibo.view.personalboard.PersonalBoardView
import it.unibo.view.token.TokenView
import scalafx.application.JFXApp3
import scalafx.scene.Scene

object GameViewDemo extends JFXApp3:

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title = "Harmonies Game"
      fullScreen = true
      val model: GameModel = GameModel(List())
      val controller: GameController = GameController(model, GameView(), (_, _) => ())
      val root: GameView = GameView()
      val tokens: List[TokenView] =
        List(TokenView(Field), TokenView(Building), TokenView(Water))
      val boardSample: PersonalBoard = PersonalBoard(SideA)
      val player = Player(1, "Filo", boardSample, List(), List())
      val boards: List[PersonalBoardView] =
        List(PersonalBoardView(player, controller.onPlaceToken), PersonalBoardView(player, controller.onPlaceToken))
      val infoPanel: InfoPanelView = InfoPanelView()
      infoPanel.addTurnHeader("Player 1")
      infoPanel.addEntry("Player 1", "Player 1 fakes drawing a card")
      root.updateSystemMessageBar("This is the system message bar")
      root.updatePersonalTokenSidebar(tokens)
      root.updateInfoPanelLog(infoPanel)
      root.updatePlayersBoards(boards)
      scene = new Scene(root)
