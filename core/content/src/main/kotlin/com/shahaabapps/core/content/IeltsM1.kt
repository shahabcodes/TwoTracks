package com.shahaabapps.core.content

/** IELTS module 1: the test, the bands, and what a 7 actually requires. */
internal object IeltsM1 {
    private val refs = Ielts.commonRefs

    private val format = Topic(
        id = "ie1-format", title = "The test: four papers, two versions, one morning", minutes = 18, references = refs,
        blocks = listOf(
            B.plain("IELTS is about two hours and forty-five minutes of Listening, Reading and Writing in one sitting, plus a face-to-face Speaking interview of 11 to 14 minutes, often on another day. Academic and General Training share Listening and Speaking; they differ in Reading and in Writing Task 1."),
            B.table(
                listOf("Paper", "Time", "Shape", "Same for both versions?"),
                listOf("Listening", "about 30 min + transfer time on paper", "4 parts, 40 questions, heard once", "Yes"),
                listOf("Reading", "60 min", "3 sections, 40 questions", "No: Academic has three long passages; General has everyday texts then one longer passage"),
                listOf("Writing", "60 min", "Task 1 (150 words) and Task 2 (250 words)", "Task 1 differs: Academic describes a chart or diagram; General writes a letter. Task 2 is an essay in both"),
                listOf("Speaking", "11 to 14 min", "Part 1 questions, Part 2 cue card, Part 3 discussion", "Yes"),
            ),
            B.diagram(
                caption = "Who sits what in this household: Usaid takes General Training; Sanya takes Academic for teacher registration, or General if Canada.",
                nodes = listOf(
                    DNode("l", "Listening", 0.14f, 0.3f, w = 0.22f, h = 0.26f, tone = 1),
                    DNode("s", "Speaking", 0.14f, 0.7f, w = 0.22f, h = 0.26f, tone = 1),
                    DNode("ra", "Reading: Academic passages", 0.5f, 0.3f, w = 0.3f, h = 0.26f),
                    DNode("rg", "Reading: General texts", 0.5f, 0.7f, w = 0.3f, h = 0.26f, tone = 2),
                    DNode("wa", "Task 1: chart report", 0.84f, 0.3f, w = 0.28f, h = 0.26f),
                    DNode("wg", "Task 1: letter", 0.84f, 0.7f, w = 0.28f, h = 0.26f, tone = 2),
                ),
                edges = listOf(DEdge("ra", "wa", label = "Academic"), DEdge("rg", "wg", label = "General")),
                aspect = 0.45f,
            ),
            B.h("Paper or computer"),
            B.bullets(
                "Computer-delivered: results in 3 to 5 days, you type Writing, Listening has no transfer time (answers go straight in), and you can highlight text in Reading.",
                "Paper: results in 13 days, you write by hand, Listening gives 10 minutes to transfer answers.",
                "Speaking is always face to face with an examiner, in both modes.",
                "Spelling counts in Listening and Reading. Capital letters do not.",
            ),
            B.tip("Choose computer if you type faster than you write and want to edit your essay. Choose paper if you think better with a pen. The score scale is identical."),
            B.check("ie1-format-k1", "ie1-format", "Sanya needs Academic for her teacher assessment. Which parts are different from Usaid's General Training test?", listOf("All four", "Reading and Writing Task 1", "Only Speaking"), 1, "Listening and Speaking are common; Reading and Task 1 differ."),
            B.h("Registration and the day"),
            B.steps("What to do, in order",
                "Book through ielts.org or the British Council or IDP, choosing Academic or General and paper or computer." to "A date two to three months out gives time for the modules here.",
                "Bring the passport you registered with; nothing else goes into the room." to "Pencils are provided for paper tests.",
                "Listening, Reading, Writing run back to back with no break." to "Eat before; there is no pause.",
                "Speaking is a separate slot, sometimes a different day." to "Arrive early; the interview is recorded.",
                "Results: a Test Report Form with four band scores and an overall band." to "Order extra copies for institutions when you book.",
            ),
            B.vocab(
                B.w("ie1-band", "band", "IELTS score from 0 to 9, in half steps", "She needs band 7 in every component.", "A target of 7.0 in each of the four papers."),
                B.w("ie1-trf", "Test Report Form", "the official results document", "The TRF shows four bands and an overall band.", "Institutions ask for the TRF number."),
                B.w("ie1-component", "component", "one of the four papers", "Each component is scored separately.", "Listening is one component."),
                B.w("ie1-cue-card", "cue card", "the Speaking Part 2 topic card", "You get one minute to prepare the cue card.", "Then you speak for up to two minutes."),
                B.w("ie1-transfer", "answer sheet", "where answers are marked on paper", "Transfer your answers to the answer sheet.", "Only the answer sheet is marked."),
            ),
            B.glossary("Academic" to "The version for university and professional registration.", "General Training" to "The version for migration and work.", "IDP / British Council" to "The two organisations that run the test."),
            B.recap("Four papers; Listening and Speaking shared; Reading and Task 1 differ by version.", "Computer gives faster results and editing; paper gives transfer time; the scale is the same.", "Spelling counts, capitals do not; bring only your passport."),
        ),
    )

    private val bands = Topic(
        id = "ie1-bands", title = "How bands are calculated, and what band 7 means in each paper", minutes = 22, references = refs,
        blocks = listOf(
            B.plain("Listening and Reading are counted: 40 questions, a raw score, a conversion table. Writing and Speaking are judged on four criteria each. Knowing the numbers tells you exactly how many mistakes you can afford."),
            B.h("Listening and Reading: raw score to band"),
            B.table(
                listOf("Band", "Listening (of 40)", "Academic Reading (of 40)", "General Reading (of 40)"),
                listOf("9", "39 to 40", "39 to 40", "40"),
                listOf("8", "35 to 36", "35 to 36", "37 to 38"),
                listOf("7", "30 to 31", "30 to 31", "34 to 35"),
                listOf("6", "23 to 24", "23 to 24", "30 to 31"),
                listOf("5", "16 to 17", "15 to 16", "23 to 24"),
            ),
            B.remember("General Training Reading is marked harder because the texts are easier: 34 of 40 for a 7, against 30 of 40 in Academic. Usaid can miss six questions; Sanya in Academic can miss ten."),
            B.diagram(
                caption = "Band 7 in Listening means 30 right of 40: you may miss ten. Band 8 means 35: miss five. Every careless spelling error is one of those.",
                nodes = listOf(
                    DNode("a", "40 questions", 0.14f, 0.5f, w = 0.22f, h = 0.3f),
                    DNode("b", "30 right = band 7", 0.45f, 0.5f, w = 0.28f, h = 0.3f, tone = 1),
                    DNode("c", "35 right = band 8", 0.8f, 0.5f, w = 0.28f, h = 0.3f, tone = 2),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c")),
                aspect = 0.4f,
            ),
            B.h("Writing and Speaking: four criteria, averaged"),
            B.table(
                listOf("Writing criteria", "What band 7 looks like"),
                listOf("Task Achievement / Response", "Every part of the task covered; a clear position throughout; main ideas extended and supported"),
                listOf("Coherence and Cohesion", "Logical paragraphs with a clear central topic each; a range of linking devices used naturally, not stuck on"),
                listOf("Lexical Resource", "Less common words and collocations with some awareness of style; occasional errors in word choice"),
                listOf("Grammatical Range and Accuracy", "A variety of complex structures; the majority of sentences error-free"),
            ),
            B.table(
                listOf("Speaking criteria", "What band 7 looks like"),
                listOf("Fluency and Coherence", "Speaks at length without noticeable effort; some hesitation for content, not for words"),
                listOf("Lexical Resource", "Flexible vocabulary, some idiomatic language, paraphrases when stuck"),
                listOf("Grammatical Range and Accuracy", "Range of complex structures with frequent error-free sentences"),
                listOf("Pronunciation", "Easy to understand throughout; features of a 6 and an 8 mixed"),
            ),
            B.tip("Each criterion is a whole band and the four are averaged. Two 7s and two 6s is 6.5. To get a 7 you need at least two criteria at 7 and the others no lower than 6.5 is impossible, since each is whole: you need three 7s and a 6, or better. Plan for four."),
            B.h("Overall band"),
            B.p("The overall band is the mean of the four, rounded to the nearest half. 7, 7, 6.5, 6.5 averages 6.75 and rounds up to 7. But the Australian teacher registration and many visas need a minimum in each paper, not just overall, so a strong Listening cannot rescue a weak Writing."),
            B.check("ie1-bands-k1", "ie1-bands", "Usaid scores 33 in General Reading. His band is", listOf("6", "6.5", "7"), 1, "34 to 35 is band 7; 32 to 33 is 6.5. One more right answer would have made the 7."),
            B.check("ie1-bands-k2", "ie1-bands", "A Writing script gets 7, 7, 6, 6 on the four criteria. Its band is", listOf("7", "6.5", "6"), 1, "The four criteria are averaged: 7 plus 7 plus 6 plus 6 divided by four is 6.5."),
            B.vocab(
                B.w("ie1-criterion", "criterion (pl. criteria)", "one of the four things the examiner scores", "Each criterion gets a whole band.", "Coherence is one criterion."),
                B.w("ie1-cohesion", "cohesion", "how sentences and ideas connect", "Linking words improve cohesion.", "However and therefore are cohesive devices."),
                B.w("ie1-lexical", "lexical resource", "range and accuracy of vocabulary", "Lexical resource rewards collocation.", "Make a decision, not do a decision."),
                B.w("ie1-task-achievement", "task achievement", "answering every part of the question", "Task achievement drops if you miss a part.", "Discuss both views and give your opinion means three things."),
                B.w("ie1-raw", "raw score", "the number of correct answers before conversion", "A raw score of 30 is band 7.", "Raw scores convert with a table."),
            ),
            B.glossary("Band descriptor" to "The public table describing each band per criterion.", "Overall band" to "Mean of the four, rounded to the nearest half.", "Minimum per component" to "A rule many visas apply on top of the overall."),
            B.recap("Listening 30 of 40 is a 7; General Reading needs 34; Academic Reading 30.", "Writing and Speaking: four whole-band criteria averaged; plan for 7 in all four.", "Most requirements set a minimum per paper, so no paper can be neglected."),
        ),
    )

    private val plan = Topic(
        id = "ie1-plan", title = "From your current level to a 7: the study plan this course follows", minutes = 16, references = refs,
        blocks = listOf(
            B.plain("Moving one band takes most people 100 to 200 hours of focused work. The modules here are the method; the practice papers are the hours. This topic sets the weekly shape and the two habits that matter most."),
            B.diagram(
                caption = "A week that moves a band: method from the modules, practice from the papers, review of every mistake.",
                nodes = listOf(
                    DNode("a", "Module topics (2 h)", 0.17f, 0.5f, w = 0.28f, h = 0.4f, tone = 1, sub = "technique and language"),
                    DNode("b", "Timed practice (3 h)", 0.5f, 0.5f, w = 0.28f, h = 0.4f, sub = "one paper per skill"),
                    DNode("c", "Error review (1 h)", 0.83f, 0.5f, w = 0.28f, h = 0.4f, tone = 2, sub = "why each answer was wrong"),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "a", dashed = true)),
                aspect = 0.55f,
            ),
            B.h("The two habits"),
            B.bullets(
                "Every wrong answer gets a reason: misheard a number, missed a paraphrase, ran out of time, spelling. Keep the list; it tells you what to drill. The review set in this app does this for the module tests.",
                "Every writing task gets a second draft after marking. The second draft is where the band improves.",
            ),
            B.h("Where the time goes"),
            B.table(
                listOf("Paper", "Typical gap from 6 to 7", "What closes it"),
                listOf("Listening", "Paraphrase and distractors; spelling", "Module 2; a full Listening paper a week"),
                listOf("Reading", "Time; True/False/Not Given; matching headings", "Module 3; timed sections, 20 minutes each"),
                listOf("Writing", "Task response, paragraphing, error density", "Modules 4, 5 and 7; two marked tasks a week"),
                listOf("Speaking", "Length of answers, Part 3 reasoning, hesitation", "Module 6; recorded answers, three a day"),
            ),
            B.steps("Set up your practice",
                "Download the free practice tests from ielts.org." to "One Academic and one General set each, with answer keys.",
                "Put a 60-minute Reading paper and a 30-minute Listening paper in your week." to "Under exam timing from the first attempt.",
                "Write one Task 1 and one Task 2 a week; mark them with the app's marker, then rewrite." to "Keep both drafts.",
                "Record three Speaking answers a day on your phone; listen back the next day." to "You hear your own hesitations.",
            ),
            B.check("ie1-plan-k1", "ie1-plan", "You got 28 in a Listening practice test. The most useful next step is", listOf("Do another test immediately", "List the reason for each of the 12 misses", "Switch to Reading"), 1, "The reasons tell you what to drill; another test without review repeats the same errors."),
            B.vocab(
                B.w("ie1-paraphrase", "paraphrase", "saying the same thing in different words", "The recording paraphrases the question.", "Cheap in the question becomes inexpensive in the audio."),
                B.w("ie1-distractor", "distractor", "a wrong answer made to sound right", "Every listening question has a distractor.", "The speaker corrects a number: the first is the distractor."),
                B.w("ie1-skim", "skim", "read quickly for the overall idea", "Skim the passage before the questions.", "Two minutes, first sentence of each paragraph."),
                B.w("ie1-scan", "scan", "search for a specific word or number", "Scan for the year 1998.", "Names, dates and numbers are scannable."),
                B.w("ie1-drill", "drill", "repeated focused practice", "Drill spelling of numbers and names.", "Ten minutes a day."),
            ),
            B.glossary("Practice paper" to "A full test under timing.", "Error log" to "Your list of mistakes and their causes.", "Second draft" to "A rewrite after marking."),
            B.recap("One band is 100 to 200 hours; the modules give method, the papers give hours.", "Log the reason for every miss; rewrite every marked task.", "Weekly: module topics, one timed paper per skill, an hour of review."),
        ),
    )

    val module = Module(
        id = "ie-m1", number = 1, title = "The test and band 7", summary = "Format, Academic versus General, how bands are calculated, the study plan.",
        examWeight = "all four", componentId = "reading", topics = listOf(format, bands, plan), practiceMinutes = 120,
        quiz = Quiz(
            id = "ie-m1-test", title = "Module 1 test", passPercent = 70,
            questions = B.spread(listOf(
                B.q("ie1-q1", "ie1-format", "Which two papers are identical in Academic and General Training?", listOf("Reading and Writing", "Listening and Speaking", "Writing and Speaking"), setOf(1), "Listening and Speaking are shared; Reading and Writing Task 1 differ."),
                B.q("ie1-q2", "ie1-format", "In General Training, Writing Task 1 is", listOf("a chart description", "a letter", "an essay"), setOf(1), "General Task 1 is a letter; Academic Task 1 describes data."),
                B.q("ie1-q3", "ie1-format", "On the computer-delivered test, Listening", listOf("has 10 minutes of transfer time", "has no transfer time because answers go straight in", "is heard twice"), setOf(1), "Only the paper test gives transfer time; the recording plays once in both."),
                B.q("ie1-q4", "ie1-format", "Which is true about spelling in Listening answers?", listOf("Spelling is ignored", "Spelling must be correct; capitals do not matter", "Only capitals matter"), setOf(1), "A misspelt answer is wrong; case is not checked."),
                B.q("ie1-q5", "ie1-bands", "How many correct answers of 40 give band 7 in Listening?", listOf("23 to 24", "30 to 31", "35 to 36"), setOf(1), "30 is the band 7 threshold; 35 is band 8."),
                B.q("ie1-q6", "ie1-bands", "Why does General Training Reading need more correct answers for the same band?", listOf("It has more questions", "Its texts are easier, so the conversion is stricter", "It is marked by a different organisation"), setOf(1), "34 of 40 for a 7 in General, against 30 in Academic."),
                B.q("ie1-q7", "ie1-bands", "A Speaking performance scores 7, 7, 7, 6. The Speaking band is", listOf("7", "6.75", "6.5"), setOf(2), "The mean 6.75 is rounded to the nearest half band for the paper score: 6.5 under the per-paper rounding, so aim for all four at 7."),
                B.q("ie1-q8", "ie1-bands", "Overall bands of 7, 7, 6.5, 6.5 give an overall of", listOf("6.5", "7", "6.75"), setOf(1), "The mean 6.75 rounds up to 7.0."),
                B.q("ie1-q9", "ie1-bands", "Which Writing criterion rewards paragraphing and linking?", listOf("Task Achievement", "Coherence and Cohesion", "Lexical Resource"), setOf(1), "Coherence and Cohesion covers organisation and linking devices."),
                B.q("ie1-q10", "ie1-plan", "The habit that most improves a band is", listOf("Taking more tests without review", "Logging the reason for each miss and rewriting marked tasks", "Reading grammar books only"), setOf(1), "Review and second drafts turn practice into improvement."),
                B.q("ie1-q11", "ie1-plan", "'The recording paraphrases the question' means the audio", listOf("repeats the question word for word", "says the same idea in different words", "skips the question"), setOf(1), "Paraphrase is the core skill in Listening and Reading."),
                B.q("ie1-q12", "ie1-format", "The Speaking test lasts", listOf("5 minutes", "11 to 14 minutes", "30 minutes"), setOf(1), "Three parts over 11 to 14 minutes, recorded."),
            )),
        ),
    )
}
