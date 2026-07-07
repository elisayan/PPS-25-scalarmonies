package it.unibo.model.card

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import it.unibo.model.personalBoard.Coordinate
import it.unibo.model.token.TerrainToken

class AnimalCardTest extends AnyFunSuite:
  private val habitat = Habitat(
    List(
      CellRequirement(Coordinate(0, 0), TerrainToken.Water, 1)
    )
  )
  private val pointsOneCube: Int = 4
  private val pointsTwoCubes: Int = 9
  private val pointsThreeCubes: Int = 15
  private val testPoints: List[Int] =
    List(pointsOneCube, pointsTwoCubes, pointsThreeCubes)
  private val maxCubes = testPoints.size

  test(
    "Una AnimalCard appena creata deve avere 0 cubi piazzati e 0 punti correnti"
  ):
    val card = AnimalCard("Orso", habitat, testPoints)
    card.name shouldBe "Orso"
    card.maxCubes shouldBe maxCubes
    card.placedCubes shouldBe 0
    card.currentPoints shouldBe 0

  test(
    "Piazzare un cubo deve restituire una nuova istanza e aggiornare i punti"
  ):
    val initialCard = AnimalCard("Orso", habitat, testPoints)
    val card1 = initialCard.placeCube
    card1.isDefined shouldBe true
    card1.get.placedCubes shouldBe 1
    card1.get.currentPoints shouldBe pointsOneCube
    val card2 = card1.get.placeCube
    card2.get.placedCubes shouldBe 2
    card2.get.currentPoints shouldBe pointsTwoCubes

  test("L'immutabilità deve essere rispettata durante il piazzamento"):
    val initialCard = AnimalCard("Orso", habitat, testPoints)
    initialCard.placeCube
    initialCard.placedCubes shouldBe 0
    initialCard.currentPoints shouldBe 0

  test("Tentare di piazzare cubi oltre il limite massimo deve restituire None"):
    val initialCard = AnimalCard("Orso", habitat, testPoints)
    val fullCardOpt = initialCard.placeCube
      .flatMap(_.placeCube)
      .flatMap(_.placeCube)
    fullCardOpt.isDefined shouldBe true
    val fullCard = fullCardOpt.get
    fullCard.placedCubes shouldBe maxCubes
    fullCard.currentPoints shouldBe pointsThreeCubes
    val overLimitCard = fullCard.placeCube
    overLimitCard shouldBe empty
