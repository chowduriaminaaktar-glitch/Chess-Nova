package com.example.chess.engine

import com.example.chess.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

enum class EngineDifficulty(val depth: Int, val elo: Int) {
    EASY(depth = 1, elo = 900),
    MEDIUM(depth = 2, elo = 1450),
    HARD(depth = 3, elo = 1850)
}

object ChessEngine {

    // Piece-Square Tables (White perspective; flipped for Black)
    private val PAWN_PST = intArrayOf(
        0,  0,  0,  0,  0,  0,  0,  0,
        50, 50, 50, 50, 50, 50, 50, 50,
        10, 10, 20, 30, 30, 20, 10, 10,
        5,  5, 10, 25, 25, 10,  5,  5,
        0,  0,  0, 20, 20,  0,  0,  0,
        5, -5,-10,  0,  0,-10, -5,  5,
        5, 10, 10,-20,-20, 10, 10,  5,
        0,  0,  0,  0,  0,  0,  0,  0
    )

    private val KNIGHT_PST = intArrayOf(
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50
    )

    private val BISHOP_PST = intArrayOf(
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20
    )

    private val ROOK_PST = intArrayOf(
        0,  0,  0,  0,  0,  0,  0,  0,
        5, 10, 10, 10, 10, 10, 10,  5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        0,  0,  0,  5,  5,  0,  0,  0
    )

    private val QUEEN_PST = intArrayOf(
        -20,-10,-10, -5, -5,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5,  5,  5,  5,  0,-10,
        -5,  0,  5,  5,  5,  5,  0, -5,
        0,  0,  5,  5,  5,  5,  0, -5,
        -10,  5,  5,  5,  5,  5,  0,-10,
        -10,  0,  5,  0,  0,  0,  0,-10,
        -20,-10,-10, -5, -5,-10,-10,-20
    )

    private val KING_MIDDLE_PST = intArrayOf(
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -20,-30,-30,-40,-40,-30,-30,-20,
        -10,-20,-20,-20,-20,-20,-20,-10,
        20, 20,  0,  0,  0,  0, 20, 20,
        20, 30, 10,  0,  0, 10, 30, 20
    )

    suspend fun searchBestMove(game: ChessGame, difficulty: EngineDifficulty): Move? = withContext(Dispatchers.Default) {
        val legalMoves = game.getLegalMoves()
        if (legalMoves.isEmpty()) return@withContext null

        val currentTurn = game.board.turn
        val isMaximizing = (currentTurn == PieceColor.WHITE)

        val orderedMoves = orderMoves(game.board, legalMoves)

        if (difficulty == EngineDifficulty.EASY) {
            // Evaluates depth 1, adds slight random fluctuation
            var bestScore = if (isMaximizing) -1000000 else 1000000
            val moveScores = mutableListOf<Pair<Move, Int>>()

            for (move in orderedMoves) {
                val nextBoard = game.board.copy()
                ChessGame.applyMoveToBoard(nextBoard, move)
                var score = evaluateBoard(nextBoard)
                // Add noise for novice feel
                score += Random.nextInt(-35, 35)
                moveScores.add(move to score)
                if (isMaximizing && score > bestScore) bestScore = score
                if (!isMaximizing && score < bestScore) bestScore = score
            }

            // Pick the best or 2nd best move
            moveScores.sortByDescending { if (isMaximizing) it.second else -it.second }
            val topCandidates = moveScores.take(min(3, moveScores.size))
            return@withContext topCandidates.random().first
        }

        var bestMove = orderedMoves.first()
        var alpha = -1000000
        var beta = 1000000
        var bestScore = if (isMaximizing) -1000000 else 1000000

        for (move in orderedMoves) {
            val nextBoard = game.board.copy()
            ChessGame.applyMoveToBoard(nextBoard, move)

            val score = minimax(
                board = nextBoard,
                depth = difficulty.depth - 1,
                alpha = alpha,
                beta = beta,
                isMaximizing = !isMaximizing,
                useQuiescence = (difficulty == EngineDifficulty.HARD)
            )

            if (isMaximizing) {
                if (score > bestScore) {
                    bestScore = score
                    bestMove = move
                }
                alpha = max(alpha, score)
            } else {
                if (score < bestScore) {
                    bestScore = score
                    bestMove = move
                }
                beta = min(beta, score)
            }

            if (beta <= alpha) break
        }

        return@withContext bestMove
    }

    private fun minimax(
        board: Board,
        depth: Int,
        alpha: Int,
        beta: Int,
        isMaximizing: Boolean,
        useQuiescence: Boolean
    ): Int {
        var currentAlpha = alpha
        var currentBeta = beta

        val movingColor = if (isMaximizing) PieceColor.WHITE else PieceColor.BLACK
        val pseudoMoves = ChessGame.generatePseudoLegalMoves(board, movingColor)
        val legalMoves = mutableListOf<Move>()
        for (m in pseudoMoves) {
            val testBoard = board.copy()
            ChessGame.applyMoveToBoard(testBoard, m)
            if (!ChessGame.isInCheck(movingColor, testBoard)) {
                legalMoves.add(m)
            }
        }

        if (legalMoves.isEmpty()) {
            return if (ChessGame.isInCheck(movingColor, board)) {
                if (isMaximizing) -100000 + (3 - depth) * 100 else 100000 - (3 - depth) * 100
            } else {
                0 // Stalemate
            }
        }

        if (depth <= 0) {
            return if (useQuiescence) {
                quiescence(board, currentAlpha, currentBeta, isMaximizing, 2)
            } else {
                evaluateBoard(board)
            }
        }

        val orderedMoves = orderMoves(board, legalMoves)

        if (isMaximizing) {
            var maxEval = -1000000
            for (move in orderedMoves) {
                val nextBoard = board.copy()
                ChessGame.applyMoveToBoard(nextBoard, move)
                val eval = minimax(nextBoard, depth - 1, currentAlpha, currentBeta, false, useQuiescence)
                maxEval = max(maxEval, eval)
                currentAlpha = max(currentAlpha, eval)
                if (currentBeta <= currentAlpha) break
            }
            return maxEval
        } else {
            var minEval = 1000000
            for (move in orderedMoves) {
                val nextBoard = board.copy()
                ChessGame.applyMoveToBoard(nextBoard, move)
                val eval = minimax(nextBoard, depth - 1, currentAlpha, currentBeta, true, useQuiescence)
                minEval = min(minEval, eval)
                currentBeta = min(currentBeta, eval)
                if (currentBeta <= currentAlpha) break
            }
            return minEval
        }
    }

    private fun quiescence(
        board: Board,
        alpha: Int,
        beta: Int,
        isMaximizing: Boolean,
        qDepth: Int
    ): Int {
        var currentAlpha = alpha
        var currentBeta = beta
        val standPat = evaluateBoard(board)

        if (qDepth <= 0) return standPat

        if (isMaximizing) {
            if (standPat >= currentBeta) return currentBeta
            if (standPat > currentAlpha) currentAlpha = standPat

            val captureMoves = getLegalCaptures(board, PieceColor.WHITE)
            val orderedCaptures = orderMoves(board, captureMoves)

            for (move in orderedCaptures) {
                val nextBoard = board.copy()
                ChessGame.applyMoveToBoard(nextBoard, move)
                val score = quiescence(nextBoard, currentAlpha, currentBeta, false, qDepth - 1)
                if (score >= currentBeta) return currentBeta
                if (score > currentAlpha) currentAlpha = score
            }
            return currentAlpha
        } else {
            if (standPat <= currentAlpha) return currentAlpha
            if (standPat < currentBeta) currentBeta = standPat

            val captureMoves = getLegalCaptures(board, PieceColor.BLACK)
            val orderedCaptures = orderMoves(board, captureMoves)

            for (move in orderedCaptures) {
                val nextBoard = board.copy()
                ChessGame.applyMoveToBoard(nextBoard, move)
                val score = quiescence(nextBoard, currentAlpha, currentBeta, true, qDepth - 1)
                if (score <= currentAlpha) return currentAlpha
                if (score < currentBeta) currentBeta = score
            }
            return currentBeta
        }
    }

    private fun getLegalCaptures(board: Board, color: PieceColor): List<Move> {
        val pseudoMoves = ChessGame.generatePseudoLegalMoves(board, color)
        val captures = mutableListOf<Move>()
        for (m in pseudoMoves) {
            if (m.isCapture || m.promotionPiece != null) {
                val test = board.copy()
                ChessGame.applyMoveToBoard(test, m)
                if (!ChessGame.isInCheck(color, test)) {
                    captures.add(m)
                }
            }
        }
        return captures
    }

    private fun orderMoves(board: Board, moves: List<Move>): List<Move> {
        return moves.sortedByDescending { move ->
            var score = 0
            val target = board[move.to]
            val attacker = board[move.from]

            if (move.isCapture && target != null && attacker != null) {
                // MVV-LVA: Most Valuable Victim, Least Valuable Attacker
                score += (target.type.baseValue * 10) - (attacker.type.baseValue)
            }
            if (move.promotionPiece != null) {
                score += move.promotionPiece.baseValue
            }
            if (move.isCastling) {
                score += 50
            }
            score
        }
    }

    fun evaluateBoard(board: Board): Int {
        var score = 0

        for (rank in 0..7) {
            for (file in 0..7) {
                val piece = board[file, rank] ?: continue
                val isWhite = (piece.color == PieceColor.WHITE)
                val sign = if (isWhite) 1 else -1

                // Material base
                score += sign * piece.type.baseValue

                // Positional score from table
                // For white, square 0 is a1 (bottom left). For PST indexing, row 0 is rank 8 (top).
                val pstIndex = if (isWhite) {
                    (7 - rank) * 8 + file
                } else {
                    rank * 8 + file
                }

                val pstBonus = when (piece.type) {
                    PieceType.PAWN -> PAWN_PST[pstIndex]
                    PieceType.KNIGHT -> KNIGHT_PST[pstIndex]
                    PieceType.BISHOP -> BISHOP_PST[pstIndex]
                    PieceType.ROOK -> ROOK_PST[pstIndex]
                    PieceType.QUEEN -> QUEEN_PST[pstIndex]
                    PieceType.KING -> KING_MIDDLE_PST[pstIndex]
                }
                score += sign * pstBonus
            }
        }

        return score
    }
}
