package it.unibo.model.token

import it.unibo.model.cell.Cell
import it.unibo.model.personalBoard.{Coordinate, PersonalBoard}
import it.unibo.model.token.TerrainToken.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TokenValidatorTest extends AnyFlatSpec with Matchers:

  // Water
  "TokenValidator" should "allow placing Water on an empty cell" in {
    TokenValidator.canPlace(Water, Cell()) shouldBe true
  }
  it should "reject placing Water on a non-empty cell" in {
    val cell = Cell(List(Ground))
    TokenValidator.canPlace(Water, cell) shouldBe false
  }
  it should "reject placing any token on top of Water" in {
    val cell = Cell(List(Water))
    TerrainToken.values.foreach { token =>
      withClue(s"token=$token ") {
        TokenValidator.canPlace(token, cell) shouldBe false
      }
    }
  }

  // Field
  it should "allow placing Field on an empty cell" in {
    TokenValidator.canPlace(Field, Cell()) shouldBe true
  }
  it should "reject placing Field on a non-empty cell" in {
    val cell = Cell(List(Mountain))
    TokenValidator.canPlace(Field, cell) shouldBe false
  }
  it should "reject placing any token on top of Field" in {
    val cell = Cell(List(Field))
    TerrainToken.values.foreach { token =>
      withClue(s"token=$token ") {
        TokenValidator.canPlace(token, cell) shouldBe false
      }
    }
  }

  // Mountain
  it should "allow placing Mountain on an empty cell" in {
    TokenValidator.canPlace(Mountain, Cell()) shouldBe true
  }
  it should "allow placing Mountain on top of Mountain" in {
    val cell = Cell(List(Mountain))
    TokenValidator.canPlace(Mountain, cell) shouldBe true
  }
  it should "allow placing Mountain on a stack of two Mountains" in {
    val cell = Cell(List(Mountain, Mountain))
    TokenValidator.canPlace(Mountain, cell) shouldBe true
  }
  it should "reject placing any non-Mountain, non-Building token on top of Mountain" in {
    val cell = Cell(List(Mountain))
    TerrainToken.values.filterNot(t => t == Mountain || t == Building).foreach {
      token =>
        withClue(s"token=$token ") {
          TokenValidator.canPlace(token, cell) shouldBe false
        }
    }
  }

  // Forest
  it should "allow placing Forest on an empty cell" in {
    TokenValidator.canPlace(Forest, Cell()) shouldBe true
  }
  it should "allow placing Forest on top of a single Ground" in {
    val cell = Cell(List(Ground))
    TokenValidator.canPlace(Forest, cell) shouldBe true
  }
  it should "allow placing Forest on top of two Ground tokens" in {
    val cell = Cell(List(Ground, Ground))
    TokenValidator.canPlace(Forest, cell) shouldBe true
  }
  it should "reject placing Forest on top of any non-Ground token" in {
    TerrainToken.values.filterNot(_ == Ground).foreach { topToken =>
      val cell = Cell(List(topToken))
      withClue(s"topToken=$topToken ") {
        TokenValidator.canPlace(Forest, cell) shouldBe false
      }
    }
  }

  // Ground
  it should "allow placing Ground on an empty cell" in {
    TokenValidator.canPlace(Ground, Cell()) shouldBe true
  }
  it should "allow placing Ground on top of a single Ground" in {
    val cell = Cell(List(Ground))
    TokenValidator.canPlace(Ground, cell) shouldBe true
  }
  it should "reject placing Ground on top of two Ground tokens" in {
    val cell = Cell(List(Ground, Ground))
    TokenValidator.canPlace(Ground, cell) shouldBe false
  }
  it should "reject placing Ground on top of any non-Ground token" in {
    TerrainToken.values.filterNot(_ == Ground).foreach { topToken =>
      val cell = Cell(List(topToken))
      withClue(s"topToken=$topToken ") {
        TokenValidator.canPlace(Ground, cell) shouldBe false
      }
    }
  }

  // Building
  it should "allow placing Building on an empty cell" in {
    TokenValidator.canPlace(Building, Cell()) shouldBe true
  }
  it should "allow placing Building on top of a single Mountain" in {
    val cell = Cell(List(Mountain))
    TokenValidator.canPlace(Building, cell) shouldBe true
  }
  it should "allow placing Building on top of a single Ground" in {
    val cell = Cell(List(Ground))
    TokenValidator.canPlace(Building, cell) shouldBe true
  }
  it should "allow placing Building on top of a single Building" in {
    val cell = Cell(List(Building))
    TokenValidator.canPlace(Building, cell) shouldBe true
  }
  it should "reject placing Building on top of any token other than Mountain, Ground, or Building" in {
    TerrainToken.values
      .filterNot(t => t == Mountain || t == Ground || t == Building)
      .foreach { topToken =>
        val cell = Cell(List(topToken))
        withClue(s"topToken=$topToken ") {
          TokenValidator.canPlace(Building, cell) shouldBe false
        }
      }
  }

  // Max stack height
  it should "reject any placement when the cell already has 3 tokens" in {
    val cell = Cell(List(Mountain, Mountain, Mountain))
    TerrainToken.values.foreach { token =>
      withClue(s"token=$token ") {
        TokenValidator.canPlace(token, cell) shouldBe false
      }
    }
  }

  // Valid positions on a PersonalBoard
  it should "return only coordinates where placement is legal" in {
    val origin = Coordinate(0, 0)
    val neighbour = origin.northEastNeighbour // (2, 1)
    val farNeighbour = origin.northNeighbour // (0, 2)

    val board = PersonalBoard(
      heightBound = 2,
      widthBound = 2,
      cells = Map(
        origin -> Cell(),
        neighbour -> Cell(List(Mountain)),
        farNeighbour -> Cell(List(Water))
      )
    )
    val result = TokenValidator.validPositions(Mountain, board)
    result should contain theSameElementsAs List(origin, neighbour)
    result should not contain farNeighbour
  }

  it should "return an empty list when no cell allows the given token" in {
    val origin = Coordinate(0, 0)
    val neighbour = origin.northEastNeighbour

    val board = PersonalBoard(
      heightBound = 2,
      widthBound = 2,
      cells = Map(
        origin -> Cell(List(Water)),
        neighbour -> Cell(List(Field))
      )
    )
    TokenValidator.validPositions(Building, board) shouldBe empty
  }
