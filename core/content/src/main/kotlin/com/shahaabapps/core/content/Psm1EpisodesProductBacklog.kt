package com.shahaabapps.core.content

/** The Product Backlog and the Product Goal: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesProductBacklog {
    private val classic = Podcast(
        "psm-product-backlog",
        listOf(
            maya("Hi, and welcome back. Today: the Product Backlog, and the goal that sits on top of it, the Product Goal. Our team builds a running app for Android, with a small backend, and every idea anyone has for that app ends up in one place."),
            leo("One place. So, a to-do list? Every team I've worked on had about six of those."),
            maya("[chuckle] That's exactly the problem it fixes. The Scrum Guide says the Product Backlog is an emergent, ordered list of what is needed to improve the product. And it's the single source of work for the Scrum Team. One list, for the whole product."),
            leo("Emergent. In plain words?"),
            maya("It means the list grows and changes as you learn. It's never finished. Picture a shopping list on the fridge: you add milk when you notice it's gone, you cross things out, you move the urgent things up. Nobody writes the whole year's shopping on day one."),
            leo("And ordered means what, sorted by priority?"),
            maya("Ordered means there's a top and a bottom, and the top is what the team will most likely work on next. The Guide says ordered, and it gives no formula for the order. The Product Owner decides it, weighing value, risk, whatever matters for this product."),
            leo("What actually goes on it? User stories?"),
            maya("Anything needed to improve the product. A new feature, a fix for a crash, a faster sign-in, an upgrade to the backend. Each one is called a Product Backlog item. User stories are one popular way to write them, but the Scrum Guide doesn't ask for them. That's a favourite exam trap."),
            leo("Okay. And where does the Product Goal come in?"),
            maya("Every artifact in Scrum carries a commitment, something that gives it focus. For the Product Backlog, that commitment is the Product Goal: the future state of the product, the long-term objective the team plans against. Think of it as the destination you type into a sat-nav."),
            leo("Give me one for the running app."),
            maya("For example: runners can train for their first ten kilometres with nothing but our app. Every item on the backlog should move the app toward that. The Guide says the Product Goal is in the Product Backlog, and the rest of the list emerges to define what will fulfil it."),
            leo("Can I have three goals at once? Marketing wants one, the backend people want another."),
            maya("One at a time. The team must fulfil one Product Goal, or abandon it, before taking on the next. [pause] Abandon is allowed. If the market changes, you drop the goal and pick a better one. What you don't do is chase three."),
            leo("Now, who looks after this list? I'm guessing it's not the developers."),
            maya("The Product Owner is accountable for it. That means developing and clearly communicating the Product Goal, creating and explaining items, ordering them, and keeping the backlog transparent, visible and understood. They can hand some of that work to others, but they stay accountable."),
            leo("And if the marketing lead wants her feature at the top?"),
            maya("She talks to the Product Owner and convinces them. Nobody reorders the list over their head. The Product Owner is one person, not a committee, and the organisation must respect their decisions."),
            leo("Right. Now, the items at the bottom must be pretty vague."),
            maya("They are, and that's fine. That's where refinement comes in. Product Backlog refinement is breaking items down and adding detail: a description, an order, a size. It's like chopping vegetables before you cook. And it's an ongoing activity, not an event. There's no refinement meeting in the Scrum Guide."),
            leo("Who decides the size? Because in my last job the manager did."),
            maya("The Developers who will do the work are responsible for sizing. The Product Owner can influence them, by helping them understand the item and choose trade-offs. But the people building it say how big it is."),
            leo("Story points? Planning poker?"),
            maya("Neither is in the Scrum Guide. They're complementary practices: tools some teams add on top. The Guide only says the Developers size the work. Use points if they help, but on the exam, never assume they're required."),
            leo("And when is an item ready to be picked for a Sprint?"),
            maya("The Guide's idea: items that can be Done by the Scrum Team within one Sprint are ready for selection in Sprint Planning. Picture a big item, offline maps. Too big for one Sprint. Refined, it becomes three: save a map, show it with no signal, delete old maps. Each fits."),
            leo("Does every item need the same fields? Our tracker has about twenty."),
            maya("The Guide says the details often vary with the kind of work. A description, an order and a size are the usual ones. Twenty fields is your tracker's choice, not Scrum's."),
            leo("And does anyone ever reach the bottom of the list?"),
            maya("Never, and that's on purpose. As long as the product exists, there's something to improve. Under the picture, the backlog is how the work stays transparent: anyone can open it and see what's coming, and in what order. You can't inspect and adapt what you can't see."),
            leo("What if the app has three teams? Android, iPhone, backend."),
            maya("Then they share one Product Goal, one Product Backlog and one Product Owner. Not three backlogs. A product has one list, however many teams work from it. The Product Owner episode covers why it's one person."),
            leo("So what do I remember for the exam?"),
            maya("The single source of work for the Scrum Team: the Product Backlog. Its commitment: the Product Goal, one at a time, fulfilled or abandoned. Who sizes items: the Developers. Is refinement an event: no, an ongoing activity. Several teams on one product: one Product Backlog, one Product Owner."),
            leo("And anything to try today?"),
            maya("Write a Product Goal for your own app in one sentence. List ten backlog items and put them in order, top to bottom. Refine the top item until it could be Done in one Sprint. Then read the Product Backlog section of the Scrum Guide, slowly."),
            leo("One list, one goal, one owner. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-product-backlog",
        listOf(
            maya("Welcome to Case Files. Today's case: The Feature Nobody Asked For. A team builds a running app for Android, with a small backend. They spent a whole Sprint building a social leaderboard. On Review day the Product Owner looked at it and said: that wasn't the plan."),
            leo("[sigh] Detective Leo, on the scene. First suspect: the developers didn't listen."),
            maya("Let's look at the evidence before we blame people. Exhibit one: where the work came from. The team had three lists. A board in the tracking tool, a spreadsheet the backend lead kept, and a slide from marketing. The leaderboard was on the slide."),
            leo("Three lists. Is that a crime?"),
            maya("In Scrum, it is. The Scrum Guide says the Product Backlog is the single source of work for the Scrum Team. One emergent, ordered list of what's needed to improve the product. Emergent means it changes as you learn; ordered means there's a top and a bottom."),
            leo("So the leaderboard should have gone where?"),
            maya("Onto the one list, where the Product Owner could place it. Anyone may want something on it. They go to the Product Owner and convince them. The Product Owner is one person, not a committee, and the organisation respects their decisions."),
            leo("Second suspect, then: nobody prioritised properly. They needed a priority column."),
            maya("[pause] Wrong suspect. They had a priority column. Forty items marked high. The Guide asks for ordered, not labelled. Ordered means item one comes before item two, no ties. The Product Owner decides that order, weighing value, risk, whatever matters for this product."),
            leo("Fine. Exhibit two?"),
            maya("The team's direction. I asked three developers what the app was trying to become. I got three answers. A race tracker, a social network, a coaching app. Nobody had a Product Goal."),
            leo("Product Goal. In plain words?"),
            maya("The future state of the product. The long-term objective the team plans against. Every artifact in Scrum carries a commitment, and the Product Backlog's commitment is the Product Goal. Picture the destination typed into a sat-nav: every turn is checked against it."),
            leo("And what should theirs have been?"),
            maya("The Product Owner wrote one, after the fact: runners can train for their first ten kilometres with nothing but our app. Hold the leaderboard against that. Does it help a beginner reach ten kilometres? Not much. It would have sat lower down."),
            leo("Could they keep the coaching goal and the social goal, side by side?"),
            maya("No. One Product Goal at a time. The team fulfils it or abandons it before taking the next. Abandoning is allowed, if the market moves. What really happens: the goal lives in the Product Backlog, and the rest of the list emerges to fulfil it."),
            leo("Third exhibit."),
            maya("The items themselves. Near the top sat one called offline maps. No detail, no size. Two developers started it, found it was four Sprints of work, and quietly switched to the leaderboard, because it looked small."),
            leo("So the item wasn't ready."),
            maya("Exactly. Items that can be Done within one Sprint are ready for selection in Sprint Planning. You get there by refinement: breaking items down and adding detail, like a description, an order and a size. Like chopping vegetables before cooking."),
            leo("Third suspect, then. They skipped the refinement meeting."),
            maya("Careful. Refinement is an ongoing activity, not an event. There's no refinement meeting in the Scrum Guide. Some teams book one, which is fine, but the Guide just says it happens. Offline maps, refined, becomes save a map, show it with no signal, and delete old maps."),
            leo("And who decided offline maps was small enough?"),
            maya("The engineering manager, by gut feel. That's the last clue. The Developers who will do the work are responsible for sizing. The Product Owner may influence them, helping them understand trade-offs. Nobody outside the team sizes it for them."),
            leo("Story points would have saved them, surely."),
            maya("Story points are a complementary practice, not part of the Scrum Guide. They can help. They're not the fix. The fix was one list, in order, with a goal on top, and items small enough to finish."),
            leo("Why did nobody notice the three lists for a whole Sprint?"),
            maya("Because the work wasn't transparent. The Product Owner is accountable for keeping the backlog transparent, visible and understood. Under the picture, that's empiricism: decisions made on what you can observe. Three lists meant nobody saw the whole, and you can't inspect what you can't see."),
            leo("So who's allowed to add things? Only the Product Owner?"),
            maya("Anyone can bring an idea, and the Product Owner can even have others write items. But they stay accountable for what's on the list, and where it sits."),
            leo("Where does all this stop working? Say they grow to three teams."),
            maya("It keeps working. Three teams on one product share one Product Goal, one Product Backlog and one Product Owner. The Sprint Planning episode picks up what happens to the top of the list."),
            leo("So, the case summary for the exam?"),
            maya("The single source of work: the Product Backlog. Its commitment: the Product Goal, one at a time, fulfilled or abandoned. Who sizes items: the Developers. Is refinement an event: no, an ongoing activity. Several teams on one product: one Product Backlog, one Product Owner."),
            leo("Case closed. Anything to try today?"),
            maya("Write a Product Goal for your own app in one sentence. List ten backlog items and put them in order, no ties. Refine the top item until it could be Done in one Sprint. Then read the Product Backlog section of the Scrum Guide."),
            leo("One list, no ties. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-product-backlog",
        listOf(
            maya("Okay, did you hear what happened on the running app team this week? Android app, small backend, a brand-new Product Owner. And everybody, and I mean everybody, had opinions about the Product Backlog."),
            leo("[laugh] I heard there was a spreadsheet. Start from the top. What is the Product Backlog, in plain words?"),
            maya("The Scrum Guide calls it an emergent, ordered list of what is needed to improve the product. Emergent means it keeps changing as you learn, like a shopping list on the fridge. Ordered means a clear top and bottom. And it's the single source of work for the Scrum Team."),
            leo("Single source. Which brings us to the spreadsheet."),
            maya("Monday. Marcus, the backend lead, kept his own spreadsheet of server jobs. Never on the backlog. So the Product Owner couldn't see half the work, and nobody could order it against features. One product, one list. Marcus has merged it now."),
            leo("Poor Marcus. Tuesday?"),
            maya("Tuesday was Dana from marketing. She had edit rights, so she dragged her promo banner to the top. Twice. But the Product Owner is accountable for ordering the backlog, and the organisation must respect their decisions. If Dana wants it higher, she convinces the Product Owner."),
            leo("And the Product Owner can't just hand all that to someone else?"),
            maya("They can delegate the work, say a business analyst writes items, but they stay accountable. And they're one person, not a committee. Dana is not a co-owner because she has edit rights."),
            leo("Why did Marcus's spreadsheet even matter? The work got done."),
            maya("Because nobody could see it. The Product Owner must keep the backlog transparent, visible and understood. Under the picture, Scrum runs on empiricism: deciding based on what you observe. Hidden work can't be inspected, so nobody adapts to it. His server jobs were eating a third of every Sprint, unseen."),
            leo("Will the backlog ever be finished, at least?"),
            maya("Never. A Product Backlog lasts as long as the product does; there's always something to improve. And the details on each item vary with the kind of work. A description, an order and a size are the usual ones."),
            leo("[chuckle] Wednesday?"),
            maya("Wednesday, Tom, the engineering manager. He sized every item himself, before the team saw them. Gut feel, in hours. Then wondered why nothing finished."),
            leo("And who should size?"),
            maya("The Developers who will do the work. The Scrum Guide says so directly. The Product Owner can influence them, by helping them understand and pick trade-offs. A manager outside the team, no."),
            leo("Did anyone get it right this week?"),
            maya("Priya. She took the biggest vague item, offline maps, and refined it with the team. Refinement is breaking items down and adding detail: a description, an order, a size. Offline maps became three items: save a map, show it with no signal, delete old ones."),
            leo("Why does that matter?"),
            maya("Because items that can be Done within one Sprint are ready for selection in Sprint Planning. Under the picture: a Sprint is a fixed stretch of a month or less, so an item that won't fit can't become a finished piece of the app. Small items keep the top of the list honest."),
            leo("Thursday?"),
            maya("Thursday was the intern. The intern told the new hire that refinement is one of the official Scrum events, with its own timebox. It isn't. Refinement is an ongoing activity. Teams may meet for it, but the Scrum Guide lists no such event."),
            leo("And Friday? There's always a Friday."),
            maya("Friday was the big one. The Product Owner announced three Product Goals at once. A coaching app, a social app, and a smartwatch app."),
            leo("What's a Product Goal again?"),
            maya("The future state of the product: the long-term objective the team plans against. It's the Product Backlog's commitment, the thing that gives the list focus. Picture the destination on a sat-nav. You can't drive to three at once."),
            leo("So, one at a time?"),
            maya("One at a time. The team fulfils a Product Goal, or abandons it, before taking on the next. They settled on: runners can train for their first ten kilometres with nothing but our app. The smartwatch idea waits."),
            leo("And user stories? I heard the intern again."),
            maya("The intern insisted every item must be a user story, with story points. Both are complementary practices. Handy, popular, and not in the Scrum Guide. On the exam, never assume either is required."),
            leo("Where does the one-list rule stop? Say the iPhone team joins."),
            maya("It doesn't stop. Several teams on one product share one Product Goal, one Product Backlog and one Product Owner. The Product Owner episode covers the gossip about that job."),
            leo("So, the gossip I should remember for the exam?"),
            maya("The single source of work: the Product Backlog. Its commitment: the Product Goal, one at a time, fulfilled or abandoned. Who sizes items: the Developers. Is refinement an event: no, an ongoing activity. Several teams on one product: one Product Backlog, one Product Owner."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Write a Product Goal for your own app in one sentence. List ten backlog items and put them in order. Refine the top item until it could be Done in one Sprint. Then read the Product Backlog section of the Scrum Guide."),
            leo("One list, Marcus. One list. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-product-backlog",
        listOf(
            nova("Hey, you. It's late. The office is dark, the phone is charging, and you're still here, still learning. [pause] Tonight: the list that never sleeps. The Product Backlog."),
            ivy("Mm. A list that never sleeps. I have one of those. It's in my head, at three in the morning."),
            nova("[chuckle] Then let's write it down properly. The Scrum Guide says the Product Backlog is an emergent, ordered list of what is needed to improve the product. And it's the single source of work for the Scrum Team."),
            ivy("Say emergent slowly, for me."),
            nova("Emergent. [pause] It means the list grows and changes as you learn. It's never done. Like a shopping list on the fridge: you add things, cross things off, move the urgent ones up. Nobody writes a whole year of shopping on the first night."),
            ivy("And ordered?"),
            nova("A top, and a bottom. The top is what the team will most likely work on next. No ties, no forty things all marked urgent. The Product Owner decides the order, weighing value, and risk, and whatever matters for the product."),
            ivy("Let's say my product is a running app. Android, small backend. What lives on the list?"),
            nova("Anything that improves it. A new feature, a crash fix, faster sign-in, a backend upgrade. Each one is a Product Backlog item. User stories are a popular way to write them, but the Guide doesn't ask for them. [pause] Remember that one."),
            ivy("And where is this list going? Lists need a reason."),
            nova("They do. Every artifact in Scrum carries a commitment, something that gives it focus. The Product Backlog's commitment is the Product Goal: the future state of the product, the long-term objective. Think of it as the place you're driving to, glowing on the sat-nav."),
            ivy("Give me a destination, then."),
            nova("Runners can train for their first ten kilometres with nothing but our app. The goal lives in the Product Backlog, and the rest of the list emerges to define what will fulfil it."),
            ivy("And if I want more than one destination tonight?"),
            nova("One at a time, Ivy. The team fulfils a Product Goal, or abandons it, before taking the next. Abandoning is fine, if the world changes. Chasing three at once is not."),
            ivy("Who keeps the list, while everyone else sleeps?"),
            nova("The Product Owner is accountable for it. Developing and explaining the Product Goal, creating and explaining items, ordering them, and keeping the backlog transparent, visible and understood. They can hand some of the work to others, and still be accountable. One person, [pause] not a committee."),
            ivy("And when someone from marketing wants their thing at the top?"),
            nova("They convince the Product Owner. Nobody reorders the list over their head. The organisation must respect the Product Owner's decisions."),
            ivy("Why does it have to be visible? Some things are nicer kept in the dark."),
            nova("[pause] Not this. Scrum runs on empiricism: decisions based on what you can observe. The backlog is how everyone sees what's coming, and in what order. You can't inspect what's hidden, and you can't adapt to what you never inspected."),
            ivy("And does the list ever end? Is there a bottom, somewhere?"),
            nova("Never. As long as the product lives, there's something to improve. The details on each item vary with the kind of work: a description, an order, a size. [pause] Those are the usual ones."),
            ivy("The items at the bottom look so blurry from here."),
            nova("They're meant to. That's what refinement is for. Product Backlog refinement means breaking items down and adding detail: a description, an order, a size. Like chopping vegetables before you cook. It's an ongoing activity, not an event. The Guide lists no refinement meeting."),
            ivy("And who says how big a thing is?"),
            nova("The Developers who will do the work. The Product Owner can influence them, helping them understand the item and the trade-offs. But the people who build it, size it. Story points, planning poker: complementary practices. Nice, if they help. Not in the Guide."),
            ivy("When is an item ready to be chosen?"),
            nova("When it can be Done by the Scrum Team within one Sprint. Then it's ready for selection in Sprint Planning. Offline maps is too big. Refined, it's three: save a map, show it with no signal, delete old maps. [pause] Each fits in one Sprint."),
            ivy("And if three teams share the app? Android, iPhone, backend?"),
            nova("Then one Product Goal, one Product Backlog, one Product Owner. However many teams. The Sprint Planning episode is your next late night, when the top of this list meets the team."),
            ivy("[sigh] Mm. Before I sleep. What do I remember for the exam?"),
            nova("The single source of work: the Product Backlog. Its commitment: the Product Goal, one at a time, fulfilled or abandoned. Who sizes items: the Developers. Is refinement an event: no, an ongoing activity. Several teams on one product: one Product Backlog, one Product Owner."),
            ivy("And something to try today, before I close my eyes?"),
            nova("Write a Product Goal for your own app in one sentence. [pause] List ten backlog items and put them in order. Refine the top item until it could be Done in one Sprint. Then read the Product Backlog section of the Scrum Guide."),
            ivy("One list, one goal. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-product-backlog",
        listOf(
            nova("Tonight is date night, darling. And I want to talk about us. About our list."),
            rafe("Our list? We have a list?"),
            nova("[laugh] Everyone has a list. In Scrum it's the Product Backlog. The Scrum Guide calls it an emergent, ordered list of what is needed to improve the product, and the single source of work for the Scrum Team."),
            rafe("Emergent. Meaning it keeps growing. Like my feelings for you."),
            nova("Meaning it changes as you learn, and is never finished. Like a shopping list on the fridge. And ordered means there's a top and a bottom: the top is what comes next. Our running app's list, for the Android app and its small backend, is just the same."),
            rafe("I do like to know what comes next. Quiz me."),
            nova("Exam style. A stakeholder keeps a separate list of features in a spreadsheet, and gives it straight to the Developers. What does the Scrum Guide say?"),
            rafe("The Product Backlog is the single source of work. The spreadsheet's ideas go to the Product Owner, who decides whether and where they go on the one list."),
            nova("Right answer. You only have eyes for one list. [kiss]"),
            rafe("My turn. Every artifact makes a commitment. What's the Product Backlog's?"),
            nova("The Product Goal. The future state of the product, the long-term objective the team plans against. Think of the destination on a sat-nav. Ours: runners can train for their first ten kilometres with nothing but our app."),
            rafe("Right answer. I'd follow you ten kilometres. [kiss]"),
            nova("And under the picture: the Product Goal lives in the Product Backlog itself, and the rest of the list emerges to define what will fulfil it."),
            rafe("Then let's be greedy. Three Product Goals, all at once."),
            nova("No kiss for that one. One at a time. The team fulfils a Product Goal, or abandons it, before taking on the next. Commitment, darling. You don't get to chase three."),
            rafe("Fine, I'm a one-goal man. Who keeps the list in order?"),
            nova("The Product Owner is accountable for it: the Product Goal, creating and explaining items, ordering them, keeping it transparent, visible and understood. They may delegate the work, never the accountability. And they're one person, not a committee."),
            rafe("So if your mother wants her feature at the top?"),
            nova("She convinces the Product Owner. Nobody reorders over their head; the organisation respects their decisions. My turn to quiz you. Who is responsible for sizing Product Backlog items?"),
            rafe("The Product Owner, because they know the value."),
            nova("No kiss for that one either. The Developers who will do the work are responsible for sizing. The Product Owner can influence them, helping them understand trade-offs. The people who do the work, size the work."),
            rafe("I'll measure more carefully next time. What about refinement? Sounds like something I could use."),
            nova("[chuckle] Product Backlog refinement is breaking items down and adding detail: description, order, size. Like chopping vegetables before cooking. Now, exam style: is refinement one of the Scrum events?"),
            rafe("No. It's an ongoing activity, not an event. Teams can meet for it, but the Scrum Guide has no refinement event."),
            nova("Right answer. Refined, and never rushed. [kiss]"),
            rafe("And when is an item ready for us?"),
            nova("When it can be Done within one Sprint. Then it's ready for selection in Sprint Planning. Offline maps is too big; refined, it's save a map, show it with no signal, delete old maps. Each one fits."),
            rafe("And story points? User stories? I've heard so much about them."),
            nova("Complementary practices. Popular, useful, and not in the Scrum Guide. Never assume they're required."),
            rafe("Why does our list have to be so visible? Can't we keep a few secrets?"),
            nova("Not on the backlog. The Product Owner keeps it transparent, visible and understood. Scrum runs on empiricism, deciding on what you can observe, and you can't inspect what you can't see. A hidden spreadsheet is a secret the whole team pays for."),
            rafe("Then a question for you. During a Sprint, can the Product Backlog change?"),
            nova("Yes. The Product Backlog is refined as needed, Sprint or no Sprint. It's emergent. What stays put during the Sprint is the Sprint Goal, and that's the Sprint Backlog episode."),
            rafe("Right answer. You never stop growing on me. [kiss]"),
            nova("And no, love, we never reach the bottom of our list. As long as the product lives, there's always something to improve."),
            rafe("Then a last quiz. Three teams build one product. How many Product Backlogs?"),
            nova("One. One Product Goal, one Product Backlog, one Product Owner, however many teams. The Product Owner episode covers why it's one person."),
            rafe("Right answer. One of everything, like us. [kiss]"),
            nova("Last question, for the exam. What do we remember?"),
            rafe("The single source of work: the Product Backlog. Its commitment: the Product Goal, one at a time, fulfilled or abandoned. Who sizes items: the Developers. Is refinement an event: no, an ongoing activity. Several teams on one product: one Product Backlog, one Product Owner."),
            nova("Five for five, darling. That earns a long one. [kiss] And something to try today?"),
            rafe("Write a Product Goal for your own app in one sentence. List ten backlog items and put them in order. Refine the top item until it could be Done in one Sprint. Then read the Product Backlog section of the Scrum Guide."),
            nova("One list, and you at the top of it. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun backlog() = DiagramSpec(
        "One ordered list for the whole product: the Product Goal on top, ready items next, vague ideas at the bottom.",
        nodes = listOf(
            DNode("goal", "Product Goal", 0.5f, 0.11f, w = 0.8f, h = 0.16f, kind = NodeKind.NOTE, tone = 1),
            DNode("top", "Ready items", 0.32f, 0.38f, w = 0.56f, h = 0.16f, tone = 1, sub = "fit one Sprint"),
            DNode("mid", "Being refined", 0.32f, 0.6f, w = 0.56f, h = 0.16f, sub = "more detail"),
            DNode("bottom", "Vague ideas", 0.32f, 0.82f, w = 0.56f, h = 0.16f, tone = 2, sub = "big, rough"),
            DNode("po", "Product Owner", 0.81f, 0.45f, w = 0.36f, h = 0.2f, sub = "orders the list"),
            DNode("dev", "Developers", 0.81f, 0.78f, w = 0.36f, h = 0.2f, tone = 2, sub = "size the items"),
        ),
        edges = listOf(
            DEdge("bottom", "mid", "refine"),
            DEdge("mid", "top", "refine"),
            DEdge("po", "top", "orders"),
            DEdge("dev", "mid", "size"),
            DEdge("goal", "top"),
        ),
        aspect = 0.8f,
    )

    private fun goal() = DiagramSpec(
        "The Product Goal is the Product Backlog's commitment: one at a time, fulfilled or abandoned before the next.",
        nodes = listOf(
            DNode("backlog", "Product Backlog", 0.5f, 0.15f, w = 0.6f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "emerges to fulfil it"),
            DNode("now", "Product Goal", 0.5f, 0.5f, w = 0.6f, h = 0.2f, tone = 1, sub = "fulfil or abandon"),
            DNode("next", "The next goal", 0.5f, 0.84f, w = 0.6f, h = 0.2f, tone = 2, sub = "only after that"),
        ),
        edges = listOf(
            DEdge("backlog", "now", "serves"),
            DEdge("now", "next", "then"),
        ),
        aspect = 0.8f,
    )

    private fun owner() = DiagramSpec(
        "The Product Owner is accountable for managing the Product Backlog, even when others do some of the work.",
        nodes = listOf(
            DNode("po", "Product Owner", 0.5f, 0.14f, w = 0.5f, h = 0.2f, tone = 1, sub = "one person"),
            DNode("goal", "Product Goal", 0.25f, 0.48f, w = 0.42f, h = 0.2f, sub = "develop, explain"),
            DNode("items", "Items", 0.75f, 0.48f, w = 0.42f, h = 0.2f, sub = "create, explain"),
            DNode("order", "Order", 0.25f, 0.82f, w = 0.42f, h = 0.2f, sub = "no ties"),
            DNode("transp", "Transparency", 0.75f, 0.82f, w = 0.42f, h = 0.2f, sub = "visible, understood"),
        ),
        edges = listOf(
            DEdge("po", "goal"),
            DEdge("po", "items"),
            DEdge("po", "order"),
            DEdge("po", "transp"),
        ),
        aspect = 0.8f,
    )

    private fun refine() = DiagramSpec(
        "Refinement breaks a big item into smaller ones, each small enough to be Done within one Sprint.",
        nodes = listOf(
            DNode("big", "Offline maps", 0.5f, 0.14f, w = 0.5f, h = 0.2f, tone = 2, sub = "too big"),
            DNode("a", "Save a map", 0.17f, 0.52f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("b", "Show with no signal", 0.5f, 0.52f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("c", "Delete old maps", 0.83f, 0.52f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("note", "Each fits in one Sprint", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("big", "a", "refine"),
            DEdge("big", "b"),
            DEdge("big", "c"),
        ),
        aspect = 0.8f,
    )

    private fun teams() = DiagramSpec(
        "Several Scrum Teams on one product share one Product Goal, one Product Backlog and one Product Owner.",
        nodes = listOf(
            DNode("pb", "Product Backlog", 0.5f, 0.14f, w = 0.6f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "one Product Goal"),
            DNode("po", "Product Owner", 0.5f, 0.46f, w = 0.5f, h = 0.16f, tone = 1),
            DNode("android", "Android team", 0.17f, 0.8f, w = 0.3f, h = 0.2f),
            DNode("ios", "iPhone team", 0.5f, 0.8f, w = 0.3f, h = 0.2f),
            DNode("backend", "Backend team", 0.83f, 0.8f, w = 0.3f, h = 0.2f),
        ),
        edges = listOf(
            DEdge("pb", "po"),
            DEdge("po", "android"),
            DEdge("po", "ios"),
            DEdge("po", "backend"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Product Backlog questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("source", "Single source of work", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Product Backlog"),
            DNode("commit", "Its commitment", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Product Goal"),
            DNode("one", "Goals at once", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one at a time"),
            DNode("size", "Who sizes items", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Developers"),
            DNode("refine", "Refinement", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "ongoing, not an event"),
            DNode("teams", "Many teams", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "one backlog, one owner"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("source", "commit", "one", "size", "refine", "teams")
    private val list3 = setOf("top", "mid", "bottom")
    private val duties = setOf("goal", "items", "order", "transp")
    private val pieces = setOf("a", "b", "c")
    private val takeaways = BoardCard("Listen for the phrase", "Single source: Product Backlog. One Product Goal. Developers size. Refining: no event.")
    private val today = BoardCard("Today", "A Product Goal, ten items in order, the top one refined to fit.")
    private val sourceCard = BoardCard("Single source of work", "An emergent, ordered list of what is needed to improve the product.")
    private val emergent = BoardCard("Emergent", "It grows and changes as you learn, like a shopping list on the fridge.")
    private val ordered = BoardCard("Ordered", "A top and a bottom, no ties. The Product Owner decides the order.")
    private val goalCard = BoardCard("Product Goal", "The backlog's commitment: the future state of the product.")
    private val oneAtATime = BoardCard("One at a time", "Fulfil a Product Goal or abandon it before taking the next.")
    private val accountable = BoardCard("Accountable", "Goal, items, order, transparency. May delegate; stays accountable.")
    private val refinement = BoardCard("Refinement", "Break items down; add description, order, size. Ongoing, not an event.")
    private val sizing = BoardCard("Who sizes", "The Developers who do the work. The Product Owner may influence.")
    private val ready = BoardCard("Ready for selection", "An item that can be Done within one Sprint.")
    private val extras = BoardCard("Story points, user stories", "Complementary practices. Not in the Scrum Guide.")
    private val transparent = BoardCard("Transparent", "Anyone can see what's coming. You can't inspect what's hidden.")
    private val manyTeams = BoardCard("Several teams", "One Product Goal, one Product Backlog, one Product Owner.")

    private fun classicBoard() = PodcastBoard(
        "psm-product-backlog",
        listOf(
            // 0: "Hi, and welcome back. Today: the Product Backlog..."
            Scene(
                0, "One list for the product", backlog(),
                beats = listOf(
                    Beat(0, card = BoardCard("The Product Backlog", "Every idea for the running app ends up in one place.")),
                    Beat(2, show = list3, light = list3, card = sourceCard),
                    Beat(4, light = setOf("bottom"), pulse = "bottom" to "mid", card = emergent),
                    Beat(6, show = setOf("po"), light = setOf("po", "top"), pulse = "po" to "top", card = ordered),
                    Beat(8, light = list3, card = BoardCard("Product Backlog item", "A feature, a crash fix, an upgrade. User stories are optional.")),
                ),
            ),
            // 9: "Okay. And where does the Product Goal come in?"
            Scene(
                9, "The Product Goal", goal(),
                beats = listOf(
                    Beat(10, show = setOf("now"), light = setOf("now"), card = goalCard),
                    Beat(12, show = setOf("backlog"), light = setOf("backlog"), pulse = "backlog" to "now", card = BoardCard("For example", "Runners train for their first ten kilometres with only our app.")),
                    Beat(14, show = setOf("next"), light = setOf("now", "next"), pulse = "now" to "next", card = oneAtATime),
                ),
            ),
            // 15: "Now, who looks after this list?"
            Scene(
                15, "The Product Owner", owner(),
                beats = listOf(
                    Beat(16, show = duties, light = duties, card = accountable),
                    Beat(18, light = setOf("po"), card = BoardCard("One person", "Others convince the Product Owner; the organisation respects it.")),
                ),
            ),
            // 19: "Right. Now, the items at the bottom must be pretty vague."
            Scene(
                19, "Refinement", refine(),
                beats = listOf(
                    Beat(20, light = setOf("big"), card = refinement),
                    Beat(22, card = sizing),
                    Beat(24, card = extras),
                    Beat(26, show = pieces + "note", light = pieces, pulse = "big" to "a", card = ready),
                ),
            ),
            // 27: "Does every item need the same fields?"
            Scene(
                27, "A list that never ends", backlog(),
                beats = listOf(
                    Beat(28, card = BoardCard("Details vary", "Description, order and size are usual; the rest is your choice.")),
                    Beat(30, light = list3, card = transparent),
                ),
            ),
            // 31: "What if the app has three teams?"
            Scene(
                31, "Three teams, one list", teams(),
                beats = listOf(
                    Beat(32, light = setOf("pb", "po"), pulse = "pb" to "po", card = manyTeams),
                ),
            ),
            // 33: "So what do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("source", "commit", "size", "refine"), card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-product-backlog--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Feature Nobody Asked For..."
            Scene(
                0, "The Feature Nobody Asked For", backlog(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "A whole Sprint on a leaderboard the Product Owner never planned.")),
                    Beat(2, card = BoardCard("Exhibit one", "Three lists: a board, a spreadsheet, a marketing slide.")),
                    Beat(4, show = list3, light = list3, card = sourceCard),
                    Beat(6, show = setOf("po"), light = setOf("po"), pulse = "po" to "top", card = BoardCard("Through the Product Owner", "Anyone who wants work done convinces the Product Owner.")),
                    Beat(8, light = setOf("top"), card = BoardCard("Ordered, not labelled", "Forty items marked high is no order. Item one, then item two.")),
                ),
            ),
            // 9: "Fine. Exhibit two?"
            Scene(
                9, "Exhibit two: direction", goal(),
                beats = listOf(
                    Beat(10, card = BoardCard("Three answers", "Race tracker, social network, coaching app: no Product Goal.")),
                    Beat(12, show = setOf("now"), light = setOf("now"), card = goalCard),
                    Beat(14, show = setOf("backlog"), light = setOf("backlog"), pulse = "backlog" to "now", card = BoardCard("Held against the goal", "Does a leaderboard help a beginner reach ten kilometres?")),
                    Beat(16, show = setOf("next"), light = setOf("now", "next"), pulse = "now" to "next", card = oneAtATime),
                ),
            ),
            // 17: "Third exhibit."
            Scene(
                17, "Exhibit three: the items", refine(),
                beats = listOf(
                    Beat(18, light = setOf("big"), card = BoardCard("Offline maps", "No detail, no size: four Sprints of work, dropped quietly.")),
                    Beat(20, card = ready),
                    Beat(22, show = pieces + "note", light = pieces, pulse = "big" to "a", card = refinement),
                    Beat(24, card = sizing),
                    Beat(26, card = BoardCard("Story points?", "A complementary practice. Useful, and not the fix.")),
                ),
            ),
            // 27: "Why did nobody notice the three lists for a whole Sprint?"
            Scene(
                27, "Transparency", backlog(),
                beats = listOf(
                    Beat(28, light = list3, card = transparent),
                    Beat(30, light = setOf("po"), card = BoardCard("Anyone may suggest", "Others may write items; the Product Owner stays accountable.")),
                ),
            ),
            // 31: "Where does all this stop working?"
            Scene(
                31, "Three teams, one list", teams(),
                beats = listOf(
                    Beat(32, light = setOf("pb", "po"), pulse = "pb" to "po", card = manyTeams),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("source", "commit", "size", "refine", "teams"), card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-product-backlog--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the running app team this week?"
            Scene(
                0, "A week of opinions", backlog(),
                beats = listOf(
                    Beat(0, card = BoardCard("The running app team", "A brand-new Product Owner, and everyone with opinions.")),
                    Beat(2, show = list3, light = list3, card = sourceCard),
                    Beat(4, light = setOf("mid"), card = BoardCard("Monday: Marcus", "Server jobs on his own spreadsheet. One product, one list.")),
                    Beat(6, show = setOf("po"), light = setOf("po"), pulse = "po" to "top", card = BoardCard("Tuesday: Dana", "Dragged her banner to the top. Convince the Product Owner.")),
                    Beat(8, card = BoardCard("Delegate, stay accountable", "One person, not a committee. Edit rights make no co-owner.")),
                    Beat(10, light = list3, card = transparent),
                    Beat(12, card = BoardCard("Never finished", "The backlog lasts as long as the product does.")),
                ),
            ),
            // 13: "Wednesday?"
            Scene(
                13, "Sizing and refining", refine(),
                beats = listOf(
                    Beat(14, light = setOf("big"), card = BoardCard("Wednesday: Tom", "Sized every item himself, by gut feel, in hours.")),
                    Beat(16, card = sizing),
                    Beat(18, show = pieces, light = pieces, pulse = "big" to "a", card = BoardCard("Priya refines", "Offline maps becomes three: save, no signal, delete.")),
                    Beat(20, show = setOf("note"), light = setOf("note"), card = ready),
                    Beat(22, card = BoardCard("Thursday: the intern", "Refinement an event? No: an ongoing activity.")),
                ),
            ),
            // 23: "And Friday? There's always a Friday."
            Scene(
                23, "Friday: three goals", goal(),
                beats = listOf(
                    Beat(24, card = BoardCard("Friday", "Three Product Goals at once: coaching, social, smartwatch.")),
                    Beat(26, show = setOf("now"), light = setOf("now"), card = goalCard),
                    Beat(28, show = setOf("backlog", "next"), light = setOf("now", "next"), pulse = "now" to "next", card = oneAtATime),
                    Beat(30, card = extras),
                ),
            ),
            // 31: "Where does the one-list rule stop?"
            Scene(
                31, "The iPhone team joins", teams(),
                beats = listOf(
                    Beat(32, light = setOf("pb", "po"), pulse = "pb" to "po", card = manyTeams),
                ),
            ),
            // 33: "So, the gossip I should remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("source", "commit", "size", "refine", "teams"), card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-product-backlog--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "The list that never sleeps", backlog(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The list that never sleeps: the Product Backlog.")),
                    Beat(2, show = list3, light = list3, card = sourceCard),
                    Beat(4, light = setOf("bottom"), pulse = "bottom" to "mid", card = emergent),
                    Beat(6, show = setOf("po"), light = setOf("po", "top"), pulse = "po" to "top", card = ordered),
                    Beat(8, light = list3, card = BoardCard("Product Backlog item", "A feature, a crash fix, an upgrade. User stories are optional.")),
                ),
            ),
            // 9: "And where is this list going?"
            Scene(
                9, "Where the list is going", goal(),
                beats = listOf(
                    Beat(10, show = setOf("now"), light = setOf("now"), card = goalCard),
                    Beat(12, show = setOf("backlog"), light = setOf("backlog"), pulse = "backlog" to "now", card = BoardCard("A destination", "Runners train for their first ten kilometres with only our app.")),
                    Beat(14, show = setOf("next"), light = setOf("now", "next"), pulse = "now" to "next", card = oneAtATime),
                ),
            ),
            // 15: "Who keeps the list, while everyone else sleeps?"
            Scene(
                15, "Who keeps the list", owner(),
                beats = listOf(
                    Beat(16, show = duties, light = duties, card = accountable),
                    Beat(18, light = setOf("po"), card = BoardCard("One person", "Others convince the Product Owner; the organisation respects it.")),
                    Beat(20, light = setOf("transp"), card = transparent),
                    Beat(22, card = BoardCard("It never ends", "As long as the product lives. Details vary with the work.")),
                ),
            ),
            // 23: "The items at the bottom look so blurry from here."
            Scene(
                23, "Blurry at the bottom", refine(),
                beats = listOf(
                    Beat(24, light = setOf("big"), card = refinement),
                    Beat(26, card = sizing),
                    Beat(28, show = pieces + "note", light = pieces, pulse = "big" to "a", card = ready),
                ),
            ),
            // 29: "And if three teams share the app?"
            Scene(
                29, "Three teams, one list", teams(),
                beats = listOf(
                    Beat(30, light = setOf("pb", "po"), pulse = "pb" to "po", card = manyTeams),
                ),
            ),
            // 31: "Before I sleep. What do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("source", "commit", "size", "refine", "teams"), card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-product-backlog--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Our list", backlog(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "About us. About our list.")),
                    Beat(2, show = list3, light = list3, card = sourceCard),
                    Beat(4, light = setOf("top"), pulse = "mid" to "top", card = BoardCard("Emergent and ordered", "It changes as you learn, and it has a top and a bottom.")),
                    Beat(6, show = setOf("po"), light = setOf("po"), card = BoardCard("Quiz: a separate list", "A stakeholder's spreadsheet, handed to the Developers?")),
                    Beat(7, pulse = "po" to "top", card = BoardCard("One list", "Its ideas go through the Product Owner, onto the one list.")),
                ),
            ),
            // 9: "My turn. Every artifact makes a commitment..."
            Scene(
                9, "Quiz: the commitment", goal(),
                beats = listOf(
                    Beat(9, card = BoardCard("Quiz: the commitment", "Every artifact makes one. The Product Backlog's?")),
                    Beat(10, show = setOf("now"), light = setOf("now"), card = goalCard),
                    Beat(12, show = setOf("backlog"), light = setOf("backlog"), pulse = "backlog" to "now", card = BoardCard("Under the picture", "The goal lives in the backlog; the list emerges to fulfil it.")),
                    Beat(13, card = BoardCard("Three goals at once?", "No kiss for that one.")),
                    Beat(14, show = setOf("next"), light = setOf("now", "next"), pulse = "now" to "next", card = oneAtATime),
                ),
            ),
            // 15: "Fine, I'm a one-goal man. Who keeps the list in order?"
            Scene(
                15, "Who keeps it in order", owner(),
                beats = listOf(
                    Beat(16, show = duties, light = duties, card = accountable),
                    Beat(18, light = setOf("po"), card = BoardCard("Quiz: who sizes items?", "The Product Owner, who knows the value? No kiss.")),
                ),
            ),
            // 19: "The Product Owner, because they know the value."
            Scene(
                19, "Sizing and refining", refine(),
                beats = listOf(
                    Beat(20, light = setOf("big"), card = sizing),
                    Beat(22, card = BoardCard("Quiz: is refinement an event?", "Breaking items down and adding detail. An event?")),
                    Beat(23, card = BoardCard("No: an ongoing activity", "Teams may meet for it; the Scrum Guide has no such event.")),
                    Beat(26, show = pieces + "note", light = pieces, pulse = "big" to "a", card = ready),
                    Beat(28, card = extras),
                ),
            ),
            // 29: "Why does our list have to be so visible?"
            Scene(
                29, "No secrets", backlog(),
                beats = listOf(
                    Beat(30, light = list3, card = transparent),
                    Beat(31, card = BoardCard("Quiz: change mid-Sprint?", "The Product Backlog is refined as needed. The Sprint Goal stays.")),
                    Beat(34, card = BoardCard("Never finished", "As long as the product lives, there's something to improve.")),
                ),
            ),
            // 35: "Then a last quiz. Three teams build one product."
            Scene(
                35, "Quiz: three teams", teams(),
                beats = listOf(
                    Beat(35, card = BoardCard("Quiz: three teams", "One product. How many Product Backlogs?")),
                    Beat(36, light = setOf("pb", "po"), pulse = "pb" to "po", card = manyTeams),
                ),
            ),
            // 38: "Last question, for the exam. What do we remember?"
            Scene(
                38, "For the exam", exam(),
                beats = listOf(
                    Beat(39, show = all6, light = setOf("source", "commit", "size", "refine", "teams"), card = takeaways),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
