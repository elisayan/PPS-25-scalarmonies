package it.unibo.model

import it.unibo.model.personalBoard.PersonalBoard
import it.unibo.model.personalBoard.PersonalBoard.BoardSide.SideA
import it.unibo.model.token.TokenValidator
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameModelTest extends AnyFlatSpec with Matchers:

  private val players = List(
    Player(1, PersonalBoard(SideA)),
    Player(2, PersonalBoard(SideA))
  )

  "GameModel" should "start with Player 1 as current player" in {
    val model = GameModel(players)
    model.currentPlayer.id shouldBe 1
  }

  it should "start in WaitingForObligatoryAction state" in {
    val model = GameModel(players)
    model.turnState shouldBe TurnState.WaitingForObligatoryAction
  }

  it should "not be game over at the start" in {
    val model = GameModel(players)
    model.isGameOver shouldBe false
  }

  // takeTokens
  it should "move to ObligatoryActionDone after taking tokens" in {
    val model = GameModel(players)
    val updated = model.takeTokens(1)
    updated.turnState shouldBe TurnState.ObligatoryActionDone
  }

  it should "have 3 tokens in hand after taking tokens" in {
    val model = GameModel(players)
    val updated = model.takeTokens(1)
    updated.tokensInHand should have size 3
  }

  it should "reject takeTokens if not in WaitingForObligatoryAction state" in {
    val model = GameModel(players)
    val updated = model.takeTokens(1)
    assertThrows[IllegalStateException] {
      updated.takeTokens(1)
    }
  }

  it should "place a token on the board and remove it from hand" in {
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val firstToken = afterTake.tokensInHand.head
    val validCoord = TokenValidator.validPositions(firstToken, afterTake.currentPlayer.board).head
    val afterPlace = afterTake.placeToken(validCoord)
    afterPlace.tokensInHand should have size 2
  }

  it should "move to TurnComplete after placing all 3 tokens" in {
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val finalModel = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      val coord = TokenValidator.validPositions(token, m.currentPlayer.board).head
      m.placeToken(coord)
    }
    finalModel.turnState shouldBe TurnState.TurnComplete
  }
