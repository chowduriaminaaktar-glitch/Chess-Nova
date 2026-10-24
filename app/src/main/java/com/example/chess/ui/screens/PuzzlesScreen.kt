package com.example.chess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.puzzles.DailyPuzzles
import com.example.chess.ui.MainViewModel
import com.example.chess.ui.components.ChessBoardView
import com.example.chess.ui.localization.AppStrings
import com.example.ui.theme.*

@Composable
fun PuzzlesScreen(
    viewModel: MainViewModel,
    language: String,
    onBack: () -> Unit
) {
    val puzzle by viewModel.currentPuzzle.collectAsState()
    val board by viewModel.puzzleBoard.collectAsState()
    val selectedSquare by viewModel.puzzleSelectedSquare.collectAsState()
    val isSolved by viewModel.puzzleSolved.collectAsState()
    val hintRevealed by viewModel.puzzleHintRevealed.collectAsState()
    val feedback by viewModel.puzzleFeedback.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBg)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("puzzles_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = AppStrings.navPuzzles(language),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${userProgress.completedPuzzles.size} puzzles solved",
                    fontSize = 12.sp,
                    color = GoldAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Puzzle selector carousel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(DailyPuzzles.puzzles) { p ->
                val isSelected = (p.id == puzzle.id)
                val isPuzSolved = userProgress.completedPuzzles.contains(p.id)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) PrimaryPurple.copy(alpha = 0.25f) else DarkNavyCard)
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) PrimaryPurple else DarkNavyBorder,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.openPuzzle(p) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isPuzSolved) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Solved",
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = if (language == "bn") p.titleBn else p.titleEn,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Puzzle Content Scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Puzzle Info Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                border = BorderStroke(1.dp, if (isSolved) EmeraldSuccess.copy(alpha = 0.6f) else DarkNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == "bn") puzzle.titleBn else puzzle.titleEn,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GoldAccent.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "+${puzzle.xpReward} XP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == "bn") puzzle.descriptionBn else puzzle.descriptionEn,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }

            // Interactive Puzzle Board
            ChessBoardView(
                board = board,
                selectedSquare = selectedSquare,
                boardTheme = settings.boardTheme,
                interactive = !isSolved,
                onSquareClicked = { viewModel.onPuzzleSquareClicked(it) }
            )

            // Feedback / Result Card
            if (isSolved) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldSuccess.copy(alpha = 0.15f)),
                    border = BorderStroke(1.dp, EmeraldSuccess),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Solved",
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == "bn") "অভিনন্দন! সঠিক সমাধান (+${puzzle.xpReward} XP)" else "Puzzle Solved! (+${puzzle.xpReward} XP)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == "bn") puzzle.solutionExplanationBn else puzzle.solutionExplanationEn,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hintRevealed) {
                        Text(
                            text = if (language == "bn") puzzle.hintBn else puzzle.hintEn,
                            fontSize = 13.sp,
                            color = GoldAccent,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        TextButton(
                            onClick = { viewModel.revealPuzzleHint() },
                            modifier = Modifier.testTag("puzzle_hint_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == "bn") "ইঙ্গিত দেখুন" else "Need a hint?",
                                color = GoldAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                feedback?.let { msg ->
                    Text(text = msg, fontSize = 12.sp, color = RoseAlert)
                }
            }
        }
    }
}
