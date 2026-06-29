package it.unibo.model.pouch

import it.unibo.model.Token
import scala.util.Random

object Pouches:
  opaque type Pouch = List[Token]

  object Pouch:
    def apply(tokens: List[Token], seed: Long): Pouch =
      Random(seed).shuffle(tokens)
    def initialPouch(seed: Long): Pouch =
      val fakeDistribution =
        List.concat(
          List.fill(20)(Token.Red),
          List.fill(20)(Token.Blue),
          List.fill(20)(Token.Gray)
        )
      Pouch(fakeDistribution, seed)

    extension (p: Pouch)
      def draw(amount: Int) : (List[Token], Pouch) =
        p.splitAt(amount)
      def size: Int = p.size
      def isEmpty: Boolean = p.isEmpty