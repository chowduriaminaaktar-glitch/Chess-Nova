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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.ui.MainViewModel
import com.example.chess.ui.components.ChessBoardView
import com.example.chess.ui.localization.AppStrings
import com.example.ui.theme.*

@Composable
fun TutorScreen(
    viewModel: MainViewModel,
    language: String,
    onBack: () -> Unit
) {
    val board by viewModel.gameBoard.collectAsState()
    val tutorResult by viewModel.tutorResult.collectAsState()
    val isAnalyzing by viewModel.isTutorAnalyzing.collectAsState()
    val settings by viewModel.settings.collectAsState()

    var userCustomQuestion by remember { mutableStateOf("") }

    val presetQueries = if (language == "bn") {
        listOf(
            "বোর্ডের সামগ্রিক অবস্থা ব্যাখ্যা করুন",
            "আমার সেরা চাল কোনটি এবং কেন?",
            "আমার কোনো মারাত্মক বিপদ আছে কি?",
            "কীভাবে কেন্দ্র নিয়ন্ত্রণ উন্নত করা যায়?"
        )
    } else {
        listOf(
            "Explain current board position",
            "What is the best candidate move?",
            "Are there any tactical threats?",
            "How can I improve my piece activity?"
        )
    }

    LaunchedEffect(Unit) {
        if (tutorResult == null) {
            viewModel.analyzeCurrentPosition()
        }
    }

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("tutor_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = PurpleLight,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = AppStrings.navTutor(language),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (language == "bn") "গ্র্যান্ডমাস্টার পরামর্শ ও চাল বিশ্লেষণ" else "Grandmaster Insights & Position Coach",
                        fontSize = 11.sp,
                        color = ElectricBlue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Position preview board
            ChessBoardView(
                board = board,
                boardTheme = settings.boardTheme,
                interactive = false
            )

            // Quick Query Chips
            Text(
                text = if (language == "bn") "শিক্ষকের কাছে প্রশ্ন করুন:" else "Ask Coach:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presetQueries) { query ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkNavyCard)
                            .border(1.dp, PrimaryPurple.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .clickable(enabled = !isAnalyzing) {
                                viewModel.analyzeCurrentPosition(query)
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = query,
                            fontSize = 12.sp,
                            color = PurpleLight,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Analysis Output Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                border = BorderStroke(1.dp, DarkNavyBorder),
                modifier = Modifier.fillMaxWidth().testTag("tutor_analysis_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == "bn") "শিক্ষকের মূল্যায়ন" else "Coach Analysis",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        if (isAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = ElectricBlue,
                                strokeWidth = 2.dp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    tutorResult?.let { result ->
                        // Engine Evaluation Box (Clearly Distinguished)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkNavyBg)
                                .border(1.dp, ElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (language == "bn") "⚙️ ইঞ্জিন পরিমাপ (লোকাল মিনম্যাক্স)" else "⚙️ Local Engine Calculation",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricBlue
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = result.engineEvaluation,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Natural Language Friendly Explanation
                        Text(
                            text = if (language == "bn") "🎓 শিক্ষকের ব্যাখ্যা ও পরামর্শ:" else "🎓 Grandmaster Advice:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurpleLight
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = result.aiExplanation,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )

                        if (result.isOfflineFallback) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (language == "bn") "💡 সম্পূর্ণ অফলাইনে স্থানীয় বিশ্লেষণ প্রদর্শিত হচ্ছে।" else "💡 Running fully offline via local heuristic analysis.",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    } ?: run {
                        if (isAnalyzing) {
                            Text(
                                text = if (language == "bn") "বোর্ড বিশ্লেষণ করা হচ্ছে..." else "Analyzing board position with chess engine...",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Custom Question Input Field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userCustomQuestion,
                    onValueChange = { userCustomQuestion = it },
                    placeholder = {
                        Text(
                            text = if (language == "bn") "নিজস্ব প্রশ্ন লিখুন..." else "Ask coach anything...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.weight(1f).testTag("custom_question_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = DarkNavyBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = DarkNavyCard,
                        unfocusedContainerColor = DarkNavyCard
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (userCustomQuestion.isNotBlank()) {
                            viewModel.analyzeCurrentPosition(userCustomQuestion)
                            userCustomQuestion = ""
                        }
                    },
                    enabled = userCustomQuestion.isNotBlank() && !isAnalyzing,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(52.dp).testTag("ask_question_button")
                ) {
                    Text(text = if (language == "bn") "পাঠান" else "Ask")
                }
            }
        }
    }
}
