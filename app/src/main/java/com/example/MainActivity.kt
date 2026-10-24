package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.ui.AppScreen
import com.example.chess.ui.MainViewModel
import com.example.chess.ui.localization.AppStrings
import com.example.chess.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessNovaTheme(darkTheme = true) {
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScaffold(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()
    val recentGames by viewModel.recentGames.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val activeLesson by viewModel.activeLesson.collectAsState()

    val language = settings.language

    // BackHandler: Handle returning to Home from sub-screens
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        when (currentScreen) {
            AppScreen.LESSON_DETAIL -> viewModel.navigateTo(AppScreen.LEARN)
            else -> viewModel.navigateTo(AppScreen.HOME)
        }
    }

    val showBottomBar = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.LEARN,
        AppScreen.PLAY,
        AppScreen.PUZZLES,
        AppScreen.TUTOR
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkNavyBg,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = DarkNavySurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.HOME,
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(22.dp)) },
                        label = { Text(AppStrings.navHome(language), fontSize = 11.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_home")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.LEARN || currentScreen == AppScreen.LESSON_DETAIL,
                        onClick = { viewModel.navigateTo(AppScreen.LEARN) },
                        icon = { Icon(Icons.Default.School, contentDescription = "Learn", modifier = Modifier.size(22.dp)) },
                        label = { Text(AppStrings.navLearn(language), fontSize = 11.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_learn")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.PLAY,
                        onClick = { viewModel.navigateTo(AppScreen.PLAY) },
                        icon = { Icon(Icons.Default.SportsEsports, contentDescription = "Play", modifier = Modifier.size(22.dp)) },
                        label = { Text(AppStrings.navPlay(language), fontSize = 11.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_play")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.PUZZLES,
                        onClick = { viewModel.navigateTo(AppScreen.PUZZLES) },
                        icon = { Icon(Icons.Default.Extension, contentDescription = "Puzzles", modifier = Modifier.size(22.dp)) },
                        label = { Text(AppStrings.navPuzzles(language), fontSize = 11.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_puzzles")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.TUTOR,
                        onClick = { viewModel.navigateTo(AppScreen.TUTOR) },
                        icon = { Icon(Icons.Default.Psychology, contentDescription = "AI Tutor", modifier = Modifier.size(22.dp)) },
                        label = { Text(AppStrings.navTutor(language), fontSize = 11.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_tutor")
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = DarkNavyBg
        ) {
            when (currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        userProgress = userProgress,
                        recentGames = recentGames,
                        language = language,
                        onNavigate = { viewModel.navigateTo(it) },
                        onStartPlay = {
                            viewModel.setPlayMode(it)
                            viewModel.navigateTo(AppScreen.PLAY)
                        },
                        onOpenLesson = { viewModel.openLesson(it) },
                        onOpenPuzzle = { viewModel.openPuzzle(it) }
                    )
                }
                AppScreen.LEARN -> {
                    LearnScreen(
                        userProgress = userProgress,
                        language = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onOpenLesson = { viewModel.openLesson(it) }
                    )
                }
                AppScreen.LESSON_DETAIL -> {
                    activeLesson?.let { lesson ->
                        LessonDetailScreen(
                            viewModel = viewModel,
                            lesson = lesson,
                            language = language,
                            onBack = { viewModel.navigateTo(AppScreen.LEARN) }
                        )
                    } ?: run {
                        viewModel.navigateTo(AppScreen.LEARN)
                    }
                }
                AppScreen.PLAY -> {
                    PlayScreen(
                        viewModel = viewModel,
                        language = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onOpenTutor = { viewModel.navigateTo(AppScreen.TUTOR) }
                    )
                }
                AppScreen.PUZZLES -> {
                    PuzzlesScreen(
                        viewModel = viewModel,
                        language = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.TUTOR -> {
                    TutorScreen(
                        viewModel = viewModel,
                        language = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        viewModel = viewModel,
                        language = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.ACHIEVEMENTS -> {
                    AchievementsScreen(
                        userProgress = userProgress,
                        language = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
            }
        }
    }
}

@Composable
private fun navItemColors(): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor = PrimaryPurple,
        selectedTextColor = PrimaryPurple,
        unselectedIconColor = TextSecondary,
        unselectedTextColor = TextSecondary,
        indicatorColor = PrimaryPurple.copy(alpha = 0.2f)
    )
}
