package com.example.chess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.chess.lessons.Lesson
import com.example.chess.ui.MainViewModel
import com.example.chess.ui.components.ChessBoardView
import com.example.ui.theme.*

@Composable
fun LessonDetailScreen(
    viewModel: MainViewModel,
    lesson: Lesson,
    language: String,
    onBack: () -> Unit
) {
    val board by viewModel.lessonBoard.collectAsState()
    val challengeSolved by viewModel.lessonChallengeSolved.collectAsState()
    val quizSelectedOption by viewModel.lessonQuizSelectedOption.collectAsState()
    val quizSubmitted by viewModel.lessonQuizSubmitted.collectAsState()
    val hintRevealed by viewModel.lessonHintRevealed.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()
    val settings by viewModel.settings.collectAsState()

    val isLessonCompleted = userProgress.completedLessons.contains(lesson.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBg)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("lesson_detail_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == "bn") lesson.titleBn else lesson.titleEn,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            if (isLessonCompleted) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmeraldSuccess.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "COMPLETED",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Theory & Concept Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                border = BorderStroke(1.dp, DarkNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (language == "bn") "তত্ত্ব ও ধারণা" else "Concept & Theory",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == "bn") lesson.theoryBn else lesson.theoryEn,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                }
            }

            // Interactive Board Challenge (if lesson has one)
            lesson.interactiveChallenge?.let { challenge ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                    border = BorderStroke(1.dp, if (challengeSolved) EmeraldSuccess else PrimaryPurple.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth().testTag("interactive_challenge_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == "bn") "অনুশীলন চ্যালেঞ্জ" else "Interactive Challenge",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (challengeSolved) EmeraldSuccess else PrimaryPurple
                            )

                            if (challengeSolved) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Solved",
                                        tint = EmeraldSuccess,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "SOLVED",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldSuccess
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == "bn") challenge.instructionBn else challenge.instructionEn,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Interactive Board
                        ChessBoardView(
                            board = board,
                            boardTheme = settings.boardTheme,
                            interactive = !challengeSolved,
                            onSquareClicked = { viewModel.onLessonSquareClicked(it) }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (challengeSolved) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(EmeraldSuccess.copy(alpha = 0.15f))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = if (language == "bn") challenge.successExplanationBn else challenge.successExplanationEn,
                                    fontSize = 13.sp,
                                    color = EmeraldSuccess,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (hintRevealed) {
                                    Text(
                                        text = if (language == "bn") challenge.hintBn else challenge.hintEn,
                                        fontSize = 12.sp,
                                        color = GoldAccent,
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    TextButton(onClick = { viewModel.revealLessonHint() }) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = GoldAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == "bn") "ইঙ্গিত দেখুন" else "Show Hint",
                                            color = GoldAccent,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quiz Question Card
            val quiz = lesson.quizQuestion
            val options = if (language == "bn") quiz.optionsBn else quiz.optionsEn

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                border = BorderStroke(1.dp, DarkNavyBorder),
                modifier = Modifier.fillMaxWidth().testTag("quiz_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (language == "bn") "পাঠ যাচাই কুইজ" else "Concept Quiz",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricBlue
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == "bn") quiz.questionBn else quiz.questionEn,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quiz Options
                    options.forEachIndexed { index, optionText ->
                        val isSelected = (quizSelectedOption == index)
                        val isCorrect = (index == quiz.correctIndex)
                        val showFeedback = quizSubmitted

                        val borderColor = when {
                            showFeedback && isCorrect -> EmeraldSuccess
                            showFeedback && isSelected && !isCorrect -> RoseAlert
                            isSelected -> PrimaryPurple
                            else -> DarkNavyBorder
                        }

                        val bgColor = when {
                            showFeedback && isCorrect -> EmeraldSuccess.copy(alpha = 0.15f)
                            showFeedback && isSelected && !isCorrect -> RoseAlert.copy(alpha = 0.15f)
                            isSelected -> PrimaryPurple.copy(alpha = 0.2f)
                            else -> DarkNavyBg.copy(alpha = 0.5f)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                .clickable(enabled = !quizSubmitted) {
                                    viewModel.selectQuizOption(index)
                                }
                                .padding(14.dp)
                                .testTag("quiz_option_$index")
                        ) {
                            Text(
                                text = optionText,
                                fontSize = 14.sp,
                                color = TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!quizSubmitted) {
                        Button(
                            onClick = { viewModel.submitQuiz() },
                            enabled = quizSelectedOption != null,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("submit_quiz_button")
                        ) {
                            Text(
                                text = if (language == "bn") "উত্তর জমা দিন" else "Submit Answer",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        // Explanation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (quizSelectedOption == quiz.correctIndex) EmeraldSuccess.copy(alpha = 0.15f)
                                    else RoseAlert.copy(alpha = 0.15f)
                                )
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (quizSelectedOption == quiz.correctIndex) {
                                        if (language == "bn") "সঠিক উত্তর! (+${lesson.xpReward} XP অর্জিত)" else "Correct! (+${lesson.xpReward} XP earned)"
                                    } else {
                                        if (language == "bn") "ভুল উত্তর। নিচে ব্যাখ্যাটি দেখুন:" else "Incorrect. Here's why:"
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (quizSelectedOption == quiz.correctIndex) EmeraldSuccess else RoseAlert
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (language == "bn") quiz.explanationBn else quiz.explanationEn,
                                    fontSize = 12.sp,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
