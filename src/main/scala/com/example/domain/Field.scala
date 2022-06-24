package com.example.domain

import scala.util.Try

enum Field:
  case NorthWest
  case North
  case NorthEast
  case West
  case Center
  case East
  case SouthWest
  case South
  case SouthEast
object Field:
  def make(value: String): Option[Field] =
    value.toIntOption.flatMap(v => Try(Field.fromOrdinal(v)).toOption)
