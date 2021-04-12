package com.example.domain

import zio._

final case class Board(fields: Map[Field, Piece]) { self =>
  def fieldIsNotFree(field: Field): Boolean = self.fields.contains(field)

  def fieldsOccupiedByPiece(piece: Piece): Set[Field] =
    self.fields.collect {
      case (field, `piece`) => field
    }.toSet

  val isFull: Boolean = self.fields.size == 9

  val unoccupiedFields: List[Field] = (Field.All.toSet -- self.fields.keySet).toList.sortBy(_.value)

  def updated(field: Field, piece: Piece): Board = Board(self.fields.updated(field, piece))
}
object Board {
  val empty: Board = Board(Map.empty)

  val winnerCombinations: UIO[Set[Set[Field]]] = {
    val horizontalWins = Set(
      Set(1, 2, 3),
      Set(4, 5, 6),
      Set(7, 8, 9)
    )

    val verticalWins = Set(
      Set(1, 4, 7),
      Set(2, 5, 8),
      Set(3, 6, 9)
    )

    val diagonalWins = Set(
      Set(1, 5, 9),
      Set(3, 5, 7)
    )

    ZIO {
      (horizontalWins ++ verticalWins ++ diagonalWins).map(_.map(i => Field.make(i.toString).get))
    }.orDieWith(_ => new IllegalStateException)
  }
}
