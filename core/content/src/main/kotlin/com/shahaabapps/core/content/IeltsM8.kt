package com.shahaabapps.core.content

/** IELTS module 8: the language that carries the band, and the two weeks before the test. */
internal object IeltsM8 {
    private val refs = Ielts.commonRefs

    private val lexis = Topic(
        id = "ie8-lexis", title = "Lexical Resource: collocation, topic banks and paraphrase", minutes = 28, references = refs,
        blocks = listOf(
            B.warmup(
                "ie8-lexis-w1", "ie8-lexis",
                "Two essays. One uses three rare words, two of them slightly wrong. The other uses ordinary words that all sit with the right partners. Which do you think scores higher on Lexical Resource?",
                listOf("The rare one, for range", "The ordinary one", "They tie"), 1,
                "The criterion is not about how unusual a word is.",
            ),
            B.plain("Lexical Resource is a quarter of your Writing and Speaking band. It does not reward rare words; it rewards the right word in the right company, used naturally. Three things move it: collocation, a bank of topic language, and the ability to say a thing another way."),
            B.h("Collocation: words that travel together"),
            B.diagram(
                caption = "Band 7 is built from accurate pairs, not long words. A wrong pair is noticed more than a simple one.",
                nodes = listOf(
                    DNode("a", "make a decision", 0.26f, 0.3f, w = 0.4f, h = 0.22f, tone = 2),
                    DNode("b", "do a decision", 0.74f, 0.3f, w = 0.4f, h = 0.22f, sub = "wrong"),
                    DNode("c", "heavy traffic", 0.26f, 0.72f, w = 0.4f, h = 0.22f, tone = 2),
                    DNode("d", "strong traffic", 0.74f, 0.72f, w = 0.4f, h = 0.22f, sub = "wrong"),
                ),
                aspect = 0.45f,
            ),
            B.table(
                listOf("Verb", "Common partners"),
                listOf("make", "a decision, progress, an effort, a contribution, a profit, friends"),
                listOf("do", "research, homework, damage, business, your best"),
                listOf("take", "action, a risk, responsibility, measures, into account"),
                listOf("have", "an impact on, access to, an opportunity, difficulty in"),
                listOf("play", "a role in, a part in"),
                listOf("raise", "awareness, standards, concerns, taxes"),
                listOf("meet", "a need, a deadline, a target, requirements"),
                listOf("pose", "a threat, a risk, a problem, a challenge"),
            ),
            B.gap("ie8-x-g1", "Collocation.", "The government should ___ action to reduce emissions.", "take"),
            B.gap("ie8-x-g2", "Collocation.", "Social media has a significant ___ on young people.", "impact", "influence", "effect"),
            B.gap("ie8-x-g3", "Collocation.", "Air pollution ___ a serious threat to public health.", "poses", "presents"),
            B.gap("ie8-x-g4", "Collocation.", "Schools should ___ awareness of mental health.", "raise", "promote", "increase"),
            B.h("Topic banks: eight topics cover the test"),
            B.table(
                listOf("Topic", "Language worth owning"),
                listOf("Education", "curriculum, rote learning, vocational training, drop-out rate, tuition fees, lifelong learning"),
                listOf("Environment", "carbon footprint, renewable sources, biodiversity, landfill, emissions, sustainable"),
                listOf("Technology", "automation, screen time, digital literacy, surveillance, obsolete, connectivity"),
                listOf("Health", "sedentary lifestyle, preventive care, life expectancy, obesity, mental wellbeing, public health"),
                listOf("Work", "work-life balance, job security, remote working, workforce, redundancy, productivity"),
                listOf("Cities and transport", "congestion, infrastructure, urban sprawl, commuter, pedestrianised, affordable housing"),
                listOf("Crime and law", "deterrent, rehabilitation, petty crime, custodial sentence, surveillance, law-abiding"),
                listOf("Media and culture", "mass media, cultural heritage, globalisation, censorship, stereotype, mainstream"),
            ),
            B.remember("Learn these as phrases inside sentences, never as single words on a list. 'A sedentary lifestyle contributes to obesity' is usable in an exam; 'sedentary: not moving' is not."),
            B.h("Paraphrase: the single most transferable skill"),
            B.p("Every paper tests it. Listening and Reading hide the answer behind a paraphrase; Writing requires you to restate the prompt; Speaking needs it when a word will not come. Practise it in both directions."),
            B.table(
                listOf("Original", "Paraphrase"),
                listOf("Children should start school at four.", "Formal education ought to begin at the age of four."),
                listOf("Governments should spend more on health.", "Public funds ought to be directed towards healthcare."),
                listOf("Cars cause pollution in cities.", "Private vehicles contribute to urban air pollution."),
                listOf("Many people work from home now.", "Remote working has become widespread."),
                listOf("It is cheap.", "It is relatively inexpensive / It costs comparatively little."),
            ),
            B.gap("ie8-x-g5", "Paraphrase the underlined idea.", "Many students ___ (cannot afford) university fees.", "struggle to afford", "are unable to afford", "cannot meet"),
            B.speak("ie8-x-s1", "Private vehicles contribute significantly to urban air pollution.", "Say the formal version aloud. In Speaking, upgrading 'cars make cities dirty' to this is exactly what Lexical Resource rewards."),
            B.speak("ie8-x-s2", "A sedentary lifestyle is widely believed to contribute to rising obesity rates.", "Topic language plus hedging in one sentence."),
            B.mistakes(
                "Dropping a rare word in the wrong place; one misused show-off word costs more than ten simple correct ones.",
                "Using the same word four times in an essay when a pronoun or synonym would do.",
                "Learning word lists without their collocations.",
                "Informal vocabulary in Writing: kids, stuff, a lot of, things like that.",
            ),
            B.check("ie8-lexis-k1", "ie8-lexis", "Which is the natural collocation?", listOf("do a decision", "make a decision", "take a decision about nothing"), 1, "Make is the verb that goes with decision."),
            B.vocab(
                B.w("ie8-collocation", "collocation", "words that naturally occur together", "Heavy traffic is a collocation.", "Strong traffic is not."),
                B.w("ie8-sedentary", "sedentary lifestyle", "a way of living with little exercise", "A sedentary lifestyle harms health.", "Health topic bank."),
                B.w("ie8-infrastructure", "infrastructure", "roads, rail, power, water", "Investment in infrastructure.", "Cities topic bank."),
                B.w("ie8-deterrent", "deterrent", "something that discourages an action", "Longer sentences act as a deterrent.", "Crime topic bank."),
                B.w("ie8-rote", "rote learning", "memorising without understanding", "Rote learning is criticised.", "Education topic bank."),
                B.w("ie8-obsolete", "obsolete", "out of date, no longer used", "The technology became obsolete.", "Technology topic bank."),
                B.w("ie8-redundancy", "redundancy", "job loss because the role ends", "Automation may cause redundancies.", "Work topic bank."),
                B.w("ie8-urban-sprawl", "urban sprawl", "uncontrolled spread of a city", "Urban sprawl consumes farmland.", "Cities topic bank."),
            ),
            B.glossary("Lexical Resource" to "The vocabulary criterion in Writing and Speaking.", "Topic bank" to "A set of phrases owned for one subject area.", "Register" to "Formality level; essays are formal, Speaking is neutral."),
            B.recap("Collocation first: make a decision, take action, pose a threat.", "Own eight topic banks as sentences, not word lists.", "Paraphrase in both directions; it is tested in all four papers."),
        ),
    )

    private val grammar = Topic(
        id = "ie8-grammar", title = "Grammatical Range and Accuracy: the four structures that lift a band", minutes = 28, references = refs,
        blocks = listOf(
            B.warmup(
                "ie8-grammar-w1", "ie8-grammar",
                "Band 7 wants 'a variety of complex structures'. How many different ones do you think you actually need to have ready?",
                listOf("As many as possible", "About four, used accurately", "One, used everywhere"), 1,
                "Variety is about mixing a handful well, not collecting them.",
            ),
            B.plain("Band 7 asks for a variety of complex structures and a majority of error-free sentences. You do not need exotic grammar. Four structures, used accurately and mixed with simple sentences, are enough."),
            B.diagram(
                caption = "Range and accuracy pull against each other. Band 7 needs both: complex structures you can actually control.",
                nodes = listOf(
                    DNode("a", "only simple sentences", 0.2f, 0.3f, w = 0.34f, h = 0.22f, sub = "accurate, band 5 to 6"),
                    DNode("b", "complex but error-filled", 0.2f, 0.72f, w = 0.34f, h = 0.22f, sub = "band 5 to 6"),
                    DNode("c", "a mix, mostly error-free", 0.72f, 0.5f, w = 0.4f, h = 0.3f, tone = 1, sub = "band 7+"),
                ),
                edges = listOf(DEdge("a", "c"), DEdge("b", "c")),
                aspect = 0.7f,
            ),
            B.h("1 Relative clauses"),
            B.table(
                listOf("Form", "Example"),
                listOf("defining (no commas)", "Students who live far from campus often arrive late."),
                listOf("non-defining (commas)", "The new policy, which was introduced last year, has had little effect."),
                listOf("with a preposition", "The city in which I grew up has changed beyond recognition."),
                listOf("with 'which' commenting on the whole clause", "Fees have doubled, which has deterred many applicants."),
            ),
            B.gap("ie8-g-g1", "Non-defining relative clause.", "The scheme, ___ began in 2020, has reduced waste.", "which"),
            B.h("2 Conditionals"),
            B.table(
                listOf("Type", "Use", "Example"),
                listOf("First", "A real future possibility", "If governments invest now, emissions will fall."),
                listOf("Second", "Hypothetical present or future", "If public transport were cheaper, fewer people would drive."),
                listOf("Third", "Past that did not happen", "If the policy had been introduced earlier, the crisis might have been avoided."),
                listOf("Mixed", "Past cause, present result", "If she had studied medicine, she would be a doctor now."),
                listOf("Unless", "Negative condition", "Unless action is taken, the problem will worsen."),
            ),
            B.gap("ie8-g-g2", "Second conditional.", "If public transport ___ cheaper, fewer people would drive.", "were", "was"),
            B.gap("ie8-g-g3", "Third conditional.", "If the warning ___ been heeded, the damage would have been smaller.", "had"),
            B.h("3 The passive, and when it is right"),
            B.p("Use the passive when the doer is unknown, obvious or unimportant: process descriptions, formal letters, and essay sentences about society. Do not use it everywhere; an essay in which every sentence is passive reads badly."),
            B.table(
                listOf("Active", "Passive and why"),
                listOf("The council built the bridge in 2010.", "The bridge was built in 2010. (the builder is unimportant)"),
                listOf("People believe that…", "It is widely believed that… (impersonal and formal)"),
                listOf("Someone should do something about it.", "Measures should be taken. (no need for an agent)"),
            ),
            B.h("4 Complex noun phrases and nominalisation"),
            B.p("Turning a verb into a noun compresses an idea and raises the register. This is the structure that most separates band 7 from band 8 in Writing."),
            B.table(
                listOf("Simple", "Nominalised"),
                listOf("Cities are growing quickly, and this causes problems.", "Rapid urban growth creates a series of problems."),
                listOf("People use cars too much, so the air is polluted.", "Excessive car use is a major cause of air pollution."),
                listOf("The government decided to raise taxes, and people were angry.", "The government's decision to raise taxes provoked public anger."),
            ),
            B.gap("ie8-g-g4", "Nominalise: 'The population increased rapidly, which caused…'", "The rapid ___ in population caused…", "increase", "growth", "rise"),
            B.h("The errors that most often break 'error-free'"),
            B.table(
                listOf("Error", "Fix"),
                listOf("Articles: 'Government should invest'", "The government should invest / Governments should invest"),
                listOf("Subject-verb: 'The number of students are rising'", "The number of students is rising"),
                listOf("Uncountables: 'informations', 'advices', 'researches'", "information, advice, research (no plural)"),
                listOf("Preposition after a verb: 'discuss about'", "discuss something, no preposition"),
                listOf("'Despite' plus a clause", "Despite the cost / Although it is expensive"),
                listOf("Comma splice: 'It is cheap, it is popular.'", "It is cheap, so it is popular. / It is cheap; it is popular."),
            ),
            B.gap("ie8-g-g5", "Fix the agreement.", "The number of applicants ___ risen sharply.", "has"),
            B.gap("ie8-g-g6", "Despite plus a noun phrase.", "___ the high cost, demand continued to grow.", "Despite", "despite"),
            B.speak("ie8-g-s1", "If public transport were more reliable, far fewer people would choose to drive into the city centre.", "A second conditional said aloud. Speaking rewards the same structures as Writing."),
            B.speak("ie8-g-s2", "The government's decision to raise fees, which was announced last year, has provoked considerable criticism.", "Nominalisation plus a non-defining relative clause in one sentence."),
            B.check("ie8-grammar-k1", "ie8-grammar", "Which sentence is error-free?", listOf("The number of students are increasing every year.", "The number of students is increasing every year.", "The numbers of student is increasing every year."), 1, "'The number of' takes a singular verb."),
            B.mistakes(
                "Writing every sentence long; a short sentence after two long ones is good style and easy to get right.",
                "Using 'Despite' with a full clause instead of 'Although'.",
                "Pluralising uncountable nouns: informations, advices, equipments.",
                "Comma splices joining two independent clauses with a comma.",
            ),
            B.vocab(
                B.w("ie8-nominalisation", "nominalisation", "turning a verb or adjective into a noun", "Rapid growth rather than it grew rapidly.", "Raises register and compresses ideas."),
                B.w("ie8-provoke", "provoke", "cause a strong reaction", "The decision provoked criticism.", "Formal and precise."),
                B.w("ie8-heed", "heed a warning", "pay attention to it", "If the warning had been heeded…", "Used in third conditionals."),
                B.w("ie8-comma-splice", "comma splice", "joining two sentences with only a comma", "Avoid comma splices.", "Use a full stop, semicolon or connector."),
                B.w("ie8-uncountable", "uncountable noun", "a noun with no plural form", "Information is uncountable.", "No -s, no 'an'."),
                B.w("ie8-beyond-recognition", "beyond recognition", "so changed it can hardly be recognised", "The city has changed beyond recognition.", "A natural collocation."),
            ),
            B.glossary("Error-free sentence" to "A sentence with no grammatical mistake; band 7 needs a majority.", "Defining relative clause" to "One that identifies which thing is meant; no commas.", "Nominalisation" to "Expressing an action as a noun."),
            B.recap("Four structures: relative clauses, conditionals, the passive, nominalisation.", "Mix complex with short simple sentences; accuracy counts as much as range.", "Drill the six common errors: articles, agreement, uncountables, prepositions, despite, comma splices."),
        ),
    )

    private val finalWeeks = Topic(
        id = "ie8-final", title = "The last two weeks, and the test day itself", minutes = 20, references = refs,
        blocks = listOf(
            B.warmup(
                "ie8-final-w1", "ie8-final",
                "Three days to go. What do you think is the best use of them?",
                listOf("A new grammar book", "Consolidating what you have and sleeping", "Two full tests a day"), 1,
                "Nothing learnt in three days arrives in time to be reliable.",
            ),
            B.plain("By this point the technique is known and the language is being built. The last fortnight is about simulation and sleep, not new material. Here is exactly what to do with it."),
            B.diagram(
                caption = "Two weeks: full papers under timing in the first, tapering and rehearsal in the second. Nothing new after day ten.",
                nodes = listOf(
                    DNode("a", "Days 1 to 7", 0.22f, 0.5f, w = 0.38f, h = 0.4f, tone = 1, sub = "two full tests, all four papers, timed; mark and log every error"),
                    DNode("b", "Days 8 to 12", 0.62f, 0.5f, w = 0.38f, h = 0.4f, tone = 2, sub = "one paper a day, review the error log, rehearse Part 2 cards"),
                    DNode("c", "Day 13 to 14", 0.91f, 0.5f, w = 0.16f, h = 0.4f, sub = "rest, admin, sleep"),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c")),
                aspect = 0.8f,
            ),
            B.steps("The fortnight, day by day",
                "Days 1 and 4: sit a complete test, all four papers, in one sitting, at the time of day your real test starts." to "Stamina is a real variable; three papers back to back is tiring.",
                "Days 2, 3, 5, 6: mark the test and write the reason for every wrong answer." to "Group the reasons; two or three causes will dominate.",
                "Day 7: drill only the dominant causes." to "Usually paraphrase, timing, or a grammar error that recurs.",
                "Days 8 to 12: one paper a day plus one marked writing task, rewritten." to "Keep the writing habit daily; it is the slowest skill to move.",
                "Days 8 to 12 also: speak for two minutes a day from a random cue card, recorded." to "Listen back once; note only hesitation points.",
                "Day 13: check the booking, the venue, the passport, and go to bed early." to "No new material.",
                "Day 14: arrive early, eat beforehand, take water." to "There is no break between the three written papers.",
            ),
            B.h("In the room"),
            B.table(
                listOf("Paper", "The one rule that saves marks"),
                listOf("Listening", "Keep up. A missed answer is one mark; losing your place costs five."),
                listOf("Reading", "Twenty minutes a section, enforced. Guess every blank before you move on."),
                listOf("Writing", "Twenty minutes Task 1, forty Task 2. Start Task 2 first if you tend to overrun."),
                listOf("Speaking", "Answer the question that was asked, not the one you prepared for."),
            ),
            B.remember("If you finish Task 2 with five minutes left, spend them checking articles, subject-verb agreement and plural -s. Those three fixes raise Grammatical Accuracy more than another sentence would."),
            B.h("If a paper goes badly"),
            B.p("Candidates lose whole bands by carrying a bad Listening into Reading. The papers are marked separately; the next one is untouched by the last. Close it and start again. If the result does overall fall short, One Skill Retake lets you resit a single component rather than the whole test, where it is offered."),
            B.check("ie8-final-k1", "ie8-final", "Three days before the test is the best time to", listOf("learn a new grammar area", "review your error log and rehearse cue cards", "sit three full tests"), 1, "The last days consolidate; new material unsettles more than it adds."),
            B.speak("ie8-f-s1", "I'd like to talk about a decision I made that changed my life.", "A cue-card opener you can use with almost any 'describe a time when' card."),
            B.speak("ie8-f-s2", "That's an interesting question. I suppose the main reason would be economic, at least in my country.", "A hedged opener for Part 3 that buys you two seconds of thinking time."),
            B.vocab(
                B.w("ie8-stamina", "stamina", "the ability to keep going", "Three papers back to back needs stamina.", "Build it with full tests."),
                B.w("ie8-taper", "taper", "reduce gradually before an event", "Taper your study in the final days.", "Borrowed from athletics."),
                B.w("ie8-simulate", "simulate", "reproduce the real conditions", "Simulate the test, including the time of day.", "Timed practice."),
                B.w("ie8-consolidate", "consolidate", "make existing knowledge firm", "The last week consolidates.", "Opposite of learning new material."),
                B.w("ie8-retake", "One Skill Retake", "resitting a single component", "A retake can lift one paper.", "Where it is offered."),
            ),
            B.glossary("Error log" to "Your list of wrong answers and their causes.", "Full test" to "All four papers in one sitting under timing.", "One Skill Retake" to "Resitting one component instead of the whole test."),
            B.recap("Week one: two full timed tests plus error analysis.", "Week two: one paper a day, daily writing and a recorded two-minute turn.", "Last two days: rest and admin; in the room, keep pace and never carry one paper into the next."),
        ),
    )

    val module = Module(
        id = "ie-m8", number = 8, title = "Language and final preparation", summary = "Collocation, topic banks, paraphrase, the four complex structures, and the last two weeks.",
        examWeight = "all four", componentId = "reading", topics = listOf(lexis, grammar, finalWeeks), practiceMinutes = 600,
        quiz = Quiz(
            id = "ie-m8-test", title = "Module 8 test", passPercent = 70,
            questions = B.spread(listOf(
                B.q(
                    "ie8-q1", "ie8-lexis", "Which is the correct collocation?",
                    listOf("do a decision", "make a decision", "have a decision"), setOf(1),
                    "Make is the verb that partners decision.",
                    whyWrong = mapOf(
                        0 to "'Do' partners homework, business and the washing-up. Decisions are made.",
                        2 to "You can have a thought or an idea, but a decision is made.",
                    ),
                ),
                B.q(
                    "ie8-q2", "ie8-lexis", "'Air pollution ___ a serious threat to health.'",
                    listOf("poses", "does", "makes"), setOf(0),
                    "Pose a threat, pose a risk, pose a challenge.",
                    whyWrong = mapOf(
                        1 to "'Do a threat' is not a pairing English makes at all.",
                        2 to "'Make' works with decisions and progress, not with threats.",
                    ),
                ),
                B.q(
                    "ie8-q3", "ie8-lexis", "Lexical Resource rewards",
                    listOf("the rarest words you know", "accurate collocation and natural phrasing", "long words used once each"), setOf(1),
                    "A misused rare word costs more than a simple correct one.",
                    whyWrong = mapOf(
                        0 to "The descriptor says less common words with awareness of style. The awareness is the part being scored.",
                        2 to "A long word dropped in once reads as decoration, and the examiner has seen the same ones many times.",
                    ),
                ),
                B.q(
                    "ie8-q4", "ie8-lexis", "'Many people work from home now' paraphrased formally is",
                    listOf("Remote working has become widespread.", "People are working in their houses.", "Home is where people work now."), setOf(0),
                    "Precise, formal and compressed.",
                    whyWrong = mapOf(
                        1 to "That is a rewording, not an upgrade: 'in their houses' is plainer than the original.",
                        2 to "It changes the emphasis rather than the register, and it reads as a slogan.",
                    ),
                ),
                B.q(
                    "ie8-q5", "ie8-lexis", "Which words belong to the environment topic bank?",
                    listOf("curriculum, tuition fees", "carbon footprint, renewable sources", "redundancy, productivity"), setOf(1),
                    "The others belong to education and work.",
                    whyWrong = mapOf(
                        0 to "Both of those are education. Topic banks are only useful if they are kept apart.",
                        2 to "Redundancy and productivity are the employment bank.",
                    ),
                ),
                B.q(
                    "ie8-q6", "ie8-grammar", "Which sentence is error-free?",
                    listOf("The number of applicants are growing.", "The number of applicants is growing.", "The numbers of applicant is growing."), setOf(1),
                    "'The number of' takes a singular verb.",
                    whyWrong = mapOf(
                        0 to "The subject is 'the number', which is singular. The plural noun next to the verb is what pulls people into 'are'.",
                        2 to "'Applicant' has to be plural after 'of', and 'numbers' is wrong here.",
                    ),
                ),
                B.q(
                    "ie8-q7", "ie8-grammar", "Choose the correct second conditional.",
                    listOf("If transport was cheap, people will drive less.", "If transport were cheaper, fewer people would drive.", "If transport is cheaper, fewer people would drive."), setOf(1),
                    "Past form in the if-clause, would in the main clause.",
                    whyWrong = mapOf(
                        0 to "'Will' belongs to the first conditional. Mixing the halves is the commonest conditional error in Task 2.",
                        2 to "Present 'is' with 'would' is the same mix the other way round.",
                    ),
                ),
                B.q(
                    "ie8-q8", "ie8-grammar", "'Despite' is correctly followed by",
                    listOf("a full clause: Despite it was expensive", "a noun phrase: Despite the expense", "a question"), setOf(1),
                    "Use Although with a clause, Despite with a noun phrase.",
                    whyWrong = mapOf(
                        0 to "That is Although's job. 'Despite the fact that' is the way to get a clause after despite.",
                        2 to "Despite never introduces a question in any register.",
                    ),
                ),
                B.q(
                    "ie8-q9", "ie8-grammar", "Which is an uncountable noun with no plural?",
                    listOf("information", "application", "measure"), setOf(0),
                    "Information, advice and research take no -s.",
                    whyWrong = mapOf(
                        1 to "Applications is perfectly good English: you can count them.",
                        2 to "Measures is common in Task 2, as in 'such measures'.",
                    ),
                ),
                B.q(
                    "ie8-q10", "ie8-grammar", "'Rapid urban growth creates problems' rather than 'Cities grow fast and this creates problems' is an example of",
                    listOf("nominalisation", "the passive voice", "a comma splice"), setOf(0),
                    "The verb becomes a noun, compressing the idea and raising the register.",
                    whyWrong = mapOf(
                        1 to "Nothing here is passive: 'growth creates' is active. What changed is that the verb turned into a noun.",
                        2 to "A comma splice is two clauses joined by a comma, and there is no comma in either version.",
                    ),
                ),
                B.q(
                    "ie8-q11", "ie8-grammar", "'It is cheap, it is popular.' is",
                    listOf("correct", "a comma splice needing a connector or semicolon", "a relative clause"), setOf(1),
                    "Two independent clauses cannot be joined by a comma alone.",
                    whyWrong = mapOf(
                        0 to "Both halves could stand alone as sentences, and that is exactly when a comma is not enough.",
                        2 to "A relative clause needs which, who or that. There is none here.",
                    ),
                ),
                B.q(
                    "ie8-q12", "ie8-final", "In the last three days before the test you should",
                    listOf("start a new grammar book", "consolidate, rehearse and rest", "sit two full tests a day"), setOf(1),
                    "New material unsettles; consolidation and sleep help.",
                    whyWrong = mapOf(
                        0 to "Nothing begun three days out becomes reliable in time, and half-learnt grammar produces new errors.",
                        2 to "Two full tests a day arrives at the exam tired, which costs more than the practice gains.",
                    ),
                ),
                B.q(
                    "ie8-q13", "ie8-final", "You finish Task 2 with five minutes left. The best use of them is",
                    listOf("adding another paragraph", "checking articles, agreement and plural -s", "recopying the essay"), setOf(1),
                    "Those three fixes raise Grammatical Accuracy most per minute.",
                    whyWrong = mapOf(
                        0 to "A fifth paragraph written in five minutes is undeveloped, and it unbalances the structure you already had.",
                        2 to "Recopying cannot be finished in five minutes, and it introduces fresh slips rather than removing them.",
                    ),
                ),
                B.q(
                    "ie8-q14", "ie8-final", "Listening went badly. For the Reading paper you should",
                    listOf("assume the day is lost", "treat it as independent, because papers are marked separately", "ask to restart"), setOf(1),
                    "Carrying a bad paper into the next one costs real marks.",
                    whyWrong = mapOf(
                        0 to "Each paper has its own band, and most requirements are per paper. One weak result does not touch the next.",
                        2 to "There is no restart. The papers run back to back with no break.",
                    ),
                ),
            )),
        ),
    )
}
