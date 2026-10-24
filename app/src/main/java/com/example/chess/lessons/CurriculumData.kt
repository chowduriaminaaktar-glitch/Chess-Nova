package com.example.chess.lessons

object CurriculumData {

    val modules: List<LessonModule> = listOf(
        // MODULE 1
        LessonModule(
            id = 1,
            titleEn = "1. Board & Coordinates",
            titleBn = "১. দাবাবোর্ড ও স্থানাঙ্ক",
            descriptionEn = "Learn files, ranks, square naming, and setup",
            descriptionBn = "ফাইল, র‍্যাঙ্ক ও ঘরের নাম জানা",
            lessons = listOf(
                Lesson(
                    id = "m1_l1",
                    moduleId = 1,
                    titleEn = "Files and Ranks",
                    titleBn = "ফাইল এবং র‍্যাঙ্ক",
                    overviewEn = "The chessboard has 64 squares arranged in 8 vertical files (a to h) and 8 horizontal ranks (1 to 8).",
                    overviewBn = "দাবাবোর্ডে ৬৪টি ঘর রয়েছে যা ৮টি উল্লম্ব ফাইল (a থেকে h) এবং ৮টি অনুভূমিক র‍্যাঙ্কে (১ থেকে ৮) বিভক্ত।",
                    theoryEn = "Every square on the board has a unique coordinate formed by combining its file letter and rank number.\n\n• Vertical columns are called Files (a, b, c, d, e, f, g, h from White's left to right).\n• Horizontal rows are called Ranks (1 to 8, with 1 being White's home row and 8 Black's).\n• The bottom-right corner must always be a light square ('white on right').",
                    theoryBn = "বোর্ডের প্রতিটি ঘরের একটি নির্দিষ্ট নাম রয়েছে যা ফাইল ও র‍্যাঙ্ক দিয়ে তৈরি।\n\n• উল্লম্ব লাইনগুলোকে ফাইল বলা হয় (a থেকে h)।\n• অনুভূমিক লাইনগুলোকে র‍্যাঙ্ক বলা হয় (১ থেকে ৮)।\n• খেলোয়াড়ের ডানদিকের কোণার ঘরটি সর্বদা সাদা হতে হয়।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "8/8/8/8/8/8/8/4P3 w - - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Move the white pawn to the e4 central square.",
                        instructionBn = "সাদা বোড়েটিকে e4 কেন্দ্রীয় ঘরে চালুন।",
                        expectedFromAlg = "e1",
                        expectedToAlg = "e4",
                        hintEn = "Drag or tap the piece on e1 and move it forward to row 4.",
                        hintBn = "e1 ঘরের ঘুঁটিটি ধরে ৪ নম্বর সারিতে চালুন।",
                        successExplanationEn = "Excellent! You navigated to e4, one of the four key central squares.",
                        successExplanationBn = "চমৎকার! আপনি e4 ঘরে পৌঁছেছেন, যা বোর্ডের অত্যন্ত গুরুত্বপূর্ণ কেন্দ্র।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "What is the coordinate of the square in file 'e' on rank 4?",
                        questionBn = "ফাইল 'e' এবং র‍্যাঙ্ক ৪ এর ঘরের নাম কী?",
                        optionsEn = listOf("e4", "4e", "d4", "e5"),
                        optionsBn = listOf("e4", "4e", "d4", "e5"),
                        correctIndex = 0,
                        explanationEn = "In algebraic chess notation, file letter always comes first followed by rank number: e4.",
                        explanationBn = "দাবার নিয়মে প্রথমে ফাইলের অক্ষর এবং পরে র‍্যাঙ্কের সংখ্যা বসে: e4।"
                    )
                )
            )
        ),

        // MODULE 2
        LessonModule(
            id = 2,
            titleEn = "2. Piece Movement & Values",
            titleBn = "২. ঘুঁটির চাল ও মান",
            descriptionEn = "Master all 6 piece types and their point values",
            descriptionBn = "সকল ৬টি ঘুঁটির চাল ও পয়েন্ট মান শিখুন",
            lessons = listOf(
                Lesson(
                    id = "m2_l1",
                    moduleId = 2,
                    titleEn = "Piece Values & Knights",
                    titleBn = "ঘুঁটির মান ও ঘোড়া",
                    overviewEn = "Pawn = 1, Knight/Bishop = 3, Rook = 5, Queen = 9. The King's value is priceless.",
                    overviewBn = "বোড়ে = ১, ঘোড়া/হাতি = ৩, নৌকা = ৫, মন্ত্রী = ৯। রাজার মূল্য অমূল্য।",
                    theoryEn = "Understanding piece values guides tactical decisions:\n\n• Pawn: 1 point. Moves forward, captures diagonally.\n• Knight: 3 points. Moves in an 'L' shape (2 squares in one direction, 1 perpendicular). The ONLY piece that can jump over others!\n• Bishop: 3 points. Diagonal slider.\n• Rook: 5 points. Straight along ranks and files.\n• Queen: 9 points. Combines Rook + Bishop power.\n• King: The game ends if trapped!",
                    theoryBn = "ঘুঁটির মান জানা জরুরি:\n\n• বোড়ে: ১ পয়েন্ট।\n• ঘোড়া: ৩ পয়েন্ট। 'L' আকারে চলে এবং অন্য ঘুঁটির উপর দিয়ে লাফাতে পারে।\n• হাতি: ৩ পয়েন্ট। কোণাকুণি চলে।\n• নৌকা: ৫ পয়েন্ট। সোজা চলে।\n• মন্ত্রী: ৯ পয়েন্ট। সবচেয়ে শক্তিশালী।\n• রাজা: অমূল্য।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "8/8/8/8/8/8/8/4N3 w - - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Jump the Knight from e1 to f3.",
                        instructionBn = "ঘোড়াটিকে e1 থেকে f3 ঘরে লাফিয়ে চালুন।",
                        expectedFromAlg = "e1",
                        expectedToAlg = "f3",
                        hintEn = "Knights move two squares up and one square right to f3.",
                        hintBn = "ঘোড়া দুই ঘর উপরে এবং এক ঘর ডানে f3 তে যায়।",
                        successExplanationEn = "Perfect! The knight is now poised actively towards the center.",
                        successExplanationBn = "দুর্দান্ত! ঘোড়াটি এখন কেন্দ্রের দিকে সক্রিয়।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "Which piece is worth 5 points and can move any number of squares horizontally or vertically?",
                        questionBn = "কোন ঘুঁটির মান ৫ পয়েন্ট এবং সোজা যে কোনো দূরত্ব যেতে পারে?",
                        optionsEn = listOf("Knight", "Bishop", "Rook", "Pawn"),
                        optionsBn = listOf("ঘোড়া", "হাতি", "নৌকা", "বোড়ে"),
                        correctIndex = 2,
                        explanationEn = "The Rook is worth 5 points and controls straight ranks and files.",
                        explanationBn = "নৌকার মান ৫ পয়েন্ট এবং এটি অনুভূমিক ও উল্লম্বভাবে চলে।"
                    )
                )
            )
        ),

        // MODULE 3
        LessonModule(
            id = 3,
            titleEn = "3. Legal Moves & Captures",
            titleBn = "৩. নিয়মসম্মত চাল ও শিকার",
            descriptionEn = "Capturing mechanics, turn order, and legality",
            descriptionBn = "ঘুঁটি শিকার ও চালের পালা",
            lessons = listOf(
                Lesson(
                    id = "m3_l1",
                    moduleId = 3,
                    titleEn = "Capturing Opponent Pieces",
                    titleBn = "শত্রু ঘুঁটি শিকার",
                    overviewEn = "Pieces capture by landing on an occupied enemy square, replacing that piece.",
                    overviewBn = "শত্রুর ঘুঁটি দখল করে সেই স্থানে নিজের ঘুঁটি বসিয়ে শিকার করা হয়।",
                    theoryEn = "In chess:\n\n• White always moves first, then turns alternate.\n• You capture by moving onto the enemy piece's square and removing it from the board.\n• Pawns capture diagonally forward one square, which is different from their forward movement!\n• You cannot jump over pieces, except with the Knight.",
                    theoryBn = "দাবায়:\n\n• সাদা সর্বদা প্রথম চাল দেয়।\n• শত্রুর ঘরে গিয়ে সেই ঘুঁটি তুলে নেওয়া হয়।\n• বোড়ে সোজা এগিয়ে যায় কিন্তু কোণাকুণি শিকার করে।\n• ঘোড়া ছাড়া কোনো ঘুঁটি অন্যটির উপর দিয়ে যেতে পারে না।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "8/8/8/3p4/4P3/8/8/8 w - - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Capture the black pawn on d5 using your pawn on e4.",
                        instructionBn = "e4 এর সাদা বোড়ে দিয়ে d5 এর কালো বোড়েটিকে শিকার করুন।",
                        expectedFromAlg = "e4",
                        expectedToAlg = "d5",
                        hintEn = "Diagonal capture: from e4 into d5.",
                        hintBn = "কোণাকুণি শিকার: e4 থেকে d5 এ যান।",
                        successExplanationEn = "Well done! You executed a pawn diagonal capture (exd5).",
                        successExplanationBn = "দারুণ! আপনি সফলভাবে বোড়ে দিয়ে শিকার করলেন।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "Can a Rook capture an enemy piece by jumping over a friendly piece?",
                        questionBn = "নৌকা কি অন্য ঘুঁটির উপর দিয়ে লাফ দিয়ে শত্রু ঘুঁটি শিকার করতে পারে?",
                        optionsEn = listOf("No, only Knights can jump", "Yes, anytime", "Only during check", "Only on the first move"),
                        optionsBn = listOf("না, শুধু ঘোড়া লাফাতে পারে", "হ্যাঁ, যেকোনো সময়", "শুধু কিস্তির সময়", "শুধু প্রথম চালে"),
                        correctIndex = 0,
                        explanationEn = "Only the Knight possesses the unique ability to leap over other pieces.",
                        explanationBn = "দাবায় কেবল ঘোড়াই অন্য ঘুঁটির উপর দিয়ে লাফ দিতে পারে।"
                    )
                )
            )
        ),

        // MODULE 4
        LessonModule(
            id = 4,
            titleEn = "4. Check, Mate & Stalemate",
            titleBn = "৪. কিস্তি, কিস্তিমাত ও ড্র",
            descriptionEn = "Escaping check, winning by checkmate, and stalemate draws",
            descriptionBn = "কিস্তি এড়ানো, কিস্তিমাত ও অচলাবস্থা",
            lessons = listOf(
                Lesson(
                    id = "m4_l1",
                    moduleId = 4,
                    titleEn = "Check vs Checkmate",
                    titleBn = "কিস্তি বনাম কিস্তিমাত",
                    overviewEn = "Check attacks the king. Checkmate attacks the king with NO legal escape, ending the game in victory.",
                    overviewBn = "কিস্তি মানে রাজাকে আক্রমণ। কিস্তিমাত মানে রাজার কোনো বাঁচার পথ নেই, খেলায় জয়।",
                    theoryEn = "When your King is in check, you MUST get out of check immediately via the CPR rule:\n\n1. C - Capture the attacking piece.\n2. P - Protect (Block the line with another piece).\n3. R - Run (Move the King to a safe square).\n\nIf NONE of these three are possible, it is CHECKMATE and the game is won!\nIf the King is NOT in check but the player has NO legal moves, it is STALEMATE (a draw).",
                    theoryBn = "রাজা আক্রান্ত হলে বাঁচার ৩টি উপায়:\n\n১. আক্রমণকারী ঘুঁটিকে মেরে ফেলা।\n২. মাঝে অন্য ঘুঁটি দিয়ে পথ বন্ধ করা।\n৩. রাজাকে নিরাপদ ঘরে সরিয়ে নেওয়া।\n\nযদি এর কোনোটিই সম্ভব না হয়, তবে তা কিস্তিমাত!",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "6k1/5ppp/8/8/8/8/5PPP/4R1K1 w - - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Deliver back-rank checkmate by moving your Rook to e8.",
                        instructionBn = "নৌকা e8 এ চালিয়ে ব্যাক-র‍্যাঙ্ক কিস্তিমাত করুন।",
                        expectedFromAlg = "e1",
                        expectedToAlg = "e8",
                        hintEn = "Move the Rook all the way up the e-file to row 8.",
                        hintBn = "নৌকাটিকে e ফাইল দিয়ে সরাসরি ৮ নম্বর ঘরে চালুন।",
                        successExplanationEn = "Checkmate! The black King has no escape squares because of its own pawns.",
                        successExplanationBn = "কিস্তিমাত! নিজের বোড়ের বাধার কারণে কালো রাজার পালানোর জায়গা নেই।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "If a player's king is NOT in check, but they have no legal moves left, what is the result?",
                        questionBn = "রাজার ওপর কিস্তি নেই, কিন্তু কোনো বৈধ চালও নেই—ফলাফল কী?",
                        optionsEn = listOf("Stalemate (Draw)", "Checkmate (Win for attacker)", "Loss for inactive player", "Extra turn granted"),
                        optionsBn = listOf("স্টেলমেট বা ড্র", "কিস্তিমাত", "সময় শেষ", "অতিরিক্ত চাল"),
                        correctIndex = 0,
                        explanationEn = "Stalemate occurs when the side to move is not in check and has no legal move. The game ends in an immediate draw.",
                        explanationBn = "স্টেলমেট বা অচলাবস্থায় খেলাটি ড্র হয়।"
                    )
                )
            )
        ),

        // MODULE 5
        LessonModule(
            id = 5,
            titleEn = "5. Castling, Promotion & En Passant",
            titleBn = "৫. ক্যাসলিং, প্রমোশন ও অঁ পাসাঁ",
            descriptionEn = "Master special rules: castling, pawn promotion, and en passant",
            descriptionBn = "বিশেষ চালসমূহ: ক্যাসলিং, প্রমোশন এবং অঁ পাসাঁ",
            lessons = listOf(
                Lesson(
                    id = "m5_l1",
                    moduleId = 5,
                    titleEn = "The Three Special Moves",
                    titleBn = "তিনটি বিশেষ চাল",
                    overviewEn = "Castling safeguards your king, pawn promotion creates queens, and en passant catches double-pawn pushes.",
                    overviewBn = "ক্যাসলিং রাজাকে নিরাপদ করে, প্রমোশন নতুন রানি তৈরি করে এবং অঁ পাসাঁ বিশেষ শিকার।",
                    theoryEn = "• Castling: King moves 2 squares towards a Rook, and Rook hops over. Neither can have moved before, and King cannot castle out of, through, or into check!\n• Pawn Promotion: When a pawn reaches the 8th rank (or 1st for Black), it immediately promotes to a Queen, Rook, Bishop, or Knight.\n• En Passant: If an opponent pawn moves two squares past your pawn, you can capture it diagonally on the very next turn as if it only moved one square.",
                    theoryBn = "• ক্যাসলিং: রাজা ও নৌকার বিশেষ যৌথ চাল যা রাজাকে নিরাপদ করে।\n• প্রমোশন: বোড়ে শেষ ঘরে পৌঁছালে মন্ত্রী বা অন্য ঘুঁটিতে রূপান্তরিত হয়।\n• অঁ পাসাঁ: শত্রু বোড়ে দুই ঘর এগিয়ে এলে তৎক্ষণাৎ কোণাকুণি শিকারের বিশেষ অধিকার।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "3k4/4P3/8/8/8/8/8/4K3 w - - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Advance your pawn to e8 and promote to a Queen!",
                        instructionBn = "বোড়েটিকে e8 এ এগিয়ে নিয়ে রানি বানান!",
                        expectedFromAlg = "e7",
                        expectedToAlg = "e8",
                        hintEn = "Push the pawn to the final rank.",
                        hintBn = "বোড়েটিকে শেষ সারিতে চালুন।",
                        successExplanationEn = "Promoted! A new Queen enters the board, giving White a decisive advantage.",
                        successExplanationBn = "প্রমোশন সম্পন্ন! নতুন রানি বোর্ড দখল করেছে।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "Can you castle if your King is currently in check?",
                        questionBn = "রাজা কিস্তিতে থাকা অবস্থায় কি ক্যাসলিং করা যায়?",
                        optionsEn = listOf("No, you cannot castle out of check", "Yes, to escape check", "Only on Kingside", "Only with Queenside Rook"),
                        optionsBn = listOf("না, কিস্তিতে থাকা অবস্থায় যায় না", "হ্যাঁ, বাঁচার জন্য", "শুধু রাজার দিকে", "শুধু রানির দিকে"),
                        correctIndex = 0,
                        explanationEn = "You cannot castle while in check, nor can the king pass through any attacked square.",
                        explanationBn = "কিস্তিতে থাকা অবস্থায় ক্যাসলিং করার অনুমতি দাবার নিয়মে নেই।"
                    )
                )
            )
        ),

        // MODULE 6
        LessonModule(
            id = 6,
            titleEn = "6. Opening & Center Control",
            titleBn = "৬. সূচনা ও কেন্দ্র নিয়ন্ত্রণ",
            descriptionEn = "Command the center squares (e4, d4, e5, d5)",
            descriptionBn = "বোর্ডের কেন্দ্রীয় ৪টি ঘর নিয়ন্ত্রণ করুন",
            lessons = listOf(
                Lesson(
                    id = "m6_l1",
                    moduleId = 6,
                    titleEn = "Controlling the Center",
                    titleBn = "কেন্দ্র নিয়ন্ত্রণ",
                    overviewEn = "The center squares (e4, d4, e5, d5) are the high ground of the chessboard.",
                    overviewBn = "কেন্দ্রের চারটি ঘর (e4, d4, e5, d5) দাবার সবচেয়ে গুরুত্বপূর্ণ এলাকা।",
                    theoryEn = "Pieces stationed in the center control the maximum number of squares and can pivot rapidly to either flank.\n\nKey opening principles:\n1. Occupy the center with pawns (1.e4 or 1.d4).\n2. Develop knights and bishops toward the center.\n3. Do not move the same piece twice early without good reason.\n4. Castle early to protect your King.",
                    theoryBn = "কেন্দ্রে অবস্থানরত ঘুঁটি সবচেয়ে বেশি ঘর নিয়ন্ত্রণ করতে পারে।\n\nসূচনার মূল নিয়মাবলী:\n১. বোড়ে দিয়ে কেন্দ্র দখল করুন (e4 বা d4)।\n২. ঘোড়া ও হাতিকে কেন্দ্রের দিকে বের করুন।\n৩. বিনা কারণে একই ঘুঁটি দুইবার চালবেন না।\n৪. দ্রুত ক্যাসলিং করে রাজাকে সুরক্ষিত করুন।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Stake your claim in the center by playing 1. e4.",
                        instructionBn = "কেন্দ্র দখলের জন্য প্রথম চালে e4 চালুন।",
                        expectedFromAlg = "e2",
                        expectedToAlg = "e4",
                        hintEn = "Push the King's pawn two squares forward.",
                        hintBn = "রাজার সামনের বোড়েটি দুই ঘর এগিয়ে দিন।",
                        successExplanationEn = "Classic! 1.e4 controls d5 and f5 while opening diagonals for your Queen and Bishop.",
                        successExplanationBn = "চমৎকার! 1.e4 কেন্দ্র নিয়ন্ত্রণ করে এবং হাতি ও মন্ত্রীর পথ খোলে।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "Which four squares comprise the classical center?",
                        questionBn = "দাবাবোর্ডের ধ্রুপদী কেন্দ্র কোন চারটি ঘর নিয়ে গঠিত?",
                        optionsEn = listOf("e4, d4, e5, d5", "a1, a8, h1, h8", "c3, f3, c6, f6", "e1, d1, e8, d8"),
                        optionsBn = listOf("e4, d4, e5, d5", "a1, a8, h1, h8", "c3, f3, c6, f6", "e1, d1, e8, d8"),
                        correctIndex = 0,
                        explanationEn = "e4, d4, e5, and d5 are the four central squares that dictate opening dominance.",
                        explanationBn = "e4, d4, e5 এবং d5 হলো প্রধান কেন্দ্রীয় চারটি ঘর।"
                    )
                )
            )
        ),

        // MODULE 7
        LessonModule(
            id = 7,
            titleEn = "7. Development & King Safety",
            titleBn = "৭. ঘুঁটি বিকাশ ও রাজার সুরক্ষা",
            descriptionEn = "Activate minor pieces and castle your king early",
            descriptionBn = "ঘুঁটি সচল করা ও দ্রুত ক্যাসলিং",
            lessons = listOf(
                Lesson(
                    id = "m7_l1",
                    moduleId = 7,
                    titleEn = "Knights before Bishops",
                    titleBn = "হাতির আগে ঘোড়ার বিকাশ",
                    overviewEn = "Knights have natural squares on f3 and c3, while bishops require pawn structures to be defined.",
                    overviewBn = "ঘোড়া সাধারণত f3 ও c3 তে সহজে বের হয় এবং কেন্দ্র পাহারা দেয়।",
                    theoryEn = "Developing means moving your inactive back-rank pieces into active squares.\n\n• Knights before Bishops is a reliable rule of thumb.\n• Avoid moving too many pawns in the opening; you only need 1 or 2 pawn pushes to release your pieces.\n• Do not bring your Queen out too early, or enemy minor pieces will attack it and gain free developing moves!",
                    theoryBn = "ঘুঁটি বিকাশ মানে পেছনের সারির ঘুঁটিগুলোকে কার্যকর জায়গায় আনা।\n\n• শুরুতেই অনেক বোড়ে চালবেন না।\n• মন্ত্রীকে খুব দ্রুত বের করবেন না, অন্যথায় প্রতিপক্ষ আক্রমণ করে চালের সুবিধা নেবে।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Develop your knight to f3, attacking Black's e5 pawn.",
                        instructionBn = "ঘোড়াটিকে f3 তে বের করে কালো e5 বোড়েকে আক্রমণ করুন।",
                        expectedFromAlg = "g1",
                        expectedToAlg = "f3",
                        hintEn = "Move the knight on g1 to f3.",
                        hintBn = "g1 ঘরের ঘোড়াটি f3 তে আনুন।",
                        successExplanationEn = "Ideal! 1.Nf3 develops with tempo, simultaneously preparing kingside castling.",
                        successExplanationBn = "দুর্দান্ত! ঘোড়া বের হলো এবং কালো বোড়ে আক্রান্ত হলো।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "Why is bringing the Queen out on move 2 generally risky?",
                        questionBn = "দ্বিতীয় চালেই রানি বা মন্ত্রী বের করা ঝুঁকিপূর্ণ কেন?",
                        optionsEn = listOf("It can be harassed by developing enemy pieces", "The queen is too weak", "It is illegal under FIDE rules", "It prevents castling"),
                        optionsBn = listOf("প্রতিপক্ষের ঘুঁটির শিকারে পরিণত হতে পারে", "রানি দুর্বল তাই", "নিয়মবিরুদ্ধ", "ক্যাসলিং আটকে যায়"),
                        correctIndex = 0,
                        explanationEn = "Opponent minor pieces can attack an exposed early queen while developing, earning free tempos.",
                        explanationBn = "শত্রু ঘুঁটি রানিকে আক্রমণ করে সহজে নিজের উন্নয়ন করতে পারে।"
                    )
                )
            )
        ),

        // MODULE 8
        LessonModule(
            id = 8,
            titleEn = "8. Tactical Weapons: Forks & Pins",
            titleBn = "৮. কৌশল: ফর্ক, পিন ও স্কিউয়ার",
            descriptionEn = "Win material with forks, pins, skewers, and discoveries",
            descriptionBn = "ফর্ক, পিন এবং স্কিউয়ারের মাধ্যমে জয়",
            lessons = listOf(
                Lesson(
                    id = "m8_l1",
                    moduleId = 8,
                    titleEn = "The Deadly Knight Fork",
                    titleBn = "মারাত্মক নাইট ফর্ক",
                    overviewEn = "A fork occurs when a single piece attacks two or more enemy pieces at once.",
                    overviewBn = "ফর্ক হলো যখন একটি ঘুঁটি একই সাথে শত্রুর দুই বা ততোধিক ঘুঁটিকে আক্রমণ করে।",
                    theoryEn = "• Fork: Knights are notorious for forking the King and Queen, guaranteeing material win.\n• Pin: A piece is paralyzed because moving it exposes a more valuable piece behind it (Absolute pin if behind piece is King).\n• Skewer: The more valuable piece is in front; when it moves away, the piece behind is captured.\n• Discovered Attack: Moving one piece unleashes an attack from another piece hiding behind it.",
                    theoryBn = "• ফর্ক: একই সাথে একাধিক ঘুঁটিকে আক্রমণ করা।\n• পিন: কোনো ঘুঁটি নড়াচড়া বন্ধ করা কারণ পেছনে মূল্যবান ঘুঁটি রয়েছে।\n• স্কিউয়ার: মূল্যবান ঘুঁটি সামনে থাকে, তা সরলে পেছনেরটি খোয়া যায়।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "4k3/8/8/3q4/8/2N5/8/4K3 w - - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Capture the black queen on d5 or fork king & queen!",
                        instructionBn = "d5 এর কালো মন্ত্রীকে আপনার ঘোড়া দিয়ে শিকার করুন!",
                        expectedFromAlg = "c3",
                        expectedToAlg = "d5",
                        hintEn = "Knight takes queen directly on d5.",
                        hintBn = "ঘোড়া দিয়ে d5 এ যান।",
                        successExplanationEn = "Splendid! Nxd5 captures Black's strongest piece.",
                        successExplanationBn = "চমৎকার! আপনি শত্রুর সবচেয়ে শক্তিশালী ঘুঁটি শিকার করেছেন।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "What is an 'Absolute Pin'?",
                        questionBn = "'অ্যাবসোলুট পিন' বা চরম পিন কাকে বলে?",
                        optionsEn = listOf("A pinned piece cannot move because it would expose the King to check", "A pin on two rooks", "A pin made by a pawn", "A pin that lasts 10 moves"),
                        optionsBn = listOf("যেখানে ঘুঁটি নড়লে রাজায় কিস্তি পড়ে তাই নড়া অসম্ভব", "দুই নৌকার পিন", "বোড়ের পিন", "দীর্ঘস্থায়ী পিন"),
                        correctIndex = 0,
                        explanationEn = "In an absolute pin, moving the pinned piece is strictly illegal because it exposes the King to direct check.",
                        explanationBn = "চরম পিনে থাকা ঘুঁটি চাললে রাজায় কিস্তি হয়, যা সম্পূর্ণ অবৈধ।"
                    )
                )
            )
        ),

        // MODULE 9
        LessonModule(
            id = 9,
            titleEn = "9. Checkmate Patterns",
            titleBn = "৯. পরিচিত কিস্তিমাত কৌশল",
            descriptionEn = "Recognize back-rank mates, scholar's mate, and batteries",
            descriptionBn = "ব্যাক-র‍্যাঙ্ক ও অন্যান্য কিস্তিমাত প্যাটার্ন",
            lessons = listOf(
                Lesson(
                    id = "m9_l1",
                    moduleId = 9,
                    titleEn = "Classic Back-Rank & Batteries",
                    titleBn = "ব্যাক-র‍্যাঙ্ক মেট",
                    overviewEn = "Trapping the king along the back row behind its own protective pawns.",
                    overviewBn = "নিজের বোড়ের পেছনে আটকা পড়া রাজাকে শেষ সারিতে কিস্তিমাত করা।",
                    theoryEn = "Spotting mating patterns finishes games quickly:\n\n• Back-Rank Mate: A rook or queen delivers mate on row 8/1 because the king's pawn shield prevents escape (luft prevents this!).\n• Queen & Bishop Battery: Lining up queen and bishop targeting the fragile h7 or f7 squares.\n• Scholar's Mate: Targeting the weak f7 pawn on move 4 (easy to defend with 1...Nf6 or g6).\n• Smothered Mate: A knight checks a king completely surrounded by friendly pieces.",
                    theoryBn = "কিস্তিমাত প্যাটার্ন চেনা খেলার সমাপ্তি নিশ্চিত করে:\n\n• ব্যাক-র‍্যাঙ্ক মেট: শেষ সারিতে নৌকার আক্রমণ।\n• স্মোদার্ড মেট: ঘোড়া দিয়ে চারপাশ ঘেরা রাজাকে কিস্তিমাত।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "3r2k1/5ppp/8/8/8/8/1Q3PPP/6K1 w - - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Defend against back-rank threat or create an escape square (Luft) with h3!",
                        instructionBn = "রাজার পালানোর পথ (লুফ্ট) তৈরি করতে h3 চালুন!",
                        expectedFromAlg = "h2",
                        expectedToAlg = "h3",
                        hintEn = "Push the h-pawn one square forward.",
                        hintBn = "h ফাইলের বোড়েটি এক ঘর এগিয়ে দিন।",
                        successExplanationEn = "Smart defense! Creating 'luft' ensures your king can step to h2 if checked.",
                        successExplanationBn = "বুদ্ধিমান চাল! রাজা এখন প্রয়োজনে h2 ঘরে সরে যেতে পারবে।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "What does creating 'luft' for your King mean?",
                        questionBn = "দাবায় রাজার জন্য 'লুফ্ট' তৈরি করা বলতে কী বোঝায়?",
                        optionsEn = listOf("Advancing a pawn to give the castled King an escape flight square", "Trading the Queens", "Offering a draw", "Capturing an enemy piece"),
                        optionsBn = listOf("বোড়ে এগিয়ে রাজার পালানোর পথ তৈরি করা", "রানি বদল করা", "ড্র প্রস্তাব", "ঘুঁটি শিকার"),
                        correctIndex = 0,
                        explanationEn = "'Luft' (German for air) means advancing a pawn in front of your castled king to prevent back-rank mate.",
                        explanationBn = "ক্যাসলিং করা রাজার সামনে বোড়ে সরিয়ে পালানোর পথ তৈরি করাকে 'লুফ্ট' বলে।"
                    )
                )
            )
        ),

        // MODULE 10
        LessonModule(
            id = 10,
            titleEn = "10. Endgame Fundamentals",
            titleBn = "১০. শেষ খেলা বা এন্ডগেম",
            descriptionEn = "King and pawn, opposition, and basic rook endgames",
            descriptionBn = "রাজা ও বোড়ে এবং নৌকার শেষ খেলা",
            lessons = listOf(
                Lesson(
                    id = "m10_l1",
                    moduleId = 10,
                    titleEn = "King Opposition & Passed Pawns",
                    titleBn = "অপোজিশন ও পাসড পন",
                    overviewEn = "In the endgame, the King transforms into a fierce attacking and escorting weapon.",
                    overviewBn = "শেষ খেলায় রাজা সবচেয়ে সক্রিয় হয়ে বোড়েকে এগিয়ে নিয়ে যায়।",
                    theoryEn = "When few pieces remain, kings must march into the battle:\n\n• The Opposition: Placing your king directly opposite the enemy king with one square in between forces the opponent to give way.\n• Passed Pawn: A pawn with no opposing pawns in its file or adjacent files to block it. It must be escorted to the 8th rank!\n• Rook Endgames: 'Rooks belong behind passed pawns' (Tarrasch rule).",
                    theoryBn = "শেষ খেলায় রাজা মূল চালিকাশক্তি:\n\n• অপোজিশন: শত্রু রাজার মুখোমুখি হয়ে তাকে সরে যেতে বাধ্য করা।\n• পাসড পন: যে বোড়েকে আটকানোর কোনো শত্রু বোড়ে সামনে নেই।\n• নৌকাকে সর্বদা পাসড পনের পেছনে রাখতে হয়।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "8/4k3/8/4P3/4K3/8/8/8 w - - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Seize the opposition by moving your King to e4 or advance!",
                        instructionBn = "বোড়েটিকে e6 এ এগিয়ে নিয়ে যান!",
                        expectedFromAlg = "e5",
                        expectedToAlg = "e6",
                        hintEn = "Advance the passed pawn forward to e6.",
                        hintBn = "e ফাইলের বোড়েটি e6 ঘরে চালুন।",
                        successExplanationEn = "Great progress! Your passed pawn steps closer to promotion.",
                        successExplanationBn = "চমৎকার! আপনার বোড়েটি প্রমোশনের এক ধাপ কাছে এগিয়ে গেল।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "Where is generally the best place to position your Rook in a Rook & Pawn endgame?",
                        questionBn = "রুক এন্ডগেমে নৌকাকে সাধারণত কোথায় রাখা সবচেয়ে লাভজনক?",
                        optionsEn = listOf("Behind the passed pawn", "In front of the passed pawn", "In the corner", "On the edge of the board"),
                        optionsBn = listOf("পাসড পনের পেছনে", "পাসড পনের সামনে", "কোণায়", "বোর্ডের কিনারায়"),
                        correctIndex = 0,
                        explanationEn = "According to Tarrasch's famous rule, rooks belong behind passed pawns to support advancement or control lines.",
                        explanationBn = "তারাশের বিখ্যাত নিয়ম অনুযায়ী, নৌকাকে সর্বদা পাসড পনের পেছনে রাখা উচিত।"
                    )
                )
            )
        ),

        // MODULE 11
        LessonModule(
            id = 11,
            titleEn = "11. Popular Openings",
            titleBn = "১১. জনপ্রিয় ওপেনিংস বা সূচনা",
            descriptionEn = "Explore Italian Game, Ruy Lopez, Sicilian, and Queen's Gambit",
            descriptionBn = "ইতালিয়ান গেম, সিসিলিয়ান ও কুইন্স গ্যাম্বিট",
            lessons = listOf(
                Lesson(
                    id = "m11_l1",
                    moduleId = 11,
                    titleEn = "The Italian Game (Giuoco Piano)",
                    titleBn = "ইতালিয়ান গেম",
                    overviewEn = "1.e4 e5 2.Nf3 Nc6 3.Bc4 develops rapidly with pressure on the weak f7 point.",
                    overviewBn = "১.e4 e5 ২.Nf3 Nc6 ৩.Bc4 দ্রুত ঘুঁটি বের করে f7 এ চাপ সৃষ্টি করে।",
                    theoryEn = "World-class openings built on timeless principles:\n\n• Italian Game (1.e4 e5 2.Nf3 Nc6 3.Bc4): Rapid piece development targeting f7.\n• Ruy Lopez (3.Bb5): Attacks the knight defending e5.\n• Sicilian Defense (1.e4 c5): Sharp counterattacking response from Black.\n• Queen's Gambit (1.d4 d5 2.c4): White sacrifices a wing pawn to gain overwhelming central domination.",
                    theoryBn = "জনপ্রিয় সূচনা:\n\n• ইতালিয়ান গেম: ৩.Bc4 খেলে f7 এ লক্ষ্য রাখা।\n• সিসিলিয়ান ডিফেন্স: ১...c5 খেলে প্রতিআক্রমণ।\n• কুইন্স গ্যাম্বিট: ২.c4 খেলে কেন্দ্রের নিয়ন্ত্রণ নেওয়া।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Play the Italian Game by developing your Bishop to c4!",
                        instructionBn = "হাতিকে c4 ঘরে চালিয়ে ইতালিয়ান গেম শুরু করুন!",
                        expectedFromAlg = "f1",
                        expectedToAlg = "c4",
                        hintEn = "Move the white light-squared bishop from f1 to c4.",
                        hintBn = "f1 এর সাদা হাতিটি c4 ঘরে আনুন।",
                        successExplanationEn = "Bravo! 3.Bc4 begins the Italian Game, aiming right at f7.",
                        successExplanationBn = "চমৎকার! 3.Bc4 খেলে ইতালিয়ান গেমের সূচনা হলো।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "In the Italian Game, which enemy square does the white bishop on c4 target?",
                        questionBn = "ইতালিয়ান গেমে c4 এর হাতি কোন দুর্বল ঘরটিকে লক্ষ্য করে?",
                        optionsEn = listOf("f7", "e7", "h7", "b7"),
                        optionsBn = listOf("f7", "e7", "h7", "b7"),
                        correctIndex = 0,
                        explanationEn = "f7 is Black's most vulnerable square in the opening because it is guarded solely by the King.",
                        explanationBn = "f7 ঘরটি শুরুতে কেবল রাজা দ্বারা রক্ষিত থাকে বলে এটি সবচেয়ে দুর্বল।"
                    )
                )
            )
        ),

        // MODULE 12
        LessonModule(
            id = 12,
            titleEn = "12. Strategy & Positional Mastery",
            titleBn = "১২. রণকৌশল ও অবস্থানগত দক্ষতা",
            descriptionEn = "Outposts, open files, pawn structures, and long-term plans",
            descriptionBn = "আউটপোস্ট, উন্মুক্ত লাইন ও দীর্ঘমেয়াদী পরিকল্পনা",
            lessons = listOf(
                Lesson(
                    id = "m12_l1",
                    moduleId = 12,
                    titleEn = "Outposts and Open Files",
                    titleBn = "আউটপোস্ট ও ওপেন ফাইল",
                    overviewEn = "Planting a knight on an unassailable outpost square paralyzes enemy coordination.",
                    overviewBn = "নিরাপদ আউটপোস্টে ঘোড়া বসিয়ে প্রতিপক্ষের খেলা ভেঙে দেওয়া।",
                    theoryEn = "Beyond calculating moves lies deep positional strategy:\n\n• Outpost: A square on the 4th, 5th, or 6th rank protected by your pawn that cannot be attacked by enemy pawns. Knights thrive here!\n• Open File: A column with no pawns on it. Rooks should dominate open files.\n• Pawn Chains & Weaknesses: Isolated pawns, backward pawns, and doubled pawns are easy targets.\n• Prophylaxis: Anticipating and preventing your opponent's active plans before they unfold.",
                    theoryBn = "উচ্চস্তরের রণকৌশল:\n\n• আউটপোস্ট: এমন একটি ঘর যেখানে কোনো শত্রু বোড়ে আক্রমণ করতে পারে না।\n• ওপেন ফাইল: কোনো বোড়ে ছাড়া সোজা লাইন, যা নৌকার জন্য আদর্শ।\n• দুর্বল বোড়ে: বিচ্ছিন্ন বোড়েকে লক্ষ্যবস্তু বানানো।",
                    interactiveChallenge = InteractiveChallenge(
                        startingFen = "r1b1k2r/pp2bppp/2n1pn2/2pp4/3P4/2N1PN2/PPP1BPPP/R2Q1RK1 w kq - 0 1",
                        playerIsWhite = true,
                        instructionEn = "Claim the open file or central knight post on e5!",
                        instructionBn = "ঘোড়াটিকে e5 এর শক্তিশালী কেন্দ্রে চালুন!",
                        expectedFromAlg = "f3",
                        expectedToAlg = "e5",
                        hintEn = "Jump your f3 knight forward to the outpost e5.",
                        hintBn = "f3 এর ঘোড়াটিকে e5 ঘরে চালুন।",
                        successExplanationEn = "Masterful! The knight occupies a commanding outpost in the heart of Black's camp.",
                        successExplanationBn = "অসাধারণ কৌশল! ঘোড়াটি এখন শত্রুর মূল কেন্দ্র নিয়ন্ত্রণ করছে।"
                    ),
                    quizQuestion = QuizQuestion(
                        questionEn = "What makes a square an ideal 'outpost' for a Knight?",
                        questionBn = "ঘোড়ার জন্য একটি ঘরকে চমৎকার 'আউটপোস্ট' বলা হয় কখন?",
                        optionsEn = listOf("It is protected by a friendly pawn and cannot be driven away by enemy pawns", "It is on the edge of the board", "It is on White's first rank", "It has a queen next to it"),
                        optionsBn = listOf("নিজের বোড়ে দিয়ে সুরক্ষিত এবং শত্রু বোড়ে একে তাড়াতে পারে না", "বোর্ডের কোণায় থাকে", "প্রথম সারিতে থাকে", "পাশে রানি থাকে"),
                        correctIndex = 0,
                        explanationEn = "An outpost is a hole in the opponent's pawn structure where your piece cannot be driven away by enemy pawns.",
                        explanationBn = "আউটপোস্টে বসানো ঘুঁটিকে শত্রু বোড়ে তাড়িয়ে দিতে পারে না এবং এটি শক্তিশালী প্রভাব বিস্তার করে।"
                    )
                )
            )
        )
    )

    fun getLessonById(id: String): Lesson? {
        for (m in modules) {
            val found = m.lessons.find { it.id == id }
            if (found != null) return found
        }
        return null
    }
}
