package it.unibo.controller

import it.unibo.model.{GameModel, Player, TurnState}
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.token.TokenValidator
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameControllerTest extends AnyFlatSpec with Matchers:

  private val players =
    List(Player(1, PersonalBoard(SideA)), Player(2, PersonalBoard(SideA)))

  "GameController" should "update model after taking tokens" in:
    val model = GameModel(players)
    val controller = GameController(model, () => ())
    controller.onTakeTokens(1)
    controller.currentModel.turnState shouldBe TurnState.ActionDone

  it should "refresh view after taking tokens" in:
    var refreshed = false
    val controller = GameController(GameModel(players), () => refreshed = true)
    controller.onTakeTokens(1)
    refreshed shouldBe true

  it should "place token through controller" in:
    val controller = GameController(GameModel(players), () => ())
    controller.onTakeTokens(1)
    val token = controller.currentModel.tokensInHand.head
    val coord = TokenValidator
      .validPositions(token, controller.currentModel.currentPlayer.board)
      .head
    controller.onPlaceToken(coord)
    controller.currentModel.tokensInHand should have size 2

  it should "end turn through controller and move to next player" in:
    val model = GameModel(players)
    val controller = GameController(model, () => ())
    controller.onTakeTokens(1)

    val currentModel = controller.currentModel
    currentModel.tokensInHand.foreach { token =>
      val coord = TokenValidator
        .validPositions(token, controller.currentModel.currentPlayer.board)
        .head
      controller.onPlaceToken(coord)
    }
    controller.onEndTurn()

    controller.currentModel.currentPlayer.id shouldBe 2
    controller.currentModel.turnState shouldBe TurnState.WaitingForAction
