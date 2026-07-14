package it.unibo.view.infopanel

import it.unibo.controller.GameController
import it.unibo.model.{GameModel, Player}
import it.unibo.model.card.{AnimalCard, CellRequirement, Habitat}
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.token.{TerrainToken, TokenValidator}
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.StackPane

object InfoPanelViewDemo extends JFXApp3:

  override def start(): Unit =

    val simpleHabitat = Habitat(
      List(
        CellRequirement(Coordinate(0, 0), TerrainToken.Mountain, 1)
      )
    )
    val testDeck = List(
      AnimalCard("Scoiattolo", simpleHabitat, List(1, 2, 3)),
      AnimalCard("Volpe", simpleHabitat, List(2, 4)),
      AnimalCard("Coniglio", simpleHabitat, List(1)),
      AnimalCard("Orso", simpleHabitat, List(3, 6, 9)),
      AnimalCard("Lupo", simpleHabitat, List(2, 5))
    )

    val players = List(
      Player(1, "Player1", PersonalBoard(SideA)),
      Player(2, "Player2", PersonalBoard(SideA))
    )

    var panel: InfoPanelView = null

    val controller = GameController(
      GameModel(players, deck = testDeck),
      (newModel, msg) =>
        if panel != null then
          panel match
            case p if msg.startsWith("HEADER:") =>
              p.addTurnHeader(msg.stripPrefix("HEADER:"))
            case p =>
              p.addEntry(newModel.currentPlayer.name, msg)
    )

    panel = InfoPanelView()
    panel.addTurnHeader("Player1")

    // Player1: prende carta animale prima dei token
    controller.onTakeAnimalCard(1)

    // Player1: prende i token
    controller.onTakeTokens(1)

    // Player1: piazza i 3 token
    controller.currentModel.tokensInHand.foreach { token =>
      val coord = TokenValidator
        .validPositions(token, controller.currentModel.currentPlayer.board)
        .head
      controller.onPlaceToken(coord)
    }

    // Player1: piazza un cubo sulla carta
    val card = controller.currentModel.currentPlayer.activeCards.head
    controller.onPlaceAnimalCube(card)

    // Player1: fine turno, prossimo turno Player2
    controller.onEndTurn()

    // Player2: prende i token
    controller.onTakeTokens(2)

    // Player2: piazza un token poi annulla
    val firstToken = controller.currentModel.tokensInHand.head
    val firstCoord = TokenValidator
      .validPositions(firstToken, controller.currentModel.currentPlayer.board)
      .head
    controller.onPlaceToken(firstCoord)
    controller.onCancelTurn()

    // Player2: riparte, riprende i token
    controller.onTakeTokens(2)

    // Player2: piazza tutti e 3 i token
    controller.currentModel.tokensInHand.foreach { token =>
      val coord = TokenValidator
        .validPositions(token, controller.currentModel.currentPlayer.board)
        .head
      controller.onPlaceToken(coord)
    }

    // Player2: fine turno, prossimo turno Player1
    controller.onEndTurn()

    stage = new JFXApp3.PrimaryStage:
      title = "Info Panel Demo"
      scene = new Scene(300, 500):
        root = new StackPane:
          children = panel.root
