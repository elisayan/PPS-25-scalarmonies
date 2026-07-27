package it.unibo.model.card

import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken.Building
import it.unibo.model.token.TerrainToken.Field
import it.unibo.model.token.TerrainToken.Forest
import it.unibo.model.token.TerrainToken.Mountain
import it.unibo.model.token.TerrainToken.Water

object AnimalDeckFactory:

  private val allCards: List[AnimalCard] = List(
    AnimalCard(
      name = "Coccodrillo",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-4, 2), Forest, 2),
          CellRequirement(Coordinate(-2, 1), Water, 1),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(4, 9, 15),
      imageId = "card_crocodile.png"
    ),
    AnimalCard(
      name = "Manta",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(0, 2), Mountain, 1),
          CellRequirement(Coordinate(-2, 1), Mountain, 1),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(4, 10, 16),
      imageId = "card_manta.png"
    ),
    AnimalCard(
      name = "Salmone",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Mountain, 2),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(3, 6, 10, 16),
      imageId = "card_salmon.png"
    ),
    AnimalCard(
      name = "Lontra",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-4, 2), Field, 1),
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(5, 10, 16),
      imageId = "card_otter.png"
    ),
    AnimalCard(
      name = "Rana",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(2, 4, 6, 10, 15),
      imageId = "card_frog.png"
    ),
    AnimalCard(
      name = "Oca",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Building, 1),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(2, 4, 8, 13),
      imageId = "card_goose.png"
    ),
    AnimalCard(
      name = "Fenicottero",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(0, 2), Water, 1),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(4, 10, 16),
      imageId = "card_flamingo.png"
    ),
    AnimalCard(
      name = "Martin Pescatore",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Forest, 2),
          CellRequirement(Coordinate(2, -1), Water, 1),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(5, 11, 18),
      imageId = "card_kingfisher.png"
    ),
    AnimalCard(
      name = "Airone",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Forest, 2),
          CellRequirement(Coordinate(2, -1), Forest, 2),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(5, 10, 17),
      imageId = "card_heron.png"
    ),
    AnimalCard(
      name = "Procione",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Water, 1),
          CellRequirement(Coordinate(2, -1), Water, 1),
          CellRequirement(Coordinate(0, 2), Field, 1),
          CellRequirement(Coordinate(0, 0), Water, 1)
        )
      ),
      points = List(6, 12),
      imageId = "card_raccoon.png"
    ),
    AnimalCard(
      name = "Geco",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-4, 2), Field, 1),
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(0, 0), Building, 1)
        )
      ),
      points = List(5, 10, 16),
      imageId = "card_gecko.png"
    ),
    AnimalCard(
      name = "Topo",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(2, -1), Field, 1),
          CellRequirement(Coordinate(0, 0), Building, 1)
        )
      ),
      points = List(5, 10, 17),
      imageId = "card_mouse.png"
    ),
    AnimalCard(
      name = "Pavone",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Water, 1),
          CellRequirement(Coordinate(2, -1), Water, 1),
          CellRequirement(Coordinate(0, 0), Building, 2)
        )
      ),
      points = List(5, 10, 17),
      imageId = "card_peacock.png"
    ),
    AnimalCard(
      name = "Scoiattolo",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Forest, 2),
          CellRequirement(Coordinate(0, 0), Building, 2)
        )
      ),
      points = List(4, 9, 15),
      imageId = "card_squirrel.png"
    ),
    AnimalCard(
      name = "Riccio",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Forest, 2),
          CellRequirement(Coordinate(2, -1), Forest, 2),
          CellRequirement(Coordinate(0, 0), Building, 1)
        )
      ),
      points = List(5, 12),
      imageId = "card_hedgehog.png"
    ),
    AnimalCard(
      name = "Ape",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(2, -1), Field, 1),
          CellRequirement(Coordinate(0, 2), Forest, 2),
          CellRequirement(Coordinate(0, 0), Field, 1)
        )
      ),
      points = List(8, 18),
      imageId = "card_bee.png"
    ),
    AnimalCard(
      name = "Coniglio",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Building, 1),
          CellRequirement(Coordinate(2, -1), Forest, 1),
          CellRequirement(Coordinate(0, 0), Field, 1)
        )
      ),
      points = List(5, 10, 17),
      imageId = "card_rabbit.png"
    ),
    AnimalCard(
      name = "Volpe",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(2, -1), Mountain, 1),
          CellRequirement(Coordinate(0, 0), Field, 1)
        )
      ),
      points = List(4, 9, 16),
      imageId = "card_fox.png"
    ),
    AnimalCard(
      name = "Corvo",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Building, 1),
          CellRequirement(Coordinate(2, -1), Building, 1),
          CellRequirement(Coordinate(0, 0), Field, 1)
        )
      ),
      points = List(4, 9),
      imageId = "card_crow.png"
    ),
    AnimalCard(
      name = "Lama",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Mountain, 2),
          CellRequirement(Coordinate(2, -1), Field, 1),
          CellRequirement(Coordinate(0, 0), Field, 1)
        )
      ),
      points = List(5, 12),
      imageId = "card_llama.png"
    ),
    AnimalCard(
      name = "Coccinella",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Forest, 1),
          CellRequirement(Coordinate(2, -1), Field, 1),
          CellRequirement(Coordinate(0, 2), Forest, 1),
          CellRequirement(Coordinate(0, 0), Field, 1)
        )
      ),
      points = List(2, 5, 8, 12, 17),
      imageId = "card_ladybug.png"
    ),
    AnimalCard(
      name = "Orso",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Mountain, 2),
          CellRequirement(Coordinate(2, -1), Forest, 1),
          CellRequirement(Coordinate(0, 0), Mountain, 1)
        )
      ),
      points = List(5, 11),
      imageId = "card_bear.png"
    ),
    AnimalCard(
      name = "Pinguino",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Water, 1),
          CellRequirement(Coordinate(2, -1), Water, 1),
          CellRequirement(Coordinate(0, 0), Mountain, 1)
        )
      ),
      points = List(4, 10, 16),
      imageId = "card_penguin.png"
    ),
    AnimalCard(
      name = "Pipistrello",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Forest, 2),
          CellRequirement(Coordinate(0, 0), Mountain, 2)
        )
      ),
      points = List(3, 6, 10, 15),
      imageId = "card_bat.png"
    ),
    AnimalCard(
      name = "Leopardo delle Nevi",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Water, 1),
          CellRequirement(Coordinate(2, -1), Water, 1),
          CellRequirement(Coordinate(0, 0), Mountain, 2)
        )
      ),
      points = List(5, 11),
      imageId = "card_snowleopard.png"
    ),
    AnimalCard(
      name = "Aquila",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(0, 0), Mountain, 2)
        )
      ),
      points = List(5, 11),
      imageId = "card_eagle"
    ),
    AnimalCard(
      name = "Suricato",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(2, -1), Field, 1),
          CellRequirement(Coordinate(0, 0), Mountain, 2)
        )
      ),
      points = List(2, 5, 9, 14),
      imageId = "card_meerkat.png"
    ),
    AnimalCard(
      name = "Pappagallo",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Water, 1),
          CellRequirement(Coordinate(2, -1), Water, 1),
          CellRequirement(Coordinate(0, 0), Forest, 2)
        )
      ),
      points = List(4, 9, 14),
      imageId = "card_macaw.png"
    ),
    AnimalCard(
      name = "Cinghiale",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Building, 1),
          CellRequirement(Coordinate(0, 0), Forest, 2)
        )
      ),
      points = List(4, 8, 13),
      imageId = "card_boar.png"
    ),
    AnimalCard(
      name = "Koala",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Forest, 1),
          CellRequirement(Coordinate(2, -1), Forest, 1),
          CellRequirement(Coordinate(0, 0), Forest, 1)
        )
      ),
      points = List(3, 6, 10, 15),
      imageId = "card_koala.png"
    ),
    AnimalCard(
      name = "Lupo",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Field, 1),
          CellRequirement(Coordinate(2, -1), Field, 1),
          CellRequirement(Coordinate(0, 0), Forest, 2)
        )
      ),
      points = List(4, 10, 16),
      imageId = "card_wolf.png"
    ),
    AnimalCard(
      name = "Pantera",
      habitat = Habitat(
        List(
          CellRequirement(Coordinate(-2, 1), Forest, 2),
          CellRequirement(Coordinate(2, -1), Field, 1),
          CellRequirement(Coordinate(0, 0), Forest, 1)
        )
      ),
      points = List(5, 11),
      imageId = "card_panther.png"
    )
  )

  def createShuffledDeck(): List[AnimalCard] =
    scala.util.Random.shuffle(allCards)
