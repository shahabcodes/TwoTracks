package com.shahaabapps.core.content

/**
 * PSM I topics for module psm-events (Psm1): the Sprint and the four events
 * inside it, paraphrased from The Scrum Guide (November 2020).
 */
internal object Psm1Events {
    private const val SG = "https://scrumguides.org/scrum-guide.html"
    private const val REV = "https://scrumguides.org/revisions.html"
    private const val ORG = "https://www.scrum.org/resources/"

    private val sprint = Topic(
        id = "psm-sprint", title = "The Sprint: the container for every other event", minutes = 20,
        references = listOf(
            B.ref("The Scrum Guide (2020): The Sprint", SG),
            B.ref("What is a Sprint? (Scrum.org)", ORG + "what-is-a-sprint-in-scrum"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
            B.ref("What is Scrum? (Scrum.org)", ORG + "what-scrum-module"),
            B.ref("Scrum Guide revisions", REV),
        ),
        blocks = listOf(
            B.warmup(
                "psm-sprint-w1", "psm-sprint",
                "The team is one feature short on the last day of a two-week Sprint. Can they extend the Sprint by three days?",
                listOf(
                    "No; the Sprint has a fixed length and the next one starts straight away",
                    "Yes, if the Product Owner agrees",
                    "Yes, once per release",
                ), 0,
                "Think about what a fixed length is for.",
            ),
            B.plain("""
                The Sprint is Scrum's heartbeat: a fixed stretch of time, one month or less, in which ideas become
                value. Every other event happens inside it: Sprint Planning, the Daily Scrums, the Sprint Review and
                the Sprint Retrospective. A new Sprint starts the moment the previous one ends, with no gap in
                between. Picture a two-week release train for your Android app: it leaves on schedule every time,
                and whatever is Done is on board.
            """),
            B.diagram(
                caption = "The Sprint holds every other event: Sprint Planning, a Daily Scrum each day, the Sprint Review and the Retrospective; the next Sprint starts immediately.",
                nodes = listOf(
                    DNode("box", "Sprint: one month or less", 0.5f, 0.1f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    DNode("plan", "Planning", 0.13f, 0.45f, w = 0.22f, h = 0.2f, sub = "up to 8h"),
                    DNode("daily", "Daily Scrum", 0.38f, 0.45f, w = 0.22f, h = 0.2f, sub = "15 min"),
                    DNode("rev", "Review", 0.63f, 0.45f, w = 0.22f, h = 0.2f, sub = "up to 4h"),
                    DNode("retro", "Retro", 0.87f, 0.45f, w = 0.22f, h = 0.2f, sub = "up to 3h"),
                    DNode("next", "Next Sprint starts at once", 0.5f, 0.85f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("plan", "daily"),
                    DEdge("daily", "rev"),
                    DEdge("rev", "retro"),
                    DEdge("retro", "next", label = "immediately", dashed = true),
                ),
                aspect = 0.7f,
            ),
            B.h("The rules during a Sprint"),
            B.table(
                listOf("During the Sprint", "On the Pocketwise team"),
                listOf("No changes are made that would endanger the Sprint Goal", "A last-minute banner request does not push out the CSV export the Sprint is about"),
                listOf("Quality does not decrease", "Tests are not skipped to finish faster"),
                listOf("The Product Backlog is refined as needed", "Next Sprint's items are broken down and clarified along the way"),
                listOf("Scope may be clarified and renegotiated with the Product Owner as more is learned", "A nice-to-have file picker is dropped so the export can still ship"),
            ),
            B.check(
                "psm-sprint-k1", "psm-sprint",
                "Which of these is allowed during a Sprint?",
                listOf(
                    "Clarifying and renegotiating scope with the Product Owner as more is learned",
                    "Lowering the quality bar to finish the selected items",
                    "Changing the Sprint Goal because a senior stakeholder asks",
                ), 0,
                "Scope can flex with the Product Owner; the Sprint Goal and quality do not.",
                whyWrong = mapOf(
                    1 to "The Guide says quality does not decrease during the Sprint.",
                    2 to "No changes are made that would endanger the Sprint Goal, whoever asks.",
                ),
            ),
            B.h("Why a fixed, short length"),
            B.p("""
                A fixed length gives consistency: the team, its stakeholders and the organisation can rely on an
                inspection at least every calendar month. When a Sprint is too long, the Guide warns, the Sprint
                Goal may become invalid, complexity may rise and risk may increase. Shorter Sprints mean more
                learning cycles and limit risk to a smaller window. The length is not stretched to finish work, and
                there are no special Sprints with a different purpose, such as a Sprint 0 for setup or a hardening
                Sprint for bug fixing: every Sprint aims at a valuable, useful Increment. Practices such as
                burn-down charts can help forecast progress, but the Guide says they do not replace empiricism.
            """),
            B.check(
                "psm-sprint-k2", "psm-sprint",
                "The team proposes a hardening Sprint before each release, only for fixing bugs and stabilising. What is the best answer?",
                listOf(
                    "Not Scrum: every Sprint should produce a valuable, useful Increment that meets the Definition of Done",
                    "A good idea, as long as it is shorter than one month",
                    "Fine if the Scrum Master runs it",
                ), 0,
                "Scrum has no special-purpose Sprints. Quality is built in every Sprint through the Definition of Done.",
                whyWrong = mapOf(
                    1 to "The length is not the issue; a Sprint that exists only to fix up earlier undone work hides poor quality.",
                    2 to "Who runs it does not matter; Scrum has no hardening or stabilisation Sprints.",
                ),
            ),
            B.h("Cancelling a Sprint"),
            B.p("""
                A Sprint can be cancelled if its Sprint Goal becomes obsolete, meaning it no longer makes sense, for
                example because the market moved or the company changed direction. Only the Product Owner has the
                authority to cancel it. Cancelling is rare; a Sprint that is merely going badly is not cancelled,
                the team renegotiates scope instead.
            """),
            B.check(
                "psm-sprint-k3", "psm-sprint",
                "Halfway through a Sprint, Google announces a change that makes the Sprint Goal pointless. What can happen?",
                listOf(
                    "The Product Owner may cancel the Sprint, because the Sprint Goal is obsolete",
                    "The Scrum Master cancels the Sprint and restarts it",
                    "The Developers silently switch to a new goal",
                ), 0,
                "A Sprint can be cancelled if its goal becomes obsolete, and only the Product Owner has that authority.",
                whyWrong = mapOf(
                    1 to "The Scrum Master has no authority to cancel a Sprint.",
                    2 to "The Sprint Goal does not change mid-Sprint; if it is obsolete, the Product Owner decides about cancelling.",
                ),
            ),
            B.worked("""
                Pocketwise runs two-week Sprints. On day nine the Developers see the CSV export will not be fully
                Done by day ten. Length: they do not extend the Sprint; its end date is fixed. Scope: they talk to
                Priya and agree to drop the date-range filter, keeping the Sprint Goal of a working export.
                Quality: the export still meets the Definition of Done. Leftovers: the filter goes back to the
                Product Backlog. Next: the Sprint Review and Retrospective happen on day ten, and Sprint Planning
                for the next Sprint follows at once.
            """),
            B.faded(
                "psm-sprint-f1",
                "Sprint Goal: users can log in with their fingerprint. On day three the biometric vendor withdraws its library and the company drops biometric login entirely.",
                listOf(
                    "The Developers raise the news with the Product Owner straight away.",
                    "The Product Owner judges that the Sprint Goal is now obsolete.",
                    "Because only the ___ has the authority, she cancels the Sprint, and a new Sprint begins with Sprint Planning.",
                ),
                blank = 2,
                answers = listOf("Product Owner", "PO"),
                note = "A Sprint can be cancelled only when its goal is obsolete, and only by the Product Owner.",
            ),
            B.mistakes(
                "Extending a Sprint by a few days to finish work; the length is fixed and the next Sprint starts at once.",
                "Planning a Sprint 0, a hardening Sprint or a release Sprint; Scrum has no special-purpose Sprints.",
                "Changing the Sprint Goal mid-Sprint instead of renegotiating scope around it.",
                "Letting quality drop near the end of a Sprint to fit everything in.",
                "Thinking the Scrum Master or the Developers can cancel a Sprint; only the Product Owner can.",
            ),
            B.example("Pocketwise keeps its two-week Sprint end date, drops a filter with Priya's agreement, ships a Done export, and sends the filter back to the Product Backlog."),
            B.h("Check yourself"),
            B.match(
                "psm-x-sprint-match", "Pair each situation with the rule.",
                "One feature short on the last day" to "Do not extend; renegotiate scope",
                "A request that would endanger the Sprint Goal" to "Not made during the Sprint",
                "The Sprint Goal becomes obsolete" to "The Product Owner may cancel",
                "A bug-fixing-only Sprint before release" to "Not part of Scrum",
            ),
            B.gap(
                "psm-x-sprint-gap1", "The longest a Sprint can be.",
                "A Sprint is a fixed length of one ___ or less.", "month", "Month",
            ),
            B.tryIt("Try it this week", "Look at your team's calendar.",
                "Write down your Sprint length and whether it has ever changed",
                "Find the last time work was stretched, rushed or carried over, and why",
                "Check whether any Sprint had a special purpose such as stabilising",
                "Read the Sprint section of the Scrum Guide",
            ),
            B.explain(
                "psm-sprint-x1",
                "Explain what a Sprint is and the rules that hold during it, using an Android team as the example.",
                "A Sprint is a fixed length of one month or less that contains all the other events, and the next Sprint starts immediately after it. During it, nothing is changed that would endanger the Sprint Goal, quality does not decrease, and scope can be renegotiated with the Product Owner. The length is never extended to finish work, and only the Product Owner can cancel a Sprint, if its goal becomes obsolete.",
                "Fixed length, one month or less" to listOf("one month", "fixed length"),
                "Container for the other events" to listOf("contains", "other events", "container"),
                "Rules: Sprint Goal and quality protected" to listOf("endanger", "quality does not"),
                "Cancellation by the Product Owner" to listOf("cancel", "obsolete"),
            ),
            B.glossary(
                "Sprint" to "A fixed-length event of one month or less that contains all the other events.",
                "Sprint Goal" to "The single objective for the Sprint.",
                "Obsolete" to "No longer useful or relevant; the only reason to cancel a Sprint.",
                "Refinement" to "Breaking down and adding detail to Product Backlog items, done as needed during the Sprint.",
                "Burn-down chart" to "An optional forecasting chart; useful, but not a replacement for empiricism and not required by Scrum.",
            ),
            B.recap(
                "A Sprint is one month or less, fixed, and the next starts immediately; it contains every other event.",
                "No changes that endanger the Sprint Goal; quality does not decrease; scope is renegotiated with the Product Owner.",
                "No Sprint 0 or hardening Sprints; only the Product Owner can cancel, and only if the goal is obsolete.",
            ),
        ),
    )

    private val sprintPlanning = Topic(
        id = "psm-sprint-planning", title = "Sprint Planning: why, what and how", minutes = 20,
        references = listOf(
            B.ref("The Scrum Guide (2020): Sprint Planning", SG),
            B.ref("What is Sprint Planning? (Scrum.org)", ORG + "what-is-sprint-planning"),
            B.ref("Navigating the Scrum Events: Sprint Planning (Scrum.org blog)", ORG + "blog/navigating-scrum-events-sprint-planning"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
        ),
        blocks = listOf(
            B.warmup(
                "psm-sprint-planning-w1", "psm-sprint-planning",
                "Who crafts the Sprint Goal at Sprint Planning?",
                listOf("The whole Scrum Team", "The Product Owner alone", "The Scrum Master"), 0,
                "The Guide says the goal is shaped together.",
            ),
            B.plain("""
                Sprint Planning starts the Sprint by laying out the work to be done. The whole Scrum Team creates
                the plan together. The Product Owner makes sure everyone is ready to discuss the most important
                Product Backlog items and how they connect to the Product Goal. The team may invite other people
                to give advice, such as a security expert before a payment feature. It answers three questions:
                why is this Sprint valuable, what can be Done, and how will the work get done.
            """),
            B.diagram(
                caption = "Sprint Planning answers why, what and how; the Sprint Goal, the selected items and the plan together make the Sprint Backlog.",
                nodes = listOf(
                    DNode("why", "Why", 0.17f, 0.25f, w = 0.3f, h = 0.22f, tone = 1, sub = "Sprint Goal"),
                    DNode("what", "What", 0.5f, 0.25f, w = 0.3f, h = 0.22f, sub = "selected items"),
                    DNode("how", "How", 0.83f, 0.25f, w = 0.3f, h = 0.22f, sub = "the plan"),
                    DNode("sb", "Sprint Backlog", 0.5f, 0.75f, w = 0.4f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 1, sub = "goal + items + plan"),
                ),
                edges = listOf(
                    DEdge("why", "sb"),
                    DEdge("what", "sb"),
                    DEdge("how", "sb"),
                ),
                aspect = 0.7f,
            ),
            B.h("Three topics"),
            B.table(
                listOf("Topic", "The question", "Who", "What comes out"),
                listOf("Why", "Why is this Sprint valuable?", "The Product Owner proposes how the product could gain value; the whole Scrum Team crafts the Sprint Goal", "The Sprint Goal, final before Planning ends"),
                listOf("What", "What can be Done this Sprint?", "The Developers select items, in discussion with the Product Owner", "The selected Product Backlog items"),
                listOf("How", "How will the chosen work get done?", "The Developers alone decide; often by breaking items into pieces of a day or less", "The plan for delivering them"),
            ),
            B.check(
                "psm-sprint-planning-k1", "psm-sprint-planning",
                "When must the Sprint Goal be finalised?",
                listOf("Before the end of Sprint Planning", "By the first Daily Scrum", "At the Sprint Review"), 0,
                "The Sprint Goal is crafted by the whole Scrum Team and finalised before Sprint Planning ends.",
                whyWrong = mapOf(
                    1 to "The Daily Scrum inspects progress toward a goal that already exists.",
                    2 to "The Review happens at the end; the goal guides the whole Sprint, so it is set at the start.",
                ),
            ),
            B.h("How much to take on"),
            B.p("""
                Choosing how much can be Done in a Sprint is hard. The Developers decide it. The more they know
                about their past performance, their capacity for the coming Sprint (who is on holiday, for example)
                and their Definition of Done, the more confident their forecast will be. The Product Owner can
                explain what matters most, but cannot push more work in. Items can be refined during Planning to
                make them clearer.
            """),
            B.check(
                "psm-sprint-planning-k2", "psm-sprint-planning",
                "At Sprint Planning the Product Owner wants twelve items in the Sprint; the Developers believe eight is realistic. Who decides?",
                listOf("The Developers", "The Product Owner", "The Scrum Master breaks the tie"), 0,
                "The Developers select how much they can complete, in discussion with the Product Owner.",
                whyWrong = mapOf(
                    1 to "The Product Owner explains value and order but does not decide how much the Developers can do.",
                    2 to "The Scrum Master does not decide scope; the Developers do.",
                ),
            ),
            B.h("The timebox and the result"),
            B.p("""
                Sprint Planning has a timebox, a maximum length, of eight hours for a one-month Sprint; for shorter
                Sprints it is usually shorter. The Sprint Goal, the Product Backlog items selected for the Sprint
                and the plan for delivering them together form the Sprint Backlog. The plan is the Developers'
                own. Only the Sprint Goal is fixed; the plan around it can change as they learn.
            """),
            B.check(
                "psm-sprint-planning-k3", "psm-sprint-planning",
                "What makes up the Sprint Backlog?",
                listOf(
                    "The Sprint Goal, the selected Product Backlog items, and the plan for delivering them",
                    "Only the selected Product Backlog items",
                    "The whole Product Backlog, reordered for this Sprint",
                ), 0,
                "The Guide names three parts: the why (Sprint Goal), the what (selected items) and the how (the plan).",
                whyWrong = mapOf(
                    1 to "The items alone miss the Sprint Goal and the plan.",
                    2 to "The Product Backlog stays the Product Owner's list; the Sprint Backlog is a separate plan for one Sprint.",
                ),
            ),
            B.worked("""
                Pocketwise plans a two-week Sprint, so Planning is kept well under eight hours. Why: Priya proposes
                that users need to get their data out, and the whole team crafts the Sprint Goal, users can export
                a month of spending as CSV. What: the Developers look at their capacity, Sam is away three days,
                and select five items with Priya, leaving a sixth out. How: the Developers break the five into
                tasks of a day or less, such as add the export endpoint and write the share-sheet test. The goal,
                the five items and the tasks form the Sprint Backlog.
            """),
            B.faded(
                "psm-sprint-planning-f1",
                "Next Sprint, Priya says users keep missing bills. Plan it the same way.",
                listOf(
                    "Why: the whole Scrum Team crafts the Sprint Goal, users get a reminder before each bill is due.",
                    "What: the Developers check their capacity and the Definition of Done, then select the items with Priya.",
                    "How: the Developers break each item into tasks of a day or less; the goal, the items and this plan form the Sprint ___.",
                ),
                blank = 2,
                answers = listOf("Backlog"),
                note = "The Sprint Backlog is the Sprint Goal (why), the selected items (what) and the plan (how).",
            ),
            B.mistakes(
                "Letting the Product Owner set the Sprint Goal alone; the whole Scrum Team crafts it.",
                "Letting the Product Owner or a manager decide how many items the Developers take on.",
                "Leaving Planning without a Sprint Goal, planning to agree one later in the Sprint.",
                "Treating eight hours as the length for every Sprint; it is the maximum for a one-month Sprint.",
                "Thinking only the Scrum Team may attend; others can be invited to give advice.",
            ),
            B.example("At Pocketwise's Planning, Priya proposes the value, the whole team crafts the export goal, the Developers choose five items for their real capacity, and they break those into day-sized tasks."),
            B.h("Check yourself"),
            B.match(
                "psm-x-sprint-planning-match", "Pair each Planning topic with its output.",
                "Why" to "The Sprint Goal",
                "What" to "The selected Product Backlog items",
                "How" to "The plan for delivering them",
            ),
            B.gap(
                "psm-x-sprint-planning-gap1", "The timebox for a one-month Sprint.",
                "Sprint Planning is timeboxed to a maximum of ___ hours for a one-month Sprint.", "8", "eight",
            ),
            B.tryIt("Try it this week", "Use your team's last or next planning session.",
                "Write the last Sprint Goal in one sentence, or notice there was none",
                "Note who decided how many items went in",
                "Check whether the selected items were broken into a plan, and by whom",
                "Read the Sprint Planning section of the Scrum Guide",
            ),
            B.explain(
                "psm-sprint-planning-x1",
                "Explain how Sprint Planning works and what it produces, using an Android team's Sprint as the example.",
                "Sprint Planning answers why, what and how. The Product Owner proposes how the product could gain value and the whole Scrum Team crafts the Sprint Goal. The Developers select the items with the Product Owner, based on their capacity, and then plan how to deliver them. The Sprint Goal, the selected items and the plan together form the Sprint Backlog, within eight hours for a one-month Sprint.",
                "Why: the Sprint Goal by the whole team" to listOf("sprint goal", "whole scrum team"),
                "What: Developers select items" to listOf("select", "capacity"),
                "How: the Developers' plan" to listOf("plan how", "the plan", "deliver"),
                "Output: the Sprint Backlog" to listOf("sprint backlog", "together form"),
            ),
            B.glossary(
                "Sprint Planning" to "The event that starts the Sprint by laying out why, what and how.",
                "Sprint Goal" to "The single objective for the Sprint, crafted by the whole Scrum Team.",
                "Sprint Backlog" to "The Sprint Goal, the selected items and the Developers' plan.",
                "Capacity" to "How much time and skill the Developers have available in the coming Sprint.",
                "Timebox" to "The maximum length of an event: eight hours here for a one-month Sprint.",
            ),
            B.recap(
                "Three topics: why (Sprint Goal, crafted by the whole team), what (Developers select items with the Product Owner), how (the Developers' plan).",
                "The Developers decide how much they can do; others may be invited to advise.",
                "Goal, items and plan form the Sprint Backlog; at most eight hours for a one-month Sprint.",
            ),
        ),
    )

    private val dailyScrum = Topic(
        id = "psm-daily-scrum", title = "The Daily Scrum: fifteen minutes for the Developers", minutes = 18,
        references = listOf(
            B.ref("The Scrum Guide (2020): Daily Scrum", SG),
            B.ref("What is a Daily Scrum? (Scrum.org)", ORG + "what-is-a-daily-scrum"),
            B.ref("Scrum Myths: Daily Scrum is not a status meeting (Scrum.org blog)", ORG + "blog/scrum-myths-daily-scrum-not-status-meeting"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
            B.ref("Scrum Guide revisions: the three questions removed", REV),
        ),
        blocks = listOf(
            B.warmup(
                "psm-daily-scrum-w1", "psm-daily-scrum",
                "Who is the Daily Scrum for?",
                listOf("The Developers", "The Scrum Master", "The stakeholders"), 0,
                "It is about the plan for the next day of work.",
            ),
            B.plain("""
                The Daily Scrum is a 15-minute event for the Developers. Its purpose is to inspect progress toward
                the Sprint Goal and adapt the Sprint Backlog, the plan, for the next day of work. It happens at the
                same time and place every working day of the Sprint, so nobody has to arrange it. Picture the
                Pocketwise Developers by the board at half past nine: what is in the way of the export, and what
                do we change today?
            """),
            B.diagram(
                caption = "Each day the Developers inspect progress toward the Sprint Goal and adapt the Sprint Backlog, producing a plan for the next day.",
                nodes = listOf(
                    DNode("goal", "Sprint Goal", 0.5f, 0.12f, w = 0.34f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    DNode("insp", "Inspect", 0.18f, 0.5f, w = 0.3f, h = 0.2f, sub = "progress"),
                    DNode("adapt", "Adapt", 0.82f, 0.5f, w = 0.3f, h = 0.2f, sub = "Sprint Backlog"),
                    DNode("day", "Next 24 hours", 0.5f, 0.85f, w = 0.32f, h = 0.2f, tone = 2, sub = "actionable plan"),
                ),
                edges = listOf(
                    DEdge("goal", "insp", label = "toward"),
                    DEdge("insp", "adapt"),
                    DEdge("adapt", "day"),
                    DEdge("day", "insp", label = "tomorrow", dashed = true),
                ),
                aspect = 0.75f,
            ),
            B.h("Who takes part"),
            B.p("""
                The Daily Scrum belongs to the Developers. If the Product Owner or the Scrum Master is actively
                working on items in the Sprint Backlog, they take part as Developers. The Scrum Master makes sure
                the event happens and stays within its 15 minutes, but does not run it or collect status. The
                Developers can pick any structure and technique they like, as long as it focuses on progress toward
                the Sprint Goal and ends with a plan for the next day of work.
            """),
            B.check(
                "psm-daily-scrum-k1", "psm-daily-scrum",
                "Tom, the Scrum Master, is not working on any Sprint Backlog items. What is his part in the Daily Scrum?",
                listOf(
                    "He makes sure it takes place and stays within 15 minutes; it is the Developers' event",
                    "He runs it and records each person's status",
                    "He must attend as a Developer",
                ), 0,
                "The Scrum Master ensures the events take place and stay within the timebox; the Daily Scrum is for the Developers.",
                whyWrong = mapOf(
                    1 to "The Daily Scrum is not a status meeting for the Scrum Master.",
                    2 to "He takes part as a Developer only when he is actively working on Sprint Backlog items.",
                ),
            ),
            B.h("No fixed questions, and not the only time to plan"),
            B.p("""
                Older versions of the Guide suggested three questions: what did I do yesterday, what will I do
                today, what is in my way. The 2020 Guide removed them; the Developers choose how to run it. The
                Daily Scrum improves communication, surfaces impediments, speeds up decisions and so removes the
                need for other meetings. It is not the only time the Developers change their plan; they often meet
                during the day to work things out in detail. The 15 minutes is the same whatever the Sprint length.
            """),
            B.check(
                "psm-daily-scrum-k2", "psm-daily-scrum",
                "Must the Developers answer three questions at the Daily Scrum?",
                listOf(
                    "No; they choose any structure that focuses on progress toward the Sprint Goal",
                    "Yes; the three questions are required",
                    "Only if the Scrum Master asks them to",
                ), 0,
                "The 2020 Guide removed the three questions. The Developers choose the structure and techniques.",
                whyWrong = mapOf(
                    1 to "The three questions were in older Guides; the 2020 Guide does not require them.",
                    2 to "The Scrum Master does not set the Daily Scrum's format; the Developers do.",
                ),
            ),
            B.h("Changing the plan any time"),
            B.p("""
                Because the Daily Scrum is about adapting the plan, a team that waits until the next morning to
                react to a problem has misunderstood it. If a Developer finds at two in the afternoon that a
                library upgrade breaks the build, the Developers can re-plan right then. Stakeholders who want
                progress get it at the Sprint Review, and from a transparent Sprint Backlog; the Daily Scrum is not
                their status meeting.
            """),
            B.check(
                "psm-daily-scrum-k3", "psm-daily-scrum",
                "During the Sprint, a Developer finds at 2 p.m. that a library upgrade breaks the release build. When can the Developers change their plan?",
                listOf("Right away; the Daily Scrum is not the only time to adapt", "Only at the next Daily Scrum", "Only at the Sprint Retrospective"), 0,
                "Developers adjust their plan whenever they learn something; the Daily Scrum is one regular point, not the only one.",
                whyWrong = mapOf(
                    1 to "Waiting a day to adapt wastes time; the Guide says it is not the only time plans are adjusted.",
                    2 to "The Retrospective improves how the team works; it is not where the Sprint plan is fixed.",
                ),
            ),
            B.worked("""
                Pocketwise's Daily Scrum is at 9:30 every working day by the board. Today Dana says the export
                endpoint is slow with a full month of data. Inspect: the Developers look at the board and see this
                puts the Sprint Goal at risk. Adapt: Marcus and Dana agree to pair on paging after the meeting, and
                Sam takes over Dana's test task. Plan: they leave with a clear plan for the next day. Time: done in
                twelve minutes. Priya is coding a small item this Sprint, so she joins as a Developer.
            """),
            B.faded(
                "psm-daily-scrum-f1",
                "At the next morning's Daily Scrum, Sam reports that the Play Store review rejected the build for a missing privacy label.",
                listOf(
                    "Inspect: the Developers see the rejection blocks the Sprint Goal of shipping the export.",
                    "Adapt: they update the Sprint Backlog, putting the privacy label task first.",
                    "They finish within ___ minutes with a plan for the next day of work.",
                ),
                blank = 2,
                answers = listOf("15", "fifteen"),
                note = "The Daily Scrum is timeboxed to 15 minutes whatever the Sprint length.",
            ),
            B.mistakes(
                "Running the Daily Scrum as a status report to the Scrum Master, the Product Owner or a manager.",
                "Requiring the three questions; the 2020 Guide removed them and the Developers choose the format.",
                "Waiting until the next Daily Scrum to react to a problem found during the day.",
                "Scaling the 15 minutes with the Sprint length; it is 15 minutes for every Sprint length.",
                "Having the Scrum Master lead the event when it belongs to the Developers.",
            ),
            B.example("Pocketwise's Developers spend twelve minutes at the board, see the export is at risk, re-pair two people and leave with a plan for the day; Priya joins only because she is coding a Sprint item."),
            B.h("Check yourself"),
            B.match(
                "psm-x-daily-scrum-match", "Pair each question with its answer.",
                "How long?" to "15 minutes",
                "Who is it for?" to "The Developers",
                "What is inspected?" to "Progress toward the Sprint Goal",
                "What is adapted?" to "The Sprint Backlog",
            ),
            B.gap(
                "psm-x-daily-scrum-gap1", "When the Daily Scrum happens.",
                "It is held at the same time and ___ every working day of the Sprint.", "place", "Place",
            ),
            B.tryIt("Try it this week", "Watch your own team's daily meeting.",
                "Time it, and note who speaks to whom",
                "Notice whether it talks about the Sprint Goal or only about individual tasks",
                "Count the plan changes made during the meeting",
                "Read the Daily Scrum section of the Scrum Guide",
            ),
            B.explain(
                "psm-daily-scrum-x1",
                "Explain the purpose and rules of the Daily Scrum, using an Android team as the example.",
                "The Daily Scrum is a 15-minute event for the Developers, held at the same time and place each working day. They inspect progress toward the Sprint Goal and adapt the Sprint Backlog, leaving with a plan for the next day. They choose its structure, since the three questions were removed. The Product Owner or Scrum Master joins as a Developer only when working on Sprint Backlog items, and the plan can change at any time.",
                "15 minutes, same time and place" to listOf("15 minutes", "fifteen minutes", "same time"),
                "For the Developers" to listOf("for the developers", "developers"),
                "Inspect progress, adapt the plan" to listOf("sprint goal", "adapt", "inspect"),
                "Structure is theirs, not a status meeting" to listOf("structure", "three questions", "status"),
            ),
            B.glossary(
                "Daily Scrum" to "A 15-minute event for the Developers to inspect progress toward the Sprint Goal and adapt the plan.",
                "Impediment" to "Anything blocking progress that the team cannot easily remove itself.",
                "Actionable plan" to "A plan clear enough that each person knows what to do next.",
                "Sprint Backlog" to "The Developers' plan for the Sprint, adapted every day.",
            ),
            B.recap(
                "15 minutes, same time and place, every working day; for the Developers.",
                "Inspect progress toward the Sprint Goal and adapt the Sprint Backlog; the Developers choose the format.",
                "Not a status meeting; the Product Owner or Scrum Master joins as a Developer only when working on Sprint Backlog items; plans can change any time.",
            ),
        ),
    )

    private val reviewRetro = Topic(
        id = "psm-review-retro", title = "Sprint Review and Sprint Retrospective", minutes = 25,
        references = listOf(
            B.ref("The Scrum Guide (2020): Sprint Review and Sprint Retrospective", SG),
            B.ref("What is a Sprint Review? (Scrum.org)", ORG + "what-is-a-sprint-review"),
            B.ref("What is a Sprint Retrospective? (Scrum.org)", ORG + "what-is-a-sprint-retrospective"),
            B.ref("How the Scrum Values direct your behaviour in the Retrospective (Scrum.org blog)", ORG + "blog/how-scrum-values-direct-your-behavior-during-sprint-retrospective-37"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
        ),
        blocks = listOf(
            B.warmup(
                "psm-review-retro-w1", "psm-review-retro",
                "Is the Sprint Review the moment the Product Owner decides whether the work may be released?",
                listOf(
                    "No; it is a working session to inspect the outcome and decide what to do next",
                    "Yes; nothing ships before the Review",
                    "Yes, but only for major features",
                ), 0,
                "The Guide says something specific about releasing value before the Review.",
            ),
            B.plain("""
                The Sprint ends with two events. The Sprint Review looks at the product: what was built and what to
                do next. The Sprint Retrospective looks at the team's way of working: how to get better. In
                Android terms, the Review is about the app; the Retrospective is about the team that builds the app.
                The Review comes first and the Retrospective concludes the Sprint.
            """),
            B.diagram(
                caption = "The Sprint Review inspects the Increment with stakeholders and adjusts the Product Backlog; the Retrospective, which ends the Sprint, plans improvements.",
                nodes = listOf(
                    DNode("inc", "Increment", 0.15f, 0.3f, w = 0.26f, h = 0.2f, sub = "Done work"),
                    DNode("rev", "Sprint Review", 0.5f, 0.3f, w = 0.3f, h = 0.2f, tone = 1, sub = "with stakeholders"),
                    DNode("pb", "Product Backlog", 0.83f, 0.3f, w = 0.32f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "adjusted"),
                    DNode("note", "Review 4h, Retro 3h max", 0.17f, 0.75f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    DNode("retro", "Retrospective", 0.5f, 0.75f, w = 0.34f, h = 0.2f, tone = 1, sub = "the Scrum Team"),
                    DNode("imp", "Improvements", 0.84f, 0.75f, w = 0.28f, h = 0.2f, sub = "next Sprint"),
                ),
                edges = listOf(
                    DEdge("inc", "rev"),
                    DEdge("rev", "pb"),
                    DEdge("rev", "retro", label = "then"),
                    DEdge("retro", "imp"),
                ),
                aspect = 0.7f,
            ),
            B.h("The Sprint Review: inspect the outcome"),
            B.p("""
                The Sprint Review inspects the outcome of the Sprint and decides future adaptations. The Scrum Team
                presents the results of its work to key stakeholders, the people with an interest in the product,
                and they discuss progress toward the Product Goal. They look at what was done and what has changed
                around them, such as a new competitor or a new Android version, and work out together what to do
                next. The Product Backlog may be adjusted. The Guide says it is a working session and should not be
                limited to a presentation. It is timeboxed to four hours for a one-month Sprint.
            """),
            B.p("""
                The Review is not a gate for releasing value. An Increment may be delivered to users before the
                Sprint ends, even several times a Sprint, and the Product Owner does not use the Review to accept
                or reject items one by one: an item is part of the Increment when it meets the Definition of Done.
            """),
            B.check(
                "psm-review-retro-k1", "psm-review-retro",
                "The Pocketwise Developers finished the CSV export on day six and it meets the Definition of Done. Can it go to the Play Store before the Sprint Review?",
                listOf(
                    "Yes; an Increment may be delivered before the Sprint ends, and the Review is not a release gate",
                    "No; nothing may be released until the Sprint Review approves it",
                    "Only if the Scrum Master agrees",
                ), 0,
                "The Guide says the Sprint Review should never be considered a gate to releasing value.",
                whyWrong = mapOf(
                    1 to "The Review inspects the outcome and adapts the backlog; it does not approve releases.",
                    2 to "Releasing is a product decision, not a Scrum Master approval.",
                ),
            ),
            B.h("The Sprint Retrospective: improve the team"),
            B.p("""
                The Sprint Retrospective plans ways to increase quality and effectiveness. The Scrum Team inspects
                how the last Sprint went: individuals, interactions, processes, tools and the Definition of Done.
                They talk about what went well, what problems came up and how those were, or were not, solved, and
                look for assumptions that led them astray. They pick the most helpful changes. The most impactful
                improvements are tackled as soon as possible and may even be added to the Sprint Backlog for the
                next Sprint. It is timeboxed to three hours for a one-month Sprint, and it concludes the Sprint.
            """),
            B.compare("Review or Retrospective?",
                B.card("Sprint Review", "The question is about the product: what was built, and what should come next.", "Scrum Team and key stakeholders", "Inspects the Increment and progress toward the Product Goal", "Adapts the Product Backlog", "Up to 4 hours for a one-month Sprint"),
                B.card("Sprint Retrospective", "The question is about how the team works: people, process, tools, the Definition of Done.", "The Scrum Team", "Inspects how the last Sprint went", "Plans improvements, maybe into the next Sprint Backlog", "Up to 3 hours; it ends the Sprint"),
            ),
            B.check(
                "psm-review-retro-k2", "psm-review-retro",
                "In the Retrospective the team agrees that every feature must be tested on a real low-end phone. Where can that change land?",
                listOf(
                    "In the Definition of Done, and if impactful, in the next Sprint Backlog",
                    "Nowhere until the Product Owner approves it at the next Sprint Review",
                    "Only in a separate improvement Sprint",
                ), 0,
                "The Retrospective inspects the Definition of Done among other things, and the most impactful improvements may be added to the next Sprint Backlog.",
                whyWrong = mapOf(
                    1 to "The Retrospective is where the Scrum Team plans its own improvements; the Review is about the product.",
                    2 to "Scrum has no special improvement Sprints.",
                ),
            ),
            B.check(
                "psm-review-retro-k3", "psm-review-retro",
                "In which order do the last two events of a Sprint happen?",
                listOf(
                    "Sprint Review, then Sprint Retrospective",
                    "Sprint Retrospective, then Sprint Review",
                    "Either order, as the team prefers",
                ), 0,
                "The Sprint Review is the second-to-last event; the Retrospective concludes the Sprint.",
                whyWrong = mapOf(
                    1 to "The Retrospective comes last; it concludes the Sprint.",
                    2 to "The Guide fixes the order: Review first, Retrospective last.",
                ),
            ),
            B.worked("""
                Pocketwise ends a two-week Sprint. Review: the team shows the working CSV export on a phone to
                Priya's key stakeholders, the finance lead and two support agents. Support says users really want
                PDF. Together they discuss the Product Goal, and Priya adjusts the Product Backlog so PDF moves up.
                It takes under two hours. Retrospective: the Scrum Team sees that two bugs slipped through because
                nobody tested on a small screen. They add small-screen testing to the Definition of Done and put
                the change in the next Sprint Backlog. The Retrospective ends the Sprint.
            """),
            B.faded(
                "psm-review-retro-f1",
                "Next Sprint the budget alerts ship. At the Review, a stakeholder says alerts arrive too late in the day; in the Retro, the team notes code reviews took two days each.",
                listOf(
                    "Review: the team and stakeholders inspect the alerts on a phone and discuss progress toward the Product Goal.",
                    "Review: Priya adjusts the Product Backlog so an earlier alert time comes next.",
                    "Retrospective: the Scrum Team agrees to review pull requests within a day and adds that improvement to the next Sprint ___.",
                ),
                blank = 2,
                answers = listOf("Backlog"),
                note = "The most impactful improvements may be added to the Sprint Backlog for the next Sprint.",
            ),
            B.mistakes(
                "Treating the Sprint Review as a demo only, or as a gate where the Product Owner accepts each item.",
                "Holding back Done work until the Review, when the Guide says value can be delivered before the Sprint ends.",
                "Using the Retrospective to talk about features instead of how the team works.",
                "Running the Retrospective before the Review; the Retrospective concludes the Sprint.",
                "Treating four and three hours as fixed lengths; they are maximums for a one-month Sprint.",
            ),
            B.example("Pocketwise's Review turns a support comment into a reordered backlog with PDF export next, and its Retrospective adds small-screen testing to the Definition of Done for the next Sprint."),
            B.h("Check yourself"),
            B.match(
                "psm-x-review-retro-match", "Pair each item with the right event.",
                "Discuss progress toward the Product Goal with stakeholders" to "Sprint Review",
                "Inspect individuals, interactions, processes and tools" to "Sprint Retrospective",
                "Up to four hours for a one-month Sprint" to "Sprint Review",
                "Concludes the Sprint" to "Sprint Retrospective",
            ),
            B.gap(
                "psm-x-review-retro-gap1", "The Retrospective's timebox.",
                "The Sprint Retrospective is timeboxed to a maximum of ___ hours for a one-month Sprint.", "3", "three",
            ),
            B.gap(
                "psm-x-review-retro-gap2", "The Review's timebox.",
                "The Sprint Review is timeboxed to a maximum of ___ hours for a one-month Sprint.", "4", "four",
            ),
            B.tryIt("Try it this week", "Use your team's last Sprint, or the last release.",
                "Write down who attended the last review and whether any backlog item moved because of it",
                "List one improvement from the last retrospective and whether it actually happened",
                "Check whether your Definition of Done changed in the last three months",
                "Read the Sprint Review and Sprint Retrospective sections of the Scrum Guide",
            ),
            B.explain(
                "psm-review-retro-x1",
                "Explain the difference between the Sprint Review and the Sprint Retrospective, using an Android team as the example.",
                "The Sprint Review inspects the outcome of the Sprint: the Scrum Team and key stakeholders look at the Increment, discuss progress toward the Product Goal and adjust the Product Backlog. It is a working session, not a release gate, up to four hours for a one-month Sprint. The Sprint Retrospective then plans improvements to quality and effectiveness, inspecting people, processes, tools and the Definition of Done, up to three hours, and it concludes the Sprint.",
                "Review: inspect the Increment with stakeholders" to listOf("stakeholders", "increment"),
                "Review adapts the Product Backlog, not a gate" to listOf("product backlog", "not a release gate", "not a gate"),
                "Retro: improve quality and effectiveness" to listOf("effectiveness", "improvements", "processes"),
                "Order and timeboxes" to listOf("concludes", "four hours", "three hours"),
            ),
            B.glossary(
                "Sprint Review" to "The event where the Scrum Team and stakeholders inspect the Sprint's outcome and adapt the Product Backlog.",
                "Sprint Retrospective" to "The event where the Scrum Team plans ways to improve quality and effectiveness; it ends the Sprint.",
                "Stakeholder" to "Someone outside the Scrum Team with an interest in the product.",
                "Increment" to "A usable step toward the Product Goal that meets the Definition of Done.",
                "Definition of Done" to "The formal description of the quality the Increment must meet; the Retrospective may improve it.",
            ),
            B.recap(
                "Review: inspect the Increment with key stakeholders, discuss the Product Goal, adjust the Product Backlog; a working session, not a release gate; up to 4 hours.",
                "Retrospective: inspect individuals, interactions, processes, tools and the Definition of Done; plan improvements; up to 3 hours.",
                "Review comes first; the Retrospective concludes the Sprint, and impactful improvements can go into the next Sprint Backlog.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(sprint, sprintPlanning, dailyScrum, reviewRetro)

    val quiz: Quiz = Quiz(
        id = "psm-events-test", title = "Module test",
        questions = B.spread(
            listOf(
                B.q(
                    "psm-sprint-q1", "psm-sprint",
                    "What is the maximum length of a Sprint?",
                    listOf("One month", "Two weeks", "Six weeks", "Whatever the team decides"), setOf(0),
                    "Sprints are fixed-length events of one month or less.",
                    whyWrong = mapOf(
                        1 to "Two weeks is a common choice, not the rule; the maximum is one month.",
                        2 to "Six weeks is longer than the one-month maximum.",
                        3 to "The team chooses the length, but within one month or less.",
                    ),
                ),
                B.q(
                    "psm-sprint-q2", "psm-sprint",
                    "During the Sprint, the Developers realise they will not finish all the selected items, though the Sprint Goal is still within reach. What should they do?",
                    listOf(
                        "Work with the Product Owner to renegotiate the scope, keeping the Sprint Goal",
                        "Extend the Sprint by a few days",
                        "Lower the Definition of Done for the remaining items",
                        "Ask the Scrum Master to cancel the Sprint",
                    ), setOf(0),
                    "Scope may be clarified and renegotiated with the Product Owner as more is learned; the Sprint Goal and quality stay.",
                    whyWrong = mapOf(
                        1 to "Sprint length is fixed; it is never extended to finish work.",
                        2 to "Quality does not decrease during the Sprint.",
                        3 to "Only the Product Owner can cancel, and only if the Sprint Goal is obsolete.",
                    ),
                ),
                B.q(
                    "psm-sprint-q3", "psm-sprint",
                    "A new organisation adopting Scrum wants a Sprint 0 to set up the architecture before any product work. What is the best answer?",
                    listOf(
                        "Scrum has no Sprint 0; every Sprint should deliver a valuable, useful Increment",
                        "Sprint 0 is required before the first real Sprint",
                        "Sprint 0 is allowed if it is shorter than a normal Sprint",
                    ), setOf(0),
                    "The Guide has no special-purpose Sprints. Set-up work happens alongside building a usable Increment.",
                    whyWrong = mapOf(
                        1 to "The Scrum Guide does not mention any Sprint 0.",
                        2 to "Length is not the issue; a Sprint without a usable Increment is not how Scrum works.",
                    ),
                ),
                B.q(
                    "psm-sprint-q4", "psm-sprint",
                    "When does the next Sprint begin?",
                    listOf(
                        "Immediately after the previous Sprint ends",
                        "After a planning week between Sprints",
                        "When the Product Owner has refined enough items",
                    ), setOf(0),
                    "A new Sprint starts immediately after the conclusion of the previous Sprint.",
                    whyWrong = mapOf(
                        1 to "There are no gaps between Sprints.",
                        2 to "Refinement happens during Sprints; it does not delay the next one.",
                    ),
                ),
                B.q(
                    "psm-sprint-planning-q1", "psm-sprint-planning",
                    "Which three topics does Sprint Planning address?",
                    listOf(
                        "Why the Sprint is valuable, what can be Done, and how the work will get done",
                        "What was done yesterday, what will be done today, and what is blocking",
                        "What went well, what went wrong, and what to improve",
                        "The budget, the deadline and the scope",
                    ), setOf(0),
                    "Sprint Planning covers why (the Sprint Goal), what (selected items) and how (the plan).",
                    whyWrong = mapOf(
                        1 to "These are the old Daily Scrum questions, removed in 2020.",
                        2 to "This sounds like a Retrospective, not Sprint Planning.",
                        3 to "Scrum does not frame Planning around budget, deadline and scope.",
                    ),
                ),
                B.q(
                    "psm-sprint-planning-q2", "psm-sprint-planning",
                    "What is the timebox for Sprint Planning in a one-month Sprint?",
                    listOf("Eight hours", "Four hours", "Two hours", "One day plus a half-day of refinement"), setOf(0),
                    "Sprint Planning is timeboxed to a maximum of eight hours for a one-month Sprint, usually shorter for shorter Sprints.",
                    whyWrong = mapOf(
                        1 to "Four hours is the Sprint Review's maximum for a one-month Sprint.",
                        2 to "Two hours is not a timebox in the Guide.",
                        3 to "There is no such rule; the maximum is eight hours.",
                    ),
                ),
                B.q(
                    "psm-sprint-planning-q3", "psm-sprint-planning",
                    "At Sprint Planning, who selects the Product Backlog items for the Sprint?",
                    listOf(
                        "The Developers, in discussion with the Product Owner",
                        "The Product Owner, who then hands them to the Developers",
                        "The Scrum Master, based on velocity",
                    ), setOf(0),
                    "Through discussion with the Product Owner, the Developers select items from the Product Backlog.",
                    whyWrong = mapOf(
                        1 to "The Product Owner discusses the items but does not decide how much the Developers take.",
                        2 to "The Scrum Master does not select work, and velocity is not in the Scrum Guide.",
                    ),
                ),
                B.q(
                    "psm-sprint-planning-q4", "psm-sprint-planning",
                    "Who may attend Sprint Planning besides the Scrum Team?",
                    listOf(
                        "Other people the Scrum Team invites to provide advice",
                        "Nobody; it is closed to everyone else",
                        "Any manager who wants to set the Sprint scope",
                    ), setOf(0),
                    "The Scrum Team may invite other people to Sprint Planning to provide advice.",
                    whyWrong = mapOf(
                        1 to "The Guide explicitly allows inviting others for advice.",
                        2 to "Invited people advise; they do not set scope.",
                    ),
                ),
                B.q(
                    "psm-daily-scrum-q1", "psm-daily-scrum",
                    "What is the purpose of the Daily Scrum?",
                    listOf(
                        "To inspect progress toward the Sprint Goal and adapt the Sprint Backlog as necessary",
                        "To report status to the Scrum Master",
                        "To update stakeholders on progress",
                        "To refine Product Backlog items",
                    ), setOf(0),
                    "The Daily Scrum inspects progress toward the Sprint Goal and adapts the Sprint Backlog, adjusting the upcoming planned work.",
                    whyWrong = mapOf(
                        1 to "It is not a status meeting for the Scrum Master.",
                        2 to "Stakeholders see progress at the Sprint Review; the Daily Scrum is for the Developers.",
                        3 to "Refinement is an ongoing activity, not the purpose of the Daily Scrum.",
                    ),
                ),
                B.q(
                    "psm-daily-scrum-q2", "psm-daily-scrum",
                    "The team runs one-week Sprints. How long is their Daily Scrum timebox?",
                    listOf("15 minutes", "5 minutes", "30 minutes"), setOf(0),
                    "The Daily Scrum is a 15-minute event whatever the Sprint length.",
                    whyWrong = mapOf(
                        1 to "Only the bigger events scale with Sprint length; the Daily Scrum is always 15 minutes.",
                        2 to "30 minutes is over the timebox for any Sprint length.",
                    ),
                ),
                B.q(
                    "psm-daily-scrum-q3", "psm-daily-scrum",
                    "A department manager wants to attend each Daily Scrum to hear what everyone is doing. What is the best response from the Scrum Master?",
                    listOf(
                        "Help the manager understand the Daily Scrum is for the Developers to plan their day, and point to the Sprint Review and Sprint Backlog for progress",
                        "Let the manager attend and ask each Developer for a status update",
                        "Move the Daily Scrum to a weekly status meeting",
                    ), setOf(0),
                    "The Daily Scrum is the Developers' event. The Scrum Master coaches the organisation and points to the transparent artifacts and the Review.",
                    whyWrong = mapOf(
                        1 to "Turning it into a status report to a manager defeats its purpose.",
                        2 to "The Daily Scrum is every working day; changing it breaks Scrum.",
                    ),
                ),
                B.q(
                    "psm-daily-scrum-q4", "psm-daily-scrum",
                    "Which statements about the Daily Scrum are true? Choose two.",
                    listOf(
                        "The Developers can choose its structure and techniques",
                        "It is held at the same time and place every working day",
                        "The Developers must answer three set questions",
                        "The Scrum Master must facilitate it",
                    ), setOf(0, 1),
                    "Same time and place every working day, and the Developers choose how to run it.",
                    whyWrong = mapOf(
                        2 to "The three questions were removed in the 2020 Guide.",
                        3 to "The Scrum Master ensures it happens; it is the Developers' event.",
                    ),
                ),
                B.q(
                    "psm-review-retro-q1", "psm-review-retro",
                    "Which best describes the Sprint Review?",
                    listOf(
                        "A working session where the Scrum Team and stakeholders inspect the outcome and decide what to do next",
                        "A demo where the Product Owner accepts or rejects each item",
                        "A gate that must be passed before anything is released",
                        "A meeting where the team improves its processes",
                    ), setOf(0),
                    "The Sprint Review inspects the outcome of the Sprint and determines future adaptations; it is a working session, not merely a presentation or a gate.",
                    whyWrong = mapOf(
                        1 to "The Review is not a presentation for acceptance; Done is decided by the Definition of Done.",
                        2 to "The Guide says the Review should never be considered a gate to releasing value.",
                        3 to "Improving processes is the Retrospective's job.",
                    ),
                ),
                B.q(
                    "psm-review-retro-q2", "psm-review-retro",
                    "What is inspected in the Sprint Retrospective?",
                    listOf(
                        "How the last Sprint went regarding individuals, interactions, processes, tools and the Definition of Done",
                        "The Increment and progress toward the Product Goal",
                        "Each Developer's individual performance",
                    ), setOf(0),
                    "The Retrospective inspects how the Sprint went and plans ways to increase quality and effectiveness.",
                    whyWrong = mapOf(
                        1 to "That is the Sprint Review's focus.",
                        2 to "The Retrospective looks at the team's way of working, not individual appraisals.",
                    ),
                ),
                B.q(
                    "psm-review-retro-q3", "psm-review-retro",
                    "What are the timeboxes for the Sprint Review and Sprint Retrospective in a one-month Sprint?",
                    listOf(
                        "Four hours for the Review, three hours for the Retrospective",
                        "Three hours for the Review, four hours for the Retrospective",
                        "Two hours each",
                        "Eight hours for the Review, four for the Retrospective",
                    ), setOf(0),
                    "For a one-month Sprint the Review is at most four hours and the Retrospective at most three; shorter Sprints usually take less.",
                    whyWrong = mapOf(
                        1 to "The numbers are swapped: Review four, Retrospective three.",
                        2 to "Two hours is not the Guide's timebox for either.",
                        3 to "Eight hours is Sprint Planning's maximum.",
                    ),
                ),
                B.q(
                    "psm-review-retro-q4", "psm-review-retro",
                    "The Retrospective identifies a highly impactful improvement. What does the Scrum Guide say about it?",
                    listOf(
                        "It is addressed as soon as possible and may be added to the Sprint Backlog for the next Sprint",
                        "It must wait for the Product Owner's approval at the next Sprint Review",
                        "It goes to the organisation's change board",
                    ), setOf(0),
                    "The most impactful improvements are addressed as soon as possible and may even be added to the next Sprint Backlog.",
                    whyWrong = mapOf(
                        1 to "The Review is about the product, and improvements do not need its approval.",
                        2 to "The Scrum Team is self-managing; there is no change board in the Guide.",
                    ),
                ),
            ),
        ),
    )
}
