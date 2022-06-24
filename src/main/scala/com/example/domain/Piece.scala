package com.example.domain

import scala.util.Try

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
    Try(Piece.valueOf(piece.toUpperCase.nn)).toOption
