package com.shahaabapps.core.content

/** The Sprint Backlog and the Sprint Goal: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesSprintBacklog {
    private val classic = Podcast(
        "psm-sprint-backlog",
        listOf(
            maya("Hi, and welcome back. Today: the Sprint Backlog, and its commitment, the Sprint Goal. Our team builds a running app for Android with a small backend, in Sprints of two weeks. The Product Backlog holds everything the app might ever need. The Sprint Backlog holds this Sprint."),
            leo("So it's the Product Backlog, just shorter? The top ten items?"),
            maya("That's the most common mix-up, and the exam knows it. The Sprint Backlog has three parts. The Sprint Goal, which is why. The Product Backlog items picked for this Sprint, which is what. And an actionable plan for delivering the Increment, which is how. Items alone are only one part."),
            leo("Increment. In plain words?"),
            maya("A finished, usable piece of the product, one that meets the team's Definition of Done. Picture a new working version of the app you could hand a runner tomorrow. The Increment episode goes deep on that."),
            leo("Okay. And who owns this thing?"),
            maya("The Scrum Guide says the Sprint Backlog is a plan by and for the Developers. They build it, they change it, they own it. Not the Scrum Master, not a manager, not the Product Owner. Nobody else adds tasks to it or hands out work inside it."),
            leo("Let's start with the why. The Sprint Goal."),
            maya("The Sprint Goal is the single objective for the Sprint. It's the Sprint Backlog's commitment, the thing that gives it focus. For example: runners can save a run when they have no signal. One sentence."),
            leo("Why bother with a sentence, if we already have the list of items?"),
            maya("Because a list is ten separate jobs, and a goal makes them one. The Guide says the Sprint Goal creates coherence and focus, and encourages the team to work together rather than on separate initiatives. Picture a rowing crew: eight people, one finish line."),
            leo("And who writes it? The Product Owner, I'd guess."),
            maya("The Product Owner proposes how the product could increase its value this Sprint, and then the whole Scrum Team collaborates to define the Sprint Goal. It's crafted in Sprint Planning, and it must be finalised before Sprint Planning ends."),
            leo("Now the what."),
            maya("The items the Developers select from the Product Backlog, the ones that serve the goal. Say you're building that offline feature: store the run on the phone, sync it when the signal comes back, show a saved offline message."),
            leo("How many items? In my old job the manager decided that."),
            maya("The Developers select them, through discussion with the Product Owner. The Guide says the more they know about their past performance, their upcoming capacity and their Definition of Done, the more confident their forecast. The people doing the work say how much fits."),
            leo("And the how?"),
            maya("The plan. Developers usually break each item into smaller pieces of work, often a day or less. Like the steps of a recipe: add a table on the phone for unsent runs, write the sync job, test it with flight mode on. How they do this is up to the Developers alone."),
            leo("Is the plan fixed once Sprint Planning ends?"),
            maya("No. It's emergent, which means it changes as they learn. The Developers update it throughout the Sprint. The Guide calls it a highly visible, real-time picture of the work. And it needs enough detail to inspect progress at the Daily Scrum."),
            leo("Daily Scrum. Remind me?"),
            maya("The fifteen-minute event for the Developers, every day. They inspect progress toward the Sprint Goal and adapt the Sprint Backlog. It's not the only time they can adapt it, but it's the daily check. The Daily Scrum episode covers it."),
            leo("Wait. If the plan keeps changing, what is the commitment for?"),
            maya("[pause] Here's the heart of it. The Sprint Goal stays fixed. The flexibility lives in the work. If the work turns out different from what they expected, the Developers collaborate with the Product Owner to negotiate the scope of the Sprint Backlog, without affecting the Sprint Goal."),
            leo("Give me that with the running app."),
            maya("Mid-Sprint, the sync turns out harder than planned. Android limits what apps can do in the background. So the Developers talk to the Product Owner, drop the fancy saved offline animation, and keep a plain message. The goal still holds: runners can save a run with no signal."),
            leo("And if marketing wants a promo banner, mid-Sprint?"),
            maya("It goes to the Product Owner and onto the Product Backlog, for a later Sprint. During the Sprint, no changes are made that would endanger the Sprint Goal. And the Sprint Backlog belongs to the Developers, not to whoever shouts loudest."),
            leo("What if the goal itself stops making sense? A rival ships the same feature."),
            maya("Then the Sprint Goal may be obsolete, and only the Product Owner can cancel the Sprint. That's rare. The Sprint episode covers cancelling. Nobody quietly swaps in a new goal halfway."),
            leo("Burn-down charts? Task boards? Do we need those?"),
            maya("Optional. The Guide mentions burn-downs only as one of several ways teams forecast progress, and says they don't replace empiricism, learning from what actually happened. A task board is a fine way to keep the plan visible. Scrum doesn't require any particular tool."),
            leo("So what do I remember for the exam?"),
            maya("Who owns and changes the Sprint Backlog: the Developers. Its three parts: the Sprint Goal, the selected items, and the plan; why, what and how. Its commitment: the Sprint Goal, the single objective. Work turns out different: renegotiate scope with the Product Owner, the goal stays. Goal obsolete: only the Product Owner cancels."),
            leo("And anything to try today?"),
            maya("Write a one-sentence Sprint Goal for your next two weeks of work. Pick the backlog items that serve it. Break the first item into steps of a day or less. Then each morning, update the plan against the goal."),
            leo("A fixed goal, a flexible plan. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-sprint-backlog",
        listOf(
            maya("Welcome to Case Files. Today's case: The Sprint That Did Everything and Finished Nothing. A team builds a running app for Android with a small backend. Two-week Sprint, twelve items started. At the Sprint Review, not one was finished."),
            leo("[sigh] Detective Leo, reporting. First suspect: the developers are slow. Give them fewer items."),
            maya("Let's read the evidence first. Exhibit one: the Sprint Backlog itself. Twelve Product Backlog items, copied from the top of the list. Nothing else. No goal, no plan."),
            leo("Isn't that what a Sprint Backlog is? The items for the Sprint?"),
            maya("Only one third of it. The Scrum Guide gives it three parts: the Sprint Goal, which is why; the selected Product Backlog items, which is what; and an actionable plan for delivering the Increment, which is how."),
            leo("Increment, in plain words?"),
            maya("A finished, usable piece of the product, meeting the Definition of Done. A working version you could hand a runner tomorrow. This team had none."),
            leo("So, no goal. What should it have been?"),
            maya("The Sprint Goal is the single objective for the Sprint, and the Sprint Backlog's commitment. Something like: runners can save a run when they have no signal. Without it, twelve items were twelve separate projects, and six developers chased six of them."),
            leo("Who should have written that goal?"),
            maya("In Sprint Planning, the Product Owner proposes how the product could gain value, and the whole Scrum Team collaborates to define the Sprint Goal. It's finalised before Sprint Planning ends. The Guide says it brings coherence and focus, so people work together, not on separate initiatives."),
            leo("Second suspect: they took too much. Someone should have stopped them."),
            maya("Closer, but look at who chose. Exhibit two: an email from the engineering manager, listing the twelve items. The Guide says the Developers select items, through discussion with the Product Owner. They know their past performance and capacity. Nobody outside picks for them."),
            leo("Exhibit three?"),
            maya("The task board. On day one, the Scrum Master put every task on it, with a name beside each. And the board never changed again."),
            leo("Two crimes in one board."),
            maya("Two. First: the Sprint Backlog is a plan by and for the Developers. The Scrum Master doesn't assign work; the Developers decide who does what. Second: the plan is emergent. It should change as they learn, updated throughout the Sprint, a real-time picture of the work."),
            leo("How would they have known it was drifting?"),
            maya("At the Daily Scrum, the fifteen-minute event for the Developers. They inspect progress toward the Sprint Goal and adapt the Sprint Backlog. With no goal to inspect against, and a frozen board, the Daily Scrum became a status report to the manager."),
            leo("Exhibit four, I can feel it."),
            maya("Day six. The Product Owner added two urgent items straight into the Sprint, and swapped the goal, well, the goal they didn't have, for a new one about a promo banner."),
            leo("Third suspect: the Product Owner. Finally."),
            maya("Partly. The Sprint Goal stays fixed during the Sprint; no changes that endanger it. New ideas go onto the Product Backlog. What can move is scope: if the work turns out different, the Developers and the Product Owner negotiate the scope without touching the goal."),
            leo("And if the goal really is dead?"),
            maya("Then it's obsolete, and only the Product Owner may cancel the Sprint. That's the edge of the idea. They don't swap goals quietly on day six."),
            leo("Last suspect. The Sprint was too short. Extend it a week."),
            maya("[pause] Never. A Sprint has a fixed length, one month or less, and isn't stretched to finish work. Unfinished items go back to the Product Backlog. The Sprint episode covers that."),
            leo("What did the fixed version look like?"),
            maya("Next Sprint: the goal, runners can save a run with no signal. Four items. The Developers broke them into tasks of a day or less. When the sync turned out harder than planned, they agreed with the Product Owner to drop the fancy animation. Goal met."),
            leo("And their burn-down chart? Did that save them?"),
            maya("A burn-down is optional. The Guide mentions it only as one way to forecast progress, and says it doesn't replace empiricism. What saved them was a goal, and a plan they owned."),
            leo("And the twelve unfinished items? Where did they go?"),
            maya("Anything not meeting the Definition of Done goes back to the Product Backlog, for the Product Owner to order again. And remember, the selected items were a forecast. What the Developers commit to is the Sprint Goal, not every single item."),
            leo("Could the Product Owner have helped plan the how?"),
            maya("They can be in the room to clarify items and trade-offs, and the team may invite others to give advice. But how the work gets done is up to the Developers alone."),
            leo("So, the case summary for the exam?"),
            maya("Who owns and changes the Sprint Backlog: the Developers. Its three parts: the Sprint Goal, the selected items, and the plan; why, what and how. Its commitment: the Sprint Goal, the single objective. Work turns out different: renegotiate scope with the Product Owner, the goal stays. Goal obsolete: only the Product Owner cancels."),
            leo("Case closed. Anything to try today?"),
            maya("Write a one-sentence Sprint Goal for your next two weeks of work. Pick the backlog items that serve it. Break the first item into steps of a day or less. Then each morning, update the plan against the goal."),
            leo("One goal, a living plan. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-sprint-backlog",
        listOf(
            maya("Okay, did you hear about the running app team's Sprint? Android app, small backend, two weeks. Everybody had their hands on the Sprint Backlog. Everybody."),
            leo("[laugh] I heard there was a promo banner involved. Start me off. What is the Sprint Backlog?"),
            maya("Three things together. The Sprint Goal, which is why. The Product Backlog items picked for the Sprint, which is what. And an actionable plan for delivering the Increment, which is how. The Scrum Guide calls it a plan by and for the Developers."),
            leo("Increment, quickly?"),
            maya("A finished, usable piece of the product that meets the Definition of Done. A working version you could hand a runner. Now. Monday."),
            leo("Who was Monday?"),
            maya("Tom, the engineering manager. He walked up to the board and added three tasks of his own, with names on them. But the Sprint Backlog belongs to the Developers. They decide who does what. A manager doesn't assign work inside it."),
            leo("Poor Tom. And the goal? Did they have one?"),
            maya("They did, and it was good. Runners can save a run when they have no signal. The Sprint Goal is the single objective for the Sprint, the Sprint Backlog's commitment. The whole Scrum Team crafted it in Sprint Planning."),
            leo("What's a goal for, though? They had the items."),
            maya("Items are separate jobs; a goal makes them one. The Guide says it creates coherence and focus, and gets people working together rather than on separate initiatives. Picture a rowing crew: eight people, one finish line."),
            leo("Tuesday?"),
            maya("Tuesday was Dana, the Product Owner. Marketing begged for a promo banner, so she announced a new Sprint Goal, the banner, on day four."),
            leo("Ouch. Can't she? She owns the product."),
            maya("She owns the Product Backlog. The Sprint Goal stays fixed during the Sprint: no changes that endanger it. The banner goes on the Product Backlog for a later Sprint. And if a goal truly becomes obsolete, the Product Owner may cancel the Sprint. Swapping goals mid-Sprint isn't an option."),
            leo("Wednesday?"),
            maya("Marcus. He wrote the plan on day one, every task, and refused to touch it. The plan is emergent: the Developers update it throughout the Sprint, as they learn. The Guide calls it a real-time picture of the work."),
            leo("And when did they check it?"),
            maya("At the Daily Scrum, the fifteen-minute event for the Developers. They inspect progress toward the Sprint Goal and adapt the Sprint Backlog. Marcus's plan was out of date by Thursday."),
            leo("[chuckle] Thursday, then. Someone redeem this team."),
            maya("Priya. The sync turned out harder than expected, because Android limits apps in the background. She didn't stretch the Sprint or panic. She and the other Developers went to Dana and negotiated scope: drop the fancy saved offline animation, keep a plain message."),
            leo("And the goal?"),
            maya("Untouched. That's the move the Guide describes: if the work turns out different, the Developers collaborate with the Product Owner to negotiate the scope of the Sprint Backlog, without affecting the Sprint Goal. Fixed goal, flexible work."),
            leo("So Priya broke a promise? Not everything got built as planned."),
            maya("No. The items are a forecast; the Sprint Goal is the commitment, a commitment by the Developers. They kept it. The animation went back to the Product Backlog as a new item, for Dana to order again."),
            leo("How do they decide how much to take in the first place?"),
            maya("The Developers select items, through discussion with the Product Owner. Their past performance, their capacity and their Definition of Done make the forecast more confident. Then they break items into pieces of work, often a day or less. How is up to them alone."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. The intern said Scrum requires a burn-down chart, and redrew one in three colours. Burn-downs are optional. The Guide mentions them only as one way to forecast progress, and says they don't replace empiricism."),
            leo("Empiricism meaning?"),
            maya("Learning from what actually happened, and deciding from that. The Sprint Planning episode covers how the goal and the items get picked in the first place."),
            leo("Anything else the intern got wrong? I sense more."),
            maya("[chuckle] The intern thought the Scrum Master should run the plan and tick off tasks for everyone. No. The Scrum Master helps the team manage itself, and removes what blocks it. The Sprint Backlog stays the Developers' own."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Who owns and changes the Sprint Backlog: the Developers. Its three parts: the Sprint Goal, the selected items, and the plan; why, what and how. Its commitment: the Sprint Goal, the single objective. Work turns out different: renegotiate scope with the Product Owner, the goal stays. Goal obsolete: only the Product Owner cancels."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Write a one-sentence Sprint Goal for your next two weeks of work. Pick the backlog items that serve it. Break the first item into steps of a day or less. Then each morning, update the plan against the goal."),
            leo("Hands off the board, Tom. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-sprint-backlog",
        listOf(
            nova("Hey, you. It's late, and the Sprint is half over. [pause] Tonight we talk about the plan you carry through it. The Sprint Backlog, and the one goal it keeps."),
            ivy("Mm. One goal. I like a night with one goal."),
            nova("[chuckle] Then you'll like this. The Scrum Guide says the Sprint Backlog is made of three things. The Sprint Goal, which is why. The Product Backlog items chosen for the Sprint, which is what. And an actionable plan for delivering the Increment, which is how."),
            ivy("Increment. Say it softly, and tell me what it is."),
            nova("A finished, usable piece of the product. It meets the team's Definition of Done. Picture a new working version of your running app that you could hand someone tomorrow morning."),
            ivy("And who does the plan belong to?"),
            nova("The Developers. The Guide calls it a plan by and for the Developers. [pause] Not the manager. Not the Scrum Master. They build it, they change it, they decide who does what."),
            ivy("Tell me about the goal, then."),
            nova("The Sprint Goal is the single objective for the Sprint. It's the Sprint Backlog's commitment. For your running app, maybe: runners can save a run when they have no signal. One sentence. It's what you whisper to yourself on day eight when it all gets messy."),
            ivy("Why a goal, and not just the list?"),
            nova("Because a list is ten separate jobs, and a goal makes them one. The Guide says it brings coherence and focus, so people work together rather than on separate initiatives. Like a rowing crew at dawn: [pause] eight people, one finish line."),
            ivy("Who chose it?"),
            nova("The Product Owner proposes how the product could gain value this Sprint. Then the whole Scrum Team crafts the goal together, in Sprint Planning. It's settled before Sprint Planning ends."),
            ivy("And the items. Who decides how many?"),
            nova("The Developers, talking it through with the Product Owner. They know their past performance, their capacity, their Definition of Done. That makes the forecast more honest. Then they break the items into small pieces, often a day or less. How, is entirely theirs."),
            ivy("Show me the pieces."),
            nova("Store unsent runs on the phone. Write the job that syncs them when the signal comes back. Test it with flight mode on. A plan you could follow half asleep."),
            ivy("And is the list of items a promise? Every single one?"),
            nova("[pause] No. The items are a forecast. What the Developers commit to is the Sprint Goal. If an item can't be Done, it goes back to the Product Backlog, to be ordered again."),
            ivy("Who else can be in the room, when the plan is made?"),
            nova("The whole Scrum Team plans together, and they may invite others to give advice. Someone from the backend, a designer. [pause] But the how stays with the Developers. And the Scrum Master doesn't hand out tasks; they help the team manage itself."),
            ivy("And once it's written, it's done?"),
            nova("Never. The plan is emergent: it changes as you learn. The Developers update it throughout the Sprint. The Guide calls it a highly visible, real-time picture of the work. [pause] And at the Daily Scrum, fifteen minutes for the Developers, they inspect progress toward the goal and adapt the plan."),
            ivy("Then what holds still, if everything moves?"),
            nova("The goal. That's the secret. The Sprint Goal stays fixed; the flexibility lives in the work. If the work turns out different from what they expected, the Developers talk with the Product Owner and negotiate the scope, without touching the goal."),
            ivy("Tell me how that feels, on a real night."),
            nova("Day seven. The sync is harder than anyone thought; Android limits what apps do in the background. So they drop the fancy saved offline animation, keep a plain message, and the goal still holds. Runners can save a run with no signal."),
            ivy("And if someone knocks on the door mid-Sprint, with a new idea?"),
            nova("It goes to the Product Owner, and onto the Product Backlog for later. Nothing changes that would endanger the Sprint Goal. And if the goal itself truly dies, only the Product Owner can cancel the Sprint. The Sprint episode is another late night."),
            ivy("Do I need a burn-down chart to sleep well?"),
            nova("[pause] No. The Guide mentions burn-downs only as one way to forecast, and says they don't replace empiricism, learning from what actually happened. A task board helps you see. Nothing is required but the plan."),
            ivy("[sigh] Mm. Before I go. What do I remember for the exam?"),
            nova("Who owns and changes the Sprint Backlog: the Developers. Its three parts: the Sprint Goal, the selected items, and the plan; why, what and how. Its commitment: the Sprint Goal, the single objective. Work turns out different: renegotiate scope with the Product Owner, the goal stays. Goal obsolete: only the Product Owner cancels."),
            ivy("And something to try today, before I close my eyes?"),
            nova("Write a one-sentence Sprint Goal for your next two weeks of work. [pause] Pick the backlog items that serve it. Break the first item into steps of a day or less. Then each morning, update the plan against the goal."),
            ivy("One goal, held all night. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-sprint-backlog",
        listOf(
            nova("Tonight is date night, darling. Two weeks together, one goal. Sound familiar?"),
            rafe("A Sprint. You're describing a Sprint. How romantic."),
            nova("[laugh] And the plan we carry through it is the Sprint Backlog. Three parts: the Sprint Goal, which is why; the Product Backlog items we picked, which is what; and an actionable plan for delivering the Increment, which is how."),
            rafe("And the Increment is?"),
            nova("A finished, usable piece of the product that meets the Definition of Done. For our running app, a new working version we could hand a runner tomorrow."),
            rafe("And who's at the table when we plan?"),
            nova("The whole Scrum Team, and anyone we invite for advice. Your mother included, as an adviser. [pause] Only as an adviser."),
            rafe("Quiz me, then. I'm feeling committed."),
            nova("Exam style. Who owns the Sprint Backlog, and may change it during the Sprint?"),
            rafe("The Developers. It's a plan by and for the Developers. They decide who does what. No manager, no Scrum Master, hands off."),
            nova("Right answer. Hands off, except mine. [kiss]"),
            rafe("And the Scrum Master? Do they hand out the chores?"),
            nova("No. The Scrum Master coaches the team to manage itself, and removes what blocks it. The chores, the tasks, the Developers share out themselves."),
            rafe("My turn. The Sprint Backlog's commitment. What is it?"),
            nova("The Sprint Goal. The single objective for the Sprint. Ours: runners can save a run when they have no signal. It brings coherence and focus, so the team works together rather than on separate initiatives."),
            rafe("Right answer. One objective, and it's you. [kiss]"),
            nova("Smooth. And who crafts the goal?"),
            rafe("The Product Owner, alone, the night before."),
            nova("No kiss for that one. The Product Owner proposes how the product could gain value, then the whole Scrum Team collaborates on the goal, in Sprint Planning. It's finalised before Sprint Planning ends. Teamwork, darling."),
            rafe("I'll learn to share. And the items? Who decides how many go in?"),
            nova("The Developers, in discussion with the Product Owner. Their past performance, capacity and Definition of Done make the forecast more confident. Then they break items into small pieces, often a day or less. How is theirs alone."),
            rafe("Pieces like what?"),
            nova("Store unsent runs on the phone, write the job that syncs them when the signal returns, test it in flight mode. Now you. Exam style. Midway through the Sprint, the work turns out harder than expected. What happens?"),
            rafe("The Developers collaborate with the Product Owner to negotiate the scope of the Sprint Backlog, without affecting the Sprint Goal. The goal is fixed. The flexibility lives in the work."),
            nova("Right answer. Flexible where it counts. [kiss]"),
            rafe("Say ours. The sync is harder than we thought, because Android limits apps in the background?"),
            nova("We drop the fancy saved offline animation, keep a plain message, and the goal still holds. Runners save their runs with no signal. The animation goes back on the Product Backlog, for another night."),
            rafe("Then let me try one. Marketing wants a promo banner, day four. The Product Owner can just add it to the Sprint and swap the goal."),
            nova("No kiss. During the Sprint, no changes that endanger the Sprint Goal. The banner goes on the Product Backlog. And if a goal really becomes obsolete, only the Product Owner may cancel the Sprint. The Sprint episode covers that."),
            rafe("And the plan itself? Can it change?"),
            nova("All the time. It's emergent: it changes as you learn. The Developers update it throughout the Sprint, a real-time picture of the work. And at the Daily Scrum, fifteen minutes for the Developers, they inspect progress toward the goal and adapt it."),
            rafe("Quiz me once more. I'm on a roll."),
            nova("Exam style. The Developers forecast eight items for the Sprint. Is that a promise to finish all eight?"),
            rafe("No. The items are a forecast. The Sprint Goal is the commitment, a commitment by the Developers. Anything not Done goes back to the Product Backlog."),
            nova("Right answer. You know what you're committing to. [kiss]"),
            rafe("Then a question for you. Is a burn-down chart required?"),
            nova("No. The Guide mentions burn-downs only as one way to forecast progress, and they don't replace empiricism, learning from what really happened."),
            rafe("Right answer. Nothing burns down here, love. [kiss]"),
            nova("Last question, for the exam. What do we remember?"),
            rafe("Who owns and changes the Sprint Backlog: the Developers. Its three parts: the Sprint Goal, the selected items, and the plan; why, what and how. Its commitment: the Sprint Goal, the single objective. Work turns out different: renegotiate scope with the Product Owner, the goal stays. Goal obsolete: only the Product Owner cancels."),
            nova("Five for five, and six kisses tonight, darling. That earns a long one. [kiss] And something to try today?"),
            rafe("Write a one-sentence Sprint Goal for your next two weeks of work. Pick the backlog items that serve it. Break the first item into steps of a day or less. Then each morning, update the plan against the goal."),
            nova("A fixed goal, and a flexible you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun parts() = DiagramSpec(
        "The Sprint Backlog: the Sprint Goal (why), the selected items (what) and the plan (how), owned by the Developers.",
        nodes = listOf(
            DNode("goal", "Sprint Goal", 0.32f, 0.16f, w = 0.56f, h = 0.2f, tone = 1, sub = "why"),
            DNode("items", "Selected items", 0.32f, 0.5f, w = 0.56f, h = 0.2f, sub = "what"),
            DNode("plan", "The plan", 0.32f, 0.84f, w = 0.56f, h = 0.2f, sub = "how"),
            DNode("devs", "Developers", 0.81f, 0.5f, w = 0.36f, h = 0.24f, tone = 2, sub = "own it, change it"),
        ),
        edges = listOf(
            DEdge("items", "goal", "serve"),
            DEdge("plan", "items", "delivers"),
            DEdge("devs", "plan", "own"),
            DEdge("devs", "items", "select"),
        ),
        aspect = 0.8f,
    )

    private fun goalFocus() = DiagramSpec(
        "The Sprint Goal turns separate items into one objective, crafted by the whole Scrum Team.",
        nodes = listOf(
            DNode("a", "Save offline", 0.17f, 0.15f, w = 0.3f, h = 0.18f),
            DNode("b", "Sync later", 0.5f, 0.15f, w = 0.3f, h = 0.18f),
            DNode("c", "Saved message", 0.83f, 0.15f, w = 0.3f, h = 0.18f),
            DNode("goal", "Sprint Goal", 0.5f, 0.54f, w = 0.8f, h = 0.2f, tone = 1, sub = "runs saved with no signal"),
            DNode("who", "Crafted by the whole Scrum Team", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("a", "goal"),
            DEdge("b", "goal"),
            DEdge("c", "goal"),
        ),
        aspect = 0.8f,
    )

    private fun plan() = DiagramSpec(
        "The Developers break an item into work of a day or less, and adapt that plan at every Daily Scrum.",
        nodes = listOf(
            DNode("item", "Offline runs", 0.5f, 0.13f, w = 0.5f, h = 0.18f, tone = 1, sub = "one item"),
            DNode("t1", "Store unsent runs", 0.17f, 0.5f, w = 0.3f, h = 0.18f),
            DNode("t2", "Sync job", 0.5f, 0.5f, w = 0.3f, h = 0.18f),
            DNode("t3", "Flight mode test", 0.83f, 0.5f, w = 0.3f, h = 0.18f),
            DNode("daily", "Daily Scrum", 0.5f, 0.85f, w = 0.6f, h = 0.18f, tone = 2, sub = "inspect, adapt"),
        ),
        edges = listOf(
            DEdge("item", "t1"),
            DEdge("item", "t2"),
            DEdge("item", "t3"),
            DEdge("daily", "t2", "adapt"),
        ),
        aspect = 0.8f,
    )

    private fun scope() = DiagramSpec(
        "The Sprint Goal stays fixed; scope is negotiated with the Product Owner, and leftovers return to the Product Backlog.",
        nodes = listOf(
            DNode("goal", "Sprint Goal", 0.5f, 0.14f, w = 0.6f, h = 0.2f, tone = 1, sub = "fixed"),
            DNode("scope", "Scope", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "negotiable"),
            DNode("po", "Product Owner", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "negotiates"),
            DNode("pb", "Product Backlog", 0.25f, 0.84f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "ideas, leftovers"),
            DNode("cancel", "Goal obsolete", 0.75f, 0.84f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "Product Owner cancels"),
        ),
        edges = listOf(
            DEdge("scope", "goal", "serves"),
            DEdge("po", "scope", "negotiate"),
            DEdge("scope", "pb", "back to"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Sprint Backlog questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("owns", "Who owns it", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Developers"),
            DNode("parts", "Three parts", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "why, what, how"),
            DNode("commit", "Its commitment", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Sprint Goal"),
            DNode("items", "Selected items", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a forecast"),
            DNode("harder", "Work turns out different", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "negotiate scope"),
            DNode("obsolete", "Goal obsolete", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Product Owner cancels"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("owns", "parts", "commit", "items", "harder", "obsolete")
    private val three = setOf("goal", "items", "plan")
    private val jobs = setOf("a", "b", "c")
    private val tasks = setOf("t1", "t2", "t3")
    private val takeaways = BoardCard("Listen for the phrase", "Developers own it. Goal fixed, scope negotiable. Obsolete goal: Product Owner cancels.")
    private val today = BoardCard("Today", "A one-line Sprint Goal, its items, a plan in days, updated daily.")
    private val threeParts = BoardCard("Why, what, how", "The Sprint Goal, the selected items, and a plan for the Increment.")
    private val increment = BoardCard("Increment", "A finished, usable piece of the product that meets the Definition of Done.")
    private val byAndFor = BoardCard("By and for the Developers", "They build it, change it and own it. Nobody assigns work in it.")
    private val goalCard = BoardCard("Sprint Goal", "The single objective for the Sprint: the Sprint Backlog's commitment.")
    private val coherence = BoardCard("Coherence and focus", "Separate jobs become one: a rowing crew, one finish line.")
    private val whoCrafts = BoardCard("Who crafts it", "The whole Scrum Team, in Sprint Planning, set before it ends.")
    private val howMany = BoardCard("How many items", "The Developers select them, with the Product Owner. A forecast.")
    private val theHow = BoardCard("The how", "Items broken into work of a day or less. Up to the Developers.")
    private val emergent = BoardCard("Emergent", "Updated all Sprint long: a real-time picture of the work.")
    private val daily = BoardCard("Daily Scrum", "Fifteen minutes: inspect progress to the goal, adapt the plan.")
    private val heart = BoardCard("Fixed goal, flexible work", "The Sprint Goal stays; the flexibility lives in the work.")
    private val negotiate = BoardCard("Negotiate scope", "Drop the fancy animation, keep a plain message. The goal holds.")
    private val obsolete = BoardCard("Goal obsolete", "Only the Product Owner can cancel the Sprint.")
    private val forecast = BoardCard("A forecast", "The items are a forecast; the Sprint Goal is the commitment.")
    private val burnDown = BoardCard("Burn-down charts", "One optional way to forecast. They don't replace empiricism.")

    private fun classicBoard() = PodcastBoard(
        "psm-sprint-backlog",
        listOf(
            // 0: "Hi, and welcome back. Today: the Sprint Backlog..."
            Scene(
                0, "Three parts", parts(),
                beats = listOf(
                    Beat(0, card = BoardCard("Sprint Backlog", "This Sprint's work. The Product Backlog holds everything else.")),
                    Beat(2, show = three, light = three, card = threeParts),
                    Beat(4, card = increment),
                    Beat(6, show = setOf("devs"), light = setOf("devs"), pulse = "devs" to "plan", card = byAndFor),
                ),
            ),
            // 7: "Let's start with the why. The Sprint Goal."
            Scene(
                7, "The Sprint Goal", goalFocus(),
                beats = listOf(
                    Beat(8, show = setOf("goal"), light = setOf("goal"), card = goalCard),
                    Beat(10, show = jobs, light = jobs + "goal", pulse = "a" to "goal", card = coherence),
                    Beat(12, show = setOf("who"), light = setOf("who"), card = whoCrafts),
                ),
            ),
            // 13: "Now the what."
            Scene(
                13, "The what", parts(),
                beats = listOf(
                    Beat(14, light = setOf("items"), pulse = "items" to "goal", card = BoardCard("The what", "Items that serve the goal: store, sync, a saved offline message.")),
                    Beat(16, light = setOf("devs", "items"), pulse = "devs" to "items", card = howMany),
                ),
            ),
            // 17: "And the how?"
            Scene(
                17, "The how", plan(),
                beats = listOf(
                    Beat(18, show = tasks, light = tasks, pulse = "item" to "t1", card = theHow),
                    Beat(20, card = emergent),
                    Beat(22, show = setOf("daily"), light = setOf("daily"), pulse = "daily" to "t2", card = daily),
                ),
            ),
            // 23: "Wait. If the plan keeps changing, what is the commitment for?"
            Scene(
                23, "Fixed goal, flexible work", scope(),
                beats = listOf(
                    Beat(24, light = setOf("goal"), card = heart),
                    Beat(26, show = setOf("scope", "po"), light = setOf("scope", "po"), pulse = "po" to "scope", card = negotiate),
                    Beat(28, show = setOf("pb"), light = setOf("pb"), pulse = "scope" to "pb", card = BoardCard("A promo banner?", "To the Product Owner and the Product Backlog. Not this Sprint.")),
                    Beat(30, show = setOf("cancel"), light = setOf("cancel"), card = obsolete),
                ),
            ),
            // 31: "Burn-down charts? Task boards?"
            Scene(
                31, "Charts and boards", parts(),
                beats = listOf(
                    Beat(32, light = setOf("plan"), card = burnDown),
                ),
            ),
            // 33: "So what do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("owns", "parts", "commit", "harder", "obsolete"), card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-sprint-backlog--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Sprint That Did Everything..."
            Scene(
                0, "Everything, and nothing", parts(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Twelve items started; none finished by the Sprint Review.")),
                    Beat(2, show = setOf("items"), light = setOf("items"), card = BoardCard("Exhibit one", "Twelve items copied from the top. No goal, no plan.")),
                    Beat(4, show = setOf("goal", "plan"), light = three, card = threeParts),
                    Beat(6, card = increment),
                ),
            ),
            // 7: "So, no goal. What should it have been?"
            Scene(
                7, "No goal", goalFocus(),
                beats = listOf(
                    Beat(8, show = jobs + "goal", light = setOf("goal"), pulse = "a" to "goal", card = goalCard),
                    Beat(10, show = setOf("who"), light = setOf("who"), card = whoCrafts),
                ),
            ),
            // 11: "Second suspect: they took too much."
            Scene(
                11, "Exhibit two: who chose", parts(),
                beats = listOf(
                    Beat(12, show = setOf("devs"), light = setOf("devs", "items"), pulse = "devs" to "items", card = BoardCard("The Developers select", "With the Product Owner. Not from a manager's email.")),
                ),
            ),
            // 13: "Exhibit three?"
            Scene(
                13, "Exhibit three: the board", plan(),
                beats = listOf(
                    Beat(14, show = tasks, light = tasks, card = BoardCard("Exhibit three", "Names on every task on day one, by the Scrum Master. Never updated.")),
                    Beat(16, pulse = "item" to "t1", card = BoardCard("Two crimes", "The Developers decide who does what, and the plan must change.")),
                    Beat(18, show = setOf("daily"), light = setOf("daily"), pulse = "daily" to "t2", card = daily),
                ),
            ),
            // 19: "Exhibit four, I can feel it."
            Scene(
                19, "Exhibit four: day six", scope(),
                beats = listOf(
                    Beat(20, light = setOf("goal"), card = BoardCard("Day six", "Two urgent items added, and a new goal about a banner.")),
                    Beat(22, show = setOf("scope", "po", "pb"), light = setOf("scope", "po"), pulse = "po" to "scope", card = heart),
                    Beat(24, show = setOf("cancel"), light = setOf("cancel"), card = obsolete),
                    Beat(26, card = BoardCard("Extend the Sprint?", "Never. A fixed length of one month or less.")),
                    Beat(28, light = setOf("scope", "goal"), pulse = "scope" to "goal", card = BoardCard("The fixed version", "One goal, four items, tasks of a day or less. Goal met.")),
                    Beat(30, card = burnDown),
                    Beat(32, light = setOf("pb"), pulse = "scope" to "pb", card = forecast),
                    Beat(34, card = BoardCard("Planning the how", "Others may advise; the how belongs to the Developers.")),
                ),
            ),
            // 35: "So, the case summary for the exam?"
            Scene(
                35, "Case closed", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("owns", "parts", "commit", "harder", "obsolete"), card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-sprint-backlog--gossip",
        listOf(
            // 0: "Okay, did you hear about the running app team's Sprint?"
            Scene(
                0, "Hands on the board", parts(),
                beats = listOf(
                    Beat(0, card = BoardCard("The running app's Sprint", "Two weeks, and everybody's hands on the Sprint Backlog.")),
                    Beat(2, show = three, light = three, card = threeParts),
                    Beat(4, card = increment),
                    Beat(6, show = setOf("devs"), light = setOf("devs", "plan"), pulse = "devs" to "plan", card = BoardCard("Monday: Tom", "Added tasks with names. The Developers decide who does what.")),
                ),
            ),
            // 7: "Poor Tom. And the goal?"
            Scene(
                7, "The goal", goalFocus(),
                beats = listOf(
                    Beat(8, show = setOf("goal"), light = setOf("goal"), card = goalCard),
                    Beat(10, show = jobs + "who", light = jobs + "goal", pulse = "a" to "goal", card = coherence),
                ),
            ),
            // 11: "Tuesday?"
            Scene(
                11, "Tuesday: Dana", scope(),
                beats = listOf(
                    Beat(12, light = setOf("goal"), card = BoardCard("Tuesday: Dana", "A new Sprint Goal, a promo banner, on day four.")),
                    Beat(14, show = setOf("pb", "cancel"), light = setOf("pb", "cancel"), card = BoardCard("The goal stays", "The banner goes on the Product Backlog. Obsolete? She may cancel.")),
                ),
            ),
            // 15: "Wednesday?"
            Scene(
                15, "Wednesday: Marcus", plan(),
                beats = listOf(
                    Beat(16, show = tasks, light = tasks, pulse = "item" to "t1", card = BoardCard("Wednesday: Marcus", "Planned on day one, never touched again. The plan is emergent.")),
                    Beat(18, show = setOf("daily"), light = setOf("daily"), pulse = "daily" to "t2", card = daily),
                ),
            ),
            // 19: "Thursday, then. Someone redeem this team."
            Scene(
                19, "Thursday: Priya", scope(),
                beats = listOf(
                    Beat(20, show = setOf("scope", "po"), light = setOf("scope", "po"), pulse = "po" to "scope", card = BoardCard("Thursday: Priya", "The sync was harder. She negotiated scope with Dana.")),
                    Beat(22, light = setOf("goal"), card = heart),
                    Beat(24, show = setOf("pb"), light = setOf("pb"), pulse = "scope" to "pb", card = forecast),
                ),
            ),
            // 25: "How do they decide how much to take in the first place?"
            Scene(
                25, "How much, and Friday", parts(),
                beats = listOf(
                    Beat(26, show = setOf("devs"), light = setOf("devs", "items"), pulse = "devs" to "items", card = howMany),
                    Beat(28, card = BoardCard("Friday: the intern", "A burn-down in three colours. Optional, not required.")),
                    Beat(30, card = BoardCard("Empiricism", "Learning from what actually happened, and deciding from that.")),
                    Beat(32, light = setOf("devs", "plan"), card = BoardCard("The Scrum Master", "Helps the team manage itself. Doesn't run the plan.")),
                ),
            ),
            // 33: "So, the gossip I should remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("owns", "parts", "commit", "harder", "obsolete"), card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-sprint-backlog--late-night",
        listOf(
            // 0: "Hey, you. It's late, and the Sprint is half over..."
            Scene(
                0, "One goal, all night", parts(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The plan you carry through the Sprint, and its one goal.")),
                    Beat(2, show = three, light = three, card = threeParts),
                    Beat(4, card = increment),
                    Beat(6, show = setOf("devs"), light = setOf("devs"), pulse = "devs" to "plan", card = byAndFor),
                ),
            ),
            // 7: "Tell me about the goal, then."
            Scene(
                7, "The goal", goalFocus(),
                beats = listOf(
                    Beat(8, show = setOf("goal"), light = setOf("goal"), card = goalCard),
                    Beat(10, show = jobs, light = jobs + "goal", pulse = "a" to "goal", card = coherence),
                    Beat(12, show = setOf("who"), light = setOf("who"), card = whoCrafts),
                ),
            ),
            // 13: "And the items. Who decides how many?"
            Scene(
                13, "The items and the pieces", plan(),
                beats = listOf(
                    Beat(14, light = setOf("item"), card = howMany),
                    Beat(16, show = tasks, light = tasks, pulse = "item" to "t1", card = theHow),
                    Beat(18, card = forecast),
                    Beat(20, card = BoardCard("Others may advise", "The how stays with the Developers. No tasks from the Scrum Master.")),
                    Beat(22, show = setOf("daily"), light = setOf("daily"), pulse = "daily" to "t2", card = emergent),
                ),
            ),
            // 23: "Then what holds still, if everything moves?"
            Scene(
                23, "What holds still", scope(),
                beats = listOf(
                    Beat(24, light = setOf("goal"), card = heart),
                    Beat(26, show = setOf("scope", "po"), light = setOf("scope", "po"), pulse = "po" to "scope", card = negotiate),
                    Beat(28, show = setOf("pb", "cancel"), light = setOf("pb", "cancel"), pulse = "scope" to "pb", card = BoardCard("A knock on the door", "New ideas go on the Product Backlog. Dead goal: the Product Owner cancels.")),
                    Beat(30, card = burnDown),
                ),
            ),
            // 31: "Before I go. What do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("owns", "parts", "commit", "harder", "obsolete"), card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-sprint-backlog--couples",
        listOf(
            // 0: "Tonight is date night, darling. Two weeks together, one goal."
            Scene(
                0, "Two weeks, one goal", parts(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Two weeks together, one goal: a Sprint.")),
                    Beat(2, show = three, light = three, card = threeParts),
                    Beat(4, card = increment),
                    Beat(6, card = BoardCard("At the table", "The whole Scrum Team, and anyone invited to advise.")),
                    Beat(8, card = BoardCard("Quiz: who owns it?", "And who may change it during the Sprint?")),
                    Beat(9, show = setOf("devs"), light = setOf("devs"), pulse = "devs" to "plan", card = byAndFor),
                    Beat(12, card = BoardCard("The Scrum Master", "Coaches self-management and removes blockers. Hands out no chores.")),
                ),
            ),
            // 13: "My turn. The Sprint Backlog's commitment."
            Scene(
                13, "Quiz: the commitment", goalFocus(),
                beats = listOf(
                    Beat(14, show = jobs + "goal", light = setOf("goal"), pulse = "a" to "goal", card = goalCard),
                    Beat(16, card = BoardCard("Quiz: who crafts it?", "The Product Owner, alone, the night before? No kiss.")),
                    Beat(18, show = setOf("who"), light = setOf("who"), card = whoCrafts),
                ),
            ),
            // 19: "I'll learn to share. And the items?"
            Scene(
                19, "Pieces of the plan", plan(),
                beats = listOf(
                    Beat(20, light = setOf("item"), card = howMany),
                    Beat(22, show = tasks, light = tasks, pulse = "item" to "t1", card = BoardCard("Quiz: harder than expected", "Midway through, the work turns out harder. What happens?")),
                ),
            ),
            // 23: "The Developers collaborate with the Product Owner..."
            Scene(
                23, "Flexible where it counts", scope(),
                beats = listOf(
                    Beat(23, show = setOf("scope", "po"), light = setOf("scope", "po"), pulse = "po" to "scope", card = BoardCard("Negotiate scope", "With the Product Owner, without affecting the Sprint Goal.")),
                    Beat(26, light = setOf("goal"), card = negotiate),
                    Beat(27, card = BoardCard("Quiz: a banner, day four?", "Can the Product Owner add it and swap the goal?")),
                    Beat(28, show = setOf("pb", "cancel"), light = setOf("pb", "cancel"), card = BoardCard("No kiss", "Nothing that endangers the goal. Obsolete: the Product Owner cancels.")),
                ),
            ),
            // 29: "And the plan itself? Can it change?"
            Scene(
                29, "A plan that moves", plan(),
                beats = listOf(
                    Beat(30, show = setOf("daily"), light = setOf("daily"), pulse = "daily" to "t2", card = emergent),
                    Beat(32, card = BoardCard("Quiz: eight items", "A forecast of eight. A promise to finish all eight?")),
                    Beat(33, card = forecast),
                    Beat(35, card = BoardCard("Quiz: burn-down required?", "Does Scrum need one?")),
                    Beat(36, card = burnDown),
                ),
            ),
            // 38: "Last question, for the exam. What do we remember?"
            Scene(
                38, "For the exam", exam(),
                beats = listOf(
                    Beat(39, show = all6, light = setOf("owns", "parts", "commit", "harder", "obsolete"), card = takeaways),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
