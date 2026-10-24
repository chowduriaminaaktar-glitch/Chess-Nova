package com.example.chess.ui

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chess.ai.GeminiTutorService
import com.example.chess.ai.TutorAnalysisResult
import com.example.chess.data.AppDatabase
import com.example.chess.data.AppSettingsEntity
import com.example.chess.data.UserProgress
import com.example.chess.data.UserRepository
import com.example.chess.engine.ChessEngine
import com.example.chess.engine.EngineDifficulty
import com.example.chess.lessons.CurriculumData
import com.example.chess.lessons.Lesson
import com.example.chess.model.*
import com.example.chess.puzzles.ChessPuzzle
import com.example.chess.puzzles.DailyPuzzles
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    LEARN,
    LESSON_DETAIL,
    PLAY,
    PUZZLES,
    TUTOR,
    SETTINGS,
    ACHIEVEMENTS
}

enum class PlayMode {
    VS_COMPUTER,
    LOCAL_2P,
    PRACTICE
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(AppDatabase.getDatabase(application))

    val userProgress: StateFlow<UserProgress> = repository.progressFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProgress(1, 0, 1, emptySet(), emptySet(), emptySet())
    )

    val settings: StateFlow<AppSettingsEntity> = repository.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettingsEntity()
    )

    val recentGames = repository.recentGamesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Active Lesson state
    private val _activeLesson = MutableStateFlow<Lesson?>(null)
    val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

    private val _lessonBoard = MutableStateFlow(Board())
    val lessonBoard: StateFlow<Board> = _lessonBoard.asStateFlow()

    private val _lessonChallengeSolved = MutableStateFlow(false)
    val lessonChallengeSolved: StateFlow<Boolean> = _lessonChallengeSolved.asStateFlow()

    private val _lessonQuizSelectedOption = MutableStateFlow<Int?>(null)
    val lessonQuizSelectedOption: StateFlow<Int?> = _lessonQuizSelectedOption.asStateFlow()

    private val _lessonQuizSubmitted = MutableStateFlow(false)
    val lessonQuizSubmitted: StateFlow<Boolean> = _lessonQuizSubmitted.asStateFlow()

    private val _lessonHintRevealed = MutableStateFlow(false)
    val lessonHintRevealed: StateFlow<Boolean> = _lessonHintRevealed.asStateFlow()

    // Play Screen state
    private val _playMode = MutableStateFlow(PlayMode.VS_COMPUTER)
    val playMode: StateFlow<PlayMode> = _playMode.asStateFlow()

    private val _engineDifficulty = MutableStateFlow(EngineDifficulty.MEDIUM)
    val engineDifficulty: StateFlow<EngineDifficulty> = _engineDifficulty.asStateFlow()

    val game = ChessGame()

    private val _gameBoard = MutableStateFlow(game.board)
    val gameBoard: StateFlow<Board> = _gameBoard.asStateFlow()

    private val _selectedSquare = MutableStateFlow<Square?>(null)
    val selectedSquare: StateFlow<Square?> = _selectedSquare.asStateFlow()

    private val _validDestinations = MutableStateFlow<List<Square>>(emptyList())
    val validDestinations: StateFlow<List<Square>> = _validDestinations.asStateFlow()

    private val _lastMove = MutableStateFlow<Move?>(null)
    val lastMove: StateFlow<Move?> = _lastMove.asStateFlow()

    private val _isBoardFlipped = MutableStateFlow(false)
    val isBoardFlipped: StateFlow<Boolean> = _isBoardFlipped.asStateFlow()

    private val _pendingPromotionMove = MutableStateFlow<Pair<Square, Square>?>(null)
    val pendingPromotionMove: StateFlow<Pair<Square, Square>?> = _pendingPromotionMove.asStateFlow()

    private val _isEngineThinking = MutableStateFlow(false)
    val isEngineThinking: StateFlow<Boolean> = _isEngineThinking.asStateFlow()

    private val _suggestedHintMove = MutableStateFlow<Move?>(null)
    val suggestedHintMove: StateFlow<Move?> = _suggestedHintMove.asStateFlow()

    private val _autoFlipInTwoPlayer = MutableStateFlow(false)
    val autoFlipInTwoPlayer: StateFlow<Boolean> = _autoFlipInTwoPlayer.asStateFlow()

    private val _boardEvaluation = MutableStateFlow(0)
    val boardEvaluation: StateFlow<Int> = _boardEvaluation.asStateFlow()

    // Clocks
    private val _whiteClockSeconds = MutableStateFlow(300)
    val whiteClockSeconds: StateFlow<Int> = _whiteClockSeconds.asStateFlow()

    private val _blackClockSeconds = MutableStateFlow(300)
    val blackClockSeconds: StateFlow<Int> = _blackClockSeconds.asStateFlow()

    private var clockJob: Job? = null

    // Puzzles state
    private val _currentPuzzle = MutableStateFlow(DailyPuzzles.getTodayPuzzle())
    val currentPuzzle: StateFlow<ChessPuzzle> = _currentPuzzle.asStateFlow()

    private val _puzzleBoard = MutableStateFlow(Board.fromFen(DailyPuzzles.getTodayPuzzle().startingFen))
    val puzzleBoard: StateFlow<Board> = _puzzleBoard.asStateFlow()

    private val _puzzleSelectedSquare = MutableStateFlow<Square?>(null)
    val puzzleSelectedSquare: StateFlow<Square?> = _puzzleSelectedSquare.asStateFlow()

    private val _puzzleSolved = MutableStateFlow(false)
    val puzzleSolved: StateFlow<Boolean> = _puzzleSolved.asStateFlow()

    private val _puzzleHintRevealed = MutableStateFlow(false)
    val puzzleHintRevealed: StateFlow<Boolean> = _puzzleHintRevealed.asStateFlow()

    private val _puzzleFeedback = MutableStateFlow<String?>(null)
    val puzzleFeedback: StateFlow<String?> = _puzzleFeedback.asStateFlow()

    // Tutor state
    private val _tutorResult = MutableStateFlow<TutorAnalysisResult?>(null)
    val tutorResult: StateFlow<TutorAnalysisResult?> = _tutorResult.asStateFlow()

    private val _isTutorAnalyzing = MutableStateFlow(false)
    val isTutorAnalyzing: StateFlow<Boolean> = _isTutorAnalyzing.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkDailyActivity()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Play actions
    fun setPlayMode(mode: PlayMode) {
        _playMode.value = mode
        resetGame()
    }

    fun setEngineDifficulty(difficulty: EngineDifficulty) {
        _engineDifficulty.value = difficulty
        resetGame()
    }

    fun resetGame() {
        game.reset()
        _gameBoard.value = game.board.copy()
        _selectedSquare.value = null
        _validDestinations.value = emptyList()
        _lastMove.value = null
        _pendingPromotionMove.value = null
        _suggestedHintMove.value = null
        _boardEvaluation.value = ChessEngine.evaluateBoard(game.board)
        _isBoardFlipped.value = false
        resetClock()
    }

    fun toggleAutoFlipInTwoPlayer() {
        _autoFlipInTwoPlayer.value = !_autoFlipInTwoPlayer.value
        if (_autoFlipInTwoPlayer.value && _playMode.value == PlayMode.LOCAL_2P) {
            _isBoardFlipped.value = (game.board.turn == PieceColor.BLACK)
        }
    }

    fun requestPracticeHint() {
        if (_isEngineThinking.value || game.isGameOver()) return
        _isEngineThinking.value = true
        viewModelScope.launch {
            val bestMove = ChessEngine.searchBestMove(game, EngineDifficulty.HARD)
            _suggestedHintMove.value = bestMove
            _isEngineThinking.value = false
        }
    }

    private fun resetClock() {
        clockJob?.cancel()
        val minutes = settings.value.clockMinutes
        if (minutes > 0) {
            _whiteClockSeconds.value = minutes * 60
            _blackClockSeconds.value = minutes * 60
            startClock()
        }
    }

    private fun startClock() {
        clockJob?.cancel()
        if (settings.value.clockMinutes <= 0) return

        clockJob = viewModelScope.launch {
            while (!game.isGameOver()) {
                delay(1000L)
                if (game.board.turn == PieceColor.WHITE) {
                    if (_whiteClockSeconds.value > 0) {
                        _whiteClockSeconds.value -= 1
                    } else {
                        game.resign(PieceColor.WHITE)
                        _gameBoard.value = game.board.copy()
                        break
                    }
                } else {
                    if (_blackClockSeconds.value > 0) {
                        _blackClockSeconds.value -= 1
                    } else {
                        game.resign(PieceColor.BLACK)
                        _gameBoard.value = game.board.copy()
                        break
                    }
                }
            }
        }
    }

    fun toggleBoardFlip() {
        _isBoardFlipped.value = !_isBoardFlipped.value
    }

    fun onGameSquareClicked(square: Square) {
        if (game.isGameOver() || _isEngineThinking.value) return

        // If playing vs computer and it's Black's turn, ignore clicks
        if (_playMode.value == PlayMode.VS_COMPUTER && game.board.turn == PieceColor.BLACK) return

        val currentSelected = _selectedSquare.value
        if (currentSelected == null) {
            // Select piece if it matches the side to move
            val piece = game.board[square]
            if (piece != null && piece.color == game.board.turn) {
                _selectedSquare.value = square
                val legalMoves = game.getLegalMovesFrom(square)
                _validDestinations.value = legalMoves.map { it.to }.distinct()
            }
        } else {
            if (currentSelected == square) {
                // Deselect
                _selectedSquare.value = null
                _validDestinations.value = emptyList()
                return
            }

            // Check if clicking another friendly piece
            val piece = game.board[square]
            if (piece != null && piece.color == game.board.turn) {
                _selectedSquare.value = square
                val legalMoves = game.getLegalMovesFrom(square)
                _validDestinations.value = legalMoves.map { it.to }.distinct()
                return
            }

            // Attempt to move
            val matchingMoves = game.getLegalMoves().filter { it.from == currentSelected && it.to == square }
            if (matchingMoves.isNotEmpty()) {
                val requiresPromotion = matchingMoves.any { it.promotionPiece != null }
                if (requiresPromotion) {
                    _pendingPromotionMove.value = currentSelected to square
                    return
                }

                executeGameMove(matchingMoves.first())
            } else {
                _selectedSquare.value = null
                _validDestinations.value = emptyList()
            }
        }
    }

    fun onPromotionPieceSelected(pieceType: PieceType) {
        val pending = _pendingPromotionMove.value ?: return
        val matchingMoves = game.getLegalMoves().filter {
            it.from == pending.first && it.to == pending.second && it.promotionPiece == pieceType
        }
        _pendingPromotionMove.value = null
        if (matchingMoves.isNotEmpty()) {
            executeGameMove(matchingMoves.first())
        }
    }

    fun dismissPromotion() {
        _pendingPromotionMove.value = null
        _selectedSquare.value = null
        _validDestinations.value = emptyList()
    }

    private fun executeGameMove(move: Move) {
        val success = game.makeMove(move)
        if (success) {
            _gameBoard.value = game.board.copy()
            _lastMove.value = move
            _selectedSquare.value = null
            _validDestinations.value = emptyList()
            _suggestedHintMove.value = null
            _boardEvaluation.value = ChessEngine.evaluateBoard(game.board)

            // Audio & Haptics feedback
            val context = getApplication<Application>()
            val sound = settings.value.soundEnabled
            val haptics = settings.value.hapticsEnabled

            if (game.isGameOver()) {
                ChessSoundEffects.playSuccessSound(context, sound, haptics)
                clockJob?.cancel()
                recordFinishedGame()
                return
            } else if (game.status == GameStatus.CHECK) {
                ChessSoundEffects.playCheckSound(context, sound, haptics)
            } else if (move.isCapture) {
                ChessSoundEffects.playCaptureSound(context, sound, haptics)
            } else {
                ChessSoundEffects.playMoveSound(context, sound, haptics)
            }

            // Auto-flip in 2P mode if toggled
            if (_playMode.value == PlayMode.LOCAL_2P && _autoFlipInTwoPlayer.value) {
                _isBoardFlipped.value = (game.board.turn == PieceColor.BLACK)
            }

            // If Vs Computer and it's Black's turn, trigger engine search
            if (_playMode.value == PlayMode.VS_COMPUTER && game.board.turn == PieceColor.BLACK) {
                triggerEngineMove()
            }
        }
    }

    private var engineJob: Job? = null

    private fun triggerEngineMove() {
        engineJob?.cancel()
        _isEngineThinking.value = true
        engineJob = viewModelScope.launch {
            // Small human-like thinking delay
            delay(400L)
            val bestMove = ChessEngine.searchBestMove(game, _engineDifficulty.value)
            _isEngineThinking.value = false
            if (bestMove != null) {
                executeGameMove(bestMove)
            }
        }
    }

    fun undoMove() {
        if (game.history.isEmpty()) return
        engineJob?.cancel()
        _isEngineThinking.value = false

        if (_playMode.value == PlayMode.VS_COMPUTER) {
            // Undo computer move if computer made a move, and human move
            game.undoMove()
            if (game.board.turn == PieceColor.BLACK && game.history.isNotEmpty()) {
                game.undoMove()
            }
        } else {
            game.undoMove()
        }

        _gameBoard.value = game.board.copy()
        _lastMove.value = game.history.lastOrNull()?.move
        _selectedSquare.value = null
        _validDestinations.value = emptyList()
        _suggestedHintMove.value = null
        _boardEvaluation.value = ChessEngine.evaluateBoard(game.board)

        if (_playMode.value == PlayMode.LOCAL_2P && _autoFlipInTwoPlayer.value) {
            _isBoardFlipped.value = (game.board.turn == PieceColor.BLACK)
        }

        ChessSoundEffects.playMoveSound(
            getApplication(),
            settings.value.soundEnabled,
            settings.value.hapticsEnabled
        )
    }

    fun resignGame() {
        game.resign(game.board.turn)
        _gameBoard.value = game.board.copy()
        clockJob?.cancel()
        recordFinishedGame()
    }

    private fun recordFinishedGame() {
        viewModelScope.launch {
            val resultStr = when (game.status) {
                GameStatus.CHECKMATE -> if (game.board.turn == PieceColor.WHITE) "LOSS" else "WIN"
                GameStatus.RESIGNATION -> if (game.resigningColor == PieceColor.WHITE) "LOSS" else "WIN"
                else -> "DRAW"
            }
            val pgn = game.history.mapIndexed { i, h ->
                if (i % 2 == 0) "${(i/2)+1}. ${h.move.san}" else h.move.san
            }.joinToString(" ")

            repository.recordGame(
                gameMode = _playMode.value.name,
                difficulty = if (_playMode.value == PlayMode.VS_COMPUTER) _engineDifficulty.value.name else null,
                result = resultStr,
                movesCount = game.history.size,
                pgn = pgn,
                playerColor = "WHITE"
            )
        }
    }

    // Lessons actions
    fun openLesson(lesson: Lesson) {
        _activeLesson.value = lesson
        _lessonBoard.value = if (lesson.interactiveChallenge != null) {
            Board.fromFen(lesson.interactiveChallenge.startingFen)
        } else {
            Board()
        }
        _lessonChallengeSolved.value = false
        _lessonQuizSelectedOption.value = null
        _lessonQuizSubmitted.value = false
        _lessonHintRevealed.value = false
        _currentScreen.value = AppScreen.LESSON_DETAIL
    }

    fun revealLessonHint() {
        _lessonHintRevealed.value = true
    }

    fun onLessonSquareClicked(square: Square) {
        val lesson = _activeLesson.value ?: return
        val challenge = lesson.interactiveChallenge ?: return
        if (_lessonChallengeSolved.value) return

        val selected = _selectedSquare.value
        if (selected == null) {
            val piece = _lessonBoard.value[square]
            if (piece != null && piece.color == _lessonBoard.value.turn) {
                _selectedSquare.value = square
                val moves = ChessGame.generatePseudoLegalMoves(_lessonBoard.value, _lessonBoard.value.turn)
                _validDestinations.value = moves.filter { it.from == square }.map { it.to }
            }
        } else {
            if (selected == square) {
                _selectedSquare.value = null
                _validDestinations.value = emptyList()
                return
            }

            val expectedFrom = Square.fromAlgebraic(challenge.expectedFromAlg)
            val expectedTo = Square.fromAlgebraic(challenge.expectedToAlg)

            if (selected == expectedFrom && square == expectedTo) {
                // Correct move!
                val piece = _lessonBoard.value[selected]
                _lessonBoard.value[square] = piece
                _lessonBoard.value[selected] = null
                _lessonBoard.value = _lessonBoard.value.copy()
                _lessonChallengeSolved.value = true
                _selectedSquare.value = null
                _validDestinations.value = emptyList()
                ChessSoundEffects.playSuccessSound(
                    getApplication(),
                    settings.value.soundEnabled,
                    settings.value.hapticsEnabled
                )
            } else {
                _selectedSquare.value = null
                _validDestinations.value = emptyList()
                ChessSoundEffects.playMoveSound(
                    getApplication(),
                    settings.value.soundEnabled,
                    settings.value.hapticsEnabled
                )
            }
        }
    }

    fun selectQuizOption(index: Int) {
        if (!_lessonQuizSubmitted.value) {
            _lessonQuizSelectedOption.value = index
        }
    }

    fun submitQuiz() {
        val selected = _lessonQuizSelectedOption.value ?: return
        val lesson = _activeLesson.value ?: return
        _lessonQuizSubmitted.value = true

        if (selected == lesson.quizQuestion.correctIndex) {
            ChessSoundEffects.playSuccessSound(
                getApplication(),
                settings.value.soundEnabled,
                settings.value.hapticsEnabled
            )
            viewModelScope.launch {
                repository.completeLesson(lesson.id, lesson.xpReward)
            }
        } else {
            ChessSoundEffects.playCheckSound(
                getApplication(),
                settings.value.soundEnabled,
                settings.value.hapticsEnabled
            )
        }
    }

    // Puzzle actions
    fun openPuzzle(puzzle: ChessPuzzle) {
        _currentPuzzle.value = puzzle
        _puzzleBoard.value = Board.fromFen(puzzle.startingFen)
        _puzzleSelectedSquare.value = null
        _puzzleSolved.value = false
        _puzzleHintRevealed.value = false
        _puzzleFeedback.value = null
        _currentScreen.value = AppScreen.PUZZLES
    }

    fun revealPuzzleHint() {
        _puzzleHintRevealed.value = true
    }

    fun onPuzzleSquareClicked(square: Square) {
        val puzzle = _currentPuzzle.value
        if (_puzzleSolved.value) return

        val selected = _puzzleSelectedSquare.value
        if (selected == null) {
            val piece = _puzzleBoard.value[square]
            if (piece != null && piece.color == _puzzleBoard.value.turn) {
                _puzzleSelectedSquare.value = square
            }
        } else {
            if (selected == square) {
                _puzzleSelectedSquare.value = null
                return
            }

            val expectedFrom = Square.fromAlgebraic(puzzle.expectedFromAlg)
            val expectedTo = Square.fromAlgebraic(puzzle.expectedToAlg)

            if (selected == expectedFrom && square == expectedTo) {
                // Solved!
                val piece = _puzzleBoard.value[selected]
                _puzzleBoard.value[square] = piece
                _puzzleBoard.value[selected] = null
                _puzzleBoard.value = _puzzleBoard.value.copy()
                _puzzleSolved.value = true
                _puzzleSelectedSquare.value = null
                _puzzleFeedback.value = puzzle.solutionExplanationEn

                ChessSoundEffects.playSuccessSound(
                    getApplication(),
                    settings.value.soundEnabled,
                    settings.value.hapticsEnabled
                )

                viewModelScope.launch {
                    repository.completePuzzle(puzzle.id, puzzle.xpReward)
                }
            } else {
                _puzzleSelectedSquare.value = null
                _puzzleFeedback.value = "Incorrect move. Try again or check the hint!"
                ChessSoundEffects.playMoveSound(
                    getApplication(),
                    settings.value.soundEnabled,
                    settings.value.hapticsEnabled
                )
            }
        }
    }

    // Tutor actions
    fun analyzeCurrentPosition(question: String? = null) {
        _isTutorAnalyzing.value = true
        viewModelScope.launch {
            val fen = game.board.toFen()
            val historySan = game.history.map { it.move.san }
            val suggested = ChessEngine.searchBestMove(game, EngineDifficulty.HARD)

            val result = GeminiTutorService.analyzePosition(
                fen = fen,
                moveHistorySan = historySan,
                suggestedMove = suggested,
                userQuestion = question,
                language = settings.value.language
            )
            _tutorResult.value = result
            _isTutorAnalyzing.value = false
        }
    }

    // Settings
    fun setLanguage(lang: String) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(language = lang) }
        }
    }

    fun setBoardTheme(theme: String) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(boardTheme = theme) }
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(soundEnabled = enabled) }
        }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(hapticsEnabled = enabled) }
        }
    }

    fun setClockMinutes(minutes: Int) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(clockMinutes = minutes) }
            resetClock()
        }
    }

    fun setPassTurnScreen(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(passTurnScreenEnabled = enabled) }
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetProgress()
            resetGame()
        }
    }
}
