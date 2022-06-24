package com.example.domain

import zio.*

final case class Board(fields: Map[Field, Piece]):
  self =>

  def fieldIsNotFree(field: Field): Boolean = self.fields.contains(field)

  def fieldsOccupiedByPiece(piece: Piece): Set[Field] =
    self.fields.collect { case (field, `piece`) =>
      field
    }.toSet

  val isFull: Boolean = self.fields.size == 9

  val unoccupiedFields: List[Field] = (Field.values.toSet -- self.fields.keySet).toList.sortBy(_.ordinal)

  def updated(field: Field, piece: Piece): Board = Board(self.fields.updated(field, piece))
object Board:
  val empty: Board = Board(Map.empty)

  val winnerCombinations: UIO[Set[Set[Field]]] =
    val horizontalWins = Set(
      Set(0, 1, 2),
      Set(3, 4, 5),
      Set(6, 7, 8)
    )

    val verticalWins = Set(
      Set(0, 3, 6),
      Set(1, 4, 7),
      Set(2, 5, 8)
    )

    val diagonalWins = Set(
      Set(0, 4, 8),
      Set(2, 4, 6)
    )

    ZIO.attempt {
      (horizontalWins ++ verticalWins ++ diagonalWins).map(_.map(i => Field.make(i.toString).get))
    }.orDieWith(_ => new IllegalStateException)
