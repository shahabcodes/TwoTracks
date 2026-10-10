package com.shahaabapps.core.content

/** IELTS module 2: Listening. The four parts, question types, prediction, distractors, spelling and numbers. */
internal object IeltsM2 {
    private val refs = Ielts.commonRefs + B.ref("Listening test format", Ielts.ORG + "take-a-test/test-format/listening")

    private val parts = Topic(
        id = "ie2-parts", title = "The four parts and the five question types", minutes = 22, references = refs,
        blocks = listOf(
            B.warmup(
                "ie2-parts-w1", "ie2-parts",
                "The Listening recording plays once. Before each part you are given a little silence. What do you think it is for?",
                listOf("Checking the answers you have already written", "Reading the questions that are coming", "Nothing in particular"), 1,
                "It is the only moment you get to know what to listen for.",
            ),
            B.plain("The recording plays once. Forty questions over four parts that get harder: a conversation about an everyday need, a monologue on an everyday topic, a conversation in an academic setting, a lecture. Everything is predictable except the words themselves."),
            B.table(
                listOf("Part", "Audio", "Typical questions", "Where the points are"),
                listOf("1", "Two people: booking, enquiry, application", "Form or note completion: names, numbers, dates, spellings", "Spelling and numbers; a corrected detail"),
                listOf("2", "One person: a talk, a tour, a recorded message", "Multiple choice, map or plan labelling, matching", "Following a map; paraphrased options"),
                listOf("3", "Two to four people: students and a tutor", "Multiple choice, matching, flow chart", "Who says what; opinions that change"),
                listOf("4", "One person: an academic lecture", "Sentence or summary completion", "Keeping pace; technical words spelt as heard"),
            ),
            B.diagram(
                caption = "Before each part you get time to read the questions. That reading time is where the band is won: predict the answer type, underline the keywords.",
                nodes = listOf(
                    DNode("a", "reading time", 0.14f, 0.5f, w = 0.22f, h = 0.32f, tone = 1, sub = "30 to 40 seconds"),
                    DNode("b", "predict: number? name? noun?", 0.42f, 0.5f, w = 0.3f, h = 0.32f),
                    DNode("c", "underline keywords", 0.72f, 0.5f, w = 0.26f, h = 0.32f, tone = 2),
                    DNode("d", "listen for paraphrases", 0.92f, 0.5f, w = 0.14f, h = 0.32f),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "d")),
                aspect = 0.65f,
            ),
            B.h("The five question types"),
            B.table(
                listOf("Type", "Rule", "Trap"),
                listOf("Form, note, table, sentence completion", "Obey the word limit: NO MORE THAN TWO WORDS AND/OR A NUMBER means two words and a number is fine, three words is wrong", "Writing a word that is already printed on the form"),
                listOf("Multiple choice", "Read all options; the audio mentions most of them", "Picking the first option you hear"),
                listOf("Matching", "The list of options is longer than the questions; options can repeat only if it says so", "Losing your place when the speakers move on"),
                listOf("Map or plan labelling", "Find the start point and the compass; follow left, right, opposite, next to", "Hearing a landmark and labelling it, when it was only a reference"),
                listOf("Short answer", "Usually a noun; copy the exact word from the audio", "Changing the form: plural, tense"),
            ),
            B.remember("Answers come in order for completion and multiple-choice questions. If you hear the answer to question 14, you missed 13; move on, never go back."),
            B.check("ie2-parts-k1", "ie2-parts", "The instruction says NO MORE THAN TWO WORDS AND/OR A NUMBER. Which answer breaks it?", listOf("25 Park Road", "second floor", "the main library"), 2, "'the main library' is three words. '25 Park Road' is a number and two words, which is allowed."),
            B.h("Hear it: a Part 1 booking"),
            B.listen("ie2-p-l1", "Receptionist: Good morning, City Dental. Caller: Hi, I'd like to make an appointment, please. My name is Patel, that's P-A-T-E-L. Receptionist: Thank you, Mr Patel. And a contact number? Caller: It's oh seven seven double two, nine one four, six oh three. Receptionist: And is it a check-up or a problem? Caller: A check-up, but actually one tooth has been hurting since Monday.",
                "What is the reason for the appointment?", listOf("a check-up only", "a check-up and a painful tooth", "an emergency"), 1),
            B.gap("ie2-p-g1", "Form completion: write the phone number you hear.", "Phone: ___", "07722914603", "0772 2914 603", "07722 914 603", audio = "The contact number is oh seven seven double two, nine one four, six oh three."),
            B.gap("ie2-p-g2", "Form completion: write the surname you hear, spelt out.", "Surname: ___", "Patel", audio = "My name is Patel, that's P-A-T-E-L."),
            B.vocab(
                B.w("ie2-completion", "completion", "filling a gap with words from the audio", "Note completion uses the exact words.", "A noun or a number, usually."),
                B.w("ie2-word-limit", "word limit", "the maximum words allowed per answer", "Check the word limit before each set.", "ONE WORD ONLY is common in Part 4."),
                B.w("ie2-monologue", "monologue", "one speaker", "Parts 2 and 4 are monologues.", "A talk or a lecture."),
                B.w("ie2-plan", "plan / map labelling", "placing letters on a drawing", "Map labelling needs a start point.", "Find 'you are here' first."),
                B.w("ie2-in-order", "in order", "answers follow the audio sequence", "Completion answers come in order.", "If you hear 14, you have missed 13."),
            ),
            B.glossary("Part 1 to 4" to "Conversation, talk, academic discussion, lecture.", "Keyword" to "The content word in a question you listen for a paraphrase of.", "Transfer" to "Copying answers to the sheet (paper only)."),
            B.recap("Four parts that get harder; forty questions heard once.", "Use the reading time to predict the answer type and underline keywords.", "Obey word limits; answers come in order; never go back."),
        ),
    )

    private val traps = Topic(
        id = "ie2-traps", title = "Paraphrase, distractors and corrections: how questions are built", minutes = 24, references = refs,
        blocks = listOf(
            B.warmup(
                "ie2-traps-w1", "ie2-traps",
                "A multiple-choice question offers Tuesday, Thursday and Friday, and the speakers mention all three. Which do you think is usually the answer?",
                listOf("The first one mentioned", "The one they end on", "Whichever is said loudest"), 1,
                "The other two are there on purpose, and they come first on purpose too.",
            ),
            B.plain("Question writers follow rules. The question paraphrases the audio; the audio mentions two or three options before settling on one; speakers correct themselves. Once you hear the pattern, the test becomes predictable."),
            B.h("Pattern 1: the paraphrase"),
            B.table(
                listOf("Question says", "Audio says"),
                listOf("cost", "how much it comes to, the price, the fee"),
                listOf("book", "reserve, put your name down"),
                listOf("weekly", "every seven days, once a week"),
                listOf("not available", "fully booked, sold out, no spaces left"),
                listOf("reduced price", "a discount, ten percent off, cheaper if"),
                listOf("compulsory", "you have to, it is required, there is no choice"),
            ),
            B.h("Pattern 2: the distractor"),
            B.diagram(
                caption = "Multiple choice: the audio touches options A and C, then settles on B. The first thing you hear is almost never the answer.",
                nodes = listOf(
                    DNode("a", "'We thought about Tuesday…'", 0.2f, 0.3f, w = 0.34f, h = 0.24f, tone = 2, sub = "A mentioned"),
                    DNode("c", "'…or even Friday, but'", 0.2f, 0.72f, w = 0.34f, h = 0.24f, tone = 2, sub = "C mentioned"),
                    DNode("b", "'…in the end Thursday suits everyone.'", 0.7f, 0.5f, w = 0.5f, h = 0.3f, tone = 1, sub = "B is the answer"),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("c", "b")),
                aspect = 0.65f,
            ),
            B.listen("ie2-t-l1", "Tutor: So when shall we hold the seminar? Student: We thought about Tuesday, because the room's free then, or even Friday afternoon. But Tuesday clashes with the statistics lecture, and on Friday half the group leaves early. In the end Thursday suits everyone.",
                "When will the seminar be held?", listOf("Tuesday", "Thursday", "Friday"), 1),
            B.h("Pattern 3: the correction"),
            B.p("Speakers change their minds: a number, a time, a spelling. Words that signal it: actually, sorry, no wait, I mean, rather than, let me check. The corrected version is the answer."),
            B.listen("ie2-t-l2", "The course starts on the fourteenth of March. Oh, sorry, I'm looking at last year's leaflet. It's the twelfth of March this time, and it runs for eight weeks rather than six.",
                "When does the course start and how long is it?", listOf("14 March, 6 weeks", "12 March, 8 weeks", "12 March, 6 weeks"), 1),
            B.h("Pattern 4: the negative and the condition"),
            B.bullets(
                "'You can bring a laptop but not a phone' answers a question about what is allowed with the laptop.",
                "'Parking is free if you stay under two hours' means the answer to 'parking costs' depends on time; the question will target one case.",
                "'Unfortunately the Tuesday class is full' removes Tuesday as an option.",
            ),
            B.listen("ie2-t-l3", "You're welcome to bring a laptop into the library, but phones must be left in the lockers by the entrance. Food isn't allowed, though you can bring water in a bottle with a lid.",
                "Which item may you take into the library?", listOf("a phone", "a laptop", "a sandwich"), 1),
            B.check("ie2-traps-k1", "ie2-traps", "You hear 'it's twenty pounds, no, sorry, twenty-five with the booking fee'. The cost is", listOf("20 pounds", "25 pounds", "45 pounds"), 1, "The correction wins: twenty-five."),
            B.vocab(
                B.w("ie2-settle", "settle on", "decide finally", "They settled on Thursday.", "After discussing the other days."),
                B.w("ie2-clash", "clash with", "happen at the same time as", "It clashes with a lecture.", "A reason to reject an option."),
                B.w("ie2-fully-booked", "fully booked", "no places left", "Tuesday is fully booked.", "A paraphrase of not available."),
                B.w("ie2-discount", "discount", "a price reduction", "A ten percent discount for students.", "A paraphrase of reduced price."),
                B.w("ie2-compulsory", "compulsory", "required, no choice", "Attendance is compulsory.", "Paraphrase: you have to attend."),
                B.w("ie2-actually", "actually / sorry / I mean", "signals of a correction", "Actually, it's the twelfth.", "The corrected value is the answer."),
            ),
            B.mistakes(
                "Answering on the first option mentioned.",
                "Ignoring 'but', 'although', 'unfortunately': they reverse what came before.",
                "Writing the number before the correction.",
                "Listening for the exact question word instead of its paraphrase.",
            ),
            B.glossary("Paraphrase" to "Same meaning, different words.", "Distractor" to "An option mentioned and rejected.", "Signal word" to "actually, sorry, but, in the end."),
            B.recap("The question paraphrases; learn the common pairs.", "Options are mentioned and rejected; the answer is where the speaker settles.", "Corrections and negatives: the second version, the allowed item."),
        ),
    )

    private val details = Topic(
        id = "ie2-details", title = "Numbers, spellings, maps and Part 4 completion", minutes = 24, references = refs,
        blocks = listOf(
            B.warmup(
                "ie2-details-w1", "ie2-details",
                "Of the forty Listening marks, roughly how many do you think turn on writing a name, number or date down correctly?",
                listOf("One or two", "Closer to ten", "None; they are all comprehension"), 1,
                "Part 1 is largely dictation, and a misspelling scores nothing.",
            ),
            B.plain("Half of Part 1 is dictation: names spelt out, numbers, dates, prices, postcodes. Part 2 often has a map. Part 4 is a lecture where you fill gaps with single words. Each needs a specific drill."),
            B.h("Numbers the British way"),
            B.table(
                listOf("You hear", "You write"),
                listOf("double seven, oh four", "7704"),
                listOf("fifteen / fifty", "15 / 50: listen for the stress: fifTEEN, FIFty"),
                listOf("two thousand and twenty-six", "2026"),
                listOf("a hundred and fifty pounds", "£150 or 150"),
                listOf("the thirtieth of June", "30 June or 30th June"),
                listOf("half past seven in the evening", "7.30 pm or 19.30"),
                listOf("point five", "0.5 or .5"),
                listOf("BN1 4GH", "BN1 4GH: letters then numbers, said in pairs"),
            ),
            B.gap("ie2-d-g1", "Write the number you hear.", "Order number: ___", "50316", "50 316", audio = "Your order number is five oh three one six."),
            B.gap("ie2-d-g2", "Write the price you hear.", "Total: £___", "115", "115.00", audio = "That comes to a hundred and fifteen pounds in total."),
            B.gap("ie2-d-g3", "Write the date you hear.", "Start date: ___ September", "19", "19th", "nineteenth", audio = "The course begins on the nineteenth of September, not the ninth."),
            B.h("Spellings"),
            B.p("Names are spelt letter by letter; the trap letters are E and I, G and J, A and R, and double letters: 'double L'. Practise with these."),
            B.gap("ie2-d-g4", "Write the surname you hear.", "Surname: ___", "Greenhill", audio = "The surname is Greenhill, G-R-double E-N-H-I-double L."),
            B.gap("ie2-d-g5", "Write the street name you hear.", "Street: ___ Road", "Jarvie", audio = "It's Jarvie Road, J-A-R-V-I-E."),
            B.h("Maps and plans"),
            B.diagram(
                caption = "Map labelling: find where you start, then follow the directions step by step. Words to catch: opposite, next to, at the end of, on the corner of, before you reach.",
                nodes = listOf(
                    DNode("s", "entrance (start)", 0.14f, 0.8f, w = 0.24f, h = 0.2f, tone = 1),
                    DNode("a", "café", 0.14f, 0.3f, w = 0.18f, h = 0.2f, tone = 2),
                    DNode("b", "library", 0.5f, 0.3f, w = 0.2f, h = 0.2f),
                    DNode("c", "gym", 0.86f, 0.3f, w = 0.18f, h = 0.2f, tone = 2),
                    DNode("d", "car park", 0.86f, 0.8f, w = 0.22f, h = 0.2f),
                ),
                edges = listOf(DEdge("s", "a", label = "straight ahead"), DEdge("a", "b", label = "turn right"), DEdge("b", "c", label = "at the end"), DEdge("s", "d", label = "to your right", dashed = true)),
                aspect = 0.5f,
            ),
            B.listen("ie2-d-l1", "From the main entrance, go straight ahead and the café is directly in front of you. Turn right at the café and walk past the library; the gym is at the end of that corridor. If you need the car park, it's immediately to your right as you come in.",
                "Where is the gym?", listOf("opposite the entrance", "at the end of the corridor past the library", "to the right of the entrance"), 1),
            B.h("Part 4: completion at lecture pace"),
            B.p("Part 4 has no pause in the middle and the gaps are single words, usually nouns. Read the sentences first, predict the word class, and listen for the sentence around the gap. If you miss one, let it go."),
            B.listen("ie2-d-l2", "Urban beekeeping has grown rapidly over the last decade. Interestingly, city bees often produce more honey than rural ones, because gardens and parks offer a greater variety of flowers, and because cities are slightly warmer, which extends the season.",
                "Why do city bees often produce more honey? Choose the completion: because of a greater variety of … and a longer …", listOf("gardens / winter", "flowers / season", "parks / day"), 1),
            B.gap("ie2-d-g6", "Sentence completion, ONE WORD ONLY.", "City bees benefit from a greater variety of ___.", "flowers", audio = "City bees often produce more honey because gardens and parks offer a greater variety of flowers."),
            B.check("ie2-details-k1", "ie2-details", "You hear 'G-R-double E-N'. You write", listOf("Green", "Greeen", "Gren"), 0, "'double E' is two Es: Green."),
            B.vocab(
                B.w("ie2-double", "double (as in double L)", "two of the same letter", "Double L in Hill.", "H-I-double L."),
                B.w("ie2-postcode", "postcode", "the UK address code", "Postcode BN1 4GH.", "Letters and numbers in groups."),
                B.w("ie2-opposite", "opposite", "facing, across from", "The gym is opposite the café.", "A map-labelling word."),
                B.w("ie2-corridor", "corridor", "a passage between rooms", "At the end of the corridor.", "Common in building plans."),
                B.w("ie2-decade", "decade", "ten years", "Over the last decade.", "A Part 4 time phrase."),
                B.w("ie2-rapidly", "rapidly", "quickly", "It has grown rapidly.", "Paraphrase: fast, sharply."),
            ),
            B.mistakes(
                "Writing fifty for fifteen; listen for the stressed syllable.",
                "Labelling the landmark you hear rather than the place the directions end at.",
                "Writing two words where ONE WORD ONLY is allowed.",
                "Stopping to agonise over a missed gap in Part 4 and losing the next two.",
            ),
            B.glossary("Dictation" to "Writing exactly what is said.", "Word class" to "Noun, verb, adjective: predict it for each gap.", "Lecture pace" to "Continuous speech with no pauses for answers."),
            B.recap("Drill numbers, dates, prices and spellings; fifteen versus fifty.", "Maps: start point, then follow each direction word.", "Part 4: predict the word class, keep pace, let misses go."),
        ),
    )

    val module = Module(
        id = "ie-m2", number = 2, title = "Listening", summary = "Four parts, five question types, paraphrase and distractors, numbers and spellings, maps, Part 4 completion.",
        examWeight = "25 percent", componentId = "listening", topics = listOf(parts, traps, details), practiceMinutes = 600,
        quiz = Quiz(
            id = "ie-m2-test", title = "Module 2 test", passPercent = 70,
            questions = B.spread(listOf(
                B.q(
                    "ie2-q1", "ie2-parts", "Listening: what does the caller want to change?",
                    listOf("the date of the booking", "the number of people", "the time of the booking"), setOf(1),
                    "She keeps the date and time but changes from four people to six.",
                    listen = "Hello, I booked a table for Saturday at seven for four people, and I'd like to keep the date and time but make it six people instead.",
                    whyWrong = mapOf(
                        0 to "Saturday is said twice, which is what makes it tempting. She says she wants to keep it.",
                        2 to "Seven o'clock is named and then explicitly kept. A detail you hear is not thereby the answer.",
                    ),
                ),
                B.q(
                    "ie2-q2", "ie2-parts", "Does 'the city museum' fit an instruction reading NO MORE THAN TWO WORDS?",
                    listOf("Yes: two words", "No: three words", "Yes: it counts as one name"), setOf(1),
                    "Articles count as words; 'city museum' would fit.",
                    whyWrong = mapOf(
                        0 to "'the' is a word on the answer sheet like any other. Drop it and you are inside the limit.",
                        2 to "A name is not exempt. The limit counts words, not things.",
                    ),
                ),
                B.q(
                    "ie2-q3", "ie2-parts", "Completion answers in Part 1",
                    listOf("come in random order", "come in the order of the audio", "are all numbers"), setOf(1),
                    "They follow the audio; if you hear a later answer, move on.",
                    whyWrong = mapOf(
                        0 to "If they were random you could never tell you had missed one. In order is what makes moving on safe.",
                        2 to "Part 1 is mostly names, numbers and dates, but not only numbers.",
                    ),
                ),
                B.q(
                    "ie2-q4", "ie2-traps", "Listening: which day is the trip?",
                    listOf("Wednesday", "Saturday", "Sunday"), setOf(2),
                    "Wednesday and Saturday are rejected; Sunday is settled on.",
                    listen = "We could go on Wednesday, but the museum's closed then. Saturday's an option, though it'll be crowded. Let's go on Sunday, it's quieter.",
                    whyWrong = mapOf(
                        0 to "'but the museum's closed then' throws it away. Listen past the first day you hear.",
                        1 to "'though it'll be crowded' is a rejection too. Both of the first two are distractors.",
                    ),
                ),
                B.q(
                    "ie2-q5", "ie2-traps", "Listening: what is the fee?",
                    listOf("40 pounds", "45 pounds", "50 pounds"), setOf(1),
                    "The speaker corrects forty to forty-five.",
                    listen = "The registration fee is forty pounds. Actually, I should say forty-five, since it went up in January.",
                    whyWrong = mapOf(
                        0 to "That is the figure before 'actually'. A correction always beats what it corrects.",
                        2 to "Fifty is never said. It is the sort of number you write when you half-heard a rise.",
                    ),
                ),
                B.q(
                    "ie2-q6", "ie2-traps", "The audio says 'you have to register in advance'. The question calls registration",
                    listOf("optional", "compulsory", "free"), setOf(1),
                    "'have to' paraphrases compulsory.",
                    whyWrong = mapOf(
                        0 to "'have to' is the opposite of optional. This is the paraphrase pair the question is built on.",
                        2 to "Nothing was said about price. Having to do a thing says nothing about what it costs.",
                    ),
                ),
                B.q(
                    "ie2-q7", "ie2-details", "Listening: write down the reference number. Which is correct?",
                    listOf("30175", "31075", "30715"), setOf(0),
                    "Three, oh, one, seven, five, in that order.",
                    listen = "Your reference number is three, oh, one, seven, five.",
                    whyWrong = mapOf(
                        1 to "The oh comes second, not third. Digits dictated one by one have to be written as they arrive.",
                        2 to "The seven and the one have swapped. Write as you hear rather than reconstructing afterwards.",
                    ),
                ),
                B.q(
                    "ie2-q8", "ie2-details", "Listening: the surname is spelt",
                    listOf("Whitley", "Witley", "Whitely"), setOf(0),
                    "The speaker spells it W-H-I-T-L-E-Y, so the H comes second and there is no E before the L.",
                    listen = "The name is Whitley, that's W-H-I-T-L-E-Y.",
                    whyWrong = mapOf(
                        1 to "The H is spelt out. Writing what the name sounds like instead of what was spelt is the commonest Part 1 loss.",
                        2 to "The E comes after the L, not before it. Follow the letters in the order given.",
                    ),
                ),
                B.q(
                    "ie2-q9", "ie2-details", "Listening: where is the lab?",
                    listOf("opposite the café", "next to the lecture hall", "at the end of the corridor"), setOf(1),
                    "Next to the lecture hall; the café is the reference, not the location.",
                    listen = "Walk past the café on your left and the lab is the next door along, right next to the lecture hall.",
                    whyWrong = mapOf(
                        0 to "The café is a landmark you walk past. Labelling the landmark instead of the destination is the standard map trap.",
                        2 to "No end of a corridor is mentioned here. It is a direction from another question set.",
                    ),
                ),
                B.q(
                    "ie2-q10", "ie2-details", "'Fifteen' and 'fifty' are told apart by",
                    listOf("the first letter", "the stressed syllable", "the context only"), setOf(1),
                    "fifTEEN stresses the second syllable; FIFty the first.",
                    whyWrong = mapOf(
                        0 to "They begin identically, which is exactly why they are confused.",
                        2 to "Context often cannot settle it: fifteen and fifty pounds are both plausible prices. The stress can.",
                    ),
                ),
                B.q(
                    "ie2-q11", "ie2-details", "In Part 4 you miss a gap. The best action is",
                    listOf("work backwards from what you remember", "move on to the next gap at once", "stop and wait for a pause"), setOf(1),
                    "Keeping pace saves the following answers.",
                    whyWrong = mapOf(
                        0 to "A lecture does not wait. Reconstructing one gap usually costs the next two.",
                        2 to "Part 4 has no pause in the middle. There is nothing to wait for.",
                    ),
                ),
                B.q(
                    "ie2-q12", "ie2-parts", "Map labelling: the first thing to find is",
                    listOf("the compass or start point", "the biggest building", "the answer options"), setOf(0),
                    "Directions only make sense from the start point.",
                    whyWrong = mapOf(
                        1 to "Size tells you nothing. 'Turn right' means nothing until you know where you are standing.",
                        2 to "The options are letters on the drawing. Reading them without a start point tells you nothing about where they are.",
                    ),
                ),
                B.q(
                    "ie2-q13", "ie2-traps", "Listening: what may students bring?",
                    listOf("phones", "calculators", "notes"), setOf(1),
                    "Calculators are allowed; phones and notes are not.",
                    listen = "You may bring a calculator into the exam, but not phones, and no notes of any kind.",
                    whyWrong = mapOf(
                        0 to "'but not phones' is the whole point of the sentence. Hearing a thing named is not hearing it allowed.",
                        2 to "'no notes of any kind' is as flat a refusal as the test gives.",
                    ),
                ),
                B.q(
                    "ie2-q14", "ie2-details", "Listening: the room alone costs, per night,",
                    listOf("95 pounds", "85 pounds", "115 pounds"), setOf(1),
                    "Eighty-five is the room; ninety-five is the room with breakfast, and the question asks for the room.",
                    listen = "The room is eighty-five pounds a night, or ninety-five if you want breakfast included. Just the room? Then eighty-five.",
                    whyWrong = mapOf(
                        0 to "That is the price with breakfast. A conditional price answers only the case the question asks about.",
                        2 to "A hundred and fifteen is never said. Adding the two figures is not what 'or' means.",
                    ),
                ),
            )),
        ),
    )
}
