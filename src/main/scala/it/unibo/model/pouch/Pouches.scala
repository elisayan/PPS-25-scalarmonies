package it.unibo.model.pouch

import it.unibo.model.token.TerrainToken

import scala.util.Random

/** Encapsulates the immutable Pouch domain logic and token distribution. */
object Pouches:
  private val MountainWaterToken = 23
  private val GroundToken = 21
  private val FieldForestToken = 19
  private val BuildingToken = 15
  opaque type Pouch = List[TerrainToken]

  object Pouch:
    /** Creates a pouch from a custom list of tokens.
      * @param tokens
      *   initial tokens.
      * @param seed
      *   random seed for deterministic testing.
      * @return
      *   a new [[Pouch]] with shuffled tokens.
      */
    def apply(
        tokens: List[TerrainToken],
        seed: Long = Random.nextLong()
    ): Pouch =
      Random(seed).shuffle(tokens)

    /** Generates the standard 120-token initial game pouch.
      * @param seed
      *   random seed for shuffling.
      * @return
      *   the starting [[Pouch]].
      */
    def initialPouch(seed: Long = Random.nextLong()): Pouch =
      val tokenDistribution =
        List.concat(
          List.fill(MountainWaterToken)(TerrainToken.Water),
          List.fill(MountainWaterToken)(TerrainToken.Mountain),
          List.fill(GroundToken)(TerrainToken.Ground),
          List.fill(FieldForestToken)(TerrainToken.Forest),
          List.fill(FieldForestToken)(TerrainToken.Field),
          List.fill(BuildingToken)(TerrainToken.Building)
        )
      Pouch(tokenDistribution, seed)

    extension (p: Pouch)
      /** Purely draws tokens from the pouch.
        * @param amount
        *   number of tokens to draw.
        * @return
        *   drawn tokens and updated [[Pouch]].
        */
      def draw(amount: Int): (List[TerrainToken], Pouch) =
        p.splitAt(amount)

      /** @return the number of tokens left in the pouch. */
      def size: Int = p.size

      /** @return true if the pouch is empty. */
      def isEmpty: Boolean = p.isEmpty
