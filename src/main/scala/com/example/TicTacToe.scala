package com.example

import com.example.domain._
import zio._
import zio.console._
import zio.random._

object TicTacToe extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] = ???

  def choosePlayerPiece: URIO[Console, Piece] = ???

  def whichPieceGoesFirst: URIO[Random, Piece] = ???

  def programLoop(state: State): URIO[Random with Console, Unit] = ???

  def drawBoard(board: Board): URIO[Console, Unit] =
    putStrLn {
      Field.All
        .map(field => board.fields.get(field) -> field.value)
        .map {
          case (Some(piece), _) => piece.toString
          case (None, value)    => value.toString
        }
        .sliding(3, 3)
        .map(fields => s""" ${fields.mkString(" ║ ")} """)
        .mkString("\n═══╬═══╬═══\n")
    }

  def step(state: State.Ongoing): URIO[Random with Console, State] =
    for {
      nextMove  <- if (state.isComputerTurn) getComputerMove(state.board) else getPlayerMove(state.board)
      nextState <- takeField(state, nextMove)
    } yield nextState

  def getComputerMove(board: Board): URIO[Random with Console, Field] = ???

  def getPlayerMove(board: Board): URIO[Console, Field] = ???

  def takeField(state: State.Ongoing, field: Field): URIO[Console, State] = ???

  def getGameResult(board: Board): UIO[Option[GameResult]] =
    for {
      crossWin  <- isWinner(board, Piece.X)
      noughtWin <- isWinner(board, Piece.O)
      gameResult <- if (crossWin && noughtWin)
                     ZIO.die(new IllegalStateException("It should not be possible for both players to win!"))
                   else if (crossWin) UIO.succeed(GameResult.Win(Piece.X)).asSome
                   else if (noughtWin) UIO.succeed(GameResult.Win(Piece.O)).asSome
                   else if (board.isFull) UIO.succeed(GameResult.Draw).asSome
                   else UIO.none
    } yield gameResult

  def isWinner(board: Board, piece: Piece): UIO[Boolean] =
    Board.winnerCombinations.map(combinations => combinations.exists(_ subsetOf board.fieldsOccupiedByPiece(piece)))
}
