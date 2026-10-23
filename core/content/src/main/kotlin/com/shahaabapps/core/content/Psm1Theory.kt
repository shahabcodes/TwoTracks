package com.shahaabapps.core.content

/**
 * PSM I topics for module psm-theory (Psm1): empiricism, lean thinking and the
 * five values, paraphrased from The Scrum Guide (November 2020).
 */
internal object Psm1Theory {
    private const val SG = "https://scrumguides.org/scrum-guide.html"
    private const val REV = "https://scrumguides.org/revisions.html"
    private const val ORG = "https://www.scrum.org/resources/"

    private val empiricism = Topic(
        id = "psm-empiricism", title = "Empiricism and lean thinking: why Scrum works the way it does", minutes = 20,
        references = listOf(
            B.ref("The Scrum Guide (2020): Scrum Theory", SG),
            B.ref("What is Scrum? (Scrum.org)", ORG + "what-scrum-module"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
            B.ref("The Scrum Artifacts (Scrum.org)", ORG + "scrum-artifacts"),
            B.ref("Scrum Guide revisions: what changed in 2020", REV),
        ),
        blocks = listOf(
            B.warmup(
                "psm-empiricism-w1", "psm-empiricism",
                "Your Android team does not know whether users want a home-screen widget. What would Scrum have you do?",
                listOf(
                    "Build a small usable slice, put it in front of users and decide from what you see",
                    "Write a complete specification first so the plan never has to change",
                    "Ask the most senior manager to decide before any work starts",
                ), 0,
                "Scrum trusts what you observe more than what you predict.",
            ),
            B.plain("""
                Empiricism is a long word for a simple habit: learn by trying, then decide from what actually
                happened. The Scrum Guide says knowledge comes from experience and from making decisions based on
                what is observed. Picture a new onboarding screen in your app. You can argue for weeks about
                whether users will finish it, or you can ship it to a slice of users on Friday and look at the
                numbers on Monday. Scrum is built for the second way, because most product work is complex: you
                cannot know the answer in advance.
            """),
            B.diagram(
                caption = "Transparency makes the real state visible, inspection looks at it often, and adaptation changes course; the Sprint repeats the loop.",
                nodes = listOf(
                    DNode("t", "Transparency", 0.5f, 0.15f, w = 0.34f, h = 0.2f, sub = "see it as it is"),
                    DNode("i", "Inspection", 0.82f, 0.6f, w = 0.32f, h = 0.2f, tone = 1, sub = "look often"),
                    DNode("a", "Adaptation", 0.18f, 0.6f, w = 0.32f, h = 0.2f, tone = 1, sub = "change course"),
                    DNode("n", "Each Sprint: one month or less", 0.5f, 0.9f, w = 0.6f, h = 0.13f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("t", "i", label = "enables"),
                    DEdge("i", "a", label = "triggers"),
                    DEdge("a", "t", dashed = true),
                ),
                aspect = 0.75f,
            ),
            B.h("The three pillars"),
            B.p("""
                Empiricism in Scrum stands on three pillars, three things that hold it up. Transparency means the
                work and the process are visible, to the people doing the work and the people receiving it.
                Inspection means looking at the work and the progress toward goals often and carefully, so
                problems show up early. Adaptation means changing course as soon as possible when something has
                drifted outside what is acceptable. The order matters: the Guide warns that inspecting something
                that is not transparent is misleading and wasteful, and that inspecting without being willing to
                change anything is pointless.
            """),
            B.table(
                listOf("Pillar", "In plain words", "On an Android team"),
                listOf("Transparency", "Everyone can see the real state of the work", "The crash dashboard and the Sprint Backlog are open to the whole team, not kept in one person's head"),
                listOf("Inspection", "Look at it often enough to spot trouble early", "The Developers check progress toward the Sprint Goal every day"),
                listOf("Adaptation", "Change course as soon as something drifts", "A crash after release moves the fix to the top of the plan the same day"),
            ),
            B.check(
                "psm-empiricism-k1", "psm-empiricism",
                "The Sprint Backlog shows the payment screen as finished, but nobody has tested it. Which pillar has failed first?",
                listOf("Transparency", "Inspection", "Adaptation"), 0,
                "The picture the team inspects is false, so the first thing broken is transparency. Inspection and adaptation built on it will both go wrong.",
                whyWrong = mapOf(
                    1 to "The team can inspect all it likes; it is looking at a misleading picture. That is a transparency problem first.",
                    2 to "Adaptation depends on what inspection finds. Here the trouble starts earlier, with what is visible.",
                ),
            ),
            B.h("Where the inspecting happens: the events"),
            B.p("""
                Scrum does not leave inspection to chance. It has five events: the Sprint, which is the container,
                and four events inside it. An event is a fixed meeting with a purpose and a timebox, a maximum
                length. The Guide calls each event a formal opportunity to inspect and adapt, and says the events
                create regularity, so fewer extra meetings are needed. Leaving one out removes one of those
                chances, and the Guide says that using only parts of Scrum is not Scrum.
            """),
            B.table(
                listOf("Event", "What is inspected", "What is adapted"),
                listOf("Sprint Planning", "The Product Backlog, the Product Goal and what the team can do", "The Sprint Backlog: the goal and the plan for this Sprint"),
                listOf("Daily Scrum", "Progress toward the Sprint Goal", "The Sprint Backlog, for the next day of work"),
                listOf("Sprint Review", "The Increment and progress toward the Product Goal", "The Product Backlog"),
                listOf("Sprint Retrospective", "How the Sprint went: people, interactions, processes, tools, the Definition of Done", "How the team works next Sprint"),
            ),
            B.check(
                "psm-empiricism-k2", "psm-empiricism",
                "According to the Scrum Guide, what is each Scrum event?",
                listOf(
                    "A formal opportunity to inspect and adapt",
                    "A status report for managers outside the team",
                    "A checkpoint where the Scrum Master checks each Developer's work",
                ), 0,
                "Every event exists to inspect something and adapt something. That is how empiricism is built into the calendar.",
                whyWrong = mapOf(
                    1 to "Reporting status to managers is not the purpose of any Scrum event.",
                    2 to "The Scrum Master does not inspect individual Developers; the team is self-managing.",
                ),
            ),
            B.h("Lean thinking and a lightweight framework"),
            B.p("""
                The Guide also says Scrum draws on lean thinking: cut waste and focus on what matters. Waste is any
                effort that adds no value, such as a forty-page spec nobody reads or a feature nobody uses. Scrum
                calls itself a lightweight framework. A framework is a frame, not a full house: Scrum defines only
                the parts it needs and is deliberately incomplete. Your team fills in the rest with its own
                practices, such as user stories, pull requests or continuous integration. Those practices are
                welcome, but they are not Scrum and the Scrum Guide does not require them. Scrum itself is free,
                described in the Guide, and its accountabilities, events, artifacts and rules are fixed.
            """),
            B.compare("Plan everything up front, or learn as you go?",
                B.card("Predictive plan", "The work is simple and well known, like repeating a release checklist you have run fifty times.", "All decisions made at the start", "Change is treated as a failure", "Works when nothing surprising can happen"),
                B.card("Empirical, as in Scrum", "The work is complex: you cannot know what users want or how the code will behave until you try.", "Decide in short cycles from what you observe", "Change is expected and cheap", "Risk is limited to one short Sprint at a time"),
            ),
            B.check(
                "psm-empiricism-k3", "psm-empiricism",
                "A manager suggests dropping the Sprint Retrospective because the team is doing fine. What is true?",
                listOf(
                    "Without it the team is no longer doing Scrum and loses a chance to inspect and adapt",
                    "That is fine; mature teams can skip events",
                    "That is fine as long as the Scrum Master agrees",
                ), 0,
                "Every event is required. Dropping one removes an inspect-and-adapt opportunity, and the Guide says a partial Scrum is not Scrum.",
                whyWrong = mapOf(
                    1 to "No team outgrows the events; a good team still improves, and the Retrospective is where it plans that.",
                    2 to "The Scrum Master's job is to make sure the events happen, not to waive them.",
                ),
            ),
            B.worked("""
                Pocketwise is a budgeting app built by one Scrum Team: Priya is the Product Owner, Tom the Scrum
                Master, and Dana, Marcus and Sam are Developers. After a release the crash rate jumps. Transparency:
                the crash dashboard is shared with the whole team and the Sprint Backlog shows the real state of
                every item. Inspection: at the next Daily Scrum the Developers see that the crash blocks the Sprint
                Goal, a working CSV export. Adaptation: the Developers change their plan that morning, put the fix
                first and drop a smaller item after talking it through with Priya. All three pillars, in one day.
            """),
            B.faded(
                "psm-empiricism-f1",
                "At the Sprint Review, the funnel numbers show most new users give up on step three of sign-up. Walk the same three pillars.",
                listOf(
                    "Transparency: the Increment and the real funnel numbers are shown to the stakeholders as they are.",
                    "Inspection: the Scrum Team and stakeholders look at the drop-off and discuss progress toward the Product Goal.",
                    "___: the Product Owner reorders the Product Backlog so a shorter sign-up comes next.",
                ),
                blank = 2,
                answers = listOf("adaptation", "adapt", "adapting"),
                note = "Changing the Product Backlog after inspecting the Increment is adaptation; the Sprint Review is where it usually happens.",
            ),
            B.mistakes(
                "Thinking empiricism means having no plan; it means planning in short cycles and changing the plan from evidence.",
                "Inspecting work that is not transparent, such as items marked done that do not meet the Definition of Done.",
                "Treating user stories, story points or pull requests as part of Scrum; they are optional practices.",
                "Dropping or merging events because the team feels mature; every event is an inspect-and-adapt chance.",
                "Inspecting at the Daily Scrum but then refusing to change the plan, which makes the inspection pointless.",
            ),
            B.example("Pocketwise ships a crash fix the same day it shows up, because the dashboard was visible to all, the Developers inspected it at the Daily Scrum, and they adapted their plan at once."),
            B.h("Check yourself"),
            B.match(
                "psm-x-empiricism-match", "Pair each pillar with what it looks like on the team.",
                "Transparency" to "The Sprint Backlog shows the real state of every item",
                "Inspection" to "Checking progress toward the Sprint Goal every day",
                "Adaptation" to "Changing the plan as soon as a problem appears",
            ),
            B.gap(
                "psm-x-empiricism-gap1", "The three pillars of empiricism.",
                "Scrum's three pillars are transparency, inspection and ___.", "adaptation", "Adaptation",
            ),
            B.gap(
                "psm-x-empiricism-gap2", "Where knowledge comes from, according to the Guide.",
                "Empiricism says knowledge comes from ___ and decisions based on what is observed.", "experience", "Experience",
            ),
            B.tryIt("Try it this week", "No tools needed; just your own team or project.",
                "Name one thing on your team that is not transparent, such as a status only one person knows",
                "Find where your team inspects it today, and how often",
                "Write down one adaptation you made last week because of something you saw",
                "Read the Scrum Theory section of the Scrum Guide, about one page",
            ),
            B.explain(
                "psm-empiricism-x1",
                "Explain empiricism and its three pillars, using an Android team as your example.",
                "Empiricism means knowledge comes from experience, so the team decides from what it observes rather than from a big up-front plan. Transparency keeps the real state visible, such as a shared crash dashboard. Inspection looks at progress toward the Sprint Goal often, for example at the Daily Scrum. Adaptation changes the plan as soon as something drifts, like fixing a release crash first.",
                "Knowledge from experience" to listOf("experience", "observe", "observation"),
                "Transparency" to listOf("transparen", "visible"),
                "Inspection" to listOf("inspect", "looks at"),
                "Adaptation" to listOf("adapt", "adjust", "changes the plan"),
            ),
            B.glossary(
                "Empiricism" to "Knowing from experience and deciding from what is observed, not from prediction.",
                "Transparency" to "The work and process are visible to those doing it and those receiving it.",
                "Inspection" to "Looking at the work and progress often enough to spot problems early.",
                "Adaptation" to "Changing course as soon as possible when something drifts outside acceptable limits.",
                "Lean thinking" to "Reducing waste and focusing on what matters.",
                "Framework" to "A minimal structure that people fill in with their own practices; Scrum is deliberately incomplete.",
                "Timebox" to "The maximum length of an event.",
            ),
            B.recap(
                "Empiricism: knowledge comes from experience and decisions based on what is observed.",
                "Three pillars: transparency, inspection, adaptation; inspection without transparency misleads.",
                "Each event is a formal chance to inspect and adapt; Scrum is lightweight, incomplete on purpose, and whole only when all of it is used.",
            ),
        ),
    )

    private val values = Topic(
        id = "psm-values", title = "The five Scrum values, and what they look like on a team", minutes = 18,
        references = listOf(
            B.ref("The Scrum Guide (2020): Scrum Values", SG),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
            B.ref("The Scrum Values and the Real World (Scrum.org blog)", ORG + "blog/scrum-values-and-real-world"),
            B.ref("Maximize Scrum with the Scrum Values: Respect (Scrum.org blog)", ORG + "blog/maximize-scrum-scrum-values-respect-part-5-5"),
            B.ref("How the Scrum Values direct your behaviour in the Retrospective (Scrum.org blog)", ORG + "blog/how-scrum-values-direct-your-behavior-during-sprint-retrospective-37"),
        ),
        blocks = listOf(
            B.warmup(
                "psm-values-w1", "psm-values",
                "A Developer knows the payment library upgrade will slip but says nothing at the Daily Scrum. Which value is most clearly missing?",
                listOf("Openness", "Focus", "Commitment"), 0,
                "Think about which value is about being honest regarding the work and its problems.",
            ),
            B.plain("""
                The Scrum Guide names five values: commitment, focus, openness, respect and courage. A value is a
                belief that guides how you behave when nobody is checking. The Guide says Scrum succeeds only when
                people get better at living these five. The events and artifacts are the skeleton; the values are
                what make people use them honestly. A Sprint Review where everyone hides bad news has all the right
                meetings and none of the empiricism.
            """),
            B.diagram(
                caption = "When the Scrum Team and the people around it live the five values, transparency, inspection and adaptation come to life and trust grows.",
                nodes = listOf(
                    DNode("c", "Commitment", 0.17f, 0.15f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL),
                    DNode("f", "Focus", 0.5f, 0.15f, w = 0.26f, h = 0.14f, kind = NodeKind.PILL),
                    DNode("o", "Openness", 0.83f, 0.15f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL),
                    DNode("r", "Respect", 0.3f, 0.42f, w = 0.28f, h = 0.14f, kind = NodeKind.PILL),
                    DNode("cg", "Courage", 0.7f, 0.42f, w = 0.28f, h = 0.14f, kind = NodeKind.PILL),
                    DNode("p", "Pillars come to life", 0.5f, 0.7f, w = 0.44f, h = 0.16f, tone = 1, sub = "the three pillars"),
                    DNode("tr", "Trust", 0.5f, 0.92f, w = 0.26f, h = 0.12f, tone = 2),
                ),
                edges = listOf(
                    DEdge("c", "r", dashed = true),
                    DEdge("o", "cg", dashed = true),
                    DEdge("r", "p"),
                    DEdge("cg", "p"),
                    DEdge("f", "p", dashed = true),
                    DEdge("p", "tr", label = "builds"),
                ),
                aspect = 0.8f,
            ),
            B.h("Each value, in plain words"),
            B.table(
                listOf("Value", "What the Guide asks", "On an Android team"),
                listOf("Commitment", "The Scrum Team commits to its goals and to supporting each other", "Dana stays with the Sprint Goal and helps Sam finish the tricky sync code"),
                listOf("Focus", "The main focus is the Sprint's work, to make the best progress toward the goals", "Nobody starts a side project on the tablet layout mid-Sprint"),
                listOf("Openness", "The team and stakeholders are open about the work and the challenges", "Marcus says at once that the payment library upgrade will slip"),
                listOf("Respect", "Team members respect each other as capable, independent people, and are respected in turn", "The backend specialist does not override the Android Developer's design without discussing it"),
                listOf("Courage", "Team members have the courage to do the right thing and work on tough problems", "Sam tells the Product Owner a feature is not Done rather than pretending it is"),
            ),
            B.check(
                "psm-values-k1", "psm-values",
                "At the Sprint Review a Developer calls a feature Done, although it misses the Definition of Done, to avoid disappointing stakeholders. Which values are being let down?",
                listOf("Openness and courage", "Focus and commitment", "Respect only"), 0,
                "Being honest about the work takes openness, and saying unwelcome news to stakeholders takes courage.",
                whyWrong = mapOf(
                    1 to "The Developer may be focused and committed; what is missing is honesty and the nerve to say it.",
                    2 to "Respect matters, but the clearest failure is hiding the truth, which is openness, and avoiding hard news, which is courage.",
                ),
            ),
            B.h("Commitment is to goals, not to every item"),
            B.p("""
                A common trap is to read commitment as a promise to finish every item picked at Sprint Planning.
                The Guide says the Scrum Team commits to achieving its goals and to supporting each other. The
                Sprint Goal is the commitment for the Sprint Backlog. If the Developers learn mid-Sprint that not
                all items will fit, they keep the Sprint Goal and renegotiate the scope with the Product Owner.
                Lowering quality, or working every weekend to finish everything, is not what commitment means.
            """),
            B.check(
                "psm-values-k2", "psm-values",
                "Halfway through the Sprint, the Developers see they cannot finish every selected item, but the Sprint Goal is still reachable. What does commitment mean here?",
                listOf(
                    "Keep working toward the Sprint Goal and renegotiate scope with the Product Owner",
                    "Work overtime until every selected item is finished",
                    "Relax the Definition of Done so that more items count as finished",
                ), 0,
                "The commitment is to the goal and to each other. Scope can be renegotiated; quality cannot drop.",
                whyWrong = mapOf(
                    1 to "Commitment is not a promise of scope. Burning out to finish every item is not what the Guide asks.",
                    2 to "Quality does not decrease during the Sprint; changing the Definition of Done to look finished breaks transparency.",
                ),
            ),
            B.h("How the values hold up the pillars"),
            B.p("""
                The Guide ties the values to empiricism. When the Scrum Team and the people they work with live
                these values, the three pillars come to life and trust grows. Here is a way to remember it, which is
                a reading aid and not a rule from the Guide: openness gives you transparency, courage lets you
                inspect honestly, and respect and commitment let people adapt together without blame. The values
                apply to the people the team works with too, not only to the Scrum Team.
            """),
            B.check(
                "psm-values-k3", "psm-values",
                "According to the Scrum Guide, what happens when the Scrum values are lived by the Scrum Team and the people they work with?",
                listOf(
                    "Transparency, inspection and adaptation come to life, building trust",
                    "The team no longer needs a Scrum Master",
                    "The team's velocity becomes predictable",
                ), 0,
                "The Guide links the values directly to the empirical pillars and to trust.",
                whyWrong = mapOf(
                    1 to "Nothing in the Guide makes the Scrum Master optional; the team always has one.",
                    2 to "Velocity is not in the Scrum Guide at all; it is an optional practice.",
                ),
            ),
            B.worked("""
                The marketing head walks into the Pocketwise Daily Scrum and asks Marcus to add a promotion banner
                today. Walk the values. Focus: the Developers are working toward the Sprint Goal, a working CSV
                export, and the banner is not part of it. Respect: Marcus treats the request as valid and points
                her to Priya, the Product Owner, who decides what goes into the Product Backlog. Courage: he says
                no to a senior person, politely. Openness: he explains why, and that the banner can be ordered for a
                later Sprint. Commitment: the team stays with its goal.
            """),
            B.faded(
                "psm-values-f1",
                "The backend lead tells Sam to skip the automated tests so the budget alert ships on Friday. Walk the values again.",
                listOf(
                    "Sam is open with the Product Owner about the risk to the date, so the trade-off is visible.",
                    "Sam says plainly that skipping tests would break the Definition of Done; saying that to a senior person takes ___.",
                    "The Product Owner and the Developers renegotiate scope instead of lowering quality, which keeps the commitment to the Sprint Goal.",
                ),
                blank = 1,
                answers = listOf("courage"),
                note = "Doing the right thing under pressure, especially toward someone senior, is the Scrum value of courage.",
            ),
            B.mistakes(
                "Reading commitment as a promise to finish every selected item, rather than a commitment to the goals and to each other.",
                "Thinking the values apply only to the Scrum Team; the Guide expects the people they work with to live them too.",
                "Hiding bad news to keep stakeholders happy, which breaks openness and makes inspection useless.",
                "Confusing focus with busyness; focus is on the Sprint's work and progress toward the goals.",
                "Treating courage as being blunt or rude; courage sits alongside respect for capable, independent people.",
            ),
            B.example("When Marcus politely sends a mid-Sprint banner request to Priya, explains why, and keeps working toward the Sprint Goal, he shows focus, respect, courage, openness and commitment in one conversation."),
            B.h("Check yourself"),
            B.match(
                "psm-x-values-match", "Pair each value with the behaviour.",
                "Openness" to "Saying early that the upgrade will slip",
                "Courage" to "Telling a stakeholder the feature is not Done",
                "Focus" to "Working on the Sprint Goal, not side projects",
                "Respect" to "Trusting a teammate as a capable professional",
                "Commitment" to "Supporting each other toward the team's goals",
            ),
            B.gap(
                "psm-x-values-gap1", "The five values.",
                "Commitment, focus, openness, respect and ___.", "courage", "Courage",
            ),
            B.tryIt("Try it this week", "Watch your own team for one Sprint, or one week.",
                "Write down one moment where someone showed courage, and one where it was missing",
                "Notice one piece of bad news that took more than a day to surface",
                "Ask yourself which value would have surfaced it sooner",
                "Read the Scrum Values section of the Scrum Guide, a few lines long",
            ),
            B.explain(
                "psm-values-x1",
                "Explain the five Scrum values and why they matter to empiricism, using your own team as the example.",
                "The five values are commitment, focus, openness, respect and courage. Commitment is to the Sprint Goal and to each other, not to every item. Openness about problems keeps the work transparent, and courage lets a Developer say a feature is not Done. When the team and its stakeholders live the values, transparency, inspection and adaptation come to life and trust grows.",
                "Names the values" to listOf("commitment", "courage", "openness"),
                "Commitment to goals, not scope" to listOf("sprint goal", "each other", "not to every"),
                "Openness and courage in practice" to listOf("not done", "problems", "bad news"),
                "Values bring the pillars to life" to listOf("pillars", "transparency", "trust"),
            ),
            B.glossary(
                "Commitment" to "The Scrum Team commits to achieving its goals and to supporting each other.",
                "Focus" to "Attention on the work of the Sprint, to make the best progress toward the goals.",
                "Openness" to "The team and stakeholders are open about the work and its challenges.",
                "Respect" to "Treating each other as capable, independent people, and being treated so.",
                "Courage" to "Doing the right thing and working on tough problems.",
                "Sprint Goal" to "The single objective for the Sprint; the commitment for the Sprint Backlog.",
            ),
            B.recap(
                "Five values: commitment, focus, openness, respect, courage.",
                "Commitment is to the goals and to each other, not to finishing every selected item.",
                "Lived by the team and the people around it, the values bring the three pillars to life and build trust.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(empiricism, values)

    val quiz: Quiz = Quiz(
        id = "psm-theory-test", title = "Module test",
        questions = B.spread(
            listOf(
                B.q(
                    "psm-empiricism-q1", "psm-empiricism",
                    "According to the Scrum Guide, what is empiricism based on?",
                    listOf(
                        "Knowledge comes from experience and decisions are based on what is observed",
                        "Detailed plans made before the work starts",
                        "The judgement of the most experienced person on the team",
                        "Metrics such as velocity and story points",
                    ), setOf(0),
                    "The Guide says empiricism asserts that knowledge comes from experience and making decisions based on what is observed.",
                    whyWrong = mapOf(
                        1 to "Up-front plans are the predictive approach that empiricism replaces for complex work.",
                        2 to "Seniority is not evidence; Scrum decides from what is observed.",
                        3 to "Velocity and story points are not in the Scrum Guide; they are optional practices.",
                    ),
                ),
                B.q(
                    "psm-empiricism-q2", "psm-empiricism",
                    "Which three are the pillars of empiricism in Scrum? Choose three.",
                    listOf("Transparency", "Inspection", "Adaptation", "Estimation", "Prediction"), setOf(0, 1, 2),
                    "Transparency, inspection and adaptation are the three pillars that hold empiricism up.",
                    whyWrong = mapOf(
                        3 to "Estimation is a practice some teams use; it is not a pillar.",
                        4 to "Prediction is what empiricism avoids relying on.",
                    ),
                ),
                B.q(
                    "psm-empiricism-q3", "psm-empiricism",
                    "What does the Scrum Guide say about inspecting something that is not transparent?",
                    listOf(
                        "It can be misleading and wasteful",
                        "It is fine as long as it happens at a Scrum event",
                        "It is the Scrum Master's job to make it work anyway",
                    ), setOf(0),
                    "Inspection relies on transparency; without it, the team inspects a false picture and makes wrong decisions.",
                    whyWrong = mapOf(
                        1 to "Holding an event does not fix a false picture; the inspection still misleads.",
                        2 to "The Scrum Master helps the team improve transparency; inspecting hidden work cannot be made to work.",
                    ),
                ),
                B.q(
                    "psm-empiricism-q4", "psm-empiricism",
                    "During the Sprint, the Developers notice at the Daily Scrum that a crash from the last release will stop them reaching the Sprint Goal. What should happen?",
                    listOf(
                        "The Developers adapt the Sprint Backlog as soon as possible",
                        "They wait for the Sprint Retrospective to discuss it",
                        "They wait for the Sprint Review so stakeholders can decide",
                        "The Scrum Master reassigns the work",
                    ), setOf(0),
                    "Adaptation should happen as soon as possible once inspection shows a deviation; the Developers own and adjust their plan.",
                    whyWrong = mapOf(
                        1 to "The Retrospective is about how the team works; delaying the fix to it makes inspection pointless.",
                        2 to "The Developers can adapt their own plan now; the Review is for the Product Backlog and the Increment.",
                        3 to "The Scrum Master does not assign work; the Developers are self-managing.",
                    ),
                ),
                B.q(
                    "psm-empiricism-q5", "psm-empiricism",
                    "Scrum is described in the Guide as which of the following?",
                    listOf(
                        "A lightweight framework that is deliberately incomplete",
                        "A complete methodology that prescribes every practice",
                        "A set of best practices for software teams only",
                        "A project management process for fixed-scope contracts",
                    ), setOf(0),
                    "Scrum defines only the parts it needs and leaves room for the team's own practices.",
                    whyWrong = mapOf(
                        1 to "Scrum is purposefully incomplete; it does not prescribe every practice.",
                        2 to "Scrum is used well beyond software, and it is a framework, not a list of best practices.",
                        3 to "Scrum is built for complex work where scope is learnt, not fixed up front.",
                    ),
                ),
                B.q(
                    "psm-empiricism-q6", "psm-empiricism",
                    "Which idea does the Scrum Guide name, alongside empiricism, as a foundation of Scrum?",
                    listOf("Lean thinking", "Waterfall planning", "Six Sigma", "Extreme Programming"), setOf(0),
                    "The Guide says Scrum is founded on empiricism and lean thinking, which reduces waste and focuses on essentials.",
                    whyWrong = mapOf(
                        1 to "Waterfall is a predictive approach; Scrum is empirical.",
                        2 to "Six Sigma is not mentioned in the Scrum Guide.",
                        3 to "Extreme Programming practices can be used with Scrum, but the Guide does not name it as a foundation.",
                    ),
                ),
                B.q(
                    "psm-empiricism-q7", "psm-empiricism",
                    "An organisation runs Sprints and a Daily Scrum but has dropped the Sprint Review and Retrospective. What is the best description?",
                    listOf(
                        "It is not Scrum; Scrum exists only in its entirety",
                        "It is a valid lightweight version of Scrum",
                        "It is Scrum as long as Sprints are one month or less",
                    ), setOf(0),
                    "The Guide says Scrum's elements are immutable and that implementing only parts of it is not Scrum.",
                    whyWrong = mapOf(
                        1 to "Scrum is already lightweight; removing events makes it something else.",
                        2 to "Sprint length is one rule of many; the missing events still make it not Scrum.",
                    ),
                ),
                B.q(
                    "psm-empiricism-q8", "psm-empiricism",
                    "Which statement about the Scrum events is true?",
                    listOf(
                        "Each event is a formal opportunity to inspect and adapt",
                        "Events are optional for experienced teams",
                        "Events are mainly for reporting progress to management",
                        "Only the Sprint Review involves inspection",
                    ), setOf(0),
                    "All the events are designed to enable transparency, inspection and adaptation, and they create regularity.",
                    whyWrong = mapOf(
                        1 to "Every event is required, whatever the team's experience.",
                        2 to "Reporting to management is not the purpose of any event.",
                        3 to "Every event inspects something: Planning, the Daily Scrum, the Review and the Retrospective.",
                    ),
                ),
                B.q(
                    "psm-values-q1", "psm-values",
                    "Which are the five Scrum values?",
                    listOf(
                        "Commitment, focus, openness, respect, courage",
                        "Transparency, inspection, adaptation, trust, focus",
                        "Honesty, speed, quality, respect, teamwork",
                        "Commitment, velocity, openness, respect, courage",
                    ), setOf(0),
                    "The five values in the Guide are commitment, focus, openness, respect and courage.",
                    whyWrong = mapOf(
                        1 to "Transparency, inspection and adaptation are the pillars of empiricism, not values.",
                        2 to "These sound good, but they are not the Guide's list.",
                        3 to "Velocity is not a Scrum value, and it is not in the Scrum Guide at all.",
                    ),
                ),
                B.q(
                    "psm-values-q2", "psm-values",
                    "A Developer hides a failing integration test until the last day of the Sprint. Which value is most directly missing?",
                    listOf("Openness", "Focus", "Commitment", "Respect"), setOf(0),
                    "The Guide asks the Scrum Team and stakeholders to be open about the work and its challenges.",
                    whyWrong = mapOf(
                        1 to "The Developer may be focused on the work; the problem is hiding it.",
                        2 to "Commitment is to goals and each other; the clearer failure is not being open.",
                        3 to "Respect is about treating people as capable professionals; the gap here is honesty about the work.",
                    ),
                ),
                B.q(
                    "psm-values-q3", "psm-values",
                    "According to the Scrum Guide, what is the Scrum Team committed to?",
                    listOf(
                        "Achieving its goals and supporting each other",
                        "Delivering every Product Backlog item selected at Sprint Planning",
                        "Meeting the deadlines set by stakeholders",
                    ), setOf(0),
                    "Commitment is to the goals and to each other. Scope can be renegotiated with the Product Owner.",
                    whyWrong = mapOf(
                        1 to "Selected items are the plan toward the Sprint Goal, not a promise; scope may be renegotiated.",
                        2 to "Stakeholders do not set the team's commitments; the Scrum Team commits to its goals.",
                    ),
                ),
                B.q(
                    "psm-values-q4", "psm-values",
                    "Who is expected to live the Scrum values?",
                    listOf(
                        "The Scrum Team and the people they work with",
                        "Only the Scrum Master",
                        "Only the Developers",
                    ), setOf(0),
                    "The Guide says the pillars come to life when the values are embodied by the Scrum Team and the people they work with.",
                    whyWrong = mapOf(
                        1 to "The Scrum Master helps others understand them, but everyone is expected to live them.",
                        2 to "The values apply to the whole Scrum Team and to stakeholders, not just the Developers.",
                    ),
                ),
                B.q(
                    "psm-values-q5", "psm-values",
                    "A senior stakeholder pressures the Developers to skip testing to hit a date. One Developer explains that this would break the Definition of Done. Which value does that show most?",
                    listOf("Courage", "Focus", "Openness", "Commitment"), setOf(0),
                    "Doing the right thing under pressure from someone senior is courage.",
                    whyWrong = mapOf(
                        1 to "Focus is on the Sprint's work; standing up to pressure is courage.",
                        2 to "The Developer is open too, but the defining act here is the nerve to push back.",
                        3 to "Commitment is to goals and each other; refusing to cut quality under pressure is courage first.",
                    ),
                ),
                B.q(
                    "psm-values-q6", "psm-values",
                    "What does the Scrum Guide say happens when the values are embodied?",
                    listOf(
                        "The pillars of transparency, inspection and adaptation come to life, building trust",
                        "The team can skip the Sprint Retrospective",
                        "Stakeholders no longer need to attend the Sprint Review",
                    ), setOf(0),
                    "Living the values brings empiricism to life and builds trust.",
                    whyWrong = mapOf(
                        1 to "No event becomes optional; the Retrospective is still required.",
                        2 to "Stakeholders still take part in the Review; the values make that conversation honest.",
                    ),
                ),
            ),
        ),
    )
}
