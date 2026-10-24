package com.example.chess.model

enum class GameStatus {
    ONGOING,
    CHECK,
    CHECKMATE,
    STALEMATE,
    DRAW_INSUFFICIENT_MATERIAL,
    DRAW_FIFTY_MOVES,
    DRAW_THREEFOLD_REPETITION,
    RESIGNATION
}

data class MoveHistoryEntry(
    val move: Move,
    val boardSnapshot: Board,
    val capturedPiece: Piece?
)

class ChessGame(startingFen: String = Board.STARTING_FEN) {
    var board: Board = Board.fromFen(startingFen)
        private set

    val history = mutableListOf<MoveHistoryEntry>()
    val capturedByWhite = mutableListOf<Piece>()
    val capturedByBlack = mutableListOf<Piece>()
    private val positionHistory = mutableMapOf<String, Int>()

    var status: GameStatus = GameStatus.ONGOING
        private set

    var resigningColor: PieceColor? = null
        private set

    init {
        recordPosition()
        updateGameStatus()
    }

    fun reset(fen: String = Board.STARTING_FEN) {
        board = Board.fromFen(fen)
        history.clear()
        capturedByWhite.clear()
        capturedByBlack.clear()
        positionHistory.clear()
        resigningColor = null
        recordPosition()
        updateGameStatus()
    }

    fun getLegalMoves(): List<Move> {
        val pseudolegal = generatePseudoLegalMoves(board, board.turn)
        val legalMoves = mutableListOf<Move>()

        for (m in pseudolegal) {
            val copy = board.copy()
            applyMoveToBoard(copy, m)
            if (!isInCheck(board.turn, copy)) {
                // Annotate SAN
                m.san = formatSan(board, m)
                legalMoves.add(m)
            }
        }
        return legalMoves
    }

    fun getLegalMovesFrom(square: Square): List<Move> {
        return getLegalMoves().filter { it.from == square }
    }

    fun makeMove(move: Move): Boolean {
        if (status != GameStatus.ONGOING && status != GameStatus.CHECK) return false

        val legalMoves = getLegalMoves()
        val matchingMove = legalMoves.firstOrNull {
            it.from == move.from && it.to == move.to &&
                    (it.promotionPiece == null || it.promotionPiece == move.promotionPiece)
        } ?: return false

        // Determine captured piece
        val capturedPiece: Piece? = if (matchingMove.isEnPassant) {
            board[matchingMove.to.file, matchingMove.from.rank]
        } else {
            board[matchingMove.to]
        }

        val snapshot = board.copy()
        applyMoveToBoard(board, matchingMove)

        if (capturedPiece != null) {
            if (snapshot.turn == PieceColor.WHITE) {
                capturedByWhite.add(capturedPiece)
            } else {
                capturedByBlack.add(capturedPiece)
            }
        }

        history.add(MoveHistoryEntry(matchingMove, snapshot, capturedPiece))
        recordPosition()
        updateGameStatus()

        // Append check or checkmate symbol to SAN
        if (status == GameStatus.CHECKMATE) {
            if (!matchingMove.san.endsWith("#")) {
                matchingMove.san = matchingMove.san.trimEnd('+') + "#"
            }
        } else if (status == GameStatus.CHECK) {
            if (!matchingMove.san.endsWith("+") && !matchingMove.san.endsWith("#")) {
                matchingMove.san += "+"
            }
        }

        return true
    }

    fun undoMove(): Boolean {
        if (history.isEmpty()) return false
        val last = history.removeAt(history.lastIndex)
        val posKey = board.toPositionKey()
        positionHistory[posKey] = (positionHistory[posKey] ?: 1) - 1
        if (positionHistory[posKey] == 0) positionHistory.remove(posKey)

        if (last.capturedPiece != null) {
            if (last.boardSnapshot.turn == PieceColor.WHITE) {
                capturedByWhite.removeAt(capturedByWhite.lastIndex)
            } else {
                capturedByBlack.removeAt(capturedByBlack.lastIndex)
            }
        }

        board = last.boardSnapshot
        resigningColor = null
        updateGameStatus()
        return true
    }

    fun resign(color: PieceColor) {
        resigningColor = color
        status = GameStatus.RESIGNATION
    }

    fun isGameOver(): Boolean {
        return status in listOf(
            GameStatus.CHECKMATE,
            GameStatus.STALEMATE,
            GameStatus.DRAW_INSUFFICIENT_MATERIAL,
            GameStatus.DRAW_FIFTY_MOVES,
            GameStatus.DRAW_THREEFOLD_REPETITION,
            GameStatus.RESIGNATION
        )
    }

    private fun recordPosition() {
        val key = board.toPositionKey()
        positionHistory[key] = (positionHistory[key] ?: 0) + 1
    }

    private fun updateGameStatus() {
        if (resigningColor != null) {
            status = GameStatus.RESIGNATION
            return
        }

        val inCheck = isInCheck(board.turn, board)
        val legalMoves = getLegalMoves()

        if (legalMoves.isEmpty()) {
            status = if (inCheck) GameStatus.CHECKMATE else GameStatus.STALEMATE
            return
        }

        if (board.halfmoveClock >= 100) {
            status = GameStatus.DRAW_FIFTY_MOVES
            return
        }

        val key = board.toPositionKey()
        if ((positionHistory[key] ?: 0) >= 3) {
            status = GameStatus.DRAW_THREEFOLD_REPETITION
            return
        }

        if (isInsufficientMaterial(board)) {
            status = GameStatus.DRAW_INSUFFICIENT_MATERIAL
            return
        }

        status = if (inCheck) GameStatus.CHECK else GameStatus.ONGOING
    }

    companion object {
        fun isInCheck(color: PieceColor, board: Board): Boolean {
            val kingSq = board.findKing(color) ?: return false
            return isSquareAttacked(kingSq, color.opposite, board)
        }

        fun isSquareAttacked(square: Square, byColor: PieceColor, board: Board): Boolean {
            // Pawns
            val pawnRank = if (byColor == PieceColor.WHITE) square.rank - 1 else square.rank + 1
            if (pawnRank in 0..7) {
                if (square.file > 0) {
                    val p = board[square.file - 1, pawnRank]
                    if (p != null && p.color == byColor && p.type == PieceType.PAWN) return true
                }
                if (square.file < 7) {
                    val p = board[square.file + 1, pawnRank]
                    if (p != null && p.color == byColor && p.type == PieceType.PAWN) return true
                }
            }

            // Knights
            val knightOffsets = arrayOf(
                1 to 2, 2 to 1, -1 to 2, -2 to 1,
                1 to -2, 2 to -1, -1 to -2, -2 to -1
            )
            for ((df, dr) in knightOffsets) {
                val f = square.file + df
                val r = square.rank + dr
                val p = board[f, r]
                if (p != null && p.color == byColor && p.type == PieceType.KNIGHT) return true
            }

            // King (adjacent)
            for (df in -1..1) {
                for (dr in -1..1) {
                    if (df == 0 && dr == 0) continue
                    val p = board[square.file + df, square.rank + dr]
                    if (p != null && p.color == byColor && p.type == PieceType.KING) return true
                }
            }

            // Orthogonal rays (Rook, Queen)
            val orthoDirs = arrayOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
            for ((df, dr) in orthoDirs) {
                var f = square.file + df
                var r = square.rank + dr
                while (f in 0..7 && r in 0..7) {
                    val p = board[f, r]
                    if (p != null) {
                        if (p.color == byColor && (p.type == PieceType.ROOK || p.type == PieceType.QUEEN)) {
                            return true
                        }
                        break
                    }
                    f += df
                    r += dr
                }
            }

            // Diagonal rays (Bishop, Queen)
            val diagDirs = arrayOf(1 to 1, 1 to -1, -1 to 1, -1 to -1)
            for ((df, dr) in diagDirs) {
                var f = square.file + df
                var r = square.rank + dr
                while (f in 0..7 && r in 0..7) {
                    val p = board[f, r]
                    if (p != null) {
                        if (p.color == byColor && (p.type == PieceType.BISHOP || p.type == PieceType.QUEEN)) {
                            return true
                        }
                        break
                    }
                    f += df
                    r += dr
                }
            }

            return false
        }

        fun generatePseudoLegalMoves(board: Board, color: PieceColor): List<Move> {
            val moves = mutableListOf<Move>()
            for (r in 0..7) {
                for (f in 0..7) {
                    val piece = board[f, r] ?: continue
                    if (piece.color != color) continue
                    val from = Square(f, r)

                    when (piece.type) {
                        PieceType.PAWN -> generatePawnMoves(board, from, color, moves)
                        PieceType.KNIGHT -> generateKnightMoves(board, from, color, moves)
                        PieceType.BISHOP -> generateSlidingMoves(board, from, color, arrayOf(1 to 1, 1 to -1, -1 to 1, -1 to -1), moves)
                        PieceType.ROOK -> generateSlidingMoves(board, from, color, arrayOf(0 to 1, 0 to -1, 1 to 0, -1 to 0), moves)
                        PieceType.QUEEN -> generateSlidingMoves(board, from, color, arrayOf(1 to 1, 1 to -1, -1 to 1, -1 to -1, 0 to 1, 0 to -1, 1 to 0, -1 to 0), moves)
                        PieceType.KING -> generateKingMoves(board, from, color, moves)
                    }
                }
            }
            return moves
        }

        private fun generatePawnMoves(board: Board, from: Square, color: PieceColor, moves: MutableList<Move>) {
            val forward = if (color == PieceColor.WHITE) 1 else -1
            val startRank = if (color == PieceColor.WHITE) 1 else 6
            val promoRank = if (color == PieceColor.WHITE) 7 else 0

            // 1 square forward
            val oneStepRank = from.rank + forward
            if (oneStepRank in 0..7 && board[from.file, oneStepRank] == null) {
                val to = Square(from.file, oneStepRank)
                if (oneStepRank == promoRank) {
                    addPromotions(from, to, isCapture = false, moves)
                } else {
                    moves.add(Move(from, to))
                    // 2 squares forward from starting rank
                    val twoStepRank = from.rank + 2 * forward
                    if (from.rank == startRank && board[from.file, twoStepRank] == null) {
                        moves.add(Move(from, Square(from.file, twoStepRank)))
                    }
                }
            }

            // Diagonal captures
            for (df in arrayOf(-1, 1)) {
                val capFile = from.file + df
                if (capFile in 0..7 && oneStepRank in 0..7) {
                    val to = Square(capFile, oneStepRank)
                    val targetPiece = board[to]
                    if (targetPiece != null && targetPiece.color != color) {
                        if (oneStepRank == promoRank) {
                            addPromotions(from, to, isCapture = true, moves)
                        } else {
                            moves.add(Move(from, to, isCapture = true))
                        }
                    } else if (board.enPassantTarget == to) {
                        moves.add(Move(from, to, isCapture = true, isEnPassant = true))
                    }
                }
            }
        }

        private fun addPromotions(from: Square, to: Square, isCapture: Boolean, moves: MutableList<Move>) {
            for (type in arrayOf(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT)) {
                moves.add(Move(from, to, promotionPiece = type, isCapture = isCapture))
            }
        }

        private fun generateKnightMoves(board: Board, from: Square, color: PieceColor, moves: MutableList<Move>) {
            val offsets = arrayOf(
                1 to 2, 2 to 1, -1 to 2, -2 to 1,
                1 to -2, 2 to -1, -1 to -2, -2 to -1
            )
            for ((df, dr) in offsets) {
                val f = from.file + df
                val r = from.rank + dr
                if (f in 0..7 && r in 0..7) {
                    val target = board[f, r]
                    if (target == null) {
                        moves.add(Move(from, Square(f, r), isCapture = false))
                    } else if (target.color != color) {
                        moves.add(Move(from, Square(f, r), isCapture = true))
                    }
                }
            }
        }

        private fun generateSlidingMoves(board: Board, from: Square, color: PieceColor, dirs: Array<Pair<Int, Int>>, moves: MutableList<Move>) {
            for ((df, dr) in dirs) {
                var f = from.file + df
                var r = from.rank + dr
                while (f in 0..7 && r in 0..7) {
                    val target = board[f, r]
                    if (target == null) {
                        moves.add(Move(from, Square(f, r), isCapture = false))
                    } else {
                        if (target.color != color) {
                            moves.add(Move(from, Square(f, r), isCapture = true))
                        }
                        break
                    }
                    f += df
                    r += dr
                }
            }
        }

        private fun generateKingMoves(board: Board, from: Square, color: PieceColor, moves: MutableList<Move>) {
            for (df in -1..1) {
                for (dr in -1..1) {
                    if (df == 0 && dr == 0) continue
                    val f = from.file + df
                    val r = from.rank + dr
                    if (f in 0..7 && r in 0..7) {
                        val target = board[f, r]
                        if (target == null) {
                            moves.add(Move(from, Square(f, r), isCapture = false))
                        } else if (target.color != color) {
                            moves.add(Move(from, Square(f, r), isCapture = true))
                        }
                    }
                }
            }

            // Castling
            if (color == PieceColor.WHITE && from == Square(4, 0)) {
                // White Kingside: e1 -> g1
                if (board.castlingRights.whiteKingSide &&
                    board[5, 0] == null && board[6, 0] == null &&
                    board[7, 0]?.type == PieceType.ROOK && board[7, 0]?.color == PieceColor.WHITE &&
                    !isSquareAttacked(Square(4, 0), PieceColor.BLACK, board) &&
                    !isSquareAttacked(Square(5, 0), PieceColor.BLACK, board) &&
                    !isSquareAttacked(Square(6, 0), PieceColor.BLACK, board)
                ) {
                    moves.add(Move(from, Square(6, 0), isCastling = true))
                }
                // White Queenside: e1 -> c1
                if (board.castlingRights.whiteQueenSide &&
                    board[1, 0] == null && board[2, 0] == null && board[3, 0] == null &&
                    board[0, 0]?.type == PieceType.ROOK && board[0, 0]?.color == PieceColor.WHITE &&
                    !isSquareAttacked(Square(4, 0), PieceColor.BLACK, board) &&
                    !isSquareAttacked(Square(3, 0), PieceColor.BLACK, board) &&
                    !isSquareAttacked(Square(2, 0), PieceColor.BLACK, board)
                ) {
                    moves.add(Move(from, Square(2, 0), isCastling = true))
                }
            } else if (color == PieceColor.BLACK && from == Square(4, 7)) {
                // Black Kingside: e8 -> g8
                if (board.castlingRights.blackKingSide &&
                    board[5, 7] == null && board[6, 7] == null &&
                    board[7, 7]?.type == PieceType.ROOK && board[7, 7]?.color == PieceColor.BLACK &&
                    !isSquareAttacked(Square(4, 7), PieceColor.WHITE, board) &&
                    !isSquareAttacked(Square(5, 7), PieceColor.WHITE, board) &&
                    !isSquareAttacked(Square(6, 7), PieceColor.WHITE, board)
                ) {
                    moves.add(Move(from, Square(6, 7), isCastling = true))
                }
                // Black Queenside: e8 -> c8
                if (board.castlingRights.blackQueenSide &&
                    board[1, 7] == null && board[2, 7] == null && board[3, 7] == null &&
                    board[0, 7]?.type == PieceType.ROOK && board[0, 7]?.color == PieceColor.BLACK &&
                    !isSquareAttacked(Square(4, 7), PieceColor.WHITE, board) &&
                    !isSquareAttacked(Square(3, 7), PieceColor.WHITE, board) &&
                    !isSquareAttacked(Square(2, 7), PieceColor.WHITE, board)
                ) {
                    moves.add(Move(from, Square(2, 7), isCastling = true))
                }
            }
        }

        fun applyMoveToBoard(b: Board, move: Move) {
            val movingPiece = b[move.from] ?: return

            // Handle en passant capture
            if (move.isEnPassant) {
                b[move.to.file, move.from.rank] = null
            }

            // Handle castling rook movement
            if (move.isCastling) {
                if (move.to == Square(6, 0)) { // White O-O
                    b[5, 0] = b[7, 0]
                    b[7, 0] = null
                } else if (move.to == Square(2, 0)) { // White O-O-O
                    b[3, 0] = b[0, 0]
                    b[0, 0] = null
                } else if (move.to == Square(6, 7)) { // Black O-O
                    b[5, 7] = b[7, 7]
                    b[7, 7] = null
                } else if (move.to == Square(2, 7)) { // Black O-O-O
                    b[3, 7] = b[0, 7]
                    b[0, 7] = null
                }
            }

            // Move the piece
            val finalPiece = if (move.promotionPiece != null) {
                Piece(move.promotionPiece, movingPiece.color)
            } else {
                movingPiece
            }
            b[move.to] = finalPiece
            b[move.from] = null

            // Update en passant target
            if (movingPiece.type == PieceType.PAWN && kotlin.math.abs(move.to.rank - move.from.rank) == 2) {
                b.enPassantTarget = Square(move.from.file, (move.from.rank + move.to.rank) / 2)
            } else {
                b.enPassantTarget = null
            }

            // Update castling rights
            var wK = b.castlingRights.whiteKingSide
            var wQ = b.castlingRights.whiteQueenSide
            var bK = b.castlingRights.blackKingSide
            var bQ = b.castlingRights.blackQueenSide

            if (movingPiece.type == PieceType.KING) {
                if (movingPiece.color == PieceColor.WHITE) {
                    wK = false
                    wQ = false
                } else {
                    bK = false
                    bQ = false
                }
            }

            if (move.from == Square(0, 0) || move.to == Square(0, 0)) wQ = false
            if (move.from == Square(7, 0) || move.to == Square(7, 0)) wK = false
            if (move.from == Square(0, 7) || move.to == Square(0, 7)) bQ = false
            if (move.from == Square(7, 7) || move.to == Square(7, 7)) bK = false

            b.castlingRights = CastlingRights(wK, wQ, bK, bQ)

            // Update 50-move clock
            if (movingPiece.type == PieceType.PAWN || move.isCapture) {
                b.halfmoveClock = 0
            } else {
                b.halfmoveClock++
            }

            if (b.turn == PieceColor.BLACK) {
                b.fullmoveNumber++
            }
            b.turn = b.turn.opposite
        }

        private fun formatSan(board: Board, move: Move): String {
            if (move.isCastling) {
                return if (move.to.file == 6) "O-O" else "O-O-O"
            }
            val piece = board[move.from] ?: return move.uci
            val sb = StringBuilder()

            if (piece.type != PieceType.PAWN) {
                sb.append(piece.type.symbol)
                // Disambiguation if needed
                val candidateMoves = generatePseudoLegalMoves(board, piece.color).filter {
                    it.to == move.to && it.from != move.from && board[it.from]?.type == piece.type
                }
                if (candidateMoves.isNotEmpty()) {
                    val sameFile = candidateMoves.any { it.from.file == move.from.file }
                    val sameRank = candidateMoves.any { it.from.rank == move.from.rank }
                    if (!sameFile) {
                        sb.append(('a' + move.from.file))
                    } else if (!sameRank) {
                        sb.append((move.from.rank + 1))
                    } else {
                        sb.append(move.from.algebraic)
                    }
                }
            } else if (move.isCapture) {
                sb.append(('a' + move.from.file))
            }

            if (move.isCapture) {
                sb.append('x')
            }

            sb.append(move.to.algebraic)

            if (move.promotionPiece != null) {
                sb.append('=').append(move.promotionPiece.symbol)
            }

            return sb.toString()
        }

        fun isInsufficientMaterial(board: Board): Boolean {
            val pieces = mutableListOf<Piece>()
            var whiteBishopSquare: Square? = null
            var blackBishopSquare: Square? = null

            for (r in 0..7) {
                for (f in 0..7) {
                    val p = board[f, r] ?: continue
                    if (p.type == PieceType.PAWN || p.type == PieceType.ROOK || p.type == PieceType.QUEEN) {
                        return false // Pawns, rooks, queens mean sufficient material
                    }
                    if (p.type == PieceType.BISHOP) {
                        if (p.color == PieceColor.WHITE) whiteBishopSquare = Square(f, r)
                        else blackBishopSquare = Square(f, r)
                    }
                    pieces.add(p)
                }
            }

            // King vs King
            if (pieces.size == 2) return true

            // King and Bishop vs King or King and Knight vs King
            if (pieces.size == 3) {
                val nonKing = pieces.first { it.type != PieceType.KING }
                if (nonKing.type == PieceType.BISHOP || nonKing.type == PieceType.KNIGHT) return true
            }

            // King and Bishop vs King and Bishop on same color squares
            if (pieces.size == 4 && whiteBishopSquare != null && blackBishopSquare != null) {
                if (whiteBishopSquare.isLightSquare == blackBishopSquare.isLightSquare) {
                    return true
                }
            }

            return false
        }
    }
}
