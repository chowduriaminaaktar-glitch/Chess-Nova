package com.example.chess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.chess.ui.MainViewModel
import com.example.chess.ui.localization.AppStrings
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    language: String,
    onBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = AppStrings.settingsTitle(language),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Online/Offline Status Indicator
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                border = BorderStroke(1.dp, DarkNavyBorder)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == "bn") "অফলাইন-ফার্স্ট সক্রিয়" else "Offline-First Active",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (language == "bn") "দাবা খেলা, ইঞ্জিন, পাঠ ও অগ্রগতি সম্পূর্ণ অফলাইনে কাজ করে।" else "Gameplay, engine, lessons & progress work 100% offline.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Language Selection
            SettingsSectionCard(title = AppStrings.language(language)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SelectableOption(
                        label = "English",
                        isSelected = settings.language == "en",
                        onClick = { viewModel.setLanguage("en") },
                        modifier = Modifier.weight(1f)
                    )
                    SelectableOption(
                        label = "বাংলা (Bengali)",
                        isSelected = settings.language == "bn",
                        onClick = { viewModel.setLanguage("bn") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Board Theme
            SettingsSectionCard(title = AppStrings.boardTheme(language)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ThemeOptionRow(
                        name = if (language == "bn") "মিডনাইট নেভি ও ইলেকট্রিক" else "Midnight Navy & Slate",
                        themeKey = "NAVY",
                        lightHex = Color(0xFF334155),
                        darkHex = Color(0xFF1E293B),
                        isSelected = settings.boardTheme == "NAVY",
                        onClick = { viewModel.setBoardTheme("NAVY") }
                    )
                    ThemeOptionRow(
                        name = if (language == "bn") "ক্লাসিক কাঠ" else "Classic Wood",
                        themeKey = "WOOD",
                        lightHex = Color(0xFFF0D9B5),
                        darkHex = Color(0xFFB58863),
                        isSelected = settings.boardTheme == "WOOD",
                        onClick = { viewModel.setBoardTheme("WOOD") }
                    )
                    ThemeOptionRow(
                        name = if (language == "bn") "এমারেল্ড এরিনা" else "Emerald Arena",
                        themeKey = "EMERALD",
                        lightHex = Color(0xFFEEEED2),
                        darkHex = Color(0xFF769656),
                        isSelected = settings.boardTheme == "EMERALD",
                        onClick = { viewModel.setBoardTheme("EMERALD") }
                    )
                }
            }

            // Chess Clock
            SettingsSectionCard(title = AppStrings.chessClock(language)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ClockOption(
                        label = "Unlimited",
                        minutes = 0,
                        current = settings.clockMinutes,
                        onClick = { viewModel.setClockMinutes(0) },
                        modifier = Modifier.weight(1f)
                    )
                    ClockOption(
                        label = "3 min",
                        minutes = 3,
                        current = settings.clockMinutes,
                        onClick = { viewModel.setClockMinutes(3) },
                        modifier = Modifier.weight(1f)
                    )
                    ClockOption(
                        label = "5 min",
                        minutes = 5,
                        current = settings.clockMinutes,
                        onClick = { viewModel.setClockMinutes(5) },
                        modifier = Modifier.weight(1f)
                    )
                    ClockOption(
                        label = "10 min",
                        minutes = 10,
                        current = settings.clockMinutes,
                        onClick = { viewModel.setClockMinutes(10) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Sound & Haptics Toggles
            SettingsSectionCard(title = AppStrings.soundHaptics(language)) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = if (language == "bn") "দাবা চালের শব্দ" else "Chess Sound Effects", fontSize = 14.sp, color = TextPrimary)
                            Text(text = if (language == "bn") "চাল, শিকার ও কিস্তির বাস্তবধর্মী শব্দ" else "Piece moves, captures, and check sounds", fontSize = 11.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = settings.soundEnabled,
                            onCheckedChange = { viewModel.setSoundEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PrimaryPurple)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = if (language == "bn") "হ্যাপটিক কম্পন" else "Haptic Vibrations", fontSize = 14.sp, color = TextPrimary)
                            Text(text = if (language == "bn") "ঘুঁটি শিকার ও কিস্তিতে সূক্ষ্ম কম্পন" else "Tactile feedback on captures and checks", fontSize = 11.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = settings.hapticsEnabled,
                            onCheckedChange = { viewModel.setHapticsEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PrimaryPurple)
                        )
                    }
                }
            }

            // Danger Zone: Reset Progress
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
                border = BorderStroke(1.dp, RoseAlert.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.resetProgress(language),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseAlert
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Reset all XP, unlocked badges, streak, and game records to initial start.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RoseAlert),
                        border = BorderStroke(1.dp, RoseAlert),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("reset_progress_button")
                    ) {
                        Text(text = AppStrings.resetProgress(language))
                    }
                }
            }
        }

        // Reset Confirmation Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text(text = AppStrings.resetProgress(language), color = RoseAlert) },
                text = { Text(text = AppStrings.resetConfirm(language), color = TextPrimary) },
                confirmButton = {
                    Button(
                        onClick = {
                            showResetDialog = false
                            viewModel.resetAllProgress()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoseAlert)
                    ) {
                        Text(text = AppStrings.confirm(language))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text(text = AppStrings.cancel(language), color = TextSecondary)
                    }
                },
                containerColor = DarkNavyCard
            )
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
        border = BorderStroke(1.dp, DarkNavyBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricBlue
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun SelectableOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryPurple.copy(alpha = 0.25f) else DarkNavyBg)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) PrimaryPurple else DarkNavyBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else TextSecondary
        )
    }
}

@Composable
private fun ThemeOptionRow(
    name: String,
    themeKey: String,
    lightHex: Color,
    darkHex: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryPurple.copy(alpha = 0.2f) else DarkNavyBg)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) PrimaryPurple else DarkNavyBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else TextPrimary
        )

        Row {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(lightHex)
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(darkHex)
            )
        }
    }
}

@Composable
private fun ClockOption(
    label: String,
    minutes: Int,
    current: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = (current == minutes)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) ElectricBlue.copy(alpha = 0.2f) else DarkNavyBg)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) ElectricBlue else DarkNavyBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) ElectricBlue else TextSecondary
        )
    }
}
