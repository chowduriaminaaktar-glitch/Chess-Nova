package com.example.chess.ai

import com.example.BuildConfig
import com.example.chess.engine.ChessEngine
import com.example.chess.model.Board
import com.example.chess.model.Move
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class TutorAnalysisResult(
    val engineEvaluation: String,
    val bestMoveSan: String?,
    val aiExplanation: String,
    val isOfflineFallback: Boolean = false
)

object GeminiTutorService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(25, TimeUnit.SECONDS)
        .build()

    suspend fun analyzePosition(
        fen: String,
        moveHistorySan: List<String>,
        suggestedMove: Move?,
        userQuestion: String? = null,
        language: String = "en"
    ): TutorAnalysisResult = withContext(Dispatchers.IO) {
        val board = Board.fromFen(fen)
        val evalScore = ChessEngine.evaluateBoard(board)
        val evalPawnScore = evalScore / 100.0
        val evalFormatted = if (evalPawnScore >= 0) "+%.2f".format(evalPawnScore) else "%.2f".format(evalPawnScore)
        val suggestedSan = suggestedMove?.san ?: "N/A"

        val engineHeader = if (language == "bn") {
            "ইঞ্জিন মূল্যায়ন: $evalFormatted | প্রস্তাবিত চাল: $suggestedSan"
        } else {
            "Engine Eval: $evalFormatted | Suggested Move: $suggestedSan"
        }

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext TutorAnalysisResult(
                engineEvaluation = engineHeader,
                bestMoveSan = suggestedSan,
                aiExplanation = getLocalOfflineAdvice(board, suggestedSan, language),
                isOfflineFallback = true
            )
        }

        try {
            val systemInstruction = if (language == "bn") {
                "আপনি ChessNova এর একজন বিশ্বমানের বন্ধুভাবাপন্ন দাবা শিক্ষক। শিক্ষার্থীকে সহজ বাংলা ভাষায় বোর্ডের অবস্থান, ঘুঁটির বিকাশ, এবং সেরা চালটি বুঝিয়ে বলুন। শিক্ষার্থীকে ভাবার সুযোগ দিন এবং সরাসরি সমাধান না দিয়ে প্রথমে সূত্র বা ক্লু দিন।"
            } else {
                "You are ChessNova's Grandmaster Chess Tutor. Analyze the given chess position and explain the key ideas clearly and concisely for an eager learner. Offer hints before spoiling the solution, and explain the strategic reason behind the engine's suggested move."
            }

            val userPrompt = """
                Current FEN: $fen
                Recent Moves: ${moveHistorySan.takeLast(6).joinToString(" ")}
                Engine Best Move: $suggestedSan
                Engine Evaluation: $evalFormatted
                User Question/Topic: ${userQuestion ?: "Analyze this position and provide strategic advice."}
                Language requested: ${if (language == "bn") "Bengali (বাংলা)" else "English"}
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", "$systemInstruction\n\n$userPrompt") })
                        })
                    })
                }
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.6)
                    put("maxOutputTokens", 600)
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBodyStr = response.body?.string() ?: ""
                val respJson = JSONObject(responseBodyStr)
                val candidates = respJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text")

                if (!text.isNullOrBlank()) {
                    return@withContext TutorAnalysisResult(
                        engineEvaluation = engineHeader,
                        bestMoveSan = suggestedSan,
                        aiExplanation = text.trim(),
                        isOfflineFallback = false
                    )
                }
            }
        } catch (_: Exception) {
            // Network failure or timeout -> fallback to offline analysis
        }

        return@withContext TutorAnalysisResult(
            engineEvaluation = engineHeader,
            bestMoveSan = suggestedSan,
            aiExplanation = getLocalOfflineAdvice(board, suggestedSan, language),
            isOfflineFallback = true
        )
    }

    private fun getLocalOfflineAdvice(board: Board, suggestedSan: String, language: String): String {
        return if (language == "bn") {
            """
                [অফলাইন শিক্ষক বিশ্লেষণ]
                • বর্তমান অবস্থান: ${if (board.turn == com.example.chess.model.PieceColor.WHITE) "সাদার চাল" else "কালোর চাল"}।
                • প্রস্তাবিত চাল: $suggestedSan।
                • পরামর্শ: কেন্দ্রের নিয়ন্ত্রণ (d4, e4, d5, e5) বজায় রাখুন এবং নিজের রাজাকে ক্যাসলিং করে সুরক্ষিত করুন। শত্রু ঘুঁটির কোনো ফাঁকা আক্রমণ আছে কিনা দেখে নিন।
            """.trimIndent()
        } else {
            """
                [Offline Tutor Analysis]
                • Turn to move: ${board.turn.name.lowercase().replaceFirstChar { it.uppercase() }}.
                • Suggested Candidate Move: $suggestedSan.
                • Coaching Tip: Focus on board center control, piece coordination, and ensuring your king is safely shielded before launching a flank attack.
            """.trimIndent()
        }
    }
}
