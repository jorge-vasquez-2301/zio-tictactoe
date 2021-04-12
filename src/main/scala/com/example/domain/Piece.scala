package com.example.domain

sealed trait Piece { self =>
  val next: Piece = self match {
    case Piece.X => Piece.O
    case Piece.O => Piece.X
  }
}
object Piece {
  final case object X extends Piece
  final case object O extends Piece

  def make(piece: String): Option[Piece] =
    if (piece.toLowerCase() == "x") Some(Piece.X)
    else if (piece.toLowerCase == "o") Some(Piece.O)
    else None
}
