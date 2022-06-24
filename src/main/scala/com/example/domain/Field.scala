package com.example.domain

enum Field(val value: Int):
  case NorthWest extends Field(1)
  case North     extends Field(2)
  case NorthEast extends Field(3)
  case West      extends Field(4)
  case Center    extends Field(5)
  case East      extends Field(6)
  case SouthWest extends Field(7)
  case South     extends Field(8)
  case SouthEast extends Field(9)
object Field:
  def make(value: String): Option[Field] = value match
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
