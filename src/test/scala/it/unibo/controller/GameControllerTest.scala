package it.unibo.controller

import it.unibo.model.{GameModel, Player, TurnState}
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.token.TokenValidator
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalafx.application.{JFXApp3, Platform}

class GameControllerTest extends AnyFlatSpec with Matchers:

  Platform.startup(() => ())

  private val players = List(
    Player(1, "Player1", PersonalBoard(SideA)),
    Player(2, "Player2", PersonalBoard(SideA))
  )

  private val stage: JFXApp3.PrimaryStage = new JFXApp3.PrimaryStage

  private def freshController(): GameController =
    GameController(
      GameModel(players),
      stage
    )

  "GameController" should "update model after taking tokens" in:
    val controller = freshController()
    controller.onTakeTokens(1)
    controller.currentModel.turnState shouldBe TurnState.ActionDone

  it should "place token through controller" in:
    val controller = freshController()
    controller.onTakeTokens(1)
    val token = controller.currentModel.tokensInHand.head
    controller.onSelectToken(token)
    val coord =
      TokenValidator
        .validPositions(
          token,
          controller.currentModel.currentPlayer.board
        )
        .head
    controller.onPlaceToken(coord)
    controller.currentModel.tokensInHand should have size 2

  it should "end turn through controller and move to next player" in:
    val controller = freshController()
    controller.onTakeTokens(1)
    controller.currentModel.tokensInHand.foreach { token =>
      controller.onSelectToken(token)
      val coord =
        TokenValidator
          .validPositions(
            token,
            controller.currentModel.currentPlayer.board
          )
          .head
      controller.onPlaceToken(coord)
    }
    controller.onEndTurn()
    controller.currentModel.currentPlayer.id shouldBe 2
    controller.currentModel.turnState shouldBe TurnState.WaitingForAction

  it should "not crash if an illegal action is attempted" in:
    val controller = freshController()
    controller.onTakeTokens(1)
    noException should be thrownBy {
      controller.onTakeTokens(1)
    }

  it should "cancel turn and restore state" in:
    val controller = freshController()
    controller.onTakeTokens(1)
    controller.onCancelTurn()
    controller.currentModel.turnState shouldBe TurnState.WaitingForAction
    controller.currentModel.tokensInHand shouldBe empty
