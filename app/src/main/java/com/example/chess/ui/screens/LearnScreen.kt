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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.chess.data.UserProgress
import com.example.chess.lessons.CurriculumData
import com.example.chess.lessons.Lesson
import com.example.chess.lessons.LessonModule
import com.example.chess.ui.localization.AppStrings
import com.example.ui.theme.*

@Composable
fun LearnScreen(
    userProgress: UserProgress,
    language: String,
    onBack: () -> Unit,
    onOpenLesson: (Lesson) -> Unit
) {
    val totalLessons = CurriculumData.modules.sumOf { it.lessons.size }
    val completedCount = userProgress.completedLessons.size
    val progressPercent = if (totalLessons > 0) (completedCount * 100 / totalLessons) else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBg)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        // Top Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("learn_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = AppStrings.navLearn(language),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$completedCount of $totalLessons lessons completed ($progressPercent%)",
                    fontSize = 12.sp,
                    color = ElectricBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Modules List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(CurriculumData.modules) { module ->
                ModuleCard(
                    module = module,
                    userProgress = userProgress,
                    language = language,
                    onOpenLesson = onOpenLesson
                )
            }
        }
    }
}

@Composable
private fun ModuleCard(
    module: LessonModule,
    userProgress: UserProgress,
    language: String,
    onOpenLesson: (Lesson) -> Unit
) {
    val moduleCompletedCount = module.lessons.count { userProgress.completedLessons.contains(it.id) }
    val isModuleFullyCompleted = (moduleCompletedCount == module.lessons.size)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
        border = BorderStroke(1.dp, if (isModuleFullyCompleted) EmeraldSuccess.copy(alpha = 0.5f) else DarkNavyBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("module_${module.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == "bn") module.titleBn else module.titleEn,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                if (isModuleFullyCompleted) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldSuccess.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "MASTERED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSuccess
                        )
                    }
                } else {
                    Text(
                        text = "$moduleCompletedCount/${module.lessons.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == "bn") module.descriptionBn else module.descriptionEn,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Lessons in module
            module.lessons.forEach { lesson ->
                val isCompleted = userProgress.completedLessons.contains(lesson.id)
                LessonItemRow(
                    lesson = lesson,
                    isCompleted = isCompleted,
                    language = language,
                    onClick = { onOpenLesson(lesson) }
                )
            }
        }
    }
}

@Composable
private fun LessonItemRow(
    lesson: Lesson,
    isCompleted: Boolean,
    language: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkNavyBg.copy(alpha = 0.6f))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("lesson_${lesson.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) EmeraldSuccess else PrimaryPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        tint = PurpleLight,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (language == "bn") lesson.titleBn else lesson.titleEn,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(GoldAccent.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "+${lesson.xpReward} XP",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }
    }
}
