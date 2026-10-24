package com.example.chess.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.model.*
import com.example.ui.theme.BoardColorThemes
import com.example.ui.theme.DarkNavyBorder
import com.example.ui.theme.DarkNavyCard
import com.example.ui.theme.TextMuted

@Composable
fun ChessBoardView(
    board: Board,
    selectedSquare: Square? = null,
    validDestinations: List<Square> = emptyList(),
    lastMove: Move? = null,
    hintMove: Move? = null,
    isInCheck: Boolean = false,
    boardTheme: String = "NAVY",
    isFlipped: Boolean = false,
    interactive: Boolean = true,
    onSquareClicked: (Square) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val (lightColor, darkColor) = when (boardTheme) {
        "WOOD" -> BoardColorThemes.WoodLightSquare to BoardColorThemes.WoodDarkSquare
        "EMERALD" -> BoardColorThemes.EmeraldLightSquare to BoardColorThemes.EmeraldDarkSquare
        else -> BoardColorThemes.NavyLightSquare to BoardColorThemes.NavyDarkSquare
    }

    val kingInCheckSquare = if (isInCheck) board.findKing(board.turn) else null

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(DarkNavyCard)
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        val boardSize = maxWidth
        val squareSize = boardSize / 8

        Column(modifier = Modifier.size(boardSize)) {
            for (row in 0..7) {
                val rank = if (isFlipped) row else 7 - row

                Row(modifier = Modifier.fillMaxWidth().height(squareSize)) {
                    for (col in 0..7) {
                        val file = if (isFlipped) 7 - col else col
                        val square = Square(file, rank)
                        val piece = board[square]

                        val isLight = square.isLightSquare
                        val baseSquareColor = if (isLight) lightColor else darkColor

                        val isSelected = (selectedSquare == square)
                        val isValidDest = validDestinations.contains(square)
                        val isLastMoveSquare = (lastMove?.from == square || lastMove?.to == square)
                        val isKingInCheck = (square == kingInCheckSquare)

                        Box(
                            modifier = Modifier
                                .size(squareSize)
                                .background(baseSquareColor)
                                .then(
                                    if (interactive) {
                                        Modifier.clickable { onSquareClicked(square) }
                                    } else Modifier
                                )
                                .testTag("square_${square.algebraic}"),
                            contentAlignment = Alignment.Center
                        ) {
                            // Overlay highlights
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val isHintSquare = (hintMove?.from == square || hintMove?.to == square)
                                if (isHintSquare) {
                                    drawRect(color = Color(0x66FBBF24))
                                    drawRect(
                                        color = Color(0xFFFBBF24),
                                        style = Stroke(width = 3.dp.toPx())
                                    )
                                }
                                if (isLastMoveSquare) {
                                    drawRect(color = BoardColorThemes.PreviousMoveHighlight)
                                }
                                if (isSelected) {
                                    drawRect(color = BoardColorThemes.SelectedHighlight)
                                }
                                if (isKingInCheck) {
                                    drawRect(color = BoardColorThemes.CheckAlert)
                                }
                                if (isValidDest) {
                                    if (piece != null) {
                                        // Capture target ring
                                        drawCircle(
                                            color = BoardColorThemes.ValidMoveDot,
                                            radius = size.minDimension * 0.42f,
                                            style = Stroke(width = 4.dp.toPx())
                                        )
                                    } else {
                                        // Empty square dot
                                        drawCircle(
                                            color = BoardColorThemes.ValidMoveDot,
                                            radius = size.minDimension * 0.16f
                                        )
                                    }
                                }
                            }

                            // Coordinate annotations on edges
                            val showFileLabel = (row == 7)
                            val showRankLabel = (col == 0)

                            if (showFileLabel || showRankLabel) {
                                Box(modifier = Modifier.fillMaxSize().padding(2.dp)) {
                                    if (showRankLabel) {
                                        Text(
                                            text = "${rank + 1}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isLight) darkColor.copy(alpha = 0.8f) else lightColor.copy(alpha = 0.8f),
                                            modifier = Modifier.align(Alignment.TopStart)
                                        )
                                    }
                                    if (showFileLabel) {
                                        Text(
                                            text = "${('a' + file)}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isLight) darkColor.copy(alpha = 0.8f) else lightColor.copy(alpha = 0.8f),
                                            modifier = Modifier.align(Alignment.BottomEnd)
                                        )
                                    }
                                }
                            }

                            // Render Piece
                            if (piece != null) {
                                ChessPieceView(piece = piece, squareSize = squareSize)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChessPieceView(piece: Piece, squareSize: Dp) {
    val symbol = when (piece.type) {
        PieceType.PAWN -> if (piece.color == PieceColor.WHITE) "♙" else "♟"
        PieceType.KNIGHT -> if (piece.color == PieceColor.WHITE) "♘" else "♞"
        PieceType.BISHOP -> if (piece.color == PieceColor.WHITE) "♗" else "♝"
        PieceType.ROOK -> if (piece.color == PieceColor.WHITE) "♖" else "♜"
        PieceType.QUEEN -> if (piece.color == PieceColor.WHITE) "♕" else "♛"
        PieceType.KING -> if (piece.color == PieceColor.WHITE) "♔" else "♚"
    }

    val isWhite = piece.color == PieceColor.WHITE
    val pieceColor = if (isWhite) Color(0xFFFFFFFF) else Color(0xFF0F172A)
    val glowShadowColor = if (isWhite) Color(0x66000000) else Color(0x6638BDF8)
    val fontSize = (squareSize.value * 0.68f).sp

    Box(contentAlignment = Alignment.Center) {
        // Outline/drop shadow
        Text(
            text = symbol,
            fontSize = fontSize,
            color = glowShadowColor,
            modifier = Modifier.offset(x = 1.dp, y = 1.dp)
        )
        Text(
            text = symbol,
            fontSize = fontSize,
            color = pieceColor,
            fontWeight = FontWeight.Bold
        )
    }
}
