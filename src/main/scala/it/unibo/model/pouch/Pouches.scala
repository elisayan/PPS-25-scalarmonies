package it.unibo.model.pouch

import it.unibo.model.token.TerrainToken

import scala.util.Random

object Pouches:
  private val TokensPerColor = 20
  opaque type Pouch = List[TerrainToken]

  object Pouch:
    def apply(tokens: List[TerrainToken], seed: Long = Random.nextLong()): Pouch =
      Random(seed).shuffle(tokens)
    def initialPouch(seed: Long = Random.nextLong()): Pouch =
      val fakeDistribution =
        List.concat(
          List.fill(TokensPerColor)(TerrainToken.Building),
          List.fill(TokensPerColor)(TerrainToken.Field),
          List.fill(TokensPerColor)(TerrainToken.Water)
        )
      Pouch(fakeDistribution, seed)

    extension (p: Pouch)
      def draw(amount: Int) : (List[TerrainToken], Pouch) =
        p.splitAt(amount)
      def size: Int = p.size
      def isEmpty: Boolean = p.isEmpty