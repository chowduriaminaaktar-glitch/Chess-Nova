package com.example.chess.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.model.Piece
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceType
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun CapturedPiecesView(
    capturedPieces: List<Piece>,
    materialAdvantage: Int,
    isWhite: Boolean,
    modifier: Modifier = Modifier
) {
    // Group pieces for clean visual scanning: Pawns, Knights, Bishops, Rooks, Queens
    val sorted = capturedPieces.sortedBy { it.type.baseValue }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
            if (sorted.isEmpty()) {
                Text(
                    text = "-",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            } else {
                sorted.forEach { piece ->
                    val symbol = when (piece.type) {
                        PieceType.PAWN -> if (piece.color == PieceColor.WHITE) "♙" else "♟"
                        PieceType.KNIGHT -> if (piece.color == PieceColor.WHITE) "♘" else "♞"
                        PieceType.BISHOP -> if (piece.color == PieceColor.WHITE) "♗" else "♝"
                        PieceType.ROOK -> if (piece.color == PieceColor.WHITE) "♖" else "♜"
                        PieceType.QUEEN -> if (piece.color == PieceColor.WHITE) "♕" else "♛"
                        PieceType.KING -> if (piece.color == PieceColor.WHITE) "♔" else "♚"
                    }
                    Text(
                        text = symbol,
                        fontSize = 18.sp,
                        color = if (piece.color == PieceColor.WHITE) Color.White else ElectricBlue
                    )
                }
            }
        }

        if (materialAdvantage > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(GoldAccent.copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "+$materialAdvantage",
                    color = GoldAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
