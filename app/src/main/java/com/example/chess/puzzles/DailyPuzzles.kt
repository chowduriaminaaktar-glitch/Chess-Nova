package com.example.chess.puzzles

object DailyPuzzles {
    val puzzles: List<ChessPuzzle> = listOf(
        ChessPuzzle(
            id = "puzzle_daily_1",
            titleEn = "Daily Challenge: Back Rank Strike",
            titleBn = "দৈনিক ধাঁধা: ব্যাক র‍্যাঙ্ক মেট",
            descriptionEn = "White to move and deliver an instant checkmate.",
            descriptionBn = "সাদা চাল দিয়ে তৎক্ষণাৎ কিস্তিমাত করুন।",
            startingFen = "3r2k1/5ppp/8/8/8/8/5PPP/1R4K1 w - - 0 1",
            expectedFromAlg = "b1",
            expectedToAlg = "b8",
            hintEn = "Look at the unguarded back rank of Black's king.",
            hintBn = "কালো রাজার পেছনের খোলা সারিতে চোখ রাখুন।",
            solutionExplanationEn = "1. Rb8! pins and overpowers Black's rook on d8, delivering unstoppable checkmate.",
            solutionExplanationBn = "১. Rb8! চাল দিয়ে সরাসরি ব্যাক-র‍্যাঙ্ক কিস্তিমাত নিশ্চিত হলো।"
        ),
        ChessPuzzle(
            id = "puzzle_2",
            titleEn = "The Scholar's Mate Trap",
            titleBn = "স্কলারস মেট ফাঁদ",
            descriptionEn = "White to move and punish Black's careless opening.",
            descriptionBn = "সাদা চাল দিয়ে ভুল চালের চরম শাস্তি দিন।",
            startingFen = "r1bqkb1r/pppp1ppp/2n5/4p3/2B1n3/5Q2/PPPP1PPP/RNB1K1NR w KQkq - 0 1",
            expectedFromAlg = "f3",
            expectedToAlg = "f7",
            hintEn = "Look at the unprotected f7 pawn guarded only by the King.",
            hintBn = "কেবলমাত্র রাজা পাহারা দিচ্ছে এমন f7 বোড়ে লক্ষ্য করুন।",
            solutionExplanationEn = "1. Qxf7# delivers an immediate Scholar's checkmate supported by the bishop on c4.",
            solutionExplanationBn = "১. Qxf7# হাতি ও মন্ত্রীর যৌথ আক্রমণে সাথে সাথে কিস্তিমাত।"
        ),
        ChessPuzzle(
            id = "puzzle_3",
            titleEn = "Royal Fork Decimation",
            titleBn = "রয়্যাল ফর্ক আক্রমণ",
            descriptionEn = "White to play: fork the King and Queen.",
            descriptionBn = "সাদা চাল: রাজা এবং রানিকে একসাথে ফর্ক করুন।",
            startingFen = "4k3/8/8/8/2N5/3q4/8/4K3 w - - 0 1",
            expectedFromAlg = "c4",
            expectedToAlg = "e5",
            hintEn = "Which square allows the Knight to attack both the King and the Queen?",
            hintBn = "কোন ঘরে গেলে ঘোড়া একই সাথে রাজা ও মন্ত্রীকে আক্রমণ করবে?",
            solutionExplanationEn = "1. Ne5+ checks the King on e8 while attacking the Queen on d3. Black must move the king, and White wins the Queen!",
            solutionExplanationBn = "১. Ne5+ কিস্তি দেয় এবং মন্ত্রী শিকার নিশ্চিত করে।"
        ),
        ChessPuzzle(
            id = "puzzle_4",
            titleEn = "The Absolute Pin Breaker",
            titleBn = "চরম পিনের সুযোগ",
            descriptionEn = "White to move: capture the helpless pinned piece.",
            descriptionBn = "সাদা চাল: পিন হওয়া অসহায় ঘুঁটিটিকে তুলে নিন।",
            startingFen = "4k3/4r3/8/8/8/8/8/4R1K1 w - - 0 1",
            expectedFromAlg = "e1",
            expectedToAlg = "e7",
            hintEn = "The black rook is pinned to its king and cannot safely retreat.",
            hintBn = "কালো নৌকাটি রাজার সাথে পিন হয়ে আছে।",
            solutionExplanationEn = "1. Rxe7+ captures Black's rook directly with check.",
            solutionExplanationBn = "১. Rxe7+ কিস্তিসহ নৌকা শিকার করলো।"
        )
    )

    fun getTodayPuzzle(): ChessPuzzle {
        return puzzles[0]
    }

    fun getPuzzleById(id: String): ChessPuzzle? {
        return puzzles.find { it.id == id }
    }
}
