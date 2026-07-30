package it.unibo.view.homepage

import it.unibo.model.personalboard.BoardSide
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Parent
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight

object HomeView:

  def apply(onGameStart: (List[String], BoardSide) => Unit): Parent =

    val allTextFields: List[TextField] = (1 to 4)
      .map: i =>
        new TextField:
          promptText = s"Nome Giocatore $i"
          maxWidth = 260
          style =
            "-fx-font-size: 14px; -fx-padding: 9px; -fx-background-radius: 6px; " +
              "-fx-border-color: #d5cfc0; -fx-border-radius: 6px;"
      .toList

    var activeTextFields: List[TextField] = List.empty
    val playerNamesBox = new VBox(10):
      alignment = Pos.Center

    def updatePlayerFields(numPlayers: Int): Unit =
      activeTextFields = allTextFields.take(numPlayers)
      playerNamesBox.children = activeTextFields

    val numPlayersCombo = new ComboBox[Int](Seq(2, 3, 4)):
      value = 2
      style = "-fx-font-size: 14px; -fx-background-radius: 6px;"
    numPlayersCombo.value.onChange: (_, _, newValue) =>
      updatePlayerFields(newValue)

    updatePlayerFields(2)

    val sideGroup = new ToggleGroup()
    val sideA = new RadioButton("Lato A (Fiumi - Consigliato)"):
      toggleGroup = sideGroup
      selected = true
      style =
        "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;"

    val sideB = new RadioButton("Lato B (Isole - Avanzato)"):
      toggleGroup = sideGroup
      style =
        "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;"

    val sideExplanation = new Label(
      "• Lato A: Punti per il Fiume blu più lungo.\n• Lato B: Punti per le Isole di terra separate dall'acqua."
    ):
      style = "-fx-font-size: 12px; -fx-text-fill: #555555;"
      wrapText = true
      maxWidth = 260

    val sidesBox = new VBox(5):
      alignment = Pos.CenterLeft
      padding = Insets(5, 0, 5, 20)
      children = Seq(sideA, sideB, sideExplanation)

    val startButton = new Button("INIZIA PARTITA"):
      font = Font.font("Palatino", FontWeight.Bold, 16.0)
      maxWidth = Double.MaxValue
      style =
        "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 13px 20px; " +
          "-fx-background-radius: 8px; -fx-cursor: hand;"
      onAction = _ =>
        val rawNames = activeTextFields.map(_.text.value)
        val finalNames = rawNames.zipWithIndex.map: (name, idx) =>
          if name.trim.isEmpty then s"Player ${idx + 1}" else name.trim
        val selectedSide =
          if sideA.selected.value then BoardSide.SideA else BoardSide.SideB
        onGameStart(finalNames, selectedSide)

    def createBadge(text: String, mandatory: Boolean): Label =
      new Label(text):
        font = Font.font("System", FontWeight.Bold, 11)
        style =
          if mandatory then
            "-fx-background-color: #fadcdc; -fx-text-fill: #900C3F; -fx-padding: 2 6; -fx-background-radius: 4;"
          else
            "-fx-background-color: #e8f5e9; -fx-text-fill: #1b5e20; -fx-padding: 2 6; -fx-background-radius: 4;"

    def actionRow(title: String, mandatory: Boolean, desc: String): VBox =
      new VBox(4):
        children = Seq(
          new HBox(8):
            alignment = Pos.CenterLeft
            children = Seq(
              new Label(s"• $title"):
                font = Font.font("Palatino", FontWeight.Bold, 13)
                style = "-fx-text-fill: #333333;"
              ,
              createBadge(
                if mandatory then "OBBLIGATORIO" else "FACOLTATIVO",
                mandatory
              )
            )
          ,
          new Label(desc):
            wrapText = true
            style =
              "-fx-font-size: 13px; -fx-text-fill: #555555; -fx-padding: 0 0 0 10;"
        )

    def terrainLegendRow(
        name: String,
        colorHexes: Seq[String],
        desc: String
    ): VBox =
      val circles = colorHexes.map: hex =>
        new Circle:
          radius = 6.0
          fill = Color.web(hex)
          stroke = Color.web("#2c3e50")
          strokeWidth = 1.0

      val circlesBox = new HBox(4):
        alignment = Pos.CenterLeft
        children = circles

      new VBox(3):
        children = Seq(
          new HBox(8):
            alignment = Pos.CenterLeft
            children = Seq(
              circlesBox,
              new Label(name):
                font = Font.font("Palatino", FontWeight.Bold, 13)
                style = "-fx-text-fill: #333333;"
            )
          ,
          new Label(desc):
            wrapText = true
            style =
              "-fx-font-size: 13px; -fx-text-fill: #555555; -fx-padding: 0 0 0 20;"
        )

    val setupCard = new VBox(18):
      alignment = Pos.Center
      padding = Insets(25, 30, 25, 30)
      prefWidth = 380
      maxWidth = 400
      style = "-fx-background-color: #ffffff; -fx-background-radius: 12px; " +
        "-fx-border-color: #dcd3c1; -fx-border-width: 1px; -fx-border-radius: 12px; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);"
      children = Seq(
        new Label("SCALARMONIES"):
          font = Font.font("Palatino", FontWeight.Bold, 30)
          style = "-fx-text-fill: #2c3e50;"
        ,
        new Separator(),
        new VBox(8):
          alignment = Pos.Center
          children = Seq(
            new Label("Numero di Giocatori:"):
              style =
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;"
            ,
            numPlayersCombo
          )
        ,
        new VBox(8):
          alignment = Pos.Center
          children = Seq(
            new Label("Nomi Giocatori:"):
              style =
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;"
            ,
            playerNamesBox
          )
        ,
        new VBox(8):
          alignment = Pos.CenterLeft
          children = Seq(
            new Label("Scegli la Plancia di Gioco:"):
              style =
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;"
            ,
            sidesBox
          )
        ,
        new Separator(),
        startButton
      )

    val tutorialHeader = new VBox(4):
      padding = Insets(16, 20, 14, 20)
      alignment = Pos.CenterLeft
      style = "-fx-background-color: #f8f6f0; -fx-border-color: #dcd3c1; " +
        "-fx-border-width: 0 0 1 0; -fx-background-radius: 12 12 0 0;"
      children = Seq(
        new Label("TUTORIAL HARMONIES"):
          font = Font.font("Palatino", FontWeight.Bold, 18)
          style = "-fx-text-fill: #2c3e50;"
        ,
        new Label(
          "Guida rapida allo svolgimento del turno, regole di gioco e punteggi"
        ):
          style = "-fx-font-size: 12px; -fx-text-fill: #666666;"
      )

    val tutorialContent = new VBox(14):
      padding = Insets(20)
      style = "-fx-background-color: #ffffff;"
      children = Seq(
        new Label("Obiettivo del Gioco"):
          font = Font.font("Palatino", FontWeight.Bold, 15)
          style = "-fx-text-fill: #c0392b;"
        ,
        new Label(
          "Costruisci paesaggi sovrapponendo i gettoni terreno e crea gli Habitat ideali " +
            "sulla tua plancia per accogliere gli Animali e fare più punti."
        ):
          wrapText = true
          style = "-fx-font-size: 13px; -fx-text-fill: #333333;"
        ,
        new Separator(),
        new Label("1. Svolgimento del Turno"):
          font = Font.font("Palatino", FontWeight.Bold, 15)
          style = "-fx-text-fill: #27ae60;"
        ,
        actionRow(
          "PRENDERE E COLLOCARE DISCHI",
          mandatory = true,
          "Prendi i 3 dischi da una casella del mercato centrale e piazzali sulla tua plancia seguendo le regole di impilamento."
        ),
        actionRow(
          "PRENDERE 1 CARTA ANIMALE",
          mandatory = false,
          "Scegli una carta dal mercato (max 1 per turno). Puoi avere massimo 4 carte attive contemporaneamente in mano."
        ),
        actionRow(
          "COLLOCARE CUBI ANIMALE",
          mandatory = false,
          "Quando la disposizione dei terreni sulla tua plancia corrisponde al modello di un Animale, puoi piazzare il cubo sulla plancia."
        ),
        new Separator(),
        new Label("2. Regole di Impilamento dei Terreni"):
          font = Font.font("Palatino", FontWeight.Bold, 15)
          style = "-fx-text-fill: #2980b9;"
        ,
        terrainLegendRow(
          "MONTAGNA (Grigio)",
          Seq("#7f8c8d"),
          "Impilabile fino ad altezza 3 (solo sopra altri dischi grigi)."
        ),
        terrainLegendRow(
          "ALBERO (Verde / Marrone)",
          Seq("#795548", "#27ae60"),
          "Tronco marrone sotto, chioma verde sopra (max altezza 3)."
        ),
        terrainLegendRow(
          "EDIFICIO (Rosso)",
          Seq("#c0392b"),
          "Max altezza 2. Si piazza sopra 1 disco Marrone, Grigio o Rosso."
        ),
        terrainLegendRow(
          "ACQUA (Blu) & CAMPI (Giallo)",
          Seq("#2980b9", "#f1c40f"),
          "Altezza massima 1 (non impilabili, solo su caselle vuote)."
        ),
        new Separator(),
        new Label("3. Calcolo dei Punteggi (Paesaggi e Animali)"):
          font = Font.font("Palatino", FontWeight.Bold, 15)
          style = "-fx-text-fill: #e67e22;"
        ,
        terrainLegendRow(
          "ALBERI",
          Seq("#795548", "#27ae60"),
          "Valgono 1, 3 o 7 punti in base all'altezza (1, 2 o 3 dischi)."
        ),
        terrainLegendRow(
          "MONTAGNE",
          Seq("#7f8c8d"),
          "Valgono 1, 3 o 7 punti in base all'altezza, ma solo se toccano almeno un'altra Montagna (da sole valgono 0)."
        ),
        terrainLegendRow(
          "EDIFICI",
          Seq("#c0392b"),
          "Valgono 5 punti ciascuno, ma solo se hanno accanto almeno 3 terreni di colori diversi."
        ),
        terrainLegendRow(
          "CAMPI",
          Seq("#f1c40f"),
          "Valgono 5 punti per ogni gruppo separato di almeno 2 campi adiacenti."
        ),
        terrainLegendRow(
          "ACQUA",
          Seq("#2980b9"),
          "Lato A: punti per il fiume più lungo.\nLato B: 5 punti per ogni isola di terra separata."
        ),
        terrainLegendRow(
          "CARTE ANIMALE",
          Seq("#e6b981"),
          "Ottieni il punteggio mostrato nello spazio più in alto senza cubi sulla carta. Una carta con tutti i cubi ancora sopra vale 0 punti."
        ),
        new Separator(),
        new Label("4. Fine della Partita"):
          font = Font.font("Palatino", FontWeight.Bold, 15)
          style = "-fx-text-fill: #8e44ad;"
        ,
        new Label(
          "La partita termina quando il sacchetto si svuota e non è più possibile rifornire il mercato " +
            "oppure quando un giocatore resta con 2 o meno caselle vuote sulla propria plancia."
        ):
          wrapText = true
          style = "-fx-font-size: 13px; -fx-text-fill: #333333;"
      )

    val tutorialScrollPane = new ScrollPane:
      content = tutorialContent
      fitToWidth = true
      vvalue = 0.0
      hbarPolicy = ScrollPane.ScrollBarPolicy.Never
      vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
      style = "-fx-background-color: transparent; -fx-background-insets: 0;"

    VBox.setVgrow(tutorialScrollPane, Priority.Always)

    val tutorialCard = new VBox(0):
      prefWidth = 480
      maxWidth = 540
      maxHeight = Double.MaxValue
      style = "-fx-background-color: #ffffff; -fx-background-radius: 12px; " +
        "-fx-border-color: #dcd3c1; -fx-border-width: 1px; -fx-border-radius: 12px; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);"
      children = Seq(tutorialHeader, tutorialScrollPane)

    val rootLayout = new HBox(35):
      alignment = Pos.Center
      padding = Insets(30)
      style = "-fx-background-color: #fcfbf7;"
      children = Seq(setupCard, tutorialCard)

    HBox.setHgrow(tutorialCard, Priority.Always)
    rootLayout
