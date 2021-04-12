package com.example.domain

sealed trait GameResult { self =>
  def show: String = self match {
    case GameResult.Win(Piece.X) => "Cross wins!"
    case GameResult.Win(Piece.O) => "Nought wins!"
    case GameResult.Draw         => "It's a draw!"
  }
}
object GameResult {
  final case class Win(piece: Piece) extends GameResult
  final case object Draw             extends GameResult
}
