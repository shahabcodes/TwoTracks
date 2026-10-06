package com.shahaabapps.core.content

/** IELTS module 6: Writing Task 2, the essay. Both versions, two thirds of the Writing mark. */
internal object IeltsM6 {
    private val refs = Ielts.commonRefs + B.ref("Writing test format", Ielts.ORG + "take-a-test/test-format/writing")

    private val questions = Topic(
        id = "ie6-questions", title = "Five question types, and answering the one you were asked", minutes = 26, references = refs,
        blocks = listOf(
            B.plain("Task 2 is the same in Academic and General: an essay of at least 250 words in 40 minutes, worth twice Task 1. More band 6 essays fail on the question than on the English. Read the task twice, decide its type, and answer every part of it."),
            B.table(
                listOf("Type", "The prompt ends with", "What you must do"),
                listOf("Opinion (agree or disagree)", "To what extent do you agree or disagree?", "Take one clear side and hold it from introduction to conclusion"),
                listOf("Discussion", "Discuss both views and give your own opinion.", "One paragraph per view, plus your opinion stated clearly"),
                listOf("Problem and solution", "What problems does this cause and what measures could be taken?", "One paragraph of problems, one of solutions, linked to each other"),
                listOf("Advantages and disadvantages", "Do the advantages outweigh the disadvantages?", "Both sides, then a verdict on which is greater"),
                listOf("Two-part question", "Two direct questions", "One paragraph per question, both answered"),
            ),
            B.diagram(
                caption = "The trap in each type. Most lost marks come from the right column, not from grammar.",
                nodes = listOf(
                    DNode("a", "Discussion", 0.2f, 0.25f, w = 0.3f, h = 0.22f, tone = 1),
                    DNode("b", "giving only your own view", 0.68f, 0.25f, w = 0.46f, h = 0.22f, tone = 2),
                    DNode("c", "Advantages/disadvantages", 0.2f, 0.62f, w = 0.3f, h = 0.22f, tone = 1),
                    DNode("d", "listing both but never saying which wins", 0.68f, 0.62f, w = 0.46f, h = 0.22f, tone = 2),
                    DNode("e", "Two-part", 0.2f, 0.92f, w = 0.3f, h = 0.16f, tone = 1),
                    DNode("f", "answering only the first question", 0.68f, 0.92f, w = 0.46f, h = 0.16f, tone = 2),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("c", "d"), DEdge("e", "f")),
                aspect = 0.58f,
            ),
            B.h("Reading the task properly"),
            B.steps("Four minutes of planning that save the essay",
                "Underline the topic and the instruction separately." to "Topic: what it is about. Instruction: what you must do with it.",
                "Decide the type from the instruction wording." to "Agree/disagree, discuss both, problems and solutions, outweigh, two questions.",
                "Write two main ideas, each with one example." to "Two developed ideas beat four undeveloped ones.",
                "Decide your position before you write a word." to "Changing sides mid-essay destroys Coherence.",
            ),
            B.p("Task: Some people believe that children should begin formal schooling at the age of four, while others argue that they should start at seven. Discuss both views and give your own opinion."),
            B.check("ie6-questions-k1", "ie6-questions", "For that task, an essay that argues only that seven is better will lose most marks on", listOf("Grammatical Range", "Task Response, because both views were required", "Lexical Resource"), 1, "A discussion essay must present both views, then your opinion."),
            B.check("ie6-questions-k2", "ie6-questions", "A task asks 'Do the advantages outweigh the disadvantages?' Your conclusion must", listOf("list both again", "state which side is greater", "avoid taking a position"), 1, "Outweigh asks for a verdict, not a summary."),
            B.h("Position: what 'to what extent' allows"),
            B.table(
                listOf("Position", "Phrase", "Risk"),
                listOf("Full agreement", "I completely agree that…", "Safe and easy to structure"),
                listOf("Full disagreement", "I firmly believe that… is misguided", "Safe"),
                listOf("Partial", "I agree that… although only in certain circumstances", "Strong if handled clearly, muddled if not"),
                listOf("No position", "There are arguments on both sides", "Caps Task Response at band 5"),
            ),
            B.remember("Band 7 requires a clear position throughout. State it in the introduction, support it in the body, restate it in the conclusion. A partial position is fine as long as it is stated and consistent."),
            B.vocab(
                B.w("ie6-outweigh", "outweigh", "be greater or more important than", "The benefits outweigh the drawbacks.", "Signals a verdict question."),
                B.w("ie6-to-what-extent", "to what extent", "how far", "To what extent do you agree?", "Allows a partial position."),
                B.w("ie6-measure", "measure", "an action taken to solve something", "Measures could be taken by government.", "Solution paragraphs."),
                B.w("ie6-drawback", "drawback", "disadvantage", "The main drawback is cost.", "Alternative to disadvantage."),
                B.w("ie6-proponent", "proponents of", "people who support", "Proponents of early schooling argue…", "Introduces a view that is not yours."),
                B.w("ie6-critic", "critics argue", "people against say", "Critics argue that it is harmful.", "Introduces the other side."),
            ),
            B.glossary("Task Response" to "The criterion rewarding a full, relevant answer with a clear position.", "Prompt" to "The statement and instruction you are given.", "Position" to "Your stated view on the question."),
            B.recap("Five types; the instruction wording tells you which.", "Answer every part: both views, both questions, or a verdict.", "State one clear position and hold it from introduction to conclusion."),
        ),
    )

    private val structure = Topic(
        id = "ie6-structure", title = "Four paragraphs, and what goes in each sentence", minutes = 28, references = refs,
        blocks = listOf(
            B.plain("A band 7 essay has an obvious skeleton. Examiners read hundreds of scripts; clarity is rewarded, not cleverness. Four paragraphs, each with a defined job, each body paragraph built the same way."),
            B.diagram(
                caption = "The skeleton. Each body paragraph is one idea, explained, exemplified and tied back to the question.",
                nodes = listOf(
                    DNode("a", "Introduction", 0.5f, 0.1f, w = 0.6f, h = 0.14f, tone = 1, sub = "paraphrase + position + outline"),
                    DNode("b", "Body 1", 0.5f, 0.36f, w = 0.6f, h = 0.16f, tone = 2, sub = "topic · explain · example · link"),
                    DNode("c", "Body 2", 0.5f, 0.62f, w = 0.6f, h = 0.16f, tone = 2, sub = "topic · explain · example · link"),
                    DNode("d", "Conclusion", 0.5f, 0.88f, w = 0.6f, h = 0.14f, tone = 1, sub = "restate position, no new ideas"),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "d")),
                aspect = 0.66f,
            ),
            B.h("The introduction: three sentences, no more"),
            B.table(
                listOf("Sentence", "Job", "Example"),
                listOf("1", "Paraphrase the statement", "It is sometimes argued that children ought to enter formal education as early as four."),
                listOf("2", "State your position", "While early schooling has clear benefits, I believe a later start at around six or seven is preferable for most children."),
                listOf("3", "Outline the essay", "This essay will consider the case for both ages before explaining my own view."),
            ),
            B.remember("Never begin with 'Nowadays' or 'In this modern era'. They say nothing, waste words and are the most recognisable band 5 opening in the test."),
            B.h("Body paragraphs: topic, explain, example, link"),
            B.table(
                listOf("Move", "Function", "Language"),
                listOf("Topic sentence", "One idea, stated plainly", "The strongest argument for an early start is that…"),
                listOf("Explanation", "Why that is true: two sentences of reasoning", "This is because young children acquire language and social habits rapidly, which means that…"),
                listOf("Example", "Concrete, invented if necessary", "In Finland, for instance, children begin at seven and consistently outperform…"),
                listOf("Link", "Tie back to the question", "This suggests that the age of entry matters less than the quality of teaching."),
            ),
            B.p("Examples may be invented. 'A study in Canada found that…' is acceptable; examiners mark the English, not your citations. What they penalise is a paragraph with no example at all, because it reads as unsupported assertion."),
            B.h("The conclusion: two sentences"),
            B.table(
                listOf("Sentence", "Job", "Example"),
                listOf("1", "Restate the position in new words", "In conclusion, although starting school at four can accelerate early literacy, the evidence suggests that a later start better supports long-term development."),
                listOf("2", "A recommendation or implication", "Governments would therefore do well to invest in high-quality preschool play rather than formal lessons."),
            ),
            B.mistakes(
                "A new idea in the conclusion; it belongs in a body paragraph or nowhere.",
                "A one-sentence conclusion, which reads as running out of time.",
                "Four short body paragraphs instead of two developed ones.",
                "Writing 400 words and leaving Task 1 unfinished; 260 to 290 is the sweet spot.",
            ),
            B.h("Cohesion: linking without stuffing"),
            B.table(
                listOf("Function", "Use", "Avoid"),
                listOf("Adding", "In addition, Furthermore, Moreover", "And also besides furthermore all in one sentence"),
                listOf("Contrasting", "However, By contrast, Nevertheless", "But at the start of every sentence"),
                listOf("Causing", "As a result, Consequently, This means that", "So"),
                listOf("Exemplifying", "For instance, A clear example of this is", "Example: like this"),
                listOf("Concluding", "In conclusion, To sum up", "At last, Finally (when it is not a list)"),
            ),
            B.p("Band 7 uses linking devices naturally, which includes referencing: this, these, such, the latter, the former. Replacing a repeated noun with 'this approach' or 'such measures' is cohesion of the kind examiners reward most."),
            B.gap("ie6-s-g1", "Referencing instead of repetition.", "Governments could subsidise childcare. ___ measures would help low-income families most.", "Such", "such", "These", "these"),
            B.gap("ie6-s-g2", "Contrast linker at the start of a sentence.", "___, there are significant drawbacks to this approach.", "However", "however", "Nevertheless", "nevertheless"),
            B.check("ie6-structure-k1", "ie6-structure", "Which is the best topic sentence?", listOf("Firstly, I will talk about education.", "The strongest argument for a later start is that young children need unstructured play.", "Education is very important nowadays."), 1, "A topic sentence states the paragraph's single idea as a claim."),
            B.vocab(
                B.w("ie6-assertion", "assertion", "a claim made without evidence", "A paragraph of assertion scores poorly.", "Add an example to fix it."),
                B.w("ie6-acquire", "acquire", "gain, learn", "Children acquire language rapidly.", "Formal alternative to get."),
                B.w("ie6-the-latter", "the former / the latter", "the first / the second mentioned", "The latter approach is cheaper.", "A cohesion device."),
                B.w("ie6-subsidise", "subsidise", "pay part of the cost of", "Governments could subsidise childcare.", "Common in solution paragraphs."),
                B.w("ie6-long-term", "long-term", "over an extended period", "Long-term development matters more.", "Hyphenated before a noun."),
                B.w("ie6-accelerate", "accelerate", "speed up", "Early schooling accelerates literacy.", "Formal and precise."),
            ),
            B.glossary("Topic sentence" to "The first sentence of a body paragraph, stating its idea.", "Referencing" to "Using this, these, such to avoid repeating a noun.", "Coherence" to "Whether the reader can follow the argument."),
            B.recap("Introduction of three sentences: paraphrase, position, outline.", "Body paragraphs: topic, explain, example, link; two developed ideas.", "Conclusion of two sentences with no new ideas; 260 to 290 words total."),
        ),
    )

    private val practice = Topic(
        id = "ie6-practice", title = "Write two essays and have them marked", minutes = 26, references = refs,
        blocks = listOf(
            B.plain("Two essays, one opinion and one problem and solution. The marker checks the content points plus the essay form: four paragraphs, linking words and a clear position. Forty minutes each, including four minutes of planning."),
            B.diagram(
                caption = "The marking loop. The rewrite is where the band moves; the model is read last, not first.",
                nodes = listOf(
                    DNode("a", "plan 4 min", 0.12f, 0.5f, w = 0.2f, h = 0.3f, tone = 1),
                    DNode("b", "write 32 min", 0.35f, 0.5f, w = 0.22f, h = 0.3f),
                    DNode("c", "check 4 min", 0.58f, 0.5f, w = 0.2f, h = 0.3f, tone = 2),
                    DNode("d", "mark", 0.78f, 0.5f, w = 0.16f, h = 0.3f),
                    DNode("e", "rewrite", 0.94f, 0.5f, w = 0.14f, h = 0.3f, tone = 1),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "d"), DEdge("d", "e")),
                aspect = 0.36f,
            ),
            B.h("Task A: opinion"),
            B.p("Some people think that governments should spend money on public transport rather than on building new roads. To what extent do you agree or disagree? Give reasons for your answer and include any relevant examples from your own knowledge or experience. Write at least 250 words."),
            B.marked("ie6-p-w1", "Write the opinion essay for Task A. Four paragraphs, a clear position in the introduction and conclusion, two developed ideas with examples. Press return twice between paragraphs.",
                """
                It is frequently argued that public funds would be better directed towards buses and railways than towards the construction of additional roads. I strongly agree with this view, both because new roads tend to generate the very congestion they are meant to relieve, and because reliable public transport serves a far wider section of society.

                The first and most practical reason concerns induced demand. When a city widens a motorway, journeys that drivers previously avoided suddenly become attractive, and within a few years the new capacity is full. For example, the ring road around my own city was expanded in 2015, yet average commuting times were longer by 2020 than they had been before the work began. Investment in rail, by contrast, removes vehicles from the network permanently rather than inviting more of them onto it.

                A second consideration is fairness. Roads benefit those who can afford a car, whereas trams, buses and trains are used by students, older people and low-income workers who have no alternative. In addition, the environmental cost of private vehicles falls on everyone, including those who do not drive. Consequently, spending on public transport delivers a social return that road building cannot match.

                Opponents of this position point out that rural communities depend on roads and would gain little from urban rail schemes. This is a reasonable objection, although it argues for maintaining existing roads rather than for building new ones.

                In conclusion, while road maintenance remains necessary, I believe governments should prioritise public transport, since it eases congestion more durably and distributes the benefit of public spending far more evenly.
                """,
                "A clear position stated" to listOf("i strongly agree", "i agree", "i disagree", "i believe", "in my opinion"),
                "First developed idea with an example" to listOf("for example", "for instance", "a clear example"),
                "Second developed idea" to listOf("a second", "secondly", "in addition", "furthermore", "another"),
                "The other side acknowledged" to listOf("opponents", "critics", "however", "although", "some argue", "objection"),
                "A conclusion restating the position" to listOf("in conclusion", "to sum up", "to conclude"),
                minWords = 250, form = WritingForm.ESSAY),
            B.h("Task B: problem and solution"),
            B.p("In many countries, people are working longer hours than in the past and report higher levels of stress. What problems does this cause, and what measures could be taken to address them? Write at least 250 words."),
            B.marked("ie6-p-w2", "Write the problem and solution essay for Task B. One paragraph of problems, one of measures, with the measures answering the problems you raised.",
                """
                In a growing number of economies, employees are spending more hours at work than their parents did, and surveys consistently report rising levels of stress. This essay will outline the principal consequences of this trend and suggest two measures that could realistically address them.

                The most serious problem is the damage to physical and mental health. Prolonged working hours are strongly associated with sleep deprivation, which in turn raises the risk of heart disease and depression. A second, less visible consequence is the erosion of family life. When parents routinely return home after their children are asleep, relationships weaken and the burden of care falls unequally on one partner. For instance, a colleague of mine regularly works until nine in the evening and admits that he sees his daughter properly only at weekends.

                Several measures could ease the situation. Firstly, governments could legislate for a genuine right to disconnect, making it unlawful for employers to require responses to messages outside contracted hours. Such a law has already been introduced in France and has reduced out-of-hours contact considerably. In addition, companies themselves could be required to report average working hours publicly, since reputational pressure often changes corporate behaviour faster than regulation does.

                However, neither measure will succeed unless workplace culture changes, because employees who fear for their jobs will continue to work late regardless of what the law permits. Training managers to judge staff on output rather than on hours present is therefore essential.

                In conclusion, longer working hours are harming health and family life, and I believe a combination of legal limits and cultural change offers the most realistic solution.
                """,
                "At least two problems named" to listOf("problem", "consequence", "health", "stress", "family"),
                "An example or detail supporting a problem" to listOf("for instance", "for example", "a colleague", "research", "surveys"),
                "At least two measures proposed" to listOf("firstly", "government", "companies", "legislat", "measure"),
                "Measures linked to the problems" to listOf("such a law", "this would", "therefore", "as a result", "consequently"),
                "A conclusion" to listOf("in conclusion", "to sum up", "to conclude"),
                minWords = 250, form = WritingForm.ESSAY),
            B.tryIt("After the marker", "The four things the marker cannot judge.",
                "Count your words: under 250 is an automatic penalty", "Underline your position in the introduction and the conclusion: do they match?", "Check each body paragraph has exactly one idea", "Count error-free sentences: band 7 wants a majority", "Rewrite the weakest paragraph before reading the model",
            ),
            B.check("ie6-practice-k1", "ie6-practice", "The marker reports 'no clear position'. The fastest fix is", listOf("adding more examples", "stating I agree or I disagree in the introduction and conclusion", "making the essay longer"), 1, "Task Response needs an explicit, consistent position."),
            B.vocab(
                B.w("ie6-induced-demand", "induced demand", "new supply creating new use", "Wider roads create induced demand.", "A precise, high-level noun phrase."),
                B.w("ie6-congestion", "congestion", "traffic jams", "Congestion worsened after the expansion.", "Precise alternative to traffic."),
                B.w("ie6-erosion", "erosion of", "gradual weakening of", "The erosion of family life.", "Formal and vivid."),
                B.w("ie6-legislate", "legislate for", "pass a law about", "Governments could legislate for shorter hours.", "Solution vocabulary."),
                B.w("ie6-prioritise", "prioritise", "treat as most important", "Governments should prioritise rail.", "Common in conclusions."),
                B.w("ie6-reputational", "reputational pressure", "pressure from public opinion", "Reputational pressure changes behaviour.", "Band 8 collocation."),
            ),
            B.glossary("Induced demand" to "The effect where new capacity creates new usage.", "Right to disconnect" to "A legal right to ignore work messages off duty.", "Error-free sentence" to "A sentence with no grammar mistake; band 7 needs a majority."),
            B.recap("Forty minutes: four to plan, thirty-two to write, four to check.", "The marker checks ideas, examples, linking, position and length.", "Rewrite before reading the model; that draft is where the band moves."),
        ),
    )

    val module = Module(
        id = "ie-m6", number = 6, title = "Writing Task 2: the essay", summary = "Five question types, the four-paragraph skeleton, cohesion, and two marked essays.",
        examWeight = "about 17 percent", componentId = "writing", topics = listOf(questions, structure, practice), practiceMinutes = 720,
        quiz = Quiz(
            id = "ie-m6-test", title = "Module 6 test", passPercent = 70,
            questions = B.spread(listOf(
                B.q("ie6-q1", "ie6-questions", "The minimum length for Task 2 is", listOf("150 words", "250 words", "350 words"), setOf(1), "Under 250 words carries an automatic penalty on Task Response."),
                B.q("ie6-q2", "ie6-questions", "'Discuss both views and give your own opinion' requires", listOf("your opinion only", "both views and your opinion", "both views without an opinion"), setOf(1), "Missing any of the three parts costs Task Response."),
                B.q("ie6-q3", "ie6-questions", "'Do the advantages outweigh the disadvantages?' asks you to", listOf("list both sides only", "decide which side is greater", "describe advantages only"), setOf(1), "Outweigh demands a verdict."),
                B.q("ie6-q4", "ie6-questions", "Writing 'there are arguments on both sides' and stopping there", listOf("shows balance and scores well", "caps Task Response because no position is taken", "is required in discussion essays"), setOf(1), "Band 7 needs a clear position throughout."),
                B.q("ie6-q5", "ie6-questions", "A two-part question where you answer only the first part", listOf("is acceptable if the answer is long", "loses marks because the task is incomplete", "scores higher for depth"), setOf(1), "Every part of the prompt must be answered."),
                B.q("ie6-q6", "ie6-structure", "The best number of body paragraphs is usually", listOf("one", "two well developed", "four short ones"), setOf(1), "Two developed ideas outscore four undeveloped ones."),
                B.q("ie6-q7", "ie6-structure", "Which opening should you avoid?", listOf("It is frequently argued that…", "Nowadays, in this modern era…", "Opinion is divided on whether…"), setOf(1), "Nowadays openings are empty and strongly associated with low bands."),
                B.q("ie6-q8", "ie6-structure", "A body paragraph should contain", listOf("several unrelated ideas", "one idea, explained and exemplified", "only statistics"), setOf(1), "Topic, explain, example, link."),
                B.q("ie6-q9", "ie6-structure", "Inventing an example such as 'a study in Canada found'", listOf("is penalised as dishonest", "is acceptable since the English is what is marked", "must be cited"), setOf(1), "Examiners do not verify facts; unsupported paragraphs are what cost marks."),
                B.q("ie6-q10", "ie6-structure", "A new idea introduced in the conclusion", listOf("shows range", "weakens Coherence and should be moved or cut", "is required"), setOf(1), "Conclusions restate; they do not introduce."),
                B.q("ie6-q11", "ie6-structure", "Replacing a repeated noun with 'such measures' is an example of", listOf("referencing, which examiners reward", "repetition", "an error"), setOf(0), "Referencing is a cohesion device that band 7 uses naturally."),
                B.q("ie6-q12", "ie6-practice", "How should the 40 minutes be divided?", listOf("40 writing", "4 planning, 32 writing, 4 checking", "10 planning, 30 writing"), setOf(1), "Planning prevents the structural errors that cost most marks."),
                B.q("ie6-q13", "ie6-practice", "Task 2 is worth", listOf("the same as Task 1", "twice Task 1", "half of Task 1"), setOf(1), "Two thirds of the Writing band comes from Task 2."),
                B.q("ie6-q14", "ie6-structure", "'However' and 'By contrast' signal", listOf("addition", "contrast", "conclusion"), setOf(1), "They introduce an opposing point."),
            )),
        ),
    )
}
