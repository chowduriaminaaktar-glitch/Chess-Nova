package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    onPrimary = Color.White,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = PurpleLight,

    secondary = ElectricBlue,
    onSecondary = Color.Black,
    secondaryContainer = ElectricBlueContainer,
    onSecondaryContainer = ElectricCyan,

    tertiary = GoldAccent,
    onTertiary = Color.Black,

    background = DarkNavyBg,
    onBackground = TextPrimary,

    surface = DarkNavySurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkNavyCard,
    onSurfaceVariant = TextSecondary,

    outline = DarkNavyBorder,
    error = RoseAlert,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = PurpleDark,

    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),

    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,

    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),

    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),

    outline = Color(0xFFCBD5E1),
    error = Color(0xFFE11D48),
    onError = Color.White
)

@Composable
fun ChessNovaTheme(
    darkTheme: Boolean = true, // Default to stunning dark navy theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
