package com.example.chess.ui

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object ChessSoundEffects {
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 75)
        } catch (_: Exception) {
            toneGenerator = null
        }
    }

    fun playMoveSound(context: Context, soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 35)
            } catch (_: Exception) {}
        }
        if (hapticsEnabled) {
            vibrate(context, 15)
        }
    }

    fun playCaptureSound(context: Context, soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 70)
            } catch (_: Exception) {}
        }
        if (hapticsEnabled) {
            vibrate(context, 35)
        }
    }

    fun playCheckSound(context: Context, soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 100)
            } catch (_: Exception) {}
        }
        if (hapticsEnabled) {
            vibrate(context, 55)
        }
    }

    fun playSuccessSound(context: Context, soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 140)
            } catch (_: Exception) {}
        }
        if (hapticsEnabled) {
            vibrate(context, 40)
        }
    }

    private fun vibrate(context: Context, durationMs: Long) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {}
    }
}
