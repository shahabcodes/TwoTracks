package com.shahaabapps.core.content

/** Empiricism and lean thinking: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesEmpiricism {
    private val classic = Podcast(
        "psm-empiricism",
        listOf(
            maya("Hi, and welcome back. Today: empiricism, the idea every other part of Scrum stands on. If you're preparing for P S M one, the Professional Scrum Master assessment, this is the floor of the whole house. And it's much simpler than the word."),
            leo("Good, because the word sounds like a philosophy exam. What is it, in plain words?"),
            maya("Empiricism means you learn from what actually happens, not from what you guessed would happen. The Scrum Guide puts it this way: knowledge comes from experience, and decisions are made on what you observe. Try something small, look at the result, change course."),
            leo("Honestly, that sounds like common sense. Every developer does that with a bug. Why does Scrum need a big word for it?"),
            maya("Because most planning doesn't work that way. Picture a team building an Android app, the old way. A six-month plan, every screen designed up front, then build it all and release to the Play Store at the end. The plan is a guess, and nobody checks the guess until month six."),
            leo("And by month six the users wanted something else."),
            maya("Or the backend couldn't take the load, or a competitor shipped first. Scrum is made for complex problems, work where you can't know the answer up front. When you can't know it, the only honest move is to take a small step and look. That's empiricism."),
            leo("So, small steps. How small?"),
            maya("A Sprint: a fixed stretch of one month or less. The Scrum Guide calls the approach iterative and incremental. Iterative means you go round the same loop again and again. Incremental means each loop adds a usable piece to the product, called an Increment."),
            leo("And that's supposed to make me more predictable? Planning less makes me more predictable? That sounds backwards."),
            maya("[chuckle] It does. But think of a weather forecast. Tomorrow's forecast is good; one for six months out is worthless. Scrum forecasts one short Sprint, which it can do well, and learns every Sprint. The Guide says the loop is there to make you more predictable and to control risk. Wrong for a month costs a month, not a year."),
            leo("Fine. The exam keeps talking about pillars. What are those?"),
            maya("Three pillars hold empiricism up: transparency, inspection and adaptation. Learn them in that order, because each one needs the one before it. [pause] Transparency first."),
            leo("Transparency. Meaning everyone sees everything?"),
            maya("Meaning the work, and the way it's done, are visible to the people doing it and to the people receiving it. Say your app's crash rate is on a screen the whole team and the Product Owner can see. That's transparent. A spreadsheet only one person ever opens isn't."),
            leo("Product Owner. Quick reminder?"),
            maya("The one person on the team accountable for the value of the product: what gets built, and in what order. There's a whole episode on them. Now, the Scrum Guide says the important decisions are made on the state of three things it calls artifacts."),
            leo("Artifacts, in plain words?"),
            maya("The three things Scrum keeps in plain view. The Product Backlog is the to-do list for the whole product. The Sprint Backlog is the plan for this Sprint. And the Increment is the usable piece you've built. If they're hidden or wrong, decisions get worse and risk goes up."),
            leo("Give me a not-transparent one. A developer one."),
            maya("For example, a ticket marked done when it really means it works on my phone, with no tests and no review. The board says done; the truth is not done. That's why Scrum has a Definition of Done: a shared description of the quality every piece must meet, so done means the same thing to everyone."),
            leo("Okay. Pillar two, inspection."),
            maya("Inspection means looking at the artifacts and at progress toward the goals, often and carefully, to spot problems early. Like reading your app's crash reports every morning instead of waiting for the one-star reviews. Scrum's events are built for it: each one is a planned moment to look."),
            leo("And if I inspect something that isn't transparent?"),
            maya("Then you're inspecting a story, not the truth. The Scrum Guide says inspection without transparency is misleading and wasteful. You'd be studying a board that says done while the code isn't. [pause] That's why transparency comes first."),
            leo("Pillar three. Adaptation."),
            maya("If what you inspect has drifted outside what's acceptable, you change it, and as soon as possible. Your plan, your process, the product itself. Say the Sprint Review shows users ignore the new sharing screen. You adapt: the Product Backlog changes, and the next Sprint goes somewhere else."),
            leo("And if we look, see the problem, and carry on anyway?"),
            maya("The Scrum Guide's word for that: pointless. Inspection without adaptation is pointless. And adaptation gets harder when people aren't empowered or aren't self-managing, which means the team decides for itself how to do its own work. If every change waits for a manager's signature, you adapt slowly."),
            leo("Now lean thinking. Is that a diet?"),
            maya("[laugh] Close, actually. The Scrum Guide says Scrum rests on empiricism and lean thinking. Lean thinking means reduce waste and focus on the essentials. Waste is anything that costs effort and adds no value: a feature nobody uses, half-finished work sitting in a branch for weeks, a report nobody reads."),
            leo("So the dark mode nobody asked for is waste."),
            maya("If nobody uses it, yes. Small Increments help here too: you find out early, before you've built the whole thing. Empiricism tells you to learn by looking; lean thinking tells you to look at what matters and drop the rest."),
            leo("Where does the word framework come in? People at work call Scrum a methodology."),
            maya("The Scrum Guide calls it a lightweight framework, and deliberately incomplete. A framework is a frame, like the frame of a house: it gives the parts Scrum needs and leaves the rest to you. It won't tell you how to write tests, which branching model to use, or how to estimate."),
            leo("So the gaps are on purpose."),
            maya("On purpose. You fill them with your own practices, and those can change. But the parts Scrum does define, the accountabilities, the events, the artifacts and the rules, you keep whole. The Guide says that using only parts of it is not Scrum, and that leaving parts out covers up the very problems Scrum would show you."),
            leo("And how do the events fit the pillars?"),
            maya("The Sprint is the container. Inside it are four events, each a formal moment to inspect and adapt. Sprint Planning plans the Sprint. The Daily Scrum inspects progress toward the Sprint Goal. The Sprint Review inspects the Increment with stakeholders. The Sprint Retrospective inspects how the team works. Each has its own episode."),
            leo("So what do I remember for the exam?"),
            maya("If a question asks for the three pillars, it's transparency, inspection and adaptation; not planning, not commitment. Empiricism means decisions based on what is observed. Inspection without transparency is misleading; inspection without adaptation is pointless. Scrum is a lightweight framework, deliberately incomplete, not a full method. And adapt as soon as possible, not at the end."),
            leo("And anything to try today?"),
            maya("Name one thing on your team that isn't transparent, like a status only one person knows. Find where your team inspects it today, and how often. Write down one adaptation you made last week because of something you saw. And read the Scrum Theory section of the Scrum Guide; it's about a page."),
            leo("Small steps, and look before you leap. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-empiricism",
        listOf(
            maya("Welcome to Case Files. Today's case: The Release Nobody Saw Coming. An Android team shipped an update to the Play Store on a Friday. Every ticket on their board said done. By Saturday the app was crashing for one user in five, and the reviews were on fire."),
            leo("[sigh] Detective Leo, reporting. I've seen this one. Bad luck, a rushed Friday release. Case closed?"),
            maya("Not so fast. The exam would ask why the team didn't see it coming, and the answer is in one idea: empiricism. It means knowledge comes from experience, and you decide based on what you actually observe, not what you hoped."),
            leo("Then my first suspect is the plan. They didn't plan enough up front. More planning, fewer crashes."),
            maya("Wrong suspect, and it's the trap the exam sets. Scrum is made for complex problems, where you can't know everything up front. A bigger plan is a bigger guess. Scrum works in Sprints, fixed stretches of one month or less, iterative and incremental, to stay predictable and keep risk small."),
            leo("Iterative and incremental. Untangle that for me."),
            maya("Iterative: you go round the same loop again and again. Incremental: each loop adds a usable piece to the product, called an Increment. Under the picture, it's a feedback loop: build a little, look, decide. A loop that only closes once, at release, can't warn you of anything."),
            leo("So what went wrong in the loop?"),
            maya("Empiricism stands on three pillars: transparency, inspection and adaptation. Each needs the one before it. Let's check them like witnesses. First clue: the board. Every ticket said done. I opened them. Half had no tests. Two were never reviewed. Done meant it works on my phone."),
            leo("So the board lied. That's pillar one, transparency?"),
            maya("Exactly. Transparency means the work, and the way it's done, are visible to the people doing it and receiving it. The Scrum Guide says the important decisions are made on the state of three artifacts: the Product Backlog, the to-do list for the whole product; the Sprint Backlog, the plan for this Sprint; and the Increment."),
            leo("And here the Increment was a fake."),
            maya("It wasn't a real Increment at all. The fix is a Definition of Done: a shared description of the quality every piece must meet. Then done means the same thing to everyone. The Increment episode covers it in full. Low transparency, says the Guide, means decisions that lose value and raise risk."),
            leo("Second suspect, then. The Daily Scrum. They had one every morning. It should have been a status report for the manager."),
            maya("[pause] Also wrong. The Daily Scrum is the Developers' own fifteen minutes to inspect progress toward the Sprint Goal and adjust their plan. Not a report to anyone. And they did inspect. Second clue: they looked at that board every morning."),
            leo("So pillar two, inspection, happened. They looked."),
            maya("They looked at a board that wasn't true. Inspection means looking at the artifacts and progress, often and carefully, to spot problems early. But the Scrum Guide says inspection without transparency is misleading and wasteful. Fifteen minutes a day of studying a story."),
            leo("Okay, third clue?"),
            maya("The Sprint Retrospective two weeks ago, the event where the team plans how to work better. The notes say: we keep marking things done too early. Everyone agreed. Then nothing changed."),
            leo("Ah. Pillar three, adaptation."),
            maya("Adaptation: if something has drifted outside what's acceptable, adjust it, and as soon as possible. The Guide's line: inspection without adaptation is pointless. And there's one more clue. Every process change needed the department head's approval, and that took a month."),
            leo("So they couldn't adapt even if they wanted to."),
            maya("That's where adaptation stops working. The Guide says it gets harder when people aren't empowered or self-managing, meaning the team doesn't decide how it does its own work. The fix isn't a better form; it's letting the team change its own way of working."),
            leo("Any accomplice?"),
            maya("Waste. Scrum also rests on lean thinking, which means reduce waste and focus on the essentials. Waste is effort that adds no value. Three half-finished features had been sitting in branches for weeks, and merging them all on Friday is what broke the build."),
            leo("And here I thought Scrum was a full rulebook that would've stopped this."),
            maya("It isn't. The Scrum Guide calls Scrum a lightweight framework, deliberately incomplete. It gives the frame: accountabilities, events, artifacts, rules. It won't tell you how to test or how to merge. Your own practices fill the gaps. But the frame stays whole; using only parts of it, says the Guide, is not Scrum."),
            leo("So the events are where the pillars live."),
            maya("Right. The Sprint holds four events, each a formal moment to inspect and adapt: Sprint Planning, the Daily Scrum, the Sprint Review and the Sprint Retrospective. This team had all four on the calendar, and all four were looking at a board that lied."),
            leo("So, the case summary for the exam?"),
            maya("The three pillars are transparency, inspection and adaptation, in that order. Empiricism means decisions based on what is observed. Inspection without transparency is misleading; inspection without adaptation is pointless. Scrum is a lightweight framework, deliberately incomplete. And when something drifts, adapt as soon as possible."),
            leo("Case closed. And to try today?"),
            maya("Name one thing on your team that isn't transparent, like a status only one person knows. Find where your team inspects it today, and how often. Write down one adaptation you made last week because of something you saw. And read the Scrum Theory section of the Scrum Guide; it's about a page."),
            leo("Trust the board only when the board tells the truth. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-empiricism",
        listOf(
            maya("Okay, did you hear what happened on the app team this week? They decided to do Scrum properly. And it turns out properly starts with a word nobody could spell: empiricism."),
            leo("[laugh] I heard Tom tried to spell it in the stand-up. So what does it actually mean?"),
            maya("Empiricism means you learn from what actually happens. The Scrum Guide says knowledge comes from experience, and decisions are based on what you observe. Try something small, look at the result, change course. Which brings us to Monday, and Priya."),
            leo("What did Priya do?"),
            maya("Priya wrote a six-month plan for the new Android release. Every screen designed, every date fixed, release to the Play Store in month six. Sixty pages. She was very proud."),
            leo("Sounds thorough. What's wrong with thorough?"),
            maya("It's a guess, and nobody checks the guess until month six. Scrum is made for complex problems, where you can't know the answer up front. So it works in Sprints, fixed stretches of one month or less. The Guide calls it iterative and incremental, to stay predictable and keep risk small."),
            leo("Iterative and incremental. Say it plainly, I'll repeat it at lunch."),
            maya("Iterative: you go round the same loop again and again. Incremental: each loop adds a usable piece, an Increment, to the product. Under the picture it's a feedback loop: build a little, look, decide. If Priya's plan is wrong, she finds out after one Sprint, not six months."),
            leo("Okay. Tuesday?"),
            maya("Tuesday was Tom. Tom marked eight tickets done. Then the tester opened one, and it only worked on Tom's own phone. No tests, no review. Done, according to Tom."),
            leo("[chuckle] Classic Tom. Which rule did he break?"),
            maya("The first pillar. Empiricism stands on three: transparency, inspection and adaptation, and each needs the one before. Transparency means the work, and the way it's done, are visible to the people doing it and receiving it. Tom's board was visible, sure. It just wasn't true."),
            leo("So what fixes Tom?"),
            maya("A Definition of Done: a shared description of the quality every piece must meet, so done means the same for everyone. The Scrum Guide says big decisions are made on the state of three artifacts: the Product Backlog, the to-do list for the product; the Sprint Backlog, the plan for the Sprint; and the Increment. Tom's Increment wasn't real."),
            leo("Wednesday?"),
            maya("Wednesday was Marcus. Marcus ran the Daily Scrum, fifteen minutes for the Developers to check progress toward the Sprint Goal. He went through Tom's board, ticket by ticket, and announced the team was ahead of schedule."),
            leo("Oh no. He inspected the lie."),
            maya("He inspected the lie. Inspection, the second pillar, means looking at the work and progress, often and carefully, to catch problems early. But the Scrum Guide says inspection without transparency is misleading and wasteful. Marcus was very carefully studying a story."),
            leo("And Thursday? There's always a Thursday."),
            maya("Thursday was Dana's Sprint Retrospective, the event where the team plans how to work better. Everyone agreed done meant too many different things. Lovely discussion. Then they wrote it on a sticky note and changed nothing."),
            leo("[sigh] Pillar three."),
            maya("Adaptation: when something drifts outside what's acceptable, adjust, as soon as possible. The Guide says inspection without adaptation is pointless. And the reason they didn't change it? Every process change needed the department head's sign-off."),
            leo("So it wasn't really Dana's fault."),
            maya("Not all of it. The Scrum Guide says adaptation gets harder when people aren't empowered or self-managing, meaning the team decides how it does its own work. That's where adaptation stops working: a team that may not change itself can only watch."),
            leo("And Friday?"),
            maya("The intern. The intern built a beautiful dark mode nobody asked for, and left two half-finished features in branches for three weeks. Then everything was merged on Friday at five."),
            leo("I can guess how the weekend went."),
            maya("That's lean thinking. The Scrum Guide says Scrum rests on empiricism and lean thinking: reduce waste and focus on the essentials. Waste is effort that adds no value. A feature nobody uses, or work sitting half-done, is waste."),
            leo("And the department head? Any gossip?"),
            maya("He called Scrum a methodology and suggested dropping the Retrospective to save time. But the Scrum Guide calls Scrum a lightweight framework, deliberately incomplete: it gives the frame and leaves the practices to you. Its events, the Sprint Planning, Daily Scrum, Sprint Review and Retrospective, are where you inspect and adapt. Drop one, and it's not Scrum."),
            leo("So, the gossip I should remember for the exam?"),
            maya("The three pillars: transparency, inspection, adaptation. Empiricism means decisions based on what is observed. Inspection without transparency is misleading; inspection without adaptation is pointless. Scrum is a lightweight framework, deliberately incomplete. And adapt as soon as possible. The values episode is next, if you want more of the team's week."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Name one thing on your team that isn't transparent, like a status only one person knows. Find where your team inspects it today, and how often. Write down one adaptation you made last week because of something you saw. And read the Scrum Theory section of the Scrum Guide; it's about a page."),
            leo("Done means done, Tom. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-empiricism",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning for P S M one. [pause] Tonight: how to know anything at all, when you can't see the future."),
            ivy("Mm. Deep, for this hour. Is this philosophy, Nova?"),
            nova("[chuckle] It's Scrum. The word is empiricism. It means you learn from what actually happens, not from what you hoped. The Scrum Guide says knowledge comes from experience, and decisions are made on what you observe."),
            ivy("So you only trust what you've seen with your own eyes."),
            nova("Exactly. Picture an Android team planning a whole year's release in one evening. Every screen, every date. [pause] It feels safe. But it's a guess, and nobody checks it until the end."),
            ivy("And the end is where it hurts."),
            nova("It is. Scrum is made for complex problems, where you can't know the answer up front. So it works in Sprints, fixed stretches of one month or less. The Guide calls it iterative and incremental."),
            ivy("Say those slowly for me."),
            nova("Iterative: you go round the same loop, again, and again. Incremental: each loop adds a usable piece to the product, called an Increment. [pause] Build a little, look, decide. The Guide says it makes you more predictable and keeps the risk small."),
            ivy("Predictable, by planning less? You'll have to convince me."),
            nova("Think of the weather. Tomorrow's forecast you can trust. Six months out, nobody can. Scrum forecasts one short Sprint at a time, and learns from each. If you're wrong, you've lost a month, not a year."),
            ivy("Okay. And what holds all this up?"),
            nova("Three pillars. Transparency, inspection, adaptation. [pause] In that order, because each one leans on the one before."),
            ivy("Transparency first, then. Nothing hidden?"),
            nova("Nothing hidden from the people who need it. The work, and the way it's done, are visible to the people doing it and the people receiving it. Your app's crash rate on a screen everyone can see. Not a spreadsheet only one person ever opens."),
            ivy("And what are they all looking at?"),
            nova("Three artifacts, the Scrum Guide calls them. The Product Backlog, the to-do list for the whole product. The Sprint Backlog, the plan for this Sprint. And the Increment, the usable piece you've built. Big decisions are made on what these show."),
            ivy("And if one of them is lying?"),
            nova("Then the decisions are bad, and the risk goes up. A ticket marked done that only works on your own phone. That's why there's a Definition of Done: a shared description of the quality every piece must meet. [pause] So done means the same thing to everyone."),
            ivy("Pillar two. Inspection. Look at me closely?"),
            nova("[laugh] Look at the work closely. Inspection means looking at the artifacts, and at progress toward the goals, often and carefully, to spot trouble early. Every Scrum event is a planned moment to look."),
            ivy("And if I'm looking at something that isn't true?"),
            nova("Then it's misleading, and it's waste. That's the Guide's own warning: inspection without transparency is misleading and wasteful. [pause] Like gazing at a pretty board that says done, when it isn't."),
            ivy("And pillar three?"),
            nova("Adaptation. If what you see has drifted outside what's acceptable, you change it, as soon as possible. The plan, the process, the product. And inspection without adaptation is, in the Guide's word, pointless."),
            ivy("When does adapting stop working?"),
            nova("When people aren't empowered, or aren't self-managing, meaning the team decides how to do its own work. If every change waits a month for a manager's signature, [pause] you can see the problem and still not move."),
            ivy("And lean thinking? I keep hearing it."),
            nova("Scrum rests on empiricism and lean thinking. Lean thinking means reduce waste and focus on the essentials. A feature nobody uses. Work sitting half-done in a branch for weeks. All waste, all effort with no value."),
            ivy("Is Scrum the full instruction book, then? Every step written down?"),
            nova("No. The Guide calls it a lightweight framework, deliberately incomplete. It gives the frame: accountabilities, events, artifacts, rules. How you test, how you merge, how you estimate, that's yours. But keep the frame whole. Use only parts, and it isn't Scrum."),
            ivy("And the events are where I inspect and adapt."),
            nova("Inside the Sprint, four of them. Sprint Planning, the Daily Scrum, the Sprint Review, the Sprint Retrospective. Each gets its own late night. [pause] The values episode is the next one, if you're still awake."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("The three pillars are transparency, inspection and adaptation; not planning, not commitment. Empiricism means decisions based on what is observed. Inspection without transparency is misleading; inspection without adaptation is pointless. Scrum is a lightweight framework, deliberately incomplete. And adapt as soon as possible."),
            ivy("And something to try today, before I sleep?"),
            nova("Name one thing on your team that isn't transparent, like a status only one person knows. [pause] Find where your team inspects it today, and how often. Write down one adaptation you made last week because of something you saw. And read the Scrum Theory section of the Scrum Guide; it's about a page."),
            ivy("Look, then leap. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-empiricism",
        listOf(
            nova("Tonight is date night, darling. And I want total transparency between us."),
            rafe("Total? On a first Scrum date? Bold."),
            nova("[laugh] It's the exam's word, love. Tonight's topic is empiricism. It means you learn from experience, and you make decisions on what you actually observe, not what you guessed."),
            rafe("I like a woman who learns from experience. So, no grand plans?"),
            nova("Small steps. Scrum is made for complex problems, where you can't know the answer up front. Picture an Android team that plans a whole year in one go, then releases to the Play Store at the end. A beautiful plan, and a guess nobody checks until it's too late."),
            rafe("So what does Scrum do instead?"),
            nova("Sprints: fixed stretches of one month or less. Iterative, which means you go round the same loop again and again. And incremental, which means each loop adds a usable piece to the product, called an Increment. The Guide says it makes you predictable and keeps the risk small."),
            rafe("Predictable, by planning less? That sounds like my excuse for never booking restaurants."),
            nova("[chuckle] Think of the weather instead. Tomorrow's forecast you can trust; one for six months out, nobody can. Scrum forecasts one short Sprint, which it can do well, and learns from every one. If we're wrong, we've lost a month, not a year."),
            rafe("Then let's see if you're predictable. Exam question. What are the three pillars of empiricism?"),
            nova("Transparency, inspection and adaptation. In that order, because each one needs the one before it."),
            rafe("Right answer. Not planning, not commitment. [kiss]"),
            nova("Mm. Transparency means the work, and the way it's done, are visible to the people doing it and the people receiving it. Your app's crash rate on a screen the whole team can see."),
            rafe("And what do we keep so visible?"),
            nova("Three artifacts. The Product Backlog, the to-do list for the whole product. The Sprint Backlog, the plan for this Sprint. And the Increment, the usable piece. The Scrum Guide says the important decisions are made on what these three show."),
            rafe("My turn to be inspected. Ask me one."),
            nova("A team inspects its board every morning. The board says every ticket is done, but half the code was never tested. What does the Scrum Guide call that inspection?"),
            rafe("Misleading and wasteful. Inspection without transparency is misleading and wasteful. They're studying a story, not the truth."),
            nova("Right answer. You see straight through me. [kiss]"),
            rafe("So how does a team stop the board lying?"),
            nova("A Definition of Done: a shared description of the quality every piece must meet, so done means the same thing to everyone. No more done meaning it works on my phone."),
            rafe("I never say done until it really is, darling. Now inspection itself?"),
            nova("Inspection means looking at the artifacts and at progress toward the goals, often and carefully, to spot trouble early. Every Scrum event is a planned moment to look. Quiz for you: what does the Guide say about inspection without adaptation?"),
            rafe("That it's fine, as long as you write it down?"),
            nova("No kiss for that one. It's pointless. If what you inspect has drifted outside what's acceptable, you adapt: change the plan, the process or the product, as soon as possible. A note on the wall changes nothing."),
            rafe("Show me adapting on our app, then."),
            nova("Say the Sprint Review shows users ignore the new sharing screen. You adapt: the Product Backlog changes, and the next Sprint goes somewhere else. You don't keep polishing a screen nobody opens."),
            rafe("Fair enough. When does adapting stop working?"),
            nova("When people aren't empowered or self-managing, which means the team decides how to do its own work. If every change needs a manager's signature, you see the problem and still can't move. That's the edge of it."),
            rafe("Then here's one for you. Scrum rests on empiricism and on what else?"),
            nova("Lean thinking. It means reduce waste and focus on the essentials. A feature nobody uses, half-finished work sitting in a branch for weeks: waste."),
            rafe("Right answer. Lean, and still so generous. [kiss]"),
            nova("Careful. Last one from me. Someone at work calls Scrum a complete methodology that covers everything. True or false?"),
            rafe("False. The Scrum Guide calls it a lightweight framework, deliberately incomplete. It gives the frame: accountabilities, events, artifacts and rules. How you test or merge is yours to fill in. But using only parts of it isn't Scrum."),
            nova("Right answer. A frame, with room for us. [kiss]"),
            rafe("And where do we actually inspect and adapt?"),
            nova("In the events, inside the Sprint: Sprint Planning, the Daily Scrum, the Sprint Review and the Sprint Retrospective. Each has its own episode. The values episode is our next date."),
            rafe("Then the last question, for the exam. What do we remember?"),
            nova("The three pillars are transparency, inspection and adaptation. Empiricism means decisions based on what is observed. Inspection without transparency is misleading; inspection without adaptation is pointless. Scrum is a lightweight framework, deliberately incomplete. And adapt as soon as possible, not at the end."),
            rafe("Five out of five. That earns a long one. [kiss]"),
            nova("Mm. And something to try today?"),
            rafe("Name one thing on our team that isn't transparent, like a status only one person knows. Find where our team inspects it today, and how often. Write down one adaptation we made last week because of something we saw. And read the Scrum Theory section of the Scrum Guide; it's about a page."),
            nova("Nothing hidden, darling. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun loop() = DiagramSpec(
        "Instead of one big guess checked at the end, build a little, look at the result and decide, every Sprint.",
        nodes = listOf(
            DNode("guess", "Six-month plan, checked at the end", 0.5f, 0.13f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
            DNode("build", "Build a little", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "an Increment"),
            DNode("look", "Look", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "what really happened"),
            DNode("decide", "Decide", 0.5f, 0.85f, w = 0.42f, h = 0.2f, tone = 1, sub = "on what you saw"),
        ),
        edges = listOf(
            DEdge("build", "look", "observe"),
            DEdge("look", "decide", "learn"),
            DEdge("decide", "build", "next Sprint"),
        ),
        aspect = 0.8f,
    )

    private fun pillars() = DiagramSpec(
        "Empiricism stands on three pillars, each needing the one before it, with lean thinking underneath.",
        nodes = listOf(
            DNode("emp", "Empiricism", 0.5f, 0.1f, w = 0.9f, h = 0.14f, tone = 1),
            DNode("transp", "Transparency", 0.17f, 0.5f, w = 0.3f, h = 0.4f, sub = "visible and true"),
            DNode("insp", "Inspection", 0.5f, 0.5f, w = 0.3f, h = 0.4f, sub = "look often"),
            DNode("adapt", "Adaptation", 0.83f, 0.5f, w = 0.3f, h = 0.4f, sub = "change soon"),
            DNode("lean", "Lean thinking: less waste", 0.5f, 0.9f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("transp", "insp", "enables"),
            DEdge("insp", "adapt", "enables"),
        ),
        aspect = 0.8f,
    )

    private fun artifacts() = DiagramSpec(
        "Decisions rest on the three artifacts; the Definition of Done keeps the Increment honest.",
        nodes = listOf(
            DNode("pb", "Product Backlog", 0.25f, 0.18f, w = 0.42f, h = 0.22f, tone = 1, sub = "the product's list"),
            DNode("sb", "Sprint Backlog", 0.75f, 0.18f, w = 0.42f, h = 0.22f, tone = 1, sub = "this Sprint's plan"),
            DNode("inc", "Increment", 0.25f, 0.72f, w = 0.42f, h = 0.22f, sub = "the usable piece"),
            DNode("dod", "Definition of Done", 0.75f, 0.72f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "shared quality bar"),
        ),
        edges = listOf(
            DEdge("pb", "sb", "chosen for"),
            DEdge("sb", "inc", "becomes"),
            DEdge("dod", "inc", "quality"),
        ),
        aspect = 0.8f,
    )

    private fun lean() = DiagramSpec(
        "Lean thinking: cut the effort that adds no value, and find out early with small Increments.",
        nodes = listOf(
            DNode("waste", "Waste", 0.25f, 0.15f, w = 0.42f, h = 0.2f, tone = 2, sub = "effort, no value"),
            DNode("ess", "Essentials", 0.75f, 0.15f, w = 0.42f, h = 0.2f, tone = 1, sub = "what adds value"),
            DNode("f1", "A feature nobody uses", 0.25f, 0.52f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("f2", "Work half-done for weeks", 0.25f, 0.84f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("inc", "Small Increments", 0.75f, 0.68f, w = 0.42f, h = 0.2f, sub = "find out early"),
        ),
        edges = listOf(
            DEdge("waste", "f1"),
            DEdge("f1", "f2"),
            DEdge("ess", "inc"),
        ),
        aspect = 0.8f,
    )

    private fun framework() = DiagramSpec(
        "Scrum gives the frame and keeps it whole; your own practices fill the gaps it leaves on purpose.",
        nodes = listOf(
            DNode("acc", "Accountabilities", 0.25f, 0.14f, w = 0.42f, h = 0.18f, tone = 1),
            DNode("ev", "Events", 0.75f, 0.14f, w = 0.42f, h = 0.18f, tone = 1),
            DNode("art", "Artifacts", 0.25f, 0.42f, w = 0.42f, h = 0.18f, tone = 1),
            DNode("rules", "Rules", 0.75f, 0.42f, w = 0.42f, h = 0.18f, tone = 1),
            DNode("yours", "Your practices", 0.5f, 0.8f, w = 0.9f, h = 0.22f, tone = 2, sub = "tests, branches, estimates"),
        ),
        edges = listOf(
            DEdge("art", "yours", "gaps left open"),
        ),
        aspect = 0.8f,
    )

    private fun events() = DiagramSpec(
        "Inside the Sprint, four events, each a formal moment to inspect and adapt.",
        nodes = listOf(
            DNode("plan", "Sprint Planning", 0.25f, 0.2f, w = 0.42f, h = 0.22f, sub = "plans the Sprint"),
            DNode("daily", "Daily Scrum", 0.75f, 0.2f, w = 0.42f, h = 0.22f, sub = "toward the Sprint Goal"),
            DNode("review", "Sprint Review", 0.75f, 0.74f, w = 0.42f, h = 0.22f, sub = "the Increment"),
            DNode("retro", "Retrospective", 0.25f, 0.74f, w = 0.42f, h = 0.22f, sub = "how we work"),
        ),
        edges = listOf(
            DEdge("plan", "daily"),
            DEdge("daily", "review"),
            DEdge("review", "retro"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's empiricism questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("pillars", "Three pillars", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "see, inspect, adapt"),
            DNode("emp", "Empiricism", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "decide on what's seen"),
            DNode("notr", "No transparency", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "inspection misleads"),
            DNode("noad", "No adaptation", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "inspection pointless"),
            DNode("frame", "Scrum is a framework", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "deliberately incomplete"),
            DNode("soon", "When to adapt", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "as soon as possible"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("pillars", "emp", "notr", "noad", "frame", "soon")
    private val loopAll = setOf("build", "look", "decide")
    private val artAll = setOf("pb", "sb", "inc")
    private val frameAll = setOf("acc", "ev", "art", "rules")
    private val takeaways = BoardCard("Listen for the phrase", "Pillars: transparency, inspection, adaptation. Framework: incomplete.")
    private val today = BoardCard("Today", "One thing not transparent; where it's inspected; one adaptation; read Theory.")

    private fun classicBoard() = PodcastBoard(
        "psm-empiricism",
        listOf(
            // 0: "Hi, and welcome back. Today: empiricism..."
            Scene(
                0, "Learning by looking", loop(),
                beats = listOf(
                    Beat(0, card = BoardCard("Empiricism", "The idea every other part of Scrum stands on.")),
                    Beat(2, card = BoardCard("In plain words", "Knowledge comes from experience; decide on what you observe.")),
                    Beat(4, show = setOf("guess"), light = setOf("guess"), card = BoardCard("The six-month plan", "Every screen up front: a guess nobody checks until month six.")),
                    Beat(6, show = loopAll, light = setOf("build"), card = BoardCard("Complex problems", "You can't know the answer up front, so take a small step and look.")),
                    Beat(8, light = setOf("build", "look"), pulse = "build" to "look", card = BoardCard("Iterative and incremental", "Round the same loop; each loop adds a usable Increment.")),
                    Beat(10, light = setOf("look", "decide"), pulse = "look" to "decide", card = BoardCard("Predictable, less risk", "Forecast one short Sprint; wrong costs a month, not a year.")),
                ),
            ),
            // 11: "Fine. The exam keeps talking about pillars."
            Scene(
                11, "Three pillars", pillars(),
                beats = listOf(
                    Beat(12, light = setOf("transp", "insp", "adapt"), card = BoardCard("Three pillars", "Transparency, inspection, adaptation: each needs the one before.")),
                    Beat(14, light = setOf("transp"), card = BoardCard("Transparency", "The work and how it's done, visible to doers and receivers.")),
                ),
            ),
            // 15: "Product Owner. Quick reminder?"
            Scene(
                15, "What decisions rest on", artifacts(),
                beats = listOf(
                    Beat(16, card = BoardCard("Product Owner", "One person, accountable for the value of the product.")),
                    Beat(18, show = artAll, light = artAll, pulse = "pb" to "sb", card = BoardCard("Three artifacts", "Product Backlog, Sprint Backlog, Increment. Hidden means risk.")),
                    Beat(20, show = setOf("dod"), light = setOf("dod", "inc"), pulse = "dod" to "inc", card = BoardCard("Definition of Done", "A shared quality bar, so done means the same to everyone.")),
                ),
            ),
            // 21: "Okay. Pillar two, inspection."
            Scene(
                21, "Inspect, then adapt", pillars(),
                beats = listOf(
                    Beat(22, light = setOf("insp"), pulse = "transp" to "insp", card = BoardCard("Inspection", "Look at the artifacts and progress, often and carefully.")),
                    Beat(24, light = setOf("transp", "insp"), card = BoardCard("No transparency", "Inspection without it is misleading and wasteful.")),
                    Beat(26, light = setOf("adapt"), pulse = "insp" to "adapt", card = BoardCard("Adaptation", "Drifted too far? Change it as soon as possible.")),
                    Beat(28, light = setOf("insp", "adapt"), card = BoardCard("No adaptation", "Inspection without it is pointless. Empower the team.")),
                ),
            ),
            // 29: "Now lean thinking. Is that a diet?"
            Scene(
                29, "Lean thinking", lean(),
                beats = listOf(
                    Beat(30, show = setOf("waste", "f1", "f2"), light = setOf("waste", "f1", "f2"), pulse = "waste" to "f1", card = BoardCard("Lean thinking", "Reduce waste and focus on the essentials.")),
                    Beat(32, show = setOf("ess", "inc"), light = setOf("ess", "inc"), pulse = "ess" to "inc", card = BoardCard("Small Increments", "Find out early, before you've built the whole thing.")),
                ),
            ),
            // 33: "Where does the word framework come in?"
            Scene(
                33, "A framework, not a method", framework(),
                beats = listOf(
                    Beat(34, show = frameAll, light = frameAll, card = BoardCard("Lightweight framework", "Deliberately incomplete: only the parts Scrum needs.")),
                    Beat(36, show = setOf("yours"), light = setOf("yours"), pulse = "art" to "yours", card = BoardCard("Keep the frame whole", "Fill the gaps your way; using only parts of it is not Scrum.")),
                ),
            ),
            // 37: "And how do the events fit the pillars?"
            Scene(
                37, "Where you inspect and adapt", events(),
                beats = listOf(
                    Beat(38, light = setOf("plan", "daily", "review", "retro"), card = BoardCard("Four events in the Sprint", "Each one a formal moment to inspect and adapt.")),
                ),
            ),
            // 39: "So what do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = all6, card = takeaways),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-empiricism--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Release Nobody Saw Coming..."
            Scene(
                0, "The Release Nobody Saw Coming", loop(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Every ticket said done. By Saturday, one user in five crashed.")),
                    Beat(2, card = BoardCard("Empiricism", "Knowledge comes from experience; decide on what you observe.")),
                    Beat(4, show = setOf("guess"), light = setOf("guess"), card = BoardCard("Suspect cleared: planning", "A bigger plan is a bigger guess. Work in short Sprints.")),
                    Beat(6, show = loopAll, light = loopAll, pulse = "build" to "look", card = BoardCard("A feedback loop", "Iterative: round again. Incremental: each loop adds a usable piece.")),
                ),
            ),
            // 8: "Empiricism stands on three pillars..."
            Scene(
                8, "Clue one: the board", pillars(),
                beats = listOf(
                    Beat(8, light = setOf("transp"), card = BoardCard("Done meant it works on my phone", "Half the tickets had no tests; two were never reviewed.")),
                ),
            ),
            // 10: "Exactly. Transparency means the work..."
            Scene(
                10, "What decisions rest on", artifacts(),
                beats = listOf(
                    Beat(10, show = artAll, light = artAll, pulse = "pb" to "sb", card = BoardCard("Transparency", "Decisions rest on three artifacts; hidden ones raise risk.")),
                    Beat(12, show = setOf("dod"), light = setOf("dod", "inc"), pulse = "dod" to "inc", card = BoardCard("Definition of Done", "A shared quality bar, so done means the same to everyone.")),
                ),
            ),
            // 13: "Second suspect, then. The Daily Scrum."
            Scene(
                13, "Clue two: the Daily Scrum", pillars(),
                beats = listOf(
                    Beat(14, card = BoardCard("Suspect cleared: status report", "The Developers' fifteen minutes, toward the Sprint Goal.")),
                    Beat(16, light = setOf("transp", "insp"), pulse = "transp" to "insp", card = BoardCard("Inspection", "Without transparency it is misleading and wasteful.")),
                ),
            ),
            // 17: "Okay, third clue?"
            Scene(
                17, "Clue three: the Retrospective", pillars(),
                beats = listOf(
                    Beat(18, light = setOf("adapt"), card = BoardCard("The Retrospective", "Everyone agreed done came too early. Nothing changed.")),
                    Beat(20, light = setOf("insp", "adapt"), pulse = "insp" to "adapt", card = BoardCard("Adaptation", "Without it, inspection is pointless. Adapt as soon as possible.")),
                    Beat(22, card = BoardCard("Not empowered", "A month for every sign-off: the team couldn't change itself.")),
                ),
            ),
            // 23: "Any accomplice?"
            Scene(
                23, "The accomplice: waste", lean(),
                beats = listOf(
                    Beat(24, show = setOf("waste", "f1", "f2", "ess", "inc"), light = setOf("f2"), pulse = "f1" to "f2", card = BoardCard("Lean thinking", "Features half-done in branches for weeks, merged on Friday.")),
                ),
            ),
            // 25: "And here I thought Scrum was a full rulebook..."
            Scene(
                25, "A frame, not a rulebook", framework(),
                beats = listOf(
                    Beat(26, show = frameAll + "yours", light = frameAll, card = BoardCard("Lightweight framework", "Deliberately incomplete; your practices fill the gaps.")),
                ),
            ),
            // 27: "So the events are where the pillars live."
            Scene(
                27, "Four events", events(),
                beats = listOf(
                    Beat(28, light = setOf("plan", "daily", "review", "retro"), card = BoardCard("All four on the calendar", "And all four looking at a board that lied.")),
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
        "psm-empiricism--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the app team this week?..."
            Scene(
                0, "A week of Scrum", loop(),
                beats = listOf(
                    Beat(0, card = BoardCard("Empiricism", "The word nobody on the team could spell.")),
                    Beat(2, card = BoardCard("In plain words", "Knowledge comes from experience; decide on what you observe.")),
                    Beat(4, show = setOf("guess"), light = setOf("guess"), card = BoardCard("Monday: Priya", "A sixty-page, six-month plan: a guess nobody checks.")),
                    Beat(6, show = loopAll, light = setOf("build"), card = BoardCard("Sprints", "One month or less: iterative and incremental.")),
                    Beat(8, light = setOf("look", "decide"), pulse = "look" to "decide", card = BoardCard("A feedback loop", "Wrong? Priya finds out after one Sprint, not six months.")),
                ),
            ),
            // 9: "Okay. Tuesday?"
            Scene(
                9, "Tuesday: Tom", pillars(),
                beats = listOf(
                    Beat(10, light = setOf("transp"), card = BoardCard("Done, according to Tom", "It only worked on Tom's own phone.")),
                    Beat(12, light = setOf("transp", "insp", "adapt"), card = BoardCard("Three pillars", "Transparency first: the board must be visible, and true.")),
                ),
            ),
            // 13: "So what fixes Tom?"
            Scene(
                13, "What fixes Tom", artifacts(),
                beats = listOf(
                    Beat(14, show = artAll + "dod", light = setOf("dod", "inc"), pulse = "dod" to "inc", card = BoardCard("Definition of Done", "A shared quality bar, so done means the same to everyone.")),
                ),
            ),
            // 15: "Wednesday?"
            Scene(
                15, "Wednesday: Marcus", pillars(),
                beats = listOf(
                    Beat(16, light = setOf("insp"), card = BoardCard("The Daily Scrum", "Fifteen minutes for the Developers, toward the Sprint Goal.")),
                    Beat(18, light = setOf("transp", "insp"), pulse = "transp" to "insp", card = BoardCard("Inspecting the lie", "Inspection without transparency: misleading and wasteful.")),
                ),
            ),
            // 19: "And Thursday? There's always a Thursday."
            Scene(
                19, "Thursday: Dana", pillars(),
                beats = listOf(
                    Beat(20, light = setOf("adapt"), card = BoardCard("A sticky note", "The Retrospective agreed on a problem, then changed nothing.")),
                    Beat(22, light = setOf("insp", "adapt"), pulse = "insp" to "adapt", card = BoardCard("Adaptation", "As soon as possible. Without it, inspection is pointless.")),
                    Beat(24, card = BoardCard("Not empowered", "Every change waited for the department head's sign-off.")),
                ),
            ),
            // 25: "And Friday?"
            Scene(
                25, "Friday: the intern", lean(),
                beats = listOf(
                    Beat(26, show = setOf("waste", "f1", "f2"), light = setOf("f1", "f2"), pulse = "f1" to "f2", card = BoardCard("Dark mode and old branches", "Nobody asked for one; the other sat half-done for weeks.")),
                    Beat(28, show = setOf("ess", "inc"), light = setOf("waste", "ess"), card = BoardCard("Lean thinking", "Reduce waste and focus on the essentials.")),
                ),
            ),
            // 29: "And the department head? Any gossip?"
            Scene(
                29, "The department head", framework(),
                beats = listOf(
                    Beat(30, show = frameAll + "yours", light = frameAll, card = BoardCard("Lightweight framework", "Deliberately incomplete. Drop an event, and it's not Scrum.")),
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
        "psm-empiricism--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Knowing, without the future", loop(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "How to know anything, when you can't see the future.")),
                    Beat(2, card = BoardCard("Empiricism", "Knowledge comes from experience; decide on what you observe.")),
                    Beat(4, show = setOf("guess"), light = setOf("guess"), card = BoardCard("A year in one evening", "It feels safe. It's a guess nobody checks until the end.")),
                    Beat(6, show = loopAll, light = setOf("build"), card = BoardCard("Sprints", "One month or less: iterative and incremental.")),
                    Beat(8, light = setOf("build", "look"), pulse = "build" to "look", card = BoardCard("Build, look, decide", "Each loop adds a usable piece, called an Increment.")),
                    Beat(10, light = setOf("look", "decide"), pulse = "look" to "decide", card = BoardCard("The weather", "Trust tomorrow's forecast: wrong costs a month, not a year.")),
                ),
            ),
            // 11: "Okay. And what holds all this up?"
            Scene(
                11, "Three pillars", pillars(),
                beats = listOf(
                    Beat(12, light = setOf("transp", "insp", "adapt"), card = BoardCard("Three pillars", "Transparency, inspection, adaptation, each on the one before.")),
                    Beat(14, light = setOf("transp"), card = BoardCard("Transparency", "Nothing hidden from those who do the work or receive it.")),
                ),
            ),
            // 15: "And what are they all looking at?"
            Scene(
                15, "What they look at", artifacts(),
                beats = listOf(
                    Beat(16, show = artAll, light = artAll, pulse = "pb" to "sb", card = BoardCard("Three artifacts", "Product Backlog, Sprint Backlog, Increment.")),
                    Beat(18, show = setOf("dod"), light = setOf("dod", "inc"), pulse = "dod" to "inc", card = BoardCard("Definition of Done", "A shared quality bar, so done means the same to everyone.")),
                ),
            ),
            // 19: "Pillar two. Inspection. Look at me closely?"
            Scene(
                19, "Look closely", pillars(),
                beats = listOf(
                    Beat(20, light = setOf("insp"), card = BoardCard("Inspection", "Artifacts and progress, often and carefully, at every event.")),
                    Beat(22, light = setOf("transp", "insp"), pulse = "transp" to "insp", card = BoardCard("No transparency", "Inspection without it is misleading and wasteful.")),
                    Beat(24, light = setOf("adapt"), pulse = "insp" to "adapt", card = BoardCard("Adaptation", "Change it as soon as possible; otherwise looking is pointless.")),
                    Beat(26, card = BoardCard("Not empowered", "A month for each signature: you see the problem, and can't move.")),
                ),
            ),
            // 27: "And lean thinking? I keep hearing it."
            Scene(
                27, "Lean thinking", lean(),
                beats = listOf(
                    Beat(28, show = setOf("waste", "f1", "f2", "ess", "inc"), light = setOf("waste", "ess"), pulse = "waste" to "f1", card = BoardCard("Lean thinking", "Reduce waste and focus on the essentials.")),
                ),
            ),
            // 29: "Is Scrum the full instruction book, then?"
            Scene(
                29, "A frame, not a book", framework(),
                beats = listOf(
                    Beat(30, show = frameAll + "yours", light = setOf("yours"), pulse = "art" to "yours", card = BoardCard("Lightweight framework", "Deliberately incomplete. Use only parts, and it isn't Scrum.")),
                ),
            ),
            // 31: "And the events are where I inspect and adapt."
            Scene(
                31, "Four events", events(),
                beats = listOf(
                    Beat(32, light = setOf("plan", "daily", "review", "retro"), card = BoardCard("Inside the Sprint", "Planning, Daily Scrum, Review, Retrospective.")),
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
        "psm-empiricism--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Total transparency", loop(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Total transparency, and a word: empiricism.")),
                    Beat(2, card = BoardCard("Empiricism", "Learn from experience; decide on what you observe.")),
                    Beat(4, show = setOf("guess"), light = setOf("guess"), card = BoardCard("A year in one go", "A beautiful plan, and a guess nobody checks.")),
                    Beat(6, show = loopAll, light = loopAll, pulse = "build" to "look", card = BoardCard("Iterative and incremental", "Round the same loop; each loop adds a usable Increment.")),
                    Beat(8, light = setOf("look", "decide"), pulse = "look" to "decide", card = BoardCard("The weather", "Forecast one short Sprint: wrong costs a month, not a year.")),
                ),
            ),
            // 9: "Then let's see if you're predictable. Exam question..."
            Scene(
                9, "Quiz: three pillars", pillars(),
                beats = listOf(
                    Beat(9, card = BoardCard("Quiz", "What are the three pillars of empiricism?")),
                    Beat(10, light = setOf("transp", "insp", "adapt"), card = BoardCard("Three pillars", "Transparency, inspection, adaptation. Not planning.")),
                    Beat(12, light = setOf("transp"), card = BoardCard("Transparency", "The work and how it's done, visible to doers and receivers.")),
                ),
            ),
            // 13: "And what do we keep so visible?"
            Scene(
                13, "Three artifacts", artifacts(),
                beats = listOf(
                    Beat(14, show = artAll, light = artAll, pulse = "pb" to "sb", card = BoardCard("Three artifacts", "Product Backlog, Sprint Backlog, Increment.")),
                ),
            ),
            // 15: "My turn to be inspected. Ask me one."
            Scene(
                15, "Quiz: the lying board", pillars(),
                beats = listOf(
                    Beat(16, light = setOf("transp", "insp"), card = BoardCard("Quiz: all done, half untested", "What does the Scrum Guide call that inspection?")),
                    Beat(17, pulse = "transp" to "insp", card = BoardCard("Misleading and wasteful", "Inspection without transparency.")),
                ),
            ),
            // 19: "So how does a team stop the board lying?"
            Scene(
                19, "Done means done", artifacts(),
                beats = listOf(
                    Beat(20, show = artAll + "dod", light = setOf("dod", "inc"), pulse = "dod" to "inc", card = BoardCard("Definition of Done", "A shared quality bar, so done means the same to everyone.")),
                ),
            ),
            // 21: "I never say done until it really is, darling. Now inspection itself?"
            Scene(
                21, "Inspect, then adapt", pillars(),
                beats = listOf(
                    Beat(22, light = setOf("insp"), card = BoardCard("Inspection", "Often and carefully. Quiz: and without adaptation?")),
                    Beat(24, light = setOf("adapt"), pulse = "insp" to "adapt", card = BoardCard("No kiss: it's pointless", "Inspection without adaptation is pointless. Adapt soon.")),
                    Beat(26, card = BoardCard("Adapting the app", "Nobody opens the sharing screen: the Product Backlog changes.")),
                    Beat(28, card = BoardCard("Not empowered", "Every change needs a signature: you see it, and can't move.")),
                ),
            ),
            // 29: "Then here's one for you. Scrum rests on empiricism and on what else?"
            Scene(
                29, "Quiz: lean thinking", lean(),
                beats = listOf(
                    Beat(30, show = setOf("waste", "f1", "f2", "ess", "inc"), light = setOf("waste", "ess"), pulse = "waste" to "f1", card = BoardCard("Lean thinking", "Reduce waste and focus on the essentials.")),
                ),
            ),
            // 32: "Careful. Last one from me..."
            Scene(
                32, "Quiz: a methodology?", framework(),
                beats = listOf(
                    Beat(32, show = frameAll, light = frameAll, card = BoardCard("Quiz: a complete methodology?", "True or false?")),
                    Beat(33, show = setOf("yours"), light = setOf("yours"), pulse = "art" to "yours", card = BoardCard("False: a framework", "Lightweight, deliberately incomplete; only parts is not Scrum.")),
                ),
            ),
            // 35: "And where do we actually inspect and adapt?"
            Scene(
                35, "Four events", events(),
                beats = listOf(
                    Beat(36, light = setOf("plan", "daily", "review", "retro"), card = BoardCard("Inside the Sprint", "Planning, Daily Scrum, Review, Retrospective.")),
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
