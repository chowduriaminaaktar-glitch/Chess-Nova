package com.example.chess

import com.example.chess.engine.ChessEngine
import com.example.chess.engine.EngineDifficulty
import com.example.chess.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ChessRulesAndEngineTest {

    @Test
    fun startingPositionHasTwentyLegalMoves() {
        val game = ChessGame()
        val legalMoves = game.getLegalMoves()
        // 8 pawns * 2 moves (single and double step) = 16
        // 2 knights * 2 moves = 4
        // Total = 20 legal moves
        assertEquals(20, legalMoves.size)
    }

    @Test
    fun testPawnDoublePushAndEnPassant() {
        val game = ChessGame("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1")
        // White plays d4
        val d4 = Move(Square.fromAlgebraic("d2"), Square.fromAlgebraic("d4"))
        assertTrue(game.makeMove(d4))
        assertEquals(Square.fromAlgebraic("d3"), game.board.enPassantTarget)

        // Black plays exd4 (normal capture)
        val exd4 = Move(Square.fromAlgebraic("e5"), Square.fromAlgebraic("d4"), isCapture = true)
        assertTrue(game.makeMove(exd4))

        // Check en passant scenario from FEN
        val epGame = ChessGame("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3")
        val epMove = Move(
            from = Square.fromAlgebraic("e5"),
            to = Square.fromAlgebraic("d6"),
            isCapture = true,
            isEnPassant = true
        )
        assertTrue(epGame.makeMove(epMove))
        // Pawn on d5 should be captured and empty
        assertNull(epGame.board[Square.fromAlgebraic("d5")])
        assertEquals(Piece(PieceType.PAWN, PieceColor.WHITE), epGame.board[Square.fromAlgebraic("d6")])
    }

    @Test
    fun testCastlingKingsideAndQueenside() {
        // Setup position where White can castle Kingside
        val fen = "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1"
        val game = ChessGame(fen)
        val moves = game.getLegalMoves()

        val whiteOO = moves.firstOrNull { it.isCastling && it.to == Square.fromAlgebraic("g1") }
        val whiteOOO = moves.firstOrNull { it.isCastling && it.to == Square.fromAlgebraic("c1") }
        assertNotNull("Kingside castling should be legal", whiteOO)
        assertNotNull("Queenside castling should be legal", whiteOOO)

        // Execute Kingside castling
        assertTrue(game.makeMove(whiteOO!!))
        assertEquals(Piece(PieceType.KING, PieceColor.WHITE), game.board[Square.fromAlgebraic("g1")])
        assertEquals(Piece(PieceType.ROOK, PieceColor.WHITE), game.board[Square.fromAlgebraic("f1")])
        assertNull(game.board[Square.fromAlgebraic("e1")])
        assertNull(game.board[Square.fromAlgebraic("h1")])
    }

    @Test
    fun testCastlingBlockedWhenInCheck() {
        // White king on e1 is checked by black rook on e8
        val fen = "4r3/8/8/8/8/8/8/R3K2R w KQ - 0 1"
        val game = ChessGame(fen)
        val moves = game.getLegalMoves()
        val castlingMoves = moves.filter { it.isCastling }
        assertTrue("Cannot castle while in check", castlingMoves.isEmpty())
    }

    @Test
    fun testPawnPromotion() {
        val fen = "3k4/4P3/8/8/8/8/8/4K3 w - - 0 1"
        val game = ChessGame(fen)
        val moves = game.getLegalMoves()

        // Should have 4 promotion options from e7 to e8 (Q, R, B, N) + King moves
        val promoMoves = moves.filter { it.from == Square.fromAlgebraic("e7") && it.to == Square.fromAlgebraic("e8") }
        assertEquals(4, promoMoves.size)

        val queenPromo = promoMoves.first { it.promotionPiece == PieceType.QUEEN }
        assertTrue(game.makeMove(queenPromo))
        assertEquals(Piece(PieceType.QUEEN, PieceColor.WHITE), game.board[Square.fromAlgebraic("e8")])
    }

    @Test
    fun testCheckmateDetectionFoolsMate() {
        // Fool's mate: 1. f3 e5 2. g4 Qh4#
        val game = ChessGame()
        assertTrue(game.makeMove(Move(Square.fromAlgebraic("f2"), Square.fromAlgebraic("f3"))))
        assertTrue(game.makeMove(Move(Square.fromAlgebraic("e7"), Square.fromAlgebraic("e5"))))
        assertTrue(game.makeMove(Move(Square.fromAlgebraic("g2"), Square.fromAlgebraic("g4"))))
        assertTrue(game.makeMove(Move(Square.fromAlgebraic("d8"), Square.fromAlgebraic("h4"))))

        assertEquals(GameStatus.CHECKMATE, game.status)
        assertTrue(game.isGameOver())
    }

    @Test
    fun testStalemateDetection() {
        // Classic stalemate position: Black king on a8, White queen on c7, White king on b6
        val fen = "k7/2Q5/1K6/8/8/8/8/8 b - - 0 1"
        val game = ChessGame(fen)
        assertEquals(GameStatus.STALEMATE, game.status)
        assertTrue(game.isGameOver())
    }

    @Test
    fun testInsufficientMaterialDraw() {
        // King vs King
        val kvk = ChessGame("4k3/8/8/8/8/8/8/4K3 w - - 0 1")
        assertEquals(GameStatus.DRAW_INSUFFICIENT_MATERIAL, kvk.status)

        // King and Knight vs King
        val kvkn = ChessGame("4k3/8/8/8/8/8/8/4KN2 w - - 0 1")
        assertEquals(GameStatus.DRAW_INSUFFICIENT_MATERIAL, kvkn.status)
    }

    @Test
    fun testChessEngineFindsCheckmateOrDefense() = runBlocking {
        // Scholar's mate delivery: White Queen on f3 can play Qxf7#
        val fen = "r1bqkb1r/pppp1ppp/2n5/4p3/2B1n3/5Q2/PPPP1PPP/RNB1K1NR w KQkq - 0 1"
        val game = ChessGame(fen)

        val bestMove = ChessEngine.searchBestMove(game, EngineDifficulty.HARD)
        assertNotNull(bestMove)
        assertEquals(Square.fromAlgebraic("f3"), bestMove!!.from)
        assertEquals(Square.fromAlgebraic("f7"), bestMove.to)
    }

    @Test
    fun testUndoMoveRevertsPositionAndCaptures() {
        val game = ChessGame()
        val e4 = Move(Square.fromAlgebraic("e2"), Square.fromAlgebraic("e4"))
        assertTrue(game.makeMove(e4))
        assertEquals(1, game.history.size)
        assertEquals(PieceColor.BLACK, game.board.turn)

        val d5 = Move(Square.fromAlgebraic("d7"), Square.fromAlgebraic("d5"))
        assertTrue(game.makeMove(d5))
        assertEquals(2, game.history.size)

        val exd5 = Move(Square.fromAlgebraic("e4"), Square.fromAlgebraic("d5"), isCapture = true)
        assertTrue(game.makeMove(exd5))
        assertEquals(1, game.capturedByWhite.size)

        // Undo capture
        assertTrue(game.undoMove())
        assertEquals(2, game.history.size)
        assertEquals(0, game.capturedByWhite.size)
        assertEquals(PieceColor.WHITE, game.board.turn)
        assertEquals(Piece(PieceType.PAWN, PieceColor.WHITE), game.board[Square.fromAlgebraic("e4")])
        assertEquals(Piece(PieceType.PAWN, PieceColor.BLACK), game.board[Square.fromAlgebraic("d5")])

        // Undo d5
        assertTrue(game.undoMove())
        assertEquals(1, game.history.size)
        assertEquals(PieceColor.BLACK, game.board.turn)

        // Undo e4
        assertTrue(game.undoMove())
        assertEquals(0, game.history.size)
        assertEquals(PieceColor.WHITE, game.board.turn)
        assertEquals(Piece(PieceType.PAWN, PieceColor.WHITE), game.board[Square.fromAlgebraic("e2")])
        assertNull(game.board[Square.fromAlgebraic("e4")])
    }
}
