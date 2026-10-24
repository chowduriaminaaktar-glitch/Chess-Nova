package com.example.chess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.data.GameRecordEntity
import com.example.chess.data.UserProgress
import com.example.chess.lessons.CurriculumData
import com.example.chess.puzzles.DailyPuzzles
import com.example.chess.ui.AppScreen
import com.example.chess.ui.PlayMode
import com.example.chess.ui.localization.AppStrings
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    userProgress: UserProgress,
    recentGames: List<GameRecordEntity>,
    language: String,
    onNavigate: (AppScreen) -> Unit,
    onStartPlay: (PlayMode) -> Unit,
    onOpenLesson: (com.example.chess.lessons.Lesson) -> Unit,
    onOpenPuzzle: (com.example.chess.puzzles.ChessPuzzle) -> Unit
) {
    val completedCount = userProgress.completedLessons.size
    val totalLessons = CurriculumData.modules.sumOf { it.lessons.size }
    val progressFraction = if (totalLessons > 0) completedCount.toFloat() / totalLessons else 0f

    // Find next uncompleted lesson
    val nextLesson = CurriculumData.modules
        .flatMap { it.lessons }
        .firstOrNull { !userProgress.completedLessons.contains(it.id) }
        ?: CurriculumData.modules.first().lessons.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Top App Bar / User Profile Stats Bar
        item {
            HeaderStatsBar(
                userProgress = userProgress,
                language = language,
                onSettingsClicked = { onNavigate(AppScreen.SETTINGS) },
                onAchievementsClicked = { onNavigate(AppScreen.ACHIEVEMENTS) }
            )
        }

        // App Branding Tagline
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "ChessNova",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = AppStrings.tagline(language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ElectricBlue
                )
            }
        }

        // Continue Learning Hero Card
        item {
            ContinueLearningHeroCard(
                nextLesson = nextLesson,
                progressFraction = progressFraction,
                completedCount = completedCount,
                totalLessons = totalLessons,
                language = language,
                onContinueClicked = { onOpenLesson(nextLesson) }
            )
        }

        // Two Big Primary Action Cards: LEARN CHESS and PLAY CHESS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // LEARN CHESS
                PrimaryActionCard(
                    title = AppStrings.learnChessBig(language),
                    subtitle = "${CurriculumData.modules.size} Modules",
                    icon = Icons.Default.School,
                    gradient = Brush.linearGradient(listOf(Color(0xFF6D28D9), Color(0xFF4C1D95))),
                    borderColor = PrimaryPurple,
                    modifier = Modifier.weight(1f),
                    testTag = "learn_chess_card",
                    onClick = { onNavigate(AppScreen.LEARN) }
                )

                // PLAY CHESS
                PrimaryActionCard(
                    title = AppStrings.playChessBig(language),
                    subtitle = "Engine & 2P",
                    icon = Icons.Default.SportsEsports,
                    gradient = Brush.linearGradient(listOf(Color(0xFF0369A1), Color(0xFF075985))),
                    borderColor = ElectricBlue,
                    modifier = Modifier.weight(1f),
                    testTag = "play_chess_card",
                    onClick = {
                        onStartPlay(PlayMode.VS_COMPUTER)
                        onNavigate(AppScreen.PLAY)
                    }
                )
            }
        }

        // Daily Chess Puzzle Card
        item {
            val dailyPuzzle = DailyPuzzles.getTodayPuzzle()
            val isSolved = userProgress.completedPuzzles.contains(dailyPuzzle.id)

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                border = BorderStroke(1.dp, if (isSolved) EmeraldSuccess.copy(alpha = 0.5f) else DarkNavyBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenPuzzle(dailyPuzzle) }
                    .testTag("daily_puzzle_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSolved) EmeraldSuccess.copy(alpha = 0.2f) else GoldAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSolved) Icons.Default.CheckCircle else Icons.Default.Extension,
                            contentDescription = "Puzzle",
                            tint = if (isSolved) EmeraldSuccess else GoldAccent,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.dailyPuzzle(language),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (isSolved) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EmeraldSuccess.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "SOLVED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldSuccess
                                    )
                                }
                            }
                        }
                        Text(
                            text = if (language == "bn") dailyPuzzle.titleBn else dailyPuzzle.titleEn,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open",
                        tint = ElectricBlue
                    )
                }
            }
        }

        // AI Chess Tutor Banner Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161F37)),
                border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.TUTOR) }
                    .testTag("tutor_banner_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryPurple.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Tutor",
                            tint = PurpleLight,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.navTutor(language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (language == "bn") "বোর্ড বিশ্লেষণ ও ব্যক্তিগত পরামর্শ" else "Analyze positions & get GM insights",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = GoldAccent
                    )
                }
            }
        }

        // Recent Games Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.recentGames(language),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        // Recent Games List or Empty State
        if (recentGames.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(DarkNavyCard.copy(alpha = 0.5f))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AppStrings.noGamesYet(language),
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            }
        } else {
            items(recentGames.take(4)) { gameRecord ->
                RecentGameItem(record = gameRecord, language = language)
            }
        }
    }
}

@Composable
private fun HeaderStatsBar(
    userProgress: UserProgress,
    language: String,
    onSettingsClicked: () -> Unit,
    onAchievementsClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Level & Streak pills
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Level Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryPurple.copy(alpha = 0.2f))
                    .border(1.dp, PrimaryPurple.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = AppStrings.levelTitle(language, userProgress.level),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleLight
                )
            }

            // Streak Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(GoldAccent.copy(alpha = 0.15f))
                    .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔥", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = AppStrings.streakDays(language, userProgress.streakDays),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
            }

            // XP Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ElectricBlue.copy(alpha = 0.15f))
                    .border(1.dp, ElectricBlue.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = AppStrings.xpPoints(language, userProgress.totalXp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricBlue
                )
            }
        }

        // Action icons
        Row {
            IconButton(
                onClick = onAchievementsClicked,
                modifier = Modifier.testTag("achievements_button")
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Achievements",
                    tint = GoldAccent
                )
            }
            IconButton(
                onClick = onSettingsClicked,
                modifier = Modifier.testTag("settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ContinueLearningHeroCard(
    nextLesson: com.example.chess.lessons.Lesson,
    progressFraction: Float,
    completedCount: Int,
    totalLessons: Int,
    language: String,
    onContinueClicked: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
        border = BorderStroke(1.dp, DarkNavyBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("continue_learning_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.continueLearning(language).uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$completedCount / $totalLessons",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == "bn") nextLesson.titleBn else nextLesson.titleEn,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = if (language == "bn") nextLesson.overviewBn else nextLesson.overviewEn,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = PrimaryPurple,
                trackColor = DarkNavyBg
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onContinueClicked,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("continue_learning_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.continueLearning(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun PrimaryActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: Brush,
    borderColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun RecentGameItem(record: GameRecordEntity, language: String) {
    val resultColor = when (record.result) {
        "WIN" -> EmeraldSuccess
        "LOSS" -> RoseAlert
        else -> GoldAccent
    }

    val resultLabel = when (record.result) {
        "WIN" -> if (language == "bn") "জয়" else "Win"
        "LOSS" -> if (language == "bn") "পরাজয়" else "Loss"
        else -> if (language == "bn") "ড্র" else "Draw"
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(resultColor)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (record.gameMode == "VS_COMPUTER") {
                            "Vs Computer (${record.difficulty ?: "Normal"})"
                        } else {
                            record.gameMode.replace("_", " ")
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${record.movesCount} moves",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(resultColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = resultLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = resultColor
                )
            }
        }
    }
}
