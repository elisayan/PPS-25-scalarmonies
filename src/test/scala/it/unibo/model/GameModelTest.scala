package it.unibo.model

import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.card.{AnimalCard, CellRequirement, Habitat}
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.token.TerrainToken
import it.unibo.model.token.TokenValidator
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameModelTest extends AnyFlatSpec with Matchers:

  private val players = List(
    Player(1, "Player1", PersonalBoard(SideA)),
    Player(2, "Player2", PersonalBoard(SideA))
  )

  private val simpleHabitat = Habitat(
    List(CellRequirement(Coordinate(0, 0), TerrainToken.Mountain, 1))
  )

  private val testCard = AnimalCard("Squirrel", simpleHabitat, List(1, 2, 3))

  private def modelAfterTake(model: GameModel): GameModel = model.takeTokens(1)

  private def placeSelectedToken(
                                  model: GameModel,
                                  token: TerrainToken
                                ): GameModel =
    val positions =
      TokenValidator.validPositions(token, model.currentPlayer.board).filterNot(_ == Coordinate(0, 0))
    if positions.isEmpty then model
    else
      val selected = model.selectToken(token)
      selected.placeToken(positions.head)

  private val CardSlot = 1

  private def playerWithMountainAt00(id: Int, name: String): Player =
    Player(id, name, PersonalBoard(SideA).placeToken(TerrainToken.Mountain, Coordinate(0, 0)).get)

  private def modelWithCards(cards: List[AnimalCard]): GameModel =
    GameModel(
      List(playerWithMountainAt00(1, "Player1"), Player(2, "Player2", PersonalBoard(SideA))),
      deck = cards
    )

  "GameModel" should "start with Player 1 as current player" in:
    val model = GameModel(players)
    model.currentPlayer.id shouldBe 1

  it should "start in WaitingForObligatoryAction state" in:
    val model = GameModel(players)
    model.turnState shouldBe TurnState.WaitingForAction

  it should "not be game over at the start" in:
    val model = GameModel(players)
    model.isGameOver shouldBe false

  // takeTokens
  it should "move to ObligatoryActionDone after taking tokens" in:
    val model = GameModel(players)
    val updated = model.takeTokens(1)
    updated.turnState shouldBe TurnState.ActionDone

  it should "have 3 tokens in hand after taking tokens" in:
    val model = GameModel(players)
    val updated = model.takeTokens(1)
    updated.tokensInHand should have size 3

  it should "reject takeTokens if not in WaitingForObligatoryAction state" in:
    val model = GameModel(players)
    val updated = model.takeTokens(1)
    assertThrows[IllegalStateException] { updated.takeTokens(1) }

  it should "place a token on the board and remove it from hand" in:
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val firstToken = afterTake.tokensInHand.head
    val validCoord = TokenValidator
      .validPositions(firstToken, afterTake.currentPlayer.board)
      .head
    val afterSelect = afterTake.selectToken(firstToken)
    val afterPlace = afterSelect.placeToken(validCoord)
    afterPlace.tokensInHand should have size 2

  it should "move to TurnComplete after placing all 3 tokens" in:
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val finalModel = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      placeSelectedToken(m, token)
    }
    finalModel.turnState shouldBe TurnState.TurnComplete

  // endTurn
  it should "pass to the next player after endTurn" in:
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      placeSelectedToken(m, token)
    }
    val afterEnd = afterPlace.endTurn()
    afterEnd.currentPlayer.id shouldBe 2

  it should "wrap back to Player 1 after last player ends turn" in:
    val model = GameModel(players)

    def playTurn(m: GameModel, slot: Int): GameModel =
      val afterTake = m.takeTokens(slot)
      val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) {
        (m2, token) => placeSelectedToken(m2, token)
      }
      afterPlace.endTurn()

    val afterPlayer1 = playTurn(model, 1)
    val afterPlayer2 = playTurn(afterPlayer1, 2)
    afterPlayer2.currentPlayer.id shouldBe 1

  it should "refill the central board after endTurn" in:
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      placeSelectedToken(m, token)
    }
    val afterEnd = afterPlace.endTurn()
    afterEnd.tokensInHand shouldBe empty
    afterEnd.currentPlayer.id shouldBe 2

  // isGameOver
  it should "not be game over when pouch still has tokens" in:
    val model = GameModel(players)
    model.isGameOver shouldBe false

  it should "activate last round when pouch is empty" in:
    val model = GameModel(
      List(
        Player(1, "Player1", PersonalBoard(SideA)),
        Player(2, "Player2", PersonalBoard(SideA))
      ),
      forceEmptyPouch = true
    )

    model.isGameOver shouldBe false

  it should "trigger last round when a player has almost full board" in:
    val almostFullBoard =
      PersonalBoard(SideA).cells.keys.toList
        .take(PersonalBoard(SideA).cells.size - 2)
        .foldLeft(PersonalBoard(SideA)) { (b, coord) =>
          b.placeToken(TerrainToken.Water, coord).get
        }

    val model = GameModel(
      List(
        Player(1, "Player1", almostFullBoard),
        Player(2, "Player2", PersonalBoard(SideA))
      ),
      forceEmptyPouch = true
    )
    model.isGameOver shouldBe false

  it should "be game over after last round is completed" in:
    val model = GameModel(
      List(
        Player(1, "Player1", PersonalBoard(SideA)),
        Player(2, "Player2", PersonalBoard(SideA))
      )
    )
    def completeTurn(m: GameModel): GameModel =
      val afterTake = m.takeTokens(1)

      afterTake.tokensInHand.foldLeft(afterTake) { (current, token) =>
        val selected = current.selectToken(token)
        val coord =
          TokenValidator
            .validPositions(
              token,
              selected.currentPlayer.board
            )
            .head

        selected.placeToken(coord)
      }

    var current = model
    while (!current.isGameOver) {
      current = completeTurn(current).endTurn()
    }
    current.isGameOver shouldBe true

  // highlightedCells
  "GameModel" should "return valid positions when no habitats are completed" in:
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val token = afterTake.tokensInHand.head
    val highlighted = afterTake.highlightedCells(token)
    highlighted should not be empty

  it should "exclude cells of completed habitats from highlighted cells" in:
    val simpleHabitat =
      Habitat(List(CellRequirement(Coordinate(0, 0), TerrainToken.Mountain, 1)))
    val card = AnimalCard("Test", simpleHabitat, List(1, 2, 3))
    // piazziamo un Mountain su (0,0)
    val boardWithMountain =
      PersonalBoard(SideA).placeToken(TerrainToken.Mountain, Coordinate(0, 0))
    val playerWithCard = Player(
      1,
      "Player1",
      boardWithMountain.get,
      activeCards = List(card.placeCube.get) // cubo simulato come piazzato
    )
    val model = GameModel(
      List(playerWithCard, Player(2, "Player2", PersonalBoard(SideA)))
    )
    val afterTake = model.takeTokens(1)
    val highlighted = afterTake.highlightedCells(TerrainToken.Mountain)
    highlighted should not contain Coordinate(0, 0)

  // take and place animalCard
  it should "allow taking animal card before taking tokens" in:
    val model = modelWithCards(List(testCard))

    noException should be thrownBy model.takeAnimalCard(CardSlot)

  it should "allow taking animal card in ActionDone state" in:
    val model = modelWithCards(List(testCard))
    val afterTake = modelAfterTake(model)

    noException should be thrownBy afterTake.takeAnimalCard(CardSlot)

  it should "allow taking animal card after placing all tokens" in:
    val model = modelWithCards(List(testCard))
    val afterTake = modelAfterTake(model)

    val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      placeSelectedToken(m, token)
    }

    noException should be thrownBy {
      afterPlace.takeAnimalCard(CardSlot)
    }

  it should "reject selecting an animal card if it has no completed habitats" in:
    val model = GameModel(players) // Plancia vuota (senza montagna in (0,0))
    val modelWithDeck = GameModel(players, deck = List(testCard))
    val afterCard = modelWithDeck.takeAnimalCard(CardSlot)
    val cardTaken = afterCard.currentPlayer.activeCards.head

    assertThrows[IllegalStateException] {
      afterCard.selectAnimalCard(cardTaken)
    }

  it should "allow placing animal cube before taking tokens" in:
    val model = modelWithCards(List(testCard))
    val afterCard = model.takeAnimalCard(CardSlot)
    val cardTaken = afterCard.currentPlayer.activeCards.head

    val afterSelect = afterCard.selectAnimalCard(cardTaken)
    noException should be thrownBy afterSelect.placeAnimalCube(Coordinate(0, 0))

  it should "allow placing animal cube when turn is complete" in:
    val model = modelWithCards(List(testCard))
    val afterCard = model.takeAnimalCard(CardSlot)
    val cardTaken = afterCard.currentPlayer.activeCards.head

    val afterTake = afterCard.takeTokens(1)

    val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      placeSelectedToken(m, token)
    } // TurnComplete

    val afterSelect = afterPlace.selectAnimalCard(cardTaken)
    noException should be thrownBy {
      afterSelect.placeAnimalCube(Coordinate(0, 0))
    }

  it should "clear selectedAnimalCard when selectToken is called" in:
    val model = modelWithCards(List(testCard))
    val afterTake = model.takeTokens(1)
    val afterCard = afterTake.takeAnimalCard(CardSlot)
    val cardTaken = afterCard.currentPlayer.activeCards.head

    val afterSelectCard = afterCard.selectAnimalCard(cardTaken)
    afterSelectCard.selectedAnimalCard shouldBe defined

    val afterSelectToken = afterSelectCard.selectToken(afterSelectCard.tokensInHand.head)
    afterSelectToken.selectedAnimalCard shouldBe empty
    afterSelectToken.selectedToken shouldBe defined

  // complete animal cards
  it should "move a completed card to completedCards when all cubes are placed" in:
    val oneUseCard = AnimalCard("Rabbit", simpleHabitat, List(1))
    val model = modelWithCards(List(oneUseCard))

    val afterCard = model.takeAnimalCard(CardSlot)
    val cardTaken = afterCard.currentPlayer.activeCards.head

    val afterSelect = afterCard.selectAnimalCard(cardTaken)
    val afterCube = afterSelect.placeAnimalCube(Coordinate(0, 0))

    afterCube.currentPlayer.activeCards should not contain cardTaken
    afterCube.currentPlayer.completedCards should have size 1

  it should "free a slot in activeCards after card is completed" in:
    val card1 = AnimalCard("A", simpleHabitat, List(1))
    val card2 = AnimalCard("B", simpleHabitat, List(1))
    val card3 = AnimalCard("C", simpleHabitat, List(1))
    val card4 = AnimalCard("D", simpleHabitat, List(1))
    val card5 = AnimalCard("E", simpleHabitat, List(1))

    val playerWithFullCards = playerWithMountainAt00(1, "Player1").copy(
      activeCards = List(card1, card2, card3, card4)
    )

    val model = GameModel(
      List(
        playerWithFullCards,
        Player(2, "Player2", PersonalBoard(SideA))
      ),
      deck = List(card5)
    )

    val afterSelect = model.selectAnimalCard(card1)
    val afterCube = afterSelect.placeAnimalCube(Coordinate(0, 0))

    afterCube.currentPlayer.activeCards should have size 3
    afterCube.currentPlayer.completedCards should have size 1

    noException should be thrownBy afterCube.takeAnimalCard(CardSlot)

  it should "keep completed card points separate from active cards" in:
    val oneUseCard = AnimalCard("Rabbit", simpleHabitat, List(3))
    val model = modelWithCards(List(oneUseCard))

    val afterCard = model.takeAnimalCard(CardSlot)
    val cardTaken = afterCard.currentPlayer.activeCards.head

    val afterSelect = afterCard.selectAnimalCard(cardTaken)
    val afterCube = afterSelect.placeAnimalCube(Coordinate(0, 0))

    afterCube.currentPlayer.completedCards.head.currentPoints shouldBe 3
    afterCube.currentPlayer.activeCards shouldBe empty

  // cancelTurn
  it should "restore state to beginning of turn after cancelTurn" in:
    val model = GameModel(players)
    val afterTake = model.takeTokens(1)
    val afterCancel = afterTake.cancelTurn()
    afterCancel.turnState shouldBe TurnState.WaitingForAction
    afterCancel.tokensInHand shouldBe empty

  it should "restore player board after cancelTurn" in:
    val model = GameModel(players)
    val boardBefore = model.currentPlayer.board
    val afterTake = model.takeTokens(1)
    val afterPlace = afterTake.tokensInHand.foldLeft(afterTake) { (m, token) =>
      placeSelectedToken(m, token)
    }
    val afterCancel = afterPlace.cancelTurn()
    afterCancel.currentPlayer.board.cells shouldBe boardBefore.cells

  it should "restore central board after cancelTurn" in:
    val model = GameModel(players)
    val boardBefore = model.currentPlayer.board
    val afterTake = model.takeTokens(1)
    val afterCancel = afterTake.cancelTurn()
    afterCancel.currentPlayer.board.cells shouldBe boardBefore.cells

  it should "do nothing if cancelTurn is called without any action" in:
    val model = GameModel(players)
    val afterCancel = model.cancelTurn()
    afterCancel.turnState shouldBe TurnState.WaitingForAction
    afterCancel.currentPlayer.id shouldBe 1