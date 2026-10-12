package com.shahaabapps.core.content

/** IELTS module 7: Speaking. Three parts, with recorded practice the phone checks. */
internal object IeltsM7 {
    private val refs = Ielts.commonRefs + B.ref("Speaking test format", Ielts.ORG + "take-a-test/test-format/speaking")

    private val part1 = Topic(
        id = "ie7-part1", title = "Part 1: familiar questions, answered the band 7 way", minutes = 24, references = refs,
        blocks = listOf(
            B.warmup(
                "ie7-part1-w1", "ie7-part1",
                "The examiner asks 'Do you like music?'. You answer 'Yes.' The answer is true and grammatical. What do you think it costs you?",
                listOf("Nothing; the question was closed", "Fluency, because the examiner has to prompt again", "Pronunciation"), 1,
                "Part 1 questions are easy on purpose. The band is in how much you do with them.",
            ),
            B.plain("The interview opens with four to five minutes of questions about you: your home, work or study, and two or three everyday topics. The questions are easy on purpose. The band comes from how you answer, not what you answer."),
            B.diagram(
                caption = "The shape of every Part 1 answer: a direct answer, a reason, and a detail. Two or three sentences, then stop.",
                nodes = listOf(
                    DNode("a", "Answer", 0.18f, 0.5f, w = 0.26f, h = 0.32f, tone = 1, sub = "Yes, I do."),
                    DNode("b", "Reason", 0.5f, 0.5f, w = 0.26f, h = 0.32f, tone = 2, sub = "mainly because…"),
                    DNode("c", "Detail", 0.82f, 0.5f, w = 0.26f, h = 0.32f, sub = "For example, last week…"),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c")),
                aspect = 0.65f,
            ),
            B.table(
                listOf("Answer length", "Band effect"),
                listOf("One word: 'Yes.'", "The examiner must prompt again; Fluency suffers"),
                listOf("Two to three sentences", "Ideal: enough to show range, short enough to keep the pace"),
                listOf("A two-minute speech", "The examiner interrupts; Part 1 is not Part 2"),
            ),
            B.h("The usual topics"),
            B.bullets(
                "Compulsory: where you live, and whether you work or study.",
                "Then two or three from: food, weather, transport, music, reading, photographs, shopping, holidays, the internet, neighbours, sleep, flowers, colours.",
                "Questions come in sets of three or four on one topic, then the topic changes.",
            ),
            B.h("Hear the difference"),
            B.listen("ie7-p1-l1", "Examiner: Do you enjoy cooking? Candidate: Yes. Examiner: Why? Candidate: Because it is nice. Examiner: And do you cook often? Candidate: Sometimes.",
                "Why would this score below band 6 for Fluency?", listOf("The grammar is wrong", "The answers are too short, so the examiner must keep prompting", "The vocabulary is too advanced"), 1),
            B.listen("ie7-p1-l2", "Examiner: Do you enjoy cooking? Candidate: I do, actually, though I only really got into it a couple of years ago. I find it quite relaxing after a long day at a screen, and it's one of the few things where you see a result in half an hour. Last weekend I tried making biryani from scratch, which took far longer than I expected.",
                "Which features make this a band 7 answer?", listOf("It is long and detailed only", "A direct answer, a reason, a specific example, and natural language such as 'got into it'", "It uses difficult grammar throughout"), 1),
            B.h("Say these yourself"),
            B.speak("ie7-p1-s1", "I do, actually, though I only got into it a couple of years ago.", "Answer plus a time detail. 'Got into' is the kind of natural phrasing band 7 rewards."),
            B.speak("ie7-p1-s2", "I find it quite relaxing, mainly because it takes my mind off work.", "Reason sentence. 'Takes my mind off' is an idiomatic phrase used correctly."),
            B.speak("ie7-p1-s3", "Last weekend I tried making biryani from scratch, which took far longer than I expected.", "A specific example with a relative clause. Specifics are what stop answers sounding rehearsed."),
            B.speak("ie7-p1-s4", "Not really, no. I'd much rather eat at home, partly because it works out cheaper.", "A negative answer still needs a reason. 'I'd much rather' shows range."),
            B.h("Fillers that help, and ones that hurt"),
            B.table(
                listOf("Natural, buys you time", "Avoid"),
                listOf("Actually, …", "Er… um… (repeated)"),
                listOf("That's an interesting question.", "Long silence"),
                listOf("I suppose / I'd say", "Repeating the question word for word"),
                listOf("To be honest, …", "'How to say…' in your own language"),
            ),
            B.remember("Hesitating to search for an idea is normal and barely penalised. Hesitating to search for a word is what lowers Fluency. If a word will not come, paraphrase it: 'the thing you use to…' and keep going."),
            B.check("ie7-part1-k1", "ie7-part1", "The examiner asks 'Do you prefer tea or coffee?' The best answer is", listOf("Coffee.", "Coffee, definitely. I need one before I can speak to anyone in the morning, although I switch to tea after lunch.", "Coffee is a drink that many people around the world consume daily and it has a long history."), 1, "Answer, reason, detail; the third is off-topic and sounds memorised."),
            B.check(
                "ie7-part1-k2", "ie7-part1",
                "Talking for a full minute on a Part 1 question",
                listOf("gets you interrupted; Part 1 is not Part 2", "is rewarded as fluency", "is required for band 7"), 0,
                "Part 1 answers are two or three sentences. The examiner has a fixed set of questions to get through and will cut you off.",
                whyWrong = mapOf(
                    1 to "Length is only fluency when the question invites it, and these do not.",
                    2 to "Band 7 here is range inside three sentences, not volume.",
                ),
            ),
            B.check(
                "ie7-part1-k3", "ie7-part1",
                "Which two Part 1 topics always come up?",
                listOf("where you live, and work or study", "food, and the weather", "holidays, and the internet"), 0,
                "Those two open every interview, which makes them the only Part 1 answers worth rehearsing.",
                whyWrong = mapOf(
                    1 to "They are common, but they are drawn from a rotating set rather than fixed.",
                    2 to "Same: possible, not compulsory.",
                ),
            ),
            B.worked("""
                Worked example. Answer, reason, detail, and then stop. Examiner: Do you enjoy cooking? The direct
                answer comes first, because an examiner who cannot hear your position has to ask again: I do,
                actually. Then the reason, which is what turns a yes into an answer: I find it quite relaxing after a
                long day at a screen. Then one specific detail, which is where the vocabulary lives: last weekend I
                tried making bread for the first time, and it was a disaster. Three sentences, about fifteen seconds.
            """),
            B.faded(
                "ie7-part1-f1",
                "A different question, the same three moves. Examiner: Do you often listen to music?",
                listOf(
                    "The direct answer first, so the examiner is not left prompting: I do, most of the day in fact.",
                    "Then the reason, which is the move that turns a yes into an answer: It helps me concentrate when I'm working.",
                    "Then one specific ___, which is where the interesting vocabulary lives: I've been working through a lot of old jazz recordings lately, mostly on the commute.",
                    "Then stop. Two or three sentences is the whole shape; a fourth invites an interruption.",
                ),
                2, listOf("detail", "example"),
                "Answer, reason, detail. The detail is the move candidates drop, and it is the only one of the three that shows any range at all.",
            ),
            B.vocab(
                B.w("ie7-get-into", "get into something", "start to enjoy or take up", "I got into cooking a few years ago.", "Natural Part 1 phrasing."),
                B.w("ie7-take-mind-off", "take your mind off", "distract you from", "It takes my mind off work.", "Idiomatic and correct."),
                B.w("ie7-from-scratch", "from scratch", "from the beginning, nothing ready-made", "I made it from scratch.", "A collocation examiners notice."),
                B.w("ie7-id-rather", "I'd rather", "I would prefer to", "I'd rather eat at home.", "Followed by the bare infinitive."),
                B.w("ie7-work-out", "work out cheaper", "turn out to cost less", "It works out cheaper.", "Phrasal verb, natural register."),
                B.w("ie7-not-really", "not really", "a softened no", "Not really, no.", "More natural than a bare 'no'."),
            ),
            B.glossary("Fluency and Coherence" to "The criterion for speaking at length without effort.", "Filler" to "A short phrase that buys thinking time.", "Paraphrase (speaking)" to "Describing a word you cannot recall."),
            B.recap("Two to three sentences per answer: answer, reason, detail.", "Specific examples stop answers sounding memorised.", "Natural fillers buy time; silence and repeated 'um' cost Fluency."),
        ),
    )

    private val part2 = Topic(
        id = "ie7-part2", title = "Part 2: the long turn, planned in sixty seconds", minutes = 26, references = refs,
        blocks = listOf(
            B.warmup(
                "ie7-part2-w1", "ie7-part2",
                "You have one minute to prepare the long turn and paper to write on. What do you think you should write?",
                listOf("Full sentences to read out", "A word or two per prompt", "Nothing; think instead"), 1,
                "Whatever you write, you will have to speak from it for two minutes.",
            ),
            B.plain("You get a card with a topic and three or four prompts, one minute to prepare with paper and pencil, then one to two minutes to speak alone. Most candidates run out at ninety seconds. The planning method below prevents that."),
            B.diagram(
                caption = "Sixty seconds of planning: one or two words per prompt, then one extra idea for the ending. Never write sentences.",
                nodes = listOf(
                    DNode("a", "what", 0.14f, 0.3f, w = 0.2f, h = 0.22f, tone = 1),
                    DNode("b", "when / where", 0.4f, 0.3f, w = 0.24f, h = 0.22f, tone = 1),
                    DNode("c", "who with", 0.66f, 0.3f, w = 0.2f, h = 0.22f, tone = 1),
                    DNode("d", "why it mattered", 0.9f, 0.3f, w = 0.2f, h = 0.22f, tone = 2),
                    DNode("e", "extra: a feeling, a comparison, a what-if for the last 20 seconds", 0.5f, 0.78f, w = 0.84f, h = 0.24f, kind = NodeKind.NOTE),
                ),
                edges = listOf(DEdge("d", "e")),
                aspect = 0.5f,
            ),
            B.h("A card, and how to fill two minutes"),
            B.p("Describe a skill you would like to learn. You should say: what the skill is; why you want to learn it; how you would go about learning it; and explain how it would change your life."),
            B.table(
                listOf("Time", "What you are saying"),
                listOf("0 to 15 s", "Introduce and name the thing: 'The skill I'd like to talk about is…'"),
                listOf("15 to 50 s", "Prompts one and two, with a specific detail each"),
                listOf("50 to 90 s", "Prompt three, the longest part, with an example"),
                listOf("90 to 120 s", "The final prompt, which is always the 'explain' one, plus your extra idea"),
            ),
            B.remember("The last bullet always begins with 'and explain'. It carries the most speaking time because it invites reasons, and it is where candidates who planned only facts fall silent."),
            B.h("Language that stretches the turn"),
            B.table(
                listOf("Function", "Phrases"),
                listOf("Opening", "The one I'd like to talk about is… / I've chosen…"),
                listOf("Adding detail", "What I should probably mention is… / It's worth saying that…"),
                listOf("Hypothesising", "If I did learn it, I'd probably… / I imagine it would…"),
                listOf("Comparing", "Compared with when I was younger… / Unlike most of my friends…"),
                listOf("Ending", "So that's the skill I'd choose, mainly because… / All in all, …"),
            ),
            B.speak("ie7-p2-s1", "The skill I'd like to talk about is sailing, which is something I've wanted to learn since I was a teenager.", "Opening move: name it and attach a time reference immediately."),
            B.speak("ie7-p2-s2", "What I should probably mention is that I grew up a long way from the sea, so the opportunity never really came up.", "A stretching phrase plus a reason. This is how you fill thirty seconds without inventing facts."),
            B.speak("ie7-p2-s3", "If I did take it up, I'd probably start with a weekend course and then join a club, because learning alone would be far harder.", "Hypothetical language for the 'how' prompt. The second conditional shows grammatical range."),
            B.speak("ie7-p2-s4", "All in all, I'd choose sailing because it would get me outdoors and teach me something completely unlike my day job.", "A closing move that answers the 'explain' prompt and signals you have finished."),
            B.h("When you dry up early"),
            B.bullets(
                "Add a contrast: 'Having said that, there's a downside too…'",
                "Add a time shift: 'Ten years ago I'd have said something different…'",
                "Add a person: 'My brother actually did this, and he told me…'",
                "Add a hypothetical: 'If I had more free time, I'd…'",
            ),
            B.listen("ie7-p2-l1", "Candidate: The skill I would like to learn is swimming. I want to learn it because it is good exercise. I would learn it at the local pool. It would change my life because I would be healthier. That's all.",
                "What is the main weakness of this long turn?", listOf("The grammar is wrong", "Each prompt gets one short sentence with no detail or example, so it ends far too early", "The topic is unsuitable"), 1),
            B.check("ie7-part2-k1", "ie7-part2", "In the one minute of preparation you should write", listOf("full sentences to read aloud", "one or two words per prompt plus one extra idea", "nothing, to sound natural"), 1, "Reading aloud destroys Fluency; notes keep you going to two minutes."),
            B.check(
                "ie7-part2-k2", "ie7-part2",
                "The last bullet on the card always begins with 'and explain'. It should take",
                listOf("the most speaking time", "the least, since it is last", "about the same as the others"), 0,
                "It invites reasons rather than facts, so it stretches. It is also where candidates who planned only facts fall silent at ninety seconds.",
                whyWrong = mapOf(
                    1 to "It is last because it is the hardest, not because it matters least.",
                    2 to "The first bullets are naming; this one is reasoning, and reasoning fills time.",
                ),
            ),
            B.check(
                "ie7-part2-k3", "ie7-part2",
                "You dry up after ninety seconds. The usual cause is",
                listOf("planning facts and no reasons", "speaking too slowly", "an unfamiliar topic"), 0,
                "Facts run out. Reasons, comparisons and hypotheticals do not, which is why the plan should carry one extra idea for the ending.",
                whyWrong = mapOf(
                    1 to "Speaking slowly fills time but costs Fluency, and it is a symptom rather than the cause.",
                    2 to "Every card can be answered from your own life; the cards are chosen to be universal.",
                ),
            ),
            B.worked("""
                Worked example. Sixty seconds of planning for: Describe a skill you would like to learn. Not
                sentences. One or two words against each prompt: what, sailing. Why, grandfather's boat. How, club
                on the coast, two summers. Explain, confidence on water, something to do with the children. Then one
                extra idea held back for the ending: how learning anything slowly as an adult feels different. That
                last note is what carries you from ninety seconds to two minutes.
            """),
            B.faded(
                "ie7-part2-f1",
                "A different card, the same sixty seconds. Describe a journey you remember well. You should say: where you went; who you went with; what happened; and explain why you remember it.",
                listOf(
                    "Not sentences. One or two words per prompt: where, Karachi to Lahore. Who, my brother.",
                    "What happened: train delayed, overnight, strangers sharing food.",
                    "The last prompt begins with and explain, so it carries the most time: first journey alone, what it changed.",
                    "Then one ___ idea, held back for the ending, which is what carries you from ninety seconds to two minutes: how differently I'd see the same trip now.",
                ),
                3, listOf("extra", "additional", "spare"),
                "The plan has five notes, not four. The spare one exists because the turn is longer than the prompts are.",
            ),
            B.vocab(
                B.w("ie7-take-up", "take something up", "start a hobby or activity", "I'd like to take up sailing.", "Phrasal verb for Part 2."),
                B.w("ie7-come-up", "come up", "arise, present itself", "The opportunity never came up.", "Natural and idiomatic."),
                B.w("ie7-having-said-that", "having said that", "however", "Having said that, it is expensive.", "A discourse marker that buys time."),
                B.w("ie7-all-in-all", "all in all", "overall", "All in all, I'd choose sailing.", "Signals your closing sentence."),
                B.w("ie7-unlike", "unlike", "in contrast to", "Unlike most of my friends…", "A comparison opener."),
                B.w("ie7-day-job", "day job", "your main paid work", "Completely unlike my day job.", "Informal but correct register."),
            ),
            B.glossary("Long turn" to "The one-to-two-minute speech in Part 2.", "Cue card" to "The card with the topic and prompts.", "Second conditional" to "If I did X, I would Y: for imagined situations."),
            B.recap("One minute of notes: a word or two per prompt plus one extra idea.", "The final 'explain' prompt takes the most time; plan reasons, not just facts.", "Stretch with contrast, time shift, another person, or a hypothetical."),
        ),
    )

    private val part3 = Topic(
        id = "ie7-part3", title = "Part 3: the discussion that decides your band", minutes = 26, references = refs,
        blocks = listOf(
            B.warmup(
                "ie7-part3-w1", "ie7-part3",
                "In Part 3 the examiner disagrees with something you said. What do you think that means?",
                listOf("You got it wrong", "It is a prompt to reason, and both defending and conceding score", "Time is running out"), 1,
                "Nothing in Part 3 is scored on being right.",
            ),
            B.plain("Part 3 lasts four to five minutes and moves from your own life to society in general. The questions are abstract and the examiner may push back. This is where a 6 and an 8 separate, because it demands reasoning, not recall."),
            B.table(
                listOf("Question type", "Example", "What it wants"),
                listOf("Opinion", "Should museums be free?", "A position plus reasons"),
                listOf("Comparison", "How has family life changed in the last fifty years?", "Then versus now, with examples"),
                listOf("Speculation", "What will cities look like in fifty years?", "Future forms and hedging"),
                listOf("Cause", "Why do you think people read less than before?", "Reasons, ranked"),
                listOf("Evaluation", "Is it the government's responsibility or the individual's?", "Both sides, then a judgement"),
            ),
            B.diagram(
                caption = "The answer shape for any abstract question: position, reason, example, and a qualification that shows you can see the other side.",
                nodes = listOf(
                    DNode("a", "Position", 0.14f, 0.5f, w = 0.22f, h = 0.32f, tone = 1),
                    DNode("b", "Reason", 0.38f, 0.5f, w = 0.2f, h = 0.32f),
                    DNode("c", "Example", 0.62f, 0.5f, w = 0.2f, h = 0.32f, tone = 2),
                    DNode("d", "Qualification", 0.87f, 0.5f, w = 0.24f, h = 0.32f, sub = "Although, that said…"),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "d")),
                aspect = 0.65f,
            ),
            B.h("Hedging: the language of band 7 and 8"),
            B.p("Abstract questions have no right answer, so confident absolutes sound naive. Hedging shows judgement and gives you thinking time at the same time."),
            B.table(
                listOf("Instead of", "Say"),
                listOf("People always prefer…", "People tend to prefer…"),
                listOf("This is the reason.", "This is probably the main reason."),
                listOf("It will happen.", "It's likely to happen / I'd imagine it will."),
                listOf("Everyone thinks…", "There's a widespread view that…"),
                listOf("It is bad.", "It can be problematic, at least in some contexts."),
            ),
            B.speak("ie7-p3-s1", "People tend to assume that technology isolates us, but I'd argue the opposite is often true.", "Hedged generalisation plus a counter-claim. 'Tend to' and 'I'd argue' are both band 7 markers."),
            B.speak("ie7-p3-s2", "That's probably the main reason, although there are other factors worth considering.", "Hedging plus a qualification, which also buys you a second to think."),
            B.speak("ie7-p3-s3", "It's difficult to say with any certainty, but I'd imagine cities will become far denser than they are now.", "Speculation language for future questions. Note 'I'd imagine' rather than 'I think' twice."),
            B.speak("ie7-p3-s4", "I'd say it's a shared responsibility, though governments are better placed to act quickly than individuals are.", "Evaluation answer: a judgement that still acknowledges both sides."),
            B.h("When the examiner disagrees"),
            B.p("The examiner may say 'But don't you think…?' This is not a trap and you have not made a mistake. It tests whether you can defend or adjust a view. Both are fine; silence is not."),
            B.table(
                listOf("Move", "Phrase"),
                listOf("Defend", "I see what you mean, but I still think… because…"),
                listOf("Concede partly", "That's a fair point, and I'd probably revise my view to…"),
                listOf("Distinguish", "It depends on the context: in a large city…, whereas in a village…"),
            ),
            B.listen("ie7-p3-l1", "Examiner: Do you think young people today have more opportunities than their parents did? Candidate: In some respects, yes, particularly when it comes to education and travel. That said, my parents' generation could buy a house on a single salary, which is almost unthinkable now. So I'd say the opportunities have shifted rather than increased.",
                "Which features make this a strong Part 3 answer?", listOf("It is long", "It qualifies, compares two generations with a concrete example, and reaches a nuanced judgement", "It agrees with the examiner"), 1),
            B.h("Things that cap Part 3 at band 6"),
            B.mistakes(
                "Answering about yourself when the question asks about society; Part 3 is general.",
                "One-sentence answers; Part 3 expects development.",
                "Memorised chunks that do not fit the question; examiners hear them immediately.",
                "Absolutes with no hedging: everyone, always, never.",
                "Going silent when challenged instead of defending or conceding.",
            ),
            B.check("ie7-part3-k1", "ie7-part3", "The examiner asks 'Why do people in your country work such long hours?' A band 7 answer would", listOf("describe your own working day", "give a societal reason, an example, and a hedged qualification", "say 'because they need money' and stop"), 1, "Part 3 asks about society and rewards developed, hedged reasoning."),
            B.check(
                "ie7-part3-k2", "ie7-part3",
                "The examiner disagrees with something you said. That means",
                listOf("it is a prompt to reason; defending or conceding both score", "you got it wrong", "time is running out"), 0,
                "Nothing in Part 3 is scored on being right. The pushback exists to see whether you can argue, and changing your mind with a reason scores as well as holding it.",
                whyWrong = mapOf(
                    1 to "There is no right answer to an abstract question, so there is nothing to get wrong.",
                    2 to "The examiner is working to a clock but the challenge is not a signal about it.",
                ),
            ),
            B.check(
                "ie7-part3-k3", "ie7-part3",
                "'People always prefer…' is weaker than 'People tend to prefer…' because",
                listOf("the absolute sounds naive and the hedge shows judgement", "tend is a rarer word", "always is grammatically wrong"), 0,
                "Hedging is the language of band 7 and 8 here, and it buys thinking time while you build the rest of the answer.",
                whyWrong = mapOf(
                    1 to "Lexical Resource is not counting rarity; both words are common.",
                    2 to "Both sentences are correct. The difference is judgement, not grammar.",
                ),
            ),
            B.worked("""
                Worked example. The four moves of an abstract answer. Question: Should museums be free? Position
                first: On balance I'd say they should be, at least the national ones. Reason: Charging turns a public
                collection into a day out for people who can afford it. Example, which may be invented: When entry
                charges were dropped in Britain in the early 2000s, visitor numbers roughly doubled. Then the
                qualification that shows you can see the other side: That said, free entry only works if the funding
                comes from somewhere else, which is not guaranteed.
            """),
            B.faded(
                "ie7-part3-f1",
                "A different abstract question, the same four moves. Examiner: Do you think people will read fewer books in the future?",
                listOf(
                    "Position first, hedged rather than absolute: I'd imagine they'll read fewer printed ones, though not necessarily less overall.",
                    "Reason: Attention is being competed for by things that are designed to be easier than a book.",
                    "Example, which may be invented: Most people I know who stopped reading didn't replace it with anything in particular; it just went.",
                    "Then the ___ that shows you can see the other side: That said, audiobooks have brought a lot of people back, so the format may matter more than the habit.",
                ),
                3, listOf("qualification", "concession", "counterpoint"),
                "Position, reason, example, qualification. The last move is what separates a 6 from an 8, because it shows the answer was reasoned rather than recalled.",
            ),
            B.vocab(
                B.w("ie7-tend-to", "tend to", "usually do", "People tend to prefer convenience.", "Essential hedging verb."),
                B.w("ie7-id-argue", "I'd argue that", "I would claim that", "I'd argue the opposite is true.", "Confident but not absolute."),
                B.w("ie7-that-said", "that said", "however", "That said, there are exceptions.", "Natural concession marker."),
                B.w("ie7-better-placed", "better placed to", "in a better position to", "Governments are better placed to act.", "Band 8 collocation."),
                B.w("ie7-unthinkable", "unthinkable", "impossible to imagine", "Almost unthinkable now.", "Strong, precise adjective."),
                B.w("ie7-in-some-respects", "in some respects", "in certain ways", "In some respects, yes.", "Opens a qualified answer."),
            ),
            B.glossary("Hedging" to "Softening a claim: tend to, likely, probably.", "Qualification" to "A clause acknowledging the other side.", "Nuanced" to "Recognising complexity rather than choosing a simple side."),
            B.recap("Position, reason, example, qualification for every abstract question.", "Hedge: tend to, probably, it's likely, in some respects.", "Answer about society, not yourself, and never go silent when challenged."),
        ),
    )

    val module = Module(
        id = "ie-m7", number = 7, title = "Speaking", summary = "Part 1 answers, the Part 2 long turn, Part 3 discussion, hedging and recorded practice.",
        examWeight = "25 percent", componentId = "speaking", topics = listOf(part1, part2, part3), practiceMinutes = 600,
        quiz = Quiz(
            id = "ie-m7-test", title = "Module 7 test", passPercent = 70,
            questions = B.spread(listOf(
                B.q(
                    "ie7-q1", "ie7-part1", "A good Part 1 answer is usually",
                    listOf("one word", "two to three sentences", "two minutes long"), setOf(1),
                    "Enough to show range without turning into a speech.",
                    whyWrong = mapOf(
                        0 to "A one-word answer gives the examiner nothing to score and forces another prompt.",
                        2 to "Two minutes is Part 2. Running that long in Part 1 means the examiner has to cut you off.",
                    ),
                ),
                B.q(
                    "ie7-q2", "ie7-part1", "Listening: why does this answer score poorly?",
                    listOf("The grammar is wrong", "It is a single word with no reason or detail", "It is off topic"), setOf(1),
                    "Minimal answers force the examiner to prompt and lower Fluency.",
                    listen = "Examiner: Do you like music? Candidate: Yes.",
                    whyWrong = mapOf(
                        0 to "'Yes' has nothing wrong with it grammatically. That is the problem: there is nothing in it at all.",
                        2 to "It answers the question asked. It just answers nothing else.",
                    ),
                ),
                B.q(
                    "ie7-q3", "ie7-part1", "Hesitating because you cannot find a word",
                    listOf("affects Fluency more than hesitating for an idea", "is never penalised", "improves naturalness"), setOf(0),
                    "Searching for language is the kind of hesitation that lowers the band.",
                    whyWrong = mapOf(
                        1 to "The descriptor names hesitation explicitly. It distinguishes the two kinds rather than ignoring both.",
                        2 to "Pausing to think is natural; groping for a word is not, and the examiner can hear the difference.",
                    ),
                ),
                B.q(
                    "ie7-q4", "ie7-part1", "Which is a natural way to buy thinking time?",
                    listOf("Er, er, er…", "That's an interesting question, actually.", "Repeating the question word for word"), setOf(1),
                    "A short natural phrase keeps the flow; repeated fillers do not.",
                    whyWrong = mapOf(
                        0 to "Filled pauses are exactly what the Fluency descriptor is about. They buy time and spend band.",
                        2 to "Echoing the question is audibly a stalling tactic, and it uses none of your own language.",
                    ),
                ),
                B.q(
                    "ie7-q5", "ie7-part2", "How long do you have to prepare in Part 2?",
                    listOf("No preparation", "One minute with paper and pencil", "Five minutes"), setOf(1),
                    "One minute, and you may make notes.",
                    whyWrong = mapOf(
                        0 to "There is a preparation minute, and candidates who do not use it are the ones who run out at ninety seconds.",
                        2 to "Five minutes would be longer than the whole long turn. The card gives one.",
                    ),
                ),
                B.q(
                    "ie7-q6", "ie7-part2", "You should speak for",
                    listOf("30 seconds", "one to two minutes", "as long as you like"), setOf(1),
                    "The examiner stops you at two minutes.",
                    whyWrong = mapOf(
                        0 to "Half a minute is well short, and stopping early is the commonest Part 2 failure.",
                        2 to "The examiner cuts in at two minutes. The skill is filling them, not exceeding them.",
                    ),
                ),
                B.q(
                    "ie7-q7", "ie7-part2", "In your preparation minute you should write",
                    listOf("full sentences", "a word or two per prompt plus one spare idea", "the examiner's questions"), setOf(1),
                    "Notes keep you going; sentences get read aloud and sound flat.",
                    whyWrong = mapOf(
                        0 to "A minute is not enough to write sentences, and reading them out loses the naturalness the criteria reward.",
                        2 to "The prompts are already printed on the card. Copying them uses the minute and adds nothing.",
                    ),
                ),
                B.q(
                    "ie7-q8", "ie7-part2", "The last bullet on the card usually begins with 'and explain'. It",
                    listOf("can be skipped", "carries the most speaking time because it invites reasons", "must be answered first"), setOf(1),
                    "It is where candidates who planned only facts run out.",
                    whyWrong = mapOf(
                        0 to "It is a prompt like the others, and it is the one that fills the second minute.",
                        2 to "The prompts work as an order to follow. Starting with the explanation leaves nothing to explain.",
                    ),
                ),
                B.q(
                    "ie7-q9", "ie7-part2", "Listening: what should this candidate add?",
                    listOf("More difficult grammar", "Detail and an example for each prompt", "A faster pace"), setOf(1),
                    "Each prompt needs development, not just a sentence.",
                    whyWrong = mapOf(
                        0 to "The grammar here is not the gap. The answer is twenty seconds long because nothing is developed.",
                        2 to "Speaking faster would end it sooner. The problem is that there is not enough being said.",
                    ),
                ),
                B.q(
                    "ie7-q10", "ie7-part3", "Part 3 questions are mostly about",
                    listOf("your own daily life", "society and abstract issues", "the Part 2 card"), setOf(1),
                    "Part 3 moves from the personal to the general.",
                    whyWrong = mapOf(
                        0 to "Your own life is Part 1. Part 3 deliberately widens out from it.",
                        2 to "The card's topic is the starting point, but the questions leave it behind quickly.",
                    ),
                ),
                B.q(
                    "ie7-q11", "ie7-part3", "'People tend to prefer convenience' is better than 'People always prefer convenience' because",
                    listOf("it is shorter", "hedging shows judgement and avoids a naive absolute", "'always' is ungrammatical"), setOf(1),
                    "Hedging is a marker of band 7 and above.",
                    whyWrong = mapOf(
                        0 to "It is barely shorter, and length is not what is being scored.",
                        2 to "'Always' is fine English. It is the claim it makes that is too strong.",
                    ),
                ),
                B.q(
                    "ie7-q12", "ie7-part3", "The examiner challenges your view. You should",
                    listOf("go silent", "defend it or partly concede, with a reason", "change the subject"), setOf(1),
                    "The challenge tests reasoning; both defending and conceding score well.",
                    whyWrong = mapOf(
                        0 to "Silence is the one response that scores nothing. The challenge is an invitation to speak more, not less.",
                        2 to "Moving off the question reads as avoidance, and Part 3 is scored on handling exactly this.",
                    ),
                ),
                B.q(
                    "ie7-q13", "ie7-part3", "Listening: which feature makes this answer strong?",
                    listOf("It is long", "It compares two periods with an example and reaches a qualified judgement", "It agrees with the examiner"), setOf(1),
                    "Comparison, example and nuance are what Part 3 rewards.",
                    listen = "Candidate: In some respects yes, particularly for education. That said, my parents could buy a house on one salary, which is unthinkable now. So the opportunities have shifted rather than increased.",
                    whyWrong = mapOf(
                        0 to "It is three sentences. What makes it work is the shape of them, not the length.",
                        2 to "It does not simply agree: 'that said' turns it, and the conclusion is a qualification rather than a yes.",
                    ),
                ),
                B.q(
                    "ie7-q14", "ie7-part1", "A memorised answer that does not quite fit the question",
                    listOf("usually scores well", "is recognised by examiners and lowers the band", "is required for Part 2"), setOf(1),
                    "Examiners hear memorised chunks immediately; specifics sound real.",
                    whyWrong = mapOf(
                        0 to "A rehearsed passage changes rhythm and register the moment it starts, and it answers a question nobody asked.",
                        2 to "Part 2 is planned in the minute you are given, from the card in front of you.",
                    ),
                ),
            )),
        ),
    )
}
