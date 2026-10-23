package com.shahaabapps.core.content

/** Sprint Planning: why, what and how: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesSprintPlanning {
    private val classic = Podcast(
        "psm-sprint-planning",
        listOf(
            maya("Hi, and welcome back. Today: Sprint Planning, the event that opens every Sprint. The Scrum Guide, the short free document that defines Scrum, says it lays out the work to be performed for the Sprint, and the plan is made by the whole Scrum Team, together."),
            leo("The whole team? At my last job, planning meant the manager emailed us a spreadsheet of tickets on Monday morning."),
            maya("[chuckle] Then this will feel different. Sprint Planning answers three questions, which the Guide calls topics. Why is this Sprint valuable? What can be Done this Sprint? And how will the chosen work get done?"),
            leo("Why, what, how. Before the details, how long does it take?"),
            maya("Its timebox, the maximum length, is eight hours for a one-month Sprint. For shorter Sprints the event is usually shorter. Picture a small app team on two-week Sprints: they rarely need a full working day."),
            leo("And who's in the room?"),
            maya("The whole Scrum Team: the Product Owner, the Scrum Master and the Developers. The team may also invite other people to give advice, say a backend expert from another team. They advise; they don't plan."),
            leo("Does anyone prepare? Or do we walk in cold?"),
            maya("The Product Owner makes sure everyone is ready to discuss the most important Product Backlog items, and how they connect to the Product Goal. The Product Backlog is the ordered list of everything the product might need; the Product Goal is the product's long-term objective."),
            leo("Okay. Topic one: why."),
            maya("The Product Owner proposes how the product could increase its value and usefulness this Sprint. Then the whole Scrum Team works together to define a Sprint Goal: the single objective that tells stakeholders why this Sprint is valuable."),
            leo("Give me a real Sprint Goal."),
            maya("For example: runners can record a run with no signal, and it syncs later. Notice it's an outcome, not a list of tickets. And the Guide says the Sprint Goal must be finalised before Sprint Planning ends."),
            leo("Who has the final say on it? The Product Owner?"),
            maya("That's a common trap. The Product Owner proposes the value; the whole Scrum Team crafts the Sprint Goal together. Not the Product Owner alone, and certainly not the Scrum Master."),
            leo("Topic two: what."),
            maya("Through discussion with the Product Owner, the Developers select items from the Product Backlog to include in the Sprint. The team may refine those items as it goes, which means breaking them into smaller ones and adding detail."),
            leo("Here's my real doubt. The Product Owner wants twelve items. We think we can do six. Who wins?"),
            maya("The Developers decide how much they take on. The Product Owner can explain, negotiate and help them see the trade-offs, but nobody pushes work onto the Developers. The exam loves this one."),
            leo("But how do we know it's six? It always feels like a guess."),
            maya("The Guide names three things that make the forecast better: the Developers' past performance, their upcoming capacity, and their Definition of Done. Capacity just means how much time they really have. Say one Developer is on holiday for a week: less fits."),
            leo("And why does the Definition of Done matter for how much fits?"),
            maya("Because it's the quality checklist every item must meet: code reviewed, tests passing, runs on the oldest Android version you support. A strict one means each item takes longer, and that's honest. Planning that ignores it is planning to cut corners."),
            leo("Topic three: how."),
            maya("For each selected item, the Developers plan the work needed to create an Increment, a usable piece of the product, that meets the Definition of Done. Often they break items into smaller pieces of work of one day or less."),
            leo("Why one day or less?"),
            maya("Small pieces make progress visible. At the Daily Scrum, a one-day task stuck for three days stands out. A single task called build sync, two weeks long, hides everything until it's too late."),
            leo("And who decides the how? Could the Scrum Master or a tech lead hand down the design?"),
            maya("No. The Guide says how is at the sole discretion of the Developers. Nobody else tells them how to turn Product Backlog items into Increments of value. Advice from an invited expert, yes. Orders, no."),
            leo("So what walks out of the room at the end?"),
            maya("The Sprint Backlog. It's three things together: the Sprint Goal, the Product Backlog items selected for the Sprint, and the plan for delivering them. The Sprint Goal is the commitment that keeps it focused."),
            leo("And is that plan fixed once we leave the room?"),
            maya("No. It's a plan by and for the Developers, and it's updated throughout the Sprint as they learn more. The Daily Scrum is where they adapt it each day. The goal stays put; the plan underneath moves. The Sprint Backlog episode goes deeper."),
            leo("So what do I remember for the exam?"),
            maya("Sprint Planning's timebox: eight hours for a one-month Sprint, shorter for shorter Sprints. Three topics: why, what, how. Who crafts the Sprint Goal: the whole Scrum Team. Who selects how much work: the Developers, with the Product Owner. Who decides how: only the Developers. And the result is the Sprint Backlog: goal, items and plan."),
            leo("And something to try today?"),
            maya("Take your next Sprint and write its Sprint Goal in one sentence, as an outcome. Pick the few Product Backlog items that serve it, and check your team's capacity. Then break one item into pieces of work of one day or less."),
            leo("Why, what, how. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-sprint-planning",
        listOf(
            maya("Welcome to Case Files. Today's case: The Plan Nobody Believed. A team builds an Android running app. Their last Sprint Planning ran two full days, and the Developers walked out not believing a word of the plan. Two weeks later, half the work was unfinished."),
            leo("[chuckle] Detective Leo, on the case. First suspect: lazy Developers. Obviously."),
            maya("[pause] Wrong suspect, and we'll see why. First, the basics. The Scrum Guide, the short free document that defines Scrum, says Sprint Planning opens the Sprint and lays out its work, and the whole Scrum Team creates the plan together. Three topics: why, what and how."),
            leo("And the meeting length? Two days sounds like a crime already."),
            maya("It is. The timebox, the maximum length, is eight hours for a one-month Sprint, and usually shorter for shorter Sprints. This team runs two-week Sprints. Two days was a symptom. Let's find the cause. First exhibit: the Product Backlog."),
            leo("The list of everything the app might need."),
            maya("The ordered list, yes. The Product Owner arrived with twenty items in no order, half of them one-line ideas. The Guide says the Product Owner makes sure attendees are prepared to discuss the most important items, and how they map to the Product Goal, the product's long-term objective."),
            leo("So the first day went on sorting the list."),
            maya("Exactly. Ordering and refining should happen before Planning, during the Sprint before. Refinement means breaking items into smaller ones and adding detail, like a size. Second exhibit: the Sprint Goal. The Scrum Master wrote it the night before and read it out."),
            leo("Second suspect: the Scrum Master. Too controlling."),
            maya("Warmer. Topic one is why this Sprint is valuable. The Product Owner proposes how the product could gain value, and then the whole Scrum Team defines the Sprint Goal together: the single objective for the Sprint. Here it was handed down, so nobody owned it."),
            leo("What would a good one have sounded like?"),
            maya("Runners can record a run with no signal, and it syncs later. An outcome, finalised before Sprint Planning ends. Third exhibit: the selection. The Product Owner insisted on twelve items. The Developers said six. The twelve went in."),
            leo("Third suspect: the Product Owner, then."),
            maya("This time, yes. Topic two is what can be Done this Sprint. Through discussion with the Product Owner, the Developers select the items. The Product Owner can negotiate and explain trade-offs, but the Developers decide how much they take on. Pushing twelve made the plan a fiction."),
            leo("But the Developers' six was only a guess, surely."),
            maya("A guess with evidence. The Guide names three things that sharpen the forecast: past performance, upcoming capacity, and the Definition of Done. Capacity is how much time they really have. One Developer was on holiday for a week. Nobody counted it."),
            leo("And the Definition of Done is the quality checklist."),
            maya("Right: code reviewed, tests passing, runs on the oldest supported Android version. Nobody counted that either, so every item looked smaller than it was. Fourth exhibit: the design. An architect from another team was invited, and he dictated how every item would be built."),
            leo("Is inviting him the crime?"),
            maya("No. The team may invite others to give advice. The crime was the dictating. Topic three is how, and the Guide says how is at the sole discretion of the Developers. Under the picture: for each item, they plan the work to create an Increment, a usable piece of product, that meets the Definition of Done."),
            leo("How small do they break it?"),
            maya("Often into pieces of one day or less. Here, every task was a week long, so nobody saw trouble until it was too late. Small pieces make a stuck task stand out at the Daily Scrum."),
            leo("And when a plan breaks mid-Sprint, like this one did?"),
            maya("It's meant to bend. The Developers update the Sprint Backlog as they learn, and renegotiate scope with the Product Owner if the work turns out bigger. The Sprint Goal holds. Here, nobody renegotiated anything; they just failed quietly."),
            leo("And the output of a healthy Sprint Planning?"),
            maya("The Sprint Backlog: the Sprint Goal, the selected Product Backlog items, and the plan for delivering them. It's a plan by and for the Developers, updated through the Sprint as they learn. The Sprint Backlog episode picks it up from there."),
            leo("So, the case summary for the exam?"),
            maya("Sprint Planning's timebox: eight hours for a one-month Sprint, shorter for shorter Sprints. Three topics: why, what, how. Who crafts the Sprint Goal: the whole Scrum Team. Who selects how much work: the Developers, with the Product Owner. Who decides how: only the Developers. And the result is the Sprint Backlog: goal, items and plan."),
            leo("Case closed. Anything to try today?"),
            maya("Take your next Sprint and write its Sprint Goal in one sentence, as an outcome. Pick the few Product Backlog items that serve it, and check your team's capacity. Then break one item into pieces of work of one day or less."),
            leo("A plan the team believes. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-sprint-planning",
        listOf(
            maya("Okay, did you hear about the app team's Sprint Planning this week? Two days. Two whole days. And they still came out without a plan anyone believed."),
            leo("[laugh] I heard they ordered lunch twice. Start me off, though. What's Sprint Planning meant to be?"),
            maya("The Scrum Guide, the short free document that defines Scrum, says it opens the Sprint and lays out the work, and the whole Scrum Team makes the plan together. It answers three topics: why is this Sprint valuable, what can be Done, and how will the work get done."),
            leo("And how long should it take?"),
            maya("Its timebox, the maximum length, is eight hours for a one-month Sprint, and usually shorter for shorter Sprints. Theirs are two weeks. Which brings us to Monday, and Marcus, who booked the room for two days."),
            leo("Poor Marcus. Why so long?"),
            maya("Because Priya, the Product Owner, arrived with twenty items in no order. The Product Owner is meant to make sure everyone's ready to discuss the most important Product Backlog items, and how they connect to the Product Goal, the product's long-term objective."),
            leo("And the Product Backlog is the ordered list of everything the app might need."),
            maya("Ordered being the key word. Getting items ready is called refinement: breaking them into smaller ones and adding detail. It happens before Planning, not in it. Tuesday was Dana."),
            leo("What did Dana do?"),
            maya("Dana's the Scrum Master. She wrote the Sprint Goal herself the night before and read it out like a weather forecast. The Sprint Goal is the single objective for the Sprint. The Product Owner proposes how the product could gain value, and the whole Scrum Team defines the goal together."),
            leo("[sigh] And finalised before Planning ends, I assume."),
            maya("Exactly. A good one is an outcome: runners can record a run with no signal, and it syncs later. Wednesday was Tom, the other Product Owner, on the other team. Tom picked twelve items and told the Developers to fit them in."),
            leo("He can't do that?"),
            maya("He can discuss, explain and negotiate. But the Developers select the items they can take on. The exam's favourite: who decides how much work goes into the Sprint? The Developers."),
            leo("How do they decide without just guessing?"),
            maya("The Guide names three things: their past performance, their upcoming capacity, and their Definition of Done. Capacity is the time they really have; one of Tom's Developers was off for a week. The Definition of Done is the quality checklist, like code reviewed and tests passing."),
            leo("Why does the Definition of Done change how much fits?"),
            maya("Because every item must meet it. A strict checklist, like running on the oldest Android version the app supports, makes each item bigger. Ignore it in Planning, and you're planning to cut corners."),
            leo("Thursday?"),
            maya("Thursday, an architect from another team came to Planning. Inviting him for advice is fine. But he started handing out designs, as if he owned the how."),
            leo("And who owns the how?"),
            maya("The Developers, alone. The Guide says how is at their sole discretion. For each item, they plan the work to create an Increment, a usable piece of the app, that meets the Definition of Done. Often by breaking items into pieces of one day or less."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. The intern thought the Sprint Goal was just the list of tickets, stapled together. It isn't. The Sprint Backlog is three things: the Sprint Goal, the selected Product Backlog items, and the plan for delivering them. The goal is the why that holds it together."),
            leo("Did anyone get it right?"),
            maya("The other team. Their Product Owner arrived with an ordered, refined backlog. They crafted one goal together in twenty minutes, the Developers picked six items they believed in, and planned them in day-sized tasks. Three hours, done. And the plan keeps changing as they learn; the Sprint Backlog episode covers that."),
            leo("Where does that plan stop working, though? Plans always break."),
            maya("It's meant to bend. The Sprint Backlog is updated as the Developers learn, and if work turns out bigger, they renegotiate scope with the Product Owner. What doesn't bend is the Sprint Goal. That's why the goal matters more than the ticket list."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Sprint Planning's timebox: eight hours for a one-month Sprint, shorter for shorter Sprints. Three topics: why, what, how. Who crafts the Sprint Goal: the whole Scrum Team. Who selects how much work: the Developers, with the Product Owner. Who decides how: only the Developers. And the result is the Sprint Backlog: goal, items and plan."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Take your next Sprint and write its Sprint Goal in one sentence, as an outcome. Pick the few Product Backlog items that serve it, and check your team's capacity. Then break one item into pieces of work of one day or less."),
            leo("Eight hours, Marcus. Tops. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-sprint-planning",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight: Sprint Planning. How a team decides what the next few weeks will hold."),
            ivy("Mm. I can barely plan tomorrow, Nova."),
            nova("[chuckle] Then you'll like this: it only ever plans one Sprint. The Scrum Guide, the short free document that defines Scrum, says Sprint Planning opens the Sprint and lays out its work. And the whole Scrum Team makes the plan together."),
            ivy("How long do they get?"),
            nova("A timebox, which is a maximum length, like the last train home. Eight hours for a one-month Sprint. Usually shorter for shorter Sprints. [pause] And three topics to get through: why, what and how."),
            ivy("Who's in the room, at this hour?"),
            nova("The whole Scrum Team: the Product Owner, the Scrum Master, the Developers. And anyone the team invites to give advice, say a backend expert from another team. They advise. They don't plan."),
            ivy("Start with why. It's that kind of hour."),
            nova("Why is this Sprint valuable? The Product Owner proposes how the product could grow in value. Then the whole Scrum Team shapes the Sprint Goal together: the single objective for the Sprint. Picture an Android running app: runners can record a run with no signal, and it syncs later."),
            ivy("And it has to be settled before they leave?"),
            nova("Finalised before Sprint Planning ends. And notice who crafts it: the whole team. Not the Product Owner alone. Not the Scrum Master, the night before."),
            ivy("What's the Sprint Goal really for, under the picture?"),
            nova("It's the one commitment in the Sprint Backlog. It gives the Developers a reason to work together, and room to change the details. [pause] If offline sync turns out harder than hoped, the goal tells them what matters most."),
            ivy("Does anyone come prepared?"),
            nova("The Product Owner makes sure everyone's ready to talk about the most important Product Backlog items, and how they connect to the Product Goal. The Product Backlog is the ordered list of everything the product might need. The Product Goal is its long-term objective."),
            ivy("Then what. Softly."),
            nova("What can be Done this Sprint? Through discussion with the Product Owner, the Developers select items from the Product Backlog. [pause] The team may refine them as it goes: breaking them into smaller ones, adding detail."),
            ivy("And if the Product Owner wants more than the Developers can carry?"),
            nova("The Developers decide how much they take. The Product Owner can explain and negotiate, but can't pile it on. And the forecast gets better with three things: past performance, upcoming capacity, and the Definition of Done."),
            ivy("Capacity. Is that how tired they are?"),
            nova("[pause] Close. It's how much time they really have. One Developer on holiday means less fits. And the Definition of Done is the quality checklist, like code reviewed and tests passing. A strict one makes each item bigger, and that's honest."),
            ivy("And then how. The how is always the hard part."),
            nova("For each item, the Developers plan the work to create an Increment, a usable piece of the product, that meets the Definition of Done. Often they break items into pieces of one day or less, so a stuck task shows itself quickly."),
            ivy("Why one day or less? Tell me slowly."),
            nova("Because small pieces can't hide. A one-day task still open after three days stands out at the Daily Scrum. A two-week task called build sync tells you nothing, [pause] until it's far too late."),
            ivy("Who decides the how? A tech lead? Someone wise and invited?"),
            nova("Only the Developers. The Guide says how is at their sole discretion. The team may invite others to give advice. [pause] Advice. Never orders."),
            ivy("And what do they carry out of the room, at the end?"),
            nova("The Sprint Backlog. Three things together: the Sprint Goal, the Product Backlog items they selected, and the plan for delivering them. A plan by and for the Developers, that keeps changing as they learn. The Sprint Backlog episode is your next late night."),
            ivy("And if the plan turns out wrong, halfway through?"),
            nova("Then it changes. The Developers update it as they learn, and if the work turns out bigger than they thought, they renegotiate scope with the Product Owner. What doesn't move is the Sprint Goal. The plan bends. [pause] The goal holds."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Sprint Planning's timebox: eight hours for a one-month Sprint, shorter for shorter Sprints. Three topics: why, what, how. Who crafts the Sprint Goal: the whole Scrum Team. Who selects how much work: the Developers, with the Product Owner. Who decides how: only the Developers. And the result is the Sprint Backlog: goal, items and plan."),
            ivy("And something to try today, before I sleep?"),
            nova("Take your next Sprint and write its Sprint Goal in one sentence, as an outcome. [pause] Pick the few Product Backlog items that serve it, and check your team's capacity. Then break one item into pieces of work of one day or less."),
            ivy("Why, what, how. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-sprint-planning",
        listOf(
            nova("Tonight is date night, darling. And I want to talk about our plans."),
            rafe("Our plans? I was going to suggest dessert."),
            nova("[laugh] Sprint plans. Sprint Planning opens every Sprint. The Scrum Guide, the short free document that defines Scrum, says the whole Scrum Team lays out the work for the Sprint together, through three topics: why, what and how."),
            rafe("Together. I like a team that plans together. How long do we get?"),
            nova("That's my first quiz, exam style. What's the timebox for Sprint Planning in a one-month Sprint?"),
            rafe("Eight hours at most, and usually shorter for shorter Sprints. A timebox is a maximum length, like a table booked for one sitting."),
            nova("Right answer. You always know when time's up. [kiss]"),
            rafe("My turn. Topic one is why this Sprint is valuable. Who crafts the Sprint Goal?"),
            nova("The Product Owner. It's their product, so it's their goal."),
            rafe("No kiss for that one. The Product Owner proposes how the product could gain value, but the whole Scrum Team defines the Sprint Goal together: the single objective for the Sprint. And it's finalised before Sprint Planning ends."),
            nova("Mm. A shared goal. Give me a good one, then."),
            rafe("For an Android running app: runners can record a run with no signal, and it syncs later. An outcome, not a list of tickets."),
            nova("Lovely. And before anyone walks in, the Product Owner makes sure people are ready to discuss the most important Product Backlog items, the ordered list of everything the product might need, and how they connect to the Product Goal, the long-term objective."),
            rafe("And who else gets invited to this planning party?"),
            nova("The whole Scrum Team is there: the Product Owner, the Scrum Master, the Developers. And the team may invite others to give advice, like a backend expert from another team. Advice only. They don't get a say in the plan."),
            rafe("Topic two, then. What can be Done. Quiz for you, darling. The Product Owner wants twelve items in the Sprint. The Developers believe six. Who decides?"),
            nova("The Developers. Through discussion with the Product Owner, they select the items. The Product Owner can negotiate and explain trade-offs, but can't push work onto them."),
            rafe("Right answer. Nobody should take on more than they can handle. [kiss]"),
            nova("And their forecast improves with three things: past performance, upcoming capacity, and the Definition of Done."),
            rafe("Capacity. As in stamina?"),
            nova("[chuckle] As in the time they really have. One Developer on holiday means less fits. And the Definition of Done is the quality checklist every item must meet, like tests passing. A strict one makes each item bigger, and that's honest."),
            rafe("And refining. Remind me what that means?"),
            nova("Breaking big items into smaller ones and adding detail, like a size, so they're ready to pick. The team may refine during Planning, but the less mess they bring in, the shorter the night."),
            rafe("Then topic three. How. My question. An architect from another team is invited, and starts dictating the design. Allowed?"),
            nova("Inviting him to give advice, yes. Dictating, no. How is at the sole discretion of the Developers. For each item, they plan the work to create an Increment, a usable piece of product, that meets the Definition of Done."),
            rafe("Right answer. Nobody tells us how. [kiss]"),
            nova("And they often break items into pieces of one day or less, so a stuck one shows up fast at the Daily Scrum; a two-week task hides everything. Last quiz. What comes out of Sprint Planning?"),
            rafe("The Sprint Backlog. Three things: the Sprint Goal, the selected Product Backlog items, and the plan for delivering them. A plan by and for the Developers, updated as they learn."),
            nova("Right answer. All three, beautifully put together. [kiss]"),
            rafe("And where does the plan go from there?"),
            nova("Into the Sprint, where the Daily Scrum adapts it each day while the goal stays put. The Sprint Backlog episode follows it."),
            rafe("And if the plan turns out wrong halfway? Some plans do, darling."),
            nova("Then it changes. The Developers update the Sprint Backlog as they learn, and if the work's bigger than they thought, they renegotiate scope with the Product Owner. The plan bends; the goal holds. Unlike your dessert plans."),
            rafe("Then, for the exam. What do we remember?"),
            nova("Sprint Planning's timebox: eight hours for a one-month Sprint, shorter for shorter Sprints. Three topics: why, what, how. Who crafts the Sprint Goal: the whole Scrum Team. Who selects how much work: the Developers, with the Product Owner. Who decides how: only the Developers. And the result is the Sprint Backlog: goal, items and plan."),
            rafe("Perfect. That earns a long one. [kiss]"),
            nova("Mm. And something to try today?"),
            rafe("Take your next Sprint and write its Sprint Goal in one sentence, as an outcome. Pick the few Product Backlog items that serve it, and check your team's capacity. Then break one item into pieces of work of one day or less."),
            nova("Why, what, how, and then dessert. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun overview() = DiagramSpec(
        "Sprint Planning answers why, what and how, and the three answers together make the Sprint Backlog.",
        nodes = listOf(
            DNode("why", "Why", 0.17f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "the Sprint Goal"),
            DNode("what", "What", 0.5f, 0.3f, w = 0.3f, h = 0.24f, sub = "items selected"),
            DNode("how", "How", 0.83f, 0.3f, w = 0.3f, h = 0.24f, sub = "the plan"),
            DNode("backlog", "Sprint Backlog", 0.5f, 0.8f, w = 0.6f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "goal, items and plan"),
        ),
        edges = listOf(
            DEdge("why", "what"),
            DEdge("what", "how"),
            DEdge("why", "backlog"),
            DEdge("what", "backlog"),
            DEdge("how", "backlog"),
        ),
        aspect = 0.8f,
    )

    private fun room() = DiagramSpec(
        "The whole Scrum Team plans together, others may be invited to advise, and the event is timeboxed.",
        nodes = listOf(
            DNode("po", "Product Owner", 0.25f, 0.15f, w = 0.42f, h = 0.2f, tone = 1, sub = "prepares the items"),
            DNode("sm", "Scrum Master", 0.75f, 0.15f, w = 0.42f, h = 0.2f, sub = "keeps it on track"),
            DNode("dev", "Developers", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "plan the work"),
            DNode("guest", "Invited advisers", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "advice only"),
            DNode("timebox", "Up to 8 hours for a one-month Sprint", 0.5f, 0.85f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        aspect = 0.8f,
    )

    private fun why() = DiagramSpec(
        "Topic one: the Product Owner proposes the value, and the whole Scrum Team crafts the Sprint Goal.",
        nodes = listOf(
            DNode("po", "Product Owner", 0.17f, 0.3f, w = 0.3f, h = 0.24f, sub = "proposes value"),
            DNode("team", "Whole Scrum Team", 0.5f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "crafts the goal"),
            DNode("goal", "Sprint Goal", 0.83f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "set in Planning"),
            DNode("note", "An outcome: record a run offline, sync it later", 0.5f, 0.8f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("po", "team", "proposes"),
            DEdge("team", "goal", "defines"),
        ),
        aspect = 0.8f,
    )

    private fun what() = DiagramSpec(
        "Topic two: the Developers select the items, and past performance, capacity and the Definition of Done shape the forecast.",
        nodes = listOf(
            DNode("backlog", "Product Backlog", 0.25f, 0.2f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "ordered items"),
            DNode("devs", "Developers", 0.75f, 0.2f, w = 0.42f, h = 0.24f, tone = 1, sub = "select how much"),
            DNode("past", "Past performance", 0.17f, 0.72f, w = 0.3f, h = 0.22f, kind = NodeKind.NOTE),
            DNode("cap", "Capacity", 0.5f, 0.72f, w = 0.3f, h = 0.22f, kind = NodeKind.NOTE, sub = "time they have"),
            DNode("done", "Definition of Done", 0.83f, 0.72f, w = 0.3f, h = 0.22f, kind = NodeKind.NOTE, sub = "the quality bar"),
        ),
        edges = listOf(
            DEdge("backlog", "devs", "select"),
            DEdge("past", "devs"),
            DEdge("cap", "devs"),
            DEdge("done", "devs"),
        ),
        aspect = 0.8f,
    )

    private fun how() = DiagramSpec(
        "Topic three: the Developers alone plan how each item becomes a Done Increment, often in pieces of a day or less.",
        nodes = listOf(
            DNode("item", "Selected item", 0.17f, 0.3f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE),
            DNode("tasks", "Day-sized work", 0.5f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "one day or less"),
            DNode("inc", "Increment", 0.83f, 0.3f, w = 0.3f, h = 0.24f, sub = "Done and usable"),
            DNode("note", "How is the Developers' call alone", 0.5f, 0.8f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("item", "tasks", "break down"),
            DEdge("tasks", "inc", "build"),
        ),
        aspect = 0.8f,
    )

    private fun backlogOut() = DiagramSpec(
        "The Sprint Backlog is the Sprint Goal, the selected items and the plan: by and for the Developers, updated as they learn.",
        nodes = listOf(
            DNode("goal", "Sprint Goal", 0.17f, 0.25f, w = 0.3f, h = 0.24f, tone = 1, sub = "why"),
            DNode("items", "Selected items", 0.5f, 0.25f, w = 0.3f, h = 0.24f, sub = "what"),
            DNode("plan", "The plan", 0.83f, 0.25f, w = 0.3f, h = 0.24f, sub = "how"),
            DNode("sb", "Sprint Backlog", 0.5f, 0.78f, w = 0.6f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2, sub = "updated as they learn"),
        ),
        edges = listOf(
            DEdge("goal", "sb"),
            DEdge("items", "sb"),
            DEdge("plan", "sb"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Sprint Planning questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("time", "Timebox", 0.26f, 0.2f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "8 hours for one month"),
            DNode("goal", "Who crafts the goal?", 0.76f, 0.2f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "the whole Scrum Team"),
            DNode("much", "Who picks how much?", 0.26f, 0.74f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "the Developers"),
            DNode("how", "Who decides how?", 0.76f, 0.74f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "only the Developers"),
        ),
        aspect = 0.8f,
    )

    private val all4 = setOf("time", "goal", "much", "how")
    private val topics = setOf("why", "what", "how")
    private val outputs = setOf("goal", "items", "plan", "sb")
    private val today = BoardCard("Today", "A one-line Sprint Goal, a few items, capacity, day-sized work.")
    private val takeaways = BoardCard("Listen for the phrase", "8 hours. Goal: whole team. How much: Developers. How: only Developers.")
    private val threeTopics = BoardCard("Three topics", "Why is it valuable? What can be Done? How will it get done?")
    private val forecast = BoardCard("Better forecasts", "Past performance, upcoming capacity, Definition of Done.")
    private val refinement = BoardCard("Refinement", "Breaking items down and adding detail, best before Planning.")
    private val anOutcome = BoardCard("An outcome", "Record a run offline, sync it later. Finalised in Planning.")
    private val soleDiscretion = BoardCard("Sole discretion", "Only the Developers decide how. Advice, yes; orders, no.")
    private val dayOrLess = BoardCard("One day or less", "Small pieces make a stuck task visible at the Daily Scrum.")
    private val sprintBacklog = BoardCard("Sprint Backlog", "The Sprint Goal, the selected items, and the plan to deliver them.")
    private val bends = BoardCard("The plan bends", "Scope renegotiated with the Product Owner; the goal holds.")
    private val whoAttends = BoardCard("Who attends", "The whole Scrum Team. Invited advisers advise; they don't plan.")
    private val poPrepares = BoardCard("The Product Owner prepares", "The most important items, and how they map to the Product Goal.")

    private fun classicBoard() = PodcastBoard(
        "psm-sprint-planning",
        listOf(
            // 0: "Hi, and welcome back. Today: Sprint Planning..."
            Scene(
                0, "Opening the Sprint", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Sprint Planning", "Opens the Sprint. The whole Scrum Team plans it together.")),
                    Beat(2, show = topics, light = topics, card = threeTopics),
                ),
            ),
            // 3: "Why, what, how. Before the details, how long does it take?"
            Scene(
                3, "Who and how long", room(),
                beats = listOf(
                    Beat(4, light = setOf("timebox"), card = BoardCard("Timebox", "Up to 8 hours for a one-month Sprint; usually shorter.")),
                    Beat(6, show = setOf("guest"), light = setOf("po", "sm", "dev", "guest"), card = whoAttends),
                    Beat(8, light = setOf("po"), card = poPrepares),
                ),
            ),
            // 9: "Okay. Topic one: why."
            Scene(
                9, "Topic one: why", why(),
                beats = listOf(
                    Beat(10, show = setOf("po", "team", "goal"), light = setOf("po", "team"), pulse = "po" to "team", card = BoardCard("Sprint Goal", "The single objective: why this Sprint is valuable.")),
                    Beat(12, show = setOf("note"), light = setOf("goal", "note"), pulse = "team" to "goal", card = anOutcome),
                    Beat(14, light = setOf("team"), card = BoardCard("The whole Scrum Team", "The Product Owner proposes; the whole team crafts the goal.")),
                ),
            ),
            // 15: "Topic two: what."
            Scene(
                15, "Topic two: what", what(),
                beats = listOf(
                    Beat(16, show = setOf("backlog", "devs"), light = setOf("devs"), pulse = "backlog" to "devs", card = BoardCard("Developers select", "With the Product Owner. Items may be refined as they go.")),
                    Beat(18, light = setOf("devs"), card = BoardCard("Twelve or six?", "The Developers decide how much they take on.")),
                    Beat(20, show = setOf("past", "cap", "done"), light = setOf("past", "cap"), pulse = "cap" to "devs", card = forecast),
                    Beat(22, light = setOf("done"), pulse = "done" to "devs", card = BoardCard("Definition of Done", "The quality checklist. Strict means bigger items, honestly.")),
                ),
            ),
            // 23: "Topic three: how."
            Scene(
                23, "Topic three: how", how(),
                beats = listOf(
                    Beat(24, show = setOf("item", "tasks", "inc"), light = setOf("tasks"), pulse = "item" to "tasks", card = BoardCard("Plan the work", "For each item, the work to create a Done Increment.")),
                    Beat(26, light = setOf("tasks"), card = dayOrLess),
                    Beat(28, show = setOf("note"), light = setOf("note"), card = soleDiscretion),
                ),
            ),
            // 29: "So what walks out of the room at the end?"
            Scene(
                29, "What comes out", backlogOut(),
                beats = listOf(
                    Beat(30, show = outputs, light = setOf("sb"), pulse = "goal" to "sb", card = sprintBacklog),
                    Beat(32, light = setOf("plan"), pulse = "plan" to "sb", card = BoardCard("A living plan", "By and for the Developers, adapted at each Daily Scrum.")),
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
        "psm-sprint-planning--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Plan Nobody Believed..."
            Scene(
                0, "The Plan Nobody Believed", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Planning ran two days; half the work was left unfinished.")),
                    Beat(2, show = topics, light = topics, card = threeTopics),
                ),
            ),
            // 3: "And the meeting length? Two days sounds like a crime already."
            Scene(
                3, "Exhibit one: the backlog", room(),
                beats = listOf(
                    Beat(4, light = setOf("timebox"), card = BoardCard("Two days?", "8 hours for a one-month Sprint; less for two weeks.")),
                    Beat(6, light = setOf("po"), card = BoardCard("Twenty unordered items", "The Product Owner prepares the most important ones.")),
                    Beat(8, card = refinement),
                ),
            ),
            // 9: "Second suspect: the Scrum Master. Too controlling."
            Scene(
                9, "Exhibit two: the goal", why(),
                beats = listOf(
                    Beat(10, show = setOf("po", "team", "goal"), light = setOf("team", "goal"), pulse = "team" to "goal", card = BoardCard("Written the night before", "By the Scrum Master. The whole Scrum Team should craft it.")),
                    Beat(12, show = setOf("note"), light = setOf("note"), card = anOutcome),
                ),
            ),
            // 13: "Third suspect: the Product Owner, then."
            Scene(
                13, "Exhibit three: twelve items", what(),
                beats = listOf(
                    Beat(14, show = setOf("backlog", "devs"), light = setOf("devs"), pulse = "backlog" to "devs", card = BoardCard("The Developers select", "The Product Owner negotiates; the Developers decide how much.")),
                    Beat(16, show = setOf("past", "cap", "done"), light = setOf("cap"), pulse = "cap" to "devs", card = BoardCard("A holiday, ignored", "Past performance, capacity and Done sharpen the forecast.")),
                    Beat(18, light = setOf("done"), pulse = "done" to "devs", card = BoardCard("Definition of Done", "Ignored too, so every item looked smaller than it was.")),
                ),
            ),
            // 19: "Is inviting him the crime?"
            Scene(
                19, "Exhibit four: the design", how(),
                beats = listOf(
                    Beat(20, show = setOf("item", "tasks", "inc", "note"), light = setOf("note"), card = soleDiscretion),
                    Beat(22, light = setOf("tasks"), pulse = "item" to "tasks", card = BoardCard("One day or less", "Week-long tasks hid the trouble until it was too late.")),
                ),
            ),
            // 23: "And when a plan breaks mid-Sprint, like this one did?"
            Scene(
                23, "A plan that bends", backlogOut(),
                beats = listOf(
                    Beat(24, show = outputs, light = setOf("plan", "goal"), pulse = "plan" to "sb", card = bends),
                    Beat(26, light = setOf("sb"), card = sprintBacklog),
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
        "psm-sprint-planning--gossip",
        listOf(
            // 0: "Okay, did you hear about the app team's Sprint Planning this week?..."
            Scene(
                0, "Two days of Planning", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Two whole days", "And still no plan anyone believed.")),
                    Beat(2, show = topics, light = topics, card = threeTopics),
                ),
            ),
            // 3: "And how long should it take?"
            Scene(
                3, "Monday: Marcus and Priya", room(),
                beats = listOf(
                    Beat(4, light = setOf("timebox"), card = BoardCard("Booked for two days", "8 hours for a one-month Sprint; shorter for two weeks.")),
                    Beat(6, light = setOf("po"), card = BoardCard("Twenty unordered items", "The Product Owner prepares the most important ones.")),
                    Beat(8, card = refinement),
                ),
            ),
            // 9: "What did Dana do?"
            Scene(
                9, "Tuesday: Dana", why(),
                beats = listOf(
                    Beat(10, show = setOf("po", "team", "goal"), light = setOf("goal", "team"), pulse = "team" to "goal", card = BoardCard("The goal, read out", "The whole Scrum Team crafts the Sprint Goal together.")),
                    Beat(12, show = setOf("note"), light = setOf("note"), card = anOutcome),
                ),
            ),
            // 13: "He can't do that?"
            Scene(
                13, "Wednesday: Tom", what(),
                beats = listOf(
                    Beat(14, show = setOf("backlog", "devs"), light = setOf("devs"), pulse = "backlog" to "devs", card = BoardCard("Twelve items, pushed", "The Developers select how much; Tom can only negotiate.")),
                    Beat(16, show = setOf("past", "cap", "done"), light = setOf("past", "cap"), pulse = "cap" to "devs", card = forecast),
                    Beat(18, light = setOf("done"), pulse = "done" to "devs", card = BoardCard("Definition of Done", "Every item must meet it. Ignore it, and you plan to cut corners.")),
                ),
            ),
            // 19: "Thursday?"
            Scene(
                19, "Thursday: the architect", how(),
                beats = listOf(
                    Beat(20, show = setOf("note"), light = setOf("note"), card = BoardCard("Advice, not orders", "Invited advisers advise. How belongs to the Developers.")),
                    Beat(22, show = setOf("tasks", "inc"), light = setOf("tasks"), pulse = "item" to "tasks", card = BoardCard("Day-sized work", "For each item, pieces of a day or less toward a Done Increment.")),
                ),
            ),
            // 23: "And Friday? There's always a Friday."
            Scene(
                23, "Friday: the intern", backlogOut(),
                beats = listOf(
                    Beat(24, show = outputs, light = outputs, card = BoardCard("Not just a ticket list", "Sprint Backlog: the Sprint Goal, the items, and the plan.")),
                    Beat(26, light = setOf("goal", "items"), card = BoardCard("Three hours, done", "Ordered backlog, one shared goal, six items, day-sized tasks.")),
                    Beat(28, light = setOf("plan"), pulse = "plan" to "sb", card = bends),
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
        "psm-sprint-planning--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "One Sprint at a time", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Sprint Planning: what the next few weeks will hold.")),
                    Beat(2, card = BoardCard("Opening the Sprint", "The whole Scrum Team lays out the work together.")),
                    Beat(4, show = topics, light = topics, card = BoardCard("Eight hours, three topics", "Up to 8 hours for a one-month Sprint: why, what and how.")),
                ),
            ),
            // 5: "Who's in the room, at this hour?"
            Scene(
                5, "Who's in the room", room(),
                beats = listOf(
                    Beat(6, show = setOf("guest"), light = setOf("po", "sm", "dev", "guest"), card = whoAttends),
                ),
            ),
            // 7: "Start with why. It's that kind of hour."
            Scene(
                7, "Why", why(),
                beats = listOf(
                    Beat(8, show = setOf("po", "team", "goal"), light = setOf("po", "team"), pulse = "po" to "team", card = BoardCard("Sprint Goal", "The single objective: record a run offline, sync it later.")),
                    Beat(10, light = setOf("team", "goal"), pulse = "team" to "goal", card = BoardCard("The whole Scrum Team", "Finalised before Planning ends. Not one person's goal.")),
                    Beat(12, show = setOf("note"), light = setOf("goal"), card = BoardCard("The one commitment", "A reason to work together, and room to change the details.")),
                ),
            ),
            // 13: "Does anyone come prepared?"
            Scene(
                13, "What", what(),
                beats = listOf(
                    Beat(14, show = setOf("backlog"), light = setOf("backlog"), card = poPrepares),
                    Beat(16, show = setOf("devs"), light = setOf("devs"), pulse = "backlog" to "devs", card = BoardCard("Developers select", "With the Product Owner. Items may be refined as they go.")),
                    Beat(18, show = setOf("past", "cap", "done"), light = setOf("past", "cap", "done"), card = forecast),
                    Beat(20, light = setOf("cap"), pulse = "cap" to "devs", card = BoardCard("Capacity", "The time they really have. A holiday means less fits.")),
                ),
            ),
            // 21: "And then how. The how is always the hard part."
            Scene(
                21, "How", how(),
                beats = listOf(
                    Beat(22, show = setOf("item", "tasks", "inc"), light = setOf("tasks"), pulse = "item" to "tasks", card = BoardCard("Plan the work", "For each item, the work to create a Done Increment.")),
                    Beat(24, card = dayOrLess),
                    Beat(26, show = setOf("note"), light = setOf("note"), card = soleDiscretion),
                ),
            ),
            // 27: "And what do they carry out of the room, at the end?"
            Scene(
                27, "The Sprint Backlog", backlogOut(),
                beats = listOf(
                    Beat(28, show = outputs, light = setOf("sb"), pulse = "goal" to "sb", card = sprintBacklog),
                    Beat(30, light = setOf("plan"), pulse = "plan" to "sb", card = bends),
                ),
            ),
            // 31: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all4, light = all4, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-sprint-planning--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Our plans", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Let's talk about our plans.")),
                    Beat(2, show = topics, light = topics, card = threeTopics),
                ),
            ),
            // 4: "That's my first quiz, exam style..."
            Scene(
                4, "Quiz: the timebox", room(),
                beats = listOf(
                    Beat(4, light = setOf("timebox"), card = BoardCard("Quiz: the timebox?", "Sprint Planning, in a one-month Sprint.")),
                    Beat(5, card = BoardCard("Eight hours at most", "Usually shorter for shorter Sprints.")),
                ),
            ),
            // 7: "My turn. Topic one is why this Sprint is valuable..."
            Scene(
                7, "Quiz: the Sprint Goal", why(),
                beats = listOf(
                    Beat(7, show = setOf("po", "team", "goal"), light = setOf("goal"), card = BoardCard("Quiz: who crafts it?", "The Sprint Goal: why this Sprint is valuable.")),
                    Beat(9, light = setOf("po", "team"), pulse = "po" to "team", card = BoardCard("No kiss for that one", "The Product Owner proposes; the whole Scrum Team defines it.")),
                    Beat(11, show = setOf("note"), light = setOf("note"), card = anOutcome),
                ),
            ),
            // 12: "Lovely. And before anyone walks in..."
            Scene(
                12, "Who's at the table", room(),
                beats = listOf(
                    Beat(12, light = setOf("po"), card = poPrepares),
                    Beat(14, show = setOf("guest"), light = setOf("guest"), card = whoAttends),
                ),
            ),
            // 15: "Topic two, then. What can be Done..."
            Scene(
                15, "Quiz: twelve or six?", what(),
                beats = listOf(
                    Beat(15, show = setOf("backlog", "devs"), light = setOf("backlog", "devs"), card = BoardCard("Quiz: who decides?", "The Product Owner wants twelve; the Developers believe six.")),
                    Beat(16, light = setOf("devs"), pulse = "backlog" to "devs", card = BoardCard("The Developers select", "The Product Owner negotiates; the Developers decide how much.")),
                    Beat(18, show = setOf("past", "cap", "done"), light = setOf("past", "cap", "done"), card = forecast),
                    Beat(20, light = setOf("cap"), pulse = "cap" to "devs", card = BoardCard("Capacity", "The time they really have. A holiday means less fits.")),
                    Beat(22, card = refinement),
                ),
            ),
            // 23: "Then topic three. How. My question..."
            Scene(
                23, "Quiz: the architect", how(),
                beats = listOf(
                    Beat(23, show = setOf("note"), light = setOf("note"), card = BoardCard("Quiz: dictating the design?", "An invited architect starts handing out designs.")),
                    Beat(24, show = setOf("item", "tasks", "inc"), light = setOf("tasks"), pulse = "item" to "tasks", card = soleDiscretion),
                    Beat(26, card = dayOrLess),
                ),
            ),
            // 27: "The Sprint Backlog. Three things..."
            Scene(
                27, "The Sprint Backlog", backlogOut(),
                beats = listOf(
                    Beat(27, show = outputs, light = setOf("sb"), pulse = "goal" to "sb", card = sprintBacklog),
                    Beat(30, light = setOf("plan"), card = BoardCard("A living plan", "The Daily Scrum adapts it; the goal stays put.")),
                    Beat(32, pulse = "plan" to "sb", card = bends),
                ),
            ),
            // 33: "Then, for the exam. What do we remember?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all4, light = all4, card = takeaways),
                    Beat(37, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
