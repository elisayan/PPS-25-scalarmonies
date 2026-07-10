package it.unibo.model.scorecalculator

object Score:

  opaque type Score = Int

  def apply(value: Int): Score =
    require(value >= 0)
    value

  val zero: Score = 0

  extension (s: Score)

    def +(other: Score): Score = other + s

    def -(other: Score): Score =
      val res = s - other
      if res < 0 then 0 else res

    def toInt: Int = s
