package com.example.chess.ui.localization

object AppStrings {
    fun appName(lang: String): String = "ChessNova"
    fun tagline(lang: String): String = if (lang == "bn") "শিখুন • চর্চা করুন • দক্ষ হোন" else "Learn • Practice • Master"

    fun navHome(lang: String): String = if (lang == "bn") "হোম" else "Home"
    fun navLearn(lang: String): String = if (lang == "bn") "শিখুন" else "Learn"
    fun navPlay(lang: String): String = if (lang == "bn") "খেলুন" else "Play"
    fun navPuzzles(lang: String): String = if (lang == "bn") "ধাঁধা" else "Puzzles"
    fun navTutor(lang: String): String = if (lang == "bn") "এআই শিক্ষক" else "AI Tutor"
    fun navSettings(lang: String): String = if (lang == "bn") "সেটিংস" else "Settings"

    fun levelTitle(lang: String, level: Int): String {
        val title = when (level) {
            1 -> if (lang == "bn") "শিক্ষানবিস" else "Novice"
            2 -> if (lang == "bn") "শিষ্য" else "Apprentice"
            3 -> if (lang == "bn") "কৌশলী" else "Tactician"
            4 -> if (lang == "bn") "রণকৌশলী" else "Strategist"
            5 -> if (lang == "bn") "উন্নত খেলোয়াড়" else "Advanced Player"
            6 -> if (lang == "bn") "ক্যান্ডিডেট মাস্টার" else "Candidate Master"
            7 -> if (lang == "bn") "মাস্টার" else "Master"
            8 -> if (lang == "bn") "আন্তর্জাতিক মাস্টার" else "International Master"
            else -> if (lang == "bn") "গ্র্যান্ডমাস্টার" else "Grandmaster"
        }
        return if (lang == "bn") "লেভেল $level: $title" else "Level $level: $title"
    }

    fun streakDays(lang: String, days: Int): String = if (lang == "bn") "$days দিনের স্ট্রিক" else "$days Day Streak"
    fun xpPoints(lang: String, xp: Int): String = if (lang == "bn") "$xp এক্সপি" else "$xp XP"
    fun continueLearning(lang: String): String = if (lang == "bn") "শেখা চালিয়ে যান" else "Continue Learning"
    fun learnChessBig(lang: String): String = if (lang == "bn") "দাবা শিখুন" else "LEARN CHESS"
    fun learnChessSubtitle(lang: String): String = if (lang == "bn") "১২টি সম্পূর্ণ মডিউল ও ইন্টারঅ্যাকটিভ কুইজ" else "12 structured modules & interactive challenges"
    fun playChessBig(lang: String): String = if (lang == "bn") "দাবা খেলুন" else "PLAY CHESS"
    fun playChessSubtitle(lang: String): String = if (lang == "bn") "কম্পিউটার বনাম, লোকাল ২-প্লেয়ার ও প্র্যাকটিস" else "Vs Computer, Local 2-Player & Practice Mode"
    fun dailyPuzzle(lang: String): String = if (lang == "bn") "দৈনিক ধাঁধা" else "Daily Chess Puzzle"
    fun solvePuzzle(lang: String): String = if (lang == "bn") "ধাঁধা সমাধান করুন" else "Solve Puzzle"
    fun recentGames(lang: String): String = if (lang == "bn") "সাম্প্রতিক খেলাসমূহ" else "Recent Games"
    fun noGamesYet(lang: String): String = if (lang == "bn") "এখনও কোনো খেলা সম্পন্ন হয়নি। একটি নতুন খেলা শুরু করুন!" else "No games played yet. Start a match!"

    // Play modes
    fun vsComputer(lang: String): String = if (lang == "bn") "কম্পিউটার বনাম" else "Vs Computer"
    fun localTwoPlayer(lang: String): String = if (lang == "bn") "এক ফোনে ২ খেলোয়াড়" else "Local 2-Player"
    fun practiceMode(lang: String): String = if (lang == "bn") "অনুশীলন মোড" else "Practice Mode"

    fun easy(lang: String): String = if (lang == "bn") "সহজ (৯০০ রেটিং)" else "Easy (900 Elo)"
    fun medium(lang: String): String = if (lang == "bn") "মাঝারি (১৪৫০ রেটিং)" else "Medium (1450 Elo)"
    fun hard(lang: String): String = if (lang == "bn") "কঠিন (১৮৫০ রেটিং)" else "Hard (1850 Elo)"

    fun whiteTurn(lang: String): String = if (lang == "bn") "সাদার চাল" else "White's Turn"
    fun blackTurn(lang: String): String = if (lang == "bn") "কালোর চাল" else "Black's Turn"
    fun check(lang: String): String = if (lang == "bn") "কিস্তি!" else "CHECK!"
    fun checkmate(lang: String): String = if (lang == "bn") "কিস্তিমাত!" else "CHECKMATE!"
    fun stalemate(lang: String): String = if (lang == "bn") "স্টেলমেট (ড্র)" else "Stalemate (Draw)"
    fun drawFiftyMoves(lang: String): String = if (lang == "bn") "৫০ চালের নিয়মে ড্র" else "Draw (50-Move Rule)"
    fun drawThreefold(lang: String): String = if (lang == "bn") "তিনবার একই অবস্থানের ড্র" else "Draw (Threefold Repetition)"
    fun drawInsufficient(lang: String): String = if (lang == "bn") "অপর্যাপ্ত ঘুঁটিতে ড্র" else "Draw (Insufficient Material)"

    fun undo(lang: String): String = if (lang == "bn") "চাল বাতিল" else "Undo"
    fun restart(lang: String): String = if (lang == "bn") "পুনরায় শুরু" else "Restart"
    fun flipBoard(lang: String): String = if (lang == "bn") "বোর্ড ঘোরান" else "Flip"
    fun hint(lang: String): String = if (lang == "bn") "ইঙ্গিত" else "Hint"
    fun resign(lang: String): String = if (lang == "bn") "আত্মসমর্পণ" else "Resign"
    fun promotePawn(lang: String): String = if (lang == "bn") "বোড়ে প্রমোশন নির্বাচন করুন" else "Promote Pawn to:"
    fun passPhoneMessage(lang: String, nextPlayer: String): String = if (lang == "bn") "চাল সম্পন্ন! ফোনটি $nextPlayer কে দিন এবং ট্যাপ করুন।" else "Turn done! Pass phone to $nextPlayer & tap to reveal."

    fun settingsTitle(lang: String): String = if (lang == "bn") "সেটিংস ও পছন্দসমূহ" else "Settings & Preferences"
    fun language(lang: String): String = if (lang == "bn") "ভাষা / Language" else "Language / ভাষা"
    fun boardTheme(lang: String): String = if (lang == "bn") "দাবাবোর্ডের থিম" else "Board Theme"
    fun soundHaptics(lang: String): String = if (lang == "bn") "শব্দ ও কম্পন" else "Sound & Haptics"
    fun chessClock(lang: String): String = if (lang == "bn") "দাবা ঘড়ি (টাইমার)" else "Chess Clock"
    fun passTurnScreen(lang: String): String = if (lang == "bn") "২ খেলোয়াড়ের চাল বিরতি পর্দা" else "Pass-The-Phone Privacy Screen"
    fun resetProgress(lang: String): String = if (lang == "bn") "অগ্রগতি মুছে ফেলুন" else "Reset Progress"
    fun resetConfirm(lang: String): String = if (lang == "bn") "আপনি কি নিশ্চিত যে আপনার সমস্ত লেভেল, এক্সপি এবং খেলার রেকর্ড মুছে ফেলবেন?" else "Are you sure you want to reset all XP, completed lessons, and game history?"
    fun confirm(lang: String): String = if (lang == "bn") "হ্যাঁ, মুছুন" else "Confirm Reset"
    fun cancel(lang: String): String = if (lang == "bn") "বাতিল" else "Cancel"
}
