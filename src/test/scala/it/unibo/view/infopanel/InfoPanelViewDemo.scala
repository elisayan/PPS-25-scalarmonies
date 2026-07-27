package it.unibo.view.infopanel

import it.unibo.model.token.TerrainToken
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.StackPane

object InfoPanelViewDemo extends JFXApp3:

  override def start(): Unit =

    val panel = InfoPanelView()

    panel.addTurnHeader("Player1")

    panel.addEntry(
      "Player1",
      "prende la carta Scoiattolo e la porta nel suo posto 1"
    )

    panel.addEntry(
      "Player1",
      "prende Forest, Water, Mountain"
    )

    panel.addEntry(
      "Player1",
      "seleziona Forest"
    )

    panel.addEntry(
      "Player1",
      "posiziona il token"
    )

    panel.addEntry(
      "Player1",
      "posiziona un cubo sul token"
    )

    // Cambio turno
    panel.addTurnHeader("Player2")

    // Turno Player2
    panel.addEntry(
      "Player2",
      "prende Montagna, Campo, Bosco"
    )

    panel.addEntry(
      "Player2",
      "seleziona Montagna"
    )

    panel.addEntry(
      "Player2",
      "posiziona Montagna al livello 1"
    )

    panel.addEntry(
      "Player2",
      "annulla il turno"
    )

    panel.addEntry(
      "Player2",
      "riprende i token"
    )

    panel.addEntry(
      "Player2",
      "posiziona un cubo sulla carta Volpe"
    )

    panel.addTurnHeader("Player1")

    panel.addEntry(
      "Player1",
      "completa la carta Orso"
    )

    panel.addEntry(
      "Player1",
      "ottiene 5 punti"
    )

    stage = new JFXApp3.PrimaryStage:
      title = "Info Panel Demo"
      scene = new Scene(350, 600):
        root = new StackPane:
          children = panel.root
