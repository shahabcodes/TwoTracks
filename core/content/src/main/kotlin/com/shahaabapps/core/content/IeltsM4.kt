package com.shahaabapps.core.content

/** IELTS module 4: Academic Writing Task 1. Charts, processes and maps in 150 words and 20 minutes. */
internal object IeltsM4 {
    private val refs = Ielts.commonRefs + B.ref("Writing test format", Ielts.ORG + "take-a-test/test-format/writing")

    private val structure = Topic(
        id = "ie4-structure", title = "The four-paragraph report that scores 7", minutes = 24, references = refs,
        blocks = listOf(
            B.warmup(
                "ie4-structure-w1", "ie4-structure",
                "A Task 1 report describes every figure on the chart accurately and in good English, but never says what the chart shows overall. What band do you think Task Achievement can reach?",
                listOf("7, since nothing is wrong", "6", "5 at most"), 2,
                "One missing paragraph puts a ceiling on the whole criterion.",
            ),
            B.plain("Academic Task 1 asks you to describe data: a line graph, bar chart, pie chart, table, process diagram or map. Twenty minutes, at least 150 words, one third of the Writing mark. There are no opinions and no reasons: only what the data shows."),
            B.diagram(
                caption = "The same four paragraphs every time. The overview is the single thing that separates a 5 from a 7.",
                nodes = listOf(
                    DNode("a", "1 Introduction", 0.5f, 0.12f, w = 0.56f, h = 0.16f, tone = 1, sub = "paraphrase the question, one sentence"),
                    DNode("b", "2 Overview", 0.5f, 0.38f, w = 0.56f, h = 0.16f, tone = 2, sub = "two or three big features, no numbers"),
                    DNode("c", "3 Detail A", 0.5f, 0.64f, w = 0.56f, h = 0.16f, sub = "the largest group, with figures"),
                    DNode("d", "4 Detail B", 0.5f, 0.88f, w = 0.56f, h = 0.16f, sub = "the rest, with figures"),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "d")),
                aspect = 0.65f,
            ),
            B.table(
                listOf("Paragraph", "Length", "Contains", "Never contains"),
                listOf("Introduction", "1 sentence", "What the chart shows, where, when, in your own words", "Copied question wording"),
                listOf("Overview", "2 to 3 sentences", "The biggest trend, the highest and lowest, any crossover", "Specific figures"),
                listOf("Detail A", "3 to 4 sentences", "Grouped data with figures and comparison", "Every single number"),
                listOf("Detail B", "3 to 4 sentences", "The remaining group, compared", "Opinions or causes"),
            ),
            B.remember("Start the overview with 'Overall,' or 'In general,'. Examiners look for it. A report with no overview is capped at band 5 for Task Achievement, however good the English."),
            B.h("Paraphrasing the question"),
            B.table(
                listOf("Question says", "You write"),
                listOf("The graph shows", "The line graph illustrates / provides information about"),
                listOf("the number of people who", "how many individuals"),
                listOf("between 2000 and 2020", "over a twenty-year period / from 2000 to 2020"),
                listOf("in four countries", "across four nations"),
                listOf("percentage of households", "proportion of homes"),
            ),
            B.h("A worked report"),
            B.p("Task: The chart below shows the percentage of households in a European city that owned a car, a bicycle and a smartphone in 2005, 2015 and 2025. Summarise the information by selecting and reporting the main features, and make comparisons where relevant."),
            B.code("text", """
                The bar chart compares the proportion of homes in one European city possessing three items,
                namely cars, bicycles and smartphones, at three points over a twenty-year period.

                Overall, smartphone ownership rose dramatically and ended as the most common of the three,
                whereas car ownership declined steadily. Bicycle ownership, by contrast, changed very little
                across the whole period.

                In 2005, cars were owned by roughly 70 percent of households, making them the most widespread
                item, while only about 15 percent had a smartphone. By 2015 the gap had narrowed sharply, with
                smartphones reaching approximately 60 percent and cars falling to just over 60 percent.

                The two lines crossed shortly afterwards. By 2025 almost 95 percent of homes contained a
                smartphone, nearly double the 2015 figure for cars, which had dropped to around 50 percent.
                Bicycle ownership, meanwhile, remained between 40 and 45 percent throughout, making it the
                most stable of the three categories.
            """),
            B.check("ie4-structure-k1", "ie4-structure", "Which sentence belongs in the overview?", listOf("In 2005 cars stood at 70 percent.", "Overall, smartphone ownership rose dramatically while car ownership fell.", "This suggests people prefer phones to cars."), 1, "The overview states big features without figures. The first has numbers; the third gives a reason, which Task 1 never does."),
            B.mistakes(
                "Copying the question sentence into the introduction; copied words are not counted toward the 150.",
                "Listing every number with no grouping or comparison.",
                "Explaining why the trend happened; Task 1 reports, it does not interpret.",
                "Writing under 150 words, which carries an automatic penalty.",
            ),
            B.vocab(
                B.w("ie4-illustrate", "illustrate", "show, present (of a chart)", "The graph illustrates population growth.", "Introduction verb."),
                B.w("ie4-proportion", "proportion", "share, percentage", "The proportion of homes with cars.", "Paraphrase of percentage."),
                B.w("ie4-respectively", "respectively", "in the order just mentioned", "Figures were 20 and 35 percent respectively.", "Pairs two items with two numbers."),
                B.w("ie4-whereas", "whereas", "while, in contrast", "Sales rose, whereas costs fell.", "A comparison connector."),
                B.w("ie4-period", "period", "a span of time", "Over a twenty-year period.", "Used in the introduction."),
                B.w("ie4-widespread", "widespread", "common, found in many places", "Cars were the most widespread item.", "Alternative to 'most common'."),
            ),
            B.glossary("Overview" to "The paragraph stating the big picture; required for band 6 and above.", "Task Achievement" to "The criterion that rewards covering the data and giving an overview.", "Paraphrase" to "Restating the question in your own words."),
            B.recap("Four paragraphs: introduction, overview, two detail paragraphs.", "The overview names the big features without figures and starts with 'Overall'.", "No causes, no opinions, at least 150 words, twenty minutes."),
        ),
    )

    private val language = Topic(
        id = "ie4-language", title = "The language of data: trends, comparisons and approximation", minutes = 26, references = refs,
        blocks = listOf(
            B.warmup(
                "ie4-language-w1", "ie4-language",
                "A bar sits a little below the halfway line. Which do you think is the safer thing to write?",
                listOf("exactly 50 percent", "just under 50 percent", "roughly 45 percent"), 1,
                "Precision you cannot read off the chart is a risk; approximation is expected.",
            ),
            B.plain("Task 1 rewards a narrow, precise set of language used accurately. Learn these tables and you can describe any chart you are given. Repeating 'increase' eight times caps Lexical Resource at 5."),
            B.h("Movement"),
            B.table(
                listOf("Direction", "Verbs", "Nouns"),
                listOf("Up", "rise, increase, grow, climb, surge, soar", "a rise, an increase, growth, a surge"),
                listOf("Down", "fall, decline, decrease, drop, plunge, dip", "a fall, a decline, a decrease, a drop"),
                listOf("No change", "remain stable, level off, plateau, stay constant", "stability, a plateau"),
                listOf("Up and down", "fluctuate, vary", "fluctuation, variation"),
                listOf("Highest or lowest", "peak at, reach a high of, bottom out at", "a peak, a low"),
            ),
            B.diagram(
                caption = "Two ways to say the same thing. Alternate between them: verb plus adverb, and adjective plus noun.",
                nodes = listOf(
                    DNode("a", "Sales rose sharply.", 0.27f, 0.35f, w = 0.44f, h = 0.24f, tone = 1, sub = "verb + adverb"),
                    DNode("b", "There was a sharp rise in sales.", 0.73f, 0.35f, w = 0.46f, h = 0.24f, tone = 2, sub = "adjective + noun"),
                    DNode("c", "same meaning, different grammar: use both for range", 0.5f, 0.8f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE),
                ),
                edges = listOf(DEdge("a", "c"), DEdge("b", "c")),
                aspect = 0.65f,
            ),
            B.table(
                listOf("Size of change", "Adverbs", "Adjectives"),
                listOf("Big", "sharply, dramatically, significantly, steeply", "sharp, dramatic, significant, steep"),
                listOf("Small", "slightly, marginally, gradually", "slight, marginal, gradual"),
                listOf("Steady", "steadily, consistently", "steady, consistent"),
            ),
            B.gap("ie4-l-g1", "Rewrite as adjective plus noun: 'Prices fell sharply.'", "There was a ___ fall in prices.", "sharp", "steep", "dramatic", "significant"),
            B.gap("ie4-l-g2", "Rewrite as verb plus adverb: 'There was a gradual increase in visitors.'", "Visitors increased ___.", "gradually", "steadily"),
            B.h("Comparison"),
            B.table(
                listOf("Relationship", "Language"),
                listOf("Much bigger", "far higher than, considerably more than, three times as many as"),
                listOf("Slightly bigger", "marginally higher than, a little above"),
                listOf("Equal", "the same as, identical to, on a par with"),
                listOf("Double or half", "twice as many as, double the figure for, half as many as"),
                listOf("Highest and lowest", "the highest figure, the lowest proportion, second only to"),
                listOf("Contrast between items", "whereas, while, by contrast, compared with"),
            ),
            B.h("Approximation: you are not a calculator"),
            B.p("You will rarely read an exact value off a graph, and you are not expected to. Approximate language is safer and scores better than a wrong exact number."),
            B.table(
                listOf("Use", "Examples"),
                listOf("Close to a number", "approximately 40 percent, around 40, roughly 40, about 40"),
                listOf("Slightly above", "just over 40, a little more than 40, 40 odd"),
                listOf("Slightly below", "just under 40, almost 40, nearly 40"),
                listOf("A range", "between 40 and 45 percent, in the region of 40"),
            ),
            B.gap("ie4-l-g3", "Approximation: the bar sits a fraction above 60.", "The figure stood at ___ 60 percent.", "just over", "a little over", "slightly over", "just above"),
            B.h("Time and prepositions, the usual errors"),
            B.table(
                listOf("Correct", "Wrong"),
                listOf("rose from 20 to 35 percent", "rose from 20 until 35"),
                listOf("an increase of 15 percentage points", "an increase of 15 percent (when it is points)"),
                listOf("in 2010, between 2010 and 2020, over the period", "at 2010, during 2010 to 2020"),
                listOf("the number of visitors", "the number of visitor"),
                listOf("there was a rise in unemployment", "there was a rise of unemployment"),
            ),
            B.remember("'Percent' and 'percentage point' are different. Going from 20 percent to 25 percent is a rise of five percentage points, or a 25 percent increase. When unsure, write 'from 20 to 25 percent' and avoid the trap."),
            B.check("ie4-language-k1", "ie4-language", "Which sentence is correct and varied?", listOf("The number of users increased dramatically between 2010 and 2020.", "The number of users was dramatically increase from 2010 until 2020.", "The users number increased dramatic in 2010 to 2020."), 0, "Verb plus adverb, with the correct prepositions 'between … and'."),
            B.vocab(
                B.w("ie4-fluctuate", "fluctuate", "rise and fall irregularly", "Prices fluctuated throughout the year.", "Noun: fluctuation."),
                B.w("ie4-plateau", "plateau / level off", "stop changing", "Numbers levelled off after 2015.", "Also: remained stable."),
                B.w("ie4-peak", "peak at", "reach the highest point", "Sales peaked at 90 units in July.", "Noun: a peak of 90."),
                B.w("ie4-marginally", "marginally", "by a very small amount", "Costs rose marginally.", "Opposite: dramatically."),
                B.w("ie4-on-a-par", "on a par with", "at the same level as", "Spending was on a par with 2010.", "A band 7 collocation."),
                B.w("ie4-outstrip", "outstrip", "exceed, overtake", "Demand outstripped supply.", "Strong comparison verb."),
            ),
            B.mistakes(
                "Repeating 'increase' every sentence instead of alternating noun and verb forms.",
                "Reading exact values off a graph; use 'approximately'.",
                "Confusing percent and percentage points.",
                "Using 'raise' for 'rise': 'raise' needs an object.",
            ),
            B.glossary("Collocation" to "Words that naturally go together: a sharp rise, not a strong rise.", "Percentage point" to "The arithmetic difference between two percentages.", "Approximation" to "Around, roughly, just over."),
            B.recap("Alternate verb plus adverb with adjective plus noun for range.", "Comparison and approximation language carries most of the marks.", "Watch from/to, between/and, percent versus percentage points."),
        ),
    )

    private val kinds = Topic(
        id = "ie4-kinds", title = "Each chart type, plus processes and maps", minutes = 26, references = refs,
        blocks = listOf(
            B.warmup(
                "ie4-kinds-w1", "ie4-kinds",
                "You open Task 1 and it is a map of a village in 1990 and today, with no numbers at all. Which of your trend verbs can you use?",
                listOf("All of them", "None of them", "Only 'increase'"), 1,
                "Rose and fell need a quantity to move. A map has none.",
            ),
            B.plain("The four paragraphs never change, but what goes into the overview and the detail paragraphs depends on what you are given. Here is the decision for each type, and the two non-data tasks that catch people out."),
            B.table(
                listOf("Type", "Overview says", "Group the detail by"),
                listOf("Line graph (over time)", "The overall direction and any crossover", "Lines that behave alike; chronology"),
                listOf("Bar chart (categories)", "The largest and smallest, and the biggest gap", "Big categories first, then the rest"),
                listOf("Pie charts (shares)", "The dominant slice and the biggest change between pies", "Largest slices, then the remainder"),
                listOf("Table", "The extremes across rows and columns", "By row or by column, never cell by cell"),
                listOf("Two charts together", "One sentence per chart, then the link between them", "One paragraph each"),
            ),
            B.h("Processes: no numbers, no trends"),
            B.p("A process diagram shows stages, natural or manufactured. The overview says how many stages there are, where it begins and ends, and whether it is a cycle. The detail follows the sequence. Use the passive voice and sequencing words, and never invent a reason."),
            B.table(
                listOf("Function", "Language"),
                listOf("Start", "The process begins with, In the first stage"),
                listOf("Sequence", "Subsequently, Following this, At the next stage, Once this is complete"),
                listOf("Simultaneous", "Meanwhile, At the same time"),
                listOf("Passive", "the beans are roasted, the mixture is heated, the water is then filtered"),
                listOf("End", "Finally, The final stage involves, resulting in"),
                listOf("Cycle", "The cycle then repeats, returning to the first stage"),
            ),
            B.code("text", """
                The diagram illustrates how recycled paper is produced, in a process consisting of six main
                stages, beginning with the collection of waste paper and ending with the delivery of new rolls.

                Overall, the process is linear rather than cyclical, and it combines mechanical separation in
                the early stages with chemical treatment in the later ones.

                Initially, used paper is collected and transported to a processing plant, where it is sorted by
                grade. The sorted paper is then pulped with water, producing a fibrous mixture. Subsequently,
                this pulp is passed through a filter, which removes staples, plastic and other contaminants.

                Once the pulp has been cleaned, it is bleached and spread onto a wide mesh belt, where the
                water drains away. Finally, the sheet is pressed, dried and wound onto rolls, which are
                transported to printers.
            """),
            B.diagram(
                caption = "A process report walks the diagram once, in order. Each arrow becomes a sequencing word.",
                nodes = listOf(
                    DNode("a", "collected", 0.12f, 0.5f, w = 0.18f, h = 0.26f, tone = 1),
                    DNode("b", "sorted", 0.31f, 0.5f, w = 0.16f, h = 0.26f),
                    DNode("c", "pulped", 0.5f, 0.5f, w = 0.16f, h = 0.26f, tone = 2),
                    DNode("d", "filtered", 0.69f, 0.5f, w = 0.18f, h = 0.26f),
                    DNode("e", "pressed and dried", 0.89f, 0.5f, w = 0.2f, h = 0.26f, tone = 1),
                ),
                edges = listOf(DEdge("a", "b", label = "then"), DEdge("b", "c"), DEdge("c", "d", label = "subsequently"), DEdge("d", "e", label = "finally")),
                aspect = 0.38f,
            ),
            B.h("Maps: then and now"),
            B.p("Two maps of the same place at different dates. The overview states the overall character of the change: more built up, more facilities, less green space. The detail uses compass directions, location language and the past passive for what was done."),
            B.table(
                listOf("Function", "Language"),
                listOf("Location", "in the north-west, along the eastern edge, in the centre, adjacent to, opposite"),
                listOf("Added", "was constructed, was built, a car park was added, has been replaced by"),
                listOf("Removed", "was demolished, was cleared, was converted into, made way for"),
                listOf("Unchanged", "remained in place, was left untouched"),
                listOf("Expanded", "was extended, was widened, now occupies a larger area"),
            ),
            B.check("ie4-kinds-k1", "ie4-kinds", "For a process diagram, the overview should state", listOf("the main trend and the highest figure", "the number of stages and whether it is a cycle", "why the process is used"), 1, "Processes have no figures and no trends; stages and shape are the big features."),
            B.gap("ie4-k-g1", "Process language, passive voice.", "The beans ___ then roasted at high temperature.", "are"),
            B.gap("ie4-k-g2", "Map language.", "A supermarket ___ built on the site of the old school.", "was", "has been"),
            B.h("Two charts in one task"),
            B.p("When you are given a graph and a table together, write one detail paragraph for each, and use the overview to state the main feature of each plus the relationship between them. Do not run out of time: 150 words still, not 300."),
            B.mistakes(
                "Writing about a process in the present perfect or the future; use the present simple passive.",
                "Using trend language (rose, fell) for maps or processes.",
                "Describing only one of two charts given.",
                "Inventing causes for a map change; report what changed, not why.",
            ),
            B.vocab(
                B.w("ie4-stage", "stage", "one step in a process", "The process has six stages.", "Also: step, phase."),
                B.w("ie4-subsequently", "subsequently", "after that", "Subsequently the pulp is filtered.", "A sequencing adverb."),
                B.w("ie4-contaminant", "contaminant", "unwanted material", "Filters remove contaminants.", "Common in process diagrams."),
                B.w("ie4-demolish", "demolish", "knock down", "The factory was demolished.", "Map language, past passive."),
                B.w("ie4-adjacent", "adjacent to", "next to", "A car park adjacent to the station.", "Formal alternative to 'next to'."),
                B.w("ie4-make-way", "make way for", "be removed so something else can be built", "Houses made way for a road.", "Map language."),
            ),
            B.glossary("Linear process" to "One that starts and ends, as opposed to a cycle.", "Passive voice" to "The mixture is heated: agent unimportant.", "Compass language" to "North, south-east, along the western edge."),
            B.recap("Each chart type changes only what goes into the overview and how you group details.", "Processes: stages, passive voice, sequencing words, no numbers.", "Maps: compass directions and past passive for what was built, removed or kept."),
        ),
    )

    private val practice = Topic(
        id = "ie4-practice", title = "Write one, and have it marked", minutes = 22, references = refs,
        blocks = listOf(
            B.warmup(
                "ie4-practice-w1", "ie4-practice",
                "You have sixty minutes for both Writing tasks. How much of it do you think Task 1 should get?",
                listOf("Half", "Twenty minutes", "Whatever is left after Task 2"), 1,
                "The two tasks are not worth the same, and the clock should follow the marks.",
            ),
            B.plain("Reading about reports does not move a band; writing them does. Two full tasks below, marked by the app against the content points and the report checks: paragraphs, an overview, figures and comparison language."),
            B.diagram(
                caption = "What the marker checks in a report. Each one is a mark you can see before an examiner ever does.",
                nodes = listOf(
                    DNode("a", "3+ paragraphs", 0.2f, 0.3f, w = 0.3f, h = 0.22f, tone = 1),
                    DNode("b", "an overview sentence", 0.6f, 0.3f, w = 0.34f, h = 0.22f, tone = 2),
                    DNode("c", "figures from the data", 0.2f, 0.72f, w = 0.3f, h = 0.22f, tone = 2),
                    DNode("d", "comparison language", 0.6f, 0.72f, w = 0.34f, h = 0.22f, tone = 1),
                    DNode("e", "150+ words", 0.9f, 0.5f, w = 0.18f, h = 0.22f),
                ),
                aspect = 0.45f,
            ),
            B.h("Task A: a line graph"),
            B.p("The line graph shows the number of international visitors, in millions, to three cities between 2000 and 2020. City A began at 2 million and rose steadily to 9 million. City B started highest at 5 million, peaked at 7 million in 2010, then fell to 4 million. City C remained between 1 and 2 million throughout. Write at least 150 words."),
            B.marked("ie4-p-w1", "Write the report for Task A: introduction, overview, and two detail paragraphs with figures and comparisons. Press return twice between paragraphs.",
                """
                The line graph illustrates how many international visitors, measured in millions, travelled to three cities over a twenty-year period from 2000 to 2020.

                Overall, City A saw by far the strongest growth and finished as the most visited destination, whereas City B rose and then declined. City C, by contrast, remained almost unchanged throughout and was consistently the least popular.

                In 2000, City B was the leading destination with around 5 million visitors, more than twice the figure for City A, which stood at approximately 2 million. City B continued to climb until 2010, when it peaked at roughly 7 million, before falling steadily to about 4 million by the end of the period.

                City A, meanwhile, grew consistently across the whole period, overtaking City B shortly after 2010 and reaching approximately 9 million in 2020. City C fluctuated marginally between 1 and 2 million, making it the most stable but also the lowest of the three.
                """,
                "An introduction that paraphrases the task" to listOf("line graph", "illustrates", "shows", "compares", "provides information"),
                "An overview naming the main features" to listOf("overall", "in general"),
                "City A described with figures" to listOf("city a"),
                "City B and City C described with figures" to listOf("city b"),
                "Comparison between the cities" to listOf("whereas", "while", "compared", "more than", "the highest", "the lowest", "overtak", "twice", "by contrast"),
                minWords = 150, form = WritingForm.REPORT),
            B.h("Task B: a process"),
            B.p("The diagram shows how glass bottles are recycled. Stages: bottles are collected from households; they are transported to a plant; glass is sorted by colour; it is washed; it is crushed into small pieces called cullet; the cullet is melted in a furnace at 1500 degrees; the molten glass is moulded into new bottles; the bottles are delivered to shops. Write at least 150 words."),
            B.marked("ie4-p-w2", "Write the process report for Task B: introduction, overview stating the number of stages and whether it is a cycle, then the stages in sequence using the passive. Press return twice between paragraphs.",
                """
                The diagram illustrates the way in which glass bottles are recycled, in a process made up of eight distinct stages, from household collection through to delivery back to shops.

                Overall, the process is cyclical rather than linear, since the bottles that leave the factory eventually return to be collected again. It can be divided into a mechanical preparation phase and a high-temperature manufacturing phase.

                In the first stage, used bottles are collected from households and transported by lorry to a recycling plant. On arrival, the glass is sorted according to its colour and is then washed to remove labels and residues. Once clean, it is crushed into small fragments known as cullet.

                Subsequently, the cullet is fed into a furnace, where it is melted at a temperature of around 1500 degrees. The resulting molten glass is moulded into new bottles, which are finally packed and delivered to shops, at which point the cycle begins again.
                """,
                "An introduction paraphrasing the diagram" to listOf("diagram", "illustrates", "shows", "process"),
                "An overview with the number of stages or the shape" to listOf("overall", "in general"),
                "Sequencing words" to listOf("first", "then", "subsequently", "finally", "once", "next", "after"),
                "Passive voice for the stages" to listOf("are collected", "is sorted", "is washed", "is crushed", "is melted", "are moulded", "is transported", "are delivered"),
                minWords = 150, form = WritingForm.REPORT),
            B.tryIt("After the marker", "The marker checks structure; you check language.",
                "Counted how many different movement verbs you used", "Replaced any repeated verb with a noun form", "Checked every from/to and between/and", "Rewrote the overview without a single figure", "Timed yourself: 20 minutes, no more",
            ),
            B.check("ie4-practice-k1", "ie4-practice", "Your report has four paragraphs and figures but the marker says the overview is missing. The likely cause is", listOf("too few words", "no sentence starting Overall or In general", "too many comparisons"), 1, "The marker, like the examiner, looks for an explicit overview statement."),
            B.vocab(
                B.w("ie4-cullet", "cullet", "crushed recycled glass", "The cullet is melted in a furnace.", "Process vocabulary."),
                B.w("ie4-furnace", "furnace", "a very hot oven", "Melted in a furnace at 1500 degrees.", "Manufacturing processes."),
                B.w("ie4-molten", "molten", "melted, liquid from heat", "Molten glass is moulded.", "Adjective before the noun."),
                B.w("ie4-residue", "residue", "what remains after cleaning", "Washed to remove residues.", "Formal register."),
                B.w("ie4-distinct", "distinct", "clearly separate", "Eight distinct stages.", "Useful in the overview."),
            ),
            B.glossary("Content point" to "A thing the task requires you to cover.", "Second draft" to "The rewrite after marking, where the band improves.", "Register" to "How formal the writing is; Task 1 is neutral to formal."),
            B.recap("Write, mark, rewrite: the second draft is where the band moves.", "The marker checks paragraphs, overview, figures, comparison and length.", "Twenty minutes per task from the first attempt, never longer."),
        ),
    )

    val module = Module(
        id = "ie-m4", number = 4, title = "Writing Task 1: Academic", summary = "Charts, processes and maps: the four-paragraph report, data language, and two marked tasks.",
        examWeight = "about 8 percent", componentId = "writing", topics = listOf(structure, language, kinds, practice), practiceMinutes = 480,
        quiz = Quiz(
            id = "ie-m4-test", title = "Module 4 test", passPercent = 70,
            questions = B.spread(listOf(
                B.q(
                    "ie4-q1", "ie4-structure", "The minimum word count for Academic Task 1 is",
                    listOf("100", "150", "250"), setOf(1),
                    "150 words for Task 1; 250 for Task 2. Under-length is penalised.",
                    whyWrong = mapOf(
                        0 to "A hundred words would be marked down for length before the content was even weighed.",
                        2 to "250 is Task 2. Writing that much on Task 1 costs the time Task 2 needs and earns nothing.",
                    ),
                ),
                B.q(
                    "ie4-q2", "ie4-structure", "Which must a band 6 or 7 report contain?",
                    listOf("A prediction about the future", "A clear overview paragraph", "The reasons behind the trend"), setOf(1),
                    "Without an overview, Task Achievement is capped at band 5.",
                    whyWrong = mapOf(
                        0 to "Task 1 describes what is shown. Predicting beyond the data is going outside the task.",
                        2 to "Why the trend happened is never in the chart, so saying it is invention rather than description.",
                    ),
                ),
                B.q(
                    "ie4-q3", "ie4-structure", "The overview should contain",
                    listOf("the main features without specific figures", "every figure in the chart", "your opinion about the data"), setOf(0),
                    "Figures belong in the detail paragraphs; opinions belong nowhere in Task 1.",
                    whyWrong = mapOf(
                        1 to "That is the detail paragraphs' job. An overview crowded with numbers stops being an overview.",
                        2 to "Task 1 has no opinion in it at all, in any paragraph.",
                    ),
                ),
                B.q(
                    "ie4-q4", "ie4-structure", "Copying the question wording into your introduction",
                    listOf("saves time and is fine", "is not counted toward your word total", "is required"), setOf(1),
                    "Copied text is discounted, so paraphrase instead.",
                    whyWrong = mapOf(
                        0 to "It costs rather than saves: the words are discounted, so an under-length script can result from it.",
                        2 to "The opposite. Paraphrasing the prompt is what the introduction is for.",
                    ),
                ),
                B.q(
                    "ie4-q5", "ie4-language", "Which is the correct noun form of 'Prices rose sharply'?",
                    listOf("There was a sharp rise in prices.", "There was a sharply rise of prices.", "Prices had a sharp rising."), setOf(0),
                    "Adjective plus noun, with 'in' after 'rise'.",
                    whyWrong = mapOf(
                        1 to "'Sharply' is the adverb, which goes with the verb. The noun 'rise' needs the adjective 'sharp'.",
                        2 to "'Rising' is not the noun here, and 'had a rising' is not English. The noun is 'rise'.",
                    ),
                ),
                B.q(
                    "ie4-q6", "ie4-language", "The bar is a fraction below 50. You write",
                    listOf("exactly 50 percent", "just under 50 percent", "about 45 percent"), setOf(1),
                    "Approximation is expected and safer than a wrong exact value.",
                    whyWrong = mapOf(
                        0 to "'Exactly' claims a precision the chart does not give, and the bar is not on the line.",
                        2 to "Approximation has to stay near the value. 45 is a different reading, not a hedge.",
                    ),
                ),
                B.q(
                    "ie4-q7", "ie4-language", "Which preposition pair is correct?",
                    listOf("rose from 20 until 35", "rose from 20 to 35", "rose between 20 to 35"), setOf(1),
                    "from … to, or between … and.",
                    whyWrong = mapOf(
                        0 to "'Until' is for time, not for a quantity you have moved to.",
                        2 to "'Between' takes 'and'. Mixing the two pairs is the commonest slip in this language set.",
                    ),
                ),
                B.q(
                    "ie4-q8", "ie4-language", "A figure that goes up and down repeatedly",
                    listOf("plateaus", "fluctuates", "peaks"), setOf(1),
                    "Fluctuate means to rise and fall irregularly.",
                    whyWrong = mapOf(
                        0 to "A plateau is the opposite: a stretch where the figure does not move.",
                        2 to "A peak is the single highest point, one moment rather than a pattern.",
                    ),
                ),
                B.q(
                    "ie4-q9", "ie4-kinds", "For a map task, the detail paragraphs should use",
                    listOf("trend verbs like rose and fell", "compass directions and the past passive", "percentages"), setOf(1),
                    "Maps describe what was built, removed or kept, and where.",
                    whyWrong = mapOf(
                        0 to "Nothing on a map rises or falls. The trend verbs need a quantity, and a map has none.",
                        2 to "There are no figures on a map task to turn into percentages.",
                    ),
                ),
                B.q(
                    "ie4-q10", "ie4-kinds", "In a process description the usual voice is",
                    listOf("active present", "passive present", "passive future"), setOf(1),
                    "The beans are roasted: the agent is unimportant.",
                    whyWrong = mapOf(
                        0 to "The active needs a subject doing each step, and a process diagram rarely names one.",
                        2 to "A process is described as it always happens, not as something that will happen.",
                    ),
                ),
                B.q(
                    "ie4-q11", "ie4-kinds", "Given a graph and a table in one task, you should",
                    listOf("describe only the graph", "write one detail paragraph for each and link them in the overview", "write 300 words"), setOf(1),
                    "Both must be covered, still in about 150 to 180 words.",
                    whyWrong = mapOf(
                        0 to "Leaving one out is a missing part of the task, which is the heaviest Task Achievement penalty there is.",
                        2 to "Two sources do not double the word count. They share the same 150 to 180 and the same twenty minutes.",
                    ),
                ),
                B.q(
                    "ie4-q12", "ie4-kinds", "A process diagram overview should mention",
                    listOf("the number of stages and whether it is a cycle", "the highest value", "why the process exists"), setOf(0),
                    "Stages and shape are the big features of a process.",
                    whyWrong = mapOf(
                        1 to "A process has no values to be highest. That is an overview for a chart.",
                        2 to "Purpose is not in the diagram, so writing it is going beyond the data again.",
                    ),
                ),
                B.q(
                    "ie4-q13", "ie4-practice", "Twenty minutes is the target for Task 1 because",
                    listOf("Task 2 is worth twice as much and needs forty", "the examiner times you", "it is the maximum allowed"), setOf(0),
                    "Task 2 carries two thirds of the Writing mark, so it gets two thirds of the hour.",
                    whyWrong = mapOf(
                        1 to "Nobody times the two tasks separately. The split is yours to keep, which is why it has to be practised.",
                        2 to "There is no per-task limit. You can spend the whole hour on Task 1 and lose most of the paper doing it.",
                    ),
                ),
                B.q(
                    "ie4-q14", "ie4-language", "'An increase of five percentage points' describes a move from",
                    listOf("20 percent to 25 percent", "20 percent to 100 percent", "5 percent to 20 percent"), setOf(0),
                    "Percentage points are the arithmetic difference between two percentages.",
                    whyWrong = mapOf(
                        1 to "That is a rise of eighty points. Five is the gap between the two figures, not a multiplier.",
                        2 to "The five is the size of the move, not the figure it started from.",
                    ),
                ),
            )),
        ),
    )
}
