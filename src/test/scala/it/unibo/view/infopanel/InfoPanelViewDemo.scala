package it.unibo.view.infopanel

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.StackPane

object InfoPanelViewDemo extends JFXApp3:

  override def start(): Unit =

    val panel = InfoPanelView()

    panel.addTurnHeader("Player1")

    panel.addEntry(
      "Player1",
      "prende la carta Scoiattolo e la porta nel suo posto 1",
      0
    )

    panel.addEntry(
      "Player1",
      "prende Forest, Water, Mountain",
      0
    )

    panel.addEntry(
      "Player1",
      "seleziona Forest",
      0
    )

    panel.addEntry(
      "Player1",
      "posiziona il token al livello 1",
      0
    )

    panel.addEntry(
      "Player1",
      "posiziona un cubo sulla carta Scoiattolo",
      0
    )

    panel.addTurnHeader("Player2")

    panel.addEntry(
      "Player2",
      "prende Montagna, Campo, Bosco",
      1
    )

    panel.addEntry(
      "Player2",
      "seleziona Montagna",
      1
    )

    panel.addEntry(
      "Player2",
      "posiziona Montagna al livello 1",
      1
    )

    panel.addEntry(
      "Player2",
      "annulla il turno",
      1
    )

    panel.addEntry(
      "Player2",
      "riprende i token iniziali",
      1
    )

    panel.addEntry(
      "Player2",
      "posiziona un cubo sulla carta Volpe",
      1
    )

    panel.addTurnHeader("Player1")

    panel.addEntry(
      "Player1",
      "completa la carta Orso",
      0
    )

    panel.addEntry(
      "Player1",
      "ottiene 5 punti",
      0
    )

    stage = new JFXApp3.PrimaryStage:
      title = "Info Panel Demo"
      scene = new Scene(350, 600):
        root = new StackPane:
          children = panel.root
