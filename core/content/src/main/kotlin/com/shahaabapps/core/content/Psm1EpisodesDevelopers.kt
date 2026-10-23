package com.shahaabapps.core.content

/** Developers: the plan, the quality and each other: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesDevelopers {
    private val classic = Podcast(
        "psm-developers",
        listOf(
            maya("Hi, and welcome back. Today: the Developers, the people inside a Scrum Team who build the product. You are probably one already. But the P S M one assessment uses the word in a precise way, and it gives Developers four things they always answer for."),
            leo("Good, because I assumed Developer just meant someone who writes code. What is it, in plain words?"),
            maya("The Scrum Guide says Developers are the people in the Scrum Team committed to creating any aspect of a usable Increment each Sprint. An Increment is the usable piece of the product you finish in a Sprint. Any aspect means code, design, tests, the release: whatever it takes."),
            leo("So the tester on my team is a Developer. And the designer."),
            maya("Both. Picture a team building a running app: three Android developers, a backend developer, a designer and a tester. Six Developers. The skills vary with the product; what they share is the accountability. Which is the next word."),
            leo("Accountability. Meaning what I get blamed for?"),
            maya("[chuckle] Meaning what you answer for. The Guide gives the Developers four. One: creating the plan for the Sprint, the Sprint Backlog. Two: instilling quality by sticking to the Definition of Done. Three: adapting their plan every day toward the Sprint Goal. Four: holding each other accountable as professionals."),
            leo("Let's take them one by one. The plan. Our manager usually writes it."),
            maya("In Scrum, the Developers do. The Sprint Backlog is the Sprint Goal, the items picked from the Product Backlog for this Sprint, and the plan for building them. The Guide calls it a plan by and for the Developers: a highly visible, real-time picture of the work they plan to do."),
            leo("Remind me, what's the Sprint Goal?"),
            maya("The single objective for the Sprint: the reason it's worth doing. For example: runners can save a run without signal and sync it later. The whole Scrum Team crafts it at Sprint Planning. The Sprint Planning episode has the detail."),
            leo("And who decides how much goes in? My manager likes to say forty points a Sprint."),
            maya("The Developers. They select the items, talking it through with the Product Owner, the person accountable for the product's value. The Guide says the more they know about their past performance, their upcoming capacity and their Definition of Done, the more confident their forecast. And story points aren't in the Scrum Guide at all; they're an optional practice."),
            leo("Can the Product Owner tell us how to build it, though? Use this library, that screen layout?"),
            maya("No. The Guide is blunt: how the Developers turn Product Backlog items into a usable Increment is their decision alone. Nobody else tells them how. The Product Owner decides what's most valuable; the Developers decide how to build it."),
            leo("Okay. Number two. Quality and the Definition of Done."),
            maya("The Definition of Done is a shared, formal description of the quality every Increment must meet. Say yours reads: code reviewed, tests passing, works offline, crash-free on the test devices, ready for the Play Store. The Developers are required to meet it, every time. That's what instilling quality means: building it in, not checking it at the end."),
            leo("And if the date is tight? Skip the tests, just this once?"),
            maya("Then it isn't done. Work that doesn't meet the Definition of Done can't be released, and isn't part of the Increment. It goes back to the Product Backlog. Under the picture, that's what keeps the board honest: done always means the same thing. The Increment episode covers where a Definition of Done comes from."),
            leo("Number three. Adapting the plan every day."),
            maya("The plan isn't fixed; the goal is. Every day the Developers check progress toward the Sprint Goal and change the plan as they learn. The Daily Scrum is their fifteen minutes for exactly that, and the Guide says they choose its structure. It's not a status report for a manager, and not the only time they can adjust."),
            leo("What if halfway through we find the work is twice as big?"),
            maya("Then the Developers talk to the Product Owner and renegotiate the scope, what's in the Sprint Backlog, without endangering the Sprint Goal. Maybe sync happens only on Wi-Fi for now. The goal stays; the details move. And quality doesn't drop."),
            leo("And number four. Holding each other accountable as professionals. That sounds awkward."),
            maya("It can be. It means peers, not a boss, keep the standard. Say a teammate merges without a review, or skips the crash test. A professional says so, kindly, the same day. Nobody waits for a manager to notice. And it works both ways: you invite the same from them."),
            leo("Where does all this stop, though? Can the Developers decide anything they like?"),
            maya("No. The Developers own the plan and the how, inside a frame. The Product Owner orders the Product Backlog, so the Developers don't choose what's most valuable. The Sprint Goal doesn't change mid-Sprint just because the work got hard. And the Definition of Done is a floor, never a suggestion."),
            leo("Can a Scrum Master or a Product Owner be a Developer too?"),
            maya("Yes. The Guide says that if the Product Owner or the Scrum Master are actively working on items in the Sprint Backlog, they take part as Developers, including in the Daily Scrum. The Product Owner episode is next."),
            leo("So what do I remember for the exam?"),
            maya("If a question asks who the Developers are: anyone in the Scrum Team who creates any part of a usable Increment, not just coders. They create the Sprint Backlog, instil quality by meeting the Definition of Done, adapt the plan daily toward the Sprint Goal, and hold each other accountable. How to build is their decision alone, and they decide how much work to take on."),
            leo("And anything to try today?"),
            maya("Write down your team's Definition of Done, or notice that there isn't one. Find one item called done last week and check it against that definition. Note who sized the last few items, and who decided how much went into the Sprint. Then read the Developers section of the Scrum Guide."),
            leo("Our plan, our quality, each other. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-developers",
        listOf(
            maya("Welcome to Case Files. Today's case: The Plan That Wasn't Theirs. An Android team missed its Sprint Goal three Sprints in a row. Then a release went to the Play Store with a crash on every older phone. Nobody on the team was surprised. That's the clue that bothered me."),
            leo("[sigh] Detective Leo. Three misses and a crash. First suspect: lazy Developers. They just need to work harder."),
            maya("Wrong suspect. They worked weekends. The exam would ask a different question: were the Developers allowed to be Developers? In Scrum, Developers are the people in the Scrum Team committed to creating any aspect of a usable Increment each Sprint. An Increment is the usable piece of the product."),
            leo("Any aspect. So not only the coders?"),
            maya("Not only the coders. Designers, testers, whoever builds part of it. And the Scrum Guide gives the Developers four accountabilities: create the plan for the Sprint, instil quality through the Definition of Done, adapt the plan daily toward the Sprint Goal, and hold each other accountable as professionals. Let's check each one. First clue: the plan."),
            leo("What about it?"),
            maya("The department manager wrote it. Every task, every person, every hour, in a spreadsheet, on Monday morning. The team got a copy."),
            leo("Second suspect, then. The manager's plan wasn't detailed enough. More detail, fewer misses."),
            maya("[pause] Also wrong. The problem isn't detail; it's whose plan it is. The Sprint Backlog, the Sprint Goal plus the chosen items plus the plan, is a plan by and for the Developers. A highly visible, real-time picture of the work they intend to do. A spreadsheet from outside can't change at the speed the work does."),
            leo("And how much they take on? The manager set that too, I bet."),
            maya("Forty points, every Sprint. But the Developers select the items, talking it through with the Product Owner, the person accountable for the product's value. The Guide says knowing their past performance, their capacity and their Definition of Done makes their forecast more confident. And points aren't in the Scrum Guide at all."),
            leo("Who decides how they build it?"),
            maya("The Developers, alone. The Guide says nobody else tells them how to turn Product Backlog items into a usable Increment. Not the manager, not the Product Owner."),
            leo("Okay. Second clue?"),
            maya("The crash. The team's Definition of Done, the shared description of quality every Increment must meet, said: tested on the oldest supported phones. On the Thursday before release, the tests were skipped. The date mattered more."),
            leo("So, quality."),
            maya("Instilling quality by adhering to the Definition of Done. Under the picture, that's what keeps the board honest: done always means the same thing. Work that doesn't meet it isn't part of the Increment and can't be released. It goes back to the Product Backlog. The Increment episode tells that part."),
            leo("Third clue?"),
            maya("The Daily Scrum. Every morning, each person reported their hours to the manager. Nobody talked about the Sprint Goal. In week two, Priya found that offline sync was twice as big as planned. She said nothing, and quietly dropped half of it."),
            leo("Is that so bad? She adapted."),
            maya("She adapted alone and in silence. The Daily Scrum is the Developers' fifteen minutes to inspect progress toward the Sprint Goal and adapt the plan, in whatever structure they choose. And when work turns out bigger, the Developers talk to the Product Owner and renegotiate the scope without endangering the Sprint Goal. Openly, the same day."),
            leo("And the fourth accountability?"),
            maya("Last clue: everyone saw the tests being skipped. Nobody said a word. Holding each other accountable as professionals means peers keep the standard, not a boss. Somebody says: that isn't done yet, kindly, that day."),
            leo("So could the Developers just have ignored the manager and done anything they liked?"),
            maya("That's where it stops. The Developers own the plan and the how, inside a frame. The Product Owner orders the Product Backlog, so they don't pick what's most valuable. The Sprint Goal holds for the Sprint. And the Definition of Done is a floor, never a suggestion."),
            leo("And what happened in the end?"),
            maya("The manager stepped back. The Developers planned their own Sprint, smaller, with a real Sprint Goal. The Definition of Done went on the wall. And at the next Daily Scrum, the tester said: the build isn't done; old phones crash. They fixed it before the release. The Scrum Master episode covers who helped make that safe."),
            leo("So, the case summary for the exam?"),
            maya("Developers are anyone in the Scrum Team who creates any part of a usable Increment, not just coders. They create the Sprint Backlog, instil quality by meeting the Definition of Done, adapt the plan daily toward the Sprint Goal, and hold each other accountable. How to build is their decision alone, and they decide how much work to take on."),
            leo("Case closed. And to try today?"),
            maya("Write down your team's Definition of Done, or notice that there isn't one. Find one item called done last week and check it against that definition. Note who sized the last few items, and who decided how much went into the Sprint. Then read the Developers section of the Scrum Guide."),
            leo("Whose plan is it? Theirs. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-developers",
        listOf(
            maya("Okay, did you hear what happened on the app team this week? It was the week everyone found out what a Developer actually is. Some of them didn't like the answer."),
            leo("[laugh] I heard the designer stormed out of Sprint Planning. Start me off. What is a Developer, officially?"),
            maya("The Scrum Guide says Developers are the people in the Scrum Team committed to creating any aspect of a usable Increment each Sprint. An Increment is the usable piece of the product you finish in a Sprint. Any aspect: code, design, tests, release work."),
            leo("And what do they answer for?"),
            maya("Four things. Creating the plan for the Sprint, called the Sprint Backlog. Instilling quality by meeting the Definition of Done. Adapting the plan every day toward the Sprint Goal. And holding each other accountable as professionals. Monday broke the first one."),
            leo("Who was Monday?"),
            maya("Marcus, the department manager. He wrote the whole Sprint Backlog himself on Sunday night. Tasks, names, hours. Emailed it at seven a.m."),
            leo("Thoughtful of him."),
            maya("Thoughtful, and not his to write. The Sprint Backlog is the Sprint Goal, the items chosen for this Sprint, and the plan to build them. The Guide calls it a plan by and for the Developers, a real-time picture of their work. And the Guide says nobody tells the Developers how to turn items into a usable Increment."),
            leo("Did Marcus also set how much they'd take on?"),
            maya("Forty story points. But the Developers select the items, with the Product Owner, the person accountable for the product's value. Their past performance, their capacity and their Definition of Done make the forecast. And story points aren't even in the Scrum Guide; they're an optional practice."),
            leo("Tuesday?"),
            maya("Tuesday was Tom. The Definition of Done, the shared description of quality every Increment must meet, says: code reviewed and tested on old phones. Tom had a date. Tom skipped both."),
            leo("[chuckle] Of course he did."),
            maya("Instilling quality means building it in, by meeting the Definition of Done every time. Under the picture, it's what keeps done meaning the same thing. Work that doesn't meet it isn't part of the Increment, can't be released, and goes back to the Product Backlog."),
            leo("Wednesday?"),
            maya("Priya. In the middle of the Sprint she found offline sync was twice as big as planned. So she quietly cut half of it, told nobody, and carried on."),
            leo("That's adapting, isn't it? She changed the plan."),
            maya("Changing the plan is right. Doing it alone and in silence isn't. The Daily Scrum is the Developers' fifteen minutes to inspect progress toward the Sprint Goal and adapt the plan together. And when work grows, the Developers renegotiate the scope with the Product Owner, without endangering the Sprint Goal. The goal stays; the details move."),
            leo("What would that have looked like?"),
            maya("Priya says it at the Daily Scrum. The Developers agree a smaller version, sync on Wi-Fi only for now, and she walks over to the Product Owner that morning. Same goal, smaller scope, nobody surprised at the Sprint Review."),
            leo("Thursday. The designer storming out?"),
            maya("Dana. She was told she had to come to Sprint Planning as a Developer. She said: I'm a designer, not a Developer, and left."),
            leo("To be fair, nobody calls a designer a developer."),
            maya("Scrum does. In Scrum it's an accountability, not a job title. Dana builds part of the usable product, so she's a Developer, and her designs belong in the plan. The skills vary with the product; the accountability is shared."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. The intern watched Tom skip the review, saw the old phones crash in testing, and said nothing, because Tom is senior."),
            leo("I'd have kept quiet too, honestly."),
            maya("Most people would. But holding each other accountable as professionals means peers keep the standard, not a boss. A kind word, the same day: that one isn't done yet. And seniors invite it, too."),
            leo("So can the Developers do whatever they like now? Marcus is worried."),
            maya("Tell Marcus no. That's the edge of it. The Developers own the plan and the how, inside a frame: the Product Owner orders the Product Backlog, the Sprint Goal holds for the Sprint, and the Definition of Done is a floor. The Product Owner episode is next, if you want more of this week."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Developers are anyone in the Scrum Team who creates any part of a usable Increment, not just coders. They create the Sprint Backlog, instil quality by meeting the Definition of Done, adapt the plan daily toward the Sprint Goal, and hold each other accountable. How to build is their decision alone, and they decide how much work to take on."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Write down your team's Definition of Done, or notice that there isn't one. Find one item called done last week and check it against that definition. Note who sized the last few items, and who decided how much went into the Sprint. Then read the Developers section of the Scrum Guide."),
            leo("Speak up, intern. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-developers",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning for P S M one. [pause] Tonight: the people who build the thing. Probably you."),
            ivy("Me? I'm just an Android developer, Nova."),
            nova("[chuckle] Then you're a Developer, with a capital D. The Scrum Guide says Developers are the people in the Scrum Team committed to creating any aspect of a usable Increment each Sprint. The Increment is the usable piece of the product you finish in a Sprint."),
            ivy("Any aspect. Even the designer?"),
            nova("Even the designer. And the tester. Picture a team building a running app: three Android developers, one backend, a designer, a tester. [pause] Six Developers. It's an accountability, not a job title."),
            ivy("Accountability. That's a heavy word for this hour."),
            nova("Just what you answer for. The Developers answer for four things. Making the plan for the Sprint. Building in quality, through the Definition of Done. Adapting the plan every day toward the Sprint Goal. [pause] And holding each other accountable, as professionals."),
            ivy("The plan first. Who writes it?"),
            nova("They do. The plan is the Sprint Backlog: the Sprint Goal, the items chosen for this Sprint, and how they'll build them. The Guide calls it a plan by and for the Developers. Highly visible. Real-time. Theirs."),
            ivy("Why does it matter so much who writes it?"),
            nova("Because the people doing the work see the problems first. [pause] A plan written somewhere else can't change at the speed the work does. Under the picture, the Sprint Backlog is how the Developers keep their own work transparent."),
            ivy("And the Sprint Goal is?"),
            nova("The single objective for the Sprint. Runners can save a run without signal, and sync it later. [pause] The whole Scrum Team crafts it at Sprint Planning."),
            ivy("Who says how much fits in a Sprint?"),
            nova("The Developers, talking it through with the Product Owner, the person accountable for the product's value. Their past performance, their capacity, their Definition of Done: the more they know, the more confident the forecast. Not a manager's number."),
            ivy("And how to build it? The Product Owner gets a say?"),
            nova("No. That's theirs alone. The Guide says nobody else tells the Developers how to turn Product Backlog items into a usable Increment. [pause] What is the Product Owner's. How is the Developers'."),
            ivy("Quality, then. Tell me about the Definition of Done."),
            nova("A shared, formal description of the quality every Increment must meet. Code reviewed. Tests passing. Works offline. Ready for the Play Store. The Developers are required to meet it. Every time. [pause] Instilling quality means building it in, not checking at the end."),
            ivy("And on a tired Thursday, when the date's tomorrow?"),
            nova("Then it isn't done. Work that doesn't meet the Definition of Done isn't part of the Increment and can't be released. It goes back to the Product Backlog. That's what keeps done meaning the same thing, even on tired Thursdays."),
            ivy("Adapting every day. How?"),
            nova("The Daily Scrum. Fifteen minutes, for the Developers, to inspect progress toward the Sprint Goal and change the plan. They choose how to run it. [pause] It's not a status report, and it's not the only time they can change course."),
            ivy("And if the work turns out twice as big?"),
            nova("They talk to the Product Owner and renegotiate the scope, without endangering the Sprint Goal. Maybe sync works on Wi-Fi only, for now. The goal stays. The details move. And quality never drops."),
            ivy("And the last one. Holding each other accountable. Sounds a little stern."),
            nova("It's gentler than it sounds. Peers keep the standard, not a boss. A teammate skips a review; you say, kindly, the same day: that one isn't done yet. [pause] And you ask them to do the same for you."),
            ivy("And if I'm the senior one?"),
            nova("Then you go first. Invite it. [pause] Ask the junior to check your tests, and thank her when she finds something."),
            ivy("Can the Developers decide everything, then?"),
            nova("No. That's the edge. The Product Owner orders the Product Backlog, so they don't choose what matters most. The Sprint Goal holds through the Sprint. And the Definition of Done is a floor, not a suggestion. [pause] The Product Owner gets their own late night next."),
            ivy("Can the Product Owner, or the Scrum Master, be a Developer too?"),
            nova("If they're actively working on items in the Sprint Backlog, yes. Then they take part as Developers, Daily Scrum and all."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Developers are anyone in the Scrum Team who creates any part of a usable Increment, not just coders. They create the Sprint Backlog, instil quality by meeting the Definition of Done, adapt the plan daily toward the Sprint Goal, and hold each other accountable. How to build is their decision alone, and they decide how much work to take on."),
            ivy("And something to try today, before I sleep?"),
            nova("Write down your team's Definition of Done, or notice that there isn't one. [pause] Find one item called done last week and check it against that definition. Note who sized the last few items, and who decided how much went into the Sprint. Then read the Developers section of the Scrum Guide."),
            ivy("Our plan. Our quality. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-developers",
        listOf(
            nova("Tonight is date night, darling. And tonight we talk about Developers. You."),
            rafe("Me? I thought tonight was about us."),
            nova("[laugh] It's about what you answer for, love. The Scrum Guide says Developers are the people in the Scrum Team committed to creating any aspect of a usable Increment each Sprint. The Increment is the usable piece of the product you finish in a Sprint."),
            rafe("Any aspect. I like a broad-minded definition. Quiz me."),
            nova("Exam style, then. A team has three Android developers, a backend developer, a designer and a tester. Who are the Developers?"),
            rafe("All six. The designer and the tester build parts of the usable product too. It's an accountability, not a job title."),
            nova("Right answer. Nobody left out. [kiss]"),
            rafe("And what do Developers answer for?"),
            nova("Four things. Creating the plan for the Sprint, the Sprint Backlog. Instilling quality by meeting the Definition of Done. Adapting the plan every day toward the Sprint Goal. And holding each other accountable as professionals."),
            rafe("Holding each other. I'll take that one personally. Now the plan: who writes it?"),
            nova("You tell me. Who creates the Sprint Backlog?"),
            rafe("The Developers. It's the Sprint Goal, the items chosen for the Sprint, and the plan to build them. The Guide calls it a plan by and for the Developers: highly visible, real-time."),
            nova("Right answer. A man with a plan. [kiss]"),
            rafe("Why does it matter who writes it?"),
            nova("Because the people doing the work see the problems first. A plan written somewhere else can't change as fast as the work does."),
            rafe("And the Sprint Goal?"),
            nova("The single objective for the Sprint. Say: runners can save a run without signal and sync it later. The whole Scrum Team crafts it at Sprint Planning. Next quiz. Your manager says the team will take forty points this Sprint. Who decides how much the team takes on?"),
            rafe("The manager. Someone has to keep us honest."),
            nova("No kiss for that one. The Developers select the items, talking it through with the Product Owner, the person accountable for the product's value. Their past performance, capacity and Definition of Done make the forecast. And story points aren't in the Scrum Guide at all."),
            rafe("Ouch. Then at least the Product Owner tells us how to build it?"),
            nova("Not that either. The Guide says nobody else tells the Developers how to turn Product Backlog items into a usable Increment. The Product Owner owns what; the Developers own how."),
            rafe("Then let me win you back. What's the Definition of Done?"),
            nova("A shared, formal description of the quality every Increment must meet. Code reviewed, tests passing, works offline, ready for the Play Store. My question back: the date is tomorrow and the tests aren't finished. Can you call it done?"),
            rafe("No. If it doesn't meet the Definition of Done, it isn't part of the Increment and can't be released. It goes back to the Product Backlog."),
            nova("Right answer. Patience is very attractive. [kiss]"),
            rafe("And adapting every day?"),
            nova("The Daily Scrum: fifteen minutes for the Developers to inspect progress toward the Sprint Goal and adapt the plan. They choose how to run it. Not a status report, and not the only time to change course."),
            rafe("And if halfway through, the work turns out twice as big?"),
            nova("You tell me."),
            rafe("Talk to the Product Owner and renegotiate the scope, without endangering the Sprint Goal. Sync on Wi-Fi only, for now. The goal stays; the details move; quality doesn't drop."),
            nova("Right answer. Flexible, and still committed. [kiss]"),
            rafe("And holding each other accountable?"),
            nova("Peers keep the standard, not a boss. A teammate skips a review; you say so, kindly, the same day. And you invite the same back."),
            rafe("And if I'm the senior one?"),
            nova("Then you go first, darling. Ask the junior to check your tests, and thank her when she finds something."),
            rafe("Can the Developers decide everything, then?"),
            nova("No, that's the edge. The Product Owner orders the Product Backlog, the Sprint Goal holds for the Sprint, and the Definition of Done is a floor. The Product Owner episode is our next date."),
            rafe("And can the Product Owner be a Developer too?"),
            nova("If she's actively working on items in the Sprint Backlog, yes. Then she takes part as a Developer, Daily Scrum and all."),
            rafe("Then the last question, for the exam. What do we remember?"),
            nova("Developers are anyone in the Scrum Team who creates any part of a usable Increment, not just coders. They create the Sprint Backlog, instil quality by meeting the Definition of Done, adapt the plan daily toward the Sprint Goal, and hold each other accountable. How to build is their decision alone, and they decide how much work to take on."),
            rafe("Every word, darling. That earns a long one. [kiss]"),
            nova("Mm. And something to try today?"),
            rafe("Write down our team's Definition of Done, or notice that there isn't one. Find one item called done last week and check it against that definition. Note who sized the last few items, and who decided how much went into the Sprint. Then read the Developers section of the Scrum Guide."),
            nova("Our plan, darling. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun builders() = DiagramSpec(
        "Everyone who builds any part of the usable Increment is a Developer, whatever their job title.",
        nodes = listOf(
            DNode("android", "Android, times 3", 0.25f, 0.2f, w = 0.42f, h = 0.22f),
            DNode("backend", "Backend", 0.75f, 0.2f, w = 0.42f, h = 0.22f),
            DNode("design", "Design", 0.25f, 0.55f, w = 0.42f, h = 0.22f),
            DNode("test", "Testing", 0.75f, 0.55f, w = 0.42f, h = 0.22f),
            DNode("note", "All Developers: an accountability, not a title", 0.5f, 0.87f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        aspect = 0.8f,
    )

    private fun four() = DiagramSpec(
        "The four things the Developers always answer for.",
        nodes = listOf(
            DNode("plan", "The plan", 0.25f, 0.22f, w = 0.42f, h = 0.26f, tone = 1, sub = "the Sprint Backlog"),
            DNode("quality", "Quality", 0.75f, 0.22f, w = 0.42f, h = 0.26f, tone = 1, sub = "the Definition of Done"),
            DNode("adapt", "Adapt daily", 0.25f, 0.68f, w = 0.42f, h = 0.26f, tone = 1, sub = "toward the Sprint Goal"),
            DNode("each", "Each other", 0.75f, 0.68f, w = 0.42f, h = 0.26f, tone = 1, sub = "as professionals"),
        ),
        aspect = 0.8f,
    )

    private fun sprintBacklog() = DiagramSpec(
        "The Sprint Backlog: the Sprint Goal, the items chosen for the Sprint and the plan, made by and for the Developers.",
        nodes = listOf(
            DNode("goal", "Sprint Goal", 0.5f, 0.14f, w = 0.8f, h = 0.2f, tone = 1, sub = "why"),
            DNode("items", "Selected items", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "what"),
            DNode("plan", "The plan", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "how"),
            DNode("who", "By and for the Developers", 0.5f, 0.85f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("items", "goal"),
            DEdge("plan", "goal"),
        ),
        aspect = 0.8f,
    )

    private fun whatHow() = DiagramSpec(
        "The Product Owner orders what is most valuable; the Developers choose how much to take on and how to build it.",
        nodes = listOf(
            DNode("po", "Product Owner", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "what is valuable"),
            DNode("devs", "Developers", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "how, and how much"),
            DNode("pb", "Product Backlog", 0.25f, 0.55f, w = 0.42f, h = 0.2f, sub = "ordered"),
            DNode("inc", "Increment", 0.75f, 0.55f, w = 0.42f, h = 0.2f, sub = "usable"),
            DNode("mgr", "A manager's number", 0.5f, 0.87f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "not in the Scrum Guide"),
        ),
        edges = listOf(
            DEdge("po", "pb", "orders"),
            DEdge("pb", "devs", "select"),
            DEdge("devs", "inc", "build"),
        ),
        aspect = 0.8f,
    )

    private fun done() = DiagramSpec(
        "Work that meets the Definition of Done joins the Increment; work that doesn't goes back to the Product Backlog.",
        nodes = listOf(
            DNode("work", "Finished work", 0.17f, 0.3f, w = 0.3f, h = 0.26f),
            DNode("dod", "Definition of Done", 0.5f, 0.3f, w = 0.3f, h = 0.26f, kind = NodeKind.NOTE, tone = 1),
            DNode("inc", "Increment", 0.83f, 0.3f, w = 0.3f, h = 0.26f, sub = "usable"),
            DNode("pb", "Back to the Product Backlog", 0.5f, 0.8f, w = 0.6f, h = 0.2f, tone = 2, sub = "not released"),
        ),
        edges = listOf(
            DEdge("work", "dod", "check"),
            DEdge("dod", "inc", "meets it"),
            DEdge("dod", "pb", "doesn't"),
        ),
        aspect = 0.8f,
    )

    private fun adapt() = DiagramSpec(
        "Every day the Developers adapt the plan toward a fixed Sprint Goal; bigger work means renegotiating scope with the Product Owner.",
        nodes = listOf(
            DNode("goal", "Sprint Goal", 0.5f, 0.15f, w = 0.6f, h = 0.2f, tone = 1, sub = "stays"),
            DNode("daily", "Daily Scrum", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "fifteen minutes"),
            DNode("plan", "The plan", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "changes"),
            DNode("po", "Product Owner", 0.25f, 0.85f, w = 0.42f, h = 0.2f, sub = "scope agreed"),
            DNode("quality", "Quality", 0.75f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "never drops"),
        ),
        edges = listOf(
            DEdge("daily", "plan", "adapt"),
            DEdge("plan", "goal", "toward"),
            DEdge("po", "plan", "renegotiate"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Developers questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("who", "Who are Developers", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "anyone building the Increment"),
            DNode("plan", "Sprint Backlog", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "by and for the Developers"),
            DNode("dod", "Quality", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "meet the Definition of Done"),
            DNode("adapt", "Every day", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "adapt toward the Sprint Goal"),
            DNode("how", "How to build", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Developers alone"),
            DNode("much", "How much to take", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Developers decide"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("who", "plan", "dod", "adapt", "how", "much")
    private val allFour = setOf("plan", "quality", "adapt", "each")
    private val allBuilders = setOf("android", "backend", "design", "test")
    private val fourCard = BoardCard("Four accountabilities", "Plan, quality, adapt daily, hold each other accountable.")
    private val frameCard = BoardCard("Inside a frame", "The Product Owner orders; the Sprint Goal holds; done is a floor.")
    private val takeaways = BoardCard("Listen for the phrase", "Any builder is a Developer. Their plan, their how, the Definition of Done.")
    private val today = BoardCard("Today", "Your Definition of Done; check last week's done; who sized; read Developers.")

    private fun classicBoard() = PodcastBoard(
        "psm-developers",
        listOf(
            // 0: "Hi, and welcome back. Today: the Developers..."
            Scene(
                0, "Who the Developers are", builders(),
                beats = listOf(
                    Beat(0, card = BoardCard("Developers", "The people inside a Scrum Team who build the product.")),
                    Beat(2, light = allBuilders, card = BoardCard("Any aspect", "Anyone creating any part of a usable Increment each Sprint.")),
                    Beat(4, light = setOf("design", "test", "note"), card = BoardCard("Six Developers", "Three Android, one backend, a designer and a tester.")),
                ),
            ),
            // 5: "Accountability. Meaning what I get blamed for?"
            Scene(
                5, "Four accountabilities", four(),
                beats = listOf(
                    Beat(6, light = allFour, card = fourCard),
                ),
            ),
            // 7: "Let's take them one by one. The plan."
            Scene(
                7, "Their plan", sprintBacklog(),
                beats = listOf(
                    Beat(8, light = setOf("who"), card = BoardCard("Sprint Backlog", "Sprint Goal, selected items and a plan, by and for the Developers.")),
                    Beat(10, light = setOf("goal"), pulse = "plan" to "goal", card = BoardCard("Sprint Goal", "The single objective: why the Sprint is worth doing.")),
                ),
            ),
            // 11: "And who decides how much goes in?"
            Scene(
                11, "What, how and how much", whatHow(),
                beats = listOf(
                    Beat(12, show = setOf("po", "pb", "devs", "mgr"), light = setOf("devs", "mgr"), pulse = "pb" to "devs", card = BoardCard("How much: the Developers", "With the Product Owner. Story points aren't in the Guide.")),
                    Beat(14, show = setOf("inc"), light = setOf("devs", "inc"), pulse = "devs" to "inc", card = BoardCard("How: the Developers alone", "Nobody else tells them how to build the Increment.")),
                ),
            ),
            // 15: "Okay. Number two. Quality and the Definition of Done."
            Scene(
                15, "Quality, built in", done(),
                beats = listOf(
                    Beat(16, show = setOf("work", "dod", "inc"), light = setOf("dod"), pulse = "work" to "dod", card = BoardCard("Definition of Done", "A formal, shared bar of quality that every Increment meets.")),
                    Beat(18, show = setOf("pb"), light = setOf("pb"), pulse = "dod" to "pb", card = BoardCard("Not done", "Not part of the Increment; back to the Product Backlog.")),
                ),
            ),
            // 19: "Number three. Adapting the plan every day."
            Scene(
                19, "Adapt every day", adapt(),
                beats = listOf(
                    Beat(20, show = setOf("goal", "daily", "plan"), light = setOf("daily", "plan"), pulse = "daily" to "plan", card = BoardCard("Daily Scrum", "The Developers' fifteen minutes; they choose the structure.")),
                    Beat(22, show = setOf("po", "quality"), light = setOf("po", "plan"), pulse = "po" to "plan", card = BoardCard("Twice as big?", "Renegotiate scope with the Product Owner; the goal stays.")),
                ),
            ),
            // 23: "And number four. Holding each other accountable..."
            Scene(
                23, "Each other", four(),
                beats = listOf(
                    Beat(24, light = setOf("each"), card = BoardCard("Hold each other accountable", "Peers keep the standard: say so, kindly, the same day.")),
                ),
            ),
            // 25: "Where does all this stop, though?"
            Scene(
                25, "The frame", whatHow(),
                beats = listOf(
                    Beat(26, light = setOf("po", "pb"), pulse = "po" to "pb", card = frameCard),
                    Beat(28, light = setOf("devs"), card = BoardCard("Two hats", "A Product Owner or Scrum Master doing Sprint work is a Developer.")),
                ),
            ),
            // 29: "So what do I remember for the exam?"
            Scene(
                29, "For the exam", exam(),
                beats = listOf(
                    Beat(30, show = all6, light = all6, card = takeaways),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-developers--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Plan That Wasn't Theirs..."
            Scene(
                0, "The Plan That Wasn't Theirs", four(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Three missed Sprint Goals, then a crash on older phones.")),
                    Beat(2, card = BoardCard("Suspect cleared: laziness", "They worked weekends. Were they allowed to be Developers?")),
                    Beat(4, light = allFour, card = fourCard),
                ),
            ),
            // 5: "What about it?"
            Scene(
                5, "Clue one: the plan", sprintBacklog(),
                beats = listOf(
                    Beat(6, show = setOf("goal", "items", "plan"), light = setOf("plan"), card = BoardCard("Written by the manager", "Every task and every hour, in a spreadsheet, on Monday.")),
                    Beat(8, show = setOf("who"), light = setOf("who"), card = BoardCard("Suspect cleared: detail", "The Sprint Backlog is by and for the Developers.")),
                ),
            ),
            // 9: "And how much they take on?"
            Scene(
                9, "How much, and how", whatHow(),
                beats = listOf(
                    Beat(10, show = setOf("po", "pb", "devs", "mgr"), light = setOf("mgr", "devs"), pulse = "pb" to "devs", card = BoardCard("Forty points", "The Developers select, with the Product Owner. No points in the Guide.")),
                    Beat(12, show = setOf("inc"), light = setOf("devs", "inc"), pulse = "devs" to "inc", card = BoardCard("How: theirs alone", "Nobody else tells them how to build the Increment.")),
                ),
            ),
            // 13: "Okay. Second clue?"
            Scene(
                13, "Clue two: the crash", done(),
                beats = listOf(
                    Beat(14, show = setOf("work", "dod"), light = setOf("dod"), card = BoardCard("Tests skipped", "Old phones were in the Definition of Done. The date won.")),
                    Beat(16, show = setOf("inc", "pb"), light = setOf("dod", "pb"), pulse = "dod" to "pb", card = BoardCard("Not done", "Not part of the Increment, not released: back to the backlog.")),
                ),
            ),
            // 17: "Third clue?"
            Scene(
                17, "Clue three: the Daily Scrum", adapt(),
                beats = listOf(
                    Beat(18, show = setOf("daily", "plan"), light = setOf("daily"), card = BoardCard("Hours, not the goal", "A report to the manager; half of sync cut in silence.")),
                    Beat(20, show = setOf("goal", "po", "quality"), light = setOf("po", "plan"), pulse = "po" to "plan", card = BoardCard("Adapt, openly", "Renegotiate scope with the Product Owner; the Sprint Goal holds.")),
                ),
            ),
            // 21: "And the fourth accountability?"
            Scene(
                21, "Clue four: silence", four(),
                beats = listOf(
                    Beat(22, light = setOf("each"), card = BoardCard("Hold each other accountable", "Everyone saw the skipped tests. Nobody said a word.")),
                    Beat(24, light = setOf("plan", "quality"), card = frameCard),
                    Beat(26, light = allFour, card = BoardCard("In the end", "Their own plan, a real goal, and a tester who spoke up.")),
                ),
            ),
            // 27: "So, the case summary for the exam?"
            Scene(
                27, "Case closed", exam(),
                beats = listOf(
                    Beat(28, show = all6, light = all6, card = takeaways),
                    Beat(30, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-developers--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the app team this week?..."
            Scene(
                0, "What a Developer is", builders(),
                beats = listOf(
                    Beat(0, card = BoardCard("Developers", "The week everyone found out what the word means.")),
                    Beat(2, light = allBuilders, card = BoardCard("Any aspect", "Anyone creating any part of a usable Increment each Sprint.")),
                ),
            ),
            // 3: "And what do they answer for?"
            Scene(
                3, "Four accountabilities", four(),
                beats = listOf(
                    Beat(4, light = allFour, card = fourCard),
                ),
            ),
            // 5: "Who was Monday?"
            Scene(
                5, "Monday: Marcus", sprintBacklog(),
                beats = listOf(
                    Beat(6, show = setOf("goal", "items", "plan"), light = setOf("plan"), card = BoardCard("Written on Sunday night", "Tasks, names and hours, emailed at seven.")),
                    Beat(8, show = setOf("who"), light = setOf("who"), card = BoardCard("By and for the Developers", "Sprint Goal, items and plan. Nobody tells them how.")),
                ),
            ),
            // 9: "Did Marcus also set how much they'd take on?"
            Scene(
                9, "Forty story points", whatHow(),
                beats = listOf(
                    Beat(10, light = setOf("devs", "mgr"), pulse = "pb" to "devs", card = BoardCard("How much", "The Developers select, with the Product Owner. Points: optional.")),
                ),
            ),
            // 11: "Tuesday?"
            Scene(
                11, "Tuesday: Tom", done(),
                beats = listOf(
                    Beat(12, show = setOf("work", "dod"), light = setOf("dod"), card = BoardCard("Skipped", "No review, no old-phone tests. Tom had a date.")),
                    Beat(14, show = setOf("inc", "pb"), light = setOf("pb"), pulse = "dod" to "pb", card = BoardCard("Not done", "Not part of the Increment; back to the Product Backlog.")),
                ),
            ),
            // 15: "Wednesday?"
            Scene(
                15, "Wednesday: Priya", adapt(),
                beats = listOf(
                    Beat(16, show = setOf("goal", "daily", "plan"), light = setOf("plan"), card = BoardCard("Half of sync, cut quietly", "Twice as big as planned, and she told nobody.")),
                    Beat(18, show = setOf("po", "quality"), light = setOf("po", "plan"), pulse = "po" to "plan", card = BoardCard("Adapt together", "At the Daily Scrum; scope renegotiated with the Product Owner.")),
                    Beat(20, light = setOf("goal"), pulse = "plan" to "goal", card = BoardCard("Same goal", "Smaller scope; nobody surprised at the Sprint Review.")),
                ),
            ),
            // 21: "Thursday. The designer storming out?"
            Scene(
                21, "Thursday: Dana", builders(),
                beats = listOf(
                    Beat(22, light = setOf("design"), card = BoardCard("I'm not a Developer", "Dana walked out of Sprint Planning.")),
                    Beat(24, light = setOf("design", "note"), card = BoardCard("Scrum says she is", "An accountability, not a job title.")),
                ),
            ),
            // 25: "And Friday? There's always a Friday."
            Scene(
                25, "Friday: the intern", four(),
                beats = listOf(
                    Beat(26, light = setOf("each"), card = BoardCard("Said nothing", "Tom is senior, thought the intern.")),
                    Beat(28, card = BoardCard("Hold each other accountable", "Peers keep the standard, kindly, the same day.")),
                    Beat(30, light = setOf("plan", "quality"), card = frameCard),
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
        "psm-developers--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "The people who build it", builders(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The people who build the thing. Probably you.")),
                    Beat(2, light = allBuilders, card = BoardCard("A capital D", "Anyone creating any part of a usable Increment.")),
                    Beat(4, light = setOf("design", "test", "note"), card = BoardCard("Six Developers", "An accountability, not a job title.")),
                ),
            ),
            // 5: "Accountability. That's a heavy word for this hour."
            Scene(
                5, "Four things", four(),
                beats = listOf(
                    Beat(6, light = allFour, card = fourCard),
                ),
            ),
            // 7: "The plan first. Who writes it?"
            Scene(
                7, "Their plan", sprintBacklog(),
                beats = listOf(
                    Beat(8, light = setOf("who"), card = BoardCard("Sprint Backlog", "Sprint Goal, items and plan: by and for the Developers.")),
                    Beat(10, light = setOf("plan"), card = BoardCard("Why it matters", "Those doing the work see the problems first.")),
                    Beat(12, light = setOf("goal"), pulse = "items" to "goal", card = BoardCard("Sprint Goal", "The single objective for the Sprint.")),
                ),
            ),
            // 13: "Who says how much fits in a Sprint?"
            Scene(
                13, "What and how", whatHow(),
                beats = listOf(
                    Beat(14, show = setOf("po", "pb", "devs"), light = setOf("devs"), pulse = "pb" to "devs", card = BoardCard("How much", "The Developers, with the Product Owner. Not a manager's number.")),
                    Beat(16, show = setOf("inc", "mgr"), light = setOf("devs", "inc"), pulse = "devs" to "inc", card = BoardCard("How: theirs alone", "What is the Product Owner's; how is the Developers'.")),
                ),
            ),
            // 17: "Quality, then."
            Scene(
                17, "Quality", done(),
                beats = listOf(
                    Beat(18, show = setOf("work", "dod", "inc"), light = setOf("dod"), pulse = "dod" to "inc", card = BoardCard("Definition of Done", "A formal, shared bar of quality. Built in, not checked at the end.")),
                    Beat(20, show = setOf("pb"), light = setOf("pb"), pulse = "dod" to "pb", card = BoardCard("Not done", "Even on a tired Thursday: back to the Product Backlog.")),
                ),
            ),
            // 21: "Adapting every day. How?"
            Scene(
                21, "Every day", adapt(),
                beats = listOf(
                    Beat(22, show = setOf("goal", "daily", "plan"), light = setOf("daily", "plan"), pulse = "daily" to "plan", card = BoardCard("Daily Scrum", "Fifteen minutes for the Developers; not a status report.")),
                    Beat(24, show = setOf("po", "quality"), light = setOf("po", "plan"), pulse = "po" to "plan", card = BoardCard("Twice as big?", "Renegotiate scope with the Product Owner; the goal stays.")),
                ),
            ),
            // 25: "And the last one. Holding each other accountable."
            Scene(
                25, "Each other", four(),
                beats = listOf(
                    Beat(26, light = setOf("each"), card = BoardCard("Hold each other accountable", "Peers keep the standard, kindly, the same day.")),
                    Beat(28, card = BoardCard("The senior goes first", "Invite it, and thank whoever finds something.")),
                    Beat(30, light = setOf("plan", "quality"), card = frameCard),
                    Beat(32, card = BoardCard("Two hats", "A Product Owner or Scrum Master doing Sprint work is a Developer.")),
                ),
            ),
            // 33: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-developers--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Tonight: Developers", builders(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Tonight: Developers. You.")),
                    Beat(2, card = BoardCard("Any aspect", "Anyone creating any part of a usable Increment.")),
                    Beat(4, card = BoardCard("Quiz", "Six people on a team. Who are the Developers?")),
                    Beat(5, light = allBuilders + "note", card = BoardCard("All six", "An accountability, not a job title.")),
                ),
            ),
            // 7: "And what do Developers answer for?"
            Scene(
                7, "Four things", four(),
                beats = listOf(
                    Beat(8, light = allFour, card = fourCard),
                ),
            ),
            // 9: "Holding each other. I'll take that one personally."
            Scene(
                9, "Quiz: the plan", sprintBacklog(),
                beats = listOf(
                    Beat(10, card = BoardCard("Quiz", "Who creates the Sprint Backlog?")),
                    Beat(11, show = setOf("goal", "items", "plan", "who"), light = setOf("who"), card = BoardCard("The Developers", "Sprint Goal, items and plan, by and for them.")),
                    Beat(14, light = setOf("plan"), pulse = "plan" to "goal", card = BoardCard("Why it matters", "Those doing the work see the problems first.")),
                ),
            ),
            // 16: "The single objective for the Sprint... Next quiz."
            Scene(
                16, "Quiz: forty points", whatHow(),
                beats = listOf(
                    Beat(16, show = setOf("po", "pb", "devs", "mgr"), light = setOf("mgr"), card = BoardCard("Quiz: forty points", "Who decides how much the team takes on?")),
                    Beat(18, light = setOf("devs", "pb"), pulse = "pb" to "devs", card = BoardCard("No kiss: the Developers", "With the Product Owner. Story points aren't in the Guide.")),
                    Beat(20, show = setOf("inc"), light = setOf("devs", "inc"), pulse = "devs" to "inc", card = BoardCard("How: the Developers", "The Product Owner owns what; the Developers own how.")),
                ),
            ),
            // 21: "Then let me win you back. What's the Definition of Done?"
            Scene(
                21, "Quiz: done?", done(),
                beats = listOf(
                    Beat(22, show = setOf("work", "dod"), light = setOf("dod"), pulse = "work" to "dod", card = BoardCard("Quiz: release tomorrow", "Tests unfinished. Can you call it done?")),
                    Beat(23, show = setOf("inc", "pb"), light = setOf("pb"), pulse = "dod" to "pb", card = BoardCard("No: back to the backlog", "Not part of the Increment, and can't be released.")),
                ),
            ),
            // 25: "And adapting every day?"
            Scene(
                25, "Quiz: twice as big", adapt(),
                beats = listOf(
                    Beat(26, show = setOf("goal", "daily", "plan"), light = setOf("daily", "plan"), pulse = "daily" to "plan", card = BoardCard("Daily Scrum", "Fifteen minutes for the Developers to adapt the plan.")),
                    Beat(27, card = BoardCard("Quiz", "Halfway through, the work is twice as big. Now what?")),
                    Beat(29, show = setOf("po", "quality"), light = setOf("po", "plan"), pulse = "po" to "plan", card = BoardCard("Renegotiate the scope", "With the Product Owner; the Sprint Goal stays; quality holds.")),
                ),
            ),
            // 31: "And holding each other accountable?"
            Scene(
                31, "Each other", four(),
                beats = listOf(
                    Beat(32, light = setOf("each"), card = BoardCard("Hold each other accountable", "Peers keep the standard, kindly, the same day.")),
                    Beat(34, card = BoardCard("The senior goes first", "Invite it, and thank whoever finds something.")),
                    Beat(36, light = setOf("plan", "quality"), card = frameCard),
                    Beat(38, card = BoardCard("Two hats", "A Product Owner doing Sprint work takes part as a Developer.")),
                ),
            ),
            // 39: "Then the last question, for the exam."
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = all6, card = takeaways),
                    Beat(43, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
