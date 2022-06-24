package com.example.domain

enum Piece:
  self =>

  case X
  case O

  def next: Piece =
    self match
      case Piece.X => Piece.O
      case Piece.O => Piece.X
object Piece:
  def make(piece: String): Option[Piece] =
    piece.toLowerCase match
      case "x" => Some(Piece.X)
      case "o" => Some(Piece.O)
      case _   => None
