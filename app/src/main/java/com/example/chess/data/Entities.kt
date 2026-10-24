package com.example.chess.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1,
    val level: Int = 1,
    val totalXp: Int = 0,
    val streakDays: Int = 1,
    val lastActiveEpochDay: Long = 0L,
    val completedLessons: String = "",
    val completedPuzzles: String = "",
    val unlockedAchievements: String = ""
) {
    fun getCompletedLessonSet(): Set<String> {
        return if (completedLessons.isBlank()) emptySet() else completedLessons.split(",").toSet()
    }

    fun getCompletedPuzzleSet(): Set<String> {
        return if (completedPuzzles.isBlank()) emptySet() else completedPuzzles.split(",").toSet()
    }

    fun getUnlockedAchievementSet(): Set<String> {
        return if (unlockedAchievements.isBlank()) emptySet() else unlockedAchievements.split(",").toSet()
    }
}

@Entity(tableName = "game_records")
data class GameRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val gameMode: String, // "VS_COMPUTER", "LOCAL_2P", "PRACTICE"
    val difficulty: String? = null,
    val result: String, // "WIN", "LOSS", "DRAW", "IN_PROGRESS"
    val movesCount: Int,
    val pgn: String,
    val playerColor: String = "WHITE"
)

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val language: String = "en", // "en" or "bn"
    val boardTheme: String = "NAVY", // "NAVY", "WOOD", "EMERALD"
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val clockMinutes: Int = 0, // 0 = unlimited, 3, 5, 10
    val passTurnScreenEnabled: Boolean = true
)
