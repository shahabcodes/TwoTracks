package com.shahaabapps.core.content

/**
 * PSM I topics for module psm-team (Psm1): the Scrum Team and its three
 * accountabilities, paraphrased from The Scrum Guide (November 2020).
 */
internal object Psm1Team {
    private const val SG = "https://scrumguides.org/scrum-guide.html"
    private const val REV = "https://scrumguides.org/revisions.html"
    private const val ORG = "https://www.scrum.org/resources/"

    private val scrumTeam = Topic(
        id = "psm-scrum-team", title = "The Scrum Team: one team, self-managing and cross-functional", minutes = 20,
        references = listOf(
            B.ref("The Scrum Guide (2020): Scrum Team", SG),
            B.ref("What is a Scrum Team? (Scrum.org)", ORG + "what-scrum-team"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
            B.ref("Scrum Guide revisions: self-managing and accountabilities", REV),
        ),
        blocks = listOf(
            B.warmup(
                "psm-scrum-team-w1", "psm-scrum-team",
                "The Pocketwise Scrum Team has grown to 14 people and its Daily Scrum drags. What does the Scrum Guide suggest?",
                listOf(
                    "Reorganise into several Scrum Teams sharing one Product Goal, Product Backlog and Product Owner",
                    "Move the testers into a separate QA sub-team",
                    "Add a second Product Owner to share the load",
                ), 0,
                "Look for the answer that keeps one product, one goal and one backlog.",
            ),
            B.plain("""
                The Scrum Team is the small group that does all the work of Scrum. The Guide calls it the
                fundamental unit, the smallest building block. It has exactly one Scrum Master, one Product Owner,
                and Developers. Inside it there are no sub-teams and no hierarchy: nobody is the boss of anybody
                else. Picture your Android squad: the person deciding what is most valuable, the person helping the
                team work well, and the people building the app, its backend and its tests. All of them focus on
                one objective at a time, the Product Goal.
            """),
            B.diagram(
                caption = "One Scrum Team: a Product Owner, a Scrum Master and Developers, all aimed at the same Product Goal, with no sub-teams and no hierarchy.",
                nodes = listOf(
                    DNode("goal", "Product Goal", 0.5f, 0.12f, w = 0.36f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    DNode("po", "Product Owner", 0.18f, 0.45f, w = 0.3f, h = 0.2f, sub = "maximises value"),
                    DNode("sm", "Scrum Master", 0.5f, 0.45f, w = 0.3f, h = 0.2f, sub = "effectiveness"),
                    DNode("dev", "Developers", 0.82f, 0.45f, w = 0.3f, h = 0.2f, sub = "the Increment"),
                    DNode("n", "One team: no sub-teams, no hierarchy, 10 or fewer", 0.5f, 0.82f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("po", "goal"),
                    DEdge("sm", "goal"),
                    DEdge("dev", "goal"),
                ),
                aspect = 0.75f,
            ),
            B.h("Cross-functional and self-managing"),
            B.p("""
                Cross-functional means the team has, between its members, all the skills needed to create value
                every Sprint. For a mobile team that might be Android, backend, design and testing. No hand-off to
                another department is needed to finish a usable Increment. Self-managing means the team decides
                for itself who does what, when, and how. Nobody outside the team assigns the tasks, and inside the
                team the Scrum Master does not either. The 2020 Guide changed several words, and the exam uses the
                new ones.
            """),
            B.table(
                listOf("Older wording (2017)", "2020 wording", "What it means"),
                listOf("Development Team", "Developers", "One Scrum Team; Developers are people in it, not a team inside a team"),
                listOf("Roles", "Accountabilities", "Three accountabilities: Product Owner, Scrum Master, Developers"),
                listOf("Self-organising", "Self-managing", "The team decides who does what, when and how"),
                listOf("Three Daily Scrum questions", "Removed", "The Developers choose how to run their Daily Scrum"),
            ),
            B.check(
                "psm-scrum-team-k1", "psm-scrum-team",
                "Who decides which person on the Scrum Team picks up the Play Store release work?",
                listOf("The Scrum Team itself, because it is self-managing", "The Scrum Master assigns it", "The Product Owner, because she orders the work"), 0,
                "Self-managing teams decide internally who does what, when and how.",
                whyWrong = mapOf(
                    1 to "The Scrum Master coaches self-management; assigning tasks would undermine it.",
                    2 to "The Product Owner orders the Product Backlog, not who does each piece of work.",
                ),
            ),
            B.h("How big, and what to do when it grows"),
            B.p("""
                The Guide says a Scrum Team is small enough to stay nimble and large enough to finish significant
                work in a Sprint, typically 10 or fewer people. Smaller teams communicate better and get more done.
                If a team gets too large, it should reorganise into several cohesive Scrum Teams, each focused on
                the same product. Those teams share the same Product Goal, the same Product Backlog and the same
                Product Owner. One product never gets a second backlog.
            """),
            B.check(
                "psm-scrum-team-k2", "psm-scrum-team",
                "Three Scrum Teams build the Pocketwise app. How many Product Backlogs and Product Owners should there be?",
                listOf(
                    "One Product Backlog and one Product Owner for all three teams",
                    "One backlog per team, merged by the Product Owner before release",
                    "One backlog and one Product Owner per team",
                ), 0,
                "Teams on the same product share one Product Goal, one Product Backlog and one Product Owner.",
                whyWrong = mapOf(
                    1 to "Separate backlogs hide the true order of work for the product; the Guide asks for one.",
                    2 to "Several Product Owners for one product would make the Product Owner a committee, which the Guide rules out.",
                ),
            ),
            B.h("Accountable together"),
            B.p("""
                The whole Scrum Team is accountable for creating a valuable, useful Increment every Sprint. An
                Increment is a finished, usable step forward of the product. The team is responsible for all
                product-related work: talking with stakeholders, verifying, maintaining, operating, experimenting
                and research. The organisation should structure and empower the team to manage its own work, and
                working in Sprints at a sustainable pace keeps the team focused and consistent.
            """),
            B.compare("Split by skill, or one team that can finish?",
                B.card("Sub-teams by skill", "Never inside a Scrum Team; the Guide rules out sub-teams.", "Android, backend and QA groups hand work to each other", "Work waits at every hand-off", "Nobody owns the usable Increment"),
                B.card("One cross-functional Scrum Team", "Always: this is what the Guide describes.", "Everyone doing the work is a Developer", "The team can finish a usable Increment alone", "The whole team is accountable for it"),
            ),
            B.check(
                "psm-scrum-team-k3", "psm-scrum-team",
                "A tester asks to be listed as the QA team inside the Scrum Team. What does the Scrum Guide say?",
                listOf(
                    "There are no sub-teams; everyone doing the work is a Developer",
                    "That is allowed if the Scrum Master agrees",
                    "That is allowed for testing but not for design",
                ), 0,
                "The Guide says there are no sub-teams or hierarchies within a Scrum Team. Testing skills sit with the Developers.",
                whyWrong = mapOf(
                    1 to "The Scrum Master cannot allow sub-teams; the Guide rules them out.",
                    2 to "No skill gets its own sub-team, testing included.",
                ),
            ),
            B.worked("""
                Pocketwise has grown to 14 people on one team, and the Daily Scrum has become a crowd. Step one:
                14 is well past the typical 10 or fewer, so reorganise. Step two: split into two cohesive Scrum
                Teams of seven, each cross-functional, with Android, backend and testing skills, so each can finish
                a usable Increment alone. Step three: both teams keep working from the same Product Backlog, toward
                the same Product Goal, with Priya as their one Product Owner. Each team has its own Scrum Master
                accountability, and the teams decide for themselves who joins which.
            """),
            B.faded(
                "psm-scrum-team-f1",
                "A Wear OS watch app is added and six new people join a team of eight. Apply the same steps.",
                listOf(
                    "Fourteen people is too many for one nimble team, so they reorganise into two cohesive Scrum Teams.",
                    "Each team gets the skills to finish a usable Increment alone: Android, Wear OS, backend and testing.",
                    "Both teams keep one Product Backlog and one Product Goal, and share one ___.",
                ),
                blank = 2,
                answers = listOf("Product Owner", "PO"),
                note = "Several Scrum Teams on one product share one Product Owner, one Product Backlog and one Product Goal.",
            ),
            B.mistakes(
                "Creating sub-teams such as a QA team or an architecture team inside the Scrum Team.",
                "Calling the Developers the Development Team; that phrase was dropped from the Guide in 2020.",
                "Letting the Scrum Master or a manager assign tasks, when the team is self-managing.",
                "Giving each of several teams on one product its own Product Backlog or Product Owner.",
                "Treating 10 as a hard limit; the Guide says typically 10 or fewer, and to split when the team is too large.",
            ),
            B.example("Pocketwise splits into two Scrum Teams of seven when it reaches 14 people. Both teams pull from Priya's one Product Backlog toward one Product Goal, and each can ship a usable Increment without waiting on the other."),
            B.h("Check yourself"),
            B.match(
                "psm-x-scrum-team-match", "Pair each term with its meaning.",
                "Cross-functional" to "Has all the skills to create value each Sprint",
                "Self-managing" to "Decides who does what, when and how",
                "Accountabilities" to "Product Owner, Scrum Master, Developers",
                "Product Goal" to "The one objective the team focuses on",
            ),
            B.gap(
                "psm-x-scrum-team-gap1", "The typical size of a Scrum Team.",
                "A Scrum Team is typically ___ or fewer people.", "10", "ten",
            ),
            B.tryIt("Try it this week", "Look at your own team or one you know.",
                "List the skills needed to ship one feature of your app to the Play Store",
                "Mark any skill that sits outside your team and causes a hand-off",
                "Note who decides who works on what today",
                "Read the Scrum Team section of the Scrum Guide",
            ),
            B.explain(
                "psm-scrum-team-x1",
                "Explain what a Scrum Team is and how it should grow, using an Android app team as the example.",
                "A Scrum Team has one Product Owner, one Scrum Master and Developers, with no sub-teams and no hierarchy. It is cross-functional, so the Android, backend and testing skills to ship a usable Increment are all inside it, and self-managing, so it decides who does what. It is typically 10 or fewer people; when it grows too large it splits into several teams that share one Product Backlog, Product Goal and Product Owner.",
                "Composition, no sub-teams" to listOf("no sub-teams", "no hierarchy", "one product owner"),
                "Cross-functional" to listOf("cross-functional", "all the skills"),
                "Self-managing" to listOf("self-managing", "who does what"),
                "Size and splitting" to listOf("10 or fewer", "ten or fewer", "splits", "several teams"),
            ),
            B.glossary(
                "Scrum Team" to "One Scrum Master, one Product Owner and Developers; the fundamental unit of Scrum.",
                "Cross-functional" to "Having, between its members, all the skills to create value each Sprint.",
                "Self-managing" to "Deciding internally who does what, when and how.",
                "Accountability" to "The 2020 word for what used to be a role: what someone is answerable for.",
                "Product Goal" to "The long-term objective of the product, the team's one target at a time.",
                "Increment" to "A usable, finished step toward the Product Goal.",
            ),
            B.recap(
                "One Scrum Master, one Product Owner and Developers; no sub-teams, no hierarchy.",
                "Cross-functional and self-managing; typically 10 or fewer, split when too large.",
                "Teams on one product share one Product Goal, Product Backlog and Product Owner; the whole team is accountable for the Increment.",
            ),
        ),
    )

    private val developers = Topic(
        id = "psm-developers", title = "Developers: the plan, the quality and each other", minutes = 20,
        references = listOf(
            B.ref("The Scrum Guide (2020): Developers", SG),
            B.ref("What is a Developer in Scrum? (Scrum.org)", ORG + "what-is-a-scrum-developer"),
            B.ref("Resources for Developers (Scrum.org)", "https://www.scrum.org/resources-developers"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
            B.ref("Scrum Guide revisions: Developers", REV),
        ),
        blocks = listOf(
            B.warmup(
                "psm-developers-w1", "psm-developers",
                "Who creates the plan for the Sprint, the Sprint Backlog?",
                listOf("The Developers", "The Scrum Master", "The Product Owner"), 0,
                "The plan belongs to the people who will do the work.",
            ),
            B.plain("""
                In Scrum, a Developer is anyone on the Scrum Team who is committed to creating any part of a usable
                Increment each Sprint. It is not a job title. On an Android team that includes the Kotlin
                developer, the backend developer, the designer and the tester. The Guide gives the Developers four
                accountabilities, four things they answer for: the plan, the quality, adapting every day, and
                holding each other accountable.
            """),
            B.diagram(
                caption = "The Developers answer for four things: the Sprint plan, quality through the Definition of Done, daily adaptation toward the Sprint Goal, and each other.",
                nodes = listOf(
                    DNode("dev", "Developers", 0.5f, 0.5f, w = 0.3f, h = 0.18f, tone = 1),
                    DNode("plan", "The plan", 0.18f, 0.15f, w = 0.32f, h = 0.2f, sub = "Sprint Backlog"),
                    DNode("q", "Quality", 0.82f, 0.15f, w = 0.32f, h = 0.2f, sub = "Definition of Done"),
                    DNode("ad", "Adapt daily", 0.18f, 0.85f, w = 0.32f, h = 0.2f, sub = "toward Sprint Goal"),
                    DNode("each", "Each other", 0.82f, 0.85f, w = 0.32f, h = 0.2f, sub = "as professionals"),
                ),
                edges = listOf(
                    DEdge("dev", "plan"),
                    DEdge("dev", "q"),
                    DEdge("dev", "ad"),
                    DEdge("dev", "each"),
                ),
                aspect = 0.75f,
            ),
            B.h("The four accountabilities"),
            B.table(
                listOf("The Developers are accountable for", "In plain words", "On the Pocketwise team"),
                listOf("Creating a plan for the Sprint, the Sprint Backlog", "They decide how the work gets done", "Dana, Marcus and Sam break the CSV export into tasks at Sprint Planning"),
                listOf("Instilling quality by adhering to the Definition of Done", "Done means the agreed quality bar, every time", "No item counts until its tests pass and it runs on a real device"),
                listOf("Adapting their plan each day toward the Sprint Goal", "The plan changes as they learn", "A backend surprise on day 4 reshuffles the remaining tasks"),
                listOf("Holding each other accountable as professionals", "Peers, not a boss, keep each other honest", "Sam asks Marcus to add the missing test before merging"),
            ),
            B.check(
                "psm-developers-k1", "psm-developers",
                "Who decides how many Product Backlog items the team takes into the Sprint?",
                listOf("The Developers, in discussion with the Product Owner", "The Product Owner alone", "The Scrum Master, from the team's velocity"), 0,
                "The Developers select the items with the Product Owner; they know their capacity and past performance best.",
                whyWrong = mapOf(
                    1 to "The Product Owner discusses the items, but the Developers decide how much they can take on.",
                    2 to "The Scrum Master does not decide scope, and velocity is not part of the Scrum Guide.",
                ),
            ),
            B.h("Quality: the Definition of Done"),
            B.p("""
                The Definition of Done is the team's written quality bar: what must be true before a piece of work
                counts as finished, for example code reviewed, tests passing, and a working build on a real device.
                The Developers must follow it. If an item does not meet it, it is not part of the Increment and
                goes back to the Product Backlog for later. During the Sprint quality does not decrease, so nobody
                quietly skips the bar to finish faster.
            """),
            B.check(
                "psm-developers-k2", "psm-developers",
                "It is time for the Sprint Review. The export feature works but still has no automated tests, which the Definition of Done requires. What happens to it?",
                listOf(
                    "It is not part of the Increment and goes back to the Product Backlog",
                    "Release it now and add the tests next Sprint",
                    "The Product Owner can accept it as done anyway",
                ), 0,
                "Work cannot be considered part of an Increment unless it meets the Definition of Done. An item that still does not meet it cannot be released or even presented at the Sprint Review; instead, it returns to the Product Backlog for future consideration.",
                whyWrong = mapOf(
                    1 to "Shipping below the Definition of Done lowers quality and breaks transparency about what is really finished.",
                    2 to "Nobody can waive the Definition of Done for one item, not even the Product Owner.",
                ),
            ),
            B.h("Adapting every day, and sizing the work"),
            B.p("""
                The Daily Scrum is the Developers' own event for checking progress toward the Sprint Goal and
                adjusting the plan, but it is not the only time they can change it; they adjust whenever they
                learn something. The Developers who will do the work are also responsible for sizing Product
                Backlog items, which means estimating how big they are. The Product Owner can help them understand
                trade-offs but does not set the size. A person can be a Developer and also hold the Scrum Master or
                Product Owner accountability. When the Product Owner or Scrum Master works on Sprint Backlog items,
                they take part in the Daily Scrum as Developers.
            """),
            B.check(
                "psm-developers-k3", "psm-developers",
                "Who is responsible for sizing Product Backlog items?",
                listOf("The Developers who will do the work", "The Product Owner", "The Scrum Master, with input from managers"), 0,
                "The Guide gives sizing to the Developers who will be doing the work; the Product Owner may help them understand trade-offs.",
                whyWrong = mapOf(
                    1 to "The Product Owner may influence the Developers by explaining trade-offs, but does not size the items.",
                    2 to "Neither the Scrum Master nor managers size the work.",
                ),
            ),
            B.worked("""
                Sprint Goal: users can export a month of spending as a CSV file. On day four Dana finds the backend
                endpoint must page through results, which nobody planned. Plan: she raises it with the other
                Developers that morning instead of waiting. Adapt: the Developers update the Sprint Backlog
                themselves, adding the paging work. Scope: the Developers talk to Priya and agree to drop the
                nice-to-have file name picker, keeping the Sprint Goal. Quality: the export only counts when it
                meets the Definition of Done. Each other: Marcus reviews Dana's code before it is merged.
            """),
            B.faded(
                "psm-developers-f1",
                "Sprint Goal: users can set a monthly budget alert. Marcus finds that Android 14 needs a new notification permission. Walk the same accountabilities.",
                listOf(
                    "Marcus raises it with the other Developers at once; they do not wait for the next Daily Scrum to change the plan.",
                    "The Developers update the Sprint Backlog themselves and agree a smaller scope with the Product Owner, keeping the Sprint Goal.",
                    "The alert only joins the Increment when it meets the Definition of ___, including a test on an Android 14 device.",
                ),
                blank = 2,
                answers = listOf("Done"),
                note = "Quality is the Developers' accountability, held by the Definition of Done; work below it is not part of the Increment.",
            ),
            B.mistakes(
                "Thinking Developer means programmer; testers, designers and analysts who create the Increment are Developers too.",
                "Letting the Product Owner or a manager decide how much work goes into the Sprint.",
                "Calling an item done when it misses the Definition of Done, then fixing it later.",
                "Waiting for the next Daily Scrum to change the plan when a problem appears.",
                "Letting the Product Owner size items; the Developers who do the work size them.",
            ),
            B.example("When the backend surprise lands, the Pocketwise Developers change their own plan the same morning, keep the Sprint Goal, renegotiate a small item with Priya, and still hold every change to the Definition of Done."),
            B.h("Check yourself"),
            B.match(
                "psm-x-developers-match", "Pair each accountability with an example.",
                "Creating the plan" to "Breaking the export into tasks at Sprint Planning",
                "Instilling quality" to "Not merging until the Definition of Done is met",
                "Adapting daily" to "Reshuffling tasks after a backend surprise",
                "Each other" to "Asking a teammate to add the missing test",
            ),
            B.gap(
                "psm-x-developers-gap1", "Who sizes the work.",
                "The ___ who will do the work are responsible for sizing Product Backlog items.", "Developers", "developers",
            ),
            B.tryIt("Try it this week", "Use your own team's current work.",
                "Write down your team's Definition of Done, or notice that there is none",
                "Find one item called done last week and check it against that definition",
                "Note who sized the last few items, and who decided how much went into the Sprint",
                "Read the Developers section of the Scrum Guide",
            ),
            B.explain(
                "psm-developers-x1",
                "Explain what the Developers are accountable for, using an Android team's Sprint as the example.",
                "The Developers create the plan for the Sprint, the Sprint Backlog, and decide how much to take on with the Product Owner. They instil quality by following the Definition of Done, so an untested feature is not part of the Increment. They adapt their plan each day toward the Sprint Goal, and they hold each other accountable as professionals, for example through code review.",
                "The plan, the Sprint Backlog" to listOf("sprint backlog", "the plan"),
                "Quality through the Definition of Done" to listOf("definition of done", "quality"),
                "Adapt daily toward the Sprint Goal" to listOf("each day", "daily", "sprint goal"),
                "Each other as professionals" to listOf("each other", "professionals"),
            ),
            B.glossary(
                "Developers" to "People in the Scrum Team committed to creating any aspect of a usable Increment each Sprint.",
                "Sprint Backlog" to "The Developers' plan: the Sprint Goal, the selected items and how to deliver them.",
                "Definition of Done" to "The formal description of the quality the Increment must meet.",
                "Sizing" to "Estimating how big a Product Backlog item is; done by the Developers.",
                "Sprint Goal" to "The single objective for the Sprint.",
            ),
            B.recap(
                "Developers create the Sprint Backlog, instil quality through the Definition of Done, adapt daily toward the Sprint Goal, and hold each other accountable.",
                "They decide how much to take into a Sprint and size the Product Backlog items.",
                "Work below the Definition of Done is not part of the Increment; it goes back to the Product Backlog.",
            ),
        ),
    )

    private val productOwner = Topic(
        id = "psm-product-owner", title = "The Product Owner: one person who maximises value", minutes = 22,
        references = listOf(
            B.ref("The Scrum Guide (2020): Product Owner", SG),
            B.ref("What is a Product Owner? (Scrum.org)", ORG + "what-is-a-product-owner"),
            B.ref("You're the Product Owner, but I don't know what that means (Scrum.org blog)", ORG + "blog/youre-product-owner-i-dont-know-what-means"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
            B.ref("The Scrum Artifacts (Scrum.org)", ORG + "scrum-artifacts"),
        ),
        blocks = listOf(
            B.warmup(
                "psm-product-owner-w1", "psm-product-owner",
                "Marketing, the CEO and customer support all want different things built first. Who decides the order of the Product Backlog?",
                listOf("The Product Owner", "The most senior stakeholder", "The Developers, by vote"), 0,
                "One person answers for the value of the product.",
            ),
            B.plain("""
                The Product Owner is accountable for maximising the value of the product that the Scrum Team
                creates. Value means what the product is worth to its users and the business, not how many features
                ship. Picture the person who decides that fixing bank sync for a million users comes before a new
                chart that ten people asked for. The Product Owner does this mainly through the Product Backlog, the
                single ordered list of everything that might be done to the product.
            """),
            B.diagram(
                caption = "Stakeholders convince the one Product Owner, who orders the Product Backlog; the Developers select from it at Sprint Planning.",
                nodes = listOf(
                    DNode("st", "Stakeholders", 0.12f, 0.3f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
                    DNode("po", "Product Owner", 0.42f, 0.3f, w = 0.28f, h = 0.2f, tone = 1, sub = "one person"),
                    DNode("pb", "Product Backlog", 0.78f, 0.3f, w = 0.34f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "ordered"),
                    DNode("dev", "Developers", 0.78f, 0.75f, w = 0.3f, h = 0.2f, sub = "select items"),
                    DNode("n", "Accountable for value, even when delegating", 0.3f, 0.78f, w = 0.5f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("st", "po", label = "convince"),
                    DEdge("po", "pb", label = "orders"),
                    DEdge("pb", "dev"),
                ),
                aspect = 0.75f,
            ),
            B.h("Managing the Product Backlog"),
            B.table(
                listOf("The Product Owner is accountable for", "On the Pocketwise team"),
                listOf("Developing and clearly communicating the Product Goal", "Priya shares the goal: help users save ten percent more each month"),
                listOf("Creating and clearly communicating Product Backlog items", "Each item says what users need and why"),
                listOf("Ordering Product Backlog items", "Bank sync fix first, new chart later"),
                listOf("Making sure the Product Backlog is transparent, visible and understood", "Anyone can open the backlog and see what is next and why"),
            ),
            B.p("""
                The Product Owner may do this work personally or delegate it to others, for example asking a
                Developer to write the detail for a technical item. Either way, the Product Owner stays
                accountable. Delegating the work does not delegate the accountability.
            """),
            B.check(
                "psm-product-owner-k1", "psm-product-owner",
                "Priya asks a senior Developer to write and refine the backend items in the Product Backlog. Who is accountable for the Product Backlog now?",
                listOf("Still the Product Owner", "The senior Developer", "The Scrum Master"), 0,
                "The Product Owner may delegate the work of backlog management but remains accountable for it.",
                whyWrong = mapOf(
                    1 to "The Developer is doing delegated work; accountability does not move with it.",
                    2 to "The Scrum Master helps with techniques for backlog management but is not accountable for the backlog.",
                ),
            ),
            B.h("One person, not a committee"),
            B.p("""
                The Guide is blunt: the Product Owner is one person, not a committee. The Product Owner may
                represent the needs of many stakeholders in the Product Backlog, but one person decides. Anyone who
                wants to change the backlog does so by convincing the Product Owner. For the Product Owner to
                succeed, the whole organisation must respect their decisions. Those decisions are visible in what
                the Product Backlog contains, in its order, and in the Increment shown at the Sprint Review. When
                several Scrum Teams work on one product, they share one Product Owner.
            """),
            B.check(
                "psm-product-owner-k2", "psm-product-owner",
                "The CEO messages a Developer directly: build the premium tier next. What should happen?",
                listOf(
                    "The CEO talks to the Product Owner, who decides where it goes in the Product Backlog",
                    "The Developer starts it, because the CEO outranks the Product Owner",
                    "The Scrum Master adds it to the current Sprint Backlog",
                ), 0,
                "People who want to change the Product Backlog do it by convincing the Product Owner; the organisation respects the Product Owner's decisions.",
                whyWrong = mapOf(
                    1 to "Seniority does not bypass the Product Owner; the Guide says the organisation must respect the Product Owner's decisions.",
                    2 to "The Scrum Master does not add work, and the Sprint Backlog belongs to the Developers.",
                ),
            ),
            B.h("The Product Owner in the events"),
            B.table(
                listOf("Event", "What the Product Owner does"),
                listOf("Sprint Planning", "Proposes how the product could gain value this Sprint; makes sure people are ready to discuss the most important items; the whole team crafts the Sprint Goal"),
                listOf("During the Sprint", "Clarifies and renegotiates scope with the Developers as they learn; only the Product Owner can cancel a Sprint, and only if the Sprint Goal becomes obsolete"),
                listOf("Daily Scrum", "Takes part only as a Developer, when actively working on Sprint Backlog items"),
                listOf("Sprint Review", "Joins the team and stakeholders to inspect the Increment and adjust the Product Backlog; it is not a gate for accepting work"),
            ),
            B.check(
                "psm-product-owner-k3", "psm-product-owner",
                "Halfway through the Sprint a competitor launches and the Sprint Goal no longer makes sense. Who can cancel the Sprint?",
                listOf("Only the Product Owner", "The Scrum Master", "The Developers, by agreement"), 0,
                "Only the Product Owner has the authority to cancel a Sprint, and only when the Sprint Goal becomes obsolete.",
                whyWrong = mapOf(
                    1 to "The Scrum Master ensures events happen well, but has no authority to cancel a Sprint.",
                    2 to "The Developers can raise it, but the decision belongs to the Product Owner.",
                ),
            ),
            B.worked("""
                Three requests land in one week: support wants the bank sync bug fixed, marketing wants a new
                spending chart, and the CEO wants a premium tier. Priya checks each against the Product Goal, help
                users save ten percent more each month. She orders the backlog: bank sync first, because users
                cannot save if their data is wrong; premium tier next; the chart later. She explains the order to
                each stakeholder and keeps the backlog visible. When the CEO pushes back, he makes his case to
                Priya; the Developers keep working from the backlog as ordered.
            """),
            B.faded(
                "psm-product-owner-f1",
                "A Google Play policy deadline, a dark-mode request from sales, and an analytics request from finance arrive together. Apply the same steps.",
                listOf(
                    "Priya checks each request against the Product Goal and what it is worth to users and the business.",
                    "She orders the Product Backlog: the policy fix first, because without it the app leaves the store.",
                    "Stakeholders who disagree must ___ Priya, because the organisation respects the Product Owner's decisions.",
                ),
                blank = 2,
                answers = listOf("convince", "persuade"),
                note = "The Product Owner is one person; others change the Product Backlog by convincing the Product Owner, not by going around them.",
            ),
            B.mistakes(
                "Treating the Product Owner as a committee, or giving each team on one product its own Product Owner.",
                "Letting senior stakeholders reorder the backlog or give work straight to the Developers.",
                "Believing that delegating backlog work also hands over the accountability for it.",
                "Using the Sprint Review as the moment the Product Owner accepts or rejects each item.",
                "Thinking the Scrum Master or the Developers can cancel a Sprint; only the Product Owner can.",
            ),
            B.example("Priya orders the Pocketwise backlog against the Product Goal, explains her order to every stakeholder, and the CEO makes his case to her instead of to the Developers."),
            B.h("Check yourself"),
            B.match(
                "psm-x-product-owner-match", "Pair each situation with the Product Owner's part.",
                "A stakeholder wants something sooner" to "Convince the Product Owner",
                "A Developer writes backlog items" to "The Product Owner stays accountable",
                "The Sprint Goal becomes obsolete" to "The Product Owner may cancel the Sprint",
                "Three teams on one product" to "One Product Owner for all of them",
            ),
            B.gap(
                "psm-x-product-owner-gap1", "What the Product Owner is accountable for maximising.",
                "The Product Owner is accountable for maximising the ___ of the product.", "value", "Value",
            ),
            B.tryIt("Try it this week", "Use a real backlog, yours or an open-source project's.",
                "Write your product's goal in one sentence",
                "Pick the top five items and say why each comes before the next",
                "Name who in your organisation can change that order today",
                "Read the Product Owner section of the Scrum Guide",
            ),
            B.explain(
                "psm-product-owner-x1",
                "Explain the Product Owner's accountability and why it is one person, using an Android app as the example.",
                "The Product Owner is accountable for maximising the value of the product, mainly by managing the Product Backlog: communicating the Product Goal, writing clear items, ordering them and keeping the backlog visible. The work can be delegated, but the accountability stays. The Product Owner is one person, not a committee, so stakeholders like the CEO must convince the Product Owner, and the organisation respects those decisions.",
                "Maximising value" to listOf("maximising the value", "maximizing the value", "value of the product"),
                "Managing the Product Backlog" to listOf("product backlog", "ordering", "order"),
                "Delegation keeps accountability" to listOf("delegate", "accountability stays", "remains accountable"),
                "One person, not a committee" to listOf("one person", "not a committee", "convince"),
            ),
            B.glossary(
                "Product Owner" to "The one person accountable for maximising the value of the product.",
                "Product Backlog" to "The single ordered list of what is needed to improve the product.",
                "Product Goal" to "The long-term objective for the product; the Product Owner develops and communicates it.",
                "Ordering" to "Deciding which Product Backlog items come first.",
                "Stakeholder" to "Anyone outside the Scrum Team with an interest in the product, such as users or executives.",
            ),
            B.recap(
                "The Product Owner maximises value, mainly through the Product Backlog: Product Goal, clear items, order, transparency.",
                "One person, not a committee; others convince the Product Owner, and the organisation respects the decisions.",
                "Work can be delegated, accountability cannot; only the Product Owner can cancel a Sprint.",
            ),
        ),
    )

    private val scrumMaster = Topic(
        id = "psm-scrum-master", title = "The Scrum Master: a true leader who serves", minutes = 22,
        references = listOf(
            B.ref("The Scrum Guide (2020): Scrum Master", SG),
            B.ref("What is a Scrum Master? (Scrum.org blog)", ORG + "blog/what-scrum-master"),
            B.ref("A Day in the Life of a Scrum Master (Scrum.org blog)", ORG + "blog/day-life-scrum-master"),
            B.ref("Scrum Glossary (Scrum.org)", ORG + "scrum-glossary"),
            B.ref("Scrum Guide revisions: true leaders who serve", REV),
        ),
        blocks = listOf(
            B.warmup(
                "psm-scrum-master-w1", "psm-scrum-master",
                "The Daily Scrum keeps running to 40 minutes. What should the Scrum Master do first?",
                listOf(
                    "Coach the Developers to keep it to 15 minutes and focused on the Sprint Goal",
                    "Run it personally as a status update to speed it up",
                    "Cancel it and collect updates by email instead",
                ), 0,
                "The Scrum Master helps the team run its events well, without taking them over.",
            ),
            B.plain("""
                The Scrum Master is accountable for establishing Scrum as the Scrum Guide defines it, by helping
                everyone in the team and the organisation understand how Scrum works and why. The Scrum Master is
                also accountable for the Scrum Team's effectiveness, by helping the team improve its practices
                within the framework. The Guide calls Scrum Masters true leaders who serve the Scrum Team and the
                larger organisation. Think of a coach, not a manager: a coach does not play the match or pick who
                kicks, but makes the whole side better.
            """),
            B.diagram(
                caption = "The Scrum Master serves three groups: the Scrum Team, the Product Owner and the wider organisation.",
                nodes = listOf(
                    DNode("sm", "Scrum Master", 0.5f, 0.2f, w = 0.32f, h = 0.2f, tone = 1, sub = "leads by serving"),
                    DNode("team", "Scrum Team", 0.17f, 0.7f, w = 0.3f, h = 0.2f, sub = "coaching"),
                    DNode("po", "Product Owner", 0.5f, 0.7f, w = 0.3f, h = 0.2f, sub = "backlog methods"),
                    DNode("org", "Organisation", 0.83f, 0.7f, w = 0.3f, h = 0.2f, sub = "adoption"),
                ),
                edges = listOf(
                    DEdge("sm", "team", label = "serves"),
                    DEdge("sm", "po"),
                    DEdge("sm", "org"),
                ),
                aspect = 0.7f,
            ),
            B.h("Who the Scrum Master serves, and how"),
            B.table(
                listOf("Serves", "How, in plain words"),
                listOf("The Scrum Team", "Coaching self-management and cross-functionality; helping the team focus on high-value Increments that meet the Definition of Done; causing impediments to be removed; making sure every event happens and is positive, productive and within its timebox"),
                listOf("The Product Owner", "Helping find techniques for defining the Product Goal and managing the backlog; helping the team see why clear, concise items matter; helping set up empirical product planning; facilitating stakeholder collaboration as needed"),
                listOf("The organisation", "Leading, training and coaching Scrum adoption; planning and advising Scrum implementations; helping people understand an empirical approach to complex work; removing barriers between stakeholders and Scrum Teams"),
            ),
            B.check(
                "psm-scrum-master-k1", "psm-scrum-master",
                "According to the Scrum Guide, who is accountable for the Scrum Team's effectiveness?",
                listOf("The Scrum Master", "The Product Owner", "The line manager of the Developers"), 0,
                "The Scrum Master is accountable for the Scrum Team's effectiveness, by enabling the team to improve its practices within the framework.",
                whyWrong = mapOf(
                    1 to "The Product Owner is accountable for maximising the value of the product, not for the team's effectiveness.",
                    2 to "Line managers are not part of the Scrum Team and have no accountability in the Guide.",
                ),
            ),
            B.h("What a Scrum Master does not do"),
            B.p("""
                A Scrum Master is not a project manager under another name. The Scrum Master does not assign
                tasks, decide what goes into the Sprint, or run the Daily Scrum as a status meeting; the Daily Scrum
                belongs to the Developers. An impediment is anything that blocks the team's progress that the team
                cannot easily solve itself. The Guide says the Scrum Master causes impediments to be removed, which
                often means helping the team or the organisation remove them, not fixing everything personally. A
                Scrum Master can also be a Developer on the same team.
            """),
            B.compare("Project manager habits, or Scrum Master?",
                B.card("Command and control", "Never the Scrum Master's way.", "Assigns tasks to people", "Collects status updates", "Decides scope and dates"),
                B.card("True leader who serves", "This is the Scrum Master in the Guide.", "Coaches the team to self-manage", "Causes impediments to be removed", "Makes sure events happen and stay within the timebox"),
            ),
            B.check(
                "psm-scrum-master-k2", "psm-scrum-master",
                "During the Sprint, a Developer has been blocked for two days waiting for the security team to approve an API key. What should the Scrum Master do?",
                listOf(
                    "Cause the impediment to be removed, for example by helping the Developers reach the security team",
                    "Reassign the Developer's task to someone else on the team",
                    "Extend the Sprint until the key arrives",
                ), 0,
                "Causing the removal of impediments is a Scrum Master service to the team; barriers between teams and the organisation are part of it.",
                whyWrong = mapOf(
                    1 to "The Scrum Master does not assign work; the Developers decide who does what.",
                    2 to "A Sprint has a fixed length and is never extended.",
                ),
            ),
            B.h("Also a Developer?"),
            B.p("""
                The Guide does not stop one person from holding the Scrum Master accountability and also working as
                a Developer. When the Scrum Master is actively working on items in the Sprint Backlog, they take
                part in the Daily Scrum as a Developer. The two accountabilities stay distinct: when coaching, they
                coach; when building, they build.
            """),
            B.check(
                "psm-scrum-master-k3", "psm-scrum-master",
                "Can the Scrum Master also be one of the Developers on the same Scrum Team?",
                listOf(
                    "Yes; the Guide allows it, and the accountabilities stay distinct",
                    "No, never",
                    "Only on teams of five or fewer",
                ), 0,
                "Nothing in the Guide forbids it. When working on Sprint Backlog items, they take part in the Daily Scrum as a Developer.",
                whyWrong = mapOf(
                    1 to "The Guide has no such rule; one person can hold both accountabilities.",
                    2 to "The Guide sets no team size for this.",
                ),
            ),
            B.worked("""
                A department head asks Tom, the Pocketwise Scrum Master, for a weekly status report from each
                Developer. Tom looks at whom he serves. The organisation: he explains how empiricism works and that
                the Increment and the Product Backlog already make progress transparent. The barrier: the head
                feels shut out, so Tom invites him to the Sprint Review, where stakeholders and the team inspect
                the real Increment together. The team: no individual reports are added, so the Developers stay
                self-managing.
            """),
            B.faded(
                "psm-scrum-master-f1",
                "A stakeholder keeps emailing the Developers with change requests in the middle of the Sprint. Apply the same thinking.",
                listOf(
                    "Tom sees a barrier between a stakeholder and the Scrum Team, which he serves the organisation by removing.",
                    "He helps the stakeholder see that changes go through the Product Owner, who orders the Product Backlog.",
                    "He facilitates stakeholder ___ by inviting her to the Sprint Review, where the team and stakeholders decide what to do next.",
                ),
                blank = 2,
                answers = listOf("collaboration"),
                note = "Facilitating stakeholder collaboration is one of the ways the Scrum Master serves the Product Owner.",
            ),
            B.mistakes(
                "Acting as a project manager who assigns tasks and chases individual status.",
                "Running the Daily Scrum as a status meeting for the Scrum Master; it is the Developers' event.",
                "Removing every impediment personally instead of helping the team and organisation remove them.",
                "Believing a Scrum Master can never be a Developer; the Guide allows one person to hold both.",
                "Serving only the team and forgetting the Product Owner and the wider organisation.",
            ),
            B.example("Tom answers a request for individual status reports by coaching the department head on empiricism and inviting him to the Sprint Review, instead of adding reports that would undermine self-management."),
            B.h("Check yourself"),
            B.match(
                "psm-x-scrum-master-match", "Pair each service with who it is for.",
                "Coaching self-management" to "The Scrum Team",
                "Techniques for backlog management" to "The Product Owner",
                "Leading Scrum adoption" to "The organisation",
                "Making sure events stay within the timebox" to "The Scrum Team, again",
            ),
            B.gap(
                "psm-x-scrum-master-gap1", "How the Guide describes Scrum Masters.",
                "Scrum Masters are true ___ who serve the Scrum Team and the larger organisation.", "leaders", "leader",
            ),
            B.tryIt("Try it this week", "Watch how your own team is led.",
                "List three impediments your team hit in the last month",
                "For each, note whether it was removed by the team, by a leader, or not at all",
                "Spot one moment when someone was assigned work instead of the team deciding",
                "Read the Scrum Master section of the Scrum Guide",
            ),
            B.explain(
                "psm-scrum-master-x1",
                "Explain what the Scrum Master is accountable for and who they serve, using an Android team as the example.",
                "The Scrum Master is accountable for establishing Scrum as the Guide defines it and for the Scrum Team's effectiveness. As a true leader who serves, they coach the team in self-management, cause impediments to be removed and make sure the events happen within their timebox. They help the Product Owner with backlog techniques and stakeholder collaboration, and they lead the organisation's Scrum adoption. They do not assign tasks.",
                "Establishing Scrum and effectiveness" to listOf("effectiveness", "establishing scrum"),
                "Serves the team" to listOf("self-management", "impediment", "coach"),
                "Serves the Product Owner" to listOf("product owner", "backlog"),
                "Serves the organisation" to listOf("organisation", "organization", "adoption"),
            ),
            B.glossary(
                "Scrum Master" to "Accountable for establishing Scrum and for the Scrum Team's effectiveness.",
                "Impediment" to "Anything blocking the team's progress that it cannot easily remove itself.",
                "True leader who serves" to "The 2020 Guide's description of the Scrum Master, replacing servant-leader.",
                "Facilitate" to "Help a conversation or event reach its purpose without taking it over.",
                "Self-management" to "The team deciding who does what, when and how.",
            ),
            B.recap(
                "The Scrum Master establishes Scrum and is accountable for the Scrum Team's effectiveness.",
                "A true leader who serves the team, the Product Owner and the organisation.",
                "Does not assign work or run the Daily Scrum; causes impediments to be removed; can also be a Developer.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(scrumTeam, developers, productOwner, scrumMaster)

    val quiz: Quiz = Quiz(
        id = "psm-team-test", title = "Module test",
        questions = B.spread(
            listOf(
                B.q(
                    "psm-scrum-team-q1", "psm-scrum-team",
                    "Which best describes the structure of a Scrum Team?",
                    listOf(
                        "One Scrum Master, one Product Owner and Developers, with no sub-teams or hierarchies",
                        "A Development Team, a QA team and a Product Owner",
                        "A team lead, Developers and a Scrum Master who reports to the lead",
                        "Developers only, with the Product Owner and Scrum Master outside the team",
                    ), setOf(0),
                    "The Guide defines the Scrum Team as one Scrum Master, one Product Owner and Developers, with no sub-teams or hierarchies.",
                    whyWrong = mapOf(
                        1 to "There are no sub-teams; the term Development Team was replaced by Developers in 2020.",
                        2 to "There is no hierarchy inside a Scrum Team and no team lead.",
                        3 to "The Product Owner and Scrum Master are members of the Scrum Team.",
                    ),
                ),
                B.q(
                    "psm-scrum-team-q2", "psm-scrum-team",
                    "What does self-managing mean for a Scrum Team?",
                    listOf(
                        "The team internally decides who does what, when and how",
                        "The team chooses its own Product Goal without the Product Owner",
                        "The team has no Scrum Master",
                    ), setOf(0),
                    "Self-managing teams decide internally who does what, when and how.",
                    whyWrong = mapOf(
                        1 to "The Product Owner develops and communicates the Product Goal.",
                        2 to "Every Scrum Team has a Scrum Master; self-management does not remove it.",
                    ),
                ),
                B.q(
                    "psm-scrum-team-q3", "psm-scrum-team",
                    "A product has grown to need 25 people. What does the Scrum Guide recommend?",
                    listOf(
                        "Several cohesive Scrum Teams sharing one Product Goal, Product Backlog and Product Owner",
                        "One Scrum Team of 25 with two Scrum Masters",
                        "Several Scrum Teams, each with its own Product Backlog",
                        "One Scrum Team for the app and a separate testing team",
                    ), setOf(0),
                    "When a team gets too large, the Guide recommends several cohesive Scrum Teams on the same product, sharing one Product Goal, Product Backlog and Product Owner.",
                    whyWrong = mapOf(
                        1 to "A Scrum Team is typically 10 or fewer; 25 is too many, and it has one Scrum Master accountability.",
                        2 to "One product has one Product Backlog, however many teams work on it.",
                        3 to "There are no separate testing teams; every Scrum Team is cross-functional.",
                    ),
                ),
                B.q(
                    "psm-scrum-team-q4", "psm-scrum-team",
                    "Who is accountable for creating a valuable, useful Increment every Sprint?",
                    listOf("The entire Scrum Team", "The Developers only", "The Product Owner only", "The Scrum Master"), setOf(0),
                    "The Guide makes the entire Scrum Team accountable for a valuable, useful Increment every Sprint.",
                    whyWrong = mapOf(
                        1 to "The Developers create it, but accountability for a valuable Increment is shared by the whole Scrum Team.",
                        2 to "The Product Owner maximises value, but the Increment is the whole team's accountability.",
                        3 to "The Scrum Master is accountable for effectiveness, not alone for the Increment.",
                    ),
                ),
                B.q(
                    "psm-developers-q1", "psm-developers",
                    "Which of these are accountabilities of the Developers? Choose two.",
                    listOf(
                        "Creating a plan for the Sprint, the Sprint Backlog",
                        "Instilling quality by adhering to the Definition of Done",
                        "Ordering the Product Backlog",
                        "Ensuring all Scrum events take place",
                    ), setOf(0, 1),
                    "The Developers create the Sprint Backlog, instil quality through the Definition of Done, adapt their plan daily and hold each other accountable.",
                    whyWrong = mapOf(
                        2 to "Ordering the Product Backlog is the Product Owner's accountability.",
                        3 to "Ensuring the events take place is a Scrum Master service.",
                    ),
                ),
                B.q(
                    "psm-developers-q2", "psm-developers",
                    "During the Sprint, the Developers realise a feature will not meet the Definition of Done by the end of the Sprint. What happens to it?",
                    listOf(
                        "It is not part of the Increment and returns to the Product Backlog",
                        "It is released with a note to fix it next Sprint",
                        "The Sprint is extended until it meets the Definition of Done",
                        "The Product Owner decides whether it counts as done",
                    ), setOf(0),
                    "Work that does not meet the Definition of Done cannot be part of the Increment; it goes back to the Product Backlog for future consideration.",
                    whyWrong = mapOf(
                        1 to "Releasing work below the Definition of Done lowers quality and misleads stakeholders.",
                        2 to "Sprints are fixed length and never extended.",
                        3 to "Nobody can waive the Definition of Done for an item.",
                    ),
                ),
                B.q(
                    "psm-developers-q3", "psm-developers",
                    "Who is responsible for sizing Product Backlog items?",
                    listOf("The Developers who will do the work", "The Product Owner", "The Scrum Master", "The stakeholders"), setOf(0),
                    "The Developers who will do the work size it; the Product Owner may help them understand and select trade-offs.",
                    whyWrong = mapOf(
                        1 to "The Product Owner may influence the Developers but does not size the items.",
                        2 to "The Scrum Master does not size work.",
                        3 to "Stakeholders do not size the Developers' work.",
                    ),
                ),
                B.q(
                    "psm-developers-q4", "psm-developers",
                    "The Product Owner is also writing code for items in the Sprint Backlog. How does she take part in the Daily Scrum?",
                    listOf(
                        "As a Developer",
                        "As an observer who may not speak",
                        "She leads it, because she owns the Product Backlog",
                    ), setOf(0),
                    "If the Product Owner or Scrum Master is actively working on Sprint Backlog items, they take part in the Daily Scrum as Developers.",
                    whyWrong = mapOf(
                        1 to "When working on Sprint Backlog items she is a Developer and participates fully.",
                        2 to "The Daily Scrum belongs to the Developers; no one leads it because they own the Product Backlog.",
                    ),
                ),
                B.q(
                    "psm-product-owner-q1", "psm-product-owner",
                    "What is the Product Owner accountable for?",
                    listOf(
                        "Maximising the value of the product resulting from the work of the Scrum Team",
                        "Assigning tasks to Developers",
                        "Making sure the team follows Scrum",
                        "Writing the Sprint Backlog",
                    ), setOf(0),
                    "The Product Owner is accountable for maximising the value of the product, largely through effective Product Backlog management.",
                    whyWrong = mapOf(
                        1 to "Nobody assigns tasks to Developers; they are self-managing.",
                        2 to "Establishing Scrum is the Scrum Master's accountability.",
                        3 to "The Sprint Backlog is the Developers' plan.",
                    ),
                ),
                B.q(
                    "psm-product-owner-q2", "psm-product-owner",
                    "A company wants a committee of three managers to act as Product Owner. What does the Scrum Guide say?",
                    listOf(
                        "The Product Owner is one person, not a committee",
                        "A committee is fine if it votes on the backlog order",
                        "A committee is required for products with several teams",
                    ), setOf(0),
                    "The Product Owner is one person. They may represent many stakeholders, but one person decides.",
                    whyWrong = mapOf(
                        1 to "Voting turns the Product Owner into a committee, which the Guide rules out.",
                        2 to "Several teams on one product share one Product Owner, not a committee.",
                    ),
                ),
                B.q(
                    "psm-product-owner-q3", "psm-product-owner",
                    "Under which condition can a Sprint be cancelled, and by whom?",
                    listOf(
                        "By the Product Owner, if the Sprint Goal becomes obsolete",
                        "By the Scrum Master, if the team is behind",
                        "By the Developers, if they cannot finish all the items",
                        "By any stakeholder, if priorities change",
                    ), setOf(0),
                    "Only the Product Owner has the authority to cancel a Sprint, and a Sprint could be cancelled if the Sprint Goal becomes obsolete.",
                    whyWrong = mapOf(
                        1 to "The Scrum Master has no authority to cancel a Sprint, and being behind is not a reason.",
                        2 to "Unfinished items are renegotiated with the Product Owner; they do not cancel the Sprint.",
                        3 to "Stakeholders influence the Product Owner; they cannot cancel a Sprint.",
                    ),
                ),
                B.q(
                    "psm-product-owner-q4", "psm-product-owner",
                    "The Product Owner delegates writing Product Backlog items to a Developer. Which statement is true?",
                    listOf(
                        "The Product Owner remains accountable for the Product Backlog",
                        "The Developer becomes accountable for those items",
                        "Delegation is not allowed by the Scrum Guide",
                    ), setOf(0),
                    "The Product Owner may do the work or delegate it to others; regardless, the Product Owner remains accountable.",
                    whyWrong = mapOf(
                        1 to "Delegating the work does not move the accountability.",
                        2 to "The Guide explicitly allows the Product Owner to delegate the work.",
                    ),
                ),
                B.q(
                    "psm-scrum-master-q1", "psm-scrum-master",
                    "Which describes the Scrum Master in the 2020 Scrum Guide?",
                    listOf(
                        "A true leader who serves the Scrum Team and the larger organisation",
                        "The project manager of the Scrum Team",
                        "The team lead who assigns and tracks work",
                        "A note-taker for the Scrum events",
                    ), setOf(0),
                    "The 2020 Guide describes Scrum Masters as true leaders who serve the Scrum Team and the larger organisation.",
                    whyWrong = mapOf(
                        1 to "Scrum has no project manager accountability; the Scrum Master is not one under another name.",
                        2 to "The team is self-managing; nobody assigns its work.",
                        3 to "The Scrum Master makes sure events are positive and productive; note-taking is not the accountability.",
                    ),
                ),
                B.q(
                    "psm-scrum-master-q2", "psm-scrum-master",
                    "Which are ways the Scrum Master serves the organisation? Choose two.",
                    listOf(
                        "Leading, training and coaching the organisation in its Scrum adoption",
                        "Removing barriers between stakeholders and Scrum Teams",
                        "Ordering the Product Backlog for the stakeholders",
                        "Approving the Increment before release",
                    ), setOf(0, 1),
                    "Serving the organisation includes leading Scrum adoption, advising implementations, helping people understand empiricism and removing barriers between stakeholders and Scrum Teams.",
                    whyWrong = mapOf(
                        2 to "Ordering the Product Backlog is the Product Owner's accountability.",
                        3 to "No one approves the Increment as a gate; work meeting the Definition of Done is part of it.",
                    ),
                ),
                B.q(
                    "psm-scrum-master-q3", "psm-scrum-master",
                    "A Developer asks the Scrum Master which task to pick up next. What is the best response?",
                    listOf(
                        "Encourage the Developers to decide together, as a self-managing team",
                        "Assign the highest-priority task to that Developer",
                        "Ask the Product Owner to assign it",
                    ), setOf(0),
                    "The Scrum Master coaches self-management; the Developers decide who does what.",
                    whyWrong = mapOf(
                        1 to "Assigning work undermines self-management, which the Scrum Master is meant to coach.",
                        2 to "The Product Owner orders the Product Backlog but does not assign tasks inside the Sprint.",
                    ),
                ),
            ),
        ),
    )
}
