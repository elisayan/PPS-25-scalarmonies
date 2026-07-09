package it.unibo.model

import it.unibo.model.card.{AnimalCard, CellRequirement, Habitat}
import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}
import it.unibo.model.personalBoard.PersonalBoard.BoardSide.SideA
import it.unibo.model.token.TerrainToken
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
    val validCoord = TokenValidator
      .validPositions(firstToken, afterTake.currentPlayer.board)
      .head
    val afterPlace = afterTake.placeToken(validCoord)
    afterPlace.tokensInHand should have size 2
  }

  it should "move to TurnComplete after placing all 3 tokens" in {
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val finalModel = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      val coord =
        TokenValidator.validPositions(token, m.currentPlayer.board).head
      m.placeToken(coord)
    }
    finalModel.turnState shouldBe TurnState.TurnComplete
  }

  // endTurn
  it should "pass to the next player after endTurn" in {
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      val coord =
        TokenValidator.validPositions(token, m.currentPlayer.board).head
      m.placeToken(coord)
    }
    val afterEnd = afterPlace.endTurn()
    afterEnd.currentPlayer.id shouldBe 2
  }

  it should "wrap back to Player 1 after last player ends turn" in {
    val model = GameModel(players)

    def playTurn(m: GameModel, slot: Int): GameModel =
      val afterTake = m.takeTokens(slot)
      val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) {
        (m2, token) =>
          val coord =
            TokenValidator.validPositions(token, m2.currentPlayer.board).head
          m2.placeToken(coord)
      }
      afterPlace.endTurn()

    val afterPlayer1 = playTurn(model, 1)
    val afterPlayer2 = playTurn(afterPlayer1, 2)
    afterPlayer2.currentPlayer.id shouldBe 1
  }

  it should "refill the central board after endTurn" in {
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      val coord =
        TokenValidator.validPositions(token, m.currentPlayer.board).head
      m.placeToken(coord)
    }
    val afterEnd = afterPlace.endTurn()
    afterEnd.tokensInHand shouldBe empty
    afterEnd.currentPlayer.id shouldBe 2
  }

  // isGameOver
  it should "not be game over when pouch still has tokens" in {
    val model = GameModel(players)
    model.isGameOver shouldBe false
  }

  it should "be game over when pouch is empty" in {
    val emptyPouchPlayers = List(
      Player(1, PersonalBoard(SideA)),
      Player(2, PersonalBoard(SideA))
    )
    val model = GameModel(emptyPouchPlayers, forceEmptyPouch = true)
    model.isGameOver shouldBe true
  }

  it should "be game over when a player has 2 or fewer empty cells" in {
    val model = GameModel(players)
    model.isGameOver shouldBe false
    // SideA ha 23 celle, lasciamo 2 vuote
    val nearlyFullBoard = PersonalBoard(SideA).cells.keys.toList
      .take(21) // occupa 21 celle su 23
      .foldLeft(PersonalBoard(SideA)) { (b, coord) =>
        b.placeToken(TerrainToken.Water, coord)
      }
    val modelWithFullBoard = GameModel(
      List(
        Player(1, nearlyFullBoard),
        Player(2, PersonalBoard(SideA))
      )
    )
    modelWithFullBoard.isGameOver shouldBe true
  }

  // highlightedCells
  "GameModel" should "return valid positions when no habitats are completed" in {
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val token = afterTake.tokensInHand.head
    val highlighted = afterTake.highlightedCells(token)
    highlighted should not be empty
  }

  it should "exclude cells of completed habitats from highlighted cells" in {
    // habitat minimo: richiede una cella a offset (0,0) con Mountain altezza 1
    val simpleHabitat = Habitat(List(CellRequirement(Coordinate(0, 0), TerrainToken.Mountain, 1)))
    val card = AnimalCard("Test", simpleHabitat, List(1, 2, 3))
    // piazziamo un Mountain su (0,0) e simuliamo cubo già piazzato
    val boardWithMountain = PersonalBoard(SideA).placeToken(TerrainToken.Mountain, Coordinate(0, 0))
    val playerWithCard = Player(1, boardWithMountain, activeCards = List(card.placeCube.get)) // cubo già piazzato
    val model = GameModel(List(playerWithCard, Player(2, PersonalBoard(SideA))))
    val afterTake = model.takeTokens(1)
    val highlighted = afterTake.highlightedCells(TerrainToken.Mountain)
    highlighted should not contain Coordinate(0, 0)
  }

