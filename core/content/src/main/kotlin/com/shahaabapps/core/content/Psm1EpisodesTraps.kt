package com.shahaabapps.core.content

/** Myths and traps, what the Scrum Guide does not say: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesTraps {
    private val classic = Podcast(
        "psm-traps",
        listOf(
            maya("Hi, and welcome back. Today: myths and traps, what the Scrum Guide does not say. Most people who stumble on P S M one already work in Scrum teams. Their office habits answer the questions for them. So today we take those habits one by one."),
            leo("So my real-world experience is a liability? Great. Eight years of stand-ups, wasted."),
            maya("[chuckle] Not wasted, but on this exam, it can trip you. Rule one: the Scrum Guide, the November 2020 edition, is the only source of truth. When your office does it differently, answer the way the Guide says. Picture a driving test: you signal at every turn, even if nobody on your street does."),
            leo("Okay. First myth?"),
            maya("Sprint Zero. A setup Sprint, for tools and architecture, with nothing usable at the end. The Guide has no such thing. The whole Scrum Team is accountable for a valuable, useful Increment every Sprint. The same goes for hardening Sprints, stabilisation Sprints and release Sprints."),
            leo("Hardening Sprint. In plain words?"),
            maya("A Sprint set aside to fix bugs and test before a release, because the earlier Sprints weren't really done. It's a sign the Definition of Done is too weak. The Increment episode covers the cure."),
            leo("Next. I bet it's stretching the Sprint."),
            maya("It is. Say a feature on the running app is two days from finished, on the last day. Tempting to add two days. No. Sprints have a fixed length, one month or less, and the next starts as soon as one ends. Unfinished work goes back to the Product Backlog."),
            leo("And velocity? My manager loves velocity."),
            maya("Velocity, story points, user stories, planning poker, Scrum of Scrums: none of them is in the Scrum Guide. They're complementary practices, tools teams may add. Burn-down charts appear only as one optional way to forecast progress."),
            leo("Scrum of Scrums. What is that, even?"),
            maya("A meeting where several teams each send someone to coordinate. It can be useful. It's just not in the Guide. So if a question says Scrum requires, and the answer names one of these practices, that answer is wrong."),
            leo("Next myth. I'm guessing the Scrum Master."),
            maya("That the Scrum Master assigns tasks, runs the Daily Scrum as a status meeting, or is the team's manager. All wrong. The Developers are self-managing: they decide who does what, when, and how. The Scrum Master is a true leader who serves, coaching the team and removing what blocks it."),
            leo("So who is the Daily Scrum actually for?"),
            maya("The Developers. It's their fifteen minutes to inspect progress toward the Sprint Goal and adapt the plan. If the Scrum Master or Product Owner are actively working on Sprint Backlog items, they take part as Developers."),
            leo("Next?"),
            maya("The Product Owner accepting stories at the Sprint Review. In lots of offices the Review is a sign-off. In the Guide, it's a working session: the Scrum Team presents results to key stakeholders and discusses progress toward the Product Goal. Done is decided by the Definition of Done. And the Review is never a gate to releasing."),
            leo("What about a separate testing team? We had one."),
            maya("No sub-teams, no hierarchies. Everyone doing the work is a Developer, whatever their skill. For example, the tester on the running app team is a Developer, and so is the designer, and so is the backend specialist."),
            leo("And changing the Sprint Goal halfway?"),
            maya("The goal stays fixed. Scope can be negotiated with the Product Owner as the team learns. If the goal becomes obsolete, only the Product Owner can cancel the Sprint. And the Product Owner doesn't decide how much work goes into a Sprint: the Developers select it, with the Product Owner."),
            leo("Are there facts that sound wrong but are right?"),
            maya("Two good ones. One person can be both Scrum Master and a Developer: the Guide even says a Scrum Master working on Sprint Backlog items takes part as a Developer. And one Product Owner can serve several teams on one product. Several teams, one product, one Product Owner."),
            leo("I keep saying roles. Is that a problem?"),
            maya("The 2020 Guide changed some words. Roles became accountabilities. Self-organising became self-managing, which means the team decides who does what, when, and how. The Development Team became the Developers. And the three Daily Scrum questions are gone. The questions use the new words."),
            leo("And the wording of the questions themselves?"),
            maya("[pause] Three traps. Best answer: several options may be partly true, so pick the one the Guide supports most directly. Must versus should or may: must is a rule, may is a choice. And all of the above: check every option, because one wrong option makes it wrong."),
            leo("Give me a must one."),
            maya("The Product Owner must attend the Daily Scrum. False. It's the Developers' event. Or: the Scrum Master must facilitate the Daily Scrum. Also false. The Scrum Master makes sure it happens; the Developers run it."),
            leo("Is Scrum a methodology, then? A full process?"),
            maya("The Guide calls it a lightweight framework, deliberately incomplete. And it says its parts are fixed: you can implement only some of them, but the result isn't Scrum. Picture chess: add any strategy you like, but change the rules and it isn't chess."),
            leo("So what do I remember for the exam?"),
            maya("Sprint Zero, hardening or release Sprints: no such thing. Extend the Sprint to finish work: never. Velocity, story points, user stories, Scrum of Scrums: not in the Guide. How much work goes into a Sprint: the Developers decide. Who cancels a Sprint: only the Product Owner. Office practice disagrees: the Guide wins."),
            leo("And anything to try today?"),
            maya("List ten habits your team calls Scrum, and mark each one as in the Guide, an allowed extra, or against the Guide. For every one against it, find the sentence it breaks. Then take the free Scrum Open practice assessment, and trace every miss the same way."),
            leo("The Guide, not the office. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-traps",
        listOf(
            maya("Welcome to Case Files. Today's case: The Seventy-Four Percent. Sam builds an Android running app. Five years of Scrum at work. He took the free Scrum Open practice assessment, expecting to ace it. Seventy-four percent. And the real one needs eighty-five."),
            leo("[sigh] Detective Leo. First suspect: the questions are tricks. The test makers are out to get him."),
            maya("Wrong suspect. The questions are fair. Every one comes from the Scrum Guide, the November 2020 edition. That's our first clue: the Guide is the only source of truth for this exam. Not Sam's office. Not a blog. The Guide."),
            leo("Then let's see what he got wrong."),
            maya("Exhibit one. A question about a team's first Sprint, setting up tools. Sam picked: run a Sprint Zero. The Guide has no Sprint Zero. Every Sprint, the whole Scrum Team is accountable for a valuable, useful Increment."),
            leo("His office did run a Sprint Zero, though."),
            maya("Many offices do. Same with hardening Sprints, set aside to fix bugs and test before a release. And release Sprints. None is in the Guide. A hardening Sprint means the Definition of Done is too weak."),
            leo("Exhibit two?"),
            maya("The last day of a Sprint, a feature two days from finished. Sam picked: extend the Sprint by two days. Wrong. Sprints have a fixed length, one month or less, and they're never stretched to finish work. Unfinished work goes back to the Product Backlog."),
            leo("Second suspect: he didn't memorise enough terms. Velocity, story points."),
            maya("[pause] The reverse. Exhibit three: which of these does Scrum require? Sam ticked velocity. Velocity, story points, user stories, planning poker and Scrum of Scrums aren't in the Guide at all. They're complementary practices: tools a team may add. Burn-downs appear only as one optional way to forecast."),
            leo("Scrum of Scrums. Explain, so I don't fall for it too."),
            maya("A meeting where several teams send someone to coordinate. Useful, maybe. Not Scrum as the Guide defines it. If an answer says required, and names one of these, it's wrong."),
            leo("Exhibit four."),
            maya("A question about who assigns tasks. Sam picked the Scrum Master, because at his office the Scrum Master does. In the Guide, the Developers are self-managing: they decide who does what, when, and how. The Scrum Master serves the team, coaching it and removing what blocks it."),
            leo("And the Daily Scrum? His Scrum Master runs that."),
            maya("Exhibit five, as it happens. The Daily Scrum is for the Developers. The Scrum Master makes sure it happens; the Developers run it. If the Scrum Master or Product Owner work on Sprint Backlog items, they take part as Developers."),
            leo("Third suspect: the exam is out of date. My office says Product Owner accepts stories."),
            maya("Exhibit six. At the Sprint Review, Sam picked: the Product Owner accepts or rejects each story. The Guide makes the Review a working session about progress toward the Product Goal, never a gate. The Definition of Done decides what's done."),
            leo("Anything about testers?"),
            maya("Exhibit seven. Sam answered that testers form a separate sub-team. The Guide: no sub-teams, no hierarchies. Everyone doing the work is a Developer, whatever their skill."),
            leo("He must have got some right."),
            maya("He did, and two surprised him. One person may be both Scrum Master and a Developer. And one Product Owner can serve several Scrum Teams working on one product. He thought both were forbidden."),
            leo("What about the Sprint Goal questions?"),
            maya("He said the Product Owner decides how much goes into a Sprint. No: the Developers select the work, with the Product Owner. And the Sprint Goal doesn't change mid-Sprint. If it becomes obsolete, only the Product Owner can cancel the Sprint."),
            leo("And the words? He said roles everywhere."),
            maya("The 2020 Guide talks of accountabilities, not roles, and self-managing, not self-organising. The three Daily Scrum questions are gone. Last clue: the wording. Best answer means the most directly supported option. Must is a rule; may is a choice. And all of the above dies on one wrong option."),
            leo("Did he think refinement was an event?"),
            maya("He did, on one question. Product Backlog refinement is an ongoing activity, not an event. And he thought a lighter Scrum, with only the parts you like, still counts. The Guide says its parts are fixed: implement only some, and the result isn't Scrum."),
            leo("So how did Sam crack it?"),
            maya("For every miss, he found the sentence in the Guide that settled it. Picture a driving test: you signal at every turn, even if nobody on your street does. His next practice run scored ninety-five. The assessment episode covers the hour itself."),
            leo("So, the case summary for the exam?"),
            maya("Sprint Zero, hardening or release Sprints: no such thing. Extend the Sprint to finish work: never. Velocity, story points, user stories, Scrum of Scrums: not in the Guide. How much work goes into a Sprint: the Developers decide. Who cancels a Sprint: only the Product Owner. Office practice disagrees: the Guide wins."),
            leo("Case closed. Anything to try today?"),
            maya("List ten habits your team calls Scrum, and mark each one as in the Guide, an allowed extra, or against the Guide. For every one against it, find the sentence it breaks. Then take the free Scrum Open practice assessment, and trace every miss the same way."),
            leo("The Guide, not the office. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-traps",
        listOf(
            maya("Okay, did you hear what happened when the running app team all decided to sit P S M one together? Android app, small backend, and a study group that turned into a week of confessions."),
            leo("[laugh] I heard the whiteboard came out. Start at the start."),
            maya("The rule they agreed first: the Scrum Guide, the November 2020 edition, is the only source of truth for the exam. Not what the office does. Which is when the confessions began. Monday. Tom."),
            leo("What did Tom confess?"),
            maya("Tom ran a Sprint Zero on his last project. Three weeks of tools and architecture, nothing usable. The Guide has no Sprint Zero. The whole Scrum Team is accountable for a valuable, useful Increment every Sprint."),
            leo("And his hardening Sprint? I know he had one."),
            maya("He did. A Sprint just for fixing bugs and testing before release. Not in the Guide either, nor release Sprints. A hardening Sprint means the Definition of Done is too weak. The Increment episode has the details."),
            leo("Tuesday?"),
            maya("Dana. As Product Owner, she once extended a Sprint by three days so a feature could finish. Sprints have a fixed length, one month or less. They're never stretched. Unfinished work goes back to the Product Backlog."),
            leo("And she decided how much went in, too?"),
            maya("Every Sprint. But the Developers select the work, with the Product Owner. And she changed the Sprint Goal twice mid-Sprint. The goal stays fixed. If it becomes obsolete, the Product Owner can cancel the Sprint. That's the one power that really is hers alone."),
            leo("[chuckle] Wednesday?"),
            maya("Marcus, the Scrum Master. He assigned every task, and ran the Daily Scrum as a status report to himself. The Developers are self-managing: they decide who does what, when, and how. And the Daily Scrum is for the Developers. He's supposed to make sure it happens, not run it."),
            leo("What does he do all day, then?"),
            maya("Serve. The Guide calls the Scrum Master a true leader who serves the team, the Product Owner and the organisation: coaching, removing what blocks the team, keeping events positive and inside their timebox."),
            leo("Thursday?"),
            maya("Priya. Priya had a velocity target on her desk. Story points per Sprint, set by management. Velocity, story points, user stories, planning poker, Scrum of Scrums: none is in the Scrum Guide. They're complementary practices. Burn-downs show up only as one optional way to forecast."),
            leo("Scrum of Scrums, quickly?"),
            maya("A meeting where several teams send someone to coordinate. Useful, maybe. Not in the Guide. So on the exam, if Scrum requires it, the answer's wrong."),
            leo("Was there a refinement confession too?"),
            maya("[chuckle] Marcus again. He called refinement the fifth Scrum event. It isn't an event. Product Backlog refinement is an ongoing activity. The events are the Sprint, and inside it Sprint Planning, the Daily Scrum, the Sprint Review and the Sprint Retrospective."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. The intern's old company had a testing sub-team, and a Product Owner who accepted stories at the Sprint Review. No sub-teams in Scrum: everyone doing the work is a Developer. And the Review is a working session, not a sign-off gate. The Definition of Done decides done."),
            leo("Did anyone get a surprise the right way round?"),
            maya("Tom. He thought one person couldn't be both Scrum Master and Developer. They can. And one Product Owner can serve several teams on one product. Picture three teams round one backlog, one Product Owner."),
            leo("And the words? Someone kept saying roles."),
            maya("Everyone did. The 2020 Guide says accountabilities, not roles; self-managing, not self-organising; Developers, not Development Team. And the three Daily Scrum questions are gone."),
            leo("Did anyone say they do a lighter version of Scrum?"),
            maya("The whole office. The Guide calls Scrum a lightweight framework, and says its parts are fixed. You can implement only some of them, but the result is not Scrum. Picture chess: add any strategy you like, but change the rules and it isn't chess."),
            leo("Any gossip about the questions themselves?"),
            maya("Dana's tip. Best answer means the option the Guide supports most directly. Must is a rule; may is a choice. For example, the Product Owner must attend the Daily Scrum: false. And all of the above dies on one wrong option."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Sprint Zero, hardening or release Sprints: no such thing. Extend the Sprint to finish work: never. Velocity, story points, user stories, Scrum of Scrums: not in the Guide. How much work goes into a Sprint: the Developers decide. Who cancels a Sprint: only the Product Owner. Office practice disagrees: the Guide wins."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("List ten habits your team calls Scrum, and mark each one as in the Guide, an allowed extra, or against the Guide. For every one against it, find the sentence it breaks. Then take the free Scrum Open practice assessment, and trace every miss the same way."),
            leo("No Sprint Zero, Tom. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-traps",
        listOf(
            nova("Hey, you. It's late, and you're still studying. [pause] Tonight, the stories people tell about Scrum that aren't true. Myths and traps, and what the Scrum Guide really says."),
            ivy("Mm. Myths at midnight. My favourite kind."),
            nova("[chuckle] The first truth: for P S M one, the Scrum Guide, the November 2020 edition, is the only source of truth. When your office does it differently, you answer the way the Guide says. Like a driving test: you signal at every turn, even on an empty road."),
            ivy("Tell me the first myth. Softly."),
            nova("Sprint Zero. A setup Sprint, with nothing usable at the end. The Guide has no such thing. Every Sprint, the whole Scrum Team is accountable for a valuable, useful Increment. [pause] No hardening Sprints, no stabilisation Sprints, no release Sprints."),
            ivy("Hardening. What does that mean, at this hour?"),
            nova("A Sprint kept aside to fix bugs and test before release, because the earlier Sprints weren't really done. It means the Definition of Done is too weak."),
            ivy("And if the Sprint runs out before the work does? Can I have two more days?"),
            nova("No. Sprints have a fixed length, one month or less, and the next begins as soon as one ends. The work that isn't Done goes back to the Product Backlog. [pause] Time doesn't stretch, not even tonight."),
            ivy("What about velocity? Story points?"),
            nova("Not in the Guide. Neither are user stories, planning poker, or Scrum of Scrums, where several teams send someone to coordinate. They're complementary practices: tools a team may add. Burn-downs appear only as one optional way to forecast."),
            ivy("And refinement? Is that one of the events?"),
            nova("[pause] No. Product Backlog refinement is an ongoing activity, not an event. The events are the Sprint, and inside it, Sprint Planning, the Daily Scrum, the Sprint Review and the Sprint Retrospective."),
            ivy("And the Scrum Master. Do they hand out the work?"),
            nova("No. The Developers are self-managing: they decide who does what, when, and how. The Scrum Master is a true leader who serves, coaching the team and clearing what blocks it. They don't assign tasks, and they don't run the Daily Scrum as a status meeting."),
            ivy("Who is the Daily Scrum for, then?"),
            nova("The Developers. Fifteen minutes, to inspect progress toward the Sprint Goal and adapt their plan. If the Scrum Master or Product Owner are working on Sprint Backlog items, they take part as Developers."),
            ivy("And at the Sprint Review, does the Product Owner accept each story?"),
            nova("[pause] That's another myth. The Review is a working session: the team shows results to key stakeholders and they discuss progress toward the Product Goal. The Definition of Done decides what's done. And the Review is never a gate to releasing."),
            ivy("Is there a team of testers, somewhere in the dark?"),
            nova("No sub-teams, no hierarchies. Everyone doing the work is a Developer, whatever their skill. The tester, the designer, the backend specialist. All Developers."),
            ivy("And the Sprint Goal? Can it drift, as the night goes on?"),
            nova("It holds still. Scope can be negotiated with the Product Owner as the team learns. If the goal becomes obsolete, only the Product Owner can cancel the Sprint. And how much work goes in? The Developers decide, with the Product Owner."),
            ivy("Tell me something that sounds forbidden, but isn't."),
            nova("One person can be both Scrum Master and a Developer. And one Product Owner can serve several teams on one product. [pause] Those surprise people."),
            ivy("And the words? I still whisper roles."),
            nova("The 2020 Guide says accountabilities, not roles. Self-managing, not self-organising. Developers, not Development Team. And the three Daily Scrum questions are gone."),
            ivy("Can I keep the parts of Scrum I like, and leave the rest?"),
            nova("You can. But then it isn't Scrum. The Guide calls Scrum a lightweight framework, deliberately incomplete, and says its parts are fixed. Like chess under the lamp: add any strategy you like, but change the rules, and it isn't chess."),
            ivy("And the questions themselves? Do they whisper tricks?"),
            nova("Three. Best answer: pick the option the Guide supports most directly. Must versus may: must is a rule, may is a choice. The Product Owner must attend the Daily Scrum: false. And all of the above dies on one wrong option. The assessment episode is your next late night."),
            ivy("[sigh] Mm. Before I sleep. What do I remember for the exam?"),
            nova("Sprint Zero, hardening or release Sprints: no such thing. Extend the Sprint to finish work: never. Velocity, story points, user stories, Scrum of Scrums: not in the Guide. How much work goes into a Sprint: the Developers decide. Who cancels a Sprint: only the Product Owner. Office practice disagrees: the Guide wins."),
            ivy("And something to try today, before I close my eyes?"),
            nova("List ten habits your team calls Scrum, and mark each one as in the Guide, an allowed extra, or against the Guide. [pause] For every one against it, find the sentence it breaks. Then take the free Scrum Open practice assessment, and trace every miss the same way."),
            ivy("The Guide, not the office. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-traps",
        listOf(
            nova("Tonight is date night, darling, and we're playing a game. Myth or truth. Every right answer earns a kiss."),
            rafe("I love this game. What's the prize for a wrong one?"),
            nova("[laugh] A lecture. The rule first: for P S M one, the Scrum Guide, the November 2020 edition, is the only source of truth. Not your office. Not your last job. The Guide."),
            rafe("Understood. The Guide is the only one for me. Go."),
            nova("Myth or truth. A new team should start with a Sprint Zero, to set up tools and architecture."),
            rafe("Myth. There's no Sprint Zero in the Guide, and no hardening or release Sprints. The whole Scrum Team is accountable for a valuable, useful Increment every Sprint."),
            nova("Right answer. Every Sprint counts with you. [kiss]"),
            rafe("And a hardening Sprint is?"),
            nova("A Sprint kept for fixing bugs and testing before release, because earlier Sprints weren't really done. A sign the Definition of Done is too weak. Your turn to ask me."),
            rafe("Myth or truth. On the last day, a feature is two days from finished. The team may extend the Sprint by two days."),
            nova("Myth. Sprints have a fixed length, one month or less, and they're never stretched to finish work. What isn't Done goes back to the Product Backlog."),
            rafe("Right answer. Some things shouldn't drag on. [kiss]"),
            nova("Myth or truth. Scrum requires velocity and story points."),
            rafe("Truth. Every team I know uses them."),
            nova("No kiss for that one. Velocity, story points, user stories, planning poker and Scrum of Scrums aren't in the Scrum Guide. They're complementary practices, tools a team may add. Burn-downs appear only as one optional way to forecast."),
            rafe("Scrum of Scrums sounds like a party I'd enjoy."),
            nova("[chuckle] It's a meeting where several teams send someone to coordinate. Not in the Guide. Ask me one."),
            rafe("Myth or truth. The Scrum Master assigns tasks to the Developers and runs the Daily Scrum."),
            nova("Myth. The Developers are self-managing: they decide who does what, when, and how. The Daily Scrum is the Developers' event; the Scrum Master makes sure it happens. The Scrum Master is a true leader who serves."),
            rafe("Right answer. Nobody tells you what to do. [kiss]"),
            nova("Myth or truth. At the Sprint Review, the Product Owner accepts or rejects each story, as a gate before release."),
            rafe("Myth. The Review is a working session about progress toward the Product Goal. The Definition of Done decides what's done, and the Review is never a gate to releasing."),
            nova("Right answer. No gatekeeping between us. [kiss]"),
            rafe("Myth or truth. Testers can form their own sub-team inside the Scrum Team."),
            nova("Myth. No sub-teams, no hierarchies. Everyone doing the work is a Developer, whatever their skill."),
            rafe("Right answer. One team, no cliques. [kiss] Now quiz me."),
            nova("Myth or truth. Product Backlog refinement is one of the Scrum events."),
            rafe("Myth. Refinement is an ongoing activity, not an event. The events are the Sprint, Sprint Planning, the Daily Scrum, the Sprint Review and the Sprint Retrospective."),
            nova("Right answer. Always refining, darling. [kiss]"),
            rafe("Then a hard one. One person can be both Scrum Master and a Developer."),
            nova("Truth. The Guide even says a Scrum Master working on Sprint Backlog items takes part as a Developer. And one Product Owner can serve several teams on one product."),
            rafe("Right answer. Two roles, one person, like you. [kiss]"),
            nova("Accountabilities, darling. The 2020 Guide says accountabilities, not roles. Self-managing, not self-organising. And the three Daily Scrum questions are gone."),
            rafe("Corrected, and kissed anyway. What about the Sprint Goal mid-Sprint?"),
            nova("It stays. Scope is negotiated with the Product Owner. If the goal becomes obsolete, only the Product Owner cancels. And the Developers, not the Product Owner, decide how much goes into a Sprint."),
            rafe("Can a team do Scrum partly? Just the bits we like?"),
            nova("Partly, yes, but then it isn't Scrum. The Guide calls it a lightweight framework, and its parts are fixed. Picture chess: change the rules and it isn't chess."),
            rafe("And the wording traps?"),
            nova("Best answer: the one the Guide supports most directly. Must is a rule; may is a choice. All of the above dies on one wrong option. The assessment episode covers the hour itself."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Sprint Zero, hardening or release Sprints: no such thing. Extend the Sprint to finish work: never. Velocity, story points, user stories, Scrum of Scrums: not in the Guide. How much work goes into a Sprint: the Developers decide. Who cancels a Sprint: only the Product Owner. Office practice disagrees: the Guide wins."),
            rafe("Six for six, and eight kisses tonight. That last one's a long one, love. [kiss] And something to try today?"),
            nova("List ten habits your team calls Scrum, and mark each one as in the Guide, an allowed extra, or against the Guide. For every one against it, find the sentence it breaks. Then take the free Scrum Open practice assessment, and trace every miss the same way."),
            rafe("Myth busted, darling. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun truth() = DiagramSpec(
        "For the assessment the Scrum Guide (2020) is the only source of truth; office habits and other practices are not.",
        nodes = listOf(
            DNode("guide", "Scrum Guide 2020", 0.5f, 0.15f, w = 0.7f, h = 0.2f, tone = 1, sub = "the only source"),
            DNode("office", "Your office", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "habits"),
            DNode("blogs", "Blogs and courses", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "practices"),
            DNode("exam", "The exam", 0.5f, 0.85f, w = 0.6f, h = 0.2f, sub = "answers from the Guide"),
        ),
        edges = listOf(
            DEdge("guide", "exam", "decides"),
        ),
        aspect = 0.8f,
    )

    private fun sprints() = DiagramSpec(
        "Scrum has no special Sprints and never stretches one: every Sprint aims at a usable Increment.",
        nodes = listOf(
            DNode("zero", "Sprint Zero", 0.25f, 0.16f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "not in the Guide"),
            DNode("hard", "Hardening Sprint", 0.75f, 0.16f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "not in the Guide"),
            DNode("release", "Release Sprint", 0.25f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "not in the Guide"),
            DNode("stretch", "Stretch the Sprint", 0.75f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "never"),
            DNode("real", "Every Sprint: a usable Increment", 0.5f, 0.84f, w = 0.9f, h = 0.18f, tone = 1),
        ),
        aspect = 0.8f,
    )

    private fun practices() = DiagramSpec(
        "What the Scrum Guide holds, beside complementary practices that many teams add but the Guide never requires.",
        nodes = listOf(
            DNode("g1", "Sprint Goal", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "in the Guide"),
            DNode("g2", "Definition of Done", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "in the Guide"),
            DNode("g3", "Product Goal", 0.25f, 0.82f, w = 0.42f, h = 0.2f, tone = 1, sub = "in the Guide"),
            DNode("c1", "Velocity", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 2, sub = "complementary"),
            DNode("c2", "Story points", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "complementary"),
            DNode("c3", "Scrum of Scrums", 0.75f, 0.82f, w = 0.42f, h = 0.2f, tone = 2, sub = "complementary"),
        ),
        aspect = 0.8f,
    )

    private fun people() = DiagramSpec(
        "The Scrum Master serves, the Developers self-manage and run their Daily Scrum, and the Review is a working session, not a gate.",
        nodes = listOf(
            DNode("sm", "Scrum Master", 0.25f, 0.15f, w = 0.42f, h = 0.2f, sub = "serves, coaches"),
            DNode("po", "Product Owner", 0.75f, 0.15f, w = 0.42f, h = 0.2f, sub = "cancels a Sprint"),
            DNode("devs", "Developers", 0.5f, 0.5f, w = 0.6f, h = 0.2f, tone = 1, sub = "self-managing, no sub-teams"),
            DNode("daily", "Daily Scrum", 0.25f, 0.85f, w = 0.42f, h = 0.2f, sub = "the Developers'"),
            DNode("review", "Sprint Review", 0.75f, 0.85f, w = 0.42f, h = 0.2f, sub = "not a gate"),
        ),
        edges = listOf(
            DEdge("sm", "devs", "serves"),
            DEdge("po", "devs", "selects with"),
            DEdge("devs", "daily", "run it"),
            DEdge("devs", "review", "present"),
        ),
        aspect = 0.8f,
    )

    private fun wording() = DiagramSpec(
        "The wording traps: the best answer, must against may, all of the above, and the 2020 Guide's own words.",
        nodes = listOf(
            DNode("best", "Best answer", 0.25f, 0.2f, w = 0.42f, h = 0.24f, kind = NodeKind.NOTE, sub = "most directly supported"),
            DNode("must", "Must or may", 0.75f, 0.2f, w = 0.42f, h = 0.24f, kind = NodeKind.NOTE, sub = "a rule, or a choice"),
            DNode("all", "All of the above", 0.25f, 0.72f, w = 0.42f, h = 0.24f, kind = NodeKind.NOTE, sub = "one wrong option kills it"),
            DNode("words", "2020 words", 0.75f, 0.72f, w = 0.42f, h = 0.24f, kind = NodeKind.NOTE, tone = 1, sub = "accountabilities"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's myth questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("zero", "Sprint Zero, hardening", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "no such thing"),
            DNode("extend", "Extend the Sprint", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "never"),
            DNode("velocity", "Velocity, story points", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "not in the Guide"),
            DNode("howmuch", "How much work", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Developers"),
            DNode("cancel", "Who cancels", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "only the Product Owner"),
            DNode("office", "Office disagrees", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Guide wins"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("zero", "extend", "velocity", "howmuch", "cancel", "office")
    private val myths3 = setOf("zero", "hard", "release")
    private val extras = setOf("c1", "c2", "c3")
    private val traps3 = setOf("best", "must", "all")
    private val takeaways = BoardCard("Listen for the phrase", "No Sprint Zero, no stretching, no velocity rule. Developers size it. The Guide wins.")
    private val today = BoardCard("Today", "Sort ten team habits against the Guide, then take Scrum Open.")
    private val ruleOne = BoardCard("Rule one", "The Scrum Guide, November 2020, is the only source of truth.")
    private val sprintZero = BoardCard("Sprint Zero", "No such thing. A valuable, useful Increment every Sprint.")
    private val hardening = BoardCard("Hardening Sprint", "Fixing and testing before release: a weak Definition of Done.")
    private val stretch = BoardCard("Stretch the Sprint?", "Never. Fixed length, one month or less. Unfinished work goes back.")
    private val notInGuide = BoardCard("Not in the Guide", "Velocity, story points, user stories, planning poker, Scrum of Scrums.")
    private val scrumOfScrums = BoardCard("Scrum of Scrums", "Several teams send someone to coordinate. Not in the Guide.")
    private val refinement = BoardCard("Refinement", "An ongoing activity, not an event.")
    private val smCard = BoardCard("The Scrum Master", "Serves and coaches. Doesn't assign tasks or run a status meeting.")
    private val dailyCard = BoardCard("Daily Scrum", "For the Developers. Others join as Developers if they do the work.")
    private val noGate = BoardCard("No acceptance gate", "The Review is a working session. The Definition of Done decides.")
    private val noSubTeams = BoardCard("No sub-teams", "Testers, designers, specialists: all of them Developers.")
    private val goalCard = BoardCard("The Sprint Goal", "Stays fixed. Developers decide how much; the Product Owner cancels.")
    private val sounds = BoardCard("Sounds wrong, is right", "One person as Scrum Master and Developer. One Product Owner, many teams.")
    private val words2020 = BoardCard("2020 words", "Accountabilities, self-managing, Developers. No three questions.")
    private val framework = BoardCard("A framework", "Lightweight, incomplete, its parts fixed. Partial Scrum isn't Scrum.")
    private val threeTraps = BoardCard("Three traps", "Best answer. Must or may. All of the above.")

    private fun classicBoard() = PodcastBoard(
        "psm-traps",
        listOf(
            // 0: "Hi, and welcome back. Today: myths and traps..."
            Scene(
                0, "The only source of truth", truth(),
                beats = listOf(
                    Beat(0, light = setOf("office"), card = BoardCard("Myths and traps", "Office habits answer the questions for you. Take them one by one.")),
                    Beat(2, light = setOf("guide", "exam"), pulse = "guide" to "exam", card = ruleOne),
                ),
            ),
            // 3: "Okay. First myth?"
            Scene(
                3, "Special Sprints", sprints(),
                beats = listOf(
                    Beat(4, show = myths3 + "real", light = setOf("zero", "real"), card = sprintZero),
                    Beat(6, light = setOf("hard"), card = hardening),
                    Beat(8, show = setOf("stretch"), light = setOf("stretch"), card = stretch),
                ),
            ),
            // 9: "And velocity? My manager loves velocity."
            Scene(
                9, "Complementary practices", practices(),
                beats = listOf(
                    Beat(10, show = extras, light = extras, card = notInGuide),
                    Beat(12, light = setOf("c3"), card = scrumOfScrums),
                ),
            ),
            // 13: "Next myth. I'm guessing the Scrum Master."
            Scene(
                13, "Who does what", people(),
                beats = listOf(
                    Beat(14, light = setOf("sm", "devs"), pulse = "sm" to "devs", card = smCard),
                    Beat(16, light = setOf("daily"), pulse = "devs" to "daily", card = dailyCard),
                    Beat(18, light = setOf("review"), pulse = "devs" to "review", card = noGate),
                    Beat(20, light = setOf("devs"), card = noSubTeams),
                    Beat(22, light = setOf("po"), pulse = "po" to "devs", card = goalCard),
                    Beat(24, light = setOf("sm", "po"), card = sounds),
                ),
            ),
            // 25: "I keep saying roles. Is that a problem?"
            Scene(
                25, "Words and wording", wording(),
                beats = listOf(
                    Beat(26, light = setOf("words"), card = words2020),
                    Beat(28, light = traps3, card = threeTraps),
                    Beat(30, light = setOf("must"), card = BoardCard("Must?", "The Product Owner must attend the Daily Scrum: false.")),
                    Beat(32, card = framework),
                ),
            ),
            // 33: "So what do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-traps--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Seventy-Four Percent..."
            Scene(
                0, "The Seventy-Four Percent", truth(),
                beats = listOf(
                    Beat(0, light = setOf("office"), card = BoardCard("The case", "Five years of Scrum, seventy-four percent on the practice test.")),
                    Beat(2, light = setOf("guide", "exam"), pulse = "guide" to "exam", card = BoardCard("Clue one", "Every question comes from the Scrum Guide, November 2020.")),
                ),
            ),
            // 3: "Then let's see what he got wrong."
            Scene(
                3, "Exhibits one and two", sprints(),
                beats = listOf(
                    Beat(4, show = setOf("zero", "real"), light = setOf("zero"), card = BoardCard("Exhibit one: Sprint Zero", "No such thing. An Increment every Sprint.")),
                    Beat(6, show = setOf("hard", "release"), light = setOf("hard", "release"), card = hardening),
                    Beat(8, show = setOf("stretch"), light = setOf("stretch"), card = BoardCard("Exhibit two: stretch it", "Never. A fixed length; unfinished work goes back.")),
                ),
            ),
            // 9: "Second suspect: he didn't memorise enough terms."
            Scene(
                9, "Exhibit three", practices(),
                beats = listOf(
                    Beat(10, show = extras, light = extras, card = BoardCard("Exhibit three", "He ticked velocity. Not in the Guide: complementary practices.")),
                    Beat(12, light = setOf("c3"), card = scrumOfScrums),
                ),
            ),
            // 13: "Exhibit four."
            Scene(
                13, "Exhibits four to seven", people(),
                beats = listOf(
                    Beat(14, light = setOf("sm", "devs"), pulse = "sm" to "devs", card = BoardCard("Exhibit four", "Who assigns tasks? Nobody outside: the Developers self-manage.")),
                    Beat(16, light = setOf("daily"), pulse = "devs" to "daily", card = BoardCard("Exhibit five", "The Daily Scrum is the Developers'. The Scrum Master ensures it.")),
                    Beat(18, light = setOf("review"), pulse = "devs" to "review", card = BoardCard("Exhibit six", "No acceptance at the Review. The Definition of Done decides.")),
                    Beat(20, light = setOf("devs"), card = BoardCard("Exhibit seven", "No testers' sub-team. Everyone doing the work is a Developer.")),
                    Beat(22, light = setOf("sm", "po"), card = sounds),
                    Beat(24, light = setOf("po"), pulse = "po" to "devs", card = goalCard),
                ),
            ),
            // 25: "And the words? He said roles everywhere."
            Scene(
                25, "Words and wording", wording(),
                beats = listOf(
                    Beat(26, light = traps3 + "words", card = BoardCard("The last clue", "Accountabilities, self-managing. Best answer, must or may.")),
                    Beat(28, card = BoardCard("Lighter Scrum?", "Refinement isn't an event, and partial Scrum isn't Scrum.")),
                    Beat(30, card = BoardCard("How he cracked it", "Each miss traced to its sentence in the Guide. Next run: 95.")),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = all6, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-traps--gossip",
        listOf(
            // 0: "Okay, did you hear what happened when the running app team all decided to sit PSM I?"
            Scene(
                0, "A week of confessions", truth(),
                beats = listOf(
                    Beat(0, light = setOf("office"), card = BoardCard("The study group", "The running app team sits PSM I together.")),
                    Beat(2, light = setOf("guide", "exam"), pulse = "guide" to "exam", card = ruleOne),
                ),
            ),
            // 3: "What did Tom confess?"
            Scene(
                3, "Monday and Tuesday", sprints(),
                beats = listOf(
                    Beat(4, show = setOf("zero", "real"), light = setOf("zero"), card = BoardCard("Monday: Tom", "A three-week Sprint Zero. No such thing.")),
                    Beat(6, show = setOf("hard", "release"), light = setOf("hard"), card = hardening),
                    Beat(8, show = setOf("stretch"), light = setOf("stretch"), card = BoardCard("Tuesday: Dana", "Stretched a Sprint by three days. Never.")),
                ),
            ),
            // 9: "And she decided how much went in, too?"
            Scene(
                9, "Dana and Marcus", people(),
                beats = listOf(
                    Beat(10, light = setOf("po", "devs"), pulse = "po" to "devs", card = goalCard),
                    Beat(12, light = setOf("sm", "daily"), pulse = "devs" to "daily", card = BoardCard("Wednesday: Marcus", "Assigned tasks, ran a status meeting. The Developers self-manage.")),
                    Beat(14, light = setOf("sm"), pulse = "sm" to "devs", card = BoardCard("A true leader who serves", "Coaching, removing blockers, keeping events in their timebox.")),
                ),
            ),
            // 15: "Thursday?"
            Scene(
                15, "Thursday: Priya", practices(),
                beats = listOf(
                    Beat(16, show = extras, light = setOf("c1", "c2"), card = BoardCard("Thursday: Priya", "A velocity target from management. Not in the Guide.")),
                    Beat(18, light = setOf("c3"), card = scrumOfScrums),
                    Beat(20, card = BoardCard("Refinement", "An ongoing activity, not a fifth event.")),
                ),
            ),
            // 21: "And Friday? There's always a Friday."
            Scene(
                21, "Friday, and surprises", people(),
                beats = listOf(
                    Beat(22, light = setOf("review", "devs"), pulse = "devs" to "review", card = BoardCard("Friday: the intern", "A testing sub-team, stories accepted at the Review. Both myths.")),
                    Beat(24, light = setOf("sm", "po"), card = sounds),
                ),
            ),
            // 25: "And the words? Someone kept saying roles."
            Scene(
                25, "The words", wording(),
                beats = listOf(
                    Beat(26, light = setOf("words"), card = words2020),
                    Beat(28, card = framework),
                    Beat(30, light = traps3, card = BoardCard("Dana's tip", "Best answer, must or may, all of the above.")),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = all6, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-traps--late-night",
        listOf(
            // 0: "Hey, you. It's late, and you're still studying..."
            Scene(
                0, "Myths at midnight", truth(),
                beats = listOf(
                    Beat(0, light = setOf("office", "blogs"), card = BoardCard("Tonight", "The stories people tell about Scrum that aren't true.")),
                    Beat(2, light = setOf("guide", "exam"), pulse = "guide" to "exam", card = ruleOne),
                ),
            ),
            // 3: "Tell me the first myth. Softly."
            Scene(
                3, "Special Sprints", sprints(),
                beats = listOf(
                    Beat(4, show = myths3 + "real", light = setOf("zero", "real"), card = sprintZero),
                    Beat(6, light = setOf("hard"), card = hardening),
                    Beat(8, show = setOf("stretch"), light = setOf("stretch"), card = stretch),
                ),
            ),
            // 9: "What about velocity? Story points?"
            Scene(
                9, "Practices", practices(),
                beats = listOf(
                    Beat(10, show = extras, light = extras, card = notInGuide),
                    Beat(12, card = refinement),
                ),
            ),
            // 13: "And the Scrum Master. Do they hand out the work?"
            Scene(
                13, "Who does what", people(),
                beats = listOf(
                    Beat(14, light = setOf("sm", "devs"), pulse = "sm" to "devs", card = smCard),
                    Beat(16, light = setOf("daily"), pulse = "devs" to "daily", card = dailyCard),
                    Beat(18, light = setOf("review"), pulse = "devs" to "review", card = noGate),
                    Beat(20, light = setOf("devs"), card = noSubTeams),
                    Beat(22, light = setOf("po"), pulse = "po" to "devs", card = goalCard),
                    Beat(24, light = setOf("sm", "po"), card = sounds),
                ),
            ),
            // 25: "And the words? I still whisper roles."
            Scene(
                25, "Words", wording(),
                beats = listOf(
                    Beat(26, light = setOf("words"), card = words2020),
                    Beat(28, card = framework),
                    Beat(30, light = traps3, card = threeTraps),
                ),
            ),
            // 31: "Before I sleep. What do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = all6, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-traps--couples",
        listOf(
            // 0: "Tonight is date night, darling, and we're playing a game..."
            Scene(
                0, "Myth or truth", truth(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Myth or truth: every right answer earns a kiss.")),
                    Beat(2, light = setOf("guide", "exam"), pulse = "guide" to "exam", card = ruleOne),
                ),
            ),
            // 4: "Myth or truth. A new team should start with a Sprint Zero..."
            Scene(
                4, "Special Sprints", sprints(),
                beats = listOf(
                    Beat(4, show = setOf("zero"), light = setOf("zero"), card = BoardCard("Myth or truth: Sprint Zero?", "A setup Sprint for tools and architecture.")),
                    Beat(5, show = setOf("hard", "release", "real"), light = myths3, card = BoardCard("Myth", "No Sprint Zero, hardening or release Sprints.")),
                    Beat(8, light = setOf("hard"), card = hardening),
                    Beat(9, show = setOf("stretch"), light = setOf("stretch"), card = BoardCard("Myth or truth: stretch it?", "Two days from finished on the last day.")),
                    Beat(10, card = BoardCard("Myth", "A fixed length; what isn't Done goes back.")),
                ),
            ),
            // 12: "Myth or truth. Scrum requires velocity and story points."
            Scene(
                12, "Quiz: velocity", practices(),
                beats = listOf(
                    Beat(12, show = setOf("c1", "c2"), light = setOf("c1", "c2"), card = BoardCard("Myth or truth: velocity?", "Does Scrum require velocity and story points?")),
                    Beat(14, show = setOf("c3"), light = extras, card = BoardCard("No kiss", "Complementary practices, not in the Scrum Guide.")),
                    Beat(16, light = setOf("c3"), card = scrumOfScrums),
                ),
            ),
            // 17: "Myth or truth. The Scrum Master assigns tasks..."
            Scene(
                17, "Who does what", people(),
                beats = listOf(
                    Beat(17, light = setOf("sm"), card = BoardCard("Myth or truth: Scrum Master", "Assigns tasks and runs the Daily Scrum?")),
                    Beat(18, light = setOf("devs", "daily"), pulse = "devs" to "daily", card = BoardCard("Myth", "The Developers self-manage; the Daily Scrum is theirs.")),
                    Beat(20, light = setOf("review"), card = BoardCard("Myth or truth: acceptance", "The Product Owner accepts stories at the Review?")),
                    Beat(21, pulse = "devs" to "review", card = noGate),
                    Beat(23, light = setOf("devs"), card = BoardCard("Myth or truth: sub-teams", "Testers in their own sub-team?")),
                    Beat(24, card = noSubTeams),
                    Beat(26, card = BoardCard("Myth or truth: refinement", "One of the Scrum events?")),
                    Beat(27, card = BoardCard("Myth", "Refinement is an ongoing activity, not an event.")),
                    Beat(29, light = setOf("sm", "devs"), pulse = "sm" to "devs", card = BoardCard("Truth: two hats", "One person can be Scrum Master and a Developer.")),
                    Beat(32, card = BoardCard("Accountabilities", "Not roles. Self-managing, not self-organising.")),
                    Beat(34, light = setOf("po"), pulse = "po" to "devs", card = goalCard),
                ),
            ),
            // 35: "Can a team do Scrum partly?"
            Scene(
                35, "Words and wording", wording(),
                beats = listOf(
                    Beat(36, light = setOf("words"), card = framework),
                    Beat(38, light = traps3, card = threeTraps),
                ),
            ),
            // 39: "Last question, for the exam. What do we remember?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = all6, card = takeaways),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
