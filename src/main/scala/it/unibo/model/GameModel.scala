package it.unibo.model

import it.unibo.model.TurnState.ActionDone
import it.unibo.model.card.AnimalCard
import it.unibo.model.card.AnimalDeckFactory
import it.unibo.model.card.HabitatMatcher
import it.unibo.model.card.HabitatMatcher.findMatches
import it.unibo.model.centralboard.CentralBoards.CentralBoard
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.personalboard.PersonalBoard
import it.unibo.model.pouch.Pouches.Pouch
import it.unibo.model.token.TerrainToken
import it.unibo.model.token.TokenValidator

/** Defines the game model and its public operations. */
trait GameModel:

  /** @return the player who starts the game. */
  def startingPlayer: Player

  /** @return the player whose turn is currently active. */
  def currentPlayer: Player

  /** @return the current turn state. */
  def turnState: TurnState

  /** @return the terrain tokens currently in the player's hand. */
  def tokensInHand: List[TerrainToken]

  /** @return the number of tokens remaining in the pouch. */
  def pouchSize: Int

  /** @return true if the game has ended. */
  def isGameOver: Boolean

  /** Draws a set of terrain tokens from the selected slot.
    * @param slot
    *   the selected slot.
    * @return
    *   the updated game model.
    */
  def takeTokens(slot: Int): GameModel

  /** @return the currently selected terrain token, if any. */
  def selectedToken: Option[TerrainToken]

  /** Selects a terrain token.
    * @param token
    *   the token to select.
    * @return
    *   the updated game model.
    */
  def selectToken(token: TerrainToken): GameModel

  /** Takes an animal card from the selected slot.
    * @param slot
    *   the selected slot.
    * @return
    *   the updated game model.
    */
  def takeAnimalCard(slot: Int): GameModel

  /** Places the selected terrain token.
    * @param coordinate
    *   the target coordinate.
    * @return
    *   the updated game model.
    */
  def placeToken(coordinate: Coordinate): GameModel

  /** Ends the current player's turn.
    * @return
    *   the updated game model.
    */
  def endTurn(): GameModel

  /** Returns the valid positions for placing a terrain token.
    * @param token
    *   the selected terrain token.
    * @return
    *   the list of valid coordinates.
    */
  def highlightedCells(token: TerrainToken): List[Coordinate]

  /** @return the currently selected animal card, if any. */
  def selectedAnimalCard: Option[AnimalCard]

  /** Selects an animal card.
    * @param card
    *   the card to select.
    * @return
    *   the updated game model.
    */
  def selectAnimalCard(card: AnimalCard): GameModel

  /** Returns the valid positions for placing an animal cube.
    * @param card
    *   the selected animal card.
    * @return
    *   the list of valid coordinates.
    */
  def highlightedAnimalCells(card: AnimalCard): List[Coordinate]

  /** Places an animal cube.
    * @param coordinate
    *   the target coordinate.
    * @return
    *   the updated game model.
    */
  def placeAnimalCube(coordinate: Coordinate): GameModel

  /** Cancels the current turn.
    * @return
    *   the restored game model.
    */
  def cancelTurn(): GameModel

  /** @return all players in the game. */
  def allPlayers: List[Player]

  /** @return the central board. */
  def centralBoard: CentralBoard

  /** @return a message describing the available actions. */
  def availableActionsMessage: String

object GameModel:

  /** Creates a game model with the given players.
    * @param players
    *   the players participating in the game.
    * @param deck
    *   the initial animal deck.
    * @return
    *   a new game model.
    */
  def apply(
      players: List[Player],
      deck: List[AnimalCard] = AnimalDeckFactory.createShuffledDeck()
  ): GameModel =
    val pouch = Pouch.initialPouch()
    val (board, updatedPouch, updatedDeck) =
      CentralBoard.empty.fill(pouch, deck)
    GameModelImpl(
      players = players,
      currentPlayerIndex = 0,
      centralBoard = board,
      pouch = updatedPouch,
      deck = updatedDeck,
      turnState = TurnState.WaitingForAction
    )

  /** Creates a game model for testing purposes.
    * @param players
    *   the players participating in the game.
    * @param forceEmptyPouch
    *   whether to start with an empty pouch.
    * @return
    *   a new game model.
    */
  def apply(players: List[Player], forceEmptyPouch: Boolean): GameModel =
    val pouch =
      if forceEmptyPouch then Pouch(List.empty) else Pouch.initialPouch()
    val (board, updatedPouch, updatedDeck) =
      CentralBoard.empty.fill(pouch, List.empty)
    GameModelImpl(
      players = players,
      currentPlayerIndex = 0,
      centralBoard = board,
      pouch = updatedPouch,
      deck = updatedDeck,
      turnState = TurnState.WaitingForAction
    )

  private case class GameModelImpl(
      players: List[Player],
      currentPlayerIndex: Int,
      centralBoard: CentralBoard,
      pouch: Pouch,
      deck: List[AnimalCard],
      turnSnapshot: Option[GameModelImpl] = None,
      override val turnState: TurnState,
      override val tokensInHand: List[TerrainToken] = List.empty,
      override val selectedToken: Option[TerrainToken] = None,
      override val selectedAnimalCard: Option[AnimalCard] = None,
      hasTakenCardThisTurn: Boolean = false,
      isLastRound: Boolean = false
  ) extends GameModel:

    private val MaxAnimalCards = 4

    private given currentBoard: PersonalBoard = currentPlayer.board

    override def startingPlayer: Player = players.head

    override def currentPlayer: Player = players(currentPlayerIndex)

    override def pouchSize: Int = pouch.size

    override def isGameOver: Boolean =
      isLastRound && currentPlayerIndex == 0

    override def takeTokens(slot: Int): GameModel =
      requireState(
        TurnState.WaitingForAction,
        "Non puoi scegliere i tokens nello stato corrente"
      ):
        centralBoard.takeTokens(slot) match
          case None =>
            throw IllegalStateException(s"Slot $slot è vuoto o invalido")
          case Some((tokens, updatedBoard)) =>
            this.copy(
              centralBoard = updatedBoard,
              tokensInHand = tokens,
              turnState = TurnState.ActionDone,
              turnSnapshot = turnSnapshot.orElse(Some(this))
            )

    override def selectToken(token: TerrainToken): GameModel =
      tokensInHand
        .find(_ == token)
        .map(_ =>
          this.copy(selectedToken = Some(token), selectedAnimalCard = None)
        )
        .getOrElse(throw IllegalStateException("Token non disponibile"))

    override def takeAnimalCard(slot: Int): GameModel =
      if hasTakenCardThisTurn then
        throw IllegalStateException(
          "Player ha gia scelto una carta Animale questo turno"
        )
      if currentPlayer.hasReachedAnimalCardLimit(MaxAnimalCards) then
        throw IllegalStateException(
          "Player ha già il numero massimo di carte Animale"
        )

      centralBoard.takeCard(slot) match
        case None =>
          throw IllegalStateException(
            s"Slot $slot della carta è vuoto o invalido"
          )
        case Some((card, updatedBoard)) =>
          val updatedPlayer = currentPlayer.copy(
            activeCards = currentPlayer.activeCards :+ card
          )
          this.copy(
            centralBoard = updatedBoard,
            players = players.updated(currentPlayerIndex, updatedPlayer),
            turnSnapshot = turnSnapshot.orElse(Some(this)),
            hasTakenCardThisTurn = true
          )

    override def placeToken(coordinate: Coordinate): GameModel =
      requireState(
        TurnState.ActionDone,
        "Non puoi posizionare token nello stato corrente"
      ):
        val token = selectedToken.getOrElse(
          throw IllegalStateException(
            "Prima di questa azione scegliere un token"
          )
        )

        if !highlightedCells(token).contains(coordinate) then
          throw IllegalStateException("Piazzamento token invalido")

        val updatedBoard = currentPlayer.board
          .placeToken(token, coordinate)
          .getOrElse(
            throw IllegalStateException("Piazzamento token invalido")
          )
        val updatedPlayer = currentPlayer.copy(board = updatedBoard)
        val updatedPlayers = players.updated(currentPlayerIndex, updatedPlayer)
        val index = tokensInHand.indexOf(token)

        val remainingTokens = tokensInHand.patch(index, Nil, 1)

        val newState =
          if remainingTokens.isEmpty then TurnState.TurnComplete
          else TurnState.ActionDone

        this.copy(
          players = updatedPlayers,
          tokensInHand = remainingTokens,
          selectedToken = None,
          turnState = newState
        )

    override def endTurn(): GameModel =
      requireState(
        TurnState.TurnComplete,
        "Non puoi terminare il turno prima di aver piazzato tutti i tokens"
      ):
        val nextIndex = (currentPlayerIndex + 1) % players.size
        val (refilledBoard, updatedPouch, updatedDeck) =
          centralBoard.fill(pouch, deck)

        val endConditionTriggered =
          updatedPouch.isEmpty || hasPlayerAlmostFullBoard
        val nextIsLastRound = isLastRound || endConditionTriggered

        this.copy(
          currentPlayerIndex = nextIndex,
          centralBoard = refilledBoard,
          pouch = updatedPouch,
          deck = updatedDeck,
          tokensInHand = List.empty,
          turnState = TurnState.WaitingForAction,
          turnSnapshot = None,
          hasTakenCardThisTurn = false,
          isLastRound = nextIsLastRound
        )

    override def highlightedCells(token: TerrainToken): List[Coordinate] =
      val physicallyValid =
        TokenValidator.validPositions(token)

      val blockedCells: Set[Coordinate] = currentPlayer.activeCards
        .filter(_.placedCubes > 0)
        .flatMap(card => currentPlayer.board.findMatches(card.habitat))
        .flatMap(m => m.involvedCells + m.origin)
        .toSet

      physicallyValid.filterNot(blockedCells.contains)

    override def selectAnimalCard(card: AnimalCard): GameModel =
      if !currentPlayer.activeCards.contains(card) then
        throw IllegalStateException("Carta non posseduta")
      if highlightedAnimalCells(card).isEmpty then
        throw IllegalStateException(
          "Nessun habitat completato per questa carta"
        )
      this.copy(selectedAnimalCard = Some(card), selectedToken = None)

    override def highlightedAnimalCells(card: AnimalCard): List[Coordinate] =
      currentPlayer.board
        .findMatches(card.habitat)
        .map(_.origin)
        .toList

    override def placeAnimalCube(coordinate: Coordinate): GameModel =
      val card = selectedAnimalCard.getOrElse(
        throw IllegalStateException("Nessuna carta selezionata")
      )

      if !highlightedAnimalCells(card).contains(coordinate) then
        throw IllegalStateException("Posizione non valida per questo habitat")

      val (updatedBoard, updatedCard) =
        updateAnimalPlacement(currentPlayer, card, coordinate).getOrElse(
          throw IllegalStateException(
            "Piazzamento animale non valido"
          )
        )

      val (newActive, newCompleted) =
        if updatedCard.placedCubes == updatedCard.maxCubes then
          (
            currentPlayer.activeCards.filterNot(_ == card),
            currentPlayer.completedCards :+ updatedCard
          )
        else
          (
            currentPlayer.activeCards.map(c =>
              if c == card then updatedCard else c
            ),
            currentPlayer.completedCards
          )

      val updatedPlayer = currentPlayer.copy(
        board = updatedBoard,
        activeCards = newActive,
        completedCards = newCompleted
      )

      this.copy(
        players = players.updated(currentPlayerIndex, updatedPlayer),
        selectedAnimalCard = None,
        turnSnapshot = turnSnapshot.orElse(Some(this))
      )

    override def cancelTurn(): GameModel =
      turnSnapshot.map(_.copy(turnSnapshot = None)).getOrElse(this)

    override def allPlayers: List[Player] =
      players

    override def availableActionsMessage: String =
      val actions = List(
        Option.when(turnState == TurnState.WaitingForAction)("scegli tokens"),
        Option.when(
          !hasTakenCardThisTurn && currentPlayer.activeCards.size < MaxAnimalCards
        )("scegli una carta animale"),
        Option.when(
          currentPlayer.activeCards
            .exists(c =>
              c.placedCubes < c.maxCubes && highlightedAnimalCells(c).nonEmpty
            )
        )("posiziona cubo animale"),
        Option.when(turnState == ActionDone && tokensInHand.nonEmpty)(
          "posiziona token"
        ),
        Option.when(
          turnState == TurnState.TurnComplete && hasTakenCardThisTurn
        )("nessuna azione possibile rimasta")
      ).flatten

      if actions.isEmpty then
        s"${currentPlayer.name} non ha azioni disponibili (termina il turno)"
      else s"${currentPlayer.name} " + actions.mkString(" oppure ")

    private def updateAnimalPlacement(
        player: Player,
        card: AnimalCard,
        coordinate: Coordinate
    ): Option[(PersonalBoard, AnimalCard)] =
      for
        updatedBoard <- player.board.placeAnimalOnCell(coordinate)
        updatedCard <- card.placeCube
      yield (updatedBoard, updatedCard)

    private def hasPlayerAlmostFullBoard: Boolean =
      players.exists { player =>
        player.board.cells.values.count(!_.hasTokens) <= 2
      }

    private def requireState(
        expected: TurnState,
        errorMessage: String
    )(action: => GameModel): GameModel =
      if turnState != expected then throw IllegalStateException(errorMessage)
      else action
