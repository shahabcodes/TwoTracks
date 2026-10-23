package com.shahaabapps.core.content

/** The Scrum Team: one team, self-managing and cross-functional: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesScrumTeam {
    private val classic = Podcast(
        "psm-scrum-team",
        listOf(
            maya("Hi, and welcome back. Today: the Scrum Team. Who's on it, how big it is, and who's in charge. That last one has a surprising answer, and the P S M one assessment asks about it a lot."),
            leo("Good, because I've been on three teams that called themselves Scrum teams, and they looked nothing alike. What is a Scrum Team, in plain words?"),
            maya("A small group of people who together build a product, one Sprint at a time. The Scrum Guide calls it the fundamental unit of Scrum. Inside it there are exactly three accountabilities: one Scrum Master, one Product Owner, and Developers."),
            leo("Accountabilities. Why not just say roles?"),
            maya("The older Guide did say roles. The 2020 Guide changed it to accountabilities, which means what you answer for. The Product Owner answers for the value of the product. The Scrum Master answers for the team's effectiveness and for Scrum being done as the Guide describes. The Developers answer for building a usable piece each Sprint."),
            leo("So give me a real team. An Android one."),
            maya("Picture a team building a fitness app. One Product Owner, who decides what's most valuable. One Scrum Master, who helps the team and the company use Scrum well. And seven Developers: three Android developers, a backend developer, a designer, a tester, and someone who looks after releases to the Play Store."),
            leo("Wait. The designer and the tester are Developers? They don't write Kotlin."),
            maya("In Scrum, a Developer is anyone on the team who builds any part of the usable product each Sprint. Code, design, tests, release work: all of it. The word isn't a job title; it's an accountability. The Developers episode goes deeper."),
            leo("And who's the boss? Who assigns the tickets?"),
            maya("[pause] Nobody inside the team. The Scrum Guide says that within a Scrum Team there are no sub-teams and no hierarchies. No lead who hands out work, no testing team inside the team, no backend team inside the team. One team, focused on one objective at a time, called the Product Goal."),
            leo("Product Goal. Quick version?"),
            maya("The longer-term objective for the product, like: runners can train for a race entirely in our app. The team works toward one Product Goal at a time. The Product Backlog episode covers it properly."),
            leo("But somebody has to decide who does what."),
            maya("The team does. That's self-managing: the Scrum Guide says the team decides internally who does what, when, and how. The older Guide said self-organising; the 2020 one says self-managing, which goes a bit further. The team doesn't just arrange the work; it manages it."),
            leo("Honestly, that sounds like chaos. My last team needed a lead to keep things moving."),
            maya("That's the developer's real doubt, and fair. But think about the alternative. A lead assigns tickets; everyone waits for the lead; the lead is the bottleneck, the one narrow point everything queues behind. A self-managing team picks up the most important work together, and the Daily Scrum is where they adjust the plan each day."),
            leo("Does self-managing mean we can do anything we like?"),
            maya("No, and that's where it stops. The team manages its own work, but inside a frame. The Product Owner orders the Product Backlog, so the team doesn't decide what's most valuable. The Sprint Goal sets the target. And the organisation structures and empowers the team, which means giving it the room to decide."),
            leo("Next word. Cross-functional."),
            maya("Cross-functional means the team, together, has all the skills it needs to create value every Sprint. For example, if every release needs a security review, and only someone outside the team can do it, the team isn't really cross-functional. Work waits in someone else's queue."),
            leo("So every single person has to know everything? Android, backend, design?"),
            maya("No. The team as a whole has the skills, not each person. You can be the Kotlin expert. But nobody outside the team should be needed to turn an idea into something usable. And people do learn from each other, which helps when someone's on holiday."),
            leo("How big is small? My company has a twenty-person team."),
            maya("The Scrum Guide says small enough to stay nimble and large enough to finish significant work in a Sprint, typically 10 or fewer people. Smaller teams communicate better and get more done. Twenty people in one Daily Scrum is a crowd, not a conversation."),
            leo("So what do we do with twenty?"),
            maya("Reorganise into several cohesive Scrum Teams, each focused on the same product. And here's the exam detail: they share the same Product Goal, the same Product Backlog and the same Product Owner. Not one Product Owner each. One product, one Product Owner."),
            leo("Who's accountable if a Sprint goes badly? The Developers?"),
            maya("The whole Scrum Team. The Guide says the entire Scrum Team is accountable for creating a valuable, useful Increment every Sprint. And the team is responsible for everything about the product: working with stakeholders, testing, maintenance, running it, experiments, research. Not just writing features."),
            leo("Can one person wear two hats? Our Scrum Master also writes code."),
            maya("Yes. A Scrum Master can also be a Developer, and the Guide says that when the Product Owner or the Scrum Master actively work on items in the Sprint, they take part as Developers. The accountabilities stay distinct, even when one person holds two. The Scrum Master episode covers that job."),
            leo("So what do I remember for the exam?"),
            maya("If a question asks who is on a Scrum Team: one Scrum Master, one Product Owner, and Developers, with no sub-teams and no hierarchies. Cross-functional means all the skills to create value each Sprint. Self-managing means the team decides who does what, when and how. Typically 10 or fewer. Too big: several Scrum Teams sharing one Product Goal, one Product Backlog and one Product Owner."),
            leo("And anything to try today?"),
            maya("List the skills needed to ship one feature of your app to the Play Store. Mark any skill that sits outside your team and causes a hand-off. Note who decides who works on what today. Then read the Scrum Team section of the Scrum Guide."),
            leo("One team, no bosses inside it. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-scrum-team",
        listOf(
            maya("Welcome to Case Files. Today's case: The Feature That Bounced. An Android team built offline mode for their running app. It took three months to reach users. The code itself was finished in three weeks. Where did the other two months go?"),
            leo("[chuckle] Detective Leo, on the trail of two missing months. First suspect: not enough people. Add more developers."),
            maya("Wrong suspect, and you'll see why. The team already had sixteen people. The exam would look at the shape of the team. So, first: what should a Scrum Team look like? The Scrum Guide calls it the fundamental unit of Scrum: one Scrum Master, one Product Owner, and Developers."),
            leo("Those are the roles?"),
            maya("Accountabilities, since the 2020 Guide: what each one answers for. The Product Owner answers for the value of the product. The Scrum Master for the team's effectiveness and for Scrum being used as the Guide describes. The Developers for building a usable piece of the product each Sprint, called an Increment."),
            leo("Okay. First clue?"),
            maya("The org chart. Inside this so-called team were three groups: the Android group, the backend group, and a testing group. Offline mode went from Android to backend, then to testing, then back again when tests failed. Each hand-off waited in a queue."),
            leo("So, sub-teams."),
            maya("Exactly. The Scrum Guide says that within a Scrum Team there are no sub-teams and no hierarchies. One team, focused on one objective at a time, the Product Goal, the product's longer-term objective. Three groups passing work over a wall is three teams pretending to be one."),
            leo("Second suspect, then. The Scrum Master. They should have been assigning the work and chasing the hand-offs."),
            maya("[pause] Also wrong. The Scrum Master doesn't assign work. The team is self-managing: the Guide says it decides internally who does what, when and how. The older Guide said self-organising; the 2020 one says self-managing, a step further. Second clue: the lead developer."),
            leo("What did the lead do?"),
            maya("Assigned every ticket, every morning. People waited for him. When he was in meetings, nothing moved. He'd become the bottleneck, the narrow point everything queued behind."),
            leo("So a hierarchy inside the team."),
            maya("A hierarchy. Under the picture, self-managing works because the people closest to the work decide, and the Daily Scrum is where the Developers adjust their plan each day. Nobody waits for permission."),
            leo("Does self-managing have limits, though?"),
            maya("It does. The team manages its own work inside a frame. The Product Owner orders the Product Backlog, the ordered list of what the product needs, so the team doesn't pick what's most valuable. The Sprint Goal sets the target. And the Guide says the organisation structures and empowers the team to manage itself."),
            leo("Third clue?"),
            maya("The security review. Every release needed one, and only a person in another department could do it. Two weeks in her queue, every time."),
            leo("So the team couldn't finish without outsiders. That's cross-functional, isn't it?"),
            maya("It is. Cross-functional means the team, together, has all the skills needed to create value every Sprint. Not each person knowing everything; the team as a whole. A team that can't finish without outsiders isn't cross-functional, and its work waits in someone else's queue."),
            leo("And the size? Sixteen people."),
            maya("Fourth clue: their Daily Scrum took forty minutes. The Guide says a Scrum Team is small enough to stay nimble and large enough to finish significant work in a Sprint, typically 10 or fewer. Smaller teams communicate better and get more done."),
            leo("So split them. Two teams, two Product Owners?"),
            maya("Half right, and that's the trap. When a team is too large, the Guide says reorganise into several cohesive Scrum Teams, each focused on the same product, sharing the same Product Goal, the same Product Backlog and the same Product Owner. One product, one Product Owner."),
            leo("And who carried the blame for the three months?"),
            maya("That was the last clue. Each group blamed the next. But the Guide says the entire Scrum Team is accountable for creating a valuable, useful Increment every Sprint, and for everything about the product: stakeholders, testing, maintenance, running it. Not my part, your part."),
            leo("So what changed?"),
            maya("Two Scrum Teams of eight, each with Android, backend and testing skills inside, one shared Product Owner and Product Backlog. The security reviewer trained two people per team. The lead became one Developer among many. Next feature: three weeks, start to Play Store. The Developers episode picks up from here."),
            leo("So, the case summary for the exam?"),
            maya("A Scrum Team is one Scrum Master, one Product Owner and Developers, with no sub-teams and no hierarchies. Cross-functional means all the skills to create value each Sprint. Self-managing means the team decides who does what, when and how. Typically 10 or fewer. Too big: several Scrum Teams sharing one Product Goal, one Product Backlog and one Product Owner."),
            leo("Case closed. And to try today?"),
            maya("List the skills needed to ship one feature of your app to the Play Store. Mark any skill that sits outside your team and causes a hand-off. Note who decides who works on what today. Then read the Scrum Team section of the Scrum Guide."),
            leo("Count the hand-offs, find the missing months. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-scrum-team",
        listOf(
            maya("Okay, did you hear what happened on the app team this week? They reorganised into a proper Scrum Team. And everybody, and I mean everybody, had an opinion about how."),
            leo("[laugh] I heard Tom made himself a new business card. Start at the start. What's a Scrum Team supposed to be?"),
            maya("The Scrum Guide calls it the fundamental unit of Scrum. One Scrum Master, one Product Owner, and Developers. Those three are called accountabilities, which means what each one answers for. The 2020 Guide stopped calling them roles."),
            leo("Answers for what, each of them?"),
            maya("The Product Owner, for the value of the product. The Scrum Master, for the team's effectiveness and for Scrum being done as the Guide describes. The Developers, for building a usable piece of the product every Sprint. Now. Monday. Tom."),
            leo("The business card."),
            maya("Lead Developer, it said. And from Monday he assigned every ticket, every morning. By Wednesday everyone was waiting for Tom, and Tom was in meetings."),
            leo("Classic Tom. What's the rule?"),
            maya("The Guide says within a Scrum Team there are no sub-teams and no hierarchies. And the team is self-managing: it decides internally who does what, when and how. Under the picture, the people closest to the work decide, and they adjust the plan each day at the Daily Scrum. No queue in front of Tom's desk."),
            leo("But surely someone senior helps the juniors?"),
            maya("Of course. People help each other, and experience counts. But help isn't hierarchy. The senior developer pairs with the junior one; he doesn't hand her tickets. Nobody inside the team is anybody else's boss."),
            leo("Tuesday?"),
            maya("Tuesday was Priya. She set up a testing sub-team, inside the Scrum Team, with its own board and its own stand-up. Very organised."),
            leo("Sounds tidy, though."),
            maya("Tidy, and not allowed. No sub-teams. The team should be cross-functional: together it has all the skills it needs to create value every Sprint. Not each person knowing everything; the team as a whole. Testing is part of the team's work, not a separate stop on the line."),
            leo("So now everyone has to test?"),
            maya("Everyone can help, and specialists are fine. What's gone is the wall between building and testing. The usable piece of the product isn't finished until it's tested, and it's the same team that builds it and tests it."),
            leo("Wednesday?"),
            maya("Marcus. Marcus got approval to grow the team to eighteen people. Bigger team, more done, he said. The first Daily Scrum took fifty minutes and nobody remembered a word of it."),
            leo("[chuckle] So how big is allowed?"),
            maya("The Guide says small enough to stay nimble and large enough to finish significant work in a Sprint, typically 10 or fewer people. Smaller teams communicate better and get more done. Eighteen is a crowd."),
            leo("So split Marcus's crowd in two."),
            maya("Which brings us to Thursday, and Dana. Dana split them into two Scrum Teams, lovely, and then asked for a second Product Owner, a second backlog and a second goal. One each, she said, for fairness."),
            leo("That sounds fair."),
            maya("It sounds fair, and it's the exam's favourite trap. The Guide says several Scrum Teams on one product share the same Product Goal, the same Product Backlog and the same Product Owner. The Product Goal is the product's longer-term objective. One product, one goal, one backlog, one Product Owner."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. The release broke, and the intern said: well, that's the Developers' problem, I'm on the design side."),
            leo("But the designer is a Developer, isn't she?"),
            maya("In Scrum, yes. A Developer is anyone who builds any part of the usable product: code, design, tests, release work. And the Guide says the entire Scrum Team is accountable for creating a valuable, useful Increment every Sprint, the Increment being the usable piece. Not my part, your part."),
            leo("And the department head? I heard he had views."),
            maya("He did. He said self-managing means the team can build whatever it fancies. It doesn't. The team decides who does what, when and how, and the Developers select the Sprint's items with the Product Owner. But the Product Owner orders the Product Backlog, the ordered list of what the product needs, and the Sprint Goal sets the target. The team manages its own work inside that frame."),
            leo("So what should he do instead?"),
            maya("Give the team room. The Guide says the organisation structures and empowers the team to manage its own work. Then trust it, and come to the Sprint Review to see the results."),
            leo("Can anyone wear two hats, then? Our Scrum Master codes too."),
            maya("Yes. A Scrum Master can also be a Developer. The Guide says that when the Product Owner or Scrum Master work on items in the Sprint, they take part as Developers. The Scrum Master episode is next, if you want more of this week."),
            leo("So, the gossip I should remember for the exam?"),
            maya("A Scrum Team is one Scrum Master, one Product Owner and Developers, with no sub-teams and no hierarchies. Cross-functional means all the skills to create value each Sprint. Self-managing means the team decides who does what, when and how. Typically 10 or fewer. Too big: several Scrum Teams sharing one Product Goal, one Product Backlog and one Product Owner."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("List the skills needed to ship one feature of your app to the Play Store. Mark any skill that sits outside your team and causes a hand-off. Note who decides who works on what today. Then read the Scrum Team section of the Scrum Guide."),
            leo("Tear up the card, Tom. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-scrum-team",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning for P S M one. [pause] Tonight: a small team, with nobody in charge of it. And somehow, it works."),
            ivy("Nobody in charge? That sounds like my student house, Nova."),
            nova("[laugh] Better organised, I promise. It's the Scrum Team. The Scrum Guide calls it the fundamental unit of Scrum. One Scrum Master, one Product Owner, and Developers."),
            ivy("Three roles."),
            nova("Accountabilities, since 2020. What each one answers for. [pause] The Product Owner answers for the value of the product. The Scrum Master, for the team's effectiveness and for Scrum done as the Guide describes. The Developers, for a usable piece of the product every Sprint."),
            ivy("Why change the word, after all those years?"),
            nova("A role can sound like a costume you put on. [pause] An accountability is what you answer for, when things go well and when they don't. And watch for the old words in a trap: roles, self-organising, or a Development Team inside the team."),
            ivy("Paint me a real team."),
            nova("Picture a team building a running app for Android. One Product Owner. One Scrum Master. And seven Developers: three Android developers, one for the backend, a designer, a tester, and someone who looks after releases to the Play Store."),
            ivy("The designer's a Developer? She'd laugh at that."),
            nova("In Scrum, a Developer is anyone who builds any part of the usable product. Code, designs, tests, releases. It's an accountability, not a job title. The Developers episode is all theirs."),
            ivy("And who's the boss, in the dark?"),
            nova("[pause] Nobody, inside the team. The Guide says within a Scrum Team there are no sub-teams and no hierarchies. No lead handing out tickets. No testing team inside the team. One team, one objective at a time: the Product Goal, the product's longer-term aim."),
            ivy("Then who decides who does what?"),
            nova("They do. That's self-managing. The team decides internally who does what, when, and how. The older Guide said self-organising. Self-managing goes a little further: they don't just arrange the work, they manage it."),
            ivy("Doesn't that fall apart without a lead?"),
            nova("It's the honest doubt. But with a lead, everyone waits for the lead, and the lead becomes the bottleneck, the narrow point everything queues behind. Without one, the people closest to the work decide, and at the Daily Scrum they adjust the plan, every day."),
            ivy("Can they decide anything at all, then?"),
            nova("No. [pause] That's the edge of it. The Product Owner orders the Product Backlog, the ordered list of what the product needs. The Sprint Goal sets the target. And the organisation gives the team its structure and the room to decide."),
            ivy("Cross-functional. I've heard that one too."),
            nova("It means the team, together, has every skill it needs to create value each Sprint. Not each person knowing everything. Just nobody outside the team needed to turn an idea into something usable. If every release waits two weeks for an outside reviewer, [pause] the team isn't cross-functional yet."),
            ivy("So I can stay the Kotlin one?"),
            nova("You can stay the Kotlin one. People specialise. [pause] People also learn from each other, so when you're away on holiday, the release still goes out."),
            ivy("And how many fit around the table?"),
            nova("Small enough to stay nimble, large enough to finish real work in a Sprint. Typically 10 or fewer, the Guide says. Smaller teams talk better and get more done."),
            ivy("And if the company keeps adding people?"),
            nova("Then reorganise into several cohesive Scrum Teams, each on the same product. And this is the line the exam loves: they share the same Product Goal, the same Product Backlog and the same Product Owner. [pause] One product. One Product Owner."),
            ivy("When it goes wrong, whose fault is it?"),
            nova("The whole team's. The Guide says the entire Scrum Team is accountable for a valuable, useful Increment every Sprint. And responsible for everything about the product: stakeholders, testing, keeping it running, experiments. Not my part, your part."),
            ivy("Can one person hold two of those accountabilities?"),
            nova("Yes. A Scrum Master can also be a Developer. When the Product Owner or the Scrum Master work on items in the Sprint, the Guide says, they take part as Developers. [pause] The Scrum Master gets their own late night soon."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("A Scrum Team is one Scrum Master, one Product Owner and Developers, with no sub-teams and no hierarchies. Cross-functional means all the skills to create value each Sprint. Self-managing means the team decides who does what, when and how. Typically 10 or fewer. Too big: several Scrum Teams sharing one Product Goal, one Product Backlog and one Product Owner."),
            ivy("And something to try today, before I sleep?"),
            nova("List the skills needed to ship one feature of your app to the Play Store. [pause] Mark any skill that sits outside your team and causes a hand-off. Note who decides who works on what today. Then read the Scrum Team section of the Scrum Guide."),
            ivy("One small team. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-scrum-team",
        listOf(
            nova("Tonight is date night, darling. And tonight it's all about knowing your role."),
            rafe("My role? I thought I was the charming one."),
            nova("[laugh] Careful, love. Scrum stopped saying roles in 2020. It says accountabilities: what you answer for. Tonight's topic is the Scrum Team, and the Scrum Guide calls it the fundamental unit of Scrum."),
            rafe("A team of two, like us?"),
            nova("A little bigger. One Scrum Master, one Product Owner, and Developers. The Product Owner answers for the value of the product. The Scrum Master for the team's effectiveness and for Scrum done as the Guide describes. The Developers for building a usable piece of the product every Sprint."),
            rafe("Then quiz me. Exam style."),
            nova("Picture a running app team: three Android developers, a backend developer, a designer and a tester, plus a Product Owner and a Scrum Master. How many Developers does the team have?"),
            rafe("Six. The designer and the tester count too. A Developer is anyone who builds any part of the usable product, code or not."),
            nova("Right answer. You count everyone in. [kiss]"),
            rafe("And the old Development Team? Is that still a thing?"),
            nova("Not since 2020. There's one Scrum Team with Developers inside it, and no team within the team. If an answer says Development Team, or self-organising, it's using the older words."),
            rafe("And who's in charge? Between us, I mean. On the team."),
            nova("Nobody inside the team. The Guide says within a Scrum Team there are no sub-teams and no hierarchies. No lead handing out tickets, no testing team inside the team. One team, focused on one objective at a time, the Product Goal, the product's longer-term aim."),
            rafe("No hierarchy between us. I like that. So who decides who does what?"),
            nova("That's my question for you. Who decides, inside the team, who does what, when and how?"),
            rafe("The team itself. It's self-managing. The older Guide said self-organising; 2020 says self-managing, which goes a little further."),
            nova("Right answer. Managing yourself so well. [kiss]"),
            rafe("But surely a team needs a lead, or it all falls apart?"),
            nova("With a lead, everyone waits for the lead, and the lead becomes the bottleneck, the narrow point everything queues behind. Without one, the people closest to the work decide, and they adjust the plan at the Daily Scrum, every day."),
            rafe("And can a self-managing team do whatever it likes?"),
            nova("No, and that's where it stops. The Product Owner orders the Product Backlog, the ordered list of what the product needs. The Sprint Goal sets the target. The team manages its own work inside that frame."),
            rafe("Then here's one for you. What does cross-functional mean?"),
            nova("The team, together, has all the skills it needs to create value every Sprint. Not every person knowing everything; the team as a whole. If every release waits two weeks for an outside reviewer, it isn't cross-functional yet."),
            rafe("Right answer. You've got all the skills I need. [kiss]"),
            nova("Behave. You know I can't do backend, though."),
            rafe("You don't have to. People specialise; the team as a whole has the skills. And people learn from each other, so the release still goes out when someone's on holiday."),
            nova("Now size. How big should a Scrum Team be?"),
            rafe("As big as possible. More people, more done."),
            nova("No kiss for that one. Small enough to stay nimble, large enough to finish significant work in a Sprint: typically 10 or fewer. Smaller teams communicate better and get more done. Twenty people in one Daily Scrum is a crowd."),
            rafe("Fine. Then my comeback question. The company has twenty-five developers on one app. What does the Guide say to do?"),
            nova("Reorganise into several cohesive Scrum Teams on the same product, sharing the same Product Goal, the same Product Backlog and the same Product Owner. Not one Product Owner each."),
            rafe("Right answer. One product, one Product Owner, and one of you. [kiss]"),
            nova("And when a Sprint goes badly, who's accountable?"),
            rafe("The whole Scrum Team. The Guide says the entire team is accountable for a valuable, useful Increment every Sprint, the Increment being the usable piece. And for everything about the product, from stakeholders to testing to keeping it running."),
            nova("Even the designer, for a crash in the backend?"),
            rafe("Even the designer. The Increment is everyone's. Not my part, your part; our part."),
            nova("And can one person hold two accountabilities?"),
            rafe("Yes. A Scrum Master can also be a Developer. When the Product Owner or Scrum Master work on items in the Sprint, they take part as Developers. The Scrum Master episode is our next date."),
            nova("Then the last question, for the exam. What do we remember?"),
            rafe("A Scrum Team is one Scrum Master, one Product Owner and Developers, with no sub-teams and no hierarchies. Cross-functional means all the skills to create value each Sprint. Self-managing means the team decides who does what, when and how. Typically 10 or fewer. Too big: several Scrum Teams sharing one Product Goal, one Product Backlog and one Product Owner."),
            nova("Perfect, darling. That earns a long one. [kiss]"),
            rafe("Mm. And something to try today?"),
            nova("List the skills needed to ship one feature of your app to the Play Store. Mark any skill that sits outside your team and causes a hand-off. Note who decides who works on what today. Then read the Scrum Team section of the Scrum Guide."),
            rafe("Small team, no bosses. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun team() = DiagramSpec(
        "One Scrum Team: one Product Owner, one Scrum Master and the Developers, working toward one Product Goal.",
        nodes = listOf(
            DNode("po", "Product Owner", 0.25f, 0.14f, w = 0.42f, h = 0.2f, tone = 1, sub = "the product's value"),
            DNode("sm", "Scrum Master", 0.75f, 0.14f, w = 0.42f, h = 0.2f, tone = 1, sub = "effectiveness"),
            DNode("devs", "Developers", 0.5f, 0.5f, w = 0.9f, h = 0.28f, sub = "a usable Increment every Sprint"),
            DNode("goal", "Product Goal", 0.5f, 0.86f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "one at a time"),
        ),
        edges = listOf(
            DEdge("po", "devs"),
            DEdge("sm", "devs"),
            DEdge("devs", "goal", "toward"),
        ),
        aspect = 0.8f,
    )

    private fun devs() = DiagramSpec(
        "Everyone who builds any part of the usable product is a Developer; there are no sub-teams inside the team.",
        nodes = listOf(
            DNode("android", "Android, times 3", 0.25f, 0.18f, w = 0.42f, h = 0.2f),
            DNode("backend", "Backend", 0.75f, 0.18f, w = 0.42f, h = 0.2f),
            DNode("design", "Design", 0.25f, 0.5f, w = 0.42f, h = 0.2f),
            DNode("test", "Testing", 0.75f, 0.5f, w = 0.42f, h = 0.2f),
            DNode("release", "Releases", 0.25f, 0.82f, w = 0.42f, h = 0.2f),
            DNode("note", "All Developers; no sub-teams", 0.75f, 0.82f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
        ),
        aspect = 0.8f,
    )

    private fun lead() = DiagramSpec(
        "A lead who assigns work becomes the queue; a self-managing team decides for itself, inside the Product Owner's order and the Sprint Goal.",
        nodes = listOf(
            DNode("lead", "A lead", 0.25f, 0.16f, w = 0.42f, h = 0.2f, tone = 2, sub = "assigns tickets"),
            DNode("queue", "Everyone waits", 0.25f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
            DNode("team", "The team decides", 0.75f, 0.16f, w = 0.42f, h = 0.2f, tone = 1, sub = "who, what, when, how"),
            DNode("daily", "Daily Scrum", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "adjust the plan"),
            DNode("frame", "Inside a frame", 0.5f, 0.85f, w = 0.9f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Product Owner orders; the Sprint Goal is the target"),
        ),
        edges = listOf(
            DEdge("lead", "queue"),
            DEdge("team", "daily"),
        ),
        aspect = 0.8f,
    )

    private fun crossFn() = DiagramSpec(
        "A cross-functional team takes an idea all the way to the Play Store; a hand-off outside means the work waits.",
        nodes = listOf(
            DNode("idea", "Idea", 0.17f, 0.3f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE),
            DNode("team", "Scrum Team", 0.5f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "all the skills"),
            DNode("store", "Play Store", 0.83f, 0.3f, w = 0.3f, h = 0.24f),
            DNode("outside", "Outside reviewer", 0.5f, 0.78f, w = 0.5f, h = 0.2f, tone = 2, sub = "work waits"),
        ),
        edges = listOf(
            DEdge("idea", "team"),
            DEdge("team", "store", "usable"),
            DEdge("team", "outside", "hand-off", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun size() = DiagramSpec(
        "Typically 10 or fewer people; a team too large splits into Scrum Teams that share one goal, backlog and Product Owner.",
        nodes = listOf(
            DNode("big", "One team of twenty", 0.5f, 0.13f, w = 0.7f, h = 0.2f, tone = 2, sub = "a crowd, not a conversation"),
            DNode("t1", "Scrum Team", 0.25f, 0.46f, w = 0.42f, h = 0.2f, tone = 1, sub = "10 or fewer"),
            DNode("t2", "Scrum Team", 0.75f, 0.46f, w = 0.42f, h = 0.2f, tone = 1, sub = "10 or fewer"),
            DNode("shared", "Shared by both", 0.5f, 0.83f, w = 0.9f, h = 0.24f, kind = NodeKind.NOTE, sub = "one Product Goal, Product Backlog and Product Owner"),
        ),
        edges = listOf(
            DEdge("big", "t1"),
            DEdge("big", "t2"),
            DEdge("t1", "shared"),
            DEdge("t2", "shared"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Scrum Team questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("who", "Three accountabilities", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "no sub-teams, no hierarchy"),
            DNode("cross", "Cross-functional", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "all skills, every Sprint"),
            DNode("self", "Self-managing", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "who, what, when, how"),
            DNode("size", "Typically 10 or fewer", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "nimble, yet big enough"),
            DNode("split", "Too big?", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "same goal, backlog, owner"),
            DNode("acc", "Accountable", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the whole Scrum Team"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("who", "cross", "self", "size", "split", "acc")
    private val three = setOf("po", "sm", "devs")
    private val allDevs = setOf("android", "backend", "design", "test", "release")
    private val takeaways = BoardCard("Listen for the phrase", "One Scrum Master, one Product Owner, Developers. No sub-teams. 10 or fewer.")
    private val today = BoardCard("Today", "Skills to ship one feature; hand-offs; who decides; read The Scrum Team.")

    private fun classicBoard() = PodcastBoard(
        "psm-scrum-team",
        listOf(
            // 0: "Hi, and welcome back. Today: the Scrum Team..."
            Scene(
                0, "Who's on the team", team(),
                beats = listOf(
                    Beat(0, card = BoardCard("The Scrum Team", "Who's on it, how big it is, and who's in charge.")),
                    Beat(2, light = three, card = BoardCard("Three accountabilities", "One Scrum Master, one Product Owner, and Developers.")),
                    Beat(4, light = setOf("po", "sm"), card = BoardCard("Not roles", "Accountabilities: what each one answers for.")),
                ),
            ),
            // 5: "So give me a real team. An Android one."
            Scene(
                5, "An Android team", devs(),
                beats = listOf(
                    Beat(6, light = allDevs, card = BoardCard("A fitness app team", "One Product Owner, one Scrum Master, seven Developers.")),
                    Beat(8, light = setOf("design", "test", "release"), card = BoardCard("All of them Developers", "Anyone who builds any part of the usable product.")),
                    Beat(10, light = setOf("note"), card = BoardCard("No sub-teams, no hierarchies", "No lead handing out work. One team.")),
                ),
            ),
            // 11: "Product Goal. Quick version?"
            Scene(
                11, "One goal at a time", team(),
                beats = listOf(
                    Beat(12, show = setOf("goal"), light = setOf("goal"), pulse = "devs" to "goal", card = BoardCard("Product Goal", "The product's longer-term objective, one at a time.")),
                ),
            ),
            // 13: "But somebody has to decide who does what."
            Scene(
                13, "Who decides", lead(),
                beats = listOf(
                    Beat(14, show = setOf("team", "daily"), light = setOf("team"), card = BoardCard("Self-managing", "The team decides who does what, when and how.")),
                    Beat(16, show = setOf("lead", "queue"), light = setOf("lead", "queue"), pulse = "lead" to "queue", card = BoardCard("The bottleneck", "With a lead, everyone waits for the lead.")),
                    Beat(18, show = setOf("frame"), light = setOf("frame"), card = BoardCard("Inside a frame", "The Product Owner orders the backlog; the Sprint Goal is the target.")),
                ),
            ),
            // 19: "Next word. Cross-functional."
            Scene(
                19, "Cross-functional", crossFn(),
                beats = listOf(
                    Beat(20, light = setOf("outside"), pulse = "team" to "outside", card = BoardCard("Cross-functional", "Together, all the skills to create value every Sprint.")),
                    Beat(22, light = setOf("team", "store"), pulse = "team" to "store", card = BoardCard("Not each person", "The team as a whole has the skills; people can specialise.")),
                ),
            ),
            // 23: "How big is small?"
            Scene(
                23, "How big is small", size(),
                beats = listOf(
                    Beat(24, show = setOf("big"), light = setOf("big"), card = BoardCard("Typically 10 or fewer", "Small enough to stay nimble, big enough for real work.")),
                    Beat(26, show = setOf("t1", "t2", "shared"), light = setOf("shared"), pulse = "t1" to "shared", card = BoardCard("Too big? Split it", "Several Scrum Teams share one goal, backlog and Product Owner.")),
                ),
            ),
            // 27: "Who's accountable if a Sprint goes badly?"
            Scene(
                27, "Accountable together", team(),
                beats = listOf(
                    Beat(28, light = three, card = BoardCard("The whole Scrum Team", "Accountable for a valuable, useful Increment every Sprint.")),
                    Beat(30, light = setOf("sm", "devs"), pulse = "sm" to "devs", card = BoardCard("Two hats", "A Scrum Master can also be a Developer.")),
                ),
            ),
            // 31: "So what do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = all6, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-scrum-team--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Feature That Bounced..."
            Scene(
                0, "The Feature That Bounced", crossFn(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Code done in three weeks; users waited three months.")),
                    Beat(2, card = BoardCard("Suspect cleared: headcount", "Sixteen people already. Look at the team's shape.")),
                ),
            ),
            // 3: "Those are the roles?"
            Scene(
                3, "Three accountabilities", team(),
                beats = listOf(
                    Beat(4, light = three, card = BoardCard("Accountabilities", "What each answers for: value, effectiveness, the Increment.")),
                ),
            ),
            // 5: "Okay. First clue?"
            Scene(
                5, "Clue one: the org chart", devs(),
                beats = listOf(
                    Beat(6, light = setOf("android", "backend", "test"), card = BoardCard("Three groups", "Android, backend, testing: work passed over walls.")),
                    Beat(8, light = setOf("note"), card = BoardCard("No sub-teams", "No sub-teams or hierarchies: one team, one Product Goal.")),
                ),
            ),
            // 9: "Second suspect, then. The Scrum Master."
            Scene(
                9, "Clue two: the lead", lead(),
                beats = listOf(
                    Beat(10, show = setOf("team", "daily"), light = setOf("team"), card = BoardCard("Suspect cleared: Scrum Master", "Doesn't assign work. The team self-manages.")),
                    Beat(12, show = setOf("lead", "queue"), light = setOf("lead", "queue"), pulse = "lead" to "queue", card = BoardCard("The bottleneck", "One lead assigned every ticket; nothing moved without him.")),
                    Beat(14, light = setOf("team", "daily"), pulse = "team" to "daily", card = BoardCard("Under the picture", "Those closest to the work decide, and adjust the plan daily.")),
                    Beat(16, show = setOf("frame"), light = setOf("frame"), card = BoardCard("Inside a frame", "The Product Owner orders; the Sprint Goal is the target.")),
                ),
            ),
            // 17: "Third clue?"
            Scene(
                17, "Clue three: the review", crossFn(),
                beats = listOf(
                    Beat(18, light = setOf("outside"), pulse = "team" to "outside", card = BoardCard("Two weeks in a queue", "A security review only an outsider could do.")),
                    Beat(20, light = setOf("team", "store"), pulse = "team" to "store", card = BoardCard("Cross-functional", "Together, all the skills to create value every Sprint.")),
                ),
            ),
            // 21: "And the size? Sixteen people."
            Scene(
                21, "Clue four: sixteen people", size(),
                beats = listOf(
                    Beat(22, show = setOf("big"), light = setOf("big"), card = BoardCard("A forty-minute Daily Scrum", "Typically 10 or fewer: smaller teams talk better.")),
                    Beat(24, show = setOf("t1", "t2", "shared"), light = setOf("shared"), pulse = "t1" to "shared", card = BoardCard("Not two Product Owners", "Teams on one product share goal, backlog and Product Owner.")),
                ),
            ),
            // 25: "And who carried the blame for the three months?"
            Scene(
                25, "The last clue", team(),
                beats = listOf(
                    Beat(26, light = three, card = BoardCard("The whole Scrum Team", "Accountable for a valuable, useful Increment every Sprint.")),
                    Beat(28, light = setOf("devs", "goal"), pulse = "devs" to "goal", card = BoardCard("Two teams of eight", "Skills inside, one Product Owner: three weeks to the Play Store.")),
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
        "psm-scrum-team--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the app team this week?..."
            Scene(
                0, "A team, reorganised", team(),
                beats = listOf(
                    Beat(0, card = BoardCard("The Scrum Team", "Everybody had an opinion about how.")),
                    Beat(2, light = three, card = BoardCard("Three accountabilities", "One Scrum Master, one Product Owner, Developers.")),
                    Beat(4, light = setOf("po", "sm"), card = BoardCard("What each answers for", "Value; effectiveness; a usable piece every Sprint.")),
                ),
            ),
            // 5: "The business card."
            Scene(
                5, "Monday: Tom", lead(),
                beats = listOf(
                    Beat(6, show = setOf("lead", "queue"), light = setOf("lead", "queue"), pulse = "lead" to "queue", card = BoardCard("Lead Developer", "Every ticket assigned by Tom; everyone waited.")),
                    Beat(8, show = setOf("team", "daily", "frame"), light = setOf("team", "daily"), pulse = "team" to "daily", card = BoardCard("Self-managing", "No hierarchy: the team decides who, what, when and how.")),
                    Beat(10, light = setOf("team"), card = BoardCard("Help isn't hierarchy", "Seniors pair with juniors; nobody is anybody's boss.")),
                ),
            ),
            // 11: "Tuesday?"
            Scene(
                11, "Tuesday: Priya", devs(),
                beats = listOf(
                    Beat(12, light = setOf("test"), card = BoardCard("A testing sub-team", "Its own board, its own stand-up.")),
                    Beat(14, light = setOf("note"), card = BoardCard("Cross-functional", "Together, all the skills to create value every Sprint.")),
                    Beat(16, light = allDevs, card = BoardCard("No wall", "The same team builds it and tests it.")),
                ),
            ),
            // 17: "Wednesday?"
            Scene(
                17, "Wednesday and Thursday", size(),
                beats = listOf(
                    Beat(18, show = setOf("big"), light = setOf("big"), card = BoardCard("Eighteen people", "A fifty-minute Daily Scrum nobody remembered.")),
                    Beat(20, card = BoardCard("Typically 10 or fewer", "Small enough to stay nimble, big enough for real work.")),
                    Beat(22, show = setOf("t1", "t2"), light = setOf("t1", "t2"), pulse = "big" to "t1", card = BoardCard("Thursday: Dana", "Two teams, then two Product Owners, for fairness.")),
                    Beat(24, show = setOf("shared"), light = setOf("shared"), pulse = "t2" to "shared", card = BoardCard("One product, one Product Owner", "Shared Product Goal, Product Backlog and Product Owner.")),
                ),
            ),
            // 25: "And Friday? There's always a Friday."
            Scene(
                25, "Friday: the intern", team(),
                beats = listOf(
                    Beat(26, light = setOf("devs"), card = BoardCard("Not my problem", "I'm on the design side, said the intern.")),
                    Beat(28, light = three, card = BoardCard("The whole Scrum Team", "Accountable for a valuable, useful Increment every Sprint.")),
                ),
            ),
            // 29: "And the department head? I heard he had views."
            Scene(
                29, "The department head", lead(),
                beats = listOf(
                    Beat(30, light = setOf("frame"), card = BoardCard("Build whatever we fancy?", "No: Developers pick Sprint items with the PO, but the PO orders the backlog.")),
                    Beat(32, light = setOf("team"), card = BoardCard("Give the team room", "The organisation structures and empowers it.")),
                    Beat(34, card = BoardCard("Two hats", "A Scrum Master can also be a Developer.")),
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
        "psm-scrum-team--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Nobody in charge", team(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "A small team, with nobody in charge of it.")),
                    Beat(2, light = three, card = BoardCard("The Scrum Team", "One Scrum Master, one Product Owner, Developers.")),
                    Beat(4, light = setOf("po", "sm"), card = BoardCard("Accountabilities", "What each one answers for, since 2020.")),
                    Beat(6, card = BoardCard("The old words", "Roles, self-organising, a Development Team: trap answers.")),
                ),
            ),
            // 7: "Paint me a real team."
            Scene(
                7, "A real team", devs(),
                beats = listOf(
                    Beat(8, light = allDevs, card = BoardCard("A running app team", "Seven Developers: Android, backend, design, test, releases.")),
                    Beat(10, light = setOf("design", "note"), card = BoardCard("Developer", "Anyone who builds any part of the usable product.")),
                ),
            ),
            // 11: "And who's the boss, in the dark?"
            Scene(
                11, "Who decides", lead(),
                beats = listOf(
                    Beat(12, card = BoardCard("No sub-teams, no hierarchies", "One team, one Product Goal at a time.")),
                    Beat(14, show = setOf("team", "daily"), light = setOf("team"), card = BoardCard("Self-managing", "Who does what, when and how: the team decides.")),
                    Beat(16, show = setOf("lead", "queue"), light = setOf("lead", "queue"), pulse = "lead" to "queue", card = BoardCard("The bottleneck", "With a lead, everyone waits for the lead.")),
                    Beat(18, show = setOf("frame"), light = setOf("frame"), card = BoardCard("The edge", "The Product Owner orders; the Sprint Goal is the target.")),
                ),
            ),
            // 19: "Cross-functional. I've heard that one too."
            Scene(
                19, "Every skill inside", crossFn(),
                beats = listOf(
                    Beat(20, light = setOf("outside"), pulse = "team" to "outside", card = BoardCard("Cross-functional", "Together, every skill to create value each Sprint.")),
                    Beat(22, light = setOf("team", "store"), pulse = "team" to "store", card = BoardCard("Stay the Kotlin one", "People specialise, and learn from each other.")),
                ),
            ),
            // 23: "And how many fit around the table?"
            Scene(
                23, "Around the table", size(),
                beats = listOf(
                    Beat(24, show = setOf("big"), light = setOf("big"), card = BoardCard("Typically 10 or fewer", "Smaller teams talk better and get more done.")),
                    Beat(26, show = setOf("t1", "t2", "shared"), light = setOf("shared"), pulse = "t1" to "shared", card = BoardCard("One product, one Product Owner", "Shared Product Goal, Product Backlog and Product Owner.")),
                ),
            ),
            // 27: "When it goes wrong, whose fault is it?"
            Scene(
                27, "Whose fault", team(),
                beats = listOf(
                    Beat(28, light = three, card = BoardCard("The whole Scrum Team", "Accountable for a valuable, useful Increment every Sprint.")),
                    Beat(30, light = setOf("sm", "devs"), pulse = "sm" to "devs", card = BoardCard("Two hats", "A Scrum Master can also be a Developer.")),
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
        "psm-scrum-team--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Knowing your role", team(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "It's all about knowing your role.")),
                    Beat(2, card = BoardCard("Accountabilities", "Not roles, since 2020: what you answer for.")),
                    Beat(4, light = three, card = BoardCard("The Scrum Team", "One Scrum Master, one Product Owner, Developers.")),
                ),
            ),
            // 5: "Then quiz me. Exam style."
            Scene(
                5, "Quiz: count the Developers", devs(),
                beats = listOf(
                    Beat(6, card = BoardCard("Quiz", "Android, backend, design, testing: how many Developers?")),
                    Beat(7, light = setOf("android", "backend", "design", "test"), card = BoardCard("Six", "Anyone who builds any part of the usable product.")),
                    Beat(10, light = setOf("note"), card = BoardCard("No Development Team", "One Scrum Team, Developers inside, no team within it.")),
                ),
            ),
            // 11: "And who's in charge? Between us, I mean."
            Scene(
                11, "Quiz: who decides", lead(),
                beats = listOf(
                    Beat(12, card = BoardCard("No hierarchies", "No lead handing out tickets; one Product Goal at a time.")),
                    Beat(14, card = BoardCard("Quiz", "Who decides who does what, when and how?")),
                    Beat(15, show = setOf("team", "daily"), light = setOf("team"), card = BoardCard("Self-managing", "The team itself. A step past self-organising.")),
                    Beat(18, show = setOf("lead", "queue"), light = setOf("lead", "queue"), pulse = "lead" to "queue", card = BoardCard("The bottleneck", "With a lead, everyone waits for the lead.")),
                    Beat(20, show = setOf("frame"), light = setOf("frame"), card = BoardCard("Where it stops", "The Product Owner orders; the Sprint Goal is the target.")),
                ),
            ),
            // 21: "Then here's one for you. What does cross-functional mean?"
            Scene(
                21, "Quiz: cross-functional", crossFn(),
                beats = listOf(
                    Beat(21, card = BoardCard("Quiz", "What does cross-functional mean?")),
                    Beat(22, light = setOf("team", "store"), pulse = "team" to "store", card = BoardCard("All the skills", "Together, everything needed to create value every Sprint.")),
                    Beat(25, light = setOf("team"), card = BoardCard("People specialise", "The team as a whole has the skills.")),
                ),
            ),
            // 26: "Now size. How big should a Scrum Team be?"
            Scene(
                26, "Quiz: size", size(),
                beats = listOf(
                    Beat(26, show = setOf("big"), light = setOf("big"), card = BoardCard("Quiz", "How big should a Scrum Team be?")),
                    Beat(28, card = BoardCard("No kiss: as big as possible?", "Typically 10 or fewer: small enough to stay nimble.")),
                    Beat(29, show = setOf("t1", "t2"), light = setOf("t1", "t2"), pulse = "big" to "t1", card = BoardCard("Quiz: twenty-five developers", "One app. What does the Guide say?")),
                    Beat(30, show = setOf("shared"), light = setOf("shared"), pulse = "t2" to "shared", card = BoardCard("Several Scrum Teams", "Sharing one Product Goal, Product Backlog and Product Owner.")),
                ),
            ),
            // 32: "And when a Sprint goes badly, who's accountable?"
            Scene(
                32, "Accountable together", team(),
                beats = listOf(
                    Beat(33, light = three, card = BoardCard("The whole Scrum Team", "Accountable for a valuable, useful Increment every Sprint.")),
                    Beat(35, light = setOf("devs"), card = BoardCard("Our part", "The Increment is everyone's.")),
                    Beat(37, light = setOf("sm", "devs"), pulse = "sm" to "devs", card = BoardCard("Two hats", "A Scrum Master can also be a Developer.")),
                ),
            ),
            // 38: "Then the last question, for the exam."
            Scene(
                38, "For the exam", exam(),
                beats = listOf(
                    Beat(39, show = all6, light = all6, card = takeaways),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
