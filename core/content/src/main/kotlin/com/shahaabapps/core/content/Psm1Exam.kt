package com.shahaabapps.core.content

/**
 * PSM I topics for module psm-exam (Psm1): the myths the questions are built
 * to catch, and how to sit and pace the assessment. Facts about the sitting
 * are only the ones Scrum.org publishes; nothing here invents a rule.
 */
internal object Psm1Exam {
    private const val GUIDE = "https://scrumguides.org/scrum-guide.html"
    private const val GUIDE_PDF = "https://scrumguides.org/docs/scrumguide/v2020/2020-Scrum-Guide-US.pdf"
    private const val PSM1 = "https://www.scrum.org/assessments/professional-scrum-master-i-certification"
    private const val OPEN = "https://www.scrum.org/open-assessments/scrum-open"

    private val traps = Topic(
        id = "psm-traps", title = "Myths and traps: what the Scrum Guide does not say", minutes = 25,
        references = listOf(
            B.ref("The Scrum Guide (2020)", GUIDE),
            B.ref("Scrum Guide revisions: what changed in 2020", "https://scrumguides.org/revisions.html"),
            B.ref("Scrum.org: Scrum glossary", "https://www.scrum.org/resources/scrum-glossary"),
            B.ref("Professional Scrum Master I (PSM I)", PSM1),
            B.ref("Scrum Open, the free practice assessment", OPEN),
        ),
        blocks = listOf(
            B.warmup(
                "psm-traps-w1", "psm-traps",
                "Which of these does the Scrum Guide itself describe?",
                listOf("The Sprint Goal", "Story points", "Velocity"), 0,
                "Only one of these words appears anywhere in the Guide.",
            ),
            B.plain("""
                The assessment tests the Scrum Guide, not the way your last company ran Scrum. Most Android teams
                have habits that feel like Scrum: story points, a velocity chart, a "Sprint 0" to set up the
                project, a stand-up where everyone reports to the Scrum Master. Some of these are complementary
                practices: extra techniques a team may choose to use inside Scrum, which the Guide neither
                requires nor describes. Others contradict the Guide. The questions are written so that the
                familiar habit looks right. Your job is to answer from the Guide every time.
            """),
            B.diagram(
                caption = "The Scrum Guide is the only source for the exam: some habits are optional extras it allows, others break its rules.",
                nodes = listOf(
                    DNode("guide", "Scrum Guide", 0.5f, 0.5f, w = 0.3f, h = 0.22f, tone = 1, sub = "the only source"),
                    DNode("sp", "Story points", 0.17f, 0.18f, w = 0.3f, h = 0.18f, tone = 2, sub = "optional extra"),
                    DNode("vel", "Velocity", 0.17f, 0.5f, w = 0.3f, h = 0.18f, tone = 2, sub = "optional extra"),
                    DNode("us", "User stories", 0.17f, 0.82f, w = 0.3f, h = 0.18f, tone = 2, sub = "optional extra"),
                    DNode("s0", "Sprint 0", 0.83f, 0.18f, w = 0.3f, h = 0.18f, sub = "breaks Scrum"),
                    DNode("hard", "Hardening Sprint", 0.83f, 0.5f, w = 0.3f, h = 0.18f, sub = "breaks Scrum"),
                    DNode("ext", "Longer Sprint", 0.83f, 0.82f, w = 0.3f, h = 0.18f, sub = "breaks Scrum"),
                ),
                edges = listOf(
                    DEdge("sp", "guide", label = "may use", dashed = true),
                    DEdge("vel", "guide", dashed = true),
                    DEdge("us", "guide", dashed = true),
                    DEdge("s0", "guide", label = "conflicts"),
                    DEdge("hard", "guide"),
                    DEdge("ext", "guide"),
                ),
                aspect = 0.75f,
            ),
            B.h("Practices Scrum allows but does not contain"),
            B.p("""
                The Guide says Scrum works well as a container for other techniques, methods and practices. So
                using story points or user stories is not wrong. What is wrong, on the exam, is an answer that
                says Scrum requires them. When an option says a team must track velocity, or that the Product
                Backlog must be written as user stories, it is a trap.
            """),
            B.table(
                listOf("Practice", "In the Scrum Guide?", "How to treat it on the exam"),
                listOf("Story points and planning poker", "No", "A way some teams size items; never required"),
                listOf("Velocity", "No", "A team's own forecasting aid; not a target, not a Scrum measure"),
                listOf("Burn-down and burn-up charts", "Named only as examples of forecasting practices", "Useful, but they do not replace empiricism"),
                listOf("User stories", "No", "One format for Product Backlog items; not required"),
                listOf("Scrum of Scrums", "No", "A scaling technique outside the Guide"),
                listOf("A refinement meeting", "No; refinement is an ongoing activity", "Not one of the five events"),
                listOf("Definition of Ready", "No; only \"ready for selection\" is described", "Not a Scrum artifact or commitment"),
            ),
            B.check(
                "psm-traps-k1", "psm-traps",
                "A manager asks the Scrum Master to report each team's velocity every Sprint. What does the Scrum Guide say about velocity?",
                listOf("Nothing; it is a complementary practice a team may choose to use", "It is the Guide's main measure of a team's progress", "Every Scrum Team must track it from the first Sprint"), 0,
                "Velocity is not in the Scrum Guide. The Guide measures progress against the Sprint Goal and the Product Goal.",
                whyWrong = mapOf(
                    1 to "The Guide never mentions velocity; its progress is measured against goals.",
                    2 to "Nothing in the Guide requires velocity, so it cannot be a must.",
                ),
            ),
            B.h("Myths that break Scrum"),
            B.table(
                listOf("Myth", "What the Scrum Guide says"),
                listOf("A Sprint 0 to set up, or a hardening or release Sprint at the end", "Every Sprint aims at a valuable, useful Increment that meets the Definition of Done; there are no special kinds of Sprint"),
                listOf("Extend the Sprint by two days to finish", "Sprints have a fixed length of one month or less"),
                listOf("Change the Sprint Goal when the work is harder than expected", "The Sprint Goal is fixed; scope is renegotiated with the Product Owner"),
                listOf("The Scrum Master assigns tasks and runs the stand-up", "Developers self-manage; the Daily Scrum is their event"),
                listOf("The PO accepts each story at the Sprint Review before release", "The Sprint Review is a working session, never a gate to releasing value"),
                listOf("A separate test team checks the work", "The Scrum Team has no sub-teams; Developers instil quality through the Definition of Done"),
                listOf("A committee of product managers acts as Product Owner", "The Product Owner is one person, not a committee"),
                listOf("Roles, self-organising, the three Daily Scrum questions", "2020 wording: accountabilities, self-managing, and no prescribed questions"),
            ),
            B.check(
                "psm-traps-k2", "psm-traps",
                "Before the first Sprint, a manager proposes a Sprint 0 for architecture and set-up that delivers no Increment. What fits the Scrum Guide?",
                listOf("The Guide has no such Sprint; every Sprint should create a valuable, useful Increment", "It is required before the first real Sprint", "It is fine if it lasts no longer than one month"), 0,
                "The whole Scrum Team is accountable for creating a valuable, useful Increment every Sprint. The Guide describes no Sprint that skips it.",
                whyWrong = mapOf(
                    1 to "The Guide never mentions a Sprint 0, so it cannot require one.",
                    2 to "Staying under a month does not fix a Sprint that aims to produce nothing usable.",
                ),
            ),
            B.h("Reading the words: best answer, must and may, all of the above"),
            B.p("""
                Several options can be partly true. The question wants the best answer: the one closest to what
                the Guide says. Watch the strength of the words. The Guide uses must for its rules, for example
                that teams must follow the organisation's Definition of Done as a minimum, and may for what is
                allowed, for example that an Increment may be delivered before the Sprint Review. An option that
                turns a may into a must is usually the trap. Absolute words such as only and never are sometimes
                right: only the Product Owner can cancel a Sprint. So check them against the Guide rather than
                rejecting them on sight. And when the Scrum Master's best move is asked, the Guide's answer is
                usually to coach, teach or facilitate, not to decide for the team.
            """),
            B.check(
                "psm-traps-k3", "psm-traps",
                "Who is required to take part in the Daily Scrum?",
                listOf("The Developers", "The whole Scrum Team", "The Developers and the Scrum Master"), 0,
                "The Daily Scrum is an event for the Developers. The PO and SM take part as Developers only if they work on Sprint Backlog items.",
                whyWrong = mapOf(
                    1 to "The Daily Scrum is for the Developers; the PO is not required unless working on Sprint Backlog items.",
                    2 to "The Scrum Master does not have to attend; it ensures the event happens, not that it is present.",
                ),
            ),
            B.worked("""
                Question: the Developers cannot finish every item, and the manager suggests extending the Sprint by
                two days. What should the Scrum Master do? Step one, spot the habit: stretching a Sprint. Step two,
                find the Guide's rule: Sprints have a fixed length, and when the work is bigger than expected the
                Developers renegotiate scope with the Product Owner without changing the Sprint Goal. Step three,
                pick the option closest to that rule: the Scrum Master coaches the team and the manager on why the
                Sprint ends on time and the scope is renegotiated, rather than agreeing to the extension.
            """),
            B.faded(
                "psm-traps-f1",
                "A stakeholder suggests a hardening Sprint before the Play Store release, to fix bugs and finish testing.",
                listOf(
                    "Spot the habit: a Sprint whose purpose is to finish work that earlier Sprints left undone.",
                    "Find the Guide's rule: every Sprint creates a usable Increment that meets the ___ of Done.",
                    "Pick the option that keeps testing and bug fixing inside every Sprint, not saved for the end.",
                ),
                blank = 1,
                answers = listOf("Definition"),
                note = "If the Definition of Done is met every Sprint, there is nothing left to harden.",
            ),
            B.mistakes(
                "Answering from your own company's practice when the Scrum Guide says something different.",
                "Choosing an option that makes velocity, story points or user stories mandatory.",
                "Accepting a Sprint 0, a hardening Sprint or an extended Sprint as normal Scrum.",
                "Using 2017 wording such as roles, self-organising or the three Daily Scrum questions.",
                "Picking the first plausible option without reading the others for the best answer.",
            ),
            B.h("Check yourself"),
            B.match(
                "psm-x-traps-match", "Pair each habit with how the Guide treats it.",
                "Story points" to "Allowed, not required",
                "Hardening Sprint" to "Not Scrum",
                "Burn-down chart" to "Example of forecasting, not required",
                "Three Daily Scrum questions" to "Removed in 2020",
            ),
            B.gap(
                "psm-x-traps-gap1", "The 2020 word for what used to be called roles.",
                "In the 2020 Guide, roles became ___.", "accountabilities", "Accountabilities",
            ),
            B.tryIt("Try it in 15 minutes", "Think of the last team you worked on, or the way you plan your own app.",
                "List ten habits that team called Scrum",
                "Mark each one as in the Guide, an allowed extra, or against the Guide",
                "For every one marked against the Guide, find the sentence it breaks",
                "Rewrite two of them the way the Guide would have them done",
            ),
            B.explain(
                "psm-traps-x1",
                "Explain how to answer a question when the familiar habit and the Scrum Guide disagree.",
                "The exam tests only the Scrum Guide, not common practice. Story points, velocity and user stories are complementary practices: a team may use them, but they are not required. Habits such as a Sprint 0, a hardening Sprint or an extended Sprint break the Guide's rules. Several options can be partly true, so read them all and choose the best answer, the one closest to the Guide.",
                "Only the Scrum Guide counts" to listOf("scrum guide", "the guide"),
                "Complementary practices are allowed but not required" to listOf("complementary", "not required"),
                "Some habits break the rules" to listOf("sprint 0", "sprint-0", "sprint zero", "hardening", "release sprint", "extend", "longer sprint"),
                "Choose the best answer" to listOf("best answer", "closest"),
            ),
            B.glossary(
                "Complementary practice" to "A technique a team may use inside Scrum that the Guide neither describes nor requires.",
                "Accountabilities" to "The 2020 word for Product Owner, Scrum Master and Developers, which used to be called roles.",
                "Self-managing" to "The 2020 word for a team that decides who does what, when and how.",
                "Best answer" to "The option closest to the Scrum Guide when more than one looks partly right.",
                "Hardening Sprint" to "A Sprint kept for finishing testing and fixing; not part of Scrum.",
            ),
            B.recap(
                "Answer from the Scrum Guide every time, even when your own team works differently.",
                "Story points, velocity and user stories are allowed extras; Sprint 0, hardening and longer Sprints break Scrum.",
                "Read every option, weigh must against may, and pick the best answer.",
            ),
        ),
    )

    private val assessment = Topic(
        id = "psm-assessment", title = "The PSM I assessment: 80 questions, 60 minutes, 85 percent", minutes = 20,
        references = listOf(
            B.ref("Professional Scrum Master I (PSM I)", PSM1),
            B.ref("Scrum Open, the free practice assessment", OPEN),
            B.ref("The Scrum Guide (2020)", GUIDE),
            B.ref("The Scrum Guide (2020), PDF to print and annotate", GUIDE_PDF),
        ),
        blocks = listOf(
            B.warmup(
                "psm-assessment-w1", "psm-assessment",
                "80 questions in 60 minutes leaves you about how long for each one?",
                listOf("45 seconds", "90 seconds", "30 seconds"), 0,
                "Turn the hour into seconds, then share it out.",
            ),
            B.plain("""
                PSM I is Scrum.org's Professional Scrum Master I assessment. You sit it online, on your own
                computer: 80 questions in 60 minutes, and you pass with 85 percent, which is 68 right answers.
                Questions come in three shapes: pick one answer, pick several, and true or false. Each attempt
                costs USD 200, and the certification does not need renewing. Every question comes from the Scrum
                Guide of November 2020. The hour is short, so this topic is about two things: pacing the clock and
                reading each question the way it was written.
            """),
            B.diagram(
                caption = "A pacing plan: a steady first pass with bookmarks, a second look at the bookmarked questions, and a last check before the hour ends.",
                nodes = listOf(
                    DNode("p1", "First pass", 0.18f, 0.28f, w = 0.3f, h = 0.22f, tone = 1, sub = "about 40 minutes"),
                    DNode("p2", "Bookmarks", 0.5f, 0.28f, w = 0.28f, h = 0.22f, sub = "about 15 minutes"),
                    DNode("p3", "Final check", 0.82f, 0.28f, w = 0.3f, h = 0.22f, tone = 2, sub = "the last minutes"),
                    DNode("note", "80 questions in 60 minutes: 45 seconds each, 68 right to pass", 0.5f, 0.75f, w = 0.9f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("p1", "p2"),
                    DEdge("p2", "p3"),
                ),
                aspect = 0.7f,
            ),
            B.h("The numbers"),
            B.table(
                listOf("Fact", "Value", "What it means for you"),
                listOf("Questions", "80", "Multiple choice, multiple answer, true or false"),
                listOf("Time", "60 minutes", "About 45 seconds a question on average"),
                listOf("Pass mark", "85 percent", "68 right; at most 12 wrong"),
                listOf("Source", "The Scrum Guide, November 2020", "Answer from the Guide, not from habit"),
                listOf("Cost", "USD 200 an attempt", "Prepare until practice scores are comfortably above the bar"),
                listOf("Renewal", "None", "Pass once and it stays"),
            ),
            B.check(
                "psm-assessment-k1", "psm-assessment",
                "How many of the 80 questions can you get wrong and still pass at 85 percent?",
                listOf("12", "20", "8"), 0,
                "85 percent of 80 is 68, so 80 minus 68 leaves 12 you can miss.",
                whyWrong = mapOf(
                    1 to "20 wrong is 60 right, which is 75 percent.",
                    2 to "8 wrong is safe, but it is not the limit: 12 is.",
                ),
            ),
            B.h("Pacing: one pass, bookmarks, a final check"),
            B.p("""
                Forty-five seconds is an average, not a rule for each question. Many questions are one line and
                take fifteen seconds; a few are scenarios that take a minute and a half. The plan is to keep
                moving. On the first pass, answer what you can. When a question will not settle in about a
                minute, pick your best answer, bookmark it, and move on, so it is never left blank: a blank can
                never score. With the first pass done, go back to the bookmarks with the time that is left. Keep
                the last few minutes to make sure every question has an answer.
            """),
            B.steps("A pacing plan for the hour",
                "Check the clock at question 20" to "About 15 minutes gone or less",
                "Check again at question 40" to "About 30 minutes gone or less",
                "Finish the first pass, every question answered" to "Around 40 to 45 minutes gone",
                "Revisit the bookmarked questions" to "Change an answer only for a reason found in the Guide",
                "Use the last minutes to confirm nothing is unanswered" to "80 of 80 answered",
            ),
            B.check(
                "psm-assessment-k2", "psm-assessment",
                "You reach question 30 with 25 minutes gone. How is your pace?",
                listOf("Slightly behind: 45 seconds each puts question 30 at about 22 minutes", "Comfortably ahead of the average", "Exactly on the average"), 0,
                "30 questions times 45 seconds is 1,350 seconds, about 22 and a half minutes. At 25 minutes you are a little behind, so bookmark more readily.",
                whyWrong = mapOf(
                    1 to "Being ahead would mean fewer than about 22 minutes gone at question 30.",
                    2 to "Exactly on the average would be about 22 and a half minutes, not 25.",
                ),
            ),
            B.h("Reading a question the way it was written"),
            B.bullets(
                "Read the instruction: does it want one answer, or several? A multiple-answer question tells you to choose more than one.",
                "Read every option, even when the first one looks right. Several can be partly true.",
                "Choose the best answer: the option closest to the Scrum Guide's own words and intent.",
                "The Guide wins over common practice. If your team did it differently, the Guide is still the answer.",
                "Weigh the strength of the words: must, should, may, only, never.",
                "When asked what a Scrum Master should do, prefer coaching, teaching and facilitating over deciding for the team.",
            ),
            B.check(
                "psm-assessment-k3", "psm-assessment",
                "The first option of a question looks right. What should you do?",
                listOf("Read every option, then choose the one that best fits the Scrum Guide", "Pick it at once to save time", "Bookmark the question without answering it"), 0,
                "Several options can be partly true, and the question wants the best one, so read them all first.",
                whyWrong = mapOf(
                    1 to "The trap is often a partly true option placed early; the best answer may come later.",
                    2 to "A bookmarked question still needs an answer; a blank can never score.",
                ),
            ),
            B.h("How to prepare"),
            B.table(
                listOf("Step", "Why"),
                listOf("Read the Scrum Guide several times, slowly", "It is short, and every question comes from it"),
                listOf("Take Scrum Open, the free practice assessment, again and again", "Keep going until you score 100 percent several times in a row"),
                listOf("Sit this course's mock under its 60-minute timer", "It trains the pace as well as the content"),
                listOf("Trace every wrong answer back to a sentence in the Guide", "A miss you can point to in the Guide does not happen twice"),
                listOf("Book the sitting when practice scores stay well above 85 percent", "A comfortable margin absorbs a few hard questions on the day"),
            ),
            B.worked("""
                Sitting it: a quiet room, water, nothing else open. Question 1 to 20 take 12 minutes; two scenario
                questions get a best answer and a bookmark. At question 40 the clock shows 27 minutes. The first
                pass ends at 44 minutes with every question answered and seven bookmarks. Thirteen minutes go on
                the bookmarks: two answers change, each because a sentence of the Guide came to mind, not because
                of a feeling. The last three minutes confirm 80 of 80 answered, and the paper is submitted.
            """),
            B.faded(
                "psm-assessment-f1",
                "A friend plans to sit PSM I next week. He scores 80 percent on Scrum Open.",
                listOf(
                    "He rereads the Scrum Guide, slowly, starting with the sections behind his wrong answers.",
                    "He retakes Scrum Open until he scores ___ percent several times in a row.",
                    "He sits a full mock under a 60-minute timer and checks his pace at questions 20 and 40.",
                ),
                blank = 1,
                answers = listOf("100", "one hundred"),
                note = "Practice to full marks, because the real bar of 85 percent leaves room for only 12 misses.",
            ),
            B.mistakes(
                "Spending three minutes on one hard question early and rushing the last twenty.",
                "Leaving a question blank to come back to it later; give a best answer and bookmark it.",
                "Answering a multiple-answer question with one option because the instruction was not read.",
                "Changing an answer on review out of nerves rather than for a reason found in the Guide.",
                "Booking the sitting after a single good practice score instead of several in a row.",
            ),
            B.h("Check yourself"),
            B.match(
                "psm-x-assessment-match", "Pair each number with what it means.",
                "80" to "Questions in the sitting",
                "60" to "Minutes on the clock",
                "68" to "Right answers needed to pass",
                "45" to "Seconds a question, on average",
            ),
            B.gap(
                "psm-x-assessment-gap1", "The pass mark as a count.",
                "To pass, you need ___ of the 80 questions right.", "68", "sixty-eight",
            ),
            B.tryIt("Try it in 15 minutes", "Scrum Open is free and online; any quiet half hour will do.",
                "Take Scrum Open once with a timer running and note your score",
                "Note the time you reached the halfway question",
                "For each wrong answer, find the sentence in the Scrum Guide that settles it",
                "Write down the pace checkpoints you will use on the day",
            ),
            B.explain(
                "psm-assessment-x1",
                "Explain how you will pace and approach the 80 questions of PSM I.",
                "PSM I is 80 questions in 60 minutes with a pass mark of 85 percent, so about 45 seconds a question and 68 right answers. On the first pass I answer everything, and when a question will not settle I give a best answer and bookmark it, then return to the bookmarks with the time left. I read every option and choose the one closest to the Scrum Guide, not to common practice. Before booking I read the Guide several times and take Scrum Open until I score 100 percent repeatedly.",
                "The pace: about 45 seconds" to listOf("45 seconds", "forty-five seconds"),
                "Bookmark and return" to listOf("bookmark", "come back"),
                "Read every option; the Guide over practice" to listOf("every option", "closest to the scrum guide", "best answer"),
                "Prepare with the Guide and Scrum Open" to listOf("scrum open", "100 percent"),
            ),
            B.glossary(
                "PSM I" to "Professional Scrum Master I, Scrum.org's first-level Scrum Master assessment.",
                "Scrum Open" to "Scrum.org's free online practice assessment on the Scrum Guide.",
                "Bookmark" to "A mark on a question so you can find it again before submitting.",
                "Pass mark" to "85 percent: 68 of the 80 questions answered correctly.",
            ),
            B.recap(
                "80 questions, 60 minutes, 85 percent to pass: 45 seconds each and at most 12 wrong.",
                "Answer every question on the first pass, bookmark the hard ones, return with the time left.",
                "Read every option and choose the best answer from the Guide; practise with Scrum Open until 100 percent, repeatedly.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(traps, assessment)

    val quiz: Quiz = Quiz(
        id = "psm-exam-test", title = "Passing the assessment module test",
        questions = B.spread(
            listOf(
                B.q(
                    "psm-exam1", "psm-traps",
                    "Which of these is part of the Scrum Guide?",
                    listOf("The Sprint Goal", "Velocity", "Story points", "Scrum of Scrums"), setOf(0),
                    "The Sprint Goal is the commitment of the Sprint Backlog. The other three are complementary practices outside the Guide.",
                    whyWrong = mapOf(
                        1 to "Velocity is a forecasting habit some teams use; the Guide never names it.",
                        2 to "Story points are one way to size items; the Guide does not describe them.",
                        3 to "Scrum of Scrums is a scaling technique outside the Guide.",
                    ),
                ),
                B.q(
                    "psm-exam2", "psm-traps",
                    "The Product Backlog must be written as user stories. Is this true according to the Scrum Guide?",
                    listOf("True", "False", "True only for teams new to Scrum"), setOf(1),
                    "The Guide does not prescribe any format for Product Backlog items. User stories are a complementary practice a team may choose.",
                    whyWrong = mapOf(
                        0 to "User stories are allowed, but the Guide never requires them.",
                        2 to "The Guide has no special rules for new teams; it prescribes no item format at all.",
                    ),
                ),
                B.q(
                    "psm-exam3", "psm-traps",
                    "A release is near and the team proposes a hardening Sprint to fix bugs and finish testing. What is the best response?",
                    listOf("Every Sprint should produce a usable Increment that meets the Definition of Done, so no hardening Sprint is needed", "Agree, as long as it is shorter than a normal Sprint", "Agree, and let the testers run it as a separate team", "Agree, and skip the Sprint Retrospective that Sprint"), setOf(0),
                    "Quality is built in every Sprint through the Definition of Done. A Sprint saved for finishing earlier work is not part of Scrum.",
                    whyWrong = mapOf(
                        1 to "Its length does not matter; a Sprint that only finishes earlier work breaks the idea of a Done Increment every Sprint.",
                        2 to "Scrum has no sub-teams, so testers are not a separate team.",
                        3 to "Every Sprint ends with a Sprint Retrospective; no event is skipped.",
                    ),
                ),
                B.q(
                    "psm-exam4", "psm-traps",
                    "Which words come from the 2020 Scrum Guide? Select two.",
                    listOf("Accountabilities", "Self-managing", "Roles", "Self-organising"), setOf(0, 1),
                    "In 2020 the Guide moved from roles to accountabilities and from self-organising to self-managing.",
                    whyWrong = mapOf(
                        2 to "Roles was the 2017 word; 2020 speaks of accountabilities.",
                        3 to "Self-organising was the 2017 word; 2020 says self-managing.",
                    ),
                ),
                B.q(
                    "psm-exam5", "psm-traps",
                    "At the Daily Scrum, each Developer must answer three set questions. Is this true according to the 2020 Scrum Guide?",
                    listOf("True", "False", "True only when the Scrum Master attends"), setOf(1),
                    "The three questions were removed in 2020. The Developers choose any structure that focuses on progress toward the Sprint Goal and produces a plan for the next day.",
                    whyWrong = mapOf(
                        0 to "The three questions were in older versions; 2020 lets the Developers pick the structure.",
                        2 to "Who attends changes nothing; the Guide prescribes no questions at all.",
                    ),
                ),
                B.q(
                    "psm-exam6", "psm-traps",
                    "The Developers will not finish all selected items. The manager asks the Scrum Master to extend the Sprint by two days. What should the Scrum Master do?",
                    listOf("Explain that Sprints have a fixed length, and help the Developers renegotiate scope with the Product Owner", "Extend the Sprint, since the Sprint Goal matters most", "Extend the Sprint and shorten the next one to make up the time", "Cancel the Sprint and start a new one"), setOf(0),
                    "Sprints are fixed in length. The flexibility is in scope, renegotiated with the Product Owner without changing the Sprint Goal.",
                    whyWrong = mapOf(
                        1 to "A Sprint is never stretched, even for the Sprint Goal.",
                        2 to "Changing Sprint lengths to balance time still stretches this one.",
                        3 to "Only the Product Owner can cancel, and only when the Sprint Goal becomes obsolete.",
                    ),
                ),
                B.q(
                    "psm-exam7", "psm-traps",
                    "Which statements about burn-down charts match the Scrum Guide? Select two.",
                    listOf("They are one of several practices for forecasting progress", "They do not replace the importance of empiricism", "Every Scrum Team must keep one", "The Scrum Master must update it daily"), setOf(0, 1),
                    "The Guide names burn-downs, burn-ups and cumulative flows as practices that can be useful, but they do not replace empiricism.",
                    whyWrong = mapOf(
                        2 to "The Guide mentions them as examples, never as a requirement.",
                        3 to "The Guide gives the Scrum Master no charting duty.",
                    ),
                ),
                B.q(
                    "psm-exam8", "psm-traps",
                    "An option says the Increment may be released before the Sprint Review. Another says it must wait for the Sprint Review. Which matches the Guide?",
                    listOf("It may be released before the Sprint Review", "It must wait for the Sprint Review", "It must wait for the next Sprint Planning"), setOf(0),
                    "An Increment may be delivered to stakeholders before the end of the Sprint. The Sprint Review is never a gate to releasing value.",
                    whyWrong = mapOf(
                        1 to "This turns the Review into a gate, which the Guide says it is not.",
                        2 to "Sprint Planning has nothing to do with releasing an Increment.",
                    ),
                ),
                B.q(
                    "psm-exam9", "psm-traps",
                    "A company appoints a committee of three product managers as the Product Owner. What does the Scrum Guide say?",
                    listOf("The Product Owner is one person, not a committee", "That is fine if they vote on the order", "That is required for large products", "That is fine if one of them attends the Sprint Review"), setOf(0),
                    "The Product Owner is one person. Others may represent needs in the backlog, but they convince the Product Owner.",
                    whyWrong = mapOf(
                        1 to "Voting is still a committee; the Guide gives the accountability to one person.",
                        2 to "Even with several teams on one product there is one Product Owner.",
                        3 to "Attendance does not change the rule: one person is accountable.",
                    ),
                ),
                B.q(
                    "psm-exam10", "psm-assessment",
                    "How many questions does the PSM I assessment have, and how long do you get?",
                    listOf("80 questions in 60 minutes", "60 questions in 80 minutes", "100 questions in 90 minutes"), setOf(0),
                    "PSM I is 80 questions in 60 minutes, which is about 45 seconds a question on average.",
                    whyWrong = mapOf(
                        1 to "The numbers are the other way round: 80 questions, 60 minutes.",
                        2 to "That is not the PSM I format; it is 80 questions in an hour.",
                    ),
                ),
                B.q(
                    "psm-exam11", "psm-assessment",
                    "What score do you need to pass PSM I?",
                    listOf("85 percent, which is 68 of 80", "80 percent, which is 64 of 80", "75 percent, which is 60 of 80"), setOf(0),
                    "The pass mark is 85 percent: 68 right answers out of 80, so at most 12 wrong.",
                    whyWrong = mapOf(
                        1 to "80 percent is a common pass mark elsewhere, but PSM I asks for 85.",
                        2 to "75 percent would leave 20 wrong; PSM I allows only 12.",
                    ),
                ),
                B.q(
                    "psm-exam12", "psm-assessment",
                    "A scenario question will not settle after a minute. What is the best move?",
                    listOf("Give your best answer, bookmark it and move on", "Leave it blank and come back at the end", "Keep working on it until you are sure"), setOf(0),
                    "An answered and bookmarked question can still score if time runs out; the clock is better spent on the questions ahead.",
                    whyWrong = mapOf(
                        1 to "A blank can never score, and you may run out of time before returning.",
                        2 to "One question is worth the same as any other; spending minutes on it costs easier ones later.",
                    ),
                ),
                B.q(
                    "psm-exam13", "psm-assessment",
                    "Your last team always let the Product Owner assign tasks at Sprint Planning. A question asks who decides how the work gets done. What should you answer from?",
                    listOf("The Scrum Guide: the Developers decide how", "Your team's practice: the Product Owner", "Whatever most teams in the industry do"), setOf(0),
                    "The assessment tests the Scrum Guide. The Guide says how the work is done is the Developers' sole decision.",
                    whyWrong = mapOf(
                        1 to "Your experience may differ from the Guide; the exam follows the Guide.",
                        2 to "Common practice is not the source of the questions; the Guide is.",
                    ),
                ),
            ),
        ),
    )
}
