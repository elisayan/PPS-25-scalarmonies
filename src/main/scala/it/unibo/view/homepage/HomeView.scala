package it.unibo.view.homepage

import scalafx.scene.{Node, Parent}
import scalafx.scene.layout.*
import scalafx.scene.control.*
import scalafx.geometry.{Insets, Pos}
import it.unibo.model.personalboard.BoardSide

object HomeView:

  def apply(onGameStart: (List[String], BoardSide) => Unit): Parent =
    var playerTextFields: List[TextField] = List.empty
    val playerNamesBox = new VBox:
      spacing = 10
      alignment = Pos.Center

    def updatePlayerFields(numPlayers: Int): Unit =
      playerTextFields = (1 to numPlayers).map: i =>
        new TextField:
          promptText = s"Nome Giocatore $i"
          maxWidth = 200
          style = "-fx-font-size: 14px; -fx-padding: 8px; -fx-background-radius: 5px;"
      .toList
      playerNamesBox.children = playerTextFields
    val numPlayersCombo = new ComboBox[Int](Seq(2, 3, 4)):
      value = 2
      style = "-fx-font-size: 14px;"
    numPlayersCombo.value.onChange: (_, _, newValue) =>
      updatePlayerFields(newValue)
    updatePlayerFields(2)
    val sideGroup = new ToggleGroup()
    val sideA = new RadioButton("Lato A"):
      toggleGroup = sideGroup
      selected = true
      style = "-fx-font-size: 14px;"

    val sideB = new RadioButton("Lato B"):
      toggleGroup = sideGroup
      style = "-fx-font-size: 14px;"

    val sidesBox = new HBox:
      spacing = 20
      alignment = Pos.Center
      children = Seq(sideA, sideB)

    val startButton = new Button("Inizia Partita"):
      style = "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-background-radius: 5px;"
      onAction = _ =>
        val rawNames = playerTextFields.map(_.text.value)
        val finalNames = rawNames.zipWithIndex.map: (name, idx) =>
          if name.trim.isEmpty then s"Player ${idx + 1}" else name.trim
        val selectedSide = if sideA.selected.value then BoardSide.SideA else BoardSide.SideB
        onGameStart(finalNames, selectedSide)

    new VBox:
      spacing = 30
      alignment = Pos.Center
      padding = Insets(50)
      style = "-fx-background-color: #ecf0f1;"
      children = Seq(
        new Label("SCALARMONIES"):
          style = "-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;"
        ,
        new VBox { alignment = Pos.Center; spacing = 10; children = Seq(new Label("Numero di Giocatori:") { style = "-fx-font-size: 16px; -fx-font-weight: bold;" }, numPlayersCombo) },
        new VBox { alignment = Pos.Center; spacing = 10; children = Seq(new Label("Nomi Giocatori:") { style = "-fx-font-size: 16px; -fx-font-weight: bold;" }, playerNamesBox) },
        new VBox { alignment = Pos.Center; spacing = 10; children = Seq(new Label("Scegli la Plancia:") { style = "-fx-font-size: 16px; -fx-font-weight: bold;" }, sidesBox) },
        startButton
      )