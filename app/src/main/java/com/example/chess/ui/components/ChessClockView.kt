package com.example.chess.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkNavyCard
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun ChessClockView(
    whiteTimeSeconds: Int,
    blackTimeSeconds: Int,
    isWhiteTurn: Boolean,
    isClockRunning: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClockCard(
            label = "White",
            timeSeconds = whiteTimeSeconds,
            isActive = isWhiteTurn && isClockRunning
        )
        ClockCard(
            label = "Black",
            timeSeconds = blackTimeSeconds,
            isActive = !isWhiteTurn && isClockRunning
        )
    }
}

@Composable
private fun ClockCard(
    label: String,
    timeSeconds: Int,
    isActive: Boolean
) {
    val minutes = timeSeconds / 60
    val seconds = timeSeconds % 60
    val formatted = "%02d:%02d".format(minutes, seconds)
    val isLowTime = timeSeconds < 30 && timeSeconds > 0

    val bgColor = if (isActive) DarkNavyCard.copy(alpha = 0.95f) else DarkNavyCard.copy(alpha = 0.5f)
    val borderColor = when {
        isLowTime && isActive -> RoseAlert
        isActive -> ElectricBlue
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(width = if (isActive) 1.5.dp else 0.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = if (isActive) ElectricBlue else TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formatted,
                color = if (isLowTime) RoseAlert else TextPrimary,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
