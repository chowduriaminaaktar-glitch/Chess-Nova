package com.example.chess.lessons

import com.example.chess.model.Move

data class LessonModule(
    val id: Int,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val lessons: List<Lesson>
)

data class InteractiveChallenge(
    val startingFen: String,
    val playerIsWhite: Boolean = true,
    val instructionEn: String,
    val instructionBn: String,
    val expectedFromAlg: String,
    val expectedToAlg: String,
    val hintEn: String,
    val hintBn: String,
    val successExplanationEn: String,
    val successExplanationBn: String
)

data class QuizQuestion(
    val questionEn: String,
    val questionBn: String,
    val optionsEn: List<String>,
    val optionsBn: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationBn: String
)

data class Lesson(
    val id: String,
    val moduleId: Int,
    val titleEn: String,
    val titleBn: String,
    val overviewEn: String,
    val overviewBn: String,
    val theoryEn: String,
    val theoryBn: String,
    val interactiveChallenge: InteractiveChallenge?,
    val quizQuestion: QuizQuestion,
    val xpReward: Int = 30
)
