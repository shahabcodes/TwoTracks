package com.shahaabapps.core.content

/** The Product Owner: one person who maximises value: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesProductOwner {
    private val classic = Podcast(
        "psm-product-owner",
        listOf(
            maya("Hi, and welcome back. Today: the Product Owner, the one person on a Scrum Team who decides what's worth building next. The P S M one assessment loves this accountability, because real companies so often get it wrong."),
            leo("I'll admit, I've always thought of the Product Owner as the boss who writes the tickets. What is it, in plain words?"),
            maya("The Scrum Guide says the Product Owner is accountable for maximising the value of the product that comes out of the Scrum Team's work. Value is whatever makes the product worth having: happier runners, more subscriptions, fewer support calls. And the Guide leaves how they do it open; it varies a lot between companies."),
            leo("Maximising value sounds vague. What do they actually do on a Monday?"),
            maya("They're accountable for managing the Product Backlog well. The Product Backlog is the ordered list of everything that might be needed in the product. The Guide names four parts: develop and communicate the Product Goal; create and clearly communicate the items; order them; and keep the backlog transparent, visible and understood."),
            leo("Product Goal first, then."),
            maya("The longer-term objective for the product. Picture a running app whose Product Goal is: a beginner can train for a first ten-kilometre race entirely in the app. Every item on the backlog should move toward it. The Product Owner writes it down and keeps saying it, so the whole team knows what it's all for."),
            leo("And ordering. Is that the same as priority?"),
            maya("Close, but the Guide says ordered, and the exam uses that word. Ordered means a single sequence: first, second, third, no ties. Offline sync above dark mode, because more runners lose data than ask for dark mode. The Product Owner decides the order, and the order is where their decisions become visible."),
            leo("So the Product Owner is the boss of the Developers?"),
            maya("[pause] No. That's the trap. The Product Owner decides what is most valuable and in what order. The Developers decide how to build it, and how much they can take on in a Sprint. In refinement, the Developers who will do the work size the items; the Product Owner can help them understand the trade-offs, but doesn't do the sizing for them."),
            leo("Refinement, remind me?"),
            maya("Ongoing work on the Product Backlog: breaking big items into smaller ones and adding detail, so they're ready for a Sprint. It's not a meeting the Guide prescribes; it's an activity. The Product Backlog episode covers it."),
            leo("Okay, here's my real doubt. Our CEO messages developers directly with one more feature. What happens then?"),
            maya("The Guide is clear: those who want to change the Product Backlog do it by convincing the Product Owner. Not by going round them. For example, the CEO wants a sharing button. She makes her case to the Product Owner, who decides where it goes in the order. The Developers keep working toward the Sprint Goal."),
            leo("And if the CEO overrules the Product Owner?"),
            maya("Then the Product Owner can't do the job. The Guide says that for Product Owners to succeed, the whole organisation must respect their decisions. Those decisions show up in what's on the Product Backlog, its order, and in the Increment, the usable product inspected at the Sprint Review."),
            leo("Our company has a product committee. Three managers decide together. Is that a Product Owner?"),
            maya("No. The Guide says the Product Owner is one person, not a committee. They may represent the needs of many stakeholders, the people outside the team with a stake in the product. They listen to all of them. But one person decides the order and answers for the value."),
            leo("And with several teams on one big app? One Product Owner each?"),
            maya("One for the product. Several Scrum Teams working on the same product share the same Product Goal, the same Product Backlog and the same Product Owner. The Scrum Team episode covers why."),
            leo("Can the Product Owner hand some of this off? Our business analyst writes most of the items."),
            maya("Yes. The Guide says the Product Owner may do the work or delegate it to others. But here's where it stops: they remain accountable. If the analyst writes a confusing item, it's still the Product Owner who answers for it. You can share the work, never the accountability."),
            leo("What about during the Sprint? Does the Product Owner just wait for the Review?"),
            maya("No. They help at Sprint Planning by proposing how the product could gain the most value this Sprint, and they're there when the Developers need to clarify or renegotiate scope. And one power is theirs alone: only the Product Owner can cancel a Sprint, and only if the Sprint Goal becomes obsolete."),
            leo("Obsolete, as in?"),
            maya("No longer worth doing. Say the Sprint Goal is a feature for a partner, and the partnership ends mid-Sprint. The Product Owner may cancel. A stakeholder being impatient isn't a reason, and the head of sales can't cancel it. And at the Sprint Review, the Product Owner doesn't stamp each item as accepted; the Review is a working session, not a gate."),
            leo("So what do I remember for the exam?"),
            maya("If a question asks who maximises the value of the product, it's the Product Owner. They're accountable for managing the Product Backlog: the Product Goal, clear items, the order, and transparency. One person, not a committee. They may delegate the work but stay accountable. Others change the backlog by convincing them. And only the Product Owner can cancel a Sprint."),
            leo("And anything to try today?"),
            maya("Write your product's goal in one sentence. Pick the top five items on your backlog and say why each comes before the next. Name who in your organisation can change that order today. Then read the Product Owner section of the Scrum Guide."),
            leo("One person, one order, one goal. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-product-owner",
        listOf(
            maya("Welcome to Case Files. Today's case: The Backlog With Three Owners. A running app team finished every Sprint on time, and the app went nowhere. Features appeared, vanished, came back. Users were confused. The Developers were exhausted. And nobody could say what the product was for."),
            leo("[chuckle] Detective Leo. A product going in circles. First suspect: slow Developers. Speed them up."),
            maya("Wrong suspect. They delivered every Sprint. The exam would look at the Product Owner, the one accountable, says the Scrum Guide, for maximising the value of the product resulting from the Scrum Team's work. Value is whatever makes the product worth having."),
            leo("And how is that supposed to work?"),
            maya("Through the Product Backlog, the ordered list of everything that might be needed in the product. The Product Owner is accountable for managing it: developing and communicating the Product Goal, writing clear items, ordering them, and keeping the backlog transparent, visible and understood. First clue: who owned it."),
            leo("Who did?"),
            maya("A product committee. Three managers: marketing, sales and the head of mobile. Each Monday, each moved their own favourite item to the top. Whoever spoke last won."),
            leo("Second suspect, then. The Scrum Master. They should have ordered the backlog fairly."),
            maya("[pause] Also wrong. Ordering the Product Backlog is the Product Owner's accountability, not the Scrum Master's. And the Guide is plain: the Product Owner is one person, not a committee. They may represent the needs of many stakeholders, the people outside the team with a stake in the product. But one person decides the order."),
            leo("Under the picture, why does one person matter so much?"),
            maya("Because an order is a single sequence: first, second, third, no ties. Three people pulling three ways can't produce one. And the order is where decisions become visible. Second clue: the Product Goal."),
            leo("What did it say?"),
            maya("Nothing. There wasn't one. The Product Goal is the longer-term objective for the product, like: a beginner can train for a first ten-kilometre race entirely in the app. Without it, nothing tells you whether an item belongs. Developing and communicating it is the Product Owner's job."),
            leo("Does a Product Goal ever change?"),
            maya("It can. The Guide says the team fulfils one Product Goal, or abandons it, before taking on the next. One at a time. The Product Backlog episode covers that part."),
            leo("Third clue?"),
            maya("The CEO. Every Friday she messaged one of the Developers directly. Can you just add a sharing button? They did, mid-Sprint, behind everyone's back."),
            leo("Well, she is the CEO."),
            maya("Even so. The Guide says those who want to change the Product Backlog do it by convincing the Product Owner. And for Product Owners to succeed, the whole organisation must respect their decisions, the CEO included. Those decisions show in the backlog's content and order, and in the Increment, the usable product, at the Sprint Review."),
            leo("Fourth clue? I can feel a fourth."),
            maya("The business analyst. Eventually the committee chose one manager, Priya, as Product Owner. She handed all the item writing to the analyst. When the items were vague, Priya said: not my problem, he wrote them."),
            leo("Can she delegate it, though?"),
            maya("She can. The Guide says the Product Owner may do the work or delegate it. That's where it stops: the Product Owner remains accountable. You can share the work of writing items; you can't hand off answering for them."),
            leo("And the final clue?"),
            maya("In week two of a Sprint, the head of sales announced the Sprint was cancelled, because a big client wanted something else. The Developers dropped everything."),
            leo("Can he do that?"),
            maya("No. Only the Product Owner can cancel a Sprint, and only if the Sprint Goal becomes obsolete, no longer worth doing. A client changing their mind goes to the Product Owner, who decides where it goes in the order. And the Product Owner doesn't boss the Developers either: they decide how to build, and they size the items in refinement."),
            leo("So how did it end?"),
            maya("Priya became the one Product Owner for the whole app, and the organisation agreed to respect her decisions. She wrote a Product Goal on the wall, ordered the backlog top to bottom, and made the CEO's sharing button a case to argue with her, not a message to a Developer. Three Sprints later, runners finished their first ten-kilometre races."),
            leo("So, the case summary for the exam?"),
            maya("The Product Owner maximises the value of the product. They're accountable for managing the Product Backlog: the Product Goal, clear items, the order, and transparency. One person, not a committee. They may delegate the work but stay accountable. Others change the backlog by convincing them. And only the Product Owner can cancel a Sprint."),
            leo("Case closed. And to try today?"),
            maya("Write your product's goal in one sentence. Pick the top five items on your backlog and say why each comes before the next. Name who in your organisation can change that order today. Then read the Product Owner section of the Scrum Guide."),
            leo("Three owners, no owner. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-product-owner",
        listOf(
            maya("Okay, did you hear what happened on the app team this week? It was all about the Product Owner. Who it is, who it isn't, and who thought they were."),
            leo("[laugh] I heard the head of sales thinks he is. Start at the start. What does a Product Owner do?"),
            maya("The Scrum Guide says the Product Owner is accountable for maximising the value of the product that comes from the Scrum Team's work. They do it by managing the Product Backlog, the ordered list of everything the product might need: the Product Goal, clear items, the order, and keeping it transparent, visible and understood."),
            leo("Product Goal, the long-term aim, right?"),
            maya("Right. For the running app: a beginner can train for a first ten-kilometre race entirely in the app. Now. Monday. The managers."),
            leo("Plural?"),
            maya("Three of them formed a product committee: marketing, sales and the head of mobile. Each one moved their favourite item to the top of the backlog. The board changed three times before lunch."),
            leo("[chuckle] Democracy in action."),
            maya("And not allowed. The Guide says the Product Owner is one person, not a committee. They represent many stakeholders, the people outside the team with a stake in the product. They listen to everyone. Then one person orders the backlog: first, second, third, no ties."),
            leo("So who wins the argument, if all three have opinions?"),
            maya("Nobody wins an argument; the Product Owner decides, after listening. The Guide leaves how they weigh it up to them. What it insists on is a backlog that's transparent, visible and understood, so anyone can see the order and ask why."),
            leo("Ordered. Not prioritised?"),
            maya("The Guide says ordered, and the exam does too. One sequence, top to bottom. And the order is where the Product Owner's decisions become visible. Tuesday was the CEO."),
            leo("Oh no."),
            maya("She messaged Tom directly: can you squeeze in a sharing button? Tom, being Tom, did it that afternoon. Mid-Sprint."),
            leo("She's the CEO, though."),
            maya("The rule doesn't change for a CEO. The Guide says those who want to change the Product Backlog do it by convincing the Product Owner. And for Product Owners to succeed, the whole organisation must respect their decisions. Under the picture, if anyone can go round the Product Owner, the order means nothing."),
            leo("Wednesday?"),
            maya("Wednesday, Priya, who was finally named the one Product Owner. She gave all the item writing to the business analyst. Then the items were vague, and Priya said: not my problem, he wrote them."),
            leo("Is she allowed to hand it off?"),
            maya("She's allowed to delegate the work. The Guide says so. But the Product Owner remains accountable. Share the writing, keep the answering. That's where delegation stops."),
            leo("So what should Priya have said?"),
            maya("Something like: those items are mine to answer for, let's fix them together. Then she sits down with the analyst and the Developers, and the items get clear before Sprint Planning."),
            leo("Thursday. The head of sales?"),
            maya("The head of sales. A big client wanted something new, so on Thursday he announced the Sprint was cancelled. The Developers started packing up their work."),
            leo("[sigh] Can he do that?"),
            maya("No. Only the Product Owner can cancel a Sprint, and only if the Sprint Goal becomes obsolete, no longer worth doing. The client's request goes to the Product Owner, who decides where it sits in the order."),
            leo("When could a Sprint be cancelled, then?"),
            maya("Say the Sprint Goal is a feature for a partner, and the partnership ends mid-Sprint. The goal is obsolete, so the Product Owner may cancel. A client simply wanting something else isn't that."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. At the Sprint Review, the intern asked Priya to sign off each item as accepted, one by one, before anything could be released. Priya also told the Developers which library to use."),
            leo("Two mistakes in one Friday."),
            maya("Two. The Sprint Review is a working session to inspect the Increment, the usable product, not a gate the Product Owner stamps. And the Product Owner decides what, not how. The Developers decide how to build it and size the items. The Product Owner can help them see the trade-offs."),
            leo("And several teams on one app? Did anyone ask?"),
            maya("Marcus did. One Product Owner for the product, he was told: several Scrum Teams on one product share the same Product Goal, Product Backlog and Product Owner. The Scrum Team episode has the details."),
            leo("So, the gossip I should remember for the exam?"),
            maya("The Product Owner maximises the value of the product. They're accountable for managing the Product Backlog: the Product Goal, clear items, the order, and transparency. One person, not a committee. They may delegate the work but stay accountable. Others change the backlog by convincing them. And only the Product Owner can cancel a Sprint."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Write your product's goal in one sentence. Pick the top five items on your backlog and say why each comes before the next. Name who in your organisation can change that order today. Then read the Product Owner section of the Scrum Guide."),
            leo("Convince Priya, not Tom. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-product-owner",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning for P S M one. [pause] Tonight: the one person who decides what's worth building next."),
            ivy("One person. That's a lot of trust, Nova."),
            nova("It is. That person is the Product Owner. The Scrum Guide says they're accountable for maximising the value of the product that comes from the Scrum Team's work. [pause] Value is whatever makes the product worth having."),
            ivy("And how do they do that, in practice?"),
            nova("Through the Product Backlog, the ordered list of everything the product might need. They answer for managing it well. Four parts: develop and communicate the Product Goal. Create clear items. Order them. And keep the whole backlog transparent, visible and understood."),
            ivy("The Product Goal. Tell me what that sounds like."),
            nova("The product's longer-term objective. Picture a running app: a beginner can train for a first ten-kilometre race entirely in the app. [pause] Every item on the backlog should lead towards it."),
            ivy("And ordering. Isn't that just priority?"),
            nova("The Guide says ordered. One sequence, first, second, third, no ties. Offline sync above dark mode, because more runners lose their runs than ask for dark mode. [pause] The order is where the Product Owner's decisions show."),
            ivy("Why does the order matter so much?"),
            nova("Because the Developers take from the top. [pause] Under the picture, the order is the Product Owner's decisions, written down where everyone can see them. Change the order, and you've changed what gets built next."),
            ivy("So they're the Developers' boss?"),
            nova("No. That's the trap. The Product Owner decides what, and in what order. The Developers decide how to build it, and how much fits in a Sprint. In refinement, the work of breaking items down and adding detail, the Developers size the items. The Product Owner can help them see the trade-offs."),
            ivy("And when the CEO wants one more thing, at midnight?"),
            nova("[chuckle] She convinces the Product Owner. The Guide says anyone who wants to change the Product Backlog does it by convincing the Product Owner. Not by messaging a Developer in the dark."),
            ivy("And if she just overrules them?"),
            nova("Then nobody can do the job. The Guide says that for Product Owners to succeed, the whole organisation must respect their decisions. [pause] Those decisions show in the backlog's content and order, and in the Increment, the usable product, at the Sprint Review."),
            ivy("Some companies have a committee instead."),
            nova("Not in Scrum. The Product Owner is one person, not a committee. They represent many stakeholders, the people outside the team with a stake in the product. [pause] They listen to everyone. One person decides."),
            ivy("How do they choose, with all those voices?"),
            nova("The Guide leaves that to them; it varies a lot between companies. [pause] What it does insist on is a backlog that's transparent, visible and understood. Anyone can see the order, and ask why."),
            ivy("And if three teams build the same app?"),
            nova("Still one. Several Scrum Teams on one product share the same Product Goal, the same Product Backlog, and the same Product Owner."),
            ivy("Can the Product Owner share the load? It's a lot for one person."),
            nova("They can. The Guide says they may do the work, or delegate it to others. A business analyst can write items. [pause] But here's the edge: the Product Owner stays accountable. You can share the work. Never the answering for it."),
            ivy("And during the Sprint, do they just wait?"),
            nova("No. At Sprint Planning they propose how the product could gain the most value this Sprint. During it, they're there when the Developers need to clarify or renegotiate scope. And one power is theirs alone."),
            ivy("Tell me."),
            nova("Only the Product Owner can cancel a Sprint, and only if the Sprint Goal becomes obsolete. No longer worth doing. [pause] Not because someone in sales is impatient. And at the Sprint Review, they don't stamp each item as accepted. The Review is a working session, not a gate."),
            ivy("When would a Sprint Goal become obsolete?"),
            nova("Say it's a feature for a partner, and the partnership ends mid-Sprint. [pause] Then there's nothing left to aim at. The Product Backlog episode is your next late night, if you want more."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("The Product Owner maximises the value of the product. They're accountable for managing the Product Backlog: the Product Goal, clear items, the order, and transparency. One person, not a committee. They may delegate the work but stay accountable. Others change the backlog by convincing them. And only the Product Owner can cancel a Sprint."),
            ivy("And something to try today, before I sleep?"),
            nova("Write your product's goal in one sentence. [pause] Pick the top five items on your backlog and say why each comes before the next. Name who in your organisation can change that order today. Then read the Product Owner section of the Scrum Guide."),
            ivy("One person, one order. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-product-owner",
        listOf(
            nova("Tonight is date night, darling. And tonight, one of us decides what matters most."),
            rafe("Let me guess. You."),
            nova("[laugh] In Scrum it's the Product Owner, love. The Scrum Guide says they're accountable for maximising the value of the product that comes from the Scrum Team's work. Value is whatever makes the product worth having."),
            rafe("Maximising value. I try every evening. How do they do it?"),
            nova("By managing the Product Backlog, the ordered list of everything the product might need. Four parts: develop and communicate the Product Goal, create clear items, order them, and keep the backlog transparent, visible and understood."),
            rafe("Then quiz me. Exam style."),
            nova("A company wants a product committee of three managers to share the Product Owner accountability. Is that allowed?"),
            rafe("No. The Product Owner is one person, not a committee. They can represent many stakeholders, the people outside the team with a stake in the product, but one person decides."),
            nova("Right answer. One and only. [kiss]"),
            rafe("And what's the Product Goal, exactly?"),
            nova("The product's longer-term objective. For a running app: a beginner can train for a first ten-kilometre race entirely in the app. Every item should lead there."),
            rafe("Can the Product Goal ever change?"),
            nova("It can. The team fulfils one Product Goal, or abandons it, before taking on the next. One at a time, darling. Like us."),
            rafe("And ordering? Is that just a posh word for priority?"),
            nova("The Guide says ordered. One sequence, first, second, third, no ties. Offline sync above dark mode, because more runners lose runs than ask for dark mode. The order is where the Product Owner's decisions show. My turn. The CEO wants a sharing button added this Sprint. What should she do?"),
            rafe("Message a Developer directly. Faster that way."),
            nova("No kiss for that one. The Guide says those who want to change the Product Backlog do it by convincing the Product Owner. And the whole organisation must respect the Product Owner's decisions, the CEO included."),
            rafe("Then I'll try convincing you instead. Ask me another."),
            nova("Can the Product Owner delegate writing backlog items to a business analyst?"),
            rafe("Yes, they may delegate the work. But they remain accountable. Share the writing; keep the answering."),
            nova("Right answer. You know exactly where the line is. [kiss]"),
            rafe("Here's one for you. Is the Product Owner the Developers' boss? Can they tell them how to build it?"),
            nova("No. The Product Owner decides what and in what order. The Developers decide how, and how much they take on. In refinement, breaking items down and adding detail, the Developers size the items; the Product Owner can help them see the trade-offs."),
            rafe("Right answer. Nobody bosses you around. [kiss]"),
            nova("Mm. And three teams on one app?"),
            rafe("One Product Owner. Several Scrum Teams on one product share the same Product Goal, the same Product Backlog and the same Product Owner."),
            nova("And how does the Product Owner choose the order, with every stakeholder shouting?"),
            rafe("The Guide leaves that to them. What it insists on is a backlog that's transparent, visible and understood, so anyone can see the order and ask why."),
            nova("Then the big one. Who can cancel a Sprint, and when?"),
            rafe("Only the Product Owner, and only if the Sprint Goal becomes obsolete, no longer worth doing. Not the head of sales, and not because a client is impatient."),
            nova("Right answer. Nobody walks out on our Sprint. [kiss]"),
            rafe("When would a Sprint Goal become obsolete?"),
            nova("Say it's a feature for a partner, and the partnership ends mid-Sprint. Nothing left to aim at. Then the Product Owner may cancel."),
            rafe("And at the Sprint Review, does the Product Owner accept each item before release?"),
            nova("No. The Sprint Review is a working session to inspect the Increment, the usable product. It isn't a gate. The Product Owner's decisions show in the backlog's content and order, and in that Increment."),
            rafe("And during the Sprint, does the Product Owner just disappear?"),
            nova("Never. At Sprint Planning they propose how the product could gain the most value this Sprint, and they're there when the Developers need to clarify or renegotiate scope. The Product Backlog episode is our next date."),
            rafe("Then the last question, for the exam. What do we remember?"),
            nova("The Product Owner maximises the value of the product. They're accountable for managing the Product Backlog: the Product Goal, clear items, the order, and transparency. One person, not a committee. They may delegate the work but stay accountable. Others change the backlog by convincing them. And only the Product Owner can cancel a Sprint."),
            rafe("All of it, darling. That earns a long one. [kiss]"),
            nova("Mm. And something to try today?"),
            rafe("Write our product's goal in one sentence. Pick the top five items on our backlog and say why each comes before the next. Name who in our organisation can change that order today. Then read the Product Owner section of the Scrum Guide."),
            nova("You're first in my order, darling. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun role() = DiagramSpec(
        "The Product Owner maximises the product's value by managing the Product Backlog in four ways.",
        nodes = listOf(
            DNode("po", "Product Owner", 0.5f, 0.13f, w = 0.6f, h = 0.2f, tone = 1, sub = "maximises value"),
            DNode("goal", "Product Goal", 0.25f, 0.47f, w = 0.42f, h = 0.2f, sub = "develop, communicate"),
            DNode("items", "Clear items", 0.75f, 0.47f, w = 0.42f, h = 0.2f, sub = "create, communicate"),
            DNode("order", "Order", 0.25f, 0.8f, w = 0.42f, h = 0.2f, sub = "one sequence"),
            DNode("transp", "Transparency", 0.75f, 0.8f, w = 0.42f, h = 0.2f, sub = "visible, understood"),
        ),
        edges = listOf(
            DEdge("po", "goal"),
            DEdge("po", "items"),
        ),
        aspect = 0.8f,
    )

    private fun ordered() = DiagramSpec(
        "An ordered Product Backlog: one sequence toward the Product Goal, and the Developers take from the top.",
        nodes = listOf(
            DNode("i1", "1. Offline sync", 0.35f, 0.14f, w = 0.6f, h = 0.14f, tone = 1),
            DNode("i2", "2. Race training plans", 0.35f, 0.33f, w = 0.6f, h = 0.14f),
            DNode("i3", "3. Sharing button", 0.35f, 0.52f, w = 0.6f, h = 0.14f),
            DNode("i4", "4. Dark mode", 0.35f, 0.71f, w = 0.6f, h = 0.14f, tone = 2),
            DNode("devs", "Developers take from the top", 0.83f, 0.14f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("goal", "Product Goal: a first 10K, all in the app", 0.5f, 0.9f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("devs", "i1", "take"),
        ),
        aspect = 0.8f,
    )

    private fun decides() = DiagramSpec(
        "Stakeholders convince the one Product Owner, who orders the backlog; the Developers take from it and decide how.",
        nodes = listOf(
            DNode("stake", "Stakeholders", 0.25f, 0.15f, w = 0.42f, h = 0.2f, tone = 2, sub = "CEO, sales, users"),
            DNode("committee", "A committee? No", 0.75f, 0.15f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
            DNode("po", "Product Owner", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "one person"),
            DNode("pb", "Product Backlog", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "ordered"),
            DNode("devs", "Developers", 0.75f, 0.85f, w = 0.42f, h = 0.2f, sub = "how, and how much"),
        ),
        edges = listOf(
            DEdge("stake", "po", "convince"),
            DEdge("po", "pb", "orders"),
            DEdge("pb", "devs", "from the top"),
            DEdge("stake", "devs", "not this way", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun delegate() = DiagramSpec(
        "The Product Owner may hand out the work, and helps the Developers see trade-offs, but stays accountable.",
        nodes = listOf(
            DNode("po", "Product Owner", 0.5f, 0.15f, w = 0.6f, h = 0.2f, tone = 1, sub = "stays accountable"),
            DNode("ba", "Business analyst", 0.25f, 0.52f, w = 0.42f, h = 0.2f, sub = "writes items"),
            DNode("devs", "Developers", 0.75f, 0.52f, w = 0.42f, h = 0.2f, sub = "size the items"),
            DNode("note", "Share the work, never the accountability", 0.5f, 0.86f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("po", "ba", "delegates"),
            DEdge("po", "devs", "trade-offs"),
        ),
        aspect = 0.8f,
    )

    private fun inSprint() = DiagramSpec(
        "In and around the Sprint: the Product Owner proposes value, clarifies scope, and alone may cancel a Sprint whose goal is obsolete.",
        nodes = listOf(
            DNode("plan", "Sprint Planning", 0.25f, 0.16f, w = 0.42f, h = 0.2f, sub = "proposes value"),
            DNode("during", "During the Sprint", 0.75f, 0.16f, w = 0.42f, h = 0.2f, sub = "clarifies scope"),
            DNode("cancel", "Cancel a Sprint", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "goal obsolete only"),
            DNode("review", "Sprint Review", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "not a gate"),
            DNode("sales", "Head of sales", 0.25f, 0.84f, w = 0.42f, h = 0.2f, tone = 2, sub = "can't cancel"),
            DNode("teams", "Several teams", 0.75f, 0.84f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "one Product Owner"),
        ),
        edges = listOf(
            DEdge("sales", "cancel", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Product Owner questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("value", "Maximises value", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Product Owner"),
            DNode("manage", "Backlog management", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "goal, items, order, clarity"),
            DNode("one", "One person", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "not a committee"),
            DNode("deleg", "Delegate the work", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "stays accountable"),
            DNode("change", "Change the backlog", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "convince the Product Owner"),
            DNode("cancel", "Cancel a Sprint", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "only the Product Owner"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("value", "manage", "one", "deleg", "change", "cancel")
    private val parts = setOf("goal", "items", "order", "transp")
    private val list = setOf("i1", "i2", "i3", "i4", "devs")
    private val manageCard = BoardCard("Product Backlog management", "Product Goal, clear items, the order, and transparency.")
    private val goalCard = BoardCard("Product Goal", "A beginner trains for a first 10K, entirely in the app.")
    private val delegateCard = BoardCard("Delegate the work", "Never the accountability.")
    private val oneOwner = BoardCard("One product, one Product Owner", "Several Scrum Teams share the goal, backlog and Product Owner.")
    private val takeaways = BoardCard("Listen for the phrase", "Maximises value; one person; delegates, stays accountable; only one cancels.")
    private val today = BoardCard("Today", "Goal in one sentence; why the top five; who changes the order; read the Guide.")

    private fun classicBoard() = PodcastBoard(
        "psm-product-owner",
        listOf(
            // 0: "Hi, and welcome back. Today: the Product Owner..."
            Scene(
                0, "One person, the value", role(),
                beats = listOf(
                    Beat(0, card = BoardCard("Product Owner", "The one person who decides what's worth building next.")),
                    Beat(2, light = setOf("po"), card = BoardCard("Maximise value", "Of the product the Scrum Team's work produces.")),
                    Beat(4, show = parts, light = parts, pulse = "po" to "goal", card = manageCard),
                ),
            ),
            // 5: "Product Goal first, then."
            Scene(
                5, "Goal and order", ordered(),
                beats = listOf(
                    Beat(6, show = setOf("goal"), light = setOf("goal"), card = goalCard),
                    Beat(8, show = list, light = setOf("i1", "devs"), pulse = "devs" to "i1", card = BoardCard("Ordered, not prioritised", "One sequence, no ties; the order shows the decisions.")),
                ),
            ),
            // 9: "So the Product Owner is the boss of the Developers?"
            Scene(
                9, "What, not how", decides(),
                beats = listOf(
                    Beat(10, show = setOf("po", "pb", "devs"), light = setOf("devs"), pulse = "pb" to "devs", card = BoardCard("Not the boss", "The Developers decide how and how much, and size the items.")),
                    Beat(12, light = setOf("pb"), card = BoardCard("Refinement", "Ongoing: break items down and add detail, ready for a Sprint.")),
                    Beat(14, show = setOf("stake"), light = setOf("stake", "po"), pulse = "stake" to "po", card = BoardCard("Convince the Product Owner", "Nobody goes round them, not even the CEO.")),
                    Beat(16, light = setOf("po", "pb"), pulse = "po" to "pb", card = BoardCard("Respect the decisions", "The whole organisation must; they show in the backlog's order.")),
                    Beat(18, show = setOf("committee"), light = setOf("committee", "po"), card = BoardCard("One person, not a committee", "They represent many stakeholders; one decides.")),
                ),
            ),
            // 19: "And with several teams on one big app?"
            Scene(
                19, "Several teams", inSprint(),
                beats = listOf(
                    Beat(20, show = setOf("teams"), light = setOf("teams"), card = oneOwner),
                ),
            ),
            // 21: "Can the Product Owner hand some of this off?"
            Scene(
                21, "Sharing the work", delegate(),
                beats = listOf(
                    Beat(22, light = setOf("po", "ba", "note"), pulse = "po" to "ba", card = delegateCard),
                ),
            ),
            // 23: "What about during the Sprint?"
            Scene(
                23, "During the Sprint", inSprint(),
                beats = listOf(
                    Beat(24, show = setOf("plan", "during", "cancel"), light = setOf("plan", "during", "cancel"), card = BoardCard("Planning and clarifying", "Proposes value; renegotiates scope with the Developers.")),
                    Beat(26, show = setOf("sales", "review"), light = setOf("cancel", "review"), pulse = "sales" to "cancel", card = BoardCard("Only the Product Owner cancels", "Only if the Sprint Goal is obsolete. The Review is no gate.")),
                ),
            ),
            // 27: "So what do I remember for the exam?"
            Scene(
                27, "For the exam", exam(),
                beats = listOf(
                    Beat(28, show = all6, light = all6, card = takeaways),
                    Beat(30, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-product-owner--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Backlog With Three Owners..."
            Scene(
                0, "The Backlog With Three Owners", role(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Every Sprint on time; the product went in circles.")),
                    Beat(2, light = setOf("po"), card = BoardCard("Suspect cleared: speed", "They delivered. Look at who maximises value.")),
                    Beat(4, show = parts, light = parts, pulse = "po" to "items", card = manageCard),
                ),
            ),
            // 5: "Who did?"
            Scene(
                5, "Clue one: a committee", decides(),
                beats = listOf(
                    Beat(6, show = setOf("committee", "stake"), light = setOf("committee"), card = BoardCard("Three managers", "Each moved a favourite item to the top, every Monday.")),
                    Beat(8, show = setOf("po"), light = setOf("po", "committee"), pulse = "stake" to "po", card = BoardCard("Suspect cleared: Scrum Master", "Ordering is the Product Owner's. One person, not a committee.")),
                ),
            ),
            // 9: "Under the picture, why does one person matter so much?"
            Scene(
                9, "One order, one goal", ordered(),
                beats = listOf(
                    Beat(10, show = list, light = setOf("i1", "devs"), pulse = "devs" to "i1", card = BoardCard("Under the picture", "An order is one sequence; three people can't make one.")),
                    Beat(12, show = setOf("goal"), light = setOf("goal"), card = BoardCard("Clue two: no Product Goal", "Nothing said whether an item belonged.")),
                    Beat(14, card = BoardCard("One goal at a time", "Fulfil or abandon one before taking on the next.")),
                ),
            ),
            // 15: "Third clue?"
            Scene(
                15, "Clue three: the CEO", decides(),
                beats = listOf(
                    Beat(16, show = setOf("stake", "devs", "po", "pb"), light = setOf("stake", "devs"), pulse = "stake" to "devs", card = BoardCard("Friday messages", "A sharing button, added mid-Sprint, behind everyone's back.")),
                    Beat(18, light = setOf("stake", "po"), pulse = "stake" to "po", card = BoardCard("Convince the Product Owner", "The organisation, CEO included, must respect the decisions.")),
                ),
            ),
            // 19: "Fourth clue? I can feel a fourth."
            Scene(
                19, "Clue four: the analyst", delegate(),
                beats = listOf(
                    Beat(20, light = setOf("ba"), card = BoardCard("Not my problem", "Priya handed the items to the analyst, then blamed him.")),
                    Beat(22, light = setOf("po", "ba", "note"), pulse = "po" to "ba", card = delegateCard),
                ),
            ),
            // 23: "And the final clue?"
            Scene(
                23, "The final clue", inSprint(),
                beats = listOf(
                    Beat(24, show = setOf("sales", "cancel"), light = setOf("sales"), pulse = "sales" to "cancel", card = BoardCard("Sales cancelled the Sprint", "Because a client wanted something else.")),
                    Beat(26, show = setOf("plan", "during", "review", "teams"), light = setOf("cancel"), card = BoardCard("Only the Product Owner", "Cancels, and only if the Sprint Goal is obsolete.")),
                    Beat(28, light = setOf("plan", "during"), card = BoardCard("How it ended", "One Product Owner, a written goal, an ordered backlog.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed", exam(),
                beats = listOf(
                    Beat(30, show = all6, light = all6, card = takeaways),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-product-owner--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the app team this week?..."
            Scene(
                0, "All about the Product Owner", role(),
                beats = listOf(
                    Beat(0, card = BoardCard("This week", "Who the Product Owner is, isn't, and thought they were.")),
                    Beat(2, show = parts, light = setOf("po"), pulse = "po" to "goal", card = manageCard),
                    Beat(4, light = setOf("goal"), card = goalCard),
                ),
            ),
            // 5: "Plural?"
            Scene(
                5, "Monday: the committee", decides(),
                beats = listOf(
                    Beat(6, show = setOf("committee"), light = setOf("committee"), card = BoardCard("Three managers", "The board changed three times before lunch.")),
                    Beat(8, show = setOf("po", "stake"), light = setOf("po"), pulse = "stake" to "po", card = BoardCard("One person", "Not a committee. Listen to everyone, then order.")),
                    Beat(10, show = setOf("pb", "devs"), light = setOf("pb"), pulse = "po" to "pb", card = BoardCard("Transparent, visible, understood", "Anyone can see the order and ask why.")),
                ),
            ),
            // 11: "Ordered. Not prioritised?"
            Scene(
                11, "Ordered", ordered(),
                beats = listOf(
                    Beat(12, light = setOf("i1", "devs"), pulse = "devs" to "i1", card = BoardCard("Ordered, not prioritised", "One sequence, top to bottom, no ties.")),
                ),
            ),
            // 13: "Oh no."
            Scene(
                13, "Tuesday: the CEO", decides(),
                beats = listOf(
                    Beat(14, light = setOf("stake", "devs"), pulse = "stake" to "devs", card = BoardCard("Squeeze it in", "A sharing button, straight to Tom, mid-Sprint.")),
                    Beat(16, light = setOf("stake", "po"), pulse = "stake" to "po", card = BoardCard("Convince the Product Owner", "Go round them, and the order means nothing.")),
                ),
            ),
            // 17: "Wednesday?"
            Scene(
                17, "Wednesday: Priya", delegate(),
                beats = listOf(
                    Beat(18, light = setOf("ba"), card = BoardCard("Not my problem", "The analyst wrote them, said Priya.")),
                    Beat(20, light = setOf("po", "ba", "note"), pulse = "po" to "ba", card = delegateCard),
                    Beat(22, light = setOf("po"), card = BoardCard("Fix them together", "Mine to answer for: clear items before Sprint Planning.")),
                ),
            ),
            // 23: "Thursday. The head of sales?"
            Scene(
                23, "Thursday: sales", inSprint(),
                beats = listOf(
                    Beat(24, show = setOf("sales", "cancel"), light = setOf("sales"), pulse = "sales" to "cancel", card = BoardCard("Cancelled, he said", "A big client wanted something new.")),
                    Beat(26, show = setOf("plan", "during", "review", "teams"), light = setOf("cancel"), card = BoardCard("Only the Product Owner", "And only if the Sprint Goal is obsolete.")),
                    Beat(28, card = BoardCard("Obsolete", "A partner feature, and the partnership ends mid-Sprint.")),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: the intern", inSprint(),
                beats = listOf(
                    Beat(30, light = setOf("review"), card = BoardCard("Sign off every item?", "And choose the Developers' library, too?")),
                    Beat(32, light = setOf("review"), card = BoardCard("Not a gate", "The Review inspects the Increment. How is the Developers'.")),
                    Beat(34, light = setOf("teams"), card = oneOwner),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = all6, card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-product-owner--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "One person decides", role(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The one person who decides what's worth building next.")),
                    Beat(2, light = setOf("po"), card = BoardCard("Maximise value", "Whatever makes the product worth having.")),
                    Beat(4, show = parts, light = parts, pulse = "po" to "items", card = manageCard),
                ),
            ),
            // 5: "The Product Goal. Tell me what that sounds like."
            Scene(
                5, "Goal and order", ordered(),
                beats = listOf(
                    Beat(6, show = setOf("goal"), light = setOf("goal"), card = goalCard),
                    Beat(8, show = list, light = setOf("i1", "i2"), card = BoardCard("Ordered", "One sequence, no ties.")),
                    Beat(10, light = setOf("i1", "devs"), pulse = "devs" to "i1", card = BoardCard("Under the picture", "The Developers take from the top: the order is the decision.")),
                ),
            ),
            // 11: "So they're the Developers' boss?"
            Scene(
                11, "What, not how", decides(),
                beats = listOf(
                    Beat(12, show = setOf("po", "pb", "devs"), light = setOf("devs"), pulse = "pb" to "devs", card = BoardCard("Not the boss", "The Developers decide how and how much, and size the items.")),
                    Beat(14, show = setOf("stake"), light = setOf("stake", "po"), pulse = "stake" to "po", card = BoardCard("Convince the Product Owner", "Not a message to a Developer at midnight.")),
                    Beat(16, light = setOf("po", "pb"), pulse = "po" to "pb", card = BoardCard("Respect the decisions", "They show in the backlog's order and the Increment.")),
                    Beat(18, show = setOf("committee"), light = setOf("committee"), card = BoardCard("One person", "Not a committee. They listen, then one decides.")),
                    Beat(20, light = setOf("pb"), card = BoardCard("Transparent, visible, understood", "Anyone can see the order, and ask why.")),
                ),
            ),
            // 21: "And if three teams build the same app?"
            Scene(
                21, "Sharing the load", delegate(),
                beats = listOf(
                    Beat(22, light = setOf("po"), card = oneOwner),
                    Beat(24, light = setOf("po", "ba", "note"), pulse = "po" to "ba", card = delegateCard),
                ),
            ),
            // 25: "And during the Sprint, do they just wait?"
            Scene(
                25, "During the Sprint", inSprint(),
                beats = listOf(
                    Beat(26, show = setOf("plan", "during"), light = setOf("plan", "during"), card = BoardCard("Planning and clarifying", "Proposes value; there when scope needs renegotiating.")),
                    Beat(28, show = setOf("cancel", "review", "sales", "teams"), light = setOf("cancel", "review"), pulse = "sales" to "cancel", card = BoardCard("Only the Product Owner cancels", "Only if the Sprint Goal is obsolete. The Review is no gate.")),
                    Beat(30, light = setOf("cancel"), card = BoardCard("Obsolete", "A partner feature, and the partnership ends mid-Sprint.")),
                ),
            ),
            // 31: "Before you send me off to bed. What do I remember for the exam?"
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
        "psm-product-owner--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Who decides what matters", role(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "One of us decides what matters most.")),
                    Beat(2, light = setOf("po"), card = BoardCard("Maximise value", "Of the product the Scrum Team's work produces.")),
                    Beat(4, show = parts, light = parts, pulse = "po" to "goal", card = manageCard),
                ),
            ),
            // 5: "Then quiz me. Exam style."
            Scene(
                5, "Quiz: a committee?", decides(),
                beats = listOf(
                    Beat(6, show = setOf("committee"), light = setOf("committee"), card = BoardCard("Quiz", "Three managers share the Product Owner job. Allowed?")),
                    Beat(7, show = setOf("po", "stake"), light = setOf("po"), pulse = "stake" to "po", card = BoardCard("No: one person", "They represent many stakeholders; one decides.")),
                ),
            ),
            // 9: "And what's the Product Goal, exactly?"
            Scene(
                9, "Goal and order", ordered(),
                beats = listOf(
                    Beat(10, show = setOf("goal"), light = setOf("goal"), card = goalCard),
                    Beat(12, card = BoardCard("One goal at a time", "Fulfil or abandon one before taking on the next.")),
                    Beat(14, show = list, light = setOf("i1", "devs"), pulse = "devs" to "i1", card = BoardCard("Ordered", "One sequence, no ties. Quiz: the CEO's sharing button?")),
                ),
            ),
            // 15: "Message a Developer directly. Faster that way."
            Scene(
                15, "No kiss: going round", decides(),
                beats = listOf(
                    Beat(15, show = setOf("stake", "devs"), light = setOf("stake", "devs"), pulse = "stake" to "devs", card = BoardCard("Message a Developer?", "No kiss for that one.")),
                    Beat(16, show = setOf("po", "pb"), light = setOf("stake", "po"), pulse = "stake" to "po", card = BoardCard("Convince the Product Owner", "The organisation must respect the decisions, CEO included.")),
                ),
            ),
            // 17: "Then I'll try convincing you instead. Ask me another."
            Scene(
                17, "Quiz: who does what", delegate(),
                beats = listOf(
                    Beat(18, card = BoardCard("Quiz", "Can a business analyst write the backlog items?")),
                    Beat(19, light = setOf("po", "ba", "note"), pulse = "po" to "ba", card = BoardCard("Yes, but", "Delegate the work; the Product Owner stays accountable.")),
                    Beat(21, card = BoardCard("Quiz: the Developers' boss?", "Can the Product Owner say how to build it?")),
                    Beat(22, light = setOf("devs"), pulse = "po" to "devs", card = BoardCard("No: what, not how", "The Developers decide how, and size the items.")),
                ),
            ),
            // 24: "Mm. And three teams on one app?"
            Scene(
                24, "Quiz: cancelling", inSprint(),
                beats = listOf(
                    Beat(25, light = setOf("teams"), card = oneOwner),
                    Beat(26, card = BoardCard("Transparent, visible, understood", "Anyone can see the order and ask why.")),
                    Beat(28, light = setOf("cancel"), card = BoardCard("Quiz", "Who can cancel a Sprint, and when?")),
                    Beat(29, light = setOf("cancel", "sales"), pulse = "sales" to "cancel", card = BoardCard("Only the Product Owner", "And only if the Sprint Goal is obsolete.")),
                    Beat(32, card = BoardCard("Obsolete", "A partner feature, and the partnership ends mid-Sprint.")),
                    Beat(34, light = setOf("review"), card = BoardCard("Not a gate", "The Review inspects the Increment; nothing gets stamped.")),
                    Beat(36, light = setOf("plan", "during"), card = BoardCard("During the Sprint", "Proposes value at Planning; clarifies scope with the Developers.")),
                ),
            ),
            // 37: "Then the last question, for the exam."
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = all6, card = takeaways),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
