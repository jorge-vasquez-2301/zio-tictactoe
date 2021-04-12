package com.example.domain

sealed abstract class Field(val value: Int)
object Field {
  final case object NorthWest extends Field(1)
  final case object North     extends Field(2)
  final case object NorthEast extends Field(3)
  final case object West      extends Field(4)
  final case object Center    extends Field(5)
  final case object East      extends Field(6)
  final case object SouthWest extends Field(7)
  final case object South     extends Field(8)
  final case object SouthEast extends Field(9)

  def make(value: String): Option[Field] = value match {
    case "1" => Some(NorthWest)
    case "2" => Some(North)
    case "3" => Some(NorthEast)
    case "4" => Some(West)
    case "5" => Some(Center)
    case "6" => Some(East)
    case "7" => Some(SouthWest)
    case "8" => Some(South)
    case "9" => Some(SouthEast)
    case _   => None
  }

  val All: List[Field] = List(
    NorthWest,
    North,
    NorthEast,
    West,
    Center,
    East,
    SouthWest,
    South,
    SouthEast
  )
}
