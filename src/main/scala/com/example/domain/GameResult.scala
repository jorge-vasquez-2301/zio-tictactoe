package com.example.domain

enum GameResult:
  self =>

  case Win(piece: Piece)
  case Draw

  def show: String =
    self match
      case GameResult.Win(Piece.X) => "Cross wins!"
      case GameResult.Win(Piece.O) => "Nought wins!"
      case GameResult.Draw         => "It's a draw!"
