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
      imageId = "card_crocodile"
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
      imageId = "card_manta"
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
      imageId = "card_salmon"
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
      imageId = "card_otter"
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
      imageId = "card_frog"
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
      imageId = "card_goose"
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
      imageId = "card_flamingo"
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
      imageId = "card_kingfisher"
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
      imageId = "card_heron"
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
      imageId = "card_raccoon"
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
      imageId = "card_gecko"
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
      imageId = "card_mouse"
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
      imageId = "card_peacock"
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
      imageId = "card_squirrel"
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
      imageId = "card_hedgehog"
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
      imageId = "card_bee"
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
      imageId = "card_rabbit"
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
      imageId = "card_fox"
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
      imageId = "card_crow"
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
      imageId = "card_llama"
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
      imageId = "card_ladybug"
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
      imageId = "card_bear"
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
      imageId = "card_penguin"
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
      imageId = "card_bat"
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
      imageId = "card_snowleopard"
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
      imageId = "card_meerkat"
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
      imageId = "card_macaw"
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
      imageId = "card_boar"
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
      imageId = "card_koala"
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
      imageId = "card_wolf"
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
      imageId = "card_panther"
    )
  )

  def createShuffledDeck(): List[AnimalCard] =
    scala.util.Random.shuffle(allCards)
