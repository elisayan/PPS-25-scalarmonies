package it.unibo.model.pouch

import it.unibo.model.Token
import scala.util.Random

object Pouches:
  opaque type Pouch = List[Token]

  object Pouch:
    def apply(tokens: List[Token], seed: Long): Pouch =
      Random(seed).shuffle(tokens)