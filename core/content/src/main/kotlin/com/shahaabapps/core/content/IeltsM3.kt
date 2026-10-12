package com.shahaabapps.core.content

/** IELTS module 3: Reading. Timing, True/False/Not Given, matching headings, and the Academic/General differences. */
internal object IeltsM3 {
    private val refs = Ielts.commonRefs + B.ref("Reading test format", Ielts.ORG + "take-a-test/test-format/reading")

    private val timing = Topic(
        id = "ie3-timing", title = "Sixty minutes, forty questions: the timing that decides the band", minutes = 22, references = refs,
        blocks = listOf(
            B.warmup(
                "ie3-timing-w1", "ie3-timing",
                "Somebody scores 6 in Reading. What do you think is most often the reason?",
                listOf("Their English is not good enough yet", "They ran out of time", "They guessed too much"), 1,
                "Most people who score 6 could answer every question. Not in an hour.",
            ),
            B.plain("Most people who score 6 in Reading know enough English for a 7. They run out of time. There is no extra transfer time on paper, so the clock is the real examiner. This topic fixes the clock first and the techniques second."),
            B.diagram(
                caption = "Twenty minutes a section, with the last three for transferring and guessing. Leave a section when its twenty minutes are gone, finished or not.",
                nodes = listOf(
                    DNode("a", "Section 1", 0.17f, 0.5f, w = 0.26f, h = 0.4f, tone = 1, sub = "20 min · easiest"),
                    DNode("b", "Section 2", 0.5f, 0.5f, w = 0.26f, h = 0.4f, sub = "20 min"),
                    DNode("c", "Section 3", 0.83f, 0.5f, w = 0.26f, h = 0.4f, tone = 2, sub = "17 min + 3 to transfer"),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c")),
                aspect = 0.55f,
            ),
            B.h("The order to work in"),
            B.steps("One section, twenty minutes",
                "Skim the passage for two minutes: title, first and last sentence of each paragraph." to "You now know what each paragraph is about, not what it says.",
                "Read the question set and underline the keywords in each question." to "Names, dates, numbers and unusual nouns are the scannable ones.",
                "Answer the questions that name something scannable first." to "They are found in seconds and build a map of the passage.",
                "Do the paragraph-level tasks (headings, matching information) next." to "Your skim already told you where topics live.",
                "Guess anything still blank. There is no penalty for a wrong answer." to "A blank is certainly zero; a guess is one in three or four.",
            ),
            B.remember("Never read a passage from start to finish before the questions. You will read it twice and have no time for section 3, which carries the hardest questions and the same marks."),
            B.h("Academic versus General"),
            B.table(
                listOf("", "Academic", "General Training"),
                listOf("Section 1", "A long passage, often science or history", "Two or three short everyday texts: notices, adverts, timetables"),
                listOf("Section 2", "A long passage", "Two work-related texts: contracts, staff handbooks, job adverts"),
                listOf("Section 3", "The hardest long passage, often argument", "One long passage of general interest"),
                listOf("Band 7 needs", "30 of 40", "34 of 40"),
                listOf("Feel", "Dense; unknown vocabulary is normal", "Easier English, stricter marking, more questions per text"),
            ),
            B.tip("General Training candidates often lose marks in section 1 by rushing the easy texts. Six marks there are worth the same as six in the long passage and take a third of the time."),
            B.check("ie3-timing-k1", "ie3-timing", "You have five minutes left and six questions blank. The best move is", listOf("read the passage again carefully", "fill every blank with a plausible guess", "leave them blank to avoid errors"), 1, "There is no penalty for wrong answers, so a blank wastes a possible mark."),
            B.check(
                "ie3-timing-k2", "ie3-timing",
                "Twenty minutes are up on section 1 and three questions are unanswered. You should",
                listOf("move to section 2 and come back if there is time", "finish section 1 properly first", "split the remaining time evenly over what is left"), 0,
                "Every section carries the same marks, and section 3 holds the hardest questions. Overrunning section 1 pays for easy marks with hard ones you never see.",
                whyWrong = mapOf(
                    1 to "Those three questions cost the same as three in section 3, which you have not read at all.",
                    2 to "Evening out the leftover is still a late start; the clock has to be enforced section by section.",
                ),
            ),
            B.check(
                "ie3-timing-k3", "ie3-timing",
                "Before the questions you should read the passage",
                listOf("as a two-minute skim of titles and topic sentences", "in full, carefully", "not at all"), 0,
                "The skim tells you what each paragraph is about, which is what the questions ask you to use. Reading it in full means reading it twice.",
                whyWrong = mapOf(
                    1 to "A full read uses a third of the section's time and still has to be repeated once you know the questions.",
                    2 to "Going in blind means hunting for every answer with no map at all.",
                ),
            ),
            B.h("Words you will not know"),
            B.p("Every passage contains vocabulary you have never seen. That is designed. You do not need the word; you need the sentence around it. If a question depends on an unknown word, the answer is usually a paraphrase in the next sentence or a contrast after 'but' or 'however'."),
            B.vocab(
                B.w("ie3-skim-b", "skim", "read fast for the gist", "Skim before you read the questions.", "Two minutes per passage."),
                B.w("ie3-scan-b", "scan", "hunt for a specific item", "Scan for the date 1947.", "Numbers and capitals are easiest."),
                B.w("ie3-keyword", "keyword", "the word in the question you search for", "Underline the keywords.", "Usually a noun or a name."),
                B.w("ie3-synonym", "synonym", "a word with the same meaning", "The passage uses a synonym of 'cheap'.", "inexpensive, affordable, low-cost."),
                B.w("ie3-gist", "gist", "the overall meaning", "Skimming gives you the gist.", "Not the detail."),
                B.w("ie3-penalty", "no penalty", "wrong answers do not subtract marks", "There is no penalty for guessing.", "Never leave a blank."),
            ),
            B.glossary("Section" to "One passage with its question sets.", "Transfer" to "Copying answers to the answer sheet (paper test only).", "Passage" to "The reading text."),
            B.recap("Twenty minutes a section; leave on time whatever is unfinished.", "Skim, underline keywords, answer scannable questions first, then paragraph tasks.", "Guess every blank; unknown words are expected and usually paraphrased nearby."),
        ),
    )

    private val tfng = Topic(
        id = "ie3-tfng", title = "True, False, Not Given: the question type that costs most marks", minutes = 26, references = refs,
        blocks = listOf(
            B.warmup(
                "ie3-tfng-w1", "ie3-tfng",
                "A passage describes two methods at length and never says which is better. A statement claims the first is more accurate. What do you think the answer is?",
                listOf("TRUE, since it is described first", "FALSE, since the passage does not say so", "NOT GIVEN"), 2,
                "There is a difference between a passage denying a thing and never raising it.",
            ),
            B.plain("True/False/Not Given, and its opinion twin Yes/No/Not Given, catch more candidates than any other type. The fix is mechanical: decide what the statement claims, find the matching sentence, and compare only those two things."),
            B.diagram(
                caption = "Three outcomes, one decision each. 'Not Given' is not 'I could not find it'; it is 'the passage does not say either way'.",
                nodes = listOf(
                    DNode("q", "statement", 0.5f, 0.12f, w = 0.3f, h = 0.16f, tone = 1),
                    DNode("t", "TRUE", 0.18f, 0.6f, w = 0.26f, h = 0.34f, tone = 2, sub = "the passage says the same thing"),
                    DNode("f", "FALSE", 0.5f, 0.6f, w = 0.26f, h = 0.34f, sub = "the passage says the opposite"),
                    DNode("n", "NOT GIVEN", 0.82f, 0.6f, w = 0.26f, h = 0.34f, tone = 2, sub = "the passage does not address it"),
                ),
                edges = listOf(DEdge("q", "t"), DEdge("q", "f"), DEdge("q", "n")),
                aspect = 0.62f,
            ),
            B.table(
                listOf("Rule", "Why"),
                listOf("Answers come in passage order", "Found question 5? Question 6 is below it. This alone saves minutes."),
                listOf("Judge the statement, not the topic", "The paragraph may be about the right topic and still not make the claim."),
                listOf("FALSE needs a contradiction", "If the passage says 'some' and the statement says 'all', that is a contradiction: FALSE."),
                listOf("NOT GIVEN needs an absence", "The passage talks about the thing but never states this. Comparison claims are often NOT GIVEN."),
                listOf("Use the exact words asked for", "Write TRUE, FALSE, NOT GIVEN, or YES, NO, NOT GIVEN as the instruction says. Mixing them is marked wrong."),
            ),
            B.h("The four signals"),
            B.table(
                listOf("Signal in the statement", "Likely answer", "Example"),
                listOf("all, every, always, never, only", "often FALSE", "Passage: 'most species'. Statement: 'all species'."),
                listOf("a comparison the passage never makes", "often NOT GIVEN", "Passage praises A and B separately. Statement: 'A is better than B'."),
                listOf("a reason or cause not stated", "often NOT GIVEN", "Passage: 'sales fell'. Statement: 'sales fell because of price'."),
                listOf("an exact synonym of the passage claim", "often TRUE", "Passage: 'rarely exceeds'. Statement: 'seldom goes above'."),
            ),
            B.h("Worked passage"),
            B.p("Passage: The honeybee's waggle dance was first decoded by Karl von Frisch in the 1940s. By repeating a figure-of-eight pattern, a returning forager indicates both the direction and the distance of a food source. Most researchers now accept this interpretation, although a minority argue that scent plays a larger role than the dance itself. Colonies in which the dance was experimentally disrupted still located food, but took significantly longer to do so."),
            B.check("ie3-tfng-k1", "ie3-tfng", "Statement: Von Frisch decoded the waggle dance in the 1940s.", listOf("TRUE", "FALSE", "NOT GIVEN"), 0, "The first sentence states exactly this. TRUE."),
            B.check("ie3-tfng-k2", "ie3-tfng", "Statement: All researchers accept von Frisch's interpretation.", listOf("TRUE", "FALSE", "NOT GIVEN"), 1, "The passage says most accept it and a minority disagree. 'All' contradicts that: FALSE."),
            B.check("ie3-tfng-k3", "ie3-tfng", "Statement: Von Frisch's work was funded by the German government.", listOf("TRUE", "FALSE", "NOT GIVEN"), 2, "Funding is never mentioned. NOT GIVEN."),
            B.check("ie3-tfng-k4", "ie3-tfng", "Statement: Bees whose dance was disrupted could not find food.", listOf("TRUE", "FALSE", "NOT GIVEN"), 1, "They still located food, just more slowly. That contradicts 'could not': FALSE."),
            B.check("ie3-tfng-k5", "ie3-tfng", "Statement: Scent is more important to foraging than the dance.", listOf("TRUE", "FALSE", "NOT GIVEN"), 2, "A minority argue this; the passage does not state it as fact, so the claim itself is not given."),
            B.h("Yes / No / Not Given"),
            B.p("Identical mechanics, applied to the writer's opinion rather than facts. Look for opinion markers: arguably, it is clear that, unfortunately, surprisingly, critics claim. If the view belongs to someone else quoted in the passage, it is not the writer's view."),
            B.mistakes(
                "Answering FALSE when the passage simply says nothing: that is NOT GIVEN.",
                "Answering NOT GIVEN because the words are different: look for the paraphrase.",
                "Writing T, F, NG when the instruction asks for the full words, or mixing TRUE with YES.",
                "Spending four minutes on one statement; mark your best guess and move on.",
            ),
            B.vocab(
                B.w("ie3-contradict", "contradict", "state the opposite of", "The passage contradicts the statement.", "That makes it FALSE."),
                B.w("ie3-minority", "a minority", "a small number of people", "A minority of researchers disagree.", "Opposite: the majority."),
                B.w("ie3-claim", "claim", "state something as true", "The writer claims that…", "An opinion marker."),
                B.w("ie3-arguably", "arguably", "it can be argued that", "Arguably the most important finding.", "Signals the writer's view."),
                B.w("ie3-significantly", "significantly", "to an important degree", "Took significantly longer.", "Common in research passages."),
                B.w("ie3-interpretation", "interpretation", "an explanation of what something means", "Most accept this interpretation.", "Not the same as proof."),
            ),
            B.glossary("TFNG" to "True / False / Not Given, about facts.", "YNNG" to "Yes / No / Not Given, about the writer's opinion.", "Opinion marker" to "A word showing a view rather than a fact."),
            B.recap("Answers run in passage order; find the sentence, compare only the claim.", "FALSE needs a contradiction; NOT GIVEN needs silence; all/every/only usually signal FALSE.", "Use the exact words the instruction asks for and never leave a blank."),
        ),
    )

    private val headings = Topic(
        id = "ie3-headings", title = "Matching headings, matching information and summary completion", minutes = 26, references = refs,
        blocks = listOf(
            B.warmup(
                "ie3-headings-w1", "ie3-headings",
                "A matching-headings task gives nine headings for six paragraphs. Why do you think there are three spare?",
                listOf("A printing convention", "So that a near-match cannot be reached by elimination", "Some paragraphs take two"), 1,
                "The spares are there to be tempting, not to be used.",
            ),
            B.plain("These three types test whether you can tell a paragraph's main idea from its details. They are the reason skimming matters: the work is done before you read a single question if your skim was honest."),
            B.h("Matching headings"),
            B.bullets(
                "There are always more headings than paragraphs, so some are never used. Never panic about leftovers.",
                "A heading describes the whole paragraph, not its most interesting sentence. If the heading fits only one example in the paragraph, it is the wrong heading.",
                "Do the paragraphs you are sure about first, then re-read the remaining headings for the rest. Cross off as you go.",
                "The first and last sentences usually carry the main idea; the middle carries evidence.",
            ),
            B.diagram(
                caption = "A paragraph has one main idea and several supporting details. The heading matches the idea; the distractor matches a detail.",
                nodes = listOf(
                    DNode("p", "Paragraph C", 0.2f, 0.5f, w = 0.26f, h = 0.4f, tone = 1),
                    DNode("m", "main idea: why the project failed", 0.58f, 0.28f, w = 0.4f, h = 0.22f, tone = 2, sub = "the heading"),
                    DNode("d", "detail: the cost of the first prototype", 0.58f, 0.72f, w = 0.4f, h = 0.22f, sub = "the distractor heading"),
                ),
                edges = listOf(DEdge("p", "m"), DEdge("p", "d", dashed = true)),
                aspect = 0.7f,
            ),
            B.h("Worked example"),
            B.p("Paragraph: When the city introduced its bicycle-sharing scheme, planners expected commuters to be the main users. Surveys after the first year told a different story. Nearly two thirds of journeys were made at weekends, and the most popular routes ran through parks rather than between stations and offices. The scheme was, in effect, being used for recreation."),
            B.check("ie3-headings-k1", "ie3-headings", "Which heading fits the paragraph?", listOf("The cost of running a bicycle scheme", "An unexpected pattern of use", "How commuters travel to work"), 1, "The paragraph's whole point is that use differed from expectations. The other two match single words, not the idea."),
            B.check(
                "ie3-headings-k2", "ie3-headings",
                "Nine headings are offered for six paragraphs. The three spares exist",
                listOf("so a near-match cannot be reached by elimination", "because some paragraphs take two", "as a printing convention"), 0,
                "The extras are built to be tempting. They also mean leftovers at the end are normal and not a sign you have gone wrong.",
                whyWrong = mapOf(
                    1 to "Each paragraph takes exactly one heading unless the instructions say otherwise, and they do not.",
                    2 to "They are written, not left over.",
                ),
            ),
            B.check(
                "ie3-headings-k3", "ie3-headings",
                "Matching information to paragraphs is best left until last because",
                listOf("it is slow, and the keywords you located for other questions help", "it is worth fewer marks", "it is usually guessable"), 0,
                "It sends you hunting through the whole passage for a detail. Doing it after the other types means you already know where most things are.",
                whyWrong = mapOf(
                    1 to "Every question in Reading is worth one mark.",
                    2 to "A buried detail is the hardest thing in the paper to guess.",
                ),
            ),
            B.h("Matching information to paragraphs"),
            B.p("Here you are told a fact and must say which paragraph contains it. Unlike headings, the fact can be a detail buried anywhere, and one paragraph may hold two answers. This type is slow: leave it until last, and use the keywords you have already located while answering other questions."),
            B.h("Summary completion"),
            B.table(
                listOf("Variety", "Rule"),
                listOf("From the passage", "Use the exact word printed in the text; obey the word limit"),
                listOf("From a box of options", "The box uses synonyms, not the passage words; grammar narrows the choice fast"),
                listOf("Both", "Read the whole summary first: it paraphrases one section of the passage in order"),
            ),
            B.p("Summary: The bicycle scheme was designed for people travelling to ___ (1), but most trips took place at the ___ (2), and riders preferred routes through ___ (3)."),
            B.gap("ie3-h-g1", "Summary completion, ONE WORD from the passage.", "designed for people travelling to ___", "work", "offices", hint = "Look for the planners' expectation."),
            B.gap("ie3-h-g2", "Summary completion, ONE WORD from the passage.", "most trips took place at the ___", "weekends", "weekend"),
            B.gap("ie3-h-g3", "Summary completion, ONE WORD from the passage.", "riders preferred routes through ___", "parks", "park"),
            B.remember("In summary completion the grammar tells you the word class before you find the word. A gap after 'the' needs a noun; a gap after 'was' needs an adjective or a participle. Predict, then confirm."),
            B.h("Other types in one line each"),
            B.table(
                listOf("Type", "The one thing to remember"),
                listOf("Multiple choice", "Options paraphrase; eliminate two quickly, then compare the last two word by word"),
                listOf("Matching features (names to statements)", "Scan for the capitalised names first, then read around each"),
                listOf("Sentence completion", "The answer is grammatically continuous with the stem; the word limit is strict"),
                listOf("Diagram or flow-chart labelling", "Work through the diagram in order; answers come from one section only"),
                listOf("Short answer", "Copy the exact word; do not change singular to plural"),
            ),
            B.mistakes(
                "Picking a heading because it repeats a word from the paragraph.",
                "Doing matching-information questions in order instead of last.",
                "Changing the form of a word copied from the passage.",
                "Ignoring the word limit in summary completion; two words where one is allowed scores zero.",
            ),
            B.vocab(
                B.w("ie3-commuter", "commuter", "someone travelling to work", "The scheme targeted commuters.", "Common in urban passages."),
                B.w("ie3-recreation", "recreation", "leisure, enjoyment", "Used for recreation, not transport.", "Adjective: recreational."),
                B.w("ie3-in-effect", "in effect", "in practice, effectively", "In effect, it was a leisure scheme.", "Signals a conclusion."),
                B.w("ie3-two-thirds", "two thirds", "a fraction, 66 percent", "Nearly two thirds of journeys.", "Fractions are common answers."),
                B.w("ie3-distractor-b", "distractor", "a plausible wrong option", "The distractor matches a detail.", "Not the main idea."),
                B.w("ie3-stem", "stem", "the part of the sentence before the gap", "The stem decides the word class.", "Used in completion tasks."),
            ),
            B.glossary("Main idea" to "What the whole paragraph is about.", "Supporting detail" to "Evidence or an example inside a paragraph.", "Word limit" to "The maximum words per answer, printed in the instruction."),
            B.recap("Headings match the main idea, never a detail; spare headings are normal.", "Matching information is slow: do it last with keywords you already found.", "In summary completion, predict the word class from grammar, then obey the word limit."),
        ),
    )

    val module = Module(
        id = "ie-m3", number = 3, title = "Reading", summary = "Timing, True/False/Not Given, matching headings, summary completion, Academic and General differences.",
        examWeight = "25 percent", componentId = "reading", topics = listOf(timing, tfng, headings), practiceMinutes = 720,
        quiz = Quiz(
            id = "ie-m3-test", title = "Module 3 test", passPercent = 70,
            questions = B.spread(listOf(
                B.q(
                    "ie3-q1", "ie3-timing", "How long should one Reading section take?",
                    listOf("about 10 minutes", "about 20 minutes", "as long as it needs"), setOf(1),
                    "Three sections in sixty minutes means twenty each, with the last few minutes for transfer and guesses.",
                    whyWrong = mapOf(
                        0 to "Ten each would leave half an hour spare, which nobody has ever had at the end of this paper.",
                        2 to "'As long as it needs' is how section 3 ends up unread. The clock is the real examiner here.",
                    ),
                ),
                B.q(
                    "ie3-q2", "ie3-timing", "On the paper test, transfer time for Reading is",
                    listOf("10 minutes extra", "not given: it is inside the 60 minutes", "5 minutes extra"), setOf(1),
                    "Unlike Listening, Reading has no extra transfer time.",
                    whyWrong = mapOf(
                        0 to "Ten minutes of transfer is the Listening paper. Reading gives none, which is why the last three minutes have to be budgeted.",
                        2 to "There is no transfer allowance at all, of any length.",
                    ),
                ),
                B.q(
                    "ie3-q3", "ie3-timing", "You meet a word you do not know in a question sentence. You should",
                    listOf("skip the question", "use the surrounding sentence and any contrast after 'but'", "look for the word in every paragraph"), setOf(1),
                    "Unknown vocabulary is designed in; the context carries the meaning.",
                    whyWrong = mapOf(
                        0 to "Every paper contains words you do not know. Skipping on that basis would cost you most of section 3.",
                        2 to "Hunting one word through the passage is the slowest thing you can do, and the passage usually paraphrases it anyway.",
                    ),
                ),
                B.q(
                    "ie3-q4", "ie3-timing", "General Training Reading differs from Academic in that",
                    listOf("it has fewer questions", "its texts are everyday and the conversion is stricter", "it has no long passage"), setOf(1),
                    "Easier texts, stricter conversion: 34 of 40 for a 7.",
                    whyWrong = mapOf(
                        0 to "Both are 40 questions in 60 minutes. Only the texts and the conversion change.",
                        2 to "General ends with one longer passage, after the shorter everyday texts.",
                    ),
                ),
                B.q(
                    "ie3-q5", "ie3-tfng", "The passage says 'sales rarely exceed 200 units'. Statement: 'Sales seldom go above 200 units.' The answer is",
                    listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(0),
                    "'Seldom go above' paraphrases 'rarely exceed'.",
                    whyWrong = mapOf(
                        1 to "Nothing is contradicted. The statement says the same thing in other words, which is what TRUE means here.",
                        2 to "The information is given; only the wording has changed. NOT GIVEN is for what the passage never addresses.",
                    ),
                ),
                B.q(
                    "ie3-q6", "ie3-tfng", "The passage describes two methods in detail but never compares them. Statement: 'The first method is more accurate.' The answer is",
                    listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(2),
                    "A comparison the passage never makes is NOT GIVEN.",
                    whyWrong = mapOf(
                        0 to "Being described first, or at length, is not a claim about accuracy. You would be supplying the comparison yourself.",
                        1 to "FALSE needs the passage to contradict the statement. Saying nothing is not contradicting.",
                    ),
                ),
                B.q(
                    "ie3-q7", "ie3-tfng", "The passage says 'most of the buildings survived'. Statement: 'All the buildings survived.' The answer is",
                    listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1),
                    "'All' contradicts 'most': FALSE.",
                    whyWrong = mapOf(
                        0 to "'Most' and 'all' are not the same claim. Quantifiers are the commonest place this type is won and lost.",
                        2 to "The passage does address it: it says most. A statement the passage contradicts is FALSE, not NOT GIVEN.",
                    ),
                ),
                B.q(
                    "ie3-q8", "ie3-tfng", "Yes/No/Not Given questions ask about",
                    listOf("facts in the passage", "the writer's opinion", "your own opinion"), setOf(1),
                    "YNNG targets the writer's views; TFNG targets factual claims.",
                    whyWrong = mapOf(
                        0 to "Facts are TRUE/FALSE/NOT GIVEN. The change of label is the clue that you are now dealing with views.",
                        2 to "Your opinion is never asked for anywhere in the Reading paper.",
                    ),
                ),
                B.q(
                    "ie3-q9", "ie3-tfng", "Writing 'NG' when the instruction says NOT GIVEN",
                    listOf("is accepted", "is marked wrong", "depends on the examiner"), setOf(1),
                    "Use the exact wording the instruction gives.",
                    whyWrong = mapOf(
                        0 to "Abbreviations you invent are not on the mark scheme, and this one costs a mark you had already earned.",
                        2 to "Marking is against a fixed key. Nothing here is left to judgement.",
                    ),
                ),
                B.q(
                    "ie3-q10", "ie3-headings", "A heading should match",
                    listOf("the most interesting sentence", "the paragraph's main idea", "the first word of the paragraph"), setOf(1),
                    "Headings are about the whole paragraph; details are distractors.",
                    whyWrong = mapOf(
                        0 to "The striking detail is usually what the wrong heading is built from. A paragraph can mention a thing without being about it.",
                        2 to "The main idea often arrives in the second or third sentence, after a lead-in.",
                    ),
                ),
                B.q(
                    "ie3-q11", "ie3-headings", "There are nine headings for six paragraphs. This means",
                    listOf("three paragraphs take two headings", "three headings are not used", "you have misread the task"), setOf(1),
                    "Extra headings are always provided as distractors.",
                    whyWrong = mapOf(
                        0 to "Each paragraph takes one heading. Doubling up is never allowed in this type.",
                        2 to "It is the normal shape of the task. Spare headings are the point.",
                    ),
                ),
                B.q(
                    "ie3-q12", "ie3-headings", "Matching-information questions are best done",
                    listOf("first, in order", "last, using keywords already located", "not at all"), setOf(1),
                    "They are the slowest type and benefit from work already done.",
                    whyWrong = mapOf(
                        0 to "They do not run in passage order, so doing them first means reading the whole text with nothing to look for.",
                        2 to "Skipping a question set guarantees the marks are lost, and there is no penalty for a guess.",
                    ),
                ),
                B.q(
                    "ie3-q13", "ie3-headings", "A summary gap follows the word 'the'. The answer is most likely",
                    listOf("a verb", "a noun", "an adverb"), setOf(1),
                    "Grammar narrows the word class before you search.",
                    whyWrong = mapOf(
                        0 to "'The' is not followed by a verb. Letting grammar rule options out is half the work of this type.",
                        2 to "An adverb after 'the' would need a noun behind it, so the gap itself would still be the noun.",
                    ),
                ),
                B.q(
                    "ie3-q14", "ie3-timing", "With one minute left and four blanks, you should",
                    listOf("guess all four", "leave them blank", "re-read the passage"), setOf(0),
                    "There is no penalty for a wrong answer, so always guess.",
                    whyWrong = mapOf(
                        1 to "A blank scores nothing with certainty. A guess on a three-option question scores about a third of the time.",
                        2 to "A minute is not enough to re-read anything, and it is the minute the guesses need.",
                    ),
                ),
            )),
        ),
    )
}
