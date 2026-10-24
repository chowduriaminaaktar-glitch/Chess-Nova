package com.example.chess.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceType
import com.example.ui.theme.DarkNavyCard
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.TextPrimary

@Composable
fun PromotionDialog(
    color: PieceColor,
    language: String,
    onSelectPiece: (PieceType) -> Unit,
    onDismiss: () -> Unit
) {
    val title = if (language == "bn") "প্রমোশনের ঘুঁটি নির্বাচন করুন" else "Choose Promotion Piece"
    val pieces = listOf(
        PieceType.QUEEN to if (color == PieceColor.WHITE) "♕" else "♛",
        PieceType.ROOK to if (color == PieceColor.WHITE) "♖" else "♜",
        PieceType.BISHOP to if (color == PieceColor.WHITE) "♗" else "♝",
        PieceType.KNIGHT to if (color == PieceColor.WHITE) "♘" else "♞"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkNavyCard,
            tonalElevation = 8.dp,
            modifier = Modifier.testTag("promotion_dialog")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pieces.forEach { (type, symbol) ->
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryPurple.copy(alpha = 0.2f))
                                .clickable { onSelectPiece(type) }
                                .testTag("promote_${type.name.lowercase()}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = symbol,
                                fontSize = 36.sp,
                                color = if (color == PieceColor.WHITE) Color.White else ElectricBlue
                            )
                        }
                    }
                }
            }
        }
    }
}
