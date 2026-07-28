package it.unibo.view.animalcube

import it.unibo.model.card.{AnimalCard, CellRequirement, Habitat}
import it.unibo.model.personalboard.BoardSide.SideA
import it.unibo.model.personalboard.{Coordinate, PersonalBoard}
import it.unibo.model.token.TerrainToken
import it.unibo.model.{GameModel, Player}
import it.unibo.view.card.AnimalCardView
import it.unibo.view.personalboard.PersonalBoardView
import scalafx.application.JFXApp3
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color

object InteractiveCubesDemo extends JFXApp3:

  var model: GameModel = _

  override def start(): Unit =
    val forestHabitat = Habitat(List(CellRequirement(Coordinate(0,0), TerrainToken.Forest, 1)))
    val mountainHabitat = Habitat(List(CellRequirement(Coordinate(0,0), TerrainToken.Mountain, 2)))

    val card1 = AnimalCard("Volpe (1 Habitat)", forestHabitat, List(2, 4))
    val card2 = AnimalCard("Aquila (2 Habitat)", mountainHabitat, List(3, 5))

    var board = PersonalBoard(SideA)

    board = board.placeToken(TerrainToken.Forest, Coordinate(0, 0)).get

    board = board.placeToken(TerrainToken.Mountain, Coordinate(-2, 1)).get
    board = board.placeToken(TerrainToken.Mountain, Coordinate(-2, 1)).get

    board = board.placeToken(TerrainToken.Mountain, Coordinate(2, 1)).get
    board = board.placeToken(TerrainToken.Mountain, Coordinate(2, 1)).get

    val player = Player(1, "Demo", board, activeCards = List(card1, card2))
    model = GameModel(List(player))

    stage = new JFXApp3.PrimaryStage:
      title = "Demo Interattiva: Piazzamento Cubi Animale"
      width = 700
      height = 650

    refreshView()

  def refreshView(): Unit =
    val player = model.currentPlayer

    val highlighted = model.selectedAnimalCard match
      case Some(card) => model.highlightedAnimalCells(card)
      case None =>
        val playable = player.activeCards.filter(c => model.highlightedAnimalCells(c).nonEmpty)
        if playable.size == 1 then model.highlightedAnimalCells(playable.head) else List()

    val boardView = PersonalBoardView(player, onCellClicked, highlighted)

    val cardsBox = new HBox(15):
      alignment = Pos.Center
      children = player.activeCards.map: c =>
        val cardView = AnimalCardView(c)
        cardView.onMouseClicked = _ => onCardClicked(c)
        val isSelected = model.selectedAnimalCard.contains(c)
        val isPlayable = model.highlightedAnimalCells(c).nonEmpty
        val hasSelection = model.selectedAnimalCard.isDefined

        if isSelected then
          cardView.effect = new DropShadow(20.0, Color.LimeGreen)
        else if !hasSelection && isPlayable then
          cardView.effect = new DropShadow(15.0, Color.Gold)
        else
          cardView.effect = null

        cardView

    val rootBox = new VBox(30):
      padding = Insets(40)
      alignment = Pos.Center
      children = Seq(cardsBox, boardView)

    stage.scene = new Scene(rootBox)

  def onCardClicked(card: AnimalCard): Unit =
    if model.selectedAnimalCard.contains(card) then return
    if model.highlightedAnimalCells(card).isEmpty then
      println("Azione bloccata: nessun habitat sulla plancia per questa carta.")
      return

    model = model.selectAnimalCard(card)
    refreshView()

  def onCellClicked(coord: Coordinate): Unit =
    if model.selectedAnimalCard.isDefined then
      try
        model = model.placeAnimalCube(coord)
        println(s"Cubo piazzato in $coord")
      catch case e: Exception => println(s"ERRORE: ${e.getMessage}")
    else
      val playable = model.currentPlayer.activeCards.filter(c => model.highlightedAnimalCells(c).nonEmpty)
      if playable.size == 1 && model.highlightedAnimalCells(playable.head).contains(coord) then
        model = model.selectAnimalCard(playable.head)
        model = model.placeAnimalCube(coord)
        println(s"Cubo piazzato in $coord")

    refreshView()
