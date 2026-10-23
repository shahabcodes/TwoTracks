package com.shahaabapps.core.content

/** Sprint Review and Sprint Retrospective: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesReviewRetro {
    private val classic = Podcast(
        "psm-review-retro",
        listOf(
            maya("Hi, and welcome back. Today: the last two events of every Sprint, the Sprint Review and the Sprint Retrospective. One looks at the product. The other looks at the team."),
            leo("Two events in one episode. Start with the Review. Is that the demo? We'd put the new screens on a projector, and everyone clapped."),
            maya("[chuckle] Clapping is allowed, but the demo is only the start. The Scrum Guide, the short free document that defines Scrum, says the Sprint Review's purpose is to inspect the outcome of the Sprint and determine future adaptations."),
            leo("Inspect the outcome. Plain words?"),
            maya("Look honestly at what got built, and decide what it means for what comes next. The Scrum Team presents its work to key stakeholders, people outside the team with a stake in the product, like the head of marketing or a big customer. Then together they discuss progress toward the Product Goal, the product's long-term objective."),
            leo("So it's not just a presentation."),
            maya("The Guide calls it a working session, and says the team should avoid limiting it to a presentation. Picture a small app team that just finished offline run recording. The stakeholders try it on their own phones, a running coach says her runners train in tunnels, and suddenly syncing on reconnect matters more than the new badges."),
            leo("And then what? Someone writes that down?"),
            maya("The attendees work out what to do next, and the Product Backlog may be adjusted to meet new opportunities. The Product Backlog is the ordered list of everything the product might need. They also look at what changed around them: a competitor's launch, a new Android version, a smaller budget."),
            leo("Who decides the new order, though? The loudest stakeholder?"),
            maya("The Product Owner. Stakeholders bring feedback and ideas; the Product Owner stays accountable for ordering the Product Backlog. The Review is where they hear it all first-hand."),
            leo("What gets shown? Everything we touched?"),
            maya("Only Done work. If an item doesn't meet the Definition of Done, the team's quality checklist, it can't be released, or even presented at the Sprint Review. It goes back to the Product Backlog for later."),
            leo("Here's my real doubt. At my old company, the Product Owner accepted stories at the Review, and only then could we release. Is that Scrum?"),
            maya("That's a classic trap. The Guide says the Sprint Review should never be considered a gate to releasing value. An Increment, a usable piece of the product, may be delivered to users before the Sprint ends, as soon as it's Done. The Review inspects; it doesn't approve releases."),
            leo("So we could ship to the Play Store on day three of the Sprint."),
            maya("If it meets the Definition of Done, yes. The Review still happens, and then that shipped Increment, and what real users made of it, is on the table."),
            leo("How long does the Review get?"),
            maya("Its timebox, the maximum length, is four hours for a one-month Sprint, and usually shorter for shorter Sprints. It's the second to last event of the Sprint."),
            leo("Which makes the Retrospective the last."),
            maya("Exactly. The Sprint Retrospective concludes the Sprint. Its purpose is to plan ways to increase quality and effectiveness. If the Review asks: did we build the right thing? The Retrospective asks: how can we work better?"),
            leo("Who's there? The stakeholders again?"),
            maya("No, the Scrum Team: the Developers, the Product Owner and the Scrum Master. It's the team looking at itself. Its timebox is up to three hours for a one-month Sprint, usually shorter for shorter Sprints."),
            leo("And what do they look at?"),
            maya("How the last Sprint went, regarding individuals, interactions, processes, tools, and their Definition of Done. For example: one reviewer was overloaded, two Developers kept clashing over code style, builds took forty minutes, and the Definition of Done never mentioned accessibility."),
            leo("The Definition of Done can change at the Retrospective?"),
            maya("It's one of the things they inspect. Adding tested with the screen reader raises the quality bar. They also find the assumptions that led them astray and where those came from, and discuss what went well, what problems came up, and how those problems were, or weren't, solved."),
            leo("Okay, my real doubt. Our retros produce a list of twenty improvements, and nothing ever changes."),
            maya("[sigh] The most common failure. The Guide says the team identifies the most helpful changes, and the most impactful ones are addressed as soon as possible. They may even be added to the Sprint Backlog for the next Sprint. So pick one or two, and give them a place in the plan."),
            leo("So the improvement gets real work time, like a feature."),
            maya("Exactly. Faster builds becomes an item in the next Sprint Backlog, picked up by the Developers. At the next Retrospective, they check whether it worked. That's empiricism, deciding from what you actually observe, turned on the team itself."),
            leo("And after the Retrospective? A breather?"),
            maya("No gap. The Retrospective concludes the Sprint, and the next Sprint starts immediately, with Sprint Planning. The Sprint episode explains why there's no pause in between."),
            leo("So what do I remember for the exam?"),
            maya("The Sprint Review: four hours for a one-month Sprint, the Scrum Team and key stakeholders, a working session on progress toward the Product Goal, where the Product Backlog may be adjusted, and never a gate to releasing value. The Sprint Retrospective: three hours, the Scrum Team, to increase quality and effectiveness, and it concludes the Sprint."),
            leo("And something to try today?"),
            maya("Plan your next Sprint Review as a working session: invite two real stakeholders and let them use the app. Then at your next Retrospective, choose one improvement only, and add it to the Sprint Backlog for the next Sprint. Check both timeboxes, too."),
            leo("The right product, and a better team. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-review-retro",
        listOf(
            maya("Welcome to Case Files. Today's case: The Feature Nobody Wanted. A team builds an Android running app. For three Sprints they built achievement badges. They shipped them to the Play Store, and users ignored them completely. Worse, the team keeps hitting the same problems, Sprint after Sprint."),
            leo("[chuckle] Detective Leo, on the case. First suspect: the users just don't get badges."),
            maya("[pause] Wrong suspect. Users don't owe us interest. The question is why the team didn't learn sooner. Two events should have caught it: the Sprint Review and the Sprint Retrospective. One inspects the product, the other the team."),
            leo("Start with the Review, then."),
            maya("The Scrum Guide, the short free document that defines Scrum, says the Review's purpose is to inspect the outcome of the Sprint and determine future adaptations. The Scrum Team presents its work to key stakeholders, people outside the team with a stake in the product, and they discuss progress toward the Product Goal, the product's long-term objective."),
            leo("And what did this team's Reviews look like?"),
            maya("First exhibit: thirty slides. No phone ever left a pocket. And the only person invited was the team's own manager. No customers, no running coaches, no marketing."),
            leo("Second suspect, then: not enough slides?"),
            maya("[chuckle] Closer to the opposite. The Guide calls the Review a working session, and says the team should avoid limiting it to a presentation. Real stakeholders, using the real app, would have said in Sprint one: my runners want offline sync, not badges."),
            leo("And then what would have happened?"),
            maya("The attendees collaborate on what to do next, and the Product Backlog, the ordered list of everything the product might need, may be adjusted to meet new opportunities. The Product Owner still orders it. But with feedback in week two, not after three Sprints. They'd also have looked at what changed outside, like a rival app launching badges first."),
            leo("Second exhibit?"),
            maya("The release log. The Product Owner held every build until the Review, where she accepted the stories one by one. Only then could anything go to the Play Store."),
            leo("That sounds like control. Is it wrong?"),
            maya("It is. The Guide says the Sprint Review should never be considered a gate to releasing value. An Increment, a usable piece of the product, may be delivered to users before the Sprint ends, as soon as it's Done. Here, the gate meant real users never saw anything until the end."),
            leo("So when does the Product Owner say yes to a release?"),
            maya("The Guide doesn't make the Review a release decision at all. Once an Increment meets the Definition of Done, it's usable, and it may be delivered to stakeholders before the Sprint ends. The Increment episode covers what Done really means."),
            leo("Third exhibit."),
            maya("At one Review, half-finished badge animations were shown as done. If an item doesn't meet the Definition of Done, the team's quality checklist, it can't be released or even presented at the Review. It goes back to the Product Backlog."),
            leo("And the timebox, the maximum length?"),
            maya("Four hours for a one-month Sprint, usually shorter for shorter Sprints. The Review is the second to last event of the Sprint. Now, the repeating problems. That's the Retrospective's job."),
            leo("Under the picture, what's a Retrospective for?"),
            maya("To plan ways to increase quality and effectiveness. It concludes the Sprint. The Scrum Team inspects how the last Sprint went, regarding individuals, interactions, processes, tools, and their Definition of Done. Timebox: three hours for a one-month Sprint."),
            leo("Fourth exhibit, I assume."),
            maya("The Retrospective notes. Every Sprint: builds take forty minutes. Every Sprint: one reviewer is overloaded. Twenty improvements listed. None ever done. And twice, the Retrospective was skipped to save time."),
            leo("[sigh] Skipping it is a crime on its own."),
            maya("It is: Scrum's events are fixed, and using the Guide partially isn't Scrum. The fix: the most impactful improvements are addressed as soon as possible, and may even be added to the Sprint Backlog for the next Sprint. Pick one, give it real work time, check it next Retrospective."),
            leo("And who was in that Retrospective room?"),
            maya("Last clue. The manager came along and took notes on who complained. So nobody said much. The Retrospective is for the Scrum Team: Developers, Product Owner, Scrum Master. They find the assumptions that led them astray, what went well, and what didn't. That needs openness. The values episode covers that part."),
            leo("So, the case summary for the exam?"),
            maya("The Sprint Review: four hours for a one-month Sprint, the Scrum Team and key stakeholders, a working session on progress toward the Product Goal, where the Product Backlog may be adjusted, and never a gate to releasing value. The Sprint Retrospective: three hours, the Scrum Team, to increase quality and effectiveness, and it concludes the Sprint."),
            leo("Case closed. Anything to try today?"),
            maya("Plan your next Sprint Review as a working session: invite two real stakeholders and let them use the app. Then at your next Retrospective, choose one improvement only, and add it to the Sprint Backlog for the next Sprint. Check both timeboxes, too."),
            leo("Ask the runners first. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-review-retro",
        listOf(
            maya("Okay, did you hear about the end of the app team's Sprint? The Review was a slideshow, the Retrospective was cancelled, and the badges feature nobody wanted went live anyway."),
            leo("[laugh] I heard someone fell asleep in the Review. Start me off, though. What are the two events for?"),
            maya("The Scrum Guide, the short free document that defines Scrum, says the Sprint Review inspects the outcome of the Sprint and determines future adaptations. The Sprint Retrospective plans ways to increase quality and effectiveness. One looks at the product; the other looks at the team."),
            leo("Okay. Monday?"),
            maya("Monday was Tom's Review. Thirty slides about the badges feature. Nobody touched the app. The Guide calls the Review a working session, and says the team should avoid limiting it to a presentation."),
            leo("What should it have been?"),
            maya("The Scrum Team presents its work to key stakeholders, people outside the team with a stake in the product, and they discuss progress toward the Product Goal, the product's long-term objective. Picture the running coach trying the app and saying: my runners need offline sync, not badges."),
            leo("And then the Product Backlog changes?"),
            maya("It may be adjusted to meet new opportunities. The Product Backlog is the ordered list of everything the app might need, and the Product Owner orders it. Attendees also talk about what changed around them, like a competitor's new app."),
            leo("Who decides, if the stakeholders all want different things?"),
            maya("The Product Owner. Stakeholders bring feedback; the Product Owner stays accountable for ordering the Product Backlog. The Review is where they hear it all first-hand."),
            leo("Tuesday?"),
            maya("Tuesday was Dana, the Product Owner. She told the team nothing could go to the Play Store until she accepted it at the Review."),
            leo("[sigh] A release gate."),
            maya("Exactly what the Guide says it isn't. The Sprint Review should never be considered a gate to releasing value. An Increment, a usable piece of the product, may be delivered before the Sprint ends, once it's Done."),
            leo("Then what's the Review for, if not approval?"),
            maya("Inspecting and adapting. What got built, what users made of it, what changed in the market, and what to do next. Approval isn't on the agenda; learning is."),
            leo("Wednesday. Go."),
            maya("Wednesday, Marcus showed a half-finished animation at the Review, to look productive. If an item doesn't meet the Definition of Done, the team's quality checklist, it can't be released or even presented at the Review. Back to the Product Backlog it goes."),
            leo("And how long should a Review run?"),
            maya("Its timebox, the maximum length, is four hours for a one-month Sprint, usually shorter for shorter ones. Tom's went three hours for a two-week Sprint, mostly slides."),
            leo("Thursday?"),
            maya("Thursday was the intern, who cancelled the Retrospective because the release was late. Saving time, he said. The Retrospective concludes the Sprint, and Scrum's events are fixed; skipping one isn't Scrum. The Retrospective is exactly where they'd have found out why the release was late."),
            leo("Did they hold one in the end?"),
            maya("Friday. And Priya invited the whole marketing team to watch. Nobody on the team said anything honest after that."),
            leo("Who's meant to be there?"),
            maya("The Scrum Team: the Developers, the Product Owner and the Scrum Master. They inspect how the Sprint went, regarding individuals, interactions, processes, tools, and their Definition of Done. Timebox: up to three hours for a one-month Sprint."),
            leo("And what came out of Friday's?"),
            maya("A list of twenty improvements. The same twenty as last time. Builds still take forty minutes, and one reviewer is still drowning."),
            leo("Why does nothing ever change?"),
            maya("Because a list isn't a plan. Twenty items, no owners, no time set aside. Nobody remembers them by Monday."),
            leo("What should they do with the list?"),
            maya("The Guide says identify the most helpful changes, and address the most impactful ones as soon as possible. They may even be added to the Sprint Backlog for the next Sprint. So one item, faster builds, gets real work time. Next Retrospective, they check it worked."),
            leo("Anything else they look at?"),
            maya("Assumptions that led them astray, and where those came from. Like assuming users wanted badges, without asking. And what went well, what went wrong, and how problems were, or weren't, solved. Then the next Sprint starts straight away; the Sprint episode covers that."),
            leo("So, the gossip I should remember for the exam?"),
            maya("The Sprint Review: four hours for a one-month Sprint, the Scrum Team and key stakeholders, a working session on progress toward the Product Goal, where the Product Backlog may be adjusted, and never a gate to releasing value. The Sprint Retrospective: three hours, the Scrum Team, to increase quality and effectiveness, and it concludes the Sprint."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Plan your next Sprint Review as a working session: invite two real stakeholders and let them use the app. Then at your next Retrospective, choose one improvement only, and add it to the Sprint Backlog for the next Sprint. Check both timeboxes, too."),
            leo("Fewer slides, Tom. More phones. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-review-retro",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight: the end of a Sprint. The Sprint Review, and the Sprint Retrospective."),
            ivy("Endings. My favourite, Nova. Always a little honest."),
            nova("[chuckle] These two are very honest. The Scrum Guide, the short free document that defines Scrum, gives them different jobs. The Review looks at the product. The Retrospective looks at the team."),
            ivy("The Review first, then. Is it the demo?"),
            nova("More than a demo. Its purpose is to inspect the outcome of the Sprint and determine future adaptations. The Scrum Team presents its work to key stakeholders, people outside the team with a stake in the product. [pause] And together they talk about progress toward the Product Goal, the product's long-term objective."),
            ivy("Just talk? Or do they touch it?"),
            nova("They should touch it. The Guide calls it a working session, and says not to limit it to a presentation. Picture a small team with an Android running app. A coach tries offline run recording and says her runners train in tunnels. Suddenly syncing on reconnect matters more than badges."),
            ivy("And what changes, after a moment like that?"),
            nova("The Product Backlog may be adjusted to meet new opportunities. That's the ordered list of everything the product might need. The Product Owner still orders it; the stakeholders bring the ideas. They also look at what's changed outside, like a competitor's launch."),
            ivy("And if the stakeholders all want different things?"),
            nova("They bring their feedback. The Product Owner decides the order. [pause] The Review is where the Product Owner hears it all, first-hand."),
            ivy("Do they show everything they worked on?"),
            nova("Only Done work. If an item doesn't meet the Definition of Done, the team's quality checklist, it can't be released or even presented at the Review. [pause] It goes quietly back to the Product Backlog."),
            ivy("And does the Review decide what gets released?"),
            nova("Never. The Guide says the Sprint Review should never be a gate to releasing value. An Increment, a usable piece of the product, may reach users before the Sprint ends, as soon as it's Done. Ship it on a Tuesday night, if it's ready."),
            ivy("Then why hold a Review at all, if it's already shipped?"),
            nova("To learn. [pause] What did real users do with it? What changed out there? What should come next? The Review inspects the outcome; it doesn't approve it."),
            ivy("How long do they get, for all that?"),
            nova("A timebox, which is a maximum length, like the last train home. Four hours for a one-month Sprint. Usually shorter for shorter Sprints. It's the second to last event of the Sprint."),
            ivy("Then the last. The Retrospective."),
            nova("It concludes the Sprint. [pause] Its purpose is to plan ways to increase quality and effectiveness. If the Review asks, did we build the right thing, the Retrospective asks, how can we work better?"),
            ivy("Who's in the room, that late in the Sprint?"),
            nova("The Scrum Team. The Developers, the Product Owner, the Scrum Master. No audience. Up to three hours for a one-month Sprint, and usually shorter for shorter ones."),
            ivy("And what do they look at, in the quiet?"),
            nova("How the Sprint went, regarding individuals, interactions, processes, tools, and their Definition of Done. One reviewer overloaded. Builds that take forty minutes. A Definition of Done that never mentioned accessibility."),
            ivy("They can change the Definition of Done?"),
            nova("They can strengthen it there. And they find the assumptions that led them astray, and where those came from. What went well. What went wrong. How problems were solved, [pause] or weren't."),
            ivy("[sigh] Mine always end with a long list, and nothing changes."),
            nova("Then make it short. The Guide says the most impactful improvements are addressed as soon as possible. They may even go into the Sprint Backlog for the next Sprint. One change, real work time, and check it next Retrospective. That's empiricism: deciding from what you see."),
            ivy("Why only one? I have so many flaws to fix."),
            nova("Because a list isn't a plan. One change with real work time beats twenty with none. Next Retrospective, you check whether it helped, and pick the next."),
            ivy("And then they rest?"),
            nova("No rest. The next Sprint starts straight away, with Sprint Planning. The Sprint episode is your next late night."),
            ivy("Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("The Sprint Review: four hours for a one-month Sprint, the Scrum Team and key stakeholders, a working session on progress toward the Product Goal, where the Product Backlog may be adjusted, and never a gate to releasing value. The Sprint Retrospective: three hours, the Scrum Team, to increase quality and effectiveness, and it concludes the Sprint."),
            ivy("And something to try today, before I sleep?"),
            nova("Plan your next Sprint Review as a working session: invite two real stakeholders and let them use the app. [pause] Then at your next Retrospective, choose one improvement only, and add it to the Sprint Backlog for the next Sprint. Check both timeboxes, too."),
            ivy("Look at the work, then look at yourselves. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-review-retro",
        listOf(
            nova("Tonight is date night, darling. And we're talking about how a Sprint ends."),
            rafe("Oh, I know how a good evening ends."),
            nova("[laugh] Behave. Two events end every Sprint. The Sprint Review looks at the product; the Sprint Retrospective looks at the team. The Scrum Guide, the short free document that defines Scrum, gives each its own purpose and timebox. One asks: did we build the right thing? The other: how can we work better?"),
            rafe("Then quiz me. Exam style."),
            nova("What's the purpose of the Sprint Review?"),
            rafe("To inspect the outcome of the Sprint and determine future adaptations. The Scrum Team presents its work to key stakeholders, people outside the team with a stake in the product, and they discuss progress toward the Product Goal, the product's long-term objective."),
            nova("Right answer. You always know how to present yourself. [kiss]"),
            rafe("My turn. Is the Review a presentation?"),
            nova("Mostly, yes. Slides, a demo, and then applause."),
            rafe("No kiss for that one. The Guide calls it a working session, and says the team should avoid limiting it to a presentation. Picture a running coach trying the Android app, and saying her runners need offline sync more than badges."),
            nova("Mm. Hands on, not just looking. Fair. And then the Product Backlog may be adjusted to meet new opportunities. That's the ordered list of everything the product might need, and the Product Owner still orders it."),
            rafe("And they look beyond the app too?"),
            nova("They review what's changed around them: a competitor's launch, a new Android version, a tighter budget. Then they work out together what to do next."),
            rafe("Quiz for you, darling. The Product Owner says nothing ships to the Play Store until she accepts it at the Review. Correct?"),
            nova("No. The Guide says the Sprint Review should never be considered a gate to releasing value. An Increment, a usable piece of the product, may be delivered before the Sprint ends, as soon as it's Done."),
            rafe("Right answer. Why wait, when it's ready? [kiss]"),
            nova("And only Done work is shown. If an item misses the Definition of Done, the team's quality checklist, it can't even be presented at the Review. My question. Timeboxes, for a one-month Sprint?"),
            rafe("Sprint Review: up to four hours. Sprint Retrospective: up to three. Both usually shorter for shorter Sprints. A timebox is a maximum length, like a table booked for one sitting."),
            nova("Right answer. Perfectly timed, as ever. [kiss]"),
            rafe("And the order? Which comes first?"),
            nova("The Review is the second to last event of the Sprint. The Retrospective comes last, and concludes it. Product first, then the team."),
            rafe("Now the Retrospective. What's it for?"),
            nova("To plan ways to increase quality and effectiveness. It concludes the Sprint. The team inspects how the Sprint went, regarding individuals, interactions, processes, tools, and their Definition of Done."),
            rafe("Individuals and interactions. Sounds like us."),
            nova("[chuckle] It does. And they find the assumptions that led them astray, like assuming users wanted badges, and discuss what went well, what went wrong, and how problems were or weren't solved."),
            rafe("My quiz. Who attends the Retrospective?"),
            nova("The Scrum Team. The Developers, the Product Owner, the Scrum Master. Stakeholders come to the Review; the Retrospective is the team looking at itself."),
            rafe("Right answer. Some conversations are just for us. [kiss]"),
            nova("Last quiz. The Retrospective produces twenty improvements, and nothing changes. What does the Guide suggest?"),
            rafe("The most impactful improvements are addressed as soon as possible, and may even be added to the Sprint Backlog for the next Sprint. Pick one, give it real work time, check it next time."),
            nova("Right answer. Small promises, kept. [kiss]"),
            rafe("And can the Definition of Done change there, darling?"),
            nova("It's one of the things they inspect. Adding tested with the screen reader raises the quality bar for every Increment after. Higher standards, all round."),
            rafe("And after the Retrospective? Do we get a night off?"),
            nova("No gap. The next Sprint starts straight away, with Sprint Planning. The Sprint episode explains why."),
            rafe("Where does all this improving stop, then?"),
            nova("It doesn't. That's empiricism, deciding from what you actually observe, turned on the team itself. Every Sprint, a little better. The values episode covers the openness it takes."),
            rafe("Then, for the exam. What do we remember?"),
            nova("The Sprint Review: four hours for a one-month Sprint, the Scrum Team and key stakeholders, a working session on progress toward the Product Goal, where the Product Backlog may be adjusted, and never a gate to releasing value. The Sprint Retrospective: three hours, the Scrum Team, to increase quality and effectiveness, and it concludes the Sprint."),
            rafe("Perfect. That earns a long one. [kiss]"),
            nova("Mm. And something to try today?"),
            rafe("Plan your next Sprint Review as a working session: invite two real stakeholders and let them use the app. Then at your next Retrospective, choose one improvement only, and add it to the Sprint Backlog for the next Sprint. Check both timeboxes, too."),
            nova("Review the work, and keep improving us. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun twoEvents() = DiagramSpec(
        "The Sprint Review inspects the product, the Retrospective the team and ends the Sprint, and the next Sprint starts at once.",
        nodes = listOf(
            DNode("review", "Sprint Review", 0.17f, 0.3f, w = 0.3f, h = 0.26f, tone = 1, sub = "the product, 4 h"),
            DNode("retro", "Retrospective", 0.5f, 0.3f, w = 0.3f, h = 0.26f, tone = 1, sub = "the team, 3 h"),
            DNode("next", "Next Sprint", 0.83f, 0.3f, w = 0.3f, h = 0.26f, tone = 2, sub = "starts at once"),
            DNode("note", "Review: the right thing? Retrospective: working better?", 0.5f, 0.8f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("review", "retro", "then"),
            DEdge("retro", "next", "ends the Sprint"),
        ),
        aspect = 0.8f,
    )

    private fun review() = DiagramSpec(
        "At the Sprint Review the Scrum Team shows its work to key stakeholders; progress toward the Product Goal is discussed and the backlog may change.",
        nodes = listOf(
            DNode("team", "Scrum Team", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "presents the work"),
            DNode("stake", "Key stakeholders", 0.75f, 0.18f, w = 0.42f, h = 0.2f, sub = "try it, give feedback"),
            DNode("goal", "Product Goal", 0.25f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "progress discussed"),
            DNode("backlog", "Product Backlog", 0.75f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "may be adjusted"),
        ),
        edges = listOf(
            DEdge("team", "stake", "a working session"),
            DEdge("stake", "backlog", "feedback"),
            DEdge("goal", "backlog"),
        ),
        aspect = 0.8f,
    )

    private fun notGate() = DiagramSpec(
        "A Done Increment may reach users at any time; the Sprint Review inspects it and never gates the release. Work not Done is not shown.",
        nodes = listOf(
            DNode("done", "Done Increment", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "usable"),
            DNode("users", "Users", 0.75f, 0.18f, w = 0.3f, h = 0.22f, kind = NodeKind.USER),
            DNode("review", "Sprint Review", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "inspects, never gates"),
            DNode("undone", "Not Done", 0.75f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "not even shown"),
        ),
        edges = listOf(
            DEdge("done", "users", "released when ready"),
            DEdge("done", "review", "inspected"),
        ),
        aspect = 0.8f,
    )

    private fun retro() = DiagramSpec(
        "The Retrospective inspects individuals, interactions, processes, tools and the Definition of Done, and the assumptions that misled the team.",
        nodes = listOf(
            DNode("ind", "Individuals", 0.17f, 0.2f, w = 0.3f, h = 0.2f),
            DNode("int", "Interactions", 0.5f, 0.2f, w = 0.3f, h = 0.2f),
            DNode("proc", "Processes", 0.83f, 0.2f, w = 0.3f, h = 0.2f),
            DNode("tools", "Tools", 0.3f, 0.52f, w = 0.36f, h = 0.2f),
            DNode("dod", "Definition of Done", 0.7f, 0.52f, w = 0.36f, h = 0.2f, tone = 1),
            DNode("note", "Assumptions that led them astray", 0.5f, 0.85f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        aspect = 0.8f,
    )

    private fun improve() = DiagramSpec(
        "From a long list to one real change: the most impactful improvement goes into the next Sprint Backlog and is checked at the next Retrospective.",
        nodes = listOf(
            DNode("list", "Twenty ideas", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "no owners, no time"),
            DNode("pick", "Most impactful", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "as soon as possible"),
            DNode("check", "Next Retrospective", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "did it help?"),
            DNode("sb", "Next Sprint Backlog", 0.75f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "real work time"),
        ),
        edges = listOf(
            DEdge("list", "pick", "choose"),
            DEdge("pick", "sb", "add"),
            DEdge("sb", "check", "inspect"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Sprint Review and Retrospective questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("rtime", "Review timebox", 0.26f, 0.2f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "4 hours, one-month Sprint"),
            DNode("gate", "A release gate?", 0.76f, 0.2f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "never"),
            DNode("who", "Who's at the Retro?", 0.26f, 0.74f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "the Scrum Team"),
            DNode("ttime", "Retro timebox", 0.76f, 0.74f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "3 hours; ends the Sprint"),
        ),
        aspect = 0.8f,
    )

    private val all4 = setOf("rtime", "gate", "who", "ttime")
    private val five = setOf("ind", "int", "proc", "tools", "dod")
    private val today = BoardCard("Today", "A working-session Review with real users; one improvement planned in.")
    private val takeaways = BoardCard("Listen for the phrase", "Review 4 h, working session, never a gate. Retro 3 h, Scrum Team, ends it.")
    private val twoJobs = BoardCard("Two events", "The Review looks at the product; the Retrospective at the team.")
    private val reviewPurpose = BoardCard("Its purpose", "Inspect the outcome of the Sprint; decide future adaptations.")
    private val workingSession = BoardCard("A working session", "Not just a presentation: a coach tries the app on her phone.")
    private val adjusted = BoardCard("Backlog adjusted", "For new opportunities, and for what changed around them.")
    private val poOrders = BoardCard("The Product Owner orders", "Stakeholders bring feedback; the Product Owner orders the backlog.")
    private val onlyDone = BoardCard("Only Done work", "Not Done can't be released, or even shown at the Review.")
    private val neverGate = BoardCard("Never a gate", "An Increment may reach users before the Sprint ends, once Done.")
    private val reviewBox = BoardCard("Review: up to 4 hours", "For a one-month Sprint; usually shorter. Second to last event.")
    private val retroPurpose = BoardCard("The Retrospective", "Concludes the Sprint. Plans ways to raise quality and effectiveness.")
    private val retroBox = BoardCard("The Scrum Team", "Up to 3 hours for a one-month Sprint; usually shorter.")
    private val fiveThings = BoardCard("What they inspect", "Individuals, interactions, processes, tools, Definition of Done.")
    private val assumptions = BoardCard("Assumptions", "Find the ones that led them astray, and where they came from.")
    private val mostImpactful = BoardCard("Most impactful first", "Addressed as soon as possible; maybe in the next Sprint Backlog.")
    private val straightOn = BoardCard("Straight on", "The next Sprint starts at once, with Sprint Planning.")

    private fun classicBoard() = PodcastBoard(
        "psm-review-retro",
        listOf(
            // 0: "Hi, and welcome back. Today: the last two events of every Sprint..."
            Scene(
                0, "The end of a Sprint", twoEvents(),
                beats = listOf(
                    Beat(0, show = setOf("review", "retro"), light = setOf("review", "retro"), pulse = "review" to "retro", card = twoJobs),
                ),
            ),
            // 1: "Two events in one episode. Start with the Review..."
            Scene(
                1, "The Sprint Review", review(),
                beats = listOf(
                    Beat(2, card = reviewPurpose),
                    Beat(4, show = setOf("team", "stake", "goal"), light = setOf("team", "stake"), pulse = "team" to "stake", card = BoardCard("Key stakeholders", "People outside the team with a stake: marketing, a big customer.")),
                    Beat(6, light = setOf("stake"), card = workingSession),
                    Beat(8, show = setOf("backlog"), light = setOf("backlog"), pulse = "stake" to "backlog", card = adjusted),
                    Beat(10, card = poOrders),
                ),
            ),
            // 11: "What gets shown? Everything we touched?"
            Scene(
                11, "Done, and never a gate", notGate(),
                beats = listOf(
                    Beat(12, show = setOf("undone"), light = setOf("undone"), card = onlyDone),
                    Beat(14, show = setOf("done", "review"), light = setOf("review"), pulse = "done" to "review", card = BoardCard("Never a gate", "The Review inspects; it doesn't approve releases.")),
                    Beat(16, show = setOf("users"), light = setOf("done", "users"), pulse = "done" to "users", card = BoardCard("Ship when Done", "To the Play Store on day three, if it meets the Definition of Done.")),
                ),
            ),
            // 17: "How long does the Review get?"
            Scene(
                17, "Two timeboxes", twoEvents(),
                beats = listOf(
                    Beat(18, light = setOf("review"), card = reviewBox),
                    Beat(20, light = setOf("retro"), card = retroPurpose),
                    Beat(22, card = retroBox),
                ),
            ),
            // 23: "And what do they look at?"
            Scene(
                23, "What they inspect", retro(),
                beats = listOf(
                    Beat(24, light = five, card = fiveThings),
                    Beat(26, light = setOf("dod", "note"), card = assumptions),
                ),
            ),
            // 27: "Okay, my real doubt. Our retros produce a list of twenty..."
            Scene(
                27, "One change, really made", improve(),
                beats = listOf(
                    Beat(28, show = setOf("list", "pick"), light = setOf("pick"), pulse = "list" to "pick", card = mostImpactful),
                    Beat(30, show = setOf("sb", "check"), light = setOf("sb", "check"), pulse = "sb" to "check", card = BoardCard("Real work time", "Faster builds becomes an item; checked at the next Retrospective.")),
                ),
            ),
            // 31: "And after the Retrospective? A breather?"
            Scene(
                31, "No gap", twoEvents(),
                beats = listOf(
                    Beat(32, light = setOf("next"), pulse = "retro" to "next", card = straightOn),
                ),
            ),
            // 33: "So what do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all4, light = all4, card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-review-retro--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Feature Nobody Wanted..."
            Scene(
                0, "The Feature Nobody Wanted", twoEvents(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Three Sprints of badges users ignored; the same problems repeat.")),
                    Beat(2, light = setOf("review", "retro"), card = BoardCard("Two events", "Should have caught it: one inspects the product, one the team.")),
                ),
            ),
            // 3: "Start with the Review, then."
            Scene(
                3, "Exhibit one: the slides", review(),
                beats = listOf(
                    Beat(4, show = setOf("team", "stake", "goal"), light = setOf("team", "stake"), pulse = "team" to "stake", card = reviewPurpose),
                    Beat(6, light = setOf("team"), card = BoardCard("Thirty slides", "No phone left a pocket. Only the manager was invited.")),
                    Beat(8, light = setOf("stake"), card = BoardCard("A working session", "Real stakeholders using the app: offline sync, not badges.")),
                    Beat(10, show = setOf("backlog"), light = setOf("backlog"), pulse = "stake" to "backlog", card = BoardCard("Backlog adjusted", "Feedback in week two, not after three Sprints.")),
                ),
            ),
            // 11: "Second exhibit?"
            Scene(
                11, "Exhibit two: the release log", notGate(),
                beats = listOf(
                    Beat(12, show = setOf("review"), light = setOf("review"), card = BoardCard("Accepted at the Review", "Every build held until the Product Owner said yes.")),
                    Beat(14, show = setOf("done", "users"), light = setOf("done", "users"), pulse = "done" to "users", card = neverGate),
                    Beat(16, light = setOf("done"), card = BoardCard("Done is the bar", "Usable once it meets the Definition of Done.")),
                ),
            ),
            // 17: "Third exhibit."
            Scene(
                17, "Exhibit three: half-finished", notGate(),
                beats = listOf(
                    Beat(18, light = setOf("undone"), card = BoardCard("Shown as done", "Not Done can't be released, or even presented.")),
                ),
            ),
            // 19: "And the timebox, the maximum length?"
            Scene(
                19, "The timeboxes", twoEvents(),
                beats = listOf(
                    Beat(20, light = setOf("review"), card = reviewBox),
                    Beat(22, light = setOf("retro"), card = retroPurpose),
                ),
            ),
            // 23: "Fourth exhibit, I assume."
            Scene(
                23, "Exhibit four: the notes", improve(),
                beats = listOf(
                    Beat(24, show = setOf("list"), light = setOf("list"), card = BoardCard("Twenty improvements", "None ever done. Twice, the Retrospective was skipped.")),
                    Beat(26, show = setOf("pick", "sb", "check"), light = setOf("pick", "sb"), pulse = "pick" to "sb", card = mostImpactful),
                ),
            ),
            // 27: "And who was in that Retrospective room?"
            Scene(
                27, "Who was in the room", retro(),
                beats = listOf(
                    Beat(28, light = five, card = BoardCard("The Scrum Team", "No manager taking notes. Openness needs the team's own room.")),
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
        "psm-review-retro--gossip",
        listOf(
            // 0: "Okay, did you hear about the end of the app team's Sprint?..."
            Scene(
                0, "The end of the Sprint", twoEvents(),
                beats = listOf(
                    Beat(0, card = BoardCard("Slides and a cancellation", "A slideshow Review, and a cancelled Retrospective.")),
                    Beat(2, light = setOf("review", "retro"), card = twoJobs),
                ),
            ),
            // 3: "Okay. Monday?"
            Scene(
                3, "Monday: Tom's slides", review(),
                beats = listOf(
                    Beat(4, show = setOf("team"), light = setOf("team"), card = BoardCard("Thirty slides", "Nobody touched the app. It's meant to be a working session.")),
                    Beat(6, show = setOf("stake", "goal"), light = setOf("stake", "goal"), pulse = "team" to "stake", card = BoardCard("Key stakeholders", "Try the app; discuss progress toward the Product Goal.")),
                    Beat(8, show = setOf("backlog"), light = setOf("backlog"), pulse = "stake" to "backlog", card = adjusted),
                    Beat(10, card = poOrders),
                ),
            ),
            // 11: "Tuesday?"
            Scene(
                11, "Tuesday: Dana", notGate(),
                beats = listOf(
                    Beat(12, show = setOf("review"), light = setOf("review"), card = BoardCard("Accept, then release", "Dana held every build for the Review.")),
                    Beat(14, show = setOf("done", "users"), light = setOf("done", "users"), pulse = "done" to "users", card = neverGate),
                    Beat(16, light = setOf("review"), pulse = "done" to "review", card = BoardCard("Inspect, not approve", "What got built, what users did, what to do next.")),
                ),
            ),
            // 17: "Wednesday. Go."
            Scene(
                17, "Wednesday: Marcus", notGate(),
                beats = listOf(
                    Beat(18, light = setOf("undone"), card = BoardCard("Half-finished, shown", "Not Done can't be released, or even presented.")),
                    Beat(20, light = setOf("review"), card = BoardCard("Review: up to 4 hours", "For a one-month Sprint. Tom's ran three, for two weeks.")),
                ),
            ),
            // 21: "Thursday?"
            Scene(
                21, "Thursday: the intern", twoEvents(),
                beats = listOf(
                    Beat(22, light = setOf("retro"), card = BoardCard("Retrospective cancelled", "It concludes the Sprint; Scrum's events aren't optional.")),
                ),
            ),
            // 23: "Did they hold one in the end?"
            Scene(
                23, "Friday: an audience", retro(),
                beats = listOf(
                    Beat(24, card = BoardCard("Marketing watching", "So nobody on the team said anything honest.")),
                    Beat(26, light = five, card = BoardCard("The Scrum Team inspects", "Individuals, interactions, processes, tools, Definition of Done.")),
                ),
            ),
            // 27: "And what came out of Friday's?"
            Scene(
                27, "Twenty improvements", improve(),
                beats = listOf(
                    Beat(28, show = setOf("list"), light = setOf("list"), card = BoardCard("The same twenty", "Builds still slow; one reviewer still drowning.")),
                    Beat(30, card = BoardCard("A list isn't a plan", "No owners, and no time set aside.")),
                    Beat(32, show = setOf("pick", "sb", "check"), light = setOf("pick", "sb"), pulse = "pick" to "sb", card = mostImpactful),
                ),
            ),
            // 33: "Anything else they look at?"
            Scene(
                33, "Assumptions", retro(),
                beats = listOf(
                    Beat(34, light = setOf("note"), card = BoardCard("Assumptions", "Users wanted badges? Nobody asked them.")),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all4, light = all4, card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-review-retro--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Endings", twoEvents(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The Sprint Review and the Sprint Retrospective.")),
                    Beat(2, light = setOf("review", "retro"), pulse = "review" to "retro", card = twoJobs),
                ),
            ),
            // 3: "The Review first, then. Is it the demo?"
            Scene(
                3, "The Sprint Review", review(),
                beats = listOf(
                    Beat(4, show = setOf("team", "stake", "goal"), light = setOf("team", "stake"), pulse = "team" to "stake", card = reviewPurpose),
                    Beat(6, light = setOf("stake"), card = BoardCard("A working session", "A coach tries the app: her runners train in tunnels.")),
                    Beat(8, show = setOf("backlog"), light = setOf("backlog"), pulse = "stake" to "backlog", card = adjusted),
                    Beat(10, card = poOrders),
                ),
            ),
            // 11: "Do they show everything they worked on?"
            Scene(
                11, "Done, and never a gate", notGate(),
                beats = listOf(
                    Beat(12, show = setOf("undone"), light = setOf("undone"), card = onlyDone),
                    Beat(14, show = setOf("done", "users", "review"), light = setOf("done", "users"), pulse = "done" to "users", card = neverGate),
                    Beat(16, light = setOf("review"), pulse = "done" to "review", card = BoardCard("Inspect, not approve", "What did users do? What changed? What comes next?")),
                ),
            ),
            // 17: "How long do they get, for all that?"
            Scene(
                17, "The timeboxes", twoEvents(),
                beats = listOf(
                    Beat(18, light = setOf("review"), card = reviewBox),
                    Beat(20, light = setOf("retro"), card = retroPurpose),
                    Beat(22, card = retroBox),
                ),
            ),
            // 23: "And what do they look at, in the quiet?"
            Scene(
                23, "In the quiet", retro(),
                beats = listOf(
                    Beat(24, light = five, card = fiveThings),
                    Beat(26, light = setOf("dod", "note"), card = assumptions),
                ),
            ),
            // 27: "Mine always end with a long list, and nothing changes."
            Scene(
                27, "One change", improve(),
                beats = listOf(
                    Beat(28, show = setOf("list", "pick", "sb"), light = setOf("pick", "sb"), pulse = "pick" to "sb", card = mostImpactful),
                    Beat(30, show = setOf("check"), light = setOf("check"), pulse = "sb" to "check", card = BoardCard("A list isn't a plan", "One change with real work time beats twenty with none.")),
                ),
            ),
            // 31: "And then they rest?"
            Scene(
                31, "No rest", twoEvents(),
                beats = listOf(
                    Beat(32, light = setOf("next"), pulse = "retro" to "next", card = straightOn),
                ),
            ),
            // 33: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all4, light = all4, card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-review-retro--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "How a Sprint ends", twoEvents(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "How a Sprint ends.")),
                    Beat(2, light = setOf("review", "retro"), pulse = "review" to "retro", card = BoardCard("Two events", "Did we build the right thing? How can we work better?")),
                ),
            ),
            // 4: "What's the purpose of the Sprint Review?"
            Scene(
                4, "Quiz: the Review", review(),
                beats = listOf(
                    Beat(4, card = BoardCard("Quiz: its purpose?", "The Sprint Review.")),
                    Beat(5, show = setOf("team", "stake", "goal"), light = setOf("team", "stake", "goal"), pulse = "team" to "stake", card = BoardCard("Inspect the outcome", "With key stakeholders; progress toward the Product Goal.")),
                    Beat(7, card = BoardCard("Quiz: a presentation?", "Slides, a demo, then applause?")),
                    Beat(9, light = setOf("stake"), card = BoardCard("No kiss for that one", "A working session: the coach tries the app.")),
                    Beat(10, show = setOf("backlog"), light = setOf("backlog"), pulse = "stake" to "backlog", card = poOrders),
                    Beat(12, card = BoardCard("Beyond the app", "A competitor's launch, a new Android version, a tighter budget.")),
                ),
            ),
            // 13: "Quiz for you, darling. The Product Owner says nothing ships..."
            Scene(
                13, "Quiz: a release gate?", notGate(),
                beats = listOf(
                    Beat(13, show = setOf("review"), light = setOf("review"), card = BoardCard("Quiz: accept, then ship?", "Nothing ships until the Review?")),
                    Beat(14, show = setOf("done", "users"), light = setOf("done", "users"), pulse = "done" to "users", card = neverGate),
                    Beat(16, show = setOf("undone"), light = setOf("undone"), card = onlyDone),
                ),
            ),
            // 17: "Sprint Review: up to four hours..."
            Scene(
                17, "Quiz: the timeboxes", twoEvents(),
                beats = listOf(
                    Beat(17, light = setOf("review", "retro"), card = BoardCard("Four and three hours", "Review 4, Retrospective 3, for a one-month Sprint.")),
                    Beat(20, light = setOf("retro"), pulse = "review" to "retro", card = BoardCard("Product first, then team", "The Retrospective comes last and concludes the Sprint.")),
                ),
            ),
            // 21: "Now the Retrospective. What's it for?"
            Scene(
                21, "The Retrospective", retro(),
                beats = listOf(
                    Beat(22, light = five, card = fiveThings),
                    Beat(24, light = setOf("note"), card = BoardCard("Assumptions", "Like assuming users wanted badges.")),
                    Beat(25, card = BoardCard("Quiz: who attends?", "The Sprint Retrospective.")),
                    Beat(26, light = five, card = BoardCard("The Scrum Team", "Stakeholders come to the Review; this is the team's own room.")),
                ),
            ),
            // 28: "Last quiz. The Retrospective produces twenty improvements..."
            Scene(
                28, "Quiz: twenty ideas", improve(),
                beats = listOf(
                    Beat(28, show = setOf("list"), light = setOf("list"), card = BoardCard("Quiz: twenty ideas?", "And nothing ever changes.")),
                    Beat(29, show = setOf("pick", "sb", "check"), light = setOf("pick", "sb"), pulse = "pick" to "sb", card = mostImpactful),
                    Beat(32, light = setOf("check"), card = BoardCard("Definition of Done", "Strengthened there: tested with the screen reader.")),
                ),
            ),
            // 33: "And after the Retrospective? Do we get a night off?"
            Scene(
                33, "No gap", twoEvents(),
                beats = listOf(
                    Beat(34, light = setOf("next"), pulse = "retro" to "next", card = straightOn),
                    Beat(36, card = BoardCard("Empiricism", "Deciding from what you observe, turned on the team itself.")),
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
