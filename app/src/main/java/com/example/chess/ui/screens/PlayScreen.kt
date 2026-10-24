package com.example.chess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.*
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
import com.example.chess.engine.EngineDifficulty
import com.example.chess.model.*
import com.example.chess.ui.MainViewModel
import com.example.chess.ui.PlayMode
import com.example.chess.ui.components.*
import com.example.chess.ui.localization.AppStrings
import com.example.ui.theme.*

@Composable
fun PlayScreen(
    viewModel: MainViewModel,
    language: String,
    onBack: () -> Unit,
    onOpenTutor: () -> Unit
) {
    val playMode by viewModel.playMode.collectAsState()
    val difficulty by viewModel.engineDifficulty.collectAsState()
    val board by viewModel.gameBoard.collectAsState()
    val selectedSquare by viewModel.selectedSquare.collectAsState()
    val validDestinations by viewModel.validDestinations.collectAsState()
    val lastMove by viewModel.lastMove.collectAsState()
    val isFlipped by viewModel.isBoardFlipped.collectAsState()
    val pendingPromotion by viewModel.pendingPromotionMove.collectAsState()
    val isThinking by viewModel.isEngineThinking.collectAsState()
    val suggestedHintMove by viewModel.suggestedHintMove.collectAsState()
    val autoFlip by viewModel.autoFlipInTwoPlayer.collectAsState()
    val evalScore by viewModel.boardEvaluation.collectAsState()
    val settings by viewModel.settings.collectAsState()

    val whiteSeconds by viewModel.whiteClockSeconds.collectAsState()
    val blackSeconds by viewModel.blackClockSeconds.collectAsState()

    var showResignConfirm by remember { mutableStateOf(false) }

    val game = viewModel.game
    val isCheck = (game.status == GameStatus.CHECK)
    val isGameOver = game.isGameOver()

    // Material difference calculation
    val whiteBaseValue = game.capturedByWhite.sumOf { it.type.baseValue }
    val blackBaseValue = game.capturedByBlack.sumOf { it.type.baseValue }
    val diff = (whiteBaseValue - blackBaseValue) / 100
    val whiteAdvantage = if (diff > 0) diff else 0
    val blackAdvantage = if (diff < 0) -diff else 0

    Box(modifier = Modifier.fillMaxSize().background(DarkNavyBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar: Navigation, Mode Selector & Quick Actions
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    // Mode segmented selector
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkNavyCard)
                            .padding(2.dp)
                    ) {
                        ModeTab(
                            title = if (language == "bn") "কম্পিউটার" else "Vs Engine",
                            selected = playMode == PlayMode.VS_COMPUTER,
                            onClick = { viewModel.setPlayMode(PlayMode.VS_COMPUTER) }
                        )
                        ModeTab(
                            title = if (language == "bn") "২ খেলোয়াড়" else "Local 2P",
                            selected = playMode == PlayMode.LOCAL_2P,
                            onClick = { viewModel.setPlayMode(PlayMode.LOCAL_2P) }
                        )
                        ModeTab(
                            title = if (language == "bn") "অনুশীলন" else "Practice",
                            selected = playMode == PlayMode.PRACTICE,
                            onClick = { viewModel.setPlayMode(PlayMode.PRACTICE) }
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleBoardFlip() },
                        modifier = Modifier.testTag("flip_board_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlipCameraAndroid,
                            contentDescription = "Flip board",
                            tint = ElectricBlue
                        )
                    }
                }

                // If playing vs computer, show difficulty pills
                if (playMode == PlayMode.VS_COMPUTER) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        DifficultyPill(
                            title = AppStrings.easy(language),
                            selected = difficulty == EngineDifficulty.EASY,
                            onClick = { viewModel.setEngineDifficulty(EngineDifficulty.EASY) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        DifficultyPill(
                            title = AppStrings.medium(language),
                            selected = difficulty == EngineDifficulty.MEDIUM,
                            onClick = { viewModel.setEngineDifficulty(EngineDifficulty.MEDIUM) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        DifficultyPill(
                            title = AppStrings.hard(language),
                            selected = difficulty == EngineDifficulty.HARD,
                            onClick = { viewModel.setEngineDifficulty(EngineDifficulty.HARD) }
                        )
                    }
                }
            }

            // Top Player Card (Black or Opponent)
            PlayerInfoCard(
                playerName = if (playMode == PlayMode.VS_COMPUTER) "ChessNova Bot (${difficulty.name})" else "Black Player",
                isTurn = (board.turn == PieceColor.BLACK && !isGameOver),
                isThinking = (isThinking && board.turn == PieceColor.BLACK),
                capturedPieces = game.capturedByBlack,
                advantage = blackAdvantage,
                isWhite = false,
                language = language
            )

            // Optional Chess Clock (if enabled in settings)
            if (settings.clockMinutes > 0) {
                ChessClockView(
                    whiteTimeSeconds = whiteSeconds,
                    blackTimeSeconds = blackSeconds,
                    isWhiteTurn = (board.turn == PieceColor.WHITE),
                    isClockRunning = !isGameOver
                )
            }

            // Real-time Turn & Evaluation Status Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Active Turn Pill
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (board.turn == PieceColor.WHITE) Color.White else ElectricBlue)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (board.turn == PieceColor.WHITE) AppStrings.whiteTurn(language) else AppStrings.blackTurn(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                // Eval & Mode Helpers
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val evalFormatted = if (evalScore >= 0) "+%.1f".format(evalScore / 100.0) else "%.1f".format(evalScore / 100.0)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(DarkNavyCard)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Eval: $evalFormatted",
                            fontSize = 11.sp,
                            color = if (evalScore >= 0) ElectricBlue else PurpleLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (playMode == PlayMode.LOCAL_2P) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (autoFlip) PrimaryPurple.copy(alpha = 0.25f) else DarkNavyCard)
                                .clickable { viewModel.toggleAutoFlipInTwoPlayer() }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (autoFlip) "Auto-Flip: ON" else "Auto-Flip: OFF",
                                fontSize = 10.sp,
                                color = if (autoFlip) PrimaryPurple else TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Interactive Chess Board
            ChessBoardView(
                board = board,
                selectedSquare = selectedSquare,
                validDestinations = validDestinations,
                lastMove = lastMove,
                hintMove = suggestedHintMove,
                isInCheck = isCheck,
                boardTheme = settings.boardTheme,
                isFlipped = isFlipped,
                interactive = !isGameOver && !isThinking,
                onSquareClicked = { viewModel.onGameSquareClicked(it) }
            )

            // Bottom Player Card (White)
            PlayerInfoCard(
                playerName = if (playMode == PlayMode.LOCAL_2P) "White Player" else "You (White)",
                isTurn = (board.turn == PieceColor.WHITE && !isGameOver),
                isThinking = false,
                capturedPieces = game.capturedByWhite,
                advantage = whiteAdvantage,
                isWhite = true,
                language = language
            )

            // Move History Strip with prominent Quick Undo button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    MoveHistoryRibbon(history = game.history.map { it.move })
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.undoMove() },
                    enabled = game.history.isNotEmpty() && !isThinking,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple,
                        disabledContainerColor = DarkNavyCard
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("ribbon_undo_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = "Undo",
                        tint = if (game.history.isNotEmpty() && !isThinking) Color.White else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = AppStrings.undo(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (game.history.isNotEmpty() && !isThinking) Color.White else TextMuted
                    )
                }
            }

            // Bottom Action Controls: Undo, Hint (Practice), Restart, Resign, AI Coach
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    icon = Icons.AutoMirrored.Filled.Undo,
                    label = AppStrings.undo(language),
                    enabled = game.history.isNotEmpty() && !isGameOver && !isThinking,
                    tint = PurpleLight,
                    onClick = { viewModel.undoMove() },
                    testTag = "undo_button"
                )

                if (playMode == PlayMode.PRACTICE) {
                    ActionButton(
                        icon = Icons.Default.Lightbulb,
                        label = AppStrings.hint(language),
                        enabled = !isGameOver && !isThinking,
                        tint = GoldAccent,
                        onClick = { viewModel.requestPracticeHint() },
                        testTag = "practice_hint_button"
                    )
                }

                ActionButton(
                    icon = Icons.Default.Refresh,
                    label = AppStrings.restart(language),
                    enabled = true,
                    onClick = { viewModel.resetGame() },
                    testTag = "restart_button"
                )

                ActionButton(
                    icon = Icons.Default.Flag,
                    label = AppStrings.resign(language),
                    enabled = !isGameOver,
                    onClick = { showResignConfirm = true },
                    testTag = "resign_button"
                )

                ActionButton(
                    icon = Icons.Default.Psychology,
                    label = AppStrings.navTutor(language),
                    enabled = true,
                    tint = GoldAccent,
                    onClick = onOpenTutor,
                    testTag = "ai_tutor_action_button"
                )
            }
        }

        // Promotion Piece Chooser Dialog
        if (pendingPromotion != null) {
            PromotionDialog(
                color = board.turn,
                language = language,
                onSelectPiece = { viewModel.onPromotionPieceSelected(it) },
                onDismiss = { viewModel.dismissPromotion() }
            )
        }

        // Resign Confirmation Dialog
        if (showResignConfirm) {
            AlertDialog(
                onDismissRequest = { showResignConfirm = false },
                title = { Text(text = AppStrings.resign(language), color = TextPrimary) },
                text = { Text(text = "Are you sure you want to resign this match?", color = TextSecondary) },
                confirmButton = {
                    TextButton(onClick = {
                        showResignConfirm = false
                        viewModel.resignGame()
                    }) {
                        Text(text = "Resign", color = RoseAlert, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResignConfirm = false }) {
                        Text(text = "Cancel", color = TextSecondary)
                    }
                },
                containerColor = DarkNavyCard
            )
        }

        // Game Over Banner Dialog
        if (isGameOver) {
            GameOverModal(
                status = game.status,
                resigningColor = game.resigningColor,
                winnerColor = if (game.status == GameStatus.CHECKMATE) game.board.turn.opposite else null,
                language = language,
                onRematch = { viewModel.resetGame() },
                onUndo = { viewModel.undoMove() }
            )
        }
    }
}

@Composable
private fun ModeTab(title: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) PrimaryPurple else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color.White else TextSecondary
        )
    }
}

@Composable
private fun DifficultyPill(title: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) ElectricBlue.copy(alpha = 0.2f) else DarkNavyCard)
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) ElectricBlue else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) ElectricBlue else TextSecondary
        )
    }
}

@Composable
private fun PlayerInfoCard(
    playerName: String,
    isTurn: Boolean,
    isThinking: Boolean,
    capturedPieces: List<Piece>,
    advantage: Int,
    isWhite: Boolean,
    language: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isTurn) DarkNavyCardElevated else DarkNavyCard)
            .border(
                width = if (isTurn) 1.5.dp else 0.dp,
                color = if (isTurn) ElectricBlue else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isWhite) Color.White else Color(0xFF0F172A))
                    .border(1.dp, Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isWhite) "♔" else "♚",
                    fontSize = 16.sp,
                    color = if (isWhite) Color.Black else Color.White
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = playerName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (isThinking) {
                    Text(
                        text = "Thinking...",
                        fontSize = 11.sp,
                        color = ElectricBlue,
                        fontWeight = FontWeight.Medium
                    )
                } else if (isTurn) {
                    Text(
                        text = if (language == "bn") "চাল চলছে" else "To move",
                        fontSize = 11.sp,
                        color = EmeraldSuccess
                    )
                }
            }
        }

        CapturedPiecesView(
            capturedPieces = capturedPieces,
            materialAdvantage = advantage,
            isWhite = isWhite,
            modifier = Modifier.widthIn(max = 180.dp)
        )
    }
}

@Composable
private fun MoveHistoryRibbon(history: List<Move>) {
    val scrollState = rememberScrollState()

    LaunchedEffect(history.size) {
        if (history.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(DarkNavyCard)
            .horizontalScroll(scrollState)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (history.isEmpty()) {
            Text(text = "Game started (Move 1)", fontSize = 12.sp, color = TextMuted)
        } else {
            history.chunked(2).forEachIndexed { turnIdx, movePair ->
                val moveNumber = turnIdx + 1
                Text(
                    text = "$moveNumber.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
                Text(
                    text = movePair[0].san,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                if (movePair.size > 1) {
                    Text(
                        text = movePair[1].san,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ElectricBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    enabled: Boolean,
    tint: Color = TextSecondary,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (enabled) tint else TextMuted.copy(alpha = 0.4f),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (enabled) TextSecondary else TextMuted.copy(alpha = 0.4f)
        )
    }
}

@Composable
private fun GameOverModal(
    status: GameStatus,
    resigningColor: PieceColor?,
    winnerColor: PieceColor?,
    language: String,
    onRematch: () -> Unit,
    onUndo: () -> Unit
) {
    val title = when (status) {
        GameStatus.CHECKMATE -> AppStrings.checkmate(language)
        GameStatus.STALEMATE -> AppStrings.stalemate(language)
        GameStatus.DRAW_FIFTY_MOVES -> AppStrings.drawFiftyMoves(language)
        GameStatus.DRAW_THREEFOLD_REPETITION -> AppStrings.drawThreefold(language)
        GameStatus.DRAW_INSUFFICIENT_MATERIAL -> AppStrings.drawInsufficient(language)
        GameStatus.RESIGNATION -> if (language == "bn") "আত্মসমর্পণ" else "Resignation"
        else -> "Game Over"
    }

    val subtitle = when {
        winnerColor != null -> "${winnerColor.name.lowercase().replaceFirstChar { it.uppercase() }} Won by Checkmate!"
        resigningColor != null -> "${resigningColor.name.lowercase().replaceFirstChar { it.uppercase() }} Resigned."
        else -> "Game drawn."
    }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = if (winnerColor == PieceColor.WHITE) EmeraldSuccess else GoldAccent
            )
        },
        text = {
            Column {
                Text(text = subtitle, fontSize = 14.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "+50 XP awarded for playing!",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricBlue
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onRematch,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("rematch_button")
            ) {
                Text(text = "Play Again", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onUndo,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlue),
                border = BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.6f)),
                modifier = Modifier.testTag("game_over_undo_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Undo,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = AppStrings.undo(language))
            }
        },
        containerColor = DarkNavyCard
    )
}
