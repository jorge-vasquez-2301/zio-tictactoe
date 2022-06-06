package com.example

import com.example.domain._
import zio._

import java.io.IOException

object TicTacToe extends ZIOAppDefault {

  def run =
    for {
      playerPiece        <- choosePlayerPiece
      pieceThatGoesFirst <- whichPieceGoesFirst.tap(piece => Console.printLine(s"$piece goes first"))
      initialState = State.Ongoing(
        Board.empty,
        if (playerPiece == Piece.X) Player.Human else Player.Computer,
        pieceThatGoesFirst
      )
      _ <- programLoop(initialState)
    } yield ()

  val choosePlayerPiece: IO[IOException, Piece] =
    for {
      input <- Console.readLine("Do you want to be X or O?: ")
      piece <- ZIO.from(Piece.make(input)) <> (Console.printLine("Invalid input") *> choosePlayerPiece)
    } yield piece

  val whichPieceGoesFirst: UIO[Piece] = Random.nextBoolean.map {
    case true  => Piece.X
    case false => Piece.O
  }

  def programLoop(state: State): IO[IOException, Unit] =
    state match {
      case state @ State.Ongoing(board, _, _) => drawBoard(board) *> step(state).flatMap(programLoop)
      case State.Over(board)                  => drawBoard(board)
    }

  def drawBoard(board: Board): IO[IOException, Unit] =
    Console.printLine {
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

  def step(state: State.Ongoing): IO[IOException, State] =
    for {
      nextMove  <- if (state.isComputerTurn) getComputerMove(state.board) else getPlayerMove(state.board)
      nextState <- takeField(state, nextMove)
    } yield nextState

  def getComputerMove(board: Board): IO[IOException, Field] =
    Random.nextIntBounded(board.unoccupiedFields.size).map(board.unoccupiedFields(_)) <*
      Console.printLine("Waiting for computer's move, press Enter to continue...") <*
      Console.readLine

  def getPlayerMove(board: Board): IO[IOException, Field] =
    for {
      input    <- Console.readLine("What's your next move? (1-9): ")
      tmpField <- ZIO.from(Field.make(input)) <> (Console.printLine("Invalid input") *> getPlayerMove(board))
      field <- if (board.fieldIsNotFree(tmpField))
                Console.printLine("That field has been already used!") *> getPlayerMove(board)
              else ZIO.succeed(tmpField)
    } yield field

  def takeField(state: State.Ongoing, field: Field): IO[IOException, State] =
    for {
      updatedBoard <- ZIO.succeed(state.board.updated(field, state.turn))
      updatedTurn  = state.turn.next
      gameResult   <- getGameResult(updatedBoard)
      nextState <- gameResult match {
                    case Some(gameResult) => Console.printLine(gameResult.show).as(State.Over(updatedBoard))
                    case None             => ZIO.succeed(state.copy(board = updatedBoard, turn = updatedTurn))
                  }
    } yield nextState

  def getGameResult(board: Board): UIO[Option[GameResult]] =
    for {
      crossWin  <- isWinner(board, Piece.X)
      noughtWin <- isWinner(board, Piece.O)
      gameResult <- if (crossWin && noughtWin)
                     ZIO.die(new IllegalStateException("It should not be possible for both players to win!"))
                   else if (crossWin) ZIO.succeed(GameResult.Win(Piece.X)).asSome
                   else if (noughtWin) ZIO.succeed(GameResult.Win(Piece.O)).asSome
                   else if (board.isFull) ZIO.succeed(GameResult.Draw).asSome
                   else ZIO.none
    } yield gameResult

  def isWinner(board: Board, piece: Piece): UIO[Boolean] =
    Board.winnerCombinations.map(combinations => combinations.exists(_ subsetOf board.fieldsOccupiedByPiece(piece)))
}
