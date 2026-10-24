package com.example.chess.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

data class UserProgress(
    val level: Int,
    val totalXp: Int,
    val streakDays: Int,
    val completedLessons: Set<String>,
    val completedPuzzles: Set<String>,
    val unlockedAchievements: Set<String>
)

class UserRepository(private val database: AppDatabase) {
    private val progressDao = database.userProgressDao()
    private val recordDao = database.gameRecordDao()
    private val settingsDao = database.appSettingsDao()

    val progressFlow: Flow<UserProgress> = progressDao.getUserProgress().map { entity ->
        entity?.let {
            UserProgress(
                level = it.level,
                totalXp = it.totalXp,
                streakDays = it.streakDays,
                completedLessons = it.getCompletedLessonSet(),
                completedPuzzles = it.getCompletedPuzzleSet(),
                unlockedAchievements = it.getUnlockedAchievementSet()
            )
        } ?: UserProgress(
            level = 1,
            totalXp = 0,
            streakDays = 1,
            completedLessons = emptySet(),
            completedPuzzles = emptySet(),
            unlockedAchievements = emptySet()
        )
    }

    val recentGamesFlow: Flow<List<GameRecordEntity>> = recordDao.getRecentGames()
    val settingsFlow: Flow<AppSettingsEntity> = settingsDao.getSettings().map {
        it ?: AppSettingsEntity()
    }

    suspend fun checkDailyActivity() {
        val current = progressDao.getUserProgressOnce() ?: UserProgressEntity()
        val todayEpochDay = LocalDate.now().toEpochDay()
        val lastEpochDay = current.lastActiveEpochDay

        val newStreak = when {
            lastEpochDay == 0L -> 1
            todayEpochDay == lastEpochDay -> current.streakDays
            todayEpochDay == lastEpochDay + 1 -> current.streakDays + 1
            else -> 1
        }

        val updated = current.copy(
            streakDays = newStreak,
            lastActiveEpochDay = todayEpochDay
        )
        progressDao.saveUserProgress(updated)
    }

    suspend fun addXp(amount: Int) {
        val current = progressDao.getUserProgressOnce() ?: UserProgressEntity()
        val newXp = current.totalXp + amount
        val newLevel = calculateLevel(newXp)
        progressDao.saveUserProgress(current.copy(totalXp = newXp, level = newLevel))
    }

    suspend fun completeLesson(lessonId: String, xpReward: Int = 30) {
        val current = progressDao.getUserProgressOnce() ?: UserProgressEntity()
        val currentSet = current.getCompletedLessonSet().toMutableSet()
        val isFirstTime = currentSet.add(lessonId)

        val newAchievements = current.getUnlockedAchievementSet().toMutableSet()
        if (currentSet.size >= 1) newAchievements.add("FIRST_LESSON")
        if (currentSet.size >= 5) newAchievements.add("TACTICS_STUDENT")
        if (currentSet.size >= 12) newAchievements.add("CHESS_SCHOLAR")

        val xpToAdd = if (isFirstTime) xpReward else 5
        val newXp = current.totalXp + xpToAdd
        val newLevel = calculateLevel(newXp)

        progressDao.saveUserProgress(
            current.copy(
                totalXp = newXp,
                level = newLevel,
                completedLessons = currentSet.joinToString(","),
                unlockedAchievements = newAchievements.joinToString(",")
            )
        )
    }

    suspend fun completePuzzle(puzzleId: String, xpReward: Int = 20) {
        val current = progressDao.getUserProgressOnce() ?: UserProgressEntity()
        val currentSet = current.getCompletedPuzzleSet().toMutableSet()
        val isFirstTime = currentSet.add(puzzleId)

        val newAchievements = current.getUnlockedAchievementSet().toMutableSet()
        if (currentSet.size >= 1) newAchievements.add("PUZZLE_SOLVER")
        if (currentSet.size >= 5) newAchievements.add("TACTICIAN")

        val xpToAdd = if (isFirstTime) xpReward else 5
        val newXp = current.totalXp + xpToAdd
        val newLevel = calculateLevel(newXp)

        progressDao.saveUserProgress(
            current.copy(
                totalXp = newXp,
                level = newLevel,
                completedPuzzles = currentSet.joinToString(","),
                unlockedAchievements = newAchievements.joinToString(",")
            )
        )
    }

    suspend fun recordGame(
        gameMode: String,
        difficulty: String?,
        result: String,
        movesCount: Int,
        pgn: String,
        playerColor: String
    ) {
        recordDao.insertGame(
            GameRecordEntity(
                gameMode = gameMode,
                difficulty = difficulty,
                result = result,
                movesCount = movesCount,
                pgn = pgn,
                playerColor = playerColor
            )
        )

        val current = progressDao.getUserProgressOnce() ?: UserProgressEntity()
        val newAchievements = current.getUnlockedAchievementSet().toMutableSet()

        var xpEarned = 10 // base match XP
        if (result == "WIN") {
            xpEarned += 40
            newAchievements.add("FIRST_VICTORY")
            if (difficulty == "HARD") {
                xpEarned += 50
                newAchievements.add("GRANDMASTER_BEATEN")
            }
        } else if (result == "DRAW") {
            xpEarned += 15
        }

        val newXp = current.totalXp + xpEarned
        val newLevel = calculateLevel(newXp)

        progressDao.saveUserProgress(
            current.copy(
                totalXp = newXp,
                level = newLevel,
                unlockedAchievements = newAchievements.joinToString(",")
            )
        )
    }

    suspend fun updateSettings(update: (AppSettingsEntity) -> AppSettingsEntity) {
        val current = settingsDao.getSettingsOnce() ?: AppSettingsEntity()
        val updated = update(current)
        settingsDao.saveSettings(updated)
    }

    suspend fun resetProgress() {
        progressDao.saveUserProgress(UserProgressEntity(id = 1))
        recordDao.clearHistory()
    }

    private fun calculateLevel(totalXp: Int): Int {
        // Level 1: 0-99 XP, Level 2: 100-249 XP, Level 3: 250-449 XP, etc.
        return when {
            totalXp < 100 -> 1
            totalXp < 250 -> 2
            totalXp < 450 -> 3
            totalXp < 700 -> 4
            totalXp < 1000 -> 5
            totalXp < 1400 -> 6
            totalXp < 1900 -> 7
            totalXp < 2500 -> 8
            totalXp < 3200 -> 9
            else -> 10
        }
    }
}
