package com.example.chess.model

data class Move(
    val from: Square,
    val to: Square,
    val promotionPiece: PieceType? = null,
    val isCapture: Boolean = false,
    val isCastling: Boolean = false,
    val isEnPassant: Boolean = false,
    var san: String = ""
) {
    val uci: String
        get() {
            val promo = promotionPiece?.let {
                when (it) {
                    PieceType.QUEEN -> "q"
                    PieceType.ROOK -> "r"
                    PieceType.BISHOP -> "b"
                    PieceType.KNIGHT -> "n"
                    else -> ""
                }
            } ?: ""
            return "${from.algebraic}${to.algebraic}$promo"
        }

    override fun toString(): String = if (san.isNotEmpty()) san else uci
}
