package com.shahaabapps.core.content

/** The Daily Scrum: fifteen minutes for the Developers: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesDailyScrum {
    private val classic = Podcast(
        "psm-daily-scrum",
        listOf(
            maya("Hi, and welcome back. Today: the Daily Scrum. Fifteen minutes, every working day of the Sprint, and probably the most misunderstood event in Scrum."),
            leo("Misunderstood how? It's the standup. Everyone says what they did yesterday, what they'll do today, and any blockers. Done."),
            maya("[chuckle] That's exactly the picture we'll take apart. The Scrum Guide, the short free document that defines Scrum, says the Daily Scrum's purpose is to inspect progress toward the Sprint Goal, and adapt the Sprint Backlog as necessary, adjusting the upcoming planned work."),
            leo("Slower. Inspect progress toward the Sprint Goal. What does that mean, in plain words?"),
            maya("Picture a small app team building an Android running app. Their Sprint Goal, the single objective for the Sprint, is: runners can record a run with no signal, and it syncs later. Each morning they look at that goal and ask: are we on track, and what should change in our plan?"),
            leo("And adapt the Sprint Backlog. That's the plan?"),
            maya("Yes. The Sprint Backlog is the Sprint Goal, the items picked for the Sprint, and the Developers' plan for delivering them. The Daily Scrum is where that plan gets updated, every day. Maybe the sync work turned out bigger, so two people pair on it and the settings screen waits."),
            leo("Who's it for? The manager? The Scrum Master?"),
            maya("It's an event for the Developers, the people doing the work of the Sprint. Not a report to the Scrum Master, not a status update for a manager. If the Product Owner or the Scrum Master are actively working on items in the Sprint Backlog, they take part as Developers."),
            leo("Fifteen minutes exactly?"),
            maya("Fifteen minutes is the timebox: the maximum length, like a parking meter. It doesn't grow for a longer Sprint. And it's held at the same time and place every working day, which the Guide says reduces complexity: nobody has to wonder when or where."),
            leo("Now my real doubt. My team does the three questions. Yesterday, today, blockers. Is that wrong?"),
            maya("Not wrong, but not required. The three questions were in older versions of the Scrum Guide, and the 2020 version removed them. Now the Developers can pick whatever structure and techniques they want."),
            leo("Any structure at all?"),
            maya("As long as it does two things: it focuses on progress toward the Sprint Goal, and it produces an actionable plan for the next day of work. Actionable means concrete enough to act on: who pairs with whom, what gets tested first."),
            leo("Give me another structure, then."),
            maya("For example, walking the board. Instead of going person by person, the team goes item by item through the Sprint Backlog, starting with whatever is closest to Done. The question becomes: what will it take to finish this? That keeps the talk on the goal, not on who looks busy."),
            leo("I like that. Person by person always turned into everyone proving they'd worked hard."),
            maya("[laugh] It does. And that's why the Guide's wording matters. It's about the goal and the plan, not about accounting for your hours. It also creates focus and improves self-management: the Developers steering their own work."),
            leo("What does the team get out of it, beyond the plan?"),
            maya("The Guide lists four things: it improves communication, identifies impediments, promotes quick decision-making, and so removes the need for other meetings. An impediment is anything blocking progress that the team can't easily clear itself."),
            leo("And when an impediment comes up, do we solve it right there?"),
            maya("Usually not inside the fifteen minutes. Say the Play Store keeps rejecting the build. The Daily Scrum spots it; the two people who can fix it meet straight after. If it's bigger than the team, the Scrum Master helps cause its removal."),
            leo("So the Daily Scrum isn't the only time we talk about the plan."),
            maya("Exactly, and the exam tests it. The Guide says it's not the only time the Developers adjust their plan. They often meet during the day for more detailed discussions, to adapt or re-plan the rest of the Sprint's work."),
            leo("And what does the Scrum Master do in it?"),
            maya("The Scrum Master ensures it happens, and that it's positive, productive and inside the timebox. That doesn't mean chairing it, or even attending. A Scrum Master who runs it as a status meeting has turned the Developers' event into their own."),
            leo("What if the whole team works remotely?"),
            maya("Same rules. A video call at the same time each day works fine. What matters is the purpose: inspect progress toward the Sprint Goal, adapt the plan, inside fifteen minutes. The Sprint Backlog episode covers the plan itself."),
            leo("So what do I remember for the exam?"),
            maya("The Daily Scrum's timebox: fifteen minutes, same time and place every working day. Who it's for: the Developers. Its purpose: inspect progress toward the Sprint Goal and adapt the Sprint Backlog. The three questions: not required since 2020; the Developers choose the structure. And it's not the only time they adjust the plan."),
            leo("And something to try today?"),
            maya("Time your next Daily Scrum, and note who speaks to whom. Notice whether it talks about the Sprint Goal or only about individual tasks, and count the plan changes made during the meeting. Then read the Daily Scrum section of the Scrum Guide, and try walking the board next time."),
            leo("Fifteen minutes, eyes on the goal. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-daily-scrum",
        listOf(
            maya("Welcome to Case Files. Today's case: The Standup That Ate the Morning. A team builds an Android running app. Their Daily Scrum now runs forty-five minutes. The Developers dread it. And a sync bug was mentioned there four days running, yet nobody fixed it."),
            leo("[chuckle] Detective Leo, on the case. First suspect: they're not strict enough with the three questions."),
            maya("[pause] Wrong suspect. The three questions, yesterday, today, blockers, were removed from the Scrum Guide in 2020. The Guide, the short free document that defines Scrum, now lets the Developers choose any structure. So the questions can't be the cure. Let's look at the purpose."),
            leo("Which is?"),
            maya("To inspect progress toward the Sprint Goal, and adapt the Sprint Backlog as necessary, adjusting the upcoming planned work. The Sprint Goal is the single objective for the Sprint. The Sprint Backlog is that goal, the selected items, and the Developers' plan."),
            leo("And this team's goal?"),
            maya("Runners can record a run with no signal, and it syncs later. First exhibit: the transcript of Tuesday's Daily Scrum. Forty-five minutes. The Sprint Goal is mentioned zero times."),
            leo("[sigh] So what filled the forty-five minutes?"),
            maya("Each Developer, one by one, reporting hours to a manager in the corner. Second exhibit: the manager. He asks the questions, and everyone answers to him."),
            leo("Second suspect, then: the manager. But surely managers are allowed to know progress?"),
            maya("They can learn progress elsewhere, like at the Sprint Review. The Daily Scrum is an event for the Developers. If the Product Owner or Scrum Master are working on Sprint Backlog items, they join as Developers. It's not a status report to anyone; that turns the Developers into an audience."),
            leo("Under the picture, then. What should the fifteen minutes produce?"),
            maya("Two things. A focus on progress toward the Sprint Goal, and an actionable plan for the next day of work: concrete enough to act on. Who pairs on the sync bug, what gets tested first. That's how it creates focus and improves self-management."),
            leo("Does the Guide say what the plan has to look like?"),
            maya("No. The Developers choose the structure and techniques. The plan just has to be actionable, and about the goal. Some teams use a board, some a list; the Guide doesn't mind which."),
            leo("Third exhibit?"),
            maya("The calendar. The Daily Scrum moved every day: nine o'clock, then eleven, then after lunch. The Guide says same time and same place every working day, to reduce complexity. Here, half the team missed it each time."),
            leo("And the timebox, the maximum length?"),
            maya("Fifteen minutes, like a parking meter. Fourth exhibit: the sync bug. Marcus raised it on Monday. Then the group spent twenty minutes debugging it, live, with six people watching."),
            leo("Isn't solving it right there efficient?"),
            maya("It feels efficient. The Guide says the Daily Scrum identifies impediments, things blocking progress that the team can't easily clear, and promotes quick decision-making. The decision is who fixes it. Then those two meet straight after. Here, the debugging ran over, the meeting ended in a rush, and nobody owned the bug."),
            leo("So they can meet again after the Daily Scrum? That's allowed?"),
            maya("Encouraged. The Guide says the Daily Scrum isn't the only time the Developers adjust their plan; they often meet during the day for more detailed discussions. Where it stops: big impediments go to the Scrum Master, who helps cause their removal."),
            leo("And the Scrum Master? Where was he?"),
            maya("Watching. The Scrum Master ensures the event happens, is positive and productive, and stays inside its timebox. He doesn't have to chair it, or attend. But he should have coached the team out of a status meeting weeks ago."),
            leo("What did they change, in the end?"),
            maya("Same time every morning, nine fifteen, at the team board or on the same video call. The manager reads the Increment at the Review instead. And they walk the board: item by item through the Sprint Backlog, closest to Done first. Eleven minutes, and the sync bug had owners by nine thirty. The Sprint Backlog episode follows that plan."),
            leo("Does any of this break when the team is remote?"),
            maya("No. A video call at the same time every day keeps the rhythm. What breaks it is cameras off and people reporting to a manager. The purpose is the same anywhere: inspect progress toward the Sprint Goal, adapt the plan, inside fifteen minutes."),
            leo("So, the case summary for the exam?"),
            maya("The Daily Scrum's timebox: fifteen minutes, same time and place every working day. Who it's for: the Developers. Its purpose: inspect progress toward the Sprint Goal and adapt the Sprint Backlog. The three questions: not required since 2020; the Developers choose the structure. And it's not the only time they adjust the plan."),
            leo("Case closed. Anything to try today?"),
            maya("Time your next Daily Scrum, and note who speaks to whom. Notice whether it talks about the Sprint Goal or only about individual tasks, and count the plan changes made during the meeting. Then read the Daily Scrum section of the Scrum Guide, and try walking the board next time."),
            leo("Fifteen minutes, then back to work. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-daily-scrum",
        listOf(
            maya("Okay, did you hear about the app team's Daily Scrum this week? It started at nine. People were still standing there at ten. Someone brought a chair."),
            leo("[laugh] A chair at a standup. That's the end of civilisation. Start me off. What is it meant to be?"),
            maya("The Scrum Guide, the short free document that defines Scrum, says it's a fifteen-minute event for the Developers. Its purpose is to inspect progress toward the Sprint Goal, and adapt the Sprint Backlog as necessary, adjusting the upcoming planned work."),
            leo("Sprint Goal and Sprint Backlog. Remind me."),
            maya("The Sprint Goal is the single objective for the Sprint: here, runners can record a run with no signal, and it syncs later. The Sprint Backlog is that goal, the items chosen for the Sprint, and the Developers' plan for them. Now, Monday. Tom."),
            leo("What did Tom do?"),
            maya("Tom decided daily was too often, and moved it to Mondays and Thursdays. Saves time, he said. But the Guide says every working day, same time, same place, to reduce complexity. Twice a week, the plan drifts for days before anyone notices."),
            leo("Twice a week sounds efficient, though."),
            maya("It sounds efficient. But a plan that's two days stale costs more than fifteen minutes a day. And the Guide is plain about it: the Daily Scrum is held every working day of the Sprint."),
            leo("Tuesday?"),
            maya("Tuesday was Dana, the Scrum Master. She stood at the front with a notebook, and each Developer reported to her: yesterday, today, blockers. Forty minutes."),
            leo("[sigh] A status meeting with a new name."),
            maya("Exactly. It's the Developers' event. The Scrum Master ensures it happens, and is positive, productive and inside its timebox, a maximum length, like a parking meter. She needn't attend at all. And it's never a report to her."),
            leo("But the three questions. Aren't they official?"),
            maya("They were, in older versions of the Guide. The 2020 version removed them. Now the Developers choose any structure and techniques they like, as long as it focuses on progress toward the Sprint Goal and produces an actionable plan for the next day: concrete enough to act on."),
            leo("Wednesday. Go."),
            maya("Wednesday was Marcus. He mentioned the sync bug, then the whole team debugged it, live, for twenty-five minutes. Six people watching one screen."),
            leo("Ouch. What should he have done?"),
            maya("Say it, decide who handles it, and move on. The Guide says the Daily Scrum identifies impediments, things blocking progress the team can't easily clear, and promotes quick decision-making. The two people who can fix it meet straight after. That's not cheating; the Guide says it's not the only time the Developers adjust their plan."),
            leo("Thursday?"),
            maya("Thursday was Priya, the Product Owner. She turned up with three new feature ideas and pitched them. She isn't working on any Sprint Backlog items, so she wasn't there as a Developer. New ideas go to the Product Backlog, and Sprint Planning."),
            leo("When can the Product Owner take part, then?"),
            maya("If the Product Owner or the Scrum Master are actively working on items in the Sprint Backlog, they take part as Developers. Otherwise, the Daily Scrum belongs to the people doing the work."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. The intern thought the Daily Scrum was the only time the team was allowed to talk about the plan, so he saved every question for the next morning. Twenty-three hours of waiting, every time."),
            leo("[chuckle] Bless him. Did anyone get it right this week?"),
            maya("The other team. Same time every morning, nine fifteen, at the team board, or the same video call when they're remote. They walk the board: item by item through the Sprint Backlog, closest to Done first. Eleven minutes, an actionable plan, done. The Sprint Backlog episode shows what that plan looks like."),
            leo("And what do they get out of it, beyond the plan?"),
            maya("The Guide lists it: better communication, impediments found early, quick decisions, and fewer other meetings. It also creates focus and improves self-management, the Developers steering their own work."),
            leo("And when the team's remote? Half of ours are."),
            maya("Same rules. The same video call at the same time each day; the place can be a link. The purpose doesn't change: inspect progress toward the Sprint Goal, adapt the Sprint Backlog, and finish inside fifteen minutes."),
            leo("So, the gossip I should remember for the exam?"),
            maya("The Daily Scrum's timebox: fifteen minutes, same time and place every working day. Who it's for: the Developers. Its purpose: inspect progress toward the Sprint Goal and adapt the Sprint Backlog. The three questions: not required since 2020; the Developers choose the structure. And it's not the only time they adjust the plan."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Time your next Daily Scrum, and note who speaks to whom. Notice whether it talks about the Sprint Goal or only about individual tasks, and count the plan changes made during the meeting. Then read the Daily Scrum section of the Scrum Guide, and try walking the board next time."),
            leo("Put the chair back, Marcus. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-daily-scrum",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight: the Daily Scrum. Fifteen minutes, every working day."),
            ivy("Fifteen minutes. Nova, that's the length of my good intentions."),
            nova("[chuckle] Then let's make them count. The Scrum Guide, the short free document that defines Scrum, says the Daily Scrum is there to inspect progress toward the Sprint Goal, and adapt the Sprint Backlog as necessary, adjusting the upcoming planned work."),
            ivy("Slowly. What's the Sprint Goal again?"),
            nova("The single objective for the Sprint. Picture a small team building an Android running app. Their goal: runners can record a run with no signal, and it syncs later. [pause] Each morning, they ask: are we closer to that? And what should change?"),
            ivy("And the Sprint Backlog is the plan."),
            nova("The goal, the items picked for the Sprint, and the Developers' plan for delivering them. The Daily Scrum is where that plan gets reshaped, a little, every day. Maybe sync is harder than they thought, so two people pair on it, and the settings screen waits."),
            ivy("Who gets to be there? Is it the boss's meeting?"),
            nova("It's for the Developers, the people doing the work. Not the boss's, not the Scrum Master's. If the Product Owner or the Scrum Master are actively working on Sprint Backlog items, they take part as Developers. Otherwise, [pause] it's not their room."),
            ivy("And when? Whenever everyone's awake?"),
            nova("Same time and place, every working day. The Guide says that reduces complexity: nobody wonders when or where. And it's timeboxed at fifteen minutes, a maximum length, like the last train home."),
            ivy("Fifteen minutes even in a long Sprint? A whole month?"),
            nova("[pause] Even then. The Daily Scrum's timebox doesn't grow with the Sprint. Planning, the Review and the Retrospective get longer for a one-month Sprint. The Daily Scrum stays at fifteen minutes, every day."),
            ivy("What about the three questions? Yesterday, today, blockers. I know them by heart."),
            nova("Then you can let them go. They were removed from the Guide in 2020. Now the Developers choose any structure they like, as long as it focuses on progress toward the Sprint Goal and ends with an actionable plan for the next day: concrete enough to act on."),
            ivy("Give me a gentler structure."),
            nova("Walking the board. Item by item through the Sprint Backlog, starting with whatever's closest to Done. [pause] What will it take to finish this one? No one has to prove they worked hard. The work speaks."),
            ivy("And the person-by-person way? Is that forbidden?"),
            nova("Not forbidden. The Developers may still use it, if it keeps the focus on the goal. It just tends to become everyone proving they've been busy, and that's a report, not a plan."),
            ivy("And what does all that give them?"),
            nova("Better communication. Impediments found early: an impediment is anything blocking progress the team can't easily clear itself. Quick decisions. And fewer other meetings. It creates focus, and improves self-management, the team steering its own work."),
            ivy("If something's broken, do they fix it there and then?"),
            nova("Usually not inside the fifteen minutes. Say the Play Store keeps rejecting the build. They name it, decide who takes it, and those two meet straight after. Bigger than the team? The Scrum Master helps cause its removal."),
            ivy("So they can talk about the plan later in the day, too?"),
            nova("[pause] As often as they need. The Guide says the Daily Scrum isn't the only time the Developers adjust their plan. They often meet during the day for more detailed talks, to adapt or re-plan the rest of the Sprint."),
            ivy("And the Scrum Master? Where are they, at nine in the morning?"),
            nova("Making sure the event happens, and is positive, productive, and inside its timebox. Not chairing it. Not collecting status. Maybe not even there. The Sprint Backlog episode is your next late night."),
            ivy("And if the whole team is remote, scattered across the night?"),
            nova("Same rules. The same video call, at the same time each day. The place can be a link. [pause] The purpose never changes: look at the goal, reshape the plan, and let the fifteen minutes end."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("The Daily Scrum's timebox: fifteen minutes, same time and place every working day. Who it's for: the Developers. Its purpose: inspect progress toward the Sprint Goal and adapt the Sprint Backlog. The three questions: not required since 2020; the Developers choose the structure. And it's not the only time they adjust the plan."),
            ivy("And something to try today, before I sleep?"),
            nova("Time your next Daily Scrum, and note who speaks to whom. [pause] Notice whether it talks about the Sprint Goal or only about individual tasks, and count the plan changes made during the meeting. Then read the Daily Scrum section of the Scrum Guide, and try walking the board next time."),
            ivy("Fifteen minutes, then let it go. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-daily-scrum",
        listOf(
            nova("Tonight is date night, darling. And I want to talk about something we do every single day."),
            rafe("Oh? I'm listening."),
            nova("[laugh] The Daily Scrum. Fifteen minutes, every working day of the Sprint. The Scrum Guide, the short free document that defines Scrum, says its purpose is to inspect progress toward the Sprint Goal and adapt the Sprint Backlog as necessary."),
            rafe("Only fifteen minutes? Some things shouldn't be rushed."),
            nova("This one should. A quiz, exam style. Who is the Daily Scrum for?"),
            rafe("The Developers, the people doing the work of the Sprint. Not a status report to the Scrum Master or a manager. If the Product Owner or Scrum Master are working on Sprint Backlog items, they join as Developers."),
            nova("Right answer. It's all about the people doing the work. [kiss]"),
            rafe("My turn. What do they inspect, exactly?"),
            nova("Progress toward the Sprint Goal, the single objective for the Sprint. Picture an Android running app: runners can record a run with no signal, and it syncs later. And they adapt the Sprint Backlog: the goal, the chosen items, and the Developers' plan."),
            rafe("And under the picture? What comes out of it?"),
            nova("An actionable plan for the next day. Maybe sync turned out bigger, so two Developers pair on it and the settings screen waits. Concrete enough that everyone knows what to do after coffee."),
            rafe("Then a harder one. The team uses the three questions: yesterday, today, blockers. Required by the Scrum Guide?"),
            nova("Yes. They're the official format."),
            rafe("No kiss for that one. They were removed in 2020. The Developers choose any structure and techniques they like, as long as it focuses on the Sprint Goal and produces an actionable plan for the next day: concrete enough to act on."),
            nova("Mm. Fair. Like walking the board, item by item through the Sprint Backlog, closest to Done first."),
            rafe("Exactly. Nobody has to prove they've been busy."),
            nova("And may the Developers still go person by person, if they like it?"),
            rafe("They may, if it keeps the focus on the goal. It just tends to turn into everyone proving they've been busy. A report, not a plan."),
            nova("And in a long Sprint, a whole month? Does it get longer?"),
            rafe("Never. Fifteen minutes whatever the Sprint length. Planning, the Review and the Retrospective grow with the Sprint; the Daily Scrum doesn't."),
            nova("My question, darling. When and where is it held?"),
            rafe("Same time and place every working day, to reduce complexity. Timeboxed at fifteen minutes: a maximum length, like a table booked for one sitting."),
            nova("Right answer. Same time, same place. I do love a reliable man. [kiss]"),
            rafe("Why the same time and place, though? Romance aside."),
            nova("Because it reduces complexity. Nobody has to ask when or where, nobody needs an invite. The habit carries the event, so the fifteen minutes go on the goal, not on finding each other."),
            rafe("And what does it give the team?"),
            nova("Better communication. Impediments found early: anything blocking progress the team can't easily clear itself. Quick decisions. And fewer other meetings. It also creates focus and improves self-management."),
            rafe("Quiz. Someone raises a nasty bug: the Play Store keeps rejecting the build. Should the team debug it together, right there?"),
            nova("No. They name it, decide who takes it, and those people meet straight after. The fifteen minutes is for the plan, not the fix. And if the impediment's bigger than the team, the Scrum Master helps cause its removal."),
            rafe("Right answer. Some things are better one to one. [kiss]"),
            nova("[chuckle] Behave. My question. Is the Daily Scrum the only time the Developers may change their plan?"),
            rafe("Not at all. The Guide says they often meet during the day for more detailed discussions, to adapt or re-plan the rest of the Sprint's work."),
            nova("Right answer. Plans can change any time of day. [kiss]"),
            rafe("And the Scrum Master? Do they run it?"),
            nova("They ensure it happens, and is positive, productive and inside its timebox. They don't chair it, and needn't attend. A Scrum Master running a status meeting has stolen the Developers' event. The Sprint Backlog episode covers the plan itself."),
            rafe("And when we're apart? A remote team, on a call?"),
            nova("Same rules, darling. The same video call at the same time each day. What matters is the purpose, not the room: inspect progress toward the goal, adapt the plan, fifteen minutes."),
            rafe("Then, for the exam. What do we remember?"),
            nova("The Daily Scrum's timebox: fifteen minutes, same time and place every working day. Who it's for: the Developers. Its purpose: inspect progress toward the Sprint Goal and adapt the Sprint Backlog. The three questions: not required since 2020; the Developers choose the structure. And it's not the only time they adjust the plan."),
            rafe("Perfect. That earns a long one. [kiss]"),
            nova("Mm. And something to try today?"),
            rafe("Time your next Daily Scrum, and note who speaks to whom. Notice whether it talks about the Sprint Goal or only about individual tasks, and count the plan changes made during the meeting. Then read the Daily Scrum section of the Scrum Guide, and try walking the board next time."),
            nova("Fifteen minutes a day, and the rest of the night for us. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun purpose() = DiagramSpec(
        "The Daily Scrum inspects progress toward the Sprint Goal and adapts the Sprint Backlog, ending in a plan for the next day.",
        nodes = listOf(
            DNode("goal", "Sprint Goal", 0.17f, 0.3f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "the objective"),
            DNode("ds", "Daily Scrum", 0.5f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "15 minutes"),
            DNode("plan", "Sprint Backlog", 0.83f, 0.3f, w = 0.3f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "adapted"),
            DNode("note", "An actionable plan for the next day", 0.5f, 0.8f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("goal", "ds", "inspect"),
            DEdge("ds", "plan", "adapt"),
        ),
        aspect = 0.8f,
    )

    private fun who() = DiagramSpec(
        "The Daily Scrum is the Developers' event; the Product Owner and Scrum Master join only as Developers, and it reports to nobody.",
        nodes = listOf(
            DNode("devs", "Developers", 0.5f, 0.17f, w = 0.6f, h = 0.22f, tone = 1, sub = "their event"),
            DNode("po", "Product Owner", 0.17f, 0.68f, w = 0.3f, h = 0.26f, sub = "if working items"),
            DNode("sm", "Scrum Master", 0.5f, 0.68f, w = 0.3f, h = 0.26f, sub = "ensures it happens"),
            DNode("mgr", "A manager", 0.83f, 0.68f, w = 0.3f, h = 0.26f, kind = NodeKind.NOTE, tone = 2, sub = "no status report"),
        ),
        edges = listOf(
            DEdge("po", "devs", "as a Developer", dashed = true),
            DEdge("sm", "devs", "as a Developer", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun rhythm() = DiagramSpec(
        "Same time and place every working day, inside fifteen minutes whatever the Sprint length, to reduce complexity.",
        nodes = listOf(
            DNode("time", "Same time", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "every working day"),
            DNode("place", "Same place", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "or the same call"),
            DNode("box", "Fifteen minutes", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "any Sprint length"),
            DNode("why", "Less complexity", 0.75f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "nobody asks when"),
        ),
        aspect = 0.8f,
    )

    private fun structure() = DiagramSpec(
        "The Developers choose the structure, so long as it focuses on the Sprint Goal and ends in an actionable plan.",
        nodes = listOf(
            DNode("old", "Three questions", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "removed in 2020"),
            DNode("walk", "Walk the board", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "item by item"),
            DNode("focus", "Focus on the goal", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "always"),
            DNode("plan", "Actionable plan", 0.75f, 0.76f, w = 0.42f, h = 0.2f, sub = "for the next day"),
        ),
        edges = listOf(
            DEdge("walk", "plan"),
            DEdge("focus", "plan"),
        ),
        aspect = 0.8f,
    )

    private fun gains() = DiagramSpec(
        "What a Daily Scrum gives the team: communication, impediments found early, quick decisions, and fewer other meetings.",
        nodes = listOf(
            DNode("comm", "Communication", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "improved"),
            DNode("imped", "Impediments", 0.75f, 0.18f, w = 0.42f, h = 0.2f, sub = "found early"),
            DNode("decide", "Quick decisions", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "made on the spot"),
            DNode("fewer", "Fewer meetings", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "no others needed"),
        ),
        aspect = 0.8f,
    )

    private fun afterwards() = DiagramSpec(
        "The Daily Scrum names a problem and who takes it; they meet straight after, and the Scrum Master helps with bigger blockers.",
        nodes = listOf(
            DNode("ds", "Daily Scrum", 0.17f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "spots it"),
            DNode("pair", "Two Developers", 0.5f, 0.3f, w = 0.3f, h = 0.24f, sub = "meet straight after"),
            DNode("sm", "Scrum Master", 0.83f, 0.3f, w = 0.3f, h = 0.24f, tone = 2, sub = "if beyond the team"),
            DNode("note", "Not the only time the Developers re-plan", 0.5f, 0.8f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("ds", "pair", "who takes it"),
            DEdge("pair", "sm", "too big?", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Daily Scrum questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("time", "Timebox", 0.26f, 0.2f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "15 minutes, same time"),
            DNode("who", "Who is it for?", 0.76f, 0.2f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "the Developers"),
            DNode("purpose", "Its purpose", 0.26f, 0.74f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "goal progress, adapt plan"),
            DNode("three", "Three questions?", 0.76f, 0.74f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "optional since 2020"),
        ),
        aspect = 0.8f,
    )

    private val all4 = setOf("time", "who", "purpose", "three")
    private val flow = setOf("goal", "ds", "plan")
    private val gainsAll = setOf("comm", "imped", "decide", "fewer")
    private val today = BoardCard("Today", "Time it, who speaks to whom, goal or tasks, count plan changes.")
    private val takeaways = BoardCard("Listen for the phrase", "15 minutes, Developers, goal progress, any structure, not the only time.")
    private val itsPurpose = BoardCard("Its purpose", "Inspect progress toward the Sprint Goal; adapt the Sprint Backlog.")
    private val sprintGoal = BoardCard("Sprint Goal", "The single objective: record a run offline, sync it later.")
    private val adaptPlan = BoardCard("Adapt the plan", "Sync is bigger: two pair on it, and the settings screen waits.")
    private val forDevelopers = BoardCard("For the Developers", "Product Owner or Scrum Master join only as Developers.")
    private val removed = BoardCard("Three questions", "Removed in 2020. The Developers choose the structure.")
    private val walking = BoardCard("Walking the board", "Item by item, closest to Done first: what will finish this?")
    private val fourGains = BoardCard("Four gains", "Communication, impediments found, quick decisions, fewer meetings.")
    private val meetAfter = BoardCard("Name it, then meet after", "The Play Store rejects a build: two people fix it straight after.")
    private val notOnlyTime = BoardCard("Not the only time", "The Developers adapt and re-plan throughout the day.")
    private val remote = BoardCard("Remote? Same rules", "The same video call at the same time each day. Same purpose.")
    private val smEnsures = BoardCard("The Scrum Master", "Ensures it happens, positive and timeboxed. Needn't attend.")
    private val longSprint = BoardCard("Even in a long Sprint", "The Daily Scrum stays at fifteen minutes.")
    private val personByPerson = BoardCard("Person by person?", "Allowed, if it keeps to the goal. Often a report, not a plan.")

    private fun classicBoard() = PodcastBoard(
        "psm-daily-scrum",
        listOf(
            // 0: "Hi, and welcome back. Today: the Daily Scrum..."
            Scene(
                0, "Fifteen minutes a day", purpose(),
                beats = listOf(
                    Beat(0, card = BoardCard("The Daily Scrum", "Fifteen minutes, every working day of the Sprint.")),
                    Beat(1, card = BoardCard("Just a standup?", "Yesterday, today, blockers: the picture we take apart.")),
                    Beat(2, show = flow, light = setOf("ds"), card = itsPurpose),
                    Beat(4, light = setOf("goal"), pulse = "goal" to "ds", card = sprintGoal),
                    Beat(6, show = setOf("note"), light = setOf("plan", "note"), pulse = "ds" to "plan", card = adaptPlan),
                ),
            ),
            // 7: "Who's it for? The manager? The Scrum Master?"
            Scene(
                7, "Whose event?", who(),
                beats = listOf(
                    Beat(8, light = setOf("devs"), pulse = "po" to "devs", card = forDevelopers),
                ),
            ),
            // 9: "Fifteen minutes exactly?"
            Scene(
                9, "Same time, same place", rhythm(),
                beats = listOf(
                    Beat(10, light = setOf("box", "time", "place", "why"), card = BoardCard("Timebox", "Fifteen minutes, like a parking meter. Same time and place daily.")),
                ),
            ),
            // 11: "Now my real doubt. My team does the three questions."
            Scene(
                11, "The three questions", structure(),
                beats = listOf(
                    Beat(12, show = setOf("old"), light = setOf("old"), card = removed),
                    Beat(14, show = setOf("focus", "plan"), light = setOf("focus", "plan"), pulse = "focus" to "plan", card = BoardCard("Any structure, if", "It focuses on the Sprint Goal and ends in an actionable plan.")),
                    Beat(16, show = setOf("walk"), light = setOf("walk"), pulse = "walk" to "plan", card = walking),
                    Beat(18, light = setOf("focus"), card = BoardCard("Self-management", "It creates focus: the Developers steer their own work.")),
                ),
            ),
            // 19: "What does the team get out of it, beyond the plan?"
            Scene(
                19, "What it gives", gains(),
                beats = listOf(
                    Beat(20, show = gainsAll, light = gainsAll, card = fourGains),
                ),
            ),
            // 21: "And when an impediment comes up..."
            Scene(
                21, "After the fifteen minutes", afterwards(),
                beats = listOf(
                    Beat(22, show = setOf("ds", "pair", "sm"), light = setOf("ds", "pair"), pulse = "ds" to "pair", card = meetAfter),
                    Beat(24, show = setOf("note"), light = setOf("note"), card = notOnlyTime),
                    Beat(26, light = setOf("sm"), card = smEnsures),
                    Beat(28, light = setOf("ds"), card = remote),
                ),
            ),
            // 29: "So what do I remember for the exam?"
            Scene(
                29, "For the exam", exam(),
                beats = listOf(
                    Beat(30, show = all4, light = all4, card = takeaways),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-daily-scrum--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Standup That Ate the Morning..."
            Scene(
                0, "The Standup That Ate the Morning", purpose(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "A 45-minute Daily Scrum, and a bug mentioned four days running.")),
                    Beat(2, card = BoardCard("Suspect cleared", "The three questions were removed from the Guide in 2020.")),
                    Beat(4, show = flow, light = setOf("ds"), card = itsPurpose),
                    Beat(6, light = setOf("goal"), pulse = "goal" to "ds", card = BoardCard("Mentioned zero times", "The Sprint Goal: record a run offline, sync it later.")),
                ),
            ),
            // 7: "So what filled the forty-five minutes?"
            Scene(
                7, "Exhibit two: the manager", who(),
                beats = listOf(
                    Beat(8, light = setOf("mgr"), card = BoardCard("Reporting hours", "One by one, to a manager in the corner.")),
                    Beat(10, light = setOf("devs", "po", "sm"), pulse = "sm" to "devs", card = forDevelopers),
                ),
            ),
            // 11: "Under the picture, then. What should the fifteen minutes produce?"
            Scene(
                11, "What it should produce", structure(),
                beats = listOf(
                    Beat(12, show = setOf("focus", "plan"), light = setOf("focus", "plan"), pulse = "focus" to "plan", card = BoardCard("Two things", "Focus on the Sprint Goal; an actionable plan for tomorrow.")),
                    Beat(14, show = setOf("old", "walk"), light = setOf("walk"), card = BoardCard("Any structure", "The Developers choose: a board, a list. The Guide doesn't mind.")),
                ),
            ),
            // 15: "Third exhibit?"
            Scene(
                15, "Exhibit three: the calendar", rhythm(),
                beats = listOf(
                    Beat(16, light = setOf("time", "place", "why"), card = BoardCard("Moved every day", "Same time and place every working day reduces complexity.")),
                    Beat(18, light = setOf("box"), card = BoardCard("Fifteen minutes", "A maximum, like a parking meter.")),
                ),
            ),
            // 19: "Isn't solving it right there efficient?"
            Scene(
                19, "Exhibit four: the bug", afterwards(),
                beats = listOf(
                    Beat(20, show = setOf("ds", "pair"), light = setOf("ds", "pair"), pulse = "ds" to "pair", card = BoardCard("Debugged live", "Name it, decide who takes it, and they meet straight after.")),
                    Beat(22, show = setOf("note", "sm"), light = setOf("note", "sm"), pulse = "pair" to "sm", card = BoardCard("Not the only time", "Re-plan through the day; big blockers go to the Scrum Master.")),
                    Beat(24, light = setOf("sm"), card = BoardCard("The Scrum Master", "Ensures it happens, positive and timeboxed. Coaches, not chairs.")),
                ),
            ),
            // 25: "What did they change, in the end?"
            Scene(
                25, "What changed", structure(),
                beats = listOf(
                    Beat(26, light = setOf("walk", "plan"), pulse = "walk" to "plan", card = BoardCard("Walking the board", "Same time daily, item by item. Eleven minutes; the bug owned.")),
                    Beat(28, card = remote),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed", exam(),
                beats = listOf(
                    Beat(30, show = all4, light = all4, card = takeaways),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-daily-scrum--gossip",
        listOf(
            // 0: "Okay, did you hear about the app team's Daily Scrum this week?..."
            Scene(
                0, "A chair at the standup", purpose(),
                beats = listOf(
                    Beat(0, card = BoardCard("Nine until ten", "Someone brought a chair to the Daily Scrum.")),
                    Beat(2, show = flow, light = setOf("ds"), card = itsPurpose),
                    Beat(4, light = setOf("goal", "plan"), card = BoardCard("Goal and backlog", "The single objective; the goal, the items and the plan.")),
                ),
            ),
            // 5: "What did Tom do?"
            Scene(
                5, "Monday: Tom", rhythm(),
                beats = listOf(
                    Beat(6, light = setOf("time"), card = BoardCard("Twice a week", "The Guide says every working day, same time and place.")),
                    Beat(8, light = setOf("why"), card = BoardCard("A stale plan", "Two days stale costs more than fifteen minutes a day.")),
                ),
            ),
            // 9: "Tuesday?"
            Scene(
                9, "Tuesday: Dana", who(),
                beats = listOf(
                    Beat(10, light = setOf("sm"), card = BoardCard("Reports to the Scrum Master", "Forty minutes, one Developer at a time.")),
                    Beat(12, light = setOf("devs"), card = BoardCard("The Developers' event", "The Scrum Master ensures it happens; needn't attend.")),
                ),
            ),
            // 13: "But the three questions. Aren't they official?"
            Scene(
                13, "The three questions", structure(),
                beats = listOf(
                    Beat(14, show = setOf("old", "focus", "plan"), light = setOf("old"), pulse = "focus" to "plan", card = BoardCard("Removed in 2020", "Any structure, if it focuses on the goal and ends in a plan.")),
                ),
            ),
            // 15: "Wednesday. Go."
            Scene(
                15, "Wednesday: Marcus", afterwards(),
                beats = listOf(
                    Beat(16, show = setOf("ds"), light = setOf("ds"), card = BoardCard("Debugged live", "Twenty-five minutes, six people, one screen.")),
                    Beat(18, show = setOf("pair", "note"), light = setOf("pair", "note"), pulse = "ds" to "pair", card = BoardCard("Name it, decide, move on", "The fixers meet straight after. Not the only time to re-plan.")),
                ),
            ),
            // 19: "Thursday?"
            Scene(
                19, "Thursday: Priya", who(),
                beats = listOf(
                    Beat(20, light = setOf("po"), card = BoardCard("Feature pitches", "New ideas go to the Product Backlog, not the Daily Scrum.")),
                    Beat(22, light = setOf("po", "sm", "devs"), pulse = "po" to "devs", card = BoardCard("Only as Developers", "If they're working on Sprint Backlog items.")),
                ),
            ),
            // 23: "And Friday? There's always a Friday."
            Scene(
                23, "Friday: the intern", afterwards(),
                beats = listOf(
                    Beat(24, light = setOf("note"), card = BoardCard("Saved every question", "The Daily Scrum isn't the only time to adjust the plan.")),
                ),
            ),
            // 25: "Bless him. Did anyone get it right this week?"
            Scene(
                25, "The other team", structure(),
                beats = listOf(
                    Beat(26, light = setOf("walk", "plan"), pulse = "walk" to "plan", card = BoardCard("Walking the board", "Same time daily, item by item. Eleven minutes, a real plan.")),
                ),
            ),
            // 27: "And what do they get out of it, beyond the plan?"
            Scene(
                27, "What they get", gains(),
                beats = listOf(
                    Beat(28, show = gainsAll, light = gainsAll, card = fourGains),
                    Beat(30, card = remote),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all4, light = all4, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-daily-scrum--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Fifteen minutes", purpose(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The Daily Scrum: fifteen minutes, every working day.")),
                    Beat(2, show = flow, light = setOf("ds"), card = itsPurpose),
                    Beat(4, light = setOf("goal"), pulse = "goal" to "ds", card = sprintGoal),
                    Beat(6, show = setOf("note"), light = setOf("plan", "note"), pulse = "ds" to "plan", card = adaptPlan),
                ),
            ),
            // 7: "Who gets to be there? Is it the boss's meeting?"
            Scene(
                7, "Whose room", who(),
                beats = listOf(
                    Beat(8, light = setOf("devs"), pulse = "sm" to "devs", card = forDevelopers),
                ),
            ),
            // 9: "And when? Whenever everyone's awake?"
            Scene(
                9, "The rhythm", rhythm(),
                beats = listOf(
                    Beat(10, light = setOf("time", "place", "why"), card = BoardCard("Same time and place", "It reduces complexity. Timeboxed at fifteen minutes.")),
                    Beat(12, light = setOf("box"), card = longSprint),
                ),
            ),
            // 13: "What about the three questions?"
            Scene(
                13, "Any structure", structure(),
                beats = listOf(
                    Beat(14, show = setOf("old", "focus", "plan"), light = setOf("old"), card = removed),
                    Beat(16, show = setOf("walk"), light = setOf("walk"), pulse = "walk" to "plan", card = walking),
                    Beat(18, light = setOf("old"), card = personByPerson),
                ),
            ),
            // 19: "And what does all that give them?"
            Scene(
                19, "What it gives", gains(),
                beats = listOf(
                    Beat(20, show = gainsAll, light = gainsAll, card = fourGains),
                ),
            ),
            // 21: "If something's broken, do they fix it there and then?"
            Scene(
                21, "After the fifteen minutes", afterwards(),
                beats = listOf(
                    Beat(22, show = setOf("ds", "pair", "sm"), light = setOf("ds", "pair"), pulse = "ds" to "pair", card = meetAfter),
                    Beat(24, show = setOf("note"), light = setOf("note"), card = notOnlyTime),
                    Beat(26, light = setOf("sm"), card = BoardCard("The Scrum Master", "Makes sure it happens. Not chairing, not collecting status.")),
                    Beat(28, light = setOf("ds"), card = remote),
                ),
            ),
            // 29: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                29, "For the exam", exam(),
                beats = listOf(
                    Beat(30, show = all4, light = all4, card = takeaways),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-daily-scrum--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Every single day", purpose(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Something we do every single day.")),
                    Beat(2, show = flow, light = setOf("ds"), card = itsPurpose),
                ),
            ),
            // 4: "This one should. A quiz, exam style. Who is the Daily Scrum for?"
            Scene(
                4, "Quiz: whose event?", who(),
                beats = listOf(
                    Beat(4, card = BoardCard("Quiz: who is it for?", "The Daily Scrum, fifteen minutes.")),
                    Beat(5, light = setOf("devs"), pulse = "po" to "devs", card = forDevelopers),
                ),
            ),
            // 7: "My turn. What do they inspect, exactly?"
            Scene(
                7, "Quiz: what's inspected?", purpose(),
                beats = listOf(
                    Beat(8, light = setOf("goal"), pulse = "goal" to "ds", card = sprintGoal),
                    Beat(10, light = setOf("plan", "note"), pulse = "ds" to "plan", card = BoardCard("An actionable plan", "Sync is bigger: two pair on it, and the settings screen waits.")),
                ),
            ),
            // 11: "Then a harder one. The team uses the three questions..."
            Scene(
                11, "Quiz: three questions", structure(),
                beats = listOf(
                    Beat(11, show = setOf("old"), light = setOf("old"), card = BoardCard("Quiz: required?", "Yesterday, today, blockers.")),
                    Beat(13, show = setOf("focus", "plan"), light = setOf("focus", "plan"), pulse = "focus" to "plan", card = BoardCard("No kiss for that one", "Removed in 2020. Any structure that keeps to the goal.")),
                    Beat(14, show = setOf("walk"), light = setOf("walk"), pulse = "walk" to "plan", card = walking),
                    Beat(16, light = setOf("old"), card = personByPerson),
                ),
            ),
            // 18: "And in a long Sprint, a whole month?"
            Scene(
                18, "Quiz: when and where?", rhythm(),
                beats = listOf(
                    Beat(18, light = setOf("box"), card = longSprint),
                    Beat(20, light = setOf("time", "place"), card = BoardCard("Same time and place", "Every working day, timeboxed at fifteen minutes.")),
                    Beat(24, light = setOf("why"), card = BoardCard("Less complexity", "No invites needed. The habit carries the event.")),
                ),
            ),
            // 25: "And what does it give the team?"
            Scene(
                25, "What it gives", gains(),
                beats = listOf(
                    Beat(26, show = gainsAll, light = gainsAll, card = fourGains),
                ),
            ),
            // 27: "Quiz. Someone raises a nasty bug..."
            Scene(
                27, "Quiz: the nasty bug", afterwards(),
                beats = listOf(
                    Beat(27, show = setOf("ds"), light = setOf("ds"), card = BoardCard("Quiz: debug it there?", "The Play Store keeps rejecting the build.")),
                    Beat(28, show = setOf("pair", "sm"), light = setOf("pair"), pulse = "ds" to "pair", card = meetAfter),
                    Beat(30, show = setOf("note"), light = setOf("note"), card = BoardCard("Quiz: the only time?", "No: the Developers re-plan throughout the day.")),
                    Beat(34, light = setOf("sm"), card = smEnsures),
                    Beat(36, light = setOf("ds"), card = remote),
                ),
            ),
            // 37: "Then, for the exam. What do we remember?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all4, light = all4, card = takeaways),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
