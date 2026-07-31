package it.unibo.model.card

import it.unibo.model.card.HabitatDSL.req
import it.unibo.model.personalboard.Coordinate
import it.unibo.model.token.TerrainToken.Building
import it.unibo.model.token.TerrainToken.Field
import it.unibo.model.token.TerrainToken.Forest
import it.unibo.model.token.TerrainToken.Mountain
import it.unibo.model.token.TerrainToken.Water

import scala.util.Random

object AnimalDeckFactory:
  private val allCards: List[AnimalCard] = List(
    AnimalCard(
      name = "Coccodrillo",
      habitat = Habitat(
        List(
          Coordinate(-4, 2) req (Forest, 3),
          Coordinate(-2, 1) req (Water, 1),
          Coordinate(0, 0) req (Water, 1)
        )
      ),
      points = List(4, 9, 15),
      imageId = "card_crocodile.jpg"
    ),
    AnimalCard(
      name = "Manta",
      habitat = Habitat(
        List(
          Coordinate(0, 2) req (Mountain, 1),
          Coordinate(-2, 1) req (Mountain, 1),
          Coordinate(0, 0) req (Water, 1)
        )
      ),
      points = List(4, 10, 16),
      imageId = "card_manta.jpg"
    ),
    AnimalCard(
      name = "Salmone",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Mountain, 3),
          Coordinate(0, 0) req (Water, 1)
        )
      ),
      points = List(3, 6, 10, 16),
      imageId = "card_salmon.jpg"
    ),
    AnimalCard(
      name = "Lontra",
      habitat = Habitat(
        List(
          Coordinate(-4, 2) req (Field, 1),
          Coordinate(-2, 1) req (Field, 1),
          Coordinate(0, 0) req (Water, 1)
        )
      ),
      points = List(5, 10, 16),
      imageId = "card_otter.jpg"
    ),
    AnimalCard(
      name = "Rana",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Field, 1),
          Coordinate(0, 0) req (Water, 1)
        )
      ),
      points = List(2, 4, 6, 10, 15),
      imageId = "card_frog.jpg"
    ),
    AnimalCard(
      name = "Oca",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Building, 2),
          Coordinate(0, 0) req (Water, 1)
        )
      ),
      points = List(2, 4, 8, 13),
      imageId = "card_goose.jpg"
    ),
    AnimalCard(
      name = "Fenicottero",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Field, 1),
          Coordinate(0, 2) req (Water, 1),
          Coordinate(0, 0) req (Water, 1)
        )
      ),
      points = List(4, 10, 16),
      imageId = "card_flamingo.jpg"
    ),
    AnimalCard(
      name = "Martin Pescatore",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Forest, 3),
          Coordinate(2, -1) req (Water, 1),
          Coordinate(0, 0) req (Water, 1)
        )
      ),
      points = List(5, 11, 18),
      imageId = "card_kingfisher.jpg"
    ),
    AnimalCard(
      name = "Airone",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Forest, 2),
          Coordinate(2, -1) req (Forest, 2),
          Coordinate(0, 0) req (Field, 1)
        )
      ),
      points = List(5, 10, 17),
      imageId = "card_heron.jpg"
    ),
    AnimalCard(
      name = "Procione",
      habitat = Habitat(
        List(
          Coordinate(-2, -1) req (Water, 1),
          Coordinate(2, -1) req (Water, 1),
          Coordinate(0, 0) req (Field, 1),
          Coordinate(0, -2) req (Water, 1)
        )
      ),
      points = List(6, 12),
      imageId = "card_raccoon.jpg"
    ),
    AnimalCard(
      name = "Geco",
      habitat = Habitat(
        List(
          Coordinate(-4, 2) req (Field, 1),
          Coordinate(-2, 1) req (Field, 1),
          Coordinate(0, 0) req (Building, 2)
        )
      ),
      points = List(5, 10, 16),
      imageId = "card_gecko.jpg"
    ),
    AnimalCard(
      name = "Topo",
      habitat = Habitat(
        List(
          Coordinate(-2, -1) req (Field, 1),
          Coordinate(2, -1) req (Field, 1),
          Coordinate(0, 0) req (Building, 2)
        )
      ),
      points = List(5, 10, 17),
      imageId = "card_mouse.jpg"
    ),
    AnimalCard(
      name = "Pavone",
      habitat = Habitat(
        List(
          Coordinate(-2, -1) req (Water, 1),
          Coordinate(2, -1) req (Water, 1),
          Coordinate(0, 0) req (Building, 2)
        )
      ),
      points = List(5, 10, 17),
      imageId = "card_peacock.jpg"
    ),
    AnimalCard(
      name = "Scoiattolo",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Forest, 3),
          Coordinate(0, 0) req (Building, 2)
        )
      ),
      points = List(4, 9, 15),
      imageId = "card_squirrel.jpg"
    ),
    AnimalCard(
      name = "Riccio",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Forest, 2),
          Coordinate(-2, -1) req (Forest, 2),
          Coordinate(0, 0) req (Building, 2)
        )
      ),
      points = List(5, 12),
      imageId = "card_hedgehog.jpg"
    ),
    AnimalCard(
      name = "Ape",
      habitat = Habitat(
        List(
          Coordinate(-2, -1) req (Field, 1),
          Coordinate(2, -1) req (Field, 1),
          Coordinate(0, 0) req (Forest, 2),
          Coordinate(0, -2) req (Field, 1)
        )
      ),
      points = List(8, 18),
      imageId = "card_bee.jpg"
    ),
    AnimalCard(
      name = "Coniglio",
      habitat = Habitat(
        List(
          Coordinate(-4, 2) req (Building, 2),
          Coordinate(-2, 1) req (Forest, 1),
          Coordinate(0, 0) req (Forest, 1)
        )
      ),
      points = List(5, 10, 17),
      imageId = "card_rabbit.jpg"
    ),
    AnimalCard(
      name = "Volpe",
      habitat = Habitat(
        List(
          Coordinate(-4, 2) req (Field, 1),
          Coordinate(-2, 1) req (Mountain, 1),
          Coordinate(0, 0) req (Mountain, 1)
        )
      ),
      points = List(4, 9, 16),
      imageId = "card_fox.jpg"
    ),
    AnimalCard(
      name = "Corvo",
      habitat = Habitat(
        List(
          Coordinate(-2, -1) req (Building, 2),
          Coordinate(2, -1) req (Building, 2),
          Coordinate(0, 0) req (Field, 1)
        )
      ),
      points = List(4, 9),
      imageId = "card_crow.jpg"
    ),
    AnimalCard(
      name = "Lama",
      habitat = Habitat(
        List(
          Coordinate(-4, 2) req (Mountain, 2),
          Coordinate(-2, 1) req (Field, 1),
          Coordinate(0, 0) req (Field, 1)
        )
      ),
      points = List(5, 12),
      imageId = "card_llama.jpg"
    ),
    AnimalCard(
      name = "Coccinella",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Forest, 1),
          Coordinate(0, 0) req (Field, 1)
        )
      ),
      points = List(2, 5, 8, 12, 17),
      imageId = "card_ladybug.jpg"
    ),
    AnimalCard(
      name = "Orso",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Mountain, 2),
          Coordinate(0, 0) req (Forest, 1),
          Coordinate(-2, -1) req (Mountain, 2)
        )
      ),
      points = List(5, 11),
      imageId = "card_bear.jpg"
    ),
    AnimalCard(
      name = "Pinguino",
      habitat = Habitat(
        List(
          Coordinate(-2, -1) req (Water, 1),
          Coordinate(2, -1) req (Water, 1),
          Coordinate(0, 0) req (Mountain, 1)
        )
      ),
      points = List(4, 10, 16),
      imageId = "card_penguin.jpg"
    ),
    AnimalCard(
      name = "Pipistrello",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Forest, 3),
          Coordinate(0, 0) req (Mountain, 1)
        )
      ),
      points = List(3, 6, 10, 15),
      imageId = "card_bat.jpg"
    ),
    AnimalCard(
      name = "Leopardo delle Nevi",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Water, 1),
          Coordinate(-2, -1) req (Water, 1),
          Coordinate(0, 0) req (Mountain, 2)
        )
      ),
      points = List(5, 11),
      imageId = "card_snowleopard.jpg"
    ),
    AnimalCard(
      name = "Aquila",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Field, 1),
          Coordinate(0, 0) req (Mountain, 3)
        )
      ),
      points = List(5, 11),
      imageId = "card_eagle.jpg"
    ),
    AnimalCard(
      name = "Suricato",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Field, 1),
          Coordinate(0, 0) req (Mountain, 2)
        )
      ),
      points = List(2, 5, 9, 14),
      imageId = "card_meerkat.jpg"
    ),
    AnimalCard(
      name = "Pappagallo",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Water, 1),
          Coordinate(-2, -1) req (Water, 1),
          Coordinate(0, 0) req (Forest, 2)
        )
      ),
      points = List(4, 9, 14),
      imageId = "card_macaw.jpg"
    ),
    AnimalCard(
      name = "Cinghiale",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Building, 2),
          Coordinate(0, 0) req (Forest, 2)
        )
      ),
      points = List(4, 8, 13),
      imageId = "card_boar.jpg"
    ),
    AnimalCard(
      name = "Koala",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Forest, 1),
          Coordinate(0, 0) req (Forest, 2)
        )
      ),
      points = List(3, 6, 10, 15),
      imageId = "card_koala.jpg"
    ),
    AnimalCard(
      name = "Lupo",
      habitat = Habitat(
        List(
          Coordinate(-2, 1) req (Field, 1),
          Coordinate(-2, -1) req (Field, 1),
          Coordinate(0, 0) req (Forest, 3)
        )
      ),
      points = List(4, 10, 16),
      imageId = "card_wolf.jpg"
    ),
    AnimalCard(
      name = "Pantera",
      habitat = Habitat(
        List(
          Coordinate(-4, 2) req (Forest, 2),
          Coordinate(-2, 1) req (Forest, 2),
          Coordinate(0, 0) req (Field, 1)
        )
      ),
      points = List(5, 11),
      imageId = "card_panther.jpg"
    )
  )

  def createShuffledDeck(seed: Long = Random.nextLong()): List[AnimalCard] =
    Random(seed).shuffle(allCards)
