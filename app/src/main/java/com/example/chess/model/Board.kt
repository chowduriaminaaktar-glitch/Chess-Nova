package com.example.chess.model

data class CastlingRights(
    val whiteKingSide: Boolean = true,
    val whiteQueenSide: Boolean = true,
    val blackKingSide: Boolean = true,
    val blackQueenSide: Boolean = true
) {
    fun toFenString(): String {
        val sb = StringBuilder()
        if (whiteKingSide) sb.append('K')
        if (whiteQueenSide) sb.append('Q')
        if (blackKingSide) sb.append('k')
        if (blackQueenSide) sb.append('q')
        return if (sb.isEmpty()) "-" else sb.toString()
    }

    companion object {
        fun fromFen(fen: String): CastlingRights {
            return CastlingRights(
                whiteKingSide = fen.contains('K'),
                whiteQueenSide = fen.contains('Q'),
                blackKingSide = fen.contains('k'),
                blackQueenSide = fen.contains('q')
            )
        }
    }
}

class Board private constructor(
    private val squares: Array<Piece?>
) {
    var turn: PieceColor = PieceColor.WHITE
    var castlingRights: CastlingRights = CastlingRights()
    var enPassantTarget: Square? = null
    var halfmoveClock: Int = 0
    var fullmoveNumber: Int = 1

    constructor() : this(Array(64) { null })

    operator fun get(square: Square): Piece? {
        if (!square.isValid()) return null
        return squares[square.index]
    }

    operator fun get(file: Int, rank: Int): Piece? {
        if (file !in 0..7 || rank !in 0..7) return null
        return squares[rank * 8 + file]
    }

    operator fun set(square: Square, piece: Piece?) {
        if (square.isValid()) {
            squares[square.index] = piece
        }
    }

    operator fun set(file: Int, rank: Int, piece: Piece?) {
        if (file in 0..7 && rank in 0..7) {
            squares[rank * 8 + file] = piece
        }
    }

    fun findKing(color: PieceColor): Square? {
        for (i in 0..63) {
            val p = squares[i]
            if (p != null && p.type == PieceType.KING && p.color == color) {
                return Square.fromIndex(i)
            }
        }
        return null
    }

    fun copy(): Board {
        val newBoard = Board(squares.copyOf())
        newBoard.turn = this.turn
        newBoard.castlingRights = this.castlingRights.copy()
        newBoard.enPassantTarget = this.enPassantTarget
        newBoard.halfmoveClock = this.halfmoveClock
        newBoard.fullmoveNumber = this.fullmoveNumber
        return newBoard
    }

    fun toFen(): String {
        val sb = StringBuilder()
        for (rank in 7 downTo 0) {
            var emptyCount = 0
            for (file in 0..7) {
                val piece = get(file, rank)
                if (piece == null) {
                    emptyCount++
                } else {
                    if (emptyCount > 0) {
                        sb.append(emptyCount)
                        emptyCount = 0
                    }
                    sb.append(piece.fenChar)
                }
            }
            if (emptyCount > 0) {
                sb.append(emptyCount)
            }
            if (rank > 0) sb.append('/')
        }

        sb.append(if (turn == PieceColor.WHITE) " w " else " b ")
        sb.append(castlingRights.toFenString())
        sb.append(" ")
        sb.append(enPassantTarget?.algebraic ?: "-")
        sb.append(" ")
        sb.append(halfmoveClock)
        sb.append(" ")
        sb.append(fullmoveNumber)

        return sb.toString()
    }

    fun toPositionKey(): String {
        // Position key for threefold repetition (board + turn + castling + enPassant)
        val sb = StringBuilder()
        for (rank in 7 downTo 0) {
            for (file in 0..7) {
                val piece = get(file, rank)
                sb.append(piece?.fenChar ?: '.')
            }
        }
        sb.append(if (turn == PieceColor.WHITE) "w" else "b")
        sb.append(castlingRights.toFenString())
        sb.append(enPassantTarget?.algebraic ?: "-")
        return sb.toString()
    }

    companion object {
        const val STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

        fun fromFen(fen: String = STARTING_FEN): Board {
            val board = Board()
            val tokens = fen.trim().split("\\s+".toRegex())
            if (tokens.isEmpty()) return board

            val piecePlacement = tokens[0]
            val ranks = piecePlacement.split('/')
            for ((rankIdx, rankStr) in ranks.withIndex()) {
                val rank = 7 - rankIdx
                var file = 0
                for (ch in rankStr) {
                    if (ch.isDigit()) {
                        file += ch.digitToInt()
                    } else {
                        val piece = Piece.fromFenChar(ch)
                        if (file in 0..7 && rank in 0..7) {
                            board[file, rank] = piece
                        }
                        file++
                    }
                }
            }

            if (tokens.size > 1) {
                board.turn = if (tokens[1] == "b") PieceColor.BLACK else PieceColor.WHITE
            }
            if (tokens.size > 2) {
                board.castlingRights = CastlingRights.fromFen(tokens[2])
            }
            if (tokens.size > 3 && tokens[3] != "-") {
                try {
                    board.enPassantTarget = Square.fromAlgebraic(tokens[3])
                } catch (_: Exception) {
                    board.enPassantTarget = null
                }
            }
            if (tokens.size > 4) {
                board.halfmoveClock = tokens[4].toIntOrNull() ?: 0
            }
            if (tokens.size > 5) {
                board.fullmoveNumber = tokens[5].toIntOrNull() ?: 1
            }

            return board
        }
    }
}
