package com.example.chess.model

data class Square(
    val file: Int, // 0..7 (a..h)
    val rank: Int  // 0..7 (1..8)
) {
    val index: Int
        get() = rank * 8 + file

    val algebraic: String
        get() = "${('a' + file)}${rank + 1}"

    val isLightSquare: Boolean
        get() = (file + rank) % 2 != 0

    fun isValid(): Boolean = file in 0..7 && rank in 0..7

    companion object {
        fun fromIndex(index: Int): Square {
            require(index in 0..63) { "Index out of range: $index" }
            return Square(file = index % 8, rank = index / 8)
        }

        fun fromAlgebraic(alg: String): Square {
            require(alg.length >= 2) { "Invalid algebraic square: $alg" }
            val file = alg[0].lowercaseChar() - 'a'
            val rank = alg[1].digitToInt() - 1
            require(file in 0..7 && rank in 0..7) { "Square out of bounds: $alg" }
            return Square(file, rank)
        }
    }
}
