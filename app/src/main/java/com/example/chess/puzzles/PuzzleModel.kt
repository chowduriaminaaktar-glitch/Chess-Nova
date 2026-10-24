package com.example.chess.puzzles

data class ChessPuzzle(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val startingFen: String,
    val expectedFromAlg: String,
    val expectedToAlg: String,
    val hintEn: String,
    val hintBn: String,
    val solutionExplanationEn: String,
    val solutionExplanationBn: String,
    val rating: Int = 1200,
    val xpReward: Int = 20
)
