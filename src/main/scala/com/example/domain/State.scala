package com.example.domain

enum State:
  self =>

  case Ongoing(board: Board, whoIsCross: Player, turn: Piece)
  case Over(board: Board)

  def isComputerTurn: Boolean =
    self match
      case State.Ongoing(_, whoIsCross, turn) =>
        (turn == Piece.X && whoIsCross == Player.Computer) || (turn == Piece.O && whoIsCross == Player.Human)
      case State.Over(_)                      => false
