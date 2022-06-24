package com.example

import com.example.domain.*
import zio.*

import java.io.IOException

object TicTacToe extends ZIOAppDefault:

  def run = ???

  def choosePlayerPiece: IO[IOException, Piece] = ???

  def whichPieceGoesFirst: UIO[Piece] = ???

  def programLoop(state: State): IO[IOException, Unit] = ???

  def drawBoard(board: Board): IO[IOException, Unit] =
    Console.printLine {
      Field.values
        .map(field => board.fields.get(field) -> field.value)
        .map {
          case (Some(piece), _) => piece.toString
          case (None, value)    => value.toString
        }
        .sliding(3, 3)
        .map(fields => s""" ${fields.mkString(" ║ ")} """)
        .mkString("\n═══╬═══╬═══\n")
    }

  def step(state: State.Ongoing): IO[IOException, State] =
    for
      nextMove  <- if state.isComputerTurn then getComputerMove(state.board) else getPlayerMove(state.board)
      nextState <- takeField(state, nextMove)
    yield nextState

  def getComputerMove(board: Board): IO[IOException, Field] = ???

  def getPlayerMove(board: Board): IO[IOException, Field] = ???

  def takeField(state: State.Ongoing, field: Field): IO[IOException, State] = ???

  def getGameResult(board: Board): UIO[Option[GameResult]] =
    for
      crossWin   <- isWinner(board, Piece.X)
      noughtWin  <- isWinner(board, Piece.O)
      gameResult <-
        if crossWin && noughtWin then
          ZIO.die(new IllegalStateException("It should not be possible for both players to win!"))
        else if crossWin then ZIO.succeed(GameResult.Win(Piece.X)).asSome
        else if noughtWin then ZIO.succeed(GameResult.Win(Piece.O)).asSome
        else if board.isFull then ZIO.succeed(GameResult.Draw).asSome
        else ZIO.none
    yield gameResult

  def isWinner(board: Board, piece: Piece): UIO[Boolean] =
    Board.winnerCombinations.map(combinations => combinations.exists(_ subsetOf board.fieldsOccupiedByPiece(piece)))
