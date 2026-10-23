package com.shahaabapps.core.content

/** The Sprint: the container for every other event: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesSprint {
    private val classic = Podcast(
        "psm-sprint",
        listOf(
            maya("Hi, and welcome back. Today: the Sprint. The Scrum Guide, the short free document that defines Scrum, calls Sprints the heartbeat of Scrum, where ideas are turned into value. Every other Scrum event lives inside one."),
            leo("Okay, but as a developer, Sprint just sounds like two weeks of crunch to me. What is it, in plain words?"),
            maya("[chuckle] Not crunch, I promise. A Sprint is a fixed stretch of time, one month or less, in which the Scrum Team turns some ideas into a usable piece of the product. When it ends, the next Sprint starts straight away. No gap in between."),
            leo("One month or less. Why the limit?"),
            maya("Because Scrum learns by looking. Every Sprint the team inspects what it built and adapts the plan, so a Sprint of one month or less guarantees that happens at least once a calendar month. Make it longer, and the goal can go stale, complexity rises, and so does risk."),
            leo("And shorter?"),
            maya("Shorter Sprints give you more learning cycles, and keep the risk of cost and effort to a smaller stretch of time. Picture a small app team on two-week Sprints: if they build the wrong thing, they've lost two weeks, not six months. The Guide says each Sprint can be thought of as a short project."),
            leo("Is the Sprint a timebox, like the other events?"),
            maya("Yes. A timebox is a maximum length, like a parking meter. The Sprint's length is fixed, for consistency. You don't stretch one by three days because a feature isn't finished. Unfinished work goes back to the Product Backlog, the ordered list of everything the product might need."),
            leo("Hmm. That feels harsh. What if we're two days from done?"),
            maya("That's exactly the doubt the exam tests. Stretching the Sprint hides the problem: the team never learns how much it can really do. Ending on time, then talking honestly about it in the Sprint Retrospective, is how the team's forecasts get better."),
            leo("Fair. So what happens inside a Sprint?"),
            maya("All the work. The Guide says everything needed to reach the Product Goal, the long-term objective for the product, happens within Sprints. That includes the four events inside it: Sprint Planning, the Daily Scrums, the Sprint Review, and the Sprint Retrospective."),
            leo("So the Sprint is a container, and the meetings sit inside it."),
            maya("Exactly: the container for every other event. Planning opens it, a Daily Scrum runs each working day, the Review comes near the end, and the Retrospective closes it. For a one-month Sprint: Planning up to eight hours, Review up to four, Retrospective up to three. Shorter Sprints usually mean shorter Planning, Review and Retrospective. The Daily Scrum is always fifteen minutes, whatever the Sprint length."),
            leo("And what holds a Sprint together? Just a pile of tickets?"),
            maya("The Sprint Goal: the single objective for the Sprint, the reason it's worth doing. For example: a runner can record a run with no signal, and it syncs later. The tickets underneath can change as the team learns; the goal stays put."),
            leo("Now my real doubt. Halfway through, the head of marketing wants a promo banner added to this Sprint. What happens?"),
            maya("Four rules for during the Sprint. One: no changes are made that would endanger the Sprint Goal. Two: quality does not decrease. Three: the Product Backlog is refined as needed. Four: scope may be clarified and renegotiated with the Product Owner as more is learned."),
            leo("So, the banner?"),
            maya("The banner goes to the Product Owner, who decides where it sits in the Product Backlog. It isn't squeezed into this Sprint if that puts the offline goal at risk. Most likely it waits for the next Sprint Planning, which is only days away."),
            leo("And quality does not decrease. Teams cut testing when they're late, though."),
            maya("That's exactly what the rule forbids. The Definition of Done, the team's quality checklist, holds on every day of the Sprint. If time is short, you renegotiate scope with the Product Owner, not quality. Fewer features, still Done."),
            leo("Refined as needed. Refinement, in plain words?"),
            maya("Product Backlog refinement means breaking big items into smaller ones and adding detail, like a description and a size, so they're ready for a future Sprint. It's an ongoing activity during the Sprint, not an event of its own."),
            leo("Can a Sprint ever be stopped early?"),
            maya("Yes, cancelled, but only if the Sprint Goal becomes obsolete, and only the Product Owner has the authority to cancel it. Say the goal was to connect the app to a partner's smartwatch, and the partner shuts down mid-Sprint. The goal makes no sense anymore, so the Product Owner may cancel."),
            leo("Not the Scrum Master? Not the boss?"),
            maya("Not the Scrum Master, not the Developers, not a manager. Others can try to convince the Product Owner, but the call is theirs alone. And it's rare: a Sprint is short enough that its goal usually survives."),
            leo("Another doubt. My old team had a Sprint Zero for setup, and a hardening Sprint before each release."),
            maya("Neither exists in the Scrum Guide. Every Sprint is meant to produce at least one usable Increment, a working step toward the Product Goal. A Sprint that only sets up, or only fixes and stabilises, breaks that promise. The Increment episode goes deeper."),
            leo("And burn-down charts? We lived on those."),
            maya("Useful, but not required by the Guide. Burn-downs, burn-ups and the like are practices for forecasting progress, and the Guide says they don't replace empiricism, which means deciding from what you actually observe. What the team learns at the Sprint Review beats any chart."),
            leo("So what do I remember for the exam?"),
            maya("Sprint length: one month or less, fixed, and the next one starts immediately. Who can cancel a Sprint: only the Product Owner, and only when the Sprint Goal becomes obsolete. During the Sprint: nothing that endangers the Sprint Goal, no drop in quality, scope renegotiated with the Product Owner. And no Sprint Zero or hardening Sprint."),
            leo("And something to try today?"),
            maya("Open the Scrum Guide and read the section on the Sprint; it's short. Write down your team's Sprint length and check it's one month or less. Then write your current Sprint Goal in one sentence, and list the four events that happen inside the Sprint."),
            leo("One month or less, then straight on to the next. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-sprint",
        listOf(
            maya("Welcome to Case Files. Today's case: The Sprint That Never Ended. A team builds an Android running app. Their Sprints were meant to be two weeks. The last one ran six. The release to the Play Store slipped twice, and the stakeholders have stopped trusting any date."),
            leo("[chuckle] Detective Leo, on duty. Start with the basics. What's a Sprint meant to be?"),
            maya("The Scrum Guide, the short free document that defines Scrum, calls Sprints the heartbeat of Scrum. A Sprint is a fixed stretch of time, one month or less, in which the team turns ideas into a usable piece of product. The next one starts immediately after the last ends."),
            leo("First suspect, then: two weeks is too short. Give them two months and they'd finish."),
            maya("[pause] Wrong suspect. One month is the ceiling. The Sprint is a timebox, a maximum length, like a parking meter, so the team inspects and adapts at least once a calendar month. Longer Sprints let the goal go stale, raise complexity, and raise risk. Shorter ones give more learning cycles."),
            leo("So the length was never the crime."),
            maya("No. First exhibit: the calendar. Every time a feature wasn't finished, someone added a week to the Sprint. The length is meant to be fixed, so every Sprint keeps the same steady rhythm. Unfinished work goes back to the Product Backlog, the ordered list of everything the product might need."),
            leo("Why does stretching hurt, though? It got the feature done."),
            maya("It hid the problem. The team never learned how much it can really do in two weeks, so every forecast was fiction. Ending on time, then discussing it in the Sprint Retrospective, is what improves the forecasts."),
            leo("Second exhibit?"),
            maya("The Sprint Goal: the single objective for the Sprint, the reason it's worth doing. This team's goal was: a runner can record a run offline, and it syncs later. In week two, the head of marketing asked for a promo banner, and the goal was quietly rewritten to fit it."),
            leo("Second suspect: the marketing boss. Arrest her."),
            maya("[chuckle] Asking isn't a crime. The rules for during a Sprint are: no changes that would endanger the Sprint Goal, quality does not decrease, the Product Backlog is refined as needed, and scope may be clarified and renegotiated with the Product Owner as more is learned. The banner belonged with the Product Owner."),
            leo("Under the picture, what should have happened?"),
            maya("The request goes to the Product Owner, who orders it in the Product Backlog. If it fits without risking the goal, the Developers and Product Owner can agree to it. If not, it waits for the next Sprint Planning, days away. The goal stays put; the work underneath can flex."),
            leo("Third exhibit."),
            maya("The test reports. As the Sprint dragged, the Developers stopped running the slow tests. The Definition of Done, the team's quality checklist, said every build passes them. Two crash reports later, the Play Store rating fell."),
            leo("So, quality does not decrease."),
            maya("Exactly. When time is short, you renegotiate scope with the Product Owner, never quality. Fewer features, still Done. Fourth exhibit: the team's plan to fix it all with a hardening Sprint, a Sprint just for stabilising, before each release."),
            leo("That sounds sensible to me, honestly."),
            maya("It's not in the Scrum Guide, and neither is a Sprint Zero for setup. Every Sprint aims at a usable Increment, a working step toward the Product Goal, the long-term objective for the product. A Sprint that only stabilises admits the earlier ones weren't Done. The Increment episode covers that."),
            leo("Did anyone try to stop the six-week monster?"),
            maya("The Scrum Master tried to cancel it. That's where his authority stops. Only the Product Owner can cancel a Sprint, and only if the Sprint Goal becomes obsolete. Say a partner's smartwatch the goal depended on is discontinued. That's obsolete. A Sprint that's going badly isn't."),
            leo("And what goes on inside a healthy Sprint?"),
            maya("All the work toward the Product Goal, and the four other events: Sprint Planning opens it, up to eight hours for a one-month Sprint. A fifteen-minute Daily Scrum each working day. A Sprint Review of up to four hours, and a Retrospective of up to three. Shorter Sprints usually mean shorter Planning, Review and Retrospective, but the Daily Scrum stays fifteen minutes."),
            leo("And refinement? Someone mentioned it in the evidence."),
            maya("Product Backlog refinement: breaking items down and adding detail so they're ready for later Sprints. It's ongoing, not an event. This team skipped it, so every Planning started from vague items. One more clue: they trusted a burn-down chart over what they saw. The Guide says charts don't replace empiricism, deciding from what you observe."),
            leo("So, the case summary for the exam?"),
            maya("Sprint length: one month or less, fixed, and the next one starts immediately. Who can cancel a Sprint: only the Product Owner, and only when the Sprint Goal becomes obsolete. During the Sprint: nothing that endangers the Sprint Goal, no drop in quality, scope renegotiated with the Product Owner. And no Sprint Zero or hardening Sprint."),
            leo("Case closed. Anything to try today?"),
            maya("Open the Scrum Guide and read the section on the Sprint; it's short. Write down your team's Sprint length and check it's one month or less. Then write your current Sprint Goal in one sentence, and list the four events that happen inside the Sprint."),
            leo("A fixed beat, every time. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-sprint",
        listOf(
            maya("Okay, did you hear what happened to the app team's Sprint this week? It started as two weeks. It's now on week four, and nobody will say when it ends."),
            leo("[laugh] I heard the whiteboard ran out of dates. Start me off. A Sprint is what, exactly?"),
            maya("The Scrum Guide, the short free document that defines Scrum, calls Sprints the heartbeat of Scrum. A fixed stretch of time, one month or less, to turn ideas into a usable piece of product. When one ends, the next starts straight away."),
            leo("Fixed, you said. So who un-fixed it?"),
            maya("Tom. Monday, the offline sync feature wasn't finished, so Tom added a week to the Sprint. Then another. The Sprint is a timebox, a maximum length, like a parking meter. You don't feed the meter to finish your shopping."),
            leo("What should Tom have done?"),
            maya("End on time. Unfinished work goes back to the Product Backlog, the ordered list of everything the app might need. Then talk honestly about it at the Sprint Retrospective. Stretching hides how much the team can really do, so every forecast becomes fiction."),
            leo("Why one month as the limit, anyway?"),
            maya("So the team inspects and adapts at least once a calendar month. Longer Sprints let the goal go stale, raise complexity and raise risk. Shorter ones give more learning cycles and risk less money if you build the wrong thing. The Guide says each Sprint is like a short project."),
            leo("Tuesday?"),
            maya("Tuesday was Dana. The Sprint Goal, the single objective for the Sprint, was: a runner can record a run offline and it syncs later. Then the head of marketing wanted a promo banner, and Dana rewrote the goal around the banner."),
            leo("[sigh] Mid-Sprint. Ouch."),
            maya("The Guide's rules for during a Sprint: no changes that would endanger the Sprint Goal. Quality does not decrease. The Product Backlog is refined as needed. And scope may be clarified and renegotiated with the Product Owner as more is learned. The banner should have gone to the Product Owner, for a later Sprint."),
            leo("Wednesday. Go."),
            maya("Wednesday was Marcus. Running late, he skipped the slow tests. But the Definition of Done, the team's quality checklist, holds every single day. Two crash reports later, the Play Store rating fell. When time is short, renegotiate scope, never quality."),
            leo("Thursday?"),
            maya("Thursday, the Scrum Master announced he was cancelling the Sprint, because it was going so badly. Wrong person, wrong reason. Only the Product Owner can cancel a Sprint, and only if the Sprint Goal becomes obsolete."),
            leo("What counts as obsolete?"),
            maya("Say the goal was syncing with a partner's smartwatch, and the partner shuts down. The goal makes no sense anymore. That's obsolete. A Sprint that's merely going badly isn't; the team learns from it instead."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. The intern proposed a Sprint Zero, just for setup, and a hardening Sprint, just for bug fixing before release. Neither is in the Scrum Guide. Every Sprint aims at a usable Increment, a working step toward the Product Goal, the long-term objective for the product."),
            leo("So what actually goes on inside a proper Sprint?"),
            maya("All the work toward the Product Goal, plus the four events: Sprint Planning, up to eight hours for a one-month Sprint. A fifteen-minute Daily Scrum each working day. A Sprint Review of up to four hours. And the Sprint Retrospective, up to three, which closes the Sprint. Shorter Sprints usually get shorter Planning, Review and Retrospective, but the Daily Scrum stays fifteen minutes."),
            leo("And refinement? Priya keeps saying it."),
            maya("Priya's the Product Owner, so she would. Product Backlog refinement means breaking items into smaller ones and adding detail, like a size, so they're ready for later Sprints. It's an ongoing activity, not an event. The Sprint Planning episode shows where those items land."),
            leo("And the burn-down chart Tom loves?"),
            maya("Allowed, but not in the Guide, and it doesn't replace empiricism, which means deciding from what you actually observe. Tom's chart said on track until the day it didn't."),
            leo("Where does this stop working, though? Some features just don't fit in a month."),
            maya("Then they're split. In refinement, big items are broken down until each can be Done within one Sprint; that's what makes an item ready for Sprint Planning. A Sprint never grows to fit the work. The work is cut to fit the Sprint."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Sprint length: one month or less, fixed, and the next one starts immediately. Who can cancel a Sprint: only the Product Owner, and only when the Sprint Goal becomes obsolete. During the Sprint: nothing that endangers the Sprint Goal, no drop in quality, scope renegotiated with the Product Owner. And no Sprint Zero or hardening Sprint."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Open the Scrum Guide and read the section on the Sprint; it's short. Write down your team's Sprint length and check it's one month or less. Then write your current Sprint Goal in one sentence, and list the four events that happen inside the Sprint."),
            leo("Stop feeding the meter, Tom. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-sprint",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight: the Sprint. The heartbeat of Scrum."),
            ivy("A heartbeat. That's romantic, Nova, for a project plan."),
            nova("[chuckle] The Scrum Guide's own word. The short free document that defines Scrum. A Sprint is a fixed stretch of time, one month or less, where the team turns ideas into a usable piece of product. And when one ends, [pause] the next begins straight away."),
            ivy("No rest between them?"),
            nova("No gap at all. All the work happens inside Sprints, every bit needed to reach the Product Goal, the long-term objective for the product. Even the other events live inside it: Sprint Planning, the Daily Scrums, the Sprint Review, the Sprint Retrospective."),
            ivy("Why one month or less? Some nights I want more time."),
            nova("Because Scrum learns by looking. A Sprint that short means the team inspects and adapts at least once a calendar month. Let it run longer, and the goal can go stale, complexity grows, and so does risk. Shorter Sprints mean more learning cycles, and less money lost if you're wrong."),
            ivy("And if the work isn't finished on the last night?"),
            nova("[pause] You let it end anyway. The Sprint is a timebox, a maximum length, like the last train home. You don't hold the train. Unfinished work goes back to the Product Backlog, the ordered list of everything the product might need."),
            ivy("That feels cold."),
            nova("It feels cold, but it's honest. Stretching a Sprint hides how much the team can really do. Ending on time, then talking about it at the Retrospective, is how the forecasts get warmer. The Guide says each Sprint is like a short project, with its own goal and its own end."),
            ivy("What keeps a Sprint together, all those days?"),
            nova("The Sprint Goal. The single objective for the Sprint. Picture an Android running app: a runner can record a run with no signal, and it syncs later. The tasks underneath can shift. The goal stays put."),
            ivy("And if someone knocks on the door mid-Sprint? A boss with a shiny new idea?"),
            nova("Then four rules. No changes that would endanger the Sprint Goal. Quality does not decrease. The Product Backlog is refined as needed. And scope may be clarified and renegotiated with the Product Owner as more is learned. The shiny idea goes to the Product Owner, for its place in the backlog."),
            ivy("Refined. Say that one slowly."),
            nova("Product Backlog refinement. Breaking big items into small ones, adding a description and a size, so they're ready for a later Sprint. [pause] It's ongoing, quiet work. Not an event of its own."),
            ivy("And quality? When it's late and everyone's tired?"),
            nova("Especially then. The Definition of Done, the team's quality checklist, holds every day of the Sprint. When time is short, you trim scope with the Product Owner. Never quality. Fewer features, still Done."),
            ivy("Can a Sprint end early? Just walk away?"),
            nova("It can be cancelled, but only if the Sprint Goal becomes obsolete, and only by the Product Owner. Say the goal depended on a partner's smartwatch, and the partner closes. Not the Scrum Master. Not a manager. And going badly isn't obsolete."),
            ivy("And work too big for one Sprint? Some dreams are."),
            nova("Then you break it down. [pause] In refinement, big items are split until each can be Done within one Sprint; that's when they're ready to be chosen. The Sprint never grows to fit the work. The work is cut to fit the Sprint."),
            ivy("And the events inside? Tell me their timeboxes."),
            nova("For a one-month Sprint: Sprint Planning, up to eight hours. The Daily Scrum, fifteen minutes. The Sprint Review, up to four hours. The Retrospective, up to three, and it closes the Sprint. With shorter Sprints, Planning, Review and Retrospective are usually shorter. [pause] The Daily Scrum stays fifteen minutes, whatever the Sprint length."),
            ivy("Some teams have a Sprint Zero. Or a hardening Sprint, before release."),
            nova("Neither is in the Scrum Guide. Every Sprint aims at a usable Increment, a working step toward the Product Goal. [pause] And burn-down charts are fine, but they don't replace empiricism, deciding from what you observe. The Sprint Planning episode is your next late night."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Sprint length: one month or less, fixed, and the next one starts immediately. Who can cancel a Sprint: only the Product Owner, and only when the Sprint Goal becomes obsolete. During the Sprint: nothing that endangers the Sprint Goal, no drop in quality, scope renegotiated with the Product Owner. And no Sprint Zero or hardening Sprint."),
            ivy("And something to try today, before I sleep?"),
            nova("Open the Scrum Guide and read the section on the Sprint; it's short. [pause] Write down your team's Sprint length and check it's one month or less. Then write your current Sprint Goal in one sentence, and list the four events that happen inside the Sprint."),
            ivy("One heartbeat, then the next. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-sprint",
        listOf(
            nova("Tonight is date night, darling. And tonight we talk about how long things should last."),
            rafe("Oh? As long as it takes, surely."),
            nova("[laugh] Not in Scrum. Tonight: the Sprint. The Scrum Guide, the short free document that defines Scrum, calls Sprints its heartbeat. A fixed stretch of time where the team turns ideas into a usable piece of product, and the next one starts the moment the last one ends."),
            rafe("No breaks in between. Demanding. I like it."),
            nova("Then a quiz, exam style. What's the longest a Sprint may be?"),
            rafe("One month or less. Long enough to build something real, short enough that the team inspects and adapts at least once a calendar month. Longer, and the goal goes stale, complexity grows and risk climbs. Shorter means more learning cycles, and less money lost if we build the wrong thing."),
            nova("Right answer. Short and frequent, just how I like it. [kiss]"),
            rafe("My turn. We're two days from finishing a feature on the last day of the Sprint. Do we extend the Sprint?"),
            nova("Yes, just a little. Two days, for the feature."),
            rafe("No kiss for that one. The Sprint is a timebox, a maximum length, like a table booked for one sitting. Its length is fixed. Unfinished work goes back to the Product Backlog, the ordered list of everything the product might need, and the team talks about it at the Retrospective."),
            nova("Mm. Stretching only hides how much we can really do. Fair. What holds a Sprint together, then?"),
            rafe("The Sprint Goal. The single objective for the Sprint. For an Android running app, say: a runner can record a run offline, and it syncs later. The tasks underneath can shift; the goal stays put."),
            nova("Committed. I like that in a goal. Quiz for you. Mid-Sprint, the head of marketing wants a promo banner squeezed in. What happens?"),
            rafe("Four rules. No changes that would endanger the Sprint Goal. Quality does not decrease. The Product Backlog is refined as needed. Scope may be clarified and renegotiated with the Product Owner. So the banner goes to the Product Owner, who orders it in the backlog, probably for the next Sprint."),
            nova("Right answer. Nothing gets between us and our goal. [kiss]"),
            rafe("Refined as needed. Remind me what refinement means, darling?"),
            nova("Product Backlog refinement: breaking big items into smaller ones and adding detail, like a size, so they're ready for a later Sprint. It's ongoing, not an event of its own."),
            rafe("And when the team is late, may they skip a few tests?"),
            nova("Never. Quality does not decrease. The Definition of Done, the team's quality checklist, holds every day. You trim scope with the Product Owner, not quality."),
            rafe("Right answer. Standards, even under pressure. [kiss]"),
            nova("My question. Who can cancel a Sprint?"),
            rafe("The Scrum Master, when it's going badly."),
            nova("Oh, darling. No kiss for that one. Only the Product Owner can cancel, and only if the Sprint Goal becomes obsolete. Say the goal depended on a partner's smartwatch, and the partner shuts down. Going badly isn't obsolete."),
            rafe("Then let me win you back. Name the events inside a one-month Sprint, with their timeboxes."),
            nova("Sprint Planning, up to eight hours. The Daily Scrum, fifteen minutes, each working day. The Sprint Review, up to four hours. The Sprint Retrospective, up to three, and it closes the Sprint. Shorter Sprints usually mean shorter Planning, Review and Retrospective, but the Daily Scrum stays fifteen minutes. And all the work toward the Product Goal, the long-term objective, happens inside Sprints."),
            rafe("Right answer. Every event, perfectly timed. [kiss]"),
            nova("Last one. My old team ran a Sprint Zero for setup, and a hardening Sprint before release. Scrum?"),
            rafe("Neither is in the Guide. Every Sprint aims at a usable Increment, a working step toward the Product Goal. And a burn-down chart is fine, but it doesn't replace empiricism, deciding from what you see. The Increment episode goes further."),
            nova("And work too big for one Sprint, darling? Some of us have big plans."),
            rafe("Then we break it down. In refinement, big items are split until each can be Done within one Sprint, and then they're ready to be picked. The Sprint never stretches to fit the work. The work is cut to fit the Sprint."),
            nova("Then, for the exam. What do we remember?"),
            rafe("Sprint length: one month or less, fixed, and the next one starts immediately. Who can cancel a Sprint: only the Product Owner, and only when the Sprint Goal becomes obsolete. During the Sprint: nothing that endangers the Sprint Goal, no drop in quality, scope renegotiated with the Product Owner. And no Sprint Zero or hardening Sprint."),
            nova("Perfect. That earns a long one. [kiss]"),
            rafe("Mm. And something to try today?"),
            nova("Open the Scrum Guide and read the section on the Sprint; it's short. Write down your team's Sprint length and check it's one month or less. Then write your current Sprint Goal in one sentence, and list the four events that happen inside the Sprint."),
            rafe("Fixed length, and never a dull beat. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun timeline() = DiagramSpec(
        "One Sprint of one month or less holds every other event, and the next Sprint starts the moment it ends.",
        nodes = listOf(
            DNode("sprint", "One Sprint: one month or less", 0.5f, 0.1f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
            DNode("plan", "Sprint Planning", 0.25f, 0.38f, w = 0.42f, h = 0.2f, tone = 1, sub = "up to 8 hours"),
            DNode("daily", "Daily Scrum", 0.75f, 0.38f, w = 0.42f, h = 0.2f, sub = "15 minutes, each day"),
            DNode("review", "Sprint Review", 0.25f, 0.68f, w = 0.42f, h = 0.2f, sub = "up to 4 hours"),
            DNode("retro", "Retrospective", 0.75f, 0.68f, w = 0.42f, h = 0.2f, tone = 1, sub = "up to 3 hours"),
            DNode("next", "The next Sprint starts at once", 0.5f, 0.91f, w = 0.6f, h = 0.12f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("plan", "daily"),
            DEdge("daily", "review"),
            DEdge("review", "retro"),
            DEdge("retro", "next"),
        ),
        aspect = 0.8f,
    )

    private fun length() = DiagramSpec(
        "Why one month or less: inspect and adapt at least monthly; the length is fixed, and unfinished work goes back.",
        nodes = listOf(
            DNode("long", "Longer Sprints", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "stale goal, more risk"),
            DNode("short", "Shorter Sprints", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "more learning cycles"),
            DNode("box", "A fixed timebox", 0.25f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "never stretched"),
            DNode("undone", "Unfinished work", 0.75f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "back to the backlog"),
        ),
        edges = listOf(
            DEdge("box", "undone", "ends on time"),
        ),
        aspect = 0.8f,
    )

    private fun rules() = DiagramSpec(
        "During the Sprint: the goal is safe, quality holds, the backlog is refined, and scope is renegotiated with the Product Owner.",
        nodes = listOf(
            DNode("goal", "Sprint Goal safe", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "nothing endangers it"),
            DNode("quality", "Quality holds", 0.75f, 0.18f, w = 0.42f, h = 0.2f, sub = "Definition of Done"),
            DNode("refine", "Backlog refined", 0.25f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "as needed"),
            DNode("scope", "Scope renegotiated", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "with the Product Owner"),
        ),
        edges = listOf(
            DEdge("goal", "quality"),
            DEdge("refine", "scope"),
        ),
        aspect = 0.8f,
    )

    private fun request() = DiagramSpec(
        "A request mid-Sprint goes to the Product Owner, who orders it in the Product Backlog; the Sprint Goal stays put.",
        nodes = listOf(
            DNode("req", "Marketing", 0.17f, 0.32f, w = 0.3f, h = 0.26f, kind = NodeKind.USER),
            DNode("po", "Product Owner", 0.5f, 0.32f, w = 0.3f, h = 0.26f, tone = 1, sub = "orders it"),
            DNode("backlog", "Product Backlog", 0.83f, 0.32f, w = 0.3f, h = 0.26f, kind = NodeKind.CYLINDER),
            DNode("note", "The Sprint Goal stays put", 0.5f, 0.82f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("req", "po", "asks"),
            DEdge("po", "backlog", "orders"),
        ),
        aspect = 0.8f,
    )

    private fun cancel() = DiagramSpec(
        "Only the Product Owner can cancel a Sprint, and only when the Sprint Goal becomes obsolete.",
        nodes = listOf(
            DNode("goal", "Sprint Goal obsolete", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the only reason"),
            DNode("po", "Product Owner", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "the only one who can"),
            DNode("bad", "Going badly", 0.25f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "not a reason"),
            DNode("sm", "Scrum Master", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "cannot cancel"),
        ),
        edges = listOf(
            DEdge("goal", "po", "cancel"),
        ),
        aspect = 0.8f,
    )

    private fun myths() = DiagramSpec(
        "Not in the Scrum Guide: Sprint Zero and hardening Sprints. Every Sprint aims at a usable Increment.",
        nodes = listOf(
            DNode("zero", "Sprint Zero", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "not in the Guide"),
            DNode("harden", "Hardening Sprint", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "not in the Guide"),
            DNode("inc", "A usable Increment", 0.25f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "every Sprint"),
            DNode("burn", "Burn-down charts", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "no substitute for looking"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Sprint questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("len", "Sprint length", 0.26f, 0.2f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "one month or less"),
            DNode("cancel", "Who can cancel?", 0.76f, 0.2f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "the Product Owner only"),
            DNode("during", "During the Sprint", 0.26f, 0.74f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "goal safe, quality holds"),
            DNode("zero", "Sprint Zero, hardening", 0.76f, 0.74f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "not Scrum"),
        ),
        aspect = 0.8f,
    )

    private val all4 = setOf("len", "cancel", "during", "zero")
    private val events = setOf("plan", "daily", "review", "retro")
    private val today = BoardCard("Today", "Read the Sprint section; check your length; one-line Sprint Goal.")
    private val takeaways = BoardCard("Listen for the phrase", "One month or less. Product Owner cancels. Goal safe. No Sprint Zero.")
    private val timeboxes = BoardCard("Timeboxes, one-month Sprint", "Planning 8 h, Daily Scrum 15 min, Review 4 h, Retrospective 3 h.")
    private val fourRules = BoardCard("During the Sprint", "Goal safe, quality holds, backlog refined, scope renegotiated.")
    private val refinement = BoardCard("Refinement", "Breaking items down and adding detail. Ongoing, not an event.")
    private val scopeNotQuality = BoardCard("Scope, never quality", "Renegotiate scope with the Product Owner. Fewer features, still Done.")
    private val cutToFit = BoardCard("Cut the work to fit", "Split items until each can be Done within one Sprint.")
    private val onlyPo = BoardCard("Only the Product Owner", "And only if the Sprint Goal is obsolete. Going badly isn't.")
    private val notInGuide = BoardCard("Not in the Guide", "No Sprint Zero, no hardening Sprint. Aim at a usable Increment.")

    private fun classicBoard() = PodcastBoard(
        "psm-sprint",
        listOf(
            // 0: "Hi, and welcome back. Today: the Sprint..."
            Scene(
                0, "The heartbeat of Scrum", timeline(),
                beats = listOf(
                    Beat(0, card = BoardCard("The Sprint", "The heartbeat of Scrum, where ideas are turned into value.")),
                    Beat(2, show = setOf("next"), light = setOf("sprint", "next"), card = BoardCard("One month or less", "A fixed stretch, then the next Sprint starts straight away.")),
                ),
            ),
            // 3: "One month or less. Why the limit?"
            Scene(
                3, "Why one month or less", length(),
                beats = listOf(
                    Beat(4, show = setOf("long"), light = setOf("long"), card = BoardCard("At least once a month", "Inspect and adapt. Longer: stale goals, more complexity, more risk.")),
                    Beat(6, show = setOf("short"), light = setOf("short"), card = BoardCard("Shorter Sprints", "More learning cycles; less lost if you build the wrong thing.")),
                    Beat(8, show = setOf("box", "undone"), light = setOf("box", "undone"), pulse = "box" to "undone", card = BoardCard("A timebox", "Fixed length. Unfinished work returns to the Product Backlog.")),
                    Beat(10, light = setOf("box"), card = BoardCard("Never stretched", "Stretching hides how much the team can really do.")),
                ),
            ),
            // 11: "Fair. So what happens inside a Sprint?"
            Scene(
                11, "The container", timeline(),
                beats = listOf(
                    Beat(12, show = events, light = events, card = BoardCard("All the work", "Everything toward the Product Goal happens inside Sprints.")),
                    Beat(14, show = setOf("next"), light = setOf("retro", "next"), pulse = "retro" to "next", card = timeboxes),
                ),
            ),
            // 15: "And what holds a Sprint together?"
            Scene(
                15, "The Sprint Goal", rules(),
                beats = listOf(
                    Beat(16, show = setOf("goal"), light = setOf("goal"), card = BoardCard("Sprint Goal", "The single objective: record a run offline, sync it later.")),
                    Beat(18, show = setOf("quality", "refine", "scope"), light = setOf("goal", "quality", "refine", "scope"), card = fourRules),
                ),
            ),
            // 19: "So, the banner?"
            Scene(
                19, "A banner, mid-Sprint", request(),
                beats = listOf(
                    Beat(19, show = setOf("req"), light = setOf("req"), card = BoardCard("A promo banner", "The head of marketing wants it in this Sprint.")),
                    Beat(20, show = setOf("po", "backlog", "note"), light = setOf("po", "backlog"), pulse = "po" to "backlog", card = BoardCard("To the Product Owner", "Ordered in the backlog; likely the next Sprint Planning.")),
                ),
            ),
            // 21: "And quality does not decrease..."
            Scene(
                21, "Quality holds", rules(),
                beats = listOf(
                    Beat(22, light = setOf("quality", "scope"), card = scopeNotQuality),
                    Beat(24, light = setOf("refine"), card = refinement),
                ),
            ),
            // 25: "Can a Sprint ever be stopped early?"
            Scene(
                25, "Cancelling a Sprint", cancel(),
                beats = listOf(
                    Beat(26, show = setOf("goal", "po"), light = setOf("goal", "po"), pulse = "goal" to "po", card = BoardCard("Cancelled if obsolete", "Only by the Product Owner. The partner's smartwatch shuts down.")),
                    Beat(28, show = setOf("bad", "sm"), light = setOf("sm", "bad"), card = BoardCard("Not the Scrum Master", "Nor Developers, nor managers: they can only convince.")),
                ),
            ),
            // 29: "Another doubt. My old team had a Sprint Zero..."
            Scene(
                29, "Not in the Guide", myths(),
                beats = listOf(
                    Beat(30, show = setOf("zero", "harden", "inc"), light = setOf("zero", "harden"), card = notInGuide),
                    Beat(32, show = setOf("burn"), light = setOf("burn"), card = BoardCard("Burn-down charts", "Useful forecasts, but no replacement for empiricism.")),
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
        "psm-sprint--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Sprint That Never Ended..."
            Scene(
                0, "The Sprint That Never Ended", timeline(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Two-week Sprints that ran six. The release slipped twice.")),
                    Beat(2, show = setOf("next"), light = setOf("sprint", "next"), card = BoardCard("The Sprint", "One month or less; the next starts immediately.")),
                ),
            ),
            // 3: "First suspect, then: two weeks is too short."
            Scene(
                3, "Suspect: too short", length(),
                beats = listOf(
                    Beat(4, show = setOf("long", "short"), light = setOf("long"), card = BoardCard("One month is the ceiling", "Inspect and adapt at least monthly. Longer means more risk.")),
                    Beat(6, show = setOf("box", "undone"), light = setOf("box"), card = BoardCard("Exhibit one: the calendar", "A week added each time. The length should stay fixed.")),
                    Beat(8, light = setOf("undone"), pulse = "box" to "undone", card = BoardCard("It hid the problem", "Forecasts became fiction. Unfinished work goes back.")),
                ),
            ),
            // 9: "Second exhibit?"
            Scene(
                9, "Exhibit two: the goal", rules(),
                beats = listOf(
                    Beat(10, show = setOf("goal"), light = setOf("goal"), card = BoardCard("The Sprint Goal", "Record a run offline; rewritten mid-Sprint for a banner.")),
                    Beat(12, show = setOf("quality", "refine", "scope"), light = setOf("goal", "quality", "refine", "scope"), card = fourRules),
                ),
            ),
            // 13: "Under the picture, what should have happened?"
            Scene(
                13, "Where the banner goes", request(),
                beats = listOf(
                    Beat(14, light = setOf("po", "backlog"), pulse = "po" to "backlog", card = BoardCard("To the Product Owner", "Ordered in the backlog. The goal stays; the work flexes.")),
                ),
            ),
            // 15: "Third exhibit."
            Scene(
                15, "Exhibit three: the tests", rules(),
                beats = listOf(
                    Beat(16, light = setOf("quality"), card = BoardCard("Slow tests skipped", "The Definition of Done says every build passes them.")),
                    Beat(18, light = setOf("scope"), card = scopeNotQuality),
                ),
            ),
            // 19: "That sounds sensible to me, honestly."
            Scene(
                19, "Exhibit four: hardening", myths(),
                beats = listOf(
                    Beat(20, show = setOf("zero", "harden", "inc"), light = setOf("harden", "zero"), card = notInGuide),
                ),
            ),
            // 21: "Did anyone try to stop the six-week monster?"
            Scene(
                21, "Who may cancel", cancel(),
                beats = listOf(
                    Beat(22, show = setOf("goal", "po", "bad", "sm"), light = setOf("goal", "po"), pulse = "goal" to "po", card = BoardCard("The Scrum Master tried", "Only the Product Owner may, and only if the goal is obsolete.")),
                ),
            ),
            // 23: "And what goes on inside a healthy Sprint?"
            Scene(
                23, "A healthy Sprint", timeline(),
                beats = listOf(
                    Beat(24, show = events + "next", light = events, card = timeboxes),
                ),
            ),
            // 25: "And refinement? Someone mentioned it in the evidence."
            Scene(
                25, "Refinement and charts", myths(),
                beats = listOf(
                    Beat(26, show = setOf("burn"), light = setOf("burn"), card = BoardCard("Refinement, and the chart", "Refinement is ongoing. Charts don't replace empiricism.")),
                ),
            ),
            // 27: "So, the case summary for the exam?"
            Scene(
                27, "Case closed", exam(),
                beats = listOf(
                    Beat(28, show = all4, light = all4, card = takeaways),
                    Beat(30, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-sprint--gossip",
        listOf(
            // 0: "Okay, did you hear what happened to the app team's Sprint this week?..."
            Scene(
                0, "The Sprint that grew", timeline(),
                beats = listOf(
                    Beat(0, card = BoardCard("Week four of two", "It started as a two-week Sprint.")),
                    Beat(2, show = setOf("next"), light = setOf("sprint", "next"), card = BoardCard("The Sprint", "One month or less; the next starts straight away.")),
                ),
            ),
            // 3: "Fixed, you said. So who un-fixed it?"
            Scene(
                3, "Monday: Tom", length(),
                beats = listOf(
                    Beat(4, show = setOf("box"), light = setOf("box"), card = BoardCard("A week added, twice", "A timebox is a maximum length. Don't feed the meter.")),
                    Beat(6, show = setOf("undone"), light = setOf("undone"), pulse = "box" to "undone", card = BoardCard("End on time", "Unfinished work to the backlog; talk at the Retrospective.")),
                    Beat(8, show = setOf("long", "short"), light = setOf("long", "short"), card = BoardCard("Why one month", "Inspect at least monthly. Shorter: more learning cycles.")),
                ),
            ),
            // 9: "Tuesday?"
            Scene(
                9, "Tuesday: Dana", rules(),
                beats = listOf(
                    Beat(10, show = setOf("goal"), light = setOf("goal"), card = BoardCard("The goal, rewritten", "Offline runs became a promo banner, mid-Sprint.")),
                    Beat(12, show = setOf("quality", "refine", "scope"), light = setOf("goal", "quality", "refine", "scope"), card = fourRules),
                ),
            ),
            // 13: "Wednesday. Go."
            Scene(
                13, "Wednesday: Marcus", rules(),
                beats = listOf(
                    Beat(14, light = setOf("quality", "scope"), card = BoardCard("Skipped the slow tests", "The Definition of Done holds every day. Scope, never quality.")),
                ),
            ),
            // 15: "Thursday?"
            Scene(
                15, "Thursday: who cancels", cancel(),
                beats = listOf(
                    Beat(16, show = setOf("sm", "bad"), light = setOf("sm", "bad"), card = BoardCard("Cancelled for going badly", "Wrong person, and wrong reason.")),
                    Beat(18, show = setOf("goal", "po"), light = setOf("goal", "po"), pulse = "goal" to "po", card = onlyPo),
                ),
            ),
            // 19: "And Friday? There's always a Friday."
            Scene(
                19, "Friday: the intern", myths(),
                beats = listOf(
                    Beat(20, show = setOf("zero", "harden", "inc"), light = setOf("zero", "harden"), card = notInGuide),
                ),
            ),
            // 21: "So what actually goes on inside a proper Sprint?"
            Scene(
                21, "A proper Sprint", timeline(),
                beats = listOf(
                    Beat(22, show = events + "next", light = events, card = timeboxes),
                ),
            ),
            // 23: "And refinement? Priya keeps saying it."
            Scene(
                23, "Refinement", rules(),
                beats = listOf(
                    Beat(24, light = setOf("refine"), card = refinement),
                    Beat(26, light = setOf("goal"), card = BoardCard("Tom's burn-down", "Allowed, but no replacement for empiricism.")),
                    Beat(28, light = setOf("refine", "scope"), card = cutToFit),
                ),
            ),
            // 29: "So, the gossip I should remember for the exam?"
            Scene(
                29, "For the exam", exam(),
                beats = listOf(
                    Beat(30, show = all4, light = all4, card = takeaways),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-sprint--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "The heartbeat", timeline(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The Sprint, the heartbeat of Scrum.")),
                    Beat(2, show = setOf("next"), light = setOf("sprint", "next"), card = BoardCard("One month or less", "And when one ends, the next begins straight away.")),
                    Beat(4, show = events, light = events, card = BoardCard("All the work, inside", "Every event, and everything toward the Product Goal.")),
                ),
            ),
            // 5: "Why one month or less? Some nights I want more time."
            Scene(
                5, "Why one month or less", length(),
                beats = listOf(
                    Beat(6, show = setOf("long", "short"), light = setOf("long", "short"), card = BoardCard("Inspect at least monthly", "Longer: stale goals, more risk. Shorter: more learning.")),
                    Beat(8, show = setOf("box", "undone"), light = setOf("box"), pulse = "box" to "undone", card = BoardCard("A timebox", "Like the last train home: you don't hold it.")),
                    Beat(10, light = setOf("undone"), card = BoardCard("A short project", "Stretching hides how much the team can really do.")),
                ),
            ),
            // 11: "What keeps a Sprint together, all those days?"
            Scene(
                11, "What holds it together", rules(),
                beats = listOf(
                    Beat(12, show = setOf("goal"), light = setOf("goal"), card = BoardCard("Sprint Goal", "The single objective: record a run offline, sync it later.")),
                    Beat(14, show = setOf("quality", "refine", "scope"), light = setOf("goal", "scope"), card = fourRules),
                    Beat(16, light = setOf("refine"), card = refinement),
                    Beat(18, light = setOf("quality"), card = scopeNotQuality),
                ),
            ),
            // 19: "Can a Sprint end early? Just walk away?"
            Scene(
                19, "Ending early", cancel(),
                beats = listOf(
                    Beat(20, show = setOf("goal", "po", "bad", "sm"), light = setOf("goal", "po"), pulse = "goal" to "po", card = onlyPo),
                ),
            ),
            // 21: "And work too big for one Sprint? Some dreams are."
            Scene(
                21, "Too big for one Sprint", length(),
                beats = listOf(
                    Beat(22, light = setOf("box"), card = cutToFit),
                ),
            ),
            // 23: "And the events inside? Tell me their timeboxes."
            Scene(
                23, "The events inside", timeline(),
                beats = listOf(
                    Beat(24, show = events + "next", light = events, card = timeboxes),
                ),
            ),
            // 25: "Some teams have a Sprint Zero."
            Scene(
                25, "Not in the Guide", myths(),
                beats = listOf(
                    Beat(26, show = setOf("zero", "harden", "inc", "burn"), light = setOf("zero", "harden"), card = notInGuide),
                ),
            ),
            // 27: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                27, "For the exam", exam(),
                beats = listOf(
                    Beat(28, show = all4, light = all4, card = takeaways),
                    Beat(30, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-sprint--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "How long things last", timeline(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "How long should things last?")),
                    Beat(2, show = setOf("next"), light = setOf("sprint", "next"), card = BoardCard("The Sprint", "The heartbeat of Scrum; the next starts as the last ends.")),
                ),
            ),
            // 4: "Then a quiz, exam style. What's the longest a Sprint may be?"
            Scene(
                4, "Quiz: how long?", length(),
                beats = listOf(
                    Beat(4, show = setOf("long", "short"), light = setOf("long"), card = BoardCard("Quiz: the longest Sprint?", "What's the maximum length?")),
                    Beat(5, light = setOf("short"), card = BoardCard("One month or less", "Inspect and adapt at least once a calendar month.")),
                    Beat(7, show = setOf("box", "undone"), light = setOf("box"), card = BoardCard("Quiz: stretch two days?", "A feature is two days from done.")),
                    Beat(9, light = setOf("undone"), pulse = "box" to "undone", card = BoardCard("No kiss for that one", "Fixed length. Unfinished work goes back to the backlog.")),
                ),
            ),
            // 11: "The Sprint Goal. The single objective for the Sprint."
            Scene(
                11, "The Sprint Goal", rules(),
                beats = listOf(
                    Beat(11, show = setOf("goal"), light = setOf("goal"), card = BoardCard("Sprint Goal", "The single objective: record a run offline, sync it later.")),
                    Beat(12, card = BoardCard("Quiz: a banner mid-Sprint", "Marketing wants it squeezed in. What happens?")),
                    Beat(13, show = setOf("quality", "refine", "scope"), light = setOf("goal", "quality", "refine", "scope"), card = fourRules),
                    Beat(16, light = setOf("refine"), card = refinement),
                    Beat(18, light = setOf("quality"), card = BoardCard("Never skip tests", "Quality holds: the Definition of Done. Trim scope instead.")),
                ),
            ),
            // 20: "My question. Who can cancel a Sprint?"
            Scene(
                20, "Quiz: who cancels?", cancel(),
                beats = listOf(
                    Beat(20, show = setOf("sm", "bad"), light = setOf("sm"), card = BoardCard("Quiz: who can cancel?", "The Scrum Master, when it's going badly?")),
                    Beat(22, show = setOf("goal", "po"), light = setOf("goal", "po"), pulse = "goal" to "po", card = onlyPo),
                ),
            ),
            // 23: "Then let me win you back. Name the events..."
            Scene(
                23, "The events inside", timeline(),
                beats = listOf(
                    Beat(24, show = events + "next", light = events, card = timeboxes),
                ),
            ),
            // 26: "Last one. My old team ran a Sprint Zero..."
            Scene(
                26, "Not in the Guide", myths(),
                beats = listOf(
                    Beat(27, show = setOf("zero", "harden", "inc", "burn"), light = setOf("zero", "harden"), card = notInGuide),
                    Beat(29, light = setOf("inc"), card = cutToFit),
                ),
            ),
            // 30: "Then, for the exam. What do we remember?"
            Scene(
                30, "For the exam", exam(),
                beats = listOf(
                    Beat(31, show = all4, light = all4, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
