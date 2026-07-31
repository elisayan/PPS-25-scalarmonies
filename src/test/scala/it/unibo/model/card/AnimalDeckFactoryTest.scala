package it.unibo.model.card

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import it.unibo.model.personalboard.Coordinate

class AnimalDeckFactoryTest extends AnyFunSuite:
  val deck: List[AnimalCard] = AnimalDeckFactory.createShuffledDeck()
  test("createShuffledDeck deve generare un mazzo completo di 32 carte"):
    deck should have size 32

  test("Ogni carta deve possedere un requisito bersaglio in Coordinate(0,0)"):
    deck.foreach { card =>
      card.habitat.requirements.map(_.offset) should contain(Coordinate(0, 0))
    }

  test("Le carte devono contenere almeno un punteggio nella lista points"):
    deck.foreach { card =>
      card.points should not be empty
    }

  test(
    "createShuffledDeck deve mescolare l'ordine delle carte mantenendo gli stessi elementi"
  ):
    val deck2 = AnimalDeckFactory.createShuffledDeck()
    deck should not equal deck2
    deck should contain theSameElementsAs deck2

  test("createShuffledDeck invocato con lo stesso seme deve garantire riproducibilità (Trasparenza Referenziale)"):
    val deterministicDeck1 = AnimalDeckFactory.createShuffledDeck(seed = 42L)
    val deterministicDeck2 = AnimalDeckFactory.createShuffledDeck(seed = 42L)
    deterministicDeck1 shouldEqual deterministicDeck2