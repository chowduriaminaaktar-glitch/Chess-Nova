package com.example.chess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
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
import com.example.chess.ui.localization.AppStrings
import com.example.ui.theme.*

data class BadgeItem(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val descEn: String,
    val descBn: String,
    val iconEmoji: String
)

@Composable
fun AchievementsScreen(
    userProgress: UserProgress,
    language: String,
    onBack: () -> Unit
) {
    val badges = listOf(
        BadgeItem("FIRST_LESSON", "First Step", "প্রথম পদক্ষেপ", "Complete your first chess lesson", "প্রথম দাবা পাঠ সম্পন্ন করুন", "🌱"),
        BadgeItem("TACTICS_STUDENT", "Tactics Disciple", "কৌশলী শিক্ষার্থী", "Complete at least 5 chess lessons", "কমপক্ষে ৫টি দাবা পাঠ সম্পন্ন করুন", "🎯"),
        BadgeItem("CHESS_SCHOLAR", "Grand Scholar", "দাবা পণ্ডিত", "Complete all 12 learning curriculum modules", "১২টি শিক্ষা মডিউল সম্পন্ন করুন", "🎓"),
        BadgeItem("PUZZLE_SOLVER", "Puzzle Cracker", "ধাঁধা সমাধানকারী", "Solve your first tactical chess puzzle", "প্রথম দাবা ধাঁধা সমাধান করুন", "🧩"),
        BadgeItem("TACTICIAN", "Master Tactician", "কৌশল বিশেষজ্ঞ", "Solve 5 or more tactical puzzles", "৫টি বা ততোধিক ধাঁধা সমাধান করুন", "⚔️"),
        BadgeItem("FIRST_VICTORY", "First Blood", "প্রথম বিজয়", "Win a game of chess against the computer", "কম্পিউটারের বিরুদ্ধে জয়লাভ করুন", "🏆"),
        BadgeItem("GRANDMASTER_BEATEN", "Giant Slayer", "দানব নিধনকারী", "Defeat the chess engine on HARD (1850 Elo)", "কঠিন স্তরে দাবা ইঞ্জিনকে পরাজিত করুন", "👑")
    )

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("achievements_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = if (language == "bn") "অর্জন ও পদক" else "Achievements & Badges",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${userProgress.unlockedAchievements.size} of ${badges.size} Unlocked",
                    fontSize = 12.sp,
                    color = GoldAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(badges) { badge ->
                val isUnlocked = userProgress.unlockedAchievements.contains(badge.id)

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isUnlocked) GoldAccent.copy(alpha = 0.6f) else DarkNavyBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isUnlocked) GoldAccent.copy(alpha = 0.2f)
                                    else DarkNavyBg
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isUnlocked) {
                                Text(text = badge.iconEmoji, fontSize = 24.sp)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == "bn") badge.titleBn else badge.titleEn,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) TextPrimary else TextSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (language == "bn") badge.descBn else badge.descEn,
                                fontSize = 12.sp,
                                color = if (isUnlocked) TextSecondary else TextMuted
                            )
                        }

                        if (isUnlocked) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GoldAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "UNLOCKED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
