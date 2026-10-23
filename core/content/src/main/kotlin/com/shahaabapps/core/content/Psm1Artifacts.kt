package com.shahaabapps.core.content

/**
 * PSM I topics for module psm-artifacts (Psm1): the three artifacts and the
 * commitment that focuses each one, paraphrased from The Scrum Guide
 * (November 2020, CC BY-SA 4.0).
 */
internal object Psm1Artifacts {
    private const val GUIDE = "https://scrumguides.org/scrum-guide.html"
    private const val PSM1 = "https://www.scrum.org/assessments/professional-scrum-master-i-certification"
    private const val OPEN = "https://www.scrum.org/open-assessments/scrum-open"
    private const val RES = "https://www.scrum.org/resources/"

    private val productBacklog = Topic(
        id = "psm-product-backlog", title = "The Product Backlog and the Product Goal", minutes = 25,
        references = listOf(
            B.ref("The Scrum Guide (2020): Product Backlog and Product Goal", GUIDE),
            B.ref("Scrum.org: What is a Product Backlog?", RES + "what-is-a-product-backlog"),
            B.ref("Scrum.org: What is a Product Goal?", RES + "what-is-a-product-goal"),
            B.ref("Professional Scrum Master I (PSM I)", PSM1),
            B.ref("Scrum Open, the free practice assessment", OPEN),
        ),
        blocks = listOf(
            B.warmup(
                "psm-product-backlog-w1", "psm-product-backlog",
                "The sales director tells two Developers to put her feature at the top of the Product Backlog. Who decides where it goes?",
                listOf("The Product Owner", "The two Developers", "The sales director"), 0,
                "One person is accountable for how the list is ordered.",
            ),
            B.plain("""
                The Product Backlog is the one to-do list for a product. Picture every idea anyone has for your
                Android app: a new screen, a crash to fix, a backend upgrade, a Play Store requirement. They all
                go on one list, in one order, with the most valuable things at the top. The Scrum Guide calls it
                emergent, which means it is never finished: it grows, shrinks and changes as the team learns. It
                is the single source of work for the Scrum Team. If a piece of work is not on it, nobody should
                be doing it.
            """),
            B.diagram(
                caption = "Stakeholders bring ideas to the Product Owner; refinement adds detail and size; ready items flow into Sprint Planning, all aimed at the Product Goal.",
                nodes = listOf(
                    DNode("goal", "Product Goal", 0.5f, 0.12f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    DNode("pb", "Product Backlog", 0.5f, 0.45f, w = 0.34f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "ordered by the PO"),
                    DNode("stake", "Stakeholders", 0.13f, 0.45f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
                    DNode("refine", "Refinement", 0.85f, 0.45f, w = 0.26f, h = 0.18f, tone = 2, sub = "Developers size"),
                    DNode("plan", "Sprint Planning", 0.5f, 0.82f, w = 0.34f, h = 0.18f, sub = "ready items"),
                ),
                edges = listOf(
                    DEdge("goal", "pb", label = "what fulfils it", dashed = true),
                    DEdge("stake", "pb", label = "via the PO"),
                    DEdge("pb", "refine", bidirectional = true),
                    DEdge("pb", "plan"),
                ),
                aspect = 0.7f,
            ),
            B.h("One product, one list, one owner"),
            B.p("""
                A product, in the Guide's words, is a vehicle to deliver value: something with a clear boundary,
                known stakeholders and well-defined users. Your Android app with its small backend is one product.
                It has one Product Backlog and one Product Owner, however many people work on it. If the app grows
                and three Scrum Teams work on it, they still share one Product Goal, one Product Backlog and one
                Product Owner. Three lists would mean three different answers to the question "what matters most
                now?", and that is exactly what the single list exists to prevent.
            """),
            B.table(
                listOf("Question", "What the Scrum Guide says"),
                listOf("Who is accountable for the Product Backlog?", "The Product Owner, for managing it well: the Product Goal, the items, their order, and keeping it transparent"),
                listOf("Can someone else do the work of managing it?", "Yes, the PO may delegate the work, but stays accountable"),
                listOf("Can a stakeholder change it?", "Only by convincing the Product Owner"),
                listOf("Who sizes the items?", "The Developers who will do the work"),
                listOf("How many per product?", "One, even with several Scrum Teams"),
                listOf("Is it ever complete?", "No; it is emergent and changes as the team learns"),
            ),
            B.check(
                "psm-product-backlog-k1", "psm-product-backlog",
                "Three Scrum Teams build the same Android app. How many Product Backlogs should there be?",
                listOf("One, shared by all three teams", "One per team, so each team owns its work", "One per team, plus a master list above them"), 0,
                "One product has one Product Backlog and one Product Owner, whatever the number of teams.",
                whyWrong = mapOf(
                    1 to "Separate lists hide the true order for the product; the Guide gives one product one Product Backlog.",
                    2 to "A master list above team lists is still several lists. The Guide has one, with no layers.",
                ),
            ),
            B.h("The Product Goal: the commitment of the Product Backlog"),
            B.p("""
                Each of Scrum's three artifacts comes with a commitment: one thing that tells everyone what the
                artifact is for and what progress is measured against. For the Product Backlog it is the Product
                Goal. The Product Goal describes a future state of the product, a target the team can plan
                against. It lives inside the Product Backlog, and the rest of the list emerges to say what will
                fulfil it. It is the Scrum Team's long-term objective, and the Guide is strict about one thing:
                the team must fulfil or abandon one Product Goal before taking on the next. One target at a time.
            """),
            B.example("""
                A Product Goal for a study app: "A learner can finish a whole exam course offline, on the train,
                and nothing is lost when the phone reconnects." Offline storage, a sync fix and a smaller download
                are items on the list because they move the app toward that state.
            """),
            B.check(
                "psm-product-backlog-k2", "psm-product-backlog",
                "Halfway to its Product Goal, the team sees a new opportunity that deserves a goal of its own. What does the Scrum Guide say?",
                listOf("Fulfil or abandon the current Product Goal before taking on the next", "Run both Product Goals at once and split the Developers", "Let each Developer choose which goal to work toward"), 0,
                "The Scrum Team must fulfil or abandon one Product Goal before taking on the next.",
                whyWrong = mapOf(
                    1 to "Two goals at once removes the focus the commitment is there to give.",
                    2 to "The Product Goal is the whole Scrum Team's objective, not a personal choice.",
                ),
            ),
            B.h("Refinement: getting items ready"),
            B.p("""
                Product Backlog refinement is breaking big items into smaller, more precise ones and adding
                detail: a description, an order and a size. "Offline mode" is too big to build in one Sprint;
                "download a course's lessons to the phone" and "show a banner when offline" might not be. It is an
                ongoing activity, not an event: there is no refinement meeting in the Scrum Guide and no timebox
                for it. The Developers who will do the work are responsible for the sizing. The Product Owner may
                influence them by helping them understand the item and choose trade-offs, but the size is theirs.
                Items that can be Done by the team within one Sprint are deemed ready for selection at Sprint
                Planning.
            """),
            B.compare("Who does what with the Product Backlog",
                B.card("Product Owner", "Ordering, the Product Goal, and saying what an item is for.", "Develops and communicates the Product Goal", "Creates and clearly communicates items", "Orders them; keeps the list transparent and understood"),
                B.card("Developers", "Sizing and breaking work down.", "Size the items they will build", "Help split and detail them during refinement", "Select items at Sprint Planning"),
                B.card("Stakeholders", "Ideas and feedback.", "Suggest items and changes", "Convince the Product Owner, not the Developers", "See the list, since it is transparent"),
            ),
            B.check(
                "psm-product-backlog-k3", "psm-product-backlog",
                "The Product Owner thinks an item is two days of work. The Developers say it is closer to two weeks. Whose size stands?",
                listOf("The Developers', because they will do the work", "The Product Owner's, because she owns the backlog", "An average the Scrum Master works out"), 0,
                "The Developers who will be doing the work are responsible for the sizing. The PO may influence them by explaining trade-offs.",
                whyWrong = mapOf(
                    1 to "The PO is accountable for the order and content, not for how big the work is.",
                    2 to "The Scrum Master does not set sizes; averaging two views also hides what the Developers know.",
                ),
            ),
            B.worked("""
                Leila from marketing wants a Black Friday banner in the app, and she asks Tom, a Developer, to slip
                it in. Tom points her to Amira, the Product Owner, because only Amira orders the Product Backlog.
                Amira weighs the banner against the Product Goal and the items already there, and adds it near the
                top because the date is fixed. In refinement the Developers split it into "banner component" and
                "remote switch to turn it off", and size both. Each can be Done within one Sprint, so both are
                ready for the next Sprint Planning.
            """),
            B.faded(
                "psm-product-backlog-f1",
                "The backend lead wants a database upgrade done before any new feature. Apply the same route.",
                listOf(
                    "He brings the request to the ___, who alone orders the Product Backlog.",
                    "She weighs the upgrade against the Product Goal and the other items, and places it.",
                    "The Developers split and size it in refinement until each piece can be Done within one Sprint.",
                ),
                blank = 0,
                answers = listOf("Product Owner", "PO"),
                note = "Anyone may ask; only the Product Owner orders. The organisation must respect her decisions.",
            ),
            B.mistakes(
                "Keeping one backlog per team when several teams build the same product.",
                "Letting a senior stakeholder reorder the list directly instead of convincing the Product Owner.",
                "Having the Product Owner or a manager size items that the Developers will build.",
                "Treating refinement as a Scrum event with its own timebox; it is an ongoing activity.",
                "Starting a second Product Goal before the first is fulfilled or abandoned.",
            ),
            B.h("Check yourself"),
            B.match(
                "psm-x-product-backlog-match", "Pair each part with who is responsible for it.",
                "Ordering the Product Backlog" to "Product Owner",
                "Sizing the items" to "Developers",
                "Asking for a new feature" to "Anyone, through the PO",
                "Committing to a long-term target" to "Product Goal",
            ),
            B.gap(
                "psm-x-product-backlog-gap1", "The word the Guide uses for a backlog that keeps changing.",
                "The Product Backlog is an ___, ordered list of what is needed to improve the product.", "emergent", "Emergent",
            ),
            B.tryIt("Try it in 15 minutes", "Use your own app's issue list, or any list of app ideas you keep.",
                "Write one Product Goal for your app as a future state in one sentence",
                "Put ten items in a single order, most valuable first, with no ties",
                "Split the biggest item until each piece could be Done in a two-week Sprint",
                "Mark which items you would bring to the next Sprint Planning as ready",
            ),
            B.explain(
                "psm-product-backlog-x1",
                "Explain what the Product Backlog is, who looks after it, and how the Product Goal fits.",
                "The Product Backlog is one emergent, ordered list of everything needed to improve the product, and the single source of work for the Scrum Team. The Product Owner is accountable for it and orders it; others change it by convincing the Product Owner. The Developers who do the work size the items in refinement. The Product Goal is its commitment: a long-term target the team fulfils or abandons before taking the next.",
                "One ordered list, the single source of work" to listOf("ordered", "single source", "one list"),
                "The Product Owner is accountable and orders it" to listOf("product owner", "orders it"),
                "Developers size items in refinement" to listOf("refinement", "sizing", "size the items"),
                "The Product Goal is its commitment" to listOf("product goal", "long-term"),
            ),
            B.glossary(
                "Product Backlog" to "The single, ordered, ever-changing list of what is needed to improve the product.",
                "Product Goal" to "The long-term objective for the Scrum Team; the commitment of the Product Backlog.",
                "Emergent" to "Never finished: it changes as the team and stakeholders learn.",
                "Refinement" to "The ongoing work of splitting items and adding description, order and size.",
                "Ready for selection" to "An item the team could get Done within one Sprint.",
                "Commitment" to "The goal or standard attached to an artifact that progress is measured against.",
            ),
            B.recap(
                "One product, one Product Backlog, one Product Owner, even with several Scrum Teams.",
                "The Product Goal is the Product Backlog's commitment: fulfil or abandon one before the next.",
                "Refinement is ongoing, not an event; the Developers who do the work size the items.",
            ),
        ),
    )

    private val sprintBacklog = Topic(
        id = "psm-sprint-backlog", title = "The Sprint Backlog and the Sprint Goal", minutes = 25,
        references = listOf(
            B.ref("The Scrum Guide (2020): Sprint Backlog and Sprint Goal", GUIDE),
            B.ref("Scrum.org: What is a Sprint Backlog?", RES + "what-is-a-sprint-backlog"),
            B.ref("Scrum.org: What is a Sprint Goal?", RES + "what-is-a-sprint-goal"),
            B.ref("Professional Scrum Master I (PSM I)", PSM1),
            B.ref("Scrum Open, the free practice assessment", OPEN),
        ),
        blocks = listOf(
            B.warmup(
                "psm-sprint-backlog-w1", "psm-sprint-backlog",
                "Halfway through a Sprint the Developers find a task they forgot. Who adds it to the Sprint Backlog?",
                listOf("The Developers", "The Product Owner", "The Scrum Master"), 0,
                "Think about whose plan the Sprint Backlog is.",
            ),
            B.plain("""
                The Sprint Backlog is the Developers' plan for one Sprint. It has three parts: the Sprint Goal,
                which says why this Sprint is worth doing; the Product Backlog items picked for the Sprint, which
                say what; and a plan for turning them into a Done Increment, which says how. The Guide calls it a
                plan by and for the Developers: a highly visible, real-time picture of the work they plan to do.
                Think of the task board on the team's wall or in Jira, updated through the day, not a document
                signed off at the start.
            """),
            B.diagram(
                caption = "The Sprint Backlog joins why, what and how; the Daily Scrum keeps adapting the plan while the Sprint Goal stays fixed.",
                nodes = listOf(
                    DNode("why", "Sprint Goal", 0.18f, 0.25f, w = 0.3f, h = 0.2f, tone = 1, sub = "why: fixed"),
                    DNode("what", "Selected items", 0.5f, 0.25f, w = 0.3f, h = 0.2f, sub = "what"),
                    DNode("how", "The plan", 0.82f, 0.25f, w = 0.3f, h = 0.2f, sub = "how: changes"),
                    DNode("sb", "Sprint Backlog: by and for the Developers", 0.5f, 0.58f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    DNode("daily", "Daily Scrum", 0.82f, 0.86f, w = 0.3f, h = 0.18f, sub = "adapts the plan"),
                ),
                edges = listOf(
                    DEdge("why", "what"),
                    DEdge("what", "how"),
                    DEdge("daily", "sb", label = "re-plan", dashed = true),
                ),
                aspect = 0.7f,
            ),
            B.h("Who owns it, and who changes it"),
            B.p("""
                The Developers own the Sprint Backlog. They create the plan at Sprint Planning and they update it
                throughout the Sprint as they learn more. If Priya finds that the offline download needs a
                database migration nobody planned, she adds that task herself; nobody has to approve it. The
                Product Owner does not add work to the Sprint Backlog, and the Scrum Master does not assign tasks
                from it. The Sprint Backlog should have enough detail that the Developers can inspect their
                progress at the Daily Scrum.
            """),
            B.table(
                listOf("Part", "Answers", "Can it change in the Sprint?"),
                listOf("Sprint Goal", "Why is this Sprint valuable?", "No: it is the fixed commitment for the Sprint"),
                listOf("Selected Product Backlog items", "What will we deliver?", "Scope can be clarified and renegotiated with the PO, as long as the Sprint Goal is not endangered"),
                listOf("Plan", "How will we deliver it?", "Yes, every day: the Developers adapt it as they learn"),
            ),
            B.check(
                "psm-sprint-backlog-k1", "psm-sprint-backlog",
                "Who can change the plan in the Sprint Backlog during the Sprint?",
                listOf("The Developers", "The Product Owner, with the Developers' agreement", "The Scrum Master, to keep the team on schedule"), 0,
                "The Sprint Backlog is a plan by and for the Developers, and only they change it.",
                whyWrong = mapOf(
                    1 to "The PO can renegotiate scope with the Developers, but the plan itself belongs to the Developers.",
                    2 to "The Scrum Master serves the team; it does not manage its plan or its schedule.",
                ),
            ),
            B.h("The Sprint Goal: the commitment of the Sprint Backlog"),
            B.p("""
                The Sprint Goal is the single objective for the Sprint. The whole Scrum Team crafts it at Sprint
                Planning, and it is added to the Sprint Backlog. It is a commitment by the Developers, but it gives
                them room: the goal is fixed, while the exact work to reach it can change. If the work turns out to
                be different from what they expected, the Developers talk to the Product Owner and renegotiate the
                scope of the Sprint Backlog within the Sprint, without affecting the Sprint Goal. The goal also
                makes the team pull together rather than work on separate initiatives.
            """),
            B.example("""
                Sprint Goal: "A learner can download one course and read it in flight mode." The team picked six
                items. On day six, Dana finds the download screen's progress animation will take a week. She and
                the Product Owner agree to drop the animation and show a plain progress bar: the scope changed, the
                Sprint Goal did not.
            """),
            B.check(
                "psm-sprint-backlog-k2", "psm-sprint-backlog",
                "Midway through the Sprint, the Developers realise they cannot finish every selected item. What should happen?",
                listOf("They renegotiate the scope with the Product Owner, keeping the Sprint Goal", "The Sprint Goal is rewritten to match what they can finish", "The Sprint is extended by a few days"), 0,
                "The Sprint Goal stays fixed; the flexibility is in the work. Scope is clarified and renegotiated with the PO.",
                whyWrong = mapOf(
                    1 to "The Sprint Goal does not change during the Sprint; that is what makes it a commitment.",
                    2 to "A Sprint has a fixed length. It is never stretched to finish work.",
                ),
            ),
            B.h("What happens to the Sprint Backlog when things change"),
            B.bullets(
                "New work discovered: the Developers add it to the plan.",
                "Work no longer needed: the Developers remove it.",
                "An item turns out bigger than thought: the Developers and the Product Owner renegotiate scope, without endangering the Sprint Goal.",
                "The Sprint Goal itself becomes obsolete, for example the feature is no longer wanted: only the Product Owner can cancel the Sprint.",
                "Unfinished items at the end of the Sprint: they are not part of the Increment and go back to the Product Backlog.",
            ),
            B.check(
                "psm-sprint-backlog-k3", "psm-sprint-backlog",
                "What does the Sprint Backlog consist of?",
                listOf("The Sprint Goal, the selected Product Backlog items and the plan to deliver them", "The selected Product Backlog items only", "The tasks each Developer has been assigned for the Sprint"), 0,
                "Why (the Sprint Goal), what (the selected items) and how (the plan) together make the Sprint Backlog.",
                whyWrong = mapOf(
                    1 to "The items are only the what. The Sprint Goal and the plan are part of it too.",
                    2 to "Nobody assigns tasks in Scrum; the Developers decide who does what.",
                ),
            ),
            B.worked("""
                Sprint Goal: "Crash reports reach the team within a minute." The Developers picked three items and
                broke them into tasks. On day three Marcus finds the crash library needs a newer Gradle plugin.
                Step one: he adds an "upgrade the Gradle plugin" task to the Sprint Backlog himself. Step two: at
                the next Daily Scrum the Developers re-plan the rest of the week around it. Step three: one item,
                a crash dashboard, no longer fits, so they talk to the Product Owner and agree to move it back to
                the Product Backlog. The Sprint Goal is untouched.
            """),
            B.faded(
                "psm-sprint-backlog-f1",
                "Sprint Goal: \"Learners can reset their password from the app.\" On day four the email provider's rate limit forces extra work.",
                listOf(
                    "The Developers add the extra retry task to the Sprint Backlog themselves.",
                    "At the Daily Scrum they adapt the plan for the rest of the Sprint.",
                    "A lower-value item no longer fits, so they renegotiate scope with the ___, keeping the Sprint Goal.",
                ),
                blank = 2,
                answers = listOf("Product Owner", "PO"),
                note = "Scope is renegotiated with the Product Owner; the Sprint Goal stays fixed.",
            ),
            B.mistakes(
                "Treating the Sprint Backlog as fixed at Sprint Planning; it changes every day as the Developers learn.",
                "Letting the Product Owner or a manager push new tasks into the Sprint Backlog.",
                "Changing the Sprint Goal when the work goes badly instead of renegotiating scope.",
                "Having the Scrum Master assign tasks from the Sprint Backlog to named people.",
                "Writing several unrelated goals for one Sprint; the Sprint Goal is a single objective.",
            ),
            B.h("Check yourself"),
            B.match(
                "psm-x-sprint-backlog-match", "Pair each part of the Sprint Backlog with the question it answers.",
                "Sprint Goal" to "Why",
                "Selected Product Backlog items" to "What",
                "The plan" to "How",
                "Daily Scrum" to "Where the plan is adapted",
            ),
            B.gap(
                "psm-x-sprint-backlog-gap1", "Whose plan it is.",
                "The Sprint Backlog is a plan by and for the ___.", "Developers", "developers",
            ),
            B.tryIt("Try it in 15 minutes", "Use the work you plan to do on your own app in the next two weeks.",
                "Write a single Sprint Goal as an outcome a user would notice",
                "Pick the backlog items that serve it and break the first one into tasks of a day or less",
                "Put the tasks on a visible board, in a notes app or on paper",
                "Tomorrow, update the board before you start work, as a Daily Scrum would",
            ),
            B.explain(
                "psm-sprint-backlog-x1",
                "Explain what the Sprint Backlog is, who changes it, and why the Sprint Goal stays fixed.",
                "The Sprint Backlog is the Developers' plan for the Sprint: the Sprint Goal for why, the selected Product Backlog items for what, and a plan for how. It belongs to the Developers, who update it every day as they learn. The Sprint Goal is its commitment and does not change during the Sprint; if the work turns out different, the Developers renegotiate scope with the Product Owner.",
                "Why, what and how" to listOf("sprint goal", "selected"),
                "Owned and updated by the Developers" to listOf("developers", "developer"),
                "The Sprint Goal is fixed" to listOf("does not change", "fixed", "commitment"),
                "Scope renegotiated with the PO" to listOf("renegotiate", "scope"),
            ),
            B.glossary(
                "Sprint Backlog" to "The Developers' plan for the Sprint: the Sprint Goal, the selected items and how they will be delivered.",
                "Sprint Goal" to "The single objective for the Sprint; the commitment of the Sprint Backlog.",
                "Renegotiate scope" to "Agree with the Product Owner to change the selected work without endangering the Sprint Goal.",
                "Real-time picture" to "The Sprint Backlog is updated as work happens, so it always shows the current plan.",
            ),
            B.recap(
                "The Sprint Backlog is why (Sprint Goal), what (selected items) and how (the plan).",
                "It is by and for the Developers; only they change the plan, and they do so every day.",
                "The Sprint Goal is fixed; scope is renegotiated with the Product Owner around it.",
            ),
        ),
    )

    private val increment = Topic(
        id = "psm-increment", title = "The Increment and the Definition of Done", minutes = 25,
        references = listOf(
            B.ref("The Scrum Guide (2020): Increment and Definition of Done", GUIDE),
            B.ref("Scrum.org: What is an Increment?", RES + "what-is-an-increment"),
            B.ref("Scrum.org: What is a Definition of Done?", RES + "what-is-a-definition-of-done"),
            B.ref("Professional Scrum Master I (PSM I)", PSM1),
            B.ref("Scrum Open, the free practice assessment", OPEN),
        ),
        blocks = listOf(
            B.warmup(
                "psm-increment-w1", "psm-increment",
                "A feature is built but its tests are not written, and the Definition of Done needs them. Can it be shown at the Sprint Review?",
                listOf("No, it is not part of the Increment", "Yes, as long as it is labelled unfinished", "Yes, if the Product Owner accepts it"), 0,
                "Ask what has to be true before work becomes part of an Increment.",
            ),
            B.plain("""
                An Increment is a piece of working product that adds to everything built before. In an Android
                team, think of a build that has passed every check the team agreed on, works with the previous
                features, and could be put in front of users. The Scrum Guide calls each Increment a concrete
                stepping stone toward the Product Goal. Each one is additive to all prior Increments and
                thoroughly verified, so that they all work together, and to provide value it must be usable.
            """),
            B.diagram(
                caption = "Work becomes an Increment only when it meets the Definition of Done; work that does not goes back to the Product Backlog.",
                nodes = listOf(
                    DNode("item", "Backlog item", 0.17f, 0.25f, w = 0.3f, h = 0.2f, sub = "being built"),
                    DNode("dod", "Meets the DoD?", 0.5f, 0.25f, w = 0.28f, h = 0.2f, kind = NodeKind.PILL, tone = 2),
                    DNode("inc", "Increment", 0.83f, 0.25f, w = 0.3f, h = 0.2f, tone = 1, sub = "usable, verified"),
                    DNode("back", "Product Backlog", 0.17f, 0.72f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "not Done yet"),
                    DNode("review", "Sprint Review", 0.5f, 0.72f, w = 0.28f, h = 0.2f, sub = "not a gate"),
                    DNode("users", "Users", 0.84f, 0.72f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
                ),
                edges = listOf(
                    DEdge("item", "dod"),
                    DEdge("dod", "inc", label = "yes"),
                    DEdge("dod", "back", label = "no"),
                    DEdge("inc", "review", label = "shown", dashed = true),
                    DEdge("inc", "users", label = "any time"),
                ),
                aspect = 0.7f,
            ),
            B.h("Several Increments, and no gate"),
            B.p("""
                Many teams think of one Increment per Sprint, delivered at the Sprint Review. The Guide says more
                than that. Several Increments may be created within a Sprint. The moment a Product Backlog item
                meets the Definition of Done, an Increment is born. The sum of the Increments is presented at the
                Sprint Review, which supports empiricism, but an Increment may be delivered to stakeholders before
                the Sprint ends. The Guide says plainly that the Sprint Review should never be considered a gate to
                releasing value. So a team that ships a crash fix to the Play Store on day four is doing Scrum, not
                breaking it.
            """),
            B.table(
                listOf("Statement", "True or false in the 2020 Guide"),
                listOf("There is exactly one Increment per Sprint", "False: several may be created"),
                listOf("An Increment may be released before the Sprint Review", "True"),
                listOf("The Sprint Review is where the PO approves the release", "False: it is not a gate to releasing value"),
                listOf("Each Increment must be usable", "True"),
                listOf("Work that is nearly Done is part of the Increment", "False: it goes back to the Product Backlog"),
            ),
            B.check(
                "psm-increment-k1", "psm-increment",
                "The Developers finish a crash fix on day four of a two-week Sprint and it meets the Definition of Done. Can it be released now?",
                listOf("Yes; an Increment may be delivered before the Sprint ends", "No; releases happen only after the Sprint Review", "Only if the Sprint is cancelled first"), 0,
                "An Increment may be delivered to stakeholders before the end of the Sprint; the Sprint Review is not a gate to releasing value.",
                whyWrong = mapOf(
                    1 to "The Guide says the Sprint Review should never be considered a gate to releasing value.",
                    2 to "Cancelling has nothing to do with releasing; only an obsolete Sprint Goal is a reason to cancel.",
                ),
            ),
            B.h("The Definition of Done: the commitment of the Increment"),
            B.p("""
                The Definition of Done, often shortened to DoD, is a formal description of the state the Increment
                is in when it meets the quality the product needs. It is a shared checklist that says what "done"
                means, so that nobody's "done" is secretly "done except testing". For an Android app it might
                read: code reviewed, unit tests pass, UI tests pass on the minimum supported Android version, no
                new lint errors, the backend change deployed behind a feature flag, release notes written. Work
                that does not meet the Definition of Done cannot be released or even presented at the Sprint
                Review. It returns to the Product Backlog for future consideration.
            """),
            B.table(
                listOf("Situation", "Who creates the Definition of Done"),
                listOf("The organisation has a Definition of Done as a standard", "All Scrum Teams follow it as a minimum, and may add to it"),
                listOf("The organisation has no standard", "The Scrum Team creates one suitable for the product"),
                listOf("Several Scrum Teams work on one product", "They must define and follow the same Definition of Done together"),
            ),
            B.p("""
                The Developers are required to conform to the Definition of Done; it is how they instil quality.
                The Definition of Done can also be improved: the Sprint Retrospective inspects it along with
                individuals, interactions, processes and tools.
            """),
            B.check(
                "psm-increment-k2", "psm-increment",
                "Two Scrum Teams work on the same Android app, and the organisation has no standard Definition of Done. What does the Guide require?",
                listOf("Both teams define and follow one shared Definition of Done", "Each team writes its own to suit the way it works", "The Product Owner writes it for both teams"), 0,
                "Several Scrum Teams working on one product must mutually define and comply with the same Definition of Done.",
                whyWrong = mapOf(
                    1 to "Two definitions on one product would mean two different meanings of usable in the same app.",
                    2 to "The Scrum Team creates it, not the Product Owner alone.",
                ),
            ),
            B.check(
                "psm-increment-k3", "psm-increment",
                "On the last day of the Sprint, one item is coded but fails the Definition of Done's UI tests. What happens to it?",
                listOf("It is not part of the Increment and returns to the Product Backlog", "It is shown at the Sprint Review as nearly done", "The Sprint is extended until the tests pass"), 0,
                "Work cannot be part of an Increment unless it meets the Definition of Done; it goes back to the Product Backlog.",
                whyWrong = mapOf(
                    1 to "Work that does not meet the Definition of Done cannot even be presented at the Sprint Review.",
                    2 to "A Sprint is never stretched; its length is fixed.",
                ),
            ),
            B.worked("""
                The organisation's Definition of Done says: code reviewed, automated tests pass, security scan
                clean. The Android team's Scrum Team adds two lines of its own: UI tests pass on the oldest
                supported phone, and the change can be switched off remotely. On Wednesday the "download course"
                item meets all five, so it is part of an Increment and goes to beta testers that day. On Friday,
                the last day, "offline banner" still fails the UI test. It is not shown at the Sprint Review and
                goes back to the Product Backlog for the Product Owner to order again.
            """),
            B.faded(
                "psm-increment-f1",
                "A second team joins the app and asks which Definition of Done it should follow.",
                listOf(
                    "The organisation's standard, if there is one, applies to every Scrum Team as a minimum.",
                    "Because both teams work on the same product, they define and follow the ___ Definition of Done together.",
                    "Work from either team that fails it is not part of the Increment and returns to the Product Backlog.",
                ),
                blank = 1,
                answers = listOf("same", "shared", "one", "common"),
                note = "One product, one Definition of Done: otherwise usable would mean two different things in one app.",
            ),
            B.mistakes(
                "Thinking there can be only one Increment per Sprint, released after the Sprint Review.",
                "Showing nearly-done work at the Sprint Review; if it fails the Definition of Done it is not shown.",
                "Letting each team on one product keep its own Definition of Done.",
                "Treating the Product Owner's approval at the Sprint Review as the gate to release.",
                "Lowering the Definition of Done near the end of a Sprint to make more items count.",
            ),
            B.h("Check yourself"),
            B.match(
                "psm-x-increment-match", "Pair each artifact with its commitment.",
                "Product Backlog" to "Product Goal",
                "Sprint Backlog" to "Sprint Goal",
                "Increment" to "Definition of Done",
                "Work that fails the DoD" to "Back to the Product Backlog",
            ),
            B.gap(
                "psm-x-increment-gap1", "What every Increment has to be, to provide value.",
                "To provide value, the Increment must be ___.", "usable", "Usable",
            ),
            B.tryIt("Try it in 15 minutes", "Use the last feature you shipped in your own app.",
                "Write a Definition of Done for your app with five to eight checkable lines",
                "Check the last feature you shipped against it and note what it would have failed",
                "Decide which line you would add first if you had an organisation standard to meet",
                "Pick one small item and release it the moment it meets your Definition of Done",
            ),
            B.explain(
                "psm-increment-x1",
                "Explain what an Increment is, how the Definition of Done decides what counts, and when it can be released.",
                "An Increment is a usable, verified stepping stone toward the Product Goal, additive to all earlier Increments. The Definition of Done is its commitment: work that does not meet it is not part of an Increment and returns to the Product Backlog. Several Increments may be made in one Sprint, and an Increment may be released before the Sprint Review, which is not a gate. Teams on one product share one Definition of Done.",
                "A usable, additive stepping stone" to listOf("usable", "additive", "stepping stone"),
                "The Definition of Done decides what counts" to listOf("definition of done", "dod"),
                "Not Done goes back to the Product Backlog" to listOf("returns to the product backlog", "back to the product backlog"),
                "Release before the Review; not a gate" to listOf("not a gate", "before the sprint review", "released"),
            ),
            B.glossary(
                "Increment" to "A usable, verified piece of product that adds to all earlier ones; a stepping stone to the Product Goal.",
                "Definition of Done" to "A formal description of the quality an Increment must meet; the commitment of the Increment.",
                "Usable" to "Working well enough that someone could actually use it.",
                "Additive" to "Each Increment adds to everything built before and works with it.",
                "Gate" to "A checkpoint that must be passed before something is allowed; the Sprint Review is not one for releases.",
            ),
            B.recap(
                "An Increment is born the moment an item meets the Definition of Done; there may be several per Sprint.",
                "An Increment may be released before the Sprint Review; the Review is never a gate to releasing value.",
                "The organisation's DoD is the minimum; teams on one product share one; work that fails it returns to the Product Backlog.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(productBacklog, sprintBacklog, increment)

    val quiz: Quiz = Quiz(
        id = "psm-artifacts-test", title = "Artifacts and commitments module test",
        questions = B.spread(
            listOf(
                B.q(
                    "psm-art1", "psm-product-backlog",
                    "Who is accountable for ordering the Product Backlog?",
                    listOf("The Product Owner", "The Developers", "The Scrum Master", "The key stakeholders, by vote"), setOf(0),
                    "The Product Owner is accountable for effective Product Backlog management, which includes ordering its items.",
                    whyWrong = mapOf(
                        1 to "The Developers size and select items, but the order is the Product Owner's call.",
                        2 to "The Scrum Master helps the PO find techniques for backlog management; it does not order the list.",
                        3 to "Stakeholders influence the order by convincing the Product Owner, not by voting.",
                    ),
                ),
                B.q(
                    "psm-art2", "psm-product-backlog",
                    "Which is the commitment for the Product Backlog?",
                    listOf("The Product Goal", "The Sprint Goal", "The Definition of Done", "The release plan"), setOf(0),
                    "Each artifact has one commitment. The Product Backlog's is the Product Goal, the long-term objective for the Scrum Team.",
                    whyWrong = mapOf(
                        1 to "The Sprint Goal is the commitment of the Sprint Backlog.",
                        2 to "The Definition of Done is the commitment of the Increment.",
                        3 to "A release plan is not part of the Scrum Guide at all.",
                    ),
                ),
                B.q(
                    "psm-art3", "psm-product-backlog",
                    "Who is responsible for sizing Product Backlog items?",
                    listOf("The Developers who will do the work", "The Product Owner", "The Scrum Master", "An architect outside the team"), setOf(0),
                    "The Developers who will be doing the work are responsible for the sizing. The Product Owner may influence them by helping them understand and select trade-offs.",
                    whyWrong = mapOf(
                        1 to "The PO may influence sizes by explaining trade-offs, but the Developers own them.",
                        2 to "The Scrum Master facilitates; it does not estimate the team's work.",
                        3 to "Someone outside the Scrum Team does not do the work, so does not size it.",
                    ),
                ),
                B.q(
                    "psm-art4", "psm-product-backlog",
                    "Which statements about Product Backlog refinement are true? Select two.",
                    listOf("It is an ongoing activity, not an event", "It adds detail such as a description, order and size", "It is a Scrum event timeboxed to two hours per Sprint", "Only the Product Owner may take part in it"), setOf(0, 1),
                    "Refinement is the ongoing activity of breaking items down and adding detail such as description, order and size. The Guide gives it no timebox.",
                    whyWrong = mapOf(
                        2 to "Refinement is not one of the five events and has no timebox in the Guide.",
                        3 to "The Developers size items during refinement, so they take part.",
                    ),
                ),
                B.q(
                    "psm-art5", "psm-product-backlog",
                    "Four Scrum Teams build one product. Which do they share? Select three.",
                    listOf("One Product Backlog", "One Product Owner", "One Product Goal", "One Scrum Master", "One Daily Scrum"), setOf(0, 1, 2),
                    "Several Scrum Teams on one product share the same Product Goal, Product Backlog and Product Owner.",
                    whyWrong = mapOf(
                        3 to "The Guide does not require the teams to share a Scrum Master; each Scrum Team has the accountability.",
                        4 to "Each team's Developers hold their own Daily Scrum.",
                    ),
                ),
                B.q(
                    "psm-art6", "psm-product-backlog",
                    "When is a Product Backlog item ready for selection at Sprint Planning?",
                    listOf("When the Scrum Team can get it Done within one Sprint", "When the Product Owner has signed off its full specification", "When it has a story-point estimate", "When it has been in the backlog for at least one Sprint"), setOf(0),
                    "The Guide says items that can be Done by the Scrum Team within one Sprint are deemed ready for selection.",
                    whyWrong = mapOf(
                        1 to "Scrum has no sign-off of a full specification; detail emerges through refinement.",
                        2 to "Story points are a complementary practice, not part of the Scrum Guide.",
                        3 to "Age on the list has nothing to do with readiness.",
                    ),
                ),
                B.q(
                    "psm-art7", "psm-sprint-backlog",
                    "Who owns the Sprint Backlog?",
                    listOf("The Developers", "The Product Owner", "The Scrum Master", "The whole organisation"), setOf(0),
                    "The Sprint Backlog is a plan by and for the Developers.",
                    whyWrong = mapOf(
                        1 to "The PO owns the Product Backlog, not the Developers' plan for the Sprint.",
                        2 to "The Scrum Master does not own the Developers' plan.",
                        3 to "It is the Developers' plan, highly visible to others but changed only by them.",
                    ),
                ),
                B.q(
                    "psm-art8", "psm-sprint-backlog",
                    "What is the commitment for the Sprint Backlog?",
                    listOf("The Sprint Goal", "The Product Goal", "The Definition of Done", "The Sprint forecast"), setOf(0),
                    "The Sprint Goal, the single objective for the Sprint, is the Sprint Backlog's commitment.",
                    whyWrong = mapOf(
                        1 to "The Product Goal belongs to the Product Backlog.",
                        2 to "The Definition of Done belongs to the Increment.",
                        3 to "A forecast is not a commitment in the 2020 Guide; the commitment is the Sprint Goal.",
                    ),
                ),
                B.q(
                    "psm-art9", "psm-sprint-backlog",
                    "During the Sprint, the Developers find work that was not planned but is needed for the Sprint Goal. What do they do?",
                    listOf("Add it to the Sprint Backlog themselves", "Ask the Product Owner to approve it first", "Leave it for the next Sprint", "Ask the Scrum Master to assign it"), setOf(0),
                    "The Sprint Backlog is updated throughout the Sprint as more is learned, by the Developers who own it.",
                    whyWrong = mapOf(
                        1 to "The plan is the Developers'; they need no approval to change it.",
                        2 to "Work needed for the Sprint Goal belongs in this Sprint.",
                        3 to "Nobody assigns work in Scrum; the Developers decide who does what.",
                    ),
                ),
                B.q(
                    "psm-art10", "psm-sprint-backlog",
                    "The selected work turns out larger than expected. Which response fits the Scrum Guide?",
                    listOf("The Developers and Product Owner renegotiate scope without affecting the Sprint Goal", "The Sprint Goal is changed to match the work that will be finished", "The Sprint is lengthened by a week", "The Scrum Master removes items from the Sprint Backlog"), setOf(0),
                    "If the work is different from what was expected, the Developers collaborate with the Product Owner to negotiate the scope of the Sprint Backlog within the Sprint without affecting the Sprint Goal.",
                    whyWrong = mapOf(
                        1 to "The Sprint Goal does not change in the Sprint; the work does.",
                        2 to "Sprints have a fixed length and are not stretched.",
                        3 to "The Scrum Master does not manage the Developers' plan.",
                    ),
                ),
                B.q(
                    "psm-art11", "psm-increment",
                    "What is the commitment for the Increment?",
                    listOf("The Definition of Done", "The Sprint Goal", "The release date", "The Product Goal"), setOf(0),
                    "The Definition of Done is the commitment of the Increment: the quality it must meet.",
                    whyWrong = mapOf(
                        1 to "The Sprint Goal belongs to the Sprint Backlog.",
                        2 to "Release dates are not part of the Scrum Guide.",
                        3 to "The Product Goal belongs to the Product Backlog.",
                    ),
                ),
                B.q(
                    "psm-art12", "psm-increment",
                    "Which statements about Increments are true? Select two.",
                    listOf("Several Increments may be created within one Sprint", "An Increment may be delivered to stakeholders before the Sprint ends", "An Increment can be released only after the Sprint Review", "Each Sprint produces exactly one Increment"), setOf(0, 1),
                    "Multiple Increments may be created in a Sprint, and an Increment may be delivered before the end of the Sprint. The Sprint Review is not a gate.",
                    whyWrong = mapOf(
                        2 to "The Sprint Review should never be considered a gate to releasing value.",
                        3 to "An Increment is born each time an item meets the Definition of Done, so there can be several.",
                    ),
                ),
                B.q(
                    "psm-art13", "psm-increment",
                    "The organisation has a standard Definition of Done. What must a Scrum Team do with it?",
                    listOf("Follow it as a minimum; it may add stricter criteria", "Replace it with one of its own", "Follow it only for releases to production", "Ignore it if the Product Owner agrees"), setOf(0),
                    "If the organisation has a Definition of Done as a standard, all Scrum Teams must follow it as a minimum.",
                    whyWrong = mapOf(
                        1 to "A team can add to the organisation's standard, not replace it.",
                        2 to "The Definition of Done applies to every Increment, not only production releases.",
                        3 to "The PO cannot waive an organisational standard.",
                    ),
                ),
                B.q(
                    "psm-art14", "psm-increment",
                    "An item does not meet the Definition of Done at the end of the Sprint. What happens to it?",
                    listOf("It returns to the Product Backlog for future consideration", "It is presented at the Sprint Review as partly done", "It is released with a known-issues note", "It automatically moves into the next Sprint Backlog"), setOf(0),
                    "Work that does not meet the Definition of Done is not part of an Increment, cannot be released or presented, and returns to the Product Backlog.",
                    whyWrong = mapOf(
                        1 to "Work that does not meet the Definition of Done cannot even be presented at the Sprint Review.",
                        2 to "It is not part of an Increment, so it cannot be released.",
                        3 to "It goes back to the Product Backlog, where the PO orders it again; nothing moves automatically.",
                    ),
                ),
            ),
        ),
    )
}
