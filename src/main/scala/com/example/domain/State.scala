package com.example.domain

sealed trait State { self =>
  def isComputerTurn: Boolean = self match {
    case State.Ongoing(_, whoIsCross, turn) =>
      (turn == Piece.X && whoIsCross == Player.Computer) || (turn == Piece.O && whoIsCross == Player.Human)
    case State.Over(_) => false
  }
}
object State {
  final case class Ongoing(board: Board, whoIsCross: Player, turn: Piece) extends State
  final case class Over(board: Board)                                     extends State
}
